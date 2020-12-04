package com.flaringapp.presentation.screens.rootPicker;

import com.flaringapp.app.Constants;
import com.flaringapp.data.TaskParams;
import com.flaringapp.presentation.base.BaseView;
import com.flaringapp.presentation.screens.taskProcess.TaskProcessView;
import com.flaringapp.presentation.utils.AppFonts;
import com.flaringapp.presentation.utils.LimitDocumentFilter;
import com.flaringapp.presentation.views.JNumberTextField;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.AbstractDocument;
import java.awt.*;

public class RootPickerView extends BaseView {

    private final TaskParams params;

    private JFrame frame;

    private JLabel pathText;
    private JButton selectPathButton;

    JTextField minSizeInput;

    JTextField searchInput;

    private JButton startButton;

    public RootPickerView(TaskParams params) {
        this.params = params;
    }

    @Override
    public void show() {
        super.show();
        frame.setVisible(true);
        updateUIOnDirectorySelected();
        updateStartButton();
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
        frame = null;
        pathText = null;
        selectPathButton = null;
        minSizeInput = null;
        searchInput = null;
        startButton = null;
    }

    private void createUI(JFrame frame) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        panel.add(createSelectPathLayout());

        panel.add(Box.createRigidArea(new Dimension(0, 15)));

        panel.add(createInputMinSizeLayout());

        panel.add(Box.createRigidArea(new Dimension(0, 15)));

        panel.add(createInputSearchLayout());

        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(createStartButton());

        panel.setBorder(new EmptyBorder(16, 16, 16, 16));

        frame.getContentPane().add(panel);
    }

    private Box createSelectPathLayout() {
        Box box = new Box(BoxLayout.Y_AXIS);
        box.add(createSelectPathTitle());
        box.add(Box.createRigidArea(new Dimension(0, 10)));
        box.add(createPathLayout());
        return box;
    }

    private JLabel createSelectPathTitle() {
        JLabel title = new JLabel("Select root directory");
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
        return title;
    }

    private Box createPathLayout() {
        Box box = new Box(BoxLayout.X_AXIS);
        box.setAlignmentX(Component.LEFT_ALIGNMENT);
        box.add(createSelectPathButton());
        box.add(createPathText());
        return box;
    }

    private JButton createSelectPathButton() {
        JButton button = new JButton("Select path");
        button.addActionListener(e -> openDirectoryPicker());
        selectPathButton = button;
        return button;
    }

    private JLabel createPathText() {
        JLabel text = new JLabel();
        text.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
        text.setForeground(Color.DARK_GRAY);
        pathText = text;
        return text;
    }

    private Box createInputMinSizeLayout() {
        Box box = new Box(BoxLayout.Y_AXIS);
        box.setAlignmentX(Component.LEFT_ALIGNMENT);
        box.add(createMinFileSizeTitle());
        box.add(Box.createRigidArea(new Dimension(0, 10)));
        box.add(createMinFileSizeInput());
        return box;
    }

    private JLabel createMinFileSizeTitle() {
        JLabel text = new JLabel("Enter min file size to count");
        text.setAlignmentX(Component.LEFT_ALIGNMENT);
        text.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 16));
        return text;
    }

    private JTextField createMinFileSizeInput() {
        JTextField input = new JNumberTextField(8);
        input.setAlignmentX(Component.LEFT_ALIGNMENT);
        input.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 16));
        input.getDocument().addDocumentListener(new ParamTextChangeListener());
        ((AbstractDocument) input.getDocument()).setDocumentFilter(new LimitDocumentFilter(8));
        minSizeInput = input;
        return input;
    }

    private Box createInputSearchLayout() {
        Box box = new Box(BoxLayout.Y_AXIS);
        box.setAlignmentX(Component.LEFT_ALIGNMENT);
        box.add(createSearchTitle());
        box.add(Box.createRigidArea(new Dimension(0, 10)));
        box.add(createSearchInput());
        return box;
    }

    private JLabel createSearchTitle() {
        JLabel text = new JLabel("Enter search constraint");
        text.setAlignmentX(Component.LEFT_ALIGNMENT);
        text.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 16));
        return text;
    }

    private JTextField createSearchInput() {
        JTextField input = new JTextField(32);
        input.setAlignmentX(Component.LEFT_ALIGNMENT);
        input.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 16));
        input.getDocument().addDocumentListener(new ParamTextChangeListener());
        ((AbstractDocument) input.getDocument()).setDocumentFilter(new LimitDocumentFilter(32));
        searchInput = input;
        return input;
    }

    private JButton createStartButton() {
        JButton button = new JButton("Start");
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setFont(AppFonts.button);
        button.addActionListener(e -> startProcess());
        startButton = button;
        return button;
    }

    private void openDirectoryPicker() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int option = fileChooser.showOpenDialog(frame);
        if (option == JFileChooser.APPROVE_OPTION) {
            params.setRootFile(fileChooser.getSelectedFile());
            updateUIOnDirectorySelected();
        }
    }

    private void updateUIOnDirectorySelected() {
        boolean hasDirectory = params.getRootFile() != null;
        if (hasDirectory) {
            pathText.setText(params.getRootFile().getPath());
            selectPathButton.setText("Change path");
        } else {
            selectPathButton.setText("Select path");
        }

        updateStartButton();
    }

    private void startProcess() {
        params.setMinFileSize(Long.parseLong(minSizeInput.getText()));
        params.setSearch(searchInput.getText());
        TaskProcessView screen = new TaskProcessView(params);
        getNavigator().navigateTo(screen);
    }

    private void updateStartButton() {
        boolean isAnyNotFilled = params.getRootFile() == null ||
                minSizeInput.getText().isEmpty() ||
                searchInput.getText().isEmpty();

        startButton.setEnabled(!isAnyNotFilled);
    }

    private class ParamTextChangeListener implements DocumentListener {

        @Override
        public void insertUpdate(DocumentEvent e) {
            updateStartButton();
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            updateStartButton();
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            updateStartButton();
        }
    }
}
