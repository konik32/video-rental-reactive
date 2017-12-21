package com.casumo.videorental.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"rentedFrom", "rentedTo"})
public class RentedMovie {
    @NotBlank
    private String title;
    @NotNull
    private LocalDate rentedFrom;
    @NotNull
    private LocalDate rentedTo;


}
