package swingy.mvc.models;

import lombok.Getter;

public class Artifact
{
    @Getter private int value;
    @Getter private String type;

    public Artifact(String type, int value)
    {
        this.type = type;
        this.value = value;
    }
}
