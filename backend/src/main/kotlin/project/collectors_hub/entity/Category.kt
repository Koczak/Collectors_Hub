package project.collectors_hub.entity

import jakarta.persistence.*
import project.collectors_hub.helper.JsonAttributeConverter

@Entity
@Table(name = "categories")
data class Category(
    @Column(nullable = false)
    val name: String,

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    val user: User,

    @Column(nullable = true)
    @Convert(converter = JsonAttributeConverter::class)
    val attributes: Map<String, Any>?,

    @OneToMany(mappedBy = "category")
    val items: List<Item>

) : BaseEntity()
