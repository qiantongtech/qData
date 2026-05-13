package tech.qiantong.qdata.module.mc.controller.admin.task.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import tech.qiantong.qdata.module.mc.controller.admin.metadata.vo.McColumnSaveReqVO;

import java.util.List;

@Data
@AllArgsConstructor
public class TableProcessResult {
    private Long addCount;
    private Long delCount;
    private Long updateCount;

    private Long totalCount;
    private Long successCount;
    private List<Long> updateTableIds;
    private List<McColumnSaveReqVO> mcColumnReqList;

    public TableProcessResult(Long addCount, Long updateCount, Long successCount) {
        this.addCount = addCount;
        this.updateCount = updateCount;
        this.successCount = successCount;
    }

    public TableProcessResult(Long addCount, Long updateCount, Long successCount, List<McColumnSaveReqVO> columnReqDTOS) {

        this.addCount = addCount;
        this.updateCount = updateCount;
        this.successCount = successCount;
        this.mcColumnReqList = columnReqDTOS;
    }

    public TableProcessResult(Long addCount, Long delCount, Long updateCount, Long totalCount, Long successCount, List<Long> updateTableIds) {

        this.addCount = addCount;
        this.updateCount = updateCount;
        this.delCount = delCount;
        this.totalCount = totalCount;
        this.successCount = successCount;
        this.updateTableIds = updateTableIds;
    }
}
