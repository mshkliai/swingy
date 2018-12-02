package swingy.mvc.models;

import lombok.Getter;
import lombok.Setter;

import java.awt.*;

public class Enemy
{
    @Getter @Setter private int   hp;
    @Getter @Setter private int   attack;
    @Getter @Setter private int   defense;
    @Getter @Setter private int   numImg;
    @Getter @Setter private Point position;
}