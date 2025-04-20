package project.collectors_hub.service

import project.collectors_hub.dto.CollectionForm
import project.collectors_hub.dto.GetAllCollectionsProjection

interface CollectionService {

    fun getAllCollectionsForCurrentUser(): List<GetAllCollectionsProjection>
    fun createCollection(collectionForm: CollectionForm): Long
}