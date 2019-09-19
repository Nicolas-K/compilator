package compilator;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.io.IOException;

public class LexicalAnalyzer {

    private static LexicalAnalyzer instance;
    TokenList list = TokenList.getInstance();
    
    private final ArrayList<Character> listToIgnore = new ArrayList<>();
    private final ArrayList<Character> listAritmetics = new ArrayList<>();
    private final ArrayList<Character> listRelationals = new ArrayList<>();
    private final ArrayList<Character> listPunctuations = new ArrayList<>();
    
    private BufferedReader reader;
    private char currentChar;
       
    public static LexicalAnalyzer getInstance() {
        if (instance == null) {
            instance = new LexicalAnalyzer();
        }
        return instance;
    }
    
    public LexicalAnalyzer(){
        listToIgnore.add(' ');
        listToIgnore.add('{');
        listToIgnore.add('\n');
        listToIgnore.add('\r');
        listToIgnore.add('\t');
        
        listAritmetics.add('+');
        listAritmetics.add('-');
        listAritmetics.add('*');
        
        listRelationals.add('>');
        listRelationals.add('<');
        listRelationals.add('=');
        listRelationals.add('!');
                
        listPunctuations.add(';');
        listPunctuations.add(',');
        listPunctuations.add('(');
        listPunctuations.add(')');
        listPunctuations.add('.');
    }

    public void openFile(String codePath) {
        System.out.println("[OpenFile] | Init\n");
        
        try {
            reader = new BufferedReader(new FileReader(codePath));
            System.out.println("[OpenFile] | File opened\n");
            
        } catch (FileNotFoundException exception) {
            System.out.println("[OpenFile] | Error, file not found\n");
            exception.getMessage();
        }
    }

    public void closeFile() {
        System.out.println("[CloseFile] | Init\n");
        
        try {
            if (reader != null) {
                reader.close();
                System.out.println("[CloseFile] | File closed\n");
            }
        } catch (IOException exception) {
            System.out.println("[CloseFile] | Error, file not closed\n");
            exception.getMessage();
        }
    }
    
    public void debug(String path){
        System.out.println("[LexicalAnalyzer] | Init\n");
        openFile(path);
        analyzeFile();
        closeFile();
    }

    public void analyzeFile() {
        int indexFile;
        Token newToken;
       
        try {
            indexFile = 1;

            currentChar = (char) reader.read();
            while (currentChar != -1) {

                while ((listToIgnore.contains(currentChar)) && currentChar != -1) {
                    if (currentChar == '{') {
                        while (currentChar != '}' && currentChar != -1) {
                            currentChar = (char) reader.read();
                        }
                        
                        currentChar = (char) reader.read();
                        
                    } else if (currentChar == '\n') {
                        indexFile++;
                        currentChar = (char) reader.read();
                        
                    } else {
                        currentChar = (char) reader.read();

                        while (currentChar == ' ' && currentChar != -1) {
                            currentChar = (char) reader.read();
                        }
                    }
                }

                if (currentChar != -1) {
                    System.out.printf("[analyzeFile] | Line: %d\n", indexFile);
                    System.out.printf("[analyzeFile] | Character: %c\n", currentChar);
                    
                    newToken = getToken(indexFile);

                    if (newToken != null) {
                        list.insertToken(newToken);
                    } else {
                        System.out.println("\n[analyzeFile] | All Tokens: ");
                        list.printAllTokens();
                        break;
                    }
                }
            }

        } catch (IOException exception) {
            System.out.println("[analyzeFile] | Error, write/read file\n");
            exception.getMessage();
        }
    }

    public Token getToken(int indexFile) {
        Token newToken = new Token();

        System.out.println("[getToken] | Init");
        
        try {
            if (Character.isDigit(currentChar)) {
                newToken = isDigit(currentChar, indexFile);
                
            } else if (Character.isLetter(currentChar)) {
                newToken = isLetter(currentChar, indexFile);
                
            } else if (currentChar == ':') {
                newToken = isAttribution(currentChar, indexFile);
                
            } else if (listAritmetics.contains(currentChar)) {
                newToken = isAritmetic(currentChar, indexFile);
                
            } else if (listRelationals.contains(currentChar)) {
                newToken = isRelational(currentChar, indexFile);
                
            } else if (listPunctuations.contains(currentChar)) {
                newToken = isPunctuation(currentChar, indexFile);
                
            } else {
                throw new LexicalException();
            }

            newToken.print();
            return newToken;

        } catch (LexicalException lexical) {
            lexical.characterInvalid(Integer.toString(indexFile));
            return null;
        }
    }

    public Token isDigit(char character, int lineIndex) {
        Token digit = new Token();
        String number = "";
        
        System.out.println("[isDigit] | Init\n");
        
        number += character;

        try {
            currentChar = (char) reader.read();

            while (Character.isDigit(currentChar)) {
                number += currentChar;
                currentChar = (char) reader.read();
            }
            
            System.out.printf("[isDigit] | Digit: %s\n", number);
            
            digit.setLine(Integer.toString(lineIndex));
            digit.setSymbol("snumero");
            digit.setLexeme(number);

            return digit;

        } catch (IOException exception) {
            exception.getMessage();
            return null;
        }
    }

    public Token isLetter(char character, int lineIndex) {      
        Token letter = new Token();
        String word = "";

        System.out.println("[isLetter] | Init\n");
        
        word += character;

        try {
            currentChar = (char) reader.read();

            while (Character.isLetter(currentChar) || Character.isDigit(currentChar) || currentChar == '_') {
                word += currentChar;
                currentChar = (char) reader.read();
            }
            
            System.out.printf("[isLetter] | Word: %s\n", word);

            letter.setLine(Integer.toString(lineIndex));

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

        } catch (IOException exception) {
            exception.getMessage();
            return null;
        }
    }

    public Token isAttribution(char character, int lineIndex) {
        Token attribution = new Token();
        String attr = "";
        
        System.out.println("[isAttribution] | Init\n"); 
        attr += character;

        attribution.setLine(Integer.toString(lineIndex));

        try {
            currentChar = (char) reader.read();

            if (currentChar == '=') {
                attr += currentChar;
                attribution.setSymbol("satribuição");
                currentChar = (char) reader.read();
            } else {
                attribution.setSymbol("sdoispontos");
            }
            
            System.out.printf("[isAttribution] | Attribution: %s\n", attr);
            attribution.setLexeme(attr);
            return attribution;

        } catch (IOException exception) {
            exception.getMessage();
            return null;
        }
    }

    public Token isAritmetic(char character, int lineIndex) {
        Token aritmetic = new Token();

        System.out.println("[isAritmetic] | Init\n");
        System.out.printf("[isAritmetic] | Aritmetic: %c\n", character);
        
        aritmetic.setLine(Integer.toString(lineIndex));

        if (character == '+') {
            aritmetic.setSymbol("smais");
        } else if (character == '-') {
            aritmetic.setSymbol("smenos");
        } else if (character == '*') {
            aritmetic.setSymbol("smult");
        }
        
        aritmetic.setLexeme(Character.toString(character));

        try {
            currentChar = (char) reader.read();
            return aritmetic;

        } catch (IOException exception) {
            exception.getMessage();
            return null;
        }
    }

    public Token isRelational(char character, int lineIndex) {
        Token relational = new Token();
        String operation = "";
        
        System.out.println("[isRelational] | Init\n");   
        operation += character;

        relational.setLine(Integer.toString(lineIndex));

        try {
            if (character == '>') {
                currentChar = (char) reader.read();

                if (currentChar == '=') {
                    operation += currentChar;
                    relational.setSymbol("smaiorig");
                    currentChar = (char) reader.read();
                } else {
                    relational.setSymbol("maior");
                }
            } else if (character == '<') {
                currentChar = (char) reader.read();

                if (currentChar == '=') {
                    operation += currentChar;
                    relational.setSymbol("smenorig");
                    currentChar = (char) reader.read();
                } else {
                    relational.setSymbol("menor");
                }
            } else if (character == '=') {
                relational.setSymbol("sig");
                currentChar = (char) reader.read();

            } else if (character == '!') {
                currentChar = (char) reader.read();

                if (currentChar == '=') {
                    operation += currentChar;
                    relational.setSymbol("Sdif");
                    currentChar = (char) reader.read();
                } else {
                    throw new LexicalException();
                }
            }
            
            System.out.printf("[isRelational] | Relational: %s\n", operation);
            relational.setLexeme(operation);
            return relational;

        } catch (IOException exception) {
            exception.getMessage();
            return null;

        } catch (LexicalException lexical) {
            lexical.relationalError(Integer.toString(lineIndex));
            return null;
        }
    }

    public Token isPunctuation(char character, int lineIndex) {
        Token punctuation = new Token();
        
        System.out.println("[isPunctuation] | Init\n");     
        System.out.printf("[isPunctuation] | Punctuation: %c\n", character);
       
        punctuation.setLine(Integer.toString(lineIndex));

        if (character == ';') {
            punctuation.setSymbol("sponto_vírgula");
        } else if (character == ',') {
            punctuation.setSymbol("svirgula");
        } else if (character == '(') {
            punctuation.setSymbol("sabre_parênteses");
        } else if (character == ')') {
            punctuation.setSymbol("sfecha_parênteses");
        } else if (character == '.') {
            punctuation.setSymbol("sponto");
        }

        punctuation.setLexeme(Character.toString(character));

        try {
            currentChar = (char) reader.read();
            return punctuation;

        } catch (IOException exception) {
            exception.getMessage();
            return null;
        }
    }

    public Token requestToken(int index) {
        return list.requestToken(index);
    }
}
