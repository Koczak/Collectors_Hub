package project.collectors_hub.controller.api

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import project.collectors_hub.dto.CollectionProjection
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

}