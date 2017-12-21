package com.casumo.videorental.repositories;

import com.casumo.videorental.model.Customer;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface CustomerRepository extends ReactiveCrudRepository<Customer, String>, CustomerRepositoryCustom {
}
