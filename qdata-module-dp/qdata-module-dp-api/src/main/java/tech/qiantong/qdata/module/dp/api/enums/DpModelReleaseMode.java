package tech.qiantong.qdata.module.dp.api.enums;


/**
 * <P>
 * Purpose: Logical Model Release Mode
 * </p>
 *
 * @author: FXB
 * @create: 2026-04-26 15:39
 **/
public enum DpModelReleaseMode {

    DELETE_REBUILD("1", "Delete and Rebuild"),
    INCREMENT_RELEASE("2", "Incremental Release");
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
     * Get enum by code
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
