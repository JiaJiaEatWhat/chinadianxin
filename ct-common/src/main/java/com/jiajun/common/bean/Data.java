package com.jiajun.common.bean;

public abstract class Data implements Val {

    public String content;


    public void setValue(Object val) {
        content = (String) val;
    }

    public String getValue() {
        return content;
    }
}
