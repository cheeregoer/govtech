package com.example.govtech.controller

import com.example.govtech.model.Transaction
import com.example.govtech.service.TransactionService
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVParser
import org.apache.commons.csv.CSVRecord
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

@RestController
class TransactionController(@Autowired val service: TransactionService) {

    @PostMapping("import")
    fun importData(@RequestParam("file") file: MultipartFile): ResponseEntity<Mono<Transaction>> {

        var records: List<CSVRecord>
        try {
            var bufferedReader: BufferedReader = BufferedReader(InputStreamReader(file.inputStream))
            var csvParser: CSVParser = CSVParser(bufferedReader, CSVFormat.EXCEL.withHeader())
            records = csvParser.records
            if (records.isEmpty())
                return ResponseEntity(Mono.empty(), HttpStatus.NO_CONTENT)

            service.saveAll(records).blockLast()

        } catch (ioException: IOException) {
            return ResponseEntity(Mono.empty(), HttpStatus.INTERNAL_SERVER_ERROR)
//            throw RestClientResponseException("Something wrong with file.", HttpStatus.BAD_REQUEST.value(), "", null, null, null)
        } catch (numberFormatException: NumberFormatException) {
//            throw ResponseStatusException(HttpStatus.BAD_REQUEST, numberFormatException.message, numberFormatException)
            return ResponseEntity(Mono.empty(), HttpStatus.BAD_REQUEST)
        }

        return ResponseEntity.ok(Mono.empty())
    }

    @GetMapping("/all")
    fun all(@RequestParam("page") page: Int, @RequestParam("size") size: Int): ResponseEntity<Flux<Transaction>> {
        val pageable: Pageable = PageRequest.of(page, size)
        val all = service.getAll(pageable)
        return ResponseEntity.ok(all)
    }

    @GetMapping("search")
    fun getByAllField(
            @RequestParam(required = false) invoiceNo: String?,
            @RequestParam(required = false) stockCode: String?,
            @RequestParam(required = false) description: String?,
            @RequestParam(required = false) quantity: Int?,
            @RequestParam(required = false) invoiceDate: String?,
            @RequestParam(required = false) unitPrice: Double?,
            @RequestParam(required = false) customerId: Int?,
            @RequestParam(required = false) country: String?,
    ): ResponseEntity<Flux<Transaction>> {
        return ResponseEntity.ok(service.getByParameter(invoiceNo, stockCode, description, quantity, invoiceDate, unitPrice, customerId, country))
    }






}