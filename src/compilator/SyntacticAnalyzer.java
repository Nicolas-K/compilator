package compilator;

public class SyntacticAnalyzer {

    private static SyntacticAnalyzer instance = null;
    private String path;
    private int label;

    private final LexicalAnalyzer lexicalAnalyzer;
    private final SemanticAnalyzer semanticAnalyzer;
    private final CodeGenerator codeGenerator;

    private Token token;
    private Token buffer;
    private SymbolTable table;

    private final ErrorMessages message;

    public static SyntacticAnalyzer getInstance() {
        if (instance == null) {
            instance = new SyntacticAnalyzer();
        }
        return instance;
    }

    public SyntacticAnalyzer() {
        this.lexicalAnalyzer = LexicalAnalyzer.getInstance();
        this.semanticAnalyzer = SemanticAnalyzer.getInstance();
        this.codeGenerator = CodeGenerator.getInstance();

        this.message = ErrorMessages.getInstance();
        this.path = null;
        this.label = 1;

        this.token = null;
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

        try {
            token = lexicalAnalyzer.lexicalAnalyze(path);

            if (!isEmpty(token)) {
                if (token.getSymbol().equals("sprograma")) {
                    token = lexicalAnalyzer.lexicalAnalyze(path);

                    if (!isEmpty(token)) {
                        if (token.getSymbol().equals("sidentificador")) {
                            symbolProgram.setLexemeName(token.getLexeme());
                            symbolProgram.setScope("");
                            table.insertSymbol(symbolProgram);

                            scopeProgram = token.getLexeme();
                            token = lexicalAnalyzer.lexicalAnalyze(path);

                            if (!isEmpty(token)) {
                                if (token.getSymbol().equals("sponto_vírgula")) {
                                    analyzeBlock(scopeProgram);

                                    if (token.getSymbol().equals("sponto")) {

                                        token = lexicalAnalyzer.lexicalAnalyze(path);

                                        if (!lexicalAnalyzer.hasFileEnd()) {
                                            throw new Exception(message.syntaticError("syntaticAnalyze", token));
                                        }
                                    } else {
                                        throw new Exception(message.syntaticError("syntaticAnalyze", token));
                                    }

                                } else {
                                    throw new Exception(message.syntaticError("syntaticAnalyze", token));
                                }

                            } else {
                                throw new Exception();
                            }

                        } else {
                            throw new Exception(message.syntaticError("syntaticAnalyze", token));
                        }

                    } else {
                        throw new Exception();
                    }

                } else {
                    throw new Exception(message.syntaticError("syntaticAnalyze", token));
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

        symbolProgram = null;
    }

    private void analyzeBlock(String scope) throws Exception {
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
        if (token.getSymbol().equals("svar")) {
            token = lexicalAnalyzer.lexicalAnalyze(path);

            if (!isEmpty(token)) {
                if (token.getSymbol().equals("sidentificador")) {
                    while (token.getSymbol().equals("sidentificador")) {
                        analyzeVariables(scope);

                        if (token.getSymbol().equals("sponto_vírgula")) {
                            token = lexicalAnalyzer.lexicalAnalyze(path);

                            if (isEmpty(token)) {
                                throw new Exception();
                            }

                        } else {
                            throw new Exception(message.syntaticError("analyzeVariablesDeclaration", token));
                        }
                    }

                } else {
                    throw new Exception(message.syntaticError("analyzeVariablesDeclaration", token));
                }
            } else {
                throw new Exception();
            }
        }
    }

    private void analyzeVariables(String scope) throws Exception {
        while (!token.getSymbol().equals("sdoispontos")) {
            Variable symbolVariable = new Variable();

            if (token.getSymbol().equals("sidentificador")) {
                symbolVariable.setLexemeName(token.getLexeme());
                symbolVariable.setScope(scope);

                if (!semanticAnalyzer.searchVariableDuplicate(symbolVariable.getLexemeName(), symbolVariable.getScope())) {
                    table.insertSymbol(symbolVariable);
                    token = lexicalAnalyzer.lexicalAnalyze(path);

                    if (!isEmpty(token)) {
                        if (token.getSymbol().equals("svirgula") || token.getSymbol().equals("sdoispontos")) {
                            if (token.getSymbol().equals("svirgula")) {
                                token = lexicalAnalyzer.lexicalAnalyze(path);

                                if (!isEmpty(token)) {
                                    if (token.getSymbol().equals("sdoispontos")) {
                                        throw new Exception(message.syntaticError("analyzeVariables", token));
                                    }

                                } else {
                                    throw new Exception();
                                }
                            }
                        }

                    } else {
                        throw new Exception();
                    }
                } else {
                    throw new Exception(message.duplicateError("analyzeVariables", "Variable", token));
                }
            }

            symbolVariable = null;
        }

        token = lexicalAnalyzer.lexicalAnalyze(path);

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
        int auxlabel = 0, flag = 0;

        if (token.getSymbol().equals("sprocedimento") || token.getSymbol().equals("sfuncao")) {
            auxlabel = label;
            codeGenerator.createCode("", "JMP ", "L" + auxlabel, "");
            label = label + 1;
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

                if (isEmpty(token)) {
                    throw new Exception();
                }

            } else {
                throw new Exception(message.syntaticError("analyzeSubRoutineDeclaration", token));
            }
        }

        if (flag == 1) {
            codeGenerator.createCode("L" + auxlabel + " ", "NULL", "", "");
        }
    }

    private void analyzeProcedureDeclaration(String scope) throws Exception {
        String scopeProcedure;
        int auxlabel = label;
        ProcedureProgram symbolProcedure = new ProcedureProgram();

        token = lexicalAnalyzer.lexicalAnalyze(path);

        if (!isEmpty(token)) {
            if (token.getSymbol().equals("sidentificador")) {
                symbolProcedure.setLexemeName(token.getLexeme());
                symbolProcedure.setScope(scope);

                if (!semanticAnalyzer.searchProcedureDuplicate(symbolProcedure.getLexemeName())) {
                    symbolProcedure.setLabel(auxlabel);
                    table.insertSymbol(symbolProcedure);
                    codeGenerator.createCode("L" + auxlabel + " ", "NULL", "", "");
                    label = label + 1;

                    scopeProcedure = token.getLexeme();
                    token = lexicalAnalyzer.lexicalAnalyze(path);

                    if (!isEmpty(token)) {
                        if (token.getSymbol().equals("sponto_vírgula")) {
                            analyzeBlock(scopeProcedure);
                        } else {
                            throw new Exception(message.syntaticError("analyzeProcedureDeclaration", token));
                        }

                    } else {
                        throw new Exception();
                    }
                } else {
                    throw new Exception(message.duplicateError("analyzeProcedureDeclaration", "Procedure", token));
                }

            } else {
                throw new Exception(message.syntaticError("analyzeProcedureDeclaration", token));
            }

        } else {
            throw new Exception();
        }

        semanticAnalyzer.unstackSymbols(scopeProcedure);
        symbolProcedure = null;
    }

    private void analyzeProcedureCall() throws Exception {
        Symbol auxProcedure;
        int auxlabel;

        if (semanticAnalyzer.identifierUsage(buffer.getLexeme())) {
            auxProcedure = table.getSymbol(semanticAnalyzer.searchSymbolPos(buffer.getLexeme()));
            auxlabel = ((ProcedureProgram) auxProcedure).getLabel();
            codeGenerator.createCode("", "CALL ", "L" + auxlabel, "");
        } else {
            throw new Exception(message.identifierUsageError("analyzeProcedureCall", buffer));
        }
    }

    private void analyzeFunctionDeclaration(String scope) throws Exception {
        String scopeFunction;
        int auxlabel = label;

        Function symbolFunction = new Function();

        token = lexicalAnalyzer.lexicalAnalyze(path);

        if (!isEmpty(token)) {
            if (token.getSymbol().equals("sidentificador")) {
                symbolFunction.setLexemeName(token.getLexeme());
                symbolFunction.setScope(scope);

                if (!semanticAnalyzer.searchFunctionDuplicate(symbolFunction.getLexemeName())) {
                    symbolFunction.setLabel(auxlabel);
                    table.insertSymbol(symbolFunction);
                    codeGenerator.createCode("L" + auxlabel + " ", "NULL", "", "");
                    label = label + 1;

                    scopeFunction = token.getLexeme();
                    token = lexicalAnalyzer.lexicalAnalyze(path);

                    if (!isEmpty(token)) {
                        if (token.getSymbol().equals("sdoispontos")) {
                            token = lexicalAnalyzer.lexicalAnalyze(path);

                            if (!isEmpty(token)) {
                                if (token.getSymbol().equals("sinteiro") || token.getSymbol().equals("sbooleano")) {
                                    if (token.getSymbol().equals("sinteiro")) {
                                        semanticAnalyzer.setTypeFunction("inteiro");
                                    } else {
                                        semanticAnalyzer.setTypeFunction("booleano");
                                    }

                                    token = lexicalAnalyzer.lexicalAnalyze(path);

                                    if (!isEmpty(token)) {
                                        if (token.getSymbol().equals("sponto_vírgula")) {
                                            analyzeBlock(scopeFunction);
                                        } else {
                                            throw new Exception(message.syntaticError("analyzeFunctionDeclaration", token));
                                        }

                                    } else {
                                        throw new Exception();
                                    }

                                } else {
                                    throw new Exception(message.syntaticError("analyzeFunctionDeclaration", token));
                                }

                            } else {
                                throw new Exception();
                            }

                        } else {
                            throw new Exception(message.syntaticError("analyzeFunctionDeclaration", token));
                        }

                    } else {
                        throw new Exception();
                    }

                } else {
                    throw new Exception(message.duplicateError("analyzeFunctionDeclaration", "Function", token));
                }

            } else {
                throw new Exception(message.syntaticError("analyzeFunctionDeclaration", token));
            }

        } else {
            throw new Exception();
        }

        semanticAnalyzer.unstackSymbols(scopeFunction);
        symbolFunction = null;
    }

    private void analyzeFunctionCall() throws Exception {
        Symbol auxFunction;
        int auxlabel;

        auxFunction = table.getSymbol(semanticAnalyzer.searchSymbolPos(buffer.getLexeme()));
        auxlabel = ((Function) auxFunction).getLabel();
        codeGenerator.createCode("", "CALL ", "L" + auxlabel, "");
    }

    /*
     *  Analisa de Comandos
     */
    private void analyzeCommands() throws Exception {
        if (token.getSymbol().equals("sinício")) {
            token = lexicalAnalyzer.lexicalAnalyze(path);

            if (!isEmpty(token)) {
                analyzeCommand();

                while (!token.getSymbol().equals("sfim")) {
                    if (token.getSymbol().equals("sponto_vírgula")) {
                        token = lexicalAnalyzer.lexicalAnalyze(path);

                        if (!isEmpty(token)) {
                            if (!token.getSymbol().equals("sfim")) {
                                analyzeCommand();
                            }

                        } else {
                            throw new Exception();
                        }

                    } else {
                        throw new Exception(message.syntaticError("analyzeCommands", token));
                    }
                }

                token = lexicalAnalyzer.lexicalAnalyze(path);

            } else {
                throw new Exception();
            }

        } else {
            throw new Exception(message.syntaticError("analyzeCommands", token));
        }
    }

    private void analyzeCommand() throws Exception {
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
        buffer = token;

        if (semanticAnalyzer.identifierUsage(buffer.getLexeme())) {
            token = lexicalAnalyzer.lexicalAnalyze(path);

            if (!isEmpty(token)) {
                if (token.getSymbol().equals("satribuição")) {
                    analyzeAttribution();
                } else {
                    analyzeProcedureCall();
                }
            } else {
                throw new Exception();
            }
        } else {
            throw new Exception(message.identifierUsageError("analyzeAttrProcedure", buffer));
        }
    }

    private void analyzeRead() throws Exception {
        token = lexicalAnalyzer.lexicalAnalyze(path);

        if (!isEmpty(token)) {
            if (token.getSymbol().equals("sabre_parênteses")) {
                token = lexicalAnalyzer.lexicalAnalyze(path);

                if (!isEmpty(token)) {
                    if (token.getSymbol().equals("sidentificador")) {
                        if (semanticAnalyzer.identifierUsage(token.getLexeme())) {
                            token = lexicalAnalyzer.lexicalAnalyze(path);

                            if (!isEmpty(token)) {
                                if (token.getSymbol().equals("sfecha_parênteses")) {
                                    token = lexicalAnalyzer.lexicalAnalyze(path);

                                    if (isEmpty(token)) {
                                        throw new Exception();
                                    }

                                } else {
                                    throw new Exception(message.syntaticError("analyzeRead", token));
                                }
                            }
                        } else {
                            throw new Exception(message.identifierUsageError("analyzeRead", token));
                        }

                    } else {
                        throw new Exception(message.syntaticError("analyzeRead", token));
                    }

                } else {
                    throw new Exception();
                }

            } else {
                throw new Exception(message.syntaticError("analyzeRead", token));
            }

        } else {
            throw new Exception();
        }
    }

    private void analyzeWrite() throws Exception {
        token = lexicalAnalyzer.lexicalAnalyze(path);

        if (token.getSymbol().equals("sabre_parênteses")) {
            token = lexicalAnalyzer.lexicalAnalyze(path);

            if (token.getSymbol().equals("sidentificador")) {
                if (semanticAnalyzer.identifierUsage(token.getLexeme())) {
                    token = lexicalAnalyzer.lexicalAnalyze(path);

                    if (token.getSymbol().equals("sfecha_parênteses")) {
                        token = lexicalAnalyzer.lexicalAnalyze(path);

                        if (isEmpty(token)) {
                            throw new Exception();
                        }

                    } else {
                        throw new Exception(message.syntaticError("analyzeWrite", token));
                    }
                } else {
                    throw new Exception(message.identifierUsageError("analyzeWrite", token));
                }

            } else {
                throw new Exception(message.syntaticError("analyzeWrite", token));
            }

        } else {
            throw new Exception(message.syntaticError("analyzeWrite", token));
        }
    }

    private void analyzeWhile() throws Exception {
        int auxlabel1 = 0, auxlabel2 = 0;
        String typeExpression;

        auxlabel1 = label;
        codeGenerator.createCode("L" + auxlabel1 + " ", "NULL", "", "");
        label = label + 1;
        token = lexicalAnalyzer.lexicalAnalyze(path);

        if (!isEmpty(token)) {
            typeExpression = analyzeExpressions();

            if (typeExpression.equals("booleano")) {
                if (token.getSymbol().equals("sfaca")) {
                    auxlabel2 = label;
                    codeGenerator.createCode("", "JMPF ", "L" + auxlabel2, "");
                    label = label + 1;
                    token = lexicalAnalyzer.lexicalAnalyze(path);

                    if (!isEmpty(token)) {
                        analyzeCommand();
                        codeGenerator.createCode("", "JMP ", "L" + auxlabel1, "");
                        codeGenerator.createCode("L" + auxlabel2 + " ", "NULL", "", "");
                    } else {
                        throw new Exception();
                    }

                } else {
                    throw new Exception(message.syntaticError("analyzeWhile", token));
                }
            } else {
                throw new Exception();
            }

        } else {
            throw new Exception();
        }
    }

    private void analyzeIf() throws Exception {
        String typeExpression;
        token = lexicalAnalyzer.lexicalAnalyze(path);

        if (!isEmpty(token)) {
            typeExpression = analyzeExpressions();

            if (typeExpression.equals("booleano")) {
                if (token.getSymbol().equals("sentao")) {
                    token = lexicalAnalyzer.lexicalAnalyze(path);

                    if (!isEmpty(token)) {
                        analyzeCommand();

                        if (token.getSymbol().equals("ssenao")) {
                            token = lexicalAnalyzer.lexicalAnalyze(path);

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
                    throw new Exception(message.syntaticError("analyzeIf", token));
                }
            } else {
                throw new Exception();
            }

        } else {
            throw new Exception();
        }
    }

    private void analyzeAttribution() throws Exception {
        String typeExpression, typeAttr;

        if (semanticAnalyzer.instanceofSymbol(buffer.getLexeme()).equals("variable")) {
            typeAttr = table.getSymbolType(semanticAnalyzer.searchSymbolPos(buffer.getLexeme()));
            token = lexicalAnalyzer.lexicalAnalyze(path);

            if (!isEmpty(token)) {
                typeExpression = analyzeExpressions();

                if (!typeAttr.equals(typeExpression)) {
                    throw new Exception();
                }

            } else {
                throw new Exception();
            }
        } else {
            throw new Exception(message.wrongUsageSymbol("analyzeAttribution", buffer));
        }
    }

    /*
     *  Analise Relacionado a Tipo, Termos e Expressões
     */
    private void analyzeType() throws Exception {
        if (token.getSymbol().equals("sinteiro") || token.getSymbol().equals("sbooleano")) {
            semanticAnalyzer.setTypeVariable(token.getLexeme());
        } else {
            throw new Exception(message.syntaticError("analyzeType", token));
        }

        token = lexicalAnalyzer.lexicalAnalyze(path);

        if (isEmpty(token)) {
            throw new Exception();
        }
    }

    private String analyzeExpressions() throws Exception {
        analyzeExpression();

        if ((token.getSymbol().equals("smaior")) || (token.getSymbol().equals("smaiorig")) || (token.getSymbol().equals("sig")) || (token.getSymbol().equals("smenor")) || (token.getSymbol().equals("smenorig")) || (token.getSymbol().equals("sdif"))) {
            buffer = token;
            semanticAnalyzer.postfixStackHandler(3);
            semanticAnalyzer.addToStack(new Operator(buffer, 3));
            token = lexicalAnalyzer.lexicalAnalyze(path);

            if (!isEmpty(token)) {
                analyzeExpression();
            } else {
                throw new Exception();
            }
        }

        semanticAnalyzer.postfixStackHandler(-2);
        return semanticAnalyzer.postfixTypeHandler();
    }

    private void analyzeExpression() throws Exception {
        if (token.getSymbol().equals("smais") || token.getSymbol().equals("smenos")) {
            buffer = token;
            semanticAnalyzer.postfixStackHandler(6);
            semanticAnalyzer.addToStack(new Operator(buffer, 6));
            token = lexicalAnalyzer.lexicalAnalyze(path);

            if (isEmpty(token)) {
                throw new Exception();
            }
        }
        analyzeTerm();

        while (token.getSymbol().equals("smais") || token.getSymbol().equals("smenos") || token.getSymbol().equals("sou")) {
            buffer = token;

            if (buffer.getSymbol().equals("sou")) {
                semanticAnalyzer.postfixStackHandler(1);
                semanticAnalyzer.addToStack(new Operator(buffer, 1));
            } else {
                semanticAnalyzer.postfixStackHandler(4);
                semanticAnalyzer.addToStack(new Operator(buffer, 4));
            }

            token = lexicalAnalyzer.lexicalAnalyze(path);

            if (!isEmpty(token)) {
                analyzeTerm();
            } else {
                throw new Exception();
            }
        }
    }

    private void analyzeTerm() throws Exception {
        analyzeFactor();

        while ((token.getSymbol().equals("smult")) || (token.getSymbol().equals("sdiv")) || (token.getSymbol().equals("se"))) {
            buffer = token;

            if (buffer.getSymbol().equals("se")) {
                semanticAnalyzer.postfixStackHandler(2);
                semanticAnalyzer.addToStack(new Operator(buffer, 2));
            } else {
                semanticAnalyzer.postfixStackHandler(5);
                semanticAnalyzer.addToStack(new Operator(buffer, 5));
            }

            token = lexicalAnalyzer.lexicalAnalyze(path);

            if (!isEmpty(token)) {
                analyzeFactor();
            } else {
                throw new Exception();
            }
        }
    }

    private void analyzeFactor() throws Exception {
        if (token.getSymbol().equals("sidentificador")) {
            if (semanticAnalyzer.identifierUsage(token.getLexeme())) {
                buffer = token;
                semanticAnalyzer.addToPostfix(buffer.getLexeme());

                if (semanticAnalyzer.instanceofSymbol(token.getLexeme()).equals("function")) {
                    analyzeFunctionCall();
                } else {
                    token = lexicalAnalyzer.lexicalAnalyze(path);
                }
            } else {
                throw new Exception(message.identifierUsageError("analyzeFactor", token));
            }

        } else if (token.getSymbol().equals("snumero")) {
            buffer = token;
            semanticAnalyzer.addToPostfix(buffer.getLexeme());
            token = lexicalAnalyzer.lexicalAnalyze(path);

            if (isEmpty(token)) {
                throw new Exception();
            }

        } else if (token.getSymbol().equals("snao")) {
            buffer = token;
            semanticAnalyzer.postfixStackHandler(6);
            semanticAnalyzer.addToStack(new Operator(buffer, 6));
            token = lexicalAnalyzer.lexicalAnalyze(path);

            if (!isEmpty(token)) {
                analyzeFactor();
            } else {
                throw new Exception();
            }

        } else if (token.getSymbol().equals("sabre_parênteses")) {
            buffer = token;
            semanticAnalyzer.postfixStackHandler(0);
            semanticAnalyzer.addToStack(new Operator(buffer, 0));
            token = lexicalAnalyzer.lexicalAnalyze(path);

            if (!isEmpty(token)) {
                analyzeExpressions();

                if (token.getSymbol().equals("sfecha_parênteses")) {
                    buffer = token;
                    semanticAnalyzer.addToStack(new Operator(buffer, -1));
                    semanticAnalyzer.postfixStackHandler(-1);
                    token = lexicalAnalyzer.lexicalAnalyze(path);

                    if (isEmpty(token)) {
                        throw new Exception();
                    }

                } else {
                    throw new Exception(message.syntaticError("analyzeFactor", token));
                }
            } else {
                throw new Exception();
            }

        } else if (token.getSymbol().equals("sverdadeiro") || token.getSymbol().equals("sfalso")) {
            buffer = token;
            semanticAnalyzer.addToPostfix(buffer.getLexeme());
            token = lexicalAnalyzer.lexicalAnalyze(path);

            if (isEmpty(token)) {
                throw new Exception();
            }

        } else {
            throw new Exception(message.syntaticError("analyzeFactor", token));
        }
    }
}
