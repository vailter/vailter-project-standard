package com.vailter.standard;

import com.vailter.standard.ret.Result;
import org.junit.Test;

public class TestRet {
    @Test
    public void testRet() {
        System.out.println(Result.success(""));
        System.out.println(Result.error(null));
    }
}
