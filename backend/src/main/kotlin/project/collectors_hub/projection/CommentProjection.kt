package project.collectors_hub.projection

import java.time.LocalDateTime

interface CommentProjection {
    fun getId(): Long
    fun getContent(): String
    fun getCreatedAt(): LocalDateTime
    fun getUsername(): String
    fun getUserId(): Long
} 