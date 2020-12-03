package com.flaringapp.task.pool;

import com.sun.istack.internal.Nullable;

import java.io.File;

public class SynchronizedFilesPool implements FilesPool {

    private final FilesPool pool;

    private final int threadsCount;
    private final Runnable completeCallback;

    private int awaitThreadsCount = 0;
    private boolean isFinished = false;

    public SynchronizedFilesPool(FilesPool pool, int threadsCount, Runnable completeCallback) {
        this.pool = pool;
        this.threadsCount = threadsCount;
        this.completeCallback = completeCallback;
    }

    @Override
    @Nullable
    public File pollNextDirectory() {
        synchronized (pool) {
            validateHasDirectories();
            if (isFinished) return null;
            return pool.pollNextDirectory();
        }
    }

    @Override
    public boolean addDirectory(File dir) {
        synchronized (pool) {
            boolean isAdded = pool.addDirectory(dir);
            if (isAdded && awaitThreadsCount > 0) {
                pool.notify();
            }
            return isAdded;
        }
    }

    @Override
    public boolean isEmpty() {
        synchronized (pool) {
            return pool.isEmpty();
        }
    }

    @Override
    public boolean isFull() {
        synchronized (pool) {
            return pool.isFull();
        }
    }

    private void validateHasDirectories() {
        if (!pool.isEmpty()) return;

        awaitThreadsCount++;
        if (threadsCount == awaitThreadsCount) {
            processTaskCompleted();
            return;
        }

        while (pool.isEmpty()) {
            waitForNewDirectories();
        }

        awaitThreadsCount--;
    }

    private void waitForNewDirectories() {
        try {
            pool.wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void processTaskCompleted() {
        completeCallback.run();
        isFinished = true;
        pool.notifyAll();
    }
}
