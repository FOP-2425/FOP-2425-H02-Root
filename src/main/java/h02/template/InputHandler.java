package h02.template;

import fopbot.World;
import h02.FourWins;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * The {@link InputHandler} handles the input of the users.
 */
public class InputHandler {
    /**
     * The input queue.
     */
    private final BlockingDeque<Integer> inputQueue = new LinkedBlockingDeque<>();
    /**
     * The {@link FourWins} instance.
     */
    private final FourWins fourWins;

    /**
     * Creates a new {@link InputHandler} instance.
     *
     * @param fourWins the {@link FourWins} instance
     */
    public InputHandler(final FourWins fourWins) {
        this.fourWins = fourWins;
    }

    /**
     * Installs the input handler to the fopbot world.
     */
    public void install() {
        World.getGlobalWorld().getInputHandler().addFieldClickListener(e -> addInput(e.getField().getX()));
    }

    /**
     * Adds an input to the input queue. When {@link #getNextInput()} is called, the program will wait until this method is called.
     *
     * @param input the input to add
     */
    public void addInput(final int input) {
        inputQueue.add(input);
    }

    /**
     * Returns the next input from the input queue. If the input is invalid, the user will be prompted to enter a new input.
     *
     * @return the next input from the input queue
     */
    public int getNextInput() {
        try {
            final int input = inputQueue.take();
            System.out.println("Received input: " + input);
            if (!fourWins.validateInput(input)) {
                System.out.println("Invalid input, please try again.");
                return getNextInput();
            }
            return input;
        } catch (final InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
