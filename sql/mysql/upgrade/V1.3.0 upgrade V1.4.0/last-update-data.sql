-- 字典类型新增
INSERT INTO `system_dict_type`
(`dict_id`, `dict_name`, `dict_type`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`)
VALUES(111, '表类型', 'table_type', '0', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `system_dict_type`
(`dict_id`, `dict_name`, `dict_type`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`)
VALUES(112, '表名-大小写', 'table_name_case', '0', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `system_dict_type`
(`dict_id`, `dict_name`, `dict_type`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`)
VALUES(113, '元数据审核状态', 'meta_audit_status', '0', 'admin', '2026-04-23 09:52:37', NULL, NULL, NULL);
INSERT INTO `system_dict_type`
(`dict_id`, `dict_name`, `dict_type`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`)
VALUES(114, '元数据任务状态', 'meta_task_status', '0', 'admin', '2026-04-21 10:23:23', NULL, NULL, NULL);
INSERT INTO `system_dict_type`
(`dict_id`, `dict_name`, `dict_type`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`)
VALUES(115, '是否主表', 'table_yes_no', '0', 'admin', '2026-04-21 10:23:01', NULL, NULL, NULL);
INSERT INTO `system_dict_type`
(`dict_id`, `dict_name`, `dict_type`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`)
VALUES(116, '元数据采集方式', 'mc_collect_mode', '0', 'admin', '2026-04-21 10:08:10', NULL, NULL, NULL);
INSERT INTO `system_dict_type`
(`dict_id`, `dict_name`, `dict_type`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`)
VALUES(117, '采集范围', 'mc_collect_scope', '0', 'admin', '2026-04-21 10:07:04', NULL, NULL, NULL);
INSERT INTO `system_dict_type`
(`dict_id`, `dict_name`, `dict_type`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`)
VALUES (118,'采集任务实例状态','mc_task_instance_status','0','admin','2026-04-21 10:07:21',null,null,null);


-- 字典值新增
INSERT INTO `system_dict_data`
(`dict_code`, `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`)
VALUES(421, 6, '报表应用', '6', 'sys_source_system_type', NULL, 'primary', 'N', '0', 'admin', '2026-04-29 16:45:18', NULL, NULL, NULL);
INSERT INTO `system_dict_data`
(`dict_code`, `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`)
VALUES(422, 5, '数据服务（API）', '5', 'sys_source_system_type', NULL, 'primary', 'N', '0', 'admin', '2026-04-29 16:45:04', NULL, NULL, NULL);
INSERT INTO `system_dict_data`
(`dict_code`, `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`)
VALUES(423, 4, '数据应用层', '4', 'sys_source_system_type', NULL, 'primary', 'N', '0', 'admin', '2026-04-29 16:44:37', NULL, NULL, NULL);
INSERT INTO `system_dict_data`
(`dict_code`, `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`)
VALUES(424, 3, '数据加工层', '3', 'sys_source_system_type', NULL, 'primary', 'N', '0', 'admin', '2026-04-29 16:44:12', 'admin', '2026-04-29 16:44:24', NULL);
INSERT INTO `system_dict_data`
(`dict_code`, `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`)
VALUES(426, 2, '数据采集层', '2', 'sys_source_system_type', NULL, 'primary', 'N', '0', 'admin', '2026-04-29 16:44:12', 'admin', '2026-04-29 16:44:24', NULL);
INSERT INTO `system_dict_data`
(`dict_code`, `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`)
VALUES(427, 1, '业务系统', '1', 'sys_source_system_type', NULL, 'primary', 'N', '0', 'admin', '2026-04-29 16:44:12', 'admin', '2026-04-29 16:44:24', NULL);
INSERT INTO `system_dict_data`
(`dict_code`, `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`)
VALUES(425, 0, '应用表', '4', 'table_type', NULL, NULL, 'N', '0', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `system_dict_data`
(`dict_code`, `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`)
VALUES(454, 0, '维度表', '3', 'table_type', NULL, NULL, 'N', '0', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `system_dict_data`
(`dict_code`, `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`)
VALUES(453, 0, '汇总表', '2', 'table_type', NULL, NULL, 'N', '0', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `system_dict_data`
(`dict_code`, `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`)
VALUES(452, 0, '明细表', '1', 'table_type', NULL, NULL, 'N', '0', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `system_dict_data`
(`dict_code`, `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`)
VALUES(451, 0, '小写', '2', 'table_name_case', NULL, NULL, 'N', '0', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `system_dict_data`
(`dict_code`, `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`)
VALUES(450, 0, '大写', '1', 'table_name_case', NULL, NULL, 'N', '0', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `system_dict_data`
(`dict_code`, `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`)
VALUES(449, 0, '审批异常', '5', 'meta_audit_status', NULL, 'warning', 'N', '0', 'admin', '2026-04-23 09:54:12', NULL, NULL, NULL);
INSERT INTO `system_dict_data`
(`dict_code`, `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`)
VALUES(448, 0, '审批撤回', '4', 'meta_audit_status', NULL, 'info', 'N', '0', 'admin', '2026-04-23 09:53:53', NULL, NULL, NULL);
INSERT INTO `system_dict_data`
(`dict_code`, `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`)
VALUES(447, 0, '审批拒绝', '3', 'meta_audit_status', NULL, 'danger', 'N', '0', 'admin', '2026-04-23 09:53:22', NULL, NULL, NULL);
INSERT INTO `system_dict_data`
(`dict_code`, `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`)
VALUES(446, 0, '审批通过', '2', 'meta_audit_status', NULL, 'success', 'N', '0', 'admin', '2026-04-23 09:53:04', NULL, NULL, NULL);
INSERT INTO `system_dict_data`
(`dict_code`, `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`)
VALUES(445, 0, '审批中', '1', 'meta_audit_status', NULL, 'primary', 'N', '0', 'admin', '2026-04-23 09:52:53', NULL, NULL, NULL);
INSERT INTO `system_dict_data`
(`dict_code`, `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`)
VALUES(444, 0, '已发布', '1', 'meta_task_status', NULL, 'success', 'N', '0', 'admin', '2026-04-21 10:25:37', NULL, NULL, NULL);
INSERT INTO `system_dict_data`
(`dict_code`, `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`)
VALUES(443, 0, '未发布', '0', 'meta_task_status', NULL, 'danger', 'N', '0', 'admin', '2026-04-21 10:25:12', 'admin', '2026-04-21 10:25:23', NULL);
INSERT INTO `system_dict_data`
(`dict_code`, `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`)
VALUES(442, 0, '否', '0', 'table_yes_no', NULL, 'danger', 'N', '0', 'admin', '2026-04-21 10:24:30', 'admin', '2026-04-21 10:24:36', NULL);
INSERT INTO `system_dict_data`
(`dict_code`, `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`)
VALUES(441, 0, '是', '1', 'table_yes_no', NULL, 'primary', 'N', '0', 'admin', '2026-04-21 10:24:18', NULL, NULL, NULL);
INSERT INTO `system_dict_data`
(`dict_code`, `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`)
VALUES(440, 0, 'CDC捕获', '3', 'mc_collect_mode', NULL, 'primary', 'N', '0', 'admin', '2026-04-21 10:08:56', 'admin', '2026-04-21 10:09:04', NULL);
INSERT INTO `system_dict_data`
(`dict_code`, `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`)
VALUES(439, 0, '全量', '2', 'mc_collect_mode', NULL, 'primary', 'N', '0', 'admin', '2026-04-21 10:08:45', NULL, NULL, NULL);
INSERT INTO `system_dict_data`
(`dict_code`, `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`)
VALUES(438, 0, '增量', '1', 'mc_collect_mode', NULL, 'primary', 'N', '0', 'admin', '2026-04-21 10:08:28', NULL, NULL, NULL);
INSERT INTO `system_dict_data`
(`dict_code`, `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`)
VALUES(437, 1, '自定义库', '1', 'mc_collect_scope', NULL, 'default', 'N', '0', 'admin', '2026-04-21 10:07:31', NULL, NULL, NULL);
INSERT INTO `system_dict_data`
(`dict_code`, `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`)
VALUES(436, 0, '整个数据源', '2', 'mc_collect_scope', NULL, 'default', 'N', '0', 'admin', '2026-04-21 10:07:21', NULL, NULL, NULL);
INSERT INTO `system_dict_data`
(`dict_code`, `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`)
VALUES (456,1,'正在执行','1','mc_task_instance_status',null,'primary','N','0','admin','2026-04-21 10:07:21',null,null,null);
INSERT INTO `system_dict_data`
(`dict_code`, `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`)
VALUES (457,2,'失败','2','mc_task_instance_status',null,'danger','N','0','admin','2026-04-21 10:07:21',null,null,null);
INSERT INTO `system_dict_data`
(`dict_code`, `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`)
VALUES (458,9,'成功','9','mc_task_instance_status',null,'success','N','0','admin','2026-04-21 10:07:21',null,null,null);

INSERT INTO `system_menu` (`menu_id`,`menu_name`,`parent_id`,`order_num`,`path`,`component`,`query`,`is_frame`,`is_cache`,`route_name`,`menu_type`,`visible`,`status`,`perms`,`icon`,`create_by`,`create_time`,`update_by`,`update_time`,`remark` ) VALUES (2738,'来源系统',2375,2,'sourceSystem','att/sourceSystem/index',null,1,0,null,'C','0','0','att:sourcesystem:list','box-3-line','admin','2026-04-03 10:32:23','admin','2026-04-24 11:14:18','来源系统菜单');
INSERT INTO `system_menu` (`menu_id`,`menu_name`,`parent_id`,`order_num`,`path`,`component`,`query`,`is_frame`,`is_cache`,`route_name`,`menu_type`,`visible`,`status`,`perms`,`icon`,`create_by`,`create_time`,`update_by`,`update_time`,`remark` ) VALUES (2739,'来源系统查询',2738,1,'#',null,null,1,0,null,'F','0','0','att:sourcesystem:query','#','admin','2026-04-03 10:35:46','admin','2026-04-15 15:09:24',null);
INSERT INTO `system_menu` (`menu_id`,`menu_name`,`parent_id`,`order_num`,`path`,`component`,`query`,`is_frame`,`is_cache`,`route_name`,`menu_type`,`visible`,`status`,`perms`,`icon`,`create_by`,`create_time`,`update_by`,`update_time`,`remark` ) VALUES (2740,'来源系统新增',2738,2,'#',null,null,1,0,null,'F','0','0','att:sourcesystem:add','#','admin','2026-04-03 10:35:49','admin','2026-04-15 15:09:46',null);
INSERT INTO `system_menu` (`menu_id`,`menu_name`,`parent_id`,`order_num`,`path`,`component`,`query`,`is_frame`,`is_cache`,`route_name`,`menu_type`,`visible`,`status`,`perms`,`icon`,`create_by`,`create_time`,`update_by`,`update_time`,`remark` ) VALUES (2741,'来源系统修改',2738,3,'#',null,null,1,0,null,'F','0','0','att:sourcesystem:edit','#','admin','2026-04-03 10:35:52','admin','2026-04-15 15:16:53',null);
INSERT INTO `system_menu` (`menu_id`,`menu_name`,`parent_id`,`order_num`,`path`,`component`,`query`,`is_frame`,`is_cache`,`route_name`,`menu_type`,`visible`,`status`,`perms`,`icon`,`create_by`,`create_time`,`update_by`,`update_time`,`remark` ) VALUES (2742,'来源系统删除',2738,4,'#',null,null,1,0,null,'F','0','0','att:sourcesystem:remove','#','admin','2026-04-03 10:35:56','admin','2026-04-15 15:16:45',null);
INSERT INTO `system_menu` (`menu_id`,`menu_name`,`parent_id`,`order_num`,`path`,`component`,`query`,`is_frame`,`is_cache`,`route_name`,`menu_type`,`visible`,`status`,`perms`,`icon`,`create_by`,`create_time`,`update_by`,`update_time`,`remark` ) VALUES (2743,'来源系统导出',2738,5,'#',null,null,1,0,null,'F','0','0','att:sourcesystem:export','#','admin','2026-04-03 10:36:08','admin','2026-04-15 15:16:29',null);
INSERT INTO `system_menu` (`menu_id`,`menu_name`,`parent_id`,`order_num`,`path`,`component`,`query`,`is_frame`,`is_cache`,`route_name`,`menu_type`,`visible`,`status`,`perms`,`icon`,`create_by`,`create_time`,`update_by`,`update_time`,`remark` ) VALUES (2744,'来源系统导入',2738,6,'#',null,null,1,0,null,'F','0','0','att:sourcesystem:import','#','admin','2026-04-03 10:36:14','admin','2026-04-15 15:16:40',null);
INSERT INTO `system_menu` (`menu_id`,`menu_name`,`parent_id`,`order_num`,`path`,`component`,`query`,`is_frame`,`is_cache`,`route_name`,`menu_type`,`visible`,`status`,`perms`,`icon`,`create_by`,`create_time`,`update_by`,`update_time`,`remark` ) VALUES (2745,'最新元数据查询',2727,1,'#',null,null,1,0,null,'F','0','0','mc:metadata:table:query','#','admin','2025-12-18 11:05:20','admin','2026-04-27 11:24:44',null);
INSERT INTO `system_menu` (`menu_id`,`menu_name`,`parent_id`,`order_num`,`path`,`component`,`query`,`is_frame`,`is_cache`,`route_name`,`menu_type`,`visible`,`status`,`perms`,`icon`,`create_by`,`create_time`,`update_by`,`update_time`,`remark` ) VALUES (2746,'最新元数据新增',2727,2,'#',null,null,1,0,null,'F','0','0','mc:metadata:table:add','#','admin','2025-12-18 11:05:20','admin','2026-04-27 11:25:04',null);
INSERT INTO `system_menu` (`menu_id`,`menu_name`,`parent_id`,`order_num`,`path`,`component`,`query`,`is_frame`,`is_cache`,`route_name`,`menu_type`,`visible`,`status`,`perms`,`icon`,`create_by`,`create_time`,`update_by`,`update_time`,`remark` ) VALUES (2747,'最新元数据修改',2727,3,'#',null,null,1,0,null,'F','0','0','mc:metadata:table:edit','#','admin','2025-12-18 11:05:20','admin','2026-04-27 11:25:12',null);
INSERT INTO `system_menu` (`menu_id`,`menu_name`,`parent_id`,`order_num`,`path`,`component`,`query`,`is_frame`,`is_cache`,`route_name`,`menu_type`,`visible`,`status`,`perms`,`icon`,`create_by`,`create_time`,`update_by`,`update_time`,`remark` ) VALUES (2748,'最新元数据删除',2727,4,'#',null,null,1,0,null,'F','0','0','mc:metadata:table:remove','#','admin','2025-12-18 11:05:21','admin','2026-04-27 11:25:19',null);
INSERT INTO `system_menu` (`menu_id`,`menu_name`,`parent_id`,`order_num`,`path`,`component`,`query`,`is_frame`,`is_cache`,`route_name`,`menu_type`,`visible`,`status`,`perms`,`icon`,`create_by`,`create_time`,`update_by`,`update_time`,`remark` ) VALUES (2749,'最新元数据详情',2727,5,'#',null,null,1,0,null,'F','0','0','mc:metadata:table:detail','#','admin','2025-12-18 11:05:21','admin','2026-04-27 11:25:26',null);

UPDATE `system_menu` SET `menu_name` = '元数据管理',`parent_id` = 2723,`order_num` = 1,`path` = 'meta',`component` = null,`query` = null,`is_frame` = 1,`is_cache` = 0,`route_name` = null,`menu_type` = 'M',`visible` = '0',`status` = '0',`perms` = null,`icon` = 'ysjgl',`create_by` = 'admin',`create_time` = '2026-03-25 17:42:50',`update_by` = 'admin',`update_time` = '2026-03-27 16:10:44',`remark` = null WHERE `menu_id` = 2724;
UPDATE `system_menu` SET `menu_name` = '采集任务',`parent_id` = 2724,`order_num` = 1,`path` = 'task',`component` = 'mc/task/structured/index',`query` = null,`is_frame` = 1,`is_cache` = 0,`route_name` = null,`menu_type` = 'C',`visible` = '0',`status` = '0',`perms` = null,`icon` = '#',`create_by` = 'admin',`create_time` = '2026-03-25 17:43:26',`update_by` = 'admin',`update_time` = '2026-05-07 11:47:58',`remark` = null WHERE `menu_id` = 2725;
UPDATE `system_menu` SET `menu_name` = '采集实例',`parent_id` = 2724,`order_num` = 2,`path` = 'instance',`component` = 'mc/instance/structured/index',`query` = null,`is_frame` = 1,`is_cache` = 0,`route_name` = null,`menu_type` = 'C',`visible` = '0',`status` = '0',`perms` = null,`icon` = '#',`create_by` = 'admin',`create_time` = '2026-03-25 17:44:36',`update_by` = 'admin',`update_time` = '2026-05-07 11:48:22',`remark` = null WHERE `menu_id` = 2726;
UPDATE `system_menu` SET `menu_name` = '最新元数据',`parent_id` = 2724,`order_num` = 3,`path` = 'management',`component` = 'meta/unreleased/structured/table/index',`query` = null,`is_frame` = 1,`is_cache` = 0,`route_name` = null,`menu_type` = 'C',`visible` = '0',`status` = '0',`perms` = 'mc:metadata:table:list',`icon` = '#',`create_by` = 'admin',`create_time` = '2026-03-25 17:44:52',`update_by` = 'admin',`update_time` = '2026-05-12 14:57:16',`remark` = null WHERE `menu_id` = 2727;
UPDATE `system_menu` SET `menu_name` = '定版元数据',`parent_id` = 2724,`order_num` = 4,`path` = 'comparison',`component` = 'meta/released/structured/table/index',`query` = null,`is_frame` = 1,`is_cache` = 0,`route_name` = null,`menu_type` = 'C',`visible` = '0',`status` = '0',`perms` = null,`icon` = '#',`create_by` = 'admin',`create_time` = '2026-03-25 17:45:07',`update_by` = 'admin',`update_time` = '2026-05-07 11:49:05',`remark` = null WHERE `menu_id` = 2728;
UPDATE `system_menu` SET `menu_name` = '元数据比对',`parent_id` = 2724,`order_num` = 5,`path` = 'comparison',`component` = 'sys/developing/index',`query` = null,`is_frame` = 1,`is_cache` = 0,`route_name` = null,`menu_type` = 'C',`visible` = '1',`status` = '1',`perms` = '',`icon` = '#',`create_by` = 'admin',`create_time` = '2026-03-25 17:45:35',`update_by` = 'admin',`update_time` = '2026-05-12 14:50:21',`remark` = null WHERE `menu_id` = 2729;