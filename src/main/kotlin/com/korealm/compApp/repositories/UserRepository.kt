package com.korealm.compApp.repositories

import com.korealm.compApp.models.*
import org.springframework.stereotype.Repository

/*
Repositories are the ones who their solely job is to contact the DB to retrieve and store information.
All constraints and data integrity checks should go on the services, which are the ones that interact with repositories.
 */

// TODO: This should be reimplemented ASAP using an H2 database. I should determine whether to use PostgresSQL or MariaDB

sealed interface UserRepository {
    fun save(user: User): String?
    fun updateUser(user: User): Boolean
    fun findByUserUniqueId(uniqueId: String): User?
    fun findByEmail(email: String): User?
}

@Repository
class UserRepositoryImpl: UserRepository {
    private val users = HashMap<String, User>()

    override fun save(user: User): String? {
        users[user.uniqueId] = user
        return users[user.uniqueId]?.uniqueId
    }

    override fun updateUser(user: User): Boolean {
        return users.replace(user.uniqueId, user) != null
    }

    override fun findByUserUniqueId(uniqueId: String): User? {
        return users[uniqueId]
    }

    // This is a bad idea tbh. Will be slow af, but as a small implementation it should never take even a couple of seconds to complete, maybe 1 or 2 at max.
    // This will be replaced for sure when we change to a proper database.
    override fun findByEmail(email: String): User? {
        return users.values.find { it.email.equals(email, ignoreCase = true) }
    }
}