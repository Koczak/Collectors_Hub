package project.collectors_hub.service

import project.collectors_hub.dto.CategoryDto
import project.collectors_hub.entity.Category
import project.collectors_hub.projection.CategoryProjection
import java.util.*

interface CategoryService {
    fun getAllCategoriesForCurrentUser(): List<CategoryProjection>
    fun createCategory(dto: CategoryDto): Long
    fun getCategoryById(id: Long): Optional<Category>
}