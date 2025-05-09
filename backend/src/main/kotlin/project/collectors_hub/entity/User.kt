package project.collectors_hub.entity

import jakarta.persistence.*

@Entity
@Table(name = "users")
data class User(

    @Column(nullable = false, unique = true)
    var username: String,

    @Column(nullable = false)
    var password: String,

    @Column(nullable = false)
    var email: String,

    @Column(nullable = false)
    val roles: String,

    @OneToMany(mappedBy="user")
    val collections: List<Collection>,

    @OneToMany(mappedBy="user")
    val categories: List<Category>,

    @OneToMany(mappedBy="user", cascade = [CascadeType.ALL], orphanRemoval = true)
    val friends: List<Friend> = mutableListOf()

) : BaseEntity() {
    companion object {
        const val ROLE_ADMIN = "ADMIN"
        const val ROLE_USER = "USER"
    }

    fun isAdmin() = roles == ROLE_ADMIN
}



