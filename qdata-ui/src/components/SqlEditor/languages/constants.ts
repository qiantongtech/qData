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

export const TokenClassConsts = {
  BINARY: 'binary',
  BINARY_ESCAPE: 'binary.escape',
  COMMENT: 'comment',
  COMMENT_QUOTE: 'comment.quote',
  DELIMITER: 'delimiter',
  DELIMITER_CURLY: 'delimiter.curly',
  DELIMITER_PAREN: 'delimiter.paren',
  DELIMITER_SQUARE: 'delimiter.square',
  IDENTIFIER: 'identifier',
  IDENTIFIER_QUOTE: 'identifier.quote',
  KEYWORD: 'keyword',
  KEYWORD_SCOPE: 'keyword.scope',
  NUMBER: 'number',
  NUMBER_FLOAT: 'number.float',
  NUMBER_BINARY: 'number.binary',
  NUMBER_OCTAL: 'number.octal',
  NUMBER_HEX: 'number.hex',
  OPERATOR: 'operators',
  OPERATOR_KEYWORD: 'operators.keyword',
  OPERATOR_SYMBOL: 'operators.symbol',
  PREDEFINED: 'predefined',
  STRING: 'string',
  STRING_ESCAPE: 'string.escape',
  STRING_ESCAPE_CHAR: 'string.escape.char',
  TYPE: 'type',
  VARIABLE: 'variable',
  WHITE: 'white'
};

export enum CustomEditorLanguage {
  JavaLog = 'javalog',
  FlinkSQL = 'flinksql'
}

// EditorLanguage
