package com.korealm.compApp.services.orderServices

import com.korealm.compApp.dtos.ShoppingOrderDto
import com.korealm.compApp.models.EducationalOrder
import com.korealm.compApp.models.Order
import com.korealm.compApp.models.ShoppingOrder
import com.korealm.compApp.models.User
import com.korealm.compApp.models.enums.OrderStatus
import com.korealm.compApp.models.exceptions.OrderNotFoundException
import com.korealm.compApp.repositories.HistoryOfOrdersPerUserRepository
import com.korealm.compApp.repositories.InMemoryShoppingOrderRepositoryImpl
import com.korealm.compApp.services.UserServiceImpl
import com.korealm.compApp.services.security.Authorization
import com.korealm.compApp.services.security.Encryption
import org.springframework.stereotype.Service
import java.time.LocalTime

@Service
class ShoppingOrderServiceImpl(
    private val shoppingOrderRepository: InMemoryShoppingOrderRepositoryImpl,
    private val historyRepository: HistoryOfOrdersPerUserRepository,
    private val encryption: Encryption,
    private val authorization: Authorization,
    private val userService: UserServiceImpl
): ShoppingOrderService {

    override fun listAll(): List<ShoppingOrder> {
        return shoppingOrderRepository.findAll()
    }

    override fun getOrderDetails(orderId: String): ShoppingOrder? {
        return shoppingOrderRepository.findByOrderId(orderId)
    }

    override fun newOrder(orderDto: ShoppingOrderDto): ShoppingOrder? {
        val creator: User = userService.getUserOrException(orderDto.creatorId)

        // Ensure that a new order does not have an existing order ID
        if (orderDto.orderId.isNotBlank()) throw IllegalArgumentException("No order ID should be handled for new orders.")

        val uniqueOrderId: String = encryption.generateSecureHash()
        val creationTime: LocalTime = LocalTime.now()

        return shoppingOrderRepository.save(
            ShoppingOrder(
                uniqueOrderId,
                orderDto.productName,
                orderDto.description,
                orderDto.placeOfMeeting,
                orderDto.price,
                creator,
                creationTime,
                OrderStatus.OFFERED
            )
        )
    }

    override fun modifyOrder(orderDto: ShoppingOrderDto): Boolean {
        val order: ShoppingOrder = authorization.confirmAuthorizedUser(orderDto, shoppingOrderRepository)

        // Only modify orders that are still in OFFERED status
        if (order.status != OrderStatus.OFFERED) throw IllegalAccessException("Cannot modify orders that have been already accepted.")

        return shoppingOrderRepository.modifyOrder(order.orderId, order) != null
    }

    override fun cancelOrder(orderDto: ShoppingOrderDto): Boolean {
        val order: ShoppingOrder = authorization.confirmAuthorizedUser(orderDto, shoppingOrderRepository)
        return shoppingOrderRepository.cancelOrder(order) != null
    }

    override fun acceptOrder(orderDto: ShoppingOrderDto): Boolean {
        if (orderDto.orderId.isBlank()) throw OrderNotFoundException("OrderId field is blank. It cannot be blank since you are accepting an order.")

        val order: ShoppingOrder = shoppingOrderRepository.findByOrderId(orderDto.orderId) ?: throw OrderNotFoundException()
        val creator: User = userService.getUserOrException(orderDto.creatorId)

        if (order.creator == creator) throw IllegalStateException("The same user cannot accept its own orders")

        shoppingOrderRepository.removeOrder(order)

        order.status = OrderStatus.ACCEPTED
        historyRepository.addOrderToHistoryOfOrders(creator, order)

        return true
    }

    override fun completeOrder(orderDto: ShoppingOrderDto): Boolean {
        val order: ShoppingOrder = authorization.confirmAuthorizedUser(orderDto, shoppingOrderRepository)
        val user: User = userService.getUserOrException(orderDto.creatorId)

        historyRepository.alterOrderFromHistoryOfOrders(user, order.orderId, OrderStatus.COMPLETED)
        return true
    }

    override fun rejectAcceptedOrder(orderDto: ShoppingOrderDto): Boolean {
        if (orderDto.orderId.isBlank()) throw OrderNotFoundException()

        val creator: User = userService.getUserOrException(orderDto.creatorId)
        val order: Order = historyRepository.searchOrderInHistory(creator, orderDto.orderId)
            ?: throw OrderNotFoundException("Order with ID ${orderDto.orderId} not found in the user's history.")

            if (order !is ShoppingOrder) {
            throw IllegalStateException("The order is not of type ShoppingOrder.")
        }

        // Ensure the order is in ACCEPTED status before rejecting it
        if (order.status != OrderStatus.ACCEPTED) {
            throw IllegalStateException("Order is not in ACCEPTED status, so it cannot be rejected.")
        }

        historyRepository.alterOrderFromHistoryOfOrders(creator, order.orderId, OrderStatus.CANCELLED)

        order.status = OrderStatus.OFFERED
        shoppingOrderRepository.save(order)

        return true
    }
}
