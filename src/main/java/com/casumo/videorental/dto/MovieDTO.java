package com.casumo.videorental.dto;

import com.casumo.videorental.model.File;
import com.casumo.videorental.model.MovieType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MovieDTO {

    @NotBlank
    private String title;

    @NotNull
    private MovieType movieType;

    @Valid
    private List<File> images;
}
