package com.jiajun.producer.io;

import com.jiajun.common.bean.Data;
import com.jiajun.common.bean.DataInput;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 本地文件数据输入
 */
public class LocalFileDataIn implements DataInput {

    private BufferedReader reader = null;

    public void setPath(String path) {
        try {
            reader = new BufferedReader( new InputStreamReader(new FileInputStream(path),"UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public LocalFileDataIn(String path){
        setPath(path);
    }

    public Object read() throws IOException {
        return null;
    }

    /**
     * 读取数据，返回数据封装集合
     * @param clazz
     * @param <T>
     * @return
     * @throws IOException
     */
    public <T extends Data> List<T> read(Class<T> clazz) throws IOException {

        List<T> ts = new ArrayList<T>();

        try {
            //从数据文件中读取所有的数据
            String line = null;
            while ( (line = reader.readLine()) != null ){
                //将数据转换指定类型的对象,返回
                T t = (T)clazz.newInstance();
                t.setValue(line);
                ts.add(t);
            }
        }catch ( Exception e ){
            e.printStackTrace();
        }

        return ts;
    }

    public void close() throws IOException {
        if( reader != null ){
            reader.close();
        }
    }


}
