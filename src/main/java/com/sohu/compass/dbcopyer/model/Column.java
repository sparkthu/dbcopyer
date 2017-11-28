package com.sohu.compass.dbcopyer.model;

/**
 * Created by sparkchen on 2017/11/27.
 */
public class Column {
    private String name = "";
    private int indexInResult = -1;
    private String type = "string";  // number int long float double enum
    private String groovy = "";

    public boolean isString() {
        return type.equals("string") || type.equals("enum");
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIndexInResult() {
        return indexInResult;
    }

    public void setIndexInResult(int indexInResult) {
        this.indexInResult = indexInResult;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getGroovy() {
        return groovy;
    }

    public void setGroovy(String groovy) {
        this.groovy = groovy;
    }
}
