package tech.qiantong.qdata.module.att.service.client.impl;

import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.common.exception.ServiceException;
import tech.qiantong.qdata.common.utils.StringUtils;
import tech.qiantong.qdata.common.utils.object.BeanUtils;
import tech.qiantong.qdata.module.att.controller.admin.client.vo.AttClientPageReqVO;
import tech.qiantong.qdata.module.att.controller.admin.client.vo.AttClientRespVO;
import tech.qiantong.qdata.module.att.controller.admin.client.vo.AttClientSaveReqVO;
import tech.qiantong.qdata.module.att.dal.dataobject.client.AttClientDO;
import tech.qiantong.qdata.module.att.dal.mapper.client.AttClientMapper;
import tech.qiantong.qdata.module.att.service.client.IAttClientService;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
/**
 * 应用管理Service业务层处理
 *
 * @author qdata
 * @date 2025-02-18
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class AttClientServiceImpl  extends ServiceImpl<AttClientMapper,AttClientDO> implements IAttClientService {
    @Resource
    private AttClientMapper attClientMapper;

    @Override
    public PageResult<AttClientDO> getAttClientPage(AttClientPageReqVO pageReqVO) {
        return attClientMapper.selectPage(pageReqVO);
    }

    @Override
    public Long createAttClient(AttClientSaveReqVO createReqVO) {
        AttClientDO dictType = BeanUtils.toBean(createReqVO, AttClientDO.class);
        dictType.setSecret(IdUtil.fastSimpleUUID());
        attClientMapper.insert(dictType);
        return dictType.getId();
    }

    @Override
    public int updateAttClient(AttClientSaveReqVO updateReqVO) {
        // 相关校验

        // 更新应用管理
        AttClientDO updateObj = BeanUtils.toBean(updateReqVO, AttClientDO.class);
        return attClientMapper.updateById(updateObj);
    }
    @Override
    public int removeAttClient(Collection<Long> idList) {
        // 批量删除应用管理
        return attClientMapper.deleteBatchIds(idList);
    }

    @Override
    public AttClientDO getAttClientById(Long id) {
        return attClientMapper.selectById(id);
    }

    @Override
    public List<AttClientDO> getAttClientList() {
        return attClientMapper.selectList();
    }

    @Override
    public Map<Long, AttClientDO> getAttClientMap() {
        List<AttClientDO> attClientList = attClientMapper.selectList();
        return attClientList.stream()
                .collect(Collectors.toMap(
                        AttClientDO::getId,
                        attClientDO -> attClientDO,
                        // 保留已存在的值
                        (existing, replacement) -> existing
                ));
    }


        /**
         * 导入应用管理数据
         *
         * @param importExcelList 应用管理数据列表
         * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
         * @param operName 操作用户
         * @return 结果
         */
        @Override
        public String importAttClient(List<AttClientRespVO> importExcelList, boolean isUpdateSupport, String operName) {
            if (StringUtils.isNull(importExcelList) || importExcelList.size() == 0) {
                throw new ServiceException("导入数据不能为空！");
            }

            int successNum = 0;
            int failureNum = 0;
            List<String> successMessages = new ArrayList<>();
            List<String> failureMessages = new ArrayList<>();

            for (AttClientRespVO respVO : importExcelList) {
                try {
                    AttClientDO attClientDO = BeanUtils.toBean(respVO, AttClientDO.class);
                    Long attClientId = respVO.getId();
                    if (isUpdateSupport) {
                        if (attClientId != null) {
                            AttClientDO existingAttClient = attClientMapper.selectById(attClientId);
                            if (existingAttClient != null) {
                                attClientMapper.updateById(attClientDO);
                                successNum++;
                                successMessages.add("数据更新成功，ID为 " + attClientId + " 的应用管理记录。");
                            } else {
                                failureNum++;
                                failureMessages.add("数据更新失败，ID为 " + attClientId + " 的应用管理记录不存在。");
                            }
                        } else {
                            failureNum++;
                            failureMessages.add("数据更新失败，某条记录的ID不存在。");
                        }
                    } else {
                        QueryWrapper<AttClientDO> queryWrapper = new QueryWrapper<>();
                        queryWrapper.eq("id", attClientId);
                        AttClientDO existingAttClient = attClientMapper.selectOne(queryWrapper);
                        if (existingAttClient == null) {
                            attClientMapper.insert(attClientDO);
                            successNum++;
                            successMessages.add("数据插入成功，ID为 " + attClientId + " 的应用管理记录。");
                        } else {
                            failureNum++;
                            failureMessages.add("数据插入失败，ID为 " + attClientId + " 的应用管理记录已存在。");
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
