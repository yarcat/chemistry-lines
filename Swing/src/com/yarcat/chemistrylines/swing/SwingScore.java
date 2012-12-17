package com.yarcat.chemistrylines.swing;

import javax.swing.JTextArea;

import com.yarcat.chemistrylines.game.Scorer;
import com.yarcat.chemistrylines.game.Scorer.ScoreListener;

public class SwingScore implements ScoreListener {
    public final JTextArea textArea;

    public SwingScore() {
        textArea = new JTextArea();
    }

    @Override
    public void onScoreChange(Scorer scorer) {
        textArea.setText("");
        textArea.append(scorer.get());
    }
}
