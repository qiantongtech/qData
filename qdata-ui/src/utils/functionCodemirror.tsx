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
 * For brand customization, please apply for brand customization authorization via official channels.
 *  *
 * More information: https://qdata.qiantong.tech/business.html
 */

import { editor, KeyCode, KeyMod } from "monaco-editor";
import { format } from "sql-formatter";
import { i18n } from "@/plugins/vueI18n";

const labels = {
  "shortcut.title": () => i18n.global.t('common.shortcut.title'),
  "shortcut.key.save": () => i18n.global.t('common.shortcut.save'),
  "shortcut.key.check": () => i18n.global.t('common.shortcut.check'),
  "shortcut.key.format": () => i18n.global.t('common.shortcut.format'),
  "shortcut.key.formatSelection": () => i18n.global.t('common.shortcut.formatSelection'),
  "shortcut.key.notes": () => i18n.global.t('common.shortcut.notes'),
  "shortcut.key.upperCase": () => i18n.global.t('common.shortcut.upperCase'),
  "shortcut.key.lowerCase": () => i18n.global.t('common.shortcut.lowerCase'),
  "shortcut.key.query": () => i18n.global.t('common.shortcut.query'),
};

/**
 * 获取标签值（调用函数获取翻译字符串）
 */
function getLabel(key) {
  const labelFunc = labels[key];
  if (typeof labelFunc === 'function') {
    try {
      return labelFunc();
    } catch (e) {
      console.warn(`Failed to get label for key: ${key}`, e);
      return key;
    }
  }
  return labelFunc || key;
}

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
    label: getLabel("shortcut.key.format"),
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
    label: getLabel("shortcut.key.formatSelection"),
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
    label: getLabel("shortcut.key.notes"),
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
    label: getLabel("shortcut.key.upperCase"),
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
    label: getLabel("shortcut.key.lowerCase"),
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
    label: getLabel("shortcut.key.query"),
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
