package com.jiajun.consumer.bean;


//通话日志对象

import com.jiajun.common.api.Column;
import com.jiajun.common.api.Rowkey;
import com.jiajun.common.api.TableRef;

@TableRef("ct:calllog")
public class Calllog {

    @Rowkey
    private String rowkey;
    @Column(family = "caller")
    private String call1;
    @Column(family = "caller")
    private String call2;
    @Column(family = "caller")
    private String calltime;
    @Column(family = "caller")
    private String duration;

    private String name;

    public Calllog() {

    }

    public Calllog(String data) {

        String[] datas = data.split("\t");

        this.call1 = datas[0];
        this.call2 = datas[1];
        this.calltime = datas[2];
        this.duration = datas[3];
    }

    public String getCall1() {
        return call1;
    }

    public void setCall1(String call1) {
        this.call1 = call1;
    }

    public String getCall2() {
        return call2;
    }

    public void setCall2(String call2) {
        this.call2 = call2;
    }

    public String getCalltime() {
        return calltime;
    }

    public void setCalltime(String calltime) {
        this.calltime = calltime;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getRowkey() {
        return rowkey;
    }

    public void setRowkey(String rowkey) {
        this.rowkey = rowkey;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
