package org.example.dto

import org.example.request.ChatMessage

data class ChatMessageSendEvent(
    val senderId: String,
    val receiverId: String,
    val message: ChatMessage
)

data class PrivateChatEvent(
    val senderId: String,
    val receiverId: String,
    val message: ChatMessage
)

data class BroadcastChatEvent(
    val senderId: String,
    val receiverId: String,
    val message: ChatMessage
)