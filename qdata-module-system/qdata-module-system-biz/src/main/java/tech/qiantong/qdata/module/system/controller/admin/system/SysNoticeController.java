/*
 * Copyright © 2025-present Jiangsu Qiantong Technology Co., Ltd.
 *
 * This file is part of qData Data Middle Platform (Open Source Edition).
 *
 * qData is licensed under Apache License 2.0 with additional qData terms.
 * You may use qData for commercial purposes, but you may not remove, hide,
 * modify, or replace the qData logo, copyright notices, license notices,
 * or attribution information without a separate commercial license.
 *
 * White-label use, OEM distribution, rebranding, or presenting qData as
 * another product requires separate commercial authorization from
 * Jiangsu Qiantong Technology Co., Ltd.
 *
 * Business License: https://community.qdata.tech/business/policy.html
 * See the LICENSE file in the project root for full license information.
 */

package tech.qiantong.qdata.module.system.controller.admin.system;

import java.util.Date;
import java.util.List;

import cn.hutool.core.date.DateUtil;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import tech.qiantong.qdata.common.annotation.Log;
import tech.qiantong.qdata.common.core.controller.BaseController;
import tech.qiantong.qdata.common.core.domain.AjaxResult;
import tech.qiantong.qdata.common.core.page.PageDomain;
import tech.qiantong.qdata.common.core.page.TableDataInfo;
import tech.qiantong.qdata.common.core.page.TableSupport;
import tech.qiantong.qdata.common.enums.BusinessType;
import tech.qiantong.qdata.common.utils.object.BeanUtils;
import tech.qiantong.qdata.common.utils.sql.SqlUtil;
import tech.qiantong.qdata.module.system.controller.admin.system.message.vo.MessagePageReqVO;
import tech.qiantong.qdata.module.system.controller.admin.system.message.websocket.WebSocketMessageServer;
import tech.qiantong.qdata.module.system.dal.dataobject.message.MessageDO;
import tech.qiantong.qdata.module.system.dal.mapper.message.MessageTemplateMapper;
import tech.qiantong.qdata.module.system.domain.SysNotice;
import tech.qiantong.qdata.module.system.service.ISysNoticeService;
import tech.qiantong.qdata.module.system.service.message.IMessageService;
import tech.qiantong.qdata.module.system.service.message.impl.MessageServiceImpl;

import javax.annotation.Resource;

import static tech.qiantong.qdata.common.utils.SecurityUtils.getLoginUser;

/**
 * Notice message handler
 *
 * @author qdata
 */
@RestController
@RequestMapping("/system/notice")
public class SysNoticeController extends BaseController
{
    @Autowired
    private ISysNoticeService noticeService;
    @Resource
    private WebSocketMessageServer webSocketMessageServer;
    @Resource
    private IMessageService messageService;

    /**
     * Get notice list
     */
    @GetMapping("/list")
    public TableDataInfo list(SysNotice notice)
    {
        startPage();
        List<SysNotice> list = noticeService.selectNoticeList(notice);
        return getDataTable(list);
    }


    /**
     * Get notice list (sorted)
     */
//    @PreAuthorize("@ss.hasPermi('system:notice:list')")
    @GetMapping("/sortList")
    public TableDataInfo sortList(SysNotice notice)
    {
        PageDomain var0 = TableSupport.buildPageRequest();
        Integer var1 = var0.getPageNum();
        Integer var2 = var0.getPageSize();
        String var3 = SqlUtil.escapeOrderBySql("top_flag desc, create_time desc");
        Boolean var4 = var0.getReasonable();
        PageHelper.startPage(var1, var2, var3).setReasonable(var4);
        List<SysNotice> list = noticeService.selectNoticeList(notice);
        return getDataTable(list);
    }


    /**
     * Get popup notice
     * @return
     */
    @GetMapping("/alertNotice")
    public AjaxResult alertNotice()
    {
        SysNotice noticeQo = new SysNotice();
        noticeQo.setAlertFlag(1);
        noticeQo.setStatus("1");
        noticeQo.getParams().put("efftectTime", DateUtil.now());
        List<SysNotice> list = noticeService.selectNoticeList(noticeQo);
        if (list.size() > 0){
            return success(list.get(0));
        }
        return success();
    }


    /**
     * Get notice details by ID
     */
    @PreAuthorize("@ss.hasPermi('system:notice:query')")
    @GetMapping(value = "/one")
    public AjaxResult getInfo(@RequestParam Long noticeId)
    {
        return success(noticeService.selectNoticeById(noticeId));
    }
    /**
     * Add notice
     */
    @PreAuthorize("@ss.hasPermi('system:notice:add')")
    @Log(title = "log.op.title.system.notice", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody SysNotice notice)
    {
        // Test message notification
        MessagePageReqVO messagePageReqVO = new MessagePageReqVO();
        messagePageReqVO.setContent(notice.getNoticeContent());
        messagePageReqVO.setTitle(notice.getNoticeTitle());
        messagePageReqVO.setEntityType(Integer.valueOf(notice.getNoticeType()));
        messagePageReqVO.setCreateTime(new Date());
        webSocketMessageServer.broadcastMessage(messagePageReqVO);
        notice.setCreateBy(getUsername());

        return toAjax(noticeService.insertNotice(notice));
    }

    /**
     * Edit notice/announcement
     */
    @PreAuthorize("@ss.hasPermi('system:notice:edit')")
    @Log(title = "log.op.title.system.notice", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody SysNotice notice)
    {
        notice.setUpdateBy(getUsername());
        MessagePageReqVO messagePageReqVO = new MessagePageReqVO();
        messagePageReqVO.setContent(notice.getNoticeContent());
        messagePageReqVO.setTitle(notice.getNoticeTitle());
        messagePageReqVO.setEntityType(Integer.valueOf(notice.getNoticeType()));
        messagePageReqVO.setCreateTime(new Date());
        webSocketMessageServer.broadcastMessage(messagePageReqVO);

        MessageDO messageDO = new MessageDO();
        // Set template basic data
        messageDO.setCategory(Integer.valueOf(0));
        messageDO.setMsgLevel(Integer.valueOf(0));
        messageDO.setTitle("Test");
        // Actual message
        messageDO.setContent("Test content");

//        messageDO.setCreatorId(getLoginUser().getUserId());
//        messageDO.setCreateBy(getLoginUser().getUser().getNickName());
        boolean save = messageService.save(messageDO);

        return toAjax(noticeService.updateNotice(notice));
    }

    /**
     * Delete notice/announcement
     */
    @PreAuthorize("@ss.hasPermi('system:notice:remove')")
    @Log(title = "log.op.title.system.notice", businessType = BusinessType.DELETE)
    @DeleteMapping("/{noticeIds}")
    public AjaxResult remove(@PathVariable Long[] noticeIds)
    {
        return toAjax(noticeService.deleteNoticeByIds(noticeIds));
    }
}
