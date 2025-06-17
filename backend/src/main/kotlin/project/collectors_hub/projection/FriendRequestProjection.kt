package project.collectors_hub.projection

interface FriendRequestProjection {
    fun getId(): Long
    fun getSenderUsername(): String
    fun getSenderEmail(): String
} 