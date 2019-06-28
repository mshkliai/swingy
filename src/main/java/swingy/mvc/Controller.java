package swingy.mvc;

import swingy.bd.DataBase;
import swingy.mvc.models.Artifact;
import swingy.mvc.models.Enemy;
import swingy.mvc.models.EnemyBuilder;
import swingy.mvc.models.Hero;
import swingy.mvc.views.IView;
import swingy.mvc.views.console.ConsoleView;
import swingy.mvc.views.swing.SwingView;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class Controller {
    private IView currentGui;

    private Hero hero;
    private int sizeMap = 0;
    private ArrayList<Enemy> enemies;
    private Random rand;

    public Controller() {
        enemies = new ArrayList<>();
        rand = new Random();
    }

    public  void    startGame(String argument) throws Exception
    {
        if ( !argument.equals("gui") && !argument.equals("console") )
            throw new IOException("bad argument [gui or console]");
        this.changeGui(argument);

        if (this.hero == null) {
            this.currentGui.ChooseHero();

            this.sizeMap = (this.hero.getLevel() - 1) * 5 + 10 - (this.hero.getLevel() % 2);
            this.initNewGame();
        }
        this.currentGui.drawGameObjects();
    }

    public void    keyPressed(int key)
    {
        this.handleKey(key);
        this.handleCollisions();

        this.currentGui.updateData();
        this.currentGui.viewRepaint();

        if (this.sizeMap > 9)
            this.currentGui.scrollPositionManager();
    }

    public void    saveHero()
    {
        if (currentGui.simpleDialog("Save your hero ?"))
            DataBase.getDb().updateHero(this.hero);
    }

    private void    handleKey(int key)
    {
        switch (key)
        {
            case 37:
                if (this.hero.getPosition().x - 1 >= 0)
                    this.hero.move(-1, 0);
                else
                    key = -1; break;
            case 38:
                if (this.hero.getPosition().y - 1 >= 0)
                    this.hero.move(0, -1);
                else key = -1; break;

            case 39:
                if (this.hero.getPosition().x + 1 < this.sizeMap)
                    this.hero.move(1, 0);
                else key = -1; break;

            case 40:
                if (this.hero.getPosition().y + 1 < this.sizeMap)
                    this.hero.move(0, 1);
                else key = -1; break;
            case -2:
                try {
                    String type = currentGui.get_Type().equals("gui") ? "console" : "gui";
                    currentGui.close();
                    this.startGame(type);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }

        if (key == -1)
        {
            if ( currentGui.simpleDialog("End of map, start a new game?") )
                this.initNewGame();
            else
            {
                this.saveHero();
                System.exit(0);
            }
        }
    }

    private void    handleCollisions()
    {
        for (int i = 0; i < this.enemies.size(); i++)
            if ( this.enemies.get(i).getPosition().equals( this.hero.getPosition() ) )
                this.manageBattle(this.enemies.get(i));
    }

    private void    initNewGame()
    {
        /* Hero initialization */

        this.hero.getPosition().setLocation( sizeMap >> 1, sizeMap >> 1);
        this.hero.setHP( hero.getMaxHp() );

        /* Enemies initialization */

        this.dropEnemies();
    }

    private void    manageBattle(Enemy enemy)
    {
        Point enemyPos = enemy.getPosition();

        this.currentGui.addLog("You met an opponent:\n    hp: " + enemy.getHp() + "\n    attack: "
                + enemy.getAttack() + "\n    defense: " + enemy.getDefense());

        if ( currentGui.simpleDialog("Do you want a battle ?") )
            this.battleAlgorithm(enemy);
        else
        {
            if ( rand.nextInt(2) % 2 == 0 ) {
                this.hero.setPosition(new Point(this.hero.getOldPosition()));
                this.currentGui.addLog("You are lucky to escape");
            }
            else
            {
                this.currentGui.addLog("Run away failed");
                this.battleAlgorithm(enemy);
            }
        }

        if ( !enemyPos.equals(hero.getPosition()) )
            return ;

        if (enemy.getHp() <= 0) {
            this.hero.setExp( hero.getExp() + ( (enemy.getAttack() + enemy.getDefense()) << 3 ) );
            this.currentGui.addLog("Opponent killed ! Raised " + (enemy.getAttack() + enemy.getDefense() << 3) + " experience !" );
            this.enemies.remove(enemy);
            this.manageMyHero(enemy);
        }
        else
            hero.setPosition(new Point(hero.getOldPosition()));
    }

    private void    battleAlgorithm(Enemy enemy)
    {
        if ( rand.nextInt(7) == 6 )
        {
            enemy.setHp(0);
            this.currentGui.addLog("Critical hit !!!");
        }
        else
        {
            this.hero.setHP( hero.getHP() - (enemy.getAttack() << 2) + hero.getFinalDefense() );
            if ( this.checkDeath() )
                return;

            enemy.setHp( enemy.getHp() - hero.getFinalAttack() + enemy.getDefense() );

            int raisedDamage = (enemy.getAttack() << 2) - hero.getFinalDefense();

            this.currentGui.addLog("You caused " + ( hero.getFinalAttack() - enemy.getDefense() )
                    + " damage to the opponent !\n" + ( raisedDamage < 0 ? " Blocked up all" : " Raised " + raisedDamage ) + " damage." );
        }
    }

    private void    manageMyHero(Enemy enemy)
    {
        if ( hero.getExp() >= hero.getNeccesaryExp() )
        {
            currentGui.addLog("Level up ! Skills increased !");
            hero.setMaxHp( hero.getMaxHp() + (4 << hero.getLevel()) );
            hero.setHP( hero.getMaxHp() );
            hero.setAttack( hero.getAttack() + (hero.getLevel() << 2) );
            hero.setDefense( hero.getDefense() + (hero.getLevel() << 1) );
            hero.setLevel( hero.getLevel() + 1 );
            sizeMap = (hero.getLevel() - 1) * 5 + 10 - (hero.getLevel() % 2);
            this.dropEnemies();
        }
        this.manageBonuses(enemy);
    }

    private boolean   checkDeath()
    {
        if ( hero.getHP() <= 0 )
        {
            this.currentGui.updateData();

            if (currentGui.simpleDialog("You died, respawn at center of map ?"))
                this.initNewGame();
            else
            {
                this.saveHero();
                System.exit(0);
            }

            return true;
        }
        return false;
    }

    private void    manageBonuses(Enemy enemy)
    {
        if (rand.nextInt(3) == 2)
        {
            if (rand.nextInt(2) == 0) {
                int up = rand.nextInt(30) + 5;
                hero.setHP(hero.getHP() + up);
                currentGui.addLog("Found health elixir + " + up + " hp !");
            }
            else
                this.manageArtifacts(enemy);
        }
    }

    private void    dropEnemies()
    {
        this.enemies.clear();

        EnemyBuilder enemyBuilder = new EnemyBuilder();

        for (int i = rand.nextInt(sizeMap) + sizeMap; i > 0; i--)
            enemies.add(enemyBuilder.buildEnemy(sizeMap, enemies, hero));
    }

    private void  manageArtifacts(Enemy enemy)
    {
        String artifact = rand.nextInt(2) == 0 ? "attack" : "defense";
        int    value = (( artifact.equals("attack") ? enemy.getAttack() : enemy.getDefense() ) >> 1) + 1;

        if ( currentGui.simpleDialog("Found " + artifact + " artifact (" + value + ") pick it up ?") ) {
            hero.setArtifact( new Artifact(artifact, value) );
            currentGui.addLog("New artifact equipped");
        }
    }

    private void    changeGui(String guiName)
    {
        this.currentGui = guiName.equals("gui") ? new SwingView(this) : new ConsoleView(this);
    }

    public Hero getHero() {
        return hero;
    }

    public void setHero(Hero hero) {
        this.hero = hero;
    }

    public int getSizeMap() {
        return sizeMap;
    }

    public ArrayList<Enemy> getEnemies() {
        return enemies;
    }
}