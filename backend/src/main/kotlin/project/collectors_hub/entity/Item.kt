package project.collectors_hub.entity

import jakarta.persistence.*
import project.collectors_hub.helper.MapStringAnyJsonAttributeConverter

@Entity
@Table(name = "items")
data class Item(

    @Column(nullable = false)
    var name: String,

    @Column(nullable = true)
    var description: String,

    @ManyToOne
    @JoinColumn(name = "collection_id", nullable = false)
    val collection: Collection,

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = true)
    var category: Category?,

    @Column(nullable = true)
    @Convert(converter = MapStringAnyJsonAttributeConverter::class)
    var attributes: Map<String, Any>?,
    
    @OneToMany(mappedBy = "item", cascade = [CascadeType.ALL], orphanRemoval = true)
    val comments: MutableList<Comment> = mutableListOf()

) : BaseEntity()
