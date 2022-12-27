package model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HighScore {
    private String mode;
    private String name;
    private int result;

    public HighScore() {
    }

    public HighScore(String mode, String name, int result) {
        this.mode = mode;
        this.name = name;
        this.result = result;
    }
}
