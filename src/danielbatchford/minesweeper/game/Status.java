package danielbatchford.minesweeper.game;

import danielbatchford.minesweeper.Settings;

enum Status implements Settings {
    UNCOVERED, COVERED, FLAGGED;

    int[] getColorMap() {

        return switch (this) {
            case COVERED -> Settings.COVERED;
            case UNCOVERED -> Settings.UNCOVERED;
            case FLAGGED -> Settings.FLAGGED;
        };
    }
}
