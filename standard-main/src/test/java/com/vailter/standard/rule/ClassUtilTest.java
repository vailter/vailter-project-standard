package com.vailter.standard.rule;

import com.bstek.urule.ClassUtils;
import com.vailter.standard.rule.urule.domain.Customer;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

public class ClassUtilTest {
    @Test
    void testClass() throws IOException {
        //File file=new File("d:/customer.xml");
        //ClassUtils.classToXml(Customer.class, file);

        System.out.println(System.getProperty("java.io.tmpdir"));
    }
}
