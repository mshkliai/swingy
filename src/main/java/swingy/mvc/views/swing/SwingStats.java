package swingy.mvc.views.swing;

import javax.swing.*;
import swingy.mvc.models.myHero;
import lombok.Setter;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SwingStats extends JPanel
{
    @Setter private myHero      hero;
    private Map<String, JLabel> stats;
    private Font                font;
    private String              res;

    /***************** Constructor *********************/

    public SwingStats(myHero hero)
    {
        this.hero = hero;
        this.stats = new HashMap<>();
        this.res = "../resources/icons/";

        this.setLayout(null);
        this.setSize(325, 500);
        this.setLocation(50, 50);
    }

    @Override
    public void paint(Graphics g)
    {
        super.paint(g);

        g.drawRect(1, 1, 323, 498);
    }

    /********************* Public method *********************/

    public void updateData() {
        if (stats.size() == 0)
            this.prepareInfo();

        stats.get("name").setText("Name: " + (hero.getName() == null ? "" : hero.getName()) );
        stats.get("type").setText("Type: " + hero.getType());

        if (hero.getPosition() != null) {
            stats.get("location").setText("Location: [" + hero.getPosition().x + ", " + hero.getPosition().y + "]");
        }

        stats.get("level").setText("Level: " + hero.getLevel());
        stats.get("exp").setText("Exp: " + hero.getExp() + "/" + hero.getNeccesaryExp());
        stats.get("attack").setText(String.valueOf(hero.getAttack()));
        stats.get("defense").setText(" " + hero.getDefense());
        stats.get("hp").setText(" " + hero.getHitP() + "/" + hero.getMaxHp());

        if (hero.getArtifact() != null && !hero.getArtifact().getType().equals(""))
        {
            stats.get("artifact").setIcon(new ImageIcon(
                    res + (hero.getArtifact().getType() == "attack" ? "artifactA.png" : "artifactD.png") ));
            stats.get("artifact").setText(" " + hero.getArtifact().getValue());
        }
        else {
            stats.get("artifact").setText("");
            stats.get("artifact").setIcon(null);
        }
    }

    /****************** Preparing Objects to painting *****************/

    private void prepareInfo()
    {
        this.loadFont();

        JLabel name = new JLabel("Name: ");
        name.setLocation(20, 5);
        name.setSize(225, 75);
        name.setFont(this.font);
        if (hero.getName() != null) {
            name.setText("Name: " + hero.getName());
        }

        JLabel type = new JLabel("Type: " + hero.getType());
        type.setLocation(20, 55);
        type.setSize(150, 50);
        type.setFont(this.font);

        JLabel level = new JLabel("Level: " + hero.getLevel());
        level.setLocation(20, 105);
        level.setSize(150, 50);
        level.setFont(this.font);

        JLabel location = new JLabel("Location: [ ]");
        location.setLocation(20, 155);
        location.setSize(200, 50);
        location.setFont(this.font);
        if (hero.getPosition() != null) {
            location.setText("Location: [" + hero.getPosition().x + ", " + hero.getPosition().y + "]");
        }

        JLabel exp = new JLabel("Exp: " + hero.getExp() + "/" + hero.getNeccesaryExp() );
        exp.setLocation(20, 205);
        exp.setSize(320, 50);
        exp.setFont(this.font);

        JLabel attack = new JLabel( String.valueOf(hero.getAttack()), new ImageIcon(res + "sword.png"), JLabel.LEFT );
        attack.setLocation(20, 260);
        attack.setSize(200, 50);
        attack.setFont(this.font);

        JLabel defense = new JLabel( " " + hero.getDefense(), new ImageIcon(res + "shield.png"), JLabel.LEFT );
        defense.setLocation(15, 315);
        defense.setSize(200, 50);
        defense.setFont(this.font);

        JLabel hp = new JLabel( " " + hero.getHitP() + "/" + this.hero.getMaxHp(), new ImageIcon(res + "hp.png"), JLabel.LEFT);
        hp.setLocation(15, 370);
        hp.setSize(320, 50);
        hp.setFont(this.font);
        hp.setVerticalTextPosition(JLabel.NORTH);

        JLabel artifact = new JLabel("");
        if (hero.getArtifact() != null)
            artifact.setIcon( new ImageIcon(res + hero.getArtifact().getType() == "attack" ? "artifactA.png" : "artifactD.png") );
        artifact.setLocation(15, 430);
        artifact.setSize(200, 50);
        artifact.setFont(this.font);

        this.stats.put("name", name);
        this.stats.put("type", type);
        this.stats.put("level", level);
        this.stats.put("location", location);
        this.stats.put("attack", attack);
        this.stats.put("defense", defense);
        this.stats.put("hp", hp);
        this.stats.put("exp", exp);
        this.stats.put("artifact", artifact);

        this.add(name);
        this.add(type);
        this.add(level);
        this.add(location);
        this.add(exp);
        this.add(attack);
        this.add(defense);
        this.add(hp);
        this.add(artifact);
    }

    private void loadFont()
    {
        try {
            this.font = Font.createFont(Font.TRUETYPE_FONT, new File("../resources/fonts/font.ttf")).deriveFont(30f);
        } catch (FontFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
