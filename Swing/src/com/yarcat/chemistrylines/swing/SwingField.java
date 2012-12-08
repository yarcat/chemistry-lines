package com.yarcat.chemistrylines.swing;

import java.awt.Color;

import javax.swing.JLabel;

import com.yarcat.chemistrylines.field.Cell;
import com.yarcat.chemistrylines.field.Element;
import com.yarcat.chemistrylines.field.Element.Category;
import com.yarcat.chemistrylines.field.Element.StateOfMatter;
import com.yarcat.chemistrylines.game.GameLogic;
import com.yarcat.chemistrylines.view.SelectionInView;

class SwingField {
    // @formatter:off
    final static Color[] categoryColor = new Color[] {
        style.DEFAULT_BG,             // Undefined
        new Color(0xff, 0x66, 0x66),  // AlkaliMetal
        new Color(0xff, 0xde, 0xad),  // AlkalineMetal
        new Color(0xff, 0xbf, 0xff),  // Lanthanoid
        new Color(0xff, 0x99, 0xcc),  // Actinoid
        new Color(0xff, 0xc0, 0xc0),  // TransitionMetal
        new Color(0xcc, 0xcc, 0xcc),  // PostTransitionMetal
        new Color(0xcc, 0xcc, 0x99),  // Metalloid
        new Color(0xa0, 0xff, 0xa0),  // OtherNonMetal
        new Color(0xff, 0xff, 0x99),  // Halogen
        new Color(0xc0, 0xff, 0xff),  // NobleGas
    };

    final static Color[] matterColor = new Color[] {
        style.DEFAULT_FG,  // Undefined
        Color.BLACK,       // Solid
        Color.GREEN,       // Liquid
        Color.RED,         // Gas
    };
    // @formatter:on

    @SuppressWarnings("serial")
    static class Button extends JLabel {
        private final int mId;

        public Button(int id) {
            mId = id;
        }

        public int id() {
            return mId;
        }
    }

    private final GameLogic mGame;
    private final Button[] mButtons;
    private final SelectionInView mSel;

    public SwingField(GameLogic game, Button[] buttons) {
        mGame = game;
        mButtons = buttons;
        mSel = new SelectionInView();
    }

    SelectionInView selection() {
        return mSel;
    }

    void refresh() {
        for (int i = 0; i < mButtons.length; ++i) {
            style(i);
            mButtons[i].setText(getTitle(i));
        }
    }

    private void style(int i) {
        Button b = mButtons[i];
        Element e = getElement(i);
        Color bg;

        if (mSel.hasSource() && mSel.getSource() == i) {
            bg = Color.DARK_GRAY;
        } else if (mSel.hasDestination() && mSel.getDestination() == i) {
            bg = Color.GRAY;
        } else {
            Category c = e == null ? Category.Undefined : e.category();
            if (c.ordinal() < categoryColor.length) {
                bg = categoryColor[c.ordinal()];
            } else {
                bg = categoryColor[0];
            }
        }
        b.setBackground(bg);

        StateOfMatter s =
            e == null ? StateOfMatter.Undefined : e.stateOfMatter();
        Color fg =
            matterColor[s.ordinal() < matterColor.length ? s.ordinal() : 0];
        b.setForeground(fg);
    }

    private String getTitle(int i) {
        return isEmpty(i) ? "" : getElement(i).getId();
    }

    private Element getElement(int i) {
        return at(i).getElement();
    }

    private boolean isEmpty(int i) {
        return at(i).isEmpty();
    }

    private Cell at(int i) {
        return mGame.getField().at(i);
    }

    public Button getButton(int n) {
        return mButtons[n];
    }

    public void clear(int n) {
        mButtons[n].setText(null);
        style(n);
    }
}
