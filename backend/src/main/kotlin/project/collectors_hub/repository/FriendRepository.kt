package project.collectors_hub.repository

import org.springframework.data.jpa.repository.JpaRepository
import project.collectors_hub.entity.Friend
import project.collectors_hub.entity.User

interface FriendRepository : JpaRepository<Friend, Long> {
    fun existsByUserAndFriend(user: User, friend: User): Boolean
}