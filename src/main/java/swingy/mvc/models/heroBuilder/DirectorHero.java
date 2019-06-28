package swingy.mvc.models.heroBuilder;

import swingy.mvc.models.Artifact;
import swingy.mvc.models.Hero;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.ValidatorFactory;
import java.sql.ResultSet;
import java.util.logging.Level;

public class DirectorHero {
    private IBuilder builder;

    public DirectorHero() {
        builder = null;
    }

    public Hero buildbyType(String type) {
        Hero newHero = new Hero();

        switch (type) {
            case "Human": this.builder = new HumanBuilder(); break;
            case "Ork":   this.builder = new OrkBuilder();   break;
            case "Elf":   this.builder = new ElfBuilder();   break;
        }
        builder.buildDefaultStats(newHero);
        return newHero;
    }

    public String   trySetName(Hero hero, String newName) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();

        hero.setName(newName);
        java.util.logging.Logger.getLogger("org.hibernate").setLevel(Level.OFF);

        for (ConstraintViolation<Hero> violation : factory.getValidator().validate(hero)) {
            return violation.getMessage();
        }
        return "";
    }

    public Hero buildByInfo(ResultSet info) throws Exception {
        Hero newHero = new Hero();

        newHero.setName(info.getString("name"));
        newHero.setType(info.getString("type"));
        newHero.setArtifact( new Artifact(info.getString("artifactT"), info.getInt("artifactV")) );
        newHero.setAttack(info.getInt("attack") );
        newHero.setDefense(info.getInt("defense"));
        newHero.setExp(info.getInt("exp"));
        newHero.setLevel(info.getInt("level"));
        newHero.setMaxHp(info.getInt("maxHp"));
        newHero.setHP(info.getInt("hp"));

        return newHero;
    }
}