package tech.qiantong.qdata.quality.controller.quality.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tech.qiantong.qdata.quality.dal.dataobject.quality.CheckErrorData;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CheckErrorDataReqDTO implements Serializable {

    /**
     * 报告id
     */
    private String reportId;

    private String id;

    private List<String> errorDataId;

    /**
     * 1-修改数据
     * 2-修改备注
     * 3-修改状态（忽略）
     */
    private String updateType;
    private String remark;

    private Integer pageNum;
    private Integer pageSize;


    private Integer repair;

    private Map<String,Object> updatedData;

    private Map<String,Object> oldData;

    private Long datasourceId;
    private String tableName;
    private String dataIssueId;

    /**
     * 查询参数
     */
    private Map<String,Object> keyWordData;

}
