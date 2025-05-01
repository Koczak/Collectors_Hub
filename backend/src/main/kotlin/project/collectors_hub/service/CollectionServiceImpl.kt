package project.collectors_hub.service

import jakarta.persistence.EntityNotFoundException
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import project.collectors_hub.dto.CollectionDto
import project.collectors_hub.projection.CollectionProjection
import project.collectors_hub.entity.Collection
import project.collectors_hub.repository.CollectionRepository
import project.collectors_hub.security.SecurityUtils

@Service
class CollectionServiceImpl(
    private val collectionRepository: CollectionRepository,
    private val userService: UserService
) : CollectionService {

    override fun getAllCollectionsForCurrentUser(): List<CollectionProjection> {
        val username = SecurityUtils.getCurrentUsername() ?: throw UsernameNotFoundException("User not authenticated")
        val user = userService.getUserByUsername(username).orElseThrow { EntityNotFoundException("User $username not found") }
        return collectionRepository.getAllCollectionsForGivenUserId(user.id)
    }

    override fun createCollection(dto: CollectionDto): Long {
        val username = SecurityUtils.getCurrentUsername() ?: throw UsernameNotFoundException("User not authenticated")
        val user = userService.getUserByUsername(username).orElseThrow { EntityNotFoundException("User $username not found") }
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

}