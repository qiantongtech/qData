package tech.qiantong.qdata.module.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.system.domain.SystemContentDO;
import tech.qiantong.qdata.module.system.domain.vo.SystemContentPageReqVO;
import tech.qiantong.qdata.module.system.domain.vo.SystemContentRespVO;
import tech.qiantong.qdata.module.system.domain.vo.SystemContentSaveReqVO;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 系统配置Service接口
 *
 * @author qdata
 * @date 2024-12-31
 */
public interface ISystemContentService extends IService<SystemContentDO> {

    /**
     * 获得系统配置分页列表
     *
     * @param pageReqVO 分页请求
     * @return 系统配置分页列表
     */
    PageResult<SystemContentDO> getSystemContentPage(SystemContentPageReqVO pageReqVO);

    /**
     * 创建系统配置
     *
     * @param createReqVO 系统配置信息
     * @return 系统配置编号
     */
    Long createSystemContent(SystemContentSaveReqVO createReqVO);

    /**
     * 更新系统配置
     *
     * @param updateReqVO 系统配置信息
     */
    int updateSystemContent(SystemContentSaveReqVO updateReqVO);

    /**
     * 删除系统配置
     *
     * @param idList 系统配置编号
     */
    int removeSystemContent(Collection<Long> idList);

    /**
     * 获得系统配置详情
     *
     * @param id 系统配置编号
     * @return 系统配置
     */
    SystemContentDO getSystemContentById(Long id);

    /**
     * 获得全部系统配置列表
     *
     * @return 系统配置列表
     */
    List<SystemContentDO> getSystemContentList();

    /**
     * 获得全部系统配置 Map
     *
     * @return 系统配置 Map
     */
    Map<Long, SystemContentDO> getSystemContentMap();


    /**
     * 导入系统配置数据
     *
     * @param importExcelList 系统配置数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName        操作用户
     * @return 结果
     */
    String importSystemContent(List<SystemContentRespVO> importExcelList, boolean isUpdateSupport, String operName);

}
