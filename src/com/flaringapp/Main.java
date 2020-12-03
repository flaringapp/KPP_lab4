package com.flaringapp;

import com.flaringapp.executor.TaskConsumer;
import com.flaringapp.executor.TaskExecutor;
import com.flaringapp.executor.TaskExecutorImpl;
import com.flaringapp.monitor.ThreadState;

import java.io.File;
import java.util.Scanner;

public class Main {

    private static boolean isFinished = false;
    private static long startTime = 0;

    private static Scanner scanner;

    public static void main(String[] args) {
        scanner = new Scanner(System.in);

        startTime = System.currentTimeMillis();

        initAndStart();

        while (!isFinished) {
            scanForAnything();
        }
    }

    private static void initAndStart() {
        System.out.println("Enter amount of threads: ");
        int threadsCount = Integer.parseInt(scanner.nextLine());

        File root = new File("/Applications");

        TaskExecutor executor = new TaskExecutorImpl(new TaskConsumerImpl());
        executor.execute(root, threadsCount);
    }

    private static void scanForAnything() {
        System.out.println("Waiting for search to end...");
        scanner.nextLine();
    }

    private static class TaskConsumerImpl implements TaskConsumer  {

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

        private static void summarize(DataHolder data) {
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
