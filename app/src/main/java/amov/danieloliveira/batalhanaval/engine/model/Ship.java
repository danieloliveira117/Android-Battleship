package amov.danieloliveira.batalhanaval.engine.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import amov.danieloliveira.batalhanaval.engine.enums.Orientation;
import amov.danieloliveira.batalhanaval.engine.enums.ShipType;

public class Ship {
    public static int shipCount = 0;
    private int id;
    private ShipType type;
    private List<Position> positionList = new ArrayList<>();
    private Orientation orientation;
    private boolean destroyed;

    public Ship(ShipType type) {
        this.id = shipCount++;
        this.orientation = Orientation.NORTH;
        this.type = type;
        this.destroyed = false;

        switch (type) {
            case ONE:
                addParts(1);
                break;
            case TWO:
                addParts(2);
                break;
            case THREE:
                addParts(3);
                break;
            case T_SHAPE:
                addParts(5);
                break;
        }
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
    }

    public boolean isDestroyed() {
        return destroyed;
    }

    public void setDestroyed(boolean destroyed) {
        this.destroyed = destroyed;
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
}
