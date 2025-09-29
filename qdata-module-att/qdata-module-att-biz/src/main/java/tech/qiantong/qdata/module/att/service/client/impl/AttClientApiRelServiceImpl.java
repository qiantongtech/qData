package tech.qiantong.qdata.module.att.service.client.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.common.exception.ServiceException;
import tech.qiantong.qdata.common.utils.StringUtils;
import tech.qiantong.qdata.common.utils.object.BeanUtils;
import tech.qiantong.qdata.module.att.controller.admin.client.vo.AttClientApiRelPageReqVO;
import tech.qiantong.qdata.module.att.controller.admin.client.vo.AttClientApiRelRespVO;
import tech.qiantong.qdata.module.att.controller.admin.client.vo.AttClientApiRelSaveReqVO;
import tech.qiantong.qdata.module.att.dal.dataobject.client.AttClientApiRelDO;
import tech.qiantong.qdata.module.att.dal.mapper.client.AttClientApiRelMapper;
import tech.qiantong.qdata.module.att.service.client.IAttClientApiRelService;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
/**
 * 应用API服务关联Service业务层处理
 *
 * @author FXB
 * @date 2025-08-21
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class AttClientApiRelServiceImpl  extends ServiceImpl<AttClientApiRelMapper,AttClientApiRelDO> implements IAttClientApiRelService {
    @Resource
    private AttClientApiRelMapper attClientApiRelMapper;

    @Override
    public PageResult<AttClientApiRelDO> getAttClientApiRelPage(AttClientApiRelPageReqVO pageReqVO) {
        return attClientApiRelMapper.selectPage(pageReqVO);
    }

    @Override
    public Long createAttClientApiRel(AttClientApiRelSaveReqVO createReqVO) {
        AttClientApiRelDO dictType = BeanUtils.toBean(createReqVO, AttClientApiRelDO.class);
        attClientApiRelMapper.insert(dictType);
        return dictType.getId();
    }

    @Override
    public int updateAttClientApiRel(AttClientApiRelSaveReqVO updateReqVO) {
        // 相关校验

        // 更新应用API服务关联
        AttClientApiRelDO updateObj = BeanUtils.toBean(updateReqVO, AttClientApiRelDO.class);
        return attClientApiRelMapper.updateById(updateObj);
    }
    @Override
    public int removeAttClientApiRel(Collection<Long> idList) {
        // 批量删除应用API服务关联
        return attClientApiRelMapper.deleteBatchIds(idList);
    }

    @Override
    public AttClientApiRelDO getAttClientApiRelById(Long id) {
        return attClientApiRelMapper.selectById(id);
    }

    @Override
    public List<AttClientApiRelDO> getAttClientApiRelList() {
        return attClientApiRelMapper.selectList();
    }

    @Override
    public Map<Long, AttClientApiRelDO> getAttClientApiRelMap() {
        List<AttClientApiRelDO> attClientApiRelList = attClientApiRelMapper.selectList();
        return attClientApiRelList.stream()
                .collect(Collectors.toMap(
                        AttClientApiRelDO::getId,
                        attClientApiRelDO -> attClientApiRelDO,
                        // 保留已存在的值
                        (existing, replacement) -> existing
                ));
    }


        /**
         * 导入应用API服务关联数据
         *
         * @param importExcelList 应用API服务关联数据列表
         * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
         * @param operName 操作用户
         * @return 结果
         */
        @Override
        public String importAttClientApiRel(List<AttClientApiRelRespVO> importExcelList, boolean isUpdateSupport, String operName) {
            if (StringUtils.isNull(importExcelList) || importExcelList.size() == 0) {
                throw new ServiceException("导入数据不能为空！");
            }

            int successNum = 0;
            int failureNum = 0;
            List<String> successMessages = new ArrayList<>();
            List<String> failureMessages = new ArrayList<>();

            for (AttClientApiRelRespVO respVO : importExcelList) {
                try {
                    AttClientApiRelDO attClientApiRelDO = BeanUtils.toBean(respVO, AttClientApiRelDO.class);
                    Long attClientApiRelId = respVO.getId();
                    if (isUpdateSupport) {
                        if (attClientApiRelId != null) {
                            AttClientApiRelDO existingAttClientApiRel = attClientApiRelMapper.selectById(attClientApiRelId);
                            if (existingAttClientApiRel != null) {
                                attClientApiRelMapper.updateById(attClientApiRelDO);
                                successNum++;
                                successMessages.add("数据更新成功，ID为 " + attClientApiRelId + " 的应用API服务关联记录。");
                            } else {
                                failureNum++;
                                failureMessages.add("数据更新失败，ID为 " + attClientApiRelId + " 的应用API服务关联记录不存在。");
                            }
                        } else {
                            failureNum++;
                            failureMessages.add("数据更新失败，某条记录的ID不存在。");
                        }
                    } else {
                        QueryWrapper<AttClientApiRelDO> queryWrapper = new QueryWrapper<>();
                        queryWrapper.eq("id", attClientApiRelId);
                        AttClientApiRelDO existingAttClientApiRel = attClientApiRelMapper.selectOne(queryWrapper);
                        if (existingAttClientApiRel == null) {
                            attClientApiRelMapper.insert(attClientApiRelDO);
                            successNum++;
                            successMessages.add("数据插入成功，ID为 " + attClientApiRelId + " 的应用API服务关联记录。");
                        } else {
                            failureNum++;
                            failureMessages.add("数据插入失败，ID为 " + attClientApiRelId + " 的应用API服务关联记录已存在。");
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
