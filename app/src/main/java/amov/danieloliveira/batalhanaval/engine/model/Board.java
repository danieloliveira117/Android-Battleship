package amov.danieloliveira.batalhanaval.engine.model;

import java.util.ArrayList;
import java.util.List;

import amov.danieloliveira.batalhanaval.engine.enums.ShipType;

import static amov.danieloliveira.batalhanaval.Consts.MAXSHIPS;

public class Board {
    private Ship[] shipList = new Ship[MAXSHIPS];
    private List<Coord> attempts = new ArrayList<>();

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

    public List<Coord> getAttempts() {
        return attempts;
    }
}
