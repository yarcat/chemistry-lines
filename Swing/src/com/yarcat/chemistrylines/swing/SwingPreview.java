package com.yarcat.chemistrylines.swing;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import com.yarcat.chemistrylines.field.Element;
import com.yarcat.chemistrylines.game.GameLogic;

public class SwingPreview implements MouseListener {

    @SuppressWarnings("serial")
    static class PreviewButton extends ElementButton {
        private Element mElement;

        @Override
        public Element getElement() {
            return mElement;
        }

        private void setElement(Element element) {
            mElement = element;
            refresh();
        }
    }

    private final PreviewButton[] mPreview;
    private final GameLogic mGame;

    public SwingPreview(GameLogic game, PreviewButton[] preview) {
        mGame = game;
        mPreview = preview;
    }

    void refresh() {
        Element[] nextElements = mGame.previewNextElements();
        for (int i = 0; i < mPreview.length; ++i) {
            mPreview[i].setElement(nextElements[i]);
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
