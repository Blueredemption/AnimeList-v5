package org.coopereisnor.manipulation;

public class Tag {
    private final String attribute;
    private final String value;
    private final boolean type;

    public Tag(String attribute, String value, boolean type){
        this.attribute = attribute;
        this.value = value;
        this.type = type;
    }

    public String getAttribute() {
        return attribute;
    }

    public String getValue() {
        return value;
    }

    public boolean isType() {
        return type;
    }
}
