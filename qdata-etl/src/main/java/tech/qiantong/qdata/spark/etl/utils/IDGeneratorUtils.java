package tech.qiantong.qdata.spark.etl.utils;

/**
 * <P>
 * 用途:生成数字id
 * </p>
 *
 * @author: FXB
 * @create: 2019-12-26 08:59
 **/
public class IDGeneratorUtils {

    private static long workerId = 1;

    private static long datacenterId = 1;

    public static long getLongId(){
        return CodeGenerateUtils.genCode();
    }
    public static String getStringId(){
        return String.valueOf(CodeGenerateUtils.genCode());
    }

    public static void main(String[] args) {
        System.out.println(getStringId());
    }
}
