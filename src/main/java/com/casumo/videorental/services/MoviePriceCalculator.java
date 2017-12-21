package com.casumo.videorental.services;

import com.casumo.videorental.model.Movie;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public interface MoviePriceCalculator {

    boolean supports(Movie movie);

    BigDecimal calculatePrice(Movie movie, LocalDate rentFrom, LocalDate rentTo);
}
