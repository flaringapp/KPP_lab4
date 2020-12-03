package com.flaringapp.task;

import java.util.concurrent.atomic.AtomicLong;

public final class DataHolder {

    private final AtomicLong filesCount = new AtomicLong();
    private final AtomicLong directoriesCount = new AtomicLong(1);

    private final SizeCounter sizeCounter = new SizeCounter();

    public long getFilesCount() {
        return filesCount.get();
    }

    public long getDirectoriesCount() {
        return directoriesCount.get();
    }

    public SizeCounter getSizeCounter() {
        return sizeCounter;
    }

    void onNewFileFound() {
        filesCount.incrementAndGet();
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
