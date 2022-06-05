package nl.tudelft.jpacman.tools;

import nl.tudelft.jpacman.board.Square;
import nl.tudelft.jpacman.level.Level;

/**
 * Tools for easier test creation and debugging.
 */
public final class Tools {

    private Tools() {

    }

    /**
     * Rotate the map 90 degrees clockwise.
     * @param map the map you want rotated
     * @return a 90 degree rotated map
     */
    public static char [][] rotateMap(char[][] map) {
        char[][] mapRotated = new char[map[0].length][map.length];
        for (int i = 0; i < map[0].length; i++) {
            for (int j = map.length - 1; j >= 0; j--) {
                mapRotated[i][j] = map[j][i];
            }
        }
        return mapRotated;
    }

    /**
     * Prints the map on the console.
     * @param level the current level with the map.
     */
    @SuppressWarnings("PMD")
    public static void printMap(Level level) {
        for (int y = 0; y < level.getBoard().getHeight(); y++) {
            for (int x = 0; x < level.getBoard().getWidth(); x++) {
                char c = ' ';
                Square sqr = level.getBoard().squareAt(x, y);
                if (sqr.getClass().getName().equals("nl.tudelft.jpacman.board.BoardFactory$Wall")) {
                    c = '#';
                }
                System.out.print(c);
            }
            System.out.println("\n");
        }
    }
}
