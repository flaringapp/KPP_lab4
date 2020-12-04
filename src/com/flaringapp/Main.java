package com.flaringapp;

import com.flaringapp.presentation.AppNavigator;
import com.flaringapp.presentation.screens.threadCount.ThreadCountView;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        AppNavigator navigator = AppNavigator.getInstance();
        SwingUtilities.invokeLater(() -> navigator.navigateTo(new ThreadCountView()));
    }

}
