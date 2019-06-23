package hgv.smash.gui;

import hgv.smash.resources.Design;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TextDialog extends JDialog implements ActionListener {

    private String content;


    public TextDialog(String content, Frame owner) {
        super(owner, "", true);
        this.content = content;
        init(new Dimension(400, 500));
    }

    public TextDialog(String content, Frame owner, String title, Dimension size) {
        super(owner, title, true);
        this.content = content;
        init(size);
    }

    private void init(Dimension size) {
        JButton okButton = new JButton("Ok");
        JPanel rootPanel = new JPanel();
        JTextArea textArea = new JTextArea(content);

        Border paddingBorder = new EmptyBorder(30, 30, 30, 30);
        textArea.setBorder(paddingBorder);
        okButton.setBorder(paddingBorder);

        textArea.setEnabled(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        okButton.addActionListener(this);

        rootPanel.setLayout(new BorderLayout());

        okButton.setFont(Design.getDefaultFont());
        okButton.setBackground(Design.getPrimaryColor());
        okButton.setForeground(Design.getSecondaryColor());
        textArea.setFont(Design.getDefaultFont());
        textArea.setBackground(Design.getPrimaryColor());
        textArea.setForeground(Design.getSecondaryColor());
        rootPanel.setBackground(Design.getPrimaryColor());
        rootPanel.setForeground(Design.getSecondaryColor());

        rootPanel.add(textArea, BorderLayout.CENTER);
        rootPanel.add(okButton, BorderLayout.SOUTH);
        getContentPane().add(rootPanel);

        setMinimumSize(size);
        pack();
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        dispose();
    }
}
