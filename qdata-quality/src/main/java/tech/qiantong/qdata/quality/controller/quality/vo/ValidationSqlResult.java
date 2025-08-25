package tech.qiantong.qdata.quality.controller.quality.vo;

import com.alibaba.fastjson2.JSONObject;
import lombok.Data;
import tech.qiantong.qdata.common.database.core.DbColumn;

import java.io.Serializable;
import java.util.List;

@Data
public class ValidationSqlResult  implements Serializable {
    private List<DbColumn> showErrorColumns;
    List<JSONObject> errorList;
    List<JSONObject> dataList;


    /**
     * 分页参数（可选）
     */
    private Integer pageNum;

    private Integer pageSize;



    /**
     * 偏移量（从第几行开始），自动计算
     */
    private Integer offset;

    /**
     * 限制行数（每页大小），自动设置
     */
    private Integer limit;
}
