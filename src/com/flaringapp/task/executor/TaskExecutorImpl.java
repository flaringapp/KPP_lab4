package com.flaringapp.task.executor;

import com.flaringapp.task.DataHolder;
import com.flaringapp.task.pool.FilesPool;
import com.flaringapp.task.pool.FilesPoolImpl;
import com.flaringapp.task.TaskThread;
import com.flaringapp.monitor.ThreadMonitor;
import com.flaringapp.task.pool.SynchronizedFilesPool;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class TaskExecutorImpl extends BaseTaskExecutor implements TaskExecutor {

    private static final long updateTimeout = 1000;

    public TaskExecutorImpl(TaskConsumer consumer) {
        super(consumer);
    }

    @Override
    public void execute(File root, int threadsCount) {
        List<Thread> threads = new ArrayList<>();
        List<ThreadMonitor> monitors = new ArrayList<>();

        DataHolder data = new DataHolder();

        FilesPool pool = new FilesPoolImpl(root);
        pool = new SynchronizedFilesPool(pool, threadsCount, () -> {
            monitors.forEach(ThreadMonitor::stop);
            notifyComplete(data);
        });

        for (int i = 0; i < threadsCount; i++) {
            Thread thread = new TaskThread(pool, data);
            ThreadMonitor monitor = new ThreadMonitor(thread, updateTimeout);
            threads.add(thread);
            monitors.add(monitor);
        }

        threads.forEach(Thread::start);
        monitors.forEach(monitor -> monitor.start(this::notifyUpdate));
    }

}
