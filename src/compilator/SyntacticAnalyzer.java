package compilator;

public class SyntacticAnalyzer {

    private static SyntacticAnalyzer instance = null;
    private LexicalAnalyzer lexicalAnalyzer = LexicalAnalyzer.getInstance();
    private Token token;

    public static SyntacticAnalyzer getInstance() {
        if (instance == null) {
            instance = new SyntacticAnalyzer();
        }
        return instance;
    }

    public void syntaticAnalyze() {
        token = lexicalAnalyzer.getNewToken();

        //try {
        if (token.symbol.equals("sprograma")) {
            token = lexicalAnalyzer.getNewToken();

            if (token.symbol.equals("sidentificador")) {
                // insere_tabela(token.lexema, "nomedeprograma","","")
                token = lexicalAnalyzer.getNewToken();

                if (token.symbol.equals("sponto_vírgula")) {
                    analyzeBlock();

                    if (token.symbol.equals("sponto")) {
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
            // ERRO
        }
        //} catch () {

        //}
    }

    public void analyzeBlock() {
        token = lexicalAnalyzer.getNewToken();
        analyzeVariablesDeclaration();
        analyzeSubRoutineDeclaration();
        analyzeCommands();
    }

    public void analyzeVariablesDeclaration() {
        // try {
        if (token.symbol.equals("svar")) {
            token = lexicalAnalyzer.getNewToken();
            if (token.symbol.equals("sidentificador")) {

                while (token.symbol.equals("sidentificador")) {
                    analyzeVariables();

                    if (token.symbol.equals("sponto_vírgula")) {
                        token = lexicalAnalyzer.getNewToken();
                    } else {
                        // ERRO
                    }
                }
            } else {
                // ERRO
            }
        }
        // } catch () {

        // }
    }

    public void analyzeVariables() {
        do {
            if (token.symbol.equals("sidentificador")) {
                token = lexicalAnalyzer.getNewToken();

                if (token.symbol.equals("svirgula") || token.symbol.equals("sdoispontos")) {
                    if (token.symbol.equals("svirgula")) {
                        token = lexicalAnalyzer.getNewToken();

                        if (token.symbol.equals("sdoispontos")) {
                            // ERRO
                        }
                    }
                }

            }
        } while (!token.symbol.equals("sdoispontos"));
    }

    public void analyzeSubRoutineDeclaration() {
        int flag = 0;

        //try {
        if (token.symbol.equals("sprocedimento") || token.symbol.equals("sfuncao")) {
            flag = 1;
        }

        while (token.symbol.equals("sprocedimento") || token.symbol.equals("sfuncao")) {
            if (token.symbol.equals("sprocedimento")) {
                analyzeProcedureDeclaration();
            } else {
                analyzeFunctionDeclaration();
            }

            if (token.symbol.equals("sponto_vírgula")) {
                token = lexicalAnalyzer.getNewToken();
            } else {
                // ERRO
            }
        }

        if (flag == 1) {

        }
        // } catch () {

        // }
    }

    public void analyzeProcedureDeclaration() {

    }

    public void analyzeFunctionDeclaration() {

    }

    public void analyzeCommands() {

    }
}