package com.flaringapp.task;

public class SizeCounter {

    private static final long KILO = 1024;
    private static final long MEGA = KILO * KILO;
    private static final long GIGA = MEGA * KILO;

    private long b;
    private long kb;
    private long mb;
    private long gb;

    void addSize(long size) {
        b += size;

        kb += b / KILO;
        b %= KILO;

        mb += kb / KILO;
        kb %= KILO;

        gb += mb / KILO;
        mb %= KILO;
    }

    public long getB() {
        return b;
    }

    public long getKb() {
        return kb;
    }

    public long getMb() {
        return mb;
    }

    public long getGb() {
        return gb;
    }

}
