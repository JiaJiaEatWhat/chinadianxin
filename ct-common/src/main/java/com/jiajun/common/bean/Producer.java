package com.jiajun.common.bean;

//生产者接口

import java.io.Closeable;

public interface Producer extends Closeable {

    public void setInput(DataInput input);
    public void setOut(DataOut out);




    //生产数据
    public void produce();
}
