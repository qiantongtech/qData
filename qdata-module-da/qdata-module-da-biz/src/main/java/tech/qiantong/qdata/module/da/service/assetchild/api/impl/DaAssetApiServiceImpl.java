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
            throw new ServiceException("导入数据不能为空！");
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
                            successMessages.add("数据更新成功，ID为 " + daAssetApiId + " 的数据资产-外部API记录。");
                        } else {
                            failureNum++;
                            failureMessages.add("数据更新失败，ID为 " + daAssetApiId + " 的数据资产-外部API记录不存在。");
                        }
                    } else {
                        failureNum++;
                        failureMessages.add("数据更新失败，某条记录的ID不存在。");
                    }
                } else {
                    QueryWrapper<DaAssetApiDO> queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq("id", daAssetApiId);
                    DaAssetApiDO existingDaAssetApi = daAssetApiMapper.selectOne(queryWrapper);
                    if (existingDaAssetApi == null) {
                        daAssetApiMapper.insert(daAssetApiDO);
                        successNum++;
                        successMessages.add("数据插入成功，ID为 " + daAssetApiId + " 的数据资产-外部API记录。");
                    } else {
                        failureNum++;
                        failureMessages.add("数据插入失败，ID为 " + daAssetApiId + " 的数据资产-外部API记录已存在。");
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
                throw new DataQueryException("API类型错误");
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new DataQueryException("Http调取失败");
        }
    }

    private void chackYapiConfig(DaAssetApiDO daAssetApiById) {
        //判断是否为null
        if (daAssetApiById == null) {
            throw new DataQueryException("API调用，未查询到api配置");
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
