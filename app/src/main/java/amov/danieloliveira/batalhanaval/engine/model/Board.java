package amov.danieloliveira.batalhanaval.engine.model;

import java.util.HashMap;
import java.util.Map;

import amov.danieloliveira.batalhanaval.engine.enums.PositionType;
import amov.danieloliveira.batalhanaval.engine.enums.ShipType;
import amov.danieloliveira.batalhanaval.engine.exceptions.InvalidShipNumberException;

import static amov.danieloliveira.batalhanaval.Consts.MAXSHIPS;

public class Board {
    private Ship[] shipList = new Ship[MAXSHIPS];
    // TODO save this player's attempts instead of adversary
    private Map<Position, PositionType> adversaryAttempts = new HashMap<>();

    public Board() {
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

    public boolean addNewAttempt(Position position) {
        // Ignore repeated positions
        if (!adversaryAttempts.containsKey(position)) {
            adversaryAttempts.put(position, PositionType.SELECTED);
        }

        int selected = 0;

        // Check if all 3 positions have been selected
        for (PositionType positionType : adversaryAttempts.values()) {
            if(positionType == PositionType.SELECTED) {
                selected++;
            }
        }

        // TODO verificar quantas casas ainda faltam -> se menor que 3 mudar o if...
        if(selected == 3) {
           return processSelectedPositions() == 3;
        }

        return false;
    }

    public int processSelectedPositions() {
        int numberOfHits = 0;

        for (Position position : adversaryAttempts.keySet()) {
            // Start as if no ship has been hit
            adversaryAttempts.put(position, PositionType.MISS);

            // Check if a ship has been hit
            for (Ship ship : shipList) {
                if (ship.getPositionList().contains(position)) {
                    // Set position as HIT
                    adversaryAttempts.put(position, PositionType.HIT);
                    numberOfHits++;

                    // Check if all parts of the ship have been destroyed
                    if (adversaryAttempts.keySet().containsAll(ship.getPositionList())) {
                        for (Position part : ship.getPositionList()) {
                            // Replace HIT for SHIP
                            adversaryAttempts.put(part, PositionType.SHIP);
                        }
                    }
                }
            }
        }

        return numberOfHits;
    }

    public PositionType getPositionType(Position position) {
        if(adversaryAttempts.containsKey(position)) {
            return adversaryAttempts.get(position);
        }

        return PositionType.UNKNOWN;
    }

    public Ship getShipByID(Integer ship) throws InvalidShipNumberException {
        if(ship < 0 || ship > MAXSHIPS - 1)
            throw new InvalidShipNumberException();

        return shipList[ship];
    }
}
