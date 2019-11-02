package compilator;

public class ErrorMessages {

    private static ErrorMessages instance = null;
    private String messageError;

    public static ErrorMessages getInstance() {
        if (instance == null) {
            instance = new ErrorMessages();
        }
        return instance;
    }

    /*
     *  mensagem de erro associada ao analisador lexico
     */
    public String lexicalError(String method, String line, String character) {
        messageError = "[" + method + "] Line: " + line + " Input Received: " + character + " | Invalid Character or Format";
        return messageError;
    }

    /*
     *  mensagem de erro associada ao analisador sintatico
     */
    public String syntaticError(String method, Token wrongToken) {
        messageError = "[" + method + "] | Symbol not expected, symbol received: " + wrongToken.getSymbol()+ "\n| Line: " + wrongToken.getLine();
        return messageError;
    }

    
    /*
     *  mensagens de erro associadas ao analisador semantico
     */
    
    public String duplicateError(String method, String type, Token errorToken){
        messageError = "[" + method + "] | " + type + "Identifier " + errorToken.getLexeme() + " already declared\n| Line: " + errorToken.getLine();
        return messageError;
    }
    
    
    public String identifierUsageError(String method, Token identifier) {
        messageError = "[" + method + "] | Identifier " + identifier.getLexeme() + " not declared before\n| Line: " + identifier.getLine();
        return messageError;
    }
}
