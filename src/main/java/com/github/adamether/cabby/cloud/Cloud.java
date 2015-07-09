package com.github.adamether.cabby.cloud;

import java.io.IOException;
import java.io.InputStream;
import java.util.Set;

public interface Cloud {

    Set<String> getAllFileIds() throws IOException;

    InputStream getFile(String fileId) throws IOException;

    void saveFile(InputStream fileStream) throws IOException;

    void deleteFile(String fileId) throws IOException;

}
