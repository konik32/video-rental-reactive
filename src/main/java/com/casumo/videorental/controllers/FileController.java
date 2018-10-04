package com.casumo.videorental.controllers;


import com.casumo.videorental.dto.FileUploadDTO;
import com.casumo.videorental.dto.FileUploadRequest;
import com.casumo.videorental.services.FileUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import static com.casumo.videorental.controllers.Paths.FILES;

@RestController
@RequiredArgsConstructor
public class FileController {

    private final FileUploadService fileUploadService;

    @PostMapping(path = FILES)
    public Mono<FileUploadDTO> prepareFileUploadData(@RequestBody FileUploadRequest fileUploadRequest) {
        return fileUploadService.prepareFileUploadData(fileUploadRequest);
    }
}
