package project.collectors_hub.controller.api

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
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
}