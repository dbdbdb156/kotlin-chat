package org.example.repository.chat

import org.example.domain.room.ChatRoomUser
import org.springframework.data.jpa.repository.JpaRepository

interface ChatRoomUserRepository : JpaRepository<ChatRoomUser, Long> {
    fun findAllByRoomId(roomId: Long): List<ChatRoomUser>
    fun findAllByUserId(userId: String): List<ChatRoomUser>
}