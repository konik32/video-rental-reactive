package com.casumo.videorental.services.file;

import com.casumo.videorental.model.File;

public class DevGCSHostingService implements GCSHostingService {

    @Override
    public File moveFileToPersistentLocation(File file) {
        return File.builder()
                .key(file.getKey())
                .bucket(file.getBucket())
                .servingUrl("http://localhost/" + file.getKey())
                .build();
    }
}
