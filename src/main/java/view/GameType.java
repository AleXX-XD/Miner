package view;

public enum GameType {
    NOVICE(10, 9, 9),
    MEDIUM(40, 16, 16),
    EXPERT(99, 16, 30);

    private final int bombsAmount;
    private final int sizeX;
    private final int sizeY;

    GameType(int bombsAmount, int sizeY, int sizeX) {
        this.bombsAmount = bombsAmount;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
    }

    public int getBombAmount() {
        return this.bombsAmount;
    }

    public int getSizeX() {
        return this.sizeX;
    }

    public int getSizeY() {
        return this.sizeY;
    }
}
