package com.casumo.videorental.repositories;

import com.casumo.videorental.model.Basket;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface BasketRepository extends ReactiveCrudRepository<Basket, String> {
}
