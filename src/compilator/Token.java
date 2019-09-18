package compilator;

public class Token {

    private String line;
    private String symbol;
    private String lexeme;

    public Token(){
        line = null;
        symbol = null;
        lexeme = null;
    }
    
    public void setLine(String line) {
        this.line = line;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public void setLexeme(String lexeme) {
        this.lexeme = lexeme;
    }

    public String getLine() {
        return this.line;
    }

    public String getSymbol() {
        return this.symbol;
    }

    public String getLexeme() {
        return this.lexeme;
    }
    
    public void print(){
        System.out.printf("[Token] | Linha: %s\n", getLine());
        System.out.printf("[Token] | Simbolo: %s\n", getSymbol());
        System.out.printf("[Token] | Lexema: %s\n", getLexeme());
    }
}
