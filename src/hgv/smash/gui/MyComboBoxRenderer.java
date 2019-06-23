package hgv.smash.gui;


import hgv.smash.resources.Design;

import javax.swing.*;
import java.awt.*;

public class MyComboBoxRenderer extends JLabel implements ListCellRenderer {
    private String titelCombobox;

    public MyComboBoxRenderer(String s) {
        setOpaque(true);
        titelCombobox = s;
    }

    @Override
    public Component getListCellRendererComponent(JList list, Object value,
                                                  int index, boolean isSelected, boolean hasFocus) {
        if (index == -1 && value == null) setText(titelCombobox);
        else setText(value.toString());
        return this;
    }

}

