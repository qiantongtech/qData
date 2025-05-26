package tech.qiantong.qdata.module.ds.api.api.dto;

import com.alibaba.fastjson2.JSONArray;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * API服务 DTO 对象 DS_API
 *
 * @author lhs
 * @date 2025-02-12
 */
@Data
public class DsApiReqDTO {

    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    private List<Long> idList;

    /** 类目id */
    private Long catId;
    private List<String> catIds;
    /** 类目编码 */
    private String catCode;

    /** 类目名称 */
    private String catName;

    /** API服务名称 */
    private String name;

    /** API版本 */
    private String apiVersion;

    /** API路径 */
    private String apiUrl;

    /** 请求方式 */
    private String reqMethod;

    /** 服务提供类型 */
    private String apiServiceType;

    /** 返回结果类型
     * 1-详情、2-列表、3-分页
     * */
    private String resDataType;

    /** IP黑名单多个，隔开 */
    private String denyIp;

    /** 执行配置JSON */
    private String configJson;

    /** 限流配置JSON */
    private String limitJson;

    /**
     *转发类型;1:API 2:地理空间数据'
     */
    private String transmitType;

    /**
     *apiId
     */
    private String apiId;

    /**
     *Header配置json
     */
    private String headerJson;

    /** 请求参数 */
    @TableField(value = "REQ_PARAMS", exist = false, typeHandler = JacksonTypeHandler.class)
    private List<ReqParam> reqParamsList;

    /** 返回参数 */
    @TableField(value = "RES_PARAMS",exist = false, typeHandler = JacksonTypeHandler.class)
    private List<ResParam> resParamsList;

    private String resParams;

    private String reqParams;

    /** 描述 */
    private String description;

    /** 状态 */
    private String status;

    /** 是否有效 */
    private Boolean validFlag;

    /** 删除标志 */
    @TableLogic
    private Boolean delFlag;

    private ExecuteConfig executeConfig;

    /** 开放属性 */
    private String openAttribute;

    /** 信息提供方 */
    private String provider;

    /** 共享类型 */
    private String shareType;

    /** 更新周期 */
    private String updatePeriod;

    /** 发布时间 */
    private String createTime;
    /** 更新时间 */
    private String updateTime;
    /** 流程状态，0：审批中，1：审批通过，2：审批拒绝，3：审批撤回，4：审批异常 */
    private String actStatus;
    /** 流程业务实例id */
    private String processInstanceId;


    /** 被申请次数 */
    private Integer count;
    /**
     * 是否开启缓存 0:否 1:是
     */
    private String cacheSwitch;

    /** 操作用户 */
    private Long userId;

    private List<String> processInstanceIdList;

    /** 申请人id */
    private Long applyId;

    /** 申请人 */
    private String applyBy;

    /** 申请时间 */
    private Date applyTime;

    /** 申请理由 */
    private String applyReason;

    /** 有效期类型 */
    private String validType;

    /** 时效开始时间 */
    private Date validStartTime;

    /** 时效结束时间 */
    private Date validEndTime;

    /** 数据权限 */
    private String authId;

    private String authName;

    //写个方法将resParams、reqParams转换成reqParamsList、resParamsList
    public  void setResParamsList() {
        if (this.resParams != null) {
            this.resParamsList = JSONArray.parseArray(this.resParams, ResParam.class);
        }
        if (this.reqParams != null) {
            this.reqParamsList = JSONArray.parseArray(this.reqParams, ReqParam.class);
        }
    }

}
