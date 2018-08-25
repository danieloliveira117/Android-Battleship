package amov.danieloliveira.batalhanaval.engine.enums;

public enum PositionType {
    VALID, INVALID, UNKNOWN, MISS, HIT, SHIP, ADJACENT, HIT_ONE, HIT_TWO, HIT_THREE, HIT_T_SHAPE, SELECTED;

    public boolean isHit() {
        return (this == PositionType.HIT ||
                this == PositionType.HIT_ONE ||
                this == PositionType.HIT_TWO ||
                this == PositionType.HIT_THREE ||
                this == PositionType.HIT_T_SHAPE);
    }
}
