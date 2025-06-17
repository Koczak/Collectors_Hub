package project.collectors_hub.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import project.collectors_hub.entity.Comment
import project.collectors_hub.projection.CommentProjection

interface CommentRepository : JpaRepository<Comment, Long> {
    
    @Query("""
        SELECT 
            c.id AS id,
            c.content AS content,
            c.createdAt AS createdAt,
            u.username AS username,
            u.id AS userId
        FROM Comment c
        JOIN c.user u
        WHERE c.item.id = :itemId
        ORDER BY c.createdAt DESC
    """)
    fun findAllCommentsByItemId(itemId: Long): List<CommentProjection>
    
    @Query("""
        SELECT 
            c.id AS id,
            c.content AS content,
            c.createdAt AS createdAt,
            u.username AS username,
            u.id AS userId
        FROM Comment c
        JOIN c.user u
        JOIN c.item i
        JOIN i.collection col
        WHERE col.id = :collectionId
        ORDER BY c.createdAt DESC
    """)
    fun findAllCommentsByCollectionId(collectionId: Long): List<CommentProjection>
} 