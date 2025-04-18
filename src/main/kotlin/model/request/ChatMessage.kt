package org.example.model.request

data class ChatMessage(
    val senderId: String,
    val receiverId: String,
    val content: String,
    val roomId: String,
    val timestamp: Long
)