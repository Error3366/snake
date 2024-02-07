package org.cis1200.snake;

import java.util.Random;
public class SideFruit extends ParentFruit {
    public SideFruit(boolean defaultFruit, int[][] gameGrid) {
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

        int[][] posLoc = new int[][] { { 1, 2 }, { 1, 3 }, { 1, 4 }, { 1, 5 }, { 1, 6 }, { 1, 7 },
            { 1, 8 }, { 1, 9 }, { 1, 10 }, { 1, 11 }, { 1, 12 },
            { 1, 13 }, { 1, 14 }, { 2, 1 }, { 3, 1 }, { 4, 1 }, { 5, 1 }, { 6, 1 }, { 7, 1 },
            { 8, 1 }, { 9, 1 }, { 10, 1 }, { 11, 1 }, { 12, 1 }, { 13, 1 },
            { 14, 1 }, { 15, 2 }, { 15, 3 }, { 15, 4 }, { 15, 5 }, { 15, 6 }, { 15, 7 }, { 15, 8 },
            { 15, 9 }, { 15, 10 }, { 15, 11 }, { 15, 12 },
            { 15, 13 }, { 15, 14 }, { 2, 15 }, { 3, 15 }, { 4, 15 }, { 5, 15 }, { 6, 15 },
            { 7, 15 }, { 8, 15 }, { 9, 15 }, { 10, 15 }, { 11, 15 },
            { 12, 15 }, { 13, 15 }, { 14, 15 } };

        if (!super.checkValidity(gameGrid, posLoc)) {
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
            int[] testLoc = posLoc[rand.nextInt(52)];

            while (gameGrid[testLoc[1] - 1][testLoc[0] - 1] != 0) {
                testLoc = posLoc[rand.nextInt(52)];
            }

            loc = testLoc;
        }

        gameGrid[loc[1] - 1][loc[0] - 1] = 2;
    }
}
