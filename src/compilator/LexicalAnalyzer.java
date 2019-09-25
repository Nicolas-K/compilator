package compilator;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.io.IOException;

public class LexicalAnalyzer {

    private static LexicalAnalyzer instance = null;
    private final ArrayList<Character> toIgnore;
    private final ArrayList<Character> aritmetics;
    private final ArrayList<Character> relationals;
    private final ArrayList<Character> punctuations;

    private BufferedReader reader;
    private int indexFileLine;
    private int charRead;
    private char currentChar;
    private TokenList list = TokenList.getInstance();

    public static LexicalAnalyzer getInstance() {
        if (instance == null) {
            instance = new LexicalAnalyzer();
        }
        return instance;
    }

    private LexicalAnalyzer() {
        reader = null;
        indexFileLine = 1;

        toIgnore = new ArrayList<>();
        aritmetics = new ArrayList<>();
        relationals = new ArrayList<>();
        punctuations = new ArrayList<>();

        toIgnore.add(' ');
        toIgnore.add('{');
        toIgnore.add('\n');
        toIgnore.add('\r');
        toIgnore.add('\t');

        aritmetics.add('+');
        aritmetics.add('-');
        aritmetics.add('*');

        relationals.add('>');
        relationals.add('<');
        relationals.add('=');
        relationals.add('!');

        punctuations.add(';');
        punctuations.add(',');
        punctuations.add('(');
        punctuations.add(')');
        punctuations.add('.');
    }

    public boolean openFile(String codePath) {
        System.out.println("[OpenFile] | Init\n");

        try {
            reader = new BufferedReader(new FileReader(codePath));
            System.out.println("[OpenFile] | File opened\n");
            return true;

        } catch (FileNotFoundException exception) {
            System.out.println("[OpenFile] | Error, file not found\n");
        }

        return false;
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
        }

    }

    public Token newToken(String path) {
        Token createToken;
        
        if (reader == null) {
            if (!openFile(path)) {
                return null;
            }
        }

        try {

            charRead = reader.read();
            currentChar = (char) charRead;

            if (charRead != -1) {

                while ((toIgnore.contains(currentChar)) && charRead != -1) {
                    if (currentChar == '{') {
                        
                        while (currentChar != '}' && charRead != -1) {
                            charRead = reader.read();
                            currentChar = (char) charRead;
                        }

                        charRead = reader.read();
                        currentChar = (char) charRead;

                    } else if (currentChar == '\n') {
                        indexFileLine++;

                        charRead = reader.read();
                        currentChar = (char) charRead;

                    } else {
                        charRead = reader.read();
                        currentChar = (char) charRead;

                        while (currentChar == ' ' && charRead != -1) {
                            charRead = reader.read();
                            currentChar = (char) charRead;
                        }
                    }
                }

                if (charRead != -1) {
                    System.out.printf("[newToken] | Line: %d\n", indexFileLine);
                    System.out.printf("[newToken] | Character: %c\n", currentChar);
                    createToken = getToken(indexFileLine);
                    return createToken;
                    
                } else {
                    closeFile();
                }

            } else {
                closeFile();
            }
            return null;
            
        } catch (IOException exception) {
            System.out.println("[newToken] | Error, read file\n");
        }
        
        return null;
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

            } else if (aritmetics.contains(currentChar)) {
                newToken = isAritmetic(currentChar, indexFile);

            } else if (relationals.contains(currentChar)) {
                newToken = isRelational(currentChar, indexFile);

            } else if (punctuations.contains(currentChar)) {
                newToken = isPunctuation(currentChar, indexFile);

            } else {
                throw new LexicalException();
            }

            return newToken;

        } catch (LexicalException lexical) {
            lexical.characterInvalid(Integer.toString(indexFile), currentChar);
        }
        
        return null;
    }

    public Token isDigit(char character, int lineIndex) {
        Token digit = new Token();
        String number = "";

        System.out.println("[isDigit] | Init\n");

        number += character;

        try {
            charRead = reader.read();
            currentChar = (char) charRead;

            while (Character.isDigit(currentChar)) {
                number += currentChar;
                charRead = reader.read();
                currentChar = (char) charRead;
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
            charRead = reader.read();
            currentChar = (char) charRead;

            while (Character.isLetter(currentChar) || Character.isDigit(currentChar) || currentChar == '_') {
                word += currentChar;
                charRead = reader.read();
                currentChar = (char) charRead;
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
            charRead = reader.read();
            currentChar = (char) charRead;

            if (currentChar == '=') {
                attr += currentChar;
                attribution.setSymbol("satribuição");
                charRead = reader.read();
                currentChar = (char) charRead;

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
            charRead = reader.read();
            currentChar = (char) charRead;
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
                charRead = reader.read();
                currentChar = (char) charRead;

                if (currentChar == '=') {
                    operation += currentChar;
                    relational.setSymbol("smaiorig");
                    charRead = reader.read();
                    currentChar = (char) charRead;

                } else {
                    relational.setSymbol("maior");
                }
            } else if (character == '<') {
                charRead = reader.read();
                currentChar = (char) charRead;

                if (currentChar == '=') {
                    operation += currentChar;
                    relational.setSymbol("smenorig");
                    charRead = reader.read();
                    currentChar = (char) charRead;

                } else {
                    relational.setSymbol("menor");
                }
            } else if (character == '=') {
                relational.setSymbol("sig");
                
                charRead = reader.read();
                currentChar = (char) charRead;

            } else if (character == '!') {
                charRead = reader.read();
                currentChar = (char) charRead;

                if (currentChar == '=') {
                    operation += currentChar;
                    relational.setSymbol("Sdif");
                    
                    charRead = reader.read();
                    currentChar = (char) charRead;

                } else {
                    throw new LexicalException();
                }
            }

            System.out.printf("[isRelational] | Relational: %s\n", operation);
            relational.setLexeme(operation);
            return relational;

        } catch (IOException exception) {
            return null;

        } catch (LexicalException lexical) {
            lexical.relationalError(Integer.toString(lineIndex), currentChar);
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
            charRead = reader.read();
            currentChar = (char) charRead;
            return punctuation;

        } catch (IOException exception) {
            exception.getMessage();
            return null;
        }
    }
}
