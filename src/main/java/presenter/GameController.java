package presenter;

import model.Cell;
import model.Game;
import model.GameField;
import model.Position;
import view.*;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class GameController {
    private final MainWindow mainWindow;
    private final DataHandler dataHandler;
    private GameType gameType;
    private Game game;
    private GameField gameField;

    public GameController(MainWindow mainWindow, DataHandler dataHandler) {
        this.mainWindow = mainWindow;
        this.dataHandler = dataHandler;
    }

    public void createNewGame() {
        mainWindow.createGameField(gameType.getSizeY(), gameType.getSizeX());
        mainWindow.setTimerValue(0);
        mainWindow.setBombsCount(gameType.getBombAmount());
        gameField = new GameField(gameType.getSizeY(), gameType.getSizeX(), gameType.getBombAmount());
        game = new Game(gameField.getFieldSize(), gameType.getBombAmount());
    }

    public void setSettings(GameType gameType) {
        this.gameType = gameType;
    }

    public Game getGame() {
        return game;
    }

    protected void putFlagOnCell(Position position) {
        Cell cell = gameField.getCellByPosition(position);
        if (!cell.isOpen()) {
            int bombCount = game.getHiddenBombsCount();
            if (!cell.isFlagged()) {
                if (bombCount > 0) {
                    cell.setFlagged(true);
                    bombCount--;
                    game.setHiddenBombsCount(bombCount);
                    mainWindow.setCellImage(cell.getColumn(), cell.getRow(), GameImage.MARKED);
                    mainWindow.setBombsCount(bombCount);
                }
            } else {
                cell.setFlagged(false);
                bombCount++;
                game.setHiddenBombsCount(bombCount);
                mainWindow.setCellImage(cell.getColumn(), cell.getRow(), GameImage.CLOSED);
                mainWindow.setBombsCount(bombCount);
            }
        }
    }

    protected void openCellsAround(Position position) {
        Cell cell = gameField.getCellByPosition(position);
        if (cell.isOpen()) {
            ArrayList<Position> positionsAround = gameField.getPositionsAround(position);
            int countFlags = 0;
            for (Position currentPosition : positionsAround) {
                Cell currentCell = gameField.getCellByPosition(currentPosition);
                if (currentCell.isFlagged()) {
                    countFlags++;
                }
            }
            if (cell.getAmountBombAround() == countFlags) {
                for (Position currentPosition : positionsAround) {
                    openCell(currentPosition);
                }
            }
        }
    }

    protected void processLeftClick(Position position) {
        Cell cell = gameField.getCellByPosition(position);
        if (game.getCountClosedCells() == gameField.getFieldSize() && !cell.isFlagged()) {
            startGame(position);
        }
        openCell(position);
    }

    private void startGame(Position firstPosition) {
        game.setStarted(true);
        gameField.generateCells(firstPosition);
        startTimer();
    }

    private void startTimer() {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            int i = 0;

            public void run() {
                if (i <= 999 && !game.isBlocked()) {
                    game.setTimerValue(i);
                    mainWindow.setTimerValue(i);
                    i++;
                } else {
                    timer.cancel();
                }
            }
        }, 0, 1000);
    }

    private void openCell(Position position) {
        if (game.isStarted()) {
            Cell cell = gameField.getCellByPosition(position);
            if (!cell.isOpen() && !cell.isFlagged()) {
                cell.setOpen(true);
                game.setCountClosedCells(game.getCountClosedCells() - 1);
                if (cell.isBomb()) {
                    mainWindow.setCellImage(cell.getColumn(), cell.getRow(), GameImage.BOMB);
                    loseGame();
                } else {
                    int amountBombAround = cell.getAmountBombAround();
                    switch (amountBombAround) {
                        case 1: {
                            mainWindow.setCellImage(cell.getColumn(), cell.getRow(), GameImage.NUM_1);
                            break;
                        }
                        case 2: {
                            mainWindow.setCellImage(cell.getColumn(), cell.getRow(), GameImage.NUM_2);
                            break;
                        }
                        case 3: {
                            mainWindow.setCellImage(cell.getColumn(), cell.getRow(), GameImage.NUM_3);
                            break;
                        }
                        case 4: {
                            mainWindow.setCellImage(cell.getColumn(), cell.getRow(), GameImage.NUM_4);
                            break;
                        }
                        case 5: {
                            mainWindow.setCellImage(cell.getColumn(), cell.getRow(), GameImage.NUM_5);
                            break;
                        }
                        case 6: {
                            mainWindow.setCellImage(cell.getColumn(), cell.getRow(), GameImage.NUM_6);
                            break;
                        }
                        case 7: {
                            mainWindow.setCellImage(cell.getColumn(), cell.getRow(), GameImage.NUM_7);
                            break;
                        }
                        case 8: {
                            mainWindow.setCellImage(cell.getColumn(), cell.getRow(), GameImage.NUM_8);
                            break;
                        }
                        default : {
                            mainWindow.setCellImage(cell.getColumn(), cell.getRow(), GameImage.EMPTY);
                            openAdjacentCells(position);
                        }
                    }
                    if (game.getCountClosedCells() == gameType.getBombAmount()) {
                        winGame();
                    }
                }
            }
        }
    }

    private void openAdjacentCells(Position position) {
        for (Position currentPosition : gameField.getPositionsAround(position)) {
            if (game.isStarted()) {
                openCell(currentPosition);
            }
        }
    }

    private void loseGame() {
        blockGame();
        for (Cell cell : gameField.getClosedCells()) {
            if (cell.isBomb() && !cell.isFlagged()) {
                mainWindow.setCellImage(cell.getColumn(), cell.getRow(), GameImage.BOMB);
            }
            if (cell.isFlagged() && !cell.isBomb()) {
                mainWindow.setCellImage(cell.getColumn(), cell.getRow(), GameImage.NOT_BOMB);
            }
        }
        LoseWindow loseWindow = new LoseWindow(mainWindow);
        loseWindow.setNewGameListener((e) -> createNewGame());
        loseWindow.setVisible(true);
    }

    private void winGame() {
        blockGame();
        WinWindow winWindow = new WinWindow(mainWindow);
        winWindow.setNewGameListener((e) -> openRecordWindow());
        winWindow.setExitListener((e) -> openRecordWindow());
        winWindow.setVisible(true);
        createNewGame();
    }

    private void openRecordWindow() {
        RecordsWindow recordsWindow = new RecordsWindow(mainWindow);
        recordsWindow.setNameListener((name) -> dataHandler.writeNewHighScore(gameType, name, game.getTimerValue()));
        int currentResult = dataHandler.getHighScoreByGameType(gameType).getResult();
        if (currentResult > game.getTimerValue()) {
            recordsWindow.setVisible(true);
        }
    }

    private void blockGame() {
        game.setStarted(false);
        game.setBlocked(true);
    }
}
