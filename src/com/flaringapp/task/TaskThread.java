package com.flaringapp.task;

import com.flaringapp.task.pool.FilesPool;

import java.io.File;
import java.util.Arrays;

public class TaskThread extends Thread {

    // MacOS data file
    private static final String DS_STORE = ".DS_Store";

    private final FilesPool pool;
    private final DataHolder data;

    public TaskThread(FilesPool pool, DataHolder data) {
        this.pool = pool;
        this.data = data;
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
    }
}
