package tech.qiantong.qdata.module.da.service.assetchild.operate.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.common.exception.ServiceException;
import tech.qiantong.qdata.common.utils.StringUtils;
import tech.qiantong.qdata.common.utils.object.BeanUtils;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.operate.vo.DaAssetOperateApplyPageReqVO;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.operate.vo.DaAssetOperateApplyRespVO;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.operate.vo.DaAssetOperateApplySaveReqVO;
import tech.qiantong.qdata.module.da.dal.dataobject.assetchild.operate.DaAssetOperateApplyDO;
import tech.qiantong.qdata.module.da.dal.mapper.assetchild.operate.DaAssetOperateApplyMapper;
import tech.qiantong.qdata.module.da.service.assetchild.operate.IDaAssetOperateApplyService;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
/**
 * 数据资产操作申请Service业务层处理
 *
 * @author qdata
 * @date 2025-05-09
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class DaAssetOperateApplyServiceImpl  extends ServiceImpl<DaAssetOperateApplyMapper,DaAssetOperateApplyDO> implements IDaAssetOperateApplyService {
    @Resource
    private DaAssetOperateApplyMapper daAssetOperateApplyMapper;

    @Override
    public PageResult<DaAssetOperateApplyDO> getDaAssetOperateApplyPage(DaAssetOperateApplyPageReqVO pageReqVO) {
        return daAssetOperateApplyMapper.selectPage(pageReqVO);
    }

    @Override
    public Long createDaAssetOperateApply(DaAssetOperateApplySaveReqVO createReqVO) {
        DaAssetOperateApplyDO dictType = BeanUtils.toBean(createReqVO, DaAssetOperateApplyDO.class);
        daAssetOperateApplyMapper.insert(dictType);
        return dictType.getId();
    }

    @Override
    public int updateDaAssetOperateApply(DaAssetOperateApplySaveReqVO updateReqVO) {
        // 相关校验

        // 更新数据资产操作申请
        DaAssetOperateApplyDO updateObj = BeanUtils.toBean(updateReqVO, DaAssetOperateApplyDO.class);
        return daAssetOperateApplyMapper.updateById(updateObj);
    }
    @Override
    public int removeDaAssetOperateApply(Collection<Long> idList) {
        // 批量删除数据资产操作申请
        return daAssetOperateApplyMapper.deleteBatchIds(idList);
    }

    @Override
    public DaAssetOperateApplyDO getDaAssetOperateApplyById(Long id) {
        return daAssetOperateApplyMapper.selectById(id);
    }

    @Override
    public List<DaAssetOperateApplyDO> getDaAssetOperateApplyList() {
        return daAssetOperateApplyMapper.selectList();
    }

    @Override
    public Map<Long, DaAssetOperateApplyDO> getDaAssetOperateApplyMap() {
        List<DaAssetOperateApplyDO> daAssetOperateApplyList = daAssetOperateApplyMapper.selectList();
        return daAssetOperateApplyList.stream()
                .collect(Collectors.toMap(
                        DaAssetOperateApplyDO::getId,
                        daAssetOperateApplyDO -> daAssetOperateApplyDO,
                        // 保留已存在的值
                        (existing, replacement) -> existing
                ));
    }


        /**
         * 导入数据资产操作申请数据
         *
         * @param importExcelList 数据资产操作申请数据列表
         * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
         * @param operName 操作用户
         * @return 结果
         */
        @Override
        public String importDaAssetOperateApply(List<DaAssetOperateApplyRespVO> importExcelList, boolean isUpdateSupport, String operName) {
            if (StringUtils.isNull(importExcelList) || importExcelList.size() == 0) {
                throw new ServiceException("导入数据不能为空！");
            }

            int successNum = 0;
            int failureNum = 0;
            List<String> successMessages = new ArrayList<>();
            List<String> failureMessages = new ArrayList<>();

            for (DaAssetOperateApplyRespVO respVO : importExcelList) {
                try {
                    DaAssetOperateApplyDO daAssetOperateApplyDO = BeanUtils.toBean(respVO, DaAssetOperateApplyDO.class);
                    Long daAssetOperateApplyId = respVO.getId();
                    if (isUpdateSupport) {
                        if (daAssetOperateApplyId != null) {
                            DaAssetOperateApplyDO existingDaAssetOperateApply = daAssetOperateApplyMapper.selectById(daAssetOperateApplyId);
                            if (existingDaAssetOperateApply != null) {
                                daAssetOperateApplyMapper.updateById(daAssetOperateApplyDO);
                                successNum++;
                                successMessages.add("数据更新成功，ID为 " + daAssetOperateApplyId + " 的数据资产操作申请记录。");
                            } else {
                                failureNum++;
                                failureMessages.add("数据更新失败，ID为 " + daAssetOperateApplyId + " 的数据资产操作申请记录不存在。");
                            }
                        } else {
                            failureNum++;
                            failureMessages.add("数据更新失败，某条记录的ID不存在。");
                        }
                    } else {
                        QueryWrapper<DaAssetOperateApplyDO> queryWrapper = new QueryWrapper<>();
                        queryWrapper.eq("id", daAssetOperateApplyId);
                        DaAssetOperateApplyDO existingDaAssetOperateApply = daAssetOperateApplyMapper.selectOne(queryWrapper);
                        if (existingDaAssetOperateApply == null) {
                            daAssetOperateApplyMapper.insert(daAssetOperateApplyDO);
                            successNum++;
                            successMessages.add("数据插入成功，ID为 " + daAssetOperateApplyId + " 的数据资产操作申请记录。");
                        } else {
                            failureNum++;
                            failureMessages.add("数据插入失败，ID为 " + daAssetOperateApplyId + " 的数据资产操作申请记录已存在。");
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
