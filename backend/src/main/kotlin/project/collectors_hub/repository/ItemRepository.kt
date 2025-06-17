package project.collectors_hub.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import project.collectors_hub.projection.ItemProjection
import project.collectors_hub.entity.Item

interface ItemRepository : JpaRepository<Item, Long> {
    @Query("""
        SELECT 
               i.id AS id,
               i.name AS name, 
               i.description AS description, 
               c.name AS categoryName,
               i.attributes AS attributes,
               i.collection.id AS collectionId
        FROM Item i
        LEFT JOIN i.category c
        WHERE i.collection.id = :collectionId
    """)
    fun findAllItemsWithCategoryForGivenCollectionId(collectionId: Long): List<ItemProjection>

    @Query("""
        SELECT 
               i.id AS id,
               i.name AS name, 
               i.description AS description, 
               c.name AS categoryName,
               i.attributes AS attributes,
               i.collection.id AS collectionId
        FROM Item i
        LEFT JOIN i.category c
        WHERE i.id = :id
    """)
    fun findItemWithCategoryForGivenId(id: Long): ItemProjection?
}