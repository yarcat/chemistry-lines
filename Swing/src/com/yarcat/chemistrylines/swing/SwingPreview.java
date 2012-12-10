package com.yarcat.chemistrylines.swing;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JLabel;

import com.yarcat.chemistrylines.field.Element;
import com.yarcat.chemistrylines.game.GameLogic;

public class SwingPreview implements MouseListener {
    private final JLabel[] mPreview;
    private final GameLogic mGame;

    public SwingPreview(GameLogic game, JLabel[] preview) {
        mGame = game;
        mPreview = preview;
    }

    void refresh() {
        Element[] nextElements = mGame.previewNextElements();
        for (int i = 0; i < mPreview.length; ++i) {
            mPreview[i].setText(nextElements[i].getId());
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        mGame.addItems();
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }
}
