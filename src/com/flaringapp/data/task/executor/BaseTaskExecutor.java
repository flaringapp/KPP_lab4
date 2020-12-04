package com.flaringapp.data.task.executor;

import com.flaringapp.data.task.DataHolder;
import com.flaringapp.data.monitor.ThreadState;

abstract class BaseTaskExecutor {

    private final TaskConsumer consumer;

    public BaseTaskExecutor(TaskConsumer consumer) {
        this.consumer = consumer;
    }

    protected void notifyUpdate(ThreadState state) {
        consumer.onTaskUpdate(state);
    }

    protected void notifyComplete(DataHolder data) {
        consumer.onTaskCompleted(data);
    }

    protected void notifyError(Throwable error) {
        consumer.onTaskError(error);
    }
}
