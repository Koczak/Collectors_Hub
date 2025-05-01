package project.collectors_hub.service

import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import project.collectors_hub.dto.CategoryDto
import project.collectors_hub.projection.CategoryProjection
import project.collectors_hub.entity.Category
import project.collectors_hub.repository.CategoryRepository
import project.collectors_hub.security.SecurityUtils
import java.util.*

@Service
class CategoryServiceImpl(
    private val categoryRepository: CategoryRepository,
    private val userService: UserService
) : CategoryService {
    override fun getAllCategoriesForCurrentUser(): List<CategoryProjection> {
        val username = SecurityUtils.getCurrentUsername() ?: throw UsernameNotFoundException("User not authenticated")
        val user = userService.getUserByUsername(username).orElseThrow { IllegalArgumentException("User $username not found") }
        return categoryRepository.getAllCategoryProjectionsForGivenUserId(user.id)
    }

    override fun createCategory(dto: CategoryDto): Long {
        val username = SecurityUtils.getCurrentUsername() ?: throw UsernameNotFoundException("User not authenticated")
        val user = userService.getUserByUsername(username).orElseThrow { IllegalArgumentException("User $username not found") }
        val category = Category(
            name = dto.name,
            user = user,
            attributes = dto.attributes,
            items = emptyList()
        )
        categoryRepository.save(category)
        return category.id
    }

    override fun getCategoryById(id: Long): Optional<Category> {
        return categoryRepository.findById(id)
    }

    override fun deleteCategoryById(id: Long): Boolean {
        if (!categoryRepository.existsById(id)) {
            throw IllegalArgumentException("Category with id $id not found")
        }
        categoryRepository.deleteById(id)
        return true
    }

    override fun editCategory(id: Long, dto: CategoryDto): Boolean {
        val category = categoryRepository.findById(id).orElseThrow { IllegalArgumentException("Category with id $id not found") }
        category.name = dto.name
        category.attributes = dto.attributes
        categoryRepository.save(category)
        return true
    }
}