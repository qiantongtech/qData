-- Copyright © 2025-present Jiangsu Qiantong Technology Co., Ltd.
--
-- This file is part of qData Data Middle Platform (Open Source Edition).
--
-- qData is licensed under Apache License 2.0 with additional qData terms.
-- You may use qData for commercial purposes, but you may not remove, hide,
-- modify, or replace the qData logo, copyright notices, license notices,
-- or attribution information without a separate commercial license.
--
-- White-label use, OEM distribution, rebranding, or presenting qData as
-- another product requires separate commercial authorization from
-- Jiangsu Qiantong Technology Co., Ltd.
--
-- Business License: https://docs.qdata.tech/business/policy.html
-- See the LICENSE file in the project root for full license information.

-- 开启清洗规则
UPDATE "QDATA"."ATT_CLEAN_RULE"	SET VALID_FLAG='1' WHERE CODE='008';
UPDATE "QDATA"."ATT_CLEAN_RULE"	SET VALID_FLAG='1' WHERE CODE='011';
UPDATE "QDATA"."ATT_CLEAN_RULE"	SET VALID_FLAG='1', STRATEGY_KEY='STRING_SUBSTR' WHERE CODE='012';
UPDATE "QDATA"."ATT_CLEAN_RULE"	SET VALID_FLAG='1' WHERE CODE='021';
UPDATE "QDATA"."ATT_CLEAN_RULE"	SET VALID_FLAG='1' WHERE CODE='039';

-- 开启稽查规则
UPDATE "QDATA"."ATT_AUDIT_RULE" SET VALID_FLAG='1' WHERE CODE in('306', '108', '111');