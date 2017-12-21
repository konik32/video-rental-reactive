package com.casumo.videorental;

import com.casumo.videorental.model.Customer;
import com.casumo.videorental.model.Movie;
import com.casumo.videorental.model.MovieType;
import com.casumo.videorental.repositories.CustomerRepository;
import com.casumo.videorental.repositories.MovieRepository;
import com.casumo.videorental.services.DynamicPriceMoviePriceCalculator;
import com.casumo.videorental.services.MoviePriceCalculator;
import com.casumo.videorental.services.StaticPriceMoviePriceCalculator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@SpringBootApplication
@EnableReactiveMongoRepositories
public class VideoRentalApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(VideoRentalApplication.class, args);
    }

    @Bean
    public MoviePriceCalculator newMoviePriceCalculator() {
        return new StaticPriceMoviePriceCalculator(BigDecimal.valueOf(40), MovieType.NEW);
    }

    @Bean
    public MoviePriceCalculator regularMoviePriceCalculator() {
        return new DynamicPriceMoviePriceCalculator(BigDecimal.valueOf(30), 3l, MovieType.REGULAR);
    }

    @Bean
    public MoviePriceCalculator oldMoviePriceCalculator() {
        return new DynamicPriceMoviePriceCalculator(BigDecimal.valueOf(30), 5l, MovieType.OLD);
    }

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private MovieRepository movieRepository;

    @Override
    public void run(String... args) throws Exception {
        customerRepository.save(new Customer("John")).subscribe();

        movieRepository.saveAll(Stream.of(
                new Movie("Spider Man", MovieType.REGULAR),
                new Movie("Avatar", MovieType.NEW),
                new Movie("Pulp Fiction", MovieType.REGULAR),
                new Movie("Star Wars", MovieType.OLD)).collect(Collectors.toList())).subscribe();
    }
}
