package com.batch.sql.partition.batchsqlpartition.config;

import org.springframework.batch.item.ItemProcessor;

import com.batch.sql.partition.batchsqlpartition.entity.Customer;

public class CustomerProcessor implements ItemProcessor<Customer,Customer> {

    @Override
    public Customer process(Customer customer) throws Exception {
            return customer;
    }
}
