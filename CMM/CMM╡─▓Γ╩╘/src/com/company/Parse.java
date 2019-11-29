//Author:刘行、王一丹
package com.company;

import com.company.Tables.Symbol;
import com.company.Tables.TreeNode;
import com.company.Tables.Variable;
import com.sun.source.tree.Tree;
import java.util.ArrayList;

public class Parse {
    //词法分析输出
    public static ArrayList<Symbol> lexArrayList = new ArrayList<>();
    //读入键值对的索引
    public static int index = 0;
    //当前键值对
    public static Symbol symbol = new Symbol();
    //语法分析是否有错误
    public static boolean existError = false;
    //变量栈
    public static ArrayList<Variable> varArrayList = new ArrayList<>();
    //每一个block在变量栈中的起始位置
    public static ArrayList varIndexList = new ArrayList();


    /*
     * 读取下一个词
     * */
    public static void NextSymbol(){
        index += 1;
        symbol = lexArrayList.get(index);
    }

    /*
     * 判断当前单词是否为所需单词
     * */
    public static void IsMatch(String type){
        if (symbol.getType().equals(type)){
            return;
        }else{
            existError = true;
//            System.out.println("非法单词！"+ index);
            System.out.println("语法错误！缺少"+type);
        }
    }

    /*
    * 判断是否已经读完
    * */
    public static boolean IsEnd(){
        if(index == lexArrayList.size() - 1){
            symbol = new Symbol(Symbol.END);
            return true;
        }else {
            return false;
        }
    }
    /*
     * 在运行栈中添加一个变量
     */
    public static void AddVar(Variable variable){
        if(!IsDeclaredInThisBlock(variable.getName()))
            varArrayList.add(variable);
    }

    /*
     * 在运行栈中添加block
     */
    public static void AddBlock(){
        int varIndex = varArrayList.size();
        varIndexList.add(varIndex);
    }

    /*
     * 在运行栈中删除一个block
     */
    public static void DeleteBlock(){
        //获取varIndexList中的最后一个元素 - 当前block的起始下标
        int sizeOfIndex = varIndexList.size();
        int pFirst = (int) varIndexList.get(sizeOfIndex-1);
        //删除varIndexList中的这个元素
        varIndexList.remove(sizeOfIndex-1);

        //删除varArrayList中的部分元素
        int sizeOfVarList = varArrayList.size();
        //要删除的元素个数
        int count = sizeOfVarList - pFirst;
        while(count !=0){
            count--;
            varArrayList.remove(pFirst);
        }
    }

    /*
     * 检查变量是否已经声明
     * 返回值为这个变量在运行栈中的下标
     * 不存在返回-1
     */
    public static int IsDeclared(String name){
        for(int i=varArrayList.size()-1;i>=0;i--){
            Variable variable = varArrayList.get(i);
            if(variable.getName().equals(name))
                return i;
        }
        //未找到变量
        System.out.println("变量" + name + "未声明！"+index);
        existError = true;
        return -1;
    }

    /*
     * 检查变量在当前层是否已经声明
     */
    public static boolean IsDeclaredInThisBlock(String name){
        int pFirst = (int) varIndexList.get( varIndexList.size() -1);
        for(int i=varArrayList.size()-1;i>=pFirst;i--){
            Variable variable = varArrayList.get(i);
            if(variable.getName().equals(name)){
                existError = true;
                System.out.println("变量"+name+"在当前层已经被声明！"+index);
                return true;
            }
        }
        return false;
    }

    /*
     * 检查变量类型是否匹配
     */
    public static boolean IsTypeMatch(String name,String type){
        int i = IsDeclared(name);
        if(i != -1) {
            Variable variable = varArrayList.get(i);
            if(!variable.getType().equals(type)) {
                return false;
            }
        }else{
//            existError = true;
//            System.out.println("变量"+name+"未声明！");
            return false;
        }
        return true;
    }

    /*
     * 对node进行广度优先遍历
     * 判断exp表达式中所有的变量和常量是否都为int类型
     * 返回值 true：全部变量都是int
     * false：存在real类型的值 导致表达式是real
     */
    public static boolean IsAllVarInt(TreeNode node){
        if(node == null)
            return true;
        //如果是常量
        if(node.getType() == TreeNode.NUMBER){
//            //判断这个常量是否为int
//            double value = node.getValue();
//            //当value不为整数的时候
//            if(value - (double)(int)value > 0)
//                return false;
//            return true;
            if(node.getValueType()!= null && node.getValueType().equals("int"))
                return true;
            else return false;
        }
        //如果是变量
        else if(node.getType() == TreeNode.VAR || node.getType() == TreeNode.ADDRESS ){
            //如果这个变量不是int类型
            if(!IsTypeMatch(node.getVarName(),Symbol.INT) && !IsTypeMatch(node.getVarName(),Symbol.PINT))
                return false;
            return true;
        }
        //否则进行广度优先遍历
        return (IsAllVarInt(node.getLeft()) && IsAllVarInt(node.getMiddle()) && IsAllVarInt(node.getRight()));
    }

    /*
     * program -> stmt-sequence
     * */
    public static TreeNode program(){
        //在变量栈中添加第一层
//        varIndexList.add(0);
        AddBlock();
        TreeNode node = new TreeNode();
        node.setType(TreeNode.PROGRAM);
        if(symbol.getType().equals(Symbol.IF)||
                symbol.getType().equals(Symbol.WHILE)||
                symbol.getType().equals(Symbol.READ)||
                symbol.getType().equals(Symbol.WRITE)||
                symbol.getType().equals(Symbol.VAR)||
                symbol.getType().equals(Symbol.INT)||
                symbol.getType().equals(Symbol.MUL)||
                symbol.getType().equals(Symbol.REAL)){
            node.setLeft(stmt_sequence());
        }else{
            existError = true;
            System.out.println("非法单词！program" + index);
            return null;
        }
        return node;
    }

    /*
     * stmt-sequence -> statement; stmt-sequence | statement | ε
     * 执行完statement()函数后
     * 根据element来决定执行文法的哪一条分支
     * */
    public static TreeNode stmt_sequence(){
        TreeNode node = new TreeNode();
        node.setType(TreeNode.STMT_SEQUENCE);
        //若stmt-sequence为空
        if(symbol.getType().equals(Symbol.RIGHTBRACE)) {

        }else if(symbol.getType().equals(Symbol.IF)||
                symbol.getType().equals(Symbol.WHILE)||
                symbol.getType().equals(Symbol.READ)||
                symbol.getType().equals(Symbol.WRITE)||
                symbol.getType().equals(Symbol.VAR)||
                symbol.getType().equals(Symbol.INT)||
                symbol.getType().equals(Symbol.MUL)||
                symbol.getType().equals(Symbol.REAL)){
            //若stmt-sequence为statement
            node.setLeft(statement());
            if(symbol.getType().equals(Symbol.RIGHTBRACE)){

            }
            //若stmt-sequence为statement stmt-sequence
            else if(symbol.getType().equals(Symbol.END)){
                return node;
            }else {
                node.setMiddle(stmt_sequence());
            }
        }else{
            existError = true;
            System.out.println("非法单词！stmt-sequence" + index);
            return null;
        }
        return node;
    }
    /*
     * statement -> if-stmt | while-stmt | assign-stmt | read-stmt | write-stmt | declare-stmt
     * 根据symbol的type来执行对应部分
     * */
    public static TreeNode statement(){
        TreeNode node = new TreeNode();
        node.setType(TreeNode.STATEMENT);
        switch (symbol.getType()){
            case Symbol.IF:
                node.setLeft(ifstmt());
                break;
            case Symbol.WHILE:
                node.setLeft(whilestmt());
                break;
            case Symbol.READ:
                node.setLeft(readstmt());
                break;
            case Symbol.WRITE:
                node.setLeft(writestmt());
                break;
            case Symbol.VAR:
            case Symbol.MUL:
                node.setLeft(assignstmt());
                break;
            //声明变量有两种类型
            case Symbol.INT:
            case Symbol.REAL:
                node.setLeft(declarestmt());
                break;
            default:
                existError = true;
                System.out.println("非法单词！statement" + index);
                return null;
        }
        return node;
    }

    /*
     * stmt-block -> statement | {stmt-sequence}
     * */
    public static TreeNode stmt_block(){
        TreeNode node = new TreeNode();
        node.setType(TreeNode.STMT_BLOCK);
        if(symbol.getType().equals(Symbol.IF)||
                symbol.getType().equals(Symbol.WHILE)||
                symbol.getType().equals(Symbol.READ)||
                symbol.getType().equals(Symbol.WRITE)||
                symbol.getType().equals(Symbol.VAR)||
                symbol.getType().equals(Symbol.INT)||
                symbol.getType().equals(Symbol.REAL)){
            node.setLeft(statement());
        }else if(symbol.getType().equals(Symbol.LEFTBRACE)){
            AddBlock();
            if(!IsEnd()){
                NextSymbol();
            }else{
                return node;
            }
            node.setLeft(stmt_sequence());
            IsMatch(Symbol.RIGHTBRACE);
            DeleteBlock();
            if(!IsEnd()){
                NextSymbol();
            }else{
                return node;
            }
        }else{
            existError = true;
            System.out.println("非法单词！stmt-block" + index);
            return null;
        }
        return node;
    }

    /*
     * if-stmt -> if (exp) stmt-block | if (exp) stmt-block else stmt-block
     * */
    public static TreeNode ifstmt(){
        TreeNode node = new TreeNode();
        node.setType(TreeNode.IF);
        IsMatch(Symbol.IF);
        if(!IsEnd()){
            NextSymbol();
        }else{
            return node;
        }
        IsMatch(Symbol.LEFTPARENT);
        if(!IsEnd()){
            NextSymbol();
        }else{
            return node;
        }
        node.setLeft(exp());
        //判断exp是否为boolean类型
        if(node.getLeft().getMiddle() == null || node.getLeft().getMiddle().getType()!=TreeNode.LOGICAL_OP){
            existError = true;
            System.out.println("if表达式不为bool类型"+index);
        }
        IsMatch(Symbol.RIGHTPARENT);
        if(!IsEnd()){
            NextSymbol();
        }else{
            return node;
        }

        node.setMiddle(stmt_block());

        if(symbol.getType().equals(Symbol.ELSE)){
            if(!IsEnd()){
                NextSymbol();
            }else{
                return node;
            }
            node.setRight(stmt_block());
        }

        return node;
    }

    /*
     * while-stmt -> while (exp) stmt-block
     * */
    public static TreeNode whilestmt(){
        TreeNode node = new TreeNode();
        node.setType(TreeNode.WHILE);
        IsMatch(Symbol.WHILE);
        if(!IsEnd()){
            NextSymbol();
        }else{
            return node;
        }
        IsMatch(Symbol.LEFTPARENT);
        if(!IsEnd()){
            NextSymbol();
        }else{
            return node;
        }
        node.setLeft(exp());
        //判断exp是否为boolean类型
        if(node.getLeft().getMiddle() == null || node.getLeft().getMiddle().getType()!=TreeNode.LOGICAL_OP){
            existError = true;
            System.out.println("while表达式不为bool类型"+index);
        }
        IsMatch(Symbol.RIGHTPARENT);
        if(!IsEnd()){
            NextSymbol();
        }else{
            return node;
        }
        node.setMiddle(stmt_block());
        return node;
    }

    /*
     * assign-stmt -> variable = exp;
     * 改为
     * ( variable = exp | variable = &variable |
     *  variable = NULL| variable[exp]=exp | *variable = exp );
     * */
    public static TreeNode assignstmt(){
        TreeNode node = new TreeNode();
        node.setType(TreeNode.ASSIGN);

        // *variable = exp
        if(symbol.getType().equals(Symbol.MUL)){
            // variable
            if(!IsEnd()){
                NextSymbol();
            }else{
                return node;
            }
            IsMatch(Symbol.VAR);
            TreeNode pointer = new TreeNode();
            pointer.setType(TreeNode.POINTER);
            String name = symbol.getVarName();
            pointer.setVarName(name);
            node.setLeft(pointer);
            // =
            if(!IsEnd()){
                NextSymbol();
            }else{
                return node;
            }
            IsMatch(Symbol.ASSIGN);

            // exp
            if(!IsEnd()){
                NextSymbol();
            }else{
                return node;
            }
            node.setMiddle(exp());
            //int real X
            if(IsDeclared(name)!=-1) {
                if (IsTypeMatch(name, Symbol.INT) || IsTypeMatch(name, Symbol.REAL)) {
                    existError = true;
                    System.out.println(name + "是指针变量！");
                }
                //int* exp(real) X
                else if (IsTypeMatch(name, Symbol.PINT) && !IsAllVarInt(node.getMiddle())) {
                    existError = true;
                    System.out.println(name + "是int指针变量,表达式为real！");
                }
            }
        }
        //  variable = exp | variable = & variable |
        //  variable = NULL| variable[exp]=exp |
        else if(symbol.getType().equals(Symbol.VAR)){
            TreeNode var = new TreeNode();
            var.setVarName(symbol.getVarName());
            var.setType(TreeNode.VAR);
            node.setLeft(var);
            if(!IsEnd()){
                NextSymbol();
            }else{
                return node;
            }
            // =
            if(symbol.getType().equals(Symbol.ASSIGN)){
                if(!IsEnd()){
                    NextSymbol();
                }else{
                    return node;
                }
                //variable = &variable
                if(symbol.getType().equals(Symbol.PADDRESS)){
                    if(!IsEnd()){
                        NextSymbol();
                    }else{
                        return node;
                    }
                    IsMatch(Symbol.VAR);
                    TreeNode address = new TreeNode();
                    address.setType(TreeNode.ADDRESS);
                    address.setVarName(symbol.getVarName());
                    node.setMiddle(address);
                    if(!IsEnd()){
                        NextSymbol();
                    }else{
                        return node;
                    }
                    //语义分析
                    //int*  int Y
                    //real* real Y
                    String namep = node.getLeft().getVarName();
                    String name = node.getMiddle().getVarName();
                    if(IsTypeMatch(namep,Symbol.PINT) && IsTypeMatch(name,Symbol.INT)){

                    }else if(IsTypeMatch(namep,Symbol.PREAL) && IsTypeMatch(name,Symbol.REAL)){

                    }else{
                        existError = true;
                        System.out.println("variable = &variable 左右两边类型不匹配！");
                    }
                }
                //variable = NULL
                else if(symbol.getType().equals(Symbol.PNULL)){
                    TreeNode pnull = new TreeNode();
                    pnull.setType(TreeNode.PNULL);
                    node.setMiddle(pnull);
                    if(!IsEnd()){
                        NextSymbol();
                    }else{
                        return node;
                    }
                    TreeNode pType = new TreeNode();
                    String type = node.getLeft().getVarName();
                    if(IsTypeMatch(type,Symbol.PINT)){
                        pType.setType(TreeNode.INT);
                    }else if(IsTypeMatch(type,Symbol.PREAL)){
                        pType.setType(TreeNode.REAL);
                    }
                    node.setRight(pType);
                    //语义分析
                    //int || real X
                    String name = node.getLeft().getVarName();
                    if(IsTypeMatch(name,Symbol.INT) ||IsTypeMatch(name,Symbol.REAL)){
                        existError = true;
                        System.out.println(name +"不是指针，不能被赋值NULL！");
                    }
                }
                //variable = exp
                else {
                    node.setMiddle(exp());
                    String name = node.getLeft().getVarName();
                    //语义分析
                    //int* || real* X
                    if(-1!=IsDeclared(name)){
                        if(IsTypeMatch(name,Symbol.PINT) || IsTypeMatch(name,Symbol.PREAL)){
                            existError=true;
                            System.out.println(name +"是指针类型，赋值语句错误！");
                        }
                        //语义分析
                        //int && exp(real) X
                        else if(IsTypeMatch(name,Symbol.INT) && !IsAllVarInt(node.getMiddle())){
                            existError = true;
                            System.out.println(name+"是int类型，表达式是real类型！");
                        }
                    }
                }
            }
            //variable[exp]=exp
            else{
                IsMatch(Symbol.LEFTBRACKET);
                var.setType(TreeNode.ADDRESS);
                if(!IsEnd()){
                    NextSymbol();
                }else{
                    return node;
                }
                node.setMiddle(exp());
                IsMatch(Symbol.RIGHTBRACKET);
                if(!IsEnd()){
                    NextSymbol();
                }else{
                    return node;
                }
                //=
                IsMatch(Symbol.ASSIGN);
                if(!IsEnd()){
                    NextSymbol();
                }else{
                    return node;
                }
                node.setRight(exp());
                String name = node.getLeft().getVarName();
                //exp1 real X
                if(!IsAllVarInt(node.getMiddle())){
                    existError = true;
                    System.out.println("数组下标是小数！");
                }
                //int real X
                if(IsDeclared(name)!=-1){
                    if(IsTypeMatch(name,Symbol.INT) || IsTypeMatch(name,Symbol.REAL)){
                        existError = true;
                        System.out.println(name+"不是数组类型！");
                    }
                    //int* real X
                    else if(IsTypeMatch(name,Symbol.PINT) && !IsAllVarInt(node.getRight())){
                        existError = true;
                        System.out.println(name+"是int数组类型,表达式是real类型！");
                    }
                    // int* real*
//                    else if(IsTypeMatch(name,Symbol.PINT) && !
                }
            }
        }

        //语义分析
//        //判断赋值语句左右两边类型是否正确
//        //如果exp是bool类型 报错
//        if(node.getMiddle().getMiddle()!=null){
//            //是bool类型
//            if(node.getMiddle().getMiddle().getType() == TreeNode.LOGICAL_OP){
//                existError = true;
//                System.out.println("赋值语句右边是bool类型！"+index);
//            }
//        }
//        //此时可判断exp是表达式
//        //变量是int exp中只要有一个变量或常量不是int类型，就报错
//        String varName = var.getVarName();
//        if(IsTypeMatch(varName,Symbol.INT)){
//            //exp 不为int
//            if(!IsAllVarInt(node.getMiddle())){
//                existError = true;
//                System.out.println("赋值语句两边类型不一致！"+index);
//            }
//        }

        IsMatch(Symbol.SEMICOLON);
        if(!IsEnd()){
            NextSymbol();
        }else{
            return node;
        }
        return node;
    }

    /*
     * read-stmt -> read variable;
     * */
    public static TreeNode readstmt(){
        TreeNode node = new TreeNode();
        node.setType(TreeNode.READ);
        IsMatch(Symbol.READ);
        if(!IsEnd()){
            NextSymbol();
        }else{
            return node;
        }
        IsMatch(Symbol.VAR);
        TreeNode var = new TreeNode();
        var.setType(TreeNode.VAR);
        var.setVarName(symbol.getVarName());
        node.setLeft(var);
        //判断变量是否被声明
        if(-1==IsDeclared(var.getVarName()))
            return node;

        if(!IsEnd()){
            NextSymbol();
        }else{
            return node;
        }
        IsMatch(Symbol.SEMICOLON);
        if(!IsEnd()){
            NextSymbol();
        }else{
            return node;
        }
        return node;
    }

    /*
     * write-stmt -> write exp;
     * */
    public static TreeNode writestmt(){
        TreeNode node = new TreeNode();
        node.setType(TreeNode.WRITE);
        IsMatch(Symbol.WRITE);
        if(!IsEnd()){
            NextSymbol();
        }else{
            return node;
        }
        node.setLeft(exp());
        //判断exp是否为表达式
        if(node.getLeft().getMiddle() != null && node.getLeft().getMiddle().getType() == TreeNode.LOGICAL_OP){
            existError = true;
            System.out.println("write语句中的内容不是表达式！"+index);
        }
        IsMatch(Symbol.SEMICOLON);
        if(!IsEnd()){
            NextSymbol();
        }else{
            return node;
        }
        return node;
    }

    /*
     * declare-stmt -> (int | real) variable; | (int | real) variable = exp;
     * 改为
     * declare-stmt -> (int | real) chain;
     * */
    public static TreeNode declarestmt(){
        TreeNode node = new TreeNode();
        node.setType(TreeNode.DECLARE);
        if(symbol.getType().equals(Symbol.INT)||symbol.getType().equals(Symbol.REAL)){
            String type = symbol.getType();
            //保存变量类型
            TreeNode varType = new TreeNode();
            node.setLeft(varType);
            //int
            if(symbol.getType().equals(Symbol.INT)) {
                varType.setType(TreeNode.INT);
                if(!IsEnd()){
                    NextSymbol();
                }else{
                    return node;
                }
                node.setMiddle(chain(Symbol.INT));
            }
            //real
            else {
                varType.setType(TreeNode.REAL);
                if(!IsEnd()){
                    NextSymbol();
                }else{
                    return node;
                }
                node.setMiddle(chain(Symbol.REAL));
            }
            //识别 ；
            IsMatch(Symbol.SEMICOLON);
//            IsMatch(Symbol.VAR);
//            //保存变量名
//            TreeNode var = new TreeNode();
//            var.setType(TreeNode.VAR);
//            var.setVarName(symbol.getVarName());
//            node.setMiddle(var);
//            if(!IsEnd()){
//                NextSymbol();
//            }else{
//                return node;
//            }
//            if(symbol.getType().equals(Symbol.ASSIGN)){
//                if(!IsEnd()){
//                    NextSymbol();
//                }else{
//                    return node;
//                }
//                node.setRight(exp());
//            }
            //将声明的变量加入变量栈中
//            Variable variable = new Variable(var.getVarName(),type);
//            if(!IsDeclaredInThisBlock(var.getVarName()))
//                AddVar(variable);
            if(!IsEnd()){
                NextSymbol();
            }else{
                return node;
            }
        } else {
            existError = true;
            System.out.println("非法单词！declare" + index);
            return null;
        }
        return node;
    }
    /*
     * factor -> number | variable | (exp) | add-op exp
     * 改为
     * factor -> number | variable | (exp) | variable[exp] | *variable
     * */
    public static TreeNode factor(){
        TreeNode node = new TreeNode();
        node.setType(TreeNode.FACTOR);
        TreeNode value = new TreeNode();
        //当factor为number时
        if(symbol.getType().equals(Symbol.NUMBER)){
            value.setType(TreeNode.NUMBER);
            value.setValueType(symbol.getValueType());
            value.setValue(symbol.getValue());
            node.setLeft(value);
            if(!IsEnd()){
                NextSymbol();
            }else{
                return node;
            }
        }
        //当factor为variable或variable[exp]时
        else if(symbol.getType().equals(Symbol.VAR)){
            value.setVarName(symbol.getVarName());
            //语义分析
            //int* || real* X
            String name  = symbol.getVarName();
            if(!IsEnd()){
                NextSymbol();
            }else{
                return node;
            }
            //variable[exp]
            if(symbol.getType().equals(Symbol.LEFTBRACKET)){
                if(!IsEnd()){
                    NextSymbol();
                }else{
                    return node;
                }
                value.setType(TreeNode.ADDRESS);
                node.setMiddle(exp());
                IsMatch(Symbol.RIGHTBRACKET);
                node.setLeft(value);
                if(!IsEnd()){
                    NextSymbol();
                }else{
                    return node;
                }
                //语义分析
                //int || real X
                if(IsTypeMatch(name,Symbol.INT) || IsTypeMatch(name,Symbol.REAL)){
                    existError = true;
                    System.out.println(name+"不是数组！");
                }
                //exp real X
                if(!IsAllVarInt(node.getMiddle())){
                    existError = true;
                    System.out.println(name+"数组下标应为整数！");
                }
            }
            else {
                value.setType(TreeNode.VAR);
                if(IsDeclared(name)!=-1){
                    if(IsTypeMatch(name,Symbol.PREAL)||IsTypeMatch(name,Symbol.PINT)){
                        existError = true;
                        System.out.println(name+"是指针变量，不能参与运算！");
                    }
                }
            }
            node.setLeft(value);
        }
        //当factor为(exp)时
        else if(symbol.getType().equals(Symbol.LEFTPARENT)){
            if(!IsEnd()){
                NextSymbol();
            }else{
                return node;
            }
            node.setLeft(exp());
            IsMatch(Symbol.RIGHTPARENT);
            if(!IsEnd()){
                NextSymbol();
            }else{
                return node;
            }
        }
        //当factor为*variable时
        else if(symbol.getType().equals(Symbol.MUL)){
            value.setType(TreeNode.POINTER);
            if(!IsEnd()){
                NextSymbol();
            }else{
                return node;
            }
            IsMatch(Symbol.VAR);
            value.setVarName(symbol.getVarName());

            //检查变量是否声明
            if(IsDeclared(symbol.getVarName())==-1)
                return node;

            if(!IsEnd()){
                NextSymbol();
            }else{
                return node;
            }
            node.setLeft(value);
            //int real X
            String name = node.getLeft().getVarName();
            if(IsTypeMatch(name,Symbol.INT)||IsTypeMatch(name,Symbol.REAL)){
                existError = true;
                System.out.println(name +"不是指针变量！");
            }
        }
        else {
            existError = true;
            System.out.println("非法单词！factor" + index);
            return null;
        }
        return node;
    }

    /*
     * term -> factor mul-op term | factor
     * */
    public static TreeNode term(){
        TreeNode node = new TreeNode();
        node.setType(TreeNode.TERM);
        node.setLeft(factor());
        if(symbol.getType().equals(Symbol.MUL)||symbol.getType().equals(Symbol.DIV)){
            TreeNode op = new TreeNode();
            op.setType(TreeNode.MUL_OP);
            if(symbol.getType().equals(Symbol.MUL)){
                op.setOp("*");
            }else {
                op.setOp("/");
            }
            node.setMiddle(op);
            if(!IsEnd()){
                NextSymbol();
            }else{
                return node;
            }
            node.setRight(term());
        }
        return node;
    }

    /*
     * additive-exp -> term add-op additive-exp | term
     * */
    public static TreeNode additive_exp(){
        TreeNode node = new TreeNode();
        node.setType(TreeNode.ADDITIVE_EXP);
        node.setLeft(term());
        if(symbol.getType().equals(Symbol.ADD)|| symbol.getType().equals(Symbol.SUB)){
            TreeNode op = new TreeNode();
            op.setType(TreeNode.ADD_OP);
            if(symbol.getType().equals(Symbol.ADD)){
                op.setOp("+");
            }else {
                op.setOp("-");
            }
            node.setMiddle(op);
            if(!IsEnd()){
                NextSymbol();
            }else{
                return node;
            }
            node.setRight(additive_exp());
        }
        return node;
    }

    /*
     * exp -> additive-exp logical-op additive-exp | additive-exp
     * */
    public static TreeNode exp(){
        TreeNode node = new TreeNode();
        node.setType(TreeNode.EXP);
        node.setLeft(additive_exp());
        if(symbol.getType().equals(Symbol.LESS)||
                symbol.getType().equals(Symbol.MORE)||
                symbol.getType().equals(Symbol.EQUAL)||
                symbol.getType().equals(Symbol.NOT_EQUAL)){
            TreeNode op = new TreeNode();
            op.setType(TreeNode.LOGICAL_OP);
            if(symbol.getType().equals(Symbol.LESS)){
                op.setOp("<");
            }else if(symbol.getType().equals(Symbol.MORE)){
                op.setOp(">");
            }else if(symbol.getType().equals(Symbol.EQUAL)){
                op.setOp("==");
            }else {
                op.setOp("<>");
            }
            node.setMiddle(op);
            if(!IsEnd()){
                NextSymbol();
            }else{
                return node;
            }
            node.setRight(additive_exp());
        }
        return node;
    }

    /*
     * chain -> declare-block ,chain | declare-block
     */
    public  static TreeNode chain(String type){
        TreeNode node = new TreeNode();
        node.setType(TreeNode.CHAIN);
        node.setLeft(declareBlock(type));
        // ,chain
        if(symbol.getType().equals(Symbol.COMMA)){
            if(!IsEnd()){
                NextSymbol();
            }else{
                return node;
            }
            node.setMiddle(chain(type));
        }
        return node;
    }

    /*
     * declare-block -> variable | variable = exp |
     * variable[ number ] | variable[ number ] = { exp-chains | ε }
     * *variable = &variable | *variable = NULL  | *variable = variable | *variable
     */
    public  static TreeNode declareBlock(String type){
        TreeNode node = new TreeNode();
        node.setType(TreeNode.DECLAREBLOCK);
        // *variable = &variable | *variable = NULL  | *variable = variable | *variable
        if(symbol.getType().equals(Symbol.MUL)){
            if(!IsEnd()){
                NextSymbol();
            }else{
                return node;
            }
            IsMatch(Symbol.VAR);
            TreeNode pointer = new TreeNode();
            pointer.setType(TreeNode.POINTER);
            pointer.setVarName(symbol.getVarName());
            node.setLeft(pointer);
            if(!IsEnd()){
                NextSymbol();
            }else{
                return node;
            }
            //语义分析,加入变量表
            String name = node.getLeft().getVarName();
            String vartype;
            if(type.equals(Symbol.INT))
                vartype = Symbol.PINT;
            else
                vartype = Symbol.PREAL;
            Variable var = new Variable(name,vartype);
            AddVar(var);
            // =
            if(symbol.getType().equals(Symbol.ASSIGN)){
                if(!IsEnd()){
                    NextSymbol();
                }else{
                    return node;
                }
                // &variable
                if(symbol.getType().equals(Symbol.PADDRESS)){
                    if(!IsEnd()){
                        NextSymbol();
                    }else{
                        return node;
                    }
                    TreeNode address = new TreeNode();
                    IsMatch(Symbol.VAR);
                    address.setType(TreeNode.ADDRESS);
                    address.setVarName(symbol.getVarName());
                    node.setMiddle(address);
                    if(!IsEnd()){
                        NextSymbol();
                    }else{
                        return node;
                    }
                    //int* ||real* X
                    String pname = address.getVarName();
                    if(IsTypeMatch(pname,Symbol.PINT)||IsTypeMatch(pname,Symbol.PREAL)){
                        existError = true;
                        System.out.println(pname+"不应为指针变量！");
                    }
                    //int* real X
                    //real* int X
                    if(IsTypeMatch(name,Symbol.PINT)&&IsTypeMatch(pname,Symbol.REAL)){
                        existError = true;
                        System.out.println("int*类型的指针不能指向real类型的变量！");
                    }
                    else if(IsTypeMatch(name,Symbol.PREAL)&&IsTypeMatch(pname,Symbol.INT)){
                        existError = true;
                        System.out.println("real*类型的指针不能指向int类型的变量！");
                    }
                }
                // NULL
                else if(symbol.getType().equals(Symbol.PNULL)){
                    TreeNode pnull = new TreeNode();
                    pnull.setType(TreeNode.PNULL);
                    node.setMiddle(pnull);
                    if(!IsEnd()){
                        NextSymbol();
                    }else{
                        return node;
                    }
                }
                // variable
                else if(symbol.getType().equals(Symbol.VAR)){
                    TreeNode treeNode = new TreeNode();
                    treeNode.setType(TreeNode.VAR);
                    treeNode.setVarName(symbol.getVarName());
                    node.setMiddle(treeNode);
                    if(!IsEnd()){
                        NextSymbol();
                    }else{
                        return node;
                    }
                    //int || real X
                    String pname = treeNode.getVarName();
                    if(IsDeclared(pname) == -1 ||IsTypeMatch(pname,Symbol.INT)||IsTypeMatch(pname,Symbol.REAL)){
                        existError = true;
                        System.out.println(pname+"应为指针变量！");
                    }
                    else return node;
                }
            }

        }
        // variable | variable = exp |
        // variable[ number ] | variable[ number ] = { exp-chains | ε }
        else {
            IsMatch(Symbol.VAR);
            TreeNode var = new TreeNode();
            var.setVarName(symbol.getVarName());
            var.setType(TreeNode.VAR);
            node.setLeft(var);
            if(!IsEnd()){
                NextSymbol();
            }else{
                return node;
            }

            //语义分析,加入变量表
            String name = node.getLeft().getVarName();
            Variable variable = new Variable(name,type);
            AddVar(variable);

            //variable = exp
            if(symbol.getType().equals(Symbol.ASSIGN)){
                if(!IsEnd()){
                    NextSymbol();
                }else{
                    return node;
                }
                node.setMiddle(exp());
                // int real X
                if(type.equals(Symbol.INT) && !IsAllVarInt(node.getMiddle())){
                    existError = true;
                    System.out.println("表达式不为int类型！");
                }
            }
            //variable[ number ] | variable[ number ] = { exp-chains | ε }
            else if(symbol.getType().equals(Symbol.LEFTBRACKET)){
                //更改变量表中的变量类型
                String vartype;
                if(type.equals(Symbol.INT))
                    vartype = Symbol.PINT;
                else
                    vartype = Symbol.PREAL;
                variable.setType(vartype);

                var.setType(TreeNode.ADDRESS);
                if(!IsEnd()){
                    NextSymbol();
                }else{
                    return node;
                }
                //number
                IsMatch(Symbol.NUMBER);
                TreeNode number = new TreeNode();
                number.setType(TreeNode.NUMBER);
                number.setValue(symbol.getValue());
                number.setValueType(symbol.getValueType());
                node.setMiddle(number);
                if(!IsEnd()){
                    NextSymbol();
                }else{
                    return node;
                }
                // ]
                IsMatch(Symbol.RIGHTBRACKET);
                //语义分析
                //number real X
                if(!IsAllVarInt(number)){
                    existError = true;
                    System.out.println(name+"数组下标应为整数！");
                }

                if(!IsEnd()){
                    NextSymbol();
                }else{
                    return node;
                }
                // = { exp-chains | ε }
                if(symbol.getType().equals(Symbol.ASSIGN)){
                    // {
                    if(!IsEnd()){
                        NextSymbol();
                    }else{
                        return node;
                    }
                    IsMatch(Symbol.LEFTBRACE);
                    if(!IsEnd()){
                        NextSymbol();
                    }else{
                        return node;
                    }
                    if(!symbol.getType().equals(Symbol.RIGHTBRACE)) {
                        node.setRight(expChains());
                        IsMatch(Symbol.RIGHTBRACE);
                        if(!IsEnd()){
                            NextSymbol();
                        }else{
                            return node;
                        }
                        //语义分析
                        //int* real X
                        if(IsTypeMatch(node.getLeft().getVarName(),Symbol.PINT) && !IsAllVarInt(node.getRight())){
                            existError=true;
                            System.out.println("int类型的数组不能赋值real类型的变量！");
                        }
                        return node;
                    }
                    if(!IsEnd()){
                        NextSymbol();
                    }else{
                        return node;
                    }
                }
            }
        }
        return node;
    }

    /*
     * exp-chains -> exp exp-chain
     */
    public  static TreeNode expChains(){
        TreeNode node = new TreeNode();
        node.setType(TreeNode.EXPCHAINS);
        node.setLeft(exp());
        node.setMiddle(expChain());
        return node;
    }

    /*
     * exp-chain -> , exp exp-chain | ε
     */
    public  static TreeNode expChain(){
        TreeNode node = new TreeNode();
        node.setType(TreeNode.EXPCHAIN);
        if(symbol.getType().equals(Symbol.COMMA)){
            if(!IsEnd()){
                NextSymbol();
            }else{
                return node;
            }
            node.setLeft(exp());
            node.setMiddle(expChain());
        }
        return node;
    }
}
