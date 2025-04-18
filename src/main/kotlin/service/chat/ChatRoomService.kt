package org.example.service.chat

import org.example.domain.room.ChatRoom
import org.example.domain.room.ChatRoomUser
import org.example.feign.UserFeignClient
import org.example.model.request.CreateRoomRequest
import org.example.model.response.ChatRoomDto
import org.example.model.response.UserBriefResponse
import org.example.repository.chat.ChatRoomRepository
import org.example.repository.chat.ChatRoomUserRepository
import org.springframework.stereotype.Service

@Service
class ChatRoomService(
    private val chatRoomRepo: ChatRoomRepository,
    private val chatRoomUserRepo: ChatRoomUserRepository,
    private val userFeignClient: UserFeignClient
) {
    fun createRoom(request: CreateRoomRequest): ChatRoomDto {
        val room = chatRoomRepo.save(ChatRoom(name = request.roomId, type = request.type))
        val users = userFeignClient.getUsersByIds(request.userIds)
        users.mapNotNull { user ->
            user.id?.
            takeIf { it.isNotBlank() }?.
            let { userId ->
                ChatRoomUser(room = room, userId = userId)
            }
        }.forEach { chatRoomUser ->
            chatRoomUserRepo.save(chatRoomUser)
        }
        return ChatRoomDto.from(room)
    }

    fun getUserIdsInRoom(roomId: Long): List<UserBriefResponse> {
        return chatRoomUserRepo.findAllByRoomId(roomId)
            .let {
                val userIds = it.map { it.userId}
                val users = userFeignClient.getUsersByIds(userIds)
                return users.map {
                    UserBriefResponse(
                        id = it.id,
                        nickname = it.nickname
                    )
                }
            }
    }

    fun getRoomIdsByUserId(userId: String): List<ChatRoom> {
        return chatRoomUserRepo.findAllByUserId(userId)
            .map { it.room }
    }

    fun getRoomById(roomId: Long): ChatRoomDto {
        val room = chatRoomRepo.findById(roomId)
            .orElseThrow { IllegalArgumentException("채팅방이 존재하지 않습니다.") }
        return ChatRoomDto.from(room)
    }
}