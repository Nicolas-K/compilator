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
    public String lexicalError(String line, String character) {
        messageError = "[ Lexical Error ] | Line: " + line + " Input Received: " + character + " | Invalid Character or Format";
        return messageError;
    }

    /*
     *  mensagem de erro associada ao analisador sintatico
     */
    public String syntaticError(Token wrongToken) {
        messageError = "[ Syntatic Error ] | Symbol not expected, symbol received: " + wrongToken.getSymbol() + "\n| Line: " + wrongToken.getLine();
        return messageError;
    }

    /*
     *  mensagens de erro associadas ao analisador semantico
     */
    public String duplicateError(String type, Token errorToken) {
        messageError = "[ Semantic Error ] | " + type + "Identifier " + errorToken.getLexeme() + " already declared\n| Line: " + errorToken.getLine();
        return messageError;
    }

    public String identifierUsageError(Token identifier) {
        messageError = "[ Semantic Error ] | Identifier " + identifier.getLexeme() + " not declared before\n| Line: " + identifier.getLine();
        return messageError;
    }

    public String wrongUsageSymbol(Token wrong) {
        messageError = "[ Semantic Error ] | Wrong use of the Symbol " + wrong.getLexeme() + "\n| Line " + wrong.getLine();
        return messageError;
    }

    public String typeExpressionError(String typeExpected) {
        messageError = "[ Semantic Error ] | Expression has different type than expected\n| Type Expected: " + typeExpected;
        return messageError;
    }

    public String noReturnfError(String scopeFunction) {
        messageError = "[ SemanticError ] | Function " + scopeFunction + " has no return";
        return messageError;
    }
}
