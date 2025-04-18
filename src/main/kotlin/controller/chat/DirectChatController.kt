package org.example.controller.message

import org.example.model.dto.ChatMessageSendEvent
import org.example.model.request.ChatMessage
import org.springframework.context.ApplicationEventPublisher
import org.springframework.messaging.handler.annotation.Header
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Controller
import java.security.Principal

@Controller
class DirectChatController(
    private val applicationEventPublisher: ApplicationEventPublisher
) {

    @MessageMapping("/chat.send")
    fun handleChat(
        @Payload message: ChatMessage,
        @Header("simpUser") sender: Principal?
    ) {
        val senderId = sender?.name ?: return

        applicationEventPublisher.publishEvent(
            ChatMessageSendEvent(senderId, message.receiverId, message)
        )
    }
}