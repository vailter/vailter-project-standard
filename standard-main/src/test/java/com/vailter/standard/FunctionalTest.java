package com.vailter.standard;

import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FunctionalTest {

    @Test
    public void testClean() {
        UserDAO userDAO = new UserDAO();
        Integer remainDays = 10;


        // 清除过期数据
        //cleanExpiredData("用户日志表", remainDays, new CleanExpiredDataOperator() {
        //    @Override
        //    public List<Date> queryExpiredDate(Integer remainDays) {
        //        System.out.println("queryExpiredDate：" + remainDays);
        //        return userDAO.queryExpiredDate(remainDays);
        //    }
        //
        //    @Override
        //    public void cleanExpiredData(Date expiredDate) {
        //        System.out.println("222");
        //        userDAO.cleanExpiredData(expiredDate);
        //    }
        //});

        // 清除过期数据
        // (remainDays1) -> userDAO.queryExpiredDate(remainDays1)
        // (date) -> userDAO.cleanExpiredData(date)
        cleanExpiredData("用户日志表", remainDays, userDAO::queryExpiredDate, userDAO::cleanExpiredData);
    }


    // 清除过期数据函数
    private void cleanExpiredData(String tableName, Integer remainDays, CleanExpiredDataOperator cleanExpiredDataOperator) {
        // 功能实现代码
        System.out.println(tableName);
        List<Date> dates = cleanExpiredDataOperator.queryExpiredDate(remainDays);
        System.out.println(dates);
    }

    // 清除过期操作接口
    interface CleanExpiredDataOperator {
        // 查询过期日期
        List<Date> queryExpiredDate(Integer remainDays);

        // 清除过期数据
        void cleanExpiredData(Date expiredDate);

    }
//   #### 建议方案：

    // 清除过期数据函数
    private void cleanExpiredData(String tableName, Integer remainDays, QueryExpiredDateOperator queryExpiredDateOperator, CleanExpiredDataOperator1 cleanExpiredDataOperator) {
        // 功能实现代码
        System.out.println(tableName);
        List<Date> dates = queryExpiredDateOperator.queryExpiredDate(remainDays);
        dates.forEach(cleanExpiredDataOperator::cleanExpiredData);
    }

    // 查询过期日期接口
    @FunctionalInterface
    interface QueryExpiredDateOperator {
        // 查询过期日期
        List<Date> queryExpiredDate(Integer remainDays);
    }

    // 清除过期操作接口
    @FunctionalInterface
    interface CleanExpiredDataOperator1 {
        // 清除过期数据
        void cleanExpiredData(Date expiredDate);
    }

    public static class UserDAO {

        List<Date> queryExpiredDate(Integer remainDays) {
            return Stream.of(new Date()).collect(Collectors.toList());
        }

        void cleanExpiredData(Date date) {
            System.out.println("clean:" + date);
        }
    }


}
