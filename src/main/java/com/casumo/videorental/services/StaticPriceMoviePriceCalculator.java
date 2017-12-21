package com.casumo.videorental.services;

import com.casumo.videorental.model.Movie;
import com.casumo.videorental.model.MovieType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static java.time.temporal.ChronoUnit.DAYS;


public class StaticPriceMoviePriceCalculator extends AbstractTypeBasedMoviePriceCalculator {

    private final BigDecimal price;

    public StaticPriceMoviePriceCalculator(BigDecimal price, MovieType... movieTypes) {
        super(movieTypes);
        this.price = price;
    }

    @Override
    public BigDecimal calculatePrice(Movie movie, LocalDate rentFrom, LocalDate rentTo) {
        final long numberOfDays = DAYS.between(rentFrom, rentTo);
        return price.multiply(BigDecimal.valueOf(numberOfDays > 1 ? numberOfDays : 1));
    }
}
