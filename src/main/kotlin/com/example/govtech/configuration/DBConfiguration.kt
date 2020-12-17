package com.example.govtech.configuration

import org.springframework.context.annotation.Configuration
import org.springframework.r2dbc.core.DatabaseClient

@Configuration
class DBConfiguration(db: DatabaseClient) {
    init {
        db.sql("create table TRANSACTION (id int auto_increment primary key, invoice_no varchar(255), stock_code varchar(255), description varchar(255), quantity int, invoice_date varchar(255), unit_price double, customer_id int, country varchar(255));")
                .then().subscribe()
    }
}
