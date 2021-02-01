package com.vailter.standard;

import com.vailter.standard.exception.v2.ResponseEnum;
import com.vailter.standard.vo.UserVo;
import org.junit.jupiter.api.Test;

public class CustExceptionTest {

    @Test
    void test1() {
        checkNotNull(null);
    }

    /**
     * 校验{@link UserVo}存在
     *
     * @param userVo
     */
    private void checkNotNull(UserVo userVo) {
//        ResponseEnum.BAD_LICENCE_TYPE.assertNotNull(userVo, new Date());
        ResponseEnum.LICENCE_NOT_FOUND.assertNotNull(userVo, "1111", null);
    }
}
