package tech.qiantong.qdata.module.ds.service.apply;

import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.ds.controller.admin.apply.vo.DsApiApplyPageReqVO;
import tech.qiantong.qdata.module.ds.controller.admin.apply.vo.DsApiApplyRespVO;
import tech.qiantong.qdata.module.ds.controller.admin.apply.vo.DsApiApplySaveReqVO;
import tech.qiantong.qdata.module.ds.dal.dataobject.apply.DsApiApplyDO;

import java.util.Collection;
import java.util.List;
import java.util.Map;
/**
 * API服务-申请Service接口
 *
 * @author qdata
 * @date 2025-04-22
 */
public interface IDsApiApplyService extends IService<DsApiApplyDO> {

    /**
     * 获得API服务-申请分页列表
     *
     * @param pageReqVO 分页请求
     * @return API服务-申请分页列表
     */
    PageResult<DsApiApplyDO> getDsApiApplyPage(DsApiApplyPageReqVO pageReqVO);

    /**
     * 创建API服务-申请
     *
     * @param createReqVO API服务-申请信息
     * @return API服务-申请编号
     */
    Map<String,Long> createDsApiApply(DsApiApplySaveReqVO createReqVO);

    /**
     * 更新API服务-申请
     *
     * @param updateReqVO API服务-申请信息
     */
    int updateDsApiApply(DsApiApplySaveReqVO updateReqVO);

    /**
     * 删除API服务-申请
     *
     * @param idList API服务-申请编号
     */
    int removeDsApiApply(Collection<Long> idList);

    /**
     * 获得API服务-申请详情
     *
     * @param id API服务-申请编号
     * @return API服务-申请
     */
    DsApiApplyDO getDsApiApplyById(Long id);

    /**
     * 获得全部API服务-申请列表
     *
     * @return API服务-申请列表
     */
    List<DsApiApplyDO> getDsApiApplyList();

    /**
     * 获得全部API服务-申请 Map
     *
     * @return API服务-申请 Map
     */
    Map<Long, DsApiApplyDO> getDsApiApplyMap();


    /**
     * 导入API服务-申请数据
     *
     * @param importExcelList API服务-申请数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName 操作用户
     * @return 结果
     */
    String importDsApiApply(List<DsApiApplyRespVO> importExcelList, boolean isUpdateSupport, String operName);

    Map<String, Object> getFlyfowApiApply(DsApiApplySaveReqVO dsApiApply);
}
