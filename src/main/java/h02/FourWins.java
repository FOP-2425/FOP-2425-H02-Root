package h02;

import fopbot.Direction;
import fopbot.Robot;
import fopbot.RobotFamily;
import fopbot.World;
import h02.template.InputHandler;
import org.tudalgo.algoutils.student.annotation.DoNotTouch;
import org.tudalgo.algoutils.student.annotation.SolutionOnly;
import org.tudalgo.algoutils.student.annotation.StudentImplementationRequired;

import java.awt.Color;

/**
 * The {@link FourWins} class represents the main class of the FourWins game.
 */
public class FourWins {
    private final InputHandler inputHandler = new InputHandler(this);

    private boolean finished = false;

    void startGame() {
        setupWorld();
        gameLoop();
    }

    void setupWorld() {
        World.setSize(7, 6);
        World.setDelay(10);
        World.setVisible(true);
        inputHandler.install();
    }


    /**
     * Switches the player for each turn. If the current player is SQUARE_BLUE, SQUARE_RED is returned as the next
     * player. If the current player is SQUARE_RED, SQUARE_BLUE is returned as the next player.
     *
     * @param currentPlayer The player color of the current player.
     * @return The player color of the next player.
     */
    @StudentImplementationRequired("H2.2.4")
    RobotFamily switchPlayer(final RobotFamily currentPlayer) {
        if (currentPlayer == RobotFamily.SQUARE_BLUE) return RobotFamily.SQUARE_RED;
        return RobotFamily.SQUARE_BLUE;
    }

    /**
     * Displays the winner of the game by printing the winning color in the console and filling the whole field
     * with Robots of the winning color.
     *
     * @param winner The RobotFamily color of the winner.
     */
    @StudentImplementationRequired("H2.2.4")
    void displayWinner(final RobotFamily winner) {
        System.out.println("Player of color " + winner + " wins the game!");

        for (int x = 0; x < World.getWidth(); x++) {
            for (int y = 0; y < World.getHeight(); y++) {
                setFieldColor(x, y, winner);
            }
        }
    }

    /**
     * Executes the main game loop, handling player turns, stone drops, and win condition checks.
     * Sets the background color of a field at the specified coordinates. The color is derived from the
     * {@link RobotFamily} SQUARE_BLUE or SQUARE_RED.
     * @param x the x coordinate of the field
     * @param y the y coordinate of the field
     * @param color the {@link RobotFamily} corresponding to the field color to set
     */
    @DoNotTouch
    void setFieldColor(final int x, final int y, final RobotFamily color) {
        World.getGlobalWorld().setFieldColor(x, y, color.getColor());
    }

    /**
     * Executes the main game loop, handling player turns, stone drops, and win condition checks.
     * This method initializes the game board as a 2D array of RobotFamily colors, representing
     * the slots that can be filled with players' stones. It starts with a predefined currentPlayer
     * and continues in a loop until a win condition is met. Each iteration of the loop waits for
     * player input to select a column to drop a stone into, switches the current player, drops the
     * stone in the selected column, and checks for win conditions. If a win condition is met, the
     * loop ends, and the winner is displayed.
     */
    @StudentImplementationRequired("H2.2.4")
    void gameLoop() {
        final RobotFamily[][] stones = new RobotFamily[World.getHeight()][World.getWidth()];
        RobotFamily currentPlayer = RobotFamily.SQUARE_BLUE;

        finished = false;
        while (!finished) {
            currentPlayer = switchPlayer(currentPlayer);
            final int column = inputHandler.getNextInput(currentPlayer, stones);

            dropStone(column, stones, currentPlayer);
            finished = testWinConditions(stones, currentPlayer);
        }

        displayWinner(currentPlayer);
    }

    /**
     * Returns {@code true} when the game is finished, {@code false} otherwise.
     * @return whether the game is finished.
     */
    public boolean isFinished() {
        return finished;
    }

    /**
     * Calculates the next unoccupied row index in the specified column. This row index is the next destination for a
     * falling stone.
     *
     * @param column The column index where the stone is to be dropped.
     * @param stones  2D array representing the game board, where each cell contains a RobotFamily
     *               object indicating the player that has placed a stone in that position.
     * @return Index of the next unoccupied row index in the specified column.
     */
    @StudentImplementationRequired("H2.2.2")
    int getDestinationRow(final int column, final RobotFamily[][] stones) {
        for (int row = 0; row < stones.length; row++) {
            if (stones[row][column] == null) return row;
        }
        return stones.length - 1;
    }

    /**
     * Drops a stone into the specified column of the game board, simulating a falling animation.
     * This method gets the destination row for the stone in the specified column with the `getDestinationRow` method.
     * It creates a new Robot instance to represent the stone with the currentPlayer's RobotFamily in the given column
     * and the destination row. After that it simulates the stone's fall by decrementing its position until it reaches
     * the destination row. Once the  stone reaches its destination, the method updates the stones array (a 2D array of
     * RobotFamily colors) to mark the slot as occupied by the currentPlayer.
     *
     * @param column        The column index where the stone is to be dropped.
     * @param stones         2D array representing the game board, where each cell contains a RobotFamily
     *                      object indicating the player that has placed a stone in that position.
     * @param currentPlayer The RobotFamily object representing the current player dropping the stone.
     */
    @StudentImplementationRequired("H2.2.2")
    void dropStone(final int column, final RobotFamily[][] stones, final RobotFamily currentPlayer) {
        int row = getDestinationRow(column, stones);

        // spawn stone
        Robot stone = new Robot(column, World.getHeight() - 1, Direction.DOWN, 0, currentPlayer);

        // let stone fall
        for (int currentRow = World.getHeight() - 1; currentRow > row; currentRow--) {
            stone.move();
        }

        // turn stone up
        stone.turnLeft();
        stone.turnLeft();

        // set slot as occupied
        stones[row][column] = currentPlayer;
    }

    /**
     * Validates if a given column index is within the bounds of the game board and not fully occupied.
     *
     * @param column The column index to validate.
     * @return true if the column is within bounds and has at least one unoccupied cell; false otherwise.
     */
    @StudentImplementationRequired("H2.2.1")
    public boolean validateInput(final int column, RobotFamily[][] stones) {
        if (column < 0 || column >= World.getWidth()) return false;
        return stones[World.getHeight() - 1][column] == null;
    }

    /**
     * Checks if the current player has won by any condition. The conditions can be a horizontal, vertical, diagonal,
     * or anti-diagonal line of at least four stones.
     *
     * @param stones         2D array representing the game board, where each cell contains a RobotFamily
     *                      color indicating the player that has placed a stone in that position.
     * @param currentPlayer The RobotFamily color representing the current player to check for a win.
     * @return true if the current player has formed a horizontal line of at least four stones; false otherwise.
     */
    @StudentImplementationRequired("H2.2.3")
    boolean testWinConditions(final RobotFamily[][] stones, final RobotFamily currentPlayer) {
        return testWinVertical(stones, currentPlayer) || testWinHorizontal(stones, currentPlayer) || testWinDiagonal(stones, currentPlayer) || testWinAntiDiagonal(stones, currentPlayer);
    }

    /**
     * Checks if the current player has won by forming a horizontal line of at least four stones.
     *
     * @param stones         2D array representing the game board, where each cell contains a RobotFamily
     *                      color indicating the player that has placed a stone in that position.
     * @param currentPlayer The RobotFamily color representing the current player to check for a win.
     * @return true if the current player has formed a horizontal line of at least four stones; false otherwise.
     */
    @StudentImplementationRequired("H2.2.3")
    boolean testWinHorizontal(final RobotFamily[][] stones, final RobotFamily currentPlayer) {
        for (int row = 0; row < World.getHeight(); row++) {
            int stoneCount = 0;
            for (int column = 0; column < World.getWidth(); column++) {
                if (stones[row][column] == currentPlayer) stoneCount++;
                else stoneCount = 0;
                if (stoneCount >= 4) return true;
            }
        }
        return false;
    }

    /**
     * Checks if the current player has won by forming a vertical line of at least four stones.
     *
     * @param stones         2D array representing the game board, where each cell contains a RobotFamily
     *                      color indicating the player that has placed a stone in that position.
     * @param currentPlayer The RobotFamily color representing the current player to check for a win.
     * @return true if the current player has formed a vertical line of at least four stones; false otherwise.
     */
    @StudentImplementationRequired("H2.2.3")
    boolean testWinVertical(final RobotFamily[][] stones, final RobotFamily currentPlayer) {
        for (int column = 0; column < World.getWidth(); column++) {
            int stoneCount = 0;
            for (int row = 0; row < World.getHeight(); row++) {
                if (stones[row][column] == currentPlayer) stoneCount++;
                else stoneCount = 0;
                if (stoneCount >= 4) return true;
            }
        }
        return false;
    }

    /**
     * Checks if the current player has won by forming a diagonal (bottom left to top right) line of at least four stones.
     *
     * @param stones         2D array representing the game board, where each cell contains a RobotFamily
     *                      color indicating the player that has placed a stone in that position.
     * @param currentPlayer The RobotFamily color representing the current player to check for a win.
     * @return true if the current player has formed a diagonal line of at least four stones; false otherwise.
     */
    @DoNotTouch
    boolean testWinDiagonal(final RobotFamily[][] stones, final RobotFamily currentPlayer) {
        final int MAX_STONES = 4;

        final int WIDTH = World.getWidth();
        final int HEIGHT = World.getHeight();

        final int SMALL_SIDE = Math.min(WIDTH, HEIGHT);

        // check upper left triangle
        for (int i = 0; i < SMALL_SIDE; i++) {
            int stoneCount = 0;
            for (int j = 0; j < SMALL_SIDE - i; j++) {
                final int x = j;
                final int y = HEIGHT - SMALL_SIDE + i + j;

                if (stones[y][x] == currentPlayer) stoneCount++;
                else stoneCount = 0;
                if (stoneCount >= MAX_STONES) return true;
            }
        }

        // check center diagonals
        if (WIDTH == SMALL_SIDE) {
            for (int i = 1; i < HEIGHT - SMALL_SIDE; i++) {
                int stoneCount = 0;
                for (int j = 0; j < SMALL_SIDE; j++) {
                    final int x = j;
                    final int y = i + j;

                    if (stones[y][x] == currentPlayer) stoneCount++;
                    else stoneCount = 0;
                    if (stoneCount >= MAX_STONES) return true;
                }
            }
        } else {
            for (int i = 1; i < WIDTH - SMALL_SIDE; i++) {
                int stoneCount = 0;
                for (int j = 0; j < SMALL_SIDE; j++) {
                    final int x = i + j;
                    final int y = j;

                    if (stones[y][x] == currentPlayer) stoneCount++;
                    else stoneCount = 0;
                    if (stoneCount >= MAX_STONES) return true;
                }
            }
        }

        // check lower right triangle
        for (int i = 0; i < SMALL_SIDE; i++) {
            int stoneCount = 0;
            for (int j = 0; j < SMALL_SIDE - i; j++) {
                final int x = WIDTH - 1 - j;
                final int y = SMALL_SIDE - 1 - (i + j);

                if (stones[y][x] == currentPlayer) stoneCount++;
                else stoneCount = 0;
                if (stoneCount >= MAX_STONES) return true;
            }
        }

        return false;
    }

    /**
     * Checks if the current player has won by forming a diagonal (top left to bottom right) line of at least four stones.
     *
     * @param stones         2D array representing the game board, where each cell contains a RobotFamily
     *                      color indicating the player that has placed a stone in that position.
     * @param currentPlayer The RobotFamily color representing the current player to check for a win.
     * @return true if the current player has formed a diagonal line of at least four stones; false otherwise.
     */
    @DoNotTouch
    boolean testWinAntiDiagonal(final RobotFamily[][] stones, final RobotFamily currentPlayer) {
        final int MAX_STONES = 4;

        final int WIDTH = World.getWidth();
        final int HEIGHT = World.getHeight();

        final int SMALL_SIDE = Math.min(WIDTH, HEIGHT);

        // check lower left triangle
        for (int i = 0; i < SMALL_SIDE; i++) {
            int stoneCount = 0;
            for (int j = 0; j < SMALL_SIDE - i; j++) {
                final int x = SMALL_SIDE - 1 - (i + j);
                final int y = j;

                if (stones[y][x] == currentPlayer) stoneCount++;
                else stoneCount = 0;
                if (stoneCount >= MAX_STONES) return true;
            }
        }

        // check center anti diagonals
        if (WIDTH == SMALL_SIDE) {
            for (int i = 1; i < HEIGHT - SMALL_SIDE; i++) {
                int stoneCount = 0;
                for (int j = 0; j < SMALL_SIDE; j++) {
                    final int x = WIDTH - 1 - j;
                    final int y = i + j;

                    if (stones[y][x] == currentPlayer) stoneCount++;
                    else stoneCount = 0;
                    if (stoneCount >= MAX_STONES) return true;
                }
            }
        } else {
            for (int i = 1; i < WIDTH - SMALL_SIDE; i++) {
                int stoneCount = 0;
                for (int j = 0; j < SMALL_SIDE; j++) {
                    final int x = WIDTH - 1 - i - j;
                    final int y = j;

                    if (stones[y][x] == currentPlayer) stoneCount++;
                    if (stoneCount >= MAX_STONES) return true;
                }
            }
        }

        // check upper right triangle
        for (int i = 0; i < SMALL_SIDE; i++) {
            int stoneCount = 0;
            for (int j = 0; j < SMALL_SIDE - i; j++) {
                final int x = WIDTH - SMALL_SIDE + i + j;
                final int y = HEIGHT - 1 - j;

                if (stones[y][x] == currentPlayer) stoneCount++;
                else stoneCount = 0;
                if (stoneCount >= MAX_STONES) return true;
            }
        }

        return false;
    }

}
