package org.example.model.response

data class UserResponse (
    val id: String?,
    val nickname: String?,
    val email: String?,
    val roles: List<String> = emptyList()
)