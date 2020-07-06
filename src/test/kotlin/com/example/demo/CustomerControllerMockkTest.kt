package com.example.demo

import com.ninjasquad.springmockk.MockkBean
import io.mockk.clearAllMocks
import io.mockk.every
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest
@AutoConfigureMockMvc
class CustomerControllerMockkTest(@Autowired private val mockMvc: MockMvc) {
    @MockkBean(relaxed = true)
    private lateinit var customerClient: CustomerClient

    @BeforeEach
    fun setup() = clearAllMocks()

    @Test
    fun `customer can be retrieved`() {
        every { customerClient.findCustomer(any()) } returns Customer(id = "testid")

        mockMvc.perform(
            get("/foobar")
        ).andExpect(status().is2xxSuccessful)
            .andExpect(jsonPath("$.id").value("testid"))
    }
}
