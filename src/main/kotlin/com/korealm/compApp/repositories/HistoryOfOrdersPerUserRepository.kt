package com.korealm.compApp.repositories

import com.korealm.compApp.models.Order
import com.korealm.compApp.models.User
import com.korealm.compApp.models.enums.OrderStatus
import com.korealm.compApp.models.exceptions.OrderNotFoundException
import org.springframework.stereotype.Repository

sealed interface HistoryOfOrdersPerUserRepository {
    fun addOrderToHistoryOfOrders(user: User, order: Order)
    fun alterOrderFromHistoryOfOrders(user: User, orderId: String, newStatus: OrderStatus)
    fun searchOrderInHistory(user: User, orderId: String): Order?
}

@Repository
class HistoryOfOrdersPerUserRepositoryImpl : HistoryOfOrdersPerUserRepository {
    private val historyOfOrdersPerUser = HashMap<User, MutableList<Order>>()

    override fun addOrderToHistoryOfOrders(user: User, order: Order) {
        val orders = historyOfOrdersPerUser[user] ?: mutableListOf()
        orders.add(order)
        historyOfOrdersPerUser[user] = orders
    }

    override fun alterOrderFromHistoryOfOrders(user: User, orderId: String, newStatus: OrderStatus) {
        val orders = historyOfOrdersPerUser[user]

        if (orders != null) {
            val order = orders.find { it.orderId == orderId }
                ?: throw OrderNotFoundException("Order with ID $orderId not found for user ${user.name}")
            order.status = newStatus
        } else {
            throw OrderNotFoundException("The user has no orders in history.")
        }
    }

    override fun searchOrderInHistory(user: User, orderId: String): Order? {
        val orders = historyOfOrdersPerUser[user]
            ?: throw OrderNotFoundException("User ${user.name} has no orders in history.")

        return orders.find { it.orderId == orderId }
            ?: throw OrderNotFoundException("Order with ID $orderId not found for user ${user.name}")
    }

}
