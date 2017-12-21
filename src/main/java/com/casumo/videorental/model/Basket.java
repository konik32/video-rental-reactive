package com.casumo.videorental.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@Document
@NoArgsConstructor
public class Basket {

    @Id
    private String id;
    private String customerId;
    private List<String> movies;
    private BigDecimal estimatedPrice;
    private BigDecimal finalPrice;
    private LocalDate rentTime;
    private LocalDate estimatedReturnTime;
    private LocalDate actualReturnTime;

    private Basket(String id, String customerId, List<String> movies, BigDecimal estimatedPrice, BigDecimal finalPrice, LocalDate rentTime, LocalDate estimatedReturnTime, LocalDate actualReturnTime) {
        this.id = id;
        this.customerId = customerId;
        this.movies = movies;
        this.estimatedPrice = estimatedPrice;
        this.finalPrice = finalPrice;
        this.rentTime = rentTime;
        this.estimatedReturnTime = estimatedReturnTime;
        this.actualReturnTime = actualReturnTime;
    }

    public static BasketBuilder builder() {
        return new BasketBuilder();
    }

    public static class BasketBuilder {
        private String id;
        private String customerId;
        private List<String> movies;
        private BigDecimal estimatedPrice;
        private BigDecimal finalPrice;
        private LocalDate rentTime;
        private LocalDate estimatedReturnTime;
        private LocalDate actualReturnTime;

        BasketBuilder() {
        }

        public Basket.BasketBuilder id(String id) {
            this.id = id;
            return this;
        }

        public Basket.BasketBuilder customerId(String customerId) {
            this.customerId = customerId;
            return this;
        }

        public Basket.BasketBuilder movies(List<String> movies) {
            this.movies = movies;
            return this;
        }

        public Basket.BasketBuilder estimatedPrice(BigDecimal estimatedPrice) {
            this.estimatedPrice = estimatedPrice;
            return this;
        }

        public Basket.BasketBuilder finalPrice(BigDecimal finalPrice) {
            this.finalPrice = finalPrice;
            return this;
        }

        public Basket.BasketBuilder rentTime(LocalDate rentTime) {
            this.rentTime = rentTime;
            return this;
        }

        public Basket.BasketBuilder estimatedReturnTime(LocalDate estimatedReturnTime) {
            this.estimatedReturnTime = estimatedReturnTime;
            return this;
        }

        public Basket.BasketBuilder actualReturnTime(LocalDate actualReturnTime) {
            this.actualReturnTime = actualReturnTime;
            return this;
        }

        public Basket build() {
            return new Basket(id, customerId, movies, estimatedPrice, finalPrice, rentTime, estimatedReturnTime, actualReturnTime);
        }
    }
}
