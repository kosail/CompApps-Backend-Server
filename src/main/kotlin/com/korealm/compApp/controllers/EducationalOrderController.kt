package com.korealm.compApp.controllers

import com.korealm.compApp.dtos.EducationalOrderDto
import com.korealm.compApp.models.EducationalOrder
import com.korealm.compApp.services.orderServices.EducationalOrderServiceImpl
import org.springframework.web.bind.annotation.*
import org.springframework.http.ResponseEntity

@RestController
@RequestMapping("/api/orders/educational")
class EducationalOrderController(private val educationalOrderService: EducationalOrderServiceImpl) {

    @GetMapping
    fun listAllOrders(): ResponseEntity<List<EducationalOrder>> {
        return ResponseEntity.ok(educationalOrderService.listAll())
    }

    @GetMapping("/{orderId}")
    fun getOrderDetails(@PathVariable orderId: String): ResponseEntity<EducationalOrder?> {
        return ResponseEntity.ok(educationalOrderService.getOrderDetails(orderId))
    }

    @PostMapping
    fun createOrder(@RequestBody orderDto: EducationalOrderDto): ResponseEntity<EducationalOrder?> {
        return ResponseEntity.ok(educationalOrderService.newOrder(orderDto))
    }

    @PutMapping("/modify")
    fun modifyOrder(@RequestBody orderDto: EducationalOrderDto): ResponseEntity<Boolean> {
        return ResponseEntity.ok(educationalOrderService.modifyOrder(orderDto))
    }

    @DeleteMapping("/cancel")
    fun cancelOrder(@RequestBody orderDto: EducationalOrderDto): ResponseEntity<Boolean> {
        return ResponseEntity.ok(educationalOrderService.cancelOrder(orderDto))
    }

    @PostMapping("/complete")
    fun completeOrder(@RequestBody orderDto: EducationalOrderDto): ResponseEntity<Boolean> {
        return ResponseEntity.ok(educationalOrderService.completeOrder(orderDto))
    }

    @PostMapping("/accept")
    fun acceptOrder(@RequestBody orderDto: EducationalOrderDto): ResponseEntity<Boolean> {
        return ResponseEntity.ok(educationalOrderService.acceptOrder(orderDto))
    }

    @PostMapping("/reject")
    fun rejectAcceptedOrder(@RequestBody orderDto: EducationalOrderDto): ResponseEntity<Boolean> {
        return ResponseEntity.ok(educationalOrderService.rejectAcceptedOrder(orderDto))
    }
}
