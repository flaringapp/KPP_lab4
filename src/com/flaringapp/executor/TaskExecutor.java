package com.flaringapp.executor;

import java.io.File;

public interface TaskExecutor {

    void execute(File root, int threadsCount);

}
