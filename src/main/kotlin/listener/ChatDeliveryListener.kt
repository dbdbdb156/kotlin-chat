package org.example.listener

import org.example.dto.BroadcastChatEvent
import org.example.dto.ChatMessageSendEvent
import org.example.service.WebSocketUserService
import org.springframework.context.event.EventListener
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service

@Service
class ChatDeliveryListener(
    private val messagingTemplate: SimpMessagingTemplate,
    private val webSocketUserService: WebSocketUserService
) {

    @Async
    @EventListener
    fun handleChatMessageSent(event: ChatMessageSendEvent) {
        val (senderId, receiverId, message) = event

        if (!webSocketUserService.isUserConnected(receiverId)) {
            println("수신자 $receiverId 는 현재 WebSocket에 연결되어 있지 않습니다.")
            return
        }
        // 보낸 사람에게 메시지 전송
        messagingTemplate.convertAndSendToUser(senderId, "/queue/messages", message)
        if (!receiverId.isNullOrBlank()) {
            // 수신자에게도 전송
            messagingTemplate.convertAndSendToUser(receiverId, "/queue/messages", message)
        }
    }

    @Async
    @EventListener
    fun handleBroadcastNotice(event: BroadcastChatEvent) {
        if (event.receiverId == "ALL") {
            messagingTemplate.convertAndSend("/topic/notice", event.message)
        } else {
            throw Exception("broad notice failed")
        }
    }

}