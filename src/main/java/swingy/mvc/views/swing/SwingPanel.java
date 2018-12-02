package swingy.mvc.views.swing;

import swingy.mvc.Controller;

import javax.swing.*;
import java.awt.*;

public class SwingPanel extends JPanel
{

    private Controller controller;

    public SwingPanel(Controller controller)
    {
        this.setLayout(null);
        this.setBounds(0, 0, 1200, 1000);
        this.controller = controller;

        this.setVisible(true);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
    }
}