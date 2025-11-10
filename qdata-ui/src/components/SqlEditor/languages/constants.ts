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
