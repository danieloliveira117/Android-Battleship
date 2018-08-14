package amov.danieloliveira.batalhanaval.engine.model;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import amov.danieloliveira.batalhanaval.engine.enums.Orientation;
import amov.danieloliveira.batalhanaval.engine.enums.ShipType;
import amov.danieloliveira.batalhanaval.engine.exceptions.InvalidPositionException;

import static org.junit.Assert.*;

public class ShipTest {

    @Test
    public void setOrientation() {
    }

    @Test
    public void updatePosition() throws InvalidPositionException {
        List<Position> positions = new ArrayList<>();
        positions.add(new Position(1, 1));
        positions.add(new Position(2, 1));
        positions.add(new Position(3, 1));

        Ship ship = new Ship(ShipType.THREE);

        ship.updatePosition(new Position(1, 1));

        assertEquals(positions, ship.getPositionList());

        System.out.println(ship.toString());

        // T-SHAPED

        positions = new ArrayList<>();
        positions.add(new Position(1, 2));
        positions.add(new Position(2, 2));
        positions.add(new Position(3, 2));
        positions.add(new Position(1, 1));
        positions.add(new Position(1, 3));

        ship = new Ship(ShipType.T_SHAPE);

        ship.updatePosition(new Position(1, 2));

        assertEquals(positions, ship.getPositionList());

        System.out.println(ship.toString());
    }
}