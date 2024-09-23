package com.korealm.compApp.controllers

import com.korealm.compApp.dtos.ShoppingOrderDto
import com.korealm.compApp.models.ShoppingOrder
import com.korealm.compApp.services.orderServices.ShoppingOrderService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/orders/shopping")
class ShoppingOrderController(private val shoppingOrderService: ShoppingOrderService) {

    @GetMapping
    fun listAllOrders(): ResponseEntity<List<ShoppingOrder>> {
        return ResponseEntity.ok(shoppingOrderService.listAll())
    }

    @GetMapping("/{orderId}")
    fun getOrderDetails(@PathVariable orderId: String): ResponseEntity<ShoppingOrder?> {
        return ResponseEntity.ok(shoppingOrderService.getOrderDetails(orderId))
    }

    @PostMapping
    fun createOrder(@RequestBody orderDto: ShoppingOrderDto): ResponseEntity<ShoppingOrder?> {
        return ResponseEntity.ok(shoppingOrderService.newOrder(orderDto))
    }

    @PutMapping("/{orderId}")
    fun modifyOrder(@PathVariable orderId: String, @RequestBody orderDto: ShoppingOrderDto): ResponseEntity<Boolean> {
        return ResponseEntity.ok(shoppingOrderService.modifyOrder(orderDto))
    }

    @DeleteMapping("/{orderId}")
    fun cancelOrder(@PathVariable orderId: String, @RequestBody orderDto: ShoppingOrderDto): ResponseEntity<Boolean> {
        return ResponseEntity.ok(shoppingOrderService.cancelOrder(orderDto))
    }

    @PostMapping("/{orderId}/complete")
    fun completeOrder(@PathVariable orderId: String, @RequestBody orderDto: ShoppingOrderDto): ResponseEntity<Boolean> {
        return ResponseEntity.ok(shoppingOrderService.completeOrder(orderDto))
    }

    @PostMapping("/{orderId}/accept")
    fun acceptOrder(@PathVariable orderId: String, @RequestBody orderDto: ShoppingOrderDto): ResponseEntity<Boolean> {
        return ResponseEntity.ok(shoppingOrderService.acceptOrder(orderDto))
    }

    @PostMapping("/{orderId}/reject")
    fun rejectAcceptedOrder(@PathVariable orderId: String, @RequestBody orderDto: ShoppingOrderDto): ResponseEntity<Boolean> {
        return ResponseEntity.ok(shoppingOrderService.rejectAcceptedOrder(orderDto))
    }
}
