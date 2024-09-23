package com.korealm.compApp.models.exceptions

class OrderNotFoundException(override val message: String = "Order not found"): Exception(message) {
}