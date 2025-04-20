package project.collectors_hub.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import project.collectors_hub.dto.GetAllCollectionsProjection
import project.collectors_hub.entity.Collection

interface CollectionRepository : JpaRepository<Collection, Long> {

    @Query("select name, description from Collections where user_id = :userId", nativeQuery = true)
    fun getAllCollectionsForGivenUserId(userId: Long): List<GetAllCollectionsProjection>
}