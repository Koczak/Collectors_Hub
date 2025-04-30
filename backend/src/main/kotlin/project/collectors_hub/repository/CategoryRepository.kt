package project.collectors_hub.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import project.collectors_hub.projection.CategoryProjection
import project.collectors_hub.entity.Category

interface CategoryRepository : JpaRepository<Category, Long> {
    @Query("SELECT c.id AS id, c.name AS name, u.username AS username, c.attributes AS attributes " +
            "FROM Category c JOIN c.user u WHERE u.id = :userId")
    fun getAllCategoryProjectionsForGivenUserId(userId: Long): List<CategoryProjection>

//    @Query("SELECT c.id AS id, c.name AS name, u.username AS username, c.attributes AS attributes " +
//            "FROM Category c JOIN c.user u WHERE c.id = :id")
//    fun getCategoryProjectionsForGivenId(id: Long): CategoryProjection?
}