package project.collectors_hub.controller.api

import org.springframework.web.bind.annotation.*
import project.collectors_hub.dto.CollectionDto
import project.collectors_hub.projection.CollectionProjection
import project.collectors_hub.service.CollectionService


@RestController
@RequestMapping(value = ["/api/collections"])
class CollectionApiController(
    private val collectionService: CollectionService
) {

    @GetMapping
    fun getCollectionsForCurrentUser(): List<CollectionProjection> {
        return collectionService.getAllCollectionsForCurrentUser()
    }

    @PostMapping
    fun createCollection(@RequestBody dto: CollectionDto): Long {
        return collectionService.createCollection(dto)
    }

}