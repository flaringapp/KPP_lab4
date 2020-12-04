package com.flaringapp.presentation.screens.threadCount;

import com.flaringapp.app.Constants;
import com.flaringapp.data.TaskParams;
import com.flaringapp.presentation.base.BaseView;
import com.flaringapp.presentation.screens.rootPicker.RootPickerView;
import com.flaringapp.presentation.utils.AppFonts;
import com.flaringapp.presentation.utils.LimitDocumentFilter;
import com.flaringapp.presentation.views.JNumberTextField;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.AbstractDocument;
import java.awt.*;

public class ThreadCountView extends BaseView {

    private JFrame frame;

    private JTextField threadCountInput;
    private JButton nextButton;

    @Override
    public void show() {
        super.show();
        frame.setVisible(true);
        updateNextButton();
    }

    @Override
    public void hide() {
        super.hide();
        frame.setVisible(false);
    }

    @Override
    protected void init() {
        frame = new JFrame();
        frame.setTitle(Constants.APP_NAME);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        createUI(frame);

        frame.pack();

        frame.setLocationRelativeTo(null);
        frame.toFront();
    }

    @Override
    protected void release() {
        super.release();
        frame = null;
        threadCountInput = null;
        nextButton = null;
    }

    private void createUI(JFrame frame) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

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
        title.setFont(AppFonts.title);
        return title;
    }

    private JTextField createInput() {
        JTextField input = new JNumberTextField(3);
        input.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 16));
        input.getDocument().addDocumentListener(new ThreadCountTextChangeListener());
        ((AbstractDocument) input.getDocument()).setDocumentFilter(new LimitDocumentFilter(2));
        threadCountInput = input;
        return input;
    }

    private JButton createSubmitButton() {
        JButton button = new JButton("Next");
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setFont(AppFonts.button);
        button.addActionListener(e -> openRootPickerScreen());
        nextButton = button;
        return button;
    }

    private void openRootPickerScreen() {
        TaskParams params = new TaskParams();
        params.setThreadsCount(Integer.parseInt(threadCountInput.getText()));
        getNavigator().navigateTo(new RootPickerView(params));
    }

    private void updateNextButton() {
        nextButton.setEnabled(!threadCountInput.getText().isEmpty());
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
