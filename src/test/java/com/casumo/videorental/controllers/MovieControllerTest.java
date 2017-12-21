package com.casumo.videorental.controllers;

import com.casumo.videorental.model.Movie;
import com.casumo.videorental.model.MovieType;
import com.casumo.videorental.repositories.MovieRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import reactor.core.publisher.Mono;

import java.util.List;

import static com.casumo.videorental.controllers.Paths.MOVIE;
import static com.casumo.videorental.controllers.Paths.MOVIES;
import static org.assertj.core.api.Assertions.assertThat;

public class MovieControllerTest extends VideoRentalITBase {

    @Autowired
    private MovieRepository movieRepository;


    @Before
    public void setUp() throws Exception {
        movieRepository.deleteAll().block();
    }

    @Test
    public void shouldCreateMovie() throws Exception {
        EntityExchangeResult<Movie> responseResult = client.post().uri(MOVIES)
                .body(Mono.just(AVATAR), Movie.class)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(Movie.class)
                .returnResult();
        Movie result = responseResult.getResponseBody();
        assertThat(responseResult.getResponseHeaders().getLocation()).isNotNull();
        assertThat(result).isEqualToComparingOnlyGivenFields(AVATAR, "title", "movieType");
    }

    @Test
    public void shouldGetExistingMovie() throws Exception {
        String id = postMovie(AVATAR);
        Movie result = client.get().uri(MOVIE, id).accept(MediaType.APPLICATION_JSON).exchange()
                .expectStatus().isOk()
                .expectBody(Movie.class)
                .returnResult().getResponseBody();
        assertThat(result).isEqualToComparingOnlyGivenFields(AVATAR, "title", "movieType");
    }

    @Test
    public void shouldReturn404OnMissingMovie() throws Exception {
        client.get().uri(MOVIE, "missingId").accept(MediaType.APPLICATION_JSON).exchange()
                .expectStatus().isNotFound();
    }
}