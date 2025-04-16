package project.collectors_hub.entity

import jakarta.persistence.*

@Entity
@Table(name = "users")
data class User(

    @Column(nullable = false, unique = true)
    val username: String,

    @Column(nullable = false)
    val password: String,

    @Column(nullable = false)
    val email: String,

    @Column(nullable = false)
    val roles: String,

    @OneToMany(mappedBy="user")
    val collections: List<Collection>,

    @OneToMany(mappedBy="user")
    val categories: List<Category>

) : BaseEntity() {
    companion object {
        const val ROLE_ADMIN = "ADMIN"
        const val ROLE_USER = "USER"
    }
}



