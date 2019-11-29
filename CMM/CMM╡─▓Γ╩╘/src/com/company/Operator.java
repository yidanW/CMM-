package com.company;

import com.company.Tables.FourItems;
import com.company.Tables.LVariable;
import com.company.Tables.Variable;
import com.company.Tables.VariableNode;

import java.util.*;

import static com.company.Lex.IsLetter;


public class Operator {
    //接收中间代码生成的四元式组
    public static ArrayList<FourItems> fourItemsArrayList = new ArrayList<>();
    //代码层数
    public static int codeLevel = 0;
    //计算值变量表--用于存放每一层计算的值
    public static ArrayList<LVariable> lVariableArrayList = new ArrayList<>();
    //指向要执行的四元式
    public static int Num = 0;
    //变量树
    public static VariableNode varTree = null;
    //储存所有变量名(一个ArrayList<String>存储一层的变量名)
    public static ArrayList<ArrayList<String>> varNameList = new ArrayList<>();


    /*
     * 根据变量名返回某一条四元式的计算结果
     * */
    public static LVariable findLVariable(String name){
        for(int i = 0;i < lVariableArrayList.size(); i++){
            if(name.equals(lVariableArrayList.get(i).getVarName())){
                return lVariableArrayList.get(i);
            }
        }
        return null;
    }

    /*
     * 将某一条四元式计算结果添加进lVariableArrayList
     * */
    public static void addLVariable(LVariable lVariable){
        for(int i = 0; i < lVariableArrayList.size(); i++){
            //若存在同名则替换
            if(lVariable.getVarName().equals(lVariableArrayList.get(i).getVarName())){
                Collections.replaceAll(lVariableArrayList,lVariableArrayList.get(i),lVariable);
                return;
            }
        }
        lVariableArrayList.add(lVariable);
    }

    /*
     * 根据变量名返回变量节点
     * */
    public static VariableNode findVariableNode(String name){
        VariableNode head = varTree;
        while (head != null){
            if(name.equals(head.getVarName())){
                return head;
            }
            head = head.getNextNode();
        }
        return null;
    }
    /*
     * 插入变量节点到变量树中,并分配地址
     * */
    public static void addVarNode(VariableNode variableNode){
        VariableNode head = varTree;
        //如果有同名变量则插入
        while (head != null){
            if(head.getVarName().equals(variableNode.getVarName())){
                variableNode.setNextNode(head.getNextNode());
                if(head.getNextNode() != null){
                    head.getNextNode().setPreNode(variableNode);
                }
                head.setNextNode(null);

                variableNode.setPreNode(head.getPreNode());
                if (head.getPreNode() != null){
                    head.getPreNode().setNextNode(variableNode);
                }
                head.setPreNode(null);

                variableNode.setLeftNode(head);
                return;
            }
            head = head.getNextNode();
        }
        //如果没有同名变量则插入变量树,并分配地址
        if(varTree == null){
            varTree = variableNode;
            if(varTree.getVarType()=="int" || varTree.getVarType()=="real" || varTree.getVarType()=="int*" || varTree.getVarType()=="real*"){
                varTree.setAddress(1);
            }
            if(varTree.getVarType()=="int[]"|| varTree.getVarType()=="real[]"){
                varTree.setAddress(1);
            }
        }else {
            head = varTree;
            head.setPreNode(variableNode);
            variableNode.setNextNode(head);
            varTree = variableNode;
            if(varTree.getVarType()=="int" || varTree.getVarType()=="real" || varTree.getVarType()=="int*" || varTree.getVarType()=="real*"){
                if(head.getVarType()=="int" || head.getVarType()=="real" || head.getVarType()=="int*" || head.getVarType()=="real*" ){
                    varTree.setAddress(head.getAddress());
                }
                if(head.getVarType()=="int[]"|| head.getVarType()=="real[]"){
                    varTree.setAddress(head.getAddress());
                }
            }
            if(varTree.getVarType()=="int[]"|| varTree.getVarType()=="real[]"){
                if(head.getVarType()=="int" || head.getVarType()=="real" || head.getVarType()=="int*" || head.getVarType()=="real*" ){
                    varTree.setArrayaddress(head.getAddress());
                }
                if(head.getVarType()=="int[]"|| head.getVarType()=="real[]"){
                    varTree.setArrayaddress(head.getAddress());
                }
            }
        }
    }
    /*
     * 删除变量树中某个变量节点
     * */
    public static void deleteVarNode(String name){
        VariableNode head = varTree;
        while (head != null){
            if(head.getVarName().equals(name)){
                VariableNode temp = head.getNextNode();
                temp.setNextNode(head.getNextNode());
                if(head.getNextNode() != null){
                    head.getNextNode().setPreNode(temp);
                }

                temp.setPreNode(head.getPreNode());
                if (head.getPreNode() != null){
                    head.getPreNode().setNextNode(temp);
                }
                head.setLeftNode(null);
            }
        }
    }

    /*
     * 判断part是否为数值
     * */
    public static boolean isNumber(String part){
        int pointNumber = 0;
        for(int i = 0;i<part.length();i++){
            if(Character.isDigit(part.charAt(i))||part.charAt(i) == ' '){

            }else if(part.charAt(i) == '.'){
                pointNumber++;
            }else {
                return false;
            }
        }
        if(pointNumber <= 1){
            return true;
        }
        return false;
    }

    /*
     * 判断part是否为变量
     * */
    public static boolean isVar(String part){
        if(part.charAt(0) == '_'||IsLetter(part.charAt(0))){
            //是否以数字与字母组成
            for(int i = part.length(); --i>=0;){
                if(Character.isDigit(part.charAt(i))||IsLetter(part.charAt(i))){

                }else {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    /*
     * 判断part是否为某条四元式的值
     * */
    public static boolean isLVar(String part){
        if(part.charAt(0) == 'L'){
            for(int i = 1; i<part.length(); i++){
                if(Character.isDigit(part.charAt(i))){

                }else {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    /*
     * 获取part的类型（part类型可能为变量，值，某条四元式的计算结果）
     * */
    public static String getPartType(String part){
        if(isNumber(part)){
            return "number";
        }else if(isLVar(part)){
            return "LVar";
        }else if(isVar(part)){
            return "Var";
        }
        return "";
    }

    /*
     * 获取part的值（仅限于part类型为int或real）
     * */
    public static double getPartValue(String part){
        if("number".equals(getPartType(part))){
            return Double.valueOf(part);
        }else if("LVar".equals(getPartType(part))){
            if("int".equals(findLVariable(part).getVarType())){
                return findLVariable(part).getIntValue();
            }else {
                return findLVariable(part).getRealValue();
            }
        }else {
            if("int".equals(findVariableNode(part).getVarType())){
                return findVariableNode(part).getIntValue();
            }else {
                return findVariableNode(part).getRealValue();
            }
        }
    }

    /*
     *获取part数组的值
     */
    public static void getIntArray(String part,int[] intArray){
        String ss1[] = part.split("\\{");
        String ss2[] = ss1[1].split("\\}");
        String ss3[] = ss2[0].split(".0,");
        for(int i=0;i<ss3.length-1;i++){
            intArray[i]=Integer.valueOf(ss3[i]).intValue();
        }
        char ss4[] = ss3[ss3.length-1].toCharArray();int num=0;
        for(int j=0;j<ss4.length;j++){
            if('.'==ss4[j]){break;}
            num++;
        }
        char ss5[] = new char[num];
        for(int x=0;x<num;x++){
            ss5[x]=ss4[x];
        }
        for(int y=0;y<num;y++){
            intArray[ss3.length-1]+=(Integer.valueOf(ss5[y]).intValue()-48)*Math.pow(10,y);
        }
    }
    public static void getRealArray(String part,double[] doubleArray){
        String ss1[] = part.split("\\{");
        String ss2[] = ss1[1].split("\\}");
        String ss3[] = ss2[0].split(",");
        for(int i=0;i<ss3.length;i++){
            doubleArray[i]=Double.parseDouble(ss3[i]);
        }
    }

    /*
     * ("level_plus", null, null, null)
     * */
    public static void LevelPlus(){
        codeLevel++;
        //创建新的变量层来存储变量名
        varNameList.add(new ArrayList<String>());
        Num++;
    }
    /*
     * ("level_minus", null, null, null)
     * */
    public static void LevelMinus(){
        codeLevel--;
        //删除这一层内创建的变量
        for(int i = 0; i < varNameList.get(varNameList.size() - 1).size(); i++){
            deleteVarNode(varNameList.get(varNameList.size() - 1).get(i));
        }
        varNameList.remove(varNameList.size() - 1);
        Num++;
    }

    /*
     * ("jmpf", part2, null, part4)
     * */
    public static void Jmpf(String part2, String part4){
        //part2为表达式计算结果
        if(findLVariable(part2).isBoolValue()){
            Num++;
        }else{
            Num = Integer.parseInt(part4);
        }
    }
    /*
     * ("jmp", null, null, part4)
     * */
    public static void Jmp(String part4){
        Num = Integer.parseInt(part4.replaceAll("L",""));
    }

    /*
     * ("assign", part2, null, part4)
     * */
    public static void Assign(String part2,String part3, String part4){
        switch (findVariableNode(part4).getVarType()){
            case "int":
                if (isLVar(part2)) {
                    try {
                        findVariableNode(part4).setIntValue(findLVariable(part2).getIntValue());
                    }catch (Exception e){
                    }
                }else {
                    if (isVar(part2)) {
                        try {
                            findVariableNode(part4).setIntValue(findVariableNode(part2).getIntValue());
                        } catch (Exception e) {
                        }
                    } else {
                        findVariableNode(part4).setIntValue((int) getPartValue(part2));
                    }
                }
                break;
            case "real":
                if (isLVar(part2)) {
                    try {
                        findVariableNode(part4).setIntValue(findLVariable(part2).getIntValue());
                    }catch (Exception e){
                    }
                }else {
                    if (isVar(part2)) {
                        try {
                            findVariableNode(part4).setRealValue(findVariableNode(part2).getRealValue());
                        } catch (Exception e) {

                        }
                    } else {
                        try {
                            findVariableNode(part4).setRealValue(getPartValue(part2));
                        } catch (Exception e) {
                        }
                    }
                }
                break;
            case "int*":
                if(isLVar(part2)){
                    try {
                        findVariableNode(part4).setIntValue(findLVariable(part2).getIntValue());
                    }catch (Exception e){
                    }
                }
                else {
                    if (isVar(part2)) {
                        if (part2 == "NULL") {
                            findVariableNode(part4).setIntValue(0);
                        } else {
                            try {
                                findVariableNode(part4).setIntValue(findVariableNode(part2).getAddress());
                            }catch (Exception e){
                            }
                        }
                    } else {
                            try {
                                findVariableNode(part4).setIntValue((int) getPartValue(part2));
                            }catch (Exception e){
                            }
                    }
                }
                break;
            case "real*":
                if(isLVar(part2)){
                    try {
                        findVariableNode(part4).setRealValue(findLVariable(part2).getRealValue());
                    }catch (Exception e){
                    }
                }
                else {
                    if (isVar(part2)) {
                        if (part2 == "NULL") {
                            findVariableNode(part4).setRealValue(0);
                        } else {
                            try {
                                findVariableNode(part4).setRealValue(findVariableNode(part2).getAddress());
                            }catch (Exception e){
                            }
                        }
                    } else {
                        try {
                            findVariableNode(part4).setRealValue((int) getPartValue(part2));
                        }catch (Exception e){
                        }
                    }
                }
                break;
            case "int[]":
                if (part3 == null){
                    int[] IntArray = new int[findVariableNode(part4).getLength()];
                    getIntArray(part2, IntArray);
                    findVariableNode(part4).setIntArraysValue(IntArray);
                }
                else if(0<=Integer.parseInt(part3)&&Integer.parseInt(part3)<findVariableNode(part4).getLength()){
                    if(isLVar(part2)){
                        findVariableNode(part4).intArrayValue[Integer.valueOf(part3).intValue()]=findLVariable(part2).getIntValue();
                    }else {
                        findVariableNode(part4).intArrayValue[Integer.valueOf(part3).intValue()] = (new Double(part2)).intValue();
                    }
                }
                break;
            case "real[]":
                if (part3 == null) {
                    double[] DoubleArray = new double[findVariableNode(part4).getLength()];
                    getRealArray(part2, DoubleArray);
                    findVariableNode(part4).setRealArraysValue(DoubleArray);
                }
                else {
                    if (0 <= Integer.parseInt(part3) && Integer.parseInt(part3) < findVariableNode(part4).getLength()) {
                        if (isLVar(part2)) {
                            findVariableNode(part4).intArrayValue[Integer.valueOf(part3).intValue()] = findLVariable(part2).getIntValue();
                        } else {
                            findVariableNode(part4).realArrayValue[Integer.valueOf(part3).intValue()] = (new Double(part2)).doubleValue();
                        }
                    }
                    else {
                        break;
                    }
                }
                break;
        }
        Num++;
    }
    /*
     * ("int", null, null, part4)
     * */
    public static void Int(String part4){
        addVarNode(new VariableNode("int", part4));
        varNameList.get(varNameList.size() - 1).add(part4);
        Num++;
    }
    /*
     * ("real", null, null, part4)
     * */
    public static void Real(String part4){
        addVarNode(new VariableNode("real",part4));
        varNameList.get(varNameList.size() - 1).add(part4);
        Num++;
    }
    /*
     * ("int[]", null, part3, part4)
     * */
    public static void IntArray(String part4,String part3){
        addVarNode(new VariableNode("int[]",part4,Integer.parseInt(part3)));
        varNameList.get(varNameList.size() - 1).add(part4);
        Num++;
    }
    /*
     * ("real[]", null, part3, part4)
     * */
    public static void RealArray(String part3,String part4){
        addVarNode(new VariableNode("real[]",part4,Integer.parseInt(part3)));
        varNameList.get(varNameList.size() - 1).add(part4);
        Num++;
    }
    /*
     * ("int*", null, null, part4)
     * */
    public static void Pint(String part4){
        addVarNode(new VariableNode("int*",part4));
        varNameList.get(varNameList.size() - 1).add(part4);
        Num++;
    }
    /*
     * ("real*", null, null, part4)
     * */
    public static void Preal(String part4){
        addVarNode(new VariableNode("real*",part4));
        varNameList.get(varNameList.size() - 1).add(part4);
        Num++;
    }
    /*
     * ("assign", NULL, int/real, part4)
     * */
    public static void Pnull(String part2,String part3,String part4){
        if(part3=="int*") {
            addVarNode(new VariableNode("int*", part4));
            Assign(part2,part3,part4);
        }
        else if(part3=="real*") {
            addVarNode(new VariableNode("real*", part4));
            Assign(part2,part3,part4);
        }
        varNameList.get(varNameList.size() - 1).add(part4);
        Num++;
    }
    /*
     * ("array", part2, part3, part4)
     * */
    public static void Array(String part2,String part3,String part4){
        LVariable lVariable = new LVariable();
        lVariable.setVarName("L" + Num);
        if(findVariableNode(part2).getVarType().equals("real[]")) {
            lVariable.setVarType("real");
            lVariable.setRealValue(findVariableNode(part2).realArrayValue[Integer.parseInt(part3)]);
        }else if(findVariableNode(part2).getVarType().equals("int[]")) {
            lVariable.setVarType("int");
            lVariable.setIntValue(findVariableNode(part2).intArrayValue[Integer.parseInt(part3)]);
        }
        addLVariable(lVariable);
        Num++;
    }

    /*
     * ("read", null, null, part4)
     * */
    public static void Read(String part4){
        Scanner sc = new Scanner(System.in);
        switch (findVariableNode(part4).getVarType()){
            case "int":
                findVariableNode(part4).setIntValue(sc.nextInt());
                break;
            case "real":
                findVariableNode(part4).setRealValue(sc.nextDouble());
                break;
        }
        Num++;
    }
    /*
     * ("write", null, null, part4)
     * */
    public static void Write(String part4){
        char[] ss=part4.toCharArray();
        if(ss[0]=='*') {
            if (findVariableNode(part4.split("[*]")[1]).getVarType() == "int*") {
                System.out.println(findVariableNode(part4.split("[*]")[1]).getIntValue());
            } else if (findVariableNode(part4.split("[*]")[1]).getVarType() == "real*") {
                System.out.println(findVariableNode(part4.split("[*]")[1]).getRealValue());
            }
        }
        else {
            if(isLVar(part4)){
                System.out.println(findLVariable(part4).getIntValue()+findLVariable(part4).getRealValue());
            }else {
                if (findVariableNode(part4).getVarType() == "int") {
                    System.out.println(findVariableNode(part4).getIntValue());
                }
                else if (findVariableNode(part4).getVarType() == "real") {
                    System.out.println(findVariableNode(part4).getRealValue());
                }
            }
        }
        Num++;
    }

    /*
     * ("<", part2, part3, part4)
     * */
    public static void Less(String part2, String part3, String part4){
        LVariable lVariable = new LVariable("bool","L" + Num);
        if(getPartValue(part2) < getPartValue(part3)){
            lVariable.setBoolValue(true);
        }else {
            lVariable.setBoolValue(false);
        }
        addLVariable(lVariable);
        Num++;
    }
    /*+
     * (">", part2, part3, part4)
     * */
    public static void More(String part2, String part3, String part4){
        LVariable lVariable = new LVariable("bool","L" + Num);
        if(getPartValue(part2) > getPartValue(part3)){
            lVariable.setBoolValue(true);
        }else {
            lVariable.setBoolValue(false);
        }
        addLVariable(lVariable);
        Num++;
    }
    /*
     * ("==", part2, part3, part4)
     * */
    public static void Equal(String part2, String part3, String part4){
        LVariable lVariable = new LVariable("bool","L" + Num);
        if(getPartValue(part2) == getPartValue(part3)){
            lVariable.setBoolValue(true);
        }else {
            lVariable.setBoolValue(false);
        }
        addLVariable(lVariable);
        Num++;
    }
    /*
     * ("<>", part2, part3, part4)
     * */
    public static void NotEqual(String part2, String part3, String part4){
        LVariable lVariable = new LVariable("bool","L" + Num);
        if(getPartValue(part2) != getPartValue(part3)){
            lVariable.setBoolValue(true);
        }else {
            lVariable.setBoolValue(false);
        }
        addLVariable(lVariable);
        Num++;
    }
    /*
     * ("+", part2, part3, part4)
     * */
    public static void Add(String part2, String part3, String part4){
        LVariable lVariable = new LVariable();
        lVariable.setVarName("L" + Num);
        lVariable.setVarType("int");

        if(getPartType(part2).equals("LVar")){
            if(findLVariable(part2).getVarType().equals("real")){
                lVariable.setVarType("real");
            }

        }else if(getPartType(part2).equals("Var")){
            if(findVariableNode(part2).getVarType().equals("real")){
                lVariable.setVarType("real");
            }
        }else {
            for(int i = 0; i<part2.length();i++){
                if(part2.charAt(i) =='.'){
                    lVariable.setVarType("real");
                }
            }
        }

        if(lVariable.getVarType().equals("int")){
            lVariable.setIntValue((int) (getPartValue(part2) + getPartValue(part3)));
        }else {
            lVariable.setRealValue(getPartValue(part2) + getPartValue(part3));
        }

        addLVariable(lVariable);
        Num++;
    }
    /*
     * ("-", part2, part3, part4)
     * */
    public static void Sub(String part2, String part3, String part4){
        LVariable lVariable = new LVariable();
        lVariable.setVarName("L" + Num);

        lVariable.setVarType("int");

        if(getPartType(part2).equals("LVar")){
            if(findLVariable(part2).getVarType().equals("real")){
                lVariable.setVarType("real");
            }

        }else if(getPartType(part2).equals("Var")){
            if(findVariableNode(part2).getVarType().equals("real")){
                lVariable.setVarType("real");
            }
        }else {
            for(int i = 0; i<part2.length();i++){
                if(part2.charAt(i) =='.'){
                    lVariable.setVarType("real");
                }
            }
        }

        if(lVariable.getVarType().equals("int")){
            lVariable.setIntValue((int) (getPartValue(part2) - getPartValue(part3)));
        }else {
            lVariable.setRealValue(getPartValue(part2) - getPartValue(part3));
        }

        addLVariable(lVariable);
        Num++;
    }
    /*
     * ("*", part2, part3, part4)
     * */
    public static void Mul(String part2, String part3, String part4){
        LVariable lVariable = new LVariable();
        lVariable.setVarName("L" + Num);
        lVariable.setVarType("int");

        if(getPartType(part2).equals("LVar")){
            if(findLVariable(part2).getVarType().equals("real")){
                lVariable.setVarType("real");
            }

        }else if(getPartType(part2).equals("Var")){
            if(findVariableNode(part2).getVarType().equals("real")){
                lVariable.setVarType("real");
            }
        }else {
            for(int i = 0; i<part2.length();i++){
                if(part2.charAt(i) =='.'){
                    lVariable.setVarType("real");
                }
            }
        }

        if(lVariable.getVarType().equals("int")){
            lVariable.setIntValue((int) (getPartValue(part2) * getPartValue(part3)));
        }else {
            lVariable.setRealValue(getPartValue(part2) * getPartValue(part3));
        }

        addLVariable(lVariable);
        Num++;
    }
    /*
     * ("/", part2, part3, part4)
     * */
    public static void Div(String part2, String part3, String part4){
        LVariable lVariable = new LVariable();
        lVariable.setVarName("L" + Num);
        lVariable.setVarType("int");

        if(getPartType(part2).equals("LVar")){
            if(findLVariable(part2).getVarType().equals("real")){
                lVariable.setVarType("real");
            }

        }else if(getPartType(part2).equals("Var")){
            if(findVariableNode(part2).getVarType().equals("real")){
                lVariable.setVarType("real");
            }
        }else {
            for(int i = 0; i<part2.length();i++){
                if(part2.charAt(i) =='.'){
                    lVariable.setVarType("real");
                }
            }
        }

        if(lVariable.getVarType().equals("int")){
            lVariable.setIntValue((int) (getPartValue(part2) / getPartValue(part3)));
        }else {
            lVariable.setRealValue(getPartValue(part2) / getPartValue(part3));
        }

        addLVariable(lVariable);
        Num++;
    }


    /*
     * 循环执行每一个四元组
     * */
    public static void performFourItems(){
        if(varNameList.size() == 0){
            varNameList.add(new ArrayList<String>());
        }
        while (Num < fourItemsArrayList.size()){
            //获取当前四元组
            FourItems thisFourItems = fourItemsArrayList.get(Num);
            switch (thisFourItems.getPart1()){
                case FourItems.LEVEL_PLUS:
                    LevelPlus();
                    break;
                case FourItems.LEVEL_MINUS:
                    LevelMinus();
                    break;
                case FourItems.JMPF:
                    Jmpf(thisFourItems.getPart2(),thisFourItems.getPart4());
                    break;
                case FourItems.JMP:
                    Jmp(thisFourItems.getPart4());
                    break;
                case FourItems.ASSIGN:
                    Assign(thisFourItems.getPart2(),thisFourItems.getPart3(),thisFourItems.getPart4());
                    break;
                case FourItems.READ:
                    Read(thisFourItems.getPart4());
                    break;
                case FourItems.WRITE:
                    Write(thisFourItems.getPart4());
                    break;
                case FourItems.INT:
                    Int(thisFourItems.getPart4());
                    break;
                case FourItems.REAL:
                    Real(thisFourItems.getPart4());
                    break;
                case FourItems.INTARRAY:
                    IntArray(thisFourItems.getPart4(),thisFourItems.getPart3());
                    break;
                case FourItems.REALARRAY:
                    RealArray(thisFourItems.getPart3(),thisFourItems.getPart4());
                    break;
                case FourItems.PINT:
                    Pint(thisFourItems.getPart4());
                    break;
                case FourItems.PREAL:
                    Preal(thisFourItems.getPart4());
                    break;
                case FourItems.ARRAY:
                    Array(thisFourItems.getPart2(),thisFourItems.getPart3(),thisFourItems.getPart4());
                    break;
                case FourItems.PNULL:
                    Pnull(thisFourItems.getPart2(),thisFourItems.getPart3(),thisFourItems.getPart4());
                    break;
                case FourItems.LESS:
                    Less(thisFourItems.getPart2(),thisFourItems.getPart3(),thisFourItems.getPart4());
                    break;
                case FourItems.MORE:
                    More(thisFourItems.getPart2(),thisFourItems.getPart3(),thisFourItems.getPart4());
                    break;
                case FourItems.EQUAL:
                    Equal(thisFourItems.getPart2(),thisFourItems.getPart3(),thisFourItems.getPart4());
                    break;
                case FourItems.NOT_EQUAL:
                    NotEqual(thisFourItems.getPart2(),thisFourItems.getPart3(),thisFourItems.getPart4());
                    break;
                case FourItems.ADD:
                    Add(thisFourItems.getPart2(),thisFourItems.getPart3(),thisFourItems.getPart4());
                    break;
                case FourItems.SUB:
                    Sub(thisFourItems.getPart2(),thisFourItems.getPart3(),thisFourItems.getPart4());
                    break;
                case FourItems.MUL:
                    Mul(thisFourItems.getPart2(),thisFourItems.getPart3(),thisFourItems.getPart4());
                    break;
                case FourItems.DIV:
                    Div(thisFourItems.getPart2(),thisFourItems.getPart3(),thisFourItems.getPart4());
                    break;
            }
        }
    }
}

