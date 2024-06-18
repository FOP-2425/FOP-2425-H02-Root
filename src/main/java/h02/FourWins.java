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
import java.awt.Point;
import java.util.HashMap;
import java.util.Map;

/**
 * The {@link FourWins} class represents the main class of the FourWins game.
 */
public class FourWins {
    private final InputHandler inputHandler = new InputHandler(this);

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
                World.getGlobalWorld().setFieldColor(x, y, winner == RobotFamily.SQUARE_BLUE ? Color.BLUE : Color.RED);
            }
        }
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

        boolean finished = false;
        while (!finished) {
            final int column = inputHandler.getNextInput();

            currentPlayer = switchPlayer(currentPlayer);
            final var pos = dropCoin(column, coins, currentPlayer);
            finished = testWinConditions(coins, currentPlayer, pos);
        }

        displayWinner(currentPlayer);
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
    Point dropCoin(final int column, final RobotFamily[][] coins, final RobotFamily currentPlayer) {
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
        return new Point(column, row);
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
    boolean testWinConditions(final RobotFamily[][] coins, final RobotFamily currentPlayer, final Point placement) {
        return testWinEfficient(coins, currentPlayer, placement);
    }

    /**
     * Checks if the current coordinates are within the bounds of the game board.
     *
     * @param x The x-coordinate to check.
     * @param y The y-coordinate to check.
     * @return true if the coordinates are within the bounds of the game board; false otherwise.
     */
    @SolutionOnly("Hilfsmethode nicht an Studis weiter geben")
    private boolean isValidCoordinate(final int x, final int y) {
        return x >= 0 && x < World.getWidth() && y >= 0 && y < World.getHeight();
    }

    /**
     * Checks if the current player has won by any condition. The conditions can be a horizontal, vertical, diagonal,
     * or anti-diagonal line of at least four coins.
     *
     * @param coins         2D array representing the game board, where each cell contains a RobotFamily
     * @param currentPlayer The RobotFamily color representing the current player to check for a win.
     * @param placement     The coordinates of the last placed coin.
     * @return true if the current player has formed a horizontal line of at least four coins; false otherwise.
     */
    public boolean testWinEfficient(final RobotFamily[][] coins, final RobotFamily currentPlayer, final Point placement) {
        final Map<Point, Integer> coinCounts = new HashMap<>();
        for (int y = -1; y <= 1; y++) {
            for (int x = -1; x <= 1; x++) {
                if (x == 0 && y == 0) continue;
                final Point pos = new Point(placement.x, placement.y);
                int coinCount = -1;
                while (isValidCoordinate(pos.x, pos.y) && coins[pos.y][pos.x] == currentPlayer) {
                    coinCount++;
                    if (coinCount + coinCounts.getOrDefault(new Point(-x, -y), 0) + 1 >= 4) return true;
                    pos.translate(x, y);
                }
                coinCounts.put(new Point(x, y), coinCount);
            }
        }
        return false;
    }
}
