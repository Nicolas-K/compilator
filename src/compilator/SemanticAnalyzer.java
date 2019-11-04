package compilator;

import java.util.ArrayList;

public class SemanticAnalyzer {

    private static SemanticAnalyzer instance = null;
    private SymbolTable table;

    private ArrayList<String> postfix;
    private ArrayList<Token> postfixStack;

    public static SemanticAnalyzer getInstance() {
        if (instance == null) {
            instance = new SemanticAnalyzer();
        }
        return instance;
    }

    public SemanticAnalyzer() {
        table = SymbolTable.getInstance();
        postfix = new ArrayList<>();
        postfixStack = new ArrayList<>();
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
                }
            }
        }

        instance = "variable";
        return instance;
    }

    /*
     *  PosFix 
     */
    public void postfixTextAdd(String newPostfixText) {
        postfix.add(newPostfixText);
    }

    public void postfixTableAdd(Token operator) {
        postfixStack.add(operator);
    }

    public void clearPostfix() {
        for (String posfixText : postfix) {
            postfix.remove(posfixText);
        }

        for (Token operator : postfixStack) {
            postfixStack.remove(operator);
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

    /*public void postfixStackHandler(int postLevel) {
     int i;

     for (i = posfixStack.size() - 1; i >= 0; i--) {
     if (postLevel == -2) {
     if (postfixTable.get(count).getSymbol() == -1) {
     postfixTable.remove(count);
     break;
     } else {
     posfixTextAdd(postfixTable.get(count).getLexeme());
     postfixTable.remove(count);
     }
     } else {
     if (postLevel == -3) {
     posfixTextAdd(postfixTable.get(count).getLexeme());
     postfixTable.remove(count);
     } else {
     if (postfixTable.get(count).getSymbol() >= postLevel) {
     posfixTextAdd(postfixTable.get(count).getLexeme());
     postfixTable.remove(count);
     } else {
     break;
     }
     }

     }
     }
     }
     */
    
    public String postfixTypeHandler() {
        int symbolPosition = -1;

        if (postfix.get(postfix.size() - 1) == ">"
                || postfix.get(postfix.size() - 1) == ">="
                || postfix.get(postfix.size() - 1) == "<"
                || postfix.get(postfix.size() - 1) == "<="
                || postfix.get(postfix.size() - 1) == "="
                || postfix.get(postfix.size() - 1) == "!=") {
            return "boolean";

        } else if (postfix.get(postfix.size() - 1) == "+"
                || postfix.get(postfix.size() - 1) == "-"
                || postfix.get(postfix.size() - 1) == "*"
                || postfix.get(postfix.size() - 1) == "div") {
            return "integer";

        } else {
            symbolPosition = searchSymbolPos(postfix.get(postfix.size() - 1));

            if (symbolPosition != -1) {
                return table.getSymbolType(symbolPosition);
            } else {
                return "integer";
            }
        }
    }
}
