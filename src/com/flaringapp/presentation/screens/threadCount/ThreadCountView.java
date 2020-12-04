package com.flaringapp.presentation.screens.threadCount;

import com.flaringapp.app.Constants;
import com.flaringapp.presentation.base.BaseView;
import com.flaringapp.presentation.screens.rootPicker.RootPickerView;
import com.flaringapp.presentation.utils.LimitDocumentFilter;
import com.flaringapp.presentation.views.JNumberTextField;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.AbstractDocument;
import java.awt.*;

public class ThreadCountView extends BaseView {

    private JFrame screen;

    private JTextField input;
    private JButton nextButton;

    @Override
    public void open() {
        super.open();
        screen.setVisible(true);
        updateNextButton();
    }

    @Override
    public void close() {
        super.close();
        screen.setVisible(false);
    }

    @Override
    protected void init() {
        screen = new JFrame();
        screen.setTitle(Constants.APP_NAME);
        screen.setResizable(false);

        createUI(screen);

        screen.pack();

        screen.setLocationRelativeTo(null);
        screen.toFront();
    }

    private void createUI(JFrame frame) {
        JPanel panel = new JPanel();
        BoxLayout layout = new BoxLayout(panel, BoxLayout.Y_AXIS);
        panel.setLayout(layout);

        panel.add(createTitle());
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(createInput());
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(createSubmitButton());

        panel.setBorder(new EmptyBorder(16, 16, 16, 16));

        frame.getContentPane().add(panel);
    }

    private JLabel createTitle() {
        JLabel title = new JLabel("Enter threads count:");
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 22));
        return title;
    }

    private JTextField createInput() {
        input = new JNumberTextField(3);
        input.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 16));
        input.getDocument().addDocumentListener(new ThreadCountTextChangeListener());
        ((AbstractDocument) input.getDocument()).setDocumentFilter(new LimitDocumentFilter(2));
        return input;
    }

    private JButton createSubmitButton() {
        nextButton = new JButton("Next");
        nextButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        nextButton.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 18));
        nextButton.addActionListener(e -> openRootPickerScreen());
        return nextButton;
    }

    private void openRootPickerScreen() {
        getNavigator().navigateTo(new RootPickerView());
    }

    private void updateNextButton() {
        nextButton.setEnabled(!input.getText().isEmpty());
    }

    private class ThreadCountTextChangeListener implements DocumentListener {

        @Override
        public void insertUpdate(DocumentEvent e) {
            updateNextButton();
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            updateNextButton();
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            updateNextButton();
        }
    }
}
