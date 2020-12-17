package com.example.govtech.data

import com.example.govtech.model.Transaction
import org.springframework.data.domain.Pageable
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Flux;

interface TransactionRepository : ReactiveCrudRepository<Transaction, Int> {

    fun findByIdNotNull(pageable: Pageable): Flux<Transaction>

    fun findByCountry(country: String): Flux<Transaction>

    @Query("select * from transaction where (:invoiceNo is null or invoice_no = :invoiceNo) and " +
            "(:stockCode is null or stock_code = :stockCode) and " +
            "(:description is null or description = :description) and " +
            "(:quantity is null or quantity = :quantity) and (:invoiceDate is null or invoice_date = :invoiceDate) and " +
            "(:unitPrice is null or unit_price = :unitPrice) and (:customerId is null or customer_id = :customerId) and " +
            "(:country is null or country = :country)")
    fun findByAllFields(invoiceNo: String?, stockCode: String?, description: String?, quantity: Int?,
                        invoiceDate: String?, unitPrice: Double?, customerId: Int?, country: String?): Flux<Transaction>
}
