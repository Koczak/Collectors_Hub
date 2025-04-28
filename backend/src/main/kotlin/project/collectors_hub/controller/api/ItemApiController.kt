package project.collectors_hub.controller.api

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import project.collectors_hub.dto.ItemDto
import project.collectors_hub.dto.ItemProjection
import project.collectors_hub.service.ItemService

@RestController
@RequestMapping(value = ["/api/items"])
class ItemApiController(
    private val itemService: ItemService
) {

    @GetMapping
    fun getAllItemsForCurrentUser(): List<ItemProjection> {
        // This is a placeholder. Replace with actual logic to fetch items for the current user.
        return itemService.getAllItemsForCurrentUser()
    }

    @PostMapping
    fun addNewItem(@RequestBody itemDto: ItemDto): Long {
        // This is a placeholder. Replace with actual logic to add a new item.
        return itemService.addNewItem(itemDto)
    }

}