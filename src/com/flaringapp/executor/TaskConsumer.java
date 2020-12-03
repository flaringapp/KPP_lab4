package com.flaringapp.executor;

import com.flaringapp.DataHolder;
import com.flaringapp.monitor.ThreadState;

public interface TaskConsumer {
    void onTaskUpdate(ThreadState state);
    void onTaskCompleted(DataHolder data);
    void onTaskError(Throwable error);
}
