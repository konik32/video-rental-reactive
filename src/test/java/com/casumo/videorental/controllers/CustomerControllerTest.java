package com.casumo.videorental.controllers;

import com.casumo.videorental.dto.Receipt;
import com.casumo.videorental.dto.Receipt.ReceiptItem;
import com.casumo.videorental.model.Customer;
import com.casumo.videorental.model.Movie;
import com.casumo.videorental.model.MovieType;
import com.casumo.videorental.model.RentedMovie;
import com.casumo.videorental.repositories.CustomerRepository;
import com.casumo.videorental.repositories.MovieRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static com.casumo.videorental.controllers.Paths.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

public class CustomerControllerTest extends VideoRentalITBase {

    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private MovieRepository movieRepository;

    @Before
    public void setUp() throws Exception {
        customerRepository.deleteAll().block();
        movieRepository.deleteAll().block();
    }

    @Test
    public void shouldCreateCustomerAndGetExisting() throws Exception {
        EntityExchangeResult<Customer> responseResult = client.post().uri(CUSTOMERS)
                .body(Mono.just(JOHN), Customer.class)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(Customer.class)
                .returnResult();
        Customer result = responseResult.getResponseBody();
        assertThat(responseResult.getResponseHeaders().getLocation()).isNotNull();
        assertThat(result.getName()).isEqualTo(JOHN.getName());
    }

    @Test
    public void shouldGetExistingCustomer() throws Exception {
        String id = postCustomer(JOHN);
        client.get().uri(CUSTOMER, id).accept(MediaType.APPLICATION_JSON).exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("name", equalTo(JOHN.getName()));
    }

    @Test
    public void shouldReturn404OnMissingCustomer() throws Exception {
        client.get().uri(CUSTOMER, "missingId").accept(MediaType.APPLICATION_JSON).exchange()
                .expectStatus().isNotFound();
    }

    @Test
    public void shouldRentMoviesAndReturnReceipt() throws Exception {
        //given
        String customerId = postCustomer(JOHN);
        //when
        Receipt result = client.post()
                .uri(CUSTOMER_MOVIES, customerId)
                .body(Mono.just(prepareRentedMovies()), List.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Receipt.class)
                .returnResult()
                .getResponseBody();
        assertThat(result.getTotal()).isEqualTo(BigDecimal.valueOf(250));

        assertThat(result.getMovies()).containsExactlyInAnyOrder(
                new ReceiptItem("Avatar", BigDecimal.valueOf(40)),
                new ReceiptItem("Pulp Fiction", BigDecimal.valueOf(90)),
                new ReceiptItem("Spider Man", BigDecimal.valueOf(30)),
                new ReceiptItem("Star Wars", BigDecimal.valueOf(90))
        );
    }

    @Test
    public void shouldReturnRentedMoviesCalculateReceiptAndAddBonusPoints() throws Exception {
        //given
        String customerId = postCustomer(JOHN);
        client.post()
                .uri(CUSTOMER_MOVIES, customerId)
                .body(Mono.just(prepareRentedMovies()), List.class)
                .exchange()
                .expectBody(Receipt.class)
                .returnResult()
                .getResponseBody();
        //when
        Receipt result = client.put()
                .uri(CUSTOMER_MOVIES_RETURN, customerId)
                .body(Mono.just(Arrays.asList("Avatar", "Pulp Fiction")), List.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Receipt.class)
                .returnResult()
                .getResponseBody();
        assertThat(result.getTotal()).isEqualTo(BigDecimal.valueOf(110));

        assertThat(result.getMovies()).containsExactlyInAnyOrder(
                new ReceiptItem("Avatar", BigDecimal.valueOf(80)),
                new ReceiptItem("Pulp Fiction", BigDecimal.valueOf(30))
        );

        Customer customer = client.get().uri(CUSTOMER, customerId).exchange()
                .expectBody(Customer.class)
                .returnResult()
                .getResponseBody();
        assertThat(customer.getBonusPoints()).isEqualTo(3);
        assertThat(customer.getRentedMovies()).extracting(RentedMovie::getTitle).containsExactlyInAnyOrder("Spider Man", "Star Wars");
    }


    private List<RentedMovie> prepareRentedMovies() {
        RentedMovie avatar = new RentedMovie(postMovie(AVATAR), LocalDate.now().minusDays(3), LocalDate.now().minusDays(2));
        RentedMovie pulpFiction = new RentedMovie(postMovie(PULP_FICTION), LocalDate.now().minusDays(6), LocalDate.now().minusDays(1));
        RentedMovie spiderMan = new RentedMovie(postMovie(new Movie("Spider Man", MovieType.REGULAR)), LocalDate.now(), LocalDate.now().plusDays(2));
        RentedMovie starWars = new RentedMovie(postMovie(STAR_WARS), LocalDate.now(), LocalDate.now().plusDays(7));
        return Arrays.asList(avatar, pulpFiction, spiderMan, starWars);
    }
}