package com.korealm.compApp.models.exceptions

class UserNotFoundException(override val message: String = "User wat not found"): Exception(message)