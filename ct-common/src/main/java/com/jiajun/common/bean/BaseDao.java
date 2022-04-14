package com.jiajun.common.bean;


import com.jiajun.common.api.Column;
import com.jiajun.common.api.Rowkey;
import com.jiajun.common.api.TableRef;
import com.jiajun.common.constant.Names;
import com.jiajun.common.constant.ValueConstant;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class BaseDao {

    private ThreadLocal<Connection> connHolder = new ThreadLocal<Connection>();
    private ThreadLocal<Admin> adminHolder = new ThreadLocal<Admin>();


    protected void start() throws Exception{
        getConnection();
        getAdmin();
    }

    protected void end() throws Exception{
        Admin admin = getAdmin();
        if(admin != null){
            admin.close();
            adminHolder.remove();
        }

        Connection conn = getConnection();
        if(conn != null){
            conn.close();
            connHolder.remove();
        }
    }

    //创建表，如果存在，删除后再创建
    protected void createTableXX(String tableName,String... familys) throws Exception {
        //创建表
        createTableXX(tableName,null,familys);
    }

    //创建表，如果存在，删除后再创建
    protected void createTableXX(String tableName,Integer regionCount,String... familys) throws Exception {
        Admin admin = getAdmin();

        TableName tn = TableName.valueOf(tableName);

        if( admin.tableExists(tn) ){
            System.out.println("表已经存在！进行删除后再创建!");
            //删除
            deleteTable(tableName);
        }

        //创建表
        createTable(tableName,regionCount,familys);
    }

    //创建表
    private void createTable(String tableName,Integer regionCount,String... familys) throws Exception{
        Admin admin = getAdmin();
        TableName tn = TableName.valueOf(tableName);

        HTableDescriptor tableDescriptor = new HTableDescriptor(tn);

        if( familys == null || familys.length == 0){
            familys = new String[1];
            familys[0] = Names.CF_INFO.getValue();
        }
        for (String family : familys) {
            HColumnDescriptor hColumnDescriptor = new HColumnDescriptor(family);
            tableDescriptor.addFamily(hColumnDescriptor);
        }

        //预分区
        if(regionCount == null || regionCount <= 0){
            admin.createTable(tableDescriptor);
        }else {
            byte[][] splitKeys = genSplitKeys(regionCount);
            admin.createTable(tableDescriptor,splitKeys);
        }

    }

    protected int genRegionNum(String tel,String date){

        //保证同一个号码在同一年同一个月的所有数据进一个分区就行
        String usercode = tel.substring(tel.length()-4);
        String yearMonth = date.substring(0,6);

        int userCodeHash = usercode.hashCode();
        int yearMonthHash = yearMonth.hashCode();

        int crc = Math.abs(userCodeHash ^ yearMonthHash);

        //取模
        int regionNum = crc % ValueConstant.REGION_COUNT;

        return regionNum;
    }

    //生成分区键
    private byte[][] genSplitKeys(int regionCount){
        int splitkeyCount = regionCount - 1;
        byte[][] bs = new byte[splitkeyCount][];

        //(-oo,0|),[0|,1|).[1|,+oo)
        List<byte[]> bsList = new ArrayList<byte[]>();
        for(int i=0;i<splitkeyCount;i++){
            String splitKey = i + "|";
            bsList.add(Bytes.toBytes(splitKey));
        }
        bsList.toArray(bs);

        return bs;
    }

    protected void putData(Object obj) throws Exception{

        //反射
        Class<?> clazz = obj.getClass();
        TableRef tableRef = clazz.getAnnotation(TableRef.class);
        String tableName = tableRef.value();

        Field[] fields = clazz.getDeclaredFields();
        String rowkey = "";
        for (Field field : fields) {
            Rowkey stringRowkey = field.getAnnotation(Rowkey.class);
            if(stringRowkey!=null){
                field.setAccessible(true);
                rowkey = (String) field.get(obj);
                break;
            }
        }
        Connection conn = getConnection();
        Table table = conn.getTable(TableName.valueOf(tableName));
        Put put = new Put(Bytes.toBytes(rowkey));

        for (Field field : fields) {
            Column column = field.getAnnotation(Column.class);
            if(column != null){
                String family = column.family();
                String colName = column.column();
                if(colName == null || "".equals(colName)){
                    colName = field.getName();
                }
                field.setAccessible(true);
                String value = (String) field.get(obj);

                put.addColumn(Bytes.toBytes(family),Bytes.toBytes(colName),Bytes.toBytes(value));

            }
        }

        //增加数据
        table.put(put);

        //关闭表
        table.close();
    }

    protected void putData(String name, Put put) throws Exception {

        //获取表对象
        Connection conn = getConnection();
        Table table = conn.getTable(TableName.valueOf(name));

        //增加数据
        table.put(put);

        //关闭表
        table.close();
    }

    //删除表
    protected void deleteTable(String tableName) throws Exception{
        TableName tn = TableName.valueOf(tableName);
        Admin admin = getAdmin();
        admin.disableTable(tn);
        admin.deleteTable(tn);
    }

    //创建命名空间，如果已经存在，不需要创建
    protected void createNameSpaceNX(String namespace) throws Exception {
        Admin admin = getAdmin();

        try {
            admin.getNamespaceDescriptor(namespace);
        }catch (NamespaceNotFoundException e){

            NamespaceDescriptor namespaceDescriptor = NamespaceDescriptor.create(namespace).build();

            admin.createNamespace(namespaceDescriptor);
        }
    }

    //获取连接对象
    protected synchronized Admin getAdmin() throws Exception{
        Admin admin = adminHolder.get();
        if(admin == null){
            admin = getConnection().getAdmin();
            adminHolder.set(admin);
        }
        return admin;
    }

    //获取连接对象
    protected synchronized Connection getConnection() throws Exception{
        Connection conn = connHolder.get();
        if(conn == null){
            Configuration conf = HBaseConfiguration.create();
            conn = ConnectionFactory.createConnection(conf);
            connHolder.set(conn);
        }
        return conn;
    }
}
