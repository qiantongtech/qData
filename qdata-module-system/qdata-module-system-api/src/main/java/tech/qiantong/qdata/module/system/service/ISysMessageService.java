package tech.qiantong.qdata.module.system.service;

import tech.qiantong.qdata.module.system.api.message.dto.MessageSaveReqDTO;

public interface ISysMessageService {
    public Boolean send(Long templateId, MessageSaveReqDTO messageSaveReqDTO, Object entity);

    /**
     * 数据发现使用
     * @param receiverId
     * @param entity
     * @return
     */
    public Boolean sendDbChangeMessage( Long receiverId, Object entity);


}
