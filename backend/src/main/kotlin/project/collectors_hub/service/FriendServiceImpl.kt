package project.collectors_hub.service

import jakarta.persistence.EntityNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import project.collectors_hub.entity.Friend
import project.collectors_hub.projection.CollectionProjection
import project.collectors_hub.projection.FriendRequestProjection
import project.collectors_hub.projection.ItemProjection
import project.collectors_hub.projection.UserFriendProjection
import project.collectors_hub.repository.FriendRepository

@Service
class FriendServiceImpl(
    private val friendRepository: FriendRepository,
    private val userService: UserService,
    private val emailService: EmailService,
    private val collectionService: CollectionService
) : FriendService {

    @Transactional
    override fun sendFriendRequest(receiverUsername: String): Boolean {
        val sender = userService.getCurrentUser()
            .orElseThrow { EntityNotFoundException("Current user not found") }

        val receiver = userService.getUserByUsername(receiverUsername)
            .orElseThrow { EntityNotFoundException("User with $receiverUsername username not found") }

        if (friendRepository.existsByUserAndFriend(sender, receiver)) {
            throw IllegalArgumentException("Friend request already sent")
        }

        val friendRequest = Friend(
            user = sender,
            friend = receiver,
            status = Friend.FriendStatus.PENDING
        )
        val savedFriendRequest = friendRepository.save(friendRequest)
        val confirmationLink = "http://localhost:8080/api/friends/confirm/${savedFriendRequest.id}"
        emailService.sendEmail(receiver.email, "Friend Request", "User ${sender.username} has sent you a friend request. Confirm it here: $confirmationLink")
        return true
    }

    @Transactional
    override fun confirmFriendRequest(invitationId: Long): Boolean {
        val friendRequest = friendRepository.findById(invitationId)
            .orElseThrow { EntityNotFoundException("Friend $invitationId not found") }

        if (friendRequest.status != Friend.FriendStatus.PENDING) {
            throw IllegalStateException("Friend request is not pending")
        }

        friendRequest.status = Friend.FriendStatus.ACCEPTED
        friendRepository.save(friendRequest)
        return true
    }

    @Transactional
    override fun rejectFriendRequest(invitationId: Long): Boolean {
        val friendRequest = friendRepository.findById(invitationId).orElseThrow { EntityNotFoundException("Friend $invitationId not found") }

        if (friendRequest.status != Friend.FriendStatus.PENDING) {
            throw IllegalStateException("Friend request is not pending")
        }

        friendRequest.status = Friend.FriendStatus.REJECTED
        friendRepository.save(friendRequest)
        return true
    }

    override fun getAllFriendsForCurrentUser(): List<UserFriendProjection> {
        val user = userService.getCurrentUser().orElseThrow { EntityNotFoundException("Current user not found") }
        return friendRepository.getAllFriendsForGivenUserId(user.id)
    }

    override fun getPendingFriendRequestsForCurrentUser(): List<FriendRequestProjection> {
        val user = userService.getCurrentUser().orElseThrow { EntityNotFoundException("Current user not found") }
        return friendRepository.getPendingFriendRequestsForUser(user.id)
    }

    override fun getAllCollectionsForFriend(friendUsername: String): List<CollectionProjection> {
        val currentUser = userService.getCurrentUser().orElseThrow { EntityNotFoundException("Current user not found") }
        
        val friendUser = userService.getUserByUsername(friendUsername)
            .orElseThrow { EntityNotFoundException("User with username $friendUsername not found") }
            
        // Sprawdzamy relację przyjaźni w obu kierunkach
        val friendship = friendRepository.findFriendshipBetweenUsers(currentUser.id, friendUser.id)
            ?: throw EntityNotFoundException("Friendship with user $friendUsername not found")
            
        if (friendship.status != Friend.FriendStatus.ACCEPTED) {
            throw IllegalArgumentException("User $friendUsername is not current user's friend")
        }
        
        return collectionService.getAllCollectionsForGivenUsername(friendUsername)
    }

    @Transactional
    override fun removeFriend(friendUsername: String): Boolean {
        val currentUser = userService.getCurrentUser()
            .orElseThrow { EntityNotFoundException("Current user not found") }

        val friendUser = userService.getUserByUsername(friendUsername)
            .orElseThrow { EntityNotFoundException("User with username $friendUsername not found") }

        // Find the friendship record in either direction
        val friendship = friendRepository.findFriendshipBetweenUsers(currentUser.id, friendUser.id)
            ?: throw EntityNotFoundException("Friendship with user $friendUsername not found")
        
        // Check if the friendship is actually accepted
        if (friendship.status != Friend.FriendStatus.ACCEPTED) {
            throw IllegalStateException("Cannot remove non-accepted friendship")
        }
        
        // Delete the friendship
        friendRepository.delete(friendship)
        return true
    }

    override fun getItemsFromFriendCollection(friendUsername: String, collectionId: Long): List<ItemProjection> {
        val currentUser = userService.getCurrentUser().orElseThrow { EntityNotFoundException("Current user not found") }
        
        val friendUser = userService.getUserByUsername(friendUsername)
            .orElseThrow { EntityNotFoundException("User with username $friendUsername not found") }
            
        // Sprawdzamy relację przyjaźni w obu kierunkach
        val friendship = friendRepository.findFriendshipBetweenUsers(currentUser.id, friendUser.id)
            ?: throw EntityNotFoundException("Friendship with user $friendUsername not found")
            
        if (friendship.status != Friend.FriendStatus.ACCEPTED) {
            throw IllegalArgumentException("User $friendUsername is not current user's friend")
        }
        
        // Sprawdzamy, czy kolekcja należy do znajomego
        val collection = collectionService.findCollectionById(collectionId)
            ?: throw EntityNotFoundException("Collection with id $collectionId not found")
            
        if (collection.user.username != friendUsername) {
            throw IllegalArgumentException("Collection $collectionId does not belong to user $friendUsername")
        }
        
        // Pobieramy przedmioty z kolekcji znajomego
        return collectionService.getItemsFromCollection(collectionId)
    }

}