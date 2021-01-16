package com.vailter.standard.learn.chain.filter;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.function.Consumer;

/**
 * @author vailter67
 * <p>
 * 1.设计一个链条，和抽象处理方法
 * 2.将具体处理器初始化到链条中，并做抽象方法具体的实现
 * 3.具体处理器之间的引用和处理条件判断
 * 4.设计链条结束标识
 * 1，2 都可以很模块化设计，3，4 设计可以多种多样，比如文中通过 pos 游标，或嵌套动态代理等.
 *
 * <a href="https://mp.weixin.qq.com/s?__biz=Mzg3NjIxMjA1Ng==&mid=2247483730&idx=1&sn=c3a2ae435205953688dcd5ee13bb932e&scene=21#wechat_redirect"></a>
 * <p>
 * 管道和过滤器链
 * <a href="https://blog.csdn.net/abinge317/article/details/52882913"></a>
 */
@FunctionalInterface
public interface Logger {
    /**
     * 枚举log等级
     */
    enum LogLevel {
        //定义 log 等级
        INFO, DEBUG, WARNING, ERROR, FUNCTIONAL_MESSAGE, FUNCTIONAL_ERROR;

        public static LogLevel[] all() {
            return values();
        }
    }

    /**
     * 函数式接口中的唯一抽象方法
     *
     * @param msg
     * @param severity
     */
    void message(String msg, LogLevel severity);

    default Logger appendNext(Logger nextLogger) {
        return (msg, severity) -> {
            // 前序logger处理完才用当前logger处理
            message(msg, severity);
            nextLogger.message(msg, severity);
        };
    }

    static Logger consoleLogger(LogLevel... levels) {
        return logger(levels, msg -> System.err.println("写到终端: " + msg));
    }

    static Logger emailLogger(LogLevel... levels) {
        return logger(levels, msg -> System.err.println("通过邮件发送: " + msg));
    }

    static Logger fileLogger(LogLevel... levels) {
        return logger(levels, msg -> System.err.println("写到日志文件中: " + msg));
    }

    static Logger logger(LogLevel[] levels, Consumer<String> writeMessage) {
        EnumSet<LogLevel> set = EnumSet.copyOf(Arrays.asList(levels));
        return (msg, severity) -> {
            // 判断当前logger是否能处理传递过来的日志级别
            if (set.contains(severity)) {
                writeMessage.accept(msg);
            }
        };
    }

    public static void main(String[] args) {
        /**
         * 构建一个固定顺序的链 【终端记录——邮件记录——文件记录】
         * consoleLogger：终端记录，可以打印所有等级的log信息
         * emailLogger：邮件记录，打印功能性问题 FUNCTIONAL_MESSAGE 和 FUNCTIONAL_ERROR 两个等级的信息
         * fileLogger：文件记录，打印 WARNING 和 ERROR 两个等级信息
         */

        Logger logger = consoleLogger(LogLevel.all())
                .appendNext(emailLogger(LogLevel.FUNCTIONAL_MESSAGE, LogLevel.FUNCTIONAL_ERROR))
                .appendNext(fileLogger(LogLevel.WARNING, LogLevel.ERROR));

        // consoleLogger 可以记录所有 level 的信息
        //logger.message("进入到订单流程，接收到参数，参数内容为XXXX", LogLevel.DEBUG);
        //logger.message("订单记录生成.", LogLevel.INFO);

        // consoleLogger 处理完，fileLogger 要继续处理
        logger.message("订单详细地址缺失", LogLevel.WARNING);
        logger.message("订单省市区信息缺失", LogLevel.ERROR);

        //// consoleLogger 处理完，emailLogger 继续处理
        //logger.message("订单短信通知服务失败", LogLevel.FUNCTIONAL_ERROR);
        //logger.message("订单已派送.", LogLevel.FUNCTIONAL_MESSAGE);
    }
}
