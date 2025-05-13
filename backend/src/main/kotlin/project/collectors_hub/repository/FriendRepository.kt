package project.collectors_hub.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import project.collectors_hub.entity.Friend
import project.collectors_hub.entity.User
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
    """
    )
    fun getAllFriendsForGivenUserId(userId: Long): List<UserFriendProjection>
}