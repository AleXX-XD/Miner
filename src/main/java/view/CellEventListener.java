package view;

import java.awt.event.MouseEvent;

public interface CellEventListener {
    void onMouseClick(int x, int y, ButtonType buttonType, MouseEvent event);
}
