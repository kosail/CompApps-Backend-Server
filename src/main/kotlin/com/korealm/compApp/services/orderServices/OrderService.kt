package com.korealm.compApp.services.orderServices

import com.korealm.compApp.dtos.*
import com.korealm.compApp.models.*

// Due differences between the DTOs and limitations in this design, I have found no choice but to create one interface for each order type. I'm not sure if this would be the most "professional" way, but I think it aligns with the single responsibility principle.

sealed interface EducationalOrderService {
    fun listAll(): List<EducationalOrder>
    fun getOrderDetails(orderId: String): EducationalOrder?
    fun newOrder(orderDto: EducationalOrderDto): EducationalOrder?
    fun modifyOrder(orderDto: EducationalOrderDto): Boolean
    fun cancelOrder(orderDto: EducationalOrderDto): Boolean
    fun completeOrder(orderDto: EducationalOrderDto): Boolean
    fun acceptOrder(orderDto: EducationalOrderDto): Boolean
    fun rejectAcceptedOrder(orderDto: EducationalOrderDto): Boolean
}

sealed interface ShoppingOrderService {
    fun listAll(): List<ShoppingOrder>
    fun getOrderDetails(orderId: String): ShoppingOrder?
    fun newOrder(orderDto: ShoppingOrderDto): ShoppingOrder?
    fun modifyOrder(orderDto: ShoppingOrderDto): Boolean
    fun cancelOrder(orderDto: ShoppingOrderDto): Boolean
    fun completeOrder(orderDto: ShoppingOrderDto): Boolean
    fun acceptOrder(orderDto: ShoppingOrderDto): Boolean
    fun rejectAcceptedOrder(orderDto: ShoppingOrderDto): Boolean
}

sealed interface RideOrderService {
    fun listAll(): List<RideOrder>
    fun getOrderDetails(orderId: String): RideOrder?
    fun newOrder(orderDto: RideOrderDto): RideOrder?
    fun modifyOrder(orderDto: RideOrderDto): Boolean
    fun cancelOrder(orderDto: RideOrderDto): Boolean
    fun completeOrder(orderDto: RideOrderDto): Boolean
    fun acceptOrder(orderDto: RideOrderDto): Boolean
    fun rejectAcceptedOrder(orderDto: RideOrderDto): Boolean
}

