package com.example.govtech.service

import com.example.govtech.data.TransactionRepository
import com.example.govtech.model.Transaction
import org.apache.commons.csv.CSVRecord
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import java.lang.NumberFormatException

@Service
class TransactionService(val repository: TransactionRepository){


    fun saveAll(csvRecordList: List<CSVRecord>): Flux<Transaction> {
        var transactionList = mutableListOf<Transaction>()
        for (csvRecord in csvRecordList) {
            var quantity: Int
            var unitPrice: Double
            var customerId: Int?
            try {
                quantity = csvRecord.get("Quantity").toInt()
            } catch (nfe: NumberFormatException) {
                throw NumberFormatException("Unable to derive quantity from: "+csvRecord.get("Quantity"))
            }
            try {
                unitPrice = csvRecord.get("UnitPrice").toDouble()
            } catch (nfe: NumberFormatException) {
                throw NumberFormatException("Unable to derive unit price from: "+csvRecord.get("UnitPrice"))
            }
            try {
                if (csvRecord.get("CustomerID").isNullOrEmpty()) {
                    customerId = null
                } else {
                }
                customerId = csvRecord.get("CustomerID").toInt()
            } catch (nfe: NumberFormatException) {
                throw NumberFormatException("Unable to derive customer id from: "+csvRecord.get("CustomerID"))
            }

            var transaction = Transaction(
                    null,
                    csvRecord.get("InvoiceNo"),
                    csvRecord.get("StockCode"),
                    csvRecord.get("Description"),
                    quantity,
                    csvRecord.get("InvoiceDate"),
                    unitPrice,
                    if(csvRecord.get("CustomerID").isNullOrEmpty()) 0 else csvRecord.get("CustomerID").toInt(),
                    csvRecord.get("Country")
            )
            transactionList.add(transaction)
        }

        val saveAll = repository.saveAll(transactionList)
        return saveAll
    }

    fun getAll(pageable: Pageable): Flux<Transaction> {
        return repository.findByIdNotNull(pageable)
    }

    fun getByParameter(invoiceNo: String?, stockCode: String?, description: String?, quantity: Int?, invoiceDate: String?, unitPrice: Double?, customerId: Int?, country: String?): Flux<Transaction> {
        return repository.findByAllFields(invoiceNo, stockCode, description, quantity, invoiceDate, unitPrice, customerId, country)
    }


}