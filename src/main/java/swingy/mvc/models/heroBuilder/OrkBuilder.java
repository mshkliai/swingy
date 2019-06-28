package swingy.mvc.models.heroBuilder;

import swingy.mvc.models.Hero;

public class OrkBuilder implements IBuilder {
    public void buildDefaultStats(Hero hero) {
        hero.setType("ork");
        hero.setLevel(1);
        hero.setExp(0);
        hero.setAttack(15);
        hero.setDefense(4);
        hero.setMaxHp(80);
        hero.setHP(80);
    }
}