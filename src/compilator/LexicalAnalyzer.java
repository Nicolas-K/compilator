package compilator;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class LexicalAnalyzer {

    private static LexicalAnalyzer instance;
    TokenList tokensList = TokenList.getInstance();

    public static LexicalAnalyzer getInstance() {
        if (instance == null) {
            instance = new LexicalAnalyzer();
        }
        return instance;
    }
    
    public LexicalAnalyzer(){
        // CONSTRUCTOR  
    }

    public void analyzeFile(String codePath) {
        int fileLine = 0;
        String nextLine;

        BufferedReader reader = null;
        Token newToken = null;

        try {
            reader = new BufferedReader(new FileReader(codePath));

            while ((nextLine = reader.readLine()) != null) {
                /*  if(caracter == digito) {
                 newToken = isDigit(fileLine);
                 } else if (caracter == letra) {
                 newToken = isLetter(fileLine);
                 } else if (caracter == ":") {
                 newToken = isAttribution(fileLine);
                 } else if (caracter == "+" || caracter == "-" || caracter == "*") {
                 newToken = isAritmetic(fileLine);
                 } else if (caracter == ">" || caracter == "<" || caracter == "=" || caracter == "!") {
                 newToken = isRelational(fileLine);
                 } else if (caracter == ";" || caracter == "," || caracter == "(" || caracter == ")" || caracter == ".") {
                 newToken = isPunctuation(fileLine);
                 } else ERROR
                 
                 */
                tokensList.insertNewToken(fileLine, newToken);

                fileLine++;
            }
        } catch (FileNotFoundException fileException) {
            System.out.println("Error! Arquivo não encontrado\n");
        } catch (IOException ioException) {
            System.out.println("Error! Leitura/Escrita do arquivo\n");
        }

        try {
            if (reader != null) {
                reader.close();
            }
        } catch (IOException ioException) {
            System.out.println("Error! Não foi possível fechar o arquivo\n");
        }
    }

    public Token isDigit(String fileLine) {
        Token digit = new Token();
        String number = null;

        // TODO logica do isDigit
        
        digit.setLine(fileLine);
        digit.setSymbol("snumero");
        digit.setLexeme(number);
        return digit;
    }

    public Token isLetter(String fileLine) {
        Token letter = new Token();
        String word = null;

        // TODO logica do isLetter
       
        letter.setLine(fileLine);

        switch (word) {
            case "programa":
                letter.setSymbol("sprograma");
                break;
            case "se":
                letter.setSymbol("sse");
                break;
            case "entao":
                letter.setSymbol("sentao");
                break;
            case "senao":
                letter.setSymbol("ssenao");
                break;
            case "enquanto":
                letter.setSymbol("senquanto");
                break;
            case "faca":
                letter.setSymbol("sfaca");
            case "inicio":
                letter.setSymbol("sinício");
                break;
            case "fim":
                letter.setSymbol("sfim");
                break;
            case "escreva":
                letter.setSymbol("sescreva");
                break;
            case "leia":
                letter.setSymbol("sleia");
                break;
            case "var":
                letter.setSymbol("svar");
                break;
            case "inteiro":
                letter.setSymbol("sinteiro");
                break;
            case "booleano":
                letter.setSymbol("sbooleano");
                break;
            case "verdadeiro":
                letter.setSymbol("sverdadeiro");
                break;
            case "falso":
                letter.setSymbol("sfalso");
                break;
            case "procedimento":
                letter.setSymbol("sprocedimento");
                break;
            case "funcao":
                letter.setSymbol("sfuncao");
                break;
            case "div":
                letter.setSymbol("sdiv");
                break;
            case "e":
                letter.setSymbol("se");
                break;
            case "ou":
                letter.setSymbol("sou");
                break;
            case "nao":
                letter.setSymbol("snao");
                break;
            default:
                letter.setSymbol("sidentificador");
                break;
        }

        letter.setLexeme(word);
        return letter;
    }

    public Token isAttribution(String fileLine) {
        Token attribution = new Token();

        attribution.setLine(fileLine);

        return attribution;
    }

    public Token isAritmetic(String fileLine, char operation) {
        Token aritmetic = new Token();

        aritmetic.setLine(fileLine);

        if (operation == '+') {
            aritmetic.setSymbol("smais");
        } else if (operation == '-') {
            aritmetic.setSymbol("smenos");
        } else if (operation == '*') {
            aritmetic.setSymbol("smult");
        }

        aritmetic.setLexeme(Character.toString(operation));
        return aritmetic;
    }

    public Token isRelational(String fileLine) {
        Token relational = new Token();
        
        relational.setLine(fileLine);
        
        // TODO logica do Relacional
            
        return relational;
    }

    public Token isPunctuation(String fileLine, char punctuationChar) {
        Token punctuation = new Token();

        punctuation.setLine(fileLine);

        if (punctuationChar == ';') {
            punctuation.setSymbol("sponto_vírgula");
        } else if (punctuationChar == ',') {
            punctuation.setSymbol("svirgula");
        } else if (punctuationChar == '(') {
            punctuation.setSymbol("sabre_parênteses");
        } else if (punctuationChar == ')') {
            punctuation.setSymbol("sfecha_parênteses");
        } else if (punctuationChar == '.') {
            punctuation.setSymbol("sponto");
        }

        punctuation.setLexeme(Character.toString(punctuationChar));
        return punctuation;
    }
}
