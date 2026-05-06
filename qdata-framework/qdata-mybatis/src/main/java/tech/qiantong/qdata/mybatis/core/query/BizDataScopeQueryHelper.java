package tech.qiantong.qdata.mybatis.core.query;

import org.apache.commons.lang3.StringUtils;

public final class BizDataScopeQueryHelper {

    private static final String SCOPE_SELF = "SELF";
    private static final String SCOPE_DEPT = "DEPT";

    private BizDataScopeQueryHelper() {
    }

    public static boolean useSelfScopeWithUnassigned(String bizScopeMode, Boolean bizScopeIncludeUnassigned, Long userId) {
        return Boolean.TRUE.equals(bizScopeIncludeUnassigned)
                && StringUtils.equalsIgnoreCase(SCOPE_SELF, bizScopeMode)
                && userId != null;
    }

    public static boolean useDeptScopeWithUnassigned(String bizScopeMode, Boolean bizScopeIncludeUnassigned, Long deptId) {
        return Boolean.TRUE.equals(bizScopeIncludeUnassigned)
                && StringUtils.equalsIgnoreCase(SCOPE_DEPT, bizScopeMode)
                && deptId != null;
    }
}
