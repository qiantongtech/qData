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

-- 新增字典
INSERT INTO `qdata`.`system_dict_data` VALUES (344, 8, 'Doris', 'Doris', 'datasource_type', NULL, 'info', 'N', '0', '吴同', '2025-06-23 11:22:50', 'wutong', '2025-09-22 09:19:40', NULL);

-- 修改稽查规则状态
UPDATE `qdata`.`att_audit_rule` SET valid_flag='1' WHERE code in('205', '115');
