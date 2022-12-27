package model;

import java.util.ArrayList;
import java.util.Random;

public class GameField {
    private final Random random = new Random();
    private final int width;
    private final int height;
    private final int bombsCount;
    private final Cell[][] cells;

    public GameField(int height, int width, int bombsCount) {
        this.width = width;
        this.height = height;
        this.bombsCount = bombsCount;
        this.cells = new Cell[width][height];
        initializingField();
    }

    public int getFieldSize() {
        return width * height;
    }

    public Cell getCellByPosition(Position position) {
        return cells[position.getX()][position.getY()];
    }

    public boolean inField(Position position) {
        return position.getX() < width && position.getX() >= 0 && position.getY() < height && position.getY() >= 0;
    }

    public ArrayList<Cell> getClosedCells() {
        ArrayList<Cell> cellsList = new ArrayList<>();
        for (Cell[] row : cells) {
            for (Cell cell : row)
                if (!cell.isOpen()) {
                    cellsList.add(cell);
                }
        }
        return cellsList;
    }

    public ArrayList<Position> getPositionsAround(Position position) {
        ArrayList<Position> positionList = new ArrayList<>();

        for (int x = position.getX() - 1; x <= position.getX() + 1; x++) {
            for (int y = position.getY() - 1; y <= position.getY() + 1; y++) {
                Position positionAround = new Position(x, y);
                if (inField(positionAround) && !positionAround.equals(position)) {
                    positionList.add(positionAround);
                }
            }
        }
        return positionList;
    }

    private void initializingField() {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                cells[x][y] = new Cell(x, y);
            }
        }
    }

    public void generateCells(Position firstPosition) {
        for (int i = 0; i < bombsCount; i++) {
            Position position = putBomb(firstPosition);
            ArrayList<Position> positionsAround = getPositionsAround(position);
            setAmountBombAround(positionsAround);
        }
    }

    private Position putBomb(Position firstPosition) {
        ArrayList<Position> clearPositions = getPositionsAround(firstPosition);
        clearPositions.add(firstPosition);

        Position currentPosition;
        while (true) {
            currentPosition = getRandomPosition();
            Cell cell = getCellByPosition(currentPosition);
            if (!cell.isBomb() && !clearPositions.contains(currentPosition)) {
                cell.setBomb(true);
                break;
            }
        }
        return currentPosition;
    }

    private Position getRandomPosition() {
        return new Position(random.nextInt(width), random.nextInt(height));
    }

    private void setAmountBombAround(ArrayList<Position> positionsAround) {
        for (Position position : positionsAround) {
            Cell cell = getCellByPosition(position);
            int amountBombAround = cell.getAmountBombAround();
            cell.setAmountBombAround(amountBombAround + 1);
        }
    }
}