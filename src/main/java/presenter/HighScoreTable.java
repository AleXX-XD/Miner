package presenter;

import model.HighScore;

import java.util.ArrayList;

public class HighScoreTable {
    private final ArrayList<HighScore> highScores = new ArrayList<>();

    public void addHighScore(HighScore highScore) {
        highScores.add(highScore);
    }

    public void replaceHighScore(int index, HighScore highScore) {
        highScores.remove(index);
        highScores.add(index, highScore);
    }

    public ArrayList<HighScore> getHighScores() {
        return highScores;
    }
}