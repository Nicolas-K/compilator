package compilator;

import java.util.Scanner;

public class Compilator {

    static Scanner scanf = new Scanner(System.in);

    public static void main(String[] args) {
        SyntacticAnalyzer syntaticAnalyzer = SyntacticAnalyzer.getInstance();
        System.out.println("Insert file's path: ");
        syntaticAnalyzer.receiveFilePath(scanf.nextLine());
        syntaticAnalyzer.syntaticAnalyze();
    }
}
