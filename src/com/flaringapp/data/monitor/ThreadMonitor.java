package com.flaringapp.data.monitor;

import com.sun.istack.internal.Nullable;

import java.util.function.Consumer;

public class ThreadMonitor {

    private final Thread observedThread;
    private final long updateTimeout;

    @Nullable
    private UpdateRunnable updateRunnable = null;

    public ThreadMonitor(Thread observedThread, long updateTimeout) {
        this.observedThread = observedThread;
        this.updateTimeout = updateTimeout;
    }

    public void start(Consumer<ThreadState> callback) {
        if (isRunning()) return;

        updateRunnable = new UpdateRunnable(
                updateTimeout,
                () -> callback.accept(generateCurrentThreadState())
        );
        new Thread(updateRunnable).start();
    }

    public void stop() {
        if (!isRunning()) return;
        updateRunnable.requestStop();
    }

    private boolean isRunning() {
        return updateRunnable != null;
    }

    private ThreadState generateCurrentThreadState() {
        return new ThreadState(
                observedThread.getName(),
                observedThread.getState().toString(),
                observedThread.getPriority(),
                observedThread.isAlive()
        );
    }

    private static class UpdateRunnable implements Runnable {

        private final long timeout;
        private final Runnable callback;

        private final Object waitObject = new Object();

        private boolean isStopped = false;

        public UpdateRunnable(long timeout, Runnable callback) {
            this.timeout = timeout;
            this.callback = callback;
        }

        @Override
        public void run() {
            synchronized (waitObject) {
                while (!isStopped) {
                    callback.run();
                    try {
                        waitObject.wait(timeout);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        void requestStop() {
            synchronized (waitObject) {
                isStopped = true;
                waitObject.notify();
            }
        }
    }
}
