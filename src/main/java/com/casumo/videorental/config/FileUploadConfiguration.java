package com.casumo.videorental.config;

import com.casumo.videorental.services.file.DefaultGCSHostingService;
import com.casumo.videorental.services.file.DevGCSHostingService;
import com.casumo.videorental.services.file.GCSHostingService;
import com.google.cloud.storage.Storage;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@EnableConfigurationProperties(FileUploadProperties.class)
public class FileUploadConfiguration {

    @Profile("dev")
    @Bean
    public GCSHostingService devGcsHostingService(){
        return new DevGCSHostingService();
    }

    @Profile("!dev")
    @Bean
    public GCSHostingService gcsHostingService(Storage storage, FileUploadProperties fileUploadProperties){
        return new DefaultGCSHostingService(storage, fileUploadProperties);
    }

}
