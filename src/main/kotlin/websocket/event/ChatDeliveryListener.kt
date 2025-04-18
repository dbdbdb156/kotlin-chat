package org.example.websocket.event

import org.example.feign.UserFeignClient
import org.example.model.dto.BroadcastChatEvent
import org.example.model.dto.ChatMessageSendEvent
import org.example.model.request.ChatMessage
import org.example.model.response.ChatMessageResponse
import org.example.service.WebSocketUserService
import org.springframework.context.event.EventListener
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import javax.sound.midi.Receiver

@Service
class ChatDeliveryListener(
    private val messagingTemplate: SimpMessagingTemplate,
    private val webSocketUserService: WebSocketUserService,
    private val userFeignClient: UserFeignClient
) {

    @Async
    @EventListener
    fun handleChatMessageSent(event: ChatMessageSendEvent) {
        val (senderId, receiverId, message) = event
        val chatMessage = this.setNickName(senderId, receiverId, message)

        if (!webSocketUserService.isUserConnected(receiverId)) {
            println("수신자 $receiverId 는 현재 WebSocket에 연결되어 있지 않습니다.")
            return
        }
        // 보낸 사람에게 메시지 전송
        messagingTemplate.convertAndSendToUser(senderId, "/queue/messages", chatMessage)
        if (!receiverId.isNullOrBlank()) {
            // 수신자에게도 전송
            messagingTemplate.convertAndSendToUser(receiverId, "/queue/messages", chatMessage)
        }
    }

    @Async
    @EventListener
    fun handleBroadcastNotice(event: BroadcastChatEvent) {
        if (event.receiverId == "ALL") {
            val destination = "/topic/notice"
            val chatMessage = this.setNickName(event.senderId, event.receiverId, event.message)
            messagingTemplate.convertAndSend(destination, chatMessage)
        } else {
            throw Exception("broad notice failed")
        }
    }

    private fun setNickName(senderId: String, receiverId: String, message: ChatMessage): ChatMessageResponse {
        val userIds = listOf(senderId, receiverId);
        val users = userFeignClient.getUsersByIds(userIds)
        val senderNickname = users.firstOrNull() { it.id == senderId}?.nickname ?: ""
        val receiverNickname = users.firstOrNull { it.id == receiverId}?.nickname ?: ""

        return ChatMessageResponse(
            senderId = message.senderId,
            senderNickname = senderNickname,
            receiverId = message.receiverId,
            receiverNickname = receiverNickname,
            content = message.content,
            roomId = message.roomId,
            timestamp = message.timestamp
        )
    }

}