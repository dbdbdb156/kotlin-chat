package org.example.config

import com.fasterxml.jackson.databind.ObjectMapper
import org.example.provider.JwtProvider
import org.example.request.ChatMessage
import org.springframework.context.annotation.Configuration
import org.springframework.messaging.Message
import org.springframework.messaging.MessageChannel
import org.springframework.messaging.simp.config.ChannelRegistration
import org.springframework.messaging.simp.stomp.StompCommand
import org.springframework.messaging.simp.stomp.StompHeaderAccessor
import org.springframework.messaging.support.ChannelInterceptor
import org.springframework.messaging.support.MessageHeaderAccessor
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer


@Configuration
@EnableWebSocketMessageBroker
class WebSocketSecurityConfig(
    private val jwtProvider: JwtProvider // 너가 만든 JWE 처리기
) : WebSocketMessageBrokerConfigurer {

    override fun configureClientInboundChannel(registration: ChannelRegistration) {
        registration.interceptors(object : ChannelInterceptor {
            override fun preSend(message: Message<*>, channel: MessageChannel): Message<*> {
                val accessor = MessageHeaderAccessor.getAccessor(
                    message,
                    StompHeaderAccessor::class.java
                )
                if (accessor.command == StompCommand.CONNECT) {
                    val authHeader = accessor.getFirstNativeHeader("Authorization")
                    val token = authHeader?.removePrefix("Bearer ")?.trim()

                    if (!token.isNullOrBlank()) {
                        try {
                            val userId = jwtProvider.getuserIdFromToken(token) // JWE 복호화 + 서명 검증
                            accessor.user = UsernamePasswordAuthenticationToken(userId, null, emptyList())
                        } catch (ex: Exception) {
                            throw IllegalArgumentException("Invalid WebSocket token: ${ex.message}")
                        }
                    } else {
                        throw IllegalArgumentException("Missing Authorization token")
                    }
                }
                return message
            }
        })
    }
}