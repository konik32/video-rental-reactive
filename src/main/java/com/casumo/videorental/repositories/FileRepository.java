package com.casumo.videorental.repositories;

import com.casumo.videorental.model.File;
import com.casumo.videorental.model.Movie;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface FileRepository extends ReactiveCrudRepository<File, Long> {
}
