package com.korealm.compApp.controllers

import com.korealm.compApp.dtos.LoginDto
import com.korealm.compApp.dtos.UserDto
import com.korealm.compApp.services.UserService
import org.springframework.web.bind.annotation.*
import org.springframework.http.ResponseEntity

@RestController
@RequestMapping("/api/users")
class UserController(private val userService: UserService) {

    @PostMapping("/register")
    fun registerUser(@RequestBody userDto: UserDto): ResponseEntity<String> {
        val uniqueUserId = userService.registerUser(userDto)
        return if (uniqueUserId != null) ResponseEntity.ok(uniqueUserId) else ResponseEntity.badRequest().body("Registration failed")
    }

    @PostMapping("/login")
    fun loginUser(@RequestBody loginDto: LoginDto): ResponseEntity<String> {
        val uniqueUserId = userService.loginUser(loginDto)
        return if (uniqueUserId != null) ResponseEntity.ok(uniqueUserId) else ResponseEntity.badRequest().body("Login failed")
    }

    @PostMapping("/reset-password")
    fun resetPassword(@RequestParam email: String): ResponseEntity<Boolean> {
        val success = userService.passwordReset(email)
        return if (success) ResponseEntity.ok(true) else ResponseEntity.badRequest().body(false)
    }
}
