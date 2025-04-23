package constants;

import java.awt.*;

public class Constants {
    public static final int WIDTH_GRID = 14;
    public static final int HEIGHT_GRID = 13;
    public static final int TILE_SIZE = 60;
    public static final int BORDER_WIDTH = 14;
    public static final int BORDER_HEIGHT = 37;
    public static final String FONT_NAME = "ZX Spectrum 7 Bold";
    public static final Dimension GAME_PANEL_SIZE = new Dimension(WIDTH_GRID * TILE_SIZE + BORDER_WIDTH,
            HEIGHT_GRID * TILE_SIZE + BORDER_HEIGHT);
    public static final Dimension MAIN_MENU_PANEL_SIZE = new Dimension(840 + BORDER_WIDTH,
                    840 + BORDER_HEIGHT);
    public static final Color PINK = new Color(253, 85, 118);
    public static final Color TAN = new Color(248, 235, 200);
    public static final Color MAIN_FROG_GREEN = new Color(108, 244, 76);
    public static final Color FROG_GREEN = new Color(0, 255, 33);
    public static final Color FROG_CYAN = new Color(28, 178, 131);
}
