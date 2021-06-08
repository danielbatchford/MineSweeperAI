package danielbatchford.minesweeper.game;

public class Util {

    static int getManhattanDistance(Tile t1, Tile t2){
        return Math.abs(t1.getX() - t2.getX()) + Math.abs(t1.getY() - t2.getY());
    }

}
