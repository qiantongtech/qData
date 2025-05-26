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
