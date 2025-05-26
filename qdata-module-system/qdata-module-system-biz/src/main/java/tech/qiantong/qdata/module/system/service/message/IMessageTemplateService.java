package tech.qiantong.qdata.module.system.service.message;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import tech.qiantong.qdata.common.core.page.PageResult;
import tech.qiantong.qdata.module.system.controller.admin.system.message.vo.MessageTemplatePageReqVO;
import tech.qiantong.qdata.module.system.convert.message.MessageTemplateConvert;
import tech.qiantong.qdata.module.system.dal.dataobject.message.MessageTemplateDO;

import java.util.List;

/**
 * 消息模板Service接口
 *
 * @author qdata
 * @date 2024-10-31
 */
public interface IMessageTemplateService extends IService<MessageTemplateDO> {

    default PageResult<MessageTemplateDO> getMessageTemplatePage(MessageTemplatePageReqVO messageTemplate) {
        QueryWrapper<MessageTemplateDO> queryWrapper = new QueryWrapper<>(MessageTemplateConvert.INSTANCE.convertToDO(messageTemplate));
        List<MessageTemplateDO> list = list(queryWrapper);

        return new PageResult(list, new PageInfo(list).getTotal());
    }
}
