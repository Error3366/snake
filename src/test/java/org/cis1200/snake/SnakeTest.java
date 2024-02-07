package org.cis1200.snake;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * You can use this file (and others) to test your
 * implementation.
 */

public class SnakeTest {

    @Test
    public void testInitialGameState() {
        GameState gameState = new GameState();

        assertTrue(gameState.isAlive());
        assertEquals(0, gameState.getScore());
        assertEquals(Direction.RIGHT, gameState.getCurrentDirection());

        int[] first = gameState.getSnakePath().pop();
        int[] last = gameState.getSnakePath().removeLast();

        assertEquals(2, first[0]);
        assertEquals(7, first[1]);
        assertEquals(5, last[0]);
        assertEquals(7, last[1]);

        int[][] gameGrid = gameState.getGameGrid();
        assertEquals(2, gameGrid[6][8]);

        assertEquals(1, gameGrid[6][4]);
        assertEquals(1, gameGrid[6][3]);
        assertEquals(1, gameGrid[6][2]);
        assertEquals(1, gameGrid[6][1]);
    }

    @Test
    public void testMoveNull() {
        GameState gameState = new GameState();

        gameState.moveSnakeHead(null);

        // Retest all the initial states
        assertTrue(gameState.isAlive());
        assertEquals(0, gameState.getScore());
        assertEquals(Direction.RIGHT, gameState.getCurrentDirection());

        int[] first = gameState.getSnakePath().pop();
        int[] last = gameState.getSnakePath().removeLast();

        assertEquals(2, first[0]);
        assertEquals(7, first[1]);
        assertEquals(5, last[0]);
        assertEquals(7, last[1]);

        int[][] gameGrid = gameState.getGameGrid();
        assertEquals(2, gameGrid[6][8]);

        assertEquals(1, gameGrid[6][4]);
        assertEquals(1, gameGrid[6][3]);
        assertEquals(1, gameGrid[6][2]);
        assertEquals(1, gameGrid[6][1]);
    }

    @Test
    public void testMoveLeftInitial() {
        GameState gameState = new GameState();

        gameState.moveSnakeHead(Direction.LEFT);

        // Retest all the initial states
        assertTrue(gameState.isAlive());
        assertEquals(0, gameState.getScore());
        assertEquals(Direction.RIGHT, gameState.getCurrentDirection());

        int[] first = gameState.getSnakePath().pop();
        int[] last = gameState.getSnakePath().removeLast();

        assertEquals(2, first[0]);
        assertEquals(7, first[1]);
        assertEquals(5, last[0]);
        assertEquals(7, last[1]);

        int[][] gameGrid = gameState.getGameGrid();
        assertEquals(2, gameGrid[6][8]);

        assertEquals(1, gameGrid[6][4]);
        assertEquals(1, gameGrid[6][3]);
        assertEquals(1, gameGrid[6][2]);
        assertEquals(1, gameGrid[6][1]);
    }

    @Test
    public void testMoveUp() {
        GameState gameState = new GameState();

        gameState.moveSnakeHead(Direction.UP);

        int[] first = gameState.getSnakePath().pop(); // tail
        int[] last = gameState.getSnakePath().removeLast(); // head

        assertEquals(3, first[0]);
        assertEquals(7, first[1]);
        assertEquals(5, last[0]);
        assertEquals(6, last[1]);

        int[][] gameGrid = gameState.getGameGrid();
        assertEquals(2, gameGrid[6][8]);

        assertEquals(1, gameGrid[5][4]);
        assertEquals(1, gameGrid[6][4]);
        assertEquals(1, gameGrid[6][3]);
        assertEquals(1, gameGrid[6][2]);
    }

    @Test
    public void testMoveDown() {
        GameState gameState = new GameState();

        gameState.moveSnakeHead(Direction.DOWN);

        int[] first = gameState.getSnakePath().pop(); // tail
        int[] last = gameState.getSnakePath().removeLast(); // head

        assertEquals(3, first[0]);
        assertEquals(7, first[1]);
        assertEquals(5, last[0]);
        assertEquals(8, last[1]);

        int[][] gameGrid = gameState.getGameGrid();
        assertEquals(2, gameGrid[6][8]);

        assertEquals(1, gameGrid[7][4]);
        assertEquals(1, gameGrid[6][4]);
        assertEquals(1, gameGrid[6][3]);
        assertEquals(1, gameGrid[6][2]);
    }

    @Test
    public void testMoveRight() {
        GameState gameState = new GameState();

        gameState.moveSnakeHead(Direction.RIGHT);

        int[] first = gameState.getSnakePath().pop(); // tail
        int[] last = gameState.getSnakePath().removeLast(); // head

        assertEquals(3, first[0]);
        assertEquals(7, first[1]);
        assertEquals(6, last[0]);
        assertEquals(7, last[1]);

        int[][] gameGrid = gameState.getGameGrid();
        assertEquals(2, gameGrid[6][8]);

        assertEquals(1, gameGrid[6][5]);
        assertEquals(1, gameGrid[6][4]);
        assertEquals(1, gameGrid[6][3]);
        assertEquals(1, gameGrid[6][2]);
    }

    @Test
    public void testMoveOppositeDirection() {
        GameState gameState = new GameState();

        gameState.moveSnakeHead(Direction.RIGHT);
        gameState.moveSnakeHead(Direction.LEFT);

        int[] first = gameState.getSnakePath().pop(); // tail
        int[] last = gameState.getSnakePath().removeLast(); // head

        assertEquals(4, first[0]);
        assertEquals(7, first[1]);
        assertEquals(7, last[0]);
        assertEquals(7, last[1]);

        int[][] gameGrid = gameState.getGameGrid();
        assertEquals(2, gameGrid[6][8]);

        assertEquals(1, gameGrid[6][6]);
        assertEquals(1, gameGrid[6][5]);
        assertEquals(1, gameGrid[6][4]);
        assertEquals(1, gameGrid[6][3]);
    }

    @Test
    public void testMoveToEatFruit() {
        GameState gameState = new GameState();

        int[] originalFruitLoc = gameState.getFruit().getLoc();

        gameState.moveSnakeHead(Direction.RIGHT);
        gameState.moveSnakeHead(Direction.RIGHT);
        gameState.moveSnakeHead(Direction.RIGHT);
        gameState.moveSnakeHead(Direction.RIGHT);

        int[] first = gameState.getSnakePath().pop(); // tail
        int[] last = gameState.getSnakePath().removeLast(); // head

        assertEquals(5, first[0]); // doesn't shift the tail after eating fruit
        assertEquals(7, first[1]);
        assertEquals(9, last[0]);
        assertEquals(7, last[1]);

        int[][] gameGrid = gameState.getGameGrid();

        assertEquals(1, gameGrid[6][8]);
        assertEquals(1, gameGrid[6][7]);
        assertEquals(1, gameGrid[6][6]);
        assertEquals(1, gameGrid[6][5]);
        assertEquals(1, gameGrid[6][4]);

        int[] fruitLoc = gameState.getFruit().getLoc();

        assertTrue(fruitLoc[0] <= 15);
        assertTrue(fruitLoc[0] >= 0);
        assertTrue(fruitLoc[1] <= 15);
        assertTrue(fruitLoc[1] >= 0);

        assertEquals(2, gameGrid[fruitLoc[1] - 1][fruitLoc[0] - 1]);
        assertNotEquals(2, gameGrid[originalFruitLoc[1] - 1][originalFruitLoc[0] - 1]);
    }

    @Test
    public void testNextToRightWall() {
        GameState gameState = new GameState();

        int[] originalFruitLoc = gameState.getFruit().getLoc();

        for (int i = 0; i < 10; i++) {
            gameState.moveSnakeHead(Direction.RIGHT);
        }

        assertTrue(gameState.isAlive());

        int[] first = gameState.getSnakePath().pop(); // tail
        int[] last = gameState.getSnakePath().removeLast(); // head

        assertEquals(11, first[0]); // doesn't shift the tail after eating fruit
        assertEquals(7, first[1]);
        assertEquals(15, last[0]);
        assertEquals(7, last[1]);

        int[][] gameGrid = gameState.getGameGrid();

        assertEquals(1, gameGrid[6][14]);
        assertEquals(1, gameGrid[6][13]);
        assertEquals(1, gameGrid[6][12]);
        assertEquals(1, gameGrid[6][11]);
        assertEquals(1, gameGrid[6][10]);

        int[] fruitLoc = gameState.getFruit().getLoc();

        assertTrue(fruitLoc[0] <= 15);
        assertTrue(fruitLoc[0] >= 0);
        assertTrue(fruitLoc[1] <= 15);
        assertTrue(fruitLoc[1] >= 0);

        assertEquals(2, gameGrid[fruitLoc[1] - 1][fruitLoc[0] - 1]);
        assertNotEquals(2, gameGrid[originalFruitLoc[1] - 1][originalFruitLoc[0] - 1]);
    }

    @Test
    public void testDownNextToRightWall() {
        GameState gameState = new GameState();

        int[] originalFruitLoc = gameState.getFruit().getLoc();

        for (int i = 0; i < 10; i++) {
            gameState.moveSnakeHead(Direction.RIGHT);
        }
        gameState.moveSnakeHead(Direction.UP);

        int[] first = gameState.getSnakePath().pop(); // tail
        int[] last = gameState.getSnakePath().removeLast(); // head

        assertTrue(gameState.isAlive());

        assertEquals(12, first[0]); // doesn't shift the tail after eating fruit
        assertEquals(7, first[1]);
        assertEquals(15, last[0]);
        assertEquals(6, last[1]);

        int[][] gameGrid = gameState.getGameGrid();

        assertEquals(1, gameGrid[6][14]);
        assertEquals(1, gameGrid[6][13]);
        assertEquals(1, gameGrid[6][12]);
        assertEquals(1, gameGrid[6][11]);
        assertEquals(1, gameGrid[5][14]);

        int[] fruitLoc = gameState.getFruit().getLoc();

        assertTrue(fruitLoc[0] <= 15);
        assertTrue(fruitLoc[0] >= 0);
        assertTrue(fruitLoc[1] <= 15);
        assertTrue(fruitLoc[1] >= 0);

        assertEquals(2, gameGrid[fruitLoc[1] - 1][fruitLoc[0] - 1]);
        assertNotEquals(2, gameGrid[originalFruitLoc[1] - 1][originalFruitLoc[0] - 1]);
    }

    @Test
    public void testHitsItself() {
        GameState gameState = new GameState();

        int[] originalFruitLoc = gameState.getFruit().getLoc();

        gameState.moveSnakeHead(Direction.RIGHT);
        gameState.moveSnakeHead(Direction.UP);
        gameState.moveSnakeHead(Direction.LEFT);
        gameState.moveSnakeHead(Direction.DOWN);

        int[] first = gameState.getSnakePath().pop(); // tail
        int[] last = gameState.getSnakePath().removeLast(); // head

        assertFalse(gameState.isAlive());

        assertEquals(5, first[0]); // doesn't shift the tail after eating fruit
        assertEquals(7, first[1]);
        assertEquals(5, last[0]);
        assertEquals(6, last[1]);

        int[][] gameGrid = gameState.getGameGrid();

        assertEquals(1, gameGrid[6][5]);
        assertEquals(1, gameGrid[6][4]);
        assertEquals(1, gameGrid[5][5]);
        assertEquals(1, gameGrid[5][4]);

        int[] fruitLoc = gameState.getFruit().getLoc();

        assertTrue(fruitLoc[0] <= 15);
        assertTrue(fruitLoc[0] >= 0);
        assertTrue(fruitLoc[1] <= 15);
        assertTrue(fruitLoc[1] >= 0);

        assertEquals(2, gameGrid[fruitLoc[1] - 1][fruitLoc[0] - 1]);
    }
}
