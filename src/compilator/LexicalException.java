package compilator;

public class LexicalException extends Exception{

    public void digitError(String line){
        System.out.printf("[DigitError] Linha: %s | Formato de digito não válido\n", line);
    }
    
    public void letterError(String line){
        System.out.printf("[LetterError] Linha: %s | Formato de palavra não válido\n", line);
    }
    
    public void attributionError(String line){
        System.out.printf("[AttributionError] Linha: %s | Formato de operação de atribuição não válido\n", line);
    }
    
    public void aritmeticError(String line){
        System.out.printf("[AritmeticError] Linha: %s | Formato de operação aritmetica não válido\n", line);
    }
    
    public void relationalError(String line){
        System.out.printf("[RelationalError] Linha: %s | Formato de operação relacional não válido\n", line);
    }
    
    public void punctuationError(String line){
        System.out.printf("[PunctuationError] Linha: %s | Formato de pontuação não válido\n", line);
    }
    
    public void characterInvalid(String line){
        System.out.printf("[CharacterError] Linha: %s | Caracter não válido\n", line);
    }
}
