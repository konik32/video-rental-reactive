package com.casumo.videorental.services;

import com.casumo.videorental.config.FileUploadProperties;
import com.casumo.videorental.dto.FileUploadDTO;
import com.casumo.videorental.dto.FileUploadRequest;
import com.casumo.videorental.utils.FileUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.util.Base64;
import com.google.api.gax.core.CredentialsProvider;
import com.google.auth.Credentials;
import com.google.auth.oauth2.ServiceAccountCredentials;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@Service
public class FileUploadService {

    private final FileUploadProperties fileUploadProperties;
    private final ObjectMapper objectMapper;
    private final ServiceAccountCredentials credentials;

    public FileUploadService(FileUploadProperties fileUploadProperties, ObjectMapper objectMapper, CredentialsProvider credentialsProvider) throws IOException {
        this.fileUploadProperties = fileUploadProperties;
        this.objectMapper = objectMapper;
        Credentials credentials = credentialsProvider.getCredentials();
        if (credentials instanceof ServiceAccountCredentials) {
            this.credentials = (ServiceAccountCredentials) credentials;
        } else {
            throw new IllegalStateException("Provided gcp credentials couldn't be casted to ServiceAccountCredentials");
        }
    }

    public Mono<FileUploadDTO> prepareFileUploadData(FileUploadRequest fileUploadRequest) {
        return Mono.fromCallable(() -> this.doPrepareFileUploadData(fileUploadRequest));
    }

    private FileUploadDTO doPrepareFileUploadData(FileUploadRequest fileUploadRequest) {
        FileUploadDTO fileUploadDTO = FileUploadDTO.builder()
                .acl(fileUploadProperties.getAcl())
                .bucket(fileUploadProperties.getTemporaryBucket())
                .contentType(fileUploadRequest.getContentType())
                .googleAccessId(credentials.getAccount())
                .key(FileUtils.createUniqueFilename(fileUploadRequest.getName()))
                .build();

        final byte[] policy = createPolicy(fileUploadDTO);
        final byte[] signature = credentials.sign(policy);

        fileUploadDTO.setPolicy(new String(policy, Charset.forName("UTF-8")));
        fileUploadDTO.setSignature(Base64.encodeBase64String(signature));

        return fileUploadDTO;
    }


    private byte[] createPolicy(FileUploadDTO fileUploadDTO) {
        final FileUploadPolicy policy = FileUploadPolicy.builder()
                .expiration(LocalDateTime.now().plusSeconds(fileUploadProperties.getTemporaryFileExpirationInSeconds()))
                .condition("acl", fileUploadDTO.getAcl())
                .condition("bucket", fileUploadDTO.getBucket())
                .condition("content-type", fileUploadDTO.getContentType())
                .condition(Arrays.asList("content-length-range", 0, fileUploadProperties.getMaxFileSize()))
                .condition("key", fileUploadDTO.getKey())
                .build();
        try {
            final String policyString = objectMapper.writeValueAsString(policy);
            return Base64.encodeBase64(policyString.getBytes(Charset.forName("UTF-8")));
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Couldn't serialize policy " + policy, e);
        }

    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FileUploadPolicy {
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
        private LocalDateTime expiration;
        private List<Object> conditions;

        public static FileUploadPolicyBuilder builder() {
            return new FileUploadPolicyBuilder();
        }

        public static class FileUploadPolicyBuilder {
            private LocalDateTime expiration;
            private List<Object> conditions = new LinkedList<>();

            FileUploadPolicyBuilder() {
            }

            public FileUploadPolicyBuilder expiration(LocalDateTime expiration) {
                this.expiration = expiration;
                return this;
            }

            public FileUploadPolicyBuilder condition(String key, String value) {
                this.conditions.add(Collections.singletonMap(key, value));
                return this;
            }

            public FileUploadPolicyBuilder condition(Object condition) {
                this.conditions.add(condition);
                return this;
            }

            public FileUploadPolicy build() {
                return new FileUploadPolicy(expiration, conditions);
            }


        }
    }
}
