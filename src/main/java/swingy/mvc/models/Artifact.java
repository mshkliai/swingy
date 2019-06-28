package swingy.mvc.models;

public class Artifact {
    private int value;
    private String type;

    public Artifact(String type, int value) {
        this.type = type;
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public String getType() {
        return type;
    }
}
