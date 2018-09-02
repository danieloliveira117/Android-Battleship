package amov.danieloliveira.batalhanaval.engine.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;

import amov.danieloliveira.batalhanaval.engine.enums.PositionType;
import amov.danieloliveira.batalhanaval.engine.enums.ShipType;
import amov.danieloliveira.batalhanaval.engine.exceptions.InvalidPositionException;
import amov.danieloliveira.batalhanaval.engine.exceptions.InvalidShipNumberException;

import static amov.danieloliveira.batalhanaval.Consts.MAXCOLUMNS;
import static amov.danieloliveira.batalhanaval.Consts.MAXROWS;
import static amov.danieloliveira.batalhanaval.Consts.MAXSELECT;
import static amov.danieloliveira.batalhanaval.Consts.MAXSHIPS;

public class Board {
    private List<Ship> shipList = new ArrayList<>(MAXSHIPS);
    private List<Ship> oldShips = new ArrayList<>(MAXSHIPS);
    private Map<Position, PositionType> adversaryAttempts = new HashMap<>();

    public Board() {
        createShips();
    }

    private void createShips() {
        shipList.clear();
        shipList.add(new Ship(ShipType.ONE));
        shipList.add(new Ship(ShipType.ONE));
        shipList.add(new Ship(ShipType.TWO));
        shipList.add(new Ship(ShipType.TWO));
        shipList.add(new Ship(ShipType.THREE));
        shipList.add(new Ship(ShipType.THREE));
        shipList.add(new Ship(ShipType.T_SHAPE));
    }

    public void addNewAttempt(Position position) {
        // Ignore repeated positions
        if (!adversaryAttempts.containsKey(position)) {
            adversaryAttempts.put(position, PositionType.SELECTED);
        }
    }

    public boolean isLastSelect() {
        int selected = 0;

        // Check if all 3 positions have been selected
        for (PositionType positionType : adversaryAttempts.values()) {
            if (positionType == PositionType.SELECTED) {
                selected++;
            }
        }

        // All 3 positions have been selected
        return selected == MAXSELECT || MAXROWS * MAXCOLUMNS == adversaryAttempts.size();
    }

    public int processSelectedPositions() {
        int numberOfHits = 0;
        List<Ship> destroyedShip = new ArrayList<>();

        for (Position position : adversaryAttempts.keySet()) {
            if (!adversaryAttempts.get(position).equals(PositionType.SELECTED)) {
                continue;
            }

            // Start as if no ship has been hit
            adversaryAttempts.put(position, PositionType.MISS);

            // Check if a ship has been hit
            for (Ship ship : shipList) {
                if (ship.getPositionList().contains(position)) {

                    // Set position as HIT
                    adversaryAttempts.put(position, ship.getHitType());
                    numberOfHits++;

                    // Check if all parts of the ship have been destroyed
                    if (adversaryAttempts.keySet().containsAll(ship.getPositionList())) {
                        destroyedShip.add(ship);
                    }
                }
            }
        }

        for (Ship ship : destroyedShip) {
            for (Position adjacent : ship.getAdjacentPositions()) {
                adversaryAttempts.put(adjacent, PositionType.MISS);
            }

            ship.setDestroyed(true);
        }

        return numberOfHits;
    }

    public Ship getShipByID(Integer ship) throws InvalidShipNumberException {
        if (ship < 0 || ship >= shipList.size())
            throw new InvalidShipNumberException();

        return shipList.get(ship);
    }

    public Ship getShipByPosition(Position position) {
        for (Ship ship : shipList) {
            if (ship.getPositionList().contains(position)) {
                return ship;
            }
        }

        return null;
    }

    public PositionType getPositionType(Position position) {
        if (adversaryAttempts.containsKey(position)) {
            return adversaryAttempts.get(position);
        }

        return PositionType.UNKNOWN;
    }

    public PositionType getPositionTypeOnOpponentView(Position position) {
        PositionType positionType = adversaryAttempts.get(position);

        for (Ship ship : shipList) {
            if (ship.getPositionList().contains(position)) {
                if (positionType != null && positionType.isHit()) {
                    return ship.getHitType();
                }

                if (positionType == null) {
                    return PositionType.VALID;
                }
            }
        }

        if (positionType != null)
            return positionType;

        return PositionType.UNKNOWN;
    }

    public PositionType getPositionValidity(Position position, Ship currentShip) {
        int count = 0;
        boolean isAdjacent = false;
        boolean isSelected = false;

        for (Ship ship : shipList) {
            if (ship.isDestroyed())
                continue;

            if (ship.getPositionList().contains(position)) {
                count++;

                if (ship.hasInvalidPositions()) {
                    return PositionType.INVALID;
                }

                if (currentShip != null && ship.equals(currentShip)) {
                    isSelected = true;
                }
            }

            if (!isAdjacent && ship.getAdjacentPositions().contains(position)) {
                isAdjacent = true;
            }
        }

        if (count > 1 || isAdjacent && count == 1)
            return PositionType.INVALID;

        if (!isAdjacent && count == 1) {
            if (isSelected)
                return PositionType.SELECTED;
            else
                return PositionType.VALID;
        }

        if (isAdjacent)
            return PositionType.ADJACENT;

        return PositionType.UNKNOWN;
    }

    public PositionType getPositionValidityOnReposition(Position position, Ship currentShip) {
        int count = 0;
        boolean isAdjacent = false;
        boolean isSelected = false;

        for (Ship ship : shipList) {
            // Ignore destroyed ships
            if (ship.isDestroyed()) {
                continue;
            }

            if (ship.getPositionList().contains(position)) {
                if (adversaryAttempts.get(position) != null && adversaryAttempts.get(position).isHit()) {
                    return PositionType.SHIP;
                }

                if (ship.hasInvalidPositions()) {
                    return PositionType.INVALID;
                }

                if (currentShip != null && ship.equals(currentShip)) {
                    isSelected = true;
                } else if (!shipIsIntact(ship)) {
                    return ship.getHitType();
                }

                count++;
            }

            if (!isAdjacent && ship.getAdjacentPositions().contains(position)) {
                isAdjacent = true;
            }
        }

        if (count > 1 || isAdjacent && count == 1) {
            return PositionType.INVALID;
        }

        if (!isAdjacent && count == 1) {
            if (isSelected) {
                return PositionType.SELECTED;
            } else {
                return PositionType.VALID;
            }
        }

        if (isAdjacent) {
            return PositionType.ADJACENT;
        }

        return PositionType.UNKNOWN;
    }

    public List<Position> getShipPositions(Position position) {
        for (Ship ship : shipList) {
            if (ship.getPositionList().contains(position)) {
                return ship.getPositionList();
            }
        }

        return null;
    }

    public boolean hasInvalidShipPositions() {
        List<Position> positionList = new ArrayList<>();

        for (Ship ship : shipList) {
            // Only valid positions
            for (Position position : ship.getPositionList()) {
                if (position == null || !position.isValid()) {
                    return true;
                }
            }

            if (!ship.isDestroyed()) {
                positionList.addAll(ship.getPositionList());
            }
        }

        return hasDuplicate(positionList);
    }

    public boolean allShipsDestroyed() {
        for (Ship ship : shipList) {
            if (!ship.isDestroyed())
                return false;
        }

        return true;
    }

    public void setRandomPlacement() {
        List<Position> positionList;
        Set<Position> adjacentSet;
        int count;
        int iter;
        boolean done = false;

        do {
            count = 0;
            iter = 0;

            List<Position> availableBoard = new ArrayList<>(MAXCOLUMNS * MAXROWS);

            for (int i = 1; i <= MAXROWS; i++) {
                for (int j = 1; j <= MAXCOLUMNS; j++) {
                    try {
                        availableBoard.add(new Position(i, j));
                    } catch (InvalidPositionException ignored) {
                        ignored.printStackTrace();
                    }
                }
            }

            // Clear ship positions
            createShips();

            while (count < shipList.size()) {
                shipList.get(count).setRandomPosition(availableBoard);

                positionList = new ArrayList<>();
                adjacentSet = new HashSet<>();

                for (int i = 0; i <= count; i++) {
                    positionList.addAll(shipList.get(i).getPositionList());
                    adjacentSet.addAll(shipList.get(i).getAdjacentPositions());
                }

                positionList.addAll(adjacentSet);

                iter++;

                if (count == 0 || !hasDuplicate(positionList)) {
                    availableBoard.removeAll(positionList);
                    count++;
                    done = true;
                } else if (iter == 100) {
                    done = false;
                    break;
                }
            }
        } while (!done);
    }

    private static <T> boolean hasDuplicate(Iterable<T> all) {
        Set<T> set = new HashSet<>();
        // Set#add returns false if the set does not change, which
        // indicates that a duplicate element has been added.
        for (T each : all) if (!set.add(each)) return true;
        return false;
    }

    public List<Position> getUnknownPositions() {
        List<Position> availableBoard = new ArrayList<>(MAXCOLUMNS * MAXROWS);

        for (int i = 1; i <= MAXROWS; i++) {
            for (int j = 1; j <= MAXCOLUMNS; j++) {
                try {
                    availableBoard.add(new Position(i, j));
                } catch (InvalidPositionException ignored) {
                    ignored.printStackTrace();
                }
            }
        }

        availableBoard.removeAll(adversaryAttempts.keySet());

        return availableBoard;
    }

    public int getShipsDestroyed() {
        int count = 0;

        for (Ship ship : shipList) {
            if (ship.isDestroyed()) {
                count++;
            }
        }

        for (Ship ship : oldShips) {
            if (ship.isDestroyed()) {
                count++;
            }
        }

        return count;
    }

    public int getNumberOfHits() {
        int count = 0;

        for (PositionType positionType : adversaryAttempts.values()) {
            if (positionType == PositionType.HIT_ONE ||
                    positionType == PositionType.HIT_TWO ||
                    positionType == PositionType.HIT_THREE ||
                    positionType == PositionType.HIT_T_SHAPE) {
                count++;
            }
        }

        return count;
    }

    public boolean shipIsIntact(Ship ship) {
        if (ship == null) {
            return false;
        }

        for (Position pos : adversaryAttempts.keySet()) {
            if (ship.getPositionList().contains(pos) && adversaryAttempts.get(pos) != null) {
                return false;
            }
        }

        return true;
    }

    /**
     * Removes MISS attempts except adjacent to HITs
     */
    public void removeOldAttempts() {
        Set<Position> adjacentToHit = new HashSet<>();

        for (Position position : adversaryAttempts.keySet()) {
            if (adversaryAttempts.get(position).isHit()) {
                adjacentToHit.addAll(position.getAdjacent());
            }
        }

        Iterator<Position> it = adversaryAttempts.keySet().iterator();

        while (it.hasNext()) {
            Position pos = it.next();

            if (adversaryAttempts.get(pos) == PositionType.MISS && !adjacentToHit.contains(pos)) {
                it.remove();
            }
        }
    }

    public boolean canRepositionShip() {
        for (Ship ship : shipList) {
            if (shipIsIntact(ship)) {
                return true;
            }
        }

        return false;
    }

    public void hideDestroyedShips() {
        ListIterator<Ship> it = shipList.listIterator();

        while (it.hasNext()) {
            Ship ship = it.next();

            if (ship.isDestroyed()) {
                adversaryAttempts.keySet().removeAll(ship.getPositionList());
                oldShips.add(ship);
                it.remove();
            }
        }
    }
}
