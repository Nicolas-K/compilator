package compilator;

public class LexicalException {

    public void digitError(String line, char character){
        System.out.printf("[DigitError] ´Line: %s Character: %c  | Invalid Digit Format\n", line, character);
    }
    
    public void letterError(String line, char character){
        System.out.printf("[LetterError] Line: %s Character: %c | Invalid Word Format\n", line, character);
    }
    
    public void attributionError(String line, char character){
        System.out.printf("[AttributionError] Line: %s Character: %c | Invalid Attribution Format\n", line, character);
    }
    
    public void aritmeticError(String line, char character){
        System.out.printf("[AritmeticError] Line: %s Character: %c | Invalid Aritmetic Format\n", line, character);
    }
    
    public void relationalError(String line, char character){
        System.out.printf("[RelationalError] Line: %s Character: %c | Invalid Relational Format\n", line, character);
    }
    
    public void punctuationError(String line, char character){
        System.out.printf("[PunctuationError] Line: %s Character: %c | Invalid Punctuation Format\n", line, character);
    }
    
    public void characterInvalid(String line, char character){
        System.out.printf("[CharacterError] Line: %s Character: %c | Invalid Character\n", line, character);
    }
}
