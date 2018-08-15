package amov.danieloliveira.batalhanaval.engine.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.Set;

import amov.danieloliveira.batalhanaval.engine.enums.Orientation;
import amov.danieloliveira.batalhanaval.engine.enums.ShipType;
import amov.danieloliveira.batalhanaval.engine.exceptions.InvalidPositionException;

import static amov.danieloliveira.batalhanaval.Consts.MAXCOLUMNS;
import static amov.danieloliveira.batalhanaval.Consts.MAXROWS;

public class Ship {
    public static int shipCount = 0;
    private int id;
    private ShipType type;
    private List<Position> positionList = new ArrayList<>();
    private Orientation orientation;
    private boolean destroyed;

    public Ship(ShipType type) {
        this.id = shipCount++;
        this.orientation = Orientation.WEST;
        this.type = type;
        this.destroyed = false;

        addParts(type.getSize());
    }

    private void addParts(int num) {
        for (int i = 0; i < num; i++) {
            positionList.add(new Position());
        }
    }

    public ShipType getType() {
        return type;
    }

    public List<Position> getPositionList() {
        return positionList;
    }

    public Orientation getOrientation() {
        return orientation;
    }

    public void setOrientation(Orientation orientation) {
        this.orientation = orientation;
        updatePosition(positionList.get(0));
    }

    public boolean isDestroyed() {
        return destroyed;
    }

    public void setDestroyed(boolean destroyed) {
        this.destroyed = destroyed;
    }

    public void updatePosition(Position position) {
        Position temp = new Position(position);
        boolean valid = true;

        if (type == ShipType.T_SHAPE) {
            Position original = new Position(temp);

            switch (orientation) {
                case NORTH:
                    // WEST
                    try {
                        original.decrementHorizontal();
                        positionList.set(3, original);
                    } catch (InvalidPositionException ignored) {
                        positionList.set(3, new Position());
                    }

                    // Restore original
                    original = new Position(temp);

                    // EAST
                    try {
                        original.incrementHorizontal();
                        positionList.set(4, original);
                    } catch (InvalidPositionException ignored) {
                        positionList.set(4, new Position());
                    }
                    break;
                case WEST:
                    // SOUTH
                    try {
                        original.decrementVertical();
                        positionList.set(3, original);
                    } catch (InvalidPositionException ignored) {
                        positionList.set(3, new Position());
                    }

                    // Restore original
                    original = new Position(temp);

                    // NORTH
                    try {
                        original.incrementVertical();
                        positionList.set(4, original);
                    } catch (InvalidPositionException ignored) {
                        positionList.set(4, new Position());
                    }
                    break;
                case SOUTH:
                    // WEST
                    try {
                        original.incrementHorizontal();
                        positionList.set(3, original);
                    } catch (InvalidPositionException ignored) {
                        positionList.set(3, new Position());
                    }

                    // Restore original
                    original = new Position(temp);

                    // EAST
                    try {
                        original.decrementHorizontal();
                        positionList.set(4, original);
                    } catch (InvalidPositionException ignored) {
                        positionList.set(4, new Position());
                    }
                    break;
                case EAST:
                    // NORTH
                    try {
                        original.incrementVertical();
                        positionList.set(3, original);
                    } catch (InvalidPositionException ignored) {
                        positionList.set(3, new Position());
                    }

                    // Restore original
                    original = new Position(temp);

                    // SOUTH
                    try {
                        original.decrementVertical();
                        positionList.set(4, original);
                    } catch (InvalidPositionException ignored) {
                        positionList.set(4, new Position());
                    }
                    break;
            }

            for (int i = 0; i < positionList.size() - 2; i++) {
                if (valid) {
                    positionList.set(i, new Position(temp));
                } else {
                    positionList.set(i, new Position());
                }

                try {
                    incrementPosition(temp);
                } catch (InvalidPositionException e) {
                    valid = false;
                }
            }
        } else { // ONE, TWO, THREE
            for (int i = 0; i < positionList.size(); i++) {
                if (valid) {
                    positionList.set(i, new Position(temp));
                } else {
                    positionList.set(i, new Position());
                }

                try {
                    incrementPosition(temp);
                } catch (InvalidPositionException e) {
                    valid = false;
                }
            }
        }
    }

    private void incrementPosition(Position position) throws InvalidPositionException {
        switch (orientation) {
            case NORTH:
                position.incrementVertical();
                break;
            case WEST:
                position.incrementHorizontal();
                break;
            case SOUTH:
                position.decrementVertical();
                break;
            case EAST:
                position.decrementHorizontal();
                break;
        }
    }

    public boolean hasInvalidPositions() {
        for (Position position : positionList) {
            if(position == null || !position.isValid()) {
                return true;
            }
        }

        return false;
    }

    private void setRandomOrientation() {
        Random random = new Random();

        switch (random.nextInt(4)) {
            case 0:
                orientation = Orientation.NORTH;
                break;
            case 1:
                orientation = Orientation.WEST;
                break;
            case 2:
                orientation = Orientation.EAST;
                break;
            case 3:
                orientation = Orientation.SOUTH;
                break;
        }
    }

    public void setRandomPosition() {
        Random random = new Random();

        do {
            setRandomOrientation();

            try {
                updatePosition(new Position(random.nextInt(MAXROWS), random.nextInt(MAXCOLUMNS)));
            } catch (InvalidPositionException ignored) {}

        } while (hasInvalidPositions());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ship ship = (Ship) o;

        return id == ship.id &&
                destroyed == ship.destroyed &&
                type == ship.type &&
                Objects.equals(positionList, ship.positionList) &&
                orientation == ship.orientation;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, type, positionList, orientation, destroyed);
    }

    @Override
    public String toString() {
        return "Ship{" +
                "type=" + type +
                ", positionList=" + positionList +
                ", orientation=" + orientation +
                ", destroyed=" + destroyed +
                '}';
    }
}
