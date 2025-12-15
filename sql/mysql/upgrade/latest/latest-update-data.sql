UPDATE qdata.att_clean_rule	SET valid_flag='1' WHERE code='008';
UPDATE qdata.att_clean_rule	SET valid_flag='1' WHERE code='011';
UPDATE qdata.att_clean_rule	SET valid_flag='1', strategy_key='STRING_SUBSTR' WHERE code='012';
UPDATE qdata.att_clean_rule	SET valid_flag='1' WHERE code='021';
UPDATE qdata.att_clean_rule	SET valid_flag='1' WHERE code='039';

UPDATE qdata.att_audit_rule SET valid_flag='1' WHERE code in('306', '108', '111');
