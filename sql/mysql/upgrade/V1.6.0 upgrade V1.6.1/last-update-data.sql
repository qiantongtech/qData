-- 清理已移除菜单的角色授权
DELETE FROM `system_role_menu` WHERE `menu_id` IN (2510, 2530, 2537);

-- 调整数据集成、数据开发菜单层级和展示信息
UPDATE `system_menu`
SET `parent_id` = 2397,
    `order_num` = 2,
    `path` = 'task/integratioTask',
    `icon` = 'wh'
WHERE `menu_id` = 2426;

UPDATE `system_menu`
SET `parent_id` = 2397,
    `order_num` = 3,
    `path` = 'task/developTask',
    `icon` = 'code-box-line'
WHERE `menu_id` = 2511;

-- 移除任务管理、资产审核及项目资产菜单
DELETE FROM `system_menu`
WHERE `menu_id` = 2510
   OR `menu_id` BETWEEN 2530 AND 2543;

-- 新增标准代码表菜单及操作权限
INSERT INTO `system_menu` (`menu_id`,`menu_name`,`parent_id`,`order_num`,`path`,`component`,`query`,`is_frame`,`is_cache`,`route_name`,`menu_type`,`visible`,`status`,`perms`,`icon`,`create_by`,`create_time`,`update_by`,`update_time`,`remark`) VALUES (2771,'标准代码表',2694,9,'dataCodeTable','dp/dataCodeTable/index',NULL,1,0,NULL,'C','0','0','dp:dataElem:list','build','qData','2025-01-21 14:53:06.000000','admin','2026-03-27 16:52:05.000000','数据元菜单');
INSERT INTO `system_menu` (`menu_id`,`menu_name`,`parent_id`,`order_num`,`path`,`component`,`query`,`is_frame`,`is_cache`,`route_name`,`menu_type`,`visible`,`status`,`perms`,`icon`,`create_by`,`create_time`,`update_by`,`update_time`,`remark`) VALUES (2772,'数据元查询',2771,1,'#','',NULL,1,0,NULL,'F','0','0','dp:dataElem:query','#','qData','2025-01-21 14:54:20.000000','admin','2025-09-30 10:11:16.000000','');
INSERT INTO `system_menu` (`menu_id`,`menu_name`,`parent_id`,`order_num`,`path`,`component`,`query`,`is_frame`,`is_cache`,`route_name`,`menu_type`,`visible`,`status`,`perms`,`icon`,`create_by`,`create_time`,`update_by`,`update_time`,`remark`) VALUES (2773,'数据元新增',2771,2,'#','',NULL,1,0,NULL,'F','0','0','dp:dataElem:add','#','qData','2025-01-21 14:54:20.000000','admin','2025-09-30 10:11:34.000000','');
INSERT INTO `system_menu` (`menu_id`,`menu_name`,`parent_id`,`order_num`,`path`,`component`,`query`,`is_frame`,`is_cache`,`route_name`,`menu_type`,`visible`,`status`,`perms`,`icon`,`create_by`,`create_time`,`update_by`,`update_time`,`remark`) VALUES (2774,'数据元修改',2771,3,'#','',NULL,1,0,NULL,'F','0','0','dp:dataElem:edit','#','qData','2025-01-21 14:54:21.000000','admin','2025-09-30 10:11:39.000000','');
INSERT INTO `system_menu` (`menu_id`,`menu_name`,`parent_id`,`order_num`,`path`,`component`,`query`,`is_frame`,`is_cache`,`route_name`,`menu_type`,`visible`,`status`,`perms`,`icon`,`create_by`,`create_time`,`update_by`,`update_time`,`remark`) VALUES (2775,'数据元删除',2771,4,'#','',NULL,1,0,NULL,'F','0','0','dp:dataElem:remove','#','qData','2025-01-21 14:54:21.000000','admin','2025-09-30 10:11:43.000000','');
INSERT INTO `system_menu` (`menu_id`,`menu_name`,`parent_id`,`order_num`,`path`,`component`,`query`,`is_frame`,`is_cache`,`route_name`,`menu_type`,`visible`,`status`,`perms`,`icon`,`create_by`,`create_time`,`update_by`,`update_time`,`remark`) VALUES (2776,'数据元导出',2771,5,'#','',NULL,1,0,NULL,'F','0','0','dp:dataElem:export','#','qData','2025-01-21 14:54:21.000000','admin','2025-09-30 10:11:52.000000','');
INSERT INTO `system_menu` (`menu_id`,`menu_name`,`parent_id`,`order_num`,`path`,`component`,`query`,`is_frame`,`is_cache`,`route_name`,`menu_type`,`visible`,`status`,`perms`,`icon`,`create_by`,`create_time`,`update_by`,`update_time`,`remark`) VALUES (2777,'数据元导入',2771,6,'#','',NULL,1,0,NULL,'F','0','0','dp:dataElem:import','#','qData','2025-01-21 14:54:21.000000','admin','2025-09-30 10:11:57.000000','');

-- 调整清洗规则备注、描述
UPDATE `att_clean_rule`
SET `description` = '通过正则表达式匹配符合特定规则的文本，并将其批量替换为指定内容',
    `example` = '使用 (\d{4})/(\d{2})/(\d{2}) 匹配 2026/08/18，替换为 $1-$2-$3 后得到 2026-08-18',
    `use_case` = '批量统一文本中的日期格式，提高数据处理效率'
WHERE `id` = 11;