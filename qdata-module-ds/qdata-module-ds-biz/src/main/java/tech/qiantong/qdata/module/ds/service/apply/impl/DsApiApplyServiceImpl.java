package tech.qiantong.qdata.module.ds.service.apply.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.common.exception.ServiceException;
import tech.qiantong.qdata.common.utils.StringUtils;
import tech.qiantong.qdata.common.utils.object.BeanUtils;
import tech.qiantong.qdata.module.ds.api.apply.dto.DsApiApplyReqDTO;
import tech.qiantong.qdata.module.ds.api.apply.dto.DsApiApplyRespDTO;
import tech.qiantong.qdata.module.ds.api.service.apply.DsApiApplyService;
import tech.qiantong.qdata.module.ds.controller.admin.apply.vo.DsApiApplyPageReqVO;
import tech.qiantong.qdata.module.ds.controller.admin.apply.vo.DsApiApplyRespVO;
import tech.qiantong.qdata.module.ds.controller.admin.apply.vo.DsApiApplySaveReqVO;
import tech.qiantong.qdata.module.ds.dal.dataobject.api.DsApiDO;
import tech.qiantong.qdata.module.ds.dal.dataobject.apply.DsApiApplyDO;
import tech.qiantong.qdata.module.ds.dal.mapper.api.DsApiMapper;
import tech.qiantong.qdata.module.ds.dal.mapper.apply.DsApiApplyMapper;
import tech.qiantong.qdata.module.ds.service.api.IDsApiService;
import tech.qiantong.qdata.module.ds.service.apply.IDsApiApplyService;
import tech.qiantong.qdata.module.system.api.flowMyBusiness.dto.SystemFlowMyBusinessReqDTO;
import tech.qiantong.qdata.module.system.api.flowMyBusiness.service.SysFlowMyBusinessService;
import tech.qiantong.qdata.mybatis.core.query.LambdaQueryWrapperX;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;
/**
 * API服务-申请Service业务层处理
 *
 * @author qdata
 * @date 2025-04-22
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class DsApiApplyServiceImpl  extends ServiceImpl<DsApiApplyMapper, DsApiApplyDO> implements IDsApiApplyService, DsApiApplyService {
    @Resource
    private DsApiApplyMapper dsApiApplyMapper;
    @Resource
    private DsApiMapper dsApiMapper;
    @Resource
    private IDsApiService dsApiService;
    @Resource
    private SysFlowMyBusinessService flowMyBusinessService;

    @Override
    public PageResult<DsApiApplyDO> getDsApiApplyPage(DsApiApplyPageReqVO pageReqVO) {
        if (pageReqVO != null) {
            String applyBy = pageReqVO.getApplyBy();
            if (StringUtils.isEmpty(applyBy)) {
                pageReqVO.setApplyBy(pageReqVO.getName());
            }
        }
        PageResult<DsApiApplyDO> apiApplyDOPageResult = dsApiApplyMapper.selectPage(pageReqVO);
        if (apiApplyDOPageResult.getTotal() > 0) {
            List<DsApiApplyDO> applyDOPageResultRows = (List<DsApiApplyDO>)apiApplyDOPageResult.getRows();
            for (int i = 0; i < applyDOPageResultRows.size(); i++) {
                DsApiApplyDO dsApiApplyDO = applyDOPageResultRows.get(i);
                String apiId = dsApiApplyDO.getApiId();
                QueryWrapper queryWrapper = new QueryWrapper();
                queryWrapper.eq("id", apiId);
                queryWrapper.eq("del_flag", 0);
                DsApiDO dsApiDO = dsApiMapper.selectOne(queryWrapper);
                if (dsApiDO != null) {
                    dsApiApplyDO.setApiName(dsApiDO.getName());
                    dsApiApplyDO.setApiUrl(dsApiDO.getApiUrl());
                    applyDOPageResultRows.set(i, dsApiApplyDO);
                }
            }
            apiApplyDOPageResult.setRows(applyDOPageResultRows);
            return apiApplyDOPageResult;
        }

        return dsApiApplyMapper.selectPage(pageReqVO);
    }

    @Override
    public PageResult<DsApiApplyRespDTO> getDsApiApplyPage(DsApiApplyReqDTO pageReqVO) {
        DsApiApplyPageReqVO applyPageReqVO = BeanUtils.toBean(pageReqVO, DsApiApplyPageReqVO.class);
        PageResult<DsApiApplyDO> applyDOPageResult = dsApiApplyMapper.selectPage(applyPageReqVO);
        return BeanUtils.toBean(applyDOPageResult,DsApiApplyRespDTO.class);
    }

    @Override
    public List<DsApiApplyRespDTO> getDsApiApplyList(DsApiApplyReqDTO pageReqVO) {
        LambdaQueryWrapperX<DsApiApplyDO> queryWrapperX = new LambdaQueryWrapperX<>();
        queryWrapperX.eqIfPresent(DsApiApplyDO::getApiId,pageReqVO.getApiId()).eqIfPresent(DsApiApplyDO::getCreatorId,pageReqVO.getCreatorId());
        List<DsApiApplyDO> dsApiApplyDOList = dsApiApplyMapper.selectList(queryWrapperX);
        return BeanUtils.toBean(dsApiApplyDOList,DsApiApplyRespDTO.class);
    }

    @Override
    public Map<String,Long> createDsApiApply(DsApiApplySaveReqVO createReqVO) {
        DsApiApplyDO dictType = BeanUtils.toBean(createReqVO, DsApiApplyDO.class);
        dsApiApplyMapper.insert(dictType);
        SystemFlowMyBusinessReqDTO systemFlowMyBusinessReqDTO = new SystemFlowMyBusinessReqDTO();
        systemFlowMyBusinessReqDTO.setDataId(dictType.getId());
        systemFlowMyBusinessReqDTO.setType("0");
        systemFlowMyBusinessReqDTO.setCreatorId(createReqVO.getCreatorId());
        systemFlowMyBusinessReqDTO.setCreateBy(createReqVO.getCreateBy());
        Long flowMyBusinessId = flowMyBusinessService.createSysFlowMyBusiness(systemFlowMyBusinessReqDTO);
        Map<String,Long> map = new HashMap<>();
        map.put("dsApiApplyId",dictType.getId());
        map.put("flowMyBusinessId",flowMyBusinessId);
        return map;
    }

    @Override
    public int updateDsApiApply(DsApiApplySaveReqVO updateReqVO) {
        // 相关校验

        // 更新API服务-申请
        DsApiApplyDO updateObj = BeanUtils.toBean(updateReqVO, DsApiApplyDO.class);
        return dsApiApplyMapper.updateById(updateObj);
    }
    @Override
    public int removeDsApiApply(Collection<Long> idList) {
        // 批量删除API服务-申请
        int batchIds = dsApiApplyMapper.deleteBatchIds(idList);
        if (batchIds > 0){
            SystemFlowMyBusinessReqDTO systemFlowMyBusinessReqDTO = new SystemFlowMyBusinessReqDTO();
            systemFlowMyBusinessReqDTO.setType("0");
            systemFlowMyBusinessReqDTO.setDataId(new ArrayList<>(idList).get(0));
            flowMyBusinessService.removeFlowMyBusiness(systemFlowMyBusinessReqDTO);
        }
        return batchIds;
    }

    @Override
    public DsApiApplyDO getDsApiApplyById(Long id) {
        return dsApiApplyMapper.selectById(id);
    }

    @Override
    public List<DsApiApplyDO> getDsApiApplyList() {
        return dsApiApplyMapper.selectList();
    }

    @Override
    public Map<Long, DsApiApplyDO> getDsApiApplyMap() {
        List<DsApiApplyDO> dsApiApplyList = dsApiApplyMapper.selectList();
        return dsApiApplyList.stream()
                .collect(Collectors.toMap(
                        DsApiApplyDO::getId,
                        dsApiApplyDO -> dsApiApplyDO,
                        // 保留已存在的值
                        (existing, replacement) -> existing
                ));
    }


        /**
         * 导入API服务-申请数据
         *
         * @param importExcelList API服务-申请数据列表
         * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
         * @param operName 操作用户
         * @return 结果
         */
        @Override
        public String importDsApiApply(List<DsApiApplyRespVO> importExcelList, boolean isUpdateSupport, String operName) {
            if (StringUtils.isNull(importExcelList) || importExcelList.size() == 0) {
                throw new ServiceException("导入数据不能为空！");
            }

            int successNum = 0;
            int failureNum = 0;
            List<String> successMessages = new ArrayList<>();
            List<String> failureMessages = new ArrayList<>();

            for (DsApiApplyRespVO respVO : importExcelList) {
                try {
                    DsApiApplyDO dsApiApplyDO = BeanUtils.toBean(respVO, DsApiApplyDO.class);
                    Long dsApiApplyId = respVO.getId();
                    if (isUpdateSupport) {
                        if (dsApiApplyId != null) {
                            DsApiApplyDO existingDsApiApply = dsApiApplyMapper.selectById(dsApiApplyId);
                            if (existingDsApiApply != null) {
                                dsApiApplyMapper.updateById(dsApiApplyDO);
                                successNum++;
                                successMessages.add("数据更新成功，ID为 " + dsApiApplyId + " 的API服务-申请记录。");
                            } else {
                                failureNum++;
                                failureMessages.add("数据更新失败，ID为 " + dsApiApplyId + " 的API服务-申请记录不存在。");
                            }
                        } else {
                            failureNum++;
                            failureMessages.add("数据更新失败，某条记录的ID不存在。");
                        }
                    } else {
                        QueryWrapper<DsApiApplyDO> queryWrapper = new QueryWrapper<>();
                        queryWrapper.eq("id", dsApiApplyId);
                        DsApiApplyDO existingDsApiApply = dsApiApplyMapper.selectOne(queryWrapper);
                        if (existingDsApiApply == null) {
                            dsApiApplyMapper.insert(dsApiApplyDO);
                            successNum++;
                            successMessages.add("数据插入成功，ID为 " + dsApiApplyId + " 的API服务-申请记录。");
                        } else {
                            failureNum++;
                            failureMessages.add("数据插入失败，ID为 " + dsApiApplyId + " 的API服务-申请记录已存在。");
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
    public Map<String, Object> getFlyfowApiApply(DsApiApplySaveReqVO dsApiApply) {
//        DsApiApplyDO dsApiApplyDO = this.getDsApiApplyById(dsApiApply.getId());
//        RpUserRespDTO userRespDTO = rpUserService.getUserById(dsApiApplyDO.getCreatorId());
//        MPJLambdaWrapper<DsApiDO> lambdaWrapper = new MPJLambdaWrapper();
//        lambdaWrapper.selectAll(DsApiDO.class)
//                .select("t2.NAME AS catName")
//                .leftJoin("ATT_API_CAT t2 on t.CAT_CODE = t2.CODE AND t2.DEL_FLAG = '0'")
//                .eq("t.ID", dsApiApplyDO.getApiId());
//        DsApiDO dsApiDO = dsApiMapper.selectOne(lambdaWrapper);
        Map<String, Object> map = new HashMap<>();
//        map.put("dsApi",dsApiDO);
//        map.put("dsApiApply",dsApiApplyDO);
//        map.put("user",userRespDTO);
        return map;
    }
}
