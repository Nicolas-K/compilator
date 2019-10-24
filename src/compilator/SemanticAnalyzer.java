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
    public boolean searchVariableDuplicate(String lexeme, String scope) {
        ArrayList<Symbol> symbols = table.requestSymbols();

        for (Symbol aux : symbols) {
            if (aux instanceof Variable) {
                if (aux.getLexemeName().equals(lexeme)) {
                    if (aux.getScope().equals(scope)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public boolean searchProcedureProgramDuplicate(String lexeme) {
        ArrayList<Symbol> symbols = table.requestSymbols();

        for (Symbol aux : symbols) {
            if (aux instanceof ProcedureProgram) {
                if (aux.getLexemeName().equals(lexeme)) {
                    return true;
                }
            }
        }
        
        return false;
    }

    public boolean searchFunctionDuplicate(String lexeme) {
        ArrayList<Symbol> symbols = table.requestSymbols();

        for (Symbol aux : symbols) {
            if (aux instanceof Function) {
                if (aux.getLexemeName().equals(lexeme)) {
                    return true;
                }
            }
        }
        
        return false;
    }
    
    /*
     *  Inserção de tipo nos simbolos
     */
    public void setTypeVariable(String type) {
        ArrayList<Symbol> symbols = table.requestSymbols();

        for (int i = symbols.size() - 1; i >= 0; i--) {
            if (symbols.get(i) instanceof Variable) {
                if (((Variable) symbols.get(i)).getType() == null) {
                    ((Variable) symbols.get(i)).setType(type);
                }
            }
        }

        table.updateSymbols(symbols);
    }

    public void setTypeFunction(String type) {
        ArrayList<Symbol> symbols = table.requestSymbols();

        for (int i = symbols.size() - 1; i >= 0; i--) {
            if (symbols.get(i) instanceof Function) {
                if (((Function) symbols.get(i)).getType() == null) {
                    ((Function) symbols.get(i)).setType(type);
                }
            }
        }

        table.updateSymbols(symbols);
    }
}
