package org.cis1200.snake;

import java.util.Random;

public class Fruit extends ParentFruit {
    public Fruit(boolean defaultFruit, int[][] gameGrid) {
        Random rand = new Random();

        if (defaultFruit) {
            loc = new int[] { 9, 7 };
        } else {
            for (int i = 0; i < gameGrid.length; i++) {
                for (int j = 0; j < gameGrid[0].length; j++) {
                    if (gameGrid[i][j] == 2) {
                        gameGrid[i][j] = 0;
                    }
                }
            }
            this.generateNewFruit(gameGrid);
        }
    }

    public void generateNewFruit(int[][] gameGrid) {
        Random rand = new Random();

        int xLoc = rand.nextInt(13) + 2;
        int yLoc = rand.nextInt(13) + 2;

        while (gameGrid[yLoc - 1][xLoc - 1] != 0) {
            xLoc = rand.nextInt(13) + 2;
            yLoc = rand.nextInt(13) + 2;
        }

        loc = new int[] { xLoc, yLoc };

        gameGrid[yLoc - 1][xLoc - 1] = 2;
    }
}