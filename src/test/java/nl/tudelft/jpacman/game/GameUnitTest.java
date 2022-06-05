package nl.tudelft.jpacman.game;

import nl.tudelft.jpacman.level.Level;
import nl.tudelft.jpacman.level.Player;
import nl.tudelft.jpacman.points.PointCalculator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

/**
 * Checks the branch coverage of game.Start() method.
 */

public class GameUnitTest {

    private Game game;
    private Level level;

    @BeforeEach
    public void initTests() {
        Player player = Mockito.mock(Player.class);
        level = Mockito.mock(Level.class);
        PointCalculator pointCalculator = Mockito.mock(PointCalculator.class);
        game = new SinglePlayerGame(player, level, pointCalculator);
    }

    @Test
    public void checkIfGameStopsWhenNoPlayerAlive() {
        when(level.isAnyPlayerAlive()).thenReturn(false);
        when(level.remainingPellets()).thenReturn(1);
        game.start();
        assertThat(game.isInProgress()).isEqualTo(false);
    }


    @Test
    public void checkIfGameStopsWhenNoPallet() {
        when(level.isAnyPlayerAlive()).thenReturn(true);
        when(level.remainingPellets()).thenReturn(0);
        game.start();
        assertThat(game.isInProgress()).isEqualTo(false);
    }


    @Test
    public void checkIfGameContinueWhenPlayerAlive() {
        when(level.isAnyPlayerAlive()).thenReturn(true);
        when(level.remainingPellets()).thenReturn(1);
        game.start();
        assertThat(game.isInProgress()).isEqualTo(true);
        game.start();
    }
}
