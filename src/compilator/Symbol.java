package compilator;

public class Symbol {

    private String lexemeName;
    private String scope;

    public Symbol() {
        lexemeName = null;
        scope = null;
    }

    public void setLexemeName(String lexemeName) {
        this.lexemeName = lexemeName;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getLexemeName() {
        return this.lexemeName;
    }

    public String getScope(){
        return this.scope;
    }
}
