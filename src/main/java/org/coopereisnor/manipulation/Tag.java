package org.coopereisnor.manipulation;

public class Tag {
    private String filter;
    private String attribute;
    private boolean type;

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

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    public void setType(boolean type) {
        this.type = type;
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
