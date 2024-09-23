package com.korealm.compApp.services.security

import org.springframework.stereotype.Service
import java.security.MessageDigest
import java.util.*

// This class is fundamental as the generateSecureHash it will generate users and orders unique ID and also hash users passwords for safe storage.

@Service
class Encryption {
    fun generateSecureHash(baseString: String = UUID.randomUUID().toString()): String {
        val sha256: MessageDigest = MessageDigest.getInstance("SHA-256")
        val hash: ByteArray = sha256.digest(baseString.toByteArray())

        return hash.joinToString("") { "%02x".format(it) }
    }
}