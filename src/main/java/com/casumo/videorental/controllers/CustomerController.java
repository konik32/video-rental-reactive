package com.casumo.videorental.controllers;

import com.casumo.videorental.model.Customer;
import com.casumo.videorental.dto.Receipt;
import com.casumo.videorental.model.RentedMovie;
import com.casumo.videorental.repositories.CustomerRepository;
import com.casumo.videorental.services.BonusPointsService;
import com.casumo.videorental.services.RentalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Set;
import java.util.stream.Collectors;

import static com.casumo.videorental.controllers.Paths.*;

@RestController
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerRepository customerRepository;
    private final RentalService rentalService;
    private final BonusPointsService bonusPointsService;

    @GetMapping(path = CUSTOMER)
    public Mono<ResponseEntity<Customer>> get(@PathVariable("id") String id) {
        return customerRepository.findById(id)
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }

    @PostMapping(path = CUSTOMERS)
    public Mono<ResponseEntity<Customer>> create(@RequestBody @Valid Customer customer, UriComponentsBuilder builder) {
        return customerRepository.save(customer)
                .map(c -> ResponseEntity.created(builder.path(CUSTOMER).build(c.getName())).body(c));
    }

    @PostMapping(path = CUSTOMER_MOVIES)
    public Mono<Receipt> rent(@PathVariable("id") String id, @RequestBody @Valid @NotEmpty Set<RentedMovie> movies) {
        return rentalService.rent(id, movies);
    }

    @PutMapping(path = CUSTOMER_MOVIES_RETURN)
    public Mono<Receipt> returnMovies(@PathVariable("id") String id, @RequestBody @NotEmpty Set<String> movies) {
        return rentalService.returnMovies(id, movies).doOnSuccess(receipt -> bonusPointsService.applyBonusPoints(id, receipt.getMovies().stream()
                .map(Receipt.ReceiptItem::getMovieTitle)
                .collect(Collectors.toSet())).subscribe());
    }


}