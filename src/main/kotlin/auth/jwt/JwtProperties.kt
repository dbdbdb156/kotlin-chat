package org.example.auth.jwt

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "jwt")
class JwtProperties {
    lateinit var jweSecret: String
    lateinit var keyPath: String
}