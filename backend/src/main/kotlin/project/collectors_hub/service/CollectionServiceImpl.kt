package project.collectors_hub.service

import org.springframework.stereotype.Service
import project.collectors_hub.dto.CollectionForm
import project.collectors_hub.dto.GetAllCollectionsProjection
import project.collectors_hub.entity.Collection
import project.collectors_hub.repository.CollectionRepository
import project.collectors_hub.repository.UserRepository
import project.collectors_hub.security.SecurityUtils

@Service
class CollectionServiceImpl(
    private val collectionRepository: CollectionRepository,
    private val userRepository: UserRepository
) : CollectionService {

    override fun getAllCollectionsForCurrentUser(): List<GetAllCollectionsProjection> {
        val username = SecurityUtils.getCurrentUsername()!!
        val user = userRepository.findByUsername(username)!!
        return collectionRepository.getAllCollectionsForGivenUserId(user.id)
    }

    override fun createCollection(collectionForm: CollectionForm): Long {
        val username = SecurityUtils.getCurrentUsername()!!
        val user = userRepository.findByUsername(username)!!
        val collection = Collection(
            name = collectionForm.name,
            description = collectionForm.description,
            user = user,
            items = emptyList()
        )
        collectionRepository.save(collection)
        return collection.id
    }

}