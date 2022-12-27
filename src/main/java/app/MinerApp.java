package app;

import presenter.DataHandler;
import presenter.GameController;
import presenter.MouseClickController;
import view.GameType;
import view.HighScoresWindow;
import view.MainWindow;
import view.SettingsWindow;

public class MinerApp {
    public static void main(String[] args) {
        MainWindow mainWindow = new MainWindow();
        SettingsWindow settingsWindow = new SettingsWindow(mainWindow);
        HighScoresWindow highScoresWindow = new HighScoresWindow(mainWindow);
        DataHandler dataHandler = new DataHandler();
        GameController gameController = new GameController(mainWindow, dataHandler);
        gameController.setSettings(GameType.NOVICE);
        MouseClickController mouseClickController = new MouseClickController(gameController);

        mainWindow.setNewGameMenuAction((e) -> {
            gameController.createNewGame();
        });
        mainWindow.setSettingsMenuAction((e) -> {
            settingsWindow.setGameTypeListener((gameType) -> {
                gameController.setSettings(gameType);
                gameController.createNewGame();
            });
            settingsWindow.setVisible(true);
        });
        mainWindow.setHighScoresMenuAction((e) -> {
            highScoresWindow.setNoviceRecord(dataHandler.getHighScoreByGameType(GameType.NOVICE).getName(),
                    dataHandler.getHighScoreByGameType(GameType.NOVICE).getResult());
            highScoresWindow.setMediumRecord(dataHandler.getHighScoreByGameType(GameType.MEDIUM).getName(),
                    dataHandler.getHighScoreByGameType(GameType.MEDIUM).getResult());
            highScoresWindow.setExpertRecord(dataHandler.getHighScoreByGameType(GameType.EXPERT).getName(),
                    dataHandler.getHighScoreByGameType(GameType.EXPERT).getResult());
            highScoresWindow.setVisible(true);
        });
        mainWindow.setExitMenuAction((e) -> mainWindow.dispose());
        mainWindow.setCellListener(mouseClickController::clickHandler);

        gameController.createNewGame();
        mainWindow.setVisible(true);
    }
}
