package com.flaringapp.task.pool;

import com.sun.istack.internal.Nullable;

import java.io.File;
import java.util.ArrayDeque;
import java.util.Queue;

public class FilesPoolImpl implements FilesPool {

    private static final int MAX_SIZE = 1024;

    private final Queue<File> directories = new ArrayDeque<>(MAX_SIZE);

    public FilesPoolImpl(File root) {
        directories.add(root);
    }

    @Override
    @Nullable
    public File pollNextDirectory() {
        return directories.poll();
    }

    @Override
    public boolean addDirectory(File file) {
        if (isFull()) return false;
        directories.add(file);
        return true;
    }

    @Override
    public boolean isEmpty() {
        return directories.size() == 0;
    }

    @Override
    public boolean isFull() {
        return directories.size() >= MAX_SIZE;
    }

}
