//Author:刘行
package com.company.Tables;

public class FourItems {
    /*
    * 进入新的代码层（代码层增加）
    * 对应四元式("level_plus", null, null, null)
    * codeLevel++
    * */
    public static final String LEVEL_PLUS = "level_plus";

    /*
    * 结束当前代码层(代码层减一)
    * 对应四元式("level_minus", null, null, null)
    * codeLevel--
    * */
    public static final String LEVEL_MINUS = "level_minus";

    /*
    * 假转语句
    * 对应四元式("jmpf", part2, null, part4)
    * 若part2为假，则跳转至part4所指定的语句
    */
    public static final String JMPF = "jmpf";

    /*
    * 无条件跳转语句
    * 对应四元式("jmp", null, null, part4)
    * 无条件跳转至part4所指定的语句
    * */
    public static final String JMP = "jmp";

    /*
    * 赋值语句    变量赋值                        例 int a=1;
    * 对应四元式("assign", part2, null, part4)    1. int  null  null     2. assign  1  null  a
    * 将part2中的值赋给part4所指定的变量
    * */
    public static final String ASSIGN = "assign";
	
	//新增
	/*
    * 赋值语句  数组元素赋值                       例 int A[3]; A[2] = 1;
    * 对应四元式("assign", part2, part3, part4)    1. int[]  null  3  A     2. assign  1   2   A
    * 将part2中的值赋给part4[part3]所指定的变量
    * */
	/*
    * 赋值语句  数组赋值                            例 int A[3] = {1,2,3};     
    * 对应四元式("assign", part2, null, part4)      1. int[]  null  3   A    2. assign  {1,2,3}  null  A
    * */
	/*
    * 赋值语句  指针赋值为空                        例 int*p = NULL     real*q = NULL
    * 对应四元式("assign", part2, part3, part4)     1. int*  null  null  p   2. assign  NULL  int  p     3. real*  null  null  q   4. assign  NULL  real  q
    * */
	public static final String PNULL = "NULL";
	/*
    * 赋值语句  指针赋值变量取地址                  例 int a;   int* p = &a  
    * 对应四元式("assign", part2, null, part4)      1. int  null  null  a     2.int*  null  null  p      3. assign  &a  null  p
    * */
	/*
    * 取值语句  从数组中取元素                      例 int A[3] = {1,2,3};   int a = A[2]
    * 对应四元式("assign", part2, null, part4)      1. int[]  null  3   A    2. assign  {1,2,3}  null  A    3.  int  null  null  a    4.  array  A  2  L4     5.  assign  L4  null  a
    * */
	public static final String ARRAY = "array";
	//截止

    /*
    * 输入语句
    *对应四元式("read", null, null, part4)
    * 将读取到的值存入part4所指示的变量中
    * */
    public static final String READ = "read";

    /*
    * 输出语句
    * 对应四元式("write", null, null, part4)
    * 输出part4
    * */
    public static final String WRITE = "write";

    /*
    * 声明语句（int）
    * 对应四元式("int", null, null, part4)
    * 声明一个int变量，变量名为part4
    * */
    public static final String INT = "int";

    /*
    * 声明语句（real）
    * 对应四元式("real", null, null, part4)
    * 声明一个real变量，变量名为part4
    * */
    public static final String REAL = "real";

	/*
    * 声明语句（int*）
    * 对应四元式("int*", null, null, part4)
    * 声明一个int*变量，变量名为part4
    * */
    public static final String PINT = "int*";
    /*
    * 声明语句（real*）
    * 对应四元式("real*", null, null, part4)
    * 声明一个real*变量，变量名为part4
    * */
    public static final String PREAL = "real*";
	/*
    * 声明语句 (int[])
    * 对应四元式("intaddress", null, part3, part4)
    * 声明一个int数组，变量名为part4，数组个数为part3
    * */
	public static final String REALARRAY = "real[]";
	/*
    * 声明语句 (real[])
    * 对应四元式("realaddress", null, part3, part4)
    * 声明一个real数组，变量名为part4，数组个数为part3
    * */
	public static final String INTARRAY = "int[]";
	//截止
	

    /*
    * 逻辑比较语句（<）
    * 对应四元式("<", part2, part3, part4)
    * 将part2与part3的比较结果存储在part4中
    * */
    public static final String LESS = "<";

    /*
    * 逻辑比较语句（>）
    * 对应四元式(">", part2, part3, part4)
    * 将part2与part3的比较结果存储在part4中
    * */
    public static final String MORE = ">";

    /*
    * 逻辑比较语句（==）
    * 对应四元式("==", part2, part3, part4)
    * 将part2与part3的比较结果存储在part4中
    * */
    public static final String EQUAL = "==";

    /*
    * 逻辑比较语句（<>）
    * 对应四元式("<>", part2, part3, part4)
    * 将part2与part3的比较结果存储在part4中
    * */
    public static final String NOT_EQUAL = "<>";

    /*
    * 四则运算语句（+）
    * 对应四元式("+", part2, part3, part4)
    * part4 = part2 + part3
    * */
    public static final String ADD = "+";
    /*
    * 四则运算语句（-）
    * 对应四元式("-", part2, part3, part4)
    * part4 = part2 - part3
    * */
    public static final String SUB = "-";

    /*
    * 四则运算语句（*）
    * 对应四元式("*", part2, part3, part4)
    * part4 = part2 * part3
    * */
    public static final String MUL = "*";

    /*
    * 四则运算语句（/）
    * 对应四元式("/", part2, part3, part4)
    * part4 = part2 / part3
    * */
    public static final String DIV = "/";
	

    private String part1;
    private String part2;
    private String part3;
    private String part4;

    public FourItems(String part1, String part2, String part3, String part4) {
        this.part1 = part1;
        this.part2 = part2;
        this.part3 = part3;
        this.part4 = part4;
    }

    public String getPart1() {
        return part1;
    }

    public void setPart1(String part1) {
        this.part1 = part1;
    }

    public String getPart2() {
        return part2;
    }

    public void setPart2(String part2) {
        this.part2 = part2;
    }

    public String getPart3() {
        return part3;
    }

    public void setPart3(String part3) {
        this.part3 = part3;
    }

    public String getPart4() {
        return part4;
    }

    public void setPart4(String part4) {
        this.part4 = part4;
    }
}

