package com.github.adamether.cabby.cloud.impl;

import com.flickr4java.flickr.Flickr;
import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.REST;
import com.flickr4java.flickr.RequestContext;
import com.flickr4java.flickr.auth.Auth;
import com.flickr4java.flickr.auth.AuthInterface;
import com.flickr4java.flickr.auth.Permission;
import com.flickr4java.flickr.uploader.UploadMetaData;
import com.flickr4java.flickr.util.AuthStore;
import com.flickr4java.flickr.util.FileAuthStore;
import com.github.adamether.cabby.cloud.Cloud;
import org.scribe.model.Token;
import org.scribe.model.Verifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;
import java.util.Set;

public class FlickrCloud implements Cloud {

    private static final Logger logger = LoggerFactory.getLogger(FlickrCloud.class);

    private static final String AUTH_STORE = "./auth_storage/";

    private final Flickr flickr;
    private final AuthStore authStore;

    public FlickrCloud(String apiKey, String secret) {
        try {
            this.flickr = new Flickr(apiKey, secret, new REST());
            this.authStore = new FileAuthStore(new File(AUTH_STORE));
        } catch (FlickrException e) {
            throw new RuntimeException(e);
        }
    }

    public void authorize() {
        try {
            Flickr.debugStream = false;
            AuthInterface authInterface = flickr.getAuthInterface();

            Scanner scanner = new Scanner(System.in);
            Token token = authInterface.getRequestToken();
            System.out.println("token: " + token);

            String url = authInterface.getAuthorizationUrl(token, Permission.WRITE);
            System.out.println("Follow this URL to authorise yourself on Flickr");
            System.out.println(url);
            System.out.println("Paste in the token it gives you:");
            System.out.print(">>");

            String tokenKey = scanner.nextLine();
            Token requestToken = authInterface.getAccessToken(token, new Verifier(tokenKey));
            Auth auth = authInterface.checkToken(requestToken);
            RequestContext.getRequestContext().setAuth(auth);
            authStore.store(auth);

            System.out.println("Authentication success");
        } catch (Exception e) {
            e.getStackTrace();
        }
    }

    private void checkAuth() throws IOException {
        RequestContext requestContext = RequestContext.getRequestContext();
        Auth[] auths = authStore.retrieveAll();

        if (auths.length != 0) {
            requestContext.setAuth(auths[0]);
        } else {
            authorize();
        }
    }

    @Override
    public Set<String> getAllFileIds() {
        throw new UnsupportedOperationException();
    }

    @Override
    public InputStream getFile(String fileId) throws IOException {
        return new URL(fileId).openStream();
    }

    @Override
    public void saveFile(InputStream fileStream) throws IOException {
        try {
            checkAuth();
            flickr.getUploader().upload(fileStream, new UploadMetaData());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    @Override
    public void deleteFile(String fileId) throws IOException {
        throw new UnsupportedOperationException();
    }

}
