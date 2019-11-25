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
    private String error;
    private String lexicalError;

    public static SyntacticAnalyzer getInstance() {
        if (instance == null) {
            instance = new SyntacticAnalyzer();
        }
        return instance;
    }

    private SyntacticAnalyzer() {
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
     *  Ocorrencia de erro
     */
    private boolean isEmpty(Token receivedToken) {
        if (receivedToken == null) {
            return true;
        }
        return false;
    }

    private void setError(String error) {
        this.error = error;
    }

    public String getError() {
        return this.error;
    }

    /*
     *  Analisador Sintatico
     */
    public boolean syntaticAnalyze() {
        int programVarCount;
        String scopeProgram;
        ProcedureProgram symbolProgram = new ProcedureProgram();

        try {
            token = lexicalAnalyzer.lexicalAnalyze(path);

            if (!isEmpty(token)) {
                if (token.getSymbol().equals("sprograma")) {
                    codeGenerator.createStart();
                    token = lexicalAnalyzer.lexicalAnalyze(path);

                    if (!isEmpty(token)) {
                        if (token.getSymbol().equals("sidentificador")) {
                            symbolProgram.setLexemeName(token.getLexeme());
                            symbolProgram.setScope("");
                            symbolProgram.setLabel(label);
                            table.insertSymbol(symbolProgram);

                            scopeProgram = token.getLexeme();
                            token = lexicalAnalyzer.lexicalAnalyze(path);

                            if (!isEmpty(token)) {
                                if (token.getSymbol().equals("sponto_vírgula")) {
                                    programVarCount = analyzeBlock(scopeProgram);

                                    if (token.getSymbol().equals("sponto")) {
                                        token = lexicalAnalyzer.lexicalAnalyze(path);

                                        if (!lexicalAnalyzer.hasFileEnd()) {
                                            throw new Exception(message.syntaticError(token));

                                        } else {
                                            if (programVarCount != 0) {
                                                codeGenerator.createDALLOC(codeGenerator.getVariablePosition() + " ", programVarCount);
                                            }
                                            semanticAnalyzer.unstackSymbols(scopeProgram);
                                            codeGenerator.createHLT();
                                            return true;
                                        }
                                    } else {
                                        throw new Exception(message.syntaticError(token));
                                    }

                                } else {
                                    throw new Exception(message.syntaticError(token));
                                }

                            } else {
                                lexicalError = lexicalAnalyzer.getErrorMessage();
                                throw new Exception(lexicalError);
                            }

                        } else {
                            throw new Exception(message.syntaticError(token));
                        }

                    } else {
                        lexicalError = lexicalAnalyzer.getErrorMessage();
                        throw new Exception(lexicalError);
                    }

                } else {
                    throw new Exception(message.syntaticError(token));
                }

            } else {
                lexicalError = lexicalAnalyzer.getErrorMessage();
                throw new Exception(lexicalError);
            }

        } catch (Exception e) {
            if (e.getMessage() != null) {
                this.setError(e.getMessage());
            } else {
                setError("[ Syntatic Error ] | Something unexpected happened");
            }
        }

        symbolProgram = null;
        return false;
    }

    private int analyzeBlock(String scope) throws Exception {
        int varDeclarationCount = 0;

        token = lexicalAnalyzer.lexicalAnalyze(path);
        token.print();

        if (!isEmpty(token)) {
            analyzeVariablesDeclaration(scope);
            varDeclarationCount = codeGenerator.getVariableCount();
            codeGenerator.resetVariableCount();

            codeGenerator.setVariablePostion(varDeclarationCount);
            analyzeSubRoutineDeclaration(scope);
            analyzeCommands(scope);

        } else {
            lexicalError = lexicalAnalyzer.getErrorMessage();
            throw new Exception(lexicalError);
        }

        return varDeclarationCount;
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
                                lexicalError = lexicalAnalyzer.getErrorMessage();
                                throw new Exception(lexicalError);
                            }

                        } else {
                            throw new Exception(message.syntaticError(token));
                        }
                    }

                } else {
                    throw new Exception(message.syntaticError(token));
                }
            } else {
                lexicalError = lexicalAnalyzer.getErrorMessage();
                throw new Exception(lexicalError);
            }
        }
    }

    private void analyzeVariables(String scope) throws Exception {
        int countVariable = 0;

        while (!token.getSymbol().equals("sdoispontos")) {
            Variable symbolVariable = new Variable();

            if (token.getSymbol().equals("sidentificador")) {
                symbolVariable.setLexemeName(token.getLexeme());
                symbolVariable.setScope(scope);

                if (!semanticAnalyzer.searchVariableDuplicate(symbolVariable.getLexemeName(), symbolVariable.getScope())) {
                    countVariable++;
                    codeGenerator.increaseVariableCount();
                    table.insertSymbol(symbolVariable);
                    token = lexicalAnalyzer.lexicalAnalyze(path);

                    if (!isEmpty(token)) {
                        if (token.getSymbol().equals("svirgula") || token.getSymbol().equals("sdoispontos")) {
                            if (token.getSymbol().equals("svirgula")) {
                                token = lexicalAnalyzer.lexicalAnalyze(path);

                                if (!isEmpty(token)) {
                                    if (token.getSymbol().equals("sdoispontos")) {
                                        throw new Exception(message.syntaticError(token));
                                    }

                                } else {
                                    lexicalError = lexicalAnalyzer.getErrorMessage();
                                    throw new Exception(lexicalError);
                                }
                            }
                        }

                    } else {
                        lexicalError = lexicalAnalyzer.getErrorMessage();
                        throw new Exception(lexicalError);
                    }
                } else {
                    throw new Exception(message.duplicateError("Variable", token));
                }

            }
            symbolVariable = null;
        }
        codeGenerator.createALLOC(codeGenerator.getVariablePosition() + " ", countVariable);

        token = lexicalAnalyzer.lexicalAnalyze(path);

        if (!isEmpty(token)) {
            analyzeType();
        } else {
            lexicalError = lexicalAnalyzer.getErrorMessage();
            throw new Exception(lexicalError);
        }
    }

    /*
     *  Analise Relacionada a SubRotinas
     */
    private void analyzeSubRoutineDeclaration(String scope) throws Exception {
        int auxlabel = 0, flag = 0;

        if (token.getSymbol().equals("sprocedimento") || token.getSymbol().equals("sfuncao")) {
            auxlabel = label;
            codeGenerator.createJMP("L" + auxlabel);
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
                    lexicalError = lexicalAnalyzer.getErrorMessage();
                    throw new Exception(lexicalError);
                }

            } else {
                throw new Exception(message.syntaticError(token));
            }
        }

        if (flag == 1) {
            codeGenerator.createNULL("L" + auxlabel + " ");
        }
    }

    private void analyzeProcedureDeclaration(String scope) throws Exception {
        String scopeProcedure;
        int auxlabel = label, procedureVarCount;
        ProcedureProgram symbolProcedure = new ProcedureProgram();

        token = lexicalAnalyzer.lexicalAnalyze(path);

        if (!isEmpty(token)) {
            if (token.getSymbol().equals("sidentificador")) {
                symbolProcedure.setLexemeName(token.getLexeme());
                symbolProcedure.setScope(scope);

                if (!semanticAnalyzer.searchProcedureDuplicate(symbolProcedure.getLexemeName())) {
                    symbolProcedure.setLabel(auxlabel);
                    table.insertSymbol(symbolProcedure);

                    codeGenerator.createNULL("L" + auxlabel + " ");
                    label = label + 1;

                    scopeProcedure = token.getLexeme();
                    token = lexicalAnalyzer.lexicalAnalyze(path);

                    if (!isEmpty(token)) {
                        if (token.getSymbol().equals("sponto_vírgula")) {
                            procedureVarCount = analyzeBlock(scopeProcedure);

                            if (procedureVarCount != 0) {
                                codeGenerator.resetVariablePosition(procedureVarCount);
                                codeGenerator.createDALLOC(codeGenerator.getVariablePosition() + " ", procedureVarCount);
                            }

                            codeGenerator.createRETURN();

                        } else {
                            throw new Exception(message.syntaticError(token));
                        }

                    } else {
                        lexicalError = lexicalAnalyzer.getErrorMessage();
                        throw new Exception(lexicalError);
                    }
                } else {
                    throw new Exception(message.duplicateError("Procedure", token));
                }

            } else {
                throw new Exception(message.syntaticError(token));
            }

        } else {
            lexicalError = lexicalAnalyzer.getErrorMessage();
            throw new Exception(lexicalError);
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
            codeGenerator.createCALL("L" + auxlabel);

        } else {
            throw new Exception(message.identifierUsageError(buffer));
        }
    }

    private void analyzeFunctionDeclaration(String scope) throws Exception {
        String scopeFunction;
        int auxlabel = label, functionVarCount;
        Function symbolFunction = new Function();

        token = lexicalAnalyzer.lexicalAnalyze(path);

        if (!isEmpty(token)) {
            if (token.getSymbol().equals("sidentificador")) {
                symbolFunction.setLexemeName(token.getLexeme());
                symbolFunction.setScope(scope);

                if (!semanticAnalyzer.searchFunctionDuplicate(symbolFunction.getLexemeName())) {
                    symbolFunction.setLabel(auxlabel);
                    symbolFunction.setReturnFunction(false);
                    table.insertSymbol(symbolFunction);
                    codeGenerator.createNULL("L" + auxlabel + " ");
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
                                            functionVarCount = analyzeBlock(scopeFunction);

                                            if (((Function) table.getSymbol(semanticAnalyzer.searchSymbolPos(scopeFunction))).getReturnFunction()) {
                                                if (functionVarCount > 0) {
                                                    codeGenerator.createRETURNF_DALLOC(codeGenerator.getVariablePosition() + " ", functionVarCount);
                                                    codeGenerator.resetVariablePosition(functionVarCount);
                                                } else {
                                                    codeGenerator.createRETURNF();
                                                }
                                            } else {
                                                throw new Exception(message.noReturnfError(scopeFunction));
                                            }

                                        } else {
                                            throw new Exception(message.syntaticError(token));
                                        }

                                    } else {
                                        lexicalError = lexicalAnalyzer.getErrorMessage();
                                        throw new Exception(lexicalError);
                                    }

                                } else {
                                    throw new Exception(message.syntaticError(token));
                                }

                            } else {
                                lexicalError = lexicalAnalyzer.getErrorMessage();
                                throw new Exception(lexicalError);
                            }

                        } else {
                            throw new Exception(message.syntaticError(token));
                        }

                    } else {
                        lexicalError = lexicalAnalyzer.getErrorMessage();
                        throw new Exception(lexicalError);
                    }

                } else {
                    throw new Exception(message.duplicateError("Function", token));
                }

            } else {
                throw new Exception(message.syntaticError(token));
            }

        } else {
            lexicalError = lexicalAnalyzer.getErrorMessage();
            throw new Exception(lexicalError);
        }

        semanticAnalyzer.unstackSymbols(scopeFunction);
        symbolFunction = null;
    }

    /*
     *  Analisa de Comandos
     */
    private void analyzeCommands(String scopeLevel) throws Exception {
        if (token.getSymbol().equals("sinício")) {
            token = lexicalAnalyzer.lexicalAnalyze(path);

            if (!isEmpty(token)) {
                analyzeCommand(scopeLevel);

                while (!token.getSymbol().equals("sfim")) {
                    if (token.getSymbol().equals("sponto_vírgula")) {
                        token = lexicalAnalyzer.lexicalAnalyze(path);

                        if (!isEmpty(token)) {
                            if (!token.getSymbol().equals("sfim")) {
                                analyzeCommand(scopeLevel);
                            }

                        } else {
                            lexicalError = lexicalAnalyzer.getErrorMessage();
                            throw new Exception(lexicalError);
                        }

                    } else {
                        throw new Exception(message.syntaticError(token));
                    }
                }

                token = lexicalAnalyzer.lexicalAnalyze(path);

                if (isEmpty(token)) {
                    lexicalError = lexicalAnalyzer.getErrorMessage();
                    throw new Exception(lexicalError);
                }

            } else {
                lexicalError = lexicalAnalyzer.getErrorMessage();
                throw new Exception(lexicalError);
            }

        } else {
            throw new Exception(message.syntaticError(token));
        }
    }

    private void analyzeCommand(String scopeLevel) throws Exception {
        int functionPos;

        functionPos = semanticAnalyzer.searchSymbolPos(scopeLevel);

        if (functionPos != -1 && semanticAnalyzer.instanceofSymbol(scopeLevel).equals("function")) {
            ((Function) table.getSymbol(functionPos)).setReturnFunction(false);
        }

        if (token.getSymbol().equals("sidentificador")) {
            analyzeAttrProcedure();
        } else if (token.getSymbol().equals("sse")) {
            analyzeIf(scopeLevel);
        } else if (token.getSymbol().equals("senquanto")) {
            analyzeWhile(scopeLevel);
        } else if (token.getSymbol().equals("sleia")) {
            analyzeRead();
        } else if (token.getSymbol().equals("sescreva")) {
            analyzeWrite();
        } else {
            analyzeCommands(scopeLevel);
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
                lexicalError = lexicalAnalyzer.getErrorMessage();
                throw new Exception(lexicalError);
            }
        } else {
            throw new Exception(message.identifierUsageError(buffer));
        }
    }

    private void analyzeRead() throws Exception {
        token = lexicalAnalyzer.lexicalAnalyze(path);

        if (!isEmpty(token)) {
            if (token.getSymbol().equals("sabre_parênteses")) {
                token = lexicalAnalyzer.lexicalAnalyze(path);

                if (!isEmpty(token)) {
                    if (token.getSymbol().equals("sidentificador")) {
                        buffer = token;

                        if (semanticAnalyzer.identifierUsage(token.getLexeme())) {
                            token = lexicalAnalyzer.lexicalAnalyze(path);

                            if (!isEmpty(token)) {
                                if (token.getSymbol().equals("sfecha_parênteses")) {
                                    token = lexicalAnalyzer.lexicalAnalyze(path);

                                    if (isEmpty(token)) {
                                        lexicalError = lexicalAnalyzer.getErrorMessage();
                                        throw new Exception(lexicalError);

                                    } else {
                                        codeGenerator.createRD();
                                        codeGenerator.createSTR(codeGenerator.getVariablePosition() - semanticAnalyzer.countVariable(buffer.getLexeme()) - 1);
                                    }

                                } else {
                                    throw new Exception(message.syntaticError(token));
                                }
                            }
                        } else {
                            throw new Exception(message.identifierUsageError(token));
                        }

                    } else {
                        throw new Exception(message.syntaticError(token));
                    }

                } else {
                    lexicalError = lexicalAnalyzer.getErrorMessage();
                    throw new Exception(lexicalError);
                }

            } else {
                throw new Exception(message.syntaticError(token));
            }

        } else {
            lexicalError = lexicalAnalyzer.getErrorMessage();
            throw new Exception(lexicalError);
        }
    }

    private void analyzeWrite() throws Exception {
        int position, auxLabel;
        String typeSymbol;

        token = lexicalAnalyzer.lexicalAnalyze(path);

        if (!isEmpty(token)) {

            if (token.getSymbol().equals("sabre_parênteses")) {
                token = lexicalAnalyzer.lexicalAnalyze(path);

                if (token.getSymbol().equals("sidentificador")) {
                    buffer = token;

                    if (semanticAnalyzer.identifierUsage(buffer.getLexeme())) {
                        position = semanticAnalyzer.searchSymbolPos(buffer.getLexeme());
                        typeSymbol = semanticAnalyzer.instanceofSymbol(buffer.getLexeme());
                        auxLabel = ((Function) table.getSymbol(position)).getLabel();

                        token = lexicalAnalyzer.lexicalAnalyze(path);

                        if (token.getSymbol().equals("sfecha_parênteses")) {
                            token = lexicalAnalyzer.lexicalAnalyze(path);

                            if (isEmpty(token)) {
                                lexicalError = lexicalAnalyzer.getErrorMessage();
                                throw new Exception(lexicalError);

                            } else {
                                if (typeSymbol.equals("variable")) {
                                    codeGenerator.createLDV(codeGenerator.getVariablePosition() - semanticAnalyzer.countVariable(buffer.getLexeme()) - 1);
                                } else {
                                    codeGenerator.createCALL("L" + auxLabel);
                                }

                                codeGenerator.createPRN();
                            }

                        } else {
                            throw new Exception(message.syntaticError(token));
                        }
                    } else {
                        throw new Exception(message.identifierUsageError(token));
                    }

                } else {
                    throw new Exception(message.syntaticError(token));
                }

            } else {
                throw new Exception(message.syntaticError(token));
            }
        } else {
            lexicalError = lexicalAnalyzer.getErrorMessage();
            throw new Exception(lexicalError);
        }
    }

    private void analyzeWhile(String scopeLevel) throws Exception {
        int auxlabel1 = 0, auxlabel2 = 0;
        String typeExpression;

        auxlabel1 = label;
        codeGenerator.createNULL("L" + auxlabel1 + " ");
        label = label + 1;
        token = lexicalAnalyzer.lexicalAnalyze(path);

        if (!isEmpty(token)) {
            typeExpression = analyzeExpressions();

            if (typeExpression.equals("booleano")) {
                codeGenerator.postfixCreation(semanticAnalyzer.getPostfix(), semanticAnalyzer);
                semanticAnalyzer.clearPostfix();

                if (token.getSymbol().equals("sfaca")) {
                    auxlabel2 = label;
                    codeGenerator.createJMPF("L" + auxlabel2);
                    label = label + 1;
                    token = lexicalAnalyzer.lexicalAnalyze(path);

                    if (!isEmpty(token)) {
                        analyzeCommand(scopeLevel);
                        codeGenerator.createJMP("L" + auxlabel1);
                        codeGenerator.createNULL("L" + auxlabel2 + " ");

                    } else {
                        lexicalError = lexicalAnalyzer.getErrorMessage();
                        throw new Exception(lexicalError);
                    }

                } else {
                    throw new Exception(message.syntaticError(token));
                }
            } else {
                throw new Exception(message.typeExpressionError("booleano"));
            }

        } else {
            lexicalError = lexicalAnalyzer.getErrorMessage();
            throw new Exception(lexicalError);
        }
    }

    private void analyzeIf(String scopeLevel) throws Exception {
        String typeExpression;
        token = lexicalAnalyzer.lexicalAnalyze(path);

        if (!isEmpty(token)) {
            typeExpression = analyzeExpressions();

            if (typeExpression.equals("booleano")) {
                codeGenerator.postfixCreation(semanticAnalyzer.getPostfix(), semanticAnalyzer);
                semanticAnalyzer.clearPostfix();

                if (token.getSymbol().equals("sentao")) {
                    token = lexicalAnalyzer.lexicalAnalyze(path);

                    if (!isEmpty(token)) {
                        analyzeCommand(scopeLevel);

                        if (token.getSymbol().equals("ssenao")) {
                            token = lexicalAnalyzer.lexicalAnalyze(path);

                            if (!isEmpty(token)) {
                                analyzeCommand(scopeLevel);
                            } else {
                                lexicalError = lexicalAnalyzer.getErrorMessage();
                                throw new Exception(lexicalError);
                            }
                        }

                    } else {
                        lexicalError = lexicalAnalyzer.getErrorMessage();
                        throw new Exception(lexicalError);
                    }

                } else {
                    throw new Exception(message.syntaticError(token));
                }
            } else {
                throw new Exception(message.typeExpressionError("booleano"));
            }

        } else {
            lexicalError = lexicalAnalyzer.getErrorMessage();
            throw new Exception(lexicalError);
        }
    }

    private void analyzeAttribution() throws Exception {
        String typeExpression, typeAttr, symbolLexeme;
        int positionSymbol;

        symbolLexeme = buffer.getLexeme();
        positionSymbol = semanticAnalyzer.searchSymbolPos(symbolLexeme);

        if (semanticAnalyzer.instanceofSymbol(symbolLexeme).equals("variable")
                || semanticAnalyzer.instanceofSymbol(symbolLexeme).equals("function")) {

            if (semanticAnalyzer.instanceofSymbol(symbolLexeme).equals("function")) {
                ((Function) table.getSymbol(positionSymbol)).setReturnFunction(true);
            }

            typeAttr = table.getSymbolType(semanticAnalyzer.searchSymbolPos(buffer.getLexeme()));
            token = lexicalAnalyzer.lexicalAnalyze(path);

            if (!isEmpty(token)) {
                typeExpression = analyzeExpressions();

                if (!typeAttr.equals(typeExpression)) {
                    throw new Exception(message.typeExpressionError(typeAttr));
                } else {
                    codeGenerator.postfixCreation(semanticAnalyzer.getPostfix(), semanticAnalyzer);
                    semanticAnalyzer.clearPostfix();
                }

            } else {
                lexicalError = lexicalAnalyzer.getErrorMessage();
                throw new Exception(lexicalError);
            }
        } else {
            throw new Exception(message.wrongUsageSymbol(buffer));
        }
    }

    /*
     *  Analise Relacionado a Tipo, Termos e Expressões
     */
    private void analyzeType() throws Exception {
        if (token.getSymbol().equals("sinteiro") || token.getSymbol().equals("sbooleano")) {
            semanticAnalyzer.setTypeVariable(token.getLexeme());
        } else {
            throw new Exception(message.syntaticError(token));
        }

        token = lexicalAnalyzer.lexicalAnalyze(path);

        if (isEmpty(token)) {
            lexicalError = lexicalAnalyzer.getErrorMessage();
            throw new Exception(lexicalError);
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
                lexicalError = lexicalAnalyzer.getErrorMessage();
                throw new Exception(lexicalError);
            }
        }

        semanticAnalyzer.postfixStackHandler(-2);
        semanticAnalyzer.printPostfix();
        return semanticAnalyzer.postfixTypeHandler();
    }

    private void analyzeExpression() throws Exception {
        if (token.getSymbol().equals("smais") || token.getSymbol().equals("smenos")) {
            buffer = token;
            semanticAnalyzer.postfixStackHandler(6);
            semanticAnalyzer.addToStack(new Operator(buffer, 6));
            token = lexicalAnalyzer.lexicalAnalyze(path);

            if (isEmpty(token)) {
                lexicalError = lexicalAnalyzer.getErrorMessage();
                throw new Exception(lexicalError);
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
                lexicalError = lexicalAnalyzer.getErrorMessage();
                throw new Exception(lexicalError);
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
                lexicalError = lexicalAnalyzer.getErrorMessage();
                throw new Exception(lexicalError);
            }
        }
    }

    private void analyzeFactor() throws Exception {
        String typeExpression;

        if (token.getSymbol().equals("sidentificador")) {
            if (semanticAnalyzer.identifierUsage(token.getLexeme())) {
                buffer = token;
                semanticAnalyzer.addToPostfix(buffer.getLexeme());
                token = lexicalAnalyzer.lexicalAnalyze(path);

                if (isEmpty(token)) {
                    lexicalError = lexicalAnalyzer.getErrorMessage();
                    throw new Exception(lexicalError);
                }

            } else {
                throw new Exception(message.identifierUsageError(token));
            }

        } else if (token.getSymbol().equals("snumero")) {
            buffer = token;
            semanticAnalyzer.addToPostfix(buffer.getLexeme());
            token = lexicalAnalyzer.lexicalAnalyze(path);

            if (isEmpty(token)) {
                lexicalError = lexicalAnalyzer.getErrorMessage();
                throw new Exception(lexicalError);
            }

        } else if (token.getSymbol().equals("snao")) {
            buffer = token;
            semanticAnalyzer.postfixStackHandler(6);
            semanticAnalyzer.addToStack(new Operator(buffer, 6));
            token = lexicalAnalyzer.lexicalAnalyze(path);

            if (!isEmpty(token)) {
                analyzeFactor();
            } else {
                lexicalError = lexicalAnalyzer.getErrorMessage();
                throw new Exception(lexicalError);
            }

        } else if (token.getSymbol().equals("sabre_parênteses")) {
            buffer = token;
            semanticAnalyzer.postfixStackHandler(0);
            semanticAnalyzer.addToStack(new Operator(buffer, 0));
            token = lexicalAnalyzer.lexicalAnalyze(path);

            if (!isEmpty(token)) {
                typeExpression = analyzeExpressions();

                if (token.getSymbol().equals("sfecha_parênteses")) {
                    buffer = token;
                    semanticAnalyzer.addToStack(new Operator(buffer, -1));
                    semanticAnalyzer.postfixStackHandler(-1);
                    token = lexicalAnalyzer.lexicalAnalyze(path);

                    if (isEmpty(token)) {
                        lexicalError = lexicalAnalyzer.getErrorMessage();
                        throw new Exception(lexicalError);
                    }

                } else {
                    throw new Exception(message.syntaticError(token));
                }
            } else {
                lexicalError = lexicalAnalyzer.getErrorMessage();
                throw new Exception(lexicalError);
            }

        } else if (token.getSymbol().equals("sverdadeiro") || token.getSymbol().equals("sfalso")) {
            buffer = token;
            semanticAnalyzer.addToPostfix(buffer.getLexeme());
            token = lexicalAnalyzer.lexicalAnalyze(path);

            if (isEmpty(token)) {
                lexicalError = lexicalAnalyzer.getErrorMessage();
                throw new Exception(lexicalError);
            }

        } else {
            throw new Exception(message.syntaticError(token));
        }
    }
}
