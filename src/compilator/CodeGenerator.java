package compilator;

import java.util.ArrayList;

public class CodeGenerator {
    private static CodeGenerator instance = null;
    private String finalText = new String();
    private int varcount = 0;
    private int varPosition = 0;
    
    public static CodeGenerator getInstance() {
        if (instance == null) {
            instance = new CodeGenerator();
        }
        return instance;
    }
    
    public void CreateCode(Object label, String command, Object target1, Object target2){
        finalText = finalText + label + command + target1 + target2 + '\n';
    }
    
    public String getFinalCodeText(){
        return finalText;
    }
    
    public void addVarCount(){
        varcount++;
    }
    
    public void clearVarCount(){
        varcount = 0;
    }
    
    public int getVarCount(){
        return varcount;
    }
    
    public void setVarPosition(int plus){
        varPosition = varPosition + plus;
    }
    
    public void relaseVarPosition(int minus){
        varPosition = varPosition - minus;
    }
    
    
    public int getVarPosition(){
        return varPosition;
    }
    
    /*public void postfixCreation(ArrayList<String> postifx, SemanticAnalyzer semantic){
        String value;
        int positivoVar =-1, positionFun = -1;
        for(int count =0; count < postifx.size(); count++){
            positivoVar =this.varPosition - semantic.varCountSearch(postifx.get(count)) -1;
            positionFun = semantic.funcDeclSearch(postifx.get(count));
            if(semantic.varDeclSearch(postifx.get(count)) != -1){
                this.Create("", "LDV ", positivoVar, "");
            }
            else{
                if(postifx.get(count).equals("+")){
                    this.Create("", "ADD", "", "");
                }
                else{
                    if(postifx.get(count).equals("*")){
                        this.Create("", "MULT", "", "");
                    }
                    else{
                        if(postifx.get(count).equals("div")){
                            this.Create("", "DIVI", "", "");
                        }
                        else{
                            if(postifx.get(count).equals("-")){
                                this.Create("", "SUB", "", "");
                            }
                            else{
                                if(postifx.get(count).equals("!")){
                                    this.Create("", "INV", "", "");
                                }
                                else{
                                    if(postifx.get(count).equals("e")){
                                        this.Create("", "AND", "", "");
                                    }
                                    else{
                                        if(postifx.get(count).equals("ou")){
                                            this.Create("", "OR", "", "");
                                        }
                                        else{
                                            if(postifx.get(count).equals("<")){
                                                this.Create("", "CME", "", "");
                                            }
                                            else{
                                                if(postifx.get(count).equals(">")){
                                                    this.Create("", "CMA", "", "");
                                                }
                                                else{
                                                    if(postifx.get(count).equals("=")){
                                                        this.Create("", "CEQ", "", "");
                                                    }
                                                    else{
                                                        if(postifx.get(count).equals("!=")){
                                                            this.Create("", "CDIF", "", "");
                                                        }
                                                        else{
                                                            if(postifx.get(count).equals("<=")){
                                                                this.Create("", "CMEQ", "", "");
                                                            }
                                                            else{
                                                                if(postifx.get(count).equals(">=")){
                                                                    this.Create("", "CMAQ", "", "");
                                                                }
                                                                else{
                                                                    if(!postifx.get(count).equals("@")){
                                                                        if(postifx.get(count).equals("verdadeiro")){
                                                                            this.Create("", "LDC ", 0, "");
                                                                        }
                                                                        else{
                                                                            if(postifx.get(count).equals("falso")){
                                                                                this.Create("", "LDC ", 1, "");
                                                                            }
                                                                            else{
                                                                                if(positionFun != -1){
                                                                                    this.Create("", "CALL ", "L"+((Function)semantic.symbolsTable.get(positionFun)).rotulo, "");
                                                                                }
                                                                                else this.Create("", "LDC ", postifx.get(count), "");
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }*/
}