package com.yarcat.chemistrylines.swing;

import javax.swing.JTextArea;

import com.yarcat.chemistrylines.field.Field;
import com.yarcat.chemistrylines.game.GameLogger;

public class SwingGameLogger implements GameLogger {

    private JTextArea mTextArea;

    public SwingGameLogger(JTextArea textArea) {
        super();
        mTextArea = textArea;
    }

    // @Override
    // public void elementAdded(Field field, int cell) {
    // // TODO(luch) #25 add field.cellName(cell)
    // //appendLine(" + " + field.at(cell).getElement().getName());
    // }

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
    public void compoundRemoved(Field field, int[] cells) {
        String s = " - ";
        for (int i : cells) {
            s += field.at(i).getElement().getName();
        }
        appendLine(s);
    }

    private void appendLine(String msg) {
        mTextArea.append(msg);
        mTextArea.append("\n");
        mTextArea.setCaretPosition(mTextArea.getDocument().getLength());
    }
}
