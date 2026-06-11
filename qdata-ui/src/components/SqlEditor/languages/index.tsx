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

/*
 *
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

import { CustomEditorLanguage } from '@/components/CustomEditor/languages/constants';
import { FlinkSQLLanguage } from '@/components/CustomEditor/languages/flinksql';
import { LogLanguage } from '@/components/CustomEditor/languages/javalog';
import { Monaco } from '@monaco-editor/react';

/**
 * 避免重复加载语言, 通过获取到 language 的 id 来判断是否已经加载过
 * @param monacoLanguages
 * @param language
 */
function canLoadLanguage(monacoLanguages: Monaco['languages'] | undefined, language: string) {
  return !monacoLanguages?.getEncodedLanguageId(language);
}

/**
 * 加载自定义语言
 * @param monacoLanguages
 * @param monacoEditor
 * @param registerCompletion 是否注册自动补全 (默认不注册)
 * @constructor
 */
export function LoadCustomEditorLanguage(
  monacoLanguages?: Monaco['languages'] | undefined,
  monacoEditor?: Monaco['editor'] | undefined,
  registerCompletion: boolean = false
) {
  if (canLoadLanguage(monacoLanguages, CustomEditorLanguage.FlinkSQL)) {
    FlinkSQLLanguage(monacoLanguages, monacoEditor, registerCompletion);
  }
  if (canLoadLanguage(monacoLanguages, CustomEditorLanguage.JavaLog)) {
    LogLanguage(monacoLanguages);
  }
}
