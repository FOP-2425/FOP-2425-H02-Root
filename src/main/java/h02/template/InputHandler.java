package h02.template;

import fopbot.RobotFamily;
import fopbot.World;
import h02.FourWins;
import org.tudalgo.algoutils.student.annotation.DoNotTouch;

import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import java.awt.Color;
import java.beans.PropertyChangeEvent;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

/**
 * The {@link InputHandler} handles the input of the users.
 */
@DoNotTouch
public class InputHandler {
    /**
     * The input queue.
     */
    private final BlockingDeque<Integer> inputQueue = new LinkedBlockingDeque<>();
    /**
     * The {@link FourWins} instance.
     */
    private final FourWins fourWins;

    private final AtomicBoolean rowSelectMode = new AtomicBoolean(false);

    private final JLabel statusLabel = new JLabel("");

    /**
     * Creates a new {@link InputHandler} instance.
     *
     * @param fourWins the {@link FourWins} instance
     */
    public InputHandler(final FourWins fourWins) {
        this.fourWins = fourWins;
    }

    /**
     * Sets the color of the given column to the given color.
     *
     * @param column        the column to set the color of
     * @param colorSupplier the color to set
     */
    private void setColumnColor(final int column, final Supplier<Color> colorSupplier) {
        for (int i = 0; i < World.getHeight(); i++) {
            final int finalI = i;
            SwingUtilities.invokeLater(() -> World.getGlobalWorld().getField(column, finalI).setFieldColor(colorSupplier));
//            World.getGlobalWorld().getField(column, i).setFieldColor(color);
        }
    }

    private void whenGameIsRunning(final Runnable action) {
        if (!fourWins.isFinished()) {
            action.run();
        }
    }

    /**
     * Installs the input handler to the fopbot world.
     */
    public void install() {
        World.getGlobalWorld().getInputHandler().addFieldClickListener(e -> whenGameIsRunning(() -> addInput(e.getField().getX())));
        World.getGlobalWorld().getInputHandler().addFieldHoverListener(e -> whenGameIsRunning(() -> {
            // deselect last hovered field, if any
            if (e.getPreviousField() != null) {
                System.out.println("deselecting column " + e.getPreviousField().getX());
                setColumnColor(e.getPreviousField().getX(), () -> null);
            }
            if (rowSelectMode.get()) {
                // select current hovered field
                if (e.getField() != null) {
                    System.out.println("selecting column " + e.getField().getX());
                    setColumnColor(e.getField().getX(), () -> World.getGlobalWorld().getGuiPanel().isDarkMode()
                                                              ? Color.yellow
                                                              : Color.orange
                    );
                }
            }
        }));
        statusLabel.setFont(statusLabel.getFont().deriveFont(20.0f));
        World.getGlobalWorld().getGuiPanel().add(statusLabel, JLabel.CENTER);
        World.getGlobalWorld().getGuiPanel().addDarkModeChangeListener(this::onDarkModeChange);
        // trigger dark mode change to set the correct color
        World.getGlobalWorld().getGuiPanel().setDarkMode(World.getGlobalWorld().getGuiPanel().isDarkMode());
    }

    public void onDarkModeChange(final PropertyChangeEvent e) {
        final var darkMode = (boolean) e.getNewValue();
        statusLabel.setForeground(darkMode ? Color.white : Color.black);
    }

    /**
     * Adds an input to the input queue. When {@link #getNextInput(RobotFamily, RobotFamily[][])} is called, the program will wait until this method is called.
     *
     * @param input the input to add
     */
    public void addInput(final int input) {
        inputQueue.add(input);
    }

    /**
     * Returns the next input from the input queue. If the input is invalid, the user will be prompted to enter a new input.
     * The program will halt until a valid input is entered.
     *
     * @return the next input from the input queue
     */
    public int getNextInput(final RobotFamily currentPlayer, final RobotFamily[][] stones) {
        rowSelectMode.set(true);
        statusLabel.setText("<html>Click on a column to insert a disc.<br>Current Player: " + currentPlayer.name() + "</html>");
        try {
            final int input = inputQueue.take();
            System.out.println("Received input: " + input);
            if (!fourWins.validateInput(input, stones)) {
                System.out.println("Invalid input, please try again.");
                return getNextInput(currentPlayer, stones);
            }
            rowSelectMode.set(false);
            return input;
        } catch (final InterruptedException e) {
            rowSelectMode.set(false);
            throw new RuntimeException(e);
        }
    }
}
