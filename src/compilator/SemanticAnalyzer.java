package compilator;

import java.util.ArrayList;

public class SemanticAnalyzer {

    private static SemanticAnalyzer instance = null;
    private SymbolTable table;

    private ArrayList<String> postfix;
    private ArrayList<Operator> stack;

    public static SemanticAnalyzer getInstance() {
        if (instance == null) {
            instance = new SemanticAnalyzer();
        }
        return instance;
    }

    private SemanticAnalyzer() {
        table = SymbolTable.getInstance();
        postfix = new ArrayList<>();
        stack = new ArrayList<>();
    }

    /*
     *  Pesquisas na tabela de simbolo por duplicatas
     */
    public boolean searchVariableDuplicate(String lexeme, String scope) throws Exception {
        ArrayList<Symbol> symbols = table.requestSymbols();

        for (Symbol aux : symbols) {
            if (aux instanceof Variable) {
                if (aux.getLexemeName().equals(lexeme)) {
                    if (aux.getScope().equals(scope)) {
                        return true;
                    }
                }
            } else {
                if (aux instanceof ProcedureProgram || aux instanceof Function) {
                    if (aux.getLexemeName().equals(lexeme)) {
                        return true;
                    }
                }
            }
        }

        symbols = null;
        return false;
    }

    public boolean searchProcedureDuplicate(String lexeme) throws Exception {
        ArrayList<Symbol> symbols = table.requestSymbols();

        for (Symbol aux : symbols) {
            if (aux instanceof ProcedureProgram) {
                if (aux.getLexemeName().equals(lexeme)) {
                    return true;
                }
            }
        }

        symbols = null;
        return false;
    }

    public boolean searchFunctionDuplicate(String lexeme) throws Exception {
        ArrayList<Symbol> symbols = table.requestSymbols();

        for (Symbol aux : symbols) {
            if (aux instanceof Function) {
                if (aux.getLexemeName().equals(lexeme)) {
                    return true;
                }
            }
        }

        symbols = null;
        return false;
    }

    /*
     *  Inserção de tipo nos simbolos
     */
    public void setTypeVariable(String type) throws Exception {
        ArrayList<Symbol> symbols = table.requestSymbols();

        for (int i = symbols.size() - 1; i >= 0; i--) {
            if (symbols.get(i) instanceof Variable) {
                if (((Variable) symbols.get(i)).getType() == null) {
                    ((Variable) symbols.get(i)).setType(type);
                }
            }
        }

        table.updateSymbols(symbols);
        symbols = null;
    }

    public void setTypeFunction(String type) throws Exception {
        ArrayList<Symbol> symbols = table.requestSymbols();

        for (int i = symbols.size() - 1; i >= 0; i--) {
            if (symbols.get(i) instanceof Function) {
                if (((Function) symbols.get(i)).getType() == null) {
                    ((Function) symbols.get(i)).setType(type);
                }
            }
        }

        table.updateSymbols(symbols);
        symbols = null;
    }

    /*
     *  Desempilhar simbolos (voltar nivel)
     *  Validar | Modificar para remover apenas simbolos de determinado escopo
     */
    public void unstackSymbols(String scope) {
        ArrayList<Symbol> symbols = table.requestSymbols();

        for (int i = symbols.size() - 1; i >= 0; i--) {
            if (symbols.get(i).getScope().equals(scope)) {
                symbols.remove(i);
            } else {
                break;
            }
        }

        table.updateSymbols(symbols);
        symbols = null;
    }

    /*
     *  Utilização de identificadores e valores de expressões
     */
    public boolean identifierUsage(String identifier) {
        ArrayList<Symbol> symbols = table.requestSymbols();

        for (Symbol aux : symbols) {
            if (aux.getLexemeName().equals(identifier)) {
                return true;
            }
        }

        symbols = null;
        return false;
    }

    public int searchSymbolPos(String lexeme) {
        ArrayList<Symbol> symbols = table.requestSymbols();
        int i = 0;

        for (Symbol search : symbols) {
            if (!search.getLexemeName().equals(lexeme)) {
                i++;
            } else {
                return i;
            }
        }

        return -1;
    }

    public int countVariable(String lexeme) {
        int count = -1;
        ArrayList<Symbol> symbols = table.requestSymbols();

        for (int i = symbols.size() - 1; i >= 0; i--) {
            if (symbols.get(i).getLexemeName().equals(lexeme) && (symbols.get(i) instanceof Variable)) {
                count++;
                return count;
            } else {
                if (symbols.get(i) instanceof Variable) {
                    count++;
                }
            }
        }

        return -1;
    }

    /*
     *  Retornar instanceof do simbolo
     */
    public String instanceofSymbol(String identifier) {
        String instance;

        ArrayList<Symbol> symbols = table.requestSymbols();

        for (Symbol aux : symbols) {
            if (aux.getLexemeName().equals(identifier)) {
                if (aux instanceof Function) {
                    instance = "function";
                    return instance;
                } else if (aux instanceof Variable) {
                    instance = "variable";
                    return instance;
                }
            }
        }

        instance = "procedure";
        return instance;
    }

    /*
     *  Postfix 
     */
    public void addToPostfix(String postfixString) {
        postfix.add(postfixString);
    }

    public void addToStack(Operator operator) {
        stack.add(operator);
    }

    public void clearPostfix() {
        for (String posfixText : postfix) {
            postfix.remove(posfixText);
        }

        for (Operator operator : stack) {
            stack.remove(operator);
        }
    }

    public ArrayList getPostfix() {
        return postfix;
    }

    public void printPostfix() {
        for (String postFix : postfix) {
            System.out.print(postFix);
        }
        System.out.println();
    }

    /*
     *     Prioridades:
     *      + - not (unarios)   =   6
     *      * /                 =   5
     *      + -                 =   4
     *      > < >= <= = !=      =   3
     *      e                   =   2
     *      ou                  =   1
     *      (                   =   0
     *      )                   =   -1
     */
    public void postfixStackHandler(int priority) { // ==> Validar Depois <== \\
        int i, j;

        for (i = stack.size() - 1; i >= 0; i--) {
            if (priority == -1) {
                stack.remove(i);
                j = i - 1;

                while (stack.get(j).getPriority() != 0) {
                    addToPostfix(stack.get(j).getOperator().getLexeme());
                    stack.remove(j);
                    j--;
                }

                stack.remove(j);
                i = j;
                break;

            } else if (priority != 0) {
                if (priority <= stack.get(i).getPriority()) {
                    addToPostfix(stack.get(i).getOperator().getLexeme());
                    stack.remove(i);
                }
            } else {
                break;
            }
        }
    }

    public String postfixTypeHandler() {
        int symbolPosition = -1;

        if (postfix.get(postfix.size() - 1) == ">"
                || postfix.get(postfix.size() - 1) == ">="
                || postfix.get(postfix.size() - 1) == "<"
                || postfix.get(postfix.size() - 1) == "<="
                || postfix.get(postfix.size() - 1) == "="
                || postfix.get(postfix.size() - 1) == "!=") {
            return "booleano";

        } else if (postfix.get(postfix.size() - 1) == "+"
                || postfix.get(postfix.size() - 1) == "-"
                || postfix.get(postfix.size() - 1) == "*"
                || postfix.get(postfix.size() - 1) == "div") {
            return "inteiro";

        } else {
            symbolPosition = searchSymbolPos(postfix.get(postfix.size() - 1));

            if (symbolPosition != -1) {
                return table.getSymbolType(symbolPosition);
            } else {
                return "inteiro";
            }
        }
    }
}
