package com.flaringapp.task.executor;

import com.flaringapp.task.DataHolder;
import com.flaringapp.monitor.ThreadState;

public interface TaskConsumer {
    void onTaskUpdate(ThreadState state);
    void onTaskCompleted(DataHolder data);
    void onTaskError(Throwable error);
}
