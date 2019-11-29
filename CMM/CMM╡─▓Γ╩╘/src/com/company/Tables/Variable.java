//Author:王一丹

package com.company.Tables;

//变量
public class Variable {
    //变量名称
    private String name;
    //变量类型
    private String type;
    //type  = int* || real* 的时候 数组长度
    private int length;

    //构造函数
    public Variable(String name,String type){
        this.name = name;
        this.type = type;
    }

    //设置数组长度
    public void setLength(int length) {
        if(this.type == Symbol.PINT || this.type == Symbol.REAL)
            this.length = length;
    }

    //获取变量名称
    public String getName(){
        return this.name;
    }
    //获取变量类型
    public String getType(){
        return type;
    }
    //设置变量名称
    public void setName(String name){
         this.name = name;
    }
    //设置变量类型
    public void setType(String type){
        this.type = type;
    }
    //获取数组长度
    public int getLength(){
        //当类型为数组的时候
        if(this.type == Symbol.PINT || this.type== Symbol.PREAL)
            return length;
        else
            return -1;
    }
}
