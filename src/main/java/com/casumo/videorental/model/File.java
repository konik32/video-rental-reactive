package com.casumo.videorental.model;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.net.URI;

@Data
@Builder
@Document
public class File {

    @Id
    private String id;
    @NotBlank
    private String bucket;
    @NotBlank
    private String key;
    private String servingUrl;
}
