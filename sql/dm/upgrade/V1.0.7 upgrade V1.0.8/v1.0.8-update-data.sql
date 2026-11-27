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

SET IDENTITY_INSERT "QDATA"."SYSTEM_MENU" ON;
INSERT INTO "QDATA"."SYSTEM_MENU"
(MENU_ID, MENU_NAME, PARENT_ID, ORDER_NUM, "PATH", COMPONENT, QUERY, IS_FRAME, IS_CACHE, ROUTE_NAME, MENU_TYPE, VISIBLE, STATUS, PERMS, ICON, CREATE_BY, CREATE_TIME, UPDATE_BY, UPDATE_TIME, REMARK)
VALUES(2693, '标准信息分类管理查询', 2687, 1, '#', NULL, NULL, 1, 0, NULL, 'F', '0', '0', 'att:documentCat:query', '#', '吴同', '2025-08-21 14:45:24.000', 'wutong', '2025-09-24 17:39:18.000', NULL);
INSERT INTO "QDATA"."SYSTEM_MENU"
(MENU_ID, MENU_NAME, PARENT_ID, ORDER_NUM, "PATH", COMPONENT, QUERY, IS_FRAME, IS_CACHE, ROUTE_NAME, MENU_TYPE, VISIBLE, STATUS, PERMS, ICON, CREATE_BY, CREATE_TIME, UPDATE_BY, UPDATE_TIME, REMARK)
VALUES(2692, '标准信息分类管理新增', 2687, 2, '#', NULL, NULL, 1, 0, NULL, 'F', '0', '0', 'att:documentCat:add', '#', '吴同', '2025-08-21 14:45:24.000', 'wutong', '2025-09-24 17:39:26.000', NULL);
INSERT INTO "QDATA"."SYSTEM_MENU"
(MENU_ID, MENU_NAME, PARENT_ID, ORDER_NUM, "PATH", COMPONENT, QUERY, IS_FRAME, IS_CACHE, ROUTE_NAME, MENU_TYPE, VISIBLE, STATUS, PERMS, ICON, CREATE_BY, CREATE_TIME, UPDATE_BY, UPDATE_TIME, REMARK)
VALUES(2691, '标准信息分类管理修改', 2687, 3, '#', NULL, NULL, 1, 0, NULL, 'F', '0', '0', 'att:documentCat:edit', '#', '吴同', '2025-08-21 14:45:24.000', 'wutong', '2025-09-24 17:39:33.000', NULL);
INSERT INTO "QDATA"."SYSTEM_MENU"
(MENU_ID, MENU_NAME, PARENT_ID, ORDER_NUM, "PATH", COMPONENT, QUERY, IS_FRAME, IS_CACHE, ROUTE_NAME, MENU_TYPE, VISIBLE, STATUS, PERMS, ICON, CREATE_BY, CREATE_TIME, UPDATE_BY, UPDATE_TIME, REMARK)
VALUES(2690, '标准信息分类管理删除', 2687, 4, '#', NULL, NULL, 1, 0, NULL, 'F', '0', '0', 'att:documentCat:remove', '#', '吴同', '2025-08-21 14:45:24.000', 'wutong', '2025-09-24 17:39:40.000', NULL);
INSERT INTO "QDATA"."SYSTEM_MENU"
(MENU_ID, MENU_NAME, PARENT_ID, ORDER_NUM, "PATH", COMPONENT, QUERY, IS_FRAME, IS_CACHE, ROUTE_NAME, MENU_TYPE, VISIBLE, STATUS, PERMS, ICON, CREATE_BY, CREATE_TIME, UPDATE_BY, UPDATE_TIME, REMARK)
VALUES(2689, '标准信息分类管理导出', 2687, 5, '#', NULL, NULL, 1, 0, NULL, 'F', '0', '0', 'att:documentCat:export', '#', '吴同', '2025-08-21 14:45:24.000', 'wutong', '2025-09-24 17:39:49.000', NULL);
INSERT INTO "QDATA"."SYSTEM_MENU"
(MENU_ID, MENU_NAME, PARENT_ID, ORDER_NUM, "PATH", COMPONENT, QUERY, IS_FRAME, IS_CACHE, ROUTE_NAME, MENU_TYPE, VISIBLE, STATUS, PERMS, ICON, CREATE_BY, CREATE_TIME, UPDATE_BY, UPDATE_TIME, REMARK)
VALUES(2688, '标准信息分类管理导入', 2687, 6, '#', NULL, NULL, 1, 0, NULL, 'F', '0', '0', 'att:documentCat:import', '#', '吴同', '2025-08-21 14:45:25.000', 'wutong', '2025-09-24 17:39:56.000', NULL);
INSERT INTO "QDATA"."SYSTEM_MENU"
(MENU_ID, MENU_NAME, PARENT_ID, ORDER_NUM, "PATH", COMPONENT, QUERY, IS_FRAME, IS_CACHE, ROUTE_NAME, MENU_TYPE, VISIBLE, STATUS, PERMS, ICON, CREATE_BY, CREATE_TIME, UPDATE_BY, UPDATE_TIME, REMARK)
VALUES(2687, '标准分类管理', 2352, 2, 'documentCat', 'att/cat/documentCat/index', NULL, 1, 0, NULL, 'C', '0', '0', 'att:documentCat:list', '#', 'admin', '2025-12-11 15:25:56.000', NULL, NULL, NULL);
SET IDENTITY_INSERT "QDATA"."SYSTEM_MENU" OFF;

UPDATE "QDATA"."SYSTEM_ROLE"
SET PROJECT_ID=-1
WHERE ROLE_ID=10;
UPDATE "QDATA"."SYSTEM_ROLE"
SET PROJECT_ID=-1
WHERE ROLE_ID=11;
UPDATE "QDATA"."SYSTEM_ROLE"
SET PROJECT_ID=-1
WHERE ROLE_ID=12;
UPDATE "QDATA"."SYSTEM_ROLE"
SET PROJECT_ID=-1
WHERE ROLE_ID=13;
UPDATE "QDATA"."SYSTEM_ROLE"
SET PROJECT_ID=-1
WHERE ROLE_ID=14;
