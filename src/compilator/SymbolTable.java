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

    public synchronized Symbol getLast() {
        return this.symbols.get(symbols.size() - 1);
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
