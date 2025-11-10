/*
 * Copyright © 2025 Qiantong Technology Co., Ltd.
 * qData Data Middle Platform (Open Source Edition)
 *  *
 * License:
 * Released under the Apache License, Version 2.0.
 * You may use, modify, and distribute this software for commercial purposes
 * under the terms of the License.
 *  *
 * Special Notice:
 * All derivative versions are strictly prohibited from modifying or removing
 * the default system logo and copyright information.
 * For brand customization, please contact the official team for authorization.
 *  *
 * More information: https://qdata.qiantong.tech/business.html
 *  *
 * ============================================================================
 *  *
 * 版权所有 © 2025 江苏千桐科技有限公司
 * qData 数据中台（开源版）
 *  *
 * 许可协议：
 * 本项目基于 Apache License 2.0 开源协议发布，
 * 允许在遵守协议的前提下进行商用、修改和分发。
 *  *
 * 特别说明：
 * 所有衍生版本不得修改或移除系统默认的 LOGO 和版权信息；
 * 如需定制品牌，请通过官方渠道申请品牌授权。
 *  *
 * 更多信息请访问：https://qdata.qiantong.tech/business.html
 */

package tech.qiantong.qdata.common.enums;

public class DataConstant {



    /**
     * 通用的是否
     */
    public enum TrueOrFalse {
        FALSE("0",false),
        TRUE("1",true);

        TrueOrFalse(String key, boolean val){
            this.key = key;
            this.val = val;
        }

        private final String key;
        private final boolean val;

        public String getKey() {
            return key;
        }

        public boolean getVal() {
            return val;
        }
    }



    /**
     * 通用的启用禁用状态
     */
    public enum EnableState {
        DISABLE("0","禁用"),
        ENABLE("1","启用");

        EnableState(String key, String val){
            this.key = key;
            this.val = val;
        }

        private final String key;
        private final String val;

        public String getKey() {
            return key;
        }

        public String getVal() {
            return val;
        }
    }

    /**
     * 流程审核状态
     */
    public enum AuditState{
        WAIT("1","待提交"),
        BACK("2", "已退回"),
        AUDIT("3","审核中"),
        AGREE("4","通过"),
        REJECT("5","不通过"),
        CANCEL("6", "已撤销");

        AuditState(String key, String val){
            this.key = key;
            this.val = val;
        }

        private final String key;
        private final String val;

        public String getKey() {
            return key;
        }

        public String getVal() {
            return val;
        }
    }





    /**
     * Api状态
     */
    public enum ApiState{
        WAIT("1","待发布"),
        RELEASE("2","已发布"),
        CANCEL("3","已下线");
        ApiState(String key, String val){
            this.key = key;
            this.val = val;
        }

        private final String key;
        private final String val;

        public String getKey() {
            return key;
        }

        public String getVal() {
            return val;
        }
    }
}
