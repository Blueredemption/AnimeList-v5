package org.coopereisnor.manipulation;

public class Wildcard {
    private String description;
    private String value;
    private Pair pair;

    public Wildcard(String description, String value, Pair pair) {
        this.description = description;
        this.value = value;
        this.pair = pair;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Pair getPair() {
        return pair;
    }

    public void setPair(Pair pair) {
        this.pair = pair;
    }
}
