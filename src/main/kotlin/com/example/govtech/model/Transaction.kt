package com.example.govtech.model

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType

@Table
data class Transaction (
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Int?=0,
        val invoiceNo: String?,
        val stockCode: String?,
        val description: String?,
        val quantity: Int?,
        val invoiceDate: String?,
        val unitPrice: Double?,
        val customerId: Int?=0,
        val country: String?
)