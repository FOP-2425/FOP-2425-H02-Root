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
     * Sets the background color of a field at the specified coordinates. The color is derived from the
     * {@link RobotFamily} SQUARE_BLUE or SQUARE_RED.
     * @param x the x coordinate of the field
     * @param y the y coordinate of the field
     * @param color the {@link RobotFamily} corresponding to the field color to set
     */
    @SolutionOnly
    void setFieldColor(final int x, final int y, final RobotFamily color) {
        if (color != RobotFamily.SQUARE_BLUE && color != RobotFamily.SQUARE_RED)
            throw new IllegalArgumentException("RobotFamily color must be either RobotFamily.SQUARE_BLUE or RobotFamily.SQUARE_RED!");
        World.getGlobalWorld().setFieldColor(x, y, color == RobotFamily.SQUARE_BLUE ? Color.BLUE : Color.RED);
    }

    /**
     * Executes the main game loop, handling player turns, coin drops, and win condition checks.
     * This method initializes the game board as a 2D array of RobotFamily colors, representing
     * the slots that can be filled with players' coins. It starts with a predefined currentPlayer
     * and continues in a loop until a win condition is met. Each iteration of the loop waits for
     * player input to select a column to drop a coin into, switches the current player, drops the
     * coin in the selected column, and checks for win conditions. If a win condition is met, the
     * loop ends, and the winner is displayed.
     */
    @StudentImplementationRequired("H2.2.4")
    void gameLoop() {
        final RobotFamily[][] coins = new RobotFamily[World.getHeight()][World.getWidth()];
        RobotFamily currentPlayer = RobotFamily.SQUARE_BLUE;

        finished = false;
        while (!finished) {
            currentPlayer = switchPlayer(currentPlayer);
            final int column = inputHandler.getNextInput(currentPlayer);

            dropCoin(column, coins, currentPlayer);
            finished = testWinConditions(coins, currentPlayer);
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
     * falling coin.
     *
     * @param column The column index where the coin is to be dropped.
     * @param coins  2D array representing the game board, where each cell contains a RobotFamily
     *               object indicating the player that has placed a coin in that position.
     * @return Index of the next unoccupied row index in the specified column.
     */
    @StudentImplementationRequired("H2.2.2")
    int getDestinationRow(final int column, final RobotFamily[][] coins) {
        for (int row = 0; row < coins.length; row++) {
            if (coins[row][column] == null) return row;
        }
        return coins.length - 1;
    }

    /**
     * Drops a coin into the specified column of the game board, simulating a falling animation.
     * This method gets the destination row for the coin in the specified column with the `getDestinationRow` method.
     * It creates a new Robot instance to represent the coin with the currentPlayer's RobotFamily in the given column
     * and the destination row. After that it simulates the coin's fall by decrementing its position until it reaches
     * the destination row. Once the  coin reaches its destination, the method updates the coins array (a 2D array of
     * RobotFamily colors) to mark the slot as occupied by the currentPlayer.
     *
     * @param column        The column index where the coin is to be dropped.
     * @param coins         2D array representing the game board, where each cell contains a RobotFamily
     *                      object indicating the player that has placed a coin in that position.
     * @param currentPlayer The RobotFamily object representing the current player dropping the coin.
     */
    @StudentImplementationRequired("H2.2.2")
    void dropCoin(final int column, final RobotFamily[][] coins, final RobotFamily currentPlayer) {
        int row = getDestinationRow(column, coins);

        // spawn coin
        Robot coin = new Robot(column, World.getHeight() - 1, Direction.DOWN, 0, currentPlayer);

        // let coin fall
        for (int currentRow = World.getHeight() - 1; currentRow > row; currentRow--) {
            coin.move();
        }

        // turn coin up
        coin.turnLeft();
        coin.turnLeft();

        // set slot as occupied
        coins[row][column] = currentPlayer;
    }

    /**
     * Checks if the given field is occupied by a robot. The input coordinates are expected to be valid.
     *
     * @param x the x-coordinate of the field
     * @param y the y-coordinate of the field
     * @return true if the field is occupied by a robot, false otherwise
     */
    @DoNotTouch
    public static boolean isOccupied(final int x, final int y) {
        return World.getGlobalWorld().getField(x, y).getEntities().stream().anyMatch(Robot.class::isInstance);
    }

    /**
     * Validates if a given column index is within the bounds of the game board and not fully occupied.
     *
     * @param column The column index to validate.
     * @return true if the column is within bounds and has at least one unoccupied cell; false otherwise.
     */
    @StudentImplementationRequired("H2.2.1")
    public boolean validateInput(final int column) {
        if (column < 0 || column >= World.getWidth()) {
            return false;
        }
        return !isOccupied(column, World.getHeight() - 1);
    }

    /**
     * Checks if the current player has won by any condition. The conditions can be a horizontal, vertical, diagonal,
     * or anti-diagonal line of at least four coins.
     *
     * @param coins         2D array representing the game board, where each cell contains a RobotFamily
     *                      color indicating the player that has placed a coin in that position.
     * @param currentPlayer The RobotFamily color representing the current player to check for a win.
     * @return true if the current player has formed a horizontal line of at least four coins; false otherwise.
     */
    @StudentImplementationRequired("H2.2.3")
    boolean testWinConditions(final RobotFamily[][] coins, final RobotFamily currentPlayer) {
        return testWinVertical(coins, currentPlayer) || testWinHorizontal(coins, currentPlayer) || testWinDiagonal(coins, currentPlayer) || testWinAntiDiagonal(coins, currentPlayer);
    }

    /**
     * Checks if the current player has won by forming a horizontal line of at least four coins.
     *
     * @param coins         2D array representing the game board, where each cell contains a RobotFamily
     *                      color indicating the player that has placed a coin in that position.
     * @param currentPlayer The RobotFamily color representing the current player to check for a win.
     * @return true if the current player has formed a horizontal line of at least four coins; false otherwise.
     */
    @StudentImplementationRequired("H2.2.3")
    boolean testWinHorizontal(final RobotFamily[][] coins, final RobotFamily currentPlayer) {
        int coinCount = 0;
        for (int row = 0; row < World.getHeight(); row++) {
            for (int column = 0; column < World.getWidth(); column++) {
                if (coins[row][column] == currentPlayer) coinCount++;
                else coinCount = 0;
                if (coinCount >= 4) return true;
            }

        }
        return false;
    }

    /**
     * Checks if the current player has won by forming a vertical line of at least four coins.
     *
     * @param coins         2D array representing the game board, where each cell contains a RobotFamily
     *                      color indicating the player that has placed a coin in that position.
     * @param currentPlayer The RobotFamily color representing the current player to check for a win.
     * @return true if the current player has formed a vertical line of at least four coins; false otherwise.
     */
    @StudentImplementationRequired("H2.2.3")
    boolean testWinVertical(final RobotFamily[][] coins, final RobotFamily currentPlayer) {
        int coinCount = 0;
        for (int column = 0; column < World.getWidth(); column++) {
            for (int row = 0; row < World.getHeight(); row++) {
                if (coins[row][column] == currentPlayer) coinCount++;
                else coinCount = 0;
                if (coinCount >= 4) return true;
            }
            coinCount = 0;
        }
        return false;
    }

    /**
     * Checks if the current player has won by forming a diagonal (bottom left to top right) line of at least four coins.
     *
     * @param coins         2D array representing the game board, where each cell contains a RobotFamily
     *                      color indicating the player that has placed a coin in that position.
     * @param currentPlayer The RobotFamily color representing the current player to check for a win.
     * @return true if the current player has formed a diagonal line of at least four coins; false otherwise.
     */
    @DoNotTouch
    boolean testWinDiagonal(final RobotFamily[][] coins, final RobotFamily currentPlayer) {
        int coinCount = 0;
        final int MAX_COINS = 4;

        final int WIDTH = World.getWidth();
        final int HEIGHT = World.getHeight();

        final int SMALL_SIDE = Math.min(WIDTH, HEIGHT);

        // upper left triangle
        for (int i = 0; i < SMALL_SIDE; i++) {
            for (int j = 0; j < SMALL_SIDE - i; j++) {
                final int x = j;
                final int y = HEIGHT - SMALL_SIDE + i + j;

                if (coins[y][x] == currentPlayer) coinCount++;
                else coinCount = 0;
                if (coinCount >= MAX_COINS) return true;
            }
            coinCount = 0;
        }

        // center
        if (WIDTH == SMALL_SIDE) {
            for (int i = 1; i < HEIGHT - SMALL_SIDE; i++) {
                for (int j = 0; j < SMALL_SIDE; j++) {
                    final int x = j;
                    final int y = i + j;

                    if (coins[y][x] == currentPlayer) coinCount++;
                    else coinCount = 0;
                    if (coinCount >= MAX_COINS) return true;
                }
                coinCount = 0;
            }
        } else {
            for (int i = 1; i < WIDTH - SMALL_SIDE; i++) {
                for (int j = 0; j < SMALL_SIDE; j++) {
                    final int x = i + j;
                    final int y = j;

                    if (coins[y][x] == currentPlayer) coinCount++;
                    else coinCount = 0;
                    if (coinCount >= MAX_COINS) return true;
                }
                coinCount = 0;
            }
        }

        // lower right triangle
        for (int i = 0; i < SMALL_SIDE; i++) {
            for (int j = 0; j < SMALL_SIDE - i; j++) {
                final int x = WIDTH - 1 - j;
                final int y = SMALL_SIDE - 1 - (i + j);

                if (coins[y][x] == currentPlayer) coinCount++;
                else coinCount = 0;
                if (coinCount >= MAX_COINS) return true;
            }
            coinCount = 0;
        }

        return false;
    }

    /**
     * Checks if the current player has won by forming a diagonal (top left to bottom right) line of at least four coins.
     *
     * @param coins         2D array representing the game board, where each cell contains a RobotFamily
     *                      color indicating the player that has placed a coin in that position.
     * @param currentPlayer The RobotFamily color representing the current player to check for a win.
     * @return true if the current player has formed a diagonal line of at least four coins; false otherwise.
     */
    @DoNotTouch
    boolean testWinAntiDiagonal(final RobotFamily[][] coins, final RobotFamily currentPlayer) {
        int coinCount = 0;
        final int MAX_COINS = 4;

        final int WIDTH = World.getWidth();
        final int HEIGHT = World.getHeight();

        final int SMALL_SIDE = Math.min(WIDTH, HEIGHT);

        // lower left triangle
        for (int i = 0; i < SMALL_SIDE; i++) {
            for (int j = 0; j < SMALL_SIDE - i; j++) {
                final int x = SMALL_SIDE - 1 - (i + j);
                final int y = j;

                if (coins[y][x] == currentPlayer) coinCount++;
                else coinCount = 0;
                if (coinCount >= MAX_COINS) return true;
            }
            coinCount = 0;
        }

        // center
        if (WIDTH == SMALL_SIDE) {
            for (int i = 1; i < HEIGHT - SMALL_SIDE; i++) {
                for (int j = 0; j < SMALL_SIDE; j++) {
                    final int x = WIDTH - 1 - j;
                    final int y = i + j;

                    if (coins[y][x] == currentPlayer) coinCount++;
                    else coinCount = 0;
                    if (coinCount >= MAX_COINS) return true;
                }
                coinCount = 0;
            }
        } else {
            for (int i = 1; i < WIDTH - SMALL_SIDE; i++) {
                for (int j = 0; j < SMALL_SIDE; j++) {
                    final int x = WIDTH - 1 - i - j;
                    final int y = j;

                    if (coins[y][x] == currentPlayer) coinCount++;
                    else coinCount = 0;
                    if (coinCount >= MAX_COINS) return true;
                }
                coinCount = 0;
            }
        }

        // upper right triangle
        for (int i = 0; i < SMALL_SIDE; i++) {
            for (int j = 0; j < SMALL_SIDE - i; j++) {
                final int x = WIDTH - SMALL_SIDE + i + j;
                final int y = HEIGHT - 1 - j;

                if (coins[y][x] == currentPlayer) coinCount++;
                else coinCount = 0;
                if (coinCount >= MAX_COINS) return true;
            }
            coinCount = 0;
        }

        return false;
    }

}
