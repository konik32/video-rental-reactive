package com.casumo.videorental.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Set;

@Data
@Document
@NoArgsConstructor
public class Customer {

    @Id
    @NotBlank
    private String name;
    private Integer bonusPoints = 0;
    private Set<RentedMovie> rentedMovies;

    public Customer(String name) {
        this.name = name;
    }
}
