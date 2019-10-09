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
        ProcedureProgram symbolProgram = new ProcedureProgram();

        token = lexicalAnalyzer.lexicalAnalyze(path);

        try {
            if (token.getSymbol().equals("sprograma")) {
                token = lexicalAnalyzer.lexicalAnalyze(path);

                if (token.getSymbol().equals("sidentificador")) {
                    symbolProgram.setLexemeName(token.getLexeme());
                    symbolProgram.setScope("");
                    table.insertSymbol(symbolProgram);
                    token = lexicalAnalyzer.lexicalAnalyze(path);

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
        token = lexicalAnalyzer.lexicalAnalyze(path);
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
                token = lexicalAnalyzer.lexicalAnalyze(path);
                if (token.getSymbol().equals("sidentificador")) {

                    while (token.getSymbol().equals("sidentificador")) {
                        analyzeVariables();

                        if (token.getSymbol().equals("sponto_vírgula")) {
                            token = lexicalAnalyzer.lexicalAnalyze(path);
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
            Variable symbolVariable = new Variable();

            if (token.getSymbol().equals("sidentificador")) {
                //Pesquisa_duplicvar_ tabela(token.lexema)
                //se não encontrou duplicidade
                symbolVariable.setLexemeName(token.getLexeme());
                symbolVariable.setScope("");
                table.insertSymbol(symbolVariable);
                token = lexicalAnalyzer.lexicalAnalyze(path);

                if (token.getSymbol().equals("svirgula") || token.getSymbol().equals("sdoispontos")) {
                    if (token.getSymbol().equals("svirgula")) {
                        token = lexicalAnalyzer.lexicalAnalyze(path);

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
                    token = lexicalAnalyzer.lexicalAnalyze(path);
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
        ProcedureProgram symbolProcedure = new ProcedureProgram();

        token = lexicalAnalyzer.lexicalAnalyze(path);

        try {
            if (token.getSymbol().equals("sidentificador")) {
                //Pesquisa
                //Inserir tabela de símbolos
                //Gera rótulo
                token = lexicalAnalyzer.lexicalAnalyze(path);
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
        //TODO
    }

    public void analyzeFunctionDeclaration() {
        Function symbolFunction = new Function();

        token = lexicalAnalyzer.lexicalAnalyze(path);

        try {
            //Label
            if (token.getSymbol().equals("sidentificador")) {
                //pesquisa
                //insere tabela
                token = lexicalAnalyzer.lexicalAnalyze(path);
                if (token.getSymbol().equals("sdoispontos")) {
                    token = lexicalAnalyzer.lexicalAnalyze(path);

                    if (token.getSymbol().equals("sinteiro") || token.getSymbol().equals("sbooleano")) {
                        if (token.getSymbol().equals("sinteiro")) {
                            //tabela de símbolo tipo = função inteiro
                        } else {
                            //função boolean
                        }
                        token = lexicalAnalyzer.lexicalAnalyze(path);

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
        //TODO
    }

    /*
     *  Analisa de Comandos
     */
    public void analyzeCommands() {
        try {
            if (token.getSymbol().equals("sinício")) {
                token = lexicalAnalyzer.lexicalAnalyze(path);
                analyzeCommand();

                while (!token.getSymbol().equals("sfim")) {
                    if (token.getSymbol().equals("sponto_vírgula")) {
                        token = lexicalAnalyzer.lexicalAnalyze(path);

                        if (!token.getSymbol().equals("sfim")) {
                            analyzeCommand();
                        }
                    } else {
                        throw new SyntacticException();
                    }
                    token = lexicalAnalyzer.lexicalAnalyze(path);
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
        token = lexicalAnalyzer.lexicalAnalyze(path);

        if (token.getSymbol().equals("satribuição")) {
            analyzeAttribution();
        } else {
            analyzeProcedureCall();
        }
    }

    public void analyzeRead() {
        token = lexicalAnalyzer.lexicalAnalyze(path);

        try {
            if (token.getSymbol().equals("sabre_parênteses")) {
                token = lexicalAnalyzer.lexicalAnalyze(path);

                if (token.getSymbol().equals("sidentificador")) {
                    // se pesquisa tabela simbolos var
                    token = lexicalAnalyzer.lexicalAnalyze(path);

                    if (token.getSymbol().equals("sfecha_parênteses")) {
                        token = lexicalAnalyzer.lexicalAnalyze(path);
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
        token = lexicalAnalyzer.lexicalAnalyze(path);

        try {
            if (token.getSymbol().equals("sabre_parênteses")) {
                token = lexicalAnalyzer.lexicalAnalyze(path);

                if (token.getSymbol().equals("sidentificador")) {
                    // se pesquisa tabela simbolos function
                    token = lexicalAnalyzer.lexicalAnalyze(path);

                    if (token.getSymbol().equals("sfecha_parênteses")) {
                        token = lexicalAnalyzer.lexicalAnalyze(path);
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
        token = lexicalAnalyzer.lexicalAnalyze(path);
        analyzeExpressions();

        try {
            if (token.getSymbol().equals("sfaca")) {
                //rot2 = rotulo
                // gera
                // rotulo = rotulo + 1
                token = lexicalAnalyzer.lexicalAnalyze(path);
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
        token = lexicalAnalyzer.lexicalAnalyze(path);
        analyzeExpression();

        try {
            if (token.getSymbol().equals("sentao")) {
                token = lexicalAnalyzer.lexicalAnalyze(path);
                analyzeCommand();

                if (token.getSymbol().equals("ssenao")) {
                    token = lexicalAnalyzer.lexicalAnalyze(path);
                    analyzeCommand();
                }
            } else {
                throw new SyntacticException();
            }
        } catch (SyntacticException exception) {

        }
    }

    public void analyzeAttribution() {
        //TODO
    }

    /*
     *  Analise Relacionado a Tipo, Termos e Expressões
     */
    public void analyzeType() {
        try {
            if (token.getSymbol().equals("sinteiro") || token.getSymbol().equals("sbooleano")) {
                table.setTypeSymbols(token.getSymbol());
            } else {
                throw new SyntacticException();
            }
        } catch (SyntacticException exception) {

        }

        token = lexicalAnalyzer.lexicalAnalyze(path);
    }

    public void analyzeExpressions() {
        analyzeExpression();
        if ((token.getSymbol().equals("smaior")) || (token.getSymbol().equals("smaiorig")) || (token.getSymbol().equals("sig")) || (token.getSymbol().equals("smenor")) || (token.getSymbol().equals("smenorig")) || (token.getSymbol().equals("sdif"))) {
            token = lexicalAnalyzer.lexicalAnalyze(path);
            analyzeExpression();
        }
    }

    public void analyzeExpression() {
        if (token.getSymbol().equals("smais") || token.getSymbol().equals("smenos")) {
            token = lexicalAnalyzer.lexicalAnalyze(path);
        }
        analyzeTerm();
        while (token.getSymbol().equals("smais") || token.getSymbol().equals("smenos") || token.getSymbol().equals("sou")) {
            token = lexicalAnalyzer.lexicalAnalyze(path);
            analyzeTerm();
        }
    }

    public void analyzeTerm() {
        analyzeFactor();
        while ((token.getSymbol().equals("smult")) || (token.getSymbol().equals("sdiv")) || (token.getSymbol().equals("se"))) {
            token = lexicalAnalyzer.lexicalAnalyze(path);
            analyzeFactor();
        }
    }

    public void analyzeFactor() {
        try {

            if (token.getSymbol().equals("sidentificador")) {
                //Se pesquisa_tabela(token.lexema,nível,ind)
                //Então Se (TabSimb[ind].tipo = “função inteiro”) ou (TabSimb[ind].tipo = “função booleano”)
                //Então analyzeFunctionCall();
                //Senão token = lexicalAnalyzer.lexicalAnalyzer(path)
                // else throw new SyntacticException();
            } else if (token.getSymbol().equals("snumero")) {
                token = lexicalAnalyzer.lexicalAnalyze(path);
            } else if (token.getSymbol().equals("snao")) {
                token = lexicalAnalyzer.lexicalAnalyze(path);
                analyzeFactor();
            } else if (token.getSymbol().equals("sabre_parênteses")) {
                token = lexicalAnalyzer.lexicalAnalyze(path);
                analyzeExpressions();

                if (token.getSymbol().equals("sfecha_parênteses")) {
                    token = lexicalAnalyzer.lexicalAnalyze(path);
                } else {
                    throw new SyntacticException();
                }

            } else if (token.getSymbol().equals("sverdadeiro") || token.getSymbol().equals("sfalso")) {
                token = lexicalAnalyzer.lexicalAnalyze(path);
            } else {
                throw new SyntacticException();
            }

        } catch (SyntacticException exception) {

        }
    }
}
