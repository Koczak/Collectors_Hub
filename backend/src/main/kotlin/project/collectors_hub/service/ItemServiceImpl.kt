package project.collectors_hub.service

import org.springframework.stereotype.Service
import project.collectors_hub.dto.ItemDto
import project.collectors_hub.dto.ItemProjection
import project.collectors_hub.entity.Item
import project.collectors_hub.repository.ItemRepository

@Service
class ItemServiceImpl(
    private val itemRepository: ItemRepository,
    private val collectionService: CollectionService,
) : ItemService {
    override fun getAllItemsForCurrentUser(): List<ItemProjection> {
        val collections = collectionService.getAllCollectionsForCurrentUser()
        val items = mutableListOf<ItemProjection>()
        for (collection in collections) {
            val collectionItems = itemRepository.findAllItemsWithCategoryForGivenCollectionId(collection.getId())
            items.addAll(collectionItems)
        }
        return items
    }

    override fun addNewItem(dto: ItemDto): Long {
        val collection = collectionService.findCollectionById(dto.collectionId)
            ?: throw IllegalArgumentException("Collection with id ${dto.collectionId} not found")

        val item = Item(
            name = dto.name,
            description = dto.description,
            collection = collection,
            category = null,
            attributes = null
        )
        itemRepository.save(item)
        return item.id
    }
}