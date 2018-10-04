package com.casumo.videorental.services;

import com.casumo.videorental.dto.MovieDTO;
import com.casumo.videorental.model.File;
import com.casumo.videorental.model.FileReference;
import com.casumo.videorental.model.Movie;
import com.casumo.videorental.repositories.MovieRepository;
import com.casumo.videorental.services.file.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MovieService {

    private final MovieRepository movieRepository;
    private final FileService fileService;

    private static final BiFunction<MovieDTO, List<File>, Movie> movieMapper = (movieDTO, images) -> Movie.builder()
            .movieType(movieDTO.getMovieType())
            .title(movieDTO.getTitle())
            .images(images.stream()
                    .map(FileReference::new)
                    .collect(Collectors.toList()))
            .build();

    public Mono<Movie> create(MovieDTO movieDTO) {
        return Flux.fromIterable(Objects.isNull(movieDTO.getImages()) ? Collections.emptyList() : movieDTO.getImages())
                .flatMap(fileService::save)
                .collectList()
                .map(images -> movieMapper.apply(movieDTO, images))
                .flatMap(movieRepository::save);
    }

    public Mono<Movie> findById(String id) {
        return movieRepository.findById(id);
    }
}
