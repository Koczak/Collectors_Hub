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
    @Enumerated(EnumType.STRING)
    var status: FriendStatus = FriendStatus.PENDING

) : BaseEntity() {
    enum class FriendStatus {
        PENDING, ACCEPTED, REJECTED
    }
    
    companion object {
        const val STATUS_PENDING = "PENDING"
        const val STATUS_ACCEPTED = "ACCEPTED"
        const val STATUS_REJECTED = "REJECTED"
    }
}