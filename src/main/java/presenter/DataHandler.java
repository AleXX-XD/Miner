package presenter;

import com.fasterxml.jackson.databind.ObjectMapper;
import model.HighScore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import view.GameType;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Класс для работы с json файлом, в котором сохраняется таблица рекордов
 */
public class DataHandler {
    private final String FILE = "/records.json";
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final ObjectMapper objectMapper = new ObjectMapper();
    private HighScoreTable highScoreTable;

    public DataHandler() {
        getHighScores();
    }

    public HighScore getHighScoreByGameType(GameType gameType) {
        HighScore highScore = new HighScore("Unknown", "NoName", 999);
        for (HighScore score : highScoreTable.getHighScores()) {
            if (gameType.name().equalsIgnoreCase(score.getMode())) {
                highScore = score;
            }
        }
        return highScore;
    }

    public void writeNewHighScore(GameType gameType, String name, int result) {
        ArrayList<HighScore> highScores = highScoreTable.getHighScores();

        for (int i = 0; i < highScores.size(); i++) {
            if (gameType.name().equalsIgnoreCase((highScores.get(i)).getMode())) {
                highScoreTable.replaceHighScore(i, new HighScore((highScores.get(i)).getMode(), name, result));
            }
        }
        writeFile(highScoreTable);
    }

    private void getHighScores() {
        URL url = this.getClass().getResource(FILE);
        if (url == null) {
            createFile();
        }
        try (InputStream inputStream = this.getClass().getResourceAsStream(FILE)) {
            highScoreTable = objectMapper.readValue(inputStream, HighScoreTable.class);
        } catch (IOException ex) {
            log.error("Ошибка чтения файла '" + FILE + "' / " + ex.getMessage());
        }
    }

    private void createFile() {
        highScoreTable = new HighScoreTable();
        highScoreTable.addHighScore(new HighScore("Novice", "NoName", 999));
        highScoreTable.addHighScore(new HighScore("Medium", "NoName", 999));
        highScoreTable.addHighScore(new HighScore("Expert", "NoName", 999));
        writeFile(highScoreTable);
    }

    private void writeFile(HighScoreTable highScoreTable) {
        try {
            ClassLoader classLoader = getClass().getClassLoader();
            File file = new File((Objects.requireNonNull(classLoader.getResource("."))).getFile() + FILE);
            objectMapper.writeValue(new File(String.valueOf(file)), highScoreTable);
        } catch (IOException ex) {
            log.error("Ошибка записи файла '" + FILE + "' / " + ex.getMessage());
        }
    }
}
