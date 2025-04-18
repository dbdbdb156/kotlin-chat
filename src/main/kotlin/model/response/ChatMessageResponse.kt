package org.example.model.response

data class ChatMessageResponse(
    val senderId: String,
    val receiverId: String,
    val content: String,
    val roomId: String,
    val timestamp: Long,
    val senderNickname: String,
    val receiverNickname: String,
)