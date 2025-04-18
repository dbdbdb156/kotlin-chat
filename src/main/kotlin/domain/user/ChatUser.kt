package org.example.domain.user

import jakarta.persistence.*

data class ChatUser(
    val userId: String,
    val email: String,
    val roles: List<UserRole>,
)