package org.zxy.sell.utils;

import java.util.Random;

public class KeyUtil {

    private static Random random = new Random();

    /**
     * 生成唯一主键；
     * 格式： 时间+随机数
     * @return
     */
    public static synchronized String getUniqueKey() {

        Integer number = random.nextInt(900000) + 100000;

        return System.currentTimeMillis() + String.valueOf(number);
    }
}
