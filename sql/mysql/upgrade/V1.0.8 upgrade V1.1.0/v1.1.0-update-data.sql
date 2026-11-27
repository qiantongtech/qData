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
UPDATE qdata.att_clean_rule	SET valid_flag='1' WHERE code='008';
UPDATE qdata.att_clean_rule	SET valid_flag='1' WHERE code='011';
UPDATE qdata.att_clean_rule	SET valid_flag='1', strategy_key='STRING_SUBSTR' WHERE code='012';
UPDATE qdata.att_clean_rule	SET valid_flag='1' WHERE code='021';
UPDATE qdata.att_clean_rule	SET valid_flag='1' WHERE code='039';

-- 开启稽查规则
UPDATE qdata.att_audit_rule SET valid_flag='1' WHERE code in('306', '108', '111');
