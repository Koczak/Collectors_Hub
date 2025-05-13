package project.collectors_hub.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import project.collectors_hub.projection.CollectionProjection
import project.collectors_hub.entity.Collection

interface CollectionRepository : JpaRepository<Collection, Long> {

    @Query("select id, name, description from Collections where user_id = :userId", nativeQuery = true)
    fun getAllCollectionsForGivenUserId(userId: Long): List<CollectionProjection>

    @Query("""
    SELECT c.id, c.name, c.description 
    FROM Collections c 
    JOIN users u ON c.user_id = u.id 
    WHERE u.username = :username
""", nativeQuery = true)
    fun getAllCollectionsForGivenUsername(username: String): List<CollectionProjection>
}