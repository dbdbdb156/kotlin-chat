package org.example.listener

import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import org.springframework.web.socket.messaging.SessionConnectEvent
import org.springframework.web.socket.messaging.SessionDisconnectEvent

@Component
class WebSocketTracker {

    private val connectedUsers = mutableSetOf<String>()

    @EventListener
    fun handleConnect(event: SessionConnectEvent) {
        event.user?.name?.let {
            connectedUsers += it
            println("✅ 접속: $it / 전체 접속자: $connectedUsers")
        }
    }

    @EventListener
    fun handleDisconnect(event: SessionDisconnectEvent) {
        event.user?.name?.let {
            connectedUsers -= it
            println("❌ 종료: $it / 전체 접속자: $connectedUsers")
        }
    }

    fun getConnectedUsers(): Set<String> = connectedUsers
}