package com.casumo.videorental.services.file;

import com.casumo.videorental.model.File;
import reactor.core.publisher.Mono;

public interface FileHostingService {

    Mono<File> moveFileToPersistentLocation(File file);

}
