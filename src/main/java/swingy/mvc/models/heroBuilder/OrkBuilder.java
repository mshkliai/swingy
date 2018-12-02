package swingy.mvc.models.heroBuilder;

import swingy.mvc.models.myHero;

public class OrkBuilder implements IBuilder {
    public void buildDefaultStats(myHero hero)
    {
        hero.setType("ork");
        hero.setLevel(1);
        hero.setExp(0);
        hero.setAttack(15);
        hero.setDefense(4);
        hero.setMaxHp(80);
        hero.setHitP(80);
    }
}
