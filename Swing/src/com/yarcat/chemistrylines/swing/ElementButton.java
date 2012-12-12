package com.yarcat.chemistrylines.swing;

import java.awt.Color;

import javax.swing.JLabel;

import com.yarcat.chemistrylines.field.Element;
import com.yarcat.chemistrylines.field.Element.Category;
import com.yarcat.chemistrylines.field.Element.StateOfMatter;

@SuppressWarnings("serial")
abstract class ElementButton extends JLabel {

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

    public abstract Element getElement();

    public void refresh() {
        updateContent();
        updateStyle();
    }

    void init() {
        updateContent();
        style.button(this);
        updateStyle();
    }

    void updateContent() {
        setText(getTitle());
    }

    private String getTitle() {
        return isEmpty() ? "" : getElement().getId();
    }

    void updateStyle() {
        setBackground(getBgColor());
        setForeground(getFgColor());
    }

    Color getBgColor() {
        Category c = isEmpty() ? Category.Undefined : getElement().category();
        int i = c.ordinal() < categoryColor.length ? c.ordinal() : 0;
        return categoryColor[i];
    }

    Color getFgColor() {
        StateOfMatter s =
            isEmpty() ? StateOfMatter.Undefined : getElement().stateOfMatter();
        return matterColor[s.ordinal() < matterColor.length ? s.ordinal() : 0];
    }

    boolean isEmpty() {
        return getElement() == null;
    }
}
