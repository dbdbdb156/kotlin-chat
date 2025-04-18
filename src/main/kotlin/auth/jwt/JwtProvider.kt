package org.example.auth.jwt

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.nimbusds.jose.crypto.DirectDecrypter
import com.nimbusds.jose.crypto.RSASSAVerifier
import com.nimbusds.jwt.EncryptedJWT
import com.nimbusds.jwt.SignedJWT
import java.security.interfaces.RSAPublicKey
import java.security.KeyFactory
import java.security.spec.X509EncodedKeySpec
import java.util.*
import org.springframework.stereotype.Component

@Component
class JwtProvider(
    private val jwtProperties: JwtProperties
) {
    fun getuserIdFromToken(jweToken: String?): String {
        if (jweToken.isNullOrBlank()) throw IllegalArgumentException("Token is empty")

        // 1. JWE 복호화
        val encryptedJWT = EncryptedJWT.parse(jweToken)
        val decrypter = DirectDecrypter(Base64.getDecoder().decode(jwtProperties.jweSecret))
        encryptedJWT.decrypt(decrypter)

        // 2. JSON 형태의 payload 파싱
        val rawPayload = encryptedJWT.payload.toString()
        val mapper = jacksonObjectMapper()
        val tokenMap = mapper.readValue(rawPayload, Map::class.java)

        val jwtToken = tokenMap["token"] as? String
            ?: throw IllegalArgumentException("No 'token' field in JWE payload")

        // 2. 복호화된 JWT 가져오기
        val jwt = SignedJWT.parse(jwtToken)

        // 3. 서명 검증
        if (!jwt.verify(RSASSAVerifier(this.loadPublicKey()))) {
            throw IllegalArgumentException("JWT signature verification failed")
        }

        // 4. 사용자명 추출
        return jwt.jwtClaimsSet.getStringClaim("userId")
    }

    private fun loadPublicKey(): RSAPublicKey {
        val keyPem = javaClass.getResource(jwtProperties.keyPath)?.readText()
            ?: throw IllegalStateException("Cannot load public.pem")

        val keyData = keyPem
            .replace("-----BEGIN PUBLIC KEY-----", "")
            .replace("-----END PUBLIC KEY-----", "")
            .replace("\\s".toRegex(), "")
        val decoded = Base64.getDecoder().decode(keyData)
        val keySpec = X509EncodedKeySpec(decoded)
        val keyFactory = KeyFactory.getInstance("RSA")
        return keyFactory.generatePublic(keySpec) as RSAPublicKey
    }

}