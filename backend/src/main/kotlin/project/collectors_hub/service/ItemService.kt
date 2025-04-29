package project.collectors_hub.service

import project.collectors_hub.dto.ItemDto
import project.collectors_hub.projection.ItemProjection

interface ItemService {
    fun getAllItemsForCurrentUser(): List<ItemProjection>

    fun addNewItem(dto: ItemDto): Long
}