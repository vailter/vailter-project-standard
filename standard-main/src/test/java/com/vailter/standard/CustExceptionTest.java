package com.vailter.standard;

import com.vailter.standard.domain.UserVo;
import com.vailter.standard.exception.v2.ResponseEnum;
import org.junit.jupiter.api.Test;

import java.util.Date;

public class CustExceptionTest {

    @Test
    void test1() {
        checkNotNull(null);
    }

    /**
     * 校验{@link com.vailter.standard.domain.UserVo}存在
     * @param userVo
     */
    private void checkNotNull(UserVo userVo) {
        ResponseEnum.BAD_LICENCE_TYPE.assertNotNull(userVo,new Date());
    }
}
