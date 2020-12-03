package com.flaringapp;

import com.sun.istack.internal.Nullable;

import java.io.File;
import java.util.ArrayDeque;
import java.util.Queue;

public class FilesPool {

    private static final int MAX_SIZE = 1024;

    private final int threadsCount;
    private final Runnable endCallback;

    private final Queue<File> directories = new ArrayDeque<>(MAX_SIZE);

    private final Object lock = new Object();

    private int awaitThreadsCount = 0;
    private boolean isFinished = false;

    public FilesPool(File root, int threadsCount, Runnable endCallback) {
        directories.add(root);
        this.threadsCount = threadsCount;
        this.endCallback = endCallback;
    }

    @Nullable
    public File getNextAvailableDirectory() {
        synchronized (lock) {
            validateHasDirectories();
            if (isFinished) return null;
            return directories.poll();
        }
    }

    public void addAvailableDirectory(File file) {
        synchronized (lock) {
            directories.add(file);
            if (awaitThreadsCount > 0) {
                lock.notify();
            }
        }
    }

    public boolean isFull() {
        synchronized (lock) {
            return directories.size() >= MAX_SIZE;
        }
    }

    private void validateHasDirectories() {
        if (!directories.isEmpty()) return;

        awaitThreadsCount++;
        if (threadsCount == awaitThreadsCount) {
            endCallback.run();
            isFinished = true;
            lock.notifyAll();
            return;
        }

        while (directories.isEmpty()) {
            waitForNewDirectories();
        }

        awaitThreadsCount--;
    }

    private void waitForNewDirectories() {
        try {
            lock.wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
