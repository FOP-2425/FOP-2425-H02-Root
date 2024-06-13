package h02;

import fopbot.Robot;
import fopbot.World;
import h02.template.InputHandler;
import org.tudalgo.algoutils.student.annotation.StudentImplementationRequired;

/**
 * The {@link FourWins} class represents the main class of the FourWins game.
 */
public class FourWins {
    //TODO: implement 4

    private final InputHandler inputHandler = new InputHandler(this);

    void startGame() {
        setupWorld();
        gameLoop();
    }

    void setupWorld() {
        World.setSize(7, 6);
        World.setDelay(0);
        World.setVisible(true);
        inputHandler.install();
    }

    void gameLoop() {
        final boolean finished = false;
        while (!finished) {
            final int column = inputHandler.getNextInput();
            // TODO: place robot

        }
    }

    /**
     * Checks if the given field is occupied by a robot. The input coordinates are expected to be valid.
     *
     * @param x the x-coordinate of the field
     * @param y the y-coordinate of the field
     * @return true if the field is occupied by a robot, false otherwise
     */
    public static boolean isOccupied(final int x, final int y) {
        return World.getGlobalWorld().getField(x, y).getEntities().stream().anyMatch(Robot.class::isInstance);
    }

    @StudentImplementationRequired("H2.2.1")
    public boolean validateInput(final int column) {
        if (column < 0 || column >= World.getWidth()) {
            return false;
        }
        for (int y = 0; y < World.getHeight(); y++) {
            if (!isOccupied(column, y)) {
                return true;
            }
        }
        return false;
    }
}
