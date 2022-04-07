package com.jiajun.producer;

import com.jiajun.common.bean.Producer;
import com.jiajun.producer.bean.LocalFileProducer;
import com.jiajun.producer.io.LocalFileDataIn;
import com.jiajun.producer.io.LocalFileDataOut;

import java.io.IOException;

public class BootStrap {
    public static void main(String[] args) throws IOException {

        //构建生产者对象
        Producer pro = new LocalFileProducer();

        pro.setInput(new LocalFileDataIn("E:\\我的文件\\contact.log"));
        pro.setOut(new LocalFileDataOut("E:\\我的文件\\call.log"));

        //生产数据
        pro.produce();

        //关闭对象
        pro.close();
    }
}
