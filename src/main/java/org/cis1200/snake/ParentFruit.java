package org.cis1200.snake;

public abstract class ParentFruit {
    int[] loc = new int[] { 9, 7 };

    public int[] getLoc() {
        return loc;
    }

    public void setLoc(int[] loc) {
        this.loc = loc;
    }

    public boolean checkValidity(int[][] gameGrid, int[][] tester) {
        for (int[] pos : tester) {
            boolean valid = (gameGrid[pos[1] - 1][pos[0] - 1] == 0);
            if (valid) {
                return valid;
            }
        }

        return false;
    }

    public abstract void generateNewFruit(int[][] gameGrid);

    public boolean update(int[][] gameGrid, int[] snakeHead) {
        if (gameGrid[snakeHead[1] - 1][snakeHead[0] - 1] == 2) {
            generateNewFruit(gameGrid);
            return true;
        } else {
            return false;
        }
    }
}
