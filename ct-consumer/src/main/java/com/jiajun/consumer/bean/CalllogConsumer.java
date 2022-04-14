package com.jiajun.consumer.bean;

import com.jiajun.common.bean.Consumer;
import com.jiajun.common.constant.Names;
import com.jiajun.consumer.dao.HBaseDao;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.io.IOException;
import java.util.Arrays;
import java.util.Properties;

public class CalllogConsumer implements Consumer {
    public void consume() {

        try {
            //创建配置对象
            Properties properties = new Properties();
            properties.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("consumer.properties"));
            //获取flume采集的数据
            KafkaConsumer<String,String> consumer = new KafkaConsumer<String, String>(properties);

            //关注主题
            consumer.subscribe(Arrays.asList(Names.TOPIC.getValue()));
            HBaseDao dao = new HBaseDao();
            dao.init();

            System.out.println("开始消费！");
            //消费数据
            while( true ){


                ConsumerRecords<String, String> poll = consumer.poll(100);

                for (ConsumerRecord<String, String> consumerRecord : poll) {

                    System.out.println(consumerRecord.value());
                    //不直接传一个长字符串过去，而是插入对象
                    //dao.insertData(consumerRecord.value());

                    Calllog calllog = new Calllog(consumerRecord.value());
                    dao.inserData(calllog);
                }
            }


        } catch ( Exception e) {
            e.printStackTrace();
        }





    }

    public void close() throws IOException {

    }
}
