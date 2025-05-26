package tech.qiantong.qdata.common.utils.uuid;

import java.security.SecureRandom;

/**
 * ID生成器工具类
 *
 * @author qdata
 */
public class IdUtils
{
    private static final SecureRandom RANDOM = new SecureRandom();

    /**
     * 获取随机UUID
     *
     * @return 随机UUID
     */
    public static String randomUUID()
    {
        return UUID.randomUUID().toString();
    }

    /**
     * 简化的UUID，去掉了横线
     *
     * @return 简化的UUID，去掉了横线
     */
    public static String simpleUUID()
    {
        return UUID.randomUUID().toString(true);
    }

    /**
     * 获取随机UUID，使用性能更好的ThreadLocalRandom生成UUID
     *
     * @return 随机UUID
     */
    public static String fastUUID()
    {
        return UUID.fastUUID().toString();
    }

    /**
     * 简化的UUID，去掉了横线，使用性能更好的ThreadLocalRandom生成UUID
     *
     * @return 简化的UUID，去掉了横线
     */
    public static String fastSimpleUUID()
    {
        return UUID.fastUUID().toString(true);
    }



    /**
     * 生成一个长整型的人造ID，组合当前时间戳和随机数部分，确保较高的唯一性
     *
     * @return long类型的人工ID
     */
    public static long generateArtificialId() {
        // 获取当前时间戳（毫秒）
        long timestamp = System.currentTimeMillis();
        // 随机生成一个 0-999 的数字，确保为三位数（不足3位则左侧补0，但在数值上直接相加即可）
        int randomDigits = RANDOM.nextInt(1000);
        // 将时间戳扩大 1000 倍后加上随机数，保证 ID 是一个长整型数字
        return timestamp * 1000 + randomDigits;
    }
}
