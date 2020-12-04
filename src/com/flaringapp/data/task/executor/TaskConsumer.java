package com.flaringapp.data.task.executor;

import com.flaringapp.data.task.DataHolder;
import com.flaringapp.data.monitor.ThreadState;

public interface TaskConsumer {
    void onTaskUpdate(ThreadState state);
    void onTaskCompleted(DataHolder data);
    void onTaskError(Throwable error);
}
