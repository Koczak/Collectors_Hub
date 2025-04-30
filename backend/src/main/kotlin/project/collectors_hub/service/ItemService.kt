package project.collectors_hub.service

import project.collectors_hub.dto.ItemDto
import project.collectors_hub.projection.ItemProjection

interface ItemService {
    fun getAllItemsForCurrentUser(): List<ItemProjection>

    fun addNewItem(dto: ItemDto): Long

    fun deleteItem(id: Long): Boolean

    fun editItem(id: Long, dto: ItemDto): Boolean
}