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
 * For brand customization, please apply for brand customization authorization via official channels.
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
 * 如需定制品牌，请通过官方渠道申请品牌定制授权。
 *  *
 * 更多信息请访问：https://qdata.qiantong.tech/business.html
 */

package tech.qiantong.qdata.module.da.service.assetchild.api.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.common.database.exception.DataQueryException;
import tech.qiantong.qdata.common.exception.ServiceException;
import tech.qiantong.qdata.common.utils.MessageUtils;
import tech.qiantong.qdata.common.httpClient.HeaderEntity;
import tech.qiantong.qdata.common.httpClient.HttpUtils;
import tech.qiantong.qdata.common.utils.StringUtils;
import tech.qiantong.qdata.common.utils.object.BeanUtils;
import tech.qiantong.qdata.module.da.api.service.assetchild.api.IDaApiOutService;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.api.vo.DaAssetApiPageReqVO;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.api.vo.DaAssetApiReqVO;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.api.vo.DaAssetApiRespVO;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.api.vo.DaAssetApiSaveReqVO;
import tech.qiantong.qdata.module.da.dal.dataobject.assetchild.api.DaAssetApiDO;
import tech.qiantong.qdata.module.da.dal.mapper.assetchild.api.DaAssetApiMapper;
import tech.qiantong.qdata.module.da.service.assetchild.api.IDaAssetApiService;
import tech.qiantong.qdata.mybatis.core.query.LambdaQueryWrapperX;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 数据资产-外部APIService业务层处理
 *
 * @author qdata
 * @date 2025-04-14
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class DaAssetApiServiceImpl  extends ServiceImpl<DaAssetApiMapper,DaAssetApiDO> implements IDaAssetApiService, IDaApiOutService {
    @Resource
    private DaAssetApiMapper daAssetApiMapper;

    @Override
    public PageResult<DaAssetApiDO> getDaAssetApiPage(DaAssetApiPageReqVO pageReqVO) {
        return daAssetApiMapper.selectPage(pageReqVO);
    }

    @Override
    public DaAssetApiRespVO getDaAssetApiByAssetId(Long assetId) {
        LambdaQueryWrapperX<DaAssetApiDO> queryWrapperX = new LambdaQueryWrapperX<>();
        queryWrapperX.eqIfPresent(DaAssetApiDO::getAssetId,assetId);
        DaAssetApiDO daAssetApiDO = daAssetApiMapper.selectOne(queryWrapperX);
        return BeanUtils.toBean(daAssetApiDO, DaAssetApiRespVO.class);
    }

    @Override
    public Long createDaAssetApi(DaAssetApiSaveReqVO createReqVO) {
        DaAssetApiDO dictType = BeanUtils.toBean(createReqVO, DaAssetApiDO.class);
        daAssetApiMapper.insert(dictType);
        return dictType.getId();
    }

    @Override
    public int updateDaAssetApi(DaAssetApiSaveReqVO updateReqVO) {
        // 相关校验

        // 更新数据资产-外部API
        DaAssetApiDO updateObj = BeanUtils.toBean(updateReqVO, DaAssetApiDO.class);
        return daAssetApiMapper.updateById(updateObj);
    }
    @Override
    public int removeDaAssetApi(Collection<Long> idList) {
        // 批量删除数据资产-外部API
        return daAssetApiMapper.deleteBatchIds(idList);
    }

    @Override
    public DaAssetApiDO getDaAssetApiById(Long id) {
        return daAssetApiMapper.selectById(id);
    }

    @Override
    public List<DaAssetApiDO> getDaAssetApiList() {
        return daAssetApiMapper.selectList();
    }

    @Override
    public Map<Long, DaAssetApiDO> getDaAssetApiMap() {
        List<DaAssetApiDO> daAssetApiList = daAssetApiMapper.selectList();
        return daAssetApiList.stream()
                .collect(Collectors.toMap(
                        DaAssetApiDO::getId,
                        daAssetApiDO -> daAssetApiDO,
                        // 保留已存在的值
                        (existing, replacement) -> existing
                ));
    }


    /**
     * 导入数据资产-外部API数据
     *
     * @param importExcelList 数据资产-外部API数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName 操作用户
     * @return 结果
     */
    @Override
    public String importDaAssetApi(List<DaAssetApiRespVO> importExcelList, boolean isUpdateSupport, String operName) {
        if (StringUtils.isNull(importExcelList) || importExcelList.size() == 0) {
            throw new ServiceException("da.error.import.empty", "导入数据不能为空！");
        }

        int successNum = 0;
        int failureNum = 0;
        List<String> successMessages = new ArrayList<>();
        List<String> failureMessages = new ArrayList<>();

        for (DaAssetApiRespVO respVO : importExcelList) {
            try {
                DaAssetApiDO daAssetApiDO = BeanUtils.toBean(respVO, DaAssetApiDO.class);
                Long daAssetApiId = respVO.getId();
                if (isUpdateSupport) {
                    if (daAssetApiId != null) {
                        DaAssetApiDO existingDaAssetApi = daAssetApiMapper.selectById(daAssetApiId);
                        if (existingDaAssetApi != null) {
                            daAssetApiMapper.updateById(daAssetApiDO);
                            successNum++;
                            successMessages.add(MessageUtils.messageWithFallback("da.import.update.success",
                                    "数据更新成功，ID为 " + daAssetApiId + " 的数据资产-外部API记录。", daAssetApiId, "数据资产-外部API"));
                        } else {
                            failureNum++;
                            failureMessages.add(MessageUtils.messageWithFallback("da.import.update.fail",
                                    "数据更新失败，ID为 " + daAssetApiId + " 的数据资产-外部API记录不存在。", daAssetApiId, "数据资产-外部API"));
                        }
                    } else {
                        failureNum++;
                        failureMessages.add(MessageUtils.messageWithFallback("da.import.update.id.missing",
                                "数据更新失败，某条记录的ID不存在。"));
                    }
                } else {
                    QueryWrapper<DaAssetApiDO> queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq("id", daAssetApiId);
                    DaAssetApiDO existingDaAssetApi = daAssetApiMapper.selectOne(queryWrapper);
                    if (existingDaAssetApi == null) {
                        daAssetApiMapper.insert(daAssetApiDO);
                        successNum++;
                        successMessages.add(MessageUtils.messageWithFallback("da.import.insert.success",
                                "数据插入成功，ID为 " + daAssetApiId + " 的数据资产-外部API记录。", daAssetApiId, "数据资产-外部API"));
                    } else {
                        failureNum++;
                        failureMessages.add(MessageUtils.messageWithFallback("da.import.insert.fail",
                                "数据插入失败，ID为 " + daAssetApiId + " 的数据资产-外部API记录已存在。", daAssetApiId, "数据资产-外部API"));
                    }
                }
            } catch (Exception e) {
                failureNum++;
                String errorMsg = MessageUtils.messageWithFallback("da.import.error.detail",
                "数据导入失败，错误信息：" + e.getMessage(), e.getMessage());
                failureMessages.add(errorMsg);
                log.error(errorMsg, e);
            }
        }
        StringBuilder resultMsg = new StringBuilder();
        if (failureNum > 0) {
            String failureDetails = String.join("<br/>", failureMessages);
            resultMsg.append(MessageUtils.messageWithFallback("da.import.result.fail",
                    "很抱歉，导入失败！共 " + failureNum + " 条数据格式不正确，错误如下：<br/>" + failureDetails,
                    failureNum, failureDetails));
            throw new ServiceException("da.error.import.fail", resultMsg.toString(), resultMsg.toString());
        } else {
            resultMsg.append(MessageUtils.messageWithFallback("da.import.result.success",
                    "恭喜您，数据已全部导入成功！共 " + successNum + " 条。", successNum));
        }
        return resultMsg.toString();
    }

    @Override
    public void queryServiceForwarding(HttpServletResponse response, DaAssetApiReqVO daAssetApi) {
        this.executeServiceForwarding(response,daAssetApi.getId(),daAssetApi.getQueryParams());
    }

    @Override
    public void executeServiceForwarding(HttpServletResponse response, Long apiId, Map<String, Object> queryParams) {
        //很具id 获取三方api配置
        DaAssetApiDO daAssetApiById = this.getDaAssetApiById(apiId);

        //判断api信息，例如是否启用等
        chackYapiConfig(daAssetApiById);

        //取出Url
        String url = daAssetApiById.getUrl();

        //封装header
        List<HeaderEntity> headerEntities = packHeadersOrYApiField(queryParams);
        //进行三方api的调取
        try {
            //取出调取方式
            String reqMethod = daAssetApiById.getHttpMethod();
            //取出入参数
            Map<String, Object> params = ( Map<String, Object>)MapUtils.getMap(queryParams, "params", new HashMap<>());
            //get
            if (StringUtils.equals(HttpUtils.GET, reqMethod)) {//封装get请求
                HttpUtils.sendGet(HttpUtils.packGetRequestURL(url, params), response, headerEntities);
            } else if (StringUtils.equals(HttpUtils.POST, reqMethod)) {//post
                HttpUtils.sendPost(url, params, response, headerEntities);
            } else {//未知
                throw new DataQueryException("db.error.api.type", "API类型错误");
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new DataQueryException("db.error.api.http", "Http调取失败");
        }
    }

    private void chackYapiConfig(DaAssetApiDO daAssetApiById) {
        //判断是否为null
        if (daAssetApiById == null) {
            throw new DataQueryException("db.error.api.config.missing", "API调用，未查询到api配置");
        }
    }


    /**
     * 封装Header
     *
     * @param queryParams
     * @return
     */
    public static List<HeaderEntity> packHeadersOrYApiField(Map<String, Object> queryParams) {
        List<Map<String,Object>> fieldHerderList = (List<Map<String,Object>>)MapUtils.getObject(queryParams, "fieldHerderList", new ArrayList<>());

        //封装 headers
        List<HeaderEntity> headerEntityList = new ArrayList<>();
        if(CollectionUtils.isEmpty(fieldHerderList)){
            return headerEntityList;
        }

        for (Map<String, Object> stringObjectMap : fieldHerderList) {
            if(MapUtils.isNotEmpty(stringObjectMap)){
                HeaderEntity headerEntity = new HeaderEntity();
                headerEntity.setKey(MapUtils.getString(stringObjectMap,"name"));
                String defaultValue = MapUtils.getString(stringObjectMap, "defaultValue");
                if(defaultValue == null){
                    throw new DataQueryException("db.error.api.header.null", "Header中不能为null");
                }
                headerEntity.setValue(defaultValue);
                headerEntityList.add(headerEntity);
            }
        }
        return headerEntityList;
    }

}
