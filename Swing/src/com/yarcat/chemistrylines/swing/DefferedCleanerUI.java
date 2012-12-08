package com.yarcat.chemistrylines.swing;

import java.awt.Container;
import java.awt.Panel;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JLabel;

import com.yarcat.chemistrylines.algorithms.CompoundReporter.CompoundReference;
import com.yarcat.chemistrylines.game.DeferredFieldCleaner;

public class DefferedCleanerUI implements MouseListener {

    private static final Panel DUMB_PANEL = new Panel();

    @SuppressWarnings("serial")
    private static class Button extends JLabel {
        public CompoundReference mRef;

        public Button(CompoundReference ref) {
            mRef = ref;
        }
    }

    private DeferredFieldCleaner mCleaner;
    private Container mPane;
    private SwingField mFieldUI;

    public DefferedCleanerUI(DeferredFieldCleaner cleaner, Container pane,
            SwingField fieldUI) {
        mCleaner = cleaner;
        mPane = pane;
        mFieldUI = fieldUI;
    }

    public void refresh() {
        mPane.setEnabled(false);
        mPane.removeAll();
        mPane.validate();
        for (CompoundReference ref : mCleaner.listCompounds()) {
            Button b = new Button(ref);
            b.addMouseListener(this);
            b.setText(ref.getCompound().getName());
            style.button(b);
            mPane.add(b);
        }
        mPane.add(DUMB_PANEL);
        mPane.validate();
        mPane.invalidate();
        mPane.setEnabled(true);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        Button b = (Button) e.getSource();
        remove(b.mRef);
        mFieldUI.refresh();
        mPane.remove(b);
    }

    private void remove(CompoundReference ref) {
        mCleaner.remove(ref);
        for (int n : ref.getCells()) {
            if (mCleaner.isEmpty(n)) {
                mFieldUI.clear(n);
            }
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        Button b = (Button) e.getSource();
        mFieldUI.refresh();
        style.highlight(b);
        for (int n : b.mRef.getCells()) {
            style.highlight(mFieldUI.getButton(n));
        }
    }

    @Override
    public void mouseExited(MouseEvent e) {
        style.defaultColor((Button) e.getSource());
        mFieldUI.refresh();
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }
}
