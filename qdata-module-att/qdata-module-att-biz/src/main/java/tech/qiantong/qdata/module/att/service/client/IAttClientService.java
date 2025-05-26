package tech.qiantong.qdata.module.att.service.client;

import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.att.controller.admin.client.vo.AttClientPageReqVO;
import tech.qiantong.qdata.module.att.controller.admin.client.vo.AttClientRespVO;
import tech.qiantong.qdata.module.att.controller.admin.client.vo.AttClientSaveReqVO;
import tech.qiantong.qdata.module.att.dal.dataobject.client.AttClientDO;

import java.util.Collection;
import java.util.List;
import java.util.Map;
/**
 * 应用管理Service接口
 *
 * @author qdata
 * @date 2025-02-18
 */
public interface IAttClientService extends IService<AttClientDO> {

    /**
     * 获得应用管理分页列表
     *
     * @param pageReqVO 分页请求
     * @return 应用管理分页列表
     */
    PageResult<AttClientDO> getAttClientPage(AttClientPageReqVO pageReqVO);

    /**
     * 创建应用管理
     *
     * @param createReqVO 应用管理信息
     * @return 应用管理编号
     */
    Long createAttClient(AttClientSaveReqVO createReqVO);

    /**
     * 更新应用管理
     *
     * @param updateReqVO 应用管理信息
     */
    int updateAttClient(AttClientSaveReqVO updateReqVO);

    /**
     * 删除应用管理
     *
     * @param idList 应用管理编号
     */
    int removeAttClient(Collection<Long> idList);

    /**
     * 获得应用管理详情
     *
     * @param id 应用管理编号
     * @return 应用管理
     */
    AttClientDO getAttClientById(Long id);

    /**
     * 获得全部应用管理列表
     *
     * @return 应用管理列表
     */
    List<AttClientDO> getAttClientList();

    /**
     * 获得全部应用管理 Map
     *
     * @return 应用管理 Map
     */
    Map<Long, AttClientDO> getAttClientMap();


    /**
     * 导入应用管理数据
     *
     * @param importExcelList 应用管理数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName 操作用户
     * @return 结果
     */
    String importAttClient(List<AttClientRespVO> importExcelList, boolean isUpdateSupport, String operName);

}
