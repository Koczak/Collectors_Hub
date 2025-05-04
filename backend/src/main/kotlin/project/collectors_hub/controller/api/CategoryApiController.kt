package project.collectors_hub.controller.api

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
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
    fun getCategoriesForCurrentUser(): ResponseEntity<List<CategoryProjection>> {
        return ResponseEntity(categoryService.getAllCategoriesForCurrentUser(), HttpStatus.OK)
    }

    @PostMapping
    fun createCategory(@RequestBody dto: CategoryDto): ResponseEntity<Long> {
        return ResponseEntity(categoryService.createCategory(dto), HttpStatus.CREATED)
    }

    @DeleteMapping("/{id}")
    fun deleteCategory(@PathVariable id: Long): ResponseEntity<Unit> {
        categoryService.deleteCategoryById(id)
        return ResponseEntity(HttpStatus.NO_CONTENT)
    }

    @PutMapping("/{id}")
    fun editCategory(@PathVariable id: Long, @RequestBody dto: CategoryDto): ResponseEntity<Unit> {
        categoryService.editCategory(id, dto)
        return ResponseEntity(HttpStatus.NO_CONTENT)
    }
}