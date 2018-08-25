package amov.danieloliveira.batalhanaval.engine.enums;

import org.junit.Test;

import amov.danieloliveira.batalhanaval.engine.model.Position;

import static org.junit.Assert.*;

public class PositionTypeTest {

    @Test
    public void isHit() {
        PositionType type;

        type = PositionType.HIT_ONE;

        assertEquals("Hit One", type.isHit(), true);

        type = PositionType.MISS;

        assertEquals("Missed", type.isHit(), false);
    }
}