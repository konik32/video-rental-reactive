package com.casumo.videorental.repositories;

import com.casumo.videorental.model.RentedMovie;
import com.mongodb.client.result.UpdateResult;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collection;

public interface CustomerRepositoryCustom {
    Flux<RentedMovie> findCustomersRentedMovies(String customerId, Collection<String> movies);

    Mono<UpdateResult> pushRentedMovies(String customerId, Collection<RentedMovie> movies);

    Mono<UpdateResult> pullRentedMovies(String customerId, Collection<String> movies);
}
