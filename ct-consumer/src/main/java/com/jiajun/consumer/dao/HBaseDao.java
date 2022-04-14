package com.jiajun.consumer.dao;

import com.jiajun.common.bean.BaseDao;
import com.jiajun.common.constant.Names;
import com.jiajun.common.constant.ValueConstant;
import com.jiajun.consumer.bean.Calllog;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;

//HBase数据访问对象
public class HBaseDao extends BaseDao {
    //初始化
    public void init() throws Exception{
        start();

        createNameSpaceNX(Names.NAMESPACE.getValue());
        createTableXX(Names.TABLE.getValue(), ValueConstant.REGION_COUNT,Names.CF_CALLER.getValue());

        end();
    }

    //插入对象
    public void inserData(Calllog log) throws Exception{
        log.setRowkey(genRegionNum(log.getCall1(),log.getCalltime()) + "_" + log.getCall1() + "_" + log.getCalltime() + "_" + log.getCall2());
        putData(log);
    }

    //插入数据
    public void insertData(String value) throws Exception{

        //将通话日志保存到HBase表中

        String[] values = value.split("\t");
        String call1 = values[0];
        String call2 = values[1];
        String calltime = values[2];
        String duration = values[3];

        //rowkey设计
        //rowkey = regionNum + call1 + time + call2 + duration
        String rowkey = "";
        rowkey = genRegionNum(call1,calltime) + "_" + call1 + "_" + calltime + "_" + call2;
        Put put = new Put(Bytes.toBytes(rowkey));

        byte[] family = Bytes.toBytes(Names.CF_CALLER.getValue());

        put.addColumn(family,Bytes.toBytes("call1"),Bytes.toBytes(call1));
        put.addColumn(family,Bytes.toBytes("call2"),Bytes.toBytes(call2));
        put.addColumn(family,Bytes.toBytes("calltime"),Bytes.toBytes(calltime));
        put.addColumn(family,Bytes.toBytes("duration"),Bytes.toBytes(duration));


        putData(Names.TABLE.getValue(),put);
    }
}
