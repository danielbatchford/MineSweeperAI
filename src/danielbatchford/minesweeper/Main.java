package danielbatchford.minesweeper;

import danielbatchford.minesweeper.game.Grid;
import danielbatchford.minesweeper.game.InvalidGridException;
import processing.core.PApplet;

import java.util.Arrays;

public class Main extends PApplet implements Settings {

    protected boolean renderMines = true;
    private Grid grid;
    private boolean paused;

    public static void main(String[] args) {

        PApplet.main("danielbatchford.minesweeper.Main");
    }

    // Internal method to allow overriden mousePressed & mouseDragged to use the same behaviour
    private void mousePressed(int mouseX, int mouseY) {

        if(this.paused){
            return;
        }

        float boxWidth = ((float) WIDTH) / ((float) DIVISIONS[0]);
        float boxHeight = ((float) HEIGHT) / ((float) DIVISIONS[1]);
        int[] target = new int[]{(int) (mouseX / boxWidth), (int) (mouseY / boxHeight)};

        if (target[0] < 0 || target[0] >= DIVISIONS[0] || target[1] < 0 || target[1] >= DIVISIONS[1]) {
            System.out.println(String.format("Target out of range (%s)", Arrays.toString(target)));
            return;
        }

        switch (mouseButton) {
            case LEFT:
                if (!grid.mine(target)) {
                    System.out.printf("Mined target %s was a mine%n", Arrays.toString(target));
                    resetGame();
                }
                break;
            case RIGHT:
                grid.toggleFlag(target);
                break;
        }
    }

    private void resetGame() {

        System.out.println("Game Reset");
        try {
            this.grid = new Grid();
        } catch (InvalidGridException e) {
            e.printStackTrace();
        }
    }

    public void settings() {

        size(WIDTH, HEIGHT);
    }

    public void setup() {

        frameRate(this.FRAME_RATE);
        surface.setTitle(WINDOW_TITLE);
        noStroke();
        textAlign(CENTER, CENTER);
        textSize(TEXT_SIZE);
        rectMode(CENTER);

        resetGame();
    }

    public void draw() {

        if(paused){
            return;
        }

        background(UNCOVERED[0], UNCOVERED[1], UNCOVERED[2]);


        if(Math.floorMod(frameCount, UPDATE_INTERVAL) == 0){
                if(!grid.updateMove()){
                    System.out.println("UpdateMove() couldn't find a move");
                    this.paused = true;
                }
            }
        grid.draw(this, renderMines);
        if (grid.checkGameEnd()) {
            System.out.println("Game Ended");
            this.paused = true;
        }
    }

    @Override
    public void mousePressed() {

        this.mousePressed(mouseX, mouseY);
    }

    @Override
    public void mouseDragged() {

        this.mousePressed(mouseX, mouseY);
    }

    @Override
    public void keyPressed() {

        switch (key) {
            case 'r' -> {
                resetGame();
                this.paused = false;
            }
            case 'm' -> this.renderMines = !this.renderMines;
            case 'p' -> this.paused = !this.paused;        }
    }

}
