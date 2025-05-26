package tech.qiantong.qdata.module.dpp.controller.admin.etl.vo;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
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
public class DppOnlDesformDataSaveRequestVO extends DppOnlDesformDataBaseRequestVO implements Serializable {

//    @ApiModelProperty(value = "数据")
    private JSONObject data;

    @Builder(toBuilder = true)
    public DppOnlDesformDataSaveRequestVO(Integer datasourceId, String databaseName, String tableNames, JSONArray fieldName, String uniFieldName, JSONObject data) {
        super(datasourceId, databaseName, tableNames, fieldName, uniFieldName);
        this.data = data;
    }
}
