package com.batch.sql.partition.batchsqlpartition.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.batch.sql.partition.batchsqlpartition.entity.Customer;

public interface CustomerRepository  extends JpaRepository<Customer,Integer> {
}