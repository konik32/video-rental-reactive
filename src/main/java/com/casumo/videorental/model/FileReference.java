package com.casumo.videorental.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileReference {

    private String id;
    private String servingUrl;

    public FileReference(File file) {
        this.id = file.getId();
        this.servingUrl = file.getServingUrl();
    }
}
