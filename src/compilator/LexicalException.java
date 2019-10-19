package compilator;

public class LexicalException {
    private String messageError;
    
    public String digitError(String line, char character){
        messageError = "[DigitError] Line: " + line + " Character: " + character + " | Invalid Digit Format\n";
        return messageError;
    }
    
    public String letterError(String line, char character){
        messageError = "[LetterError] Line: " + line + " Character: " + character + " | Invalid Word Format\n";
        return messageError;
    }
    
    public String attributionError(String line, char character){
        messageError = "[AttributionError] Line: " + line + " Character: " + character + " | Invalid Attribution Format\n";
        return messageError;
    }
    
    public String aritmeticError(String line, char character){
        messageError = "[AritmeticError] Line: " + line + " Character: " + character + " | Invalid Aritmetic Format\n";
        return messageError;
    }
    
    public String relationalError(String line, String character){
        messageError = "[RelationalError] Line: " + line + " Character: " + character + " | Invalid Relational Format\n";
        return messageError;
    }
    
    public String punctuationError(String line, char character){
        messageError = "[PunctuationError] Line: " + line + " Character: " + character + " | Invalid Punctuation Format\n";
        return messageError;
    }
    
    public String characterInvalid(String line, char character){
        messageError = "[CharacterError] Line: " + line + " Character: " + character + " | Invalid Character\n";
        return messageError;
    }
}
