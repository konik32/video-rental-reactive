package com.casumo.videorental.services.file;

import com.casumo.videorental.config.FileUploadProperties;
import com.casumo.videorental.model.File;
import com.google.cloud.storage.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;

@RequiredArgsConstructor
public class DefaultGCSHostingService implements GCSHostingService {


    private final Storage storage;
    private final FileUploadProperties fileUploadProperties;


    public File moveFileToPersistentLocation(File file) {
        final BlobId source = BlobId.of(file.getBucket(), file.getKey());
        final BlobId target = BlobId.of(fileUploadProperties.getPersistentBucket(), file.getKey());

        final BlobInfo targetBlobInfo = BlobInfo.newBuilder(target)
                .setAcl(Collections.singletonList(Acl.of(Acl.User.ofAllUsers(), Acl.Role.READER)))
                .build();

        Storage.CopyRequest copyRequest = Storage.CopyRequest.newBuilder()
                .setSource(source)
                .setTarget(targetBlobInfo)
                .build();

        Blob blob = storage.copy(copyRequest).getResult();

        return File.builder()
                .bucket(fileUploadProperties.getPersistentBucket())
                .key(file.getKey())
                .servingUrl(blob.getMediaLink())
                .build();
    }
}
