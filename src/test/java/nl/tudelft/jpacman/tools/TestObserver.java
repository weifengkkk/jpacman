package nl.tudelft.jpacman.tools;

import nl.tudelft.jpacman.level.Level;

/**
 * Observer for testing if we won or lost.
 */
public class TestObserver implements Level.LevelObserver {
    private boolean observedWin = false;
    private boolean observedLoss = false;

    @Override
    public void levelWon() {
        observedWin = true;
    }

    @Override
    public void levelLost() {
        observedLoss = true;
    }

    /**
     * If we observed a win.
     * @return true if we observed a win, false if not.
     */
    public boolean isObservedWin() {
        return observedWin;
    }

    /**
     * If we observed a loss.
     * @return true if we observed a loss, false if not.
     */
    public boolean isObservedLoss() {
        return observedLoss;
    }
}
