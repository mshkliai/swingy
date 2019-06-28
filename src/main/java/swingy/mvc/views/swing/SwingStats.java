package swingy.mvc.views.swing;

import swingy.mvc.models.Hero;
import swingy.resources.Resources;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class SwingStats extends JPanel {
    private final String PATH_TO_ICONS;

    private Hero hero;
    private Map<String, JLabel> stats;

    public SwingStats(Hero hero) {
        PATH_TO_ICONS = Resources.findPathToResources().concat("icons/");
        this.hero = hero;
        stats = new HashMap<>();

        setLayout(null);
        setSize(325, 500);
        setLocation(50, 50);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g.drawRect(1, 1, 323, 498);
    }

    public void updateData() {
        if (stats.isEmpty()) {
            prepareInfo();
        }

        stats.get("name").setText("Name: ".concat(hero.getName() == null ? "" : hero.getName()));
        stats.get("type").setText("Type: ".concat(hero.getType()));

        if (hero.getPosition() != null) {
            stats.get("location").setText("Location: [".concat(String.valueOf(hero.getPosition().x)).concat(", ")
                    .concat(String.valueOf(hero.getPosition().y)).concat("]"));
        }

        stats.get("level").setText("Level: " + hero.getLevel());
        stats.get("exp").setText("Exp: " + hero.getExp() + "/" + hero.getNeccesaryExp());
        stats.get("attack").setText(String.valueOf(hero.getAttack()));
        stats.get("defense").setText(" " + hero.getDefense());
        stats.get("hp").setText(" " + hero.getHP() + "/" + hero.getMaxHp());

        if (hero.getArtifact() != null && !hero.getArtifact().getType().equals("")) {
            stats.get("artifact").setIcon(new ImageIcon(
                    PATH_TO_ICONS.concat(hero.getArtifact().getType().equals("attack") ? "artifactA.png" : "artifactD.png"))
            );
            stats.get("artifact").setText(" " + hero.getArtifact().getValue());
        }
        else {
            stats.get("artifact").setText("");
            stats.get("artifact").setIcon(null);
        }
    }

    private void prepareInfo() {
        JLabel name = new JLabel("Name: ");
        name.setLocation(20, 5);
        name.setSize(225, 75);
        name.setFont(Resources.bigFont);
        if (hero.getName() != null) {
            name.setText("Name: " + hero.getName());
        }

        JLabel type = new JLabel("Type: " + hero.getType());
        type.setLocation(20, 55);
        type.setSize(150, 50);
        type.setFont(Resources.bigFont);

        JLabel level = new JLabel("Level: " + hero.getLevel());
        level.setLocation(20, 105);
        level.setSize(150, 50);
        level.setFont(Resources.bigFont);

        JLabel location = new JLabel("Location: [ ]");
        location.setLocation(20, 155);
        location.setSize(200, 50);
        location.setFont(Resources.bigFont);
        if (hero.getPosition() != null) {
            location.setText("Location: [" + hero.getPosition().x + ", " + hero.getPosition().y + "]");
        }

        JLabel exp = new JLabel("Exp: " + hero.getExp() + "/" + hero.getNeccesaryExp() );
        exp.setLocation(20, 205);
        exp.setSize(320, 50);
        exp.setFont(Resources.bigFont);

        JLabel attack = new JLabel( String.valueOf(hero.getAttack()), new ImageIcon(PATH_TO_ICONS.concat("sword.png")), JLabel.LEFT );
        attack.setLocation(20, 260);
        attack.setSize(200, 50);
        attack.setFont(Resources.bigFont);

        JLabel defense = new JLabel( " " + hero.getDefense(), new ImageIcon(PATH_TO_ICONS.concat("shield.png")), JLabel.LEFT );
        defense.setLocation(15, 315);
        defense.setSize(200, 50);
        defense.setFont(Resources.bigFont);

        JLabel hp = new JLabel( " " + hero.getHP() + "/" + this.hero.getMaxHp(), new ImageIcon(PATH_TO_ICONS.concat("hp.png")), JLabel.LEFT);
        hp.setLocation(15, 370);
        hp.setSize(320, 50);
        hp.setFont(Resources.bigFont);
        hp.setVerticalTextPosition(JLabel.NORTH);

        JLabel artifact = new JLabel("");
        if (hero.getArtifact() != null)
            artifact.setIcon( new ImageIcon(PATH_TO_ICONS.concat(hero.getArtifact().getType().equals("attack") ? "artifactA.png" : "artifactD.png")));
        artifact.setLocation(15, 430);
        artifact.setSize(200, 50);
        artifact.setFont(Resources.bigFont);

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


    public Hero getHero() {
        return hero;
    }

    public void setHero(Hero hero) {
        this.hero = hero;
    }
}