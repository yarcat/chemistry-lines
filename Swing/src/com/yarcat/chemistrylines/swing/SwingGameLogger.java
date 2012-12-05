package com.yarcat.chemistrylines.swing;

import javax.swing.JTextArea;

import com.yarcat.chemistrylines.algorithms.CompoundReporter.CompoundReference;
import com.yarcat.chemistrylines.field.Field;
import com.yarcat.chemistrylines.game.GameLogger;

public class SwingGameLogger implements GameLogger {

    private JTextArea mTextArea;

    public SwingGameLogger(JTextArea textArea) {
        super();
        mTextArea = textArea;
    }

    @Override
    public void elementsAdded(Field field, int[] cells) {
        String s = "    +";
        for (int i : cells) {
            s += " ";
            s += field.at(i).getElement().getName();
        }
        appendLine(s);
    }

    @Override
    public void compoundRemoved(Field field, CompoundReference ref) {
        String s = " - ";
        if (ref.getCompound() == null) {
            for (int i : ref.getCells()) {
                s += field.at(i).getElement().getName();
            }
        } else {
            s += ref.getCompound().getName();
        }
        appendLine(s);
    }

    private void appendLine(String msg) {
        mTextArea.append(msg);
        mTextArea.append("\n");
        mTextArea.setCaretPosition(mTextArea.getDocument().getLength());
    }
}
