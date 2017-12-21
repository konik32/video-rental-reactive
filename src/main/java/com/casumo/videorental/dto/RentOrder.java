package com.casumo.videorental.dto;

import com.casumo.videorental.model.RentedMovie;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class RentOrder {

    @NotEmpty
    @Valid
    private List<RentedMovie> rentedMovies;
}
