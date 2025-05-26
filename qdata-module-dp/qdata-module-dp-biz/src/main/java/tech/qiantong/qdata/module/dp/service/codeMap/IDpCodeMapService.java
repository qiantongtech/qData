package tech.qiantong.qdata.module.dp.service.codeMap;

import com.baomidou.mybatisplus.extension.service.IService;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.dp.controller.admin.codeMap.vo.DpCodeMapPageReqVO;
import tech.qiantong.qdata.module.dp.controller.admin.codeMap.vo.DpCodeMapRespVO;
import tech.qiantong.qdata.module.dp.controller.admin.codeMap.vo.DpCodeMapSaveReqVO;
import tech.qiantong.qdata.module.dp.dal.dataobject.codeMap.DpCodeMapDO;

import java.util.Collection;
import java.util.List;
import java.util.Map;
/**
 * 数据元代码映射Service接口
 *
 * @author qdata
 * @date 2025-01-21
 */
public interface IDpCodeMapService extends IService<DpCodeMapDO> {

    /**
     * 获得数据元代码映射分页列表
     *
     * @param pageReqVO 分页请求
     * @return 数据元代码映射分页列表
     */
    PageResult<DpCodeMapDO> getDpCodeMapPage(DpCodeMapPageReqVO pageReqVO);

    /**
     * 创建数据元代码映射
     *
     * @param createReqVO 数据元代码映射信息
     * @return 数据元代码映射编号
     */
    Long createDpCodeMap(DpCodeMapSaveReqVO createReqVO);

    /**
     * 更新数据元代码映射
     *
     * @param updateReqVO 数据元代码映射信息
     */
    int updateDpCodeMap(DpCodeMapSaveReqVO updateReqVO);

    /**
     * 删除数据元代码映射
     *
     * @param idList 数据元代码映射编号
     */
    int removeDpCodeMap(Collection<Long> idList);

    /**
     * 获得数据元代码映射详情
     *
     * @param id 数据元代码映射编号
     * @return 数据元代码映射
     */
    DpCodeMapDO getDpCodeMapById(Long id);

    /**
     * 获得全部数据元代码映射列表
     *
     * @return 数据元代码映射列表
     */
    List<DpCodeMapDO> getDpCodeMapList();

    /**
     * 获得全部数据元代码映射 Map
     *
     * @return 数据元代码映射 Map
     */
    Map<Long, DpCodeMapDO> getDpCodeMapMap();


    /**
     * 导入数据元代码映射数据
     *
     * @param importExcelList 数据元代码映射数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName 操作用户
     * @return 结果
     */
    String importDpCodeMap(List<DpCodeMapRespVO> importExcelList, boolean isUpdateSupport, String operName);

}
