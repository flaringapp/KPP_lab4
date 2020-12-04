package com.flaringapp.presentation.base;

import com.flaringapp.presentation.AppNavigator;
import com.flaringapp.presentation.Navigatable;

public abstract class BaseView implements Navigatable {

    private boolean isCreated = false;

    private boolean isInitialized = false;

    private boolean isShown = false;

    @Override
    public void onCreate() {
        if (isCreated) {
            throw new IllegalStateException(
                    getClass().getSimpleName() + " cannot be created because it's already created"
            );
        }
        if (!isInitialized) {
            init();
            isInitialized = true;
        }
        isCreated = true;
    }

    @Override
    public void onDestroy() {
        if (!isCreated) {
            throw new IllegalStateException(
                    getClass().getSimpleName() + " cannot be destroyed because it's not created"
            );
        }
        isCreated = false;
        release();
    }

    @Override
    public void show() {
        if (!isCreated) {
            throw new IllegalStateException(
                    getClass().getSimpleName() + " cannot be shown because it's not created"
            );
        };
    }

    @Override
    public void hide() {
        if (!isCreated) {
            throw new IllegalStateException(
                    getClass().getSimpleName() + " cannot be shown because it's not created"
            );
        }
    }

    protected void init() {
    }

    protected void release() {
    }

    protected AppNavigator getNavigator() {
        return AppNavigator.getInstance();
    }
}
