/*
 * Copyright © 2025 Qiantong Technology Co., Ltd.
 * qData Data Middle Platform (Open Source Edition)
 *  *
 * License:
 * Released under the Apache License, Version 2.0.
 * You may use, modify, and distribute this software for commercial purposes
 * under the terms of the License.
 *  *
 * Special Notice:
 * All derivative versions are strictly prohibited from modifying or removing
 * the default system logo and copyright information.
 * For brand customization, please contact the official team for authorization.
 *  *
 * More information: https://qdata.qiantong.tech/business.html
 *  *
 * ============================================================================
 *  *
 * 版权所有 © 2025 江苏千桐科技有限公司
 * qData 数据中台（开源版）
 *  *
 * 许可协议：
 * 本项目基于 Apache License 2.0 开源协议发布，
 * 允许在遵守协议的前提下进行商用、修改和分发。
 *  *
 * 特别说明：
 * 所有衍生版本不得修改或移除系统默认的 LOGO 和版权信息；
 * 如需定制品牌，请通过官方渠道申请品牌授权。
 *  *
 * 更多信息请访问：https://qdata.qiantong.tech/business.html
 */

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
