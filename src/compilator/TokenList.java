package compilator;
import java.util.ArrayList;

public class TokenList {
    private static TokenList instance;
    private ArrayList<Token> tokens = new ArrayList<>();
    
    public static TokenList getInstance() {
        if (instance == null) {
            instance = new TokenList();
        }
        return instance;
    }
    
    public void insertNewToken(int tokenIndex, Token newToken){
        this.tokens.add(tokenIndex, newToken);
    }
    
    public void deleteToken(Token removeToken) {
        this.tokens.remove(removeToken);
    }
    
    public ArrayList getTokensList(){
        return this.tokens;
    }
}
