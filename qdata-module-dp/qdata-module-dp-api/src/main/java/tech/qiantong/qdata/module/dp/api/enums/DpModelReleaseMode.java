package tech.qiantong.qdata.module.dp.api.enums;


/**
 * <P>
 * 用途:逻辑模型发布模式
 * </p>
 *
 * @author: FXB
 * @create: 2026-04-26 15:39
 **/
public enum DpModelReleaseMode {

    DELETE_REBUILD("1", "删除重建"),
    INCREMENT_RELEASE("2", "增量发布");
    private String code;
    private String desc;

    DpModelReleaseMode(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    /**
     * 根据code获取枚举的方法
     *
     * @param code
     * @return
     */
    public static DpModelReleaseMode getByCode(String code) {
        for (DpModelReleaseMode value : DpModelReleaseMode.values()) {
            if (value.code.equals(code)) {
                return value;
            }
        }
        return null;
    }
}
