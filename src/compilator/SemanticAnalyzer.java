package compilator;

import java.util.ArrayList;

public class SemanticAnalyzer {

    private static SemanticAnalyzer instance = null;
    private SymbolTable table;

    private ArrayList<String> posfix;
    private ArrayList<Token> posfixStack;

    public static SemanticAnalyzer getInstance() {
        if (instance == null) {
            instance = new SemanticAnalyzer();
        }
        return instance;
    }

    public SemanticAnalyzer() {
        table = SymbolTable.getInstance();
        posfix = new ArrayList<>();
        posfixStack = new ArrayList<>();
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
    public void posfixTextAdd(String newPosfixText) {
        posfix.add(newPosfixText);
    }

    public void posfixTableAdd(Token operator) {
        posfixStack.add(operator);
    }

    public void clearPosfix() {
        for(String posfixText : posfix) {
            posfix.remove(posfixText);
        }
        
        for (Token operator : posfixStack) {
            posfixStack.remove(operator);
        }
    }

    public ArrayList getPosfix() {
        return posfix;
    }

    public void printPosfix() {
        for (String posFix : posfix) {
            System.out.print(posFix);
        }
        System.out.println();
    }

    /*public void posfixStackHandler(int postLevel) {
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

    public String posfixType() {
        int varPosition = -1, funPosition = -1;
        if (booleanString.contains(postfix.get(postfix.size() - 1))) {
            return "boolean";
        } else {
            if (intString.contains(postfix.get(postfix.size() - 1))) {
                return "integer";
            } else {
                varPosition = varDeclSearch(postfix.get(postfix.size() - 1));
                if (varPosition != -1) {
                    return ((Variable) symbolsTable.get(varPosition)).type;
                } else {
                    funPosition = funcDeclSearch(postfix.get(postfix.size() - 1));
                    if (funPosition != -1) {
                        return ((Function) symbolsTable.get(funPosition)).type;
                    } else {
                        return "integer";
                    }
                }
            }

        }
    }*/
}
