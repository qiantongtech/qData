UPDATE `qdata`.`system_role`
SET `project_id`=-1
WHERE `role_id`=10;
UPDATE `qdata`.`system_role`
SET `project_id`=-1
WHERE `role_id`=11;
UPDATE `qdata`.`system_role`
SET `project_id`=-1
WHERE `role_id`=12;
UPDATE `qdata`.`system_role`
SET `project_id`=-1
WHERE `role_id`=13;
UPDATE `qdata`.`system_role`
SET `project_id`=-1
WHERE `role_id`=14;

INSERT INTO `system_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `is_frame`, `is_cache`, `route_name`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (2693, '标准信息分类管理查询', 2687, 1, '#', NULL, NULL, 1, 0, NULL, 'F', '0', '0', 'att:documentCat:query', '#', '吴同', '2025-08-21 14:45:24', 'wutong', '2025-09-24 17:39:18', NULL);
INSERT INTO `system_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `is_frame`, `is_cache`, `route_name`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (2692, '标准信息分类管理新增', 2687, 2, '#', NULL, NULL, 1, 0, NULL, 'F', '0', '0', 'att:documentCat:add', '#', '吴同', '2025-08-21 14:45:24', 'wutong', '2025-09-24 17:39:26', NULL);
INSERT INTO `system_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `is_frame`, `is_cache`, `route_name`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (2691, '标准信息分类管理修改', 2687, 3, '#', NULL, NULL, 1, 0, NULL, 'F', '0', '0', 'att:documentCat:edit', '#', '吴同', '2025-08-21 14:45:24', 'wutong', '2025-09-24 17:39:33', NULL);
INSERT INTO `system_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `is_frame`, `is_cache`, `route_name`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`)VALUES (2690, '标准信息分类管理删除', 2687, 4, '#', NULL, NULL, 1, 0, NULL, 'F', '0', '0', 'att:documentCat:remove', '#', '吴同', '2025-08-21 14:45:24', 'wutong', '2025-09-24 17:39:40', NULL);
INSERT INTO `system_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `is_frame`, `is_cache`, `route_name`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (2689, '标准信息分类管理导出', 2687, 5, '#', NULL, NULL, 1, 0, NULL, 'F', '0', '0', 'att:documentCat:export', '#', '吴同', '2025-08-21 14:45:24', 'wutong', '2025-09-24 17:39:49', NULL);
INSERT INTO `system_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `is_frame`, `is_cache`, `route_name`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (2688, '标准信息分类管理导入', 2687, 6, '#', NULL, NULL, 1, 0, NULL, 'F', '0', '0', 'att:documentCat:import', '#', '吴同', '2025-08-21 14:45:25', 'wutong', '2025-09-24 17:39:56', NULL);
INSERT INTO `system_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `is_frame`, `is_cache`, `route_name`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (2687, '标准分类管理', 2352, 2, 'documentCat', 'att/cat/documentCat/index', NULL, 1, 0, NULL, 'C', '0', '0', 'att:documentCat:list', '#', 'admin', '2025-12-11 15:25:56', NULL, NULL, NULL);
