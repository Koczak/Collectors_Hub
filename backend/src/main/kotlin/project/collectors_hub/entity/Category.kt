package project.collectors_hub.entity

import jakarta.persistence.*

@Entity
@Table(name = "categories")
data class Category(
    @Column(nullable = false)
    val name: String,

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    val user: User,

    @Lob
    @Column(nullable = true)
    val attributeSchema: String,

    @OneToMany(mappedBy = "category")
    val items: List<Item>

) : BaseEntity()
