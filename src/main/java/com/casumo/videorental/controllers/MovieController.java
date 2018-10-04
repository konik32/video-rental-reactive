package com.casumo.videorental.controllers;

import com.casumo.videorental.dto.MovieDTO;
import com.casumo.videorental.model.Movie;
import com.casumo.videorental.repositories.MovieRepository;
import com.casumo.videorental.services.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

import static com.casumo.videorental.controllers.Paths.MOVIE;
import static com.casumo.videorental.controllers.Paths.MOVIES;

@RestController
@RequiredArgsConstructor
public class MovieController {

    private final MovieService movieService;

    @PostMapping(path = MOVIES)
    public Mono<ResponseEntity<Movie>> create(@RequestBody @Valid MovieDTO movie, UriComponentsBuilder builder) {
        return movieService.create(movie)
                .map(m -> ResponseEntity.created(builder.path(MOVIE).build(m.getTitle())).body(m));
    }

    @GetMapping(path = MOVIE)
    public Mono<ResponseEntity<Movie>> get(@PathVariable("id") String id) {
        return movieService.findById(id)
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }
}
