package org.example.domain.room

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "chat_room_user")
data class ChatRoomUser(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    val room: ChatRoom,

    val userId: String,

    val createdAt: LocalDateTime = LocalDateTime.now()
)