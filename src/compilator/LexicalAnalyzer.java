package compilator;


import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.BufferedReader;
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
    private final ErrorMessages message = ErrorMessages.getInstance();
    private String error;

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

    private boolean openFile(String codePath) {
        try {
            reader = new BufferedReader(new FileReader(codePath));
            System.out.println("[OpenFile] | File opened");
            return true;

        } catch (FileNotFoundException exception) {
            System.out.println("[OpenFile] | Error, file not found");
        }

        return false;
    }

    private boolean closeFile() {
        try {
            if (reader != null) {
                reader.close();
                System.out.println("[CloseFile] | File closed");
            }

            return true;
        } catch (IOException exception) {
            System.out.println("[CloseFile] | Error, file not closed");
        }

        return false;
    }

    protected boolean hasFileEnd() {
        if (charRead == -1) {
            return true;
        } else {
            return false;
        }
    }

    private void setErrorMessage(String errorMessage) {
        this.error = errorMessage;
    }

    public String getErrorMessage() {
        return error;
    }

    public Token lexicalAnalyze(String path) throws IOException {
        Token createToken;

        if (reader == null) {
            if (!this.openFile(path)) {
                return null;
            } else {
                charRead = reader.read();
                currentChar = (char) charRead;
            }
        }

        try {
            if (!hasFileEnd()) {
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

                if (!hasFileEnd()) {
                    return this.getToken(indexFileLine);

                } else {
                    this.closeFile();
                }

            } else {
                this.closeFile();
            }

        } catch (Exception e) {
            if (e.getMessage() != null) {
                setErrorMessage(e.getMessage());
            } else {
                setErrorMessage("[ Lexical Error ] | Something unexpected happened");
            }
        }
        return null;
    }

    private Token getToken(int indexFile) throws Exception {
        Token newToken = new Token();

        if (Character.isDigit(currentChar)) {
            newToken = this.isDigit(currentChar, indexFile);

        } else if (Character.isLetter(currentChar)) {
            newToken = this.isLetter(currentChar, indexFile);

        } else if (currentChar == ':') {
            newToken = this.isAttribution(currentChar, indexFile);

        } else if (aritmetics.contains(currentChar)) {
            newToken = this.isAritmetic(currentChar, indexFile);

        } else if (relationals.contains(currentChar)) {
            newToken = this.isRelational(currentChar, indexFile);

        } else if (punctuations.contains(currentChar)) {
            newToken = this.isPunctuation(currentChar, indexFile);

        } else {
            throw new Exception(message.lexicalError(Integer.toString(indexFile), Character.toString(currentChar)));
        }

        return newToken;
    }

    private Token isDigit(char character, int lineIndex) throws Exception {
        Token digit = new Token();
        String number = "";

        number += character;
        charRead = reader.read();
        currentChar = (char) charRead;

        while (Character.isDigit(currentChar)) {
            number += currentChar;
            charRead = reader.read();
            currentChar = (char) charRead;
        }

        digit.setLine(Integer.toString(lineIndex));
        digit.setSymbol("snumero");
        digit.setLexeme(number);
        return digit;
    }

    private Token isLetter(char character, int lineIndex) throws Exception {
        Token letter = new Token();
        String word = "";

        word += character;
        charRead = reader.read();
        currentChar = (char) charRead;

        while (Character.isLetter(currentChar) || Character.isDigit(currentChar) || currentChar == '_') {
            word += currentChar;
            charRead = reader.read();
            currentChar = (char) charRead;
        }

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
                break;
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

    private Token isAttribution(char character, int lineIndex) throws Exception {
        Token attribution = new Token();
        String attr = "";

        attr += character;
        attribution.setLine(Integer.toString(lineIndex));
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

        attribution.setLexeme(attr);
        return attribution;
    }

    private Token isAritmetic(char character, int lineIndex) throws Exception {
        Token aritmetic = new Token();

        aritmetic.setLine(Integer.toString(lineIndex));

        if (character == '+') {
            aritmetic.setSymbol("smais");
        } else if (character == '-') {
            aritmetic.setSymbol("smenos");
        } else if (character == '*') {
            aritmetic.setSymbol("smult");
        }

        aritmetic.setLexeme(Character.toString(character));
        charRead = reader.read();
        currentChar = (char) charRead;
        return aritmetic;
    }

    private Token isRelational(char character, int lineIndex) throws Exception {
        Token relational = new Token();
        String operation = "";

        operation += character;
        relational.setLine(Integer.toString(lineIndex));

        if (character == '>') {
            charRead = reader.read();
            currentChar = (char) charRead;

            if (currentChar == '=') {
                operation += currentChar;
                relational.setSymbol("smaiorig");
                charRead = reader.read();
                currentChar = (char) charRead;

            } else {
                relational.setSymbol("smaior");
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
                relational.setSymbol("smenor");
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
                relational.setSymbol("sdif");
                charRead = reader.read();
                currentChar = (char) charRead;

            } else {
                throw new Exception(message.lexicalError(Integer.toString(lineIndex), operation));
            }
        }

        relational.setLexeme(operation);
        return relational;
    }

    private Token isPunctuation(char character, int lineIndex) throws Exception {
        Token punctuation = new Token();

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
        charRead = reader.read();
        currentChar = (char) charRead;
        return punctuation;
    }
}
