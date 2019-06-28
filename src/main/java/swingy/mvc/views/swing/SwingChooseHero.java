package swingy.mvc.views.swing;

import swingy.bd.DataBase;
import swingy.mvc.models.Hero;
import swingy.mvc.models.heroBuilder.DirectorHero;
import swingy.resources.Resources;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SwingChooseHero {
    private SwingPanel              panel;
    private JTextField              inputName;
    private JComboBox               heroTypes;
    private JComboBox               oldHeroes;
    private SwingStats              stats;
    private Hero selectedHero;
    private DirectorHero            builder;

    private Map<String, JLabel>     labels;
    private Map<String, JButton>    buttons;

    public SwingChooseHero(SwingPanel panel) {
        this.panel = panel;
        String[] nameTypes = {"Human", "Ork", "Elf"};

        inputName = new JTextField("", 5);
        heroTypes = new JComboBox(nameTypes);
        oldHeroes = new JComboBox();
        builder = new DirectorHero();

        labels = new HashMap<>();
        labels.put("Name", new JLabel("Name:"));
        labels.put("Old", new JLabel("Previous saved heroes:"));

        buttons = new HashMap<>();
        buttons.put("create", new JButton("Create new hero"));
        buttons.put("select", new JButton("Select"));
        buttons.put("remove", new JButton("Remove"));
    }

    public Hero ChooseHero() {
        DataBase.getDb().connectDb();

        prepareObjects();
        addObjOnFrame();

        panel.revalidate();
        panel.repaint();

        while (true) {
            if (panel.getComponents().length == 0) {
                break;
            }
        }
        panel.revalidate();
        panel.repaint();

        return selectedHero;
    }

    private void prepareObjects() {
        prepareLabels();
        prepareNameField();
        prepareBoxes();
        prepareButtons();
    }

    private void prepareLabels() {
        labels.get("Name").setLocation(950, 50);
        labels.get("Name").setSize(150, 100);
        labels.get("Name").setFont(Resources.font2);

        labels.get("Old").setLocation(85, 100);
        labels.get("Old").setSize(200, 100);
        labels.get("Old").setFont(Resources.font2);
    }

    private void prepareNameField() {
        inputName.setLocation(900, 125);
        inputName.setSize(150, 35);
        inputName.setFont(Resources.font3);
    }

    private void prepareBoxes() {
        heroTypes.setLocation(898, 175);
        heroTypes.setSize(160, 50);
        heroTypes.addItemListener ((ItemEvent e) -> {
                    this.stats.setHero(builder.buildbyType(heroTypes.getSelectedItem().toString()));
                    this.stats.updateData();
                }
        );

        /* Box old heroes */

        List<String> names = DataBase.getDb().getNames();
        for (String name : names) {
            oldHeroes.addItem(name);
        }
        oldHeroes.setLocation(100, 175);
        oldHeroes.setSize(160, 50);
        oldHeroes.addItemListener((ItemEvent e) -> {
            try {
                Hero newHero = DataBase.getDb().getHero( (String)oldHeroes.getSelectedItem() );
                if (newHero == null) {
                    newHero = builder.buildbyType(heroTypes.getSelectedItem().toString());
                }

                stats.setHero(newHero);
                stats.updateData();
            }
            catch (Exception e1) {
                e1.printStackTrace();
            }
        });

        stats = new SwingStats(!names.isEmpty() ? DataBase.getDb().getHero(oldHeroes.getSelectedItem().toString()) :
                builder.buildbyType(heroTypes.getSelectedItem().toString()));
        stats.setLocation(400, 400);
    }

    private void    prepareButtons() {
        buttons.get("create").setLocation(875, 250);
        buttons.get("create").setSize(210, 25);
        buttons.get("create").addActionListener((ActionEvent e) -> tryCreateNewHero());
        buttons.get("create").setFont(Resources.font3);

        buttons.get("select").setLocation(75, 250);
        buttons.get("select").setSize(100, 25);
        buttons.get("select").addActionListener((ActionEvent e) -> {
            if (oldHeroes.getItemCount() == 0) {
                JOptionPane.showMessageDialog(panel, "You haven't any hero, create him");
            }
            else {
                selectHero();
                panel.removeAll();
            }
        });
        buttons.get("select").setFont(Resources.font3);
        buttons.get("remove").setLocation(185, 250);
        buttons.get("remove").setSize(100, 25);
        buttons.get("remove").addActionListener((ActionEvent e) -> tryRemoveHero());
        buttons.get("remove").setFont(Resources.font3);
    }

    private void    addObjOnFrame() {
        panel.add(labels.get("Name"));
        panel.add(labels.get("Old"));
        panel.add(inputName);
        panel.add(heroTypes);
        panel.add(oldHeroes);
        panel.add(stats);
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

    private void tryCreateNewHero() {
        Hero newHero = builder.buildbyType(heroTypes.getSelectedItem().toString());
        String error = builder.trySetName(newHero, inputName.getText());

        for (int i = 0; i < oldHeroes.getItemCount(); ++i) {
            if (oldHeroes.getItemAt(i).equals(inputName.getText())) {
                error = "Hero with that name already created";
            }
        }

        if (!error.isEmpty()) {
            JOptionPane.showMessageDialog(panel, error);
        }
        else {
            DataBase.getDb().addNewHero(newHero);
            oldHeroes.addItem(inputName.getText());
            inputName.setText("");
        }
    }

    private void tryRemoveHero() {
        Object heroToRemoving = oldHeroes.getSelectedItem();

        DataBase.getDb().remove(heroToRemoving.toString());
        oldHeroes.removeItem(heroToRemoving);
    }

    private void selectHero() {
        this.selectedHero = DataBase.getDb().getHero(oldHeroes.getSelectedItem().toString());
    }
}