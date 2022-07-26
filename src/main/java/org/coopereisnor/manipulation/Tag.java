package org.coopereisnor.manipulation;

public class Tag {
    private final String filter;
    private final String attribute;
    private final boolean type;

    public Tag(String filter, String attribute, boolean type){
        this.filter = filter;
        this.attribute = attribute;
        this.type = type;
    }

    public String getFilter() {
        return filter;
    }

    public String getAttribute() {
        return attribute;
    }

    public boolean isType() {
        return type;
    }

    @Override
    public boolean equals(Object o){
        if(o.getClass().equals(this.getClass())){
            Tag t = (Tag)o;
            return filter.equals(t.filter) && attribute.equals(t.attribute);
        }else{
            return false;
        }
    }
}
