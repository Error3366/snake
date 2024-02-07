package org.cis1200.snake;

import java.util.Random;

public class CornerFruit extends ParentFruit {
    public CornerFruit(boolean defaultFruit, int[][] gameGrid) {
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

    @Override
    public void generateNewFruit(int[][] gameGrid) {
        Random rand = new Random();

        int[][] posLoc = new int[][] { { 1, 1 }, { 1, 15 }, { 15, 1 }, { 15, 15 } };

        if (!checkValidity(gameGrid, posLoc)) {
            int xLoc = rand.nextInt(15) + 1;
            int yLoc = rand.nextInt(15) + 1;

            while (gameGrid[yLoc - 1][xLoc - 1] != 0) {
                xLoc = rand.nextInt(15) + 1;
                yLoc = rand.nextInt(15) + 1;
            }

            loc = new int[] { xLoc, yLoc };

            for (int i = 0; i < 15; i++) {
                for (int j = 0; j < 15; j++) {
                    System.out.print(gameGrid[i][j]);
                }
                System.out.println();
            }
        } else {
            int[] testLoc = posLoc[rand.nextInt(4)];

            while (gameGrid[testLoc[1] - 1][testLoc[0] - 1] != 0) {
                testLoc = posLoc[rand.nextInt(4)];
            }

            loc = testLoc;
        }

        gameGrid[loc[1] - 1][loc[0] - 1] = 2;
    }
}
