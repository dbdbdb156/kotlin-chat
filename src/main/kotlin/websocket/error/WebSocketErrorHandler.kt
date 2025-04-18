package org.example.websocket.error

import org.springframework.messaging.handler.annotation.MessageExceptionHandler
import org.springframework.web.bind.annotation.ControllerAdvice

@ControllerAdvice
class WebSocketErrorHandler {
    @MessageExceptionHandler(Exception::class)
    fun handleException(e: Exception): String {
        println("❌ WebSocket 핸들러 오류: ${e.message}")
        return "오류 발생"
    }
}