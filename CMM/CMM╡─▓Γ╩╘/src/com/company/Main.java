package com.company;

import com.company.Tables.TreeNode;

import java.io.FileNotFoundException;
import java.io.FileReader;

public class Main {
    public static void main(String[] args) {

//        Lex.file = "/home/pope/test";
        Lex.file = "/Users/yidan/Documents/解释器构造/CMM(已修改词法语法)/test";
//        Lex.file ="/Users/xyh/desktop/CMM的测试/test";
//        Lex.file = "F:\\test.txt";
        try {
            Lex.fr = new FileReader(Lex.file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        Lex.Lex();
        /*System.out.println(Lex.symbols.size());
        for(int i=0;i<Lex.symbols.size();i++){
            System.out.println(Lex.symbols.get(i).getType());
        }*/

        Parse.lexArrayList = Lex.symbols;
        Parse.symbol = Parse.lexArrayList.get(Parse.index);
        TreeNode node = Parse.program();
        if(!Parse.existError) {
            IntermediateCodeGeneration.syntaxTree = node;
            IntermediateCodeGeneration.program(IntermediateCodeGeneration.syntaxTree);
            for(int i=0 ;i<IntermediateCodeGeneration. fourItemsGroup.size() ;i++){
                System.out.println (IntermediateCodeGeneration.fourItemsGroup.get(i).getPart1() + '\t'+
                        IntermediateCodeGeneration . fourItemsGroup .get (i) .getPart2()+'\t'+
                        IntermediateCodeGeneration. fourItemsGroup.get(i) .getPart3() +'\t'+
                        IntermediateCodeGeneration. fourItemsGroup.get (i) .getPart4()) ;
            }
            Operator.fourItemsArrayList = IntermediateCodeGeneration.fourItemsGroup;
            Operator.performFourItems();
        }else {
            System.out.println("语法分析错误！");
        }
        System.out.println(1);
    }
}
