package view;

import javax.swing.*;

public enum GameImage {
    CLOSED("img/cell.png"),
    MARKED("img/flag.png"),
    EMPTY("img/empty.png"),
    NUM_1("img/1.png"),
    NUM_2("img/2.png"),
    NUM_3("img/3.png"),
    NUM_4("img/4.png"),
    NUM_5("img/5.png"),
    NUM_6("img/6.png"),
    NUM_7("img/7.png"),
    NUM_8("img/8.png"),
    BOMB("img/mine.png"),
    NOT_BOMB("img/notMine.png"),
    TIMER("img/timer.png"),
    BOMB_ICON("img/mineImage.png");

    private final String fileName;
    private ImageIcon imageIcon;

    GameImage(String fileName) {
        this.fileName = fileName;
    }

    public synchronized ImageIcon getImageIcon() {
        if (imageIcon == null) {
            imageIcon = new ImageIcon(ClassLoader.getSystemResource(fileName));
        }

        return imageIcon;
    }
}
