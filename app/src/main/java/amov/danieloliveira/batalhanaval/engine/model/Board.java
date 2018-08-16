package amov.danieloliveira.batalhanaval.engine.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
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
    private Ship[] shipList = new Ship[MAXSHIPS];
    private Map<Position, PositionType> adversaryAttempts = new HashMap<>();

    public Board() {
        createShips();
    }

    private void createShips() {
        shipList[0] = new Ship(ShipType.ONE);
        shipList[1] = new Ship(ShipType.ONE);
        shipList[2] = new Ship(ShipType.TWO);
        shipList[3] = new Ship(ShipType.TWO);
        shipList[4] = new Ship(ShipType.THREE);
        shipList[5] = new Ship(ShipType.THREE);
        shipList[6] = new Ship(ShipType.T_SHAPE);
    }

    public Ship[] getShipList() {
        return shipList;
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
            if(!adversaryAttempts.get(position).equals(PositionType.SELECTED))
                continue;

            // Start as if no ship has been hit
            adversaryAttempts.put(position, PositionType.MISS);

            // Check if a ship has been hit
            for (Ship ship : shipList) {
                if (ship.getPositionList().contains(position)) {
                    // Set position as HIT
                    switch (ship.getType()) {
                        case ONE:
                            adversaryAttempts.put(position, PositionType.HIT_ONE);
                            break;
                        case TWO:
                            adversaryAttempts.put(position, PositionType.HIT_TWO);
                            break;
                        case THREE:
                            adversaryAttempts.put(position, PositionType.HIT_THREE);
                            break;
                        case T_SHAPE:
                            adversaryAttempts.put(position, PositionType.HIT_T_SHAPE);
                            break;
                    }

                    numberOfHits++;

                    // Check if all parts of the ship have been destroyed
                    if (adversaryAttempts.keySet().containsAll(ship.getPositionList())) {
                        destroyedShip.add(ship);
                    }
                }
            }
        }

        for(Ship ship : destroyedShip) {
            for (Position adjacent : ship.getAdjacentPositions()) {
                adversaryAttempts.put(adjacent, PositionType.MISS);
            }

            ship.setDestroyed(true);
        }

        return numberOfHits;
    }

    public PositionType getPositionType(Position position) {
        if (adversaryAttempts.containsKey(position)) {
            return adversaryAttempts.get(position);
        }

        return PositionType.UNKNOWN;
    }

    public Ship getShipByID(Integer ship) throws InvalidShipNumberException {
        if (ship < 0 || ship > MAXSHIPS - 1)
            throw new InvalidShipNumberException();

        return shipList[ship];
    }

    public Ship getShipByPosition(Position position) {
        for (Ship ship : shipList) {
            if (ship.getPositionList().contains(position)) {
                return ship;
            }
        }

        return null;
    }

    public PositionType getPositionValidity(Position position) {
        int count = 0;
        boolean isAdjacent = false;

        for (Ship ship : shipList) {

            if (ship.getPositionList().contains(position)) {
                count++;

                if (ship.hasInvalidPositions()) {
                    return PositionType.INVALID;
                }
            }

            if (!isAdjacent && ship.getAdjacentPositions().contains(position)) {
                isAdjacent = true;
            }
        }

        if (count > 1 || isAdjacent && count == 1)
            return PositionType.INVALID;

        if (!isAdjacent && count == 1)
            return PositionType.VALID;

        if (isAdjacent)
            return PositionType.ADJACENT;

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

            positionList.addAll(ship.getPositionList());
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

            while (count < MAXSHIPS) {
                shipList[count].setRandomPosition(availableBoard);

                positionList = new ArrayList<>();
                adjacentSet = new HashSet<>();

                for (int i = 0; i <= count; i++) {
                    positionList.addAll(shipList[i].getPositionList());
                    adjacentSet.addAll(shipList[i].getAdjacentPositions());
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
}
