package compilator;
import java.util.ArrayList;

public class TokenList {
    private static TokenList instance = null;
    private ArrayList<Token> tokens;
    
    public static TokenList getInstance() {
        if (instance == null) {
            instance = new TokenList();
        }
        return instance;
    }
    
    public TokenList(){
        tokens = new ArrayList<>();
    }
    
    public synchronized void insertToken(Token newToken){
        this.tokens.add(newToken);
    }

    public synchronized ArrayList<Token> requestList(){
        return this.tokens;
    }

    public synchronized void printAllTokens() {
        for(Token aux : this.tokens){
            aux.print();
        }
    }
}
