//Author:刘行
package com.company.Tables;

public class TreeNode {
    /*
    * 当表达式为空时
    * */
    public static final int NONE = -2;
    /*
    * left存放stmt-sequence
    * */
    public static final int PROGRAM = -1;
    /*
    * left存放statement
    * 若有stmt-sequence，middle存放stmt-sequence
    * 若无stmt-sequence，middle为null
    * */
    public static final int STMT_SEQUENCE = 0;
    /*
    * 根据关键词来执行不同的语句函数
    * */
    public static final int STATEMENT = 1;
    /*
    * left存放statement或者{stmt-sequence}
    * */
    public static final int STMT_BLOCK = 2;
    /*
    * left存放exp
    * middle存放stmt-block
    * 若有else，right存放else部分
    * */
    public static final int IF = 3;
    /*
    * left存放exp
    * middle存放stmt-block
    * */
    public static final int WHILE = 4;
    /*
    * left存放变量名
    * middle存放变量值
    * */
    public static final int ASSIGN = 5;
    /*
    * left存放变量名
    * */
    public static final int READ = 6;
    /*
    * left存放exp
    * */
    public static final int WRITE = 7;
    /*
    * left存放类型（int或者real）
    * middle存放变量名
    * 若有赋值，right存放值
    * */
    public static final int DECLARE = 8;
    /*
    * left存放number或variable
    * */
    public static final int FACTOR = 9;
    /*
    * op中存放操作符"<",">","<>","=="
    * */
    public static final int LOGICAL_OP = 10;
    /*
    * op中存放操作符"+","-"
    * */
    public static final int ADD_OP = 11;
    /*
    * op中存放操作符"*","/"
    * */
    public static final int MUL_OP = 12;
    /*
    * left中存放factor
    * middle中存放mul-op
    * right存放term
    * 或
    * left中存放factorI
    * middle为null
    * */
    public static final int TERM = 13;
    /*
    * left存放term
    * middle存放add-op
    * right存放additive-exp
    * 或
    * left存放term
    * middle为null
    * */
    public static final int ADDITIVE_EXP = 14;
    /*
    * left存放additive-exp
    * middle存放logical-op
    * right存放additive-exp
    * 或
    * left存放additive-exp
    * middle为null
    * */
    public static final int EXP = 15;
    /*
    * value存放值
    * */
    public static final int NUMBER = 16;
    /*
    * varName存放变量名
    * */
    public static final int VAR = 17;

    /*
    * 变量类型
    * */
    public static final int INT = 18;

    /*
    * 变量类型
    * */
    public static final int REAL = 19;

    /*
     * 指针类型
     * */
    public static final int POINTER = 20;

    /*
     * 地址类型（数组）
     * */
    public static final int ADDRESS = 21;

    /*
     * 空类型
     * */
    public static final int PNULL = 22;

    /*
     * 连续声明链
     * left 存放 declare-block
     * middle 存放 chain
     * */
    public static final int CHAIN = 23;

    /*
     * 连续表达式链
     * left 存放 exp
     * middle 存放 exp-chain
     * */
    public static final int EXPCHAIN = 24;

    /*
     * 连续表达式链
     * left 存放 exp
     * middle 存放 exp-chain
     * */
    public static final int EXPCHAINS = 25;


    /*
     * 声明块
     * 一个孩子：
     * variable | * variable|
     * 两个孩子：
     * *variable = &variable | *variable = NULL  | *variable = variable | variable = exp | variable[ number ]
     * 三个孩子：
     * variable[ number ] = { exp-chains | ε }
     * */
    public static final int DECLAREBLOCK = 26;

    /*
    * Type存放节点类型
    * left，middle，right根据不同的类型存放不同的部分
    * */
    private int type;
    private TreeNode left = null;
    private TreeNode middle = null;
    private TreeNode right = null;

    //当type为VAR时，存放变量名
    private String varName;

    //当type为NUMBER时，存放值
    private String valueType;
    private double value;

    //当type为LOGICAL_OP或者ADD_OP或者MUL_OP时，存放操作符
    private String op;

    public TreeNode() {
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public TreeNode getLeft() {
        return left;
    }

    public void setLeft(TreeNode left) {
        this.left = left;
    }

    public TreeNode getMiddle() {
        return middle;
    }

    public void setMiddle(TreeNode middle) {
        this.middle = middle;
    }

    public TreeNode getRight() {
        return right;
    }

    public void setRight(TreeNode right) {
        this.right = right;
    }

    public String getVarName() {
        return varName;
    }

    public void setVarName(String varName) {
        this.varName = varName;
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

    public String getOp() {
        return op;
    }

    public void setOp(String op) {
        this.op = op;
    }

}
