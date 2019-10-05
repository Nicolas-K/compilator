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

    public synchronized void setTypeSymbols(String type) {
        for (int i = symbols.size(); i > 0; i++) {
            if (this.symbols.get(i) instanceof Variable) {
                if (((Variable) this.symbols.get(i)).getType() == null) {
                    ((Variable) this.symbols.get(i)).setType(type);
                }
            } else if (this.symbols.get(i) instanceof Function) {
                if (((Function) this.symbols.get(i)).getType() == null) {
                    ((Function) this.symbols.get(i)).setType(type);
                }
            }
        }
    }

    public synchronized ArrayList<Symbol> requestSymbols() {
        return this.symbols;
    }
}
