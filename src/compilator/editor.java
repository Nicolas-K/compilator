package compilator;

import java.awt.*;
import javax.swing.*;
import java.io.*;
import java.awt.event.*;
import javax.swing.plaf.metal.*;
import javax.swing.text.*;
import java.awt.BorderLayout;
import java.io.IOException;
import java.io.OutputStream;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import java.util.Scanner;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class editor extends JFrame implements ActionListener {

    String path = null;

    JTextArea codigo;
    JTextArea console;

    TextAreaOutputStream taOutputStream;

    //JPanel painelCodigo;
    //JPanel painelConsole;

    JScrollPane scrollCodigo;
    JScrollPane scrollConsole;

    LineNumberingTextArea lineNumberingTextArea;
    
    JLabel labelConsole;

    JFrame mainFrame;

    public editor() {

        setLayout(new BorderLayout());

        mainFrame = new JFrame("Compilador LPD");
        mainFrame.pack();
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        labelConsole = new JLabel("  Console");

        try {
            UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
            MetalLookAndFeel.setCurrentTheme(new OceanTheme());
        } catch (Exception e) {
        }

        codigo = new JTextArea(25, 30);
        console = new JTextArea(13, 30);

        taOutputStream = new TextAreaOutputStream(console, "");
        System.setOut(new PrintStream(taOutputStream));

        scrollCodigo = new JScrollPane(codigo);
        scrollConsole = new JScrollPane(console);

        lineNumberingTextArea = new LineNumberingTextArea(codigo);
        scrollCodigo.setRowHeaderView(lineNumberingTextArea);

        //painelCodigo = new JPanel();
        //    painelCodigo.add(codigo);
        //painelConsole = new JPanel();
        //    painelConsole.add(console);

        JMenuBar mb = new JMenuBar();
        JMenuItem mi1 = new JMenuItem("Novo");
        JMenuItem mi2 = new JMenuItem("Abrir");
        JMenuItem mi3 = new JMenuItem("Salvar");
        JMenuItem mi4 = new JMenuItem("Compilar");

        mi1.addActionListener(this);
        mi1.setHorizontalAlignment(SwingConstants.CENTER);
        mi2.addActionListener(this);
        mi2.setHorizontalAlignment(SwingConstants.CENTER);
        mi3.addActionListener(this);
        mi3.setHorizontalAlignment(SwingConstants.CENTER);
        mi4.addActionListener(this);
        mi4.setHorizontalAlignment(SwingConstants.CENTER);

        codigo.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent documentEvent) {
                lineNumberingTextArea.updateLineNumbers();
            }

            @Override
            public void removeUpdate(DocumentEvent documentEvent) {
                lineNumberingTextArea.updateLineNumbers();
            }

            @Override
            public void changedUpdate(DocumentEvent documentEvent) {
                lineNumberingTextArea.updateLineNumbers();
            }
        });

        mb.add(mi1);
        mb.add(mi2);
        mb.add(mi3);
        mb.add(mi4);

        mainFrame.setJMenuBar(mb);
        mainFrame.add(scrollCodigo, "North");
        mainFrame.add(labelConsole);
        mainFrame.add(scrollConsole, "South");
        mainFrame.setSize(1000, 700);
        mainFrame.setLocationRelativeTo(null);
        mainFrame.show();
    }

    public void actionPerformed(ActionEvent e) {

        String stringAction = e.getActionCommand();

        JFileChooser j = new JFileChooser("f:");

        if (stringAction.equals("Salvar")) {

            int r = j.showSaveDialog(null);

            if (r == JFileChooser.APPROVE_OPTION) {

                File fi = new File(j.getSelectedFile().getAbsolutePath());

                try {

                    FileWriter wr = new FileWriter(fi, false);
                    BufferedWriter w = new BufferedWriter(wr);

                    w.write(codigo.getText());
                    w.flush();
                    w.close();

                    path = j.getSelectedFile().getAbsolutePath();
                } catch (Exception evt) {
                    JOptionPane.showMessageDialog(mainFrame, evt.getMessage());
                }
            } else {
                JOptionPane.showMessageDialog(mainFrame, "Operação cancelada.");
            }
        } else if (stringAction.equals("Compilar")) {
            try {
                if (path == null) {
                    int r = j.showSaveDialog(null);

                    if (r == JFileChooser.APPROVE_OPTION) {
                        File fi = new File(j.getSelectedFile().getAbsolutePath());
                        FileWriter wr = new FileWriter(fi, false);
                        BufferedWriter w = new BufferedWriter(wr);

                        w.write(codigo.getText());
                        w.flush();
                        w.close();

                        path = j.getSelectedFile().getAbsolutePath();
                    } else {
                        JOptionPane.showMessageDialog(mainFrame, "Operação cancelada.");
                    }
                }

                boolean sucess;
                SyntacticAnalyzer syntaticAnalyzer = SyntacticAnalyzer.getInstance();
                CodeGenerator generator = CodeGenerator.getInstance();

                syntaticAnalyzer.receiveFilePath(path);
                sucess = syntaticAnalyzer.syntaticAnalyze();

                if (!sucess) {
                    System.out.println(syntaticAnalyzer.getError());
                } else {

                    try {
                        BufferedWriter codeWriter = new BufferedWriter(new FileWriter("Codigo_Gerado"));
                        codeWriter.write(generator.getCodeText());

                        codeWriter.close();
                        System.out.println("Fechou arquivo");
                    } catch (IOException exception) {
                    }
                }
            } catch (Exception evt) {
                JOptionPane.showMessageDialog(mainFrame, evt.getMessage());
            }
        } else if (stringAction.equals("Abrir")) {

            int r = j.showOpenDialog(null);

            if (r == JFileChooser.APPROVE_OPTION) {
                File fi = new File(j.getSelectedFile().getAbsolutePath());

                try {

                    String s1 = "", sl = "";

                    FileReader fr = new FileReader(fi);

                    BufferedReader br = new BufferedReader(fr);

                    sl = br.readLine();

                    while ((s1 = br.readLine()) != null) {
                        sl = sl + "\n" + s1;
                    }

                    codigo.setText(sl);

                    path = j.getSelectedFile().getAbsolutePath();
                } catch (Exception evt) {
                    JOptionPane.showMessageDialog(mainFrame, evt.getMessage());
                }
            } else {
                JOptionPane.showMessageDialog(mainFrame, "Operação cancelada.");
            }
        } else if (stringAction.equals("Novo")) {
            codigo.setText("");
            path = null;
        }
    }
}
