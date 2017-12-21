package com.casumo.videorental.services;

import com.casumo.videorental.model.*;
import com.casumo.videorental.repositories.CustomerRepository;
import com.casumo.videorental.repositories.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BonusPointsService {

    private final CustomerRepository customerRepository;
    private final MovieRepository movieRepository;

    public Mono<Customer> applyBonusPoints(String customerId, Set<String> rentedMovies) {
        return customerRepository.findById(customerId)
                .zipWith(movieRepository.findAllById(rentedMovies)
                        .map(Movie::getMovieType)
                        .map(this::calculateBonusPoints)
                        .collect(Collectors.summingInt(Integer::intValue)))
                .flatMap(t -> applyBonusPoints(t.getT1(), t.getT2()));
    }

    private int calculateBonusPoints(MovieType movieType) {
        return movieType == MovieType.NEW ? 2 : 1;
    }

    private Mono<Customer> applyBonusPoints(Customer customer, Integer additionalPoints) {
        customer.setBonusPoints(customer.getBonusPoints() + additionalPoints);
        return customerRepository.save(customer);
    }
}
