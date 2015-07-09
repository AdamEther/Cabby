# Cabby

`Cabby` is a smart tool to move all files from one cloud to another store.

`Cabby` means the taxi driver who does everything for you when all you have to do sit back graying and engage your affairs.

Using like this:

```
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
```

Feel free to add new `Cloud` interface implementations.