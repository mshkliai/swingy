package swingy.mvc.models;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.awt.*;

public class Hero {
    @Pattern(regexp = "^[0-9A-Za-z]+", message = "Only digits and letters in name")
    @Size(min = 4, max = 12, message = "Size of name must be 4-12 symbols length")
    private String  name;
    private String  type;
    private int     level;
    private int     exp;
    private int     attack;
    private int     defense;
    private int     maxHp;
    private int     hp;

    private Point   position;
    private Point   oldPosition;

    private Artifact artifact;

    public Hero() {
        position = new Point(0, 0);
        oldPosition = new Point(0, 0);
        artifact = null;
    }

    public String getFormattedInfo() {
        String formattedString = "\n Type: ".concat(type)
                .concat("\n\n Level: ").concat(String.valueOf(level))
                .concat("\n\n Exp: ").concat(String.valueOf(exp))
                .concat("\n\n Attack: ").concat(String.valueOf(attack))
                .concat("\n\n Defense: ").concat(String.valueOf(defense))
                .concat("\n\n HP: ").concat(String.valueOf(hp));

        return formattedString;
    }

    public void move(int x, int y) {
        oldPosition.setLocation(position.x, position.y);
        position.setLocation(position.x + x, position.y + y);
    }

    public int getNeccesaryExp() {
        return (int)(level * 1000 + Math.pow(level - 1, 2) * 450);
    }

    public void   setHP(int hp) {
        if (hp < 0) {
            this.hp = 0;
        } else if (hp > maxHp) {
            this.hp = maxHp;
        } else {
            this.hp = hp;
        }
    }

    public int getFinalAttack() {
        return (artifact != null && artifact.getType().equals("attack")) ? ((attack + artifact.getValue()) << 2) : attack << 2;
    }

    public int getFinalDefense() {
        return (artifact != null && artifact.getType().equals("defense")) ? defense + artifact.getValue() : defense;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getExp() {
        return exp;
    }

    public void setExp(int exp) {
        this.exp = exp;
    }

    public int getAttack() {
        return attack;
    }

    public void setAttack(int attack) {
        this.attack = attack;
    }

    public int getDefense() {
        return defense;
    }

    public void setDefense(int defense) {
        this.defense = defense;
    }

    public int getMaxHp() {
        return maxHp;
    }

    public void setMaxHp(int maxHp) {
        this.maxHp = maxHp;
    }

    public int getHP() {
        return hp;
    }

    public Point getPosition() {
        return position;
    }

    public void setPosition(Point position) {
        this.position = position;
    }

    public Point getOldPosition() {
        return oldPosition;
    }

    public Artifact getArtifact() {
        return artifact;
    }

    public void setArtifact(Artifact artifact) {
        this.artifact = artifact;
    }
}