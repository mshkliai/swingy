package swingy.mvc.views.swing;

import swingy.bd.DataBase;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.lang.*;
import java.util.List;

import swingy.mvc.models.heroBuilder.*;
import swingy.mvc.models.myHero;

public class SwingChooseHero
{
    private SwingPanel              panel;
    private JTextField              inputName;
    private JComboBox               heroTypes;
    private JComboBox               oldHeroes;
    private SwingStats              stats;
    private myHero                  selectedHero;
    private DirectorHero            builder;

    private Map<String, JLabel>     labels;
    private Map<String, JButton>    buttons;
    private Font[]                  fonts;

    /********************* Constructor **********************/

    public SwingChooseHero(SwingPanel panel)
    {
        this.panel = panel;
        this.inputName = new JTextField("", 5);

        String[] nameTypes = {"Human", "Ork", "Elf"};

        this.heroTypes = new JComboBox(nameTypes);
        this.oldHeroes = new JComboBox();
        this.builder = new DirectorHero();

        this.labels = new HashMap<>();
        labels.put("Name", new JLabel("Name:"));
        labels.put("Old", new JLabel("Previous saved heroes:"));

        this.buttons = new HashMap<>();
        buttons.put("create", new JButton("Create new hero"));
        buttons.put("select", new JButton("Select"));
        buttons.put("remove", new JButton("Remove"));

        this.fonts = new Font[2];
    }

    /***************** Public Method *********************/

    public myHero     ChooseHero() throws Exception
    {
        DataBase.getDb().connectDb();

        this.prepareObjects();
        this.addObjOnFrame();

        panel.revalidate();
        panel.repaint();

        while (true) { if (this.panel.getComponents().length == 0) break; }
        this.panel.revalidate();
        this.panel.repaint();

        return this.selectedHero;
    }


    /************* Preparing Objects to painting *******************/

    private void    prepareObjects() throws Exception
    {
        this.loadFonts();
        this.prepareLabels();
        this.prepareNameField();
        this.prepareBoxes();
        this.prepareButtons();
    }

    private void    prepareLabels()
    {
        labels.get("Name").setLocation(950, 50);
        labels.get("Name").setSize(150, 100);
        labels.get("Name").setFont(fonts[0]);

        labels.get("Old").setLocation(85, 100);
        labels.get("Old").setSize(200, 100);
        labels.get("Old").setFont(fonts[0]);
    }

    private void    prepareNameField()
    {
        this.inputName.setLocation(900, 125);
        this.inputName.setSize(150, 35);
        this.inputName.setFont(fonts[1]);
    }

    private void    prepareBoxes() throws Exception
    {
        this.heroTypes.setLocation(898, 175);
        this.heroTypes.setSize(160, 50);
        this.heroTypes.addItemListener (
                (ItemEvent e) ->
                {
                    this.stats.setHero(builder.buildbyType( (String)heroTypes.getSelectedItem() ));
                    this.stats.updateData();
                }
        );

        /* Box old heroes */

        List<String> names = DataBase.getDb().getNames();
        for (String name : names)
            this.oldHeroes.addItem(name);
        this.oldHeroes.setLocation(100, 175);
        this.oldHeroes.setSize(160, 50);
        this.oldHeroes.addItemListener(
                (ItemEvent e) -> {
                    try
                    {
                        myHero newHero = DataBase.getDb().getHero( (String)oldHeroes.getSelectedItem() );
                        if (newHero == null)
                            newHero = builder.buildbyType( (String)heroTypes.getSelectedItem() );

                        this.stats.setHero(newHero);
                        this.stats.updateData();
                    }
                    catch (Exception e1) {
                        e1.printStackTrace();
                    }
                }
        );

        this.stats = new SwingStats( names.size() != 0 ? DataBase.getDb().getHero((String)oldHeroes.getSelectedItem()) :
                builder.buildbyType((String)heroTypes.getSelectedItem()) );
        this.stats.setLocation(400, 400);
    }

    private void    prepareButtons()
    {
        buttons.get("create").setLocation(875, 250);
        buttons.get("create").setSize(210, 25);
        buttons.get("create").addActionListener( (ActionEvent e) -> this.tryCreateNewHero() );
        buttons.get("create").setFont(fonts[1]);

        buttons.get("select").setLocation(75, 250);
        buttons.get("select").setSize(100, 25);
        buttons.get("select").addActionListener( (ActionEvent e) ->
        {
            if ( this.oldHeroes.getItemCount() == 0 )
                JOptionPane.showMessageDialog(this.panel, "You haven't any hero, create him");
            else
            {
                this.selectHero();
                this.panel.removeAll();
            }
        });
        buttons.get("select").setFont(fonts[1]);

        buttons.get("remove").setLocation(185, 250);
        buttons.get("remove").setSize(100, 25);
        buttons.get("remove").addActionListener( (ActionEvent e) -> this.tryRemoveHero() );
        buttons.get("remove").setFont(fonts[1]);
    }

    private void    addObjOnFrame()
    {
        panel.add(labels.get("Name"));
        panel.add(labels.get("Old"));
        panel.add(this.inputName);
        panel.add(this.heroTypes);
        panel.add(this.oldHeroes);
        panel.add(this.stats);
        panel.add(buttons.get("create"));
        panel.add(buttons.get("select"));
        panel.add(buttons.get("remove"));


        labels.get("Name").repaint();
        labels.get("Old").repaint();
        inputName.repaint();
        heroTypes.repaint();
        oldHeroes.repaint();
        stats.repaint();
        stats.updateData();
        buttons.get("create").repaint();
        buttons.get("select").repaint();
        buttons.get("remove").repaint();
    }

    private void    loadFonts()
    {
        try {
            this.fonts[0] = Font.createFont(Font.TRUETYPE_FONT, new File("../resources/fonts/font2.ttf")).deriveFont(20f);
            this.fonts[1] = Font.createFont(Font.TRUETYPE_FONT, new File("../resources/fonts/font3.ttf")).deriveFont(20f);
        } catch (FontFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /********* Hero Management ***********/

    private void    tryCreateNewHero()
    {
        myHero newHero = builder.buildbyType( (String)this.heroTypes.getSelectedItem() );
        String error = builder.trySetName(newHero, this.inputName.getText() );

        for (int i = 0; i < this.oldHeroes.getItemCount(); i++)
            if ( this.oldHeroes.getItemAt(i).equals(this.inputName.getText()) )
                error = "Hero with that name already created";

        if (error != null)
            JOptionPane.showMessageDialog(this.panel, error);
        else
        {
            try
            {
                DataBase.getDb().addNewHero(newHero);
                this.oldHeroes.addItem(this.inputName.getText());
                this.inputName.setText("");
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void    tryRemoveHero()
    {
        Object removeHero = this.oldHeroes.getSelectedItem();

        try
        {
            DataBase.getDb().remove( (String)removeHero );
            this.oldHeroes.removeItem( removeHero );
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void    selectHero()
    {
        try {
            this.selectedHero = DataBase.getDb().getHero( (String)this.oldHeroes.getSelectedItem() );
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }
}