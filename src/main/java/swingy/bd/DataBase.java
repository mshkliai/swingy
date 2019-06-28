package swingy.bd;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import swingy.mvc.models.heroBuilder.DirectorHero;
import swingy.mvc.models.Hero;

public class DataBase
{
    private static DataBase db = null;

    private static Statement    statm;
    private static ResultSet    info;
    private final String        driverName;
    private final String        connectionString;
    private Connection          connection;

    private DataBase()
    {
        this.driverName  = "org.sqlite.JDBC";
        this.connectionString = "jdbc:sqlite:".concat(System.getProperty("user.dir")).concat("/heroes.db");
        this.connection = null;
    }

    public static DataBase  getDb()
    {
        if (db == null)
            db = new DataBase();

        return db;
    }

    public void connectDb()
    {
        if (connection == null)
        {
            try {
                Class.forName(driverName);

                connection = DriverManager.getConnection(connectionString);
                statm = connection.createStatement();
                statm.execute("CREATE  TABLE if not EXISTS 'heroes' ('name' text, 'type' text, 'level' INT, 'exp' INT," +
                        "'attack' INT, 'defense' INT, 'hp' INT, 'maxHp' INT, 'artifactT' text, 'artifactV' INT);");
            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public List<String> getNames() {
        List<String> names = new ArrayList<>();

        try {
            info = statm.executeQuery("SELECT * FROM heroes");
            while (info.next())
                names.add(info.getString("name"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return names;
    }

    public void        addNewHero(Hero newHero)
    {
        String artifactType = newHero.getArtifact() == null ? "" : newHero.getArtifact().getType();
        int    artifactValue = artifactType == "" ? 0 : newHero.getArtifact().getValue();

        String  requestAdd = "VALUES ('" + newHero.getName() + "', '" + newHero.getType() + "', " + newHero.getLevel() + "," +
                newHero.getExp() + "," + newHero.getAttack() + "," + newHero.getDefense() + "," + newHero.getHP() + ","
                + newHero.getMaxHp() + ",'" + artifactType + "'," + artifactValue + ");";

        try {
            statm.execute("INSERT INTO 'heroes' ('name', 'type', 'level', 'exp', 'attack', 'defense', 'hp', 'maxHP', 'artifactT', 'artifactV')" + requestAdd );
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void        remove(String name)
    {
        try {
            statm.execute("DELETE FROM heroes WHERE name = '" + name + "';");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Hero getHero(String name)
    {
        try {
            info = statm.executeQuery("SELECT * FROM heroes where name = '" + name + "';");
            if (info.next()) {
                return new DirectorHero().buildByInfo(info);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public void         updateHero(Hero hero)
    {
        String request = "UPDATE heroes SET level = " + hero.getLevel() + ", exp = " + hero.getExp() +
                ", attack = " + hero.getAttack() + ", defense = " + hero.getDefense() + ", hp = " + hero.getMaxHp() +
                ", maxHp = " + hero.getMaxHp() + ", artifactT = '" + ( hero.getArtifact() == null ? "" : hero.getArtifact().getType() ) +
                "' , artifactV = " + hero.getArtifact().getValue() + " WHERE name = '" + hero.getName() + "';";

        try {
            statm.execute(request);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}