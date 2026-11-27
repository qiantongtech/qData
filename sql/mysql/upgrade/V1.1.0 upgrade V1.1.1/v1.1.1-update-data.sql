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

INSERT INTO qdata.system_dict_data VALUES(413,5,'SQL Server 2008','SQL_Server2008','datasource_type',null,'info','N','0','admin','2025-11-21 19:40:47','admin','2025-11-21 19:41:13',null);


-- 清洗规则
UPDATE `qdata`.`att_clean_rule` SET valid_flag='1' WHERE code='022';
UPDATE  `qdata`.`att_clean_rule` SET valid_flag='1' WHERE code='009';
UPDATE  `qdata`.`att_clean_rule` SET valid_flag='1' WHERE code='007';

UPDATE `qdata`.`att_clean_rule`  SET strategy_key='VALUE_REPLACE' WHERE id=2;
UPDATE `qdata`.`att_clean_rule`  SET strategy_key='PHONE_FORMAT_STD' WHERE id=6;
UPDATE `qdata`.`att_clean_rule`  SET strategy_key='DATE_FORMAT_STD' WHERE id=7;
UPDATE `qdata`.`att_clean_rule`  SET strategy_key='DATE_FILL_NULL' WHERE id=17;
UPDATE `qdata`.`att_clean_rule`  SET strategy_key='PK_DEDUP_FIRST' WHERE id=25;
