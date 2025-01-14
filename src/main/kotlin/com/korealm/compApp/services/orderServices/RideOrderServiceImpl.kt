package com.korealm.compApp.services.orderServices


import com.korealm.compApp.models.User
import com.korealm.compApp.models.RideOrder
import com.korealm.compApp.dtos.RideOrderDto
import com.korealm.compApp.models.Order
import com.korealm.compApp.models.enums.OrderStatus
import com.korealm.compApp.models.exceptions.OrderNotFoundException
import com.korealm.compApp.repositories.HistoryOfOrdersPerUserRepository
import com.korealm.compApp.repositories.InMemoryRideOrderRepositoryImpl
import com.korealm.compApp.services.UserServiceImpl
import com.korealm.compApp.services.security.Authorization
import com.korealm.compApp.services.security.Encryption
import org.springframework.stereotype.Service
import java.time.LocalTime

@Service
class RideOrderServiceImpl(
    private val rideOrderRepository: InMemoryRideOrderRepositoryImpl,
    private val historyRepository: HistoryOfOrdersPerUserRepository,
    private val encryption: Encryption,
    private val authorization: Authorization,
    private val userService: UserServiceImpl
): RideOrderService {

    override fun listAll(): List<RideOrder> {
        return rideOrderRepository.findAll()
    }

    override fun getOrderDetails(orderId: String): RideOrder? {
        return rideOrderRepository.findByOrderId(orderId)
    }

    override fun newOrder(orderDto: RideOrderDto): RideOrder? {
        val creator: User = userService.getUserOrException(orderDto.creatorId)

        if (orderDto.orderId.isNotBlank()) throw IllegalArgumentException("No order ID should be handled for new orders.")

        val uniqueOrderId: String = encryption.generateSecureHash()
        val creationTime: LocalTime = LocalTime.now()

        return rideOrderRepository.save(
            RideOrder(
                uniqueOrderId,
                orderDto.timeOfDeparture,
                orderDto.direction,
                orderDto.placeOfMeetup,
                orderDto.price,
                creator,
                creationTime,
                OrderStatus.OFFERED
            )
        )
    }

    override fun modifyOrder(orderDto: RideOrderDto): Boolean {
        val order: RideOrder = authorization.confirmAuthorizedUser(orderDto, rideOrderRepository)

        if (order.status != OrderStatus.OFFERED) throw IllegalAccessException("Cannot modify orders that have been already accepted.")

        return rideOrderRepository.modifyOrder(order.orderId, order) != null
    }

    override fun cancelOrder(orderDto: RideOrderDto): Boolean {
        val order: RideOrder = authorization.confirmAuthorizedUser(orderDto, rideOrderRepository)
        return rideOrderRepository.cancelOrder(order) != null
    }

    override fun acceptOrder(orderDto: RideOrderDto): Boolean {
        if (orderDto.orderId.isBlank()) throw OrderNotFoundException()
        val order: RideOrder = rideOrderRepository.findByOrderId(orderDto.orderId) ?: throw OrderNotFoundException()
        val creator: User = userService.getUserOrException(orderDto.creatorId)

        if (order.creator == creator) throw IllegalStateException("The same user cannot accept its own orders")

        rideOrderRepository.removeOrder(order)
        historyRepository.addOrderToHistoryOfOrders(creator, order)

        return true
    }

    override fun completeOrder(orderDto: RideOrderDto): Boolean {
        val order: RideOrder = authorization.confirmAuthorizedUser(orderDto, rideOrderRepository)

        val user: User = userService.getUserOrException(orderDto.creatorId)

        historyRepository.alterOrderFromHistoryOfOrders(user, order.orderId, OrderStatus.COMPLETED)
        return true
    }

    override fun rejectAcceptedOrder(orderDto: RideOrderDto): Boolean {
        if (orderDto.orderId.isBlank()) throw OrderNotFoundException()

        val creator: User = userService.getUserOrException(orderDto.creatorId)
        val order: Order = historyRepository.searchOrderInHistory(creator, orderDto.orderId)
            ?: throw OrderNotFoundException("Order with ID ${orderDto.orderId} not found in the user's history.")

        // Ensure the order is of type EducationalOrder, if not then I cannot save it back to the offered orders array
        if (order !is RideOrder) {
            throw IllegalStateException("The order is not of type EducationalOrder.")
        }

        // Check if the order is in ACCEPTED status, because if not then it doesn't make sense to reject it... because it was never accepted before
        if (order.status != OrderStatus.ACCEPTED) {
            throw IllegalStateException("Order is not in ACCEPTED status, so it cannot be rejected.")
        }

        historyRepository.alterOrderFromHistoryOfOrders(creator, order.orderId, OrderStatus.CANCELLED)

        order.status = OrderStatus.OFFERED
        rideOrderRepository.save(order)

        return true
    }
}