package com.github.adamether.cabby;

import com.github.adamether.cabby.cloud.Cloud;

public class CabbyBuilder {

    private Cloud from;

    private Cloud to;

    private boolean isNeedDeleteFile;

    public CabbyBuilder from(Cloud from) {
        this.from = from;
        return this;
    }

    public CabbyBuilder to(Cloud to) {
        this.to = to;
        return this;
    }

    public CabbyBuilder needDeleteFile(boolean isNeedDeleteFile) {
        this.isNeedDeleteFile = isNeedDeleteFile;
        return this;
    }

    public Cabby build() {
        return new Cabby(from, to, isNeedDeleteFile);
    }

}