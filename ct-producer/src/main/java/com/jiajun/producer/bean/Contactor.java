package com.jiajun.producer.bean;

import com.jiajun.common.bean.Data;

//联系人
public class Contactor extends Data {
    private String tel;
    private String name;

    public String getTel() {
        return tel;
    }


    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setValue(Object val) {
        content = (String)val;
        String[] values = content.split("\t");

        setTel(values[0]);
        setName(values[1]);
    }

    @Override
    public String toString() {
        return "Contactor{" +
                "tel='" + tel + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
