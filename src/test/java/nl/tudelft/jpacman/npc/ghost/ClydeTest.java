package nl.tudelft.jpacman.npc.ghost;

import nl.tudelft.jpacman.board.BoardFactory;
import nl.tudelft.jpacman.board.Direction;
import nl.tudelft.jpacman.level.Level;
import nl.tudelft.jpacman.level.LevelFactory;
import nl.tudelft.jpacman.level.Player;
import nl.tudelft.jpacman.level.PlayerFactory;
import nl.tudelft.jpacman.points.DefaultPointCalculator;
import nl.tudelft.jpacman.points.PointCalculator;
import nl.tudelft.jpacman.sprite.PacManSprites;
import nl.tudelft.jpacman.tools.Tools;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test if the ghost Clyde is behaving correctly.
 */
public class ClydeTest {
    private GhostMapParser ghostMapParser;
    private Player player;

    /**
     * Initializes the GhostMapParser for every test.
     */
    @BeforeEach
    public void initTests() {
        PacManSprites pacManSprites = new PacManSprites();
        GhostFactory ghostFactory = new GhostFactory(pacManSprites);
        PointCalculator pointCalculator = new DefaultPointCalculator();
        LevelFactory levelFactory = new LevelFactory(pacManSprites, ghostFactory, pointCalculator);
        BoardFactory boardFactory = new BoardFactory(pacManSprites);
        PlayerFactory playerFactory = new PlayerFactory(pacManSprites);
        player = playerFactory.createPacMan();
        ghostMapParser = new GhostMapParser(levelFactory, boardFactory, ghostFactory);
    }

    /**
     * Tests if Clyde moves closer to the player
     * if he is more than shyness(default=8) spaces away from player.
     */
    @Test
    public void testIfClydeMovesToPlayerIfMoreThanShynessSpacesAway() {
        char[][] map = new char[][]{
            "############".toCharArray(),
            "#P        C#".toCharArray(),
            "############".toCharArray()
        };
        Level level = ghostMapParser.parseMap(Tools.rotateMap(map));

        level.registerPlayer(player);
        player.setDirection(Direction.EAST);

        Clyde clyde = Navigation.findUnitInBoard(Clyde.class, level.getBoard());
        assertThat(clyde).isNotNull();
        Optional<Direction> dir = clyde.nextAiMove();
        assertThat(dir).isPresent();
        assertThat(dir.get()).isEqualTo(Direction.WEST);
    }

    /**
     * Tests if Clyde moves away from the player
     * if he is smaller than shyness(default=8) spaces away from player.
     */
    @Test
    public void testIfClydeMovesAwayFromPlayerIfMoreThanShynessSpacesAway() {
        char[][] map = new char[][]{
            "############".toCharArray(),
            "#P C       #".toCharArray(),
            "############".toCharArray()
        };
        Level level = ghostMapParser.parseMap(Tools.rotateMap(map));

        level.registerPlayer(player);
        player.setDirection(Direction.EAST);

        Clyde clyde = Navigation.findUnitInBoard(Clyde.class, level.getBoard());
        assertThat(clyde).isNotNull();
        Optional<Direction> dir = clyde.nextAiMove();
        assertThat(dir).isPresent();
        assertThat(dir.get()).isEqualTo(Direction.EAST);
    }

    /**
     * Tests if Clyde has no direction,
     * when there is a wall in between, thus no available path to the player.
     */
    @Test
    public void testIfNoPathToGo() {
        char[][] map = new char[][]{
            "############".toCharArray(),
            "#P    #   C#".toCharArray(),
            "############".toCharArray()
        };
        Level level = ghostMapParser.parseMap(Tools.rotateMap(map));

        level.registerPlayer(player);
        player.setDirection(Direction.EAST);

        Clyde clyde = Navigation.findUnitInBoard(Clyde.class, level.getBoard());
        assertThat(clyde).isNotNull();
        Optional<Direction> dir = clyde.nextAiMove();
        assertThat(dir).isEmpty();
    }

    /**
     * Tests if Clyde has no direction,
     * when there is no player.
     */
    @Test
    public void testIfNoPlayerExists() {
        char[][] map = new char[][]{
            "############".toCharArray(),
            "#         C#".toCharArray(),
            "############".toCharArray()
        };
        Level level = ghostMapParser.parseMap(Tools.rotateMap(map));

        Clyde clyde = Navigation.findUnitInBoard(Clyde.class, level.getBoard());
        assertThat(clyde).isNotNull();
        Optional<Direction> dir = clyde.nextAiMove();
        assertThat(dir).isEmpty();
    }

}
