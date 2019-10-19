package compilator;

public class SyntacticException {
    private String messageError;
    
    public String programError(Token wrongToken) {
        messageError = "[programError] | Symbol sprograma expected, symbol received: " + wrongToken.getSymbol();
        return messageError;
    }

    public String identifierError(Token wrongToken) {
        messageError = "[identifierError] | Symbol sidentificador expected, symbol received: " + wrongToken.getSymbol();
        return messageError;
    }

    public String semicolonError(Token wrongToken) {
        messageError = "[semicolonError] | Symbol sponto_virgula expected, symbol received: " + wrongToken.getSymbol();
        return messageError;
    }

    public String dotError(Token wrongToken) {
        messageError = "[dotError] | Symbol sponto expected, symbol received: " + wrongToken.getSymbol();
        return messageError;
    }

    public String colonError(Token wrongToken) {
        messageError = "[colonError] | Symbol sdoispontos expected, symbol received: " + wrongToken.getSymbol();
        return messageError;
    }

    public String typeError(Token wrongToken) {
        messageError = "[typeError] | Symbol sinteiro or sbooleano expected, symbol received: " + wrongToken.getSymbol();
        return messageError;
    }

    public String beginError(Token wrongToken) {
        messageError = "[beginError] | symbol sinicio expected, symbol received: " + wrongToken.getSymbol();
        return messageError;
    }

    public String booleanError(Token wrongToken) {
        messageError = "[booleanError] | Symbol sverdadeiro or sfalso expected, symbol received: " + wrongToken.getSymbol();
        return messageError;
    }

    public String openparenthesesError(Token wrongToken) {
        messageError = "[openparenthesesError] | Symbol sabre_parenteses expected, symbol received: " + wrongToken.getSymbol();
        return messageError;
    }

    public String closeparenthesesError(Token wrongToken) {
        messageError = "[closeparenthesesError] | Symbol sfecha_parenteses expected, symbol received: " + wrongToken.getSymbol();
        return messageError;
    }

    public String doError(Token wrongToken) {
        messageError = "[doError] | Symbol sfaca expected, symbol received: " + wrongToken.getSymbol();
        return messageError;
    }

    public String thenError(Token wrongToken) {
        messageError = "[thenError] | Symbol sentao expected, symbol received: " + wrongToken.getSymbol();
        return messageError;
    }

    public String endoffileError(Token wrongToken) {
        messageError = "[endoffileError] | End of file expected, symbol received: " + wrongToken.getSymbol();
        return messageError;
    }
}
