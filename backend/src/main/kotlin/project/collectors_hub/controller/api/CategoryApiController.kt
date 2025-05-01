package project.collectors_hub.controller.api

import org.springframework.web.bind.annotation.*
import project.collectors_hub.dto.CategoryDto
import project.collectors_hub.projection.CategoryProjection
import project.collectors_hub.service.CategoryService

@RestController
@RequestMapping("/api/categories")
class CategoryApiController(
    private val categoryService: CategoryService
) {

    @GetMapping
    fun getCategoriesForCurrentUser(): List<CategoryProjection> {
        return categoryService.getAllCategoriesForCurrentUser()
    }

    @PostMapping
    fun createCategory(@RequestBody dto: CategoryDto): Long {
        return categoryService.createCategory(dto)
    }

    @DeleteMapping("/{id}")
    fun deleteCategory(@PathVariable id: Long): Boolean {
        return categoryService.deleteCategoryById(id)
    }

    @PutMapping("/{id}")
    fun editCategory(@PathVariable id: Long, @RequestBody dto: CategoryDto): Boolean {
        return categoryService.editCategory(id, dto)
    }
}