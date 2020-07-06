package com.example.demo

import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status


@SpringBootTest
@AutoConfigureMockMvc
class CustomerControllerMockitoTest(@Autowired private val mockMvc: MockMvc) {
    @MockBean
    private lateinit var customerClient: CustomerClient

    @Test
    fun `customer can be retrieved`() {
        `when`(customerClient.findCustomer("foobar")).thenReturn(Customer(id = "testid"))

        mockMvc.perform(
            get("/foobar")
        ).andExpect(status().is2xxSuccessful)
            .andExpect(jsonPath("$.id").value("testid"))
    }
}
