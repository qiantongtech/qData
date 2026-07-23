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

import { CustomEditorLanguage } from '../constants';
import { JAVA_LOG_KEYWORD } from './keyword';

export function buildMonarchTokensProvider(monacoLanguages) {
  monacoLanguages?.setMonarchTokensProvider(CustomEditorLanguage.JavaLog, {
    defaultToken: '',
    tokenPostfix: '.log',
    keywords: JAVA_LOG_KEYWORD,
    operators: [],
    ignoreCase: true, // Ignore case
    builtinFunctions: [],
    builtinVariables: [],
    typeKeywords: [],
    scopeKeywords: [],
    pseudoColumns: [],
    brackets: [
      { open: '{', close: '}', token: 'delimiter.curly' },
      { open: '[', close: ']', token: 'delimiter.bracket' },
      { open: '(', close: ')', token: 'delimiter.parenthesis' },
      { open: '<', close: '>', token: 'delimiter.angle' }
    ],
    autoClosingPairs: [
      { open: '{', close: '}' },
      { open: '[', close: ']' },
      { open: '(', close: ')' },
      { open: '"', close: '"' },
      { open: "'", close: "'" },
      { open: '`', close: '`' },
      { open: '<', close: '>' }
    ],
    surroundingPairs: [
      { open: '{', close: '}' },
      { open: '[', close: ']' },
      { open: '(', close: ')' },
      { open: '"', close: '"' },
      { open: "'", close: "'" },
      { open: '`', close: '`' },
      { open: '<', close: '>' }
    ],
    folding: {
      markers: {
        start:
          /((CREATE|ALERT|DROP|USE\s+)?(TABLE|DATABASE|STREAM|FUNCTION|CATALOG|SCHEMA|VIEW)\b)|((EXECUTE\s+)?(JAR|CDCSOURCE)?\b)/i,
        end: /\)\\;\b/i
      }
    },
    tokenizer: {
      root: [
        // Case insensitive by default //
        [/\[(\w*-\d*)+\]/, 'custom-thread'],
        [/(\w+(\.))+(\w+)(\(\d+\))?(:){1}/, 'custom-class'],
        [/(\w+(\.))+(\w+)(\(\d+\))?\s+/, 'custom-class'],
        [/error/, 'custom-error'],
        [/warring/, 'custom-warning'],
        [/warn/, 'custom-warning'],
        [/info/, 'custom-info'],
        [
          /^[1-9]\d{3}-(0[1-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1])\s+(20|21|22|23|[0-1]\d):[0-5]\d:[0-5]\d\.\d{3}/,
          'custom-date'
        ],
        [
          /[1-9]\d{3}-(0[1-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1])\s+(20|21|22|23|[0-1]\d):[0-5]\d:[0-5]\d\s(CST)/,
          'custom-date'
        ]
      ]
    },
    unicode: true
  });
}
