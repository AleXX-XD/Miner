package model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Cell {
    private int column;
    private int row;
    private int amountBombAround;
    private boolean isOpen;
    private boolean isBomb;
    private boolean flagged;

    public Cell(int column, int row) {
        this.column = column;
        this.row = row;
        setAmountBombAround(0);
        setOpen(false);
        setBomb(false);
        setFlagged(false);
    }
}