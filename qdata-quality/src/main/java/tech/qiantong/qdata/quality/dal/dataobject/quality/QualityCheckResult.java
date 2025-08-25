package tech.qiantong.qdata.quality.dal.dataobject.quality;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QualityCheckResult {

    private String ruleId;         // 质量规则ID
    private String batch;          // 执行批次号（如20250715123001）
    private Integer errorCount;    // 异常记录数
    private Integer totalCount;    // 总记录数
    private String errorMessage;   // 错误信息（如果有异常）

    public QualityCheckResult(String ruleId, String batch, String errorMessage) {
        this.ruleId = ruleId;
        this.batch = batch;
        this.errorMessage = errorMessage;
    }
    public QualityCheckResult(String ruleId, String batch, Integer errorCount,Integer totalCount) {
        this.ruleId = ruleId;
        this.batch = batch;
        this.errorCount = errorCount;
        this.totalCount = totalCount;
    }
}
