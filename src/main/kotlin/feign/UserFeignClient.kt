package org.example.feign

import org.example.model.response.UserResponse
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam

@FeignClient(
    name = "user-feign-client",
    url = "http://localhost:3000",  // 직접 명시 추후 배포시 로드벨런서 등록 예정
    fallback = UserFeignClient.UserClientFallback::class
    )
interface UserFeignClient {

    @GetMapping("/user/list")
    fun getUsersByIds(@RequestParam("ids") ids: List<String>): List<UserResponse>

    @Component
    class UserClientFallback : UserFeignClient {
        override fun getUsersByIds(ids: List<String>): List<UserResponse> {
            println("⚠️ UserClient fallback triggered! Returning empty list.")
            return emptyList()
        }
    }

}