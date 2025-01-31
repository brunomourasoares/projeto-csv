package br.com;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.sql.*;
import java.util.List;

public class Main extends JFrame {
    private JTextField filePathField;
    private JButton browseButton;
    private JComboBox<String> tableComboBox;
    private JButton submitButton;
    private JButton exportButton;

    public Main() {
        setTitle("Inserir Dados CSV no Banco");
        setSize(500, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        filePathField = new JTextField();
        browseButton = new JButton("Procurar");
        tableComboBox = new JComboBox<>();
        submitButton = new JButton("Inserir Dados");
        exportButton = new JButton("Exportar Dados");

        // Customizing components
        filePathField.setPreferredSize(new Dimension(200, 25));
        filePathField.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        Dimension buttonSize = new Dimension(150, 30);
        submitButton.setPreferredSize(buttonSize);
        exportButton.setPreferredSize(buttonSize);

        // Adding components to the layout
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(new JLabel("Tabela:"), gbc);
        gbc.gridx = 1;
        add(tableComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        add(filePathField, gbc);
        gbc.gridx = 1;
        add(browseButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(submitButton, gbc);

        gbc.gridy = 3;
        add(exportButton, gbc);

        // Disable submit button initially
        submitButton.setEnabled(false);

        // Add DocumentListener to filePathField
        filePathField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                toggleSubmitButton();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                toggleSubmitButton();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                toggleSubmitButton();
            }

            private void toggleSubmitButton() {
                submitButton.setEnabled(!filePathField.getText().trim().isEmpty());
            }
        });

        browseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("CSV Files", "csv"));
                int returnValue = fileChooser.showOpenDialog(null);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    filePathField.setText(selectedFile.getAbsolutePath());
                }
            }
        });

        submitButton.addActionListener(new SubmitButtonListener());
        exportButton.addActionListener(new ExportButtonListener());

        loadTables();
    }

    private void loadTables() {
        try {
            List<String> tables = DAO.getTables();
            for (String table : tables) {
                tableComboBox.addItem(table);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar as tabelas: " + e.getMessage());
        }
    }

    private class SubmitButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String filePath = filePathField.getText();
            if (filePath.trim().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Por favor, selecione um arquivo CSV.");
                return;
            }
            String tableName = (String) tableComboBox.getSelectedItem();
            try {
                List<String[]> data = CSVReader.readCSV(filePath);
                DAO.insertData(tableName, data);
                JOptionPane.showMessageDialog(null, "Dados inseridos com sucesso!");
            } catch (IOException | SQLException ex) {
                JOptionPane.showMessageDialog(null, "Erro: " + ex.getMessage());
            }
        }
    }

    private class ExportButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String tableName = (String) tableComboBox.getSelectedItem();
            if (tableName == null || tableName.trim().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Por favor, selecione uma tabela.");
                return;
            }

            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Text Files", "txt"));
            int returnValue = fileChooser.showSaveDialog(null);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                if (!file.getName().endsWith(".txt")) {
                    file = new File(file.getAbsolutePath() + ".txt");
                }

                try {
                    DAO.exportTableData(tableName, file);
                    JOptionPane.showMessageDialog(null, "Dados exportados com sucesso!");
                } catch (SQLException | IOException ex) {
                    JOptionPane.showMessageDialog(null, "Erro: " + ex.getMessage());
                }
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Main mainFrame = new Main();
            mainFrame.setVisible(true);
        });
    }
}