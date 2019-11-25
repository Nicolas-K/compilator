package compilator;

import java.util.Scanner;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Compilator {

    static Scanner scanf = new Scanner(System.in);

    public static void main(String[] args) {
        boolean sucess;
        SyntacticAnalyzer syntaticAnalyzer = SyntacticAnalyzer.getInstance();
        CodeGenerator generator = CodeGenerator.getInstance();

        System.out.println("Insert file's path: ");
        syntaticAnalyzer.receiveFilePath(scanf.nextLine());
        sucess = syntaticAnalyzer.syntaticAnalyze();

        if (!sucess) {
            System.out.println(syntaticAnalyzer.getError());
        } else {
           
            try {
                BufferedWriter codeWriter = new BufferedWriter(new FileWriter("Codigo_Gerado"));
                codeWriter.write(generator.getCodeText());

                codeWriter.close();
            } catch (IOException e) {
            }
        }
    }
}
