package com.korealm.compApp.models

import com.korealm.compApp.models.enums.OrderCategory
import com.korealm.compApp.models.enums.OrderStatus
import com.korealm.compApp.models.enums.RideDirection
import java.time.LocalTime

// Order class is sealed because in that way it can only be inherited by the classes defined within this same file, making the compiler know that only these are the possible options when dealing with Order-type objects.

sealed class Order(
    open val orderId: String,
    open val category: OrderCategory,
    open val price: Double,
    open val creator: User,
    open val creationTime: LocalTime,
    open var status: OrderStatus
)

data class ShoppingOrder(
    override val orderId: String,
    val productName: String,
    val description: String,
    val placeOfMeeting: String,
    override val price: Double,
    override val creator: User,
    override val creationTime: LocalTime,
    override var status: OrderStatus
): Order(orderId, OrderCategory.SHOPPING, price, creator, creationTime, status)

data class EducationalOrder(
    override val orderId: String,
    val assignment: String,
    val description: String,
    override val price: Double,
    override val creator: User,
    override val creationTime: LocalTime,
    override var status: OrderStatus
): Order(orderId, OrderCategory.EDUCATIONAL, price, creator, creationTime, status)

data class RideOrder(
    override val orderId: String,
    val timeOfDeparture: String,
    val direction: RideDirection,
    val placeOfMeetup: String,
    override val price: Double,
    override val creator: User,
    override val creationTime: LocalTime,
    override var status: OrderStatus
): Order(orderId, OrderCategory.RIDES, price, creator, creationTime, status)

