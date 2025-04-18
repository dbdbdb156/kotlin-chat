package org.example.model.response

import jakarta.persistence.*
import org.example.domain.room.ChatRoom
import org.example.domain.room.ChatRoomType

data class ChatRoomDto (
    val id: Long,
    val name: String,
    val type: ChatRoomType
) {
    companion object {
        fun from(chatRoom: ChatRoom): ChatRoomDto =
            ChatRoomDto(
                id = chatRoom.id,
                name = chatRoom.name,
                type = chatRoom.type
            )
    }
}