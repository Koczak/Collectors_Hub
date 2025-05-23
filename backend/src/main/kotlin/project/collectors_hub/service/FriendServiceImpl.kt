package project.collectors_hub.service

import jakarta.persistence.EntityNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import project.collectors_hub.entity.Friend
import project.collectors_hub.projection.CollectionProjection
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
            status = Friend.STATUS_PENDING
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

        if (friendRequest.status != "PENDING") {
            throw IllegalStateException("Friend request is not pending")
        }

        friendRequest.status = Friend.STATUS_ACCEPTED
        friendRepository.save(friendRequest)
        return true
    }

    @Transactional
    override fun rejectFriendRequest(invitationId: Long): Boolean {
        val friendRequest = friendRepository.findById(invitationId).orElseThrow { EntityNotFoundException("Friend $invitationId not found") }

        if (friendRequest.status != "PENDING") {
            throw IllegalStateException("Friend request is not pending")
        }

        friendRequest.status = Friend.STATUS_REJECTED
        friendRepository.save(friendRequest)
        return true
    }

    override fun getAllFriendsForCurrentUser(): List<UserFriendProjection> {
        val user = userService.getCurrentUser().orElseThrow { EntityNotFoundException("Current user not found") }
        return friendRepository.getAllFriendsForGivenUserId(user.id)
    }

    override fun getAllCollectionsForFriend(friendUsername: String): List<CollectionProjection> {
        val user = userService.getCurrentUser().orElseThrow { EntityNotFoundException("Current user not found") }
//        if (!user.friends.map { it.friend.username }.contains(friendUsername)) {
//            throw IllegalArgumentException("User $friendUsername is not current user's friend")
//        }
        val friend = user.friends.firstOrNull { it.friend.username == friendUsername }
            ?: throw EntityNotFoundException("Friend with username $friendUsername not found")
        if (friend.status != Friend.STATUS_ACCEPTED) {
            throw IllegalArgumentException("User $friendUsername is not current user's friend")
        }
        return collectionService.getAllCollectionsForGivenUsername(friendUsername)
    }

}