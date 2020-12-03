package com.flaringapp.monitor;

public class ThreadState {
    private final String name;
    private final String state;
    private final int priority;
    private final boolean isAlive;

    public ThreadState(String name, String state, int priority, boolean isAlive) {
        this.name = name;
        this.state = state;
        this.priority = priority;
        this.isAlive = isAlive;
    }

    public String getName() {
        return name;
    }

    public String getState() {
        return state;
    }

    public int getPriority() {
        return priority;
    }

    public boolean isAlive() {
        return isAlive;
    }

    @Override
    public String toString() {
        return "ThreadState{" +
                "name='" + name + '\'' +
                ", state='" + state + '\'' +
                ", priority=" + priority +
                ", isAlive=" + isAlive +
                '}';
    }
}
