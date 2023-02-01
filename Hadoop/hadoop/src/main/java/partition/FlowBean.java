package com.example.hadoop_3.partion;

import org.apache.hadoop.io.Writable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * 1、实现writable
 * 2、重写write、readFields接口
 * 3、重写空参构造函数
 * 4、重写toString方法
 */
public class FlowBean implements Writable {

    public String getStr() {
        return str;
    }

    public void setStr(String str) {
        this.str = str;
    }

    private String str;

    public FlowBean() {
        super();
    }

    @Override
    public void write(DataOutput dataOutput) throws IOException {
        dataOutput.writeBytes(str);
    }

    @Override
    public void readFields(DataInput dataInput) throws IOException {
        this.str = dataInput.readLine();
    }

    @Override
    public String toString() {
        return str;
    }
}
