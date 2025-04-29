package project.collectors_hub.service

import project.collectors_hub.dto.CategoryDto
import project.collectors_hub.projection.CategoryProjection

interface CategoryService {
    fun getAllCategoriesForCurrentUser(): List<CategoryProjection>
    fun createCategory(dto: CategoryDto): Long
}