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
     *  mensagens de erro associadas ao analisador lexico
     */
    public String digitError(String line, char character) {
        messageError = "[DigitError] Line: " + line + " Character: " + character + " | Invalid Digit Format";
        return messageError;
    }

    public String letterError(String line, char character) {
        messageError = "[LetterError] Line: " + line + " Character: " + character + " | Invalid Word Format";
        return messageError;
    }

    public String attributionError(String line, char character) {
        messageError = "[AttributionError] Line: " + line + " Character: " + character + " | Invalid Attribution Format";
        return messageError;
    }

    public String aritmeticError(String line, char character) {
        messageError = "[AritmeticError] Line: " + line + " Character: " + character + " | Invalid Aritmetic Format";
        return messageError;
    }

    public String relationalError(String line, String character) {
        messageError = "[RelationalError] Line: " + line + " Character: " + character + " | Invalid Relational Format";
        return messageError;
    }

    public String punctuationError(String line, char character) {
        messageError = "[PunctuationError] Line: " + line + " Character: " + character + " | Invalid Punctuation Format";
        return messageError;
    }

    public String characterInvalid(String line, char character) {
        messageError = "[CharacterError] Line: " + line + " Character: " + character + " | Invalid Character";
        return messageError;
    }

    /*
     *  Mensagens de erro associadas ao analisador sintatico
     */
    public String programError(String method, Token wrongToken) {
        messageError = "[" + method + "] | Expression not, symbol received: " + wrongToken.getSymbol()+ "\n| Line: " + wrongToken.getLine();
        return messageError;
    }

    public String identifierError(String method, Token wrongToken) {
        messageError = "[" + method + "] | Symbol sidentificador expected, symbol received: " + wrongToken.getSymbol()+ "\n| Line: " + wrongToken.getLine();
        return messageError;
    }

    public String semicolonError(String method, Token wrongToken) {
        messageError = "[" + method + "] | Symbol sponto_virgula expected, symbol received: " + wrongToken.getSymbol()+ "\n| Line: " + wrongToken.getLine();
        return messageError;
    }

    public String dotError(String method, Token wrongToken) {
        messageError = "[" + method + "] | Symbol sponto expected, symbol received: " + wrongToken.getSymbol()+ "\n| Line: " + wrongToken.getLine();
        return messageError;
    }

    public String colonError(String method, Token wrongToken) {
        messageError = "[" + method + "] | Symbol received not expected, symbol received: " + wrongToken.getSymbol()+ "\n| Line: " + wrongToken.getLine();
        return messageError;
    }

    public String typeError(String method, Token wrongToken) {
        messageError = "[" + method + "] | Symbol sinteiro or sbooleano expected, symbol received: " + wrongToken.getSymbol()+ "\n| Line: " + wrongToken.getLine();
        return messageError;
    }

    public String beginError(String method, Token wrongToken) {
        messageError = "[" + method + "] | symbol sinicio expected, symbol received: " + wrongToken.getSymbol()+ "\n| Line: " + wrongToken.getLine();
        return messageError;
    }

    public String booleanError(String method, Token wrongToken) {
        messageError = "[" + method + "] | Symbol sverdadeiro or sfalso expected, symbol received: " + wrongToken.getSymbol()+ "\n| Line: " + wrongToken.getLine();
        return messageError;
    }

    public String openparenthesesError(String method, Token wrongToken) {
        messageError = "[" + method + "] | Symbol sabre_parenteses expected, symbol received: " + wrongToken.getSymbol()+ "\n| Line: " + wrongToken.getLine();
        return messageError;
    }

    public String closeparenthesesError(String method, Token wrongToken) {
        messageError = "[" + method + "] | Symbol sfecha_parenteses expected, symbol received: " + wrongToken.getSymbol()+ "\n| Line: " + wrongToken.getLine();
        return messageError;
    }

    public String doError(String method, Token wrongToken) {
        messageError = "[" + method + "] | Symbol sfaca expected, symbol received: " + wrongToken.getSymbol()+ "\n| Line: " + wrongToken.getLine();
        return messageError;
    }

    public String thenError(String method, Token wrongToken) {
        messageError = "[" + method + "] | Symbol sentao expected, symbol received: " + wrongToken.getSymbol()+ "\n| Line: " + wrongToken.getLine();
        return messageError;
    }

    public String endoffileError(String method, Token wrongToken) {
        messageError = "[" + method + "] | End of file expected, symbol received: " + wrongToken.getSymbol()+ "\n| Line: " + wrongToken.getLine();
        return messageError;
    }
    
    /*
     *  mensagens de erro associadas ao analisador semantico
     */
    
    public String duplicateVariableError(String method, String variable){
        messageError = "[" + method + "] | Variable" + variable + " already declared in this scope";
        return messageError;
    }
    
    public String duplicateFunctionError(String method, String function) {
        messageError = "[" + method + "] | Function" + function + " already declared";
        return messageError;
    }
    
    public String duplicateProcedureProgramError(String method, String function) {
        messageError = "[" + method + "] | Function" + function + " already declared";
        return messageError;
    }
}
