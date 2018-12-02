package swingy.mvc.views.swing;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class SwingGameLog extends JTextArea
{
    private Font    font;

    public SwingGameLog()
    {
        this.font = null;
        this.setLayout(null);
        this.setAutoscrolls(true);
        this.setBackground(Color.gray);
        this.setForeground( new Color(124, 252, 0) );

        this.setEditable(false);
        this.setFocusable(false);
    }

    private void    loadFont()
    {
        try {
            this.font = Font.createFont(Font.TRUETYPE_FONT, new File("../resources/fonts/font.otf")).deriveFont(18f);
        } catch (FontFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void	append(String str)
    {
        if (this.font == null)
        {
            this.loadFont();
            this.setFont(font);
        }

        super.append(str);
        this.setRows(getRows() + 1);
    }
}