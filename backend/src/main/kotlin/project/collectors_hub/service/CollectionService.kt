package project.collectors_hub.service

import project.collectors_hub.dto.CollectionDto
import project.collectors_hub.projection.CollectionProjection
import project.collectors_hub.projection.ItemProjection
import project.collectors_hub.entity.Collection

interface CollectionService {

    fun getAllCollectionsForCurrentUser(): List<CollectionProjection>
    fun getAllCollectionsForGivenUsername(username: String): List<CollectionProjection>
//    fun createCollection(collectionForm: CollectionForm): Long
    fun createCollection(dto: CollectionDto): Long
    fun findCollectionById(id: Long): Collection?
    fun getItemsFromCollection(collectionId: Long): List<ItemProjection>
}