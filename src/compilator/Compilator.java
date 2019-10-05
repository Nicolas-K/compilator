package compilator;

import java.util.Scanner;

public class Compilator {

    static Scanner scanf = new Scanner(System.in);

    public static void main(String[] args) {
        SyntacticAnalyzer syntatic = SyntacticAnalyzer.getInstance();
        System.out.println("Insert file's path: ");
        syntatic.receiveFilePath(scanf.nextLine());
        syntatic.syntaticAnalyze();
    }
}
