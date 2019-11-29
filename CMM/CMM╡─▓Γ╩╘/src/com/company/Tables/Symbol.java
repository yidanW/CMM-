//Author:刘行
package com.company.Tables;

public class Symbol {
    /*
     * 因为在使用switch语句时需要是常量，因此所有的声明均为final
     */
    //"+"
    public static final String ADD = "+";
    //"-"
    public static final String SUB = "-";
    //"*"
    public static final String MUL = "*";
    //"/"
    public static final String DIV = "/";
    //"<"
    public static final String LESS = "<";
    //">"
    public static final String MORE = ">";
    //"=="
    public static final String EQUAL = "==";
    //"<>"
    public static final String NOT_EQUAL = "<>";
    //"("
    public static final String LEFTPARENT = "(";
    //")"
    public static final String RIGHTPARENT = ")";
    //"["
    public static final String LEFTBRACKET = "[";
    //"]"
    public static final String RIGHTBRACKET = "]";
    //"{"
    public static final String LEFTBRACE = "{";
    //"}"
    public static final String RIGHTBRACE = "}";
    //";"
    public static final String  SEMICOLON = ";";
    //变量名
    public static final String VAR = "var";
    //"if"
    public static final String IF = "if";
    //"else"
    public static final String ELSE = "else";
    //"while"
    public static final String WHILE = "while";
    //"read"
    public static final String READ = "read";
    //"write"
    public static final String WRITE = "write";
    //"int"
    public static final String  INT = "int";
    //"real"
    public static final String REAL = "real";
    //数字
    public static final String NUMBER = "number";
    //"="
    public static final String ASSIGN = "=";
    //结束符号
    public static final String END = "$";

    //新增
    //空指针"NULL"
    public static final String  PNULL = "NULL";
    //取地址符"&"
    public static final String  PADDRESS = "&";
    //","
    public static final String  COMMA = ",";
    //"int*"
    public static final String  PINT = "int*";
    //"real*"
    public static final String  PREAL = "real*";

    //截止

    private String type;
    //当type为VAR时存放变量名
    private String varName;
    //当type为NUMBER时存放值
    private String valueType;
    private double value;

    public Symbol() {
    }

    public Symbol(String type) {
        this.type = type;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValueType() {
        return valueType;
    }

    public void setValueType(String valueType) {
        this.valueType = valueType;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public String getVarName() {
        return varName;
    }

    public void setVarName(String varName) {
        this.varName = varName;
    }
}
