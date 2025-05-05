package project.collectors_hub.dto

data class CategoryDto(
    val name: String,
    val attributes: Set<String>? = null
)
