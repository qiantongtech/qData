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

import { CustomEditorLanguage } from "../constants";
import { buildFlinkSQLConfiguration, buildMonarchTokensProvider, registerFlinkSQLCompilation } from "./function";

export function FlinkSQLLanguage(monacoLanguages, monacoEditor, registerCompletion) {
  // Register a new language
  monacoLanguages?.register({
    id: CustomEditorLanguage.FlinkSQL,
    extensions: [".sql"],
    mimetypes: ["text/x-flinksql", "text/x-flinksql", "text/x-flinksql", "text/flinksql"],
    aliases: ["flinksql", "fsql", "flinkSQL", "FlinkSQL"],
  });
  buildMonarchTokensProvider(monacoLanguages);

  // Register a completion item provider for the new language
  if (registerCompletion) {
    registerFlinkSQLCompilation(monacoLanguages);
  }
  buildFlinkSQLConfiguration(monacoLanguages);

  monacoLanguages?.onLanguageEncountered(CustomEditorLanguage.FlinkSQL, () => {
    monacoEditor?.getModels().forEach((model) => {
      model.onDidChangeLanguage(() => {
        if (model.getLanguageId() === CustomEditorLanguage.FlinkSQL) {
          buildFlinkSQLConfiguration(monacoLanguages);
        }
      });
    });
    buildMonarchTokensProvider(monacoLanguages);
  });
}
