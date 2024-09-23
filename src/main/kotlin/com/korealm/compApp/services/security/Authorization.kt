package com.korealm.compApp.services.security

import com.korealm.compApp.dtos.OrderDto
import com.korealm.compApp.models.Order
import com.korealm.compApp.models.User
import com.korealm.compApp.models.exceptions.*
import com.korealm.compApp.repositories.OrderRepository
import com.korealm.compApp.repositories.UserRepository
import org.springframework.stereotype.Service

@Service
class Authorization(private val userRepository: UserRepository) {

    fun <T: Order, D: OrderDto> confirmAuthorizedUser(
        orderDto: D,
        orderRepository: OrderRepository<T>
    ): T {
        if (orderDto.orderId.isBlank()) throw OrderNotFoundException()

        val order: T = orderRepository.findByOrderId(orderDto.orderId) ?: throw OrderNotFoundException()
        val creator: User = userRepository.findByUserUniqueId(orderDto.creatorId) ?: throw UserNotFoundException()

        if (order.creator != creator) throw IllegalAccessException("User ${creator.uniqueId} is not allowed to perform this action")

        return order
    }
}
