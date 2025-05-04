package project.collectors_hub.controller.api

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
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
    fun getAllItemsForCurrentUser(): ResponseEntity<List<ItemProjection>> {
        return ResponseEntity(itemService.getAllItemsForCurrentUser(), HttpStatus.OK)
    }

    @PostMapping
    fun addNewItem(@RequestBody dto: ItemDto): ResponseEntity<Long> {
        return ResponseEntity(itemService.addNewItem(dto), HttpStatus.CREATED)
    }

    @DeleteMapping("/{id}")
    fun deleteItemById(@PathVariable id: Long): ResponseEntity<Unit> {
        itemService.deleteItem(id)
        return ResponseEntity(HttpStatus.NO_CONTENT)
    }

    @PutMapping("/{id}")
    fun editItem(@PathVariable id: Long, @RequestBody dto: ItemDto): ResponseEntity<Unit> {
        itemService.editItem(id, dto)
        return ResponseEntity(HttpStatus.NO_CONTENT)
    }


}