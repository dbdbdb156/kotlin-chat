package org.example.model.request

import org.example.domain.room.ChatRoomType

data class CreateRoomRequest (
    val roomId: String,
    val userIds: List<String>,
    val type: ChatRoomType,
)