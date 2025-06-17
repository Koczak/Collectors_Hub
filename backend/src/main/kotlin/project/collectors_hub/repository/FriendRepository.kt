package project.collectors_hub.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import project.collectors_hub.entity.Friend
import project.collectors_hub.entity.User
import project.collectors_hub.projection.FriendRequestProjection
import project.collectors_hub.projection.UserFriendProjection

interface FriendRepository : JpaRepository<Friend, Long> {
    fun existsByUserAndFriend(user: User, friend: User): Boolean
    
    @Query("""
    SELECT
        f.username AS friendUsername, 
        f.email AS friendEmail
    FROM Friend fr
    JOIN fr.friend f
    WHERE fr.user.id = :userId AND fr.status = 'ACCEPTED'
    UNION
    SELECT
        u.username AS friendUsername,
        u.email AS friendEmail
    FROM Friend fr
    JOIN fr.user u
    WHERE fr.friend.id = :userId AND fr.status = 'ACCEPTED'
    """)
    fun getAllFriendsForGivenUserId(userId: Long): List<UserFriendProjection>
    
    @Query("""
    SELECT
        fr.id AS id,
        u.username AS senderUsername,
        u.email AS senderEmail
    FROM Friend fr
    JOIN fr.user u
    WHERE fr.friend.id = :userId AND fr.status = 'PENDING'
    """)
    fun getPendingFriendRequestsForUser(userId: Long): List<FriendRequestProjection>
    
    @Query("SELECT fr FROM Friend fr WHERE (fr.user.id = :userId AND fr.friend.id = :friendId) OR (fr.user.id = :friendId AND fr.friend.id = :userId)")
    fun findFriendshipBetweenUsers(userId: Long, friendId: Long): Friend?
}