package hgv.smash.gui;


import javax.swing.*;
import java.awt.*;

public class MyComboBoxRenderer extends JLabel implements ListCellRenderer {
    private String titleCombobox;

    public MyComboBoxRenderer(String s) {
        setOpaque(true);
        titleCombobox = s;
    }

    @Override
    public Component getListCellRendererComponent(JList list, Object value,
                                                  int index, boolean isSelected, boolean hasFocus) {
        if (index == -1 && value == null) setText(titleCombobox);
        else setText(value.toString());
        return this;
    }

}

