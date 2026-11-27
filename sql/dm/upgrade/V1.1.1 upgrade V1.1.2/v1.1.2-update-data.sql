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

--更新字典状态
UPDATE "QDATA"."SYSTEM_DICT_DATA" SET STATUS='0' WHERE DICT_CODE=344;

-- 更新稽查规则状态
UPDATE "QDATA"."ATT_AUDIT_RULE" SET VALID_FLAG='1' WHERE CODE in('205', '115');
