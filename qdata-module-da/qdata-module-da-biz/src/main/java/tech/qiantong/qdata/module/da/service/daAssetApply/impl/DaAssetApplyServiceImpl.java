package tech.qiantong.qdata.module.da.service.daAssetApply.impl;

import cn.hutool.core.date.DateUtil;
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
import tech.qiantong.qdata.module.da.controller.admin.assetchild.theme.vo.DaAssetThemeRelPageReqVO;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.theme.vo.DaAssetThemeRelRespVO;
import tech.qiantong.qdata.module.da.controller.admin.daAssetApply.vo.DaAssetApplyPageReqVO;
import tech.qiantong.qdata.module.da.controller.admin.daAssetApply.vo.DaAssetApplyRespVO;
import tech.qiantong.qdata.module.da.controller.admin.daAssetApply.vo.DaAssetApplySaveReqVO;
import tech.qiantong.qdata.module.da.dal.dataobject.daAssetApply.DaAssetApplyDO;
import tech.qiantong.qdata.module.da.dal.mapper.daAssetApply.DaAssetApplyMapper;
import tech.qiantong.qdata.module.da.service.assetchild.theme.IDaAssetThemeRelService;
import tech.qiantong.qdata.module.da.service.daAssetApply.IDaAssetApplyService;
import tech.qiantong.qdata.module.system.api.message.dto.MessageSaveReqDTO;
import tech.qiantong.qdata.module.system.service.ISysMessageService;
import tech.qiantong.qdata.mybatis.core.query.LambdaQueryWrapperX;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 数据资产申请Service业务层处理
 *
 * @author shu
 * @date 2025-03-19
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class DaAssetApplyServiceImpl extends ServiceImpl<DaAssetApplyMapper, DaAssetApplyDO> implements IDaAssetApplyService {
    @Resource
    private DaAssetApplyMapper daAssetApplyMapper;
    @Resource
    private ISysMessageService iSysMessageService;
    @Resource
    private IDaAssetThemeRelService daAssetThemeRelService;

    @Override
    public PageResult<DaAssetApplyDO> getDaAssetApplyPage(DaAssetApplyPageReqVO pageReqVO) {
        if (StringUtils.isNotEmpty(pageReqVO.getThemeName())) {
            DaAssetThemeRelPageReqVO rel = new DaAssetThemeRelPageReqVO();
            rel.setThemeName(pageReqVO.getThemeName());
            List<DaAssetThemeRelRespVO> daAssetThemeRelList = daAssetThemeRelService.getDaAssetThemeRelList(rel);
            if (daAssetThemeRelList.isEmpty()) {
                return PageResult.empty();
            }
            Set<Long> assetIds = daAssetThemeRelList.stream().map(DaAssetThemeRelRespVO::getAssetId).collect(Collectors.toSet());
            pageReqVO.setAssetIds(assetIds);
        }
        PageResult<DaAssetApplyDO> daAssetApplyDOPageResult = daAssetApplyMapper.selectPage(pageReqVO);

        List<DaAssetApplyDO> daAssetApplyDOList = (List<DaAssetApplyDO>) daAssetApplyDOPageResult.getRows();
        for (DaAssetApplyDO daAssetApplyDO : daAssetApplyDOList) {
            DaAssetThemeRelPageReqVO daAssetThemeRelPageReqVO = new DaAssetThemeRelPageReqVO();
            daAssetThemeRelPageReqVO.setAssetId(daAssetApplyDO.getAssetId());
            List<DaAssetThemeRelRespVO> daAssetThemeRelList = daAssetThemeRelService.getDaAssetThemeRelList(daAssetThemeRelPageReqVO);
            if (!daAssetThemeRelList.isEmpty()) {
                String themeName = daAssetThemeRelList.stream().map(DaAssetThemeRelRespVO::getThemeName).collect(Collectors.joining(","));
                daAssetApplyDO.setThemeName(themeName);
                daAssetApplyDO.setDaAssetThemeRelList(daAssetThemeRelList);
            }
        }
        return daAssetApplyDOPageResult;
    }

    @Override
    public Long createDaAssetApply(DaAssetApplySaveReqVO createReqVO) {
        DaAssetApplyDO dictType = BeanUtils.toBean(createReqVO, DaAssetApplyDO.class);
        LambdaQueryWrapperX<DaAssetApplyDO> queryWrapperX = new LambdaQueryWrapperX<>();
        queryWrapperX.eq(true, DaAssetApplyDO::getAssetId, dictType.getAssetId())
                .eq(true, DaAssetApplyDO::getProjectId, dictType.getProjectId())
                .eq(true, DaAssetApplyDO::getProjectCode, dictType.getProjectCode());
        DaAssetApplyDO daAssetApplyDO = daAssetApplyMapper.selectOne(queryWrapperX);
        if (daAssetApplyDO != null && "2".equals(daAssetApplyDO.getStatus())) {
            daAssetApplyDO.setStatus("1");
            int updateById = daAssetApplyMapper.updateById(daAssetApplyDO);
            return daAssetApplyDO.getId();
        }
        if (daAssetApplyDO != null) {
            throw new ServiceException(createReqVO.getProjectName() + "项目已申请" + createReqVO.getAssetName() + "资产,请等待审批!");
        }
        daAssetApplyMapper.insert(dictType);
        return dictType.getId();
    }

    @Override
    public int updateDaAssetApply(DaAssetApplySaveReqVO updateReqVO) {
        // 相关校验

        // 更新数据资产申请
        DaAssetApplyDO updateObj = BeanUtils.toBean(updateReqVO, DaAssetApplyDO.class);
        int updateById = daAssetApplyMapper.updateById(updateObj);
        if (updateById > 0 && !"1".equals(updateObj.getStatus())) {
            MessageSaveReqDTO messageSaveReqDTO = new MessageSaveReqDTO();
            messageSaveReqDTO.setSenderId(1L);
            messageSaveReqDTO.setCreatorId(1L);
            messageSaveReqDTO.setCreateBy("超级管理员");
            messageSaveReqDTO.setReceiverId(updateObj.getCreatorId());
            Map<String, Object> map = new HashMap<>();
            map.put("assetName", updateObj.getAssetName());
            map.put("time", DateUtil.date());
            map.put("userName", updateObj.getUpdateBy());
            map.put("statusName", "2".equals(updateObj.getStatus()) ? "驳回" : "通过");
            map.put("approvalReason", updateObj.getApprovalReason());
            iSysMessageService.send("2".equals(updateObj.getStatus()) ? 6L : 5L, messageSaveReqDTO, map);
        }
        return updateById;
    }

    @Override
    public int removeDaAssetApply(Collection<Long> idList) {
        // 批量删除数据资产申请
        return daAssetApplyMapper.deleteBatchIds(idList);
    }

    @Override
    public DaAssetApplyDO getDaAssetApplyById(Long id) {
        MPJLambdaWrapper<DaAssetApplyDO> lambdaWrapper = new MPJLambdaWrapper<>();

        lambdaWrapper.selectAll(DaAssetApplyDO.class)
                .select("t2.NAME AS assetName,t2.DESCRIPTION AS description, t2.TABLE_NAME AS assetTableName, t3.NAME AS projectName, " +
                        "t5.DATASOURCE_NAME AS datasourceName, t5.IP AS datasourceIp, t5.DATASOURCE_TYPE AS datasourceType, t6.PHONENUMBER AS phonenumber")
                .leftJoin("DA_ASSET t2 on t.ASSET_ID = t2.ID AND t2.DEL_FLAG = '0'")
                .leftJoin("ATT_PROJECT t3 on t.PROJECT_ID = t3.ID AND t3.DEL_FLAG = '0'")
//                .leftJoin("ATT_THEME t4 on t2.THEME_ID = t4.ID AND t4.DEL_FLAG = '0'")
                .leftJoin("DA_DATASOURCE t5 on t2.DATASOURCE_ID = t5.ID AND t5.DEL_FLAG = '0'")
                .leftJoin("SYSTEM_USER t6 on t.CREATOR_ID = t6.USER_ID AND t6.DEL_FLAG = '0'")
                .eq(true, DaAssetApplyDO::getId, id);
        DaAssetApplyDO daAssetApplyDO = daAssetApplyMapper.selectOne(lambdaWrapper);

        DaAssetThemeRelPageReqVO daAssetThemeRelPageReqVO = new DaAssetThemeRelPageReqVO();
        daAssetThemeRelPageReqVO.setAssetId(daAssetApplyDO.getId());
        List<DaAssetThemeRelRespVO> daAssetThemeRelList = daAssetThemeRelService.getDaAssetThemeRelList(daAssetThemeRelPageReqVO);
        daAssetApplyDO.setDaAssetThemeRelList(daAssetThemeRelList);

        return daAssetApplyDO;
    }

    @Override
    public List<DaAssetApplyDO> getDaAssetApplyList() {
        return daAssetApplyMapper.selectList();
    }

    @Override
    public Map<Long, DaAssetApplyDO> getDaAssetApplyMap() {
        List<DaAssetApplyDO> daAssetApplyList = daAssetApplyMapper.selectList();
        return daAssetApplyList.stream()
                .collect(Collectors.toMap(
                        DaAssetApplyDO::getId,
                        daAssetApplyDO -> daAssetApplyDO,
                        // 保留已存在的值
                        (existing, replacement) -> existing
                ));
    }


    /**
     * 导入数据资产申请数据
     *
     * @param importExcelList 数据资产申请数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName        操作用户
     * @return 结果
     */
    @Override
    public String importDaAssetApply(List<DaAssetApplyRespVO> importExcelList, boolean isUpdateSupport, String operName) {
        if (StringUtils.isNull(importExcelList) || importExcelList.size() == 0) {
            throw new ServiceException("导入数据不能为空！");
        }

        int successNum = 0;
        int failureNum = 0;
        List<String> successMessages = new ArrayList<>();
        List<String> failureMessages = new ArrayList<>();

        for (DaAssetApplyRespVO respVO : importExcelList) {
            try {
                DaAssetApplyDO daAssetApplyDO = BeanUtils.toBean(respVO, DaAssetApplyDO.class);
                Long daAssetApplyId = respVO.getId();
                if (isUpdateSupport) {
                    if (daAssetApplyId != null) {
                        DaAssetApplyDO existingDaAssetApply = daAssetApplyMapper.selectById(daAssetApplyId);
                        if (existingDaAssetApply != null) {
                            daAssetApplyMapper.updateById(daAssetApplyDO);
                            successNum++;
                            successMessages.add("数据更新成功，ID为 " + daAssetApplyId + " 的数据资产申请记录。");
                        } else {
                            failureNum++;
                            failureMessages.add("数据更新失败，ID为 " + daAssetApplyId + " 的数据资产申请记录不存在。");
                        }
                    } else {
                        failureNum++;
                        failureMessages.add("数据更新失败，某条记录的ID不存在。");
                    }
                } else {
                    QueryWrapper<DaAssetApplyDO> queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq("id", daAssetApplyId);
                    DaAssetApplyDO existingDaAssetApply = daAssetApplyMapper.selectOne(queryWrapper);
                    if (existingDaAssetApply == null) {
                        daAssetApplyMapper.insert(daAssetApplyDO);
                        successNum++;
                        successMessages.add("数据插入成功，ID为 " + daAssetApplyId + " 的数据资产申请记录。");
                    } else {
                        failureNum++;
                        failureMessages.add("数据插入失败，ID为 " + daAssetApplyId + " 的数据资产申请记录已存在。");
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
