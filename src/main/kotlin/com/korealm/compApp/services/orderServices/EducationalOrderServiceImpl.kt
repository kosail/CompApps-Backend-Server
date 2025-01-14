package com.korealm.compApp.services.orderServices

import com.korealm.compApp.dtos.EducationalOrderDto
import com.korealm.compApp.models.EducationalOrder
import com.korealm.compApp.models.Order
import com.korealm.compApp.models.User
import com.korealm.compApp.models.enums.OrderStatus
import com.korealm.compApp.models.exceptions.OrderNotFoundException
import com.korealm.compApp.repositories.HistoryOfOrdersPerUserRepository
import com.korealm.compApp.repositories.InMemoryEducationalOrderRepositoryImpl
import com.korealm.compApp.services.UserServiceImpl
import com.korealm.compApp.services.security.Authorization
import com.korealm.compApp.services.security.Encryption
import org.springframework.stereotype.Service
import java.time.LocalTime

@Service
class EducationalOrderServiceImpl(
    private val educationalOrderRepository: InMemoryEducationalOrderRepositoryImpl,
    private val historyRepository: HistoryOfOrdersPerUserRepository,
    private val encryption: Encryption,
    private val authorization: Authorization,
    private val userService: UserServiceImpl
): EducationalOrderService {

    override fun listAll(): List<EducationalOrder> {
        return educationalOrderRepository.findAll()
    }

    override fun getOrderDetails(orderId: String): EducationalOrder? {
        return educationalOrderRepository.findByOrderId(orderId)
    }

    override fun newOrder(orderDto: EducationalOrderDto): EducationalOrder? {
        val creator: User = userService.getUserOrException(orderDto.creatorId)

        // orderId should be blank to create new orders. It doesn't have sense to receive an existing orderId. I know that this is not the smartest approach, but is the one that I thought it could work.
        if (orderDto.orderId.isNotBlank()) throw IllegalArgumentException("No order ID should be handled for new orders.")

        val uniqueOrderId: String = encryption.generateSecureHash()
        val creationTime: LocalTime = LocalTime.now()

        return educationalOrderRepository.save(
            EducationalOrder(
                uniqueOrderId,
                orderDto.assignment,
                orderDto.description,
                orderDto.price,
                creator,
                creationTime,
                OrderStatus.OFFERED
            )
        )
    }

    override fun modifyOrder(orderDto: EducationalOrderDto): Boolean {
        val order: EducationalOrder = authorization.confirmAuthorizedUser(orderDto, educationalOrderRepository)

        if (order.status != OrderStatus.OFFERED) throw IllegalAccessException("Cannot modify orders that have been already accepted.")

        return educationalOrderRepository.modifyOrder(order.orderId, order) != null
    }

    override fun cancelOrder(orderDto: EducationalOrderDto): Boolean {
        val order: EducationalOrder = authorization.confirmAuthorizedUser(orderDto, educationalOrderRepository)
        return educationalOrderRepository.cancelOrder(order) != null
    }

    override fun acceptOrder(orderDto: EducationalOrderDto): Boolean {
        if (orderDto.orderId.isBlank()) throw OrderNotFoundException("OrderId field is blank. It cannot be blank since you are accepting an order.")
        val order: EducationalOrder = educationalOrderRepository.findByOrderId(orderDto.orderId) ?: throw OrderNotFoundException()
        val creator: User = userService.getUserOrException(orderDto.creatorId)

        if (order.creator == creator) throw IllegalStateException("The same user cannot accept its own orders")

        educationalOrderRepository.removeOrder(order)

        order.status = OrderStatus.ACCEPTED
        historyRepository.addOrderToHistoryOfOrders(creator, order)

        return true
    }

    override fun completeOrder(orderDto: EducationalOrderDto): Boolean {
        val order: EducationalOrder = authorization.confirmAuthorizedUser(orderDto, educationalOrderRepository)
        val user: User = userService.getUserOrException(orderDto.creatorId)

        historyRepository.alterOrderFromHistoryOfOrders(user, order.orderId, OrderStatus.COMPLETED)
        return true
    }

    override fun rejectAcceptedOrder(orderDto: EducationalOrderDto): Boolean {
        if (orderDto.orderId.isBlank()) throw OrderNotFoundException()

        val creator: User = userService.getUserOrException(orderDto.creatorId)
        val order: Order = historyRepository.searchOrderInHistory(creator, orderDto.orderId)
            ?: throw OrderNotFoundException("Order with ID ${orderDto.orderId} not found in the user's history.")

        // Ensure the order is of type EducationalOrder, if not then I cannot save it back to the offered orders array
        if (order !is EducationalOrder) {
            throw IllegalStateException("The order is not of type EducationalOrder.")
        }

        // Check if the order is in ACCEPTED status, because if not then it doesn't make sense to reject it... because it was never accepted before
        if (order.status != OrderStatus.ACCEPTED) {
            throw IllegalStateException("Order is not in ACCEPTED status, so it cannot be rejected.")
        }

        historyRepository.alterOrderFromHistoryOfOrders(creator, order.orderId, OrderStatus.CANCELLED)

        order.status = OrderStatus.OFFERED
        educationalOrderRepository.save(order)

        return true // at this point, everything should have gone well
    }

}