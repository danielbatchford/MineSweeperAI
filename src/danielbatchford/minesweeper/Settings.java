package danielbatchford.minesweeper;

public interface Settings {

    int[] COVERED = new int[]{33, 79, 75};
    int[] UNCOVERED = new int[]{214, 248, 214};
    int[] FLAGGED = new int[]{127, 198, 164};
    int[] MINE = new int[]{231, 111, 81};
    int[] PREV_TARGET = new int[]{214,162,173};
    int[] PREV_INFER_TARGET = new int[]{255,255,0};

    String WINDOW_TITLE = "Minesweeper AI";
    int TEXT_SIZE = 15;

    int WIDTH = 600;
    int HEIGHT = 1000;

    int[] DIVISIONS = new int[]{24, 40};

    float TILE_SCALE = 0.95f;
    float MINE_SCALE = 0.5f;
    float DEBUG_SCALE = 0.7f;

    float MINE_PERCENTAGE = 0.10f;
    float COVERED_PERCENTAGE = 0.90f;

    boolean USE_GLOBAL_RANDOMNESS = true;
    boolean USE_NEIGHBOR_RANDOMNESS = true;
    boolean USE_LOCAL_NEIGHBORS = true;
    boolean SHOW_DEBUG_TILES = true;

    int UPDATE_INTERVAL = 1;
    int FRAME_RATE = 120;

}
