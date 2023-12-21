package agh.ics.oop.model.maps;

import agh.ics.oop.model.elements.Animal;
import agh.ics.oop.model.enums.MoveDirection;
import agh.ics.oop.model.Vector2d;
import agh.ics.oop.model.exceptions.IllegalPositionException;
import agh.ics.oop.model.elements.WorldElement;

import java.util.ArrayList;

/**
 * The interface responsible for interacting with the map of the world.
 * Assumes that Vector2d and MoveDirection classes are defined.
 *
 * @author apohllo, idzik
 */
public interface WorldMap extends MoveValidator {

    /**
     * Place an animal on the map.
     *
     * @param animal The animal to place on the map.
     * @return True if the animal was placed. The animal cannot be placed if the move is not valid.
     */
    void place(Animal animal) throws IllegalPositionException;

    /**
     * Moves an animal (if it is present on the map) according to specified direction.
     * If the move is not possible, this method has no effect.
     */
    void move(Animal animal, MoveDirection direction);

    /**
     * Return true if given position on the map is occupied. Should not be
     * confused with canMove since there might be empty positions where the animal
     * cannot move.
     *
     * @param position Position to check.
     * @return True if the position is occupied.
     */
    boolean isOccupied(Vector2d position);

    /**
     * Return an animal at a given position.
     *
     * @param position The position of the animal.
     * @return animal or null if the position is not occupied.
     */
    WorldElement objectAt(Vector2d position);
    ArrayList<WorldElement> getElements();
    Boundary getCurrentBounds();
    int getMapID();

    /**
     * The map visualizer converts the {@link WorldMap} map into a string
     * representation.
     *
     * @author apohllo, idzik
     */
    class MapVisualizer {
        private static final String EMPTY_CELL = " ";
        private static final String FRAME_SEGMENT = "-";
        private static final String CELL_SEGMENT = "|";
        private final WorldMap map;

        /**
         * Initializes the MapVisualizer with an instance of map to visualize.
         *
         * @param map
         */
        public MapVisualizer(WorldMap map) {
            this.map = map;
        }

        /**
         * Convert selected region of the map into a string. It is assumed that the
         * indices of the map will have no more than two characters (including the
         * sign).
         *
         * @param lowerLeft  The lower left corner of the region that is drawn.
         * @param upperRight The upper right corner of the region that is drawn.
         * @return String representation of the selected region of the map.
         */
        public String draw(Vector2d lowerLeft, Vector2d upperRight) {
            StringBuilder builder = new StringBuilder();
            for (int i = upperRight.y() + 1; i >= lowerLeft.y() - 1; i--) {
                if (i == upperRight.y() + 1) {
                    builder.append(drawHeader(lowerLeft, upperRight));
                }
                builder.append(String.format("%3d: ", i));
                for (int j = lowerLeft.x(); j <= upperRight.x() + 1; j++) {
                    if (i < lowerLeft.y() || i > upperRight.y()) {
                        builder.append(drawFrame(j <= upperRight.x()));
                    } else {
                        builder.append(CELL_SEGMENT);
                        if (j <= upperRight.x()) {
                            builder.append(drawObject(new Vector2d(j, i)));
                        }
                    }
                }
                builder.append(System.lineSeparator());
            }
            return builder.toString();
        }

        private String drawFrame(boolean innerSegment) {
            if (innerSegment) {
                return FRAME_SEGMENT + FRAME_SEGMENT;
            } else {
                return FRAME_SEGMENT;
            }
        }

        private String drawHeader(Vector2d lowerLeft, Vector2d upperRight) {
            StringBuilder builder = new StringBuilder();
            builder.append(" y\\x ");
            for (int j = lowerLeft.x(); j < upperRight.x() + 1; j++) {
                builder.append(String.format("%2d", j));
            }
            builder.append(System.lineSeparator());
            return builder.toString();
        }

        private String drawObject(Vector2d currentPosition) {
            Object object = this.map.objectAt(currentPosition);
            if (object != null) {
                return object.toString();
            }
            return EMPTY_CELL;
        }
    }
}
