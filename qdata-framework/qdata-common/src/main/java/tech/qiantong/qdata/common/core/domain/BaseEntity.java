package tech.qiantong.qdata.common.core.domain;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Entity基类
 *
 * @author qdata
 */
public class BaseEntity implements Serializable
{
    private static final long serialVersionUID = 1L;

    /**
     * 搜索值
     */
    @JsonIgnore
    @TableField(exist = false)
    @Schema(description = "搜索值", example = "")
    private String searchValue;

    @Schema(description = "创建者id", example = "")
    @TableField(fill = FieldFill.INSERT)
    private Long creatorId;

    /**
     * 创建者
     */
    @Schema(description = "创建者", example = "")
    @TableField(fill = FieldFill.INSERT)
    private String createBy;

    /**
     * 创建时间
     */
    // 创建时间自动填充
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "创建时间", example = "")
    private Date createTime;

    @Schema(description = "更新者id", example = "")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updaterId;

    /**
     * 更新者
     */
    @Schema(description = "更新者", example = "")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updateBy;

    /**
     * 更新时间
     */
    // 更新时间自动填充
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @Schema(description = "更新时间", example = "")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /**
     * 备注
     */
    @Schema(description = "备注", example = "")
    private String remark;

    /**
     * 请求参数
     */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @TableField(exist = false)
    @Schema(description = "其他请求参数", example = "")
    private Map<String, Object> params;

    public String getSearchValue() {
        return searchValue;
    }

    public void setSearchValue(String searchValue) {
        this.searchValue = searchValue;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    public Long getUpdatorId() {
        return updaterId;
    }

    public void setUpdatorId(Long updaterId) {
        this.updaterId = updaterId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Map<String, Object> getParams() {
        if (params == null) {
            params = new HashMap<>();
        }
        return params;
    }

    public Object getParamByKey(String key) {
        if (params == null) {
            params = new HashMap<>();
        }
        return params.get(key);
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }

}
