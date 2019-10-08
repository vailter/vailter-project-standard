package com.vailter.standard.ddd.fizzbuzz;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

public class NumberGameTest {
    @Test
    public void testPrint() {
        NumberGame numberGame = new NumberGame();
        Assert.assertEquals(numberGame.print(1), "1");
        Assert.assertEquals(numberGame.print(3), "Fizz");
        Assert.assertEquals(numberGame.print(5), "Buzz");
    }
}
