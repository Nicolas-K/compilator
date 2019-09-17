package compilator;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class LexicalAnalyzer {

    private static LexicalAnalyzer instance;
    TokenList list = TokenList.getInstance();
    private BufferedReader reader;
    private char currentChar;

    public static LexicalAnalyzer getInstance() {
        if (instance == null) {
            instance = new LexicalAnalyzer();
        }
        return instance;
    }

    public void openFile(String codePath) {
        try {
            reader = new BufferedReader(new FileReader(codePath));
        } catch (FileNotFoundException exception) {
            System.out.println("Error! Arquivo não encontrado\n");
            exception.getMessage();
        }
    }

    public void closeFile() {
        try {
            if (reader != null) {
                reader.close();
            }
        } catch (IOException exception) {
            System.out.println("Error! Não foi possível fechar o arquivo\n");
            exception.getMessage();
        }
    }

    public void analyzeFile() {
        int indexFile;
        Token newToken;
        try {
            indexFile = 0;

            currentChar = (char) reader.read();
            while (currentChar != -1) {

                while ((currentChar == ' ' || currentChar == '{' || currentChar == '\n') && currentChar != -1) {
                    if (currentChar == '{') {
                        while (currentChar != '}' && currentChar != -1) {
                            currentChar = (char) reader.read();
                        }
                        
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
                    newToken = getToken(indexFile);

                    if (newToken != null) {
                        list.insertToken(newToken);
                    }
                }

            }

        } catch (IOException exception) {
            System.out.println("Error! Leitura/Escrita do arquivo\n");
            exception.getMessage();
        }
    }

    public Token getToken(int indexFile) {
        Token newToken = new Token();

        try {
            if (Character.isDigit(currentChar)) {
                newToken = isDigit(currentChar, indexFile);
            } else if (Character.isLetter(currentChar)) {
                newToken = isLetter(currentChar, indexFile);
            } else if (currentChar == ':') {
                newToken = isAttribution(currentChar, indexFile);
            } else if (currentChar == '+' || currentChar == '-' || currentChar == '*') {
                newToken = isAritmetic(currentChar, indexFile);
            } else if (currentChar == '>' || currentChar == '<' || currentChar == '=' || currentChar == '!') {
                newToken = isRelational(currentChar, indexFile);
            } else if (currentChar == ';' || currentChar == ',' || currentChar == '(' || currentChar == ')' || currentChar == '.') {
                newToken = isPunctuation(currentChar, indexFile);
            } else {
                throw new LexicalException();
            }

            return newToken;

        } catch (LexicalException lexical) {
            lexical.characterInvalid(Integer.toString(indexFile));
            return null;
        }
    }

    public Token isDigit(char character, int lineIndex) {
        Token digit = new Token();
        String number = null;

        number += character;

        try {
            currentChar = (char) reader.read();

            while (Character.isDigit(currentChar)) {
                number += currentChar;
                currentChar = (char) reader.read();
            }

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
        String word = null;

        word += character;

        try {
            currentChar = (char) reader.read();

            while (Character.isLetter(currentChar) || Character.isDigit(currentChar) || currentChar == '_') {
                word += currentChar;
                currentChar = (char) reader.read();
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
        String attr = null;
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

            attribution.setLexeme(attr);
            return attribution;

        } catch (IOException exception) {
            exception.getMessage();
            return null;
        }
    }

    public Token isAritmetic(char character, int lineIndex) {
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
        String operation = null;
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
