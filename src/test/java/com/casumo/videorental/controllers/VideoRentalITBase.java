package com.casumo.videorental.controllers;

import com.casumo.videorental.model.Customer;
import com.casumo.videorental.model.Movie;
import com.casumo.videorental.model.MovieType;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

import static com.casumo.videorental.controllers.Paths.CUSTOMERS;
import static com.casumo.videorental.controllers.Paths.MOVIE;
import static com.casumo.videorental.controllers.Paths.MOVIES;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
public abstract class VideoRentalITBase {

    protected final static BigDecimal BASIC_PRICE = new BigDecimal("30");
    protected final static BigDecimal PREMIUM_PRICE = new BigDecimal("40");

    protected final static Movie AVATAR = new Movie("Avatar", MovieType.NEW);
    protected final static Movie PULP_FICTION = new Movie("Pulp Fiction", MovieType.REGULAR);
    protected final static Movie STAR_WARS = new Movie("Star Wars", MovieType.OLD);

    protected final static Customer JOHN = new Customer("John");


    @Autowired
    protected WebTestClient client;


    protected String postMovie(Movie movie) {
        String title = client.post()
                .uri(MOVIES)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(movie), Movie.class)
                .exchange()
                .expectBody(Movie.class)
                .returnResult()
                .getResponseBody()
                .getTitle();
        assertThat(title).isNotBlank();
        return title;
    }

    protected String postCustomer(Customer customer) {
        String name = client.post()
                .uri(CUSTOMERS)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(customer), Customer.class)
                .exchange()
                .expectBody(Customer.class)
                .returnResult()
                .getResponseBody()
                .getName();
        assertThat(name).isNotBlank();
        return name;
    }


}
