package com.casumo.videorental.utils;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public final class FileUtils {

    private FileUtils() {
    }

    public static Optional<String> getFileExtension(String originalFilename) {
        return Optional.ofNullable(FilenameUtils.getExtension(originalFilename))
                .map(ext -> StringUtils.isBlank(ext) ? null : ext);
    }


    public static String appendExtension(String name, String extension) {
        return name + (Objects.isNull(extension) ? "" : "." + extension);
    }

    public static String createUniqueFilename(String originalFileName) {
        final String randomId = UUID.randomUUID().toString();
        return FileUtils.getFileExtension(originalFileName)
                .map(ext -> FileUtils.appendExtension(randomId, ext))
                .orElse(randomId);
    }
}
