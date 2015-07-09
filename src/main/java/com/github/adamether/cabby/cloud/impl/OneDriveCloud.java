package com.github.adamether.cabby.cloud.impl;

import com.github.adamether.cabby.cloud.Cloud;
import net.tjeerd.onedrive.core.OneDrive;
import net.tjeerd.onedrive.core.Principal;
import net.tjeerd.onedrive.exception.RestException;
import net.tjeerd.onedrive.json.folder.Data;
import net.tjeerd.onedrive.json.folder.File;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static net.tjeerd.onedrive.enums.FriendlyNamesEnum.ALL;

public class OneDriveCloud implements Cloud {

    private static final Logger logger = LoggerFactory.getLogger(OneDriveCloud.class);

    private static final String PHOTO_FILE_TYPE = "photo";

    private OneDrive oneDrive;

    public OneDriveCloud(
            String clientId,
            String clientSecret,
            String authorizationCode,
            String refreshToken) {

        final Principal principal = new Principal(
                clientId,
                clientSecret,
                authorizationCode,
                refreshToken
        );
        this.oneDrive = new OneDrive(principal, true);
        this.oneDrive.initAccessTokenByRefreshTokenAndClientId();
    }

    @Override
    public Set<String> getAllFileIds() throws IOException {
        try {
            final Set<String> ids = new HashSet<>();
            final List<Data> data = oneDrive.getMyFilesList(ALL).getData();
            for (Data nextFolder : data) {
                try {
                    ids.addAll(
                            oneDrive.getFileList(nextFolder.getId())
                                    .getData()
                                    .stream()
                                    .filter(file -> file.getType().equals(PHOTO_FILE_TYPE))
                                    .map(Data::getId)
                                    .collect(Collectors.toSet()));
                } catch (Exception e) {
                    logger.warn(e.getMessage(), e);
                }
            }
            return ids;
        } catch (Exception e) {
            throw new IOException(e);
        }
    }

    @Override
    public InputStream getFile(String fileId) {
        return oneDrive.openFile(fileId);
    }

    @Override
    public void saveFile(InputStream fileStream) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void deleteFile(String fileId) throws IOException {
        try {
            oneDrive.deleteFile(new File(fileId));
        } catch (RestException e) {
            throw new IOException(e);
        }
    }

}
