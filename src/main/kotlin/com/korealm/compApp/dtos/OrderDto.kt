package com.korealm.compApp.dtos

import com.korealm.compApp.models.enums.RideDirection
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.PositiveOrZero

/* DTOs are simplified versions of models, and always remember that they contain information that will be externally requested or sent.
*  No sensible data should be here, thus using uniqueId instead of studentId for every incoming or outgoing data.
*/

interface OrderDto {
    val creatorId: String
    val orderId: String
}

data class ShoppingOrderDto(
    @field:NotBlank(message = "There must be a user associated with every order")
    override val creatorId: String,

    override val orderId: String,

    @field:NotBlank(message = "Order ID cannot be blank")
    val productName: String,
    val description: String,

    @field:NotBlank(message = "Place of meeting cannot be blank")
    val placeOfMeeting: String,

    @field:NotBlank(message = "Price cannot be blank")
    @field:PositiveOrZero(message = "Price must be positive")
    @field:Max(1000, message = "Max price cannot be greater than $1000.00")
    val price: Double
): OrderDto

data class EducationalOrderDto(
    @field:NotBlank(message = "There must be a user associated with every order")
    override val creatorId: String,

    override val orderId: String,

    @field:NotBlank(message="Assigment must be specified, cannot be blank")
    val assignment: String,
    val description: String,

    @field:NotBlank(message = "Price cannot be blank")
    @field:PositiveOrZero(message = "Price must be positive")
    @field:Max(1000, message = "Max price cannot be greater than $1000.00")
    val price: Double
): OrderDto

data class RideOrderDto(
    @field:NotBlank(message = "There must be a user associated with every order")
    override val creatorId: String,

    override val orderId: String,

    @field:NotBlank(message = "Departure time cannot be blank")
    val timeOfDeparture: String,

    @field:NotBlank(message = "Direction must be specified, cannot be blank")
    val direction: RideDirection,

    @field:NotBlank(message="Place of meeting cannot be blank")
    val placeOfMeetup: String,

    @field:NotBlank(message = "Price cannot be blank")
    @field:PositiveOrZero(message = "Price must be positive")
    @field:Max(1000, message = "Max price cannot be greater than $1000.00")
    val price: Double
): OrderDto