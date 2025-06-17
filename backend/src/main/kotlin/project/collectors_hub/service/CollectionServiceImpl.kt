package project.collectors_hub.service

import jakarta.persistence.EntityNotFoundException
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import project.collectors_hub.dto.CollectionDto
import project.collectors_hub.projection.CollectionProjection
import project.collectors_hub.entity.Collection
import project.collectors_hub.repository.CollectionRepository
import project.collectors_hub.security.SecurityUtils
import project.collectors_hub.projection.ItemProjection
import project.collectors_hub.repository.ItemRepository

@Service
class CollectionServiceImpl(
    private val collectionRepository: CollectionRepository,
    private val userService: UserService,
    private val itemRepository: ItemRepository
) : CollectionService {

    override fun getAllCollectionsForCurrentUser(): List<CollectionProjection> {
        val user = userService.getCurrentUser().orElseThrow { EntityNotFoundException("Current user not found") }
        return collectionRepository.getAllCollectionsForGivenUserId(user.id)
    }

    override fun getAllCollectionsForGivenUsername(username: String): List<CollectionProjection> {
        if (!userService.existsByUsername(username)) {
            throw UsernameNotFoundException("User with username $username not found")
        }
        return collectionRepository.getAllCollectionsForGivenUsername(username)
    }

    override fun createCollection(dto: CollectionDto): Long {
        val user = userService.getCurrentUser().orElseThrow { EntityNotFoundException("Current user not found") }
        val collection = Collection(
            name = dto.name,
            description = dto.description,
            user = user,
            items = emptyList()
        )
        collectionRepository.save(collection)
        return collection.id
    }

//    override fun createCollection(collectionForm: CollectionForm): Long {
//        val username = SecurityUtils.getCurrentUsername()!!
//        val user = userService.getUserByUsername(username)!!
//        val collection = Collection(
//            name = collectionForm.name,
//            description = collectionForm.description,
//            user = user,
//            items = emptyList()
//        )
//        collectionRepository.save(collection)
//        return collection.id
//    }

    override fun findCollectionById(id: Long): Collection? {
        return collectionRepository.findById(id).orElse(null)
    }

    override fun getItemsFromCollection(collectionId: Long): List<ItemProjection> {
        val collection = findCollectionById(collectionId) ?: throw EntityNotFoundException("Collection with id $collectionId not found")
        
        // Weryfikacja uprawnień jest obsługiwana przez metody wywołujące tę funkcję
        return itemRepository.findAllItemsWithCategoryForGivenCollectionId(collectionId)
    }

}