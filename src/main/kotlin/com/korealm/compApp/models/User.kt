package com.korealm.compApp.models

import com.korealm.compApp.models.enums.UserRole

// Models are the actual business logic, the way "things should be". The correct design of these are fundamental. They mark down the way all things will be done in the backend.
// DTO's are simplified versions of these, and when DTO's are exposed externally, models are for internal use only.

data class User (
    val uniqueId: String,
    val studentId: Int,
    val name: String,
    val email: String,
    val password: String,
    val role: UserRole
) {
    fun changePassword(newPassword: String): User {
        return this.copy(password = newPassword)
    }
}