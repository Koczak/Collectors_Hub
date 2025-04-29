package project.collectors_hub.dto

data class CategoryDto(
    val name: String,
    val attributes: Map<String, Any>? = null
)
