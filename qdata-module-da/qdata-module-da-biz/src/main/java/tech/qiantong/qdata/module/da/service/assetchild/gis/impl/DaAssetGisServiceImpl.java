package tech.qiantong.qdata.module.da.service.assetchild.gis.impl;

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
import tech.qiantong.qdata.common.httpClient.HeaderEntity;
import tech.qiantong.qdata.common.httpClient.HttpUtils;
import tech.qiantong.qdata.common.utils.StringUtils;
import tech.qiantong.qdata.common.utils.object.BeanUtils;
import tech.qiantong.qdata.module.da.api.service.assetchild.gis.IDaAssetGisOutService;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.gis.vo.DaAssetGisPageReqVO;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.gis.vo.DaAssetGisReqVO;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.gis.vo.DaAssetGisRespVO;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.gis.vo.DaAssetGisSaveReqVO;
import tech.qiantong.qdata.module.da.dal.dataobject.assetchild.gis.DaAssetGisDO;
import tech.qiantong.qdata.module.da.dal.mapper.assetchild.gis.DaAssetGisMapper;
import tech.qiantong.qdata.module.da.service.assetchild.gis.IDaAssetGisService;
import tech.qiantong.qdata.mybatis.core.query.LambdaQueryWrapperX;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 数据资产-地理空间服务Service业务层处理
 *
 * @author qdata
 * @date 2025-04-14
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class DaAssetGisServiceImpl  extends ServiceImpl<DaAssetGisMapper,DaAssetGisDO> implements IDaAssetGisService, IDaAssetGisOutService {
    @Resource
    private DaAssetGisMapper daAssetGisMapper;

    @Override
    public PageResult<DaAssetGisDO> getDaAssetGisPage(DaAssetGisPageReqVO pageReqVO) {
        return daAssetGisMapper.selectPage(pageReqVO);
    }

    @Override
    public DaAssetGisRespVO getDaAssetGisByAssetId(Long assetId) {
        LambdaQueryWrapperX<DaAssetGisDO> queryWrapperX = new LambdaQueryWrapperX<>();
        queryWrapperX.eqIfPresent(DaAssetGisDO::getAssetId,assetId);
        DaAssetGisDO daAssetApiDO = daAssetGisMapper.selectOne(queryWrapperX);
        return BeanUtils.toBean(daAssetApiDO, DaAssetGisRespVO.class);
    }

    @Override
    public Long createDaAssetGis(DaAssetGisSaveReqVO createReqVO) {
        DaAssetGisDO dictType = BeanUtils.toBean(createReqVO, DaAssetGisDO.class);
        daAssetGisMapper.insert(dictType);
        return dictType.getId();
    }

    @Override
    public int updateDaAssetGis(DaAssetGisSaveReqVO updateReqVO) {
        // 相关校验

        // 更新数据资产-地理空间服务
        DaAssetGisDO updateObj = BeanUtils.toBean(updateReqVO, DaAssetGisDO.class);
        return daAssetGisMapper.updateById(updateObj);
    }
    @Override
    public int removeDaAssetGis(Collection<Long> idList) {
        // 批量删除数据资产-地理空间服务
        return daAssetGisMapper.deleteBatchIds(idList);
    }

    @Override
    public DaAssetGisDO getDaAssetGisById(Long id) {
        return daAssetGisMapper.selectById(id);
    }

    @Override
    public List<DaAssetGisDO> getDaAssetGisList() {
        return daAssetGisMapper.selectList();
    }

    @Override
    public Map<Long, DaAssetGisDO> getDaAssetGisMap() {
        List<DaAssetGisDO> daAssetGisList = daAssetGisMapper.selectList();
        return daAssetGisList.stream()
                .collect(Collectors.toMap(
                        DaAssetGisDO::getId,
                        daAssetGisDO -> daAssetGisDO,
                        // 保留已存在的值
                        (existing, replacement) -> existing
                ));
    }


        /**
         * 导入数据资产-地理空间服务数据
         *
         * @param importExcelList 数据资产-地理空间服务数据列表
         * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
         * @param operName 操作用户
         * @return 结果
         */
        @Override
        public String importDaAssetGis(List<DaAssetGisRespVO> importExcelList, boolean isUpdateSupport, String operName) {
            if (StringUtils.isNull(importExcelList) || importExcelList.size() == 0) {
                throw new ServiceException("导入数据不能为空！");
            }

            int successNum = 0;
            int failureNum = 0;
            List<String> successMessages = new ArrayList<>();
            List<String> failureMessages = new ArrayList<>();

            for (DaAssetGisRespVO respVO : importExcelList) {
                try {
                    DaAssetGisDO daAssetGisDO = BeanUtils.toBean(respVO, DaAssetGisDO.class);
                    Long daAssetGisId = respVO.getId();
                    if (isUpdateSupport) {
                        if (daAssetGisId != null) {
                            DaAssetGisDO existingDaAssetGis = daAssetGisMapper.selectById(daAssetGisId);
                            if (existingDaAssetGis != null) {
                                daAssetGisMapper.updateById(daAssetGisDO);
                                successNum++;
                                successMessages.add("数据更新成功，ID为 " + daAssetGisId + " 的数据资产-地理空间服务记录。");
                            } else {
                                failureNum++;
                                failureMessages.add("数据更新失败，ID为 " + daAssetGisId + " 的数据资产-地理空间服务记录不存在。");
                            }
                        } else {
                            failureNum++;
                            failureMessages.add("数据更新失败，某条记录的ID不存在。");
                        }
                    } else {
                        QueryWrapper<DaAssetGisDO> queryWrapper = new QueryWrapper<>();
                        queryWrapper.eq("id", daAssetGisId);
                        DaAssetGisDO existingDaAssetGis = daAssetGisMapper.selectOne(queryWrapper);
                        if (existingDaAssetGis == null) {
                            daAssetGisMapper.insert(daAssetGisDO);
                            successNum++;
                            successMessages.add("数据插入成功，ID为 " + daAssetGisId + " 的数据资产-地理空间服务记录。");
                        } else {
                            failureNum++;
                            failureMessages.add("数据插入失败，ID为 " + daAssetGisId + " 的数据资产-地理空间服务记录已存在。");
                        }
                    }
                } catch (Exception e) {
                    failureNum++;
                    String errorMsg = "数据导入失败，错误信息：" + e.getMessage();
                    failureMessages.add(errorMsg);
                    log.error(errorMsg, e);
                }
            }
            StringBuilder resultMsg = new StringBuilder();
            if (failureNum > 0) {
                resultMsg.append("很抱歉，导入失败！共 ").append(failureNum).append(" 条数据格式不正确，错误如下：");
                resultMsg.append("<br/>").append(String.join("<br/>", failureMessages));
                throw new ServiceException(resultMsg.toString());
            } else {
                resultMsg.append("恭喜您，数据已全部导入成功！共 ").append(successNum).append(" 条。");
            }
            return resultMsg.toString();
        }

    @Override
    public void queryServiceForwarding(HttpServletResponse response, DaAssetGisReqVO daAssetGisReqVO) {
        this.executeServiceForwarding(response,daAssetGisReqVO.getId(),daAssetGisReqVO.getQueryParams());
    }

    @Override
    public void executeServiceForwarding(HttpServletResponse response, Long gisId, Map<String, Object> queryParams) {
        //很具id 获取三方api配置
        DaAssetGisDO daAssetGisDO = this.getDaAssetGisById(gisId);

        //判断api信息，例如是否启用等
        chackYapiConfig(daAssetGisDO);

        //取出Url
        String url = daAssetGisDO.getUrl();

        //封装header
        List<HeaderEntity> headerEntities = packHeadersOrYApiField(queryParams);
        //进行三方api的调取
        try {
            //取出调取方式
            String reqMethod = daAssetGisDO.getHttpMethod();
            //取出入参数
            Map<String, Object> params = ( Map<String, Object>) MapUtils.getMap(queryParams, "params", new HashMap<>());
            this.fillDefaultWmtsParams(params,reqMethod);
            //get
            if (StringUtils.equals(HttpUtils.GET, reqMethod)) {//封装get请求
                HttpUtils.sendGet(HttpUtils.packGetRequestURL(url, params), response, headerEntities);
            } else if (StringUtils.equals(HttpUtils.POST, reqMethod)) {//post
                HttpUtils.sendPost(url, params, response, headerEntities);
            } else {//未知
                throw new DataQueryException("API类型错误");
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new DataQueryException("Http调取失败");
        }
    }

    private void chackYapiConfig(DaAssetGisDO daAssetGisDO) {
        //判断是否为null
        if (daAssetGisDO == null) {
            throw new DataQueryException("API调用，未查询到api配置");
        }
    }

    /**
     * 显式判断缺失的 WMTS 参数并补全
     * @param params 入参 Map，会被修改
     */
    private void fillDefaultWmtsParams(Map<String, Object> params,String reqMethod ) {
        String service = MapUtils.getString(params, "service");
        if (StringUtils.isBlank(service)) {
            params.put("service", "WMTS");
        }

        String request = MapUtils.getString(params, "request");
        if (StringUtils.isBlank(request)) {
            params.put("request", "GetCapabilities");
        }

        String version = MapUtils.getString(params, "version");
        if (StringUtils.isBlank(version)) {
            params.put("version", "1.0.0");
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
                    throw new DataQueryException("Header中不能为null");
                }
                headerEntity.setValue(defaultValue);
                headerEntityList.add(headerEntity);
            }
        }
        return headerEntityList;
    }
}
