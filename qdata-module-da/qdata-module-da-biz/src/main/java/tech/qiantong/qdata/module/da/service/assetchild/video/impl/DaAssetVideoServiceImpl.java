package tech.qiantong.qdata.module.da.service.assetchild.video.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.common.exception.ServiceException;
import tech.qiantong.qdata.common.utils.StringUtils;
import tech.qiantong.qdata.common.utils.object.BeanUtils;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.video.vo.DaAssetVideoPageReqVO;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.video.vo.DaAssetVideoReqVO;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.video.vo.DaAssetVideoRespVO;
import tech.qiantong.qdata.module.da.controller.admin.assetchild.video.vo.DaAssetVideoSaveReqVO;
import tech.qiantong.qdata.module.da.dal.dataobject.assetchild.video.DaAssetVideoDO;
import tech.qiantong.qdata.module.da.dal.mapper.assetchild.video.DaAssetVideoMapper;
import tech.qiantong.qdata.module.da.service.assetchild.video.IDaAssetVideoService;
import tech.qiantong.qdata.mybatis.core.query.LambdaQueryWrapperX;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 数据资产-视频数据Service业务层处理
 *
 * @author qdata
 * @date 2025-04-14
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class DaAssetVideoServiceImpl  extends ServiceImpl<DaAssetVideoMapper,DaAssetVideoDO> implements IDaAssetVideoService {
    @Resource
    private DaAssetVideoMapper daAssetVideoMapper;

    @Override
    public PageResult<DaAssetVideoDO> getDaAssetVideoPage(DaAssetVideoPageReqVO pageReqVO) {
        return daAssetVideoMapper.selectPage(pageReqVO);
    }

    @Override
    public DaAssetVideoRespVO getDaAssetVideoByAssetId(Long assetId) {
        LambdaQueryWrapperX<DaAssetVideoDO> queryWrapperX = new LambdaQueryWrapperX<>();
        queryWrapperX.eqIfPresent(DaAssetVideoDO::getAssetId,assetId);
        DaAssetVideoDO daAssetApiDO = daAssetVideoMapper.selectOne(queryWrapperX);
        return BeanUtils.toBean(daAssetApiDO, DaAssetVideoRespVO.class);
    }

    @Override
    public Long createDaAssetVideo(DaAssetVideoSaveReqVO createReqVO) {
        DaAssetVideoDO dictType = BeanUtils.toBean(createReqVO, DaAssetVideoDO.class);
        daAssetVideoMapper.insert(dictType);
        return dictType.getId();
    }

    @Override
    public int updateDaAssetVideo(DaAssetVideoSaveReqVO updateReqVO) {
        // 相关校验

        // 更新数据资产-视频数据
        DaAssetVideoDO updateObj = BeanUtils.toBean(updateReqVO, DaAssetVideoDO.class);
        return daAssetVideoMapper.updateById(updateObj);
    }
    @Override
    public int removeDaAssetVideo(Collection<Long> idList) {
        // 批量删除数据资产-视频数据
        return daAssetVideoMapper.deleteBatchIds(idList);
    }

    @Override
    public DaAssetVideoDO getDaAssetVideoById(Long id) {
        return daAssetVideoMapper.selectById(id);
    }

    @Override
    public List<DaAssetVideoDO> getDaAssetVideoList() {
        return daAssetVideoMapper.selectList();
    }

    @Override
    public Map<Long, DaAssetVideoDO> getDaAssetVideoMap() {
        List<DaAssetVideoDO> daAssetVideoList = daAssetVideoMapper.selectList();
        return daAssetVideoList.stream()
                .collect(Collectors.toMap(
                        DaAssetVideoDO::getId,
                        daAssetVideoDO -> daAssetVideoDO,
                        // 保留已存在的值
                        (existing, replacement) -> existing
                ));
    }


        /**
         * 导入数据资产-视频数据数据
         *
         * @param importExcelList 数据资产-视频数据数据列表
         * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
         * @param operName 操作用户
         * @return 结果
         */
        @Override
        public String importDaAssetVideo(List<DaAssetVideoRespVO> importExcelList, boolean isUpdateSupport, String operName) {
            if (StringUtils.isNull(importExcelList) || importExcelList.size() == 0) {
                throw new ServiceException("导入数据不能为空！");
            }

            int successNum = 0;
            int failureNum = 0;
            List<String> successMessages = new ArrayList<>();
            List<String> failureMessages = new ArrayList<>();

            for (DaAssetVideoRespVO respVO : importExcelList) {
                try {
                    DaAssetVideoDO daAssetVideoDO = BeanUtils.toBean(respVO, DaAssetVideoDO.class);
                    Long daAssetVideoId = respVO.getId();
                    if (isUpdateSupport) {
                        if (daAssetVideoId != null) {
                            DaAssetVideoDO existingDaAssetVideo = daAssetVideoMapper.selectById(daAssetVideoId);
                            if (existingDaAssetVideo != null) {
                                daAssetVideoMapper.updateById(daAssetVideoDO);
                                successNum++;
                                successMessages.add("数据更新成功，ID为 " + daAssetVideoId + " 的数据资产-视频数据记录。");
                            } else {
                                failureNum++;
                                failureMessages.add("数据更新失败，ID为 " + daAssetVideoId + " 的数据资产-视频数据记录不存在。");
                            }
                        } else {
                            failureNum++;
                            failureMessages.add("数据更新失败，某条记录的ID不存在。");
                        }
                    } else {
                        QueryWrapper<DaAssetVideoDO> queryWrapper = new QueryWrapper<>();
                        queryWrapper.eq("id", daAssetVideoId);
                        DaAssetVideoDO existingDaAssetVideo = daAssetVideoMapper.selectOne(queryWrapper);
                        if (existingDaAssetVideo == null) {
                            daAssetVideoMapper.insert(daAssetVideoDO);
                            successNum++;
                            successMessages.add("数据插入成功，ID为 " + daAssetVideoId + " 的数据资产-视频数据记录。");
                        } else {
                            failureNum++;
                            failureMessages.add("数据插入失败，ID为 " + daAssetVideoId + " 的数据资产-视频数据记录已存在。");
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
    public void queryServiceForwarding(HttpServletResponse response, DaAssetVideoReqVO daAssetVideoReqVO) {

    }
}
