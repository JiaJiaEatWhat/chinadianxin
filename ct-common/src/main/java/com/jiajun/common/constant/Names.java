package com.jiajun.common.constant;

import com.jiajun.common.bean.Val;

public enum Names implements Val {
    NAMESPACE("ct")
    , TABLE("ct:calllog")
    ,CF_CALLER("caller")
    ,CF_INFO("info")
    ,TOPIC("ct");

    private String name;

    private Names(String name){
        this.name = name;
    }

    public void setValue(Object val) {
        this.name = (String)val;
    }

    public String getValue() {
        return name;
    }
}
