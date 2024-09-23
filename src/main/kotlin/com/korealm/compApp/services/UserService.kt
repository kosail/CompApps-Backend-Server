package com.korealm.compApp.services

import com.korealm.compApp.models.User
import com.korealm.compApp.dtos.*
import com.korealm.compApp.models.exceptions.UserNotFoundException
import com.korealm.compApp.repositories.UserRepository
import com.korealm.compApp.services.security.Encryption
import org.springframework.stereotype.Service

/*
TODO: As for now, register and login functions are returning the user's unique ID as a form of telling the Component that the register/login went successful. In the future this will be changed by a session ID, which tbh I have no idea how to create. I have to look into this.
*/

sealed interface UserService {
    fun registerUser(userDto: UserDto): String?
    fun loginUser(loginDto: LoginDto): String?
    fun passwordReset(email: String): Boolean
}

// We need the repositories to be able to store, retrieve or modify information on our DB (In-memory DB by the moment). However, it is NOT a good practice to instantiate the dependencies within the class, but instead receive them as an argument. This is the dependency injection pattern ahead in the constructor.

@Service
class UserServiceImpl(
    private val userRepository: UserRepository,
    private val emailService: EmailService,
    private val encryption: Encryption
): UserService {

    override fun registerUser(userDto: UserDto): String? {
        validateUserInput(userDto)

        val validateUniqueUser: Boolean = isUserRegistered(userDto.email) == null
        require(validateUniqueUser) { "The student ID or email is already registered." }

        val userUniqueID = encryption.generateSecureHash()

        val user = User(
            userUniqueID,
            userDto.studentId,
            userDto.name,
            userDto.email,
            encryption.generateSecureHash(userDto.password),
            userDto.role
            )

        // Note that this is not a static call of a method. Kotlin does not have a "static" concept. Instead, we are using dependency injection
        emailService.sendWelcomeMessage(user.email, user.name)

        return userRepository.save(user)
    }

    override fun loginUser(loginDto: LoginDto): String? {
        val email: String = loginDto.email

        require(isEmailValid(email)) {"Only institutional emails are allowed."}

        val user: User? = isUserRegistered(loginDto.email)
        val password: String = loginDto.password

        require(encryption.generateSecureHash(password) == user?.password) {"Incorrect password."}

        return user?.uniqueId
    }

    // This password reset function is primitive, non-safe at all since anyone knowing the user's email can reset the password.
    // TODO: I should send a confirmation email instead with a link where they can reset the password, but I don't know how to do that. I have to research about it.
    // After resetting the password it should also end all user sessions for security and obvious reasons.
    override fun passwordReset(email: String): Boolean {
        val user: User? = isUserRegistered(email)
        val updatedUser: User

        if (user == null) {
            throw UserNotFoundException("The user is not yet registered.")
        } else {
            val newPassword: String = encryption.generateSecureHash().substring(0, 8)
            updatedUser = user.changePassword(newPassword)
            emailService.sendPasswordReset(email, newPassword)
        }

        return userRepository.updateUser(updatedUser)
    }

    // This function will verify that the email and the password meet the required criteria. To keep it legible and simple, it will be split in two other functions, which will contain their own logic. Also, in this way we keep the dependency injection pattern
    private fun validateUserInput(userDto: UserDto) {
        val validateEmail: Boolean = isEmailValid(userDto.email) && doEmailMatchStudentId(userDto.email, userDto.studentId)

        // TODO: DEACTIVATED FOR THE PRESENTATION OF THE PROJECT so anyone can register. UNDO THIS.
//        require(validateEmail) {"Only institutional emails are allowed, and it must also match the student ID."}
        require(isPasswordStrong(userDto.password)) {"The password does not meet the minimum security criteria. It must contain at least 1 uppercase and 1 lowercase letter, 1 number and 1 symbol"}
    }

    // This function ensures that:
    //      Email starts with l, as this is the institutional format.
    //      The domain matches the institutional one.
    // If the mail does not match any of these criteria, or it's format does not match the specified one, this method will return false and the validateUserInput function will launch an IllegalArgumentException.
    private fun isEmailValid(email: String): Boolean {
        val allowedDomain = ".tecnm.mx"
        return email[0] == 'l' && email.endsWith(allowedDomain)
    }

    // Temporarily we are only accepting student emails, not teachers as it can be seen within this function. This should be reimplemented in the future
    // TODO: Reimplement this as soon as possible
    private fun doEmailMatchStudentId(email: String, studentId: Int): Boolean {
        val username: Int? = email.substring(1, email.indexOf('@')).toIntOrNull()
        return username == studentId
    }

    private fun isPasswordStrong(password: String): Boolean {
        // Regex pattern that ensures the password has at least 1 uppercase letter, 1 lowercase letter, 1 number and 1 symbol.
        val passwordPattern = Regex("^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[\\W_]).+$")
        return password.matches(passwordPattern)
    }

    private fun isUserRegistered(email: String): User? {
        val user: User? = userRepository.findByEmail(email)
        return user
    }

    fun getUserOrException(creatorId: String): User {
        return userRepository.findByUserUniqueId(creatorId) ?: throw UserNotFoundException("Invalid user ID")
    }
}

