package com.flaringapp.presentation.screens.rootPicker;

import com.flaringapp.app.Constants;
import com.flaringapp.presentation.base.BaseView;
import com.flaringapp.presentation.utils.AppFonts;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.File;

public class RootPickerView extends BaseView {

    private JFrame frame;

    private JLabel pathText;
    private JButton selectPathButton;

    private JButton startButton;

    private File selectedDirectory = null;

    @Override
    public void show() {
        super.show();
        frame.setVisible(true);
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

        createUI(frame);

        frame.pack();

        frame.setLocationRelativeTo(null);
        frame.toFront();
    }

    @Override
    protected void release() {
        frame = null;
        pathText = null;
        selectPathButton = null;
        startButton = null;
    }

    private void createUI(JFrame frame) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        panel.add(createTitle());
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(createPathLayout());
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(createStartButton());

        panel.setBorder(new EmptyBorder(16, 16, 16, 16));

        frame.getContentPane().add(panel);
    }

    private JLabel createTitle() {
        JLabel title = new JLabel("Select root directory");
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setFont(AppFonts.title);
        return title;
    }

    private JPanel createPathLayout() {
        JPanel panel = new JPanel();
        FlowLayout layout = new FlowLayout(FlowLayout.LEFT);
        panel.setLayout(layout);

        panel.add(createPathText());
        panel.add(createSelectPathButton());

        panel.setPreferredSize(new Dimension(400, 40));
        return panel;
    }

    private JLabel createPathText() {
        JLabel text = new JLabel();
        text.setMaximumSize(new Dimension(400, 1000));
        text.setAlignmentY(Component.CENTER_ALIGNMENT);
        text.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 16));
        text.setForeground(Color.DARK_GRAY);
        pathText = text;
        return text;
    }

    private JButton createSelectPathButton() {
        JButton button = new JButton("Select path");
        button.setAlignmentY(Component.CENTER_ALIGNMENT);
        button.addActionListener(e -> openDirectoryPicker());
        selectPathButton = button;
        return button;
    }

    private JButton createStartButton() {
        JButton button = new JButton("Start");
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setFont(AppFonts.button);
//        button.addActionListener(e -> openDirectoryPicker());
        startButton = button;
        return button;
    }

    private void openDirectoryPicker() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int option = fileChooser.showOpenDialog(frame);
        if (option == JFileChooser.APPROVE_OPTION) {
            selectedDirectory = fileChooser.getSelectedFile();
            updateUIOnDirectorySelected();
        }
    }

    private void updateUIOnDirectorySelected() {
        boolean hasDirectory = selectedDirectory != null;
        if (hasDirectory) {
            pathText.setText(selectedDirectory.getPath());
            selectPathButton.setText("Change path");
        } else {
            selectPathButton.setText("Select path");
        }

        startButton.setEnabled(hasDirectory);
    }
}
