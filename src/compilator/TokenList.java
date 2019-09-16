package compilator;
import java.util.ArrayList;

public class TokenList {
    private static TokenList instance;
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
    
    public void insertToken(Token newToken){
        this.tokens.add(newToken);
    }
    
    public Token requestToken(int index){
        return this.tokens.get(index);
    }
    
    public ArrayList requestList(){
        return this.tokens;
    }
}
