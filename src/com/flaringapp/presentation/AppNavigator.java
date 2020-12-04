package com.flaringapp.presentation;

import java.util.Stack;

public class AppNavigator {

    private static AppNavigator instance;

    private final Stack<Navigatable> stack = new Stack<>();

    private AppNavigator() {
    }

    public static AppNavigator getInstance() {
        if (instance == null) instance = new AppNavigator();
        return instance;
    }

    public void navigateTo(Navigatable screen) {
        if (!stack.empty()) {
            stack.peek().close();
        }
        stack.add(screen);
        screen.open();
    }

    public void navigateBack() {
        if (stack.empty()) return;
        stack.pop().close();

        if (!stack.empty()) {
            stack.peek().open();
        }
    }
}
