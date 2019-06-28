package swingy.mvc.views.swing;

import swingy.mvc.Controller;
import swingy.mvc.models.Enemy;
import swingy.resources.Resources;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class SwingMapPanel extends JPanel {
    private Controller  controller;
    private int         sizeSquare;

    public SwingMapPanel(Controller controller, int sizeSquare) {
        this.controller = controller;
        this.sizeSquare = sizeSquare;
        setLayout(null);
        setDoubleBuffered(true);
    }

    @Override
    public void  paint(Graphics g) {
        super.paint(g);
        Graphics2D g2 = (Graphics2D) g;

        this.drawMap(g2);
        this.drawHero(g2);
        this.drawEnemies(g2);
    }

    private void drawMap(Graphics2D g2) {
        setPreferredSize(new Dimension(sizeSquare * controller.getSizeMap(), sizeSquare * controller.getSizeMap()));

        for (int i = 0; i < controller.getSizeMap(); ++i) {
            for (int j = 0; j < controller.getSizeMap(); ++j) {
                g2.drawRect(sizeSquare * j, sizeSquare * i, sizeSquare, sizeSquare);
            }
        }
    }

    private void drawHero(Graphics2D g2) {
        Image img = getToolkit().getImage(Resources.findPathToResources().concat("characters/")
                .concat(controller.getHero().getType()).concat(".png"));

        prepareImage(img, this);

        g2.setColor(new Color(101, 255, 0, 75));
        g2.fillRect(controller.getHero().getPosition().x * sizeSquare, controller.getHero().getPosition().y * sizeSquare, sizeSquare, sizeSquare);
        g2.drawImage(img, controller.getHero().getPosition().x * sizeSquare + (sizeSquare >> 2), controller.getHero().getPosition().y * sizeSquare, this);
    }

    private void drawEnemies(Graphics2D g2) {
        ArrayList<Enemy> enemies = controller.getEnemies();
        String pathToResources = Resources.findPathToResources();

        for (Enemy enemy : enemies) {
            Image image = getToolkit().getImage(pathToResources.concat("characters/enemy")
                    .concat(String.valueOf(enemy.getNumImg())).concat(".png"));

            prepareImage(image, this);
            g2.drawImage(image, enemy.getPosition().x * sizeSquare + (sizeSquare >> 2), enemy.getPosition().y * sizeSquare, this);
        }
    }
}