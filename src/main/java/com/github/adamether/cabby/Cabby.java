package com.github.adamether.cabby;

import com.github.adamether.cabby.cloud.Cloud;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicLong;

public final class Cabby {

    private static final Logger logger = LoggerFactory.getLogger(Cabby.class);

    private Cloud from;

    private Cloud to;

    private boolean isNeedDeleteFile;

    private AtomicLong processedFilesCount;

    public Cabby(Cloud from, Cloud to, boolean isNeedDeleteFile) {
        this.from = from;
        this.to = to;
        this.isNeedDeleteFile = isNeedDeleteFile;
        this.processedFilesCount = new AtomicLong();
    }

    public void setFrom(Cloud from) {
        this.from = from;
    }

    public void setTo(Cloud to) {
        this.to = to;
    }

    public void setNeedDeleteFile(boolean needDeleteFile) {
        this.isNeedDeleteFile = needDeleteFile;
    }

    public void carryAllThings() throws IOException {
        from.getAllFileIds().parallelStream().forEach(this::carryOneThing);
    }

    private void carryOneThing(String id) {
        final long iterationCount = processedFilesCount.incrementAndGet();
        logger.info("# [" + iterationCount + "] process [" + id + "] file");
        try {
            to.saveFile(
                    from.getFile(id)
            );
            if (isNeedDeleteFile) {
                from.deleteFile(id);
            }
        } catch (IOException e) {
            logger.error("Can't carry file: [" + id + "], " + e.getMessage());
        }
    }

}
