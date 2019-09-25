package com.vailter.standard.ddd.fizzbuzz;

import com.vailter.standard.utils.CommonUtil;

public class NumberGame {

    public String print(int num) {
        String res = printFizzBuzz(num) + printFizz(num) + printBuzz(num);
        return CommonUtil.isNotBlank(res) ? res : String.valueOf(num);
    }

    private String printFizz(int num) {
        if (isDivisibleBy(num, 3) || String.valueOf(num).contains("3")) {
            return "Fizz";
        }
        return "";
    }

    private String printBuzz(int num) {
        if (isDivisibleBy(num, 5) || String.valueOf(num).contains("5")) {
            return "Buzz";
        }
        return "";
    }

    private String printFizzBuzz(int num) {
        if (String.valueOf(num).contains("5") && String.valueOf(num).contains("3")) {
            return "FizzBuzz";
        }
        return "";
    }

    private boolean isDivisibleBy(int num, int dividedNum) {
        return num % dividedNum == 0;
    }
}
