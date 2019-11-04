package compilator;

import java.util.ArrayList;

public class SymbolTable {

    private static SymbolTable instance = null;
    private ArrayList<Symbol> symbols;

    public static SymbolTable getInstance() {
        if (instance == null) {
            instance = new SymbolTable();
        }
        return instance;
    }

    public SymbolTable() {
        symbols = new ArrayList<>();
    }

    public synchronized void insertSymbol(Symbol newSymbol) {
        symbols.add(newSymbol);
    }

    public synchronized void updateSymbols(ArrayList<Symbol> symbols) {
        this.symbols = symbols;
    }

    public synchronized ArrayList<Symbol> requestSymbols() {
        return this.symbols;
    }
    
    public synchronized Symbol getSymbol(int index) {
        return this.symbols.get(index);
    }
    
    public synchronized String getSymbolType(int index) {
        Symbol aux;
        
        aux = getSymbol(index);
        
        if (aux instanceof Variable) {
            return ((Variable) aux).getType();
        } else {
            return ((Function) aux).getType();
        }
    }

    public synchronized void printTable() {
        for (Symbol aux : this.symbols) {
            if (aux instanceof Variable) {
                ((Variable) aux).printVariable();
            } else if (aux instanceof ProcedureProgram) {
                ((ProcedureProgram) aux).printProcedureProgram();
            } else if (aux instanceof Function) {
                ((Function) aux).printFunction();
            }
        }
    }
}
