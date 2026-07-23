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
 * Get the tag value (call the function to get the translation string)
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
 * register editor key binding | register editor shortcut key
 *
 * @param editorInstance
 */
function registerEditorKeyBinding(editorInstance) {
  // Add ctrl + z to undo
  editorInstance?.addCommand(KeyMod.CtrlCmd | KeyCode.KeyZ, () => {
    editorInstance?.trigger("anyString", "undo", "");
  });
  // Add ctrl + y to restore
  editorInstance?.addCommand(KeyMod.CtrlCmd | KeyCode.KeyY, () => {
    editorInstance?.trigger("anyString", "redo", "");
  });
  // Format all code
  editorInstance?.addCommand(KeyMod.Alt | KeyCode.Digit3, () => {
    editorInstance?.trigger("anyString", "editor.action.formatDocument", "");
    editorInstance?.setValue(format(editorInstance?.getValue()));
  });
  // Format selection
  editorInstance?.addCommand(KeyMod.Alt | KeyCode.Digit4, () => {
    editorInstance?.trigger("anyString", "editor.action.formatSelection", "");
    editorInstance?.setValue(format(editorInstance?.getValue()));
  });
}

/**
 * <p> If sql-formatter is used, the following parameters can be used </p> <br/>
 * language: the SQL dialect to use (when using format()). | The SQL dialect to use (when using format()) <br/>
 * dialect: the SQL dialect to use (when using formatDialect() since version 12).
 * tabWidth: amount of indentation to use. | The amount of indentation to use<br/>
 * useTabs: to use tabs for indentation. | To use tabs for indentation<br/>
 * keywordCase: uppercases or lowercases keywords. | Keyword case<br/>
 * identifierCase: uppercases or lowercases identifiers. (experimental!) | Identifier case<br/>
 * indentStyle: defines overall indentation style.| Overall indentation style<br/>
 * logicalOperatorNewline: newline before or after boolean operator (AND, OR, XOR). | Newline position of Boolean operator (AND, OR, XOR)<br/>
 * expressionWidth: maximum number of characters in parenthesized expressions to be kept on single line. | Maximum number of characters kept on a line in parenthesized expressions<br/>
 * linesBetweenQueries: how many newlines to insert between queries.
 * denseOperators: packs operators densely without spaces. | Densely packs operators without spaces<br/>
 * newlineBeforeSemicolon: places semicolon on separate line. | Place semicolon on separate line <br/>
 * params: collection of values for placeholder replacement. | Collection of values for placeholder replacement<br/>
 * paramTypes: specifies parameter placeholders types to support | Specifies parameter placeholder types to support <br/>
 * register editor action
 * @param editorInstance editor instance
 */
function registerEditorAction(editorInstance) {
  // Format all code Add to context menu | format document
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
  // Format selection Add to context menu | format selection
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
  // Comment this line Add to context menu | comment line
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
  // to uppercase add to right-click menu | to uppercase
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
  // To lowercase Add to right-click menu | to lowercase
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
 * register editor key binding and action | Register editor shortcut keys and right-click menu
 * @param editorInstance
 */
export function registerEditorKeyBindingAndAction(editorInstance) {
  registerEditorKeyBinding(editorInstance);
  registerEditorAction(editorInstance);
}
