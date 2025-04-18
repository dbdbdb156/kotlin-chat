package org.example.domain.room

import jakarta.persistence.*

@Entity
@Table(name = "chat_room")
data class ChatRoom(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false)
    val name: String,

    @Enumerated(EnumType.STRING)
    val type: ChatRoomType = ChatRoomType.GROUP // PRIVATE, GROUP 등
)