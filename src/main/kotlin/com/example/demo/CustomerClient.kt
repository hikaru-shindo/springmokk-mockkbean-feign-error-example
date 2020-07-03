package com.example.demo

import com.netflix.hystrix.exception.HystrixBadRequestException
import feign.*
import feign.codec.ErrorDecoder
import feign.hystrix.FallbackFactory
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.context.annotation.Bean
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component

@FeignClient(
    name = "customer",
    url = "http://localhost:1337", // will error out!
    configuration = [CustomerConfig::class],
    fallbackFactory = CustomerFallbackFactory::class
)
interface CustomerClient {
    @RequestLine("GET /{id}")
    fun findCustomer(@Param("id") id: String): Customer
}

data class Customer(val id: String)

class CustomerErrorDecoder : ErrorDecoder {
    override fun decode(methodKey: String?, response: Response?): Exception {
        return response?.status().let { status ->
            when (status) {
                HttpStatus.NOT_FOUND.value() -> CustomerNotFoundException()
                HttpStatus.UNAUTHORIZED.value() -> RequestUnauthorizedException()
                else -> ErrorDecoder.Default().decode(methodKey, response)
            }
        }
    }
}

class CustomerNotFoundException: HystrixBadRequestException("customer not found")
class RequestUnauthorizedException: HystrixBadRequestException("requestor is not authorized to execute request")

class CustomerConfig {

    @Bean
    fun errorDecoder(): ErrorDecoder = CustomerErrorDecoder()

    @Bean
    fun feignLoggerLevel(): Logger.Level {
        return Logger.Level.FULL
    }

    @Bean
    fun feignContract(): Contract {
        return Contract.Default()
    }
}
@Component
class CustomerFallbackFactory : FallbackFactory<CustomerClient> {
    override fun create(cause: Throwable?): CustomerClient = object : CustomerClient {
        override fun findCustomer(id: String): Customer = Customer(
            id = "fallback_customer"
        )
    }

}
