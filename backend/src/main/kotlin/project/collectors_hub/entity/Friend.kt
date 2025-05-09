package project.collectors_hub.entity

import jakarta.persistence.*

@Entity
@Table(name = "friends")
data class Friend(

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    val user: User,

    @ManyToOne
    @JoinColumn(name = "friend_id", nullable = false)
    val friend: User,

    @Column(nullable = false)
    var status: String = STATUS_PENDING

) : BaseEntity() {
    companion object {
        const val STATUS_PENDING = "PENDING"
        const val STATUS_ACCEPTED = "ACCEPTED"
        const val STATUS_REJECTED = "REJECTED"
    }
}