package project.collectors_hub.controller.api

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import project.collectors_hub.dto.ItemDto
import project.collectors_hub.projection.ItemProjection
import project.collectors_hub.service.ItemService

@RestController
@RequestMapping(value = ["/api/items"])
class ItemApiController(
    private val itemService: ItemService
) {

    @GetMapping
    fun getAllItemsForCurrentUser(): List<ItemProjection> {
        return itemService.getAllItemsForCurrentUser()
    }

    @PostMapping
    fun addNewItem(@RequestBody dto: ItemDto): Long {
        return itemService.addNewItem(dto)
    }

}