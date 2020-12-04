package com.flaringapp.data.task.executor;

import com.flaringapp.data.TaskParams;
import com.flaringapp.data.task.DataHolder;
import com.flaringapp.data.task.pool.FilesPool;
import com.flaringapp.data.task.pool.FilesPoolImpl;
import com.flaringapp.data.task.TaskThread;
import com.flaringapp.data.monitor.ThreadMonitor;
import com.flaringapp.data.task.pool.SynchronizedFilesPool;

import java.util.ArrayList;
import java.util.List;

public class TaskExecutorImpl extends BaseTaskExecutor implements TaskExecutor {

    private static final long updateTimeout = 1000;

    public TaskExecutorImpl(TaskConsumer consumer) {
        super(consumer);
    }

    @Override
    public void execute(TaskParams params) {
        int threadsCount = params.getThreadsCount();

        List<Thread> threads = new ArrayList<>();
        List<ThreadMonitor> monitors = new ArrayList<>();

        DataHolder data = new DataHolder();

        FilesPool pool = new FilesPoolImpl(params.getRootFile());
        pool = new SynchronizedFilesPool(pool, threadsCount, () -> {
            monitors.forEach(ThreadMonitor::stop);
            notifyComplete(data);
        });

        for (int i = 0; i < threadsCount; i++) {
            Thread thread = new TaskThread(pool, data, params.getMinFileSize(), params.getSearch());
            ThreadMonitor monitor = new ThreadMonitor(thread, updateTimeout);
            threads.add(thread);
            monitors.add(monitor);
        }

        threads.forEach(Thread::start);
        monitors.forEach(monitor -> monitor.start(this::notifyUpdate));
    }

}
