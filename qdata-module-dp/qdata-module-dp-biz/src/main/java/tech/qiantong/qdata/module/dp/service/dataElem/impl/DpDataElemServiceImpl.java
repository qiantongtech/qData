package tech.qiantong.qdata.module.dp.service.dataElem.impl;

import java.util.*;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;

import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.common.exception.ServiceException;
import tech.qiantong.qdata.common.utils.StringUtils;
import tech.qiantong.qdata.common.utils.object.BeanUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.qiantong.qdata.module.dp.api.service.dataElem.IDataElemApiService;
import tech.qiantong.qdata.module.dp.controller.admin.dataElem.vo.DpDataElemPageReqVO;
import tech.qiantong.qdata.module.dp.controller.admin.dataElem.vo.DpDataElemRespVO;
import tech.qiantong.qdata.module.dp.controller.admin.dataElem.vo.DpDataElemSaveReqVO;
import tech.qiantong.qdata.module.dp.dal.dataobject.dataElem.DpDataElemDO;
import tech.qiantong.qdata.module.dp.dal.dataobject.document.DpDocumentDO;
import tech.qiantong.qdata.module.dp.dal.dataobject.model.DpModelDO;
import tech.qiantong.qdata.module.dp.dal.mapper.dataElem.DpDataElemMapper;
import tech.qiantong.qdata.module.dp.service.dataElem.IDpDataElemService;
import tech.qiantong.qdata.module.dp.service.document.IDpDocumentService;
import tech.qiantong.qdata.mybatis.core.query.LambdaQueryWrapperX;

/**
 * 数据元Service业务层处理
 *
 * @author qdata
 * @date 2025-01-21
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class DpDataElemServiceImpl extends ServiceImpl<DpDataElemMapper, DpDataElemDO> implements IDpDataElemService, IDataElemApiService {
    @Resource
    private DpDataElemMapper dpDataElemMapper;

    @Resource
    private IDpDocumentService dpDocumentService;

    @Override
    public PageResult<DpDataElemDO> getDpDataElemPage(DpDataElemPageReqVO pageReqVO) {
        return dpDataElemMapper.selectPage(pageReqVO);
    }

    @Override
    public List<DpDataElemDO> getDpDataElemList(DpDataElemPageReqVO reqVO) {
        LambdaQueryWrapperX<DpDataElemDO> queryWrapper = new LambdaQueryWrapperX<>();
        Set<String> allowedColumns = new HashSet<>(Arrays.asList("id", "create_time", "update_time"));

        queryWrapper.likeIfPresent(DpDataElemDO::getName, reqVO.getName())
                .likeIfPresent(DpDataElemDO::getEngName, reqVO.getEngName())
                .eqIfPresent(DpDataElemDO::getCatCode, reqVO.getCatCode())
                .eqIfPresent(DpDataElemDO::getType, reqVO.getType())
                .eqIfPresent(DpDataElemDO::getPersonCharge, reqVO.getPersonCharge())
                .eqIfPresent(DpDataElemDO::getContactNumber, reqVO.getContactNumber())
                .eqIfPresent(DpDataElemDO::getColumnType, reqVO.getColumnType())
                .eqIfPresent(DpDataElemDO::getStatus, reqVO.getStatus())
                .eqIfPresent(DpDataElemDO::getDocumentId, reqVO.getDocumentId())
                .eqIfPresent(DpDataElemDO::getDescription, reqVO.getDescription())
                .eqIfPresent(DpDataElemDO::getCreateTime, reqVO.getCreateTime())
                // 如果 reqVO.getName() 不为空，则添加 name 的精确匹配条件（name = '<name>'）
                // .likeIfPresent(DpDataElemDO::getName, reqVO.getName())
                // 按照 createTime 字段降序排序
                .orderBy(reqVO.getOrderByColumn(), reqVO.getIsAsc(), allowedColumns);

        return dpDataElemMapper.selectList(queryWrapper);
    }

    @Override
    public Long createDpDataElem(DpDataElemSaveReqVO createReqVO) {
        DpDataElemDO dictType = BeanUtils.toBean(createReqVO, DpDataElemDO.class);
        dpDataElemMapper.insert(dictType);
        return dictType.getId();
    }

    @Override
    public int updateDpDataElem(DpDataElemSaveReqVO updateReqVO) {
        // 相关校验

        // 更新数据元
        DpDataElemDO updateObj = BeanUtils.toBean(updateReqVO, DpDataElemDO.class);
        return dpDataElemMapper.updateById(updateObj);
    }

    @Override
    public int removeDpDataElem(Collection<Long> idList) {
        // 批量删除数据元
        return dpDataElemMapper.deleteBatchIds(idList);
    }

    @Override
    public DpDataElemDO getDpDataElemById(Long id) {
        MPJLambdaWrapper<DpDataElemDO> lambdaWrapper = new MPJLambdaWrapper();
        lambdaWrapper.selectAll(DpDataElemDO.class)
                .select("t2.NAME AS catName","t3.NICK_NAME AS personChargeName")
                .leftJoin("ATT_DATA_ELEM_CAT t2 on t.CAT_CODE = t2.CODE AND t2.DEL_FLAG = '0'")
                .leftJoin("SYSTEM_USER t3 on t.PERSON_CHARGE = t3.USER_ID AND t3.DEL_FLAG = '0'")
                .eq(DpDataElemDO::getId, id);
        DpDataElemDO dpDataElemDO = dpDataElemMapper.selectJoinOne(DpDataElemDO.class, lambdaWrapper);

        if(dpDataElemDO.getDocumentId() != null){
            DpDocumentDO dpDocumentById = dpDocumentService.getDpDocumentById(dpDataElemDO.getDocumentId());
            dpDocumentById = dpDocumentById == null ? new DpDocumentDO():dpDocumentById;

            dpDataElemDO.setDocumentCode(dpDocumentById.getCode());
            dpDataElemDO.setDocumentName(dpDocumentById.getName());
            dpDataElemDO.setDocumentType(dpDocumentById.getType());
        }

        return dpDataElemDO;
    }

    @Override
    public List<DpDataElemDO> getDpDataElemList() {
        return dpDataElemMapper.selectList();
    }

    @Override
    public Map<Long, DpDataElemDO> getDpDataElemMap() {
        List<DpDataElemDO> dpDataElemList = dpDataElemMapper.selectList();
        return dpDataElemList.stream()
                .collect(Collectors.toMap(
                        DpDataElemDO::getId,
                        dpDataElemDO -> dpDataElemDO,
                        // 保留已存在的值
                        (existing, replacement) -> existing
                ));
    }


    /**
     * 导入数据元数据
     *
     * @param importExcelList 数据元数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName        操作用户
     * @return 结果
     */
    @Override
    public String importDpDataElem(List<DpDataElemRespVO> importExcelList, boolean isUpdateSupport, String operName) {
        if (StringUtils.isNull(importExcelList) || importExcelList.size() == 0) {
            throw new ServiceException("导入数据不能为空！");
        }

        int successNum = 0;
        int failureNum = 0;
        List<String> successMessages = new ArrayList<>();
        List<String> failureMessages = new ArrayList<>();

        for (DpDataElemRespVO respVO : importExcelList) {
            try {
                DpDataElemDO dpDataElemDO = BeanUtils.toBean(respVO, DpDataElemDO.class);
                Long dpDataElemId = respVO.getId();
                if (isUpdateSupport) {
                    if (dpDataElemId != null) {
                        DpDataElemDO existingDpDataElem = dpDataElemMapper.selectById(dpDataElemId);
                        if (existingDpDataElem != null) {
                            dpDataElemMapper.updateById(dpDataElemDO);
                            successNum++;
                            successMessages.add("数据更新成功，ID为 " + dpDataElemId + " 的数据元记录。");
                        } else {
                            failureNum++;
                            failureMessages.add("数据更新失败，ID为 " + dpDataElemId + " 的数据元记录不存在。");
                        }
                    } else {
                        failureNum++;
                        failureMessages.add("数据更新失败，某条记录的ID不存在。");
                    }
                } else {
                    QueryWrapper<DpDataElemDO> queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq("id", dpDataElemId);
                    DpDataElemDO existingDpDataElem = dpDataElemMapper.selectOne(queryWrapper);
                    if (existingDpDataElem == null) {
                        dpDataElemMapper.insert(dpDataElemDO);
                        successNum++;
                        successMessages.add("数据插入成功，ID为 " + dpDataElemId + " 的数据元记录。");
                    } else {
                        failureNum++;
                        failureMessages.add("数据插入失败，ID为 " + dpDataElemId + " 的数据元记录已存在。");
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

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean updateStatus(Long id, Long status) {
        return this.update(Wrappers.lambdaUpdate(DpDataElemDO.class)
                .eq(DpDataElemDO::getId, id)
                .set(DpDataElemDO::getStatus, status));
    }

    @Override
    public Long getCountByCatCode(String catCode) {
        return baseMapper.selectCount(Wrappers.lambdaQuery(DpDataElemDO.class)
                .likeRight(DpDataElemDO::getCatCode, catCode));
    }
}
