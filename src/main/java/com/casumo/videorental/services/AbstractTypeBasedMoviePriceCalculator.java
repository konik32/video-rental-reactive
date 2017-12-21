package com.casumo.videorental.services;

import com.casumo.videorental.model.Movie;
import com.casumo.videorental.model.MovieType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.stream.Stream;

import static java.time.temporal.ChronoUnit.DAYS;

public abstract class AbstractTypeBasedMoviePriceCalculator implements MoviePriceCalculator {

    private final MovieType supportedTypes[];

    protected AbstractTypeBasedMoviePriceCalculator(MovieType... supportedTypes) {
        this.supportedTypes = supportedTypes;
    }

    @Override
    public boolean supports(Movie movie) {
        return supportedTypes.length == 0 || Stream.of(supportedTypes).anyMatch(movie.getMovieType()::equals);
    }

    protected long daysBetween(LocalDate from, LocalDate to) {
        final long daysBetween = DAYS.between(from, to);
        return daysBetween > 1 ? daysBetween : 1;
    }
}
