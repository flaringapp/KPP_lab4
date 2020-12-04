package com.flaringapp.data.task.pool;

import com.sun.istack.internal.Nullable;

import java.io.File;

public interface FilesPool {

    @Nullable
    File pollNextDirectory();

    boolean addDirectory(File dir);

    boolean isEmpty();

    boolean isFull();

}
