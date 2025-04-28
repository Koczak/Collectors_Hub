package project.collectors_hub.entity

import jakarta.persistence.*
import project.collectors_hub.helper.JsonAttributeConverter

@Entity
@Table(name = "items")
data class Item(

    @Column(nullable = false)
    val name: String,

    @Column(nullable = true)
    val description: String,

    @ManyToOne
    @JoinColumn(name = "collection_id", nullable = false)
    val collection: Collection,

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = true)
    val category: Category?,

    @Column(nullable = true)
    @Convert(converter = JsonAttributeConverter::class)
    val attributes: Map<String, Any>?,

) : BaseEntity()
