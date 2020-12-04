package com.flaringapp.presentation.screens.taskProcess;

import com.flaringapp.app.Constants;
import com.flaringapp.data.TaskParams;
import com.flaringapp.data.monitor.ThreadState;
import com.flaringapp.data.task.DataHolder;
import com.flaringapp.data.task.SizeCounter;
import com.flaringapp.data.task.executor.TaskConsumer;
import com.flaringapp.data.task.executor.TaskExecutor;
import com.flaringapp.data.task.executor.TaskExecutorImpl;
import com.flaringapp.presentation.base.BaseView;

import javax.swing.*;
import java.awt.*;

public class TaskProcessView extends BaseView implements TaskConsumer {

    private final TaskParams params;

    private JFrame frame;

    private JScrollPane scrollPane;
    private JLabel logText;

    private long startTime = 0;

    private boolean isPendingScroll = false;

    public TaskProcessView(TaskParams params) {
        this.params = params;
    }

    @Override
    public void show() {
        super.show();
        frame.setVisible(true);
        startProcess();
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
        scrollPane = null;
        logText = null;
    }

    private void createUI(JFrame frame) {
        JPanel panel = new JPanel();

        JLabel log = createLogText();
        JScrollPane scrollView = createScrollView(log);

        panel.add(scrollView);

        frame.getContentPane().add(panel);
    }

    private JScrollPane createScrollView(JLabel text) {
        JScrollPane pane = new JScrollPane(
                text,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED
        );
        pane.setPreferredSize(new Dimension(600, 375));
        scrollPane = pane;
        return pane;
    }

    private JLabel createLogText() {
        JLabel text = new JLabel("<html>");
        text.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
        logText = text;
        return text;
    }

    private void startProcess() {
        startTime = System.currentTimeMillis();
        TaskExecutor executor = new TaskExecutorImpl(this);
        executor.execute(params);
    }

    @Override
    public void onTaskUpdate(ThreadState state) {
        SwingUtilities.invokeLater(() -> logUpdate(state));
    }

    @Override
    public void onTaskCompleted(DataHolder data) {
        SwingUtilities.invokeLater(() -> logComplete(data));
    }

    @Override
    public void onTaskError(Throwable error) {
        SwingUtilities.invokeLater(() -> appendLogLine(error.getLocalizedMessage()));
    }

    private void logUpdate(ThreadState state) {
        appendLogLine("");
        appendLogLine("-------------");
        appendLogLine("");
        appendLogLine("Task update");
        appendLogLine(state.toString());
    }

    private void logComplete(DataHolder data) {
        appendLogLine("");
        appendLogLine("-------------");
        appendLogLine("");
        appendLogLine("Task completed!");
        appendLogLine("");

        long timeElapsed = System.currentTimeMillis() - startTime;
        long secondsElapsed = timeElapsed / 1000;

        appendLogLine("Seconds spent: " + secondsElapsed);
        appendLogLine("");
        appendLogLine("Files found: " + data.getFilesCount());
        appendLogLine("Directories found: " + data.getDirectoriesCount());

        appendLogLine("Files over target size (" + params.getMinFileSize() + "): " + data.getFilesOverSizeCount());

        appendLogLine("Files matching search (" + params.getSearch() + "): " + data.getFilesSearchMatchCount());

        SizeCounter sizes = data.getSizeCounter();
        appendLogLine("Total size: " +
                sizes.getGb() + "gB " +
                sizes.getMb() + "mB " +
                sizes.getKb() + "kB " +
                sizes.getB() + "b"
        );
    }

    private void appendLogLine(String line) {
        logText.setText(logText.getText() + "<br>" + line);

        if (!isPendingScroll) {
            isPendingScroll = true;
            SwingUtilities.invokeLater(() -> {
                isPendingScroll = false;
                JScrollBar vertical = scrollPane.getVerticalScrollBar();
                vertical.setValue(vertical.getMaximum());
            });
        }
    }
}
