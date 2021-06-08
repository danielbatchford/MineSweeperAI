package danielbatchford.minesweeper.game;

import danielbatchford.minesweeper.Main;
import processing.core.PApplet;

import java.util.*;
import java.util.stream.Collectors;

public class Grid extends Main {

    private final Tile[][] tiles;
    private final int noOfMines;
    private final int noOfCovered;
    private Tile prevTarget = new Tile(0, 0);
    private Tile inferTarget = new Tile(0,0);

    public Grid() throws InvalidGridException {

        noOfMines = (int) (MINE_PERCENTAGE * (DIVISIONS[0] * DIVISIONS[1]));
        noOfCovered = (int) (COVERED_PERCENTAGE * (DIVISIONS[0] * DIVISIONS[1]));

        if (noOfMines >= DIVISIONS[0] * DIVISIONS[1]) {
            throw new InvalidGridException(String.format("no of mines %s is greater than no of squares %s", noOfMines, DIVISIONS[0] * DIVISIONS[1]));
        }

        if (noOfCovered > DIVISIONS[0] * DIVISIONS[1]) {
            throw new InvalidGridException(String.format("no of covered squares %s is greater than no of squares %s", noOfCovered, DIVISIONS[0] * DIVISIONS[1]));
        }

        if (noOfCovered < noOfMines) {
            throw new InvalidGridException(String.format("no of covered squares %s is greater than no of mines %s", noOfCovered, noOfMines));
        }

        this.tiles = new Tile[DIVISIONS[0]][DIVISIONS[1]];

        for (int x = 0; x < DIVISIONS[0]; x++) {
            for (int y = 0; y < DIVISIONS[1]; y++) {
                this.tiles[x][y] = new Tile(x, y);
            }
        }

        initialiseGrid();
    }

    public void draw(PApplet parent, boolean renderMines) {

        float boxWidth = ((float) WIDTH) / ((float) DIVISIONS[0]);
        float boxHeight = ((float) HEIGHT) / ((float) DIVISIONS[1]);

        for (int x = 0; x < DIVISIONS[0]; x++) {
            for (int y = 0; y < DIVISIONS[1]; y++) {

                Tile tile = tiles[x][y];
                int[] colorMap = tile.getColorMap();
                parent.fill(colorMap[0], colorMap[1], colorMap[2]);
                parent.rect(boxWidth * (x + 0.5f), boxHeight * (y + 0.5f), TILE_SCALE * boxWidth, TILE_SCALE * boxHeight);

                // For debug, draw prev target on screen
                if(prevTarget.equals(tile) && SHOW_DEBUG_TILES){
                    parent.fill(PREV_TARGET[0], PREV_TARGET[1], PREV_TARGET[2]);
                    parent.rect(boxWidth * (x + 0.5f), boxHeight * (y + 0.5f), DEBUG_SCALE * boxWidth, DEBUG_SCALE * boxHeight);
                }

                // For debug, draw infer target on screen
                if(inferTarget.equals(tile) && SHOW_DEBUG_TILES){
                    parent.fill(PREV_INFER_TARGET[0], PREV_INFER_TARGET[1], PREV_INFER_TARGET[2]);
                    parent.rect(boxWidth * (x + 0.5f), boxHeight * (y + 0.5f), DEBUG_SCALE * boxWidth, DEBUG_SCALE * boxHeight);
                }

                // draws mines on screen if renderMines is true
                if (tile.isMine() && renderMines) {
                    parent.fill(MINE[0], MINE[1], MINE[2]);
                    parent.rect(boxWidth * (x + 0.5f), boxHeight * (y + 0.5f), MINE_SCALE * boxWidth, MINE_SCALE * boxHeight);
                }

                // draw numbers
                if (tile.getStatus().equals(Status.UNCOVERED) && tile.getValue() != 0) {
                    parent.fill(0, 0, 0);
                    parent.text(String.valueOf(tile.getValue()), (x + 0.5f) * boxWidth, (y + 0.5f) * boxHeight);

                }

            }
        }
    }

    public boolean updateMove() {

        List<Tile> targetList = getUncoveredTiles();

        if(USE_GLOBAL_RANDOMNESS){
            Collections.shuffle(targetList);
        }

        if(USE_LOCAL_NEIGHBORS){
            targetList.sort((o1, o2) -> Util.getManhattanDistance(o1, prevTarget) < Util.getManhattanDistance(o2, prevTarget) ? 1 : 0);
        }

        while (targetList.size() > 0) {

            Tile target = tiles[targetList.get(0).getX()][targetList.get(0).getY()];
            targetList.remove(0);

            List<Tile> neighbors = getNeighbors(target);

            List<Tile> coveredNeighbors = getCoveredTiles(neighbors);
            List<Tile> flaggedNeighbors = getFlaggedTiles(neighbors);

            // uncover a tile in the covered neighbour list if all flags have been set
            if(flaggedNeighbors.size() == target.getValue() && !coveredNeighbors.isEmpty()){
                Tile toUncover = coveredNeighbors.get(0);
                toUncover.setStatus(Status.UNCOVERED);
                this.prevTarget = toUncover;
                this.inferTarget = target;
                //System.out.printf("Mined tile %s%n", toUncover.toString());
                return true;
            }

            // Uncover one of covered tiles if all neighbour flags have been set
            if(coveredNeighbors.size() + flaggedNeighbors.size() == target.getValue() && !coveredNeighbors.isEmpty()){
                Tile toFlag = coveredNeighbors.get(0);
                toFlag.setStatus(Status.FLAGGED);
                this.prevTarget = toFlag;
                this.inferTarget = target;
                //System.out.printf("Flagged tile %s%n", toFlag.toString());
                return true;
            }
        }

        return false;
    }

    public boolean mine(int[] target) {

        Tile mined = tiles[target[0]][target[1]];
        mined.setStatus(Status.UNCOVERED);
        this.prevTarget = mined;
        return !mined.isMine();
    }

    public void toggleFlag(int[] target) {

        Tile flagged = tiles[target[0]][target[1]];
        this.prevTarget = flagged;
        flagged.toggleFlagged();
    }

    public boolean checkGameEnd() {
        return getFlaggedTiles(getTileArrayAsList()).size() == noOfMines;
    }

    private void initialiseGrid() {

        // Gen mines & covered tiles
        List<Tile> tileList = getTileArrayAsList();

        Collections.shuffle(tileList);
        List<Tile> mineTileList = tileList.subList(0, noOfMines);
        List<Tile> extraCoveredTileList = tileList.subList(noOfMines, noOfCovered);

        for (Tile tile : mineTileList) {
            tiles[tile.getX()][tile.getY()].setToMine();
            tiles[tile.getX()][tile.getY()].setStatus(Status.COVERED);
        }

        for (Tile tile : extraCoveredTileList) {
            tiles[tile.getX()][tile.getY()].setStatus(Status.COVERED);
        }

        // Generate tile values
        for(Tile tile: tileList){
            int neighborMineCount = 0;
            System.out.println(getNeighbors(tile).size());
            for(Tile neighbor: getNeighbors(tile)){
                neighborMineCount += neighbor.isMine() ? 1 : 0;
            }
            tile.setValue(neighborMineCount);
        }
    }

    private List<Tile> getTileArrayAsList() {

        List<Tile> tileList = new ArrayList<>(DIVISIONS[0] * DIVISIONS[1]);

        for (int x = 0; x < DIVISIONS[0]; x++) {
            for (int y = 0; y < DIVISIONS[1]; y++) {
                tileList.add(tiles[x][y]);
            }
        }
        return tileList;
    }

    private List<Tile> getUncoveredTiles() {

        return getUncoveredTiles(getTileArrayAsList());
    }

    private List<Tile> getCoveredTiles(List<Tile> list) {

        return list.stream().filter(Tile::isCovered).collect(Collectors.toList());
    }

    private List<Tile> getUncoveredTiles(List<Tile> list) {

        return list.stream().filter(Tile::isUncovered).collect(Collectors.toList());
    }

    private List<Tile> getFlaggedTiles(List<Tile> list){

        return list.stream().filter(Tile::isFlagged).collect(Collectors.toList());
    }

    private List<Tile> getNeighbors(int x, int y) {

        List<Tile> neighbors = new ArrayList<>(8);
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (!(x + i < 0 || x + i >= DIVISIONS[0] || y + j < 0 || y + j >= DIVISIONS[1])) {
                    if(!(i == 0 && j == 0)) {
                        neighbors.add(tiles[x + i][y + j]);
                    }
                }
            }
        }

        if(USE_NEIGHBOR_RANDOMNESS) {
            Collections.shuffle(neighbors);
        }

        return neighbors;
    }

    private List<Tile> getNeighbors(Tile tile) {

        return getNeighbors(tile.getX(), tile.getY());
    }

}
