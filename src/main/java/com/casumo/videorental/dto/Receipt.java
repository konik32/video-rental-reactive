package com.casumo.videorental.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Set;

@Data
@AllArgsConstructor
public class Receipt {

    private Set<ReceiptItem> movies;

    public BigDecimal getTotal() {
        return movies == null ? BigDecimal.ZERO : movies.stream()
                .map(ReceiptItem::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Data
    @AllArgsConstructor
    public static class ReceiptItem {
        private String movieTitle;
        private BigDecimal price;
    }
}
