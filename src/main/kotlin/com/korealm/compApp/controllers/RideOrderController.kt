package com.korealm.compApp.controllers

import com.korealm.compApp.dtos.RideOrderDto
import com.korealm.compApp.models.RideOrder
import com.korealm.compApp.services.orderServices.RideOrderService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/orders/ride")
class RideOrderController(private val rideOrderService: RideOrderService) {

    @GetMapping
    fun listAllOrders(): ResponseEntity<List<RideOrder>> {
        return ResponseEntity.ok(rideOrderService.listAll())
    }

    @GetMapping("/{orderId}")
    fun getOrderDetails(@PathVariable orderId: String): ResponseEntity<RideOrder?> {
        return ResponseEntity.ok(rideOrderService.getOrderDetails(orderId))
    }

    @PostMapping
    fun createOrder(@RequestBody orderDto: RideOrderDto): ResponseEntity<RideOrder?> {
        return ResponseEntity.ok(rideOrderService.newOrder(orderDto))
    }

    @PutMapping("/{orderId}")
    fun modifyOrder(@PathVariable orderId: String, @RequestBody orderDto: RideOrderDto): ResponseEntity<Boolean> {
        return ResponseEntity.ok(rideOrderService.modifyOrder(orderDto))
    }

    @DeleteMapping("/{orderId}")
    fun cancelOrder(@PathVariable orderId: String, @RequestBody orderDto: RideOrderDto): ResponseEntity<Boolean> {
        return ResponseEntity.ok(rideOrderService.cancelOrder(orderDto))
    }

    @PostMapping("/{orderId}/complete")
    fun completeOrder(@PathVariable orderId: String, @RequestBody orderDto: RideOrderDto): ResponseEntity<Boolean> {
        return ResponseEntity.ok(rideOrderService.completeOrder(orderDto))
    }

    @PostMapping("/{orderId}/accept")
    fun acceptOrder(@PathVariable orderId: String, @RequestBody orderDto: RideOrderDto): ResponseEntity<Boolean> {
        return ResponseEntity.ok(rideOrderService.acceptOrder(orderDto))
    }

    @PostMapping("/{orderId}/reject")
    fun rejectAcceptedOrder(@PathVariable orderId: String, @RequestBody orderDto: RideOrderDto): ResponseEntity<Boolean> {
        return ResponseEntity.ok(rideOrderService.rejectAcceptedOrder(orderDto))
    }
}
