package com.korealm.compApp.controllers

import com.korealm.compApp.models.User
import com.korealm.compApp.repositories.UserRepositoryImpl
import com.korealm.compApp.services.UserService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/test")
class TestController (
    private val userRepositoryImpl: UserRepositoryImpl
) {
    @GetMapping("/getUsers")
    fun getUsers(): String? {
        return userRepositoryImpl.findByEmail("l23050968@saltillo.tecnm.mx")?.uniqueId
    }
}