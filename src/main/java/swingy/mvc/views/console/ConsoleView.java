package swingy.mvc.views.console;

import lombok.Getter;
import org.apache.commons.lang.StringUtils;
import swingy.bd.DataBase;
import swingy.mvc.Controller;
import swingy.mvc.models.heroBuilder.DirectorHero;
import swingy.mvc.models.Enemy;
import swingy.mvc.views.IView;

import java.io.IOException;
import java.util.*;

public class ConsoleView implements IView
{
    private Controller            controller;
    private Scanner               scanner;
    private ArrayList<char[]>     map;
    private int                   numStat;
    private String                type;

    public ConsoleView(Controller controller)
    {
        this.controller = controller;
        this.scanner = new Scanner(System.in);
        this.map = new ArrayList<>();
        this.numStat = 0;
        this.type = "console";
    }

    @Override
    public void ChooseHero() throws Exception {
        controller.setHero( new ConsoleChooseHero(scanner).getHero() );
    }

    @Override
    public void drawGameObjects()
    {
        this.drawMap();
        System.out.println("\n0) Exit\n\n     1) North\n2) West     3) East\n     4) South\n\"gui\" - for gui-mode");
        controller.keyPressed( this.getNiceValue() );
    }

    @Override
    public void viewRepaint(){
        this.drawGameObjects();
    }

    @Override
    public boolean simpleDialog(String message)
    {
        System.out.println(message + "\n 1) Yes     2) No");

        int key;

        while ( ( key = this.getNiceValue() ) != 38 && key != 37 )
            System.err.println("Unknown value.");

        return (key == 38);
    }

    @Override
    public void   scrollPositionManager(){}

    @Override
    public void   updateData(){}

    @Override
    public void   addLog(String text) {
        System.out.println(text);
    }

    @Override
    public String get_Type() { return this.type; }

    @Override
    public void   close()
    {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    private void    drawMap()
    {
        char[] buff = new char[controller.getSizeMap()];
        Arrays.fill(buff, '.');

        map.clear();
        for (int i = 0; i < controller.getSizeMap(); i++)
            map.add( buff.clone() );

        map.get(controller.getHero().getPosition().y)[controller.getHero().getPosition().x] = 'H';
        for (Enemy enemy : controller.getEnemies())
            map.get(enemy.getPosition().y)[enemy.getPosition().x] = 'E';

        numStat = 0;
        for (char[] str : map)
            System.out.println( "     " + String.valueOf(str) + this.getStat(numStat++) );
    }

    private int    getNiceValue()
    {
        String str;

        while (true)
        {
            str = "";

            while (str.equals(""))
            {
                try
                {
                    str = scanner.nextLine();
                }
                catch (Exception e) {
                    System.err.println("CTRL+D is bad!");
                    System.exit(0);
                }
            }

            if (str.equals("gui"))
                return -2;
            else if (!str.matches("^[0-9]+"))
                System.err.println("Enter nice value !");
            else
                break;
        }

        int value = -3;
        switch ( Integer.parseInt(str) )
        {
            case 1: value = 38;                                  break;
            case 2: value = 37;                                  break;
            case 3: value = 39;                                  break;
            case 4: value = 40;                                  break;
            case 0: controller.saveHero();System.exit(0); break;
            case -2: value = -2;                                 break;
        }
        return value;
    }

    private String  getStat(int numStat)
    {
        if (numStat > 9)
            return "";

        String stat = "       ";

        switch (numStat)
        {
            case 0: stat += "Name: " + controller.getHero().getName();          break;
            case 1: stat += "Type: " + controller.getHero().getType();          break;
            case 2: stat += "Level: " + controller.getHero().getLevel();        break;
            case 3: stat += "Location [" + controller.getHero().getPosition().x + ", "
                    + controller.getHero().getPosition().y + "]";               break;
            case 4: stat += "Exp: " + controller.getHero().getExp() + "/" +
                    controller.getHero().getNeccesaryExp();                     break;
            case 5: stat += "Attack: " + controller.getHero().getAttack();      break;
            case 6: stat += "Defense: " + controller.getHero().getDefense();    break;
            case 7: stat += "Hp: " + controller.getHero().getHitP() + "/"
                    + controller.getHero().getMaxHp();                          break;
            case 8: if ( controller.getHero().getArtifact() != null && !controller.getHero().getArtifact().getType().equals("") ) {
                stat += "Artifact-" + controller.getHero().getArtifact().getType() + ": " +
                        controller.getHero().getArtifact().getValue();
            }                                                                   break;
        }

        return stat;
    }
}
