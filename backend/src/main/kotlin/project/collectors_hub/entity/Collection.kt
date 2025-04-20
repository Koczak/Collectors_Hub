package project.collectors_hub.entity

import jakarta.persistence.*

@Entity
@Table(name = "collections")
data class Collection(

    @Column(nullable = true)
    val name: String,

    @Column(nullable = true)
    val description: String,

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    val user: User,

    @OneToMany(mappedBy="collection")
    val items: List<Item>

) : BaseEntity()
