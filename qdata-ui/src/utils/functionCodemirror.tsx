/*
 * Copyright © 2025 Qiantong Technology Co., Ltd.
 * qData Data Middle Platform (Open Source Edition)
 *  *
 * License:
 * Released under the Apache License, Version 2.0.
 * You may use, modify, and distribute this software for commercial purposes
 * under the terms of the License.
 *  *
 * Special Notice:
 * All derivative versions are strictly prohibited from modifying or removing
 * the default system logo and copyright information.
 * For brand customization, please contact the official team for authorization.
 *  *
 * More information: https://qdata.qiantong.tech/business.html
 *  *
 * ============================================================================
 *  *
 * 版权所有 © 2025 江苏千桐科技有限公司
 * qData 数据中台（开源版）
 *  *
 * 许可协议：
 * 本项目基于 Apache License 2.0 开源协议发布，
 * 允许在遵守协议的前提下进行商用、修改和分发。
 *  *
 * 特别说明：
 * 所有衍生版本不得修改或移除系统默认的 LOGO 和版权信息；
 * 如需定制品牌，请通过官方渠道申请品牌授权。
 *  *
 * 更多信息请访问：https://qdata.qiantong.tech/business.html
 */

import { editor, KeyCode, KeyMod } from "monaco-editor";
import { format } from "sql-formatter";

const labels = {
  "shortcut.title": "快捷键",
  "shortcut.key.save": "保存",
  "shortcut.key.check": "校验",
  "shortcut.key.format": "格式化",
  "shortcut.key.formatSelection": "格式化选中内容",
  "shortcut.key.notes": "注释/取消注释该行(区域)",
  "shortcut.key.upperCase": "转为大写",
  "shortcut.key.lowerCase": "转为小写",
  "shortcut.key.query": "查询",
};

/**
 * 注册编辑器快捷键
 */
function registerEditorKeyBinding(editorInstance) {
  // 撤销
  editorInstance?.addCommand(KeyMod.CtrlCmd | KeyCode.KeyZ, () => {
    editorInstance?.trigger("anyString", "undo", "");
  });
  // 恢复
  editorInstance?.addCommand(KeyMod.CtrlCmd | KeyCode.KeyY, () => {
    editorInstance?.trigger("anyString", "redo", "");
  });
  // 格式化所有
  editorInstance?.addCommand(KeyMod.Alt | KeyCode.Digit3, () => {
    editorInstance?.trigger("anyString", "editor.action.formatDocument", "");
    editorInstance?.setValue(format(editorInstance?.getValue()));
  });
  // 格式化选中
  editorInstance?.addCommand(KeyMod.Alt | KeyCode.Digit4, () => {
    editorInstance?.trigger("anyString", "editor.action.formatSelection", "");
    editorInstance?.setValue(format(editorInstance?.getValue()));
  });
}

/**
 * 注册右键菜单 & 其他功能
 */
function registerEditorAction(editorInstance) {
  // 格式化所有
  editorInstance?.addAction({
    id: "format",
    label: labels["shortcut.key.format"],
    keybindings: [KeyMod.CtrlCmd | KeyMod.Alt | KeyCode.KeyL],
    contextMenuGroupId: "custom",
    contextMenuOrder: 1.5,
    run: () => {
      editorInstance?.trigger("anyString", "editor.action.formatDocument", "");
      editorInstance?.setValue(format(editorInstance?.getValue(), { language: "spark" }));
    },
  });

  // 格式化选中
  editorInstance?.addAction({
    id: "formatSelection",
    label: labels["shortcut.key.formatSelection"],
    keybindings: [KeyMod.CtrlCmd | KeyCode.Digit4],
    contextMenuGroupId: "custom",
    contextMenuOrder: 1.6,
    run: () => {
      editorInstance?.trigger("anyString", "editor.action.formatSelection", "");
      editorInstance?.setValue(format(editorInstance?.getValue(), { language: "spark" }));
    },
  });

  // 注释
  editorInstance?.addAction({
    id: "commentLine",
    label: labels["shortcut.key.notes"],
    keybindings: [KeyMod.CtrlCmd | KeyCode.Slash],
    contextMenuGroupId: "custom",
    contextMenuOrder: 1.7,
    run: () => {
      editorInstance?.trigger("anyString", "editor.action.commentLine", "");
    },
  });

  // 大写
  editorInstance?.addAction({
    id: "upperCase",
    label: labels["shortcut.key.upperCase"],
    keybindings: [KeyMod.CtrlCmd | KeyCode.KeyU],
    contextMenuGroupId: "custom",
    contextMenuOrder: 1.8,
    run: () => {
      editorInstance?.trigger("anyString", "editor.action.transformToUppercase", "");
    },
  });

  // 小写
  editorInstance?.addAction({
    id: "lowerCase",
    label: labels["shortcut.key.lowerCase"],
    keybindings: [KeyMod.CtrlCmd | KeyCode.KeyL],
    contextMenuGroupId: "custom",
    contextMenuOrder: 1.9,
    run: () => {
      editorInstance?.trigger("anyString", "editor.action.transformToLowercase", "");
    },
  });

  // 保留查询右键菜单（可点击触发），但不注册快捷键
  editorInstance?.addAction({
    id: "query",
    label: labels["shortcut.key.query"],
    contextMenuGroupId: "custom",
    contextMenuOrder: 2.0,
    run: () => {
      const domNode = editorInstance.getDomNode();
      if (domNode) {
        const selection = editorInstance.getSelection();
        let selectedText = "";
        if (selection && !selection.isEmpty()) {
          selectedText = editorInstance.getModel().getValueInRange(selection);
        } else {
          selectedText = editorInstance.getValue();
        }

        const event = new CustomEvent("editor-query", {
          bubbles: true,
          detail: { value: selectedText },
        });
        domNode.dispatchEvent(event);
      } else {
        console.warn("Monaco editor DOM 节点未找到，查询事件无法触发");
      }
    },
  });
}

/**
 * 注册快捷键和右键菜单
 */
export function registerEditorKeyBindingAndAction(editorInstance) {
  registerEditorKeyBinding(editorInstance);
  registerEditorAction(editorInstance);
}
