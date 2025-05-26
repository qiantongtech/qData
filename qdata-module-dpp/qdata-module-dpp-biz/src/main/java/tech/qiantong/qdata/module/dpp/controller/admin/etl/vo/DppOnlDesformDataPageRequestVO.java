package tech.qiantong.qdata.module.dpp.controller.admin.etl.vo;

import com.alibaba.fastjson.JSONArray;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * <P>
 * 用途:
 * </p>
 *
 * @author: FXB
 * @create: 2024-05-24 16:01
 **/
@Data
public class DppOnlDesformDataPageRequestVO extends DppOnlDesformDataBaseRequestVO implements Serializable {

//    @ApiModelProperty(value = "第几页")
    private Integer pageNum;

//    @ApiModelProperty(value = "每页数量")
    private Integer pageSize;

    @Builder(toBuilder = true)
    public DppOnlDesformDataPageRequestVO(Integer datasourceId, String databaseName, String tableNames, JSONArray fieldName, String uniFieldName, Integer pageNum, Integer pageSize) {
        super(datasourceId, databaseName, tableNames, fieldName, uniFieldName);
        this.pageNum = pageNum;
        this.pageSize = pageSize;
    }
}
