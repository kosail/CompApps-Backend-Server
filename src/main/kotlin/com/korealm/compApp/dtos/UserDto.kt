package com.korealm.compApp.dtos

import com.korealm.compApp.models.enums.UserRole
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size


data class UserDto(
    @field:NotBlank(message = "Name cannot be blank")
    @field:Size(min = 1, max = 128, message = "Name must be between 1 and 128 characters")
    val name: String,

    @field:Email(message = "Email should be a valid one")
    @field:NotBlank(message = "Email cannot be blank")
    val email: String,

    // TODO: Verify with Katsuko password minimum requirements
    @field:NotBlank(message = "Password cannot be blank")
    @field:Size(min = 6, max=128, message = "Password should have at least 6 characters")
    val password: String,

    // TODO: Verify with Katsuko how we can upload (and where) the users photo, which will be 3: their face photo, and the student credential upfront and backside. Determine if (and how) she will send the photo in the JSON, or an URL of where the photo will be stored or something.
    @field:NotBlank(message = "Student ID cannot be blank")
    @field:Size(min = 8, max = 8, message = "Student ID should be exactly 8 characters")
    val studentId: Int,

    @field:NotBlank(message = "User type must be specified")
    val role: UserRole
)

data class LoginDto(

    @field:Email(message = "Email should be a valid one")
    @field:NotBlank(message = "Email cannot be blank")
    val email: String,

    @field:NotBlank(message = "Password cannot be blank")
    @field:Size(min = 6, max=128, message = "Password should have at least 6 characters")
    val password: String
)

// TODO: Confirm with Katsuko what data will user profiles contain
data class ProfileDto(
    val bio: String?,
    val profileImageUrl: String?,
    val rating: Double?
)
