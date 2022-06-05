package nl.tudelft.jpacman.level;

import nl.tudelft.jpacman.PacmanConfigurationException;
import nl.tudelft.jpacman.board.BoardFactory;
import nl.tudelft.jpacman.board.Square;
import nl.tudelft.jpacman.npc.Ghost;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;


public class MapParserTest {

    private MapParser parser;

    @Mock
    private LevelFactory levelFactoryMock;
    @Mock
    private BoardFactory boardFactoryMock;

    @Mock
    private Square ground;
    @Mock
    private Square wall;
    @Mock
    private Pellet pellet;
    @Mock
    private Ghost ghost;


    @BeforeEach
    void setup() {
        initMocks(this);

        parser = new MapParser(levelFactoryMock, boardFactoryMock);

        // Setup returns for BoardFactory square creation
        when(boardFactoryMock.createGround()).thenReturn(ground);
        when(boardFactoryMock.createWall()).thenReturn(wall);
        when(levelFactoryMock.createPellet()).thenReturn(pellet);
        when(levelFactoryMock.createGhost()).thenReturn(ghost);

        // Setup better output for test output
        when(ground.toString()).thenReturn("Ground Square");
        when(wall.toString()).thenReturn("Wall Square");
        when(ghost.toString()).thenReturn("Ghost");
    }

    @Test
    void testIfMapHasCorrectDimensions() {
        parser.parseMap(new char[][] {{' '}, {' '}});

        verify(boardFactoryMock, times(1)).createBoard(any());
        verify(boardFactoryMock).createBoard(argThat(
            (Square[][] grid) -> grid.length == 2 && grid[0].length == 1 && grid[1].length == 1)
        );

        verify(boardFactoryMock, times(2)).createGround();
    }


    @Test
    void testIfMapCreatesGroundCorrect() {
        parser.parseMap(new char[][] {{' '}});

        verify(boardFactoryMock, times(1)).createGround();
    }


    @Test
    void testIfMapCreatesWallCorrect() {
        parser.parseMap(new char[][] {{'#'}});

        verify(boardFactoryMock, times(1)).createWall();
    }


    @Test
    void testIfMapCreatesPelletCorrect() {
        parser.parseMap(new char[][] {{'.'}});

        verify(boardFactoryMock, times(1)).createGround();
        verify(levelFactoryMock, times(1)).createPellet();
        verify(pellet, times(1)).occupy(ground);
    }


    @Test
    void testIfMapCreatesGhostSquareCorrect() {
        parser.parseMap(new char[][] {{'G'}});

        List<Ghost> ghosts = new ArrayList<Ghost>() {
            {
                add(ghost);
            }
        };

        verify(boardFactoryMock, times(ghosts.size())).createGround();
        verify(levelFactoryMock, times(ghosts.size())).createGhost();
        verify(ghost, times(ghosts.size())).occupy(ground);
        verify(levelFactoryMock, times(1)).createLevel(
            any(),
            eq(ghosts),
            any()
        );
    }

    @Test
    void testIfMapCreatesPlayerSquareCorrect() {
        when(boardFactoryMock.createGround()).thenReturn(ground);

        parser.parseMap(new char[][] {{'P'}});

        List<Square> startingPositions = new ArrayList<Square>() {
            {
                add(ground);
            }
        };

        verify(boardFactoryMock, times(startingPositions.size())).createGround();
        verify(levelFactoryMock, times(startingPositions.size())).createLevel(
            any(),
            any(),
            eq(startingPositions)
        );
    }

    @Test
    void testIfMapIsCorrectlyConvertedFromStringListToCharArray() {
        parser.parseMap(new ArrayList<String>() {
            {
                add(" #");
                add("# ");
            }
        });
        final Square[][] expected = new Square[][]{
            {ground, wall},
            {wall, ground},
        };

        verify(boardFactoryMock).createBoard(eq(expected));
    }


    @Test
    void testIfMapIsCorrectlyReadFromInputStream() throws IOException {
        InputStream anyInputStream =
            new ByteArrayInputStream(" #\n##\n  ".getBytes(StandardCharsets.UTF_8));

        final Square[][] expected = new Square[][]{
            {ground, wall, ground},
            {wall, wall, ground},
        };

        parser.parseMap(anyInputStream);

        verify(boardFactoryMock).createBoard(eq(expected));
    }


    @Test
    void testIfMapIsCorrectlyReadFromFile() throws IOException {
        final Square[][] expected = new Square[][]{
            {ground}
        };

        parser.parseMap("/simplemap.txt");

        verify(boardFactoryMock).createBoard(eq(expected));
    }


    @Test
    void testBoardGetter() {
        assertThat(parser.getBoardCreator()).isEqualTo(boardFactoryMock);
    }


    @Test()
    void testIfWrongCharacterThrowsException() {
        assertThrows(PacmanConfigurationException.class,
            () -> parser.parseMap(new char[][]{{'E'}}));
    }


    @Test()
    void testIfNullInputThrowsException() {
        assertThrows(PacmanConfigurationException.class,
            () -> parser.parseMap((List<String>) null));
    }


    @Test()
    void testIfEmptyInputThrowsException() {
        assertThrows(PacmanConfigurationException.class,
            () -> parser.parseMap(new ArrayList<String>() {
            })
        );
    }


    @Test()
    void testIfEmptyLinesInputThrowsException() {
        assertThrows(PacmanConfigurationException.class,
            () -> parser.parseMap(new ArrayList<String>() {
                {
                    add("");
                    add("");
                }
            })
        );
    }

    @Test()
    void testIfInequivalentLineWidthInputThrowsException() {
        assertThrows(PacmanConfigurationException.class,
            () -> parser.parseMap(new ArrayList<String>() {
                {
                    add("ad");
                    add("a");
                }
            })
        );
    }

    @Test()
    void testIfInvalidMapNameThrowsException() {
        assertThrows(PacmanConfigurationException.class,
            () -> parser.parseMap("test this name should never be a map name!!!!"));
    }
}
