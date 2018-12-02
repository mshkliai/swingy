package swingy.mvc.models;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Size;
import javax.validation.constraints.Pattern;
import java.awt.*;

public class myHero
{
    @Pattern(regexp = "^[0-9A-Za-z]+", message = "Only digits and letters in name")
    @Size(min = 4, max = 12, message = "Size of name must be 4-12 symbols length")
    @Getter @Setter private String  name;
    @Getter @Setter private String  type;
    @Getter @Setter private int     level;
    @Getter @Setter private int     exp;
    @Getter @Setter private int     attack;
    @Getter @Setter private int     defense;
    @Getter @Setter private int     maxHp;
    @Getter         private int     hitP;

    @Getter @Setter private Point   position;
    @Getter         private Point   oldPosition;

    @Getter @Setter private Artifact artifact;

    public myHero() {
        this.position = new Point(0, 0);
        this.oldPosition = new Point(0, 0);
        this.artifact = null;
    }

    public String getInfo()
    {
        return ("\n Type: " + type + "\n\n Level: " + level + "\n\n Exp: " + exp
        + "\n\n Attack: " + attack + "\n\n Defense: " + defense + "\n\n Hit points: " + hitP);
    }

    public void   move(int x, int y)
    {
        this.oldPosition.setLocation(this.position.x, this.position.y);
        this.position.setLocation(this.position.x + x, this.position.y + y);
    }

    public int    getNeccesaryExp()
    {
        return (int)(level * 1000 + Math.pow(level - 1, 2) * 450);
    }

    public void   setHitP(int hp)
    {
        if (hp < 0)
            hitP = 0;
        else if (hp > maxHp)
            hitP = maxHp;
        else
            hitP = hp;
    }

    public int    getFinalAttack()
    {
        if ( artifact != null && artifact.getType().equals("attack") )
            return ( (attack + artifact.getValue()) << 2 );

        return (attack << 2);
    }

    public int    getFinalDefense()
    {
        if ( artifact != null && artifact.getType().equals("defense") )
            return ( defense + artifact.getValue() );

        return defense;
    }
}