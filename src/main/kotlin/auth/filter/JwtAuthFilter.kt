package org.example.auth.filter

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.example.auth.jwt.JwtProvider
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtAuthFilter(
    private val jwtProvider: JwtProvider
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val authHeader = request.getHeader("Authorization")
        if (!authHeader.isNullOrBlank() && authHeader.startsWith("Bearer ")) {
            try {
                val token = authHeader.removePrefix("Bearer ").trim()
                val userId = jwtProvider.getuserIdFromToken(token)

                val authentication = UsernamePasswordAuthenticationToken(
                    userId,
                    null,
                    emptyList() //listOf(SimpleGrantedAuthority("ROLE_USER"))
                )
                SecurityContextHolder.getContext().authentication = authentication
                request.setAttribute("userId", userId)
            } catch (e: Exception) {
                response.status = HttpServletResponse.SC_UNAUTHORIZED
                response.writer.write("Unauthorized")
                return
            }
        }

        filterChain.doFilter(request, response)
    }
}
