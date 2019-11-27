package compilator;

import java.util.ArrayList;

public class CodeGenerator {

    private static CodeGenerator instance = null;
    private String codeText;
    private int variableCount;
    private int variablePosition;

    private SymbolTable table;

    public static CodeGenerator getInstance() {
        if (instance == null) {
            instance = new CodeGenerator();
        }
        return instance;
    }

    private CodeGenerator() {
        this.codeText = new String();
        this.variableCount = 0;
        this.variablePosition = 0;
        this.table = SymbolTable.getInstance();
    }

    private void createCode(Object label, String command, Object target1, Object target2) {
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
            positionVariable = getVariablePosition() - semantic.countVariable(postfix.get(i)) - 1;

            if (semantic.searchSymbolPos(postfix.get(i)) != -1
                    && semantic.instanceofSymbol(postfix.get(i)).equals("variable")) {
                createLDV(positionVariable);

            } else if (postfix.get(i).equals("+")) {
                createADD();

            } else if (postfix.get(i).equals("*")) {
                createMULT();

            } else if (postfix.get(i).equals("div")) {
                createDIVI();

            } else if (postfix.get(i).equals("-")) {
                createSUB();

            } else if (postfix.get(i).equals("nao")) {
                createNEG();

            } else if (postfix.get(i).equals("e")) {
                createAND();

            } else if (postfix.get(i).equals("ou")) {
                createOR();

            } else if (postfix.get(i).equals("<")) {
                createCME();

            } else if (postfix.get(i).equals(">")) {
                createCMA();

            } else if (postfix.get(i).equals("=")) {
                createCEQ();

            } else if (postfix.get(i).equals("!=")) {
                createCDIF();

            } else if (postfix.get(i).equals("<=")) {
                createCMEQ();

            } else if (postfix.get(i).equals(">=")) {
                createCMAQ();

            } else if (postfix.get(i).equals("verdadeiro")) {
                createLDC(1);

            } else if (postfix.get(i).equals("falso")) {
                createLDC(0);

            } else if (semantic.searchSymbolPos(postfix.get(i)) != -1) {
                positionFunction = semantic.searchSymbolPos(postfix.get(i));
                createCALL("L" + ((Function) table.getSymbol(positionFunction)).getLabel());

            } else {
                createLDC(postfix.get(i));
            }

            i++;
        }
    }

    /*
     *  Instrução de Memoria
     */
    public void createStart() {
        createCode("", "START", "", "");
    }

    public void createHLT() {
        createCode("", "HLT", "", "");
    }

    public void createALLOC(Object m, Object n) {
        createCode("", "ALLOC ", m, n);
    }

    public void createDALLOC(Object m, Object n) {
        createCode("", "DALLOC ", m, n);
    }

    public void createLDC(Object k) {
        createCode("", "LDC ", k, "");
    }

    public void createLDV(Object v) {
        createCode("", "LDV ", v, "");
    }

    public void createSTR(Object position) {
        createCode("", "STR ", position, "");
    }

    /*
     *  Instruções de Salto
     */
    public void createJMP(Object label) {
        createCode("", "JMP ", label, "");
    }

    public void createJMPF(Object label) {
        createCode("", "JMPF ", label, "");
    }

    public void createNULL(Object label) {
        createCode(label, "NULL", "", "");
    }

    public void createCALL(Object label) {
        createCode("", "CALL ", label, "");
    }

    public void createRETURN() {
        createCode("", "RETURN", "", "");
    }

    public void createRETURNF() {
        createCode("", "RETURNF", "", "");
    }

    public void createRETURNF_DALLOC(Object m, Object n) {
        createCode("", "RETURNF ", m, n);
    }

    /*
     *  Instrução de Comparação
     */
    public void createCMEQ() {
        createCode("", "CMEQ", "", "");
    }

    public void createCMAQ() {
        createCode("", "CMAQ", "", "");
    }

    public void createCMA() {
        createCode("", "CMA", "", "");
    }

    public void createCME() {
        createCode("", "CME", "", "");
    }

    public void createCEQ() {
        createCode("", "CEQ", "", "");
    }

    public void createCDIF() {
        createCode("", "CDIF", "", "");
    }

    /*
     *  Instruções Aritmeticas 
     */
    public void createADD() {
        createCode("", "ADD", "", "");
    }

    public void createSUB() {
        createCode("", "SUB", "", "");
    }

    public void createMULT() {
        createCode("", "MULT", "", "");
    }

    public void createDIVI() {
        createCode("", "DIVI", "", "");
    }

    public void createINV() {
        createCode("", "INV", "", "");
    }

    /*
     *  Instruções Logicas
     */
    public void createAND() {
        createCode("", "AND", "", "");
    }

    public void createOR() {
        createCode("", "OR", "", "");
    }

    public void createNEG() {
        createCode("", "NEG", "", "");
    }

    /*
     *  Instruções de Leitura e Escrita   
     */
    public void createPRN() {
        createCode("", "PRN", "", "");
    }

    public void createRD() {
        createCode("", "RD", "", "");
    }
}
