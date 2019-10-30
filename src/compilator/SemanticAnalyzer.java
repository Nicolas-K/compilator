package compilator;

import java.util.ArrayList;

public class SemanticAnalyzer {

    private static SemanticAnalyzer instance = null;
    private SymbolTable table = SymbolTable.getInstance();

    public static SemanticAnalyzer getInstance() {
        if (instance == null) {
            instance = new SemanticAnalyzer();
        }
        return instance;
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

    public boolean searchProcedureProgramDuplicate(String lexeme) throws Exception {
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
    public void unstackSymbols(Symbol target) {
        ArrayList<Symbol> symbols = table.requestSymbols();

        for (int i = symbols.size() - 1; i >= 0; i--) {
            if (!symbols.get(i).equals(target)) {
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
            if(aux.getLexemeName().equals(identifier)){
                if (aux instanceof Function) {
                    instance = "function";
                    return instance;
                } 
            }
        }
        
        instance = "variable";
        return instance;
    }
}
