package project.collectors_hub.dto

data class ItemDto(
    val name: String = "",
    val description: String = "",
    val categoryId: Long? = -1,
    val collectionId: Long = 0,
    val attributes: Map<String, Any>? = null
)
