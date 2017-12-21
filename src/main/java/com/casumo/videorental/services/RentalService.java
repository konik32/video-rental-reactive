package com.casumo.videorental.services;

import com.casumo.videorental.dto.Receipt;
import com.casumo.videorental.model.*;
import com.casumo.videorental.dto.Receipt.ReceiptItem;
import com.casumo.videorental.repositories.CustomerRepository;
import com.casumo.videorental.repositories.MovieRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class RentalService {

    private final MovieRepository movieRepository;
    private final CustomerRepository customerRepository;
    private final List<MoviePriceCalculator> moviePriceCalculators;

    public Mono<Receipt> rent(String customerId, Collection<RentedMovie> movies) {
        Map<String, RentedMovie> rentedMoviesMap = movies.stream().collect(Collectors.toMap(RentedMovie::getTitle, m -> m));
        return movieRepository.findAllById(rentedMoviesMap.keySet())
                .map(movie -> match(movie, rentedMoviesMap))
                .map(item -> mapToReceiptItem(item, item.getFrom(), item.getTo()))
                .collect(Collectors.toSet())
                .map(Receipt::new)
                .doOnSuccess(bigDecimal -> customerRepository.pushRentedMovies(customerId, movies).subscribe());
    }

    public Mono<Receipt> returnMovies(String customerId, Collection<String> movies) {
        return customerRepository.findCustomersRentedMovies(customerId, movies)
                .distinct()
                .collect(Collectors.toMap(RentedMovie::getTitle, m -> m))
                .flatMap(rentedMoviesMap -> movieRepository.findAllById(rentedMoviesMap.keySet())
                        .map(movie -> match(movie, rentedMoviesMap))
                        .filter(orderItem -> orderItem.getTo().isBefore(LocalDate.now()))
                        .map(item -> mapToReceiptItem(item, item.getTo(), LocalDate.now()))
                        .collect(Collectors.toSet())
                        .map(Receipt::new))
                .doOnSuccess(bigDecimal -> customerRepository.pullRentedMovies(customerId, movies).subscribe());

    }

    private ReceiptItem mapToReceiptItem(OrderItem item, LocalDate rentFrom, LocalDate rentTo) {
        return moviePriceCalculators.stream()
                .filter(c -> c.supports(item.getMovie()))
                .findFirst()
                .map(c -> c.calculatePrice(item.getMovie(), rentFrom, rentTo))
                .map(price -> new ReceiptItem(item.getMovie().getTitle(), price))
                .orElseThrow(() -> new IllegalStateException("Movie " + item.getMovie() + " not supported"));
    }

    private OrderItem match(Movie movie, Map<String, RentedMovie> rentedMovieMap) {
        RentedMovie rentedMovie = rentedMovieMap.get(movie.getTitle());
        return new OrderItem(movie, rentedMovie.getRentedFrom(), rentedMovie.getRentedTo());
    }


    @AllArgsConstructor
    @Data
    public static class OrderItem {

        private Movie movie;
        private LocalDate from;
        private LocalDate to;
    }
}
