package project.collectors_hub.service

import project.collectors_hub.dto.CommentDto
import project.collectors_hub.projection.CommentProjection

interface CommentService {
    fun getAllCommentsForItem(itemId: Long): List<CommentProjection>
    fun getAllCommentsForCollection(collectionId: Long): List<CommentProjection>
    fun addComment(dto: CommentDto): Long
    fun deleteComment(commentId: Long): Boolean
} 