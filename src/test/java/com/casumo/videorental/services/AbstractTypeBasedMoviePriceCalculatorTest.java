package com.casumo.videorental.services;

import com.casumo.videorental.model.Movie;
import com.casumo.videorental.model.MovieType;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class AbstractTypeBasedMoviePriceCalculatorTest {

    private AbstractTypeBasedMoviePriceCalculator calculator;

    @Before
    public void setUp() throws Exception {
        calculator = new AbstractTypeBasedMoviePriceCalculator(MovieType.NEW, MovieType.OLD) {
            @Override
            public BigDecimal calculatePrice(Movie movie, LocalDate rentFrom, LocalDate rentTo) {
                return null;
            }
        };
    }

    @Test
    public void shouldSupportedMovieTypesProvidedInConstructor() throws Exception {
        assertThat(calculator.supports(MovieCalculatorTestBase.AVATAR)).isTrue();
        assertThat(calculator.supports(MovieCalculatorTestBase.PULP_FICTION)).isFalse();
        assertThat(calculator.supports(MovieCalculatorTestBase.STAR_WARS)).isTrue();
    }

    @Test
    public void shouldSupportAnyOnEmptyArray() throws Exception {
        AbstractTypeBasedMoviePriceCalculator calculator = new AbstractTypeBasedMoviePriceCalculator() {
            @Override
            public BigDecimal calculatePrice(Movie movie, LocalDate rentFrom, LocalDate rentTo) {
                return null;
            }
        };
        assertThat(calculator.supports(MovieCalculatorTestBase.AVATAR)).isTrue();
        assertThat(calculator.supports(MovieCalculatorTestBase.PULP_FICTION)).isTrue();
        assertThat(calculator.supports(MovieCalculatorTestBase.STAR_WARS)).isTrue();
    }
}
