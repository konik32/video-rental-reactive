package com.casumo.videorental.services;

import com.casumo.videorental.model.MovieType;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collection;

import static com.casumo.videorental.services.MovieCalculatorTestBase.ParameterizedMoviePriceCalculationTestBase;

@RunWith(Parameterized.class)
public class StaticPriceMoviePriceCalculatorTest extends ParameterizedMoviePriceCalculationTestBase {

    @Parameters(name = "Price for {1} days should be {2}")
    public static Collection<Object[]> values() {
        return Arrays.asList(new Object[][]{
                {AVATAR, 0, PREMIUM_PRICE},
                {AVATAR, 5, PREMIUM_PRICE.multiply(BigDecimal.valueOf(5))}
        });
    }

    @Override
    protected MoviePriceCalculator getCalculator() {
        return new StaticPriceMoviePriceCalculator(PREMIUM_PRICE, MovieType.NEW);
    }

}

