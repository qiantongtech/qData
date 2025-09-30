package tech.qiantong.qdata.module.system.service.message;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.system.controller.admin.system.message.vo.MessagePageReqVO;
import tech.qiantong.qdata.module.system.controller.admin.system.message.vo.MessageSaveReqVO;
import tech.qiantong.qdata.module.system.convert.message.MessageConvert;
import tech.qiantong.qdata.module.system.dal.dataobject.message.MessageDO;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 消息Service接口
 *
 * @author qdata
 * @date 2024-10-31
 */
public interface IMessageService extends IService<MessageDO> {

    default PageResult<MessageDO> getMessagePage(MessagePageReqVO message) {
        QueryWrapper<MessageDO> qw = new QueryWrapper<>(MessageConvert.INSTANCE.convertToDO(message));

        Date startTime = message.getStartTime();
        Date endTime   = message.getEndTime();

        if (startTime != null || endTime != null) {
            if (startTime != null) {
                // >= 当日 00:00:00
                Calendar cal = Calendar.getInstance();
                cal.setTime(startTime);
                cal.set(Calendar.HOUR_OF_DAY, 0);
                cal.set(Calendar.MINUTE, 0);
                cal.set(Calendar.SECOND, 0);
                cal.set(Calendar.MILLISECOND, 0);
                qw.ge("create_time", cal.getTime());
            }
            if (endTime != null) {
                // <= 当日 23:59:59.999
                Calendar cal = Calendar.getInstance();
                cal.setTime(endTime);
                cal.set(Calendar.HOUR_OF_DAY, 23);
                cal.set(Calendar.MINUTE, 59);
                cal.set(Calendar.SECOND, 59);
                cal.set(Calendar.MILLISECOND, 999);
                qw.le("create_time", cal.getTime());
            }
        }

        List<MessageDO> list = list(qw);
        return new PageResult<>(list, new PageInfo<>(list).getTotal());
    }

    /**
     * 通过模版向某一个用户发送消息
     * @param templateId 模版id
     * @param messageSaveReqVO 消息创建
     * @param entity 实体对象
     * @return 是否发送成功
     */
    public Boolean send(Long templateId, MessageSaveReqVO messageSaveReqVO, Object entity);

    /**
     * 查询消息数量
     * @param message 查询条件
     * @return 数量
     */
    public Long getNum(MessagePageReqVO message);

    /**
     * 设置已读
     * @param id 消息id
     * @return 是否成功
     */
    public Boolean read(Long id);

    /**
     * 全部已读
     * @param receiverId 接收人id
     * @param category 消息类型
     * @param module 消息模块
     * @return 是否成功
     */
    public Boolean readAll(Long receiverId, Integer category, Integer module);

    /**
     * 更新接收人未读消息
     *
     * @param receiverId 接收人id
     */
    public void getReceiverWDNum(Long receiverId);

}
