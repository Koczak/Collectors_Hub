package project.collectors_hub.controller.api

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
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
    fun getCollectionsForCurrentUser(): ResponseEntity<List<CollectionProjection>> {
        return ResponseEntity(collectionService.getAllCollectionsForCurrentUser(), HttpStatus.OK)
    }

    @PostMapping
    fun createCollection(@RequestBody dto: CollectionDto): ResponseEntity<Long> {
        return ResponseEntity(collectionService.createCollection(dto), HttpStatus.CREATED)
    }

}