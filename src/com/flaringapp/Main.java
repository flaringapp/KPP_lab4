package com.flaringapp;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static boolean isFinished = false;
    private static long millisElapsed;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter amount of threads: ");
        int count = Integer.parseInt(scanner.nextLine());

        File root = new File("/Applications");

        final long timeStart = System.currentTimeMillis();

        FilesPool pool = new FilesPool(root, count, () -> {
            isFinished = true;
            millisElapsed = System.currentTimeMillis() - timeStart;
        });
        DataHolder data = new DataHolder();

        List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            threads.add(new TaskThread(pool, data));
        }

        threads.forEach(Thread::start);

        while (!isFinished) {
            System.out.println("Waiting for search to end...");
            for (int i = 0; i < count; i++) {
                System.out.println("Thread " + i + " is " + threads.get(i).getState().toString());
            }
            scanner.nextLine();
        }

        System.out.println("Job finished! ");
        System.out.println("Millis elapsed: " + millisElapsed + ", in seconds: " + millisElapsed / 1000);
        System.out.println("Files count: " + data.getFilesCount());
        System.out.println("Directories count: " + data.getDirectoriesCount());
        System.out.println("Bytes: " + data.getSizeCounter().getB());
        System.out.println("Kilobytes: " + data.getSizeCounter().getKb());
        System.out.println("Megabytes: " + data.getSizeCounter().getMb());
        System.out.println("Gigabytes: " + data.getSizeCounter().getGb());
    }
}
