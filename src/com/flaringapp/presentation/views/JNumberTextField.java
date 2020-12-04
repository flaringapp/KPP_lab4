package com.flaringapp.presentation.views;

import javax.swing.*;
import javax.swing.text.Document;
import java.awt.event.KeyEvent;

public class JNumberTextField extends JTextField {

    public JNumberTextField() {
    }

    public JNumberTextField(String text) {
        super(text);
    }

    public JNumberTextField(int columns) {
        super(columns);
    }

    public JNumberTextField(String text, int columns) {
        super(text, columns);
    }

    public JNumberTextField(Document doc, String text, int columns) {
        super(doc, text, columns);
    }

    @Override
    public void processKeyEvent(KeyEvent ev) {
        if (Character.isDigit(ev.getKeyChar()) ||
                ev.isActionKey() ||
                ev.getKeyCode() == KeyEvent.VK_DELETE ||
                ev.getKeyCode() == KeyEvent.VK_BACK_SPACE
        ) {
            super.processKeyEvent(ev);
        }
        ev.consume();
    }

    public Long getNumber() {
        Long result = null;
        String text = getText();
        if (text != null && !"".equals(text)) {
            result = Long.valueOf(text);
        }
        return result;
    }

}
