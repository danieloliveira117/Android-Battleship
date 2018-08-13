package amov.danieloliveira.batalhanaval.engine.enums;

public enum ShipType {
    ONE(1), TWO(2), THREE(3), T_SHAPE(5);

    private int size;

    ShipType(int size)
    {
        this.size = size;
    }

    /**
     * @return the size
     */
    public int getSize() {
        return this.size;
    }
}
