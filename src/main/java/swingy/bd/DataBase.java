package swingy.bd;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import swingy.mvc.models.Artifact;
import swingy.mvc.models.heroBuilder.DirectorHero;
import swingy.mvc.models.myHero;

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
        this.connectionString = "jdbc:sqlite:../heroes.db";
        this.connection = null;
    }

    public static DataBase  getDb()
    {
        if (db == null)
            db = new DataBase();

        return db;
    }

    public void   connectDb() throws Exception
    {
        if (this.connection == null)
        {
            Class.forName(this.driverName);
            this.connection = DriverManager.getConnection(this.connectionString);

            statm = this.connection.createStatement();
            statm.execute("CREATE  TABLE if not EXISTS 'heroes' ('name' text, 'type' text, 'level' INT, 'exp' INT," +
                    "'attack' INT, 'defense' INT, 'hp' INT, 'maxHp' INT, 'artifactT' text, 'artifactV' INT);");
        }
    }

    public List<String> getNames() throws Exception
    {
        info = statm.executeQuery("SELECT * FROM heroes");
        List<String> names = new ArrayList<>();

        while (info.next())
            names.add(info.getString("name"));

        return names;
    }

    public void        addNewHero(myHero newHero) throws Exception
    {
        String artifactType = newHero.getArtifact() == null ? "" : newHero.getArtifact().getType();
        int    artifactValue = artifactType == "" ? 0 : newHero.getArtifact().getValue();

        String  requestAdd = "VALUES ('" + newHero.getName() + "', '" + newHero.getType() + "', " + newHero.getLevel() + "," +
                newHero.getExp() + "," + newHero.getAttack() + "," + newHero.getDefense() + "," + newHero.getHitP() + ","
                + newHero.getMaxHp() + ",'" + artifactType + "'," + artifactValue + ");";

        statm.execute("INSERT INTO 'heroes' ('name', 'type', 'level', 'exp', 'attack', 'defense', 'hp', 'maxHP', 'artifactT', 'artifactV')" + requestAdd );
    }

    public void        remove(String name) throws Exception
    {
        statm.execute("DELETE FROM heroes WHERE name = '" + name + "';");
    }

    public myHero        getHero(String name) throws Exception
    {
        info = statm.executeQuery("SELECT * FROM heroes where name = '" + name + "';");

        return info.next() ? new DirectorHero().buildByInfo(info) : null;
    }

    public void         updateHero(myHero hero)
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