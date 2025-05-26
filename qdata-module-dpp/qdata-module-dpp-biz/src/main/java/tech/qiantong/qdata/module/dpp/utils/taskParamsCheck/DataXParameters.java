package tech.qiantong.qdata.module.dpp.utils.taskParamsCheck;

import org.apache.commons.lang3.StringUtils;

public class DataXParameters extends AbstractParameters {

    private String someDataXParam;

    @Override
    public boolean checkParameters() {
        // 假设某个参数不能为空
        return StringUtils.isNotEmpty(someDataXParam);
    }

    // Getter and Setter
    public String getSomeDataXParam() {
        return someDataXParam;
    }

    public void setSomeDataXParam(String someDataXParam) {
        this.someDataXParam = someDataXParam;
    }
}
