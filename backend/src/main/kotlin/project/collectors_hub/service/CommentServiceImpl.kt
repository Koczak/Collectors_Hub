package project.collectors_hub.service

import jakarta.persistence.EntityNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import project.collectors_hub.dto.CommentDto
import project.collectors_hub.entity.Comment
import project.collectors_hub.entity.Friend
import project.collectors_hub.exception.PermissionDeniedException
import project.collectors_hub.projection.CommentProjection
import project.collectors_hub.repository.CommentRepository
import project.collectors_hub.repository.FriendRepository
import project.collectors_hub.repository.ItemRepository

@Service
class CommentServiceImpl(
    private val commentRepository: CommentRepository,
    private val itemRepository: ItemRepository,
    private val friendRepository: FriendRepository,
    private val userService: UserService
) : CommentService {

    override fun getAllCommentsForItem(itemId: Long): List<CommentProjection> {
        return commentRepository.findAllCommentsByItemId(itemId)
    }

    override fun getAllCommentsForCollection(collectionId: Long): List<CommentProjection> {
        return commentRepository.findAllCommentsByCollectionId(collectionId)
    }

    @Transactional
    override fun addComment(dto: CommentDto): Long {
        val currentUser = userService.getCurrentUser()
            .orElseThrow { EntityNotFoundException("Current user not found") }

        val item = itemRepository.findById(dto.itemId)
            .orElseThrow { EntityNotFoundException("Item with id ${dto.itemId} not found") }

        // Sprawdzenie czy przedmiot należy do zalogowanego użytkownika lub jego znajomego
        if (item.collection.user.id != currentUser.id) {
            // Jeśli przedmiot nie należy do zalogowanego użytkownika, sprawdzamy czy jest znajomym
            val friendship = friendRepository.findFriendshipBetweenUsers(
                currentUser.id, item.collection.user.id
            )
            
            if (friendship == null || friendship.status != Friend.FriendStatus.ACCEPTED) {
                throw PermissionDeniedException("Cannot comment on an item that does not belong to you or your friend")
            }
        }

        val comment = Comment(
            content = dto.content,
            user = currentUser,
            item = item
        )

        val savedComment = commentRepository.save(comment)
        return savedComment.id
    }

    @Transactional
    override fun deleteComment(commentId: Long): Boolean {
        val currentUser = userService.getCurrentUser()
            .orElseThrow { EntityNotFoundException("Current user not found") }

        val comment = commentRepository.findById(commentId)
            .orElseThrow { EntityNotFoundException("Comment with id $commentId not found") }

        // Sprawdzenie czy komentarz należy do zalogowanego użytkownika
        if (comment.user.id != currentUser.id) {
            throw PermissionDeniedException("Cannot delete comment that does not belong to current user")
        }

        commentRepository.delete(comment)
        return true
    }
} 