package com.vailter.standard.java8;

import java.math.BigDecimal;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

/**
 *
 */
public class FunctionalLibDemo {

    public static void main(String[] args) {
        // Predicate
        Predicate<Integer> predicate = x -> x > 176;
        int height = 175;
        System.out.println("身高超过176？" + predicate.test(height));

        // Consumer
        Consumer<String> consumer = System.out::println;
        consumer.accept("Hello World");

        // Function
        Function<String, Integer> function = Integer::valueOf;
        Integer val = function.apply("123");
        System.out.println(val);

        // Supplier
        Supplier<String> stringSupplier = BigDecimal.TEN::toString;
        System.out.println(stringSupplier.get());

        // UnaryOperator
        UnaryOperator<String> unaryOperator = s -> s + " world";
        String apply2 = unaryOperator.apply("hello");
        System.out.println(apply2);

        // BinaryOperator
        BinaryOperator<Integer> operator = (x, y) -> x * y;
        Integer integer = operator.apply(2, 3);
        System.out.println(integer);
    }
}
