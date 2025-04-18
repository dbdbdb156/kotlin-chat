package org.example.service

import org.springframework.messaging.simp.user.SimpUserRegistry
import org.springframework.stereotype.Service

@Service
class WebSocketUserService(
    private val simpUserRegistry: SimpUserRegistry
) {

    fun isUserConnected(userId: String): Boolean {
        return simpUserRegistry.users.any { it.name == userId }
    }

    fun getAllConnectedUserIds(): List<String> {
        return simpUserRegistry.users.map { it.name }
    }
}