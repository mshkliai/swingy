package swingy.mvc.models.heroBuilder;

import swingy.mvc.models.*;

public class HumanBuilder implements IBuilder {
    public void buildDefaultStats(Hero hero) {
        hero.setType("human");
        hero.setLevel(1);
        hero.setExp(0);
        hero.setAttack(10);
        hero.setDefense(3);
        hero.setMaxHp(100);
        hero.setHP(100);
    }
}
