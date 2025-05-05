package project.collectors_hub.service

import jakarta.persistence.EntityNotFoundException
import org.springframework.stereotype.Service
import project.collectors_hub.dto.ItemDto
import project.collectors_hub.projection.ItemProjection
import project.collectors_hub.entity.Item
import project.collectors_hub.repository.ItemRepository

@Service
class ItemServiceImpl(
    private val itemRepository: ItemRepository,
    private val collectionService: CollectionService,
    private val categoryService: CategoryService
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
            ?: throw EntityNotFoundException("Collection with id ${dto.collectionId} not found")

        if (dto.categoryId != null && dto.categoryId != -1L) {
            val category = categoryService.getCategoryById(dto.categoryId).orElseThrow { EntityNotFoundException("Category with id ${dto.categoryId} not found") }

            if (category.user.id != collection.user.id) {
                throw EntityNotFoundException("Category with id ${dto.categoryId} does not belong to the user")
            }

            val categoryAttributes = category.attributes
            val itemAttributes = dto.attributes?.keys

            if (categoryAttributes != null && itemAttributes != null) {
                for (attribute in itemAttributes) {
                    if (!categoryAttributes.contains(attribute)) {
                        throw EntityNotFoundException("Attribute $attribute does not exist in the category")
                    }
                }
            } else {
                throw IllegalArgumentException("Category attributes or item attributes are null")
            }

            val item = Item(
                name = dto.name,
                description = dto.description,
                collection = collection,
                category = category,
                attributes = dto.attributes
            )
            itemRepository.save(item)
            return item.id
        } else {
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

    override fun deleteItem(id: Long): Boolean {
        if (!itemRepository.existsById(id)) {
            throw EntityNotFoundException("Item with id $id not found")
        }
        itemRepository.deleteById(id)
        return true
    }

    override fun editItem(id: Long, dto: ItemDto): Boolean {
        val item = itemRepository.findById(id).orElseThrow { EntityNotFoundException("Item with id $id not found") }

        when {
            dto.categoryId == null -> {
                item.category = null
                item.attributes = null
            }
            dto.categoryId != -1L -> {
                val category = categoryService.getCategoryById(dto.categoryId).orElseThrow { EntityNotFoundException("Category with id ${dto.categoryId} not found") }
                item.category = category
                val categoryAttributes = category.attributes
                val itemAttributes = dto.attributes?.keys
                if (categoryAttributes != null && itemAttributes != null) {
                    for (attribute in itemAttributes) {
                        if (!categoryAttributes.contains(attribute)) {
                            throw EntityNotFoundException("Attribute $attribute does not exist in the category")
                        }
                    }
                } else {
                    throw IllegalArgumentException("Category attributes or item attributes are null")
                }
                item.attributes = dto.attributes
            }
        }

        item.name = dto.name
        item.description = dto.description

        itemRepository.save(item)
        return true
    }
}