package com.jiajun.producer.bean;

import com.jiajun.common.bean.DataInput;
import com.jiajun.common.bean.DataOut;
import com.jiajun.common.bean.Producer;
import com.jiajun.common.util.DateUtil;
import com.jiajun.common.util.NumberUtil;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * 本地数据文件生产者
 */
public class LocalFileProducer implements Producer {

    private DataInput in;
    private DataOut out;
    private volatile boolean flag = true;

    public void setInput(DataInput input) {
        this.in = input;
    }

    public void setOut(DataOut out) {
        this.out = out;
    }

    //生产数据
    public void produce() {

        System.out.println("hhhhhhhhhhhhhhhh");
        //读取通讯录数据
        try {
            List<Contactor> contactors = in.read(Contactor.class);

            while(flag){
                //从通讯录中随机查找2个电话号码
                int call1 = new Random().nextInt(contactors.size());
                int call2;
                while(true){ //保证不相等
                    call2 = new Random().nextInt(contactors.size());
                    if(call1 != call2){
                        break;
                    }
                }
                Contactor caller = contactors.get(call1);
                Contactor receiver = contactors.get(call2);

               //生成随机的通话时间
                String startDate = "20220101000000";
                String endDate = "20230101000000";

                long startTime = DateUtil.parse(startDate,"yyyyMMddHHmmss").getTime();
                long endTime = DateUtil.parse(endDate,"yyyyMMddHHmmss").getTime();
                long calltime = startTime + (long)((endTime - startTime) * Math.random());
                String callTimeString = DateUtil.format(new Date(calltime),"yyyyMMddHHmmss");

                //生成随机的通话时长
                String duration = NumberUtil.format(new Random().nextInt(3000),4);

                //生成通话记录
                CallLog log = new CallLog(caller.getTel(),receiver.getTel(),callTimeString,duration);

                System.out.println(log);
                //将通话记录写到文件中
                out.write(log);

                Thread.sleep( 500 );
            }
        }catch ( Exception e){
            e.printStackTrace();
        }


    }

    public void close() throws IOException {
        if( in!=null ){
            in.close();
        }

        if( out!=null ){
            out.close();
        }
    }
}
