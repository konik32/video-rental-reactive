package com.casumo.videorental.repositories;

import com.casumo.videorental.model.Customer;
import com.casumo.videorental.model.RentedMovie;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@RunWith(SpringRunner.class)
public class CustomerRepositoryImplTest {

    @Autowired
    private CustomerRepository customerRepository;

    private Customer customer = new Customer("John");
    private RentedMovie avatar = new RentedMovie("Avatar", LocalDate.now(), LocalDate.now());
    private RentedMovie pulpFiction = new RentedMovie("Pulp Fiction", LocalDate.now(), LocalDate.now());
    private RentedMovie starWars = new RentedMovie("Star Wars", LocalDate.now(), LocalDate.now());

    @Before
    public void setUp() throws Exception {
        customerRepository.deleteAll();
        Set<RentedMovie> rentedMovies = Stream.of(avatar, pulpFiction, starWars).collect(Collectors.toSet());
        customer.setRentedMovies(rentedMovies);

    }

    @Test
    public void shouldReturnCustomerRentedMoviesByCustomerNameAndMovieTitlesCollection() throws Exception {
        //given
        customerRepository.save(customer).block();
        //when
        Flux<RentedMovie> customersRentedMovies = customerRepository.findCustomersRentedMovies(customer.getName(), Arrays.asList("Avatar", "Star Wars"));
        //then
        assertThat(customersRentedMovies.toIterable()).containsOnly(avatar, starWars);
    }

    @Test
    public void shouldAddRentedMoviesToCustomersRentedMovies() throws Exception {
        //given
        Set<RentedMovie> rentedMovies = Stream.of(pulpFiction, starWars).collect(Collectors.toSet());
        customer.setRentedMovies(rentedMovies);
        customerRepository.save(customer).block();
        //when
        customerRepository.pushRentedMovies(customer.getName(), Arrays.asList(avatar)).block();
        //then
        StepVerifier.create(customerRepository.findById(customer.getName()))
                .assertNext(customer -> assertThat(customer.getRentedMovies()).containsOnly(avatar, pulpFiction, starWars))
                .verifyComplete();
    }


    @Test
    public void shouldRemoveMoviesFromCustomerRentedMovies() throws Exception {
        //given
        customerRepository.save(customer).block();
        //when
        customerRepository.pullRentedMovies(customer.getName(), Arrays.asList(avatar.getTitle(), pulpFiction.getTitle())).block();
        //then
        StepVerifier.create(customerRepository.findById(customer.getName()))
                .assertNext(customer -> assertThat(customer.getRentedMovies()).containsOnly(starWars))
                .verifyComplete();
    }
}