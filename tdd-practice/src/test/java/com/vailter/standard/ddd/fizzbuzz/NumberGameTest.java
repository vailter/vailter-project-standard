package com.vailter.standard.ddd.fizzbuzz;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class NumberGameTest {
    @Test
    public void testPrint() {
        NumberGame numberGame = new NumberGame();
        Assertions.assertEquals(numberGame.print(1), "1");
        Assertions.assertEquals(numberGame.print(3), "Fizz");
        Assertions.assertEquals(numberGame.print(5), "Buzz");
    }
}
