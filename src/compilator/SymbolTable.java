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

    public synchronized Symbol getSymbolData(String lexeme) {
        for (Symbol aux : this.symbols) {
            if (aux.getLexemeName().equals(lexeme)) {
                return aux;
            }
        }
        
        return null;
    }
    
    public synchronized void setTypeSymbols(String type){
        
    }

    public synchronized ArrayList<Symbol> requestSymbols() {
        return this.symbols;
    }
}
