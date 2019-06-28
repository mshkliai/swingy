package swingy.mvc.models;

import java.awt.*;

public class Enemy {
      private int   hp;
      private int   attack;
      private int   defense;
      private int   numImg;
      private Point position;

      public int getHp() {
            return hp;
      }

      public void setHp(int hp) {
            this.hp = hp;
      }

      public int getAttack() {
            return attack;
      }

      public void setAttack(int attack) {
            this.attack = attack;
      }

      public int getDefense() {
            return defense;
      }

      public void setDefense(int defense) {
            this.defense = defense;
      }

      public int getNumImg() {
            return numImg;
      }

      public void setNumImg(int numImg) {
            this.numImg = numImg;
      }

      public Point getPosition() {
            return position;
      }

      public void setPosition(Point position) {
            this.position = position;
      }
}