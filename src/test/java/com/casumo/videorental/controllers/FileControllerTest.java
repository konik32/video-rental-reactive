package com.casumo.videorental.controllers;

import com.casumo.videorental.config.FileUploadProperties;
import com.casumo.videorental.dto.FileUploadDTO;
import com.casumo.videorental.dto.FileUploadRequest;
import com.casumo.videorental.services.FileUploadService.FileUploadPolicy;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.util.Base64;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;

import static com.casumo.videorental.MockCredentialsProvider.CLIENT_EMAIL;
import static com.casumo.videorental.MockCredentialsProvider.MOCK_SIGNATURE;
import static com.casumo.videorental.controllers.Paths.FILES;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
public class FileControllerTest {


    @Autowired
    private WebTestClient client;

    @Autowired
    private FileUploadProperties fileUploadProperties;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    public void shouldPrepareFileUploadData() throws Exception {
        //given
        final FileUploadRequest fileUploadRequest = new FileUploadRequest("image.jpg", "image/jpeg");
        //when
        EntityExchangeResult<FileUploadDTO> responseResult = client.post().uri(FILES)
                .body(Mono.just(fileUploadRequest), FileUploadRequest.class)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(FileUploadDTO.class)
                .returnResult();
        FileUploadDTO result = responseResult.getResponseBody();
        assertThat(result.getAcl()).isEqualTo(fileUploadProperties.getAcl());
        assertThat(result.getBucket()).isEqualTo(fileUploadProperties.getTemporaryBucket());
        assertThat(result.getContentType()).isEqualTo(fileUploadRequest.getContentType());
        assertThat(result.getKey()).contains("jpg");
        assertThat(result.getSignature()).isEqualTo(Base64.encodeBase64String(MOCK_SIGNATURE.getBytes()));
        assertThat(result.getGoogleAccessId()).isEqualTo(CLIENT_EMAIL);

        final String decodedPolicy = new String(Base64.decodeBase64(result.getPolicy()));
        FileUploadPolicy fileUploadPolicy = objectMapper.readValue(decodedPolicy, FileUploadPolicy.class);
        assertThat(fileUploadPolicy.getExpiration()).isBefore(LocalDateTime.now().plusSeconds(fileUploadProperties.getTemporaryFileExpirationInSeconds()));
        assertThat(fileUploadPolicy.getExpiration()).isAfter(LocalDateTime.now());
        assertThat(fileUploadPolicy.getConditions()).contains(Collections.singletonMap("acl", fileUploadProperties.getAcl()));
        assertThat(fileUploadPolicy.getConditions()).contains(Collections.singletonMap("bucket", fileUploadProperties.getTemporaryBucket()));
        assertThat(fileUploadPolicy.getConditions()).contains(Collections.singletonMap("content-type", fileUploadRequest.getContentType()));
        assertThat(fileUploadPolicy.getConditions()).contains(Collections.singletonMap("key", result.getKey()));
        assertThat(fileUploadPolicy.getConditions()).contains(Arrays.asList("content-length-range", 0, fileUploadProperties.getMaxFileSize()));
    }
}
