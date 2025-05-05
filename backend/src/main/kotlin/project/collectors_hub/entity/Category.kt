package project.collectors_hub.entity

import jakarta.persistence.*
import project.collectors_hub.helper.SetStringJsonAttributeConverter

@Entity
@Table(name = "categories")
data class Category(
    @Column(nullable = false)
    var name: String,

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    val user: User,

    @Column(nullable = true)
    @Convert(converter = SetStringJsonAttributeConverter::class)
    var attributes: Set<String>?,

    @OneToMany(mappedBy = "category")
    val items: List<Item>

) : BaseEntity() {
    @PreRemove
    fun removeCategoryFromItems() {
        for (item in items) {
            item.category = null
        }
    }
}
