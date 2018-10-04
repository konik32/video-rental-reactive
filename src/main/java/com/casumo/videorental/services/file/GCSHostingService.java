package com.casumo.videorental.services.file;

import com.casumo.videorental.model.File;


public interface GCSHostingService {

    File moveFileToPersistentLocation(File file);
}
