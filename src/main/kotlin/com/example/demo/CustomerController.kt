package com.example.demo

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/{customerId}")
class CustomerController(
    val customerClient: CustomerClient
) {
    @GetMapping
    fun find(@PathVariable customerId: String) =
        customerClient.findCustomer(customerId)
}
