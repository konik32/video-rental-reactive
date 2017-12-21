package com.casumo.videorental.services;

import com.casumo.videorental.model.MovieType;
import com.casumo.videorental.services.MovieCalculatorTestBase.ParameterizedMoviePriceCalculationTestBase;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Parameterized.class)
public class DynamicPriceMoviePriceCalculatorTestMoviePrice extends ParameterizedMoviePriceCalculationTestBase {

    @Parameters(name = "Price for {1} days should be {2}")
    public static Collection<Object[]> values() {
        return Arrays.asList(new Object[][]{
                {AVATAR, 0, BASIC_PRICE},
                {AVATAR, 3, BASIC_PRICE},
                {AVATAR, 5, BigDecimal.valueOf(90)}
        });
    }

    @Override
    protected MoviePriceCalculator getCalculator() {
        return new DynamicPriceMoviePriceCalculator(BASIC_PRICE, 3, MovieType.NEW);
    }


}