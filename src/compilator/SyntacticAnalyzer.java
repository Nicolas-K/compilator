package compilator;

public class SyntacticAnalyzer {

    private static SyntacticAnalyzer instance = null;
    private String path;

    private final LexicalAnalyzer lexicalAnalyzer;
    // private final SemanticAnalyzer semanticAnalyzer;

    private Token token;
    private final SymbolTable table;

    private final ErrorMessages message;

    public static SyntacticAnalyzer getInstance() {
        if (instance == null) {
            instance = new SyntacticAnalyzer();
        }
        return instance;
    }

    public SyntacticAnalyzer() {
        this.message = ErrorMessages.getInstance();
        this.lexicalAnalyzer = LexicalAnalyzer.getInstance();
        // this.semanticAnalyzer = SemanticAnalyzer.getInstance();
        this.token = null;
        this.path = null;
        this.table = SymbolTable.getInstance();
    }

    protected void receiveFilePath(String path) {
        this.path = path;
    }

    /*
     *  Ocorrencia de erro no analisador léxico
     */
    private boolean isEmpty(Token receivedToken) {
        if (receivedToken == null) {
            return true;
        }
        return false;
    }

    public void syntaticAnalyze() {
        String scopeProgram;
        ProcedureProgram symbolProgram = new ProcedureProgram();
        
        System.out.println("[syntaticAnalyze] | ");

        try {
            token = lexicalAnalyzer.lexicalAnalyze(path);
            token.print();

            if (!isEmpty(token)) {
                if (token.getSymbol().equals("sprograma")) {
                    token = lexicalAnalyzer.lexicalAnalyze(path);
                    token.print();

                    if (!isEmpty(token)) {
                        if (token.getSymbol().equals("sidentificador")) {
                            symbolProgram.setLexemeName(token.getLexeme());
                            symbolProgram.setScope("");
                            table.insertSymbol(symbolProgram);

                            scopeProgram = token.getLexeme();
                            token = lexicalAnalyzer.lexicalAnalyze(path);
                            token.print();

                            if (!isEmpty(token)) {
                                if (token.getSymbol().equals("sponto_vírgula")) {
                                    analyzeBlock(scopeProgram);

                                    if (token.getSymbol().equals("sponto")) {
                                        
                                        token = lexicalAnalyzer.lexicalAnalyze(path);
                                        
                                        if (!lexicalAnalyzer.hasFileEnd()) {
                                            throw new Exception(message.endoffileError("syntaticAnalyze", token));
                                        }
                                    } else {
                                        throw new Exception(message.dotError("syntaticAnalyze", token));
                                    }

                                } else {
                                    throw new Exception(message.semicolonError("syntaticAnalyze", token));
                                }

                            } else {
                                throw new Exception();
                            }

                        } else {
                            throw new Exception(message.identifierError("syntaticAnalyze", token));
                        }

                    } else {
                        throw new Exception();
                    }

                } else {
                    throw new Exception(message.programError("syntaticAnalyze", token));
                }

            } else {
                throw new Exception();
            }

        } catch (Exception e) {
            if (e.getMessage() != null) {
                System.out.println(e.getMessage());
            }

            System.out.println("[syntaticAnalyze] | Error has ocurred");
            System.out.println("[syntaticAnalyze] | Ending compilation process");
        }

        System.out.println("\n[syntaticAnalyze] | Symbol Table:");
        table.printTable();
        symbolProgram = null;
    }

    private void analyzeBlock(String scope) throws Exception {
        System.out.println("[analyzeBlock] | ");
        token = lexicalAnalyzer.lexicalAnalyze(path);
        token.print();

        if (!isEmpty(token)) {
            analyzeVariablesDeclaration(scope);
            analyzeSubRoutineDeclaration(scope);
            analyzeCommands();
        } else {
            throw new Exception();
        }
    }

    /*
     *  Analise Relacionada a Variaveis
     */
    private void analyzeVariablesDeclaration(String scope) throws Exception { 
        System.out.println("[analyzeVariablesDeclaration] | ");
        if (token.getSymbol().equals("svar")) {
            token = lexicalAnalyzer.lexicalAnalyze(path);
            token.print();

            if (!isEmpty(token)) {
                if (token.getSymbol().equals("sidentificador")) {
                    while (token.getSymbol().equals("sidentificador")) {
                        analyzeVariables(scope);

                        if (token.getSymbol().equals("sponto_vírgula")) {
                            token = lexicalAnalyzer.lexicalAnalyze(path);
                            token.print();

                            if (isEmpty(token)) {
                                throw new Exception();
                            }

                        } else {
                            throw new Exception(message.semicolonError("analyzeVariablesDeclaration", token));
                        }
                    }

                } else {
                    throw new Exception(message.identifierError("analyzeVariablesDeclaration", token));
                }
            } else {
                throw new Exception();
            }
        }
    }

    private void analyzeVariables(String scope) throws Exception {
        System.out.println("[analyzeVariables] | ");
        while (!token.getSymbol().equals("sdoispontos")) {
            Variable symbolVariable = new Variable();

            if (token.getSymbol().equals("sidentificador")) {
                symbolVariable.setLexemeName(token.getLexeme());
                symbolVariable.setScope(scope);
                //Pesquisa_duplicvar_ tabela(token.lexema)
                //se não encontrou duplicidade
                table.insertSymbol(symbolVariable);

                token = lexicalAnalyzer.lexicalAnalyze(path);
                token.print();

                if (!isEmpty(token)) {
                    if (token.getSymbol().equals("svirgula") || token.getSymbol().equals("sdoispontos")) {
                        if (token.getSymbol().equals("svirgula")) {

                            token = lexicalAnalyzer.lexicalAnalyze(path);
                            token.print();

                            if (!isEmpty(token)) {
                                if (token.getSymbol().equals("sdoispontos")) {
                                    throw new Exception(message.colonError("analyzeVariables", token));
                                }

                            } else {
                                throw new Exception();
                            }
                        }
                    }

                } else {
                    throw new Exception();
                }
            }
            symbolVariable = null;
        }

        token = lexicalAnalyzer.lexicalAnalyze(path);
        token.print();

        if (!isEmpty(token)) {
            analyzeType();
        } else {
            throw new Exception();
        }
    }

    /*
     *  Analise Relacionada a SubRotinas
     */
    private void analyzeSubRoutineDeclaration(String scope) throws Exception {
        int flag = 0;
        
        System.out.println("[analyzeSubRoutineDeclaration] | ");
        if (token.getSymbol().equals("sprocedimento") || token.getSymbol().equals("sfuncao")) {
            //auxrot:= rotulo
            //GERA(´ ´,JMP,rotulo,´ ´) {Salta sub-rotinas}
            // rotulo:= rotulo + 1 
            flag = 1;
        }

        while (token.getSymbol().equals("sprocedimento") || token.getSymbol().equals("sfuncao")) {
            if (token.getSymbol().equals("sprocedimento")) {
                analyzeProcedureDeclaration(scope);
            } else {
                analyzeFunctionDeclaration(scope);
            }

            if (token.getSymbol().equals("sponto_vírgula")) {
                token = lexicalAnalyzer.lexicalAnalyze(path);
                token.print();

                if (isEmpty(token)) {
                    throw new Exception();
                }

            } else {
                throw new Exception(message.semicolonError("analyzeSubRoutineDeclaration", token));
            }
        }

        if (flag == 1) {
            //Gera(auxrot,NULL,´ ´,´ ´) {início do principal} 
        }
    }

    private void analyzeProcedureDeclaration(String scope) throws Exception {
        String scopeProcedure;
        ProcedureProgram symbolProcedure = new ProcedureProgram();

        System.out.println("[analyzeProcedureDeclaration] | ");
        token = lexicalAnalyzer.lexicalAnalyze(path);
        token.print();

        if (!isEmpty(token)) {
            if (token.getSymbol().equals("sidentificador")) {
                symbolProcedure.setLexemeName(token.getLexeme());
                symbolProcedure.setScope(scope);
                //Pesquisa
                //Inserir tabela de símbolos
                table.insertSymbol(symbolProcedure);
                //Gera rótulo

                scopeProcedure = token.getLexeme();
                token = lexicalAnalyzer.lexicalAnalyze(path);
                token.print();

                if (!isEmpty(token)) {
                    if (token.getSymbol().equals("sponto_vírgula")) {
                        analyzeBlock(scopeProcedure);
                    } else {
                        throw new Exception(message.semicolonError("analyzeProcedureDeclaration", token));
                    }

                } else {
                    throw new Exception();
                }

            } else {
                throw new Exception(message.identifierError("analyzeProcedureDeclaration", token));
            }

        } else {
            throw new Exception();
        }

        symbolProcedure = null;
        //Desempilha e volta nível
    }

    private void analyzeProcedureCall() throws Exception {
        System.out.println("[analyzeProcedureCall] | ");
        token = lexicalAnalyzer.lexicalAnalyze(path);
        token.print();

        if (!isEmpty(token)) {

        } else {
            throw new Exception();
        }
    }

    private void analyzeFunctionDeclaration(String scope) throws Exception {
        String scopeFunction;
        Function symbolFunction = new Function();

        System.out.println("[analyzeFunctionDeclaration] | ");
        token = lexicalAnalyzer.lexicalAnalyze(path);
        token.print();

        if (!isEmpty(token)) {
            //Label
            if (token.getSymbol().equals("sidentificador")) {
                symbolFunction.setLexemeName(token.getLexeme());
                symbolFunction.setScope(scope);
                //pesquisa
                //insere tabela
                table.insertSymbol(symbolFunction);

                scopeFunction = token.getLexeme();
                token = lexicalAnalyzer.lexicalAnalyze(path);
                token.print();

                if (!isEmpty(token)) {
                    if (token.getSymbol().equals("sdoispontos")) {
                        token = lexicalAnalyzer.lexicalAnalyze(path);
                        token.print();

                        if (!isEmpty(token)) {
                            if (token.getSymbol().equals("sinteiro") || token.getSymbol().equals("sbooleano")) {
                                if (token.getSymbol().equals("sinteiro")) {
                                    //tabela de símbolo tipo = função inteiro
                                    //table.setTypeSymbols(token.getLexeme());
                                } else {
                                    //função boolean
                                }

                                token = lexicalAnalyzer.lexicalAnalyze(path);
                                token.print();

                                if (!isEmpty(token)) {
                                    if (token.getSymbol().equals("sponto_vírgula")) {
                                        analyzeBlock(scopeFunction);
                                    } else {
                                        throw new Exception(message.semicolonError("analyzeFunctionDeclaration", token));
                                    }

                                } else {
                                    throw new Exception();
                                }

                            } else {
                                throw new Exception(message.typeError("analyzeFunctionDeclaration", token));
                            }

                        } else {
                            throw new Exception();
                        }

                    } else {
                        throw new Exception(message.colonError("analyzeFunctionDeclaration", token));
                    }

                } else {
                    throw new Exception();
                }

            } else {
                throw new Exception(message.identifierError("analyzeFunctionDeclaration", token));
            }

        } else {
            throw new Exception();
        }

        symbolFunction = null;
        //Desempilha u volta nível
    }

    private void analyzeFunctionCall() throws Exception {
        System.out.println("[analyzeFunctionCall] | ");
        token = lexicalAnalyzer.lexicalAnalyze(path);
        token.print();

        if (!isEmpty(token)) {

        } else {
            throw new Exception();
        }
    }

    /*
     *  Analisa de Comandos
     */
    private void analyzeCommands() throws Exception {
        System.out.println("[analyzeCommands] | ");
        if (token.getSymbol().equals("sinício")) {
            token = lexicalAnalyzer.lexicalAnalyze(path);
            token.print();

            if (!isEmpty(token)) {
                analyzeCommand();

                while (!token.getSymbol().equals("sfim")) {
                    if (token.getSymbol().equals("sponto_vírgula")) {
                        token = lexicalAnalyzer.lexicalAnalyze(path);
                        token.print();

                        if (!isEmpty(token)) {
                            if (!token.getSymbol().equals("sfim")) {
                                analyzeCommand();
                            }

                        } else {
                            throw new Exception();
                        }

                    } else {
                        throw new Exception(message.semicolonError("analyzeCommands", token));
                    }
                }
                
                token = lexicalAnalyzer.lexicalAnalyze(path);
                
            } else {
                throw new Exception();
            }

        } else {
            throw new Exception(message.beginError("analyzeCommands", token));
        }
    }

    private void analyzeCommand() throws Exception {
        System.out.println("[analyzeCommand] | ");
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

    private void analyzeAttrProcedure() throws Exception {
        System.out.println("[analyzeAttrProcedure] | ");
        token = lexicalAnalyzer.lexicalAnalyze(path);
        token.print();

        if (!isEmpty(token)) {

            if (token.getSymbol().equals("satribuição")) {
                analyzeAttribution();
            } else {
                //analyzeProcedureCall();
            }
        } else {
            throw new Exception();
        }
    }

    private void analyzeRead() throws Exception {
        System.out.println("[analyzeRead] | ");
        token = lexicalAnalyzer.lexicalAnalyze(path);
        token.print();

        if (!isEmpty(token)) {
            if (token.getSymbol().equals("sabre_parênteses")) {
                token = lexicalAnalyzer.lexicalAnalyze(path);
                token.print();

                if (!isEmpty(token)) {
                    if (token.getSymbol().equals("sidentificador")) {
                        // se pesquisa tabela simbolos var
                        token = lexicalAnalyzer.lexicalAnalyze(path);
                        token.print();

                        if (!isEmpty(token)) {
                            if (token.getSymbol().equals("sfecha_parênteses")) {
                                token = lexicalAnalyzer.lexicalAnalyze(path);
                                token.print();

                                if (isEmpty(token)) {
                                    throw new Exception();
                                }

                            } else {
                                throw new Exception(message.closeparenthesesError("analyzeRead", token));
                            }
                        }
                        // senao throw new SyntacticException();
                    } else {
                        throw new Exception(message.identifierError("analyzeRead", token));
                    }

                } else {
                    throw new Exception();
                }

            } else {
                throw new Exception(message.openparenthesesError("analyzeRead", token));
            }

        } else {
            throw new Exception();
        }
    }

    private void analyzeWrite() throws Exception {
        System.out.println("[analyzeWrite] | ");
        token = lexicalAnalyzer.lexicalAnalyze(path);
        token.print();

        if (token.getSymbol().equals("sabre_parênteses")) {
            token = lexicalAnalyzer.lexicalAnalyze(path);
            token.print();

            if (token.getSymbol().equals("sidentificador")) {
                // se pesquisa tabela simbolos function
                token = lexicalAnalyzer.lexicalAnalyze(path);
                token.print();

                if (token.getSymbol().equals("sfecha_parênteses")) {
                    token = lexicalAnalyzer.lexicalAnalyze(path);
                    token.print();

                    if (isEmpty(token)) {
                        throw new Exception();
                    }

                } else {
                    throw new Exception(message.closeparenthesesError("analyzeWrite", token));
                }
                // senao throw new SyntacticException();
            } else {
                throw new Exception(message.identifierError("analyzeWrite", token));
            }

        } else {
            throw new Exception(message.openparenthesesError("analyzeWrite", token));
        }
    }

    private void analyzeWhile() throws Exception {    
        System.out.println("[analyzeWhile] | ");
        //Rotulos definição
        //Gera
        // rotulo = rotulo + 1
        token = lexicalAnalyzer.lexicalAnalyze(path);
        token.print();

        if (!isEmpty(token)) {
            analyzeExpressions();

            if (token.getSymbol().equals("sfaca")) {
                //rot2 = rotulo
                // gera
                // rotulo = rotulo + 1
                token = lexicalAnalyzer.lexicalAnalyze(path);
                token.print();

                if (!isEmpty(token)) {
                    analyzeCommand();
                    //Gera
                    //Gera
                } else {
                    throw new Exception();
                }

            } else {
                throw new Exception(message.doError("analyzeWhile", token));
            }

        } else {
            throw new Exception();
        }
    }

    private void analyzeIf() throws Exception {
        System.out.println("[analyzeIf] | ");
        token = lexicalAnalyzer.lexicalAnalyze(path);
        token.print();

        if (!isEmpty(token)) {
            analyzeExpressions();

            if (token.getSymbol().equals("sentao")) {
                token = lexicalAnalyzer.lexicalAnalyze(path);
                token.print();

                if (!isEmpty(token)) {
                    analyzeCommand();

                    if (token.getSymbol().equals("ssenao")) {
                        token = lexicalAnalyzer.lexicalAnalyze(path);
                        token.print();

                        if (!isEmpty(token)) {
                            analyzeCommand();
                        } else {
                            throw new Exception();
                        }
                    }

                } else {
                    throw new Exception();
                }

            } else {
                throw new Exception(message.thenError("analyzeIf", token));
            }

        } else {
            throw new Exception();
        }
    }

    private void analyzeAttribution() throws Exception { 
        System.out.println("[analyzeAttribution] | ");
        token = lexicalAnalyzer.lexicalAnalyze(path);

        if (!isEmpty(token)) {
            analyzeExpressions();
        } else {
            throw new Exception();
        }
    }

    /*
     *  Analise Relacionado a Tipo, Termos e Expressões
     */
    private void analyzeType() throws Exception {    
        System.out.println("[analyzeType] | ");
        if (token.getSymbol().equals("sinteiro") || token.getSymbol().equals("sbooleano")) {
            //table.setTypeSymbols(token.getLexeme());
        } else {
            throw new Exception(message.typeError("analyzeType", token));
        }

        token = lexicalAnalyzer.lexicalAnalyze(path);
        token.print();

        if (isEmpty(token)) {
            throw new Exception();
        }
    }

    private void analyzeExpressions() throws Exception { 
        System.out.println("[analyzeExpressions] | ");
        analyzeExpression();

        if ((token.getSymbol().equals("smaior")) || (token.getSymbol().equals("smaiorig")) || (token.getSymbol().equals("sig")) || (token.getSymbol().equals("smenor")) || (token.getSymbol().equals("smenorig")) || (token.getSymbol().equals("sdif"))) {
            token = lexicalAnalyzer.lexicalAnalyze(path);
            token.print();

            if (!isEmpty(token)) {
                analyzeExpression();
            } else {
                throw new Exception();
            }
        }
    }

    private void analyzeExpression() throws Exception { 
        System.out.println("[analyzeExpression] | ");
        if (token.getSymbol().equals("smais") || token.getSymbol().equals("smenos")) {
            token = lexicalAnalyzer.lexicalAnalyze(path);
            token.print();

            if (isEmpty(token)) {
                throw new Exception();
            }
        }
        analyzeTerm();

        while (token.getSymbol().equals("smais") || token.getSymbol().equals("smenos") || token.getSymbol().equals("sou")) {
            token = lexicalAnalyzer.lexicalAnalyze(path);
            token.print();

            if (!isEmpty(token)) {
                analyzeTerm();
            } else {
                throw new Exception();
            }
        }
    }

    private void analyzeTerm() throws Exception {     
        System.out.println("[analyzeTerm] | ");
        analyzeFactor();

        while ((token.getSymbol().equals("smult")) || (token.getSymbol().equals("sdiv")) || (token.getSymbol().equals("se"))) {
            token = lexicalAnalyzer.lexicalAnalyze(path);
            token.print();

            if (!isEmpty(token)) {
                analyzeFactor();
            } else {
                throw new Exception();
            }
        }
    }

    private void analyzeFactor() throws Exception {       
        System.out.println("[analyzeFactor] | ");
        if (token.getSymbol().equals("sidentificador")) {
            //Se pesquisa_tabela(token.lexema,nível,ind)
            //Então Se (TabSimb[ind].tipo = “função inteiro”) ou (TabSimb[ind].tipo = “função booleano”)
            //analyzeFunctionCall();
            //Senão token = lexicalAnalyzer.lexicalAnalyzer(path);
            
            token = lexicalAnalyzer.lexicalAnalyze(path);
            token.print();
            // else throw new SyntacticException();

        } else if (token.getSymbol().equals("snumero")) {
            token = lexicalAnalyzer.lexicalAnalyze(path);
            token.print();

            if (isEmpty(token)) {
                throw new Exception();
            }

        } else if (token.getSymbol().equals("snao")) {
            token = lexicalAnalyzer.lexicalAnalyze(path);
            token.print();

            if (!isEmpty(token)) {
                analyzeFactor();
            } else {
                throw new Exception();
            }

        } else if (token.getSymbol().equals("sabre_parênteses")) {
            token = lexicalAnalyzer.lexicalAnalyze(path);
            token.print();

            if (!isEmpty(token)) {
                analyzeExpressions();

                if (token.getSymbol().equals("sfecha_parênteses")) {
                    token = lexicalAnalyzer.lexicalAnalyze(path);
                    token.print();

                    if (isEmpty(token)) {
                        throw new Exception();
                    }

                } else {
                    throw new Exception(message.closeparenthesesError("analyzeFactor", token));
                }
            } else {
                throw new Exception();
            }

        } else if (token.getSymbol().equals("sverdadeiro") || token.getSymbol().equals("sfalso")) {
            token = lexicalAnalyzer.lexicalAnalyze(path);
            token.print();

            if (isEmpty(token)) {
                throw new Exception();
            }

        } else {
            throw new Exception(message.booleanError("analyzeFactor", token));
        }
    }
}
