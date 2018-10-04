package com.casumo.videorental.config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("application.file-upload")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileUploadProperties {

    private String acl;
    private String temporaryBucket;
    private String persistentBucket;
    private int temporaryFileExpirationInSeconds;
    private int maxFileSize;

}
