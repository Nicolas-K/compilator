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
    
    public void insertNewToken(int tokenIndex, Token newToken){
        this.tokens.add(tokenIndex, newToken);
    }
    
    public ArrayList getTokensList(){
        return this.tokens;
    }
}
