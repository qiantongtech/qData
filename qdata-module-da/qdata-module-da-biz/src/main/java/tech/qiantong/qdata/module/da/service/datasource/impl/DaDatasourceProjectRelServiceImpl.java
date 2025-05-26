package tech.qiantong.qdata.module.da.service.datasource.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.common.exception.ServiceException;
import tech.qiantong.qdata.common.utils.StringUtils;
import tech.qiantong.qdata.common.utils.object.BeanUtils;
import tech.qiantong.qdata.module.da.controller.admin.datasource.vo.DaDatasourceProjectRelPageReqVO;
import tech.qiantong.qdata.module.da.controller.admin.datasource.vo.DaDatasourceProjectRelRespVO;
import tech.qiantong.qdata.module.da.controller.admin.datasource.vo.DaDatasourceProjectRelSaveReqVO;
import tech.qiantong.qdata.module.da.dal.dataobject.datasource.DaDatasourceProjectRelDO;
import tech.qiantong.qdata.module.da.dal.mapper.datasource.DaDatasourceProjectRelMapper;
import tech.qiantong.qdata.module.da.service.datasource.IDaDatasourceProjectRelService;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 数据源与项目关联关系Service业务层处理
 *
 * @author qdata
 * @date 2025-03-13
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class DaDatasourceProjectRelServiceImpl extends ServiceImpl<DaDatasourceProjectRelMapper, DaDatasourceProjectRelDO> implements IDaDatasourceProjectRelService {
    @Resource
    private DaDatasourceProjectRelMapper daDatasourceProjectRelMapper;

    @Override
    public PageResult<DaDatasourceProjectRelDO> getDaDatasourceProjectRelPage(DaDatasourceProjectRelPageReqVO pageReqVO) {
        return daDatasourceProjectRelMapper.selectPage(pageReqVO);
    }

    @Override
    public Long createDaDatasourceProjectRel(DaDatasourceProjectRelSaveReqVO createReqVO) {
        DaDatasourceProjectRelDO dictType = BeanUtils.toBean(createReqVO, DaDatasourceProjectRelDO.class);
        daDatasourceProjectRelMapper.insert(dictType);
        return dictType.getId();
    }

    @Override
    public int updateDaDatasourceProjectRel(DaDatasourceProjectRelSaveReqVO updateReqVO) {
        // 相关校验

        // 更新数据源与项目关联关系
        DaDatasourceProjectRelDO updateObj = BeanUtils.toBean(updateReqVO, DaDatasourceProjectRelDO.class);
        return daDatasourceProjectRelMapper.updateById(updateObj);
    }

    @Override
    public int removeDaDatasourceProjectRel(Collection<Long> idList) {
        // 批量删除数据源与项目关联关系
        return daDatasourceProjectRelMapper.deleteBatchIds(idList);
    }

    @Override
    public DaDatasourceProjectRelDO getDaDatasourceProjectRelById(Long id) {
        return daDatasourceProjectRelMapper.selectById(id);
    }

    @Override
    public List<DaDatasourceProjectRelDO> getDaDatasourceProjectRelList() {
        return daDatasourceProjectRelMapper.selectList();
    }

    @Override
    public List<DaDatasourceProjectRelDO> getDaDatasourceProjectRelList(DaDatasourceProjectRelDO daDatasourceProjectRelDO) {
        LambdaQueryWrapper<DaDatasourceProjectRelDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(daDatasourceProjectRelDO.getDatasourceId() != null, DaDatasourceProjectRelDO::getDatasourceId, daDatasourceProjectRelDO.getDatasourceId());
        queryWrapper.eq(daDatasourceProjectRelDO.getProjectId() != null, DaDatasourceProjectRelDO::getProjectId, daDatasourceProjectRelDO.getProjectId());
        queryWrapper.eq(StringUtils.isNotEmpty(daDatasourceProjectRelDO.getProjectCode()), DaDatasourceProjectRelDO::getProjectCode, daDatasourceProjectRelDO.getProjectCode());
        return daDatasourceProjectRelMapper.selectList(queryWrapper);
    }

    @Override
    public List<DaDatasourceProjectRelDO> getJoinProjectAndDatasource(DaDatasourceProjectRelDO daDatasourceProjectRelDO) {
        MPJLambdaWrapper<DaDatasourceProjectRelDO> lambdaWrapper = new MPJLambdaWrapper();
        lambdaWrapper.selectAll(DaDatasourceProjectRelDO.class)
                .select("u.datasource_name as datasourceName,d.name as projectName")
                .leftJoin("DA_DATASOURCE u on t.DATASOURCE_ID = u.id")
                .leftJoin("ATT_PROJECT d on t.PROJECT_ID = d.id")
                .eq("u.del_flag", "0")
                .eq("d.del_flag", "0")
                .eq(daDatasourceProjectRelDO.getDatasourceId() != null, DaDatasourceProjectRelDO::getDatasourceId, daDatasourceProjectRelDO.getDatasourceId())
                .eq(daDatasourceProjectRelDO.getProjectId() != null, DaDatasourceProjectRelDO::getProjectId, daDatasourceProjectRelDO.getProjectId())
                .eq(StringUtils.isNotEmpty(daDatasourceProjectRelDO.getProjectCode()), DaDatasourceProjectRelDO::getProjectCode, daDatasourceProjectRelDO.getProjectCode());
        return daDatasourceProjectRelMapper.selectList(lambdaWrapper);
    }

    @Override
    public Map<Long, DaDatasourceProjectRelDO> getDaDatasourceProjectRelMap() {
        List<DaDatasourceProjectRelDO> daDatasourceProjectRelList = daDatasourceProjectRelMapper.selectList();
        return daDatasourceProjectRelList.stream()
                .collect(Collectors.toMap(
                        DaDatasourceProjectRelDO::getId,
                        daDatasourceProjectRelDO -> daDatasourceProjectRelDO,
                        // 保留已存在的值
                        (existing, replacement) -> existing
                ));
    }


    /**
     * 导入数据源与项目关联关系数据
     *
     * @param importExcelList 数据源与项目关联关系数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName        操作用户
     * @return 结果
     */
    @Override
    public String importDaDatasourceProjectRel(List<DaDatasourceProjectRelRespVO> importExcelList, boolean isUpdateSupport, String operName) {
        if (StringUtils.isNull(importExcelList) || importExcelList.size() == 0) {
            throw new ServiceException("导入数据不能为空！");
        }

        int successNum = 0;
        int failureNum = 0;
        List<String> successMessages = new ArrayList<>();
        List<String> failureMessages = new ArrayList<>();

        for (DaDatasourceProjectRelRespVO respVO : importExcelList) {
            try {
                DaDatasourceProjectRelDO daDatasourceProjectRelDO = BeanUtils.toBean(respVO, DaDatasourceProjectRelDO.class);
                Long daDatasourceProjectRelId = respVO.getId();
                if (isUpdateSupport) {
                    if (daDatasourceProjectRelId != null) {
                        DaDatasourceProjectRelDO existingDaDatasourceProjectRel = daDatasourceProjectRelMapper.selectById(daDatasourceProjectRelId);
                        if (existingDaDatasourceProjectRel != null) {
                            daDatasourceProjectRelMapper.updateById(daDatasourceProjectRelDO);
                            successNum++;
                            successMessages.add("数据更新成功，ID为 " + daDatasourceProjectRelId + " 的数据源与项目关联关系记录。");
                        } else {
                            failureNum++;
                            failureMessages.add("数据更新失败，ID为 " + daDatasourceProjectRelId + " 的数据源与项目关联关系记录不存在。");
                        }
                    } else {
                        failureNum++;
                        failureMessages.add("数据更新失败，某条记录的ID不存在。");
                    }
                } else {
                    QueryWrapper<DaDatasourceProjectRelDO> queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq("id", daDatasourceProjectRelId);
                    DaDatasourceProjectRelDO existingDaDatasourceProjectRel = daDatasourceProjectRelMapper.selectOne(queryWrapper);
                    if (existingDaDatasourceProjectRel == null) {
                        daDatasourceProjectRelMapper.insert(daDatasourceProjectRelDO);
                        successNum++;
                        successMessages.add("数据插入成功，ID为 " + daDatasourceProjectRelId + " 的数据源与项目关联关系记录。");
                    } else {
                        failureNum++;
                        failureMessages.add("数据插入失败，ID为 " + daDatasourceProjectRelId + " 的数据源与项目关联关系记录已存在。");
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
}
