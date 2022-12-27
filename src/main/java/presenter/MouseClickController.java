package presenter;

import model.Game;
import model.Position;
import view.ButtonType;

import java.awt.event.MouseEvent;

public class MouseClickController {
    private final GameController gameController;
    private boolean leftIsPressed = false;
    private boolean rightIsPressed = false;
    private boolean isMiddleButtonFunction = false;

    public MouseClickController(GameController gameController) {
        this.gameController = gameController;
    }

    public void clickHandler(int x, int y, ButtonType buttonType, MouseEvent event) {
        Game game = gameController.getGame();
        if (!game.isBlocked()) {
            Position position = new Position(x, y);
            switch (buttonType) {
                case LEFT_BUTTON : {
                    if (leftIsPressed && !rightIsPressed) {
                        if (isMiddleButtonFunction) {
                            middleClick(position);
                            isMiddleButtonFunction = false;
                        } else {
                            leftClick(position);
                        }
                    }
                    break;
                }
                case RIGHT_BUTTON : {
                    if (rightIsPressed && !leftIsPressed) {
                        if (isMiddleButtonFunction) {
                            middleClick(position);
                            isMiddleButtonFunction = false;
                        } else {
                            rightClick(position);
                        }
                    }
                    break;
                }
                case MIDDLE_BUTTON : {
                    middleClick(position);
                    break;
                }
                default : {
                }
            }
            defineClick(buttonType, event);
            if (leftIsPressed && rightIsPressed) {
                isMiddleButtonFunction = true;
            }
        }
    }

    protected void leftClick(Position position) {
        gameController.processLeftClick(position);
    }

    protected void rightClick(Position position) {
        gameController.putFlagOnCell(position);
    }

    protected void middleClick(Position position) {
        gameController.openCellsAround(position);
    }

    /**
     * Метод определяет нажата или отпущена указанная кнопка мыши
     * и сохраняет значения в переменных leftIsPressed и rightIsPressed
     *
     * @param buttonType тип кнопки мыши (обрабатываются только левая и правая кнопки)
     * @param event      событие (обрабатываются параметры: MOUSE_PRESSED и MOUSE_RELEASED)
     */
    private void defineClick(ButtonType buttonType, MouseEvent event) {
        switch (buttonType) {
            case LEFT_BUTTON : {
                if (event.getID() == 501) {
                    leftIsPressed = true;
                }
                if (event.getID() == 502) {
                    leftIsPressed = false;
                }
                break;
            }
            case RIGHT_BUTTON : {
                if (event.getID() == 501) {
                    rightIsPressed = true;
                }
                if (event.getID() == 502) {
                    rightIsPressed = false;
                }
                break;
            }
        }
    }
}
