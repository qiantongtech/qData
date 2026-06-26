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
 * register editor key binding | 注册编辑器快捷键
 *
 * @param editorInstance
 */
function registerEditorKeyBinding(editorInstance) {
  // 添加 ctrl + z 撤销
  editorInstance?.addCommand(KeyMod.CtrlCmd | KeyCode.KeyZ, () => {
    editorInstance?.trigger("anyString", "undo", "");
  });
  // 添加 ctrl + y 恢复
  editorInstance?.addCommand(KeyMod.CtrlCmd | KeyCode.KeyY, () => {
    editorInstance?.trigger("anyString", "redo", "");
  });
  // 格式化所有代码
  editorInstance?.addCommand(KeyMod.Alt | KeyCode.Digit3, () => {
    editorInstance?.trigger("anyString", "editor.action.formatDocument", "");
    editorInstance?.setValue(format(editorInstance?.getValue()));
  });
  // 格式化选定内容
  editorInstance?.addCommand(KeyMod.Alt | KeyCode.Digit4, () => {
    editorInstance?.trigger("anyString", "editor.action.formatSelection", "");
    editorInstance?.setValue(format(editorInstance?.getValue()));
  });
}

/**
 * <p> 如果使用了 sql-formatter , 可以使用以下参数 </p> <br/>
 * language: the SQL dialect to use (when using format()). | 要使用的SQL方言（当使用format（）时） <br/>
 * dialect: the SQL dialect to use (when using formatDialect() since version 12). | 要使用的SQL方言（自版本12起）<br/>
 * tabWidth: amount of indentation to use. | 要使用的缩进量<br/>
 * useTabs: to use tabs for indentation. | 要使用制表符进行缩进<br/>
 * keywordCase: uppercases or lowercases keywords. | 关键字大小写<br/>
 * identifierCase: uppercases or lowercases identifiers. (experimental!) | 标识符大小写<br/>
 * indentStyle: defines overall indentation style.| 总体缩进样式<br/>
 * logicalOperatorNewline: newline before or after boolean operator (AND, OR, XOR). | 布尔运算符（AND，OR，XOR）的换行位置<br/>
 * expressionWidth: maximum number of characters in parenthesized expressions to be kept on single line. | 带括号的表达式中保持在一行的最大字符数<br/>
 * linesBetweenQueries: how many newlines to insert between queries. | 在查询之间插入的换行数<br/>
 * denseOperators: packs operators densely without spaces. | 密集地封装运算符，没有空格<br/>
 * newlineBeforeSemicolon: places semicolon on separate line. | 将分号放在单独的行上 <br/>
 * params: collection of values for placeholder replacement. | 占位符替换的值的集合<br/>
 * paramTypes: specifies parameter placeholders types to support | 指定要支持的参数占位符类型 <br/>
 * register editor action
 * @param editorInstance editor instance
 */
function registerEditorAction(editorInstance) {
  // 格式化所有代码 添加到 右键菜单 | format document
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
  // 格式化选定内容 添加到 右键菜单 | format selection
  editorInstance?.addAction({
    id: "formatSelection",
    label: getLabel("shortcut.key.formatSelection"),
    keybindings: [KeyMod.CtrlCmd | KeyCode.Digit4],
    contextMenuGroupId: "custom",
    contextMenuOrder: 1.5,
    run: () => {
      editorInstance?.trigger("anyString", "editor.action.formatSelection", "");
      editorInstance?.setValue(format(editorInstance?.getValue(), { language: "spark" }));
    },
  });
  // 注释该行 添加到 右键菜单 | comment line
  editorInstance?.addAction({
    id: "commentLine",
    label: getLabel("shortcut.key.notes"),
    keybindings: [KeyMod.CtrlCmd | KeyCode.Slash],
    contextMenuGroupId: "custom",
    contextMenuOrder: 1.5,
    run: () => {
      editorInstance?.trigger("anyString", "editor.action.commentLine", "");
    },
  });
  // 转为 大写 添加到 右键菜单 | to uppercase
  editorInstance?.addAction({
    id: "upperCase",
    label: getLabel("shortcut.key.upperCase"),
    keybindings: [KeyMod.CtrlCmd | KeyCode.KeyU],
    contextMenuGroupId: "custom",
    contextMenuOrder: 1.5,
    run: () => {
      editorInstance?.trigger("anyString", "editor.action.transformToUppercase", "");
    },
  });
  // 转为 小写 添加到 右键菜单 | to lowercase
  editorInstance?.addAction({
    id: "lowerCase",
    label: getLabel("shortcut.key.lowerCase"),
    keybindings: [KeyMod.CtrlCmd | KeyCode.KeyL],
    contextMenuGroupId: "custom",
    contextMenuOrder: 1.5,
    run: () => {
      editorInstance?.trigger("anyString", "editor.action.transformToLowercase", "");
    },
  });
}

/**
 * register editor key binding and action | 注册编辑器快捷键和右键菜单
 * @param editorInstance
 */
export function registerEditorKeyBindingAndAction(editorInstance) {
  registerEditorKeyBinding(editorInstance);
  registerEditorAction(editorInstance);
}
