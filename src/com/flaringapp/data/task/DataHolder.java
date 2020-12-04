package com.flaringapp.data.task;

import java.util.concurrent.atomic.AtomicLong;

public final class DataHolder {

    private final AtomicLong filesCount = new AtomicLong();
    private final AtomicLong directoriesCount = new AtomicLong(1);

    private final AtomicLong filesOverSizeCount = new AtomicLong();
    private final AtomicLong filesSearchMatchCount = new AtomicLong();

    private final SizeCounter sizeCounter = new SizeCounter();

    public long getFilesCount() {
        return filesCount.get();
    }

    public long getDirectoriesCount() {
        return directoriesCount.get();
    }

    public long getFilesOverSizeCount() {
        return filesOverSizeCount.get();
    }

    public long getFilesSearchMatchCount() {
        return filesSearchMatchCount.get();
    }

    public SizeCounter getSizeCounter() {
        return sizeCounter;
    }

    void onNewFileFound() {
        filesCount.incrementAndGet();
    }

    void onNewFileOverSizeFound() {
        filesOverSizeCount.incrementAndGet();
    }

    void onNewFileSearchMatchFound() {
        filesSearchMatchCount.incrementAndGet();
    }

    void recordFileSize(long size) {
        synchronized(sizeCounter) {
            sizeCounter.addSize(size);
        }
    }

    void onNewDirectoryFound() {
        directoriesCount.incrementAndGet();
    }

}
