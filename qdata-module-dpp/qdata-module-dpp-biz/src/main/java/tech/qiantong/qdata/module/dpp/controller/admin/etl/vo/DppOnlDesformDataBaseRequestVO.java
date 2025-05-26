package tech.qiantong.qdata.module.dpp.controller.admin.etl.vo;

import com.alibaba.fastjson.JSONArray;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <P>
 * 用途:
 * </p>
 *
 * @author: FXB
 * @create: 2024-05-24 16:01
 **/
@NoArgsConstructor
@AllArgsConstructor
@Data
public class DppOnlDesformDataBaseRequestVO implements Serializable {

//    @ApiModelProperty(value = "关联数据源id")
    protected Integer datasourceId;

//    @ApiModelProperty(value = "数据库名")
    protected String databaseName;

//    @ApiModelProperty(value = "数据库表名称")
    protected String tableNames;

//    @ApiModelProperty(value = "字段")
    protected JSONArray fieldName;

//    @ApiModelProperty(value = "唯一标识字段(用于更新、删除)")
    protected String uniFieldName;
}
