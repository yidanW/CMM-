package com.company;

import com.company.Tables.Symbol;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Lex {
    //���
    public static ArrayList<Symbol> symbols = new ArrayList<>();
    //�ļ�λ��
    public static String file = "";
    //�ļ���--��file�������ļ�
    public static FileReader fr = null;
    //��ȡ�����ַ���int��
    public static int ch = 0;
    //ָ��ǰ��ȡ�����ַ�
    public static char pointer = ' ';
    //����Ѷ�ȡ���ַ�
    public static String storeStr = "";

    /*
     * ��ȡ��һ���ַ�
     * */
    public static void NextChar() {
        try {
            ch = fr.read();
            if(ch != -1){
                pointer = (char)ch;
            }else {
                pointer = '$';
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /*
     * �ж������ַ��Ƿ�Ϊ�����
     * "=="��"<>"���ݵ�һ���ַ���������'<'��'='�����
     * */
    public static boolean IsOp(char ch){
        if(ch == '+'||
                ch == '-'||
                ch == '*'||
                ch == '/'||
                ch == '<'||
                ch == '>'||
                ch == '='){
            return true;
        }else {
            return false;
        }
    }
    /*
     * �ж������ַ��Ƿ�Ϊ��ĸ
     * */
    public static boolean IsLetter(char c){
        if (((c <= 'z') && (c >= 'a')) || ((c <= 'Z') && (c >= 'A')))
            return true;
        else
            return false;
    }
    /*
     * �ж�storeStr�Ƿ�Ϊ��ʶ��(ȥ�����ܶ�ȡ���Ŀո��±���ʶ������)
     * */
    public static boolean IsIdentifier(){
        //ȥ���ո�
        storeStr = storeStr.trim();
        //�Ƿ����»��߻���ĸ��ͷ
        if(storeStr.charAt(0) == '_'||IsLetter(storeStr.charAt(0))||storeStr.charAt(0) == ' '){
            //�Ƿ�����������ĸ���
            for(int i = 1;i<storeStr.length(); i++){
                if(Character.isDigit(storeStr.charAt(i))||IsLetter(storeStr.charAt(i))||storeStr.charAt(i) == ' '){

                }else {
                    return false;
                }
            }
            return true;
        }
        return false;
    }
    /*
     * �ж�storeStr�Ƿ�Ϊ�Ϸ�����
     * */
    public static boolean IsLegalNumber() {
        int pointNumber = 0;
        for(int i = 0;i<storeStr.length();i++){
            if(Character.isDigit(storeStr.charAt(i))||storeStr.charAt(i) == ' '){

            }else if(storeStr.charAt(i) == '.'){
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
    * 判断数字是否为整数
    * */
    public static boolean IsInt(String str){
        for(int i = 0;i<str.length();i++){
            if(storeStr.charAt(i) == '.'){
                return false;
            }
        }
        return true;
    }

    /*
     * �ж�storeStr�Ƿ�Ϊ�ؼ���
     * ����������storeStrδ���
     * */
    public static boolean IsSymbol(){
        Symbol symbol = new Symbol();
        switch (storeStr) {
            case "":
                return true;
            case "+":
                symbol.setType(Symbol.ADD);
                symbols.add(symbol);
                return true;
            case "-":
                symbol.setType(Symbol.SUB);
                symbols.add(symbol);
                return true;
            case "*":
                symbol.setType(Symbol.MUL);
                symbols.add(symbol);
                return true;
            case "/":
                symbol.setType(Symbol.DIV);
                symbols.add(symbol);
                return true;
            case "=":
                if (pointer == '=') {
                    storeStr = storeStr + pointer;
                    NextChar();
                    if (IsSymbol()) {
                        return true;
                    } else {
                        return false;
                    }
                } else {
                    symbol.setType(Symbol.ASSIGN);
                    symbols.add(symbol);
                }
                return true;
            case "<":
                if (pointer == '>') {
                    storeStr = storeStr + pointer;
                    NextChar();
                    IsSymbol();
                } else {
                    symbol.setType(Symbol.LESS);
                    symbols.add(symbol);
                }
                return true;
            case ">":
                symbol.setType(Symbol.MORE);
                symbols.add(symbol);
                return true;
            case "==":
                symbol.setType(Symbol.EQUAL);
                symbols.add(symbol);
                return true;
            case "<>":
                symbol.setType(Symbol.NOT_EQUAL);
                symbols.add(symbol);
                return true;
            case "(":
                symbol.setType(Symbol.LEFTPARENT);
                symbols.add(symbol);
                return true;
            case ")":
                symbol.setType(Symbol.RIGHTPARENT);
                symbols.add(symbol);
                return true;
            case "{":
                symbol.setType(Symbol.LEFTBRACE);
                symbols.add(symbol);
                return true;
            case "}":
                symbol.setType(Symbol.RIGHTBRACE);
                symbols.add(symbol);
                return true;
            case ";":
                symbol.setType(Symbol.SEMICOLON);
                symbols.add(symbol);
                return true;
            case "if":
                symbol.setType(Symbol.IF);
                symbols.add(symbol);
                return true;
            case "else":
                symbol.setType(Symbol.ELSE);
                symbols.add(symbol);
                return true;
            case "while":
                symbol.setType(Symbol.WHILE);
                symbols.add(symbol);
                return true;
            case "int":
                symbol.setType(Symbol.INT);
                symbols.add(symbol);
                return true;
            case "real":
                symbol.setType(Symbol.REAL);
                symbols.add(symbol);
                return true;
            case "write":
                symbol.setType(Symbol.WRITE);
                symbols.add(symbol);
                return true;
            case "read":
                symbol.setType(Symbol.READ);
                symbols.add(symbol);
                return true;

            //����
            case "[":
                IsSymbol();
                symbol.setType(Symbol.LEFTBRACKET);
                symbols.add(symbol);
                return true;
            case "]":
                IsSymbol();
                symbol.setType(Symbol.RIGHTBRACKET);
                symbols.add(symbol);
                return true;
            case "NULL":
                symbol.setType(Symbol.PNULL);
                symbols.add(symbol);
                return true;
            case "&":
                symbol.setType(Symbol.PADDRESS);
                symbols.add(symbol);
                return true;
            case ",":
                symbol.setType(Symbol.COMMA);
                symbols.add(symbol);
                return true;
            //��ֹ

            //��storeStr��Ϊ�ؼ���ʱ
            default:
                if (IsIdentifier()) {
                    symbol.setType(Symbol.VAR);
                    symbol.setVarName(storeStr);
                    symbols.add(symbol);
                    return true;
                } else if (IsLegalNumber()) {
                    symbol.setType(Symbol.NUMBER);
                    if(IsInt(storeStr)){
                        symbol.setValueType("int");
                    }
                    else {
                        symbol.setValueType("real");
                    }
                    symbol.setValue(Double.parseDouble(storeStr));
                    symbols.add(symbol);
                    return true;
                }
        }
        return false;
    }
    /*
     * �ж�pointer�Ƿ�Ϊ�ָ����������/�ո�/����/���ţ�
     * ��pointerΪ�ָ�������storeStr�е��ַ������жϣ�����pointer�����ж�
     * */
    public static boolean _PointerIsSymbol(){
        Symbol symbol = new Symbol();
        switch (pointer){
            case '+':
                IsSymbol();
                symbol.setType(Symbol.ADD);
                symbols.add(symbol);
                return true;
            case '-':
                IsSymbol();
                symbol.setType(Symbol.SUB);
                symbols.add(symbol);
                return true;
            case '*':
                IsSymbol();
                symbol.setType(Symbol.MUL);
                symbols.add(symbol);
                return true;
            case '/':
                IsSymbol();
                symbol.setType(Symbol.DIV);
                symbols.add(symbol);
                return true;
            case '<':
                NextChar();
                if(pointer == '>'){
                    IsSymbol();
                    symbol.setType(Symbol.NOT_EQUAL);
                    symbols.add(symbol);
                    return true;
                }else {
                    IsSymbol();
                    symbol.setType(Symbol.LESS);
                    symbols.add(symbol);
                    return false;
                }
            case '>':
                IsSymbol();
                symbol.setType(Symbol.MORE);
                symbols.add(symbol);
                return true;
            case '=':
                NextChar();
                if(pointer == '='){
                    IsSymbol();
                    symbol.setType(Symbol.EQUAL);
                    symbols.add(symbol);
                    return true;
                }else {
                    IsSymbol();
                    symbol.setType(Symbol.ASSIGN);
                    symbols.add(symbol);
                    return false;
                }
            case ';':
                IsSymbol();
                symbol.setType(Symbol.SEMICOLON);
                symbols.add(symbol);
                return true;
            case '(':
                IsSymbol();
                symbol.setType(Symbol.LEFTPARENT);
                symbols.add(symbol);
                return true;
            case ')':
                IsSymbol();
                symbol.setType(Symbol.RIGHTPARENT);
                symbols.add(symbol);
                return true;
            case '{':
                IsSymbol();
                symbol.setType(Symbol.LEFTBRACE);
                symbols.add(symbol);
                return true;
            case '}':
                IsSymbol();
                symbol.setType(Symbol.RIGHTBRACE);
                symbols.add(symbol);
                return true;

            //����
            case '[':
                IsSymbol();
                symbol.setType(Symbol.LEFTBRACKET);
                symbols.add(symbol);
                return true;
            case ']':
                IsSymbol();
                symbol.setType(Symbol.RIGHTBRACKET);
                symbols.add(symbol);
                return true;
            case '&':
                symbol.setType(Symbol.PADDRESS);
                symbols.add(symbol);
                return true;
            case ',':
                IsSymbol();
                symbol.setType(Symbol.COMMA);
                symbols.add(symbol);
                return true;
            //��ֹ

        }

        return false;
    }


    /*
     * �����ļ������з���
     * */
    public static void Lex() {
        //���ļ�δ����ʱ
        //����һ���ַ�
        NextChar();
        while (pointer!='$'){
            //����ȡ���ո�/���з�/�س���/�����/��С����/��С����/������/�һ�����/�ֺ�ʱ���Ѷ�ȡ�����ַ������бȽϽ��бȽ�
            //�����ָ��� ȡ��ַ��'&'  '['  ']' ','
            if (IsOp(pointer) ||
                    pointer == '('||
                    pointer == ')'||
                    pointer == '{'||
                    pointer == '}'||
                    pointer == '['||     //����
                    pointer == ']'||     //����
                    pointer == '&'||     //����
                    pointer == ','||     //����
                    pointer == ';') {
                if(_PointerIsSymbol()){
                    storeStr = "";
                }else {
                    storeStr = "";
                    storeStr = storeStr + pointer;
                    if("&".equals(storeStr) || "{".equals(storeStr) || "*".equals(storeStr) || "(".equals(storeStr) ){
                        IsSymbol();
                        storeStr = "";
                    }
                    if(" ".equals(storeStr)){
                        storeStr = "";
                    }
                }
                NextChar();
            }else if(pointer == ' ' ||
                    pointer == '\n' ||
                    pointer == '\r' ||
                    pointer == '\t'){
                IsSymbol();
                storeStr = "";
                NextChar();
            }
            //pointer��Ϊ���ʷָ���ʱֱ�Ӵ���storeStr
            else {
                storeStr = storeStr + pointer;
                NextChar();
            }
        }
        IsSymbol();
    }
}
