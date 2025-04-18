package org.example.listener

import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import org.springframework.web.socket.messaging.SessionConnectEvent
import org.springframework.web.socket.messaging.SessionDisconnectEvent

@Component
class WebSocketEventListener {
    @EventListener
    fun handleSessionConnect(event: SessionConnectEvent) {
        println("✅ 연결됨: ${event.message.headers["simpUser"]}")
    }

    @EventListener
    fun handleSessionDisconnect(event: SessionDisconnectEvent) {
        println("❌ 연결 해제됨: ${event.sessionId}")
    }
}
