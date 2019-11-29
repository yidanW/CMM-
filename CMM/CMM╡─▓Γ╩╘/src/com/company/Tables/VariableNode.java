package com.company.Tables;

public class VariableNode {
    String varType;
    String varName;
    int intValue;
    double realValue;
    int Length;
    public int intArrayValue[];
    public double realArrayValue[];

    //存放变量树中上一个变量
    VariableNode preNode = null;
    //存放变量树中下一个变量
    VariableNode nextNode = null;
    //存放同名不同层变量
    VariableNode leftNode = null;

    //单个变量的地址
    int address;

    public VariableNode() {
    }
    //只声明整型或实数型变量
    public VariableNode(String varType, String varName) {
        this.varType = varType;
        this.varName = varName;
    }
    //只声明整型或实数型数组变量
    public VariableNode(String varType, String varName,int length) {
        this.varType = varType;
        this.varName = varName;
        this.Length = length;
        if (this.varType == "int[]") {
            this.intArrayValue=new int[this.Length];
        }else if (this.varType == "real[]") {
            this.realArrayValue=new double[this.Length];
        }
    }

    //给变量分配一个随机地址
    public void setAddress(int preaddress) {
        this.address = preaddress+1;
    }
    // 给数组分配一个随机的连续地址
    public void setArrayaddress(int preaddress){
        this.address = preaddress+this.Length;
    }
    //获取地址
    public int getAddress(){
        return this.address;
    }
    /*public int getArrayAddress(){
        return this.Arrayaddress[Length-1];
    }*/

    public String getVarType() {
        return varType;
    }

    public String getVarName() {
        return varName;
    }

    public int getIntValue() {
        return intValue;
    }

    public void setIntValue(int intValue) {
        this.intValue = intValue;
    }

    public double getRealValue() {
        return realValue;
    }

    public void setRealValue(double realValue) {
        this.realValue = realValue;
    }

    public int getIntArrayValue(int num) {
        return intArrayValue[num];
    }
    public void setIntArraysValue(int intValue[]) {
        this.intArrayValue = intValue;
    }
    public double getRealArrayValue(int num) {
        return realArrayValue[num];
    }
    public void setRealArraysValue(double realValue[]) {
        this.realArrayValue = realValue;
    }

    public VariableNode getNextNode() {
        return nextNode;
    }

    public void setNextNode(VariableNode nextNode) {
        this.nextNode = nextNode;
    }

    public VariableNode getLeftNode() {
        return leftNode;
    }

    public void setLeftNode(VariableNode leftNode) {
        this.leftNode = leftNode;
    }

    public VariableNode getPreNode() {
        return preNode;
    }

    public void setPreNode(VariableNode preNode) {
        this.preNode = preNode;
    }

    public int getLength(){int length=this.Length;return length;}
}
