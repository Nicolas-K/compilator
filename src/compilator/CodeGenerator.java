package compilator;

import java.util.ArrayList;

public class CodeGenerator {

    private static CodeGenerator instance = null;
    private String codeText = new String();
    private int variableCount = 0;
    private int variablePosition = 0;

    public static CodeGenerator getInstance() {
        if (instance == null) {
            instance = new CodeGenerator();
        }
        return instance;
    }

    public void CreateCode(Object label, String command, Object target1, Object target2) {
        codeText = codeText + label + command + target1 + target2 + '\n';
    }

    public String getCodeText() {
        return codeText;
    }

    public void increaseVariableCount() {
        variableCount++;
    }

    public void resetVariableCount() {
        variableCount = 0;
    }

    public int getVariableCount() {
        return variableCount;
    }

    public void setVariablePostion(int plus) {
        variablePosition = variablePosition + plus;
    }

    public void resetVarPosition(int minus) {
        variablePosition = variablePosition - minus;
    }

    public int getVariablePosition() {
        return variablePosition;
    }

    public void posfixCreation(ArrayList<String> postfix, SemanticAnalyzer semantic) {
        String value;
        int positionVariable, positionFunction, i = 0;

        while (i < postfix.size()) {
            positionVariable = variablePosition - 1;
            positionFunction = 
                    
            if (semantic.varDeclSearch(postfix.get(i)) != -1) {
                CreateCode("", "LDV", positionVariable, "");

            } else if (postfix.get(i).equals("+")) {
                CreateCode("", "ADD", "", "");

            } else if (postfix.get(i).equals("*")) {
                CreateCode("", "MULT", "", "");

            } else if (postfix.get(i).equals("div")) {
                CreateCode("", "DIVI", "", "");

            } else if (postfix.get(i).equals("-")) {
                CreateCode("", "SUB", "", "");

            } else if (postfix.get(i).equals("!")) {
                CreateCode("", "INV", "", "");

            } else if (postfix.get(i).equals("e")) {
                CreateCode("", "AND", "", "");

            } else if (postfix.get(i).equals("ou")) {
                CreateCode("", "OR", "", "");

            } else if (postfix.get(i).equals("<")) {
                CreateCode("", "CME", "", "");

            } else if (postfix.get(i).equals(">")) {
                CreateCode("", "CMA", "", "");

            } else if (postfix.get(i).equals("=")) {
                CreateCode("", "CEQ", "", "");

            } else if (postfix.get(i).equals("!=")) {
                CreateCode("", "CDIF", "", "");

            } else if (postfix.get(i).equals("<=")) {
                CreateCode("", "CMEQ", "", "");

            } else if (postfix.get(i).equals(">=")) {
                CreateCode("", "CMAQ", "", "");

            } else if (postfix.get(i).equals("verdadeiro")) {
                CreateCode("", "LDC ", 0, "");

            } else if (postfix.get(i).equals("falso")) {
                CreateCode("", "LDC ", 1, "");

            } else if (positionFunction != -1) {
                CreateCode("", "CALL ", "L" + ((Function) semantic.symbolsTable.get(positionFunction)).rotulo, "");

            } else {
                CreateCode("", "LDC ", postfix.get(i), "");
            }

            i++;
        }
    }
}
