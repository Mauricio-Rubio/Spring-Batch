package com.batch.sql.batchsql.repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.batch.sql.batchsql.entity.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {
    
}
