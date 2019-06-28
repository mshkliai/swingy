package swingy.mvc.models.heroBuilder;

import swingy.mvc.models.Hero;

public class ElfBuilder implements IBuilder {
    public void buildDefaultStats(Hero hero) {
        hero.setType("elf");
        hero.setLevel(1);
        hero.setExp(0);
        hero.setAttack(3);
        hero.setDefense(10);
        hero.setMaxHp(150);
        hero.setHP(150);
    }
}