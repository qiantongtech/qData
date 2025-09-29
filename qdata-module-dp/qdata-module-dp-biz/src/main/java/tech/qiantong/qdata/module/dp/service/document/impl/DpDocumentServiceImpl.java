package tech.qiantong.qdata.module.dp.service.document.impl;

import com.aliyun.oss.ServiceException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.common.utils.StringUtils;
import tech.qiantong.qdata.common.utils.object.BeanUtils;
import tech.qiantong.qdata.module.dp.api.service.document.IDpDocumentApiService;
import tech.qiantong.qdata.module.dp.controller.admin.document.vo.*;
import tech.qiantong.qdata.module.dp.dal.dataobject.document.DpDocumentDO;
import tech.qiantong.qdata.module.dp.dal.mapper.document.DpDocumentMapper;
import tech.qiantong.qdata.module.dp.service.document.IDpDocumentService;
import tech.qiantong.qdata.mybatis.core.util.MyBatisUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 标准信息登记Service业务层处理
 *
 * @author qdata
 * @date 2025-08-21
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class DpDocumentServiceImpl  extends ServiceImpl<DpDocumentMapper,DpDocumentDO> implements IDpDocumentService, IDpDocumentApiService {
    @Resource
    private DpDocumentMapper dpDocumentMapper;

    @Override
    public PageResult<DpDocumentDO> getDpDocumentPage(DpDocumentPageReqVO pageReqVO) {
        return dpDocumentMapper.selectPage(pageReqVO);
    }

    @Override
    public List<DpDocumentDO> getDpDocumentList(DpDocumentPageReqVO reqVO) {
        MPJLambdaWrapper<DpDocumentDO> lambdaWrapper = new MPJLambdaWrapper();
        lambdaWrapper.selectAll(DpDocumentDO.class)
                .select("t2.NAME AS catName")
                .leftJoin("ATT_DOCUMENT_CAT t2 on t.CAT_CODE = t2.CODE AND t2.DEL_FLAG = '0'")
                .like(org.apache.commons.lang3.StringUtils.isNotBlank(reqVO.getName()), DpDocumentDO::getName, reqVO.getName())
                .like(org.apache.commons.lang3.StringUtils.isNotBlank(reqVO.getCode()), DpDocumentDO::getCode, reqVO.getCode())
                .and(org.apache.commons.lang3.StringUtils.isNotBlank(reqVO.getKeyWordParam()),
                        q -> q.like(DpDocumentDO::getCode, reqVO.getKeyWordParam())
                                .or()
                                .like(DpDocumentDO::getName, reqVO.getKeyWordParam()))
                .like(org.apache.commons.lang3.StringUtils.isNotBlank(reqVO.getKeyWordParam()), DpDocumentDO::getCode, reqVO.getKeyWordParam())
                .like(org.apache.commons.lang3.StringUtils.isNotBlank(reqVO.getKeyWordParam()), DpDocumentDO::getName, reqVO.getKeyWordParam())
                .like(org.apache.commons.lang3.StringUtils.isNotBlank(reqVO.getIssuingAgency()), DpDocumentDO::getIssuingAgency, reqVO.getIssuingAgency())
                .likeRight(org.apache.commons.lang3.StringUtils.isNotBlank(reqVO.getCatCode()), DpDocumentDO::getCatCode, reqVO.getCatCode())
                .eq(org.apache.commons.lang3.StringUtils.isNotBlank(reqVO.getType()),DpDocumentDO::getType, reqVO.getType())
                .eq(org.apache.commons.lang3.StringUtils.isNotBlank(reqVO.getStatus()),DpDocumentDO::getStatus, reqVO.getStatus())
                .eq(org.apache.commons.lang3.StringUtils.isNotBlank(reqVO.getVersion()),DpDocumentDO::getVersion, reqVO.getVersion());
        return dpDocumentMapper.selectList(lambdaWrapper);
    }

    @Override
    public Long createDpDocument(DpDocumentSaveReqVO createReqVO) {
        DpDocumentDO dictType = BeanUtils.toBean(createReqVO, DpDocumentDO.class);
        dpDocumentMapper.insert(dictType);
        return dictType.getId();
    }

    @Override
    public int updateDpDocument(DpDocumentSaveReqVO updateReqVO) {
        // 相关校验

        // 更新标准信息登记
        DpDocumentDO updateObj = BeanUtils.toBean(updateReqVO, DpDocumentDO.class);
        return dpDocumentMapper.updateById(updateObj);
    }
    @Override
    public int removeDpDocument(Collection<Long> idList) {
        // 批量删除标准信息登记
        return dpDocumentMapper.deleteBatchIds(idList);
    }

    @Override
    public DpDocumentDO getDpDocumentById(Long id) {
        MPJLambdaWrapper<DpDocumentDO> lambdaWrapper = new MPJLambdaWrapper();
        lambdaWrapper.selectAll(DpDocumentDO.class)
                .select("t2.NAME AS catName")
                .leftJoin("ATT_DOCUMENT_CAT t2 on t.CAT_CODE = t2.CODE AND t2.DEL_FLAG = '0'")
                .eq( DpDocumentDO::getId, id);
        return dpDocumentMapper.selectOne(lambdaWrapper);
    }

    @Override
    public List<DpDocumentDO> getDpDocumentList() {
        return dpDocumentMapper.selectList();
    }

    @Override
    public Map<Long, DpDocumentDO> getDpDocumentMap() {
        List<DpDocumentDO> dpDocumentList = dpDocumentMapper.selectList();
        return dpDocumentList.stream()
                .collect(Collectors.toMap(
                        DpDocumentDO::getId,
                        dpDocumentDO -> dpDocumentDO,
                        // 保留已存在的值
                        (existing, replacement) -> existing
                ));
    }


        /**
         * 导入标准信息登记数据
         *
         * @param importExcelList 标准信息登记数据列表
         * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
         * @param operName 操作用户
         * @return 结果
         */
        @Override
        public String importDpDocument(List<DpDocumentRespVO> importExcelList, boolean isUpdateSupport, String operName) {
            if (StringUtils.isNull(importExcelList) || importExcelList.size() == 0) {
                throw new ServiceException("导入数据不能为空！");
            }

            int successNum = 0;
            int failureNum = 0;
            List<String> successMessages = new ArrayList<>();
            List<String> failureMessages = new ArrayList<>();

            for (DpDocumentRespVO respVO : importExcelList) {
                try {
                    DpDocumentDO dpDocumentDO = BeanUtils.toBean(respVO, DpDocumentDO.class);
                    Long dpDocumentId = respVO.getId();
                    if (isUpdateSupport) {
                        if (dpDocumentId != null) {
                            DpDocumentDO existingDpDocument = dpDocumentMapper.selectById(dpDocumentId);
                            if (existingDpDocument != null) {
                                dpDocumentMapper.updateById(dpDocumentDO);
                                successNum++;
                                successMessages.add("数据更新成功，ID为 " + dpDocumentId + " 的标准信息登记记录。");
                            } else {
                                failureNum++;
                                failureMessages.add("数据更新失败，ID为 " + dpDocumentId + " 的标准信息登记记录不存在。");
                            }
                        } else {
                            failureNum++;
                            failureMessages.add("数据更新失败，某条记录的ID不存在。");
                        }
                    } else {
                        QueryWrapper<DpDocumentDO> queryWrapper = new QueryWrapper<>();
                        queryWrapper.eq("id", dpDocumentId);
                        DpDocumentDO existingDpDocument = dpDocumentMapper.selectOne(queryWrapper);
                        if (existingDpDocument == null) {
                            dpDocumentMapper.insert(dpDocumentDO);
                            successNum++;
                            successMessages.add("数据插入成功，ID为 " + dpDocumentId + " 的标准信息登记记录。");
                        } else {
                            failureNum++;
                            failureMessages.add("数据插入失败，ID为 " + dpDocumentId + " 的标准信息登记记录已存在。");
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
    public PageResult<DpDocumentSearchRespVO> getDpDocumentSearchPage(DpDocumentSearchReqVO dpDocument) {
        IPage<DpDocumentSearchRespVO> mpPage = dpDocumentMapper.getDpDocumentSearchPage(MyBatisUtils.buildPage(dpDocument),dpDocument);//BeanUtils.toBean(dppEtlTaskDOPageResult, DppEtlTaskRespVO.class);
        return new PageResult(mpPage.getRecords(), mpPage.getTotal());
    }

    @Override
    public Long getCountByCatCode(String catCode) {
        return baseMapper.selectCount(Wrappers.lambdaQuery(DpDocumentDO.class)
                .likeRight(DpDocumentDO::getCatCode, catCode));
    }

}
