package com.casumo.videorental.controllers;

import com.casumo.videorental.dto.MovieDTO;
import com.casumo.videorental.model.File;
import com.casumo.videorental.model.FileReference;
import com.casumo.videorental.model.Movie;
import com.casumo.videorental.model.MovieType;
import com.casumo.videorental.repositories.MovieRepository;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.CopyWriter;
import com.google.cloud.storage.Storage;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.casumo.videorental.controllers.Paths.MOVIE;
import static com.casumo.videorental.controllers.Paths.MOVIES;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

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
    public void shouldCreateMovieWithImages() {
        List<File> images = Stream.of("image1.png", "image2.png")
                .map(fileName -> File.builder()
                        .bucket("temp-bucket")
                        .key(fileName)
                        .build())
                .collect(Collectors.toList());
        Blob copiedImage = mock(Blob.class);
        CopyWriter copyWriter = mock(CopyWriter.class);
        given(storage.copy(any(Storage.CopyRequest.class))).willReturn(copyWriter);
        given(copyWriter.getResult()).willReturn(copiedImage);
        given(copiedImage.getMediaLink()).willReturn("mediaLink1", "mediaLink2");
        MovieDTO movieDTO = new MovieDTO(AVATAR.getTitle(), AVATAR.getMovieType(), images);
        //when
        EntityExchangeResult<Movie> responseResult = client.post().uri(MOVIES)
                .body(Mono.just(movieDTO), MovieDTO.class)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(Movie.class)
                .returnResult();

        Movie result = responseResult.getResponseBody();
        assertThat(responseResult.getResponseHeaders().getLocation()).isNotNull();
        assertThat(result).isEqualToComparingOnlyGivenFields(AVATAR, "title", "movieType");
        assertThat(result.getImages()).extracting(FileReference::getServingUrl).containsExactlyInAnyOrder("mediaLink1", "mediaLink2");
        assertThat(result.getImages()).extracting(FileReference::getId).allMatch(Objects::nonNull);
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