package org.cis1200.snake;

import java.io.*;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.Random;

/**
 * GameState
 *
 * This class holds the primary game logic for how different objects interact
 * with one another. Take time to understand how the timer interacts with the
 * different methods and how it repaints the GUI on every tick().
 */
public class GameState {
    private int[][] gameGrid = new int[15][15]; // [row][col]
    private int[] snakeHead = new int[] { 5, 7 };
    private Direction currentDirection = Direction.RIGHT;
    private LinkedList<int[]> snakePath = new LinkedList<>();
    private boolean isAlive = true;
    private boolean startGame = false;
    private ParentFruit fruit = new Fruit(true, gameGrid);
    private int score = 0;
    static final String PATH_TO_SAFE_FILE = "files/save_file.txt";

    public GameState() {
        // Snake path
        snakePath.add(new int[] { 2, 7 });
        snakePath.add(new int[] { 3, 7 });
        snakePath.add(new int[] { 4, 7 });
        snakePath.add(new int[] { 5, 7 }); // last

        // Body of the snake
        gameGrid[6][4] = 1;
        gameGrid[6][3] = 1;
        gameGrid[6][2] = 1;
        gameGrid[6][1] = 1;

        // Default fruit location
        gameGrid[6][8] = 2;
    }

    public static int convertInt(int i) {
        return ((50 * (i - 1)) + 3);
    }

    public int[] getSnakeHead() {
        return snakeHead;
    }

    public LinkedList<int[]> getSnakePath() {
        return snakePath;
    }

    public ParentFruit getFruit() {
        return fruit;
    }

    public boolean isAlive() {
        return isAlive;
    }

    public int getScore() {
        return score;
    }

    public int[][] getGameGrid() {
        return gameGrid;
    }

    public Direction getCurrentDirection() {
        return currentDirection;
    }

    public void setStartGame(boolean startGame) {
        this.startGame = startGame;
    }

    public void moveSnakeHead(Direction d) { // returns if the snake collides
        if (d != null) {
            // Checks to see for "invalid" directions
            if (!((d.equals(Direction.RIGHT) && this.currentDirection.equals(Direction.LEFT)) ||
                    (d.equals(Direction.LEFT) && this.currentDirection.equals(Direction.RIGHT)) ||
                    (d.equals(Direction.UP) && this.currentDirection.equals(Direction.DOWN)) ||
                    (d.equals(Direction.DOWN) && this.currentDirection.equals(Direction.UP)))) {
                currentDirection = d;
                startGame = true;
            }

            if (startGame) {
                // Test to see if next move is valid
                int[] testHead = snakeHead.clone();

                switch (currentDirection) {
                    case UP:
                        testHead[1] -= 1;
                        break;
                    case DOWN:
                        testHead[1] += 1;
                        break;
                    case LEFT:
                        testHead[0] -= 1;
                        break;
                    case RIGHT:
                        testHead[0] += 1;
                        break;
                    default:
                        break;
                }

                if (testHead[0] > 15 || testHead[0] <= 0 || testHead[1] > 15 || testHead[1] <= 0 ||
                        gameGrid[testHead[1] - 1][testHead[0] - 1] == 1) {
                    this.isAlive = false;
                }

                if (this.isAlive) {
                    switch (currentDirection) {
                        case UP:
                            this.snakeHead[1] -= 1;
                            break;
                        case DOWN:
                            this.snakeHead[1] += 1;
                            break;
                        case LEFT:
                            this.snakeHead[0] -= 1;
                            break;
                        case RIGHT:
                            this.snakeHead[0] += 1;
                            break;
                        default:
                            break;
                    }

                    // Updates fruit
                    boolean grow = fruit.update(gameGrid, snakeHead);

                    // Adds previous head pos to the path
                    snakePath.add(snakeHead.clone());

                    // Clears the tail game grid
                    assert snakePath.peekFirst() != null;
                    int[] tempSnakeTail = snakePath.peekFirst().clone();

                    // Checks if snake is growing, don't remove if growing
                    if (!grow) {
                        snakePath.removeFirst();
                    } else {
                        Random rand = new Random();

                        int randSelector = rand.nextInt(6);

                        if (randSelector == 0) {
                            fruit = new CornerFruit(false, gameGrid);
                        } else if (randSelector == 1 || randSelector == 2) {
                            fruit = new SideFruit(false, gameGrid);
                        } else {
                            fruit = new Fruit(false, gameGrid);
                        }

                        score += 1;
                    }

                    // Clearing old tail
                    gameGrid[tempSnakeTail[1] - 1][tempSnakeTail[0] - 1] = 0;

                    // Updates path on game grid, using for each loop to update after loading
                    for (int[] pos : snakePath) {
                        gameGrid[pos[1] - 1][pos[0] - 1] = 1;
                    }
                }
            }
        }
    }

    public void saveState() {
        try {
            File file = Paths.get(PATH_TO_SAFE_FILE).toFile();

            FileWriter fw = new FileWriter(file, false);
            BufferedWriter bw = new BufferedWriter(fw);

            // Head Location
            bw.write(snakeHead[0] + " " + snakeHead[1]);
            bw.newLine();

            // Fruit Location
            bw.write(fruit.getLoc()[0] + " " + fruit.getLoc()[1]);
            bw.newLine();

            // Score
            bw.write(String.valueOf(score));
            bw.newLine();

            // Path Linked List
            for (int[] loc : snakePath) {
                bw.write(loc[0] + " " + loc[1] + ",");
            }
            bw.newLine();

            // Direction
            bw.write(String.valueOf(currentDirection));

            bw.flush();
            bw.close();

        } catch (IOException e) {
            System.out.println("Error Saving, Try Again! Error: " + e);
        }
    }

    public boolean testLoadState() {
        try {
            FileReader file = new FileReader(PATH_TO_SAFE_FILE);
            BufferedReader bw = new BufferedReader(file);

            // Snake Head
            String snakeHeadStr = bw.readLine();
            String[] snakeHeadStrList = snakeHeadStr.split(" ");

            if (snakeHeadStrList.length != 2) {
                return false;
            }

            int[] testSnakeHead = new int[] { Integer.parseInt(snakeHeadStrList[0]),
                Integer.parseInt(snakeHeadStrList[1]) };

            if (testSnakeHead[0] > 15 || testSnakeHead[1] > 15 ||
                    testSnakeHead[0] < 0 || testSnakeHead[1] < 0) {
                System.out.println("ERROR SNAKE HEAD");
                return false;
            }

            // Fruit Loc
            String fruitLocStr = bw.readLine();
            String[] fruitLocStrList = fruitLocStr.split(" ");

            if (fruitLocStrList.length != 2) {
                return false;
            }

            int[] testFruitLoc = new int[] { Integer.parseInt(fruitLocStrList[0]),
                Integer.parseInt(fruitLocStrList[1]) };

            if (testFruitLoc[0] > 15 || testFruitLoc[1] > 15 ||
                    testFruitLoc[0] < 0 || testFruitLoc[1] < 0) {
                System.out.println("ERROR FRUIT LOC");
                return false;
            }

            // Score
            String scoreStr = bw.readLine();
            int testScore = Integer.parseInt(scoreStr);

            if (testScore < 0) {
                System.out.println("ERROR SCORE");
                return false;
            }

            // Snake Path
            LinkedList<int[]> testSnakePath = new LinkedList<>();
            String pathStr = bw.readLine();
            pathStr = pathStr.substring(0, pathStr.length() - 1); // remove the last ","

            String[] pathStrList = pathStr.split(",");

            if (pathStrList.length != (4 + testScore)) {
                System.out.println("ERROR LENGTH");
                return false;
            } else {
                for (String segStr : pathStrList) {
                    String[] segStrList = segStr.split(" ");

                    if (segStrList.length != 2) {
                        return false;
                    }

                    int[] testSeg = new int[] { Integer.parseInt(segStrList[0]),
                        Integer.parseInt(segStrList[1]) };

                    if (testSnakePath.contains(testSeg)) {
                        System.out.println("DUPLICATE SEGMENTS");
                        return false;
                    }

                    if (testSeg[0] > 15 || testSeg[1] > 15 || testSeg[0] < 0 || testSeg[1] < 0) {
                        System.out.println("PATH OUTSIDE OF BOUNDS");
                        return false;
                    } else {
                        if (testSnakePath.isEmpty()) {
                            testSnakePath.add(
                                    new int[] { Integer.parseInt(segStrList[0]),
                                        Integer.parseInt(segStrList[1]) }
                            );
                        } else {
                            if (testSeg[0] == testSnakePath.peekLast()[0] + 1 ||
                                    testSeg[0] == testSnakePath.peekLast()[0] - 1 ||
                                    testSeg[1] == testSnakePath.peekLast()[1] - 1 ||
                                    testSeg[1] == testSnakePath.peekLast()[1] + 1) {
                                testSnakePath.add(
                                        new int[] { Integer.parseInt(segStrList[0]),
                                            Integer.parseInt(segStrList[1]) }
                                );
                            } else {
                                System.out.println("ERROR NOT PATH LINKED");
                                return false;
                            }
                        }
                    }
                }

                if (testSnakePath.peekLast()[0] != testSnakeHead[0] ||
                        testSnakePath.peekLast()[1] != testSnakeHead[1]) {
                    System.out.println("ERROR PATH NOT LINKED WITH HEAD");
                    return false;
                }
            }

            // Direction
            Direction testDirection = Direction.valueOf(bw.readLine());

            System.out.println("\nCHECKED CORRECTLY\n");

            return true;
        } catch (NullPointerException | IOException | IllegalArgumentException e) {
            return false;
        }
    }

    public void loadState() {
        try {
            FileReader file = new FileReader(PATH_TO_SAFE_FILE);
            BufferedReader bw = new BufferedReader(file);

            // Snake Head
            String snakeHeadStr = bw.readLine();
            String[] snakeHeadStrList = snakeHeadStr.split(" ");
            snakeHead = new int[] { Integer.parseInt(snakeHeadStrList[0]),
                Integer.parseInt(snakeHeadStrList[1]) };

            // Fruit Loc
            String fruitLocStr = bw.readLine();
            String[] fruitLocStrList = fruitLocStr.split(" ");
            int[] fruitLoc = new int[] { Integer.parseInt(fruitLocStrList[0]),
                Integer.parseInt(fruitLocStrList[1]) };

            if ((fruitLoc[0] == 1 && fruitLoc[1] == 1) || (fruitLoc[0] == 1 && fruitLoc[1] == 15) ||
                    (fruitLoc[0] == 15 && fruitLoc[1] == 1)
                    || (fruitLoc[0] == 15 && fruitLoc[1] == 15)) {
                fruit = new CornerFruit(false, gameGrid);
            } else if (fruitLoc[0] == 1 || fruitLoc[1] == 1 || fruitLoc[0] == 15
                    || fruitLoc[1] == 15) {
                fruit = new SideFruit(false, gameGrid);
            }

            fruit.setLoc(fruitLoc);

            // Score
            String scoreStr = bw.readLine();
            score = Integer.parseInt(scoreStr);

            // Snake Path
            snakePath = new LinkedList<>();
            String pathStr = bw.readLine();
            pathStr = pathStr.substring(0, pathStr.length() - 1); // remove the last ","
            String[] pathStrList = pathStr.split(",");

            for (String segStr : pathStrList) {
                String[] segStrList = segStr.split(" ");
                snakePath.add(
                        new int[] { Integer.parseInt(segStrList[0]),
                            Integer.parseInt(segStrList[1]) }
                );
            }

            // Direction
            currentDirection = Direction.valueOf(bw.readLine());

            // Game Grid
            gameGrid = new int[15][15];
            for (int[] loc : snakePath) {
                gameGrid[loc[1] - 1][loc[0] - 1] = 1;
            }

            gameGrid[Integer.parseInt(fruitLocStrList[1]) - 1][Integer.parseInt(fruitLocStrList[0])
                    - 1] = 2;
        } catch (NullPointerException | IOException | IllegalArgumentException e) {
            System.out.println("ERROR OCCURRED: " + e);
        }
    }
}