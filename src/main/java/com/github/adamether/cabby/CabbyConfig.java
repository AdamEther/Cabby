package com.github.adamether.cabby;

import org.aeonbits.owner.Config;

public interface CabbyConfig extends Config {

    String flickrApiKey();

    String flickrSecret();

    String oneDriveClientId();

    String oneDriveClientSecret();

    String oneDriveAuthorizationCode();

    String oneDriveRefreshToken();

}
