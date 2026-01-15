
INSERT INTO qdata.system_dict_data VALUES(413,5,'SQL Server2008','SQL_Server2008','datasource_type',null,'info','N','0','admin','2025-11-21 19:40:47','admin','2025-11-21 19:41:13',null);


--清洗规则
UPDATE `qdata`.`att_clean_rule` SET valid_flag='1' WHERE code='022';
UPDATE  `qdata`.`att_clean_rule` SET valid_flag='1' WHERE code='009';
UPDATE  `qdata`.`att_clean_rule` SET valid_flag='1' WHERE code='007';

UPDATE `qdata`.`att_clean_rule`  SET strategy_key='VALUE_REPLACE' WHERE id=2;
UPDATE `qdata`.`att_clean_rule`  SET strategy_key='PHONE_FORMAT_STD' WHERE id=6;
UPDATE `qdata`.`att_clean_rule`  SET strategy_key='DATE_FORMAT_STD' WHERE id=7;
UPDATE `qdata`.`att_clean_rule`  SET strategy_key='DATE_FILL_NULL' WHERE id=17;
UPDATE `qdata`.`att_clean_rule`  SET strategy_key='PK_DEDUP_FIRST' WHERE id=25;
