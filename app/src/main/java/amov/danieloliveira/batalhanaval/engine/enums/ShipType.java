package amov.danieloliveira.batalhanaval.engine.enums;

public enum ShipType {
    ONE(1, 8), TWO(2, 10), THREE(3, 12), T_SHAPE(5, 16);

    private int size;
    private int adjacentSize;

    ShipType(int size, int adjacentSize)
    {
        this.size = size;
        this.adjacentSize = adjacentSize;
    }

    public int getSize() {
        return this.size;
    }

    public int getAdjacentSize() {
        return this.adjacentSize;
    }
}
