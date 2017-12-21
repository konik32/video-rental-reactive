package com.casumo.videorental.services;

import com.casumo.videorental.model.Movie;
import com.casumo.videorental.model.MovieType;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

public abstract class MovieCalculatorTestBase {
    protected final static BigDecimal BASIC_PRICE = new BigDecimal("30");
    protected final static BigDecimal PREMIUM_PRICE = new BigDecimal("40");

    protected final static Movie AVATAR = new Movie("Avatar", MovieType.NEW);
    protected final static Movie PULP_FICTION = new Movie("Pulp Fiction", MovieType.REGULAR);
    protected final static Movie STAR_WARS = new Movie("Star Wars", MovieType.OLD);


    @RunWith(Parameterized.class)
    protected abstract static class ParameterizedMoviePriceCalculationTestBase extends MovieCalculatorTestBase {

        @Parameterized.Parameter(0)
        public Movie movie;

        @Parameterized.Parameter(1)
        public int numberOfDays;

        @Parameterized.Parameter(2)
        public BigDecimal expected;

        @Test
        public void shouldCalculateMovieRentalPrice() throws Exception {
            assertThat(getCalculator().calculatePrice(movie, LocalDate.now().minusDays(numberOfDays), LocalDate.now())).isEqualTo(expected);
        }

        protected abstract MoviePriceCalculator getCalculator();
    }
}
