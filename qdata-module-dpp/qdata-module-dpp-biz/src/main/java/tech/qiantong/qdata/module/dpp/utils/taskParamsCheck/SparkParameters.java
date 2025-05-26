package tech.qiantong.qdata.module.dpp.utils.taskParamsCheck;

import org.apache.commons.lang3.StringUtils;

public class SparkParameters extends AbstractParameters {

    private String sparkJobName;

    @Override
    public boolean checkParameters() {
        // 假设 sparkJobName 不为空
        return StringUtils.isNotEmpty(sparkJobName);
    }

    // Getter and Setter
    public String getSparkJobName() {
        return sparkJobName;
    }

    public void setSparkJobName(String sparkJobName) {
        this.sparkJobName = sparkJobName;
    }
}
