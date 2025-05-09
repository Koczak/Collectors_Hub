package project.collectors_hub.service

interface FriendService {
    fun sendFriendRequest(receiverUsername: String): Boolean
    fun confirmFriendRequest(invitationId: Long): Boolean
    fun rejectFriendRequest(invitationId: Long): Boolean
}