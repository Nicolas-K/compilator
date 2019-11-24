package compilator;

import java.util.Scanner;

public class Compilator {
    static Scanner scanf = new Scanner(System.in);

    public static void main(String[] args) {
        boolean sucess;
        SyntacticAnalyzer syntaticAnalyzer = SyntacticAnalyzer.getInstance();
        CodeGenerator generator = CodeGenerator.getInstance();
                
        System.out.println("Insert file's path: ");
        syntaticAnalyzer.receiveFilePath(scanf.nextLine());
        sucess = syntaticAnalyzer.syntaticAnalyze();
        
        if(!sucess) {
            System.out.println(syntaticAnalyzer.getError());
        } else {
            generator.getCodeText();
        }
    }
}
