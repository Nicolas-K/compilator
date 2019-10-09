package compilator;

public class SyntacticAnalyzer {

    private static SyntacticAnalyzer instance = null;
    private LexicalAnalyzer lexicalAnalyzer = LexicalAnalyzer.getInstance();
    private Token token;
    private String path;
    private SymbolTable table = SymbolTable.getInstance();

    public static SyntacticAnalyzer getInstance() {
        if (instance == null) {
            instance = new SyntacticAnalyzer();
        }
        return instance;
    }

    public void receiveFilePath(String path) {
        this.path = path;
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
                            throw new SyntacticException();
                        }
                    } else {
                        throw new SyntacticException();
                    }
                } else {
                    throw new SyntacticException();
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

    /*
     *  Analise Relacionada a Variaveis
     */
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

    /*
     *  Analise Relacionada a SubRotinas
     */
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
        token = lexicalAnalyzer.newToken(path);

        try {
            if (token.getSymbol().equals("sidentificador")) {
                //Pesquisa
                //Inserir tabela de símbolos
                //Gera rótulo
                token = lexicalAnalyzer.newToken(path);
                if (token.getSymbol().equals("sponto_vírgula")) {
                    analyzeBlock();
                } else {
                    throw new SyntacticException();
                }
            } else {
                throw new SyntacticException();
            }
        } catch (SyntacticException exception) {

        }
        //Desempilha e volta nível
    }

    public void analyzeProcedureCall() {

    }

    public void analyzeFunctionDeclaration() {
        token = lexicalAnalyzer.newToken(path);

        try {
            //Label
            if (token.getSymbol().equals("sidentificador")) {
                //pesquisa
                //insere tabela
                token = lexicalAnalyzer.newToken(path);
                if (token.getSymbol().equals("sdoispontos")) {
                    token = lexicalAnalyzer.newToken(path);
                    if (token.getSymbol().equals("sinteiro") || token.getSymbol().equals("sbooleano")) {
                        if (token.getSymbol().equals("sinteiro")) {
                            //tabela de símbolo tipo = função inteiro
                        } else {
                            //função boolean
                        }
                        token = lexicalAnalyzer.newToken(path);

                        if (token.getSymbol().equals("sponto_vírgula")) {
                            analyzeBlock();
                        } else {
                            throw new SyntacticException();
                        }

                    } else {
                        throw new SyntacticException();
                    }
                } else {
                    throw new SyntacticException();
                }
            } else {
                throw new SyntacticException();
            }

        } catch (SyntacticException exception) {

        }
        //Desempilha u volta nível
    }

    public void analyzeFunctionCall() {

    }

    /*
     *  Analisa de Comandos
     */
    public void analyzeCommands() {
        try {
            if (token.getSymbol().equals("sinício")) {
                token = lexicalAnalyzer.newToken(path);
                analyzeCommand();
                while (!token.getSymbol().equals("sfim")) {
                    if (token.getSymbol().equals("sponto_vírgula")) {
                        token = lexicalAnalyzer.newToken(path);
                        if (!token.getSymbol().equals("sfim")) {
                            analyzeCommand();
                        }
                    } else {
                        throw new SyntacticException();
                    }
                    token = lexicalAnalyzer.newToken(path);
                }
            } else {
                throw new SyntacticException();
            }
        } catch (SyntacticException exception) {

        }
    }

    public void analyzeCommand() {
        if (token.getSymbol().equals("sidentificador")) {
            analyzeAttrProcedure();
        } else if (token.getSymbol().equals("sse")) {
            analyzeIf();
        } else if (token.getSymbol().equals("senquanto")) {
            analyzeWhile();
        } else if (token.getSymbol().equals("sleia")) {
            analyzeRead();
        } else if (token.getSymbol().equals("sescreva")) {
            analyzeWrite();
        } else {
            analyzeCommands();
        }
    }

    public void analyzeAttrProcedure() {
        token = lexicalAnalyzer.newToken(path);
        if (token.getSymbol().equals("satribuição")) {
            analyzeAttribution();
        } else {
            analyzeProcedureCall();
        }
    }

    public void analyzeRead() {
        token = lexicalAnalyzer.newToken(path);
        try {
            if (token.getSymbol().equals("sabre_parênteses")) {
                token = lexicalAnalyzer.newToken(path);
                if (token.getSymbol().equals("sidentificador")) {
                    // se pesquisa tabela simbolos var
                    token = lexicalAnalyzer.newToken(path);
                    if (token.getSymbol().equals("sfecha_parênteses")) {
                        token = lexicalAnalyzer.newToken(path);
                    } else {
                        throw new SyntacticException();
                    }
                    // senao throw new SyntacticException();
                } else {
                    throw new SyntacticException();
                }
            } else {
                throw new SyntacticException();
            }
        } catch (SyntacticException exception) {

        }
    }

    public void analyzeWrite() {
        token = lexicalAnalyzer.newToken(path);
        try {
            if (token.getSymbol().equals("sabre_parênteses")) {
                token = lexicalAnalyzer.newToken(path);
                if (token.getSymbol().equals("sidentificador")) {
                    // se pesquisa tabela simbolos function
                    token = lexicalAnalyzer.newToken(path);
                    if (token.getSymbol().equals("sfecha_parênteses")) {
                        token = lexicalAnalyzer.newToken(path);
                    } else {
                        throw new SyntacticException();
                    }
                    // senao throw new SyntacticException();
                } else {
                    throw new SyntacticException();
                }
            } else {
                throw new SyntacticException();
            }
        } catch (SyntacticException exception) {

        }
    }

    public void analyzeWhile() {
        //Rotulos definição
        //Gera
        // rotulo = rotulo + 1
        token = lexicalAnalyzer.newToken(path);
        analyzeExpressions();
        try {
            if (token.getSymbol().equals("sfaca")) {
                //rot2 = rotulo
                // gera
                // rotulo = rotulo + 1
                token = lexicalAnalyzer.newToken(path);
                analyzeCommand();
                //Gera
                //Gera
            } else {
                throw new SyntacticException();
            }
        } catch (SyntacticException exception) {

        }
    }

    public void analyzeIf() {
        token = lexicalAnalyzer.newToken(path);
        analyzeExpression();

        try {
            if (token.getSymbol().equals("sentao")) {
                token = lexicalAnalyzer.newToken(path);
                analyzeCommand();
                if (token.getSymbol().equals("ssenao")) {
                    token = lexicalAnalyzer.newToken(path);
                    analyzeCommand();
                }
            } else {
                throw new SyntacticException();
            }
        } catch (SyntacticException exception) {

        }
    }

    public void analyzeAttribution() {

    }

    /*
     *  Analise Relacionado a Tipo, Termos e Expressões
     */
    public void analyzeType() {

    }

    public void analyzeExpressions() {
        analyzeExpression();
        if ((token.getSymbol().equals("smaior")) || (token.getSymbol().equals("smaiorig")) || (token.getSymbol().equals("sig")) || (token.getSymbol().equals("smenor")) || (token.getSymbol().equals("smenorig")) || (token.getSymbol().equals("sdif"))) {
            token = lexicalAnalyzer.newToken(path);
            analyzeExpression();
        }
    }

    public void analyzeExpression() {

    }

    public void analyzeTerm() {
        analyzeFactor();
        while ((token.getSymbol().equals("smult")) || (token.getSymbol().equals("sdiv")) || (token.getSymbol().equals("se"))) {
            token = lexicalAnalyzer.newToken(path);
            analyzeFactor();
        }
    }

    public void analyzeFactor() {

    }
}
