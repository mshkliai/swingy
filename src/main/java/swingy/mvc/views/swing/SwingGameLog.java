package swingy.mvc.views.swing;

import swingy.resources.Resources;

import javax.swing.*;
import java.awt.*;

public class SwingGameLog extends JTextArea {
    public SwingGameLog() {
        setLayout(null);
        setAutoscrolls(true);
        setBackground(Color.gray);
        setForeground( new Color(124, 252, 0) );

        setEditable(false);
        setFocusable(false);
        setFont(Resources.smallFont);
    }

    @Override
    public void	append(String str) {
        super.append(str);
        setRows(getRows() + 1);
    }
}