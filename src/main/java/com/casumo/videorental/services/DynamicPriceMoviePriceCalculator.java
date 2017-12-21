package com.casumo.videorental.services;

import com.casumo.videorental.model.Movie;
import com.casumo.videorental.model.MovieType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class DynamicPriceMoviePriceCalculator extends AbstractTypeBasedMoviePriceCalculator {

    private final BigDecimal price;
    private final long threshold;

    public DynamicPriceMoviePriceCalculator(BigDecimal price, long threshold, MovieType... supportedTypes) {
        super(supportedTypes);
        this.threshold = threshold;
        this.price = price;
    }

    @Override
    public BigDecimal calculatePrice(Movie movie, LocalDate rentFrom, LocalDate rentTo) {
        final long numberOfDays = daysBetween(rentFrom, rentTo);
        final long rest = numberOfDays > threshold ? numberOfDays - threshold : 0;
        return price.add(price.multiply(BigDecimal.valueOf(rest)));
    }
}
