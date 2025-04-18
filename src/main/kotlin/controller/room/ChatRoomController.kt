package org.example.controller.room

import org.example.domain.room.ChatRoom
import org.example.model.request.CreateRoomRequest
import org.example.model.response.ChatRoomDto
import org.example.model.response.UserBriefResponse
import org.example.service.chat.ChatRoomService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/rooms")
class ChatRoomController(
    private val chatRoomService: ChatRoomService
) {
    @PostMapping
    suspend fun createRoom(@RequestBody body: CreateRoomRequest): ChatRoomDto {
        return chatRoomService.createRoom(body)
    }

    @GetMapping("/{roomId}")
    suspend fun getRoom(@PathVariable roomId: Long): ChatRoomDto {
        return chatRoomService.getRoomById(roomId)
    }

    @GetMapping("/users/{roomId}")
    suspend fun getUserIdsInRoom(@PathVariable roomId: Long): List<UserBriefResponse> {
        return chatRoomService.getUserIdsInRoom(roomId)
    }

    @GetMapping("/room/{userId}")
    suspend fun getRoomIdsByUserId(@PathVariable userId: String): List<ChatRoom> {
        return chatRoomService.getRoomIdsByUserId(userId)
    }
}