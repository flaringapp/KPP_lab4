package com.flaringapp.data.task;

import com.flaringapp.data.task.pool.FilesPool;

import java.io.File;
import java.util.Arrays;

public class TaskThread extends Thread {

    // MacOS data file
    private static final String DS_STORE = ".DS_Store";

    private final FilesPool pool;
    private final DataHolder data;
    private final long minFileSize;
    private final String search;

    public TaskThread(FilesPool pool, DataHolder data, long minFileSize, String search) {
        this.pool = pool;
        this.data = data;
        this.minFileSize = minFileSize;
        this.search = search;
    }

    @Override
    public void run() {
        File dir = pool.pollNextDirectory();
        while (dir != null) {
            lookupDirectory(dir);
            dir = pool.pollNextDirectory();
        }
    }

    private void lookupDirectory(File dir) {
        File[] nestedFiles = dir.listFiles();
        if (nestedFiles == null) return;
        Arrays.stream(nestedFiles)
                .forEach(this::processNestedFile);
    }

    private void processNestedFile(File file) {
        if (file.isDirectory()) {
            processDirectory(file);
        } else {
            processSimpleFile(file);
        }
    }

    private void processDirectory(File file) {
        data.onNewDirectoryFound();
        if (!pool.addDirectory(file)) {
            lookupDirectory(file);
        }
    }

    private void processSimpleFile(File file) {
        if (!file.getName().equals(DS_STORE)) {
            data.onNewFileFound();
        }
        data.recordFileSize(file.length());
        if (file.length() > minFileSize) {
            data.onNewFileOverSizeFound();
        }
        if (file.getName().contains(search)) {
            data.onNewFileSearchMatchFound();
        }
    }
}
