package com.jaymorelli.posp.order.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class TestTest {



    @Test
    void testMult() {
        Math m = new Math();
        assertEquals(16, m.mult(8, 2));
    }

    @Test
    void testSub() {
        Math m = new Math();
        assertEquals(6, m.sub(8, 2));
    }

    @Test
    void testSum() {
        Math m = new Math();
        assertEquals(10, m.sum(8, 2));
    }
}
