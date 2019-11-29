package com.company;

import com.company.Tables.FourItems;
import com.company.Tables.TreeNode;
import java.util.ArrayList;


public class IntermediateCodeGeneration {
    //语法分析后生成的语法树
    public static TreeNode syntaxTree = new TreeNode();
    //四元式列表
    public static ArrayList<FourItems> fourItemsGroup = new ArrayList<>();
    //四元式数量
    public static int fourItemsNumber = -1;
    //代码层数
    public static int codeLevel = 0;

    /*
     * program -> stmt-sequence
     * */
    public static void program(TreeNode node){
        stmt_sequence(node.getLeft());
    }
    /*
     * stmt-sequence -> statement stmt-sequence | statement | ε
     * */
    public static void stmt_sequence(TreeNode node){
        statement(node.getLeft());
        if(node.getMiddle() != null){
            stmt_sequence(node.getMiddle());
        }
    }
    /*
     * statement -> if-stmt | while-stmt | assign-stmt | read-stmt | write-stmt | declare-stmt
     * */
    public static void statement(TreeNode node){
        switch (node.getLeft().getType()){
            //statement->if-stmt
            case TreeNode.IF:
                if_stmt(node.getLeft());
                break;
            //statement->while-stmt
            case TreeNode.WHILE:
                while_stmt(node.getLeft());
                break;
            //statement->read-stmt
            case TreeNode.READ:
                read_stmt(node.getLeft());
                break;
            //statement->write-stmt
            case TreeNode.WRITE:
                write_stmt(node.getLeft());
                break;
            //statement->assign-stmt
            case TreeNode.ASSIGN:
                assign_stmt(node.getLeft());
                break;
            //statement->declare-stmt
            case TreeNode.DECLARE:
                declare_stmt(node.getLeft());
                break;
            default:
                break;
        }
    }
    /*
     * stmt-block -> statement | {stmt-sequence}
     * */
    public static void stmt_block(TreeNode node){
        //stmt-block->statement
        if(node.getLeft().getType() == TreeNode.STATEMENT){
            statement(node.getLeft());
        }
        //stmt-block->{stmt-sequence}
        else{
            codeLevel++;
            fourItemsGroup.add(new FourItems(FourItems.LEVEL_PLUS, null, null, null));
            fourItemsNumber++;

            stmt_sequence(node.getLeft());

            fourItemsGroup.add(new FourItems(FourItems.LEVEL_MINUS, null, null, null));
            fourItemsNumber++;
            codeLevel--;
        }
    }
    /*
     * if-stmt -> if (exp) stmt-block | if (exp) stmt-block else stmt-block
     * */
    public static void if_stmt(TreeNode node){
        FourItems jumpf = new FourItems(FourItems.JMPF, exp(node.getLeft()), null, null);
        fourItemsGroup.add(jumpf);
        fourItemsNumber++;

        stmt_block(node.getMiddle());

        //jumpf指向if代码块下一行，因此fourItemNumber要+1
        jumpf.setPart4(String.valueOf(fourItemsNumber + 1));

        //如果存在else部分则读取else
        if(node.getRight() != null){
            FourItems elseJump = new FourItems(FourItems.JMP, null, null, null);
            fourItemsGroup.add(elseJump);
            fourItemsNumber++;
            //if跳转到else部分
            jumpf.setPart4(String.valueOf(fourItemsNumber + 1));
            codeLevel++;
            fourItemsGroup.add(new FourItems(FourItems.LEVEL_PLUS, null, null, null));
            fourItemsNumber++;

            stmt_block(node.getRight());

            fourItemsGroup.add(new FourItems(FourItems.LEVEL_MINUS, null, null, null));
            fourItemsNumber++;
            codeLevel--;
            //elseJump指向else代码块下一行
            elseJump.setPart4(String.valueOf(fourItemsNumber + 1));
        }
    }
    /*
     * while-stmt -> while (exp) stmt-block
     * */
    public static void while_stmt(TreeNode node){
        //创建四元组jump，while循环循环体结束时无条件跳转至循环开头
        //jump指向while循环开头，因此应该指向fourItemsNumber + 1
        FourItems jump = new FourItems(FourItems.JMP, null, null, String.valueOf(fourItemsNumber + 1));

        FourItems jumpf = new FourItems(FourItems.JMPF, exp(node.getLeft()), null, null);
        fourItemsGroup.add(jumpf);;
        fourItemsNumber++;

        stmt_block(node.getMiddle());

        //添加jump
        fourItemsGroup.add(jump);
        fourItemsNumber++;
        jumpf.setPart4(String.valueOf(fourItemsNumber + 1));
    }

    /*
     * assign-stmt -> variable = exp;
     * */
    /*public static void assign_stmt(TreeNode node){
        fourItemsGroup.add(new FourItems(FourItems.ASSIGN,exp(node.getMiddle()),null,node.getLeft().getVarName()));
        fourItemsNumber++;
    }
	*/

    //修改
    /*
     * assign-stmt -> ( variable=exp | variable[exp] = exp | variable = &variable | variable =NULL | *variable = exp );
     * */
    public static void assign_stmt(TreeNode node){
        if(node.getRight() != null){
            if(node.getRight().getType() == TreeNode.INT){               // int variable =NULL
                fourItemsGroup.add(new FourItems(FourItems.ASSIGN, FourItems.PNULL, "int", node.getLeft().getVarName()));
                fourItemsNumber++;
            }else if(node.getRight().getType() == TreeNode.REAL){         // real variable =NULL
                fourItemsGroup.add(new FourItems(FourItems.ASSIGN, FourItems.PNULL, "real", node.getLeft().getVarName()));
                fourItemsNumber++;
            }else if(node.getRight().getType() == TreeNode.EXP){           // variable[exp] = exp
                fourItemsGroup.add(new FourItems(FourItems.ASSIGN,exp(node.getRight()),(new Double(Double.parseDouble(exp(node.getMiddle())))).intValue()+"", node.getLeft().getVarName()));
                fourItemsNumber++;
            }
        }else{
            if(node.getMiddle().getType() == TreeNode.ADDRESS){          //variable = &variable
                fourItemsGroup.add(new FourItems(FourItems.ASSIGN, node.getMiddle().getVarName(), null, node.getLeft().getVarName()));
                fourItemsNumber++;
            }else if(node.getMiddle().getType() == TreeNode.EXP){     // variable=exp 或者 *variable = exp
                fourItemsGroup.add(new FourItems(FourItems.ASSIGN,exp(node.getMiddle()),null,node.getLeft().getVarName()));
                fourItemsNumber++;
            }
        }
    }

    /*
     * read-stmt -> read variable;
     * */
    public static void read_stmt(TreeNode node){
        fourItemsGroup.add(new FourItems(FourItems.READ, null, null, node.getLeft().getVarName()));
        fourItemsNumber++;
    }

    /*
     * write-stmt -> write exp;
     * */
    public static void write_stmt(TreeNode node){
        fourItemsGroup.add(new FourItems(FourItems.WRITE, null, null, exp(node.getLeft())));
        fourItemsNumber++;
    }
    /*
     * declare-stmt -> (int | real) variable; | (int | real) variable= exp;
     * */
    /*public static void declare_stmt(TreeNode node){
        //声明为int
        if(node.getLeft().getType() == TreeNode.INT){
            fourItemsGroup.add(new FourItems(FourItems.INT, null, null, node.getMiddle().getVarName()));
            fourItemsNumber++;
        }
        //声明为real
        else{
            fourItemsGroup.add(new FourItems(FourItems.INT, null, null, node.getMiddle().getVarName()));
            fourItemsNumber++;
        }
        //有赋值
        if(node.getRight() != null){
            fourItemsGroup.add(new FourItems(FourItems.ASSIGN, String.valueOf(node.getRight().getValue()), null, node.getMiddle().getVarName()));
            fourItemsNumber++;
        }
    }*/

    //修改
    /*
     * declare-stmt -> (int | real) chain;
     * */
    public static void declare_stmt(TreeNode node){
        //声明为int
        if(node.getLeft().getType() == TreeNode.INT){
            chain(node.getMiddle(),TreeNode.INT);
        }
        //声明为real
        else if(node.getLeft().getType() == TreeNode.REAL){
            chain(node.getMiddle(),TreeNode.REAL);
        }else{
            return;
        }
    }

    //新增
    /*
     * exp-chain -> , exp exp-chain | ε
     * */
    public static String exp_chain(TreeNode node){
        if(node.getMiddle() != null){
            String temp = exp_chain(node.getMiddle());
            return "," + exp(node.getLeft()) + temp;
        }
        else{
            return "";
        }
    }
    /*
     * exp-chains -> exp exp-chain
     * */
    public static String exp_chains(TreeNode node){
        if(node.getLeft() != null){
            String temp = exp_chain(node.getMiddle());
            return exp(node.getLeft()) + temp;
        }
        else{
            return "";
        }
    }
    /*
     * chain -> declare-block ,chain | declare-block
     * */
    public static void chain(TreeNode node,int type){
        declare_block(node.getLeft(),type);
        if(node.getMiddle() != null){
            chain(node.getMiddle(),type);
        }
    }
    /*
     * declare-block -> variable | variable=exp | *variable | *variable = &variable | *variable = variable |  *variable = NULL  |  variable[ number ]  | variable[ number ] = { exp-chains | ε }
     * */
    public static void declare_block(TreeNode node,int type){
        if(type == TreeNode.INT){
            if(node.getLeft().getType() == TreeNode.POINTER){
                if(node.getMiddle() == null){                        //*variable
                    fourItemsGroup.add(new FourItems(FourItems.PINT, null, null, node.getLeft().getVarName()));
                    fourItemsNumber++;
                }else if(node.getMiddle().getType() == TreeNode.PNULL){    //*variable = NULL
                    fourItemsGroup.add(new FourItems(FourItems.PINT, null, null, node.getLeft().getVarName()));
                    fourItemsNumber++;
                    fourItemsGroup.add(new FourItems(FourItems.ASSIGN, FourItems.PNULL, "int", node.getLeft().getVarName()));
                    fourItemsNumber++;
                }else if(node.getMiddle().getType() == TreeNode.VAR){      //*variable = variable
                    fourItemsGroup.add(new FourItems(FourItems.PINT, null, null, node.getLeft().getVarName()));
                    fourItemsNumber++;
                    fourItemsGroup.add(new FourItems(FourItems.ASSIGN, node.getMiddle().getVarName(), null, node.getLeft().getVarName()));
                    fourItemsNumber++;
                }else if(node.getMiddle().getType() == TreeNode.ADDRESS){      //*variable = &variable
                    fourItemsGroup.add(new FourItems(FourItems.PINT, null, null, node.getLeft().getVarName()));
                    fourItemsNumber++;
                    fourItemsGroup.add(new FourItems(FourItems.ASSIGN, node.getMiddle().getVarName(), null, node.getLeft().getVarName()));
                    fourItemsNumber++;
                }
            }else if(node.getLeft().getType() == TreeNode.ADDRESS){
                if(node.getMiddle().getType() == TreeNode.NUMBER){
                    //variable[number]
                    if(node.getRight() == null){
                        fourItemsGroup.add(new FourItems(FourItems.INTARRAY, null, (new Integer((int)node.getMiddle().getValue())).intValue()+"", node.getLeft().getVarName()));
                        fourItemsNumber++;
                    }else{
                        //variable[number] = {exp-chains|ε}
                        fourItemsGroup.add(new FourItems(FourItems.INTARRAY, null, (new Integer((int)node.getMiddle().getValue())).intValue()+"", node.getLeft().getVarName()));
                        fourItemsNumber++;
                        fourItemsGroup.add(new FourItems(FourItems.ASSIGN, "{" + exp_chains(node.getRight()) + "}", null, node.getLeft().getVarName()));
                        fourItemsNumber++;
                    }
                }else{
                    return;
                }
            }else if(node.getLeft().getType() == TreeNode.VAR){
                if(node.getMiddle() == null){                      //variable
                    fourItemsGroup.add(new FourItems(FourItems.INT, null, null, node.getLeft().getVarName()));
                    fourItemsNumber++;
                }else if(node.getMiddle().getType() == TreeNode.EXP){     //variable=exp
                    fourItemsGroup.add(new FourItems(FourItems.INT, null, null, node.getLeft().getVarName()));
                    fourItemsNumber++;
                    fourItemsGroup.add(new FourItems(FourItems.ASSIGN, exp(node.getMiddle()), null, node.getLeft().getVarName()));
                    fourItemsNumber++;
                }
            }
        }else if(type==TreeNode.REAL){
            if(node.getLeft().getType() == TreeNode.POINTER){
                if(node.getMiddle() == null){                        //*variable
                    fourItemsGroup.add(new FourItems(FourItems.PREAL, null, null, node.getLeft().getVarName()));
                    fourItemsNumber++;
                }else if(node.getMiddle().getType() == TreeNode.PNULL){    //*variable = NULL
                    fourItemsGroup.add(new FourItems(FourItems.PREAL, null, null, node.getLeft().getVarName()));
                    fourItemsNumber++;
                    fourItemsGroup.add(new FourItems(FourItems.ASSIGN, FourItems.PNULL, "real", node.getLeft().getVarName()));
                    fourItemsNumber++;
                }else if(node.getMiddle().getType() == TreeNode.VAR){      //*variable = variable
                    fourItemsGroup.add(new FourItems(FourItems.PREAL, null, null, node.getLeft().getVarName()));
                    fourItemsNumber++;
                    fourItemsGroup.add(new FourItems(FourItems.ASSIGN, node.getMiddle().getVarName(), null, node.getLeft().getVarName()));
                    fourItemsNumber++;
                }else if(node.getMiddle().getType() == TreeNode.ADDRESS){      //*variable = &variable
                    fourItemsGroup.add(new FourItems(FourItems.PREAL, null, null, node.getLeft().getVarName()));
                    fourItemsNumber++;
                    fourItemsGroup.add(new FourItems(FourItems.ASSIGN, node.getMiddle().getVarName(), null, node.getLeft().getVarName()));
                    fourItemsNumber++;
                }
            }else if(node.getLeft().getType() == TreeNode.ADDRESS){
                if(node.getMiddle().getType() == TreeNode.NUMBER){   //variable[number]
                    if(node.getRight() == null){
                        fourItemsGroup.add(new FourItems(FourItems.REALARRAY, null, (new Integer((int)node.getMiddle().getValue())).intValue()+"", node.getLeft().getVarName()));
                        fourItemsNumber++;
                    }else{                                           //variable[number] = {exp-chains|ε}
                        fourItemsGroup.add(new FourItems(FourItems.REALARRAY, null, (new Integer((int)node.getMiddle().getValue())).intValue()+"", node.getLeft().getVarName()));
                        fourItemsNumber++;
                        fourItemsGroup.add(new FourItems(FourItems.ASSIGN, "{" + exp_chains(node.getRight()) + "}",null, node.getLeft().getVarName()));
                        fourItemsNumber++;
                    }
                }else{
                    return;
                }
            }else if(node.getLeft().getType() == TreeNode.VAR){
                if(node.getMiddle() == null){                      //variable
                    fourItemsGroup.add(new FourItems(FourItems.REAL, null, null, node.getLeft().getVarName()));
                    fourItemsNumber++;
                }else if(node.getMiddle().getType() == TreeNode.EXP){     //variable=exp
                    fourItemsGroup.add(new FourItems(FourItems.REAL, null, null, node.getLeft().getVarName()));
                    fourItemsNumber++;
                    fourItemsGroup.add(new FourItems(FourItems.ASSIGN, exp(node.getMiddle()), null, node.getLeft().getVarName()));
                    fourItemsNumber++;
                }
            }
        }
    }

    /*
     * factor -> number | variable | (exp) | add-op exp
     * 返回的String为"L" + 行数，指某行存储的值。
     * 如"L10"指第十行计算后存储起来的的值
     * */
    /*public static String factor(TreeNode node){
        switch (node.getLeft().getType()){
            case TreeNode.NUMBER:
                return String.valueOf(node.getLeft().getValue());
            case TreeNode.VAR:
                return node.getLeft().getVarName();
            case TreeNode.EXP:
                return exp(node.getLeft());
            default:
                return "-1";
        }
    }*/

    //修改
    /*
     * factor -> ( exp ) | number | variable | variable[exp] | *variable
     * 返回的String为"L" + 行数，指某行存储的值。
     * 如"L10"指第十行计算后存储起来的的值
     * */
    public static String factor(TreeNode node){
        switch (node.getLeft().getType()){
            case TreeNode.NUMBER:
                if(node.getLeft().getValueType().equals("int"))
                    return String.valueOf((int)node.getLeft().getValue());
                else
                    return String.valueOf(node.getLeft().getValue());
            case TreeNode.VAR:
                return node.getLeft().getVarName();
            case TreeNode.POINTER:
                return "*" + node.getLeft().getVarName();
            case TreeNode.ADDRESS:
                FourItems array = new FourItems(FourItems.ARRAY,node.getLeft().getVarName(),(new Double(exp(node.getMiddle()))).intValue()+"",null);
                fourItemsNumber++;//指向本行
                array.setPart4("L" + fourItemsNumber);
                fourItemsGroup.add(array);
                return "L" + fourItemsNumber;
            case TreeNode.EXP:
                return exp(node.getLeft());
            default:
                return "-1";
        }
    }


    /*
     * term -> factor mul-op term | factor
     * 返回的String为"L" + 行数，指某行存储的值。
     * 如"L10"指第十行计算后存储起来的的值
     * */
    public static String term(TreeNode node){
        if(node.getMiddle() == null){
            return factor(node.getLeft());
        }else {
            if(node.getMiddle().getOp().equals("*")){
                FourItems mul = new FourItems(FourItems.MUL,factor(node.getLeft()),term(node.getRight()),null);
                fourItemsNumber++; //指向本行
                mul.setPart4("L" + fourItemsNumber);
                fourItemsGroup.add(mul);
            }else{
                FourItems div = new FourItems(FourItems.DIV,factor(node.getLeft()),term(node.getRight()),null);
                fourItemsNumber++;//指向本行
                div.setPart4("L" + fourItemsNumber);
                fourItemsGroup.add(div);
            }
            return "L" + fourItemsNumber;
        }
    }
    /*
     * additive-exp -> term add-op additive-exp | term
     * */
    public static String additive_exp(TreeNode node){
        if(node.getMiddle() == null){
            return term(node.getLeft());
        }else {
            if(node.getMiddle().getOp().equals("+")){
                FourItems add = new FourItems(FourItems.ADD,term(node.getLeft()),additive_exp(node.getRight()),null);
                fourItemsNumber++;//指向本行
                add.setPart4("L" + fourItemsNumber);
                fourItemsGroup.add(add);
            }else {
                FourItems sub = new FourItems(FourItems.SUB,term(node.getLeft()),additive_exp(node.getRight()),null);
                fourItemsNumber++;//指向本行
                sub.setPart4("L" + fourItemsNumber);
                fourItemsGroup.add(sub);
            }
            return "L" + fourItemsNumber;
        }
    }
    /*
     * exp -> additive-exp logical-op additive-exp | additive-exp
     * */
    public static String exp(TreeNode node){
        if(node.getMiddle() == null){
            return additive_exp(node.getLeft());
        }else {
            if(node.getMiddle().getOp().equals("<")){
                FourItems less = new FourItems(FourItems.LESS,additive_exp(node.getLeft()),additive_exp(node.getRight()),null);
                fourItemsNumber++;//指向本行
                less.setPart4("L" + fourItemsNumber);
                fourItemsGroup.add(less);
            }else if(node.getMiddle().getOp().equals(">")){
                FourItems more = new FourItems(FourItems.MORE,additive_exp(node.getLeft()),additive_exp(node.getRight()),null);
                fourItemsNumber++;//指向本行
                more.setPart4("L" + fourItemsNumber);
                fourItemsGroup.add(more);
            }else if(node.getMiddle().getOp().equals("==")){
                FourItems equal = new FourItems(FourItems.EQUAL,additive_exp(node.getLeft()),additive_exp(node.getRight()),null);
                fourItemsNumber++;//指向本行
                equal.setPart4("L" + fourItemsNumber);
                fourItemsGroup.add(equal);
            }else if(node.getMiddle().getOp().equals("<>")){
                FourItems not_equal = new FourItems(FourItems.NOT_EQUAL,additive_exp(node.getLeft()),additive_exp(node.getRight()),null);
                fourItemsNumber++;//指向本行
                not_equal.setPart4("L" + fourItemsNumber);
                fourItemsGroup.add(not_equal);
            }
            return "L" + fourItemsNumber;
        }
    }

}
