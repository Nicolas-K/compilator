package compilator;

public class SyntacticAnalyzer {

    private static SyntacticAnalyzer instance = null;
    private LexicalAnalyzer lexicalAnalyzer = LexicalAnalyzer.getInstance();
    private Token token;
    private String path;

    public static SyntacticAnalyzer getInstance() {
        if (instance == null) {
            instance = new SyntacticAnalyzer();
        }
        return instance;
    }

    public void syntaticAnalyze() {
        token = lexicalAnalyzer.newToken(path);

        try {
            if (token.getSymbol().equals("sprograma")) {
                token = lexicalAnalyzer.newToken(path);

                if (token.getSymbol().equals("sidentificador")) {
                    // insere_tabela(token.lexema, "nomedeprograma","","")
                    token = lexicalAnalyzer.newToken(path);

                    if (token.getSymbol().equals("sponto_vírgula")) {
                        analyzeBlock();

                        if (token.getSymbol().equals("sponto")) {
                            // se acabou arquivo ou é comentario
                            //sucesso
                            //senao ERRO
                        } else {
                            // ERRO
                        }
                    } else {
                        //ERRO
                    }
                } else {
                    // ERRO
                }
            } else {
                throw new SyntacticException();
            }
        } catch (SyntacticException exception) {
        }
    }

    public void analyzeBlock() {
        token = lexicalAnalyzer.newToken(path);
        analyzeVariablesDeclaration();
        analyzeSubRoutineDeclaration();
        analyzeCommands();
    }

    public void analyzeVariablesDeclaration() {
        try {
            if (token.getSymbol().equals("svar")) {
                token = lexicalAnalyzer.newToken(path);
                if (token.getSymbol().equals("sidentificador")) {

                    while (token.getSymbol().equals("sidentificador")) {
                        analyzeVariables();

                        if (token.getSymbol().equals("sponto_vírgula")) {
                            token = lexicalAnalyzer.newToken(path);
                        } else {
                            // ERRO
                        }
                    }
                } else {
                    throw new SyntacticException();
                }
            }
        } catch (SyntacticException exception) {

        }
    }

    public void analyzeVariables() {
        do {
            if (token.getSymbol().equals("sidentificador")) {
                token = lexicalAnalyzer.newToken(path);

                if (token.getSymbol().equals("svirgula") || token.getSymbol().equals("sdoispontos")) {
                    if (token.getSymbol().equals("svirgula")) {
                        token = lexicalAnalyzer.newToken(path);

                        if (token.getSymbol().equals("sdoispontos")) {
                            // ERRO
                        }
                    }
                }

            }
        } while (!token.getSymbol().equals("sdoispontos"));
    }

    public void analyzeSubRoutineDeclaration() {
        int flag = 0;

        try {
            if (token.getSymbol().equals("sprocedimento") || token.getSymbol().equals("sfuncao")) {
                flag = 1;
            }

            while (token.getSymbol().equals("sprocedimento") || token.getSymbol().equals("sfuncao")) {
                if (token.getSymbol().equals("sprocedimento")) {
                    analyzeProcedureDeclaration();
                } else {
                    analyzeFunctionDeclaration();
                }

                if (token.getSymbol().equals("sponto_vírgula")) {
                    token = lexicalAnalyzer.newToken(path);
                } else {
                    throw new SyntacticException();
                }
            }

            if (flag == 1) {

            }
        } catch (SyntacticException exception) {

        }
    }

    public void analyzeProcedureDeclaration() {

    }

    public void analyzeFunctionDeclaration() {

    }

    public void analyzeCommands() {

    }
}
