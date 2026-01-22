-- 新增字典
INSERT INTO `qdata`.`system_dict_data` VALUES (344, 8, 'Doris', 'Doris', 'datasource_type', NULL, 'info', 'N', '0', '吴同', '2025-06-23 11:22:50', 'wutong', '2025-09-22 09:19:40', NULL);

-- 修改稽查规则状态
UPDATE `qdata`.`att_audit_rule` SET valid_flag='1' WHERE code in('205', '115');
