package com.example.govtech

import com.example.govtech.model.Transaction
import com.example.govtech.service.TransactionService
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.hamcrest.Matchers.`is`
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.data.domain.PageRequest
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.mock.web.MockMultipartFile
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import reactor.core.publisher.Flux
import java.io.File
import java.lang.NumberFormatException

@WebMvcTest
class ControllerTest(@Autowired val mockMvc: MockMvc) {

    @MockkBean
    private lateinit var transactionService: TransactionService

    private val transaction_1: Transaction = Transaction(1, "123ABC", "Pineapple", "This is a Pineapple", 4, "2020/01/01", 3.2, 1, "Singapore")
    private val transaction_2: Transaction = Transaction(2, "123ABC", "Apple", "This is a Apple", 7, "2020/01/01", 1.4, 1, "Singapore")
    private val transaction_3: Transaction = Transaction(3, "123ABC", "Pen", "This is a Pen", 10, "2020/01/01", 2.0, 1, "Singapore")
    private val transaction_4: Transaction = Transaction(4, null, "Pen Pineapple Apple Pen", "Pen Pineapple Apple Pen ~", 1, "2099/12/12", 999.0, 2, "Singapore")

    @Test
    fun getOneData() {
        every { transactionService.getAll(PageRequest.of(0,1)) } returns Flux.just(transaction_1)
        val mvcResult = mockMvc.perform(get("/all?page=0&size=1")).andExpect(request().asyncStarted())
                .andReturn()

        mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isOk)
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id", `is`(1)))
                .andExpect(jsonPath("$.length()", `is`(1)))
    }

    @Test
    fun getAll_NoData() {
        every { transactionService.getAll(PageRequest.of(0,1)) } returns Flux.empty()
        val mvcResult = mockMvc.perform(get("/all?page=0&size=1")).andExpect(request().asyncStarted())
                .andReturn()
        mockMvc.perform(asyncDispatch(mvcResult)).andExpect(status().isOk).andExpect(content().string("[]"))
    }

    @Test
    fun uploadTest() {

        every { transactionService.saveAll(any()) } returns Flux.just(transaction_1, transaction_2, transaction_3, transaction_4)

        val mockMultipartFile = MockMultipartFile("file", "data.csv", MediaType.MULTIPART_FORM_DATA_VALUE,
                File("src/test/resources/data.csv").readBytes())
        val builder: MockHttpServletRequestBuilder  = MockMvcRequestBuilders.multipart("/import").file(mockMultipartFile)

        mockMvc.perform(builder).andExpect(status().isOk)
    }


    @Test
    fun uploadTest_fail_additionalfield() {
        val mockMultipartFile = MockMultipartFile("file", "data3.csv", MediaType.MULTIPART_FORM_DATA_VALUE,
                File("src/test/resources/data.csv").readBytes())
        val builder: MockHttpServletRequestBuilder  = MockMvcRequestBuilders.multipart("/import").file(mockMultipartFile)

        every { transactionService.saveAll(any()) } throws NumberFormatException("Unable to derive quantity from: PPAP")
        mockMvc.perform(builder).andExpect(status().`is`(HttpStatus.BAD_REQUEST.value()))

    }

}