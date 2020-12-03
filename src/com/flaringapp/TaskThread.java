package com.flaringapp;

import java.io.File;
import java.util.Arrays;

public class TaskThread extends Thread {

    private static final String DS_STORE = ".DS_Store";

    private final FilesPool pool;
    private final DataHolder data;

    public TaskThread(FilesPool pool, DataHolder data) {
        this.pool = pool;
        this.data = data;
    }

    @Override
    public void run() {
        File dir = pool.getNextAvailableDirectory();
        while (dir != null) {
            processDirectory(dir);
            dir = pool.getNextAvailableDirectory();
        }
    }

    private void processDirectory(File dir) {
        File[] nestedFiles = dir.listFiles();
        if (nestedFiles == null) return;
        Arrays.stream(nestedFiles)
                .forEach(this::processNestedFile);
    }

    private void processNestedFile(File file) {
        if (file.isDirectory()) {
            data.onNewDirectoryFound();
            if (pool.isFull()) processDirectory(file);
            else pool.addAvailableDirectory(file);
        } else {
            if (!file.getName().equals(DS_STORE)) {
                data.onNewFileFound();
            }

            data.recordFileSize(file.length());
        }
    }
}
