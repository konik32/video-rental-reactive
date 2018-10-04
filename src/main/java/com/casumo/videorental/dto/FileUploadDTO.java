package com.casumo.videorental.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FileUploadDTO {

    private String acl;
    private String bucket;
    private String key;

    @JsonProperty("content-type")
    private String contentType;

    @JsonProperty("GoogleAccessId")
    private String googleAccessId;

    private String policy;
    private String signature;
}
