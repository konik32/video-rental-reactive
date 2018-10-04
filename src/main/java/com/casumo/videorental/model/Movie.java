package com.casumo.videorental.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Movie {
    @Id
    private String title;

    private MovieType movieType;

    private List<FileReference> images;


    public Movie(String title, MovieType movieType) {
        this.title = title;
        this.movieType = movieType;
    }
}
