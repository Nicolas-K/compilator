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

    public void createCode(Object label, String command, Object target1, Object target2) {
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

    public void resetVariablePosition(int minus) {
        variablePosition = variablePosition - minus;
    }

    public int getVariablePosition() {
        return variablePosition;
    }

    public void postfixCreation(ArrayList<String> postfix, SemanticAnalyzer semantic) {
        String value;
        int positionVariable, positionFunction, i = 0;

        while (i < postfix.size()) {
            positionVariable = variablePosition - 1;
            //positionFunction = 
                    
            if (semantic.searchSymbolPos(postfix.get(i)) != -1) {
                createCode("", "LDV ", positionVariable, "");

            } else if (postfix.get(i).equals("+")) {
                createCode("", "ADD", "", "");

            } else if (postfix.get(i).equals("*")) {
                createCode("", "MULT", "", "");

            } else if (postfix.get(i).equals("div")) {
                createCode("", "DIVI", "", "");

            } else if (postfix.get(i).equals("-")) {
                createCode("", "SUB", "", "");

            } else if (postfix.get(i).equals("!")) {
                createCode("", "INV", "", "");

            } else if (postfix.get(i).equals("e")) {
                createCode("", "AND", "", "");

            } else if (postfix.get(i).equals("ou")) {
                createCode("", "OR", "", "");

            } else if (postfix.get(i).equals("<")) {
                createCode("", "CME", "", "");

            } else if (postfix.get(i).equals(">")) {
                createCode("", "CMA", "", "");

            } else if (postfix.get(i).equals("=")) {
                createCode("", "CEQ", "", "");

            } else if (postfix.get(i).equals("!=")) {
                createCode("", "CDIF", "", "");

            } else if (postfix.get(i).equals("<=")) {
                createCode("", "CMEQ", "", "");

            } else if (postfix.get(i).equals(">=")) {
                createCode("", "CMAQ", "", "");

            } else if (postfix.get(i).equals("verdadeiro")) {
                createCode("", "LDC ", 1, "");

            } else if (postfix.get(i).equals("falso")) {
                createCode("", "LDC ", 0, "");

            } /*else if (positionFunction != -1) {
                // CreateCode("", "CALL ", "L" + ((Function) semantic.symbolsTable.get(positionFunction)).rotulo, "");

            }*/ else {
                createCode("", "LDC ", postfix.get(i), "");
            }

            i++;
        }
    }
}
