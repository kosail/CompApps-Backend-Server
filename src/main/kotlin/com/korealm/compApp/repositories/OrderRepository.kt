package com.korealm.compApp.repositories

import com.korealm.compApp.models.Order
import com.korealm.compApp.models.ShoppingOrder
import com.korealm.compApp.models.EducationalOrder
import com.korealm.compApp.models.RideOrder
import org.springframework.stereotype.Repository

// TODO: This should be reimplemented ASAP using an H2 database. I should determine whether to use PostgresSQL or MariaDB

sealed interface OrderRepository<T: Order> {
    fun save(order: T): T?
    fun modifyOrder(orderId: String, order: T): T?
    fun cancelOrder(order: T): T? // We can return a boolean here, but let's keep the object T as a return and handle that boolean verification in services.
    fun removeOrder(order: T): T?
    fun findByOrderId(orderId: String): T?
    fun findAll(): List<T>  // Returning List instead of MutableList to ensure the immutability of data in a way that it needs to be manually and explicitly modified by the frontend as intended.
}

// Separating each order type in three different classes even tho at this point it can be a simple generic method. This because  I want to stick to the Single Responsibility Principle, and also because it will become less clear and prone to undefined behaviour in the findByOrder and findAll methods.
// Note for myself: Maybe it can be done in much smart manner, but this is the way I manage to understand and code in a clear way for myself.

@Repository
class InMemoryShoppingOrderRepositoryImpl: OrderRepository<ShoppingOrder> {
    private val shoppingOrders = mutableMapOf<String, ShoppingOrder>()


    override fun save(order: ShoppingOrder): ShoppingOrder? {
        shoppingOrders[order.orderId] = order
        return shoppingOrders[order.orderId]
    }

    override fun modifyOrder(orderId: String, order: ShoppingOrder): ShoppingOrder? {
        return shoppingOrders.replace(orderId, order)
    }

    override fun cancelOrder(order: ShoppingOrder): ShoppingOrder? {
        return shoppingOrders.remove(order.orderId)
    }

    override fun removeOrder(order: ShoppingOrder): ShoppingOrder? {
        return shoppingOrders.remove(order.orderId)
    }

    override fun findByOrderId(orderId: String): ShoppingOrder? {
        return shoppingOrders[orderId]
    }

    override fun findAll(): List<ShoppingOrder> {
        return shoppingOrders.values.toList()
    }
}

@Repository
class InMemoryEducationalOrderRepositoryImpl: OrderRepository<EducationalOrder> {
    private val educationalOrders = mutableMapOf<String, EducationalOrder>()

    override fun save (order: EducationalOrder): EducationalOrder? {
        educationalOrders[order.orderId] = order
        return educationalOrders[order.orderId]
    }

    override fun modifyOrder(orderId: String, order: EducationalOrder): EducationalOrder? {
        return educationalOrders.replace(orderId, order)
    }

    override fun cancelOrder(order: EducationalOrder): EducationalOrder? {
        return educationalOrders.remove(order.orderId)
    }

    override fun removeOrder(order: EducationalOrder): EducationalOrder? {
        return educationalOrders.remove(order.orderId)
    }

    override fun findByOrderId (orderId: String): EducationalOrder? {
        return educationalOrders[orderId]
    }

    override fun findAll(): List<EducationalOrder> {
        return educationalOrders.values.toList()
    }

}

@Repository
class InMemoryRideOrderRepositoryImpl: OrderRepository<RideOrder> {
    private val rideOrders = mutableMapOf<String, RideOrder>()

    override fun save(order: RideOrder): RideOrder? {
        rideOrders[order.orderId] = order
        return rideOrders[order.orderId]
    }

    override fun modifyOrder(orderId: String, order: RideOrder): RideOrder? {
        return rideOrders.replace(orderId, order)
    }

    override fun cancelOrder(order: RideOrder): RideOrder? {
        return rideOrders.remove(order.orderId)
    }

    override fun removeOrder(order: RideOrder): RideOrder? {
        return rideOrders.remove(order.orderId)
    }

    override fun findByOrderId(orderId: String): RideOrder? {
        return rideOrders[orderId]
    }

    override fun findAll(): List<RideOrder> {
        return rideOrders.values.toList()
    }
}