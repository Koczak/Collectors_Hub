package project.collectors_hub.service

import project.collectors_hub.projection.CollectionProjection
import project.collectors_hub.projection.UserFriendProjection

interface FriendService {
    fun sendFriendRequest(receiverUsername: String): Boolean
    fun confirmFriendRequest(invitationId: Long): Boolean
    fun rejectFriendRequest(invitationId: Long): Boolean
    fun getAllFriendsForCurrentUser(): List<UserFriendProjection>
    fun getAllCollectionsForFriend(friendUsername: String): List<CollectionProjection>
}