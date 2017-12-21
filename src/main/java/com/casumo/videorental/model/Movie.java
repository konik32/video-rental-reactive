package com.casumo.videorental.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.TextScore;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Document
@NoArgsConstructor
public class Movie {
    @Id
    @NotBlank
    private String title;
    @NotNull
    private MovieType movieType;

    public Movie(String title, MovieType movieType) {
        this.title = title;
        this.movieType = movieType;
    }
}
