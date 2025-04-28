package project.collectors_hub.service

import project.collectors_hub.dto.CollectionForm
import project.collectors_hub.dto.CollectionProjection
import project.collectors_hub.entity.Collection

interface CollectionService {

    fun getAllCollectionsForCurrentUser(): List<CollectionProjection>
    fun createCollection(collectionForm: CollectionForm): Long
    fun findCollectionById(id: Long): Collection?
}