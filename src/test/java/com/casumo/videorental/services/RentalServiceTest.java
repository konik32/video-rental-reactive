package com.casumo.videorental.services;


import com.casumo.videorental.dto.Receipt;
import com.casumo.videorental.dto.Receipt.ReceiptItem;
import com.casumo.videorental.model.Movie;
import com.casumo.videorental.model.MovieType;
import com.casumo.videorental.model.RentedMovie;
import com.casumo.videorental.repositories.CustomerRepository;
import com.casumo.videorental.repositories.MovieRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;

@RunWith(MockitoJUnitRunner.class)
public class RentalServiceTest {

    private RentalService rentalService;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private MovieRepository movieRepository;

    @Mock
    private MoviePriceCalculator calculator;

    private Movie movie1 = new Movie("Avatar", MovieType.NEW);
    private Movie movie2 = new Movie("Pulp Fiction", MovieType.NEW);
    ;

    private RentedMovie avatar = new RentedMovie("Avatar", LocalDate.now(), LocalDate.now());
    private RentedMovie pulpFiction = new RentedMovie("Pulp Fiction", LocalDate.now(), LocalDate.now());


    @Before
    public void setUp() throws Exception {
        rentalService = new RentalService(movieRepository, customerRepository, Arrays.asList(calculator));
        Mockito.when(customerRepository.pullRentedMovies(anyString(), anyCollection())).thenReturn(Mono.empty());
        Mockito.when(customerRepository.pushRentedMovies(anyString(), anyCollection())).thenReturn(Mono.empty());
        Mockito.when(customerRepository.findCustomersRentedMovies(anyString(), anyCollection())).thenReturn(Flux.just(avatar, pulpFiction));
        Mockito.when(movieRepository.findAllById(anyIterable())).thenReturn(Flux.just(movie1, movie2));
        Mockito.when(calculator.supports(any())).thenReturn(true);
        Mockito.when(calculator.calculatePrice(eq(movie1), any(), any())).thenReturn(BigDecimal.ONE);
        Mockito.when(calculator.calculatePrice(eq(movie2), any(), any())).thenReturn(BigDecimal.TEN);
    }

    @Test
    public void shouldRentReturnReceiptWithTotalSumAndMoviesPrices() throws Exception {
        //when
        Receipt result = rentalService.rent("John", Arrays.asList(avatar, pulpFiction)).block();
        //then
        assertThat(result.getTotal()).isEqualTo(BigDecimal.valueOf(11));
        assertThat(result.getMovies()).containsOnly(new ReceiptItem("Avatar", BigDecimal.ONE),
                new ReceiptItem("Pulp Fiction", BigDecimal.TEN));
    }

    @Test
    public void shouldReturnEmptyReceiptOnNotFoundMovies() throws Exception {
        //given
        Mockito.when(movieRepository.findAllById(anyIterable())).thenReturn(Flux.empty());
        //when
        Receipt result = rentalService.rent("John", Arrays.asList(avatar, pulpFiction)).block();
        //then
        assertThat(result.getTotal()).isEqualTo(BigDecimal.ZERO);
        assertThat(result.getMovies()).isEmpty();
    }

    @Test
    public void shouldReturnMoviesReturnReceiptIfAnyMovieWasReturnedLate() throws Exception {
        //given
        pulpFiction.setRentedFrom(LocalDate.now().minusDays(3));
        pulpFiction.setRentedTo(LocalDate.now().minusDays(2));
        //when
        Receipt result = rentalService.returnMovies("John", Arrays.asList(avatar.getTitle(), pulpFiction.getTitle())).block();
        //then
        assertThat(result.getTotal()).isEqualTo(BigDecimal.TEN);
        assertThat(result.getMovies()).containsOnly(
                new ReceiptItem("Pulp Fiction", BigDecimal.TEN));
    }


}