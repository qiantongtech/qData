package tech.qiantong.qdata.module.ds.service.api;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qdata.common.core.domain.AjaxResult;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.ds.controller.admin.api.vo.*;
import tech.qiantong.qdata.module.ds.dal.dataobject.api.DsApiDO;
import tech.qiantong.qdata.module.ds.dal.dataobject.api.SqlParseDto;

import javax.servlet.http.HttpServletResponse;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * API服务Service接口
 *
 * @author lhs
 * @date 2025-02-12
 */
public interface IDsApiService extends IService<DsApiDO> {

    /**
     * 获得API服务分页列表
     *
     * @param pageReqVO 分页请求
     * @return API服务分页列表
     */
    PageResult<DsApiDO> getDsApiPage(DsApiPageReqVO pageReqVO);

    /**
     * 创建API服务
     *
     * @param createReqVO API服务信息
     * @return API服务编号
     */
    Long createDsApi(DsApiSaveReqVO createReqVO);

    /**
     * 更新API服务
     *
     * @param updateReqVO API服务信息
     */
    int updateDsApi(DsApiSaveReqVO updateReqVO);

    /**
     * 删除API服务
     *
     * @param idList API服务编号
     */
    int removeDsApi(Collection<Long> idList);

    /**
     * 获得API服务详情
     *
     * @param id API服务编号
     * @return API服务
     */
    DsApiDO getDsApiById(Long id);

    /**
     * 获得全部API服务列表
     *
     * @return API服务列表
     */
    List<DsApiDO> getDsApiList();

    /**
     * 获得全部API服务 Map
     *
     * @return API服务 Map
     */
    Map<Long, DsApiDO> getDsApiMap();


    /**
     * 导入API服务数据
     *
     * @param importExcelList API服务数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName 操作用户
     * @return 结果
     */
    String importDsApi(List<DsApiRespVO> importExcelList, boolean isUpdateSupport, String operName);


    SqlParseVo sqlParse(SqlParseDto sqlParseDto);


    Object serviceTesting(DsApiDO dataApi);


    AjaxResult saveDataApi(DsApiDO dataApi);


    AjaxResult updateDataApi(DsApiDO dataApi);


    void releaseDataApi(String id,Long updateId, String updateBy);

    void cancelDataApi(String id,Long updateId, String updateBy);

    DsApiDO repeatFlag(JSONObject jsonObject);

    void queryServiceForwarding(HttpServletResponse response, DsApiReqVO dsApiReqVO);
}
