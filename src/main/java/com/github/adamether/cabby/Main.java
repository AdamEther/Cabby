package com.github.adamether.cabby;

import com.github.adamether.cabby.cloud.impl.FlickrCloud;
import com.github.adamether.cabby.cloud.impl.OneDriveCloud;

import java.io.IOException;

import org.aeonbits.owner.ConfigFactory;

public class Main {

    public static void main(String[] args) throws IOException {
        final CabbyConfig config = ConfigFactory.create(CabbyConfig.class);

        final Cabby cabby = new CabbyBuilder()
                .from(
                    new OneDriveCloud(
                        config.oneDriveClientId(),
                        config.oneDriveClientSecret(),
                        config.oneDriveAuthorizationCode(),
                        config.oneDriveRefreshToken()
                    )
                ).to(
                    new FlickrCloud(
                        config.flickrApiKey(),
                        config.flickrSecret()
                    )
                ).needDeleteFile(false)
                .build();

        cabby.carryAllThings();
    }

}
