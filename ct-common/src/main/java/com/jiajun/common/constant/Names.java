package com.jiajun.common.constant;

import com.jiajun.common.bean.Val;

public enum Names implements Val {
    NAMESPACE("ct");

    private String name;

    private Names(String name){
        this.name = name;
    }

    public void setValue(Object val) {
        this.name = (String)val;
    }

    public Object getValue() {
        return null;
    }
}
