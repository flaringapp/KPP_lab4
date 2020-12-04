package com.flaringapp.presentation.base;

import com.flaringapp.presentation.AppNavigator;
import com.flaringapp.presentation.Navigatable;

public abstract class BaseView implements Navigatable {

    private boolean isOpened = false;

    private boolean isInitialized = false;

    @Override
    public void open() {
        if (isOpened) {
            throw new IllegalStateException(
                    getClass().getSimpleName() + " cannot be opened because it's already opened"
            );
        }
        if (!isInitialized) {
            init();
            isInitialized = true;
        }
        isOpened = true;
    }

    @Override
    public void close() {
        if (!isOpened) {
            throw new IllegalStateException(
                    getClass().getSimpleName() + " cannot be closed because it's not opened"
            );
        }
        isOpened = false;
    }

    protected void init() {
    }

    protected AppNavigator getNavigator() {
        return AppNavigator.getInstance();
    }
}
