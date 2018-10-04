package com.casumo.videorental.services.file;

import com.casumo.videorental.model.File;
import com.casumo.videorental.repositories.FileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Service
public class FileService {

    private final FileRepository fileRepository;
    private final FileHostingService fileHostingService;

    public Mono<File> save(File file) {
        return fileHostingService.moveFileToPersistentLocation(file)
                .flatMap(fileRepository::save);
    }
}
