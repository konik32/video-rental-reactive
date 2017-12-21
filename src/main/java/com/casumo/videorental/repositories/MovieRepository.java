package com.casumo.videorental.repositories;

import com.casumo.videorental.model.Movie;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface MovieRepository extends ReactiveCrudRepository<Movie, String> {
}
