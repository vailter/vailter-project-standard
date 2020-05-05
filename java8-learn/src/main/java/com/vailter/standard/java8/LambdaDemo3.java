package com.vailter.standard.java8;

import lombok.NonNull;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * Lambda结合
 * FunctionalInterface Lib,
 * forEach,
 * stream(),
 * method reference
 * 等新特性可以使代码变的更加简洁！
 */
public class LambdaDemo3 {
    public static void main(String[] args) {
        // 指定一个数组，打印出所有以 "a" 开头的
        List<String> list = Arrays.asList("asad", "312qdas", "ddad", "adfsdfds");
//        checkAndExecute(
//                arr,
//                (param) -> {
//                    return StringUtils.startsWith(param, "a");
//                },
//                (param) -> {
//                    System.out.println(param);
//                }
//         );

        // 最初的
        checkAndExecute(
                list,
                (param) -> StringUtils.startsWith(param, "a"),
                System.out::println
        );

        // FunctionalInterface Lib
        checkAndExecuteByLib(
                list,
                (param) -> StringUtils.startsWith(param, "a"),
                System.out::println
        );
    }

    static void checkAndExecute(@NonNull List<String> list, ParamChecker paramChecker, Executor executor) {
        for (String param : list) {
            if (paramChecker.check(param)) {
                executor.execute(param);
            }
        }
    }

    /**
     * 利用函数式接口包
     * java.util.function
     *
     * @param list
     * @param predicate
     * @param consumer
     */
    static void checkAndExecuteByLib(@NonNull List<String> list, Predicate<String> predicate, Consumer<String> consumer) {
//        for (String param : list) {
//            if (predicate.test(param)) {
//                consumer.accept(param);
//            }
//        }

        // 利用 foreach
//        list.forEach(param -> {
//            if (predicate.test(param)) {
//                consumer.accept(param);
//            }
//        });

        // 使用 stream
//        list.stream()
//                .filter(param -> predicate.test(param))
//                .forEach(param -> consumer.accept(param));

        // 使用 Method reference, 就是用已经写好的别的 Object/Class 的method 来代替 Lambda expression。
        // Object::methodName
        // Class::staticMethod
//        list.stream()
//                .filter(predicate::test)
//                .forEach(consumer::accept);
        list.stream()
                .filter(predicate)
                .forEach(consumer);
    }

    @FunctionalInterface
    public interface ParamChecker {
        boolean check(String param);
    }

    @FunctionalInterface
    public interface Executor {
        void execute(String param);
    }
}
