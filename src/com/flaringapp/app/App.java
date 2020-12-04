package com.flaringapp.app;

import com.flaringapp.data.monitor.ThreadState;
import com.flaringapp.data.task.DataHolder;
import com.flaringapp.data.task.executor.TaskConsumer;
import com.flaringapp.data.task.executor.TaskExecutor;
import com.flaringapp.data.task.executor.TaskExecutorImpl;

import java.io.File;
import java.util.Scanner;

public class App {

    private final Scanner scanner;

    private boolean isFinished = false;
    private long startTime = 0;

    public App() {
        scanner = new Scanner(System.in);
    }

    public void run() {
        startTime = System.currentTimeMillis();

        execute();

        while (!isFinished) {
            scanForAnything();
        }
    }

    private void execute() {
        int threadsCount = getThreadsCount();
        File root = new File("/Applications");

        TaskExecutor executor = new TaskExecutorImpl(new TaskConsumerImpl());
        executor.execute(root, threadsCount);
    }

    private int getThreadsCount() {
        System.out.println("Enter amount of threads: ");
        return Integer.parseInt(scanner.nextLine());
    }

    private void scanForAnything() {
        System.out.println("Waiting for search to end...");
        scanner.nextLine();
    }

    private class TaskConsumerImpl implements TaskConsumer {

        @Override
        public void onTaskUpdate(ThreadState state) {
            System.out.println(state.toString());
        }

        @Override
        public void onTaskCompleted(DataHolder data) {
            System.out.println("Task completed");
            summarize(data);
            isFinished = true;
        }

        @Override
        public void onTaskError(Throwable error) {
            System.out.println("Task error: " + error.getMessage());
            error.printStackTrace();
            isFinished = true;
        }

        private void summarize(DataHolder data) {
            long millisElapsed = System.currentTimeMillis() - startTime;
            System.out.println("Millis elapsed: " + millisElapsed + ", in seconds: " + millisElapsed / 1000);
            System.out.println("Files count: " + data.getFilesCount());
            System.out.println("Directories count: " + data.getDirectoriesCount());
            System.out.println("Bytes: " + data.getSizeCounter().getB());
            System.out.println("Kilobytes: " + data.getSizeCounter().getKb());
            System.out.println("Megabytes: " + data.getSizeCounter().getMb());
            System.out.println("Gigabytes: " + data.getSizeCounter().getGb());
        }
    }

}
