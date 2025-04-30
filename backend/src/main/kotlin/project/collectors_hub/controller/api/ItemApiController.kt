package project.collectors_hub.controller.api

import org.springframework.web.bind.annotation.*
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

    @DeleteMapping("/{id}")
    fun deleteItemById(@PathVariable id: Long): Boolean {
        return itemService.deleteItem(id)
    }

    @PutMapping("/{id}")
    fun editItem(@PathVariable id: Long, @RequestBody dto: ItemDto): Boolean {
        return itemService.editItem(id, dto)
    }


}