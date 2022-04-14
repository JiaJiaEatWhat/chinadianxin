package com.jiajun.consumer;

import com.jiajun.common.bean.Consumer;
import com.jiajun.consumer.bean.CalllogConsumer;

public class Bootstrap{
    public static void main(String[] args) throws Exception{

        //创建消费者
        Consumer consumer = new CalllogConsumer();

        //消费数据
        consumer.consume();

        //关闭资源
        consumer.close();

    }
}
