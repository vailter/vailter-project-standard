package com.vailter.standard;

import org.junit.jupiter.api.Test;

import java.text.MessageFormat;
import java.util.Date;

/**
 * 参考：
 * <a>https://blog.csdn.net/zp357252539/article/details/103269456</a>
 */
public class MessageFormatTest {
    @Test
    void test() {
        double num = 8;
        String str = "I am not a {0}, age is {1,number,integer}, height is {2,number,#.#}";

        str = MessageFormat.format(str, num, new Date(), num);
        System.out.println(str);

        //String pig = "{0}{1}{2}{3}{4}{5}{6}{7}{8}{9}{10}{11}{12}{13}{14}{15}{16}";
        //Object[] array = new Object[]{"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q"};
        //String value = MessageFormat.format(pig, array);
        //System.out.println(value);

        String message = "oh, {0,number,#.#} is a pig";
        Object[] array = new Object[]{3.00};
        String value = MessageFormat.format(message, array);
        System.out.println(value);
    }
}
