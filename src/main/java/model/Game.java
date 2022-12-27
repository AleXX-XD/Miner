package model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Game {
    private int hiddenBombsCount;
    private int timerValue;
    private int countClosedCells;
    private boolean isBlocked;
    private boolean isStarted;

    public Game(int countClosedCells, int hiddenBombsCount) {
        this.countClosedCells = countClosedCells;
        this.hiddenBombsCount = hiddenBombsCount;
        setBlocked(false);
        setStarted(false);
    }
}
