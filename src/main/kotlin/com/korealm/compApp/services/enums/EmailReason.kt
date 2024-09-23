package com.korealm.compApp.services.enums

enum class EmailReason(private val path: String) {
    WELCOME("templates/WelcomeNewUser.html"),
    PASSWORD_RESET("templates/PasswordReset.html");

    fun getPath(): String {
        return this.path
    }

}