package compilator;

public class SyntacticException {

    public void programError(Token wrongToken) {
        System.out.println("[programError] | Expected token symbol sprogram");
        System.out.printf("[programError] | Token Symbol Received: %s\n", wrongToken.getSymbol());
        System.out.printf("[programError] | Line of File of Token: %s\n", wrongToken.getLine());
    }

    public void identifierError(Token wrongToken) {
        System.out.println("[identifierError] | Expected token symbol sidentificador");
        System.out.printf("[identifierError] | Token Symbol Received: %s\n", wrongToken.getSymbol());
        System.out.printf("[identifierError] | Line of File of Token: %s\n", wrongToken.getLine());
    }

    public void semicolonError(Token wrongToken) {
        System.out.println("[semicolonError] | Expected token symbol sponto_virgula");
        System.out.printf("[semicolonError] | Token Symbol Received: %s\n", wrongToken.getSymbol());
        System.out.printf("[semicolonError] | Line of File of Token: %s\n", wrongToken.getLine());
    }

    public void dotError(Token wrongToken) {
        System.out.println("[dotError] | Expected token symbol sponto");
        System.out.printf("[dotError] | Token Symbol Received: %s\n", wrongToken.getSymbol());
        System.out.printf("[dotError] | Line of File of Token: %s\n", wrongToken.getLine());
    }

    public void colonError(Token wrongToken) {
        System.out.println("[colonError] | Expected token symbol sdoispontos");
        System.out.printf("[colonError] | Token Symbol Received: %s\n", wrongToken.getSymbol());
        System.out.printf("[colonError] | Line of File of Token: %s\n", wrongToken.getLine());
    }

    public void typeError(Token wrongToken) {
        System.out.println("[typeError] | Expected token symbol sinteiro or sbooleano");
        System.out.printf("[typeError] | Token Symbol Received: %s\n", wrongToken.getSymbol());
        System.out.printf("[typeError] | Line of File of Token: %s\n", wrongToken.getLine());
    }

    public void beginError(Token wrongToken) {
        System.out.println("[beginError] | Expected token symbol sprogram");
        System.out.printf("[beginError] | Token Symbol Received: %s\n", wrongToken.getSymbol());
        System.out.printf("[beginError] | Line of File of Token: %s\n", wrongToken.getLine());
    }

    public void booleanError(Token wrongToken) {
        System.out.println("[booleanError] | Expected token symbol sverdadeiro or sfalso");
        System.out.printf("[booleanError] | Token Symbol Received: %s\n", wrongToken.getSymbol());
        System.out.printf("[booleanError] | Line of File of Token: %s\n", wrongToken.getLine());
    }

    public void openparenthesesError(Token wrongToken) {
        System.out.println("[openparenthesesError] | Expected token symbol sabre_parenteses");
        System.out.printf("[openparenthesesError] | Token Symbol Received: %s", wrongToken.getSymbol());
        System.out.printf("[openparenthesesError] | Line of File of Token: %s\n", wrongToken.getLine());
    }

    public void closeparenthesesError(Token wrongToken) {
        System.out.println("[closeparenthesesError] | Expected token symbol sfecha_parenteses");
        System.out.printf("[closeparenthesesError] | Token Symbol Received: %s", wrongToken.getSymbol());
        System.out.printf("[closeparenthesesError] | Line of File of Token: %s\n", wrongToken.getLine());
    }

    public void doError(Token wrongToken) {
        System.out.println("[doError] | Expected token symbol sfaca");
        System.out.printf("[doError] | Token Symbol Received: %s", wrongToken.getSymbol());
        System.out.printf("[doError] | Line of File of Token: %s\n", wrongToken.getLine());
    }

    public void thenError(Token wrongToken) {
        System.out.println("[thenError] | Expected token symbol sentao");
        System.out.printf("[thenError] | Token Symbol Received: %s", wrongToken.getSymbol());
        System.out.printf("[thenError] | Line of File of Token: %s\n", wrongToken.getLine());
    }

    public void endoffileError(Token wrongToken) {
        System.out.println("[endoffileError] | Expected end of file");
        System.out.printf("[endoffileError] | Token Symbol Received: %s", wrongToken.getSymbol());
        System.out.printf("[endoffileError] | Line of File of Token: %s\n", wrongToken.getLine());
    }
}
