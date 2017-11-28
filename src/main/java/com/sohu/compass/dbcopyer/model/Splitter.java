package com.sohu.compass.dbcopyer.model;

/**
 * Created by sparkchen on 2017/11/27.
 */
public class Splitter {
    public Splitter(String betweenValue, String betweenLine, String beforeLine, String afterLine, String wrapString) {
        this.betweenValue = betweenValue;
        this.betweenLine = betweenLine;
        this.beforeLine = beforeLine;
        this.afterLine = afterLine;
        this.wrapString = wrapString;
    }

    public Splitter() {
    }

    public String betweenValue = ",";
    public String  betweenLine = ",";
    public String  beforeLine = "(";
    public String  afterLine = ")";
    public String  wrapString = "'";
}
