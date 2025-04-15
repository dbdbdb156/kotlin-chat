package org.example.controller

import org.example.request.ChatMessage
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.simp.annotation.SendToUser
import org.springframework.stereotype.Controller
import java.security.Principal

@Controller
class PrivateChatController {

    @MessageMapping("/chat.private")
    @SendToUser("/queue/private")
    fun sendPrivate(message: ChatMessage, principal: Principal): ChatMessage {
        println("보낸 사람: ${principal.name}")
        return message // sender 본인에게 echo (1:1 메시지는 3번 방식 사용 권장)
    }
}