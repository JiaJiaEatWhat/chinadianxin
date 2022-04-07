package com.jiajun.common.bean;

import java.io.Closeable;

public interface DataOut extends Closeable {
    public void setpath(String path);

    public void write( Object data ) throws Exception;
    public void write( String data ) throws Exception;

}
