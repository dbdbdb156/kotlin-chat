package org.example.controller.message

import org.example.model.dto.BroadcastChatEvent
import org.example.model.request.ChatMessage
import org.springframework.context.ApplicationEventPublisher
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.stereotype.Controller

@Controller
class BroadcastChatController(
    private val applicationEventPublisher: ApplicationEventPublisher
) {

    @MessageMapping("/chat.notice")
    @SendTo("/topic/notice")
    fun broadcastNotice(message: ChatMessage) {
        val senderId = message.senderId
        applicationEventPublisher.publishEvent(
            BroadcastChatEvent(senderId, message.receiverId, message)
        )
    }
}

