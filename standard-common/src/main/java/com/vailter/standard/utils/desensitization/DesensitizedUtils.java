package com.vailter.standard.utils.desensitization;

import org.apache.commons.lang3.StringUtils;

public class DesensitizedUtils {

    /**
     * 【中文姓名】只显示第一个汉字，其他隐藏为2个星号<例子：李**>
     *
     * @param fullName 姓名
     * @return
     */
    public static String chineseName(String fullName) {
        return left(fullName, 1);
    }

    /**
     * 只显示第一个汉字，其他隐藏为2个星号<例子：李**>
     *
     * @param fullName 名称
     * @param index    第index位开始脱敏
     * @return
     */
    public static String left(String fullName, int index) {
        if (StringUtils.isBlank(fullName)) {
            return "";
        }
        String name = StringUtils.left(fullName, index);
        return StringUtils.rightPad(name, StringUtils.length(fullName), "*");
    }

    /**
     * 110****58，前面保留3位明文，后面保留2位明文
     *
     * @param name
     * @param index 3
     * @param end   2
     * @return
     */
    public static String around(String name, int index, int end) {
        if (StringUtils.isBlank(name)) {
            return "";
        }
        return StringUtils.left(name, index).concat(StringUtils.removeStart(StringUtils.leftPad(StringUtils.right(name, end), StringUtils.length(name), "*"), "***"));
    }

    /**
     * 后四位，其他隐藏<例子：****1234>
     *
     * @param num
     * @return
     */
    public static String right(String num, int end) {
        if (StringUtils.isBlank(num)) {
            return "";
        }
        return StringUtils.leftPad(StringUtils.right(num, end), StringUtils.length(num), "*");
    }

    // 手机号码前三后四脱敏
    public static String mobileEncrypt(String mobile) {
        if (StringUtils.isEmpty(mobile) || (mobile.length() != 11)) {
            return mobile;
        }
        return mobile.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
    }

    /**
     * 【固定电话】 后四位，其他隐藏，比如1234
     *
     * @param num
     * @return
     */
    public static String fixedPhone(String num) {
        if (StringUtils.isBlank(num)) {
            return "";
        }
        return StringUtils.leftPad(StringUtils.right(num, 4), StringUtils.length(num), "*");
    }

    //身份证前三后四脱敏
    public static String idCardEncrypt(String id) {
        if (StringUtils.isEmpty(id) || (id.length() < 8)) {
            return id;
        }
        return id.replaceAll("(?<=\\w{3})\\w(?=\\w{4})", "*");
    }

    //护照前2后3位脱敏，护照一般为8或9位
    public static String idPassport(String id) {
        if (StringUtils.isEmpty(id) || (id.length() < 8)) {
            return id;
        }
        return id.substring(0, 2) + new String(new char[id.length() - 5]).replace("\0", "*") + id.substring(id.length() - 3);
    }

    /**
     * 证件后几位脱敏
     *
     * @param id
     * @param sensitiveSize
     * @return
     */
    public static String idPassport(String id, int sensitiveSize) {
        if (StringUtils.isBlank(id)) {
            return "";
        }
        int length = StringUtils.length(id);
        return StringUtils.rightPad(StringUtils.left(id, length - sensitiveSize), length, "*");
    }

    /**
     * 【地址】只显示到地区，不显示详细地址，比如：北京市海淀区****
     *
     * @param address
     * @param sensitiveSize 敏感信息长度
     * @return
     */
    public static String address(String address, int sensitiveSize) {
        if (StringUtils.isBlank(address)) {
            return "";
        }
        //int length = StringUtils.length(address);
        //return StringUtils.rightPad(StringUtils.left(address, length - sensitiveSize), length, "*");
        return address.replaceAll("\\d+", "**");
    }

    /**
     * 【电子邮箱 邮箱前缀仅显示第一个字母，前缀其他隐藏，用星号代替，@及后面的地址显示，比如：d**@126.com>
     *
     * @param email
     * @return
     */
    public static String email(String email) {
        if (StringUtils.isBlank(email)) {
            return "";
        }
        int index = StringUtils.indexOf(email, "@");
        if (index <= 1) {
            return email;
        } else {
            return StringUtils.rightPad(StringUtils.left(email, 1), index, "*").concat(StringUtils.mid(email, index, StringUtils.length(email)));
        }
    }

    /**
     * 【银行卡号】前六位，后四位，其他用星号隐藏每位1个星号，比如：6222600**********1234>
     *
     * @param cardNum
     * @return
     */
    public static String bankCard(String cardNum) {
        if (StringUtils.isBlank(cardNum)) {
            return "";
        }
        return StringUtils.left(cardNum, 6).concat(StringUtils.removeStart(StringUtils.leftPad(StringUtils.right(cardNum, 4), StringUtils.length(cardNum), "*"), "******"));
    }

    /**
     * 【密码】密码的全部字符都用*代替，比如：******
     *
     * @param password
     * @return
     */
    public static String password(String password) {
        if (StringUtils.isBlank(password)) {
            return "";
        }
        String pwd = StringUtils.left(password, 0);
        return StringUtils.rightPad(pwd, StringUtils.length(password), "*");
    }

    /**
     * 【车牌号】前两位后一位，比如：苏M****5
     *
     * @param carNumber
     * @return
     */
    public static String carNumber(String carNumber) {
        if (StringUtils.isBlank(carNumber)) {
            return "";
        }
        return StringUtils.left(carNumber, 2).
                concat(StringUtils.removeStart(StringUtils.leftPad(StringUtils.right(carNumber, 1), StringUtils.length(carNumber), "*"), "**"));

    }

    public static void main(String[] args) {
        System.out.println(left("张三", 1));
        System.out.println(idPassport("123456", 4));
        System.out.println(idPassport("11eqweqwe", 4));
        System.out.println(mobileEncrypt("13388888888"));
        System.out.println(idCardEncrypt("340521199009104233")); // 130722***********671X
    }
}
