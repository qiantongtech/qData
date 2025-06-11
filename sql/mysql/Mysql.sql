SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for att_api_cat
-- ----------------------------
DROP TABLE IF EXISTS `att_api_cat`;
CREATE TABLE `att_api_cat`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '类别名称',
  `parent_id` bigint(20) NULL DEFAULT NULL COMMENT '关联上级ID',
  `sort_order` int(11) NOT NULL DEFAULT 0 COMMENT '类别排序',
  `description` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '描述',
  `code` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '层级编码',
  `valid_flag` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '1' COMMENT '是否有效;0：无效，1：有效',
  `del_flag` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '删除标志;1：已删除，0：未删除',
  `create_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `creator_id` bigint(20) NULL DEFAULT NULL COMMENT '创建人id',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `updater_id` bigint(20) NULL DEFAULT NULL COMMENT '更新人id',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `ID_90028463113800`(`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 65 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '数据服务类目管理' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of att_api_cat
-- ----------------------------
INSERT INTO `att_api_cat` VALUES (3, '基础数据', 0, 0, NULL, 'A01', '1', '0', '舒月鑫', 733, '2025-03-24 11:34:05', '超级管理员', 1, '2025-05-10 09:10:01', NULL);
INSERT INTO `att_api_cat` VALUES (10, '江河湖泊', 3, 0, NULL, 'A01A01', '1', '0', '超级管理员', 1, '2025-05-10 09:10:29', '超级管理员', 1, '2025-05-10 09:10:29', NULL);
INSERT INTO `att_api_cat` VALUES (11, '流域', 10, 0, NULL, 'A01A01A01', '1', '0', '超级管理员', 1, '2025-05-10 09:10:46', '超级管理员', 1, '2025-05-10 09:10:46', NULL);
INSERT INTO `att_api_cat` VALUES (12, '河流', 10, 1, NULL, 'A01A01A02', '1', '0', '超级管理员', 1, '2025-05-10 09:11:00', '超级管理员', 1, '2025-05-10 09:11:00', NULL);
INSERT INTO `att_api_cat` VALUES (13, '湖泊', 10, 2, NULL, 'A01A01A03', '1', '0', '超级管理员', 1, '2025-05-10 09:11:25', '超级管理员', 1, '2025-05-10 09:11:25', NULL);
INSERT INTO `att_api_cat` VALUES (14, '水利工程', 3, 1, NULL, 'A01A02', '1', '0', '超级管理员', 1, '2025-05-10 09:15:26', '超级管理员', 1, '2025-05-10 09:15:26', NULL);
INSERT INTO `att_api_cat` VALUES (15, '水库', 14, 1, NULL, 'A01A02A01', '1', '0', '超级管理员', 1, '2025-05-10 09:15:48', '超级管理员', 1, '2025-05-10 09:15:48', NULL);
INSERT INTO `att_api_cat` VALUES (16, '水库大坝', 14, 1, NULL, 'A01A02A02', '1', '0', '超级管理员', 1, '2025-05-10 09:16:39', '超级管理员', 1, '2025-05-10 09:16:39', NULL);
INSERT INTO `att_api_cat` VALUES (17, '水电站', 14, 2, NULL, 'A01A02A03', '1', '0', '超级管理员', 1, '2025-05-10 09:16:56', '超级管理员', 1, '2025-05-10 09:16:56', NULL);
INSERT INTO `att_api_cat` VALUES (18, '灌区', 14, 4, NULL, 'A01A02A04', '1', '0', '超级管理员', 1, '2025-05-10 09:17:30', '超级管理员', 1, '2025-05-10 09:17:30', NULL);
INSERT INTO `att_api_cat` VALUES (19, '渠（沟）道', 14, 5, NULL, 'A01A02A05', '1', '0', '超级管理员', 1, '2025-05-10 09:17:54', '超级管理员', 1, '2025-05-10 09:17:54', NULL);
INSERT INTO `att_api_cat` VALUES (20, '取水井', 14, 6, NULL, 'A01A02A06', '1', '0', '超级管理员', 1, '2025-05-10 09:18:12', '超级管理员', 1, '2025-05-10 09:18:12', NULL);
INSERT INTO `att_api_cat` VALUES (21, '水闸', 14, 7, NULL, 'A01A02A07', '1', '0', '超级管理员', 1, '2025-05-10 09:18:25', '超级管理员', 1, '2025-05-10 09:18:25', NULL);
INSERT INTO `att_api_cat` VALUES (22, '渡槽', 14, 8, NULL, 'A01A02A08', '1', '0', '超级管理员', 1, '2025-05-10 09:18:48', '超级管理员', 1, '2025-05-10 09:18:48', NULL);
INSERT INTO `att_api_cat` VALUES (23, '倒虹吸', 14, 9, NULL, 'A01A02A09', '1', '0', '超级管理员', 1, '2025-05-10 09:19:02', '超级管理员', 1, '2025-05-10 09:19:02', NULL);
INSERT INTO `att_api_cat` VALUES (24, '泵站', 14, 10, NULL, 'A01A02A10', '1', '0', '超级管理员', 1, '2025-05-10 09:19:22', '超级管理员', 1, '2025-05-10 09:19:22', NULL);
INSERT INTO `att_api_cat` VALUES (25, '涵洞', 14, 11, NULL, 'A01A02A11', '1', '0', '超级管理员', 1, '2025-05-10 09:19:40', '超级管理员', 1, '2025-05-10 09:19:40', NULL);
INSERT INTO `att_api_cat` VALUES (26, '引调水工程', 14, 12, NULL, 'A01A02A12', '1', '0', '超级管理员', 1, '2025-05-10 09:19:57', '超级管理员', 1, '2025-05-10 09:19:57', NULL);
INSERT INTO `att_api_cat` VALUES (27, '农村供水工程', 14, 13, NULL, 'A01A02A13', '1', '0', '超级管理员', 1, '2025-05-10 09:20:14', '超级管理员', 1, '2025-05-10 09:20:14', NULL);
INSERT INTO `att_api_cat` VALUES (28, '窖池', 14, 14, NULL, 'A01A02A14', '1', '0', '超级管理员', 1, '2025-05-10 09:20:26', '超级管理员', 1, '2025-05-10 09:20:26', NULL);
INSERT INTO `att_api_cat` VALUES (29, '塘坝', 14, 15, NULL, 'A01A02A15', '1', '0', '超级管理员', 1, '2025-05-10 09:21:03', '超级管理员', 1, '2025-05-10 09:21:03', NULL);
INSERT INTO `att_api_cat` VALUES (30, '蓄滞洪区', 14, 14, NULL, 'A01A02A16', '1', '0', '超级管理员', 1, '2025-05-10 09:21:34', '超级管理员', 1, '2025-05-10 09:21:34', NULL);
INSERT INTO `att_api_cat` VALUES (31, '堤防', 14, 15, NULL, 'A01A02A17', '1', '0', '超级管理员', 1, '2025-05-10 09:21:48', '超级管理员', 1, '2025-05-10 09:21:48', NULL);
INSERT INTO `att_api_cat` VALUES (32, '圩垸', 14, 16, NULL, 'A01A02A18', '1', '0', '超级管理员', 1, '2025-05-10 09:24:04', '超级管理员', 1, '2025-05-10 09:24:04', NULL);
INSERT INTO `att_api_cat` VALUES (33, '治河工程', 14, 17, NULL, 'A01A02A19', '1', '0', '超级管理员', 1, '2025-05-10 09:24:42', '超级管理员', 1, '2025-05-10 09:24:42', NULL);
INSERT INTO `att_api_cat` VALUES (34, '淤地坝', 14, 18, NULL, 'A01A02A20', '1', '0', '超级管理员', 1, '2025-05-10 09:25:03', '超级管理员', 1, '2025-05-10 09:25:03', NULL);
INSERT INTO `att_api_cat` VALUES (35, '橡胶坝', 14, 19, NULL, 'A01A02A21', '1', '0', '超级管理员', 1, '2025-05-10 09:25:20', '超级管理员', 1, '2025-05-10 09:25:20', NULL);
INSERT INTO `att_api_cat` VALUES (36, '监测站（点）', 3, 2, NULL, 'A01A03', '1', '0', '超级管理员', 1, '2025-05-10 09:25:51', '超级管理员', 1, '2025-05-10 09:25:51', NULL);
INSERT INTO `att_api_cat` VALUES (37, '水文测站', 36, 0, NULL, 'A01A03A01', '1', '0', '超级管理员', 1, '2025-05-10 09:26:04', '超级管理员', 1, '2025-05-10 09:26:04', NULL);
INSERT INTO `att_api_cat` VALUES (38, '水土保持监测站', 36, 1, NULL, 'A01A03A02', '1', '0', '超级管理员', 1, '2025-05-10 09:27:51', '超级管理员', 1, '2025-05-10 09:27:51', NULL);
INSERT INTO `att_api_cat` VALUES (39, '水工程安全监测点', 36, 3, NULL, 'A01A03A03', '1', '0', '超级管理员', 1, '2025-05-10 09:28:11', '超级管理员', 1, '2025-05-10 09:28:11', NULL);
INSERT INTO `att_api_cat` VALUES (40, '供（取）水量监测点', 36, 4, NULL, 'A01A03A04', '1', '0', '超级管理员', 1, '2025-05-10 09:28:38', '超级管理员', 1, '2025-05-10 09:28:38', NULL);
INSERT INTO `att_api_cat` VALUES (41, '水事影响监视点', 36, 5, NULL, 'A01A03A05', '1', '0', '超级管理员', 1, '2025-05-10 09:28:57', '超级管理员', 1, '2025-05-10 09:28:57', NULL);
INSERT INTO `att_api_cat` VALUES (42, '墒情监测站', 36, 6, NULL, 'A01A03A06', '1', '0', '超级管理员', 1, '2025-05-10 09:29:14', '超级管理员', 1, '2025-05-10 09:29:14', NULL);
INSERT INTO `att_api_cat` VALUES (43, '其他管理对象', 3, 4, NULL, 'A01A04', '1', '0', '超级管理员', 1, '2025-05-10 09:29:54', '超级管理员', 1, '2025-05-10 09:29:54', NULL);
INSERT INTO `att_api_cat` VALUES (44, '水资源分区', 43, 0, NULL, 'A01A04A01', '1', '0', '超级管理员', 1, '2025-05-10 09:30:08', '超级管理员', 1, '2025-05-10 09:30:08', NULL);
INSERT INTO `att_api_cat` VALUES (45, '水功能分区', 43, 1, NULL, 'A01A04A02', '1', '0', '超级管理员', 1, '2025-05-10 09:30:33', '超级管理员', 1, '2025-05-10 09:30:33', NULL);
INSERT INTO `att_api_cat` VALUES (46, '取水口', 43, 2, NULL, 'A01A04A03', '1', '0', '超级管理员', 1, '2025-05-10 09:30:51', '超级管理员', 1, '2025-05-10 09:30:51', NULL);
INSERT INTO `att_api_cat` VALUES (48, '地理空间数据', 0, 1, NULL, 'A02', '1', '0', '超级管理员', 1, '2025-05-10 09:32:35', '超级管理员', 1, '2025-05-10 09:32:45', NULL);
INSERT INTO `att_api_cat` VALUES (49, '数字正射影像图', 48, 0, NULL, 'A02A01', '1', '0', '超级管理员', 1, '2025-05-10 09:33:16', '超级管理员', 1, '2025-05-10 09:33:16', NULL);
INSERT INTO `att_api_cat` VALUES (50, '数字高程模型/数字表面模型', 48, 1, NULL, 'A02A02', '1', '0', '超级管理员', 1, '2025-05-10 09:33:44', '超级管理员', 1, '2025-05-10 09:33:44', NULL);
INSERT INTO `att_api_cat` VALUES (51, '倾斜摄影影像/激光云点', 48, 2, NULL, 'A02A03', '1', '0', '超级管理员', 1, '2025-05-10 09:34:15', '超级管理员', 1, '2025-05-10 09:34:15', NULL);
INSERT INTO `att_api_cat` VALUES (52, '水下地形', 48, 3, NULL, 'A02A04', '1', '0', '超级管理员', 1, '2025-05-10 09:34:32', '超级管理员', 1, '2025-05-10 09:34:32', NULL);
INSERT INTO `att_api_cat` VALUES (53, '建筑信息模型', 48, 4, NULL, 'A02A05', '1', '0', '超级管理员', 1, '2025-05-10 09:34:46', '超级管理员', 1, '2025-05-10 09:34:46', NULL);
INSERT INTO `att_api_cat` VALUES (54, '业务数据', 0, 3, NULL, 'A03', '1', '0', '超级管理员', 1, '2025-05-10 09:35:05', '超级管理员', 1, '2025-05-10 09:35:05', NULL);
INSERT INTO `att_api_cat` VALUES (55, '水文', 54, 0, NULL, 'A03A01', '1', '0', '超级管理员', 1, '2025-05-10 09:35:15', '超级管理员', 1, '2025-05-10 09:35:15', NULL);
INSERT INTO `att_api_cat` VALUES (56, '水资源', 54, 1, NULL, 'A03A02', '1', '0', '超级管理员', 1, '2025-05-10 09:35:25', '超级管理员', 1, '2025-05-10 09:35:25', NULL);
INSERT INTO `att_api_cat` VALUES (57, '水环境', 54, 2, NULL, 'A03A03', '1', '0', '超级管理员', 1, '2025-05-10 09:35:40', '超级管理员', 1, '2025-05-10 09:35:40', NULL);
INSERT INTO `att_api_cat` VALUES (58, '水利工程', 54, 4, NULL, 'A03A04', '1', '0', '超级管理员', 1, '2025-05-10 09:35:54', '超级管理员', 1, '2025-05-10 09:35:54', NULL);
INSERT INTO `att_api_cat` VALUES (59, '农村水利', 54, 5, NULL, 'A03A05', '1', '0', '超级管理员', 1, '2025-05-10 09:36:07', '超级管理员', 1, '2025-05-10 09:36:07', NULL);
INSERT INTO `att_api_cat` VALUES (60, '水旱灾害防御', 54, 6, NULL, 'A03A06', '1', '0', '超级管理员', 1, '2025-05-10 09:36:22', '超级管理员', 1, '2025-05-10 09:36:22', NULL);
INSERT INTO `att_api_cat` VALUES (61, '河湖保护 ', 54, 7, NULL, 'A03A07', '1', '0', '超级管理员', 1, '2025-05-10 09:36:34', '超级管理员', 1, '2025-05-10 09:36:34', NULL);
INSERT INTO `att_api_cat` VALUES (62, '水土保持', 54, 8, NULL, 'A03A08', '1', '0', '超级管理员', 1, '2025-05-10 09:36:48', '超级管理员', 1, '2025-05-10 09:36:48', NULL);
INSERT INTO `att_api_cat` VALUES (63, '节约用水', 54, 9, NULL, 'A03A09', '1', '0', '超级管理员', 1, '2025-05-10 09:37:05', '超级管理员', 1, '2025-05-10 09:37:05', NULL);
INSERT INTO `att_api_cat` VALUES (64, '示例目录', 0, 0, '千数开源示例', 'A04', '0', '0', 'qData', 1, '2025-05-23 16:30:28', '管理员', 1, '2025-05-29 06:21:11', NULL);

-- ----------------------------
-- Table structure for att_asset_cat
-- ----------------------------
DROP TABLE IF EXISTS `att_asset_cat`;
CREATE TABLE `att_asset_cat`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '类别名称',
  `parent_id` bigint(20) NULL DEFAULT NULL COMMENT '关联上级ID',
  `sort_order` int(11) NOT NULL DEFAULT 0 COMMENT '类别排序',
  `description` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '描述',
  `code` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '层级编码',
  `valid_flag` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '1' COMMENT '是否有效;0：无效，1：有效',
  `del_flag` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '删除标志;1：已删除，0：未删除',
  `create_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `creator_id` bigint(20) NULL DEFAULT NULL COMMENT '创建人id',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `updater_id` bigint(20) NULL DEFAULT NULL COMMENT '更新人id',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `ID_90028827681500`(`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 229 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '数据资产类目管理' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of att_asset_cat
-- ----------------------------
INSERT INTO `att_asset_cat` VALUES (21, '基础数据', 0, 0, NULL, 'A05', '1', '0', 'admin', 1, '2025-02-05 17:33:07', '超级管理员', 1, '2025-04-21 11:13:46', NULL);
INSERT INTO `att_asset_cat` VALUES (22, '水利监测数据（假的）', 0, 30, NULL, 'A06', '1', '0', 'admin', 1, '2025-02-05 17:33:24', '超级管理员', 1, '2025-04-18 11:43:15', NULL);
INSERT INTO `att_asset_cat` VALUES (23, '水利业务管理数据(假的)', 0, 20, NULL, 'A07', '1', '0', 'admin', 1, '2025-02-05 17:33:40', '超级管理员', 1, '2025-04-18 11:43:38', NULL);
INSERT INTO `att_asset_cat` VALUES (24, '行政区域（假的）', 21, 80, NULL, 'A05A01', '1', '0', 'admin', 1, '2025-02-05 17:42:18', '超级管理员', 1, '2025-04-18 11:42:17', NULL);
INSERT INTO `att_asset_cat` VALUES (31, '江河湖泊', 21, 0, NULL, 'A05A05', '1', '0', '超级管理员', 1, '2025-04-18 10:55:15', '超级管理员', 1, '2025-04-18 10:55:21', NULL);
INSERT INTO `att_asset_cat` VALUES (32, '流域', 31, 0, NULL, 'A05A05A01', '1', '0', '超级管理员', 1, '2025-04-18 10:55:33', '超级管理员', 1, '2025-04-18 10:55:36', NULL);
INSERT INTO `att_asset_cat` VALUES (33, '河流', 31, 1, NULL, 'A05A05A02', '1', '0', '超级管理员', 1, '2025-04-18 10:57:03', '超级管理员', 1, '2025-04-18 10:57:03', NULL);
INSERT INTO `att_asset_cat` VALUES (34, '湖泊', 31, 2, NULL, 'A05A05A03', '1', '0', '超级管理员', 1, '2025-04-18 10:57:20', '超级管理员', 1, '2025-04-18 10:57:23', NULL);
INSERT INTO `att_asset_cat` VALUES (35, '水利工程', 21, 1, NULL, 'A05A06', '1', '0', '超级管理员', 1, '2025-04-18 10:58:42', '超级管理员', 1, '2025-04-18 10:59:13', NULL);
INSERT INTO `att_asset_cat` VALUES (36, '水库', 35, 0, NULL, 'A05A06A01', '1', '0', '超级管理员', 1, '2025-04-18 10:59:29', '超级管理员', 1, '2025-04-18 10:59:29', NULL);
INSERT INTO `att_asset_cat` VALUES (37, '水库大坝', 35, 1, NULL, 'A05A06A02', '1', '0', '超级管理员', 1, '2025-04-18 10:59:47', '超级管理员', 1, '2025-04-18 10:59:47', NULL);
INSERT INTO `att_asset_cat` VALUES (38, '水电站', 35, 2, NULL, 'A05A06A03', '1', '0', '超级管理员', 1, '2025-04-18 11:00:08', '超级管理员', 1, '2025-04-18 11:00:15', NULL);
INSERT INTO `att_asset_cat` VALUES (39, '灌区', 35, 4, NULL, 'A05A06A04', '1', '0', '超级管理员', 1, '2025-04-18 11:00:36', '超级管理员', 1, '2025-04-18 11:00:36', NULL);
INSERT INTO `att_asset_cat` VALUES (40, '渠（沟）道', 35, 5, NULL, 'A05A06A05', '1', '0', '超级管理员', 1, '2025-04-18 11:02:05', '超级管理员', 1, '2025-04-18 11:02:05', NULL);
INSERT INTO `att_asset_cat` VALUES (41, '取水井', 35, 6, NULL, 'A05A06A06', '1', '0', '超级管理员', 1, '2025-04-18 11:02:31', '超级管理员', 1, '2025-04-18 11:02:31', NULL);
INSERT INTO `att_asset_cat` VALUES (42, '水闸', 35, 7, NULL, 'A05A06A07', '1', '0', '超级管理员', 1, '2025-04-18 11:03:02', '超级管理员', 1, '2025-04-18 11:03:02', NULL);
INSERT INTO `att_asset_cat` VALUES (43, '渡槽', 35, 8, NULL, 'A05A06A08', '1', '0', '超级管理员', 1, '2025-04-18 11:03:26', '超级管理员', 1, '2025-04-18 11:03:26', NULL);
INSERT INTO `att_asset_cat` VALUES (44, '倒虹吸', 35, 9, NULL, 'A05A06A09', '1', '0', '超级管理员', 1, '2025-04-18 11:03:49', '超级管理员', 1, '2025-04-18 11:03:49', NULL);
INSERT INTO `att_asset_cat` VALUES (45, '泵站', 35, 10, NULL, 'A05A06A10', '1', '0', '超级管理员', 1, '2025-04-18 11:04:10', '超级管理员', 1, '2025-04-18 11:04:10', NULL);
INSERT INTO `att_asset_cat` VALUES (46, '涵洞', 35, 11, NULL, 'A05A06A11', '1', '0', '超级管理员', 1, '2025-04-18 11:04:28', '超级管理员', 1, '2025-04-18 11:04:28', NULL);
INSERT INTO `att_asset_cat` VALUES (47, '引调水工程', 35, 12, NULL, 'A05A06A12', '1', '0', '超级管理员', 1, '2025-04-18 11:04:51', '超级管理员', 1, '2025-04-18 11:04:51', NULL);
INSERT INTO `att_asset_cat` VALUES (48, '农村供水工程', 35, 13, NULL, 'A05A06A13', '1', '0', '超级管理员', 1, '2025-04-18 11:05:13', '超级管理员', 1, '2025-04-18 11:05:13', NULL);
INSERT INTO `att_asset_cat` VALUES (49, '窖池', 35, 14, NULL, 'A05A06A14', '1', '0', '超级管理员', 1, '2025-04-18 11:05:44', '超级管理员', 1, '2025-04-18 11:05:44', NULL);
INSERT INTO `att_asset_cat` VALUES (50, '塘坝', 35, 15, NULL, 'A05A06A15', '1', '0', '超级管理员', 1, '2025-04-18 11:06:10', '超级管理员', 1, '2025-04-18 11:06:10', NULL);
INSERT INTO `att_asset_cat` VALUES (51, '蓄滞洪区', 35, 16, NULL, 'A05A06A16', '1', '0', '超级管理员', 1, '2025-04-18 11:09:24', '超级管理员', 1, '2025-04-18 11:09:24', NULL);
INSERT INTO `att_asset_cat` VALUES (52, '堤防', 35, 17, NULL, 'A05A06A17', '1', '0', '超级管理员', 1, '2025-04-18 11:09:40', '超级管理员', 1, '2025-04-18 11:09:40', NULL);
INSERT INTO `att_asset_cat` VALUES (53, '圩垸', 35, 18, NULL, 'A05A06A18', '1', '0', '超级管理员', 1, '2025-04-18 11:11:45', '超级管理员', 1, '2025-04-18 11:11:45', NULL);
INSERT INTO `att_asset_cat` VALUES (54, '治河工程', 35, 19, NULL, 'A05A06A19', '1', '0', '超级管理员', 1, '2025-04-18 11:13:15', '超级管理员', 1, '2025-04-18 11:13:30', NULL);
INSERT INTO `att_asset_cat` VALUES (55, '淤地坝', 35, 20, NULL, 'A05A06A20', '1', '0', '超级管理员', 1, '2025-04-18 11:14:07', '超级管理员', 1, '2025-04-18 11:14:07', NULL);
INSERT INTO `att_asset_cat` VALUES (56, '橡胶坝', 35, 21, NULL, 'A05A06A21', '1', '0', '超级管理员', 1, '2025-04-18 11:14:23', '超级管理员', 1, '2025-04-18 11:14:23', NULL);
INSERT INTO `att_asset_cat` VALUES (57, '监测站（点）', 21, 3, NULL, 'A05A07', '1', '0', '超级管理员', 1, '2025-04-18 11:15:13', '超级管理员', 1, '2025-04-18 11:15:13', NULL);
INSERT INTO `att_asset_cat` VALUES (58, '水文测站', 57, 1, NULL, 'A05A07A01', '1', '0', '超级管理员', 1, '2025-04-18 11:15:50', '超级管理员', 1, '2025-04-18 11:15:50', NULL);
INSERT INTO `att_asset_cat` VALUES (59, '水土保持监测站', 57, 2, NULL, 'A05A07A02', '1', '0', '超级管理员', 1, '2025-04-18 11:16:18', '超级管理员', 1, '2025-04-18 11:16:18', NULL);
INSERT INTO `att_asset_cat` VALUES (60, '供（取）水量监测点', 57, 3, NULL, 'A05A07A03', '1', '0', '超级管理员', 1, '2025-04-18 11:16:46', '超级管理员', 1, '2025-04-18 11:16:46', NULL);
INSERT INTO `att_asset_cat` VALUES (61, '水事影像监视点', 57, 4, NULL, 'A05A07A04', '1', '0', '超级管理员', 1, '2025-04-18 11:17:59', '超级管理员', 1, '2025-04-18 11:17:59', NULL);
INSERT INTO `att_asset_cat` VALUES (62, '墒情监测站', 57, 5, NULL, 'A05A07A05', '1', '0', '超级管理员', 1, '2025-04-18 11:19:22', '超级管理员', 1, '2025-04-18 11:19:22', NULL);
INSERT INTO `att_asset_cat` VALUES (63, '其他管理对象', 21, 4, NULL, 'A05A08', '1', '0', '超级管理员', 1, '2025-04-18 11:20:42', '超级管理员', 1, '2025-04-18 11:20:42', NULL);
INSERT INTO `att_asset_cat` VALUES (64, '水资源分区', 63, 0, NULL, 'A05A08A01', '1', '0', '超级管理员', 1, '2025-04-18 11:21:02', '超级管理员', 1, '2025-04-18 11:21:02', NULL);
INSERT INTO `att_asset_cat` VALUES (65, '水功能区', 63, 1, NULL, 'A05A08A02', '1', '0', '超级管理员', 1, '2025-04-18 11:21:24', '超级管理员', 1, '2025-04-18 11:22:28', NULL);
INSERT INTO `att_asset_cat` VALUES (66, '水文地质单元', 63, 3, NULL, 'A05A08A03', '1', '0', '超级管理员', 1, '2025-04-18 11:21:52', '超级管理员', 1, '2025-04-18 11:22:31', NULL);
INSERT INTO `att_asset_cat` VALUES (67, '地下水超采区', 63, 4, NULL, 'A05A08A04', '1', '0', '超级管理员', 1, '2025-04-18 11:22:17', '超级管理员', 1, '2025-04-18 11:22:17', NULL);
INSERT INTO `att_asset_cat` VALUES (68, '水土保持区划', 63, 5, NULL, 'A05A08A05', '1', '0', '超级管理员', 1, '2025-04-18 11:22:50', '超级管理员', 1, '2025-04-18 11:22:50', NULL);
INSERT INTO `att_asset_cat` VALUES (69, '河湖管理范围', 63, 6, NULL, 'A05A08A06', '1', '0', '超级管理员', 1, '2025-04-18 11:23:10', '超级管理员', 1, '2025-04-18 11:23:10', NULL);
INSERT INTO `att_asset_cat` VALUES (70, '岸线功能分区', 63, 7, NULL, 'A05A08A07', '1', '0', '超级管理员', 1, '2025-04-18 11:23:33', '超级管理员', 1, '2025-04-18 11:23:33', NULL);
INSERT INTO `att_asset_cat` VALUES (71, '河段', 63, 8, NULL, 'A05A08A08', '1', '0', '超级管理员', 1, '2025-04-18 11:23:45', '超级管理员', 1, '2025-04-18 11:23:45', NULL);
INSERT INTO `att_asset_cat` VALUES (72, '堤段', 63, 9, NULL, 'A05A08A09', '1', '0', '超级管理员', 1, '2025-04-18 11:24:08', '超级管理员', 1, '2025-04-18 11:24:08', NULL);
INSERT INTO `att_asset_cat` VALUES (73, '险工险段', 63, 10, NULL, 'A05A08A10', '1', '0', '超级管理员', 1, '2025-04-18 11:24:27', '超级管理员', 1, '2025-04-18 11:24:27', NULL);
INSERT INTO `att_asset_cat` VALUES (74, '水源地', 63, 11, NULL, 'A05A08A11', '1', '0', '超级管理员', 1, '2025-04-18 11:24:40', '超级管理员', 1, '2025-04-18 11:24:40', NULL);
INSERT INTO `att_asset_cat` VALUES (75, '取水口', 63, 12, NULL, 'A05A08A12', '1', '0', '超级管理员', 1, '2025-04-18 11:24:53', '超级管理员', 1, '2025-04-18 11:24:53', NULL);
INSERT INTO `att_asset_cat` VALUES (76, '退排水口', 63, 13, NULL, 'A05A08A13', '1', '0', '超级管理员', 1, '2025-04-18 11:26:31', '超级管理员', 1, '2025-04-18 11:26:31', NULL);
INSERT INTO `att_asset_cat` VALUES (77, '取用水户', 63, 14, NULL, 'A05A08A14', '1', '0', '超级管理员', 1, '2025-04-18 11:26:46', '超级管理员', 1, '2025-04-18 11:26:46', NULL);
INSERT INTO `att_asset_cat` VALUES (78, '退排水户', 63, 15, NULL, 'A05A08A15', '1', '0', '超级管理员', 1, '2025-04-18 11:27:10', '超级管理员', 1, '2025-04-18 11:27:10', NULL);
INSERT INTO `att_asset_cat` VALUES (79, '地理空间数据', 0, 1, NULL, 'A08', '1', '0', '超级管理员', 1, '2025-04-18 11:29:06', '超级管理员', 1, '2025-04-18 11:29:06', NULL);
INSERT INTO `att_asset_cat` VALUES (80, '数字孪生地理空间数据', 79, 0, NULL, 'A08A01', '1', '0', '超级管理员', 1, '2025-04-18 11:29:29', '超级管理员', 1, '2025-04-18 11:29:48', NULL);
INSERT INTO `att_asset_cat` VALUES (81, '其他', 79, 1, NULL, 'A08A02', '1', '0', '超级管理员', 1, '2025-04-18 11:29:42', '超级管理员', 1, '2025-04-18 11:29:42', NULL);
INSERT INTO `att_asset_cat` VALUES (82, '数字正射影像图', 80, 0, NULL, 'A08A01A01', '1', '0', '超级管理员', 1, '2025-04-18 11:30:39', '超级管理员', 1, '2025-04-18 11:30:39', NULL);
INSERT INTO `att_asset_cat` VALUES (83, '数字高程模型/数字表面模型', 80, 1, NULL, 'A08A01A02', '1', '0', '超级管理员', 1, '2025-04-18 11:31:14', '超级管理员', 1, '2025-04-18 11:31:59', NULL);
INSERT INTO `att_asset_cat` VALUES (84, '倾斜摄影影像/激光云点', 80, 3, NULL, 'A08A01A03', '1', '0', '超级管理员', 1, '2025-04-18 11:31:52', '超级管理员', 1, '2025-04-18 11:31:52', NULL);
INSERT INTO `att_asset_cat` VALUES (85, '水下地形', 80, 4, NULL, 'A08A01A04', '1', '0', '超级管理员', 1, '2025-04-18 11:32:21', '超级管理员', 1, '2025-04-18 11:32:21', NULL);
INSERT INTO `att_asset_cat` VALUES (86, '建筑信息模型', 80, 5, NULL, 'A08A01A05', '1', '0', '超级管理员', 1, '2025-04-18 11:32:38', '超级管理员', 1, '2025-04-18 11:32:38', NULL);
INSERT INTO `att_asset_cat` VALUES (87, '其他地理空间数据', 81, 0, NULL, 'A08A02A01', '1', '0', '超级管理员', 1, '2025-04-18 11:32:59', '超级管理员', 1, '2025-04-18 11:32:59', NULL);
INSERT INTO `att_asset_cat` VALUES (88, '业务数据', 0, 2, NULL, 'A09', '1', '0', '超级管理员', 1, '2025-04-18 11:34:01', '超级管理员', 1, '2025-04-18 11:34:01', NULL);
INSERT INTO `att_asset_cat` VALUES (89, '水文', 88, 0, NULL, 'A09A01', '1', '0', '超级管理员', 1, '2025-04-18 11:34:41', '超级管理员', 1, '2025-04-18 11:34:41', NULL);
INSERT INTO `att_asset_cat` VALUES (90, '实时', 89, 0, NULL, 'A09A01A01', '1', '0', '超级管理员', 1, '2025-04-18 11:34:55', '超级管理员', 1, '2025-04-18 11:34:55', NULL);
INSERT INTO `att_asset_cat` VALUES (91, '整编', 89, 1, NULL, 'A09A01A02', '1', '0', '超级管理员', 1, '2025-04-18 11:35:08', '超级管理员', 1, '2025-04-18 11:35:08', NULL);
INSERT INTO `att_asset_cat` VALUES (92, '预报', 89, 2, NULL, 'A09A01A03', '1', '0', '超级管理员', 1, '2025-04-18 11:35:25', '超级管理员', 1, '2025-04-18 11:35:25', NULL);
INSERT INTO `att_asset_cat` VALUES (93, '预警', 89, 4, NULL, 'A09A01A04', '1', '0', '超级管理员', 1, '2025-04-18 11:35:42', '超级管理员', 1, '2025-04-18 11:35:42', NULL);
INSERT INTO `att_asset_cat` VALUES (94, '水资源', 88, 1, NULL, 'A09A02', '1', '0', '超级管理员', 1, '2025-04-18 11:36:32', '超级管理员', 1, '2025-04-18 11:36:32', NULL);
INSERT INTO `att_asset_cat` VALUES (95, '开发', 94, 0, NULL, 'A09A02A01', '1', '0', '超级管理员', 1, '2025-04-18 11:36:53', '超级管理员', 1, '2025-04-18 11:36:53', NULL);
INSERT INTO `att_asset_cat` VALUES (96, '利用', 94, 1, NULL, 'A09A02A02', '1', '0', '超级管理员', 1, '2025-04-18 11:38:49', '超级管理员', 1, '2025-04-18 11:38:49', NULL);
INSERT INTO `att_asset_cat` VALUES (97, '保护', 94, 2, NULL, 'A09A02A03', '1', '0', '超级管理员', 1, '2025-04-18 11:39:06', '超级管理员', 1, '2025-04-18 11:39:06', NULL);
INSERT INTO `att_asset_cat` VALUES (98, '配置', 94, 3, NULL, 'A09A02A04', '1', '0', '超级管理员', 1, '2025-04-18 11:39:22', '超级管理员', 1, '2025-04-18 11:39:22', NULL);
INSERT INTO `att_asset_cat` VALUES (99, '节约', 94, 4, NULL, 'A09A02A05', '1', '0', '超级管理员', 1, '2025-04-18 11:39:32', '超级管理员', 1, '2025-04-18 11:39:32', NULL);
INSERT INTO `att_asset_cat` VALUES (100, '计量', 94, 5, NULL, 'A09A02A06', '0', '0', '超级管理员', 1, '2025-04-18 11:39:45', '超级管理员', 1, '2025-04-18 11:39:45', NULL);
INSERT INTO `att_asset_cat` VALUES (101, '评价', 94, 6, NULL, 'A09A02A07', '1', '0', '超级管理员', 1, '2025-04-18 11:39:59', '超级管理员', 1, '2025-04-18 11:39:59', NULL);
INSERT INTO `att_asset_cat` VALUES (102, '预测', 94, 7, NULL, 'A09A02A08', '1', '0', '超级管理员', 1, '2025-04-18 11:40:15', '超级管理员', 1, '2025-04-18 11:40:15', NULL);
INSERT INTO `att_asset_cat` VALUES (103, '调度', 94, 8, NULL, 'A09A02A09', '1', '0', '超级管理员', 1, '2025-04-18 11:40:26', '超级管理员', 1, '2025-04-18 11:40:26', NULL);
INSERT INTO `att_asset_cat` VALUES (104, '水环境', 88, 2, NULL, 'A09A03', '1', '0', '超级管理员', 1, '2025-04-18 11:44:07', '超级管理员', 1, '2025-04-18 11:44:07', NULL);
INSERT INTO `att_asset_cat` VALUES (105, '水环境调查', 104, 0, NULL, 'A09A03A01', '1', '0', '超级管理员', 1, '2025-04-18 11:44:28', '超级管理员', 1, '2025-04-18 11:44:28', NULL);
INSERT INTO `att_asset_cat` VALUES (106, '水环境评估', 104, 1, NULL, 'A09A03A02', '1', '0', '超级管理员', 1, '2025-04-18 11:44:48', '超级管理员', 1, '2025-04-18 11:44:48', NULL);
INSERT INTO `att_asset_cat` VALUES (107, '水环境治理', 104, 2, NULL, 'A09A03A03', '1', '0', '超级管理员', 1, '2025-04-18 11:45:26', '超级管理员', 1, '2025-04-18 11:46:57', NULL);
INSERT INTO `att_asset_cat` VALUES (108, '水利工程', 88, 3, NULL, 'A09A04', '1', '0', '超级管理员', 1, '2025-04-18 11:46:24', '超级管理员', 1, '2025-04-18 11:46:24', NULL);
INSERT INTO `att_asset_cat` VALUES (109, '灌溉', 108, 0, NULL, 'A09A04A01', '1', '0', '超级管理员', 1, '2025-04-18 11:46:45', '超级管理员', 1, '2025-04-18 11:46:45', NULL);
INSERT INTO `att_asset_cat` VALUES (110, '给排水', 108, 1, NULL, 'A09A04A02', '1', '0', '超级管理员', 1, '2025-04-18 13:33:58', '超级管理员', 1, '2025-04-18 13:33:58', NULL);
INSERT INTO `att_asset_cat` VALUES (111, '发电', 108, 2, NULL, 'A09A04A03', '1', '0', '超级管理员', 1, '2025-04-18 13:34:11', '超级管理员', 1, '2025-04-18 13:34:11', NULL);
INSERT INTO `att_asset_cat` VALUES (112, '航运', 108, 3, NULL, 'A09A04A04', '1', '0', '超级管理员', 1, '2025-04-18 13:34:29', '超级管理员', 1, '2025-04-18 13:34:29', NULL);
INSERT INTO `att_asset_cat` VALUES (113, '防洪', 108, 4, NULL, 'A09A04A05', '1', '0', '超级管理员', 1, '2025-04-18 13:34:44', '超级管理员', 1, '2025-04-18 13:34:44', NULL);
INSERT INTO `att_asset_cat` VALUES (114, '挡潮', 108, 5, NULL, 'A09A04A06', '1', '0', '超级管理员', 1, '2025-04-18 13:35:01', '超级管理员', 1, '2025-04-18 13:35:01', NULL);
INSERT INTO `att_asset_cat` VALUES (115, '农村水利', 88, 5, NULL, 'A09A05', '1', '0', '超级管理员', 1, '2025-04-18 13:37:12', '超级管理员', 1, '2025-04-18 13:37:41', NULL);
INSERT INTO `att_asset_cat` VALUES (116, '灌溉', 115, 0, NULL, 'A09A05A01', '1', '0', '超级管理员', 1, '2025-04-18 13:38:10', '超级管理员', 1, '2025-04-18 13:38:10', NULL);
INSERT INTO `att_asset_cat` VALUES (117, '农村供水', 115, 1, NULL, 'A09A05A02', '1', '0', '超级管理员', 1, '2025-04-18 13:38:26', '超级管理员', 1, '2025-04-18 13:38:26', NULL);
INSERT INTO `att_asset_cat` VALUES (118, '农村水电', 115, 2, NULL, 'A09A05A03', '1', '0', '超级管理员', 1, '2025-04-18 13:38:41', '超级管理员', 1, '2025-04-18 13:38:41', NULL);
INSERT INTO `att_asset_cat` VALUES (119, '水旱灾害防御', 88, 6, NULL, 'A09A06', '1', '0', '超级管理员', 1, '2025-04-18 13:40:58', '超级管理员', 1, '2025-04-18 13:40:58', NULL);
INSERT INTO `att_asset_cat` VALUES (120, '水情旱情预警', 119, 0, NULL, 'A09A06A01', '1', '0', '超级管理员', 1, '2025-04-18 13:41:19', '超级管理员', 1, '2025-04-18 13:41:19', NULL);
INSERT INTO `att_asset_cat` VALUES (121, '工程调度', 119, 1, NULL, 'A09A06A02', '1', '0', '超级管理员', 1, '2025-04-18 13:41:37', '超级管理员', 1, '2025-04-18 13:41:37', NULL);
INSERT INTO `att_asset_cat` VALUES (122, '应急抢险技术支撑', 119, 2, NULL, 'A09A06A03', '1', '0', '超级管理员', 1, '2025-04-18 13:42:02', '超级管理员', 1, '2025-04-18 13:42:02', NULL);
INSERT INTO `att_asset_cat` VALUES (123, '综合分析评价', 119, 4, NULL, 'A09A06A04', '1', '0', '超级管理员', 1, '2025-04-18 13:42:30', '超级管理员', 1, '2025-04-18 13:42:30', NULL);
INSERT INTO `att_asset_cat` VALUES (124, '管理信息', 119, 5, NULL, 'A09A06A05', '1', '0', '超级管理员', 1, '2025-04-18 13:42:45', '超级管理员', 1, '2025-04-18 13:42:45', NULL);
INSERT INTO `att_asset_cat` VALUES (125, '基础信息', 119, 6, NULL, 'A09A06A06', '1', '0', '超级管理员', 1, '2025-04-18 13:42:59', '超级管理员', 1, '2025-04-18 13:42:59', NULL);
INSERT INTO `att_asset_cat` VALUES (126, '工程险情', 119, 7, NULL, 'A09A06A07', '1', '0', '超级管理员', 1, '2025-04-18 13:43:30', '超级管理员', 1, '2025-04-18 13:43:52', NULL);
INSERT INTO `att_asset_cat` VALUES (127, '水旱灾情', 119, 8, NULL, 'A09A06A08', '1', '0', '超级管理员', 1, '2025-04-18 13:43:48', '超级管理员', 1, '2025-04-18 13:43:48', NULL);
INSERT INTO `att_asset_cat` VALUES (128, '河湖保护', 88, 7, NULL, 'A09A07', '1', '0', '超级管理员', 1, '2025-04-18 13:44:33', '超级管理员', 1, '2025-04-18 13:44:33', NULL);
INSERT INTO `att_asset_cat` VALUES (129, '河湖长制', 128, 0, NULL, 'A09A07A01', '1', '0', '超级管理员', 1, '2025-04-18 13:44:52', '超级管理员', 1, '2025-04-18 13:44:52', NULL);
INSERT INTO `att_asset_cat` VALUES (130, '水域岸线', 128, 1, NULL, 'A09A07A02', '1', '0', '超级管理员', 1, '2025-04-18 13:45:13', '超级管理员', 1, '2025-04-18 13:45:13', NULL);
INSERT INTO `att_asset_cat` VALUES (131, '采砂', 128, 2, NULL, 'A09A07A03', '1', '0', '超级管理员', 1, '2025-04-18 13:45:27', '超级管理员', 1, '2025-04-18 13:45:27', NULL);
INSERT INTO `att_asset_cat` VALUES (138, '移民', 88, 9, NULL, 'A09A09', '1', '0', '超级管理员', 1, '2025-04-18 13:51:32', '超级管理员', 1, '2025-04-18 13:51:32', NULL);
INSERT INTO `att_asset_cat` VALUES (141, '移民后期扶持', 138, 2, NULL, 'A09A09A03', '1', '0', '超级管理员', 1, '2025-04-18 13:52:13', '超级管理员', 1, '2025-04-18 13:52:13', NULL);
INSERT INTO `att_asset_cat` VALUES (142, '节约用水', 88, 10, NULL, 'A09A10', '1', '0', '超级管理员', 1, '2025-04-18 13:52:49', '超级管理员', 1, '2025-04-21 11:17:29', NULL);
INSERT INTO `att_asset_cat` VALUES (145, '用水定额', 142, 2, NULL, 'A09A10A03', '1', '0', '超级管理员', 1, '2025-04-18 13:53:33', '超级管理员', 1, '2025-04-18 13:53:33', NULL);
INSERT INTO `att_asset_cat` VALUES (146, '计划用水', 142, 3, NULL, 'A09A10A04', '1', '0', '超级管理员', 1, '2025-04-18 13:53:46', '超级管理员', 1, '2025-04-18 13:53:46', NULL);
INSERT INTO `att_asset_cat` VALUES (147, '节水评价', 142, 4, NULL, 'A09A10A05', '1', '0', '超级管理员', 1, '2025-04-18 13:54:01', '超级管理员', 1, '2025-04-18 13:54:01', NULL);
INSERT INTO `att_asset_cat` VALUES (148, '节水载体', 142, 5, NULL, 'A09A10A06', '1', '0', '超级管理员', 1, '2025-04-18 13:54:14', '超级管理员', 1, '2025-04-18 13:54:14', NULL);
INSERT INTO `att_asset_cat` VALUES (149, '行政管理数据', 0, 4, NULL, 'A10', '1', '0', '超级管理员', 1, '2025-04-21 09:19:46', '超级管理员', 1, '2025-04-21 09:19:55', NULL);
INSERT INTO `att_asset_cat` VALUES (150, '综合办公', 149, 0, NULL, 'A10A01', '1', '0', '超级管理员', 1, '2025-04-21 09:20:21', '超级管理员', 1, '2025-04-21 09:20:21', NULL);
INSERT INTO `att_asset_cat` VALUES (151, '政策与法规', 149, 1, NULL, 'A10A02', '1', '0', '超级管理员', 1, '2025-04-21 09:20:42', '超级管理员', 1, '2025-04-21 09:20:51', NULL);
INSERT INTO `att_asset_cat` VALUES (152, '财务与审计', 149, 2, NULL, 'A10A03', '1', '0', '超级管理员', 1, '2025-04-21 09:21:11', '超级管理员', 1, '2025-04-21 09:21:11', NULL);
INSERT INTO `att_asset_cat` VALUES (153, '人事与教育', 149, 3, NULL, 'A10A04', '1', '0', '超级管理员', 1, '2025-04-21 09:21:28', '超级管理员', 1, '2025-04-21 09:21:28', NULL);
INSERT INTO `att_asset_cat` VALUES (154, '国际合作', 149, 5, NULL, 'A10A05', '1', '0', '超级管理员', 1, '2025-04-21 09:25:58', '超级管理员', 1, '2025-04-21 09:25:58', NULL);
INSERT INTO `att_asset_cat` VALUES (155, '科技管理', 149, 6, NULL, 'A10A06', '1', '0', '超级管理员', 1, '2025-04-21 09:26:18', '超级管理员', 1, '2025-04-21 09:26:18', NULL);
INSERT INTO `att_asset_cat` VALUES (156, '监督', 149, 7, NULL, 'A10A07', '1', '0', '超级管理员', 1, '2025-04-21 09:26:46', '超级管理员', 1, '2025-04-21 09:26:46', NULL);
INSERT INTO `att_asset_cat` VALUES (157, '纪检与监察', 149, 8, NULL, 'A10A08', '1', '0', '超级管理员', 1, '2025-04-21 09:27:32', '超级管理员', 1, '2025-04-21 09:27:32', NULL);
INSERT INTO `att_asset_cat` VALUES (158, '机关管理', 149, 9, NULL, 'A10A09', '1', '0', '超级管理员', 1, '2025-04-21 09:27:59', '超级管理员', 1, '2025-04-21 11:23:21', NULL);
INSERT INTO `att_asset_cat` VALUES (159, '信息系统运行和安全数据', 149, 10, NULL, 'A10A10', '1', '0', '超级管理员', 1, '2025-04-21 09:28:38', '超级管理员', 1, '2025-04-21 09:28:38', NULL);
INSERT INTO `att_asset_cat` VALUES (160, '公文处理数据', 150, 0, NULL, 'A10A01A01', '1', '0', '超级管理员', 1, '2025-04-21 09:31:34', '超级管理员', 1, '2025-04-21 09:31:34', NULL);
INSERT INTO `att_asset_cat` VALUES (161, '水利信访数据', 150, 1, NULL, 'A10A01A02', '1', '0', '超级管理员', 1, '2025-04-21 09:31:56', '超级管理员', 1, '2025-04-21 09:31:56', NULL);
INSERT INTO `att_asset_cat` VALUES (162, '印章管理数据', 150, 2, NULL, 'A10A01A03', '1', '0', '超级管理员', 1, '2025-04-21 09:32:17', '超级管理员', 1, '2025-04-21 09:58:35', NULL);
INSERT INTO `att_asset_cat` VALUES (163, '政务公开数据', 150, 4, NULL, 'A10A01A04', '1', '0', '超级管理员', 1, '2025-04-21 09:32:38', '超级管理员', 1, '2025-04-21 09:32:38', NULL);
INSERT INTO `att_asset_cat` VALUES (164, '档案管理数据', 150, 5, NULL, 'A10A01A05', '1', '0', '超级管理员', 1, '2025-04-21 09:32:59', '超级管理员', 1, '2025-04-21 09:33:12', NULL);
INSERT INTO `att_asset_cat` VALUES (165, '会议管理数据', 150, 6, NULL, 'A10A01A06', '1', '0', '超级管理员', 1, '2025-04-21 09:33:31', '超级管理员', 1, '2025-04-21 09:33:31', NULL);
INSERT INTO `att_asset_cat` VALUES (166, '行政审批数据', 150, 7, NULL, 'A10A01A07', '1', '0', '超级管理员', 1, '2025-04-21 09:33:50', '超级管理员', 1, '2025-04-21 09:33:50', NULL);
INSERT INTO `att_asset_cat` VALUES (167, '法律制定、修订数据', 151, 0, NULL, 'A10A02A01', '1', '0', '超级管理员', 1, '2025-04-21 09:34:25', '超级管理员', 1, '2025-04-21 09:34:25', NULL);
INSERT INTO `att_asset_cat` VALUES (168, '行政法规和法规性文件制定、修订数据', 151, 1, NULL, 'A10A02A02', '1', '0', '超级管理员', 1, '2025-04-21 09:34:56', '超级管理员', 1, '2025-04-21 09:34:56', NULL);
INSERT INTO `att_asset_cat` VALUES (169, '政策制定数据', 151, 2, NULL, 'A10A02A03', '1', '0', '超级管理员', 1, '2025-04-21 09:35:10', '超级管理员', 1, '2025-04-21 09:35:10', NULL);
INSERT INTO `att_asset_cat` VALUES (170, '部门规章制定、修订数据', 151, 4, NULL, 'A10A02A04', '1', '0', '超级管理员', 1, '2025-04-21 09:35:34', '超级管理员', 1, '2025-04-21 09:35:34', NULL);
INSERT INTO `att_asset_cat` VALUES (171, '水行政执法数据', 151, 5, NULL, 'A10A02A05', '0', '0', '超级管理员', 1, '2025-04-21 09:35:59', '超级管理员', 1, '2025-04-21 09:35:59', NULL);
INSERT INTO `att_asset_cat` VALUES (172, '水政监察数据', 151, 6, NULL, 'A10A02A06', '1', '0', '超级管理员', 1, '2025-04-21 09:36:18', '超级管理员', 1, '2025-04-21 09:36:18', NULL);
INSERT INTO `att_asset_cat` VALUES (173, '财务管理数据', 152, 0, NULL, 'A10A03A01', '1', '0', '超级管理员', 1, '2025-04-21 09:36:39', '超级管理员', 1, '2025-04-21 09:36:49', NULL);
INSERT INTO `att_asset_cat` VALUES (174, '资产监管数据', 152, 1, NULL, 'A10A03A02', '1', '0', '超级管理员', 1, '2025-04-21 09:37:05', '超级管理员', 1, '2025-04-21 09:37:05', NULL);
INSERT INTO `att_asset_cat` VALUES (175, '预算管理数据', 152, 2, NULL, 'A10A03A03', '1', '0', '超级管理员', 1, '2025-04-21 09:37:19', '超级管理员', 1, '2025-04-21 09:37:19', NULL);
INSERT INTO `att_asset_cat` VALUES (176, '绩效管理数据', 152, 4, NULL, 'A10A03A04', '1', '0', '超级管理员', 1, '2025-04-21 09:37:34', '超级管理员', 1, '2025-04-21 09:37:39', NULL);
INSERT INTO `att_asset_cat` VALUES (177, '编制管理数据', 153, 0, NULL, 'A10A04A01', '1', '0', '超级管理员', 1, '2025-04-21 09:38:04', '超级管理员', 1, '2025-04-21 09:38:04', NULL);
INSERT INTO `att_asset_cat` VALUES (178, '机构改革数据', 153, 1, NULL, 'A10A04A02', '1', '0', '超级管理员', 1, '2025-04-21 09:38:18', '超级管理员', 1, '2025-04-21 09:38:18', NULL);
INSERT INTO `att_asset_cat` VALUES (179, '劳动保护数据', 153, 2, NULL, 'A10A04A03', '1', '0', '超级管理员', 1, '2025-04-21 09:38:34', '超级管理员', 1, '2025-04-21 09:38:34', NULL);
INSERT INTO `att_asset_cat` VALUES (180, '卫生保障数据', 153, 4, NULL, 'A10A04A04', '1', '0', '超级管理员', 1, '2025-04-21 09:38:48', '超级管理员', 1, '2025-04-21 09:39:14', NULL);
INSERT INTO `att_asset_cat` VALUES (181, '医疗休养数据', 153, 5, NULL, 'A10A04A05', '1', '0', '超级管理员', 1, '2025-04-21 09:39:08', '超级管理员', 1, '2025-04-21 09:39:08', NULL);
INSERT INTO `att_asset_cat` VALUES (182, '离退休数据', 153, 7, NULL, 'A10A04A06', '1', '0', '超级管理员', 1, '2025-04-21 09:39:31', '超级管理员', 1, '2025-04-21 09:39:31', NULL);
INSERT INTO `att_asset_cat` VALUES (185, '水土保持', 88, 8, NULL, 'A09A12', '1', '0', '超级管理员', 1, '2025-04-21 09:55:22', '超级管理员', 1, '2025-04-21 09:55:22', NULL);
INSERT INTO `att_asset_cat` VALUES (186, '人才招聘数据', 153, 9, NULL, 'A10A04A08', '1', '0', '超级管理员', 1, '2025-04-21 09:59:40', '超级管理员', 1, '2025-04-21 10:00:43', NULL);
INSERT INTO `att_asset_cat` VALUES (187, '经济技术合作数据', 154, 0, NULL, 'A10A05A01', '1', '0', '超级管理员', 1, '2025-04-21 10:30:22', '超级管理员', 1, '2025-04-21 10:30:22', NULL);
INSERT INTO `att_asset_cat` VALUES (188, '智力引进数据', 154, 1, NULL, 'A10A05A02', '1', '0', '超级管理员', 1, '2025-04-21 10:30:44', '超级管理员', 1, '2025-04-21 10:30:44', NULL);
INSERT INTO `att_asset_cat` VALUES (189, '国际河流/湖泊数据', 154, 3, NULL, 'A10A05A03', '1', '0', '超级管理员', 1, '2025-04-21 10:31:06', '超级管理员', 1, '2025-04-21 10:31:06', NULL);
INSERT INTO `att_asset_cat` VALUES (190, '科技项目管理数据', 155, 0, NULL, 'A10A06A01', '1', '0', '超级管理员', 1, '2025-04-21 10:31:34', '超级管理员', 1, '2025-04-21 10:31:34', NULL);
INSERT INTO `att_asset_cat` VALUES (191, '科技成果评估数据', 155, 1, NULL, 'A10A06A02', '1', '0', '超级管理员', 1, '2025-04-21 10:31:55', '超级管理员', 1, '2025-04-21 10:31:55', NULL);
INSERT INTO `att_asset_cat` VALUES (192, '科技推广数据', 155, 2, NULL, 'A10A06A03', '1', '0', '超级管理员', 1, '2025-04-21 10:32:11', '超级管理员', 1, '2025-04-21 10:32:11', NULL);
INSERT INTO `att_asset_cat` VALUES (193, '创新平台数据', 155, 3, NULL, 'A10A06A04', '1', '0', '超级管理员', 1, '2025-04-21 10:32:31', '超级管理员', 1, '2025-04-21 10:32:31', NULL);
INSERT INTO `att_asset_cat` VALUES (194, '标准化数据', 155, 5, NULL, 'A10A06A05', '1', '0', '超级管理员', 1, '2025-04-21 10:33:02', '超级管理员', 1, '2025-04-21 10:33:02', NULL);
INSERT INTO `att_asset_cat` VALUES (195, '监督检查体系建设数据', 156, 0, NULL, 'A10A07A01', '1', '0', '超级管理员', 1, '2025-04-21 10:33:49', '超级管理员', 1, '2025-04-21 10:33:49', NULL);
INSERT INTO `att_asset_cat` VALUES (196, '节约用水督查数据', 156, 1, NULL, 'A10A07A02', '1', '0', '超级管理员', 1, '2025-04-21 10:35:11', '超级管理员', 1, '2025-04-21 10:35:11', NULL);
INSERT INTO `att_asset_cat` VALUES (197, '水土流失', 185, 0, NULL, 'A09A12A01', '1', '0', '超级管理员', 1, '2025-04-21 11:16:11', '超级管理员', 1, '2025-04-21 11:16:11', NULL);
INSERT INTO `att_asset_cat` VALUES (198, '综合治理', 185, 1, NULL, 'A09A12A02', '1', '0', '超级管理员', 1, '2025-04-21 11:16:30', '超级管理员', 1, '2025-04-21 11:16:30', NULL);
INSERT INTO `att_asset_cat` VALUES (199, '预防监督', 185, 2, NULL, 'A09A12A03', '1', '0', '超级管理员', 1, '2025-04-21 11:16:43', '超级管理员', 1, '2025-04-21 11:16:43', NULL);
INSERT INTO `att_asset_cat` VALUES (200, '综合信息', 185, 4, NULL, 'A09A12A04', '1', '0', '超级管理员', 1, '2025-04-21 11:16:56', '超级管理员', 1, '2025-04-21 11:16:56', NULL);
INSERT INTO `att_asset_cat` VALUES (201, '基础信息', 185, 5, NULL, 'A09A12A05', '1', '0', '超级管理员', 1, '2025-04-21 11:17:08', '超级管理员', 1, '2025-04-21 11:17:08', NULL);
INSERT INTO `att_asset_cat` VALUES (202, '移民征地', 138, 0, NULL, 'A09A09A04', '1', '0', '超级管理员', 1, '2025-04-21 11:17:52', '超级管理员', 1, '2025-04-21 11:17:52', NULL);
INSERT INTO `att_asset_cat` VALUES (203, '移民安置', 138, 1, NULL, 'A09A09A05', '1', '0', '超级管理员', 1, '2025-04-21 11:18:06', '超级管理员', 1, '2025-04-21 11:18:06', NULL);
INSERT INTO `att_asset_cat` VALUES (204, '用水量', 142, 0, NULL, 'A09A10A07', '1', '0', '超级管理员', 1, '2025-04-21 11:18:29', '超级管理员', 1, '2025-04-21 11:18:29', NULL);
INSERT INTO `att_asset_cat` VALUES (205, '用水效率', 142, 1, NULL, 'A09A10A08', '1', '0', '超级管理员', 1, '2025-04-21 11:18:45', '超级管理员', 1, '2025-04-21 11:18:45', NULL);
INSERT INTO `att_asset_cat` VALUES (206, '职称评审数据', 153, 10, NULL, 'A10A04A09', '1', '0', '超级管理员', 1, '2025-04-21 11:19:43', '超级管理员', 1, '2025-04-21 11:19:43', NULL);
INSERT INTO `att_asset_cat` VALUES (207, '岗位聘用数据', 153, 11, NULL, 'A10A04A10', '1', '0', '超级管理员', 1, '2025-04-21 11:20:04', '超级管理员', 1, '2025-04-21 11:20:04', NULL);
INSERT INTO `att_asset_cat` VALUES (208, '人事档案管理数据', 153, 12, NULL, 'A10A04A11', '1', '0', '超级管理员', 1, '2025-04-21 11:20:20', '超级管理员', 1, '2025-04-21 11:20:20', NULL);
INSERT INTO `att_asset_cat` VALUES (209, '教育培训数据', 153, 14, NULL, 'A10A04A12', '1', '0', '超级管理员', 1, '2025-04-21 11:21:53', '超级管理员', 1, '2025-04-21 11:21:53', NULL);
INSERT INTO `att_asset_cat` VALUES (210, '人事考核数据', 153, 15, NULL, 'A10A04A13', '1', '0', '超级管理员', 1, '2025-04-21 11:22:12', '超级管理员', 1, '2025-04-21 11:22:12', NULL);
INSERT INTO `att_asset_cat` VALUES (211, '职务任免数据', 153, 16, NULL, 'A10A04A14', '1', '0', '超级管理员', 1, '2025-04-21 11:22:29', '超级管理员', 1, '2025-04-21 11:22:29', NULL);
INSERT INTO `att_asset_cat` VALUES (212, '水资源管理督查数据', 156, 3, NULL, 'A10A07A03', '1', '0', '超级管理员', 1, '2025-04-21 11:28:43', '超级管理员', 1, '2025-04-21 11:28:43', NULL);
INSERT INTO `att_asset_cat` VALUES (213, '水利建设与管理督查数据', 156, 4, NULL, 'A10A07A04', '1', '0', '超级管理员', 1, '2025-04-21 11:29:36', '超级管理员', 1, '2025-04-21 11:29:36', NULL);
INSERT INTO `att_asset_cat` VALUES (214, '水利工程质量监督数据', 156, 5, NULL, 'A10A07A05', '1', '0', '超级管理员', 1, '2025-04-21 11:30:07', '超级管理员', 1, '2025-04-21 11:30:07', NULL);
INSERT INTO `att_asset_cat` VALUES (215, '水利行业安全生产监督数据', 156, 6, NULL, 'A10A07A06', '1', '0', '超级管理员', 1, '2025-04-21 11:30:50', '超级管理员', 1, '2025-04-21 11:30:50', NULL);
INSERT INTO `att_asset_cat` VALUES (216, '水土保持问题监督数据', 156, 7, NULL, 'A10A07A07', '1', '0', '超级管理员', 1, '2025-04-21 13:36:21', '超级管理员', 1, '2025-04-21 13:36:21', NULL);
INSERT INTO `att_asset_cat` VALUES (217, '农村饮水问题监督数据', 156, 8, NULL, 'A10A07A08', '1', '0', '超级管理员', 1, '2025-04-21 13:36:50', '超级管理员', 1, '2025-04-21 13:36:50', NULL);
INSERT INTO `att_asset_cat` VALUES (218, '水利建设市场监督数据', 156, 10, NULL, 'A10A07A09', '1', '0', '超级管理员', 1, '2025-04-21 13:37:10', '超级管理员', 1, '2025-04-21 13:37:10', NULL);
INSERT INTO `att_asset_cat` VALUES (219, '纪检数据', 157, 0, NULL, 'A10A08A01', '1', '0', '超级管理员', 1, '2025-04-21 13:37:33', '超级管理员', 1, '2025-04-21 13:37:33', NULL);
INSERT INTO `att_asset_cat` VALUES (220, '执纪问责数据', 157, 1, NULL, 'A10A08A02', '1', '0', '超级管理员', 1, '2025-04-21 13:37:57', '超级管理员', 1, '2025-04-21 13:37:57', NULL);
INSERT INTO `att_asset_cat` VALUES (221, '干部监督数据', 157, 2, NULL, 'A10A08A03', '1', '0', '超级管理员', 1, '2025-04-21 13:38:18', '超级管理员', 1, '2025-04-21 13:38:18', NULL);
INSERT INTO `att_asset_cat` VALUES (222, '民主评议数据', 157, 4, NULL, 'A10A08A04', '1', '0', '超级管理员', 1, '2025-04-21 13:38:34', '超级管理员', 1, '2025-04-21 13:38:34', NULL);
INSERT INTO `att_asset_cat` VALUES (223, '财务管理数据', 158, 0, NULL, 'A10A09A01', '1', '0', '超级管理员', 1, '2025-04-21 13:38:58', '超级管理员', 1, '2025-04-21 13:38:58', NULL);
INSERT INTO `att_asset_cat` VALUES (224, '资产管理数据', 158, 1, NULL, 'A10A09A02', '1', '0', '超级管理员', 1, '2025-04-21 13:39:14', '超级管理员', 1, '2025-04-21 13:39:14', NULL);
INSERT INTO `att_asset_cat` VALUES (225, '后勤管理数据', 158, 2, NULL, 'A10A09A03', '1', '0', '超级管理员', 1, '2025-04-21 13:39:32', '超级管理员', 1, '2025-04-21 13:39:32', NULL);
INSERT INTO `att_asset_cat` VALUES (226, '系统数据', 159, 0, NULL, 'A10A10A01', '1', '0', '超级管理员', 1, '2025-04-21 13:39:45', '超级管理员', 1, '2025-04-21 13:40:06', NULL);
INSERT INTO `att_asset_cat` VALUES (227, '系统运行安全数据', 159, 1, NULL, 'A10A10A02', '1', '0', '超级管理员', 1, '2025-04-21 13:40:02', '超级管理员', 1, '2025-04-21 13:40:02', NULL);
INSERT INTO `att_asset_cat` VALUES (228, '示例目录', 0, 0, '千数开源示例', 'A01', '0', '0', 'qData', 1, '2025-05-23 16:30:25', '管理员', 1, '2025-05-29 06:21:03', NULL);

-- ----------------------------
-- Table structure for att_audit_rule
-- ----------------------------
DROP TABLE IF EXISTS `att_audit_rule`;
CREATE TABLE `att_audit_rule`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '规则ID',
  `name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '规则名称',
  `quality_dim` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '质量维度;1：完整性，2：准确性，3：一致性，4：规范性，5：时效性',
  `type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '规则类型;1：字符校验，2：数值校验，3：空值校验，4：长度校验，5：重复检查，6：格式检查',
  `level` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '规则级别;1：字段级，2：表级',
  `description` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '规则描述',
  `valid_flag` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '1' COMMENT '是否有效;0：无效，1：有效',
  `del_flag` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '删除标志;1：已删除，0：未删除',
  `create_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `creator_id` bigint(20) NULL DEFAULT NULL COMMENT '创建人id',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `updater_id` bigint(20) NULL DEFAULT NULL COMMENT '更新人id',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `ID_90029221152100`(`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '稽查规则表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of att_audit_rule
-- ----------------------------
INSERT INTO `att_audit_rule` VALUES (1, '空值个数，固定值', '1', '3', '1', '取该字段的空值数与固定值进行比较。', '1', '0', NULL, NULL, '2025-01-21 11:35:36', NULL, NULL, '2025-01-21 11:35:36', NULL);
INSERT INTO `att_audit_rule` VALUES (2, '空值校验', '1', '3', '1', '空值校验', '1', '0', NULL, NULL, '2025-01-22 16:30:09', '孟繁明', 731, '2025-03-07 14:00:54', '12');

-- ----------------------------
-- Table structure for att_clean_rule
-- ----------------------------
DROP TABLE IF EXISTS `att_clean_rule`;
CREATE TABLE `att_clean_rule`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '规则ID',
  `name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '规则名称',
  `type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '规则类型;1：字符串转化，2：数值处理，3：时间处理，4：重复值处理，5：空值处理',
  `level` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '规则级别;1：字段级，2：表级',
  `description` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '规则描述',
  `valid_flag` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '1' COMMENT '是否有效;0：无效，1：有效',
  `del_flag` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '删除标志;1：已删除，0：未删除',
  `create_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `creator_id` bigint(20) NULL DEFAULT NULL COMMENT '创建人id',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `updater_id` bigint(20) NULL DEFAULT NULL COMMENT '更新人id',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `ID_90030177089800`(`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 15 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '清洗规则表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of att_clean_rule
-- ----------------------------
INSERT INTO `att_clean_rule` VALUES (8, '大小写转换', '1', '1', '把大写字符串数据转换成小写字符串数据', '1', '0', '舒月鑫', 733, '2025-02-25 10:47:40', '舒月鑫', 733, '2025-02-25 10:47:40', NULL);
INSERT INTO `att_clean_rule` VALUES (9, '空值替换', '1', '1', '空值用别的数据进行替换', '1', '0', '舒月鑫', 733, '2025-02-25 10:48:10', '舒月鑫', 733, '2025-02-25 10:48:10', NULL);
INSERT INTO `att_clean_rule` VALUES (10, '首尾移除空格', '1', '1', '首位的空格进行移除', '1', '0', '舒月鑫', 733, '2025-02-25 10:48:36', '孟繁明', 731, '2025-03-07 14:00:58', NULL);
INSERT INTO `att_clean_rule` VALUES (14, '正则表达式替换', '1', '1', '正则表达式替换', '1', '0', '超级管理员', 1, '2025-04-11 10:30:13', '超级管理员', 1, '2025-04-11 10:30:13', NULL);

-- ----------------------------
-- Table structure for att_client
-- ----------------------------
DROP TABLE IF EXISTS `att_client`;
CREATE TABLE `att_client`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '应用名称',
  `type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '应用类型',
  `secret` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '应用秘钥',
  `homepage_url` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '主页地址',
  `allow_url` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '允许授权的url',
  `sync_url` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '同步地址',
  `logo` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '应用图标',
  `description` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '应用描述',
  `public_flag` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '1' COMMENT '是否公开',
  `valid_flag` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '1' COMMENT '是否有效;0：无效，1：有效',
  `del_flag` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '删除标志;1：已删除，0：未删除',
  `create_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `creator_id` bigint(20) NULL DEFAULT NULL COMMENT '创建人id',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `updater_id` bigint(20) NULL DEFAULT NULL COMMENT '更新人id',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `ID_90030681894600`(`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '应用管理' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of att_client
-- ----------------------------
INSERT INTO `att_client` VALUES (6, '示例应用', '0', 'f6fc9d700d0f4de2ac13426ab0bf2c03', NULL, NULL, NULL, NULL, '示例应用', '1', '1', '0', 'qData', 1, '2025-05-23 16:30:42', '管理员', 1, '2025-05-29 06:22:49', NULL);

-- ----------------------------
-- Table structure for att_data_dev_cat
-- ----------------------------
DROP TABLE IF EXISTS `att_data_dev_cat`;
CREATE TABLE `att_data_dev_cat`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '类别名称',
  `parent_id` bigint(20) NULL DEFAULT NULL COMMENT '关联上级ID',
  `sort_order` int(11) NOT NULL DEFAULT 0 COMMENT '类别排序',
  `description` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '描述',
  `code` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '层级编码',
  `valid_flag` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '1' COMMENT '是否有效;0：无效，1：有效',
  `del_flag` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '删除标志;1：已删除，0：未删除',
  `create_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `creator_id` bigint(20) NULL DEFAULT NULL COMMENT '创建人id',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `updater_id` bigint(20) NULL DEFAULT NULL COMMENT '更新人id',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `project_id` bigint(20) NOT NULL COMMENT '项目id',
  `project_code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '项目编码',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `ID_90031083379600`(`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 13 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '数据开发类目管理' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of att_data_dev_cat
-- ----------------------------
INSERT INTO `att_data_dev_cat` VALUES (12, '示例类目', 0, 0, NULL, 'A01', '0', '0', 'qData', 1, '2025-05-23 17:51:50', '管理员', 1, '2025-05-29 06:23:34', NULL, 84, '141883958809440');

-- ----------------------------
-- Table structure for att_data_elem_cat
-- ----------------------------
DROP TABLE IF EXISTS `att_data_elem_cat`;
CREATE TABLE `att_data_elem_cat`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '类别名称',
  `parent_id` bigint(20) NULL DEFAULT 0 COMMENT '关联上级ID',
  `sort_order` int(11) NOT NULL DEFAULT 0 COMMENT '类别排序',
  `description` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '描述',
  `code` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '层级编码',
  `valid_flag` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '1' COMMENT '是否有效;0：无效，1：有效',
  `del_flag` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '删除标志;1：已删除，0：未删除',
  `create_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `creator_id` bigint(20) NULL DEFAULT NULL COMMENT '创建人id',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `updater_id` bigint(20) NULL DEFAULT NULL COMMENT '更新人id',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `ID_90031545195400`(`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 31 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '数据元类目管理' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of att_data_elem_cat
-- ----------------------------
INSERT INTO `att_data_elem_cat` VALUES (8, '水利信息', 0, 1, NULL, 'A03', '1', '0', 'admin', 1, '2025-01-23 11:19:57', 'admin', 1, '2025-02-05 15:47:31', NULL);
INSERT INTO `att_data_elem_cat` VALUES (12, '基础信息', 8, 0, NULL, 'A03A01', '1', '0', 'admin', 1, '2025-01-23 14:00:35', 'admin', 1, '2025-02-05 15:47:35', NULL);
INSERT INTO `att_data_elem_cat` VALUES (14, '河道信息', 12, 1, NULL, 'A03A01A01', '1', '0', 'admin', 1, '2025-02-05 15:50:47', '演示账号', 732, '2025-04-22 09:52:36', NULL);
INSERT INTO `att_data_elem_cat` VALUES (15, '基础信息', 0, 0, NULL, 'A04', '1', '0', 'admin', 1, '2025-02-05 15:51:42', '千桐科技', 1, '2025-05-28 09:26:19', NULL);
INSERT INTO `att_data_elem_cat` VALUES (17, '灌区信息', 12, 0, '', 'A03A01A02', '1', '0', 'admin', 1, '2025-02-05 17:28:24', '千桐科技', 1, '2025-05-28 09:26:33', NULL);
INSERT INTO `att_data_elem_cat` VALUES (24, '示例目录', 0, 0, '千数开源示例', 'A01', '0', '0', 'qData', 1, '2025-05-23 16:30:16', '管理员', 1, '2025-05-29 06:20:49', NULL);
INSERT INTO `att_data_elem_cat` VALUES (25, '订单管理', 0, 4, NULL, 'A05', '0', '0', '千桐科技', 1, '2025-05-30 11:41:00', '千桐科技', 1, '2025-05-30 11:42:58', NULL);
INSERT INTO `att_data_elem_cat` VALUES (26, '下单信息', 25, 1, NULL, 'A05A01', '0', '0', '千桐科技', 1, '2025-05-30 11:41:33', '千桐科技', 1, '2025-05-30 11:41:33', NULL);
INSERT INTO `att_data_elem_cat` VALUES (27, '营销信息', 25, 2, NULL, 'A05A02', '0', '0', '千桐科技', 1, '2025-05-30 11:41:42', '千桐科技', 1, '2025-05-30 11:41:42', NULL);
INSERT INTO `att_data_elem_cat` VALUES (28, '商品信息', 25, 3, NULL, 'A05A03', '0', '0', '千桐科技', 1, '2025-05-30 11:41:51', '千桐科技', 1, '2025-05-30 11:41:51', NULL);
INSERT INTO `att_data_elem_cat` VALUES (29, '会员信息', 25, 5, NULL, 'A05A04', '0', '0', '千桐科技', 1, '2025-05-30 11:42:02', '千桐科技', 1, '2025-05-30 11:42:02', NULL);
INSERT INTO `att_data_elem_cat` VALUES (30, '进销存管理', 0, 6, NULL, 'A06', '0', '0', '千桐科技', 1, '2025-05-30 11:42:48', '千桐科技', 1, '2025-05-30 11:42:48', NULL);

-- ----------------------------
-- Table structure for att_model_cat
-- ----------------------------
DROP TABLE IF EXISTS `att_model_cat`;
CREATE TABLE `att_model_cat`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '类别名称',
  `parent_id` bigint(20) NULL DEFAULT 0 COMMENT '关联上级ID',
  `sort_order` int(11) NOT NULL DEFAULT 0 COMMENT '类别排序',
  `description` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '描述',
  `code` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '层级编码',
  `valid_flag` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '1' COMMENT '是否有效;0：无效，1：有效',
  `del_flag` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '删除标志;1：已删除，0：未删除',
  `create_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `creator_id` bigint(20) NULL DEFAULT NULL COMMENT '创建人id',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `updater_id` bigint(20) NULL DEFAULT NULL COMMENT '更新人id',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `ID_90032032628000`(`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 34 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '逻辑模型类目管理' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of att_model_cat
-- ----------------------------
INSERT INTO `att_model_cat` VALUES (15, '水利基础数据', 0, 1, NULL, 'A03', '1', '0', 'admin', 1, '2025-02-05 11:58:54', 'admin', 1, '2025-02-05 11:58:54', NULL);
INSERT INTO `att_model_cat` VALUES (16, '水利业务数据', 0, 0, NULL, 'A04', '1', '0', 'admin', 1, '2025-02-05 11:59:06', '孟繁明', 731, '2025-03-07 14:01:15', NULL);
INSERT INTO `att_model_cat` VALUES (17, '其他数据', 0, 3, NULL, 'A05', '1', '0', 'admin', 1, '2025-02-05 11:59:16', 'admin', 1, '2025-02-05 11:59:16', NULL);
INSERT INTO `att_model_cat` VALUES (18, '江河湖泊', 15, 1, NULL, 'A03A01', '1', '0', 'admin', 1, '2025-02-05 11:59:34', 'admin', 1, '2025-02-05 11:59:34', NULL);
INSERT INTO `att_model_cat` VALUES (19, '水利工程', 15, 2, NULL, 'A03A02', '1', '0', 'admin', 1, '2025-02-05 11:59:51', 'admin', 1, '2025-02-05 11:59:51', NULL);
INSERT INTO `att_model_cat` VALUES (20, '监测站(点)', 15, 1, NULL, 'A03A03', '1', '0', 'admin', 1, '2025-02-05 12:00:12', 'admin', 1, '2025-02-05 17:49:31', NULL);
INSERT INTO `att_model_cat` VALUES (21, '水文', 16, 1, NULL, 'A04A01', '1', '0', 'admin', 1, '2025-02-05 12:00:33', 'admin', 1, '2025-02-05 12:00:33', NULL);
INSERT INTO `att_model_cat` VALUES (22, '水资源', 16, 2, NULL, 'A04A02', '1', '0', 'admin', 1, '2025-02-05 12:00:46', 'admin', 1, '2025-02-05 12:00:46', NULL);
INSERT INTO `att_model_cat` VALUES (23, '水环境', 16, 3, NULL, 'A04A03', '1', '0', 'admin', 1, '2025-02-05 12:01:01', 'admin', 1, '2025-02-05 12:01:01', NULL);
INSERT INTO `att_model_cat` VALUES (30, '水文站', 21, 0, NULL, 'A04A01A01', '1', '0', 'admin', 1, '2025-02-05 19:03:41', '超级管理员', 1, '2025-04-10 16:42:50', NULL);
INSERT INTO `att_model_cat` VALUES (31, '测站', 15, 0, NULL, 'A03A04', '1', '0', 'admin', 1, '2025-02-06 10:11:48', 'admin', 1, '2025-02-06 10:11:48', NULL);
INSERT INTO `att_model_cat` VALUES (32, '监测数据', 0, 0, NULL, 'A06', '1', '0', 'admin', 1, '2025-02-06 10:12:03', 'admin', 1, '2025-02-06 10:12:03', NULL);
INSERT INTO `att_model_cat` VALUES (33, '示例目录', 0, 0, '千数开源示例', 'A01', '0', '0', 'qData', 1, '2025-05-23 16:30:22', '管理员', 1, '2025-05-29 06:20:56', NULL);

-- ----------------------------
-- Table structure for att_project
-- ----------------------------
DROP TABLE IF EXISTS `att_project`;
CREATE TABLE `att_project`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '项目名称',
  `manager_id` bigint(20) NOT NULL COMMENT '项目管理员id',
  `description` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '项目描述',
  `valid_flag` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '1' COMMENT '是否有效;0：无效，1：有效',
  `del_flag` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '删除标志;1：已删除，0：未删除',
  `create_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `creator_id` bigint(20) NULL DEFAULT NULL COMMENT '创建人id',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `updater_id` bigint(20) NULL DEFAULT NULL COMMENT '更新人id',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `ID_90032445363400`(`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 85 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '项目表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of att_project
-- ----------------------------
INSERT INTO `att_project` VALUES (84, '示例项目', 1, '示例项目', '1', '0', 'qData', 1, '2025-05-23 16:29:12', 'qData', 1, '2025-05-23 16:29:12', NULL, '141883958809440');

-- ----------------------------
-- Table structure for att_project_user_rel
-- ----------------------------
DROP TABLE IF EXISTS `att_project_user_rel`;
CREATE TABLE `att_project_user_rel`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `project_id` bigint(20) NOT NULL COMMENT '项目空间ID',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `valid_flag` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '1' COMMENT '是否有效;0：无效，1：有效',
  `del_flag` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '删除标志;1：已删除，0：未删除',
  `create_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `creator_id` bigint(20) NULL DEFAULT NULL COMMENT '创建人id',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `updater_id` bigint(20) NULL DEFAULT NULL COMMENT '更新人id',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `ID_90032869014200`(`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '项目表与用户表关联关系表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of att_project_user_rel
-- ----------------------------
INSERT INTO `att_project_user_rel` VALUES (1, 84, 1, '1', '0', '管理员', 1, '2025-05-23 16:29:12', '管理员', 1, '2025-05-28 06:32:53', NULL);
INSERT INTO `att_project_user_rel` VALUES (2, 84, 2, '1', '0', '管理员', 1, '2025-05-29 06:03:39', '管理员', 1, '2025-05-29 06:55:01', NULL);

-- ----------------------------
-- Table structure for att_task_cat
-- ----------------------------
DROP TABLE IF EXISTS `att_task_cat`;
CREATE TABLE `att_task_cat`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '类别名称',
  `parent_id` bigint(20) NULL DEFAULT NULL COMMENT '关联上级ID',
  `sort_order` int(11) NOT NULL DEFAULT 0 COMMENT '类别排序',
  `description` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '描述',
  `code` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '层级编码',
  `valid_flag` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '1' COMMENT '是否有效;0：无效，1：有效',
  `del_flag` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '删除标志;1：已删除，0：未删除',
  `create_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `creator_id` bigint(20) NULL DEFAULT NULL COMMENT '创建人id',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `updater_id` bigint(20) NULL DEFAULT NULL COMMENT '更新人id',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `project_id` bigint(20) NOT NULL COMMENT '项目id',
  `project_code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '项目编码',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `ID_90033373321800`(`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 12 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '数据集成任务类目管理' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of att_task_cat
-- ----------------------------
INSERT INTO `att_task_cat` VALUES (11, '示例-集成任务', 0, 0, NULL, 'A01', '1', '0', 'qData', 1, '2025-05-23 17:51:41', 'qData', 1, '2025-05-23 17:51:41', NULL, 84, '141883958809440');

-- ----------------------------
-- Table structure for att_theme
-- ----------------------------
DROP TABLE IF EXISTS `att_theme`;
CREATE TABLE `att_theme`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主题名称',
  `icon` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '图标url',
  `sort_order` int(11) NULL DEFAULT 0 COMMENT '排序',
  `description` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '描述',
  `valid_flag` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '1' COMMENT '是否有效;0：无效，1：有效',
  `del_flag` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '删除标志;1：已删除，0：未删除',
  `create_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `creator_id` bigint(20) NULL DEFAULT NULL COMMENT '创建人id',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `updater_id` bigint(20) NULL DEFAULT NULL COMMENT '更新人id',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `ID_90033691760400`(`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 15 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '主题' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of att_theme
-- ----------------------------
INSERT INTO `att_theme` VALUES (14, '示例主题', NULL, 0, '示例主题', '1', '0', 'qData', 1, '2025-05-23 16:29:43', 'qData', 1, '2025-05-23 16:29:47', NULL);

-- ----------------------------
-- Table structure for auth_client
-- ----------------------------
DROP TABLE IF EXISTS `auth_client`;
CREATE TABLE `auth_client`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '应用ID',
  `secret_key` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '应用秘钥',
  `name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '应用名称',
  `type` int(11) NOT NULL COMMENT '应用类型;0：Web，1：App，2：小程序',
  `icon` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '应用图标',
  `home_url` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '应用首页',
  `sync_url` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '同步地址',
  `redirect_url` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '允许授权的url',
  `public_flag` tinyint(4) NOT NULL DEFAULT 1 COMMENT '是否公开',
  `valid_flag` tinyint(4) NOT NULL DEFAULT 1 COMMENT '是否有效;0：无效，1：有效',
  `del_flag` tinyint(4) NOT NULL DEFAULT 0 COMMENT '删除标志;1：已删除，0：未删除',
  `create_by` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `creator_id` bigint(20) NULL DEFAULT NULL COMMENT '创建人id;创建者的sys_user_id',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `updater_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新人id;更新者的sys_user_id',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `ID_90034015323000`(`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 39 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '应用管理' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of auth_client
-- ----------------------------

-- ----------------------------
-- Table structure for auth_client_user
-- ----------------------------
DROP TABLE IF EXISTS `auth_client_user`;
CREATE TABLE `auth_client_user`  (
  `client_id` bigint(20) NOT NULL COMMENT '应用ID',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `open_id` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'open_id'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '应用和用户关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of auth_client_user
-- ----------------------------

-- ----------------------------
-- Table structure for da_asset
-- ----------------------------
DROP TABLE IF EXISTS `da_asset`;
CREATE TABLE `da_asset`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `name` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '资产名称',
  `cat_code` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '类目编码',
  `datasource_id` bigint(20) NOT NULL COMMENT '数据连接id',
  `table_name` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '表名称',
  `table_comment` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '表描述;表注释',
  `data_count` int(11) NOT NULL DEFAULT 0 COMMENT '数据量',
  `field_count` int(11) NOT NULL DEFAULT 0 COMMENT '字段量',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '1' COMMENT '状态;1:未发布 2:已发布 3:审核中(审核预留字典)',
  `description` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '描述',
  `valid_flag` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '1' COMMENT '是否有效;0：无效，1：有效',
  `del_flag` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '删除标志;1：已删除，0：未删除',
  `create_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `creator_id` bigint(20) NULL DEFAULT NULL COMMENT '创建人id',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `updater_id` bigint(20) NULL DEFAULT NULL COMMENT '更新人id',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `source` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '来源;1:数据发现；2:数据模型；',
  `type` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '1' COMMENT '资产类型;1:数据库表  2:外部API 3: 地理空间服务 4:矢量数据 5:视频数据',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `ID_90027993769800`(`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 180 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '数据资产表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of da_asset
-- ----------------------------
INSERT INTO `da_asset` VALUES (166, '百度API', 'A01', -1, '-1', NULL, 0, 0, '0', '', '1', '0', 'qData', 1, '2025-05-23 16:45:56', 'qData', 1, '2025-05-24 16:04:51', NULL, '3', '2');
INSERT INTO `da_asset` VALUES (167, 'api', 'A09', -1, '-1', '', 0, 0, '0', '', '1', '1', '超级管理员', 1, '2025-04-21 10:03:30', '超级管理员', 1, '2025-04-24 09:26:01', NULL, '3', '4');
INSERT INTO `da_asset` VALUES (168, '视频2', 'A05', -1, '-1', '', 0, 0, '0', '', '1', '1', '超级管理员', 1, '2025-04-21 10:58:48', '超级管理员', 1, '2025-04-23 17:43:24', NULL, '3', '5');
INSERT INTO `da_asset` VALUES (169, '地理空间', 'A05', -1, '-1', '', 0, 0, '0', '', '1', '1', '超级管理员', 1, '2025-04-21 10:59:23', '超级管理员', 1, '2025-04-23 17:43:21', NULL, '3', '3');
INSERT INTO `da_asset` VALUES (170, '表', 'A08', -1, '-1', NULL, 0, 0, '0', '1', '1', '1', '超级管理员', 1, '2025-04-30 19:09:18', '超级管理员', 1, '2025-04-30 19:09:18', NULL, '3', '3');
INSERT INTO `da_asset` VALUES (171, '水利地图信息-超图', 'A07', -1, '-1', NULL, 0, 0, '0', '水利地图信息-超图', '1', '1', '超级管理员', 1, '2025-05-06 11:10:29', '超级管理员', 1, '2025-05-06 11:19:55', NULL, '3', '3');
INSERT INTO `da_asset` VALUES (172, '地理空间服务', 'A01', -1, '-1', NULL, 0, 0, '0', '', '1', '0', 'qData', 1, '2025-05-23 17:01:54', 'qData', 1, '2025-05-23 17:01:54', NULL, '3', '3');
INSERT INTO `da_asset` VALUES (173, '示例-消息', 'A01', 48, 'MESSAGE', '消息', 0, 22, '1', '', '1', '0', 'qData', 1, '2025-05-23 17:42:18', 'qData', 1, '2025-05-23 17:42:18', NULL, '3', '1');
INSERT INTO `da_asset` VALUES (174, '用户信息表', 'A01', 1001, 'DP_USER_INFO', '存储用户基础信息的表', 1000, 8, '0', '包含用户ID、姓名、邮箱等字段', '1', '0', 'admin', 1, '2025-05-30 10:30:00', '管理员', 1, '2025-05-30 17:59:41', '初始版本', '0', '1');
INSERT INTO `da_asset` VALUES (175, '订单表', 'A01', 1002, 'DP_ORDER', '存储订单信息的表', 5000, 10, '0', '包含订单ID、金额、状态等字段', '1', '0', 'admin', 1, '2025-05-30 10:35:00', '管理员', 1, '2025-05-30 17:59:33', '初始版本', '0', '1');
INSERT INTO `da_asset` VALUES (176, '产品表', 'A01', 1003, 'DP_PRODUCT', '存储产品信息的表', 200, 6, '0', '包含产品ID、名称、价格等字段', '1', '0', 'admin', 1, '2025-05-30 10:40:00', '管理员', 1, '2025-05-30 17:59:25', '初始版本', '0', '1');
INSERT INTO `da_asset` VALUES (177, '库存表', 'A01', 1004, 'DP_INVENTORY', '存储库存信息的表', 800, 7, '0', '包含库存ID、数量、仓库信息', '1', '0', 'admin', 1, '2025-05-30 10:45:00', '管理员', 1, '2025-05-30 17:59:16', '初始版本', '0', '1');
INSERT INTO `da_asset` VALUES (178, '客户表', 'A05', 1005, 'DP_CUSTOMER', '存储客户信息的表', 1200, 9, '0', '包含客户ID、姓名、联系方式', '1', '0', 'admin', 1, '2025-05-30 10:50:00', '管理员', 1, '2025-05-30 17:59:08', '初始版本', '0', '1');
INSERT INTO `da_asset` VALUES (179, '支付表', 'A01', 1006, 'DP_PAYMENT', '存储支付信息的表', 3000, 8, '0', '包含支付ID、金额、支付方式', '1', '0', 'admin', 1, '2025-05-30 10:55:00', '管理员', 1, '2025-05-30 17:59:01', '初始版本', '0', '1');

-- ----------------------------
-- Table structure for da_asset_api
-- ----------------------------
DROP TABLE IF EXISTS `da_asset_api`;
CREATE TABLE `da_asset_api`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `asset_id` bigint(20) NOT NULL COMMENT '资产id',
  `url` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'API路径',
  `http_method` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '请求方式;GET、POST',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '1' COMMENT '状态;1:未发布 2:已发布',
  `valid_flag` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '1' COMMENT '是否有效;0：无效，1：有效',
  `del_flag` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '删除标志;1：已删除，0：未删除',
  `create_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `creator_id` bigint(20) NULL DEFAULT NULL COMMENT '创建人id',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `updater_id` bigint(20) NULL DEFAULT NULL COMMENT '更新人id',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `developer_name` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '开发者',
  `app_name` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '应用名称',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `ID_90072173106000`(`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 15 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '数据资产-外部API表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of da_asset_api
-- ----------------------------
INSERT INTO `da_asset_api` VALUES (14, 166, 'https://www.baidu.com/', 'GET', '1', '1', '0', 'qData', 1, '2025-05-23 16:45:55', 'qData', 1, '2025-05-24 16:04:50', NULL, '张珊', '百度');

-- ----------------------------
-- Table structure for da_asset_api_param
-- ----------------------------
DROP TABLE IF EXISTS `da_asset_api_param`;
CREATE TABLE `da_asset_api_param`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `api_id` bigint(20) NOT NULL COMMENT 'API id',
  `parent_id` bigint(20) NULL DEFAULT NULL COMMENT '父级id',
  `name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '参数名称',
  `type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '参数类型;1:入参 2:出参 3:Herder',
  `request_flag` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '是否必填;0：否，1：是',
  `column_type` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '字段类型;1:String 2:Integer 3: Long 4:Double 5:Boolean 6:Date 7:Object 8:Array',
  `valid_flag` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '1' COMMENT '是否有效;0：无效，1：有效',
  `del_flag` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '删除标志;1：已删除，0：未删除',
  `create_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `creator_id` bigint(20) NULL DEFAULT NULL COMMENT '创建人id',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `updater_id` bigint(20) NULL DEFAULT NULL COMMENT '更新人id',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `default_value` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '数据默认值',
  `example_value` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '示例值',
  `description` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '描述',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `ID_90073689556400`(`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '数据资产-外部API-参数表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of da_asset_api_param
-- ----------------------------

-- ----------------------------
-- Table structure for da_asset_apply
-- ----------------------------
DROP TABLE IF EXISTS `da_asset_apply`;
CREATE TABLE `da_asset_apply`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `asset_id` bigint(20) NOT NULL COMMENT '资产id',
  `project_id` bigint(20) NOT NULL COMMENT '项目id',
  `project_code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '项目编码',
  `apply_reason` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '申请理由',
  `approval_reason` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '审批理由',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '1' COMMENT '状态;1:待审批 2:审批不通过 9:审批通过',
  `valid_flag` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '1' COMMENT '是否有效;0：无效，1：有效',
  `del_flag` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '删除标志;1：已删除，0：未删除',
  `create_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `creator_id` bigint(20) NULL DEFAULT NULL COMMENT '创建人id',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `updater_id` bigint(20) NULL DEFAULT NULL COMMENT '更新人id',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `source_type` varchar(5) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '是申请过来的资产还是项目自己生成的资产0：申请，1：自创',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `ID_90075349207500`(`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 31 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '数据资产申请表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of da_asset_apply
-- ----------------------------
INSERT INTO `da_asset_apply` VALUES (30, 173, 84, '141883958809440', NULL, NULL, '3', '1', '0', 'qData', 1, '2025-05-24 09:53:47', 'qData', 1, '2025-05-24 10:21:17', NULL, '0');

-- ----------------------------
-- Table structure for da_asset_column
-- ----------------------------
DROP TABLE IF EXISTS `da_asset_column`;
CREATE TABLE `da_asset_column`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `asset_id` bigint(20) NOT NULL COMMENT '资产id',
  `column_name` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '字段名称/英文名称',
  `column_comment` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '字段注释/中文名称',
  `column_type` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '数据类型',
  `column_length` int(11) NULL DEFAULT NULL COMMENT '长度',
  `column_scale` int(11) NULL DEFAULT 0 COMMENT '小数位;精度',
  `nullable_flag` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '是否必填;0:否 1:是',
  `pk_flag` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '是否主键;0:否 1:是',
  `default_value` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '默认值',
  `data_elem_code_flag` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '是否代码;0:否 1:是',
  `data_elem_code_id` bigint(20) NULL DEFAULT NULL COMMENT '代码id',
  `sensitive_level_id` bigint(20) NULL DEFAULT NULL COMMENT '敏感等级id',
  `rel_data_elme_flag` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '关联数据元;0:否 1:是',
  `rel_clean_flag` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '关联清洗规则;0:否 1:是',
  `rel_audit_flag` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '关联稽查规则;0:否 1:是',
  `description` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '描述',
  `valid_flag` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '1' COMMENT '是否有效;0：无效，1：有效',
  `del_flag` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '删除标志;1：已删除，0：未删除',
  `create_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `creator_id` bigint(20) NULL DEFAULT NULL COMMENT '创建人id',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `updater_id` bigint(20) NULL DEFAULT NULL COMMENT '更新人id',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `ID_90076493363200`(`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 740 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '数据资产字段表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of da_asset_column
-- ----------------------------
INSERT INTO `da_asset_column` VALUES (706, 173, 'ID', 'ID', 'INT', 4, 0, '0', '1', NULL, '0', NULL, NULL, '0', '0', '0', NULL, '1', '0', 'qData', 1, '2025-05-23 17:42:18', 'qData', 1, '2025-05-23 17:42:18', NULL);
INSERT INTO `da_asset_column` VALUES (707, 173, 'SENDER_ID', '发送人', 'BIGINT', 8, 0, '1', '0', NULL, '0', NULL, NULL, '0', '0', '0', NULL, '1', '0', 'qData', 1, '2025-05-23 17:42:19', 'qData', 1, '2025-05-23 17:42:19', NULL);
INSERT INTO `da_asset_column` VALUES (708, 173, 'RECEIVER_ID', '接收人', 'BIGINT', 8, 0, '1', '0', NULL, '0', NULL, NULL, '0', '0', '0', NULL, '1', '0', 'qData', 1, '2025-05-23 17:42:19', 'qData', 1, '2025-05-23 17:42:19', NULL);
INSERT INTO `da_asset_column` VALUES (709, 173, 'TITLE', '消息标题', 'VARCHAR', 128, 0, '0', '0', NULL, '0', NULL, NULL, '0', '0', '0', NULL, '1', '0', 'qData', 1, '2025-05-23 17:42:19', 'qData', 1, '2025-05-23 17:42:19', NULL);
INSERT INTO `da_asset_column` VALUES (710, 173, 'CONTENT', '消息模板内容', 'VARCHAR', 3072, 0, '0', '0', NULL, '0', NULL, NULL, '0', '0', '0', NULL, '1', '0', 'qData', 1, '2025-05-23 17:42:19', 'qData', 1, '2025-05-23 17:42:19', NULL);
INSERT INTO `da_asset_column` VALUES (711, 173, 'CATEGORY', '消息类别', 'INT', 4, 0, '0', '0', NULL, '0', NULL, NULL, '0', '0', '0', NULL, '1', '0', 'qData', 1, '2025-05-23 17:42:19', 'qData', 1, '2025-05-23 17:42:19', NULL);
INSERT INTO `da_asset_column` VALUES (712, 173, 'MSG_LEVEL', '消息等级', 'INT', 4, 0, '0', '0', '0', '0', NULL, NULL, '0', '0', '0', NULL, '1', '0', 'qData', 1, '2025-05-23 17:42:19', 'qData', 1, '2025-05-23 17:42:19', NULL);
INSERT INTO `da_asset_column` VALUES (713, 173, 'MODULE', '消息模块', 'INT', 4, 0, '0', '0', '0', '0', NULL, NULL, '0', '0', '0', NULL, '1', '0', 'qData', 1, '2025-05-23 17:42:19', 'qData', 1, '2025-05-23 17:42:19', NULL);
INSERT INTO `da_asset_column` VALUES (714, 173, 'ENTITY_TYPE', '实体类型', 'INT', 4, 0, '1', '0', NULL, '0', NULL, NULL, '0', '0', '0', NULL, '1', '0', 'qData', 1, '2025-05-23 17:42:19', 'qData', 1, '2025-05-23 17:42:19', NULL);
INSERT INTO `da_asset_column` VALUES (715, 173, 'ENTITY_ID', '实体id', 'BIGINT', 8, 0, '1', '0', NULL, '0', NULL, NULL, '0', '0', '0', NULL, '1', '0', 'qData', 1, '2025-05-23 17:42:19', 'qData', 1, '2025-05-23 17:42:19', NULL);
INSERT INTO `da_asset_column` VALUES (716, 173, 'ENTITY_URL', '消息链接', 'VARCHAR', 256, 0, '1', '0', NULL, '0', NULL, NULL, '0', '0', '0', NULL, '1', '0', 'qData', 1, '2025-05-23 17:42:19', 'qData', 1, '2025-05-23 17:42:19', NULL);
INSERT INTO `da_asset_column` VALUES (717, 173, 'HAS_READ', '是否已读', 'TINYINT', 1, 0, '1', '0', '0', '0', NULL, NULL, '0', '0', '0', NULL, '1', '0', 'qData', 1, '2025-05-23 17:42:19', 'qData', 1, '2025-05-23 17:42:19', NULL);
INSERT INTO `da_asset_column` VALUES (718, 173, 'HAS_RETRACTION', '是否撤回', 'TINYINT', 1, 0, '1', '0', '0', '0', NULL, NULL, '0', '0', '0', NULL, '1', '0', 'qData', 1, '2025-05-23 17:42:19', 'qData', 1, '2025-05-23 17:42:19', NULL);
INSERT INTO `da_asset_column` VALUES (719, 173, 'VALID_FLAG', '是否有效;0：无效，1：有效', 'TINYINT', 1, 0, '0', '0', '1', '0', NULL, NULL, '0', '0', '0', NULL, '1', '0', 'qData', 1, '2025-05-23 17:42:19', 'qData', 1, '2025-05-23 17:42:19', NULL);
INSERT INTO `da_asset_column` VALUES (720, 173, 'DEL_FLAG', '删除标志;1：已删除，0：未删除', 'TINYINT', 1, 0, '0', '0', '0', '0', NULL, NULL, '0', '0', '0', NULL, '1', '0', 'qData', 1, '2025-05-23 17:42:19', 'qData', 1, '2025-05-23 17:42:19', NULL);
INSERT INTO `da_asset_column` VALUES (721, 173, 'CREATE_BY', '创建人', 'VARCHAR', 32, 0, '1', '0', NULL, '0', NULL, NULL, '0', '0', '0', NULL, '1', '0', 'qData', 1, '2025-05-23 17:42:19', 'qData', 1, '2025-05-23 17:42:19', NULL);
INSERT INTO `da_asset_column` VALUES (722, 173, 'CREATOR_ID', '创建人id', 'BIGINT', 8, 0, '1', '0', NULL, '0', NULL, NULL, '0', '0', '0', NULL, '1', '0', 'qData', 1, '2025-05-23 17:42:19', 'qData', 1, '2025-05-23 17:42:19', NULL);
INSERT INTO `da_asset_column` VALUES (723, 173, 'CREATE_TIME', '创建时间', 'TIMESTAMP', 8, 0, '0', '0', 'CURRENT_TIMESTAMP()', '0', NULL, NULL, '0', '0', '0', NULL, '1', '0', 'qData', 1, '2025-05-23 17:42:19', 'qData', 1, '2025-05-23 17:42:19', NULL);
INSERT INTO `da_asset_column` VALUES (724, 173, 'UPDATE_BY', '更新人', 'VARCHAR', 32, 0, '1', '0', NULL, '0', NULL, NULL, '0', '0', '0', NULL, '1', '0', 'qData', 1, '2025-05-23 17:42:19', 'qData', 1, '2025-05-23 17:42:19', NULL);
INSERT INTO `da_asset_column` VALUES (725, 173, 'UPDATE_TIME', '更新时间', 'TIMESTAMP', 8, 0, '0', '0', 'CURRENT_TIMESTAMP()', '0', NULL, NULL, '0', '0', '0', NULL, '1', '0', 'qData', 1, '2025-05-23 17:42:19', 'qData', 1, '2025-05-23 17:42:19', NULL);
INSERT INTO `da_asset_column` VALUES (726, 173, 'UPDATER_ID', '更新人id', 'BIGINT', 8, 0, '1', '0', NULL, '0', NULL, NULL, '0', '0', '0', NULL, '1', '0', 'qData', 1, '2025-05-23 17:42:19', 'qData', 1, '2025-05-23 17:42:19', NULL);
INSERT INTO `da_asset_column` VALUES (727, 173, 'REMARK', '备注', 'VARCHAR', 512, 0, '1', '0', NULL, '0', NULL, NULL, '0', '0', '0', NULL, '1', '0', 'qData', 1, '2025-05-23 17:42:19', 'qData', 1, '2025-05-23 17:42:19', NULL);
INSERT INTO `da_asset_column` VALUES (728, 174, 'USER_ID', '用户ID', 'BIGINT', 20, 0, '0', '1', NULL, '0', NULL, NULL, '0', '0', '0', '主键', '1', '0', 'admin', 1, '2025-05-30 11:05:00', 'admin', 1, '2025-05-30 11:05:00', '');
INSERT INTO `da_asset_column` VALUES (729, 174, 'USERNAME', '用户名', 'VARCHAR2', 256, NULL, '0', '0', NULL, '0', NULL, NULL, '0', '0', '0', '必填', '1', '0', 'admin', 1, '2025-05-30 11:05:00', 'admin', 1, '2025-05-30 11:05:00', '');
INSERT INTO `da_asset_column` VALUES (730, 175, 'ORDER_ID', '订单ID', 'BIGINT', 20, 0, '0', '1', NULL, '0', NULL, NULL, '0', '0', '0', '主键', '1', '0', 'admin', 1, '2025-05-30 11:10:00', 'admin', 1, '2025-05-30 11:10:00', '');
INSERT INTO `da_asset_column` VALUES (731, 175, 'ORDER_AMOUNT', '订单金额', 'DECIMAL', 10, 2, '0', '0', NULL, '0', NULL, NULL, '0', '0', '0', '必填', '1', '0', 'admin', 1, '2025-05-30 11:10:00', 'admin', 1, '2025-05-30 11:10:00', '');
INSERT INTO `da_asset_column` VALUES (732, 176, 'PRODUCT_ID', '产品ID', 'BIGINT', 20, 0, '0', '1', NULL, '0', NULL, NULL, '0', '0', '0', '主键', '1', '0', 'admin', 1, '2025-05-30 11:15:00', 'admin', 1, '2025-05-30 11:15:00', '');
INSERT INTO `da_asset_column` VALUES (733, 176, 'PRODUCT_NAME', '产品名称', 'VARCHAR2', 256, NULL, '0', '0', NULL, '0', NULL, NULL, '0', '0', '0', '必填', '1', '0', 'admin', 1, '2025-05-30 11:15:00', 'admin', 1, '2025-05-30 11:15:00', '');
INSERT INTO `da_asset_column` VALUES (734, 177, 'INVENTORY_ID', '库存ID', 'BIGINT', 20, 0, '0', '1', NULL, '0', NULL, NULL, '0', '0', '0', '主键', '1', '0', 'admin', 1, '2025-05-30 11:20:00', 'admin', 1, '2025-05-30 11:20:00', '');
INSERT INTO `da_asset_column` VALUES (735, 177, 'QUANTITY', '库存数量', 'INTEGER', NULL, NULL, '0', '0', NULL, '0', NULL, NULL, '0', '0', '0', '必填', '1', '0', 'admin', 1, '2025-05-30 11:20:00', 'admin', 1, '2025-05-30 11:20:00', '');
INSERT INTO `da_asset_column` VALUES (736, 178, 'CUSTOMER_ID', '客户ID', 'BIGINT', 20, 0, '0', '1', NULL, '0', NULL, NULL, '0', '0', '0', '主键', '1', '0', 'admin', 1, '2025-05-30 11:25:00', 'admin', 1, '2025-05-30 11:25:00', '');
INSERT INTO `da_asset_column` VALUES (737, 178, 'CUSTOMER_NAME', '客户名称', 'VARCHAR2', 256, NULL, '0', '0', NULL, '0', NULL, NULL, '0', '0', '0', '必填', '1', '0', 'admin', 1, '2025-05-30 11:25:00', 'admin', 1, '2025-05-30 11:25:00', '');
INSERT INTO `da_asset_column` VALUES (738, 179, 'PAYMENT_ID', '支付ID', 'BIGINT', 20, 0, '0', '1', NULL, '0', NULL, NULL, '0', '0', '0', '主键', '1', '0', 'admin', 1, '2025-05-30 11:30:00', 'admin', 1, '2025-05-30 11:30:00', '');
INSERT INTO `da_asset_column` VALUES (739, 179, 'PAYMENT_AMOUNT', '支付金额', 'DECIMAL', 10, 2, '0', '0', NULL, '0', NULL, NULL, '0', '0', '0', '必填', '1', '0', 'admin', 1, '2025-05-30 11:30:00', 'admin', 1, '2025-05-30 11:30:00', '');

-- ----------------------------
-- Table structure for da_asset_geo
-- ----------------------------
DROP TABLE IF EXISTS `da_asset_geo`;
CREATE TABLE `da_asset_geo`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `asset_id` bigint(20) NOT NULL COMMENT '资产id',
  `file_name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '文件名称',
  `file_url` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '文件路径',
  `element_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '要素类型;1:点 2:线 3:面 (混合多选一逗号隔开)',
  `coordinate_system` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '坐标系',
  `valid_flag` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '1' COMMENT '是否有效;0：无效，1：有效',
  `del_flag` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '删除标志;1：已删除，0：未删除',
  `create_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `creator_id` bigint(20) NULL DEFAULT NULL COMMENT '创建人id',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `updater_id` bigint(20) NULL DEFAULT NULL COMMENT '更新人id',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '1' COMMENT '状态;1:未发布 2:已发布',
  `file_type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '文件类型;例如 pdf/doc/txt等',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `ID_90077468043400`(`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '数据资产-矢量表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of da_asset_geo
-- ----------------------------
INSERT INTO `da_asset_geo` VALUES (1, 141, '', '/profile/2025/04/21/6805a768c2ae53d5f126b0e8.geojson', '', '', '1', '0', '超级管理员', 1, '2025-04-21 10:03:30', '超级管理员', 1, '2025-04-21 10:03:30', NULL, '1', 'geojson');
INSERT INTO `da_asset_geo` VALUES (2, 141, '', '/profile/2025/04/21/6805a768c2ae53d5f126b0e8.geojson', '', '', '1', '0', '超级管理员', 1, '2025-04-21 10:03:30', '超级管理员', 1, '2025-04-21 10:03:30', NULL, '1', 'geojson');

-- ----------------------------
-- Table structure for da_asset_gis
-- ----------------------------
DROP TABLE IF EXISTS `da_asset_gis`;
CREATE TABLE `da_asset_gis`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `asset_id` bigint(20) NULL DEFAULT NULL COMMENT '资产id',
  `url` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '服务地址',
  `type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '服务类型;1:数据服务 2:地图服务',
  `http_method` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '请求方式;GET、POST',
  `coordinate_system` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '坐标系;例如 EPSG:4326',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '1' COMMENT '状态;1:未发布 2:已发布',
  `valid_flag` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '1' COMMENT '是否有效;0：无效，1：有效',
  `del_flag` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '删除标志;1：已删除，0：未删除',
  `create_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `creator_id` bigint(20) NULL DEFAULT NULL COMMENT '创建人id',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `updater_id` bigint(20) NULL DEFAULT NULL COMMENT '更新人id',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `ID_90078639538100`(`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '数据资产-地理空间服务表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of da_asset_gis
-- ----------------------------
INSERT INTO `da_asset_gis` VALUES (1, 152, 'https://www.baidu.com/', '1', 'POST', '', '1', '1', '0', '超级管理员', 1, '2025-04-21 10:59:23', '超级管理员', 1, '2025-04-21 11:54:59', '');
INSERT INTO `da_asset_gis` VALUES (2, 157, 'https://www.bing.com/', '1', 'GET', '', '1', '1', '0', '超级管理员', 1, '2025-04-30 19:09:18', '超级管理员', 1, '2025-04-30 19:09:18', '');
INSERT INTO `da_asset_gis` VALUES (3, 162, 'https://iserver.supermap.io/iserver/services/map-world/wmts-china', '1', 'GET', '', '1', '1', '0', '超级管理员', 1, '2025-05-06 11:10:29', '超级管理员', 1, '2025-05-06 11:10:29', '');
INSERT INTO `da_asset_gis` VALUES (4, 152, 'https://www.baidu.com/', '1', 'POST', '', '1', '1', '0', '超级管理员', 1, '2025-04-21 10:59:23', '超级管理员', 1, '2025-04-21 11:54:59', '');
INSERT INTO `da_asset_gis` VALUES (5, 157, 'https://www.bing.com/', '1', 'GET', '', '1', '1', '0', '超级管理员', 1, '2025-04-30 19:09:18', '超级管理员', 1, '2025-04-30 19:09:18', '');
INSERT INTO `da_asset_gis` VALUES (6, 162, 'https://iserver.supermap.io/iserver/services/map-world/wmts-china', '1', 'GET', '', '1', '1', '0', '超级管理员', 1, '2025-05-06 11:10:29', '超级管理员', 1, '2025-05-06 11:10:29', '');
INSERT INTO `da_asset_gis` VALUES (7, 172, 'https://cn.bing.com/', '1', 'GET', '', '1', '1', '0', 'qData', 1, '2025-05-23 17:01:54', 'qData', 1, '2025-05-23 17:01:54', '');

-- ----------------------------
-- Table structure for da_asset_operate_apply
-- ----------------------------
DROP TABLE IF EXISTS `da_asset_operate_apply`;
CREATE TABLE `da_asset_operate_apply`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `asset_id` bigint(20) NOT NULL COMMENT '资产id',
  `datasource_id` bigint(20) NOT NULL COMMENT '数据连接id',
  `table_name` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '表名称',
  `table_comment` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '表注释/表描述',
  `operate_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '操作类型;1: 新增 2:修改 3:删除 4:导入',
  `operate_json` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '操作JSON数据;每个操作类型 新增：{\"data\":{}} 修改:{\"oldData\":{},\"newData\":{}} 删除：{\"data\":{}} 导入：{\"fileUrl\":\"excel文件的路径\",\"fileName\":\"excel文件的名称\"}',
  `operate_time` datetime NULL DEFAULT NULL COMMENT '操作时间',
  `execute_flag` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '是否已执行;0：否，1：是',
  `execute_time` datetime NULL DEFAULT NULL COMMENT '执行时间',
  `valid_flag` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '1' COMMENT '是否有效;0：无效，1：有效',
  `del_flag` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '删除标志;1：已删除，0：未删除',
  `create_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `creator_id` bigint(20) NULL DEFAULT NULL COMMENT '创建人id',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `updater_id` bigint(20) NULL DEFAULT NULL COMMENT '更新人id',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `ID_90080119653400`(`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '数据资产操作申请表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of da_asset_operate_apply
-- ----------------------------

-- ----------------------------
-- Table structure for da_asset_operate_log
-- ----------------------------
DROP TABLE IF EXISTS `da_asset_operate_log`;
CREATE TABLE `da_asset_operate_log`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `asset_id` bigint(20) NOT NULL COMMENT '资产id',
  `datasource_id` bigint(20) NOT NULL COMMENT '数据连接id',
  `table_name` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '表名称',
  `table_comment` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '表注释/表描述',
  `operate_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '操作类型;1: 新增 2:修改 3:删除 4:导入',
  `operate_time` datetime NULL DEFAULT NULL COMMENT '操作时间',
  `execute_time` datetime NULL DEFAULT NULL COMMENT '执行时间',
  `update_before` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '修改前数据(JSON数据);新增、导入该数据为空',
  `update_after` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '修改后数据(JSON数据);删除数据为空',
  `field_names` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '字段',
  `file_url` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '导入文件URL',
  `file_name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '导入文件名称',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '1' COMMENT '状态;1:执行中  2:失败  3:成功',
  `valid_flag` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '1' COMMENT '是否有效;0：无效，1：有效',
  `del_flag` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '删除标志;1：已删除，0：未删除',
  `create_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `creator_id` bigint(20) NULL DEFAULT NULL COMMENT '创建人id',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `updater_id` bigint(20) NULL DEFAULT NULL COMMENT '更新人id',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `UPDATE_WHERE_MD5` varchar(4000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新条件 JSON MD5 字符串',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `ID_90081736673600`(`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '数据资产操作记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of da_asset_operate_log
-- ----------------------------

-- ----------------------------
-- Table structure for da_asset_project_rel
-- ----------------------------
DROP TABLE IF EXISTS `da_asset_project_rel`;
CREATE TABLE `da_asset_project_rel`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `asset_id` bigint(20) NOT NULL COMMENT '资产id',
  `project_id` bigint(20) NOT NULL COMMENT '项目id',
  `project_code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '项目编码',
  `valid_flag` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '1' COMMENT '是否有效;0：无效，1：有效',
  `del_flag` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '删除标志;1：已删除，0：未删除',
  `create_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `creator_id` bigint(20) NULL DEFAULT NULL COMMENT '创建人id',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `updater_id` bigint(20) NULL DEFAULT NULL COMMENT '更新人id',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `ID_90085115893900`(`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '数据资产与项目关联关系表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of da_asset_project_rel
-- ----------------------------

-- ----------------------------
-- Table structure for da_asset_theme_rel
-- ----------------------------
DROP TABLE IF EXISTS `da_asset_theme_rel`;
CREATE TABLE `da_asset_theme_rel`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `asset_id` bigint(20) NOT NULL COMMENT '资产id',
  `theme_id` bigint(20) NOT NULL COMMENT '主题id',
  `valid_flag` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '1' COMMENT '是否有效;0：无效，1：有效',
  `del_flag` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '删除标志;1：已删除，0：未删除',
  `create_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `creator_id` bigint(20) NULL DEFAULT NULL COMMENT '创建人id',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `updater_id` bigint(20) NULL DEFAULT NULL COMMENT '更新人id',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `ID_90089975734400`(`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 101 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '数据资产-主题关联关系表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of da_asset_theme_rel
-- ----------------------------
INSERT INTO `da_asset_theme_rel` VALUES (96, 166, 14, '1', '1', 'qData', 1, '2025-05-23 16:45:56', 'qData', 1, '2025-05-23 16:45:56', NULL);
INSERT INTO `da_asset_theme_rel` VALUES (97, 172, 14, '1', '0', 'qData', 1, '2025-05-23 17:01:54', 'qData', 1, '2025-05-23 17:01:54', NULL);
INSERT INTO `da_asset_theme_rel` VALUES (98, 173, 14, '1', '0', 'qData', 1, '2025-05-23 17:42:19', 'qData', 1, '2025-05-23 17:42:19', NULL);
INSERT INTO `da_asset_theme_rel` VALUES (99, 166, 14, '1', '1', 'qData', 1, '2025-05-24 16:04:16', 'qData', 1, '2025-05-24 16:04:16', NULL);
INSERT INTO `da_asset_theme_rel` VALUES (100, 166, 14, '1', '0', 'qData', 1, '2025-05-24 16:04:51', 'qData', 1, '2025-05-24 16:04:51', NULL);

-- ----------------------------
-- Table structure for da_asset_video
-- ----------------------------
DROP TABLE IF EXISTS `da_asset_video`;
CREATE TABLE `da_asset_video`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `asset_id` bigint(20) NOT NULL COMMENT '资产id',
  `ip` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'IP;例如 127.0.0.1:443',
  `port` int(11) NOT NULL COMMENT '端口号',
  `protocol` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '协议',
  `platform` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '1' COMMENT '平台;1:海康 2:大华',
  `config` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '配置JSON;海康：{\"appKey\":\"xxx\",\"appSecret\":\"xxx\",\"cameraIndexCode\":\"监控点唯一标识\"} 大华：{\"username\":\"账号\",\"password\":\"密码\",\"channel\":\"通道号\"}',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '1' COMMENT '状态;1:未发布 2:已发布',
  `valid_flag` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '1' COMMENT '是否有效;0：无效，1：有效',
  `del_flag` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '删除标志;1：已删除，0：未删除',
  `create_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `creator_id` bigint(20) NULL DEFAULT NULL COMMENT '创建人id',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `updater_id` bigint(20) NULL DEFAULT NULL COMMENT '更新人id',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `ID_90092558566600`(`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '数据资产-视频数据表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of da_asset_video
-- ----------------------------
INSERT INTO `da_asset_video` VALUES (1, 151, '1', 9090, '', '1', '{\"cameraCode\":\"4\",\"cameraName\":\"2\",\"appkey\":\"2\",\"appSecret\":\"42\",\"artemisPath\":\"artemisPath\"}', '1', '1', '0', '超级管理员', 1, '2025-04-21 10:58:48', '超级管理员', 1, '2025-04-22 09:39:12', NULL);
INSERT INTO `da_asset_video` VALUES (2, 151, '1', 9090, '', '1', '{\"cameraCode\":\"4\",\"cameraName\":\"2\",\"appkey\":\"2\",\"appSecret\":\"42\",\"artemisPath\":\"artemisPath\"}', '1', '1', '0', '超级管理员', 1, '2025-04-21 10:58:48', '超级管理员', 1, '2025-04-22 09:39:12', NULL);

-- ----------------------------
-- Table structure for da_datasource
-- ----------------------------
DROP TABLE IF EXISTS `da_datasource`;
CREATE TABLE `da_datasource`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `datasource_name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '数据源名称',
  `datasource_type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '数据源类型',
  `datasource_config` varchar(3072) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '数据源配置(json字符串)',
  `ip` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'IP',
  `port` int(11) NOT NULL COMMENT '端口号',
  `list_count` int(11) NULL DEFAULT 0 COMMENT '数据库表数（预留）',
  `sync_count` int(11) NULL DEFAULT 0 COMMENT '同步记录数（预留）',
  `data_size` int(11) NULL DEFAULT 0 COMMENT '同步数据量大小（预留）',
  `description` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '描述',
  `valid_flag` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '1' COMMENT '是否有效;0：无效，1：有效',
  `create_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `creator_id` bigint(20) NULL DEFAULT NULL COMMENT '创建人id',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `updater_id` bigint(20) NULL DEFAULT NULL COMMENT '更新人id',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `del_flag` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `ID_90093795537900`(`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 49 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '数据源表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of da_datasource
-- ----------------------------
INSERT INTO `da_datasource` VALUES (4, '中台业务-临时库-dm', 'DM8', '{\"username\":\"test\",\"password\":\"admin123\",\"dbname\":\"TEMP\",\"sid\":\"helowin\"}', '127.0.0.1', 40013, 5, 5, 5, '', '1', 'admin', 1, '2025-05-29 07:43:30', '舒月鑫', 733, '2025-05-29 07:43:30', '', '0');
INSERT INTO `da_datasource` VALUES (7, '错误复现-业务数据库-dm', 'DM8', '{\"username\":\"dmtest\",\"password\":\"123456\",\"dbname\":\"dmtest\"}', '127.0.0.1', 40013, 0, 0, 0, '', '1', 'admin', 1, '2025-05-27 04:41:58', '超级管理员', 1, '2025-05-27 04:41:58', NULL, '0');
INSERT INTO `da_datasource` VALUES (9, '测试-贴源数据库-人大金仓', 'Kingbase8', '{\"username\":\"test\",\"password\":\"admin123\",\"dbname\":\"test\",\"sid\":\"public\"}', '127.0.0.1', 40020, 0, 0, 0, NULL, '1', 'admin', 1, '2025-05-30 03:32:03', '舒月鑫', 733, '2025-05-30 03:32:03', NULL, '0');
INSERT INTO `da_datasource` VALUES (13, '测试-项目库-dm', 'DM8', '{\"username\":\"test\",\"password\":\"admin123\",\"dbname\":\"test\"}', '127.0.0.1', 40013, 0, 0, 0, NULL, '1', '孟繁明', 731, '2025-05-27 05:01:21', '超级管理员', 1, '2025-05-27 05:01:21', NULL, '0');
INSERT INTO `da_datasource` VALUES (15, '水资源-水文数据库', 'Oracle11', '{\"username\":\"TEST\",\"password\":\"admin123\",\"dbname\":\"TEST\",\"sid\":\"helowin\"}', '127.0.0.1', 40002, 0, 0, 0, '水文数据库-Oracle', '1', '孟繁明', 731, '2025-05-27 15:08:26', '超级管理员', 1, '2025-05-27 15:08:26', NULL, '0');
INSERT INTO `da_datasource` VALUES (26, '测试-kafka', 'Kafka', '{}', '127.0.0.1', 40025, 0, 0, 0, NULL, '1', '超级管理员', 1, '2025-05-28 14:58:48', '超级管理员', 1, '2025-05-28 14:58:48', NULL, '0');
INSERT INTO `da_datasource` VALUES (29, '水文数据库', 'MySql', '{\"username\":\"root\",\"password\":\"admin123\",\"dbname\":\"test\"}', '127.0.0.1', 3306, 0, 0, 0, 'wang负责MySQL', '1', '超级管理员', 1, '2025-05-29 03:17:33', '千桐科技', 1, '2025-05-29 03:17:33', NULL, '0');
INSERT INTO `da_datasource` VALUES (37, '测试-HDFS', 'HDFS', '{}', '127.0.0.1', 18020, 0, 0, 0, NULL, '1', '千桐科技', 1, '2025-05-26 09:05:22', '千桐科技', 1, '2025-05-26 09:05:22', NULL, '0');
INSERT INTO `da_datasource` VALUES (38, '物联网平台接入测试', 'MySql', '{\"username\":\"mesdev\",\"password\":\"admin123\",\"dbname\":\"mes2_dev\"}', '127.0.0.1', 3306, 0, 0, 0, NULL, '1', '超级管理员', 1, '2025-05-29 17:36:02', '超级管理员', 1, '2025-05-29 17:36:02', NULL, '0');
INSERT INTO `da_datasource` VALUES (42, '物联网平台接入测试2', 'MySql', '{\"username\":\"hingdev\",\"password\":\"admin123\",\"dbname\":\"hing_dev\"}', '127.0.0.1', 3306, 0, 0, 0, NULL, '1', '超级管理员', 1, '2025-05-27 22:11:00', '超级管理员', 1, '2025-05-27 22:11:00', NULL, '0');
INSERT INTO `da_datasource` VALUES (44, '水资源-水文数据库-中间库', 'DM8', '{\"username\":\"test\",\"password\":\"admin123\",\"dbname\":\"TEMP\"}', '127.0.0.1', 40013, 0, 0, 0, NULL, '1', '超级管理员', 1, '2025-05-26 16:05:03', '超级管理员', 1, '2025-05-26 16:05:03', NULL, '0');
INSERT INTO `da_datasource` VALUES (45, '驾驶舱到报率', 'Oracle11', '{\"username\":\"admin\",\"password\":\"admin123\",\"dbname\":\"USER\",\"sid\":\"orcl\"}', '127.0.0.1', 1521, 0, 0, 0, NULL, '1', '超级管理员', 1, '2025-05-28 08:18:35', '超级管理员', 1, '2025-05-28 08:18:35', NULL, '0');
INSERT INTO `da_datasource` VALUES (46, '水利厅', 'Oracle11', '{\"username\":\"admin\",\"password\":\"admin123\",\"dbname\":\"USER\",\"sid\":\"orcl\"}', '127.0.0.1', 1521, 0, 0, 0, NULL, '1', '超级管理员', 1, '2025-05-25 02:00:37', '超级管理员', 1, '2025-05-25 02:00:37', NULL, '0');
INSERT INTO `da_datasource` VALUES (47, '测试-Hive', 'Hive', '{\"username\":\"hadoop\",\"dbname\":\"test\"}', '127.0.0.1', 10000, 0, 0, 0, NULL, '1', '超级管理员', 1, '2025-05-27 02:20:49', '超级管理员', 1, '2025-05-27 02:20:49', NULL, '0');
INSERT INTO `da_datasource` VALUES (48, '示例连接', 'DM8', '{\"username\":\"QDATA_TEST\",\"password\":\"123456\",\"dbname\":\"QDATA_TEST\"}', '127.0.0.1', 5236, 0, 0, 0, '示例数据源链接', '1', 'qData', 1, '2025-05-30 06:28:58', 'qData', 1, '2025-05-30 06:28:58', '不要对此数据库进行恶意操作，被发现者会给予处罚', '0');

-- ----------------------------
-- Table structure for da_datasource_project_rel
-- ----------------------------
DROP TABLE IF EXISTS `da_datasource_project_rel`;
CREATE TABLE `da_datasource_project_rel`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `project_id` bigint(20) NOT NULL COMMENT '项目id',
  `project_code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '项目编码',
  `datasource_id` bigint(20) NOT NULL COMMENT '数据源id',
  `description` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '描述',
  `valid_flag` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '1' COMMENT '是否有效;0：无效，1：有效',
  `create_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `creator_id` bigint(20) NULL DEFAULT NULL COMMENT '创建人id',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `updater_id` bigint(20) NULL DEFAULT NULL COMMENT '更新人id',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `dpp_assigned` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '0：分配到数据研发，1：数据研发的新增',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `ID_90095409115500`(`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 16 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '数据源与项目关联关系表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of da_datasource_project_rel
-- ----------------------------
INSERT INTO `da_datasource_project_rel` VALUES (1, 84, '141883958809440', 4, NULL, '1', 'qData', 1, '2025-05-24 10:35:28', 'qData', 1, '2025-05-24 10:35:28', NULL, '0');
INSERT INTO `da_datasource_project_rel` VALUES (2, 84, '141883958809440', 7, NULL, '1', 'qData', 1, '2025-05-24 10:35:28', 'qData', 1, '2025-05-24 10:35:28', NULL, '0');
INSERT INTO `da_datasource_project_rel` VALUES (3, 84, '141883958809440', 9, NULL, '1', 'qData', 1, '2025-05-24 10:35:28', 'qData', 1, '2025-05-24 10:35:28', NULL, '0');
INSERT INTO `da_datasource_project_rel` VALUES (4, 84, '141883958809440', 13, NULL, '1', 'qData', 1, '2025-05-24 10:35:28', 'qData', 1, '2025-05-24 10:35:28', NULL, '0');
INSERT INTO `da_datasource_project_rel` VALUES (5, 84, '141883958809440', 15, NULL, '1', 'qData', 1, '2025-05-24 10:35:28', 'qData', 1, '2025-05-24 10:35:28', NULL, '0');
INSERT INTO `da_datasource_project_rel` VALUES (6, 84, '141883958809440', 26, NULL, '1', 'qData', 1, '2025-05-24 10:35:28', 'qData', 1, '2025-05-24 10:35:28', NULL, '0');
INSERT INTO `da_datasource_project_rel` VALUES (7, 84, '141883958809440', 29, NULL, '1', 'qData', 1, '2025-05-24 10:35:28', 'qData', 1, '2025-05-24 10:35:28', NULL, '0');
INSERT INTO `da_datasource_project_rel` VALUES (8, 84, '141883958809440', 37, NULL, '1', 'qData', 1, '2025-05-24 10:35:28', 'qData', 1, '2025-05-24 10:35:28', NULL, '0');
INSERT INTO `da_datasource_project_rel` VALUES (9, 84, '141883958809440', 38, NULL, '1', 'qData', 1, '2025-05-24 10:35:28', 'qData', 1, '2025-05-24 10:35:28', NULL, '0');
INSERT INTO `da_datasource_project_rel` VALUES (10, 84, '141883958809440', 42, NULL, '1', 'qData', 1, '2025-05-24 10:35:28', 'qData', 1, '2025-05-24 10:35:28', NULL, '0');
INSERT INTO `da_datasource_project_rel` VALUES (11, 84, '141883958809440', 44, NULL, '1', 'qData', 1, '2025-05-24 10:35:28', 'qData', 1, '2025-05-24 10:35:28', NULL, '0');
INSERT INTO `da_datasource_project_rel` VALUES (12, 84, '141883958809440', 45, NULL, '1', 'qData', 1, '2025-05-24 10:35:28', 'qData', 1, '2025-05-24 10:35:28', NULL, '0');
INSERT INTO `da_datasource_project_rel` VALUES (13, 84, '141883958809440', 46, NULL, '1', 'qData', 1, '2025-05-24 10:35:28', 'qData', 1, '2025-05-24 10:35:28', NULL, '0');
INSERT INTO `da_datasource_project_rel` VALUES (14, 84, '141883958809440', 47, NULL, '1', 'qData', 1, '2025-05-24 10:35:28', 'qData', 1, '2025-05-24 10:35:28', NULL, '0');
INSERT INTO `da_datasource_project_rel` VALUES (15, 84, '141883958809440', 48, NULL, '1', 'qData', 1, '2025-05-24 10:35:28', 'qData', 1, '2025-05-24 10:35:28', NULL, '0');

-- ----------------------------
-- Table structure for da_sensitive_level
-- ----------------------------
DROP TABLE IF EXISTS `da_sensitive_level`;
CREATE TABLE `da_sensitive_level`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `sensitive_level` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `sensitive_rule` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '敏感规则',
  `start_char_loc` int(11) NULL DEFAULT NULL COMMENT '起始字符位置',
  `end_char_loc` int(11) NULL DEFAULT NULL COMMENT '截止字符位置',
  `mask_character` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '遮盖字符',
  `online_flag` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '1' COMMENT '上下线标识;0：下线，1：上线',
  `description` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '描述',
  `valid_flag` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '1' COMMENT '是否有效;0：无效，1：有效',
  `del_flag` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '删除标志;1：已删除，0：未删除',
  `create_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `creator_id` bigint(20) NULL DEFAULT NULL COMMENT '创建人id',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `updater_id` bigint(20) NULL DEFAULT NULL COMMENT '更新人id',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `ID_90099918903600`(`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 14 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '敏感等级表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of da_sensitive_level
-- ----------------------------
INSERT INTO `da_sensitive_level` VALUES (1, '1级', '1', NULL, NULL, '***', '1', '一级敏感', '1', '1', 'qData', 1, '2025-01-21 16:48:17', 'qData', 1, '2025-02-06 16:33:54', '阿斯顿');
INSERT INTO `da_sensitive_level` VALUES (7, '2级', '3', 2, 1, '2', '1', '从第二位开始进行字符替换', '1', '0', 'qData', 1, '2025-01-22 14:54:43', '超级管理员', 1, '2025-03-10 10:13:30', NULL);
INSERT INTO `da_sensitive_level` VALUES (8, '3级', '3', NULL, NULL, '2', '1', '等级较高，全部都替换掉', '1', '0', 'qData', 1, '2025-01-22 14:54:48', '超级管理员', 1, '2025-03-10 10:13:53', NULL);
INSERT INTO `da_sensitive_level` VALUES (10, '4级', '3', 2, 2, '*', '1', '从第二位开始到第二位结束，全部替换成*符号', '1', '0', 'qData', 1, '2025-01-22 15:28:13', '超级管理员', 1, '2025-03-10 10:14:22', NULL);
INSERT INTO `da_sensitive_level` VALUES (11, '3', '3', NULL, NULL, '3', '1', NULL, '1', '1', 'qData', 1, '2025-01-22 15:29:14', 'qData', 1, '2025-01-22 15:29:21', NULL);
INSERT INTO `da_sensitive_level` VALUES (12, '5级', '2', 1, 6, '*', '1', '从第一位开始进行替换截止到第6位进行替换', '1', '0', 'qData', 1, '2025-02-05 11:49:48', '超级管理员', 1, '2025-03-10 10:15:25', NULL);
INSERT INTO `da_sensitive_level` VALUES (13, '1级', '1', NULL, NULL, '***', '1', '全部都替换掉', '1', '0', '舒月鑫', 733, '2025-02-28 09:47:31', '超级管理员', 1, '2025-03-10 10:15:34', NULL);

-- ----------------------------
-- Table structure for dp_code_map
-- ----------------------------
DROP TABLE IF EXISTS `dp_code_map`;
CREATE TABLE `dp_code_map`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `data_elem_id` bigint(20) NOT NULL COMMENT '数据元id',
  `original_value` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '原始值',
  `code_name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '代码名',
  `code_value` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '代码值',
  `valid_flag` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '1' COMMENT '是否有效;0：无效，1：有效',
  `del_flag` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '删除标志;1：已删除，0：未删除',
  `create_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `creator_id` bigint(20) NULL DEFAULT NULL COMMENT '创建人id',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `updater_id` bigint(20) NULL DEFAULT NULL COMMENT '更新人id',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `ID_90101069373800`(`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '数据元代码映射表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of dp_code_map
-- ----------------------------
INSERT INTO `dp_code_map` VALUES (7, 11, '1', '女', 's', '1', '0', 'qData', 1, '2025-05-23 16:33:09', 'qData', 1, '2025-05-23 16:33:09', NULL);
INSERT INTO `dp_code_map` VALUES (8, 11, '0', '男', 'm', '1', '0', 'qData', 1, '2025-05-23 16:33:13', 'qData', 1, '2025-05-23 16:33:13', NULL);

-- ----------------------------
-- Table structure for dp_data_elem
-- ----------------------------
DROP TABLE IF EXISTS `dp_data_elem`;
CREATE TABLE `dp_data_elem`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `code` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '编码',
  `name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '名称',
  `eng_name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '英文名称',
  `cat_code` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '类目编码',
  `type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '1' COMMENT '类型;1:数据元 2:代码表',
  `person_charge` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '责任人',
  `contact_number` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '联系电话',
  `column_type` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '字段类型',
  `status` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '1' COMMENT '状态;0:禁用  1:启用',
  `description` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '描述',
  `valid_flag` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '1' COMMENT '是否有效;0：无效，1：有效',
  `del_flag` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '删除标志;1：已删除，0：未删除',
  `create_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `creator_id` bigint(20) NULL DEFAULT NULL COMMENT '创建人id',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `updater_id` bigint(20) NULL DEFAULT NULL COMMENT '更新人id',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `ID_90101809430700`(`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 19 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '数据元表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of dp_data_elem
-- ----------------------------
INSERT INTO `dp_data_elem` VALUES (9, NULL, '身份证号', 'idno', 'A01', '1', '731', '13462477777', 'VARCHAR', '0', '', '1', '0', 'qData', 1, '2025-02-05 09:50:08', 'qData', 1, '2025-05-23 16:34:27', NULL);
INSERT INTO `dp_data_elem` VALUES (12, NULL, '姓别', 'sex', 'A01', '2', '0', NULL, 'CHAR', '1', NULL, '1', '0', 'qData', 1, '2025-02-05 11:32:11', 'qData', 1, '2025-05-23 16:34:32', NULL);
INSERT INTO `dp_data_elem` VALUES (13, 'DE_USER_ID', '用户ID', 'USER_ID', 'CAT_USER', '1', '张三', '13800000001', 'BIGINT', '1', '用户主键', '1', '0', 'admin', 1, '2025-05-30 10:00:00', 'admin', 1, '2025-05-30 10:00:00', '主键');
INSERT INTO `dp_data_elem` VALUES (14, 'DE_USERNAME', '用户名', 'USERNAME', 'CAT_USER', '1', '张三', '13800000001', 'VARCHAR2', '1', '用户名称', '1', '0', 'admin', 1, '2025-05-30 10:00:00', 'admin', 1, '2025-05-30 10:00:00', '必填');
INSERT INTO `dp_data_elem` VALUES (15, 'DE_ORDER_ID', '订单ID', 'ORDER_ID', 'CAT_ORDER', '1', '李四', '13800000002', 'BIGINT', '1', '订单主键', '1', '0', 'admin', 1, '2025-05-30 10:05:00', 'admin', 1, '2025-05-30 10:05:00', '主键');
INSERT INTO `dp_data_elem` VALUES (16, 'DE_ORDER_AMOUNT', '订单金额', 'ORDER_AMOUNT', 'CAT_ORDER', '1', '李四', '13800000002', 'DECIMAL', '1', '订单金额', '1', '0', 'admin', 1, '2025-05-30 10:05:00', 'admin', 1, '2025-05-30 10:05:00', '必填');
INSERT INTO `dp_data_elem` VALUES (17, 'DE_PRODUCT_ID', '产品ID', 'PRODUCT_ID', 'CAT_PROD', '1', '王五', '13800000003', 'BIGINT', '1', '产品主键', '1', '0', 'admin', 1, '2025-05-30 10:10:00', 'admin', 1, '2025-05-30 10:10:00', '主键');
INSERT INTO `dp_data_elem` VALUES (18, 'DE_PRODUCT_NAME', '产品名称', 'PRODUCT_NAME', 'CAT_PROD', '1', '王五', '13800000003', 'VARCHAR2', '1', '产品名称', '1', '0', 'admin', 1, '2025-05-30 10:10:00', 'admin', 1, '2025-05-30 10:10:00', '必填');

-- ----------------------------
-- Table structure for dp_data_elem_asset_rel
-- ----------------------------
DROP TABLE IF EXISTS `dp_data_elem_asset_rel`;
CREATE TABLE `dp_data_elem_asset_rel`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `data_elem_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '数据元类型;1:数据元 2:代码表',
  `data_elem_id` bigint(20) NOT NULL COMMENT '数据元id',
  `asset_id` bigint(20) NOT NULL COMMENT '资产id(数据表id)',
  `table_name` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '数据表',
  `column_id` bigint(20) NOT NULL COMMENT '关联字段id',
  `column_name` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '关联字段',
  `valid_flag` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '1' COMMENT '是否有效;0：无效，1：有效',
  `del_flag` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '删除标志;1：已删除，0：未删除',
  `create_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `creator_id` bigint(20) NULL DEFAULT NULL COMMENT '创建人id',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `updater_id` bigint(20) NULL DEFAULT NULL COMMENT '更新人id',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `ID_90103512542700`(`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '数据元数据资产关联信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of dp_data_elem_asset_rel
-- ----------------------------

-- ----------------------------
-- Table structure for dp_data_elem_code
-- ----------------------------
DROP TABLE IF EXISTS `dp_data_elem_code`;
CREATE TABLE `dp_data_elem_code`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `data_elem_id` bigint(20) NOT NULL COMMENT '数据元id',
  `code_value` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '代码值',
  `code_name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '代码名称',
  `valid_flag` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '1' COMMENT '是否有效;0：无效，1：有效',
  `del_flag` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '删除标志;1：已删除，0：未删除',
  `create_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `creator_id` bigint(20) NULL DEFAULT NULL COMMENT '创建人id',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `updater_id` bigint(20) NULL DEFAULT NULL COMMENT '更新人id',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `ID_90104431096000`(`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 17 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '数据元代码表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of dp_data_elem_code
-- ----------------------------
INSERT INTO `dp_data_elem_code` VALUES (5, 4, 's', '女', '1', '0', 'qData', 1, '2025-01-22 11:15:21', 'qData', 1, '2025-01-22 11:24:18', 's代表的是女生');
INSERT INTO `dp_data_elem_code` VALUES (10, 12, '1', '男', '1', '0', 'qData', 1, '2025-02-05 11:32:31', 'qData', 1, '2025-02-05 11:32:31', NULL);
INSERT INTO `dp_data_elem_code` VALUES (11, 12, '2', '女', '1', '0', 'qData', 1, '2025-02-05 11:32:37', 'qData', 1, '2025-02-05 11:32:37', NULL);
INSERT INTO `dp_data_elem_code` VALUES (15, 11, 's', '女', '1', '0', 'qData', 1, '2025-05-23 16:32:48', 'qData', 1, '2025-05-23 16:32:48', NULL);
INSERT INTO `dp_data_elem_code` VALUES (16, 11, 'm', '男', '1', '0', 'qData', 1, '2025-05-23 16:32:57', 'qData', 1, '2025-05-23 16:32:57', NULL);

-- ----------------------------
-- Table structure for dp_data_elem_rule_rel
-- ----------------------------
DROP TABLE IF EXISTS `dp_data_elem_rule_rel`;
CREATE TABLE `dp_data_elem_rule_rel`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `data_elem_id` bigint(20) NOT NULL COMMENT '数据元id',
  `rule_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '规则类型;1:稽核规则 2:清洗规则',
  `rule_id` bigint(20) NOT NULL COMMENT '规则id',
  `rule_config` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '规则配置;json字符串',
  `valid_flag` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '1' COMMENT '是否有效;0：无效，1：有效',
  `del_flag` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '删除标志;1：已删除，0：未删除',
  `create_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `creator_id` bigint(20) NULL DEFAULT NULL COMMENT '创建人id',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `updater_id` bigint(20) NULL DEFAULT NULL COMMENT '更新人id',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `ID_90104743817000`(`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 57 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '数据元数据规则关联信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of dp_data_elem_rule_rel
-- ----------------------------
INSERT INTO `dp_data_elem_rule_rel` VALUES (54, 9, '2', 8, '{\"toggleCase\":{\"type\":\"2\"},\"nullReplace\":{\"value\":null},\"trimSpace\":{}}', '1', '0', 'qData', 1, '2025-05-23 16:31:55', 'qData', 1, '2025-05-23 16:32:07', NULL);
INSERT INTO `dp_data_elem_rule_rel` VALUES (55, 9, '2', 10, '{\"toggleCase\":{\"type\":\"1\"},\"nullReplace\":{\"value\":null},\"trimSpace\":{}}', '1', '0', 'qData', 1, '2025-05-23 16:32:07', 'qData', 1, '2025-05-23 16:32:07', NULL);
INSERT INTO `dp_data_elem_rule_rel` VALUES (56, 9, '1', 1, '{\"nullCheck\":{\"type\":\"2\"}}', '1', '0', 'qData', 1, '2025-05-23 16:32:26', 'qData', 1, '2025-05-23 16:32:26', NULL);

-- ----------------------------
-- Table structure for dp_model
-- ----------------------------
DROP TABLE IF EXISTS `dp_model`;
CREATE TABLE `dp_model`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `model_name` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '模型编码;表名称',
  `model_comment` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '模型名称;表注释/表描述',
  `cat_code` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '类目编码',
  `status` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '状态;0:未发布，1:已发布',
  `create_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '1' COMMENT '创建方式;1:手动录入,2:物化表生成',
  `datasource_id` bigint(20) NULL DEFAULT NULL COMMENT '数据源id',
  `contact` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '联系人',
  `contact_number` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '联系电话',
  `description` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '描述',
  `valid_flag` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '1' COMMENT '是否有效;0：无效，1：有效',
  `del_flag` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '删除标志;1：已删除，0：未删除',
  `create_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `creator_id` bigint(20) NULL DEFAULT NULL COMMENT '创建人id',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `updater_id` bigint(20) NULL DEFAULT NULL COMMENT '更新人id',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `ID_90105842552000`(`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 68 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '逻辑模型表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of dp_model
-- ----------------------------
INSERT INTO `dp_model` VALUES (54, 'Q_DATA_TEST', '示例逻辑模型', 'A01', '1', '1', NULL, '0', '', '千数开源平台所初始化的示例代码', '1', '0', 'qData', 1, '2025-05-23 16:36:42', '管理员', 1, '2025-05-29 06:31:42', NULL);
INSERT INTO `dp_model` VALUES (55, 'DA_ASSET', '数据资产表', 'A01', '0', '2', 48, '', '', '', '1', '0', 'qData', 1, '2025-05-23 16:44:19', 'qData', 1, '2025-05-23 16:44:19', NULL);
INSERT INTO `dp_model` VALUES (62, 'DP_USER_INFO', '用于存储用户基础信息的模型', 'CAT_USER', '0', '1', 1001, '张三', '13800000001', '包含用户ID、姓名、邮箱等字段', '1', '0', 'admin', 1, '2025-05-30 10:00:00', 'admin', 1, '2025-05-30 10:00:00', 'Initial version');
INSERT INTO `dp_model` VALUES (63, 'DP_ORDER', '用于存储订单信息的模型', 'CAT_ORDER', '0', '1', 1002, '李四', '13800000002', '包含订单ID、金额、状态等字段', '1', '0', 'admin', 1, '2025-05-30 10:05:00', 'admin', 1, '2025-05-30 10:05:00', 'Initial version');
INSERT INTO `dp_model` VALUES (64, 'DP_PRODUCT', '用于存储产品信息的模型', 'CAT_PROD', '0', '1', 1003, '王五', '13800000003', '包含产品ID、名称、价格等字段', '1', '0', 'admin', 1, '2025-05-30 10:10:00', 'admin', 1, '2025-05-30 10:10:00', 'Initial version');
INSERT INTO `dp_model` VALUES (65, 'DP_INVENTORY', '用于存储库存信息的模型', 'CAT_INV', '0', '1', 1004, '赵六', '13800000004', '包含库存ID、数量、仓库信息等字段', '1', '0', 'admin', 1, '2025-05-30 10:15:00', 'admin', 1, '2025-05-30 10:15:00', 'Initial version');
INSERT INTO `dp_model` VALUES (66, 'DP_CUSTOMER', '用于存储客户信息的模型', 'CAT_CUST', '0', '1', 1005, '孙七', '13800000005', '包含客户ID、姓名、联系方式等字段', '1', '0', 'admin', 1, '2025-05-30 10:20:00', 'admin', 1, '2025-05-30 10:20:00', 'Initial version');
INSERT INTO `dp_model` VALUES (67, 'DP_PAYMENT', '用于存储支付信息的模型', 'CAT_PAY', '0', '1', 1006, '周八', '13800000006', '包含支付ID、金额、支付方式等字段', '1', '0', 'admin', 1, '2025-05-30 10:25:00', 'admin', 1, '2025-05-30 10:25:00', 'Initial version');

-- ----------------------------
-- Table structure for dp_model_column
-- ----------------------------
DROP TABLE IF EXISTS `dp_model_column`;
CREATE TABLE `dp_model_column`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `model_id` bigint(20) NOT NULL COMMENT '逻辑模型表ID',
  `eng_name` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '英文名称;表字段名称',
  `cn_name` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '中文名称;表字段备注',
  `column_type` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '数据类型',
  `column_length` int(11) NULL DEFAULT NULL COMMENT '属性长度',
  `column_scale` int(11) NULL DEFAULT NULL COMMENT '小数长度',
  `default_value` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '默认值',
  `pk_flag` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '是否主键;是否主键（0否，1是）',
  `nullable_flag` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '是否必填;是否必填（0否，1是）',
  `sort_order` int(11) NULL DEFAULT NULL COMMENT '排序',
  `authority_dept` bigint(20) NULL DEFAULT NULL COMMENT '权威部门',
  `data_elem_id` bigint(20) NULL DEFAULT NULL COMMENT '数据元id',
  `valid_flag` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '1' COMMENT '是否有效;0：无效，1：有效',
  `del_flag` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '删除标志;1：已删除，0：未删除',
  `create_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `creator_id` bigint(20) NULL DEFAULT NULL COMMENT '创建人id',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `updater_id` bigint(20) NULL DEFAULT NULL COMMENT '更新人id',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `ID_90106678773800`(`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 429 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '逻辑模型属性信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of dp_model_column
-- ----------------------------
INSERT INTO `dp_model_column` VALUES (392, 54, 'ID', '主键id', 'TINYINT', 9, 0, '', '1', '1', NULL, NULL, NULL, '1', '0', 'qData', 1, '2025-05-23 16:36:43', 'qData', 1, '2025-05-23 16:36:43', NULL);
INSERT INTO `dp_model_column` VALUES (393, 54, 'NAME', '姓名', 'VARCHAR2', 100, 0, '', '0', '0', NULL, NULL, NULL, '1', '0', 'qData', 1, '2025-05-23 16:36:43', 'qData', 1, '2025-05-23 16:36:43', NULL);
INSERT INTO `dp_model_column` VALUES (394, 54, 'idno', '身份证号', 'VARCHAR', 100, 0, '', '0', '0', NULL, NULL, 9, '1', '0', 'qData', 1, '2025-05-23 16:36:43', 'qData', 1, '2025-05-23 16:36:43', NULL);
INSERT INTO `dp_model_column` VALUES (395, 54, 'sex', '姓别', 'CHAR', 1, 0, '', '0', '0', NULL, NULL, 12, '1', '0', 'qData', 1, '2025-05-23 16:36:44', 'qData', 1, '2025-05-23 16:36:44', NULL);
INSERT INTO `dp_model_column` VALUES (396, 55, 'ID', 'ID', 'BIGINT', 8, 0, NULL, '1', '0', 1, NULL, NULL, '1', '0', 'qData', 1, '2025-05-23 16:44:19', 'qData', 1, '2025-05-23 16:44:19', NULL);
INSERT INTO `dp_model_column` VALUES (397, 55, 'NAME', '资产名称', 'VARCHAR2', 256, 0, NULL, '0', '0', 2, NULL, NULL, '1', '0', 'qData', 1, '2025-05-23 16:44:19', 'qData', 1, '2025-05-23 16:44:19', NULL);
INSERT INTO `dp_model_column` VALUES (398, 55, 'CAT_CODE', '类目编码', 'VARCHAR2', 256, 0, NULL, '0', '0', 3, NULL, NULL, '1', '0', 'qData', 1, '2025-05-23 16:44:19', 'qData', 1, '2025-05-23 16:44:19', NULL);
INSERT INTO `dp_model_column` VALUES (399, 55, 'DATASOURCE_ID', '数据连接id', 'VARCHAR2', 8188, 0, NULL, '0', '0', 4, NULL, NULL, '1', '0', 'qData', 1, '2025-05-23 16:44:19', 'qData', 1, '2025-05-23 16:44:19', NULL);
INSERT INTO `dp_model_column` VALUES (400, 55, 'TABLE_NAME', '表名称', 'VARCHAR2', 256, 0, NULL, '0', '0', 5, NULL, NULL, '1', '0', 'qData', 1, '2025-05-23 16:44:19', 'qData', 1, '2025-05-23 16:44:19', NULL);
INSERT INTO `dp_model_column` VALUES (401, 55, 'TABLE_COMMENT', '表描述;表注释', 'VARCHAR2', 2000, 0, NULL, '0', '1', 6, NULL, NULL, '1', '0', 'qData', 1, '2025-05-23 16:44:19', 'qData', 1, '2025-05-23 16:44:19', NULL);
INSERT INTO `dp_model_column` VALUES (402, 55, 'DATA_COUNT', '数据量', 'INTEGER', 4, 0, '0', '0', '0', 7, NULL, NULL, '1', '0', 'qData', 1, '2025-05-23 16:44:19', 'qData', 1, '2025-05-23 16:44:19', NULL);
INSERT INTO `dp_model_column` VALUES (403, 55, 'FIELD_COUNT', '字段量', 'INTEGER', 4, 0, '0', '0', '0', 8, NULL, NULL, '1', '0', 'qData', 1, '2025-05-23 16:44:19', 'qData', 1, '2025-05-23 16:44:19', NULL);
INSERT INTO `dp_model_column` VALUES (404, 55, 'STATUS', '状态;1:未发布 2:已发布 3:审核中(审核预留字典)', 'CHAR', 1, 0, '1', '0', '0', 9, NULL, NULL, '1', '0', 'qData', 1, '2025-05-23 16:44:19', 'qData', 1, '2025-05-23 16:44:19', NULL);
INSERT INTO `dp_model_column` VALUES (405, 55, 'DESCRIPTION', '描述', 'VARCHAR2', 512, 0, NULL, '0', '1', 10, NULL, NULL, '1', '0', 'qData', 1, '2025-05-23 16:44:19', 'qData', 1, '2025-05-23 16:44:19', NULL);
INSERT INTO `dp_model_column` VALUES (406, 55, 'VALID_FLAG', '是否有效;0：无效，1：有效', 'VARCHAR2', 1, 0, '1', '0', '0', 11, NULL, NULL, '1', '0', 'qData', 1, '2025-05-23 16:44:19', 'qData', 1, '2025-05-23 16:44:19', NULL);
INSERT INTO `dp_model_column` VALUES (407, 55, 'DEL_FLAG', '删除标志;1：已删除，0：未删除', 'VARCHAR2', 1, 0, '0', '0', '0', 12, NULL, NULL, '1', '0', 'qData', 1, '2025-05-23 16:44:19', 'qData', 1, '2025-05-23 16:44:19', NULL);
INSERT INTO `dp_model_column` VALUES (408, 55, 'CREATE_BY', '创建人', 'VARCHAR2', 32, 0, NULL, '0', '1', 13, NULL, NULL, '1', '0', 'qData', 1, '2025-05-23 16:44:19', 'qData', 1, '2025-05-23 16:44:19', NULL);
INSERT INTO `dp_model_column` VALUES (409, 55, 'CREATOR_ID', '创建人id', 'BIGINT', 8, 0, NULL, '0', '1', 14, NULL, NULL, '1', '0', 'qData', 1, '2025-05-23 16:44:19', 'qData', 1, '2025-05-23 16:44:19', NULL);
INSERT INTO `dp_model_column` VALUES (410, 55, 'CREATE_TIME', '创建时间', 'DATETIME', 8, 6, 'CURRENT_TIMESTAMP', '0', '0', 15, NULL, NULL, '1', '0', 'qData', 1, '2025-05-23 16:44:19', 'qData', 1, '2025-05-23 16:44:19', NULL);
INSERT INTO `dp_model_column` VALUES (411, 55, 'UPDATE_BY', '更新人', 'VARCHAR2', 32, 0, NULL, '0', '1', 16, NULL, NULL, '1', '0', 'qData', 1, '2025-05-23 16:44:19', 'qData', 1, '2025-05-23 16:44:19', NULL);
INSERT INTO `dp_model_column` VALUES (412, 55, 'UPDATER_ID', '更新人id', 'BIGINT', 8, 0, NULL, '0', '1', 17, NULL, NULL, '1', '0', 'qData', 1, '2025-05-23 16:44:19', 'qData', 1, '2025-05-23 16:44:19', NULL);
INSERT INTO `dp_model_column` VALUES (413, 55, 'UPDATE_TIME', '更新时间', 'DATETIME', 8, 6, 'CURRENT_TIMESTAMP', '0', '0', 18, NULL, NULL, '1', '0', 'qData', 1, '2025-05-23 16:44:19', 'qData', 1, '2025-05-23 16:44:19', NULL);
INSERT INTO `dp_model_column` VALUES (414, 55, 'REMARK', '备注', 'VARCHAR2', 512, 0, NULL, '0', '1', 19, NULL, NULL, '1', '0', 'qData', 1, '2025-05-23 16:44:20', 'qData', 1, '2025-05-23 16:44:20', NULL);
INSERT INTO `dp_model_column` VALUES (415, 55, 'SOURCE', '来源;1:数据发现；2:数据模型；', 'CHAR', 1, 0, '\'0\'', '0', '0', 20, NULL, NULL, '1', '0', 'qData', 1, '2025-05-23 16:44:20', 'qData', 1, '2025-05-23 16:44:20', NULL);
INSERT INTO `dp_model_column` VALUES (416, 55, 'TYPE', '资产类型;1:数据库表  2:外部API 3: 地理空间服务 4:矢量数据 5:视频数据', 'VARCHAR2', 10, 0, '\'1\'', '0', '0', 21, NULL, NULL, '1', '0', 'qData', 1, '2025-05-23 16:44:20', 'qData', 1, '2025-05-23 16:44:20', NULL);
INSERT INTO `dp_model_column` VALUES (417, 62, 'USER_ID', '用户ID', 'BIGINT', 20, 0, NULL, '1', '0', 1, NULL, NULL, '1', '0', 'admin', 1, '2025-05-30 10:00:00', 'admin', 1, '2025-05-30 10:00:00', '主键');
INSERT INTO `dp_model_column` VALUES (418, 62, 'USERNAME', '用户名', 'VARCHAR2', 256, NULL, NULL, '0', '0', 2, NULL, NULL, '1', '0', 'admin', 1, '2025-05-30 10:00:00', 'admin', 1, '2025-05-30 10:00:00', '必填');
INSERT INTO `dp_model_column` VALUES (419, 63, 'ORDER_ID', '订单ID', 'BIGINT', 20, 0, NULL, '1', '0', 1, NULL, NULL, '1', '0', 'admin', 1, '2025-05-30 10:05:00', 'admin', 1, '2025-05-30 10:05:00', '主键');
INSERT INTO `dp_model_column` VALUES (420, 63, 'ORDER_AMOUNT', '订单金额', 'DECIMAL', 10, 2, NULL, '0', '0', 2, NULL, NULL, '1', '0', 'admin', 1, '2025-05-30 10:05:00', 'admin', 1, '2025-05-30 10:05:00', '必填');
INSERT INTO `dp_model_column` VALUES (421, 64, 'PRODUCT_ID', '产品ID', 'BIGINT', 20, 0, NULL, '1', '0', 1, NULL, NULL, '1', '0', 'admin', 1, '2025-05-30 10:10:00', 'admin', 1, '2025-05-30 10:10:00', '主键');
INSERT INTO `dp_model_column` VALUES (422, 64, 'PRODUCT_NAME', '产品名称', 'VARCHAR2', 256, NULL, NULL, '0', '0', 2, NULL, NULL, '1', '0', 'admin', 1, '2025-05-30 10:10:00', 'admin', 1, '2025-05-30 10:10:00', '必填');
INSERT INTO `dp_model_column` VALUES (423, 65, 'INVENTORY_ID', '库存ID', 'BIGINT', 20, 0, NULL, '1', '0', 1, NULL, NULL, '1', '0', 'admin', 1, '2025-05-30 10:15:00', 'admin', 1, '2025-05-30 10:15:00', '主键');
INSERT INTO `dp_model_column` VALUES (424, 65, 'QUANTITY', '库存数量', 'INTEGER', NULL, NULL, NULL, '0', '0', 2, NULL, NULL, '1', '0', 'admin', 1, '2025-05-30 10:15:00', 'admin', 1, '2025-05-30 10:15:00', '必填');
INSERT INTO `dp_model_column` VALUES (425, 66, 'CUSTOMER_ID', '客户ID', 'BIGINT', 20, 0, NULL, '1', '0', 1, NULL, NULL, '1', '0', 'admin', 1, '2025-05-30 10:20:00', 'admin', 1, '2025-05-30 10:20:00', '主键');
INSERT INTO `dp_model_column` VALUES (426, 66, 'CUSTOMER_NAME', '客户名称', 'VARCHAR2', 256, NULL, NULL, '0', '0', 2, NULL, NULL, '1', '0', 'admin', 1, '2025-05-30 10:20:00', 'admin', 1, '2025-05-30 10:20:00', '必填');
INSERT INTO `dp_model_column` VALUES (427, 67, 'PAYMENT_ID', '支付ID', 'BIGINT', 20, 0, NULL, '1', '0', 1, NULL, NULL, '1', '0', 'admin', 1, '2025-05-30 10:25:00', 'admin', 1, '2025-05-30 10:25:00', '主键');
INSERT INTO `dp_model_column` VALUES (428, 67, 'PAYMENT_AMOUNT', '支付金额', 'DECIMAL', 10, 2, NULL, '0', '0', 2, NULL, NULL, '1', '0', 'admin', 1, '2025-05-30 10:25:00', 'admin', 1, '2025-05-30 10:25:00', '必填');

-- ----------------------------
-- Table structure for dp_model_materialized
-- ----------------------------
DROP TABLE IF EXISTS `dp_model_materialized`;
CREATE TABLE `dp_model_materialized`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `model_name` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '模型编码;表名',
  `model_alias` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '模型名称;表描述、名称',
  `model_id` bigint(20) NOT NULL COMMENT '模型表id',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '状态;1未创建，2创建中，3成功，4失败，5已存在。',
  `message` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '执行日志信息',
  `sql_command` varchar(3072) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '执行sql备份',
  `datasource_id` bigint(20) NOT NULL COMMENT '数据源id',
  `datasource_type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '数据源类型',
  `datasource_name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '数据源名称',
  `asset_id` bigint(20) NULL DEFAULT NULL COMMENT '资产表id',
  `valid_flag` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '1' COMMENT '是否有效;0：无效，1：有效',
  `del_flag` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '删除标志;1：已删除，0：未删除',
  `create_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `creator_id` bigint(20) NULL DEFAULT NULL COMMENT '创建人id',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `updater_id` bigint(20) NULL DEFAULT NULL COMMENT '更新人id',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `ID_90108344914500`(`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '物化模型记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of dp_model_materialized
-- ----------------------------

-- ----------------------------
-- Table structure for dpp_etl_node
-- ----------------------------
DROP TABLE IF EXISTS `dpp_etl_node`;
CREATE TABLE `dpp_etl_node`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `task_type` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '1' COMMENT '任务类型;1：离线任务 2：实时任务 3：数据开发任务 4：作业任务',
  `type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '节点类型;DATAX、SPARK及SUB_PROCESS等',
  `name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '节点名称',
  `code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '节点编码',
  `version` bigint(20) NOT NULL DEFAULT 1 COMMENT '节点版本',
  `project_id` bigint(20) NOT NULL COMMENT '项目id',
  `project_code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '项目编码',
  `parameters` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '节点参数',
  `priority` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'MEDIUM' COMMENT '任务优先级;HIGHEST,HIGH,MEDIUM,LOW,LOWEST',
  `fail_retry_times` int(11) NULL DEFAULT 0 COMMENT '失败重试次数',
  `fail_retry_interval` int(11) NULL DEFAULT 1 COMMENT '失败重试间隔（分钟）',
  `timeout` int(11) NULL DEFAULT 0 COMMENT '超时时间',
  `delay_time` int(11) NULL DEFAULT 0 COMMENT '延迟执行时间（分钟）',
  `cpu_quota` int(11) NULL DEFAULT -1 COMMENT 'CPU配额',
  `memory_max` int(11) NULL DEFAULT -1 COMMENT '最大内存',
  `description` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '描述',
  `ds_id` bigint(20) NULL DEFAULT NULL COMMENT 'DolphinScheduler的id',
  `valid_flag` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '1' COMMENT '是否有效;0：无效，1：有效',
  `del_flag` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '删除标志;1：已删除，0：未删除',
  `create_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `creator_id` bigint(20) NULL DEFAULT NULL COMMENT '创建人id',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `updater_id` bigint(20) NULL DEFAULT NULL COMMENT '更新人id',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `component_type` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '1' COMMENT '组件类型',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `ID_90109803313800`(`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1230 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '数据集成节点' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of dpp_etl_node
-- ----------------------------
INSERT INTO `dpp_etl_node` VALUES (1222, '3', 'SQL', '示例-数据开发', '141950767490912', 1, 84, '141883958809440', '{\"sqlType\":\"0\",\"type\":\"DM8\",\"sql\":\"SELECT * QDATA_OPEN.DS_API\",\"typaCode\":\"DM\",\"localParams\":[],\"datasources\":{\"datasourceType\":\"DM8\",\"datasourceConfig\":\"{\\\"username\\\":\\\"QDATA_OPEN\\\",\\\"password\\\":\\\"Iqo0tpF$~8R$Zn1yvhJA\\\",\\\"dbname\\\":\\\"QDATA_OPEN\\\"}\",\"ip\":\"110.42.38.62\",\"port\":40013,\"dbname\":\"QDATA\",\"datasource_id\":48,\"datasourceId\":48}}', 'MEDIUM', 0, 0, 0, 0, -1, -1, '', 552, '1', '0', 'qData', 1, '2025-05-24 10:36:40', 'qData', 1, '2025-05-24 10:36:40', NULL, '51');
INSERT INTO `dpp_etl_node` VALUES (1223, '3', 'SQL', '示例-数据开发带参数', '141950875765600', 1, 84, '141883958809440', '{\"sqlType\":\"0\",\"type\":\"DM8\",\"sql\":\"SELECT * QDATA_OPEN.DS_API WHERE id > ${id}\",\"typaCode\":\"DM\",\"localParams\":[{\"prop\":\"id\",\"type\":\"TINYINT\",\"value\":\"1\",\"direct\":\"IN\"}],\"datasources\":{\"datasourceType\":\"DM8\",\"datasourceConfig\":\"{\\\"username\\\":\\\"QDATA_OPEN\\\",\\\"password\\\":\\\"Iqo0tpF$~8R$Zn1yvhJA\\\",\\\"dbname\\\":\\\"QDATA_OPEN\\\"}\",\"ip\":\"110.42.38.62\",\"port\":40013,\"dbname\":\"QDATA\",\"datasource_id\":48,\"datasourceId\":48}}', 'MEDIUM', 0, 0, 0, 0, -1, -1, '', 553, '1', '0', 'qData', 1, '2025-05-24 10:38:25', 'qData', 1, '2025-05-24 10:38:25', NULL, '51');
INSERT INTO `dpp_etl_node` VALUES (1224, '3', 'PROCEDURE', '示例-数据开发插入', '141951009362784', 1, 84, '141883958809440', '{\"sqlType\":\"2\",\"type\":\"DM8\",\"sql\":\"INSERT INTO table_name (column1, column2, column3)\\nVALUES (value1, value2, value3)\",\"typaCode\":\"DM\",\"localParams\":[],\"datasources\":{\"datasourceType\":\"DM8\",\"datasourceConfig\":\"{\\\"username\\\":\\\"QDATA_OPEN\\\",\\\"password\\\":\\\"Iqo0tpF$~8R$Zn1yvhJA\\\",\\\"dbname\\\":\\\"QDATA_OPEN\\\"}\",\"ip\":\"110.42.38.62\",\"port\":40013,\"dbname\":\"QDATA\",\"datasource_id\":48,\"datasourceId\":48}}', 'MEDIUM', 0, 0, 0, 0, -1, -1, '', 554, '1', '0', 'qData', 1, '2025-05-24 10:40:34', 'qData', 1, '2025-05-24 10:40:34', NULL, '52');
INSERT INTO `dpp_etl_node` VALUES (1225, '1', 'DATAX', '表输入组件', '141951029426016', 1, 84, '141883958809440', '{\"tableFields\":[{\"columnName\":\"ID\",\"columnComment\":\"ID\",\"columnType\":\"INT\",\"columnLength\":4,\"columnScale\":0,\"nullableFlag\":\"0\",\"pkFlag\":\"1\"},{\"columnName\":\"TITLE\",\"columnComment\":\"消息标题\",\"columnType\":\"VARCHAR\",\"columnLength\":128,\"columnScale\":0,\"nullableFlag\":\"0\",\"pkFlag\":\"0\"},{\"columnName\":\"CONTENT\",\"columnComment\":\"消息模板内容\",\"columnType\":\"VARCHAR\",\"columnLength\":3072,\"columnScale\":0,\"nullableFlag\":\"0\",\"pkFlag\":\"0\"},{\"columnName\":\"CATEGORY\",\"columnComment\":\"消息类别\",\"columnType\":\"INT\",\"columnLength\":4,\"columnScale\":0,\"nullableFlag\":\"0\",\"pkFlag\":\"0\"},{\"columnName\":\"MSG_LEVEL\",\"columnComment\":\"消息等级\",\"columnType\":\"INT\",\"columnLength\":4,\"columnScale\":0,\"nullableFlag\":\"0\",\"pkFlag\":\"0\",\"defaultValue\":\"0\"},{\"columnName\":\"VALID_FLAG\",\"columnComment\":\"是否有效;0：无效，1：有效\",\"columnType\":\"TINYINT\",\"columnLength\":1,\"columnScale\":0,\"nullableFlag\":\"0\",\"pkFlag\":\"0\",\"defaultValue\":\"1\"},{\"columnName\":\"DEL_FLAG\",\"columnComment\":\"删除标志;1：已删除，0：未删除\",\"columnType\":\"TINYINT\",\"columnLength\":1,\"columnScale\":0,\"nullableFlag\":\"0\",\"pkFlag\":\"0\",\"defaultValue\":\"0\"},{\"columnName\":\"CREATE_BY\",\"columnComment\":\"创建人\",\"columnType\":\"VARCHAR\",\"columnLength\":32,\"columnScale\":0,\"nullableFlag\":\"1\",\"pkFlag\":\"0\"},{\"columnName\":\"CREATOR_ID\",\"columnComment\":\"创建人id\",\"columnType\":\"BIGINT\",\"columnLength\":8,\"columnScale\":0,\"nullableFlag\":\"1\",\"pkFlag\":\"0\"},{\"columnName\":\"CREATE_TIME\",\"columnComment\":\"创建时间\",\"columnType\":\"TIMESTAMP\",\"columnLength\":8,\"columnScale\":0,\"nullableFlag\":\"0\",\"pkFlag\":\"0\",\"defaultValue\":\"CURRENT_TIMESTAMP()\"},{\"columnName\":\"UPDATE_BY\",\"columnComment\":\"更新人\",\"columnType\":\"VARCHAR\",\"columnLength\":32,\"columnScale\":0,\"nullableFlag\":\"1\",\"pkFlag\":\"0\"},{\"columnName\":\"UPDATER_ID\",\"columnComment\":\"更新人id\",\"columnType\":\"BIGINT\",\"columnLength\":8,\"columnScale\":0,\"nullableFlag\":\"1\",\"pkFlag\":\"0\"},{\"columnName\":\"UPDATE_TIME\",\"columnComment\":\"更新时间\",\"columnType\":\"TIMESTAMP\",\"columnLength\":8,\"columnScale\":0,\"nullableFlag\":\"0\",\"pkFlag\":\"0\",\"defaultValue\":\"CURRENT_TIMESTAMP()\"},{\"columnName\":\"REMARK\",\"columnComment\":\"备注\",\"columnType\":\"VARCHAR\",\"columnLength\":512,\"columnScale\":0,\"nullableFlag\":\"1\",\"pkFlag\":\"0\"}],\"datasource_id\":\"\",\"csvFile\":\"\",\"columns\":[\"ID\",\"TITLE\",\"CONTENT\",\"CATEGORY\",\"MSG_LEVEL\",\"VALID_FLAG\",\"DEL_FLAG\",\"CREATE_BY\",\"CREATOR_ID\",\"CREATE_TIME\",\"UPDATE_BY\",\"UPDATER_ID\",\"UPDATE_TIME\",\"REMARK\"],\"typeName\":\"表输入组件\",\"icon\":\"data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACQAAAAeCAYAAABE4bxTAAAAAXNSR0IArs4c6QAAAXNJREFUWEftlz1LA0EQhp+52J3hYqOVhSklYCMINl7+gYUgWImdlY2lCBb+ABs7rUT0F1iJB1ZiIfjRBtIpgjFfWog7EsR4u2ISQZIr7sqXZee5mXd2Z4WEfRLnyYX1cBB8z1E2+oprAQVhUwcBVI38NscPoIyYwtNZ9q4fYCNhvWDwbjoCoeyK8NgPIBVGUVY7Agm6DFpuA4mXR1kBsxGHVLxFVGoi7ye2ntkU0QPUlOyf8g7BLFmayoSK7P+pZLliY8qobNUifz6+WRA21kEq1cjfs/S5xrE3xHbldPjaXt8sVSM/H9d6KpnroRTISasmLkPAJejLN6j4wATorWPIcUTewNzb5pVJoAzadPQZ0AtHa+09nZq6lZUgTLvs0x3Bf59DiTup0y7rdtsn7mBMgbqVLHld5kyMCmMgRUGP7EGMWVVePeEqrhtYEDgXeHDWrwnsWFovE2PqoW4eSjPULUP9eP64MX4d0BL3lB5EdtyYH6YFnD0Rf7InAAAAAElFTkSuQmCC\",\"asset_id\":\"MESSAGE_TEMPLATE\",\"type\":1,\"table_name\":\"MESSAGE_TEMPLATE\",\"logicOperator\":\"and\",\"parentId\":\"\",\"querySql\":\"\",\"taskType\":\"DATAX\",\"dateIncrementConfig\":{\"logic\":\"and\",\"dateFormat\":\"yyyy-MM-dd\",\"column\":[]},\"idIncrementConfig\":{\"incrementColumn\":[],\"incrementStart\":\"\"},\"readerDatasource\":{\"datasourceType\":\"DM8\",\"datasourceConfig\":\"{\\\"username\\\":\\\"QDATA_OPEN\\\",\\\"password\\\":\\\"Iqo0tpF$~8R$Zn1yvhJA\\\",\\\"dbname\\\":\\\"QDATA_OPEN\\\"}\",\"ip\":\"110.42.38.62\",\"port\":40013,\"dbname\":\"QDATA\",\"datasource_id\":48,\"datasourceId\":48},\"datasourceId\":\"\",\"topic\":\"\",\"where\":\"\",\"readModeType\":\"1\",\"batchSize\":\"1024\",\"config\":\"\",\"columnsList\":[{\"colName\":\"ID\",\"dataType\":\"INT\"},{\"colName\":\"TITLE\",\"dataType\":\"VARCHAR\"},{\"colName\":\"CONTENT\",\"dataType\":\"VARCHAR\"},{\"colName\":\"CATEGORY\",\"dataType\":\"INT\"},{\"colName\":\"MSG_LEVEL\",\"dataType\":\"INT\"},{\"colName\":\"VALID_FLAG\",\"dataType\":\"TINYINT\"},{\"colName\":\"DEL_FLAG\",\"dataType\":\"TINYINT\"},{\"colName\":\"CREATE_BY\",\"dataType\":\"VARCHAR\"},{\"colName\":\"CREATOR_ID\",\"dataType\":\"BIGINT\"},{\"colName\":\"CREATE_TIME\",\"dataType\":\"TIMESTAMP\"},{\"colName\":\"UPDATE_BY\",\"dataType\":\"VARCHAR\"},{\"colName\":\"UPDATER_ID\",\"dataType\":\"BIGINT\"},{\"colName\":\"UPDATE_TIME\",\"dataType\":\"TIMESTAMP\"},{\"colName\":\"REMARK\",\"dataType\":\"VARCHAR\"}],\"clmt\":\"0\"}', 'MEDIUM', 0, 1, 0, 0, -1, -1, NULL, NULL, '1', '0', 'qData', 1, '2025-05-24 10:41:50', 'qData', 1, '2025-05-24 10:41:50', NULL, '1');
INSERT INTO `dpp_etl_node` VALUES (1226, '1', 'DATAX', '表输出组件', '141951044262752', 1, 84, '141883958809440', '{\"postSql\":\"\",\"tableFields\":[{\"columnName\":\"ID\",\"columnComment\":\"ID\",\"columnType\":\"INT\",\"columnLength\":4,\"columnScale\":0,\"nullableFlag\":\"0\",\"pkFlag\":\"1\",\"isChecked\":true},{\"columnName\":\"TITLE\",\"columnComment\":\"消息标题\",\"columnType\":\"VARCHAR\",\"columnLength\":128,\"columnScale\":0,\"nullableFlag\":\"0\",\"pkFlag\":\"0\",\"isChecked\":true},{\"columnName\":\"CONTENT\",\"columnComment\":\"消息模板内容\",\"columnType\":\"VARCHAR\",\"columnLength\":3072,\"columnScale\":0,\"nullableFlag\":\"0\",\"pkFlag\":\"0\",\"isChecked\":true},{\"columnName\":\"CATEGORY\",\"columnComment\":\"消息类别\",\"columnType\":\"INT\",\"columnLength\":4,\"columnScale\":0,\"nullableFlag\":\"0\",\"pkFlag\":\"0\",\"isChecked\":true},{\"columnName\":\"MSG_LEVEL\",\"columnComment\":\"消息等级\",\"columnType\":\"INT\",\"columnLength\":4,\"columnScale\":0,\"nullableFlag\":\"0\",\"pkFlag\":\"0\",\"defaultValue\":\"0\",\"isChecked\":true},{\"columnName\":\"VALID_FLAG\",\"columnComment\":\"是否有效;0：无效，1：有效\",\"columnType\":\"TINYINT\",\"columnLength\":1,\"columnScale\":0,\"nullableFlag\":\"0\",\"pkFlag\":\"0\",\"defaultValue\":\"1\",\"isChecked\":true},{\"columnName\":\"DEL_FLAG\",\"columnComment\":\"删除标志;1：已删除，0：未删除\",\"columnType\":\"TINYINT\",\"columnLength\":1,\"columnScale\":0,\"nullableFlag\":\"0\",\"pkFlag\":\"0\",\"defaultValue\":\"0\",\"isChecked\":true},{\"columnName\":\"CREATE_BY\",\"columnComment\":\"创建人\",\"columnType\":\"VARCHAR\",\"columnLength\":32,\"columnScale\":0,\"nullableFlag\":\"1\",\"pkFlag\":\"0\",\"isChecked\":true},{\"columnName\":\"CREATOR_ID\",\"columnComment\":\"创建人id\",\"columnType\":\"BIGINT\",\"columnLength\":8,\"columnScale\":0,\"nullableFlag\":\"1\",\"pkFlag\":\"0\",\"isChecked\":true},{\"columnName\":\"CREATE_TIME\",\"columnComment\":\"创建时间\",\"columnType\":\"TIMESTAMP\",\"columnLength\":8,\"columnScale\":0,\"nullableFlag\":\"0\",\"pkFlag\":\"0\",\"defaultValue\":\"CURRENT_TIMESTAMP()\",\"isChecked\":true},{\"columnName\":\"UPDATE_BY\",\"columnComment\":\"更新人\",\"columnType\":\"VARCHAR\",\"columnLength\":32,\"columnScale\":0,\"nullableFlag\":\"1\",\"pkFlag\":\"0\",\"isChecked\":true},{\"columnName\":\"UPDATER_ID\",\"columnComment\":\"更新人id\",\"columnType\":\"BIGINT\",\"columnLength\":8,\"columnScale\":0,\"nullableFlag\":\"1\",\"pkFlag\":\"0\",\"isChecked\":true},{\"columnName\":\"UPDATE_TIME\",\"columnComment\":\"更新时间\",\"columnType\":\"TIMESTAMP\",\"columnLength\":8,\"columnScale\":0,\"nullableFlag\":\"0\",\"pkFlag\":\"0\",\"defaultValue\":\"CURRENT_TIMESTAMP()\",\"isChecked\":true},{\"columnName\":\"REMARK\",\"columnComment\":\"备注\",\"columnType\":\"VARCHAR\",\"columnLength\":512,\"columnScale\":0,\"nullableFlag\":\"1\",\"pkFlag\":\"0\",\"isChecked\":true}],\"selectedColumn\":\"\",\"target_columns\":[\"SCHED_NAME\",\"ENTRY_ID\",\"TRIGGER_NAME\",\"TRIGGER_GROUP\",\"INSTANCE_NAME\",\"FIRED_TIME\",\"SCHED_TIME\",\"PRIORITY\",\"STATE\",\"JOB_NAME\",\"JOB_GROUP\",\"IS_NONCONCURRENT\",\"REQUESTS_RECOVERY\"],\"target_table_name\":\"QRTZ_FIRED_TRIGGERS\",\"columns\":[\"ID\",\"TITLE\",\"CONTENT\",\"CATEGORY\",\"MSG_LEVEL\",\"VALID_FLAG\",\"DEL_FLAG\",\"CREATE_BY\",\"CREATOR_ID\",\"CREATE_TIME\",\"UPDATE_BY\",\"UPDATER_ID\",\"UPDATE_TIME\"],\"typeName\":\"表输出组件\",\"icon\":\"data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACQAAAAeCAYAAABE4bxTAAAAAXNSR0IArs4c6QAAAW9JREFUWEftl71LA0EQxd+Lpb1WFkkpAZuQEyvtTbQQBCuxs7KxlNxdwD/Axk4rEW29gK1g45lC8KMNpFMsBEEtxHsiknC7Yi6CJFfclo9l58fMm9ldImWLcR4vmJ4dBp9XuTjrxDWA/MDRMIDcStjl+AFERcVatXk3CDCvUSpSIzc9gUTsUnwcBBAQjQFc7wkEalXKtTtAOaogYE3ilgW5DOAZwGlcJ1QjcRCJLUs/FLhiaXkA+38qWT0oT4n03flwMX5Y/cTZFPTkVi/3jMZolI/xwW1vIbyO637gtNxKWDD39lEy20MZkJVWpS5DAJtA9NoFFUdB5AHdmqbmBIB3QPeW2SchtkG9WPsdQKGhfZ9dykz9lZWsyzre8P57DqVuUmddlnTbp24wZkBJJUtdl9kvRgHjhOYAHhn3EDVD4U3glXlnaUngOYEHS98AuGNqfbwYMw8leSjLUFKGBvP9MaP8+kBL3Vd6GNmxY34CUyDPPWNEMvYAAAAASUVORK5CYII=\",\"target_asset_id\":\"QRTZ_FIRED_TRIGGERS\",\"type\":2,\"parentId\":\"\",\"taskType\":\"DATAX\",\"selectedColumns\":[],\"writerDatasource\":{\"datasourceType\":\"DM8\",\"datasourceConfig\":\"{\\\"username\\\":\\\"QDATA_OPEN\\\",\\\"password\\\":\\\"Iqo0tpF$~8R$Zn1yvhJA\\\",\\\"dbname\\\":\\\"QDATA_OPEN\\\"}\",\"ip\":\"110.42.38.62\",\"port\":40013,\"dbname\":\"QDATA\",\"target_asset_id\":48,\"datasourceId\":48},\"toColumnsList\":[{\"columnName\":\"SCHED_NAME\",\"columnComment\":\"调度名称\",\"columnType\":\"VARCHAR\",\"columnLength\":120,\"columnScale\":0,\"nullableFlag\":\"0\",\"pkFlag\":\"1\",\"isChecked\":true},{\"columnName\":\"ENTRY_ID\",\"columnComment\":\"调度器实例id\",\"columnType\":\"VARCHAR\",\"columnLength\":95,\"columnScale\":0,\"nullableFlag\":\"0\",\"pkFlag\":\"1\",\"isChecked\":true},{\"columnName\":\"TRIGGER_NAME\",\"columnComment\":\"qrtz_triggers表trigger_name的外键\",\"columnType\":\"VARCHAR\",\"columnLength\":200,\"columnScale\":0,\"nullableFlag\":\"0\",\"pkFlag\":\"0\",\"isChecked\":true},{\"columnName\":\"TRIGGER_GROUP\",\"columnComment\":\"qrtz_triggers表trigger_group的外键\",\"columnType\":\"VARCHAR\",\"columnLength\":200,\"columnScale\":0,\"nullableFlag\":\"0\",\"pkFlag\":\"0\",\"isChecked\":true},{\"columnName\":\"INSTANCE_NAME\",\"columnComment\":\"调度器实例名\",\"columnType\":\"VARCHAR\",\"columnLength\":200,\"columnScale\":0,\"nullableFlag\":\"0\",\"pkFlag\":\"0\",\"isChecked\":true},{\"columnName\":\"FIRED_TIME\",\"columnComment\":\"触发的时间\",\"columnType\":\"BIGINT\",\"columnLength\":8,\"columnScale\":0,\"nullableFlag\":\"0\",\"pkFlag\":\"0\",\"isChecked\":true},{\"columnName\":\"SCHED_TIME\",\"columnComment\":\"定时器制定的时间\",\"columnType\":\"BIGINT\",\"columnLength\":8,\"columnScale\":0,\"nullableFlag\":\"0\",\"pkFlag\":\"0\",\"isChecked\":true},{\"columnName\":\"PRIORITY\",\"columnComment\":\"优先级\",\"columnType\":\"INT\",\"columnLength\":4,\"columnScale\":0,\"nullableFlag\":\"0\",\"pkFlag\":\"0\",\"isChecked\":true},{\"columnName\":\"STATE\",\"columnComment\":\"状态\",\"columnType\":\"VARCHAR\",\"columnLength\":16,\"columnScale\":0,\"nullableFlag\":\"0\",\"pkFlag\":\"0\",\"isChecked\":true},{\"columnName\":\"JOB_NAME\",\"columnComment\":\"任务名称\",\"columnType\":\"VARCHAR\",\"columnLength\":200,\"columnScale\":0,\"nullableFlag\":\"1\",\"pkFlag\":\"0\",\"isChecked\":true},{\"columnName\":\"JOB_GROUP\",\"columnComment\":\"任务组名\",\"columnType\":\"VARCHAR\",\"columnLength\":200,\"columnScale\":0,\"nullableFlag\":\"1\",\"pkFlag\":\"0\",\"isChecked\":true},{\"columnName\":\"IS_NONCONCURRENT\",\"columnComment\":\"是否并发\",\"columnType\":\"VARCHAR\",\"columnLength\":1,\"columnScale\":0,\"nullableFlag\":\"1\",\"pkFlag\":\"0\",\"isChecked\":true},{\"columnName\":\"REQUESTS_RECOVERY\",\"columnComment\":\"是否接受恢复执行\",\"columnType\":\"VARCHAR\",\"columnLength\":1,\"columnScale\":0,\"nullableFlag\":\"1\",\"pkFlag\":\"0\",\"isChecked\":true}],\"target_datasource_id\":\"\",\"datasourceId\":\"\",\"where\":\"\",\"batchSize\":\"1024\",\"config\":\"\",\"writeModeType\":2,\"preSql\":\"\"}', 'MEDIUM', 0, 1, 0, 0, -1, -1, NULL, NULL, '1', '0', 'qData', 1, '2025-05-24 10:41:50', 'qData', 1, '2025-05-24 10:41:50', NULL, '91');
INSERT INTO `dpp_etl_node` VALUES (1227, '1', 'DATAX', '表输入组件', '141951101460320', 1, 84, '141883958809440', '{\"tableFields\":[{\"columnName\":\"SCHED_NAME\",\"columnComment\":\"调度名称\",\"columnType\":\"VARCHAR\",\"columnLength\":120,\"columnScale\":0,\"nullableFlag\":\"0\",\"pkFlag\":\"1\"},{\"columnName\":\"TRIGGER_NAME\",\"columnComment\":\"qrtz_triggers表trigger_name的外键\",\"columnType\":\"VARCHAR\",\"columnLength\":200,\"columnScale\":0,\"nullableFlag\":\"0\",\"pkFlag\":\"1\"},{\"columnName\":\"TRIGGER_GROUP\",\"columnComment\":\"qrtz_triggers表trigger_group的外键\",\"columnType\":\"VARCHAR\",\"columnLength\":200,\"columnScale\":0,\"nullableFlag\":\"0\",\"pkFlag\":\"1\"},{\"columnName\":\"BLOB_DATA\",\"columnComment\":\"存放持久化Trigger对象\",\"columnType\":\"BLOB\",\"columnLength\":2147483647,\"columnScale\":0,\"nullableFlag\":\"1\",\"pkFlag\":\"0\"}],\"datasource_id\":\"\",\"csvFile\":\"\",\"columns\":[\"SCHED_NAME\",\"TRIGGER_NAME\",\"TRIGGER_GROUP\",\"BLOB_DATA\"],\"typeName\":\"表输入组件\",\"icon\":\"data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACQAAAAeCAYAAABE4bxTAAAAAXNSR0IArs4c6QAAAXNJREFUWEftlz1LA0EQhp+52J3hYqOVhSklYCMINl7+gYUgWImdlY2lCBb+ABs7rUT0F1iJB1ZiIfjRBtIpgjFfWog7EsR4u2ISQZIr7sqXZee5mXd2Z4WEfRLnyYX1cBB8z1E2+oprAQVhUwcBVI38NscPoIyYwtNZ9q4fYCNhvWDwbjoCoeyK8NgPIBVGUVY7Agm6DFpuA4mXR1kBsxGHVLxFVGoi7ye2ntkU0QPUlOyf8g7BLFmayoSK7P+pZLliY8qobNUifz6+WRA21kEq1cjfs/S5xrE3xHbldPjaXt8sVSM/H9d6KpnroRTISasmLkPAJejLN6j4wATorWPIcUTewNzb5pVJoAzadPQZ0AtHa+09nZq6lZUgTLvs0x3Bf59DiTup0y7rdtsn7mBMgbqVLHld5kyMCmMgRUGP7EGMWVVePeEqrhtYEDgXeHDWrwnsWFovE2PqoW4eSjPULUP9eP64MX4d0BL3lB5EdtyYH6YFnD0Rf7InAAAAAElFTkSuQmCC\",\"asset_id\":\"QRTZ_BLOB_TRIGGERS\",\"type\":1,\"table_name\":\"QRTZ_BLOB_TRIGGERS\",\"logicOperator\":\"and\",\"parentId\":\"\",\"querySql\":\"\",\"taskType\":\"DATAX\",\"dateIncrementConfig\":{\"logic\":\"and\",\"dateFormat\":\"yyyy-MM-dd\",\"column\":[]},\"idIncrementConfig\":{\"incrementColumn\":[],\"incrementStart\":\"\"},\"readerDatasource\":{\"datasourceType\":\"DM8\",\"datasourceConfig\":\"{\\\"username\\\":\\\"QDATA_OPEN\\\",\\\"password\\\":\\\"Iqo0tpF$~8R$Zn1yvhJA\\\",\\\"dbname\\\":\\\"QDATA_OPEN\\\"}\",\"ip\":\"110.42.38.62\",\"port\":40013,\"dbname\":\"QDATA\",\"datasource_id\":48,\"datasourceId\":48},\"datasourceId\":\"\",\"topic\":\"\",\"where\":\"\",\"readModeType\":\"1\",\"batchSize\":\"1024\",\"config\":\"\",\"columnsList\":[{\"colName\":\"SCHED_NAME\",\"dataType\":\"VARCHAR\"},{\"colName\":\"TRIGGER_NAME\",\"dataType\":\"VARCHAR\"},{\"colName\":\"TRIGGER_GROUP\",\"dataType\":\"VARCHAR\"},{\"colName\":\"BLOB_DATA\",\"dataType\":\"BLOB\"}],\"clmt\":\"0\"}', 'MEDIUM', 0, 1, 0, 0, -1, -1, NULL, NULL, '1', '0', 'qData', 1, '2025-05-24 10:44:04', 'qData', 1, '2025-05-24 10:44:04', NULL, '1');
INSERT INTO `dpp_etl_node` VALUES (1228, '1', 'SPARK', '转换组件', '141951104758624', 1, 84, '141883958809440', '{\"mainArgs\":{\"cleanRuleList\":[{\"columns\":\"SCHED_NAME\",\"cleanRules\":[{\"ruleId\":10,\"data\":\"{\\\"toggleCase\\\":{\\\"type\\\":\\\"1\\\"},\\\"nullReplace\\\":{\\\"value\\\":null},\\\"trimSpace\\\":{},\\\"regexValidate\\\":{\\\"pattern\\\":\\\"\\\",\\\"replacement\\\":\\\"\\\",\\\"resultType\\\":\\\"1\\\"},\\\"defaultValueFill\\\":{\\\"value\\\":\\\"\\\"},\\\"dictValueList\\\":[],\\\"tokens\\\":[{\\\"type\\\":\\\"field\\\",\\\"value\\\":\\\"SCHED_NAME\\\",\\\"valueType\\\":\\\"\\\"},{\\\"type\\\":\\\"operator\\\",\\\"value\\\":\\\"+\\\",\\\"valueType\\\":\\\"\\\"},{\\\"type\\\":\\\"field\\\",\\\"value\\\":\\\"SCHED_NAME\\\",\\\"valueType\\\":\\\"\\\"}]}\",\"ruleName\":\"首尾移除空格\"}]},{\"columns\":\"TRIGGER_NAME\",\"cleanRules\":[]},{\"columns\":\"TRIGGER_GROUP\",\"cleanRules\":[]},{\"columns\":\"BLOB_DATA\",\"cleanRules\":[]}]},\"tableFields\":[{\"columnName\":\"SCHED_NAME\",\"columnComment\":\"调度名称\",\"columnType\":\"VARCHAR\",\"columnLength\":120,\"columnScale\":0,\"nullableFlag\":\"0\",\"pkFlag\":\"1\",\"elementId\":[10],\"cleanRuleList\":[{\"ruleId\":10,\"ruleName\":\"首尾移除空格\",\"ruleConfig\":\"{\\\"toggleCase\\\":{\\\"type\\\":\\\"1\\\"},\\\"nullReplace\\\":{\\\"value\\\":null},\\\"trimSpace\\\":{},\\\"regexValidate\\\":{\\\"pattern\\\":\\\"\\\",\\\"replacement\\\":\\\"\\\",\\\"resultType\\\":\\\"1\\\"},\\\"defaultValueFill\\\":{\\\"value\\\":\\\"\\\"},\\\"dictValueList\\\":[],\\\"tokens\\\":[{\\\"type\\\":\\\"field\\\",\\\"value\\\":\\\"SCHED_NAME\\\",\\\"valueType\\\":\\\"\\\"},{\\\"type\\\":\\\"operator\\\",\\\"value\\\":\\\"+\\\",\\\"valueType\\\":\\\"\\\"},{\\\"type\\\":\\\"field\\\",\\\"value\\\":\\\"SCHED_NAME\\\",\\\"valueType\\\":\\\"\\\"}]}\"}]},{\"columnName\":\"TRIGGER_NAME\",\"columnComment\":\"qrtz_triggers表trigger_name的外键\",\"columnType\":\"VARCHAR\",\"columnLength\":200,\"columnScale\":0,\"nullableFlag\":\"0\",\"pkFlag\":\"1\"},{\"columnName\":\"TRIGGER_GROUP\",\"columnComment\":\"qrtz_triggers表trigger_group的外键\",\"columnType\":\"VARCHAR\",\"columnLength\":200,\"columnScale\":0,\"nullableFlag\":\"0\",\"pkFlag\":\"1\"},{\"columnName\":\"BLOB_DATA\",\"columnComment\":\"存放持久化Trigger对象\",\"columnType\":\"BLOB\",\"columnLength\":2147483647,\"columnScale\":0,\"nullableFlag\":\"1\",\"pkFlag\":\"0\"}],\"taskType\":\"SPARK\",\"typeName\":\"转换组件\",\"icon\":\"data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACQAAAAjCAYAAAD8BaggAAAAAXNSR0IArs4c6QAAAtpJREFUWEfN2E1PE1EUBuD3tKVQqbICTYwbjXHlChNDZcrcwYAgC0VpoivFuDAEDP4QQUNY6cKNiaBijHzU2JnSKdVEwh8gRlfE0IUmILVAOWbsNJamdVqdgXbZe+85T9+5c5sZQpV9qMo8qH7Q/Hzw5M4O3wdwFkCjQwkmAfoAuIaEiH7J77EroXA40FRb615mxiGHIIVlkx4Pn5Kk+LfcwC6QpgXvAjxqDq4BWHII1gzAb9Rm5gFFiY8XBamq9JAIg9lBuiFE7IkTIE0L3gL4URaEEUXR75UCjRPhzm8OcUiW45NOgFQ1GCLiZ9k+NC7LsQFHQJFI4ER7e+KT1Y/YE5CqSk+JcI2IX25sHLze3T2bLgXbE5CmSat5x8S7VMrfUwq1V6BLAJ4DcGf3BlSvd7snEHifKkxqT0BGU1WVLhLhFQCPiUp4vdvnC1G2gYxT3GrDMrPMDONcyaGWmN1tQkTXc2ttAWmaNAvgghWo2DgRVmVZP2wbaGKiz93Y+HX7XzC5NamUvy63yW1JKBqVhplxpTIUNwCuY0Q8Jcv6TdsSqgxhPduWhDStdYyI+phpBeAf1m3/zCDCC1nWR2xLaGamq9bnW/9ZCaJwbjJ5xBMKTWayx4MN/2UFJ3Gltjkh9C7bEjIKaZrsBzIJAKfNwsZdd9vlogUrXVtbbDl/ji0J5VBEmTAzAmaDLWZcVhR92grlCMgoGg531Hu9qTkArWaTDDP1Kkrsdbko2xLKNUwkWnzptHsaIGF+lxRCb9o3kHn56oDMGwDtAE0JEevdV5DRfHGxuWZtzXduc/PAx87Ot2WfTbZfsnKTKDWvbFA0GnzAzEPZQs49dahqaz8RPTbBo0Lowzn8357LjEuw+L9plFh/BkC9OTYohD5WFBSJtBx1uTyfAdQ4BCksm97a2jne0bGwUhSUvXukqwCMh7gGh1HfibhfluNT+X2q/+2Hw6lYlq+6hH4BXF3bMxTlLUAAAAAASUVORK5CYII=\",\"datasourceId\":\"\",\"where\":\"\",\"type\":3,\"batchSize\":\"1024\",\"config\":\"\",\"parentId\":\"\"}', 'MEDIUM', 0, 1, 0, 0, -1, -1, NULL, NULL, '1', '0', 'qData', 1, '2025-05-24 10:44:04', 'qData', 1, '2025-05-24 10:44:04', NULL, '31');
INSERT INTO `dpp_etl_node` VALUES (1229, '1', 'DATAX', '表输出组件', '141951127567200', 1, 84, '141883958809440', '{\"postSql\":\"\",\"tableFields\":[{\"columnName\":\"SCHED_NAME\",\"columnComment\":\"调度名称\",\"columnType\":\"VARCHAR\",\"columnLength\":120,\"columnScale\":0,\"nullableFlag\":\"0\",\"pkFlag\":\"1\",\"elementId\":[10],\"cleanRuleList\":[{\"ruleId\":10,\"ruleName\":\"首尾移除空格\",\"ruleConfig\":\"{\\\"toggleCase\\\":{\\\"type\\\":\\\"1\\\"},\\\"nullReplace\\\":{\\\"value\\\":null},\\\"trimSpace\\\":{},\\\"regexValidate\\\":{\\\"pattern\\\":\\\"\\\",\\\"replacement\\\":\\\"\\\",\\\"resultType\\\":\\\"1\\\"},\\\"defaultValueFill\\\":{\\\"value\\\":\\\"\\\"},\\\"dictValueList\\\":[],\\\"tokens\\\":[{\\\"type\\\":\\\"field\\\",\\\"value\\\":\\\"SCHED_NAME\\\",\\\"valueType\\\":\\\"\\\"},{\\\"type\\\":\\\"operator\\\",\\\"value\\\":\\\"+\\\",\\\"valueType\\\":\\\"\\\"},{\\\"type\\\":\\\"field\\\",\\\"value\\\":\\\"SCHED_NAME\\\",\\\"valueType\\\":\\\"\\\"}]}\"}],\"isChecked\":true},{\"columnName\":\"TRIGGER_NAME\",\"columnComment\":\"qrtz_triggers表trigger_name的外键\",\"columnType\":\"VARCHAR\",\"columnLength\":200,\"columnScale\":0,\"nullableFlag\":\"0\",\"pkFlag\":\"1\",\"isChecked\":true},{\"columnName\":\"TRIGGER_GROUP\",\"columnComment\":\"qrtz_triggers表trigger_group的外键\",\"columnType\":\"VARCHAR\",\"columnLength\":200,\"columnScale\":0,\"nullableFlag\":\"0\",\"pkFlag\":\"1\",\"isChecked\":true},{\"columnName\":\"BLOB_DATA\",\"columnComment\":\"存放持久化Trigger对象\",\"columnType\":\"BLOB\",\"columnLength\":2147483647,\"columnScale\":0,\"nullableFlag\":\"1\",\"pkFlag\":\"0\",\"isChecked\":true}],\"selectedColumn\":\"\",\"target_columns\":[\"SCHED_NAME\",\"ENTRY_ID\",\"TRIGGER_NAME\",\"TRIGGER_GROUP\"],\"target_table_name\":\"QRTZ_FIRED_TRIGGERS\",\"columns\":[\"SCHED_NAME\",\"TRIGGER_NAME\",\"TRIGGER_GROUP\",\"BLOB_DATA\"],\"typeName\":\"表输出组件\",\"icon\":\"data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACQAAAAeCAYAAABE4bxTAAAAAXNSR0IArs4c6QAAAW9JREFUWEftl71LA0EQxd+Lpb1WFkkpAZuQEyvtTbQQBCuxs7KxlNxdwD/Axk4rEW29gK1g45lC8KMNpFMsBEEtxHsiknC7Yi6CJFfclo9l58fMm9ldImWLcR4vmJ4dBp9XuTjrxDWA/MDRMIDcStjl+AFERcVatXk3CDCvUSpSIzc9gUTsUnwcBBAQjQFc7wkEalXKtTtAOaogYE3ilgW5DOAZwGlcJ1QjcRCJLUs/FLhiaXkA+38qWT0oT4n03flwMX5Y/cTZFPTkVi/3jMZolI/xwW1vIbyO637gtNxKWDD39lEy20MZkJVWpS5DAJtA9NoFFUdB5AHdmqbmBIB3QPeW2SchtkG9WPsdQKGhfZ9dykz9lZWsyzre8P57DqVuUmddlnTbp24wZkBJJUtdl9kvRgHjhOYAHhn3EDVD4U3glXlnaUngOYEHS98AuGNqfbwYMw8leSjLUFKGBvP9MaP8+kBL3Vd6GNmxY34CUyDPPWNEMvYAAAAASUVORK5CYII=\",\"target_asset_id\":\"QRTZ_FIRED_TRIGGERS\",\"type\":2,\"parentId\":\"\",\"taskType\":\"DATAX\",\"selectedColumns\":[],\"writerDatasource\":{\"datasourceType\":\"DM8\",\"datasourceConfig\":\"{\\\"username\\\":\\\"QDATA_OPEN\\\",\\\"password\\\":\\\"Iqo0tpF$~8R$Zn1yvhJA\\\",\\\"dbname\\\":\\\"QDATA_OPEN\\\"}\",\"ip\":\"110.42.38.62\",\"port\":40013,\"dbname\":\"QDATA\",\"target_asset_id\":48,\"datasourceId\":48},\"toColumnsList\":[{\"columnName\":\"SCHED_NAME\",\"columnComment\":\"调度名称\",\"columnType\":\"VARCHAR\",\"columnLength\":120,\"columnScale\":0,\"nullableFlag\":\"0\",\"pkFlag\":\"1\",\"isChecked\":true},{\"columnName\":\"ENTRY_ID\",\"columnComment\":\"调度器实例id\",\"columnType\":\"VARCHAR\",\"columnLength\":95,\"columnScale\":0,\"nullableFlag\":\"0\",\"pkFlag\":\"1\",\"isChecked\":true},{\"columnName\":\"TRIGGER_NAME\",\"columnComment\":\"qrtz_triggers表trigger_name的外键\",\"columnType\":\"VARCHAR\",\"columnLength\":200,\"columnScale\":0,\"nullableFlag\":\"0\",\"pkFlag\":\"0\",\"isChecked\":true},{\"columnName\":\"TRIGGER_GROUP\",\"columnComment\":\"qrtz_triggers表trigger_group的外键\",\"columnType\":\"VARCHAR\",\"columnLength\":200,\"columnScale\":0,\"nullableFlag\":\"0\",\"pkFlag\":\"0\",\"isChecked\":true},{\"columnName\":\"INSTANCE_NAME\",\"columnComment\":\"调度器实例名\",\"columnType\":\"VARCHAR\",\"columnLength\":200,\"columnScale\":0,\"nullableFlag\":\"0\",\"pkFlag\":\"0\",\"isChecked\":true},{\"columnName\":\"FIRED_TIME\",\"columnComment\":\"触发的时间\",\"columnType\":\"BIGINT\",\"columnLength\":8,\"columnScale\":0,\"nullableFlag\":\"0\",\"pkFlag\":\"0\",\"isChecked\":true},{\"columnName\":\"SCHED_TIME\",\"columnComment\":\"定时器制定的时间\",\"columnType\":\"BIGINT\",\"columnLength\":8,\"columnScale\":0,\"nullableFlag\":\"0\",\"pkFlag\":\"0\",\"isChecked\":true},{\"columnName\":\"PRIORITY\",\"columnComment\":\"优先级\",\"columnType\":\"INT\",\"columnLength\":4,\"columnScale\":0,\"nullableFlag\":\"0\",\"pkFlag\":\"0\",\"isChecked\":true},{\"columnName\":\"STATE\",\"columnComment\":\"状态\",\"columnType\":\"VARCHAR\",\"columnLength\":16,\"columnScale\":0,\"nullableFlag\":\"0\",\"pkFlag\":\"0\",\"isChecked\":true},{\"columnName\":\"JOB_NAME\",\"columnComment\":\"任务名称\",\"columnType\":\"VARCHAR\",\"columnLength\":200,\"columnScale\":0,\"nullableFlag\":\"1\",\"pkFlag\":\"0\",\"isChecked\":true},{\"columnName\":\"JOB_GROUP\",\"columnComment\":\"任务组名\",\"columnType\":\"VARCHAR\",\"columnLength\":200,\"columnScale\":0,\"nullableFlag\":\"1\",\"pkFlag\":\"0\",\"isChecked\":true},{\"columnName\":\"IS_NONCONCURRENT\",\"columnComment\":\"是否并发\",\"columnType\":\"VARCHAR\",\"columnLength\":1,\"columnScale\":0,\"nullableFlag\":\"1\",\"pkFlag\":\"0\",\"isChecked\":true},{\"columnName\":\"REQUESTS_RECOVERY\",\"columnComment\":\"是否接受恢复执行\",\"columnType\":\"VARCHAR\",\"columnLength\":1,\"columnScale\":0,\"nullableFlag\":\"1\",\"pkFlag\":\"0\",\"isChecked\":true}],\"target_datasource_id\":\"\",\"datasourceId\":\"\",\"where\":\"\",\"batchSize\":\"1024\",\"config\":\"\",\"writeModeType\":2,\"preSql\":\"\"}', 'MEDIUM', 0, 1, 0, 0, -1, -1, NULL, NULL, '1', '0', 'qData', 1, '2025-05-24 10:44:04', 'qData', 1, '2025-05-24 10:44:04', NULL, '91');

-- ----------------------------
-- Table structure for dpp_etl_node_instance
-- ----------------------------
DROP TABLE IF EXISTS `dpp_etl_node_instance`;
CREATE TABLE `dpp_etl_node_instance`  (
  `id` bigint(20) NOT NULL COMMENT 'ID',
  `task_type` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '1' COMMENT '任务类型;1：离线任务 2：实时任务 3：数据开发任务 4：	作业任务',
  `name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '节点实例名称',
  `node_type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '节点类型;DATAX、SPARK及SUB_PROCESS等',
  `node_id` bigint(20) NOT NULL COMMENT '节点id',
  `node_code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '节点编码',
  `node_version` int(11) NOT NULL COMMENT '节点版本',
  `task_instance_id` bigint(20) NOT NULL COMMENT '任务实例id',
  `task_instance_name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '任务实例名称',
  `project_id` bigint(20) NOT NULL COMMENT '项目id',
  `project_code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '项目编码',
  `submit_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '提交时间',
  `start_time` datetime NULL DEFAULT NULL COMMENT '开始时间',
  `end_time` datetime NULL DEFAULT NULL COMMENT '结束时间',
  `execute_path` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '执行路径',
  `log_path` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '日志路径',
  `parameters` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '节点参数',
  `priority` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '2' COMMENT '节点优先级;0 Highest,1 High,2 Medium,3 Low,4 Lowest',
  `retry_times` int(11) NULL DEFAULT 0 COMMENT '重试次数',
  `fretry_interval` int(11) NULL DEFAULT 1 COMMENT '重试间隔（分钟）',
  `delay_time` int(11) NULL DEFAULT 0 COMMENT '延迟执行时间（分钟）',
  `cpu_quota` int(11) NULL DEFAULT -1 COMMENT 'CPU配额',
  `memory_max` int(11) NULL DEFAULT -1 COMMENT '最大内存',
  `status` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '状态;0 提交成功,1 正在运行,3 暂停,5 停止,6 失败,7 成功,8 需要容错,9 Kill,12 延时执行,13 强制成功 17派发',
  `ds_id` bigint(20) NOT NULL COMMENT 'DolphinScheduler的id',
  `ds_task_instance_id` bigint(20) NOT NULL COMMENT 'DolphinScheduler的任务实例id',
  `valid_flag` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '1' COMMENT '是否有效;0：无效，1：有效',
  `del_flag` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '删除标志;1：已删除，0：未删除',
  `create_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `creator_id` bigint(20) NULL DEFAULT NULL COMMENT '创建人id',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `updater_id` bigint(20) NULL DEFAULT NULL COMMENT '更新人id',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `component_type` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '组件类型',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `dpp_etl_node_instance_task_instance_id_idx`(`task_instance_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '数据集成节点实例' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of dpp_etl_node_instance
-- ----------------------------

-- ----------------------------
-- Table structure for dpp_etl_node_log
-- ----------------------------
DROP TABLE IF EXISTS `dpp_etl_node_log`;
CREATE TABLE `dpp_etl_node_log`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `task_type` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '1' COMMENT '任务类型;1：离线任务 2：实时任务 3：数据开发任务 4：	作业任务',
  `type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '节点类型;DATAX、SPARK及SUB_PROCESS等',
  `name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '节点名称',
  `code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '节点编码',
  `version` bigint(20) NOT NULL DEFAULT 1 COMMENT '节点版本',
  `project_id` bigint(20) NOT NULL COMMENT '项目id',
  `project_code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '项目编码',
  `parameters` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '节点参数',
  `priority` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'MEDIUM' COMMENT '任务优先级;HIGHEST,HIGH,MEDIUM,LOW,LOWEST',
  `fail_retry_times` int(11) NULL DEFAULT 0 COMMENT '失败重试次数',
  `fail_retry_interval` int(11) NULL DEFAULT 1 COMMENT '失败重试间隔（分钟）',
  `timeout` int(11) NULL DEFAULT 0 COMMENT '超时时间',
  `delay_time` int(11) NULL DEFAULT 0 COMMENT '延迟执行时间（分钟）',
  `cpu_quota` int(11) NULL DEFAULT -1 COMMENT 'CPU配额',
  `memory_max` int(11) NULL DEFAULT -1 COMMENT '最大内存',
  `description` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '描述',
  `ds_id` bigint(20) NULL DEFAULT NULL COMMENT 'DolphinScheduler的id',
  `valid_flag` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '1' COMMENT '是否有效;0：无效，1：有效',
  `del_flag` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '删除标志;1：已删除，0：未删除',
  `create_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `creator_id` bigint(20) NULL DEFAULT NULL COMMENT '创建人id',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `updater_id` bigint(20) NULL DEFAULT NULL COMMENT '更新人id',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `component_type` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '1' COMMENT '组件类型',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `ID_90111825819100`(`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 284 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '数据集成节点-日志表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of dpp_etl_node_log
-- ----------------------------

-- ----------------------------
-- Table structure for dpp_etl_scheduler
-- ----------------------------
DROP TABLE IF EXISTS `dpp_etl_scheduler`;
CREATE TABLE `dpp_etl_scheduler`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `task_id` bigint(20) NOT NULL COMMENT '任务id',
  `task_code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '任务编码;DolphinScheduler任务的编码',
  `start_time` datetime NULL DEFAULT NULL COMMENT '开始时间',
  `end_time` datetime NULL DEFAULT NULL COMMENT '结束时间',
  `timezone_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'Asia/Shanghai' COMMENT '时区;直接默认 Asia/Shanghai',
  `cron_expression` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'cron表达式',
  `failure_strategy` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '1' COMMENT '失败策略;是否继续 0:否 1:是',
  `ds_id` bigint(20) NOT NULL COMMENT 'DolphinScheduler的id',
  `valid_flag` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '1' COMMENT '是否有效;0：无效，1：有效',
  `del_flag` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '删除标志;1：已删除，0：未删除',
  `create_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `creator_id` bigint(20) NULL DEFAULT NULL COMMENT '创建人id',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `updater_id` bigint(20) NULL DEFAULT NULL COMMENT '更新人id',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `status` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '-1' COMMENT '调度状态;-1:草稿，0:未上线，1:已上线',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `ID_90112286383000`(`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 284 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '数据集成调度信息' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of dpp_etl_scheduler
-- ----------------------------
INSERT INTO `dpp_etl_scheduler` VALUES (279, 244, '141950771024736', '2025-05-24 10:36:39', '2125-04-30 10:36:39', 'Asia/Shanghai', '0 0 0 1 * ?', '1', -1, '1', '0', 'qData', 1, '2025-05-24 10:36:39', 'qData', 1, '2025-05-24 10:36:39', 'NULL', '0');
INSERT INTO `dpp_etl_scheduler` VALUES (280, 245, '141950878896992', '2025-05-24 10:38:25', '2125-04-30 10:38:25', 'Asia/Shanghai', '0 0 0 1 * ?', '1', -1, '1', '0', 'qData', 1, '2025-05-24 10:38:25', 'qData', 1, '2025-05-24 10:38:25', 'NULL', '0');
INSERT INTO `dpp_etl_scheduler` VALUES (281, 246, '141951011072864', '2025-05-24 10:40:34', '2125-04-30 10:40:34', 'Asia/Shanghai', '0 0 0 1 * ?', '1', -1, '1', '0', 'qData', 1, '2025-05-24 10:40:34', 'qData', 1, '2025-05-24 10:40:34', 'NULL', '0');
INSERT INTO `dpp_etl_scheduler` VALUES (282, 247, '141951088294752', '2025-05-24 10:41:49', '2125-04-30 10:41:49', 'Asia/Shanghai', '0 0 0 1 * ?', '1', -1, '1', '0', 'qData', 1, '2025-05-24 10:41:49', 'qData', 1, '2025-05-24 10:41:49', 'NULL', '0');
INSERT INTO `dpp_etl_scheduler` VALUES (283, 248, '141951225860960', '2025-05-24 10:44:04', '2125-04-30 10:44:04', 'Asia/Shanghai', '0 0 0 1 * ?', '1', -1, '1', '0', 'qData', 1, '2025-05-24 10:44:04', 'qData', 1, '2025-05-24 10:44:04', 'NULL', '0');

-- ----------------------------
-- Table structure for dpp_etl_task
-- ----------------------------
DROP TABLE IF EXISTS `dpp_etl_task`;
CREATE TABLE `dpp_etl_task`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `cat_id` bigint(20) NULL DEFAULT NULL COMMENT '类目id',
  `cat_code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '类目编码',
  `type` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '任务类型;1：离线任务 2：实时任务 3：数据开发任务 4：	作业任务',
  `name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '任务名称',
  `code` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '任务编码;DolphinScheduler中的编码',
  `version` int(11) NOT NULL DEFAULT 1 COMMENT '任务版本',
  `project_id` bigint(20) NOT NULL COMMENT '项目id',
  `project_code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '项目编码',
  `person_charge` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '责任人',
  `contact_number` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '联系电话',
  `locations` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '节点坐标信息',
  `description` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '描述',
  `timeout` int(11) NOT NULL DEFAULT 0 COMMENT '超时时间',
  `extraction_count` int(11) NULL DEFAULT NULL COMMENT '抽取量',
  `write_count` int(11) NULL DEFAULT NULL COMMENT '写入量',
  `execution_type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '执行策略;任务的执行策略：PARALLEL (并行)、SERIAL_WAIT (串行等待)、SERIAL_DISCARD (串行抛弃)、SERIAL_PRIORITY (串行优先)',
  `status` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '-1' COMMENT '任务状态;-1:草稿，0:未上线，1:已上线',
  `ds_id` bigint(20) NOT NULL COMMENT 'DolphinScheduler的id',
  `valid_flag` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '1' COMMENT '是否有效;0：无效，1：有效',
  `del_flag` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '删除标志;1：已删除，0：未删除',
  `create_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `creator_id` bigint(20) NULL DEFAULT NULL COMMENT '创建人id',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `updater_id` bigint(20) NULL DEFAULT NULL COMMENT '更新人id',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `etl_node_code` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'ETL在调度器中的节点编码',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `ID_90112705237800`(`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 249 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '数据集成任务' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of dpp_etl_task
-- ----------------------------
INSERT INTO `dpp_etl_task` VALUES (244, 12, 'A01', '3', '示例-数据开发', '141950771024736', 1, 84, '141883958809440', '1', '19951942682', '[{\"taskCode\":141950767490912,\"x\":0,\"y\":0}]', '', 0, NULL, NULL, 'PARALLEL', '0', 807, '1', '0', 'qData', 1, '2025-05-24 10:36:40', 'qData', 1, '2025-05-24 10:36:40', '', NULL);
INSERT INTO `dpp_etl_task` VALUES (245, 12, 'A01', '3', '示例-数据开发带参数', '141950878896992', 1, 84, '141883958809440', '1', '19951942682', '[{\"taskCode\":141950875765600,\"x\":0,\"y\":0}]', '', 0, NULL, NULL, 'PARALLEL', '0', 808, '1', '0', 'qData', 1, '2025-05-24 10:38:25', 'qData', 1, '2025-05-24 10:38:25', '', NULL);
INSERT INTO `dpp_etl_task` VALUES (246, 12, 'A01', '3', '示例-数据开发插入', '141951011072864', 1, 84, '141883958809440', '1', '19951942682', '[{\"taskCode\":141951009362784,\"x\":0,\"y\":0}]', '', 0, NULL, NULL, 'PARALLEL', '0', 809, '1', '0', 'qData', 1, '2025-05-24 10:40:34', 'qData', 1, '2025-05-24 10:40:34', '', NULL);
INSERT INTO `dpp_etl_task` VALUES (247, 11, 'A01', '1', '示例-表到表不带转换组件', '141951088294752', 1, 84, '141883958809440', '1', '19951942682', '[{\"taskCode\":141951029426016,\"x\":280,\"y\":160},{\"taskCode\":141951044262752,\"x\":300,\"y\":350}]', NULL, 0, NULL, NULL, 'PARALLEL', '0', 810, '1', '0', 'qData', 1, '2025-05-24 10:41:50', 'qData', 1, '2025-05-24 10:41:50', '', NULL);
INSERT INTO `dpp_etl_task` VALUES (248, 11, 'A01', '1', '示例-表到表(带转换组件)', '141951225860960', 1, 84, '141883958809440', '1', '19951942682', '[{\"taskCode\":141951101460320,\"x\":270,\"y\":150},{\"taskCode\":141951104758624,\"x\":270,\"y\":280},{\"taskCode\":141951127567200,\"x\":320,\"y\":410}]', NULL, 0, NULL, NULL, 'PARALLEL', '0', 811, '1', '0', 'qData', 1, '2025-05-24 10:44:04', 'qData', 1, '2025-05-24 10:44:04', '', NULL);

-- ----------------------------
-- Table structure for dpp_etl_task_ext
-- ----------------------------
DROP TABLE IF EXISTS `dpp_etl_task_ext`;
CREATE TABLE `dpp_etl_task_ext`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `task_id` bigint(20) NOT NULL COMMENT '数据汇聚任务id',
  `etl_node_id` bigint(20) NULL DEFAULT NULL COMMENT '数据汇聚节点id;ds的数据',
  `etl_node_name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '数据汇聚节点名称;ds的数据',
  `etl_node_code` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '数据汇聚节点编码;ds的数据',
  `etl_node_version` int(11) NULL DEFAULT NULL COMMENT '数据汇聚节点版本;ds的数据',
  `etl_relation_id` bigint(20) NULL DEFAULT NULL COMMENT '数据汇聚节点关系id;ds的数据',
  `valid_flag` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '1' COMMENT '是否有效;0：无效，1：有效',
  `del_flag` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '删除标志;1：已删除，0：未删除',
  `create_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `creator_id` bigint(20) NULL DEFAULT NULL COMMENT '创建人id',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `updater_id` bigint(20) NULL DEFAULT NULL COMMENT '更新人id',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `etl_task_code` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '数据汇聚任务编码',
  `etl_task_version` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '数据汇聚任务版本',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `ID_90113147045600`(`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 58 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '数据集成任务-扩展数据' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of dpp_etl_task_ext
-- ----------------------------
INSERT INTO `dpp_etl_task_ext` VALUES (56, 247, 555, '示例-表到表不带转换组件-2025-05-24', '141951088336736', 1, 1636, '1', '0', 'qData', 1, '2025-05-24 10:41:50', 'qData', 1, '2025-05-24 10:41:50', NULL, '141951088480096', '1');
INSERT INTO `dpp_etl_task_ext` VALUES (57, 248, 556, '示例-表到表(带转换组件)-2025-05-24', '141951225899872', 1, 1637, '1', '0', 'qData', 1, '2025-05-24 10:44:04', 'qData', 1, '2025-05-24 10:44:04', NULL, '141951225942880', '1');

-- ----------------------------
-- Table structure for dpp_etl_task_instance
-- ----------------------------
DROP TABLE IF EXISTS `dpp_etl_task_instance`;
CREATE TABLE `dpp_etl_task_instance`  (
  `id` bigint(20) NOT NULL COMMENT 'ID',
  `cat_id` bigint(20) NOT NULL COMMENT '类目id',
  `cat_code` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '类目编码',
  `task_type` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '1' COMMENT '任务类型;1：离线任务 2：实时任务 3：数据开发任务 4：	作业任务',
  `name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '任务实例名称;任务名称_任务版本_时间',
  `task_id` bigint(20) NOT NULL COMMENT '任务id',
  `task_code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '任务编码;DolphinScheduler中的编码',
  `task_version` bigint(20) NOT NULL COMMENT '任务版本',
  `status_history` varchar(4000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '状态历史(json列表)',
  `person_charge` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '责任人',
  `contact_number` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '联系电话',
  `project_id` bigint(20) NOT NULL COMMENT '项目id',
  `project_code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '项目编码',
  `schedule_time` datetime NULL DEFAULT NULL COMMENT '调度时间',
  `start_time` datetime NULL DEFAULT NULL COMMENT '开始时间',
  `end_time` datetime NULL DEFAULT NULL COMMENT '结束时间',
  `run_times` int(11) NULL DEFAULT 1 COMMENT '运行次数',
  `command_type` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '运行类型;0 启动工作流,1 从当前节点开始执行,2 恢复被容错的工作流,3 恢复暂停流程,4 从失败节点开始执行,5 补数,6 调度,7 重跑,8 暂停,9 停止,10 恢复等待线程',
  `max_try_times` int(11) NULL DEFAULT 0 COMMENT '最大重试次数',
  `failure_strategy` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '失败策略;0 失败后结束，1 失败后继续',
  `sub_task_flag` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '是否是子任务;0:不是 ，1:是',
  `status` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '状态;0：提交成功 1：正在执行 2:准备暂停 3：暂停 4：准备停止 5：停止 6：失败 7：成功 12：延时执行  14：串行等待  15 ：准备锁定 16：锁定',
  `parent_task_instance_id` bigint(20) NULL DEFAULT NULL COMMENT '父任务实例id;只有为子任务时才有该值',
  `parent_node_instance_id` bigint(20) NULL DEFAULT NULL COMMENT '父任务节点实例id;只有为子任务时才有该值',
  `ds_id` bigint(20) NOT NULL COMMENT 'DolphinScheduler的id',
  `valid_flag` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '1' COMMENT '是否有效;0：无效，1：有效',
  `del_flag` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '删除标志;1：已删除，0：未删除',
  `create_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `creator_id` bigint(20) NULL DEFAULT NULL COMMENT '创建人id',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `updater_id` bigint(20) NULL DEFAULT NULL COMMENT '更新人id',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `dpp_etl_task_instance_parent_task_instance_id_idx`(`parent_task_instance_id`, `parent_node_instance_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '数据集成任务实例' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of dpp_etl_task_instance
-- ----------------------------

-- ----------------------------
-- Table structure for dpp_etl_task_log
-- ----------------------------
DROP TABLE IF EXISTS `dpp_etl_task_log`;
CREATE TABLE `dpp_etl_task_log`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `type` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '1' COMMENT '任务类型;1：离线任务 2：实时任务 3：数据开发任务 4：	作业任务',
  `name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '任务名称',
  `code` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '任务编码;DolphinScheduler中的编码',
  `version` int(11) NOT NULL DEFAULT 1 COMMENT '任务版本',
  `project_id` bigint(20) NOT NULL COMMENT '项目id',
  `project_code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '项目编码',
  `person_charge` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '责任人',
  `contact_number` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '联系电话',
  `locations` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '节点坐标信息',
  `description` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '描述',
  `timeout` int(11) NOT NULL DEFAULT 0 COMMENT '超时时间',
  `extraction_count` int(11) NULL DEFAULT NULL COMMENT '抽取量',
  `write_count` int(11) NULL DEFAULT NULL COMMENT '写入量',
  `execution_type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '任务的执行策略;：PARALLEL (并行)、SERIAL_WAIT (串行等待)、SERIAL_DISCARD (串行抛弃)、SERIAL_PRIORITY (串行优先)',
  `status` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '-1' COMMENT '任务状态;-1:草稿，0:未上线，1:已上线',
  `ds_id` bigint(20) NOT NULL COMMENT 'DolphinScheduler的id',
  `valid_flag` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '1' COMMENT '是否有效;0：无效，1：有效',
  `del_flag` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '删除标志;1：已删除，0：未删除',
  `create_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `creator_id` bigint(20) NULL DEFAULT NULL COMMENT '创建人id',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `updater_id` bigint(20) NULL DEFAULT NULL COMMENT '更新人id',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `ID_90114761686300`(`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 520 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '数据集成任务-日志表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of dpp_etl_task_log
-- ----------------------------
INSERT INTO `dpp_etl_task_log` VALUES (515, '3', '示例-数据开发', '141950771024736', 1, 84, '141883958809440', '1', NULL, '[{\"taskCode\":141950767490912,\"x\":0,\"y\":0}]', '', 0, NULL, NULL, 'PARALLEL', '0', 807, '1', '0', 'qData', 1, '2025-05-24 10:36:40', 'qData', 1, '2025-05-24 10:36:40', '');
INSERT INTO `dpp_etl_task_log` VALUES (516, '3', '示例-数据开发带参数', '141950878896992', 1, 84, '141883958809440', '1', NULL, '[{\"taskCode\":141950875765600,\"x\":0,\"y\":0}]', '', 0, NULL, NULL, 'PARALLEL', '0', 808, '1', '0', 'qData', 1, '2025-05-24 10:38:25', 'qData', 1, '2025-05-24 10:38:25', '');
INSERT INTO `dpp_etl_task_log` VALUES (517, '3', '示例-数据开发插入', '141951011072864', 1, 84, '141883958809440', '1', NULL, '[{\"taskCode\":141951009362784,\"x\":0,\"y\":0}]', '', 0, NULL, NULL, 'PARALLEL', '0', 809, '1', '0', 'qData', 1, '2025-05-24 10:40:34', 'qData', 1, '2025-05-24 10:40:34', '');
INSERT INTO `dpp_etl_task_log` VALUES (518, '1', '示例-表到表不带转换组件', '141951088294752', 1, 84, '141883958809440', '1', NULL, '[{\"taskCode\":141951029426016,\"x\":280,\"y\":160},{\"taskCode\":141951044262752,\"x\":300,\"y\":350}]', NULL, 0, NULL, NULL, 'PARALLEL', '0', 810, '1', '0', 'qData', 1, '2025-05-24 10:41:50', 'qData', 1, '2025-05-24 10:41:50', '');
INSERT INTO `dpp_etl_task_log` VALUES (519, '1', '示例-表到表(带转换组件)', '141951225860960', 1, 84, '141883958809440', '1', NULL, '[{\"taskCode\":141951101460320,\"x\":270,\"y\":150},{\"taskCode\":141951104758624,\"x\":270,\"y\":280},{\"taskCode\":141951127567200,\"x\":320,\"y\":410}]', NULL, 0, NULL, NULL, 'PARALLEL', '0', 811, '1', '0', 'qData', 1, '2025-05-24 10:44:04', 'qData', 1, '2025-05-24 10:44:04', '');

-- ----------------------------
-- Table structure for dpp_etl_task_node_rel
-- ----------------------------
DROP TABLE IF EXISTS `dpp_etl_task_node_rel`;
CREATE TABLE `dpp_etl_task_node_rel`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `project_id` bigint(20) NOT NULL COMMENT '项目id',
  `project_code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '项目编码',
  `task_id` bigint(20) NOT NULL COMMENT '任务id',
  `task_code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '任务编码',
  `task_version` bigint(20) NULL DEFAULT NULL COMMENT '任务版本',
  `pre_node_id` bigint(20) NULL DEFAULT NULL COMMENT '前节点id',
  `pre_node_code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '前节点编码',
  `pre_node_version` bigint(20) NULL DEFAULT NULL COMMENT '前节点版本',
  `post_node_id` bigint(20) NOT NULL COMMENT '后节点id',
  `post_node_code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '后节点编码',
  `post_node_version` bigint(20) NULL DEFAULT NULL COMMENT '后节点版本',
  `valid_flag` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '1' COMMENT '是否有效;0：无效，1：有效',
  `del_flag` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '删除标志;1：已删除，0：未删除',
  `create_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `creator_id` bigint(20) NULL DEFAULT NULL COMMENT '创建人id',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `updater_id` bigint(20) NULL DEFAULT NULL COMMENT '更新人id',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `ID_90115152591000`(`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1865 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '数据集成任务节点关系表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of dpp_etl_task_node_rel
-- ----------------------------
INSERT INTO `dpp_etl_task_node_rel` VALUES (1857, 84, '141883958809440', 244, '141950771024736', 1, -1, '0', 0, -1, '141950767490912', 1, '1', '0', 'qData', 1, '2025-05-24 10:36:40', 'qData', 1, '2025-05-24 10:36:40', NULL);
INSERT INTO `dpp_etl_task_node_rel` VALUES (1858, 84, '141883958809440', 245, '141950878896992', 1, -1, '0', 0, -1, '141950875765600', 1, '1', '0', 'qData', 1, '2025-05-24 10:38:26', 'qData', 1, '2025-05-24 10:38:26', NULL);
INSERT INTO `dpp_etl_task_node_rel` VALUES (1859, 84, '141883958809440', 246, '141951011072864', 1, -1, '0', 0, -1, '141951009362784', 1, '1', '0', 'qData', 1, '2025-05-24 10:40:34', 'qData', 1, '2025-05-24 10:40:34', NULL);
INSERT INTO `dpp_etl_task_node_rel` VALUES (1860, 84, '141883958809440', 247, '141951088294752', 1, -1, '0', 1, 1225, '141951029426016', 1, '1', '0', 'qData', 1, '2025-05-24 10:41:50', 'qData', 1, '2025-05-24 10:41:50', NULL);
INSERT INTO `dpp_etl_task_node_rel` VALUES (1861, 84, '141883958809440', 247, '141951088294752', 1, 1225, '141951029426016', 1, 1226, '141951044262752', 1, '1', '0', 'qData', 1, '2025-05-24 10:41:50', 'qData', 1, '2025-05-24 10:41:50', NULL);
INSERT INTO `dpp_etl_task_node_rel` VALUES (1862, 84, '141883958809440', 248, '141951225860960', 1, -1, '0', 1, 1227, '141951101460320', 1, '1', '0', 'qData', 1, '2025-05-24 10:44:05', 'qData', 1, '2025-05-24 10:44:05', NULL);
INSERT INTO `dpp_etl_task_node_rel` VALUES (1863, 84, '141883958809440', 248, '141951225860960', 1, 1227, '141951101460320', 1, 1228, '141951104758624', 1, '1', '0', 'qData', 1, '2025-05-24 10:44:05', 'qData', 1, '2025-05-24 10:44:05', NULL);
INSERT INTO `dpp_etl_task_node_rel` VALUES (1864, 84, '141883958809440', 248, '141951225860960', 1, 1228, '141951104758624', 1, 1229, '141951127567200', 1, '1', '0', 'qData', 1, '2025-05-24 10:44:05', 'qData', 1, '2025-05-24 10:44:05', NULL);

-- ----------------------------
-- Table structure for dpp_etl_task_node_rel_log
-- ----------------------------
DROP TABLE IF EXISTS `dpp_etl_task_node_rel_log`;
CREATE TABLE `dpp_etl_task_node_rel_log`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `project_id` bigint(20) NOT NULL COMMENT '项目id',
  `project_code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '项目编码',
  `task_id` bigint(20) NOT NULL COMMENT '任务id',
  `task_code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '任务编码',
  `task_version` bigint(20) NULL DEFAULT NULL COMMENT '任务版本',
  `pre_node_id` bigint(20) NULL DEFAULT NULL COMMENT '前节点id',
  `pre_node_code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '前节点编码',
  `pre_node_version` bigint(20) NULL DEFAULT NULL COMMENT '前节点版本',
  `post_node_id` bigint(20) NOT NULL COMMENT '后节点id',
  `post_node_code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '后节点编码',
  `post_node_version` bigint(20) NULL DEFAULT NULL COMMENT '后节点版本',
  `valid_flag` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '1' COMMENT '是否有效;0：无效，1：有效',
  `del_flag` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '删除标志;1：已删除，0：未删除',
  `create_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `creator_id` bigint(20) NULL DEFAULT NULL COMMENT '创建人id',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `updater_id` bigint(20) NULL DEFAULT NULL COMMENT '更新人id',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `ID_90115720298800`(`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1102 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '数据集成任务节点关系表-日志' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of dpp_etl_task_node_rel_log
-- ----------------------------
INSERT INTO `dpp_etl_task_node_rel_log` VALUES (1094, 84, '141883958809440', 515, '141950771024736', 1, -1, '0', 0, -1, '141950767490912', 1, '1', '0', 'qData', 1, '2025-05-24 10:36:40', 'qData', 1, '2025-05-24 10:36:40', NULL);
INSERT INTO `dpp_etl_task_node_rel_log` VALUES (1095, 84, '141883958809440', 516, '141950878896992', 1, -1, '0', 0, -1, '141950875765600', 1, '1', '0', 'qData', 1, '2025-05-24 10:38:26', 'qData', 1, '2025-05-24 10:38:26', NULL);
INSERT INTO `dpp_etl_task_node_rel_log` VALUES (1096, 84, '141883958809440', 517, '141951011072864', 1, -1, '0', 0, -1, '141951009362784', 1, '1', '0', 'qData', 1, '2025-05-24 10:40:34', 'qData', 1, '2025-05-24 10:40:34', NULL);
INSERT INTO `dpp_etl_task_node_rel_log` VALUES (1097, 84, '141883958809440', 247, '141951088294752', 1, -1, '0', 1, 1225, '141951029426016', 1, '1', '0', 'qData', 1, '2025-05-24 10:41:50', 'qData', 1, '2025-05-24 10:41:50', NULL);
INSERT INTO `dpp_etl_task_node_rel_log` VALUES (1098, 84, '141883958809440', 247, '141951088294752', 1, 1225, '141951029426016', 1, 1226, '141951044262752', 1, '1', '0', 'qData', 1, '2025-05-24 10:41:50', 'qData', 1, '2025-05-24 10:41:50', NULL);
INSERT INTO `dpp_etl_task_node_rel_log` VALUES (1099, 84, '141883958809440', 248, '141951225860960', 1, -1, '0', 1, 1227, '141951101460320', 1, '1', '0', 'qData', 1, '2025-05-24 10:44:05', 'qData', 1, '2025-05-24 10:44:05', NULL);
INSERT INTO `dpp_etl_task_node_rel_log` VALUES (1100, 84, '141883958809440', 248, '141951225860960', 1, 1227, '141951101460320', 1, 1228, '141951104758624', 1, '1', '0', 'qData', 1, '2025-05-24 10:44:05', 'qData', 1, '2025-05-24 10:44:05', NULL);
INSERT INTO `dpp_etl_task_node_rel_log` VALUES (1101, 84, '141883958809440', 248, '141951225860960', 1, 1228, '141951104758624', 1, 1229, '141951127567200', 1, '1', '0', 'qData', 1, '2025-05-24 10:44:05', 'qData', 1, '2025-05-24 10:44:05', NULL);

-- ----------------------------
-- Table structure for ds_api
-- ----------------------------
DROP TABLE IF EXISTS `ds_api`;
CREATE TABLE `ds_api`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `cat_id` bigint(20) NOT NULL COMMENT '类目id',
  `cat_code` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '类目编码',
  `name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'API服务名称',
  `api_version` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'API版本',
  `api_url` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'API路径',
  `req_method` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '请求方式;1：get，2：post',
  `api_service_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '服务提供类型;1：单表向导式，2：SQL脚本式，3：三方转发',
  `res_data_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '返回结果类型;1：详情，2：列表，3：分页',
  `deny_ip` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT 'IP黑名单多个，隔开',
  `config_json` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '执行配置JSON',
  `limit_json` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '限流配置JSON',
  `req_params` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '请求参数',
  `res_params` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '返回参数',
  `description` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '描述',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '状态;0：不启用，1启用',
  `valid_flag` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '1' COMMENT '是否有效;0：无效，1：有效',
  `del_flag` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '删除标志;1：已删除，0：未删除',
  `create_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `creator_id` bigint(20) NULL DEFAULT NULL COMMENT '创建人id',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `updater_id` bigint(20) NULL DEFAULT NULL COMMENT '更新人id',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `transmit_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '转发类型;1:API 2:地理空间数据',
  `api_id` bigint(20) NULL DEFAULT NULL COMMENT 'API id',
  `header_json` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT 'Header配置json',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `ID_90116106443200`(`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'API服务' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ds_api
-- ----------------------------
INSERT INTO `ds_api` VALUES (1, 64, 'A01', '示例-表向导', 'v0.0.1', '/user/list', '2', '1', '3', NULL, '{\"apiServiceType\":\"1\",\"sourceId\":48,\"reqParams\":[{\"paramName\":\"ID\",\"paramComment\":\"ID\",\"nullable\":\"0\",\"creatorId\":1,\"createBy\":\"千桐科技\",\"createTime\":\"2025-05-23 17:42:18\",\"updateBy\":\"千桐科技\",\"updateTime\":\"2025-05-23 17:42:18\",\"remark\":null,\"id\":706,\"assetId\":173,\"columnName\":\"ID\",\"columnComment\":\"ID\",\"columnType\":\"INT\",\"columnLength\":4,\"columnScale\":0,\"nullableFlag\":\"0\",\"pkFlag\":\"1\",\"defaultValue\":null,\"dataElemCodeFlag\":\"0\",\"dataElemCodeId\":null,\"sensitiveLevelId\":null,\"relDataElmeFlag\":\"0\",\"relCleanFlag\":\"0\",\"relAuditFlag\":\"0\",\"description\":null,\"validFlag\":true,\"delFlag\":false,\"elementId\":[],\"relDataElmeName\":null,\"sensitiveLevelName\":null,\"dataElemCodeName\":null,\"cleanRuleList\":null,\"updatorId\":1,\"paramValue\":\"1\"},{\"paramName\":\"SENDER_ID\",\"paramComment\":\"发送人\",\"nullable\":\"1\",\"creatorId\":1,\"createBy\":\"千桐科技\",\"createTime\":\"2025-05-23 17:42:18\",\"updateBy\":\"千桐科技\",\"updateTime\":\"2025-05-23 17:42:18\",\"remark\":null,\"id\":707,\"assetId\":173,\"columnName\":\"SENDER_ID\",\"columnComment\":\"发送人\",\"columnType\":\"BIGINT\",\"columnLength\":8,\"columnScale\":0,\"nullableFlag\":\"1\",\"pkFlag\":\"0\",\"defaultValue\":null,\"dataElemCodeFlag\":\"0\",\"dataElemCodeId\":null,\"sensitiveLevelId\":null,\"relDataElmeFlag\":\"0\",\"relCleanFlag\":\"0\",\"relAuditFlag\":\"0\",\"description\":null,\"validFlag\":true,\"delFlag\":false,\"elementId\":[],\"relDataElmeName\":null,\"sensitiveLevelName\":null,\"dataElemCodeName\":null,\"cleanRuleList\":null,\"updatorId\":1},{\"paramName\":\"RECEIVER_ID\",\"paramComment\":\"接收人\",\"nullable\":\"1\",\"creatorId\":1,\"createBy\":\"千桐科技\",\"createTime\":\"2025-05-23 17:42:18\",\"updateBy\":\"千桐科技\",\"updateTime\":\"2025-05-23 17:42:18\",\"remark\":null,\"id\":708,\"assetId\":173,\"columnName\":\"RECEIVER_ID\",\"columnComment\":\"接收人\",\"columnType\":\"BIGINT\",\"columnLength\":8,\"columnScale\":0,\"nullableFlag\":\"1\",\"pkFlag\":\"0\",\"defaultValue\":null,\"dataElemCodeFlag\":\"0\",\"dataElemCodeId\":null,\"sensitiveLevelId\":null,\"relDataElmeFlag\":\"0\",\"relCleanFlag\":\"0\",\"relAuditFlag\":\"0\",\"description\":null,\"validFlag\":true,\"delFlag\":false,\"elementId\":[],\"relDataElmeName\":null,\"sensitiveLevelName\":null,\"dataElemCodeName\":null,\"cleanRuleList\":null,\"updatorId\":1},{\"paramName\":\"TITLE\",\"paramComment\":\"消息标题\",\"nullable\":\"1\",\"creatorId\":1,\"createBy\":\"千桐科技\",\"createTime\":\"2025-05-23 17:42:18\",\"updateBy\":\"千桐科技\",\"updateTime\":\"2025-05-23 17:42:18\",\"remark\":null,\"id\":709,\"assetId\":173,\"columnName\":\"TITLE\",\"columnComment\":\"消息标题\",\"columnType\":\"VARCHAR\",\"columnLength\":128,\"columnScale\":0,\"nullableFlag\":\"0\",\"pkFlag\":\"0\",\"defaultValue\":null,\"dataElemCodeFlag\":\"0\",\"dataElemCodeId\":null,\"sensitiveLevelId\":null,\"relDataElmeFlag\":\"0\",\"relCleanFlag\":\"0\",\"relAuditFlag\":\"0\",\"description\":null,\"validFlag\":true,\"delFlag\":false,\"elementId\":[],\"relDataElmeName\":null,\"sensitiveLevelName\":null,\"dataElemCodeName\":null,\"cleanRuleList\":null,\"updatorId\":1},{\"paramName\":\"CONTENT\",\"paramComment\":\"消息模板内容\",\"nullable\":\"1\",\"creatorId\":1,\"createBy\":\"千桐科技\",\"createTime\":\"2025-05-23 17:42:18\",\"updateBy\":\"千桐科技\",\"updateTime\":\"2025-05-23 17:42:18\",\"remark\":null,\"id\":710,\"assetId\":173,\"columnName\":\"CONTENT\",\"columnComment\":\"消息模板内容\",\"columnType\":\"VARCHAR\",\"columnLength\":3072,\"columnScale\":0,\"nullableFlag\":\"0\",\"pkFlag\":\"0\",\"defaultValue\":null,\"dataElemCodeFlag\":\"0\",\"dataElemCodeId\":null,\"sensitiveLevelId\":null,\"relDataElmeFlag\":\"0\",\"relCleanFlag\":\"0\",\"relAuditFlag\":\"0\",\"description\":null,\"validFlag\":true,\"delFlag\":false,\"elementId\":[],\"relDataElmeName\":null,\"sensitiveLevelName\":null,\"dataElemCodeName\":null,\"cleanRuleList\":null,\"updatorId\":1},{\"paramName\":\"CATEGORY\",\"paramComment\":\"消息类别\",\"nullable\":\"1\",\"creatorId\":1,\"createBy\":\"千桐科技\",\"createTime\":\"2025-05-23 17:42:18\",\"updateBy\":\"千桐科技\",\"updateTime\":\"2025-05-23 17:42:18\",\"remark\":null,\"id\":711,\"assetId\":173,\"columnName\":\"CATEGORY\",\"columnComment\":\"消息类别\",\"columnType\":\"INT\",\"columnLength\":4,\"columnScale\":0,\"nullableFlag\":\"0\",\"pkFlag\":\"0\",\"defaultValue\":null,\"dataElemCodeFlag\":\"0\",\"dataElemCodeId\":null,\"sensitiveLevelId\":null,\"relDataElmeFlag\":\"0\",\"relCleanFlag\":\"0\",\"relAuditFlag\":\"0\",\"description\":null,\"validFlag\":true,\"delFlag\":false,\"elementId\":[],\"relDataElmeName\":null,\"sensitiveLevelName\":null,\"dataElemCodeName\":null,\"cleanRuleList\":null,\"updatorId\":1},{\"paramName\":\"MSG_LEVEL\",\"paramComment\":\"消息等级\",\"nullable\":\"1\",\"creatorId\":1,\"createBy\":\"千桐科技\",\"createTime\":\"2025-05-23 17:42:18\",\"updateBy\":\"千桐科技\",\"updateTime\":\"2025-05-23 17:42:18\",\"remark\":null,\"id\":712,\"assetId\":173,\"columnName\":\"MSG_LEVEL\",\"columnComment\":\"消息等级\",\"columnType\":\"INT\",\"columnLength\":4,\"columnScale\":0,\"nullableFlag\":\"0\",\"pkFlag\":\"0\",\"defaultValue\":\"0\",\"dataElemCodeFlag\":\"0\",\"dataElemCodeId\":null,\"sensitiveLevelId\":null,\"relDataElmeFlag\":\"0\",\"relCleanFlag\":\"0\",\"relAuditFlag\":\"0\",\"description\":null,\"validFlag\":true,\"delFlag\":false,\"elementId\":[],\"relDataElmeName\":null,\"sensitiveLevelName\":null,\"dataElemCodeName\":null,\"cleanRuleList\":null,\"updatorId\":1},{\"paramName\":\"MODULE\",\"paramComment\":\"消息模块\",\"nullable\":\"1\",\"creatorId\":1,\"createBy\":\"千桐科技\",\"createTime\":\"2025-05-23 17:42:18\",\"updateBy\":\"千桐科技\",\"updateTime\":\"2025-05-23 17:42:18\",\"remark\":null,\"id\":713,\"assetId\":173,\"columnName\":\"MODULE\",\"columnComment\":\"消息模块\",\"columnType\":\"INT\",\"columnLength\":4,\"columnScale\":0,\"nullableFlag\":\"0\",\"pkFlag\":\"0\",\"defaultValue\":\"0\",\"dataElemCodeFlag\":\"0\",\"dataElemCodeId\":null,\"sensitiveLevelId\":null,\"relDataElmeFlag\":\"0\",\"relCleanFlag\":\"0\",\"relAuditFlag\":\"0\",\"description\":null,\"validFlag\":true,\"delFlag\":false,\"elementId\":[],\"relDataElmeName\":null,\"sensitiveLevelName\":null,\"dataElemCodeName\":null,\"cleanRuleList\":null,\"updatorId\":1},{\"paramName\":\"ENTITY_TYPE\",\"paramComment\":\"实体类型\",\"nullable\":\"1\",\"creatorId\":1,\"createBy\":\"千桐科技\",\"createTime\":\"2025-05-23 17:42:18\",\"updateBy\":\"千桐科技\",\"updateTime\":\"2025-05-23 17:42:18\",\"remark\":null,\"id\":714,\"assetId\":173,\"columnName\":\"ENTITY_TYPE\",\"columnComment\":\"实体类型\",\"columnType\":\"INT\",\"columnLength\":4,\"columnScale\":0,\"nullableFlag\":\"1\",\"pkFlag\":\"0\",\"defaultValue\":null,\"dataElemCodeFlag\":\"0\",\"dataElemCodeId\":null,\"sensitiveLevelId\":null,\"relDataElmeFlag\":\"0\",\"relCleanFlag\":\"0\",\"relAuditFlag\":\"0\",\"description\":null,\"validFlag\":true,\"delFlag\":false,\"elementId\":[],\"relDataElmeName\":null,\"sensitiveLevelName\":null,\"dataElemCodeName\":null,\"cleanRuleList\":null,\"updatorId\":1},{\"paramName\":\"ENTITY_ID\",\"paramComment\":\"实体id\",\"nullable\":\"1\",\"creatorId\":1,\"createBy\":\"千桐科技\",\"createTime\":\"2025-05-23 17:42:18\",\"updateBy\":\"千桐科技\",\"updateTime\":\"2025-05-23 17:42:18\",\"remark\":null,\"id\":715,\"assetId\":173,\"columnName\":\"ENTITY_ID\",\"columnComment\":\"实体id\",\"columnType\":\"BIGINT\",\"columnLength\":8,\"columnScale\":0,\"nullableFlag\":\"1\",\"pkFlag\":\"0\",\"defaultValue\":null,\"dataElemCodeFlag\":\"0\",\"dataElemCodeId\":null,\"sensitiveLevelId\":null,\"relDataElmeFlag\":\"0\",\"relCleanFlag\":\"0\",\"relAuditFlag\":\"0\",\"description\":null,\"validFlag\":true,\"delFlag\":false,\"elementId\":[],\"relDataElmeName\":null,\"sensitiveLevelName\":null,\"dataElemCodeName\":null,\"cleanRuleList\":null,\"updatorId\":1},{\"paramName\":\"ENTITY_URL\",\"paramComment\":\"消息链接\",\"nullable\":\"1\",\"creatorId\":1,\"createBy\":\"千桐科技\",\"createTime\":\"2025-05-23 17:42:18\",\"updateBy\":\"千桐科技\",\"updateTime\":\"2025-05-23 17:42:18\",\"remark\":null,\"id\":716,\"assetId\":173,\"columnName\":\"ENTITY_URL\",\"columnComment\":\"消息链接\",\"columnType\":\"VARCHAR\",\"columnLength\":256,\"columnScale\":0,\"nullableFlag\":\"1\",\"pkFlag\":\"0\",\"defaultValue\":null,\"dataElemCodeFlag\":\"0\",\"dataElemCodeId\":null,\"sensitiveLevelId\":null,\"relDataElmeFlag\":\"0\",\"relCleanFlag\":\"0\",\"relAuditFlag\":\"0\",\"description\":null,\"validFlag\":true,\"delFlag\":false,\"elementId\":[],\"relDataElmeName\":null,\"sensitiveLevelName\":null,\"dataElemCodeName\":null,\"cleanRuleList\":null,\"updatorId\":1},{\"paramName\":\"HAS_READ\",\"paramComment\":\"是否已读\",\"nullable\":\"1\",\"creatorId\":1,\"createBy\":\"千桐科技\",\"createTime\":\"2025-05-23 17:42:18\",\"updateBy\":\"千桐科技\",\"updateTime\":\"2025-05-23 17:42:18\",\"remark\":null,\"id\":717,\"assetId\":173,\"columnName\":\"HAS_READ\",\"columnComment\":\"是否已读\",\"columnType\":\"TINYINT\",\"columnLength\":1,\"columnScale\":0,\"nullableFlag\":\"1\",\"pkFlag\":\"0\",\"defaultValue\":\"0\",\"dataElemCodeFlag\":\"0\",\"dataElemCodeId\":null,\"sensitiveLevelId\":null,\"relDataElmeFlag\":\"0\",\"relCleanFlag\":\"0\",\"relAuditFlag\":\"0\",\"description\":null,\"validFlag\":true,\"delFlag\":false,\"elementId\":[],\"relDataElmeName\":null,\"sensitiveLevelName\":null,\"dataElemCodeName\":null,\"cleanRuleList\":null,\"updatorId\":1},{\"paramName\":\"HAS_RETRACTION\",\"paramComment\":\"是否撤回\",\"nullable\":\"1\",\"creatorId\":1,\"createBy\":\"千桐科技\",\"createTime\":\"2025-05-23 17:42:18\",\"updateBy\":\"千桐科技\",\"updateTime\":\"2025-05-23 17:42:18\",\"remark\":null,\"id\":718,\"assetId\":173,\"columnName\":\"HAS_RETRACTION\",\"columnComment\":\"是否撤回\",\"columnType\":\"TINYINT\",\"columnLength\":1,\"columnScale\":0,\"nullableFlag\":\"1\",\"pkFlag\":\"0\",\"defaultValue\":\"0\",\"dataElemCodeFlag\":\"0\",\"dataElemCodeId\":null,\"sensitiveLevelId\":null,\"relDataElmeFlag\":\"0\",\"relCleanFlag\":\"0\",\"relAuditFlag\":\"0\",\"description\":null,\"validFlag\":true,\"delFlag\":false,\"elementId\":[],\"relDataElmeName\":null,\"sensitiveLevelName\":null,\"dataElemCodeName\":null,\"cleanRuleList\":null,\"updatorId\":1},{\"paramName\":\"VALID_FLAG\",\"paramComment\":\"是否有效;0：无效，1：有效\",\"nullable\":\"1\",\"creatorId\":1,\"createBy\":\"千桐科技\",\"createTime\":\"2025-05-23 17:42:19\",\"updateBy\":\"千桐科技\",\"updateTime\":\"2025-05-23 17:42:19\",\"remark\":null,\"id\":719,\"assetId\":173,\"columnName\":\"VALID_FLAG\",\"columnComment\":\"是否有效;0：无效，1：有效\",\"columnType\":\"TINYINT\",\"columnLength\":1,\"columnScale\":0,\"nullableFlag\":\"0\",\"pkFlag\":\"0\",\"defaultValue\":\"1\",\"dataElemCodeFlag\":\"0\",\"dataElemCodeId\":null,\"sensitiveLevelId\":null,\"relDataElmeFlag\":\"0\",\"relCleanFlag\":\"0\",\"relAuditFlag\":\"0\",\"description\":null,\"validFlag\":true,\"delFlag\":false,\"elementId\":[],\"relDataElmeName\":null,\"sensitiveLevelName\":null,\"dataElemCodeName\":null,\"cleanRuleList\":null,\"updatorId\":1},{\"paramName\":\"DEL_FLAG\",\"paramComment\":\"删除标志;1：已删除，0：未删除\",\"nullable\":\"1\",\"creatorId\":1,\"createBy\":\"千桐科技\",\"createTime\":\"2025-05-23 17:42:19\",\"updateBy\":\"千桐科技\",\"updateTime\":\"2025-05-23 17:42:19\",\"remark\":null,\"id\":720,\"assetId\":173,\"columnName\":\"DEL_FLAG\",\"columnComment\":\"删除标志;1：已删除，0：未删除\",\"columnType\":\"TINYINT\",\"columnLength\":1,\"columnScale\":0,\"nullableFlag\":\"0\",\"pkFlag\":\"0\",\"defaultValue\":\"0\",\"dataElemCodeFlag\":\"0\",\"dataElemCodeId\":null,\"sensitiveLevelId\":null,\"relDataElmeFlag\":\"0\",\"relCleanFlag\":\"0\",\"relAuditFlag\":\"0\",\"description\":null,\"validFlag\":true,\"delFlag\":false,\"elementId\":[],\"relDataElmeName\":null,\"sensitiveLevelName\":null,\"dataElemCodeName\":null,\"cleanRuleList\":null,\"updatorId\":1},{\"paramName\":\"CREATE_BY\",\"paramComment\":\"创建人\",\"nullable\":\"1\",\"creatorId\":1,\"createBy\":\"千桐科技\",\"createTime\":\"2025-05-23 17:42:19\",\"updateBy\":\"千桐科技\",\"updateTime\":\"2025-05-23 17:42:19\",\"remark\":null,\"id\":721,\"assetId\":173,\"columnName\":\"CREATE_BY\",\"columnComment\":\"创建人\",\"columnType\":\"VARCHAR\",\"columnLength\":32,\"columnScale\":0,\"nullableFlag\":\"1\",\"pkFlag\":\"0\",\"defaultValue\":null,\"dataElemCodeFlag\":\"0\",\"dataElemCodeId\":null,\"sensitiveLevelId\":null,\"relDataElmeFlag\":\"0\",\"relCleanFlag\":\"0\",\"relAuditFlag\":\"0\",\"description\":null,\"validFlag\":true,\"delFlag\":false,\"elementId\":[],\"relDataElmeName\":null,\"sensitiveLevelName\":null,\"dataElemCodeName\":null,\"cleanRuleList\":null,\"updatorId\":1},{\"paramName\":\"CREATOR_ID\",\"paramComment\":\"创建人id\",\"nullable\":\"1\",\"creatorId\":1,\"createBy\":\"千桐科技\",\"createTime\":\"2025-05-23 17:42:19\",\"updateBy\":\"千桐科技\",\"updateTime\":\"2025-05-23 17:42:19\",\"remark\":null,\"id\":722,\"assetId\":173,\"columnName\":\"CREATOR_ID\",\"columnComment\":\"创建人id\",\"columnType\":\"BIGINT\",\"columnLength\":8,\"columnScale\":0,\"nullableFlag\":\"1\",\"pkFlag\":\"0\",\"defaultValue\":null,\"dataElemCodeFlag\":\"0\",\"dataElemCodeId\":null,\"sensitiveLevelId\":null,\"relDataElmeFlag\":\"0\",\"relCleanFlag\":\"0\",\"relAuditFlag\":\"0\",\"description\":null,\"validFlag\":true,\"delFlag\":false,\"elementId\":[],\"relDataElmeName\":null,\"sensitiveLevelName\":null,\"dataElemCodeName\":null,\"cleanRuleList\":null,\"updatorId\":1},{\"paramName\":\"CREATE_TIME\",\"paramComment\":\"创建时间\",\"nullable\":\"1\",\"creatorId\":1,\"createBy\":\"千桐科技\",\"createTime\":\"2025-05-23 17:42:19\",\"updateBy\":\"千桐科技\",\"updateTime\":\"2025-05-23 17:42:19\",\"remark\":null,\"id\":723,\"assetId\":173,\"columnName\":\"CREATE_TIME\",\"columnComment\":\"创建时间\",\"columnType\":\"TIMESTAMP\",\"columnLength\":8,\"columnScale\":0,\"nullableFlag\":\"0\",\"pkFlag\":\"0\",\"defaultValue\":\"CURRENT_TIMESTAMP()\",\"dataElemCodeFlag\":\"0\",\"dataElemCodeId\":null,\"sensitiveLevelId\":null,\"relDataElmeFlag\":\"0\",\"relCleanFlag\":\"0\",\"relAuditFlag\":\"0\",\"description\":null,\"validFlag\":true,\"delFlag\":false,\"elementId\":[],\"relDataElmeName\":null,\"sensitiveLevelName\":null,\"dataElemCodeName\":null,\"cleanRuleList\":null,\"updatorId\":1},{\"paramName\":\"UPDATE_BY\",\"paramComment\":\"更新人\",\"nullable\":\"1\",\"creatorId\":1,\"createBy\":\"千桐科技\",\"createTime\":\"2025-05-23 17:42:19\",\"updateBy\":\"千桐科技\",\"updateTime\":\"2025-05-23 17:42:19\",\"remark\":null,\"id\":724,\"assetId\":173,\"columnName\":\"UPDATE_BY\",\"columnComment\":\"更新人\",\"columnType\":\"VARCHAR\",\"columnLength\":32,\"columnScale\":0,\"nullableFlag\":\"1\",\"pkFlag\":\"0\",\"defaultValue\":null,\"dataElemCodeFlag\":\"0\",\"dataElemCodeId\":null,\"sensitiveLevelId\":null,\"relDataElmeFlag\":\"0\",\"relCleanFlag\":\"0\",\"relAuditFlag\":\"0\",\"description\":null,\"validFlag\":true,\"delFlag\":false,\"elementId\":[],\"relDataElmeName\":null,\"sensitiveLevelName\":null,\"dataElemCodeName\":null,\"cleanRuleList\":null,\"updatorId\":1},{\"paramName\":\"UPDATE_TIME\",\"paramComment\":\"更新时间\",\"nullable\":\"1\",\"creatorId\":1,\"createBy\":\"千桐科技\",\"createTime\":\"2025-05-23 17:42:19\",\"updateBy\":\"千桐科技\",\"updateTime\":\"2025-05-23 17:42:19\",\"remark\":null,\"id\":725,\"assetId\":173,\"columnName\":\"UPDATE_TIME\",\"columnComment\":\"更新时间\",\"columnType\":\"TIMESTAMP\",\"columnLength\":8,\"columnScale\":0,\"nullableFlag\":\"0\",\"pkFlag\":\"0\",\"defaultValue\":\"CURRENT_TIMESTAMP()\",\"dataElemCodeFlag\":\"0\",\"dataElemCodeId\":null,\"sensitiveLevelId\":null,\"relDataElmeFlag\":\"0\",\"relCleanFlag\":\"0\",\"relAuditFlag\":\"0\",\"description\":null,\"validFlag\":true,\"delFlag\":false,\"elementId\":[],\"relDataElmeName\":null,\"sensitiveLevelName\":null,\"dataElemCodeName\":null,\"cleanRuleList\":null,\"updatorId\":1},{\"paramName\":\"UPDATER_ID\",\"paramComment\":\"更新人id\",\"nullable\":\"1\",\"creatorId\":1,\"createBy\":\"千桐科技\",\"createTime\":\"2025-05-23 17:42:19\",\"updateBy\":\"千桐科技\",\"updateTime\":\"2025-05-23 17:42:19\",\"remark\":null,\"id\":726,\"assetId\":173,\"columnName\":\"UPDATER_ID\",\"columnComment\":\"更新人id\",\"columnType\":\"BIGINT\",\"columnLength\":8,\"columnScale\":0,\"nullableFlag\":\"1\",\"pkFlag\":\"0\",\"defaultValue\":null,\"dataElemCodeFlag\":\"0\",\"dataElemCodeId\":null,\"sensitiveLevelId\":null,\"relDataElmeFlag\":\"0\",\"relCleanFlag\":\"0\",\"relAuditFlag\":\"0\",\"description\":null,\"validFlag\":true,\"delFlag\":false,\"elementId\":[],\"relDataElmeName\":null,\"sensitiveLevelName\":null,\"dataElemCodeName\":null,\"cleanRuleList\":null,\"updatorId\":1},{\"paramName\":\"REMARK\",\"paramComment\":\"备注\",\"nullable\":\"1\",\"creatorId\":1,\"createBy\":\"千桐科技\",\"createTime\":\"2025-05-23 17:42:19\",\"updateBy\":\"千桐科技\",\"updateTime\":\"2025-05-23 17:42:19\",\"remark\":null,\"id\":727,\"assetId\":173,\"columnName\":\"REMARK\",\"columnComment\":\"备注\",\"columnType\":\"VARCHAR\",\"columnLength\":512,\"columnScale\":0,\"nullableFlag\":\"1\",\"pkFlag\":\"0\",\"defaultValue\":null,\"dataElemCodeFlag\":\"0\",\"dataElemCodeId\":null,\"sensitiveLevelId\":null,\"relDataElmeFlag\":\"0\",\"relCleanFlag\":\"0\",\"relAuditFlag\":\"0\",\"description\":null,\"validFlag\":true,\"delFlag\":false,\"elementId\":[],\"relDataElmeName\":null,\"sensitiveLevelName\":null,\"dataElemCodeName\":null,\"cleanRuleList\":null,\"updatorId\":1}],\"resParams\":[{\"fieldName\":\"ID\",\"fieldComment\":\"ID\",\"creatorId\":1,\"createBy\":\"千桐科技\",\"createTime\":\"2025-05-23 17:42:18\",\"updateBy\":\"千桐科技\",\"updateTime\":\"2025-05-23 17:42:18\",\"remark\":null,\"id\":706,\"assetId\":173,\"columnName\":\"ID\",\"columnComment\":\"ID\",\"columnType\":\"INT\",\"columnLength\":4,\"columnScale\":0,\"nullableFlag\":\"0\",\"pkFlag\":\"1\",\"defaultValue\":null,\"dataElemCodeFlag\":\"0\",\"dataElemCodeId\":null,\"sensitiveLevelId\":null,\"relDataElmeFlag\":\"0\",\"relCleanFlag\":\"0\",\"relAuditFlag\":\"0\",\"description\":null,\"validFlag\":true,\"delFlag\":false,\"elementId\":[],\"relDataElmeName\":null,\"sensitiveLevelName\":null,\"dataElemCodeName\":null,\"cleanRuleList\":null,\"updatorId\":1},{\"fieldName\":\"SENDER_ID\",\"fieldComment\":\"发送人\",\"creatorId\":1,\"createBy\":\"千桐科技\",\"createTime\":\"2025-05-23 17:42:18\",\"updateBy\":\"千桐科技\",\"updateTime\":\"2025-05-23 17:42:18\",\"remark\":null,\"id\":707,\"assetId\":173,\"columnName\":\"SENDER_ID\",\"columnComment\":\"发送人\",\"columnType\":\"BIGINT\",\"columnLength\":8,\"columnScale\":0,\"nullableFlag\":\"1\",\"pkFlag\":\"0\",\"defaultValue\":null,\"dataElemCodeFlag\":\"0\",\"dataElemCodeId\":null,\"sensitiveLevelId\":null,\"relDataElmeFlag\":\"0\",\"relCleanFlag\":\"0\",\"relAuditFlag\":\"0\",\"description\":null,\"validFlag\":true,\"delFlag\":false,\"elementId\":[],\"relDataElmeName\":null,\"sensitiveLevelName\":null,\"dataElemCodeName\":null,\"cleanRuleList\":null,\"updatorId\":1},{\"fieldName\":\"RECEIVER_ID\",\"fieldComment\":\"接收人\",\"creatorId\":1,\"createBy\":\"千桐科技\",\"createTime\":\"2025-05-23 17:42:18\",\"updateBy\":\"千桐科技\",\"updateTime\":\"2025-05-23 17:42:18\",\"remark\":null,\"id\":708,\"assetId\":173,\"columnName\":\"RECEIVER_ID\",\"columnComment\":\"接收人\",\"columnType\":\"BIGINT\",\"columnLength\":8,\"columnScale\":0,\"nullableFlag\":\"1\",\"pkFlag\":\"0\",\"defaultValue\":null,\"dataElemCodeFlag\":\"0\",\"dataElemCodeId\":null,\"sensitiveLevelId\":null,\"relDataElmeFlag\":\"0\",\"relCleanFlag\":\"0\",\"relAuditFlag\":\"0\",\"description\":null,\"validFlag\":true,\"delFlag\":false,\"elementId\":[],\"relDataElmeName\":null,\"sensitiveLevelName\":null,\"dataElemCodeName\":null,\"cleanRuleList\":null,\"updatorId\":1},{\"fieldName\":\"TITLE\",\"fieldComment\":\"消息标题\",\"creatorId\":1,\"createBy\":\"千桐科技\",\"createTime\":\"2025-05-23 17:42:18\",\"updateBy\":\"千桐科技\",\"updateTime\":\"2025-05-23 17:42:18\",\"remark\":null,\"id\":709,\"assetId\":173,\"columnName\":\"TITLE\",\"columnComment\":\"消息标题\",\"columnType\":\"VARCHAR\",\"columnLength\":128,\"columnScale\":0,\"nullableFlag\":\"0\",\"pkFlag\":\"0\",\"defaultValue\":null,\"dataElemCodeFlag\":\"0\",\"dataElemCodeId\":null,\"sensitiveLevelId\":null,\"relDataElmeFlag\":\"0\",\"relCleanFlag\":\"0\",\"relAuditFlag\":\"0\",\"description\":null,\"validFlag\":true,\"delFlag\":false,\"elementId\":[],\"relDataElmeName\":null,\"sensitiveLevelName\":null,\"dataElemCodeName\":null,\"cleanRuleList\":null,\"updatorId\":1},{\"fieldName\":\"CONTENT\",\"fieldComment\":\"消息模板内容\",\"creatorId\":1,\"createBy\":\"千桐科技\",\"createTime\":\"2025-05-23 17:42:18\",\"updateBy\":\"千桐科技\",\"updateTime\":\"2025-05-23 17:42:18\",\"remark\":null,\"id\":710,\"assetId\":173,\"columnName\":\"CONTENT\",\"columnComment\":\"消息模板内容\",\"columnType\":\"VARCHAR\",\"columnLength\":3072,\"columnScale\":0,\"nullableFlag\":\"0\",\"pkFlag\":\"0\",\"defaultValue\":null,\"dataElemCodeFlag\":\"0\",\"dataElemCodeId\":null,\"sensitiveLevelId\":null,\"relDataElmeFlag\":\"0\",\"relCleanFlag\":\"0\",\"relAuditFlag\":\"0\",\"description\":null,\"validFlag\":true,\"delFlag\":false,\"elementId\":[],\"relDataElmeName\":null,\"sensitiveLevelName\":null,\"dataElemCodeName\":null,\"cleanRuleList\":null,\"updatorId\":1},{\"fieldName\":\"CATEGORY\",\"fieldComment\":\"消息类别\",\"creatorId\":1,\"createBy\":\"千桐科技\",\"createTime\":\"2025-05-23 17:42:18\",\"updateBy\":\"千桐科技\",\"updateTime\":\"2025-05-23 17:42:18\",\"remark\":null,\"id\":711,\"assetId\":173,\"columnName\":\"CATEGORY\",\"columnComment\":\"消息类别\",\"columnType\":\"INT\",\"columnLength\":4,\"columnScale\":0,\"nullableFlag\":\"0\",\"pkFlag\":\"0\",\"defaultValue\":null,\"dataElemCodeFlag\":\"0\",\"dataElemCodeId\":null,\"sensitiveLevelId\":null,\"relDataElmeFlag\":\"0\",\"relCleanFlag\":\"0\",\"relAuditFlag\":\"0\",\"description\":null,\"validFlag\":true,\"delFlag\":false,\"elementId\":[],\"relDataElmeName\":null,\"sensitiveLevelName\":null,\"dataElemCodeName\":null,\"cleanRuleList\":null,\"updatorId\":1},{\"fieldName\":\"MSG_LEVEL\",\"fieldComment\":\"消息等级\",\"creatorId\":1,\"createBy\":\"千桐科技\",\"createTime\":\"2025-05-23 17:42:18\",\"updateBy\":\"千桐科技\",\"updateTime\":\"2025-05-23 17:42:18\",\"remark\":null,\"id\":712,\"assetId\":173,\"columnName\":\"MSG_LEVEL\",\"columnComment\":\"消息等级\",\"columnType\":\"INT\",\"columnLength\":4,\"columnScale\":0,\"nullableFlag\":\"0\",\"pkFlag\":\"0\",\"defaultValue\":\"0\",\"dataElemCodeFlag\":\"0\",\"dataElemCodeId\":null,\"sensitiveLevelId\":null,\"relDataElmeFlag\":\"0\",\"relCleanFlag\":\"0\",\"relAuditFlag\":\"0\",\"description\":null,\"validFlag\":true,\"delFlag\":false,\"elementId\":[],\"relDataElmeName\":null,\"sensitiveLevelName\":null,\"dataElemCodeName\":null,\"cleanRuleList\":null,\"updatorId\":1},{\"fieldName\":\"MODULE\",\"fieldComment\":\"消息模块\",\"creatorId\":1,\"createBy\":\"千桐科技\",\"createTime\":\"2025-05-23 17:42:18\",\"updateBy\":\"千桐科技\",\"updateTime\":\"2025-05-23 17:42:18\",\"remark\":null,\"id\":713,\"assetId\":173,\"columnName\":\"MODULE\",\"columnComment\":\"消息模块\",\"columnType\":\"INT\",\"columnLength\":4,\"columnScale\":0,\"nullableFlag\":\"0\",\"pkFlag\":\"0\",\"defaultValue\":\"0\",\"dataElemCodeFlag\":\"0\",\"dataElemCodeId\":null,\"sensitiveLevelId\":null,\"relDataElmeFlag\":\"0\",\"relCleanFlag\":\"0\",\"relAuditFlag\":\"0\",\"description\":null,\"validFlag\":true,\"delFlag\":false,\"elementId\":[],\"relDataElmeName\":null,\"sensitiveLevelName\":null,\"dataElemCodeName\":null,\"cleanRuleList\":null,\"updatorId\":1},{\"fieldName\":\"ENTITY_TYPE\",\"fieldComment\":\"实体类型\",\"creatorId\":1,\"createBy\":\"千桐科技\",\"createTime\":\"2025-05-23 17:42:18\",\"updateBy\":\"千桐科技\",\"updateTime\":\"2025-05-23 17:42:18\",\"remark\":null,\"id\":714,\"assetId\":173,\"columnName\":\"ENTITY_TYPE\",\"columnComment\":\"实体类型\",\"columnType\":\"INT\",\"columnLength\":4,\"columnScale\":0,\"nullableFlag\":\"1\",\"pkFlag\":\"0\",\"defaultValue\":null,\"dataElemCodeFlag\":\"0\",\"dataElemCodeId\":null,\"sensitiveLevelId\":null,\"relDataElmeFlag\":\"0\",\"relCleanFlag\":\"0\",\"relAuditFlag\":\"0\",\"description\":null,\"validFlag\":true,\"delFlag\":false,\"elementId\":[],\"relDataElmeName\":null,\"sensitiveLevelName\":null,\"dataElemCodeName\":null,\"cleanRuleList\":null,\"updatorId\":1},{\"fieldName\":\"ENTITY_ID\",\"fieldComment\":\"实体id\",\"creatorId\":1,\"createBy\":\"千桐科技\",\"createTime\":\"2025-05-23 17:42:18\",\"updateBy\":\"千桐科技\",\"updateTime\":\"2025-05-23 17:42:18\",\"remark\":null,\"id\":715,\"assetId\":173,\"columnName\":\"ENTITY_ID\",\"columnComment\":\"实体id\",\"columnType\":\"BIGINT\",\"columnLength\":8,\"columnScale\":0,\"nullableFlag\":\"1\",\"pkFlag\":\"0\",\"defaultValue\":null,\"dataElemCodeFlag\":\"0\",\"dataElemCodeId\":null,\"sensitiveLevelId\":null,\"relDataElmeFlag\":\"0\",\"relCleanFlag\":\"0\",\"relAuditFlag\":\"0\",\"description\":null,\"validFlag\":true,\"delFlag\":false,\"elementId\":[],\"relDataElmeName\":null,\"sensitiveLevelName\":null,\"dataElemCodeName\":null,\"cleanRuleList\":null,\"updatorId\":1},{\"fieldName\":\"ENTITY_URL\",\"fieldComment\":\"消息链接\",\"creatorId\":1,\"createBy\":\"千桐科技\",\"createTime\":\"2025-05-23 17:42:18\",\"updateBy\":\"千桐科技\",\"updateTime\":\"2025-05-23 17:42:18\",\"remark\":null,\"id\":716,\"assetId\":173,\"columnName\":\"ENTITY_URL\",\"columnComment\":\"消息链接\",\"columnType\":\"VARCHAR\",\"columnLength\":256,\"columnScale\":0,\"nullableFlag\":\"1\",\"pkFlag\":\"0\",\"defaultValue\":null,\"dataElemCodeFlag\":\"0\",\"dataElemCodeId\":null,\"sensitiveLevelId\":null,\"relDataElmeFlag\":\"0\",\"relCleanFlag\":\"0\",\"relAuditFlag\":\"0\",\"description\":null,\"validFlag\":true,\"delFlag\":false,\"elementId\":[],\"relDataElmeName\":null,\"sensitiveLevelName\":null,\"dataElemCodeName\":null,\"cleanRuleList\":null,\"updatorId\":1},{\"fieldName\":\"HAS_READ\",\"fieldComment\":\"是否已读\",\"creatorId\":1,\"createBy\":\"千桐科技\",\"createTime\":\"2025-05-23 17:42:18\",\"updateBy\":\"千桐科技\",\"updateTime\":\"2025-05-23 17:42:18\",\"remark\":null,\"id\":717,\"assetId\":173,\"columnName\":\"HAS_READ\",\"columnComment\":\"是否已读\",\"columnType\":\"TINYINT\",\"columnLength\":1,\"columnScale\":0,\"nullableFlag\":\"1\",\"pkFlag\":\"0\",\"defaultValue\":\"0\",\"dataElemCodeFlag\":\"0\",\"dataElemCodeId\":null,\"sensitiveLevelId\":null,\"relDataElmeFlag\":\"0\",\"relCleanFlag\":\"0\",\"relAuditFlag\":\"0\",\"description\":null,\"validFlag\":true,\"delFlag\":false,\"elementId\":[],\"relDataElmeName\":null,\"sensitiveLevelName\":null,\"dataElemCodeName\":null,\"cleanRuleList\":null,\"updatorId\":1},{\"fieldName\":\"HAS_RETRACTION\",\"fieldComment\":\"是否撤回\",\"creatorId\":1,\"createBy\":\"千桐科技\",\"createTime\":\"2025-05-23 17:42:18\",\"updateBy\":\"千桐科技\",\"updateTime\":\"2025-05-23 17:42:18\",\"remark\":null,\"id\":718,\"assetId\":173,\"columnName\":\"HAS_RETRACTION\",\"columnComment\":\"是否撤回\",\"columnType\":\"TINYINT\",\"columnLength\":1,\"columnScale\":0,\"nullableFlag\":\"1\",\"pkFlag\":\"0\",\"defaultValue\":\"0\",\"dataElemCodeFlag\":\"0\",\"dataElemCodeId\":null,\"sensitiveLevelId\":null,\"relDataElmeFlag\":\"0\",\"relCleanFlag\":\"0\",\"relAuditFlag\":\"0\",\"description\":null,\"validFlag\":true,\"delFlag\":false,\"elementId\":[],\"relDataElmeName\":null,\"sensitiveLevelName\":null,\"dataElemCodeName\":null,\"cleanRuleList\":null,\"updatorId\":1},{\"fieldName\":\"VALID_FLAG\",\"fieldComment\":\"是否有效;0：无效，1：有效\",\"creatorId\":1,\"createBy\":\"千桐科技\",\"createTime\":\"2025-05-23 17:42:19\",\"updateBy\":\"千桐科技\",\"updateTime\":\"2025-05-23 17:42:19\",\"remark\":null,\"id\":719,\"assetId\":173,\"columnName\":\"VALID_FLAG\",\"columnComment\":\"是否有效;0：无效，1：有效\",\"columnType\":\"TINYINT\",\"columnLength\":1,\"columnScale\":0,\"nullableFlag\":\"0\",\"pkFlag\":\"0\",\"defaultValue\":\"1\",\"dataElemCodeFlag\":\"0\",\"dataElemCodeId\":null,\"sensitiveLevelId\":null,\"relDataElmeFlag\":\"0\",\"relCleanFlag\":\"0\",\"relAuditFlag\":\"0\",\"description\":null,\"validFlag\":true,\"delFlag\":false,\"elementId\":[],\"relDataElmeName\":null,\"sensitiveLevelName\":null,\"dataElemCodeName\":null,\"cleanRuleList\":null,\"updatorId\":1},{\"fieldName\":\"DEL_FLAG\",\"fieldComment\":\"删除标志;1：已删除，0：未删除\",\"creatorId\":1,\"createBy\":\"千桐科技\",\"createTime\":\"2025-05-23 17:42:19\",\"updateBy\":\"千桐科技\",\"updateTime\":\"2025-05-23 17:42:19\",\"remark\":null,\"id\":720,\"assetId\":173,\"columnName\":\"DEL_FLAG\",\"columnComment\":\"删除标志;1：已删除，0：未删除\",\"columnType\":\"TINYINT\",\"columnLength\":1,\"columnScale\":0,\"nullableFlag\":\"0\",\"pkFlag\":\"0\",\"defaultValue\":\"0\",\"dataElemCodeFlag\":\"0\",\"dataElemCodeId\":null,\"sensitiveLevelId\":null,\"relDataElmeFlag\":\"0\",\"relCleanFlag\":\"0\",\"relAuditFlag\":\"0\",\"description\":null,\"validFlag\":true,\"delFlag\":false,\"elementId\":[],\"relDataElmeName\":null,\"sensitiveLevelName\":null,\"dataElemCodeName\":null,\"cleanRuleList\":null,\"updatorId\":1},{\"fieldName\":\"CREATE_BY\",\"fieldComment\":\"创建人\",\"creatorId\":1,\"createBy\":\"千桐科技\",\"createTime\":\"2025-05-23 17:42:19\",\"updateBy\":\"千桐科技\",\"updateTime\":\"2025-05-23 17:42:19\",\"remark\":null,\"id\":721,\"assetId\":173,\"columnName\":\"CREATE_BY\",\"columnComment\":\"创建人\",\"columnType\":\"VARCHAR\",\"columnLength\":32,\"columnScale\":0,\"nullableFlag\":\"1\",\"pkFlag\":\"0\",\"defaultValue\":null,\"dataElemCodeFlag\":\"0\",\"dataElemCodeId\":null,\"sensitiveLevelId\":null,\"relDataElmeFlag\":\"0\",\"relCleanFlag\":\"0\",\"relAuditFlag\":\"0\",\"description\":null,\"validFlag\":true,\"delFlag\":false,\"elementId\":[],\"relDataElmeName\":null,\"sensitiveLevelName\":null,\"dataElemCodeName\":null,\"cleanRuleList\":null,\"updatorId\":1},{\"fieldName\":\"CREATOR_ID\",\"fieldComment\":\"创建人id\",\"creatorId\":1,\"createBy\":\"千桐科技\",\"createTime\":\"2025-05-23 17:42:19\",\"updateBy\":\"千桐科技\",\"updateTime\":\"2025-05-23 17:42:19\",\"remark\":null,\"id\":722,\"assetId\":173,\"columnName\":\"CREATOR_ID\",\"columnComment\":\"创建人id\",\"columnType\":\"BIGINT\",\"columnLength\":8,\"columnScale\":0,\"nullableFlag\":\"1\",\"pkFlag\":\"0\",\"defaultValue\":null,\"dataElemCodeFlag\":\"0\",\"dataElemCodeId\":null,\"sensitiveLevelId\":null,\"relDataElmeFlag\":\"0\",\"relCleanFlag\":\"0\",\"relAuditFlag\":\"0\",\"description\":null,\"validFlag\":true,\"delFlag\":false,\"elementId\":[],\"relDataElmeName\":null,\"sensitiveLevelName\":null,\"dataElemCodeName\":null,\"cleanRuleList\":null,\"updatorId\":1},{\"fieldName\":\"CREATE_TIME\",\"fieldComment\":\"创建时间\",\"creatorId\":1,\"createBy\":\"千桐科技\",\"createTime\":\"2025-05-23 17:42:19\",\"updateBy\":\"千桐科技\",\"updateTime\":\"2025-05-23 17:42:19\",\"remark\":null,\"id\":723,\"assetId\":173,\"columnName\":\"CREATE_TIME\",\"columnComment\":\"创建时间\",\"columnType\":\"TIMESTAMP\",\"columnLength\":8,\"columnScale\":0,\"nullableFlag\":\"0\",\"pkFlag\":\"0\",\"defaultValue\":\"CURRENT_TIMESTAMP()\",\"dataElemCodeFlag\":\"0\",\"dataElemCodeId\":null,\"sensitiveLevelId\":null,\"relDataElmeFlag\":\"0\",\"relCleanFlag\":\"0\",\"relAuditFlag\":\"0\",\"description\":null,\"validFlag\":true,\"delFlag\":false,\"elementId\":[],\"relDataElmeName\":null,\"sensitiveLevelName\":null,\"dataElemCodeName\":null,\"cleanRuleList\":null,\"updatorId\":1},{\"fieldName\":\"UPDATE_BY\",\"fieldComment\":\"更新人\",\"creatorId\":1,\"createBy\":\"千桐科技\",\"createTime\":\"2025-05-23 17:42:19\",\"updateBy\":\"千桐科技\",\"updateTime\":\"2025-05-23 17:42:19\",\"remark\":null,\"id\":724,\"assetId\":173,\"columnName\":\"UPDATE_BY\",\"columnComment\":\"更新人\",\"columnType\":\"VARCHAR\",\"columnLength\":32,\"columnScale\":0,\"nullableFlag\":\"1\",\"pkFlag\":\"0\",\"defaultValue\":null,\"dataElemCodeFlag\":\"0\",\"dataElemCodeId\":null,\"sensitiveLevelId\":null,\"relDataElmeFlag\":\"0\",\"relCleanFlag\":\"0\",\"relAuditFlag\":\"0\",\"description\":null,\"validFlag\":true,\"delFlag\":false,\"elementId\":[],\"relDataElmeName\":null,\"sensitiveLevelName\":null,\"dataElemCodeName\":null,\"cleanRuleList\":null,\"updatorId\":1},{\"fieldName\":\"UPDATE_TIME\",\"fieldComment\":\"更新时间\",\"creatorId\":1,\"createBy\":\"千桐科技\",\"createTime\":\"2025-05-23 17:42:19\",\"updateBy\":\"千桐科技\",\"updateTime\":\"2025-05-23 17:42:19\",\"remark\":null,\"id\":725,\"assetId\":173,\"columnName\":\"UPDATE_TIME\",\"columnComment\":\"更新时间\",\"columnType\":\"TIMESTAMP\",\"columnLength\":8,\"columnScale\":0,\"nullableFlag\":\"0\",\"pkFlag\":\"0\",\"defaultValue\":\"CURRENT_TIMESTAMP()\",\"dataElemCodeFlag\":\"0\",\"dataElemCodeId\":null,\"sensitiveLevelId\":null,\"relDataElmeFlag\":\"0\",\"relCleanFlag\":\"0\",\"relAuditFlag\":\"0\",\"description\":null,\"validFlag\":true,\"delFlag\":false,\"elementId\":[],\"relDataElmeName\":null,\"sensitiveLevelName\":null,\"dataElemCodeName\":null,\"cleanRuleList\":null,\"updatorId\":1},{\"fieldName\":\"UPDATER_ID\",\"fieldComment\":\"更新人id\",\"creatorId\":1,\"createBy\":\"千桐科技\",\"createTime\":\"2025-05-23 17:42:19\",\"updateBy\":\"千桐科技\",\"updateTime\":\"2025-05-23 17:42:19\",\"remark\":null,\"id\":726,\"assetId\":173,\"columnName\":\"UPDATER_ID\",\"columnComment\":\"更新人id\",\"columnType\":\"BIGINT\",\"columnLength\":8,\"columnScale\":0,\"nullableFlag\":\"1\",\"pkFlag\":\"0\",\"defaultValue\":null,\"dataElemCodeFlag\":\"0\",\"dataElemCodeId\":null,\"sensitiveLevelId\":null,\"relDataElmeFlag\":\"0\",\"relCleanFlag\":\"0\",\"relAuditFlag\":\"0\",\"description\":null,\"validFlag\":true,\"delFlag\":false,\"elementId\":[],\"relDataElmeName\":null,\"sensitiveLevelName\":null,\"dataElemCodeName\":null,\"cleanRuleList\":null,\"updatorId\":1},{\"fieldName\":\"REMARK\",\"fieldComment\":\"备注\",\"creatorId\":1,\"createBy\":\"千桐科技\",\"createTime\":\"2025-05-23 17:42:19\",\"updateBy\":\"千桐科技\",\"updateTime\":\"2025-05-23 17:42:19\",\"remark\":null,\"id\":727,\"assetId\":173,\"columnName\":\"REMARK\",\"columnComment\":\"备注\",\"columnType\":\"VARCHAR\",\"columnLength\":512,\"columnScale\":0,\"nullableFlag\":\"1\",\"pkFlag\":\"0\",\"defaultValue\":null,\"dataElemCodeFlag\":\"0\",\"dataElemCodeId\":null,\"sensitiveLevelId\":null,\"relDataElmeFlag\":\"0\",\"relCleanFlag\":\"0\",\"relAuditFlag\":\"0\",\"description\":null,\"validFlag\":true,\"delFlag\":false,\"elementId\":[],\"relDataElmeName\":null,\"sensitiveLevelName\":null,\"dataElemCodeName\":null,\"cleanRuleList\":null,\"updatorId\":1}],\"table\":{\"creatorId\":1,\"createBy\":\"千桐科技\",\"createTime\":\"2025-05-23 17:42:18\",\"updateBy\":\"千桐科技\",\"updateTime\":\"2025-05-23 17:42:18\",\"remark\":null,\"id\":173,\"name\":\"示例-消息\",\"type\":\"1\",\"catCode\":\"A01\",\"catName\":null,\"sourceType\":null,\"daAssetThemeRelList\":null,\"datasourceId\":48,\"datasourceName\":null,\"datasourceIp\":null,\"datasourceType\":null,\"tableName\":\"MESSAGE\",\"tableComment\":\"消息\",\"dataCount\":0,\"fieldCount\":22,\"source\":\"3\",\"status\":\"1\",\"description\":\"\",\"validFlag\":true,\"delFlag\":false,\"projectId\":null,\"projectCode\":null,\"updatorId\":1},\"filteredTableOptions\":[{\"creatorId\":1,\"createBy\":\"千桐科技\",\"createTime\":\"2025-05-23 17:42:18\",\"updateBy\":\"千桐科技\",\"updateTime\":\"2025-05-23 17:42:18\",\"remark\":null,\"id\":173,\"name\":\"示例-消息\",\"type\":\"1\",\"catCode\":\"A01\",\"catName\":null,\"sourceType\":null,\"daAssetThemeRelList\":null,\"datasourceId\":48,\"datasourceName\":null,\"datasourceIp\":null,\"datasourceType\":null,\"tableName\":\"MESSAGE\",\"tableComment\":\"消息\",\"dataCount\":0,\"fieldCount\":22,\"source\":\"3\",\"status\":\"1\",\"description\":\"\",\"validFlag\":true,\"delFlag\":false,\"projectId\":null,\"projectCode\":null,\"updatorId\":1}],\"tableId\":173,\"tableName\":\"MESSAGE\",\"fieldParams\":[{\"creatorId\":1,\"createBy\":\"千桐科技\",\"createTime\":\"2025-05-23 17:42:18\",\"updateBy\":\"千桐科技\",\"updateTime\":\"2025-05-23 17:42:18\",\"remark\":null,\"id\":706,\"assetId\":173,\"columnName\":\"ID\",\"columnComment\":\"ID\",\"columnType\":\"INT\",\"columnLength\":4,\"columnScale\":0,\"nullableFlag\":\"0\",\"pkFlag\":\"1\",\"defaultValue\":null,\"dataElemCodeFlag\":\"0\",\"dataElemCodeId\":null,\"sensitiveLevelId\":null,\"relDataElmeFlag\":\"0\",\"relCleanFlag\":\"0\",\"relAuditFlag\":\"0\",\"description\":null,\"validFlag\":true,\"delFlag\":false,\"elementId\":[],\"relDataElmeName\":null,\"sensitiveLevelName\":null,\"dataElemCodeName\":null,\"cleanRuleList\":null,\"updatorId\":1},{\"creatorId\":1,\"createBy\":\"千桐科技\",\"createTime\":\"2025-05-23 17:42:18\",\"updateBy\":\"千桐科技\",\"updateTime\":\"2025-05-23 17:42:18\",\"remark\":null,\"id\":707,\"assetId\":173,\"columnName\":\"SENDER_ID\",\"columnComment\":\"发送人\",\"columnType\":\"BIGINT\",\"columnLength\":8,\"columnScale\":0,\"nullableFlag\":\"1\",\"pkFlag\":\"0\",\"defaultValue\":null,\"dataElemCodeFlag\":\"0\",\"dataElemCodeId\":null,\"sensitiveLevelId\":null,\"relDataElmeFlag\":\"0\",\"relCleanFlag\":\"0\",\"relAuditFlag\":\"0\",\"description\":null,\"validFlag\":true,\"delFlag\":false,\"elementId\":[],\"relDataElmeName\":null,\"sensitiveLevelName\":null,\"dataElemCodeName\":null,\"cleanRuleList\":null,\"updatorId\":1},{\"creatorId\":1,\"createBy\":\"千桐科技\",\"createTime\":\"2025-05-23 17:42:18\",\"updateBy\":\"千桐科技\",\"updateTime\":\"2025-05-23 17:42:18\",\"remark\":null,\"id\":708,\"assetId\":173,\"columnName\":\"RECEIVER_ID\",\"columnComment\":\"接收人\",\"columnType\":\"BIGINT\",\"columnLength\":8,\"columnScale\":0,\"nullableFlag\":\"1\",\"pkFlag\":\"0\",\"defaultValue\":null,\"dataElemCodeFlag\":\"0\",\"dataElemCodeId\":null,\"sensitiveLevelId\":null,\"relDataElmeFlag\":\"0\",\"relCleanFlag\":\"0\",\"relAuditFlag\":\"0\",\"description\":null,\"validFlag\":true,\"delFlag\":false,\"elementId\":[],\"relDataElmeName\":null,\"sensitiveLevelName\":null,\"dataElemCodeName\":null,\"cleanRuleList\":null,\"updatorId\":1},{\"creatorId\":1,\"createBy\":\"千桐科技\",\"createTime\":\"2025-05-23 17:42:18\",\"updateBy\":\"千桐科技\",\"updateTime\":\"2025-05-23 17:42:18\",\"remark\":null,\"id\":709,\"assetId\":173,\"columnName\":\"TITLE\",\"columnComment\":\"消息标题\",\"columnType\":\"VARCHAR\",\"columnLength\":128,\"columnScale\":0,\"nullableFlag\":\"0\",\"pkFlag\":\"0\",\"defaultValue\":null,\"dataElemCodeFlag\":\"0\",\"dataElemCodeId\":null,\"sensitiveLevelId\":null,\"relDataElmeFlag\":\"0\",\"relCleanFlag\":\"0\",\"relAuditFlag\":\"0\",\"description\":null,\"validFlag\":true,\"delFlag\":false,\"elementId\":[],\"relDataElmeName\":null,\"sensitiveLevelName\":null,\"dataElemCodeName\":null,\"cleanRuleList\":null,\"updatorId\":1},{\"creatorId\":1,\"createBy\":\"千桐科技\",\"createTime\":\"2025-05-23 17:42:18\",\"updateBy\":\"千桐科技\",\"updateTime\":\"2025-05-23 17:42:18\",\"remark\":null,\"id\":710,\"assetId\":173,\"columnName\":\"CONTENT\",\"columnComment\":\"消息模板内容\",\"columnType\":\"VARCHAR\",\"columnLength\":3072,\"columnScale\":0,\"nullableFlag\":\"0\",\"pkFlag\":\"0\",\"defaultValue\":null,\"dataElemCodeFlag\":\"0\",\"dataElemCodeId\":null,\"sensitiveLevelId\":null,\"relDataElmeFlag\":\"0\",\"relCleanFlag\":\"0\",\"relAuditFlag\":\"0\",\"description\":null,\"validFlag\":true,\"delFlag\":false,\"elementId\":[],\"relDataElmeName\":null,\"sensitiveLevelName\":null,\"dataElemCodeName\":null,\"cleanRuleList\":null,\"updatorId\":1},{\"creatorId\":1,\"createBy\":\"千桐科技\",\"createTime\":\"2025-05-23 17:42:18\",\"updateBy\":\"千桐科技\",\"updateTime\":\"2025-05-23 17:42:18\",\"remark\":null,\"id\":711,\"assetId\":173,\"columnName\":\"CATEGORY\",\"columnComment\":\"消息类别\",\"columnType\":\"INT\",\"columnLength\":4,\"columnScale\":0,\"nullableFlag\":\"0\",\"pkFlag\":\"0\",\"defaultValue\":null,\"dataElemCodeFlag\":\"0\",\"dataElemCodeId\":null,\"sensitiveLevelId\":null,\"relDataElmeFlag\":\"0\",\"relCleanFlag\":\"0\",\"relAuditFlag\":\"0\",\"description\":null,\"validFlag\":true,\"delFlag\":false,\"elementId\":[],\"relDataElmeName\":null,\"sensitiveLevelName\":null,\"dataElemCodeName\":null,\"cleanRuleList\":null,\"updatorId\":1},{\"creatorId\":1,\"createBy\":\"千桐科技\",\"createTime\":\"2025-05-23 17:42:18\",\"updateBy\":\"千桐科技\",\"updateTime\":\"2025-05-23 17:42:18\",\"remark\":null,\"id\":712,\"assetId\":173,\"columnName\":\"MSG_LEVEL\",\"columnComment\":\"消息等级\",\"columnType\":\"INT\",\"columnLength\":4,\"columnScale\":0,\"nullableFlag\":\"0\",\"pkFlag\":\"0\",\"defaultValue\":\"0\",\"dataElemCodeFlag\":\"0\",\"dataElemCodeId\":null,\"sensitiveLevelId\":null,\"relDataElmeFlag\":\"0\",\"relCleanFlag\":\"0\",\"relAuditFlag\":\"0\",\"description\":null,\"validFlag\":true,\"delFlag\":false,\"elementId\":[],\"relDataElmeName\":null,\"sensitiveLevelName\":null,\"dataElemCodeName\":null,\"cleanRuleList\":null,\"updatorId\":1},{\"creatorId\":1,\"createBy\":\"千桐科技\",\"createTime\":\"2025-05-23 17:42:18\",\"updateBy\":\"千桐科技\",\"updateTime\":\"2025-05-23 17:42:18\",\"remark\":null,\"id\":713,\"assetId\":173,\"columnName\":\"MODULE\",\"columnComment\":\"消息模块\",\"columnType\":\"INT\",\"columnLength\":4,\"columnScale\":0,\"nullableFlag\":\"0\",\"pkFlag\":\"0\",\"defaultValue\":\"0\",\"dataElemCodeFlag\":\"0\",\"dataElemCodeId\":null,\"sensitiveLevelId\":null,\"relDataElmeFlag\":\"0\",\"relCleanFlag\":\"0\",\"relAuditFlag\":\"0\",\"description\":null,\"validFlag\":true,\"delFlag\":false,\"elementId\":[],\"relDataElmeName\":null,\"sensitiveLevelName\":null,\"dataElemCodeName\":null,\"cleanRuleList\":null,\"updatorId\":1},{\"creatorId\":1,\"createBy\":\"千桐科技\",\"createTime\":\"2025-05-23 17:42:18\",\"updateBy\":\"千桐科技\",\"updateTime\":\"2025-05-23 17:42:18\",\"remark\":null,\"id\":714,\"assetId\":173,\"columnName\":\"ENTITY_TYPE\",\"columnComment\":\"实体类型\",\"columnType\":\"INT\",\"columnLength\":4,\"columnScale\":0,\"nullableFlag\":\"1\",\"pkFlag\":\"0\",\"defaultValue\":null,\"dataElemCodeFlag\":\"0\",\"dataElemCodeId\":null,\"sensitiveLevelId\":null,\"relDataElmeFlag\":\"0\",\"relCleanFlag\":\"0\",\"relAuditFlag\":\"0\",\"description\":null,\"validFlag\":true,\"delFlag\":false,\"elementId\":[],\"relDataElmeName\":null,\"sensitiveLevelName\":null,\"dataElemCodeName\":null,\"cleanRuleList\":null,\"updatorId\":1},{\"creatorId\":1,\"createBy\":\"千桐科技\",\"createTime\":\"2025-05-23 17:42:18\",\"updateBy\":\"千桐科技\",\"updateTime\":\"2025-05-23 17:42:18\",\"remark\":null,\"id\":715,\"assetId\":173,\"columnName\":\"ENTITY_ID\",\"columnComment\":\"实体id\",\"columnType\":\"BIGINT\",\"columnLength\":8,\"columnScale\":0,\"nullableFlag\":\"1\",\"pkFlag\":\"0\",\"defaultValue\":null,\"dataElemCodeFlag\":\"0\",\"dataElemCodeId\":null,\"sensitiveLevelId\":null,\"relDataElmeFlag\":\"0\",\"relCleanFlag\":\"0\",\"relAuditFlag\":\"0\",\"description\":null,\"validFlag\":true,\"delFlag\":false,\"elementId\":[],\"relDataElmeName\":null,\"sensitiveLevelName\":null,\"dataElemCodeName\":null,\"cleanRuleList\":null,\"updatorId\":1},{\"creatorId\":1,\"createBy\":\"千桐科技\",\"createTime\":\"2025-05-23 17:42:18\",\"updateBy\":\"千桐科技\",\"updateTime\":\"2025-05-23 17:42:18\",\"remark\":null,\"id\":716,\"assetId\":173,\"columnName\":\"ENTITY_URL\",\"columnComment\":\"消息链接\",\"columnType\":\"VARCHAR\",\"columnLength\":256,\"columnScale\":0,\"nullableFlag\":\"1\",\"pkFlag\":\"0\",\"defaultValue\":null,\"dataElemCodeFlag\":\"0\",\"dataElemCodeId\":null,\"sensitiveLevelId\":null,\"relDataElmeFlag\":\"0\",\"relCleanFlag\":\"0\",\"relAuditFlag\":\"0\",\"description\":null,\"validFlag\":true,\"delFlag\":false,\"elementId\":[],\"relDataElmeName\":null,\"sensitiveLevelName\":null,\"dataElemCodeName\":null,\"cleanRuleList\":null,\"updatorId\":1},{\"creatorId\":1,\"createBy\":\"千桐科技\",\"createTime\":\"2025-05-23 17:42:18\",\"updateBy\":\"千桐科技\",\"updateTime\":\"2025-05-23 17:42:18\",\"remark\":null,\"id\":717,\"assetId\":173,\"columnName\":\"HAS_READ\",\"columnComment\":\"是否已读\",\"columnType\":\"TINYINT\",\"columnLength\":1,\"columnScale\":0,\"nullableFlag\":\"1\",\"pkFlag\":\"0\",\"defaultValue\":\"0\",\"dataElemCodeFlag\":\"0\",\"dataElemCodeId\":null,\"sensitiveLevelId\":null,\"relDataElmeFlag\":\"0\",\"relCleanFlag\":\"0\",\"relAuditFlag\":\"0\",\"description\":null,\"validFlag\":true,\"delFlag\":false,\"elementId\":[],\"relDataElmeName\":null,\"sensitiveLevelName\":null,\"dataElemCodeName\":null,\"cleanRuleList\":null,\"updatorId\":1},{\"creatorId\":1,\"createBy\":\"千桐科技\",\"createTime\":\"2025-05-23 17:42:18\",\"updateBy\":\"千桐科技\",\"updateTime\":\"2025-05-23 17:42:18\",\"remark\":null,\"id\":718,\"assetId\":173,\"columnName\":\"HAS_RETRACTION\",\"columnComment\":\"是否撤回\",\"columnType\":\"TINYINT\",\"columnLength\":1,\"columnScale\":0,\"nullableFlag\":\"1\",\"pkFlag\":\"0\",\"defaultValue\":\"0\",\"dataElemCodeFlag\":\"0\",\"dataElemCodeId\":null,\"sensitiveLevelId\":null,\"relDataElmeFlag\":\"0\",\"relCleanFlag\":\"0\",\"relAuditFlag\":\"0\",\"description\":null,\"validFlag\":true,\"delFlag\":false,\"elementId\":[],\"relDataElmeName\":null,\"sensitiveLevelName\":null,\"dataElemCodeName\":null,\"cleanRuleList\":null,\"updatorId\":1},{\"creatorId\":1,\"createBy\":\"千桐科技\",\"createTime\":\"2025-05-23 17:42:19\",\"updateBy\":\"千桐科技\",\"updateTime\":\"2025-05-23 17:42:19\",\"remark\":null,\"id\":719,\"assetId\":173,\"columnName\":\"VALID_FLAG\",\"columnComment\":\"是否有效;0：无效，1：有效\",\"columnType\":\"TINYINT\",\"columnLength\":1,\"columnScale\":0,\"nullableFlag\":\"0\",\"pkFlag\":\"0\",\"defaultValue\":\"1\",\"dataElemCodeFlag\":\"0\",\"dataElemCodeId\":null,\"sensitiveLevelId\":null,\"relDataElmeFlag\":\"0\",\"relCleanFlag\":\"0\",\"relAuditFlag\":\"0\",\"description\":null,\"validFlag\":true,\"delFlag\":false,\"elementId\":[],\"relDataElmeName\":null,\"sensitiveLevelName\":null,\"dataElemCodeName\":null,\"cleanRuleList\":null,\"updatorId\":1},{\"creatorId\":1,\"createBy\":\"千桐科技\",\"createTime\":\"2025-05-23 17:42:19\",\"updateBy\":\"千桐科技\",\"updateTime\":\"2025-05-23 17:42:19\",\"remark\":null,\"id\":720,\"assetId\":173,\"columnName\":\"DEL_FLAG\",\"columnComment\":\"删除标志;1：已删除，0：未删除\",\"columnType\":\"TINYINT\",\"columnLength\":1,\"columnScale\":0,\"nullableFlag\":\"0\",\"pkFlag\":\"0\",\"defaultValue\":\"0\",\"dataElemCodeFlag\":\"0\",\"dataElemCodeId\":null,\"sensitiveLevelId\":null,\"relDataElmeFlag\":\"0\",\"relCleanFlag\":\"0\",\"relAuditFlag\":\"0\",\"description\":null,\"validFlag\":true,\"delFlag\":false,\"elementId\":[],\"relDataElmeName\":null,\"sensitiveLevelName\":null,\"dataElemCodeName\":null,\"cleanRuleList\":null,\"updatorId\":1},{\"creatorId\":1,\"createBy\":\"千桐科技\",\"createTime\":\"2025-05-23 17:42:19\",\"updateBy\":\"千桐科技\",\"updateTime\":\"2025-05-23 17:42:19\",\"remark\":null,\"id\":721,\"assetId\":173,\"columnName\":\"CREATE_BY\",\"columnComment\":\"创建人\",\"columnType\":\"VARCHAR\",\"columnLength\":32,\"columnScale\":0,\"nullableFlag\":\"1\",\"pkFlag\":\"0\",\"defaultValue\":null,\"dataElemCodeFlag\":\"0\",\"dataElemCodeId\":null,\"sensitiveLevelId\":null,\"relDataElmeFlag\":\"0\",\"relCleanFlag\":\"0\",\"relAuditFlag\":\"0\",\"description\":null,\"validFlag\":true,\"delFlag\":false,\"elementId\":[],\"relDataElmeName\":null,\"sensitiveLevelName\":null,\"dataElemCodeName\":null,\"cleanRuleList\":null,\"updatorId\":1},{\"creatorId\":1,\"createBy\":\"千桐科技\",\"createTime\":\"2025-05-23 17:42:19\",\"updateBy\":\"千桐科技\",\"updateTime\":\"2025-05-23 17:42:19\",\"remark\":null,\"id\":722,\"assetId\":173,\"columnName\":\"CREATOR_ID\",\"columnComment\":\"创建人id\",\"columnType\":\"BIGINT\",\"columnLength\":8,\"columnScale\":0,\"nullableFlag\":\"1\",\"pkFlag\":\"0\",\"defaultValue\":null,\"dataElemCodeFlag\":\"0\",\"dataElemCodeId\":null,\"sensitiveLevelId\":null,\"relDataElmeFlag\":\"0\",\"relCleanFlag\":\"0\",\"relAuditFlag\":\"0\",\"description\":null,\"validFlag\":true,\"delFlag\":false,\"elementId\":[],\"relDataElmeName\":null,\"sensitiveLevelName\":null,\"dataElemCodeName\":null,\"cleanRuleList\":null,\"updatorId\":1},{\"creatorId\":1,\"createBy\":\"千桐科技\",\"createTime\":\"2025-05-23 17:42:19\",\"updateBy\":\"千桐科技\",\"updateTime\":\"2025-05-23 17:42:19\",\"remark\":null,\"id\":723,\"assetId\":173,\"columnName\":\"CREATE_TIME\",\"columnComment\":\"创建时间\",\"columnType\":\"TIMESTAMP\",\"columnLength\":8,\"columnScale\":0,\"nullableFlag\":\"0\",\"pkFlag\":\"0\",\"defaultValue\":\"CURRENT_TIMESTAMP()\",\"dataElemCodeFlag\":\"0\",\"dataElemCodeId\":null,\"sensitiveLevelId\":null,\"relDataElmeFlag\":\"0\",\"relCleanFlag\":\"0\",\"relAuditFlag\":\"0\",\"description\":null,\"validFlag\":true,\"delFlag\":false,\"elementId\":[],\"relDataElmeName\":null,\"sensitiveLevelName\":null,\"dataElemCodeName\":null,\"cleanRuleList\":null,\"updatorId\":1},{\"creatorId\":1,\"createBy\":\"千桐科技\",\"createTime\":\"2025-05-23 17:42:19\",\"updateBy\":\"千桐科技\",\"updateTime\":\"2025-05-23 17:42:19\",\"remark\":null,\"id\":724,\"assetId\":173,\"columnName\":\"UPDATE_BY\",\"columnComment\":\"更新人\",\"columnType\":\"VARCHAR\",\"columnLength\":32,\"columnScale\":0,\"nullableFlag\":\"1\",\"pkFlag\":\"0\",\"defaultValue\":null,\"dataElemCodeFlag\":\"0\",\"dataElemCodeId\":null,\"sensitiveLevelId\":null,\"relDataElmeFlag\":\"0\",\"relCleanFlag\":\"0\",\"relAuditFlag\":\"0\",\"description\":null,\"validFlag\":true,\"delFlag\":false,\"elementId\":[],\"relDataElmeName\":null,\"sensitiveLevelName\":null,\"dataElemCodeName\":null,\"cleanRuleList\":null,\"updatorId\":1},{\"creatorId\":1,\"createBy\":\"千桐科技\",\"createTime\":\"2025-05-23 17:42:19\",\"updateBy\":\"千桐科技\",\"updateTime\":\"2025-05-23 17:42:19\",\"remark\":null,\"id\":725,\"assetId\":173,\"columnName\":\"UPDATE_TIME\",\"columnComment\":\"更新时间\",\"columnType\":\"TIMESTAMP\",\"columnLength\":8,\"columnScale\":0,\"nullableFlag\":\"0\",\"pkFlag\":\"0\",\"defaultValue\":\"CURRENT_TIMESTAMP()\",\"dataElemCodeFlag\":\"0\",\"dataElemCodeId\":null,\"sensitiveLevelId\":null,\"relDataElmeFlag\":\"0\",\"relCleanFlag\":\"0\",\"relAuditFlag\":\"0\",\"description\":null,\"validFlag\":true,\"delFlag\":false,\"elementId\":[],\"relDataElmeName\":null,\"sensitiveLevelName\":null,\"dataElemCodeName\":null,\"cleanRuleList\":null,\"updatorId\":1},{\"creatorId\":1,\"createBy\":\"千桐科技\",\"createTime\":\"2025-05-23 17:42:19\",\"updateBy\":\"千桐科技\",\"updateTime\":\"2025-05-23 17:42:19\",\"remark\":null,\"id\":726,\"assetId\":173,\"columnName\":\"UPDATER_ID\",\"columnComment\":\"更新人id\",\"columnType\":\"BIGINT\",\"columnLength\":8,\"columnScale\":0,\"nullableFlag\":\"1\",\"pkFlag\":\"0\",\"defaultValue\":null,\"dataElemCodeFlag\":\"0\",\"dataElemCodeId\":null,\"sensitiveLevelId\":null,\"relDataElmeFlag\":\"0\",\"relCleanFlag\":\"0\",\"relAuditFlag\":\"0\",\"description\":null,\"validFlag\":true,\"delFlag\":false,\"elementId\":[],\"relDataElmeName\":null,\"sensitiveLevelName\":null,\"dataElemCodeName\":null,\"cleanRuleList\":null,\"updatorId\":1},{\"creatorId\":1,\"createBy\":\"千桐科技\",\"createTime\":\"2025-05-23 17:42:19\",\"updateBy\":\"千桐科技\",\"updateTime\":\"2025-05-23 17:42:19\",\"remark\":null,\"id\":727,\"assetId\":173,\"columnName\":\"REMARK\",\"columnComment\":\"备注\",\"columnType\":\"VARCHAR\",\"columnLength\":512,\"columnScale\":0,\"nullableFlag\":\"1\",\"pkFlag\":\"0\",\"defaultValue\":null,\"dataElemCodeFlag\":\"0\",\"dataElemCodeId\":null,\"sensitiveLevelId\":null,\"relDataElmeFlag\":\"0\",\"relCleanFlag\":\"0\",\"relAuditFlag\":\"0\",\"description\":null,\"validFlag\":true,\"delFlag\":false,\"elementId\":[],\"relDataElmeName\":null,\"sensitiveLevelName\":null,\"dataElemCodeName\":null,\"cleanRuleList\":null,\"updatorId\":1}],\"headerJson\":null}', NULL, '[{\"paramName\":\"ID\",\"paramComment\":\"ID\",\"nullable\":\"0\",\"creatorId\":1,\"createBy\":\"千桐科技\",\"createTime\":\"2025-05-23 17:42:18\",\"updateBy\":\"千桐科技\",\"updateTime\":\"2025-05-23 17:42:18\",\"remark\":null,\"id\":706,\"assetId\":173,\"columnName\":\"ID\",\"columnComment\":\"ID\",\"columnType\":\"INT\",\"columnLength\":4,\"columnScale\":0,\"nullableFlag\":\"0\",\"pkFlag\":\"1\",\"defaultValue\":null,\"dataElemCodeFlag\":\"0\",\"dataElemCodeId\":null,\"sensitiveLevelId\":null,\"relDataElmeFlag\":\"0\",\"relCleanFlag\":\"0\",\"relAuditFlag\":\"0\",\"description\":null,\"validFlag\":true,\"delFlag\":false,\"elementId\":[],\"relDataElmeName\":null,\"sensitiveLevelName\":null,\"dataElemCodeName\":null,\"cleanRuleList\":null,\"updatorId\":1,\"paramValue\":\"1\"},{\"paramName\":\"SENDER_ID\",\"paramComment\":\"发送人\",\"nullable\":\"1\",\"creatorId\":1,\"createBy\":\"千桐科技\",\"createTime\":\"2025-05-23 17:42:18\",\"updateBy\":\"千桐科技\",\"updateTime\":\"2025-05-23 17:42:18\",\"remark\":null,\"id\":707,\"assetId\":173,\"columnName\":\"SENDER_ID\",\"columnComment\":\"发送人\",\"columnType\":\"BIGINT\",\"columnLength\":8,\"columnScale\":0,\"nullableFlag\":\"1\",\"pkFlag\":\"0\",\"defaultValue\":null,\"dataElemCodeFlag\":\"0\",\"dataElemCodeId\":null,\"sensitiveLevelId\":null,\"relDataElmeFlag\":\"0\",\"relCleanFlag\":\"0\",\"relAuditFlag\":\"0\",\"description\":null,\"validFlag\":true,\"delFlag\":false,\"elementId\":[],\"relDataElmeName\":null,\"sensitiveLevelName\":null,\"dataElemCodeName\":null,\"cleanRuleList\":null,\"updatorId\":1},{\"paramName\":\"RECEIVER_ID\",\"paramComment\":\"接收人\",\"nullable\":\"1\",\"creatorId\":1,\"createBy\":\"千桐科技\",\"createTime\":\"2025-05-23 17:42:18\",\"updateBy\":\"千桐科技\",\"updateTime\":\"2025-05-23 17:42:18\",\"remark\":null,\"id\":708,\"assetId\":173,\"columnName\":\"RECEIVER_ID\",\"columnComment\":\"接收人\",\"columnType\":\"BIGINT\",\"columnLength\":8,\"columnScale\":0,\"nullableFlag\":\"1\",\"pkFlag\":\"0\",\"defaultValue\":null,\"dataElemCodeFlag\":\"0\",\"dataElemCodeId\":null,\"sensitiveLevelId\":null,\"relDataElmeFlag\":\"0\",\"relCleanFlag\":\"0\",\"relAuditFlag\":\"0\",\"description\":null,\"validFlag\":true,\"delFlag\":false,\"elementId\":[],\"relDataElmeName\":null,\"sensitiveLevelName\":null,\"dataElemCodeName\":null,\"cleanRuleList\":null,\"updatorId\":1},{\"paramName\":\"TITLE\",\"paramComment\":\"消息标题\",\"nullable\":\"1\",\"creatorId\":1,\"createBy\":\"千桐科技\",\"createTime\":\"2025-05-23 17:42:18\",\"updateBy\":\"千桐科技\",\"updateTime\":\"2025-05-23 17:42:18\",\"remark\":null,\"id\":709,\"assetId\":173,\"columnName\":\"TITLE\",\"columnComment\":\"消息标题\",\"columnType\":\"VARCHAR\",\"columnLength\":128,\"columnScale\":0,\"nullableFlag\":\"0\",\"pkFlag\":\"0\",\"defaultValue\":null,\"dataElemCodeFlag\":\"0\",\"dataElemCodeId\":null,\"sensitiveLevelId\":null,\"relDataElmeFlag\":\"0\",\"relCleanFlag\":\"0\",\"relAuditFlag\":\"0\",\"description\":null,\"validFlag\":true,\"delFlag\":false,\"elementId\":[],\"relDataElmeName\":null,\"sensitiveLevelName\":null,\"dataElemCodeName\":null,\"cleanRuleList\":null,\"updatorId\":1},{\"paramName\":\"CONTENT\",\"paramComment\":\"消息模板内容\",\"nullable\":\"1\",\"creatorId\":1,\"createBy\":\"千桐科技\",\"createTime\":\"2025-05-23 17:42:18\",\"updateBy\":\"千桐科技\",\"updateTime\":\"2025-05-23 17:42:18\",\"remark\":null,\"id\":710,\"assetId\":173,\"columnName\":\"CONTENT\",\"columnComment\":\"消息模板内容\",\"columnType\":\"VARCHAR\",\"columnLength\":3072,\"columnScale\":0,\"nullableFlag\":\"0\",\"pkFlag\":\"0\",\"defaultValue\":null,\"dataElemCodeFlag\":\"0\",\"dataElemCodeId\":null,\"sensitiveLevelId\":null,\"relDataElmeFlag\":\"0\",\"relCleanFlag\":\"0\",\"relAuditFlag\":\"0\",\"description\":null,\"validFlag\":true,\"delFlag\":false,\"elementId\":[],\"relDataElmeName\":null,\"sensitiveLevelName\":null,\"dataElemCodeName\":null,\"cleanRuleList\":null,\"updatorId\":1},{\"paramName\":\"CATEGORY\",\"paramComment\":\"消息类别\",\"nullable\":\"1\",\"creatorId\":1,\"createBy\":\"千桐科技\",\"createTime\":\"2025-05-23 17:42:18\",\"updateBy\":\"千桐科技\",\"updateTime\":\"2025-05-23 17:42:18\",\"remark\":null,\"id\":711,\"assetId\":173,\"columnName\":\"CATEGORY\",\"columnComment\":\"消息类别\",\"columnType\":\"INT\",\"columnLength\":4,\"columnScale\":0,\"nullableFlag\":\"0\",\"pkFlag\":\"0\",\"defaultValue\":null,\"dataElemCodeFlag\":\"0\",\"dataElemCodeId\":null,\"sensitiveLevelId\":null,\"relDataElmeFlag\":\"0\",\"relCleanFlag\":\"0\",\"relAuditFlag\":\"0\",\"description\":null,\"validFlag\":true,\"delFlag\":false,\"elementId\":[],\"relDataElmeName\":null,\"sensitiveLevelName\":null,\"dataElemCodeName\":null,\"cleanRuleList\":null,\"updatorId\":1},{\"paramName\":\"MSG_LEVEL\",\"paramComment\":\"消息等级\",\"nullable\":\"1\",\"creatorId\":1,\"createBy\":\"千桐科技\",\"createTime\":\"2025-05-23 17:42:18\",\"updateBy\":\"千桐科技\",\"updateTime\":\"2025-05-23 17:42:18\",\"remark\":null,\"id\":712,\"assetId\":173,\"columnName\":\"MSG_LEVEL\",\"columnComment\":\"消息等级\",\"columnType\":\"INT\",\"columnLength\":4,\"columnScale\":0,\"nullableFlag\":\"0\",\"pkFlag\":\"0\",\"defaultValue\":\"0\",\"dataElemCodeFlag\":\"0\",\"dataElemCodeId\":null,\"sensitiveLevelId\":null,\"relDataElmeFlag\":\"0\",\"relCleanFlag\":\"0\",\"relAuditFlag\":\"0\",\"description\":null,\"validFlag\":true,\"delFlag\":false,\"elementId\":[],\"relDataElmeName\":null,\"sensitiveLevelName\":null,\"dataElemCodeName\":null,\"cleanRuleList\":null,\"updatorId\":1},{\"paramName\":\"MODULE\",\"paramComment\":\"消息模块\",\"nullable\":\"1\",\"creatorId\":1,\"createBy\":\"千桐科技\",\"createTime\":\"2025-05-23 17:42:18\",\"updateBy\":\"千桐科技\",\"updateTime\":\"2025-05-23 17:42:18\",\"remark\":null,\"id\":713,\"assetId\":173,\"columnName\":\"MODULE\",\"columnComment\":\"消息模块\",\"columnType\":\"INT\",\"columnLength\":4,\"columnScale\":0,\"nullableFlag\":\"0\",\"pkFlag\":\"0\",\"defaultValue\":\"0\",\"dataElemCodeFlag\":\"0\",\"dataElemCodeId\":null,\"sensitiveLevelId\":null,\"relDataElmeFlag\":\"0\",\"relCleanFlag\":\"0\",\"relAuditFlag\":\"0\",\"description\":null,\"validFlag\":true,\"delFlag\":false,\"elementId\":[],\"relDataElmeName\":null,\"sensitiveLevelName\":null,\"dataElemCodeName\":null,\"cleanRuleList\":null,\"updatorId\":1},{\"paramName\":\"ENTITY_TYPE\",\"paramComment\":\"实体类型\",\"nullable\":\"1\",\"creatorId\":1,\"createBy\":\"千桐科技\",\"createTime\":\"2025-05-23 17:42:18\",\"updateBy\":\"千桐科技\",\"updateTime\":\"2025-05-23 17:42:18\",\"remark\":null,\"id\":714,\"assetId\":173,\"columnName\":\"ENTITY_TYPE\",\"columnComment\":\"实体类型\",\"columnType\":\"INT\",\"columnLength\":4,\"columnScale\":0,\"nullableFlag\":\"1\",\"pkFlag\":\"0\",\"defaultValue\":null,\"dataElemCodeFlag\":\"0\",\"dataElemCodeId\":null,\"sensitiveLevelId\":null,\"relDataElmeFlag\":\"0\",\"relCleanFlag\":\"0\",\"relAuditFlag\":\"0\",\"description\":null,\"validFlag\":true,\"delFlag\":false,\"elementId\":[],\"relDataElmeName\":null,\"sensitiveLevelName\":null,\"dataElemCodeName\":null,\"cleanRuleList\":null,\"updatorId\":1},{\"paramName\":\"ENTITY_ID\",\"paramComment\":\"实体id\",\"nullable\":\"1\",\"creatorId\":1,\"createBy\":\"千桐科技\",\"createTime\":\"2025-05-23 17:42:18\",\"updateBy\":\"千桐科技\",\"updateTime\":\"2025-05-23 17:42:18\",\"remark\":null,\"id\":715,\"assetId\":173,\"columnName\":\"ENTITY_ID\",\"columnComment\":\"实体id\",\"columnType\":\"BIGINT\",\"columnLength\":8,\"columnScale\":0,\"nullableFlag\":\"1\",\"pkFlag\":\"0\",\"defaultValue\":null,\"dataElemCodeFlag\":\"0\",\"dataElemCodeId\":null,\"sensitiveLevelId\":null,\"relDataElmeFlag\":\"0\",\"relCleanFlag\":\"0\",\"relAuditFlag\":\"0\",\"description\":null,\"validFlag\":true,\"delFlag\":false,\"elementId\":[],\"relDataElmeName\":null,\"sensitiveLevelName\":null,\"dataElemCodeName\":null,\"cleanRuleList\":null,\"updatorId\":1},{\"paramName\":\"ENTITY_URL\",\"paramComment\":\"消息链接\",\"nullable\":\"1\",\"creatorId\":1,\"createBy\":\"千桐科技\",\"createTime\":\"2025-05-23 17:42:18\",\"updateBy\":\"千桐科技\",\"updateTime\":\"2025-05-23 17:42:18\",\"remark\":null,\"id\":716,\"assetId\":173,\"columnName\":\"ENTITY_URL\",\"columnComment\":\"消息链接\",\"columnType\":\"VARCHAR\",\"columnLength\":256,\"columnScale\":0,\"nullableFlag\":\"1\",\"pkFlag\":\"0\",\"defaultValue\":null,\"dataElemCodeFlag\":\"0\",\"dataElemCodeId\":null,\"sensitiveLevelId\":null,\"relDataElmeFlag\":\"0\",\"relCleanFlag\":\"0\",\"relAuditFlag\":\"0\",\"description\":null,\"validFlag\":true,\"delFlag\":false,\"elementId\":[],\"relDataElmeName\":null,\"sensitiveLevelName\":null,\"dataElemCodeName\":null,\"cleanRuleList\":null,\"updatorId\":1},{\"paramName\":\"HAS_READ\",\"paramComment\":\"是否已读\",\"nullable\":\"1\",\"creatorId\":1,\"createBy\":\"千桐科技\",\"createTime\":\"2025-05-23 17:42:18\",\"updateBy\":\"千桐科技\",\"updateTime\":\"2025-05-23 17:42:18\",\"remark\":null,\"id\":717,\"assetId\":173,\"columnName\":\"HAS_READ\",\"columnComment\":\"是否已读\",\"columnType\":\"TINYINT\",\"columnLength\":1,\"columnScale\":0,\"nullableFlag\":\"1\",\"pkFlag\":\"0\",\"defaultValue\":\"0\",\"dataElemCodeFlag\":\"0\",\"dataElemCodeId\":null,\"sensitiveLevelId\":null,\"relDataElmeFlag\":\"0\",\"relCleanFlag\":\"0\",\"relAuditFlag\":\"0\",\"description\":null,\"validFlag\":true,\"delFlag\":false,\"elementId\":[],\"relDataElmeName\":null,\"sensitiveLevelName\":null,\"dataElemCodeName\":null,\"cleanRuleList\":null,\"updatorId\":1},{\"paramName\":\"HAS_RETRACTION\",\"paramComment\":\"是否撤回\",\"nullable\":\"1\",\"creatorId\":1,\"createBy\":\"千桐科技\",\"createTime\":\"2025-05-23 17:42:18\",\"updateBy\":\"千桐科技\",\"updateTime\":\"2025-05-23 17:42:18\",\"remark\":null,\"id\":718,\"assetId\":173,\"columnName\":\"HAS_RETRACTION\",\"columnComment\":\"是否撤回\",\"columnType\":\"TINYINT\",\"columnLength\":1,\"columnScale\":0,\"nullableFlag\":\"1\",\"pkFlag\":\"0\",\"defaultValue\":\"0\",\"dataElemCodeFlag\":\"0\",\"dataElemCodeId\":null,\"sensitiveLevelId\":null,\"relDataElmeFlag\":\"0\",\"relCleanFlag\":\"0\",\"relAuditFlag\":\"0\",\"description\":null,\"validFlag\":true,\"delFlag\":false,\"elementId\":[],\"relDataElmeName\":null,\"sensitiveLevelName\":null,\"dataElemCodeName\":null,\"cleanRuleList\":null,\"updatorId\":1},{\"paramName\":\"VALID_FLAG\",\"paramComment\":\"是否有效;0：无效，1：有效\",\"nullable\":\"1\",\"creatorId\":1,\"createBy\":\"千桐科技\",\"createTime\":\"2025-05-23 17:42:19\",\"updateBy\":\"千桐科技\",\"updateTime\":\"2025-05-23 17:42:19\",\"remark\":null,\"id\":719,\"assetId\":173,\"columnName\":\"VALID_FLAG\",\"columnComment\":\"是否有效;0：无效，1：有效\",\"columnType\":\"TINYINT\",\"columnLength\":1,\"columnScale\":0,\"nullableFlag\":\"0\",\"pkFlag\":\"0\",\"defaultValue\":\"1\",\"dataElemCodeFlag\":\"0\",\"dataElemCodeId\":null,\"sensitiveLevelId\":null,\"relDataElmeFlag\":\"0\",\"relCleanFlag\":\"0\",\"relAuditFlag\":\"0\",\"description\":null,\"validFlag\":true,\"delFlag\":false,\"elementId\":[],\"relDataElmeName\":null,\"sensitiveLevelName\":null,\"dataElemCodeName\":null,\"cleanRuleList\":null,\"updatorId\":1},{\"paramName\":\"DEL_FLAG\",\"paramComment\":\"删除标志;1：已删除，0：未删除\",\"nullable\":\"1\",\"creatorId\":1,\"createBy\":\"千桐科技\",\"createTime\":\"2025-05-23 17:42:19\",\"updateBy\":\"千桐科技\",\"updateTime\":\"2025-05-23 17:42:19\",\"remark\":null,\"id\":720,\"assetId\":173,\"columnName\":\"DEL_FLAG\",\"columnComment\":\"删除标志;1：已删除，0：未删除\",\"columnType\":\"TINYINT\",\"columnLength\":1,\"columnScale\":0,\"nullableFlag\":\"0\",\"pkFlag\":\"0\",\"defaultValue\":\"0\",\"dataElemCodeFlag\":\"0\",\"dataElemCodeId\":null,\"sensitiveLevelId\":null,\"relDataElmeFlag\":\"0\",\"relCleanFlag\":\"0\",\"relAuditFlag\":\"0\",\"description\":null,\"validFlag\":true,\"delFlag\":false,\"elementId\":[],\"relDataElmeName\":null,\"sensitiveLevelName\":null,\"dataElemCodeName\":null,\"cleanRuleList\":null,\"updatorId\":1},{\"paramName\":\"CREATE_BY\",\"paramComment\":\"创建人\",\"nullable\":\"1\",\"creatorId\":1,\"createBy\":\"千桐科技\",\"createTime\":\"2025-05-23 17:42:19\",\"updateBy\":\"千桐科技\",\"updateTime\":\"2025-05-23 17:42:19\",\"remark\":null,\"id\":721,\"assetId\":173,\"columnName\":\"CREATE_BY\",\"columnComment\":\"创建人\",\"columnType\":\"VARCHAR\",\"columnLength\":32,\"columnScale\":0,\"nullableFlag\":\"1\",\"pkFlag\":\"0\",\"defaultValue\":null,\"dataElemCodeFlag\":\"0\",\"dataElemCodeId\":null,\"sensitiveLevelId\":null,\"relDataElmeFlag\":\"0\",\"relCleanFlag\":\"0\",\"relAuditFlag\":\"0\",\"description\":null,\"validFlag\":true,\"delFlag\":false,\"elementId\":[],\"relDataElmeName\":null,\"sensitiveLevelName\":null,\"dataElemCodeName\":null,\"cleanRuleList\":null,\"updatorId\":1},{\"paramName\":\"CREATOR_ID\",\"paramComment\":\"创建人id\",\"nullable\":\"1\",\"creatorId\":1,\"createBy\":\"千桐科技\",\"createTime\":\"2025-05-23 17:42:19\",\"updateBy\":\"千桐科技\",\"updateTime\":\"2025-05-23 17:42:19\",\"remark\":null,\"id\":722,\"assetId\":173,\"columnName\":\"CREATOR_ID\",\"columnComment\":\"创建人id\",\"columnType\":\"BIGINT\",\"columnLength\":8,\"columnScale\":0,\"nullableFlag\":\"1\",\"pkFlag\":\"0\",\"defaultValue\":null,\"dataElemCodeFlag\":\"0\",\"dataElemCodeId\":null,\"sensitiveLevelId\":null,\"relDataElmeFlag\":\"0\",\"relCleanFlag\":\"0\",\"relAuditFlag\":\"0\",\"description\":null,\"validFlag\":true,\"delFlag\":false,\"elementId\":[],\"relDataElmeName\":null,\"sensitiveLevelName\":null,\"dataElemCodeName\":null,\"cleanRuleList\":null,\"updatorId\":1},{\"paramName\":\"CREATE_TIME\",\"paramComment\":\"创建时间\",\"nullable\":\"1\",\"creatorId\":1,\"createBy\":\"千桐科技\",\"createTime\":\"2025-05-23 17:42:19\",\"updateBy\":\"千桐科技\",\"updateTime\":\"2025-05-23 17:42:19\",\"remark\":null,\"id\":723,\"assetId\":173,\"columnName\":\"CREATE_TIME\",\"columnComment\":\"创建时间\",\"columnType\":\"TIMESTAMP\",\"columnLength\":8,\"columnScale\":0,\"nullableFlag\":\"0\",\"pkFlag\":\"0\",\"defaultValue\":\"CURRENT_TIMESTAMP()\",\"dataElemCodeFlag\":\"0\",\"dataElemCodeId\":null,\"sensitiveLevelId\":null,\"relDataElmeFlag\":\"0\",\"relCleanFlag\":\"0\",\"relAuditFlag\":\"0\",\"description\":null,\"validFlag\":true,\"delFlag\":false,\"elementId\":[],\"relDataElmeName\":null,\"sensitiveLevelName\":null,\"dataElemCodeName\":null,\"cleanRuleList\":null,\"updatorId\":1},{\"paramName\":\"UPDATE_BY\",\"paramComment\":\"更新人\",\"nullable\":\"1\",\"creatorId\":1,\"createBy\":\"千桐科技\",\"createTime\":\"2025-05-23 17:42:19\",\"updateBy\":\"千桐科技\",\"updateTime\":\"2025-05-23 17:42:19\",\"remark\":null,\"id\":724,\"assetId\":173,\"columnName\":\"UPDATE_BY\",\"columnComment\":\"更新人\",\"columnType\":\"VARCHAR\",\"columnLength\":32,\"columnScale\":0,\"nullableFlag\":\"1\",\"pkFlag\":\"0\",\"defaultValue\":null,\"dataElemCodeFlag\":\"0\",\"dataElemCodeId\":null,\"sensitiveLevelId\":null,\"relDataElmeFlag\":\"0\",\"relCleanFlag\":\"0\",\"relAuditFlag\":\"0\",\"description\":null,\"validFlag\":true,\"delFlag\":false,\"elementId\":[],\"relDataElmeName\":null,\"sensitiveLevelName\":null,\"dataElemCodeName\":null,\"cleanRuleList\":null,\"updatorId\":1},{\"paramName\":\"UPDATE_TIME\",\"paramComment\":\"更新时间\",\"nullable\":\"1\",\"creatorId\":1,\"createBy\":\"千桐科技\",\"createTime\":\"2025-05-23 17:42:19\",\"updateBy\":\"千桐科技\",\"updateTime\":\"2025-05-23 17:42:19\",\"remark\":null,\"id\":725,\"assetId\":173,\"columnName\":\"UPDATE_TIME\",\"columnComment\":\"更新时间\",\"columnType\":\"TIMESTAMP\",\"columnLength\":8,\"columnScale\":0,\"nullableFlag\":\"0\",\"pkFlag\":\"0\",\"defaultValue\":\"CURRENT_TIMESTAMP()\",\"dataElemCodeFlag\":\"0\",\"dataElemCodeId\":null,\"sensitiveLevelId\":null,\"relDataElmeFlag\":\"0\",\"relCleanFlag\":\"0\",\"relAuditFlag\":\"0\",\"description\":null,\"validFlag\":true,\"delFlag\":false,\"elementId\":[],\"relDataElmeName\":null,\"sensitiveLevelName\":null,\"dataElemCodeName\":null,\"cleanRuleList\":null,\"updatorId\":1},{\"paramName\":\"UPDATER_ID\",\"paramComment\":\"更新人id\",\"nullable\":\"1\",\"creatorId\":1,\"createBy\":\"千桐科技\",\"createTime\":\"2025-05-23 17:42:19\",\"updateBy\":\"千桐科技\",\"updateTime\":\"2025-05-23 17:42:19\",\"remark\":null,\"id\":726,\"assetId\":173,\"columnName\":\"UPDATER_ID\",\"columnComment\":\"更新人id\",\"columnType\":\"BIGINT\",\"columnLength\":8,\"columnScale\":0,\"nullableFlag\":\"1\",\"pkFlag\":\"0\",\"defaultValue\":null,\"dataElemCodeFlag\":\"0\",\"dataElemCodeId\":null,\"sensitiveLevelId\":null,\"relDataElmeFlag\":\"0\",\"relCleanFlag\":\"0\",\"relAuditFlag\":\"0\",\"description\":null,\"validFlag\":true,\"delFlag\":false,\"elementId\":[],\"relDataElmeName\":null,\"sensitiveLevelName\":null,\"dataElemCodeName\":null,\"cleanRuleList\":null,\"updatorId\":1},{\"paramName\":\"REMARK\",\"paramComment\":\"备注\",\"nullable\":\"1\",\"creatorId\":1,\"createBy\":\"千桐科技\",\"createTime\":\"2025-05-23 17:42:19\",\"updateBy\":\"千桐科技\",\"updateTime\":\"2025-05-23 17:42:19\",\"remark\":null,\"id\":727,\"assetId\":173,\"columnName\":\"REMARK\",\"columnComment\":\"备注\",\"columnType\":\"VARCHAR\",\"columnLength\":512,\"columnScale\":0,\"nullableFlag\":\"1\",\"pkFlag\":\"0\",\"defaultValue\":null,\"dataElemCodeFlag\":\"0\",\"dataElemCodeId\":null,\"sensitiveLevelId\":null,\"relDataElmeFlag\":\"0\",\"relCleanFlag\":\"0\",\"relAuditFlag\":\"0\",\"description\":null,\"validFlag\":true,\"delFlag\":false,\"elementId\":[],\"relDataElmeName\":null,\"sensitiveLevelName\":null,\"dataElemCodeName\":null,\"cleanRuleList\":null,\"updatorId\":1}]', '[{\"fieldName\":\"ID\",\"fieldComment\":\"ID\",\"creatorId\":1,\"createBy\":\"千桐科技\",\"createTime\":\"2025-05-23 17:42:18\",\"updateBy\":\"千桐科技\",\"updateTime\":\"2025-05-23 17:42:18\",\"remark\":null,\"id\":706,\"assetId\":173,\"columnName\":\"ID\",\"columnComment\":\"ID\",\"columnType\":\"INT\",\"columnLength\":4,\"columnScale\":0,\"nullableFlag\":\"0\",\"pkFlag\":\"1\",\"defaultValue\":null,\"dataElemCodeFlag\":\"0\",\"dataElemCodeId\":null,\"sensitiveLevelId\":null,\"relDataElmeFlag\":\"0\",\"relCleanFlag\":\"0\",\"relAuditFlag\":\"0\",\"description\":null,\"validFlag\":true,\"delFlag\":false,\"elementId\":[],\"relDataElmeName\":null,\"sensitiveLevelName\":null,\"dataElemCodeName\":null,\"cleanRuleList\":null,\"updatorId\":1},{\"fieldName\":\"SENDER_ID\",\"fieldComment\":\"发送人\",\"creatorId\":1,\"createBy\":\"千桐科技\",\"createTime\":\"2025-05-23 17:42:18\",\"updateBy\":\"千桐科技\",\"updateTime\":\"2025-05-23 17:42:18\",\"remark\":null,\"id\":707,\"assetId\":173,\"columnName\":\"SENDER_ID\",\"columnComment\":\"发送人\",\"columnType\":\"BIGINT\",\"columnLength\":8,\"columnScale\":0,\"nullableFlag\":\"1\",\"pkFlag\":\"0\",\"defaultValue\":null,\"dataElemCodeFlag\":\"0\",\"dataElemCodeId\":null,\"sensitiveLevelId\":null,\"relDataElmeFlag\":\"0\",\"relCleanFlag\":\"0\",\"relAuditFlag\":\"0\",\"description\":null,\"validFlag\":true,\"delFlag\":false,\"elementId\":[],\"relDataElmeName\":null,\"sensitiveLevelName\":null,\"dataElemCodeName\":null,\"cleanRuleList\":null,\"updatorId\":1},{\"fieldName\":\"RECEIVER_ID\",\"fieldComment\":\"接收人\",\"creatorId\":1,\"createBy\":\"千桐科技\",\"createTime\":\"2025-05-23 17:42:18\",\"updateBy\":\"千桐科技\",\"updateTime\":\"2025-05-23 17:42:18\",\"remark\":null,\"id\":708,\"assetId\":173,\"columnName\":\"RECEIVER_ID\",\"columnComment\":\"接收人\",\"columnType\":\"BIGINT\",\"columnLength\":8,\"columnScale\":0,\"nullableFlag\":\"1\",\"pkFlag\":\"0\",\"defaultValue\":null,\"dataElemCodeFlag\":\"0\",\"dataElemCodeId\":null,\"sensitiveLevelId\":null,\"relDataElmeFlag\":\"0\",\"relCleanFlag\":\"0\",\"relAuditFlag\":\"0\",\"description\":null,\"validFlag\":true,\"delFlag\":false,\"elementId\":[],\"relDataElmeName\":null,\"sensitiveLevelName\":null,\"dataElemCodeName\":null,\"cleanRuleList\":null,\"updatorId\":1},{\"fieldName\":\"TITLE\",\"fieldComment\":\"消息标题\",\"creatorId\":1,\"createBy\":\"千桐科技\",\"createTime\":\"2025-05-23 17:42:18\",\"updateBy\":\"千桐科技\",\"updateTime\":\"2025-05-23 17:42:18\",\"remark\":null,\"id\":709,\"assetId\":173,\"columnName\":\"TITLE\",\"columnComment\":\"消息标题\",\"columnType\":\"VARCHAR\",\"columnLength\":128,\"columnScale\":0,\"nullableFlag\":\"0\",\"pkFlag\":\"0\",\"defaultValue\":null,\"dataElemCodeFlag\":\"0\",\"dataElemCodeId\":null,\"sensitiveLevelId\":null,\"relDataElmeFlag\":\"0\",\"relCleanFlag\":\"0\",\"relAuditFlag\":\"0\",\"description\":null,\"validFlag\":true,\"delFlag\":false,\"elementId\":[],\"relDataElmeName\":null,\"sensitiveLevelName\":null,\"dataElemCodeName\":null,\"cleanRuleList\":null,\"updatorId\":1},{\"fieldName\":\"CONTENT\",\"fieldComment\":\"消息模板内容\",\"creatorId\":1,\"createBy\":\"千桐科技\",\"createTime\":\"2025-05-23 17:42:18\",\"updateBy\":\"千桐科技\",\"updateTime\":\"2025-05-23 17:42:18\",\"remark\":null,\"id\":710,\"assetId\":173,\"columnName\":\"CONTENT\",\"columnComment\":\"消息模板内容\",\"columnType\":\"VARCHAR\",\"columnLength\":3072,\"columnScale\":0,\"nullableFlag\":\"0\",\"pkFlag\":\"0\",\"defaultValue\":null,\"dataElemCodeFlag\":\"0\",\"dataElemCodeId\":null,\"sensitiveLevelId\":null,\"relDataElmeFlag\":\"0\",\"relCleanFlag\":\"0\",\"relAuditFlag\":\"0\",\"description\":null,\"validFlag\":true,\"delFlag\":false,\"elementId\":[],\"relDataElmeName\":null,\"sensitiveLevelName\":null,\"dataElemCodeName\":null,\"cleanRuleList\":null,\"updatorId\":1},{\"fieldName\":\"CATEGORY\",\"fieldComment\":\"消息类别\",\"creatorId\":1,\"createBy\":\"千桐科技\",\"createTime\":\"2025-05-23 17:42:18\",\"updateBy\":\"千桐科技\",\"updateTime\":\"2025-05-23 17:42:18\",\"remark\":null,\"id\":711,\"assetId\":173,\"columnName\":\"CATEGORY\",\"columnComment\":\"消息类别\",\"columnType\":\"INT\",\"columnLength\":4,\"columnScale\":0,\"nullableFlag\":\"0\",\"pkFlag\":\"0\",\"defaultValue\":null,\"dataElemCodeFlag\":\"0\",\"dataElemCodeId\":null,\"sensitiveLevelId\":null,\"relDataElmeFlag\":\"0\",\"relCleanFlag\":\"0\",\"relAuditFlag\":\"0\",\"description\":null,\"validFlag\":true,\"delFlag\":false,\"elementId\":[],\"relDataElmeName\":null,\"sensitiveLevelName\":null,\"dataElemCodeName\":null,\"cleanRuleList\":null,\"updatorId\":1},{\"fieldName\":\"MSG_LEVEL\",\"fieldComment\":\"消息等级\",\"creatorId\":1,\"createBy\":\"千桐科技\",\"createTime\":\"2025-05-23 17:42:18\",\"updateBy\":\"千桐科技\",\"updateTime\":\"2025-05-23 17:42:18\",\"remark\":null,\"id\":712,\"assetId\":173,\"columnName\":\"MSG_LEVEL\",\"columnComment\":\"消息等级\",\"columnType\":\"INT\",\"columnLength\":4,\"columnScale\":0,\"nullableFlag\":\"0\",\"pkFlag\":\"0\",\"defaultValue\":\"0\",\"dataElemCodeFlag\":\"0\",\"dataElemCodeId\":null,\"sensitiveLevelId\":null,\"relDataElmeFlag\":\"0\",\"relCleanFlag\":\"0\",\"relAuditFlag\":\"0\",\"description\":null,\"validFlag\":true,\"delFlag\":false,\"elementId\":[],\"relDataElmeName\":null,\"sensitiveLevelName\":null,\"dataElemCodeName\":null,\"cleanRuleList\":null,\"updatorId\":1},{\"fieldName\":\"MODULE\",\"fieldComment\":\"消息模块\",\"creatorId\":1,\"createBy\":\"千桐科技\",\"createTime\":\"2025-05-23 17:42:18\",\"updateBy\":\"千桐科技\",\"updateTime\":\"2025-05-23 17:42:18\",\"remark\":null,\"id\":713,\"assetId\":173,\"columnName\":\"MODULE\",\"columnComment\":\"消息模块\",\"columnType\":\"INT\",\"columnLength\":4,\"columnScale\":0,\"nullableFlag\":\"0\",\"pkFlag\":\"0\",\"defaultValue\":\"0\",\"dataElemCodeFlag\":\"0\",\"dataElemCodeId\":null,\"sensitiveLevelId\":null,\"relDataElmeFlag\":\"0\",\"relCleanFlag\":\"0\",\"relAuditFlag\":\"0\",\"description\":null,\"validFlag\":true,\"delFlag\":false,\"elementId\":[],\"relDataElmeName\":null,\"sensitiveLevelName\":null,\"dataElemCodeName\":null,\"cleanRuleList\":null,\"updatorId\":1},{\"fieldName\":\"ENTITY_TYPE\",\"fieldComment\":\"实体类型\",\"creatorId\":1,\"createBy\":\"千桐科技\",\"createTime\":\"2025-05-23 17:42:18\",\"updateBy\":\"千桐科技\",\"updateTime\":\"2025-05-23 17:42:18\",\"remark\":null,\"id\":714,\"assetId\":173,\"columnName\":\"ENTITY_TYPE\",\"columnComment\":\"实体类型\",\"columnType\":\"INT\",\"columnLength\":4,\"columnScale\":0,\"nullableFlag\":\"1\",\"pkFlag\":\"0\",\"defaultValue\":null,\"dataElemCodeFlag\":\"0\",\"dataElemCodeId\":null,\"sensitiveLevelId\":null,\"relDataElmeFlag\":\"0\",\"relCleanFlag\":\"0\",\"relAuditFlag\":\"0\",\"description\":null,\"validFlag\":true,\"delFlag\":false,\"elementId\":[],\"relDataElmeName\":null,\"sensitiveLevelName\":null,\"dataElemCodeName\":null,\"cleanRuleList\":null,\"updatorId\":1},{\"fieldName\":\"ENTITY_ID\",\"fieldComment\":\"实体id\",\"creatorId\":1,\"createBy\":\"千桐科技\",\"createTime\":\"2025-05-23 17:42:18\",\"updateBy\":\"千桐科技\",\"updateTime\":\"2025-05-23 17:42:18\",\"remark\":null,\"id\":715,\"assetId\":173,\"columnName\":\"ENTITY_ID\",\"columnComment\":\"实体id\",\"columnType\":\"BIGINT\",\"columnLength\":8,\"columnScale\":0,\"nullableFlag\":\"1\",\"pkFlag\":\"0\",\"defaultValue\":null,\"dataElemCodeFlag\":\"0\",\"dataElemCodeId\":null,\"sensitiveLevelId\":null,\"relDataElmeFlag\":\"0\",\"relCleanFlag\":\"0\",\"relAuditFlag\":\"0\",\"description\":null,\"validFlag\":true,\"delFlag\":false,\"elementId\":[],\"relDataElmeName\":null,\"sensitiveLevelName\":null,\"dataElemCodeName\":null,\"cleanRuleList\":null,\"updatorId\":1},{\"fieldName\":\"ENTITY_URL\",\"fieldComment\":\"消息链接\",\"creatorId\":1,\"createBy\":\"千桐科技\",\"createTime\":\"2025-05-23 17:42:18\",\"updateBy\":\"千桐科技\",\"updateTime\":\"2025-05-23 17:42:18\",\"remark\":null,\"id\":716,\"assetId\":173,\"columnName\":\"ENTITY_URL\",\"columnComment\":\"消息链接\",\"columnType\":\"VARCHAR\",\"columnLength\":256,\"columnScale\":0,\"nullableFlag\":\"1\",\"pkFlag\":\"0\",\"defaultValue\":null,\"dataElemCodeFlag\":\"0\",\"dataElemCodeId\":null,\"sensitiveLevelId\":null,\"relDataElmeFlag\":\"0\",\"relCleanFlag\":\"0\",\"relAuditFlag\":\"0\",\"description\":null,\"validFlag\":true,\"delFlag\":false,\"elementId\":[],\"relDataElmeName\":null,\"sensitiveLevelName\":null,\"dataElemCodeName\":null,\"cleanRuleList\":null,\"updatorId\":1},{\"fieldName\":\"HAS_READ\",\"fieldComment\":\"是否已读\",\"creatorId\":1,\"createBy\":\"千桐科技\",\"createTime\":\"2025-05-23 17:42:18\",\"updateBy\":\"千桐科技\",\"updateTime\":\"2025-05-23 17:42:18\",\"remark\":null,\"id\":717,\"assetId\":173,\"columnName\":\"HAS_READ\",\"columnComment\":\"是否已读\",\"columnType\":\"TINYINT\",\"columnLength\":1,\"columnScale\":0,\"nullableFlag\":\"1\",\"pkFlag\":\"0\",\"defaultValue\":\"0\",\"dataElemCodeFlag\":\"0\",\"dataElemCodeId\":null,\"sensitiveLevelId\":null,\"relDataElmeFlag\":\"0\",\"relCleanFlag\":\"0\",\"relAuditFlag\":\"0\",\"description\":null,\"validFlag\":true,\"delFlag\":false,\"elementId\":[],\"relDataElmeName\":null,\"sensitiveLevelName\":null,\"dataElemCodeName\":null,\"cleanRuleList\":null,\"updatorId\":1},{\"fieldName\":\"HAS_RETRACTION\",\"fieldComment\":\"是否撤回\",\"creatorId\":1,\"createBy\":\"千桐科技\",\"createTime\":\"2025-05-23 17:42:18\",\"updateBy\":\"千桐科技\",\"updateTime\":\"2025-05-23 17:42:18\",\"remark\":null,\"id\":718,\"assetId\":173,\"columnName\":\"HAS_RETRACTION\",\"columnComment\":\"是否撤回\",\"columnType\":\"TINYINT\",\"columnLength\":1,\"columnScale\":0,\"nullableFlag\":\"1\",\"pkFlag\":\"0\",\"defaultValue\":\"0\",\"dataElemCodeFlag\":\"0\",\"dataElemCodeId\":null,\"sensitiveLevelId\":null,\"relDataElmeFlag\":\"0\",\"relCleanFlag\":\"0\",\"relAuditFlag\":\"0\",\"description\":null,\"validFlag\":true,\"delFlag\":false,\"elementId\":[],\"relDataElmeName\":null,\"sensitiveLevelName\":null,\"dataElemCodeName\":null,\"cleanRuleList\":null,\"updatorId\":1},{\"fieldName\":\"VALID_FLAG\",\"fieldComment\":\"是否有效;0：无效，1：有效\",\"creatorId\":1,\"createBy\":\"千桐科技\",\"createTime\":\"2025-05-23 17:42:19\",\"updateBy\":\"千桐科技\",\"updateTime\":\"2025-05-23 17:42:19\",\"remark\":null,\"id\":719,\"assetId\":173,\"columnName\":\"VALID_FLAG\",\"columnComment\":\"是否有效;0：无效，1：有效\",\"columnType\":\"TINYINT\",\"columnLength\":1,\"columnScale\":0,\"nullableFlag\":\"0\",\"pkFlag\":\"0\",\"defaultValue\":\"1\",\"dataElemCodeFlag\":\"0\",\"dataElemCodeId\":null,\"sensitiveLevelId\":null,\"relDataElmeFlag\":\"0\",\"relCleanFlag\":\"0\",\"relAuditFlag\":\"0\",\"description\":null,\"validFlag\":true,\"delFlag\":false,\"elementId\":[],\"relDataElmeName\":null,\"sensitiveLevelName\":null,\"dataElemCodeName\":null,\"cleanRuleList\":null,\"updatorId\":1},{\"fieldName\":\"DEL_FLAG\",\"fieldComment\":\"删除标志;1：已删除，0：未删除\",\"creatorId\":1,\"createBy\":\"千桐科技\",\"createTime\":\"2025-05-23 17:42:19\",\"updateBy\":\"千桐科技\",\"updateTime\":\"2025-05-23 17:42:19\",\"remark\":null,\"id\":720,\"assetId\":173,\"columnName\":\"DEL_FLAG\",\"columnComment\":\"删除标志;1：已删除，0：未删除\",\"columnType\":\"TINYINT\",\"columnLength\":1,\"columnScale\":0,\"nullableFlag\":\"0\",\"pkFlag\":\"0\",\"defaultValue\":\"0\",\"dataElemCodeFlag\":\"0\",\"dataElemCodeId\":null,\"sensitiveLevelId\":null,\"relDataElmeFlag\":\"0\",\"relCleanFlag\":\"0\",\"relAuditFlag\":\"0\",\"description\":null,\"validFlag\":true,\"delFlag\":false,\"elementId\":[],\"relDataElmeName\":null,\"sensitiveLevelName\":null,\"dataElemCodeName\":null,\"cleanRuleList\":null,\"updatorId\":1},{\"fieldName\":\"CREATE_BY\",\"fieldComment\":\"创建人\",\"creatorId\":1,\"createBy\":\"千桐科技\",\"createTime\":\"2025-05-23 17:42:19\",\"updateBy\":\"千桐科技\",\"updateTime\":\"2025-05-23 17:42:19\",\"remark\":null,\"id\":721,\"assetId\":173,\"columnName\":\"CREATE_BY\",\"columnComment\":\"创建人\",\"columnType\":\"VARCHAR\",\"columnLength\":32,\"columnScale\":0,\"nullableFlag\":\"1\",\"pkFlag\":\"0\",\"defaultValue\":null,\"dataElemCodeFlag\":\"0\",\"dataElemCodeId\":null,\"sensitiveLevelId\":null,\"relDataElmeFlag\":\"0\",\"relCleanFlag\":\"0\",\"relAuditFlag\":\"0\",\"description\":null,\"validFlag\":true,\"delFlag\":false,\"elementId\":[],\"relDataElmeName\":null,\"sensitiveLevelName\":null,\"dataElemCodeName\":null,\"cleanRuleList\":null,\"updatorId\":1},{\"fieldName\":\"CREATOR_ID\",\"fieldComment\":\"创建人id\",\"creatorId\":1,\"createBy\":\"千桐科技\",\"createTime\":\"2025-05-23 17:42:19\",\"updateBy\":\"千桐科技\",\"updateTime\":\"2025-05-23 17:42:19\",\"remark\":null,\"id\":722,\"assetId\":173,\"columnName\":\"CREATOR_ID\",\"columnComment\":\"创建人id\",\"columnType\":\"BIGINT\",\"columnLength\":8,\"columnScale\":0,\"nullableFlag\":\"1\",\"pkFlag\":\"0\",\"defaultValue\":null,\"dataElemCodeFlag\":\"0\",\"dataElemCodeId\":null,\"sensitiveLevelId\":null,\"relDataElmeFlag\":\"0\",\"relCleanFlag\":\"0\",\"relAuditFlag\":\"0\",\"description\":null,\"validFlag\":true,\"delFlag\":false,\"elementId\":[],\"relDataElmeName\":null,\"sensitiveLevelName\":null,\"dataElemCodeName\":null,\"cleanRuleList\":null,\"updatorId\":1},{\"fieldName\":\"CREATE_TIME\",\"fieldComment\":\"创建时间\",\"creatorId\":1,\"createBy\":\"千桐科技\",\"createTime\":\"2025-05-23 17:42:19\",\"updateBy\":\"千桐科技\",\"updateTime\":\"2025-05-23 17:42:19\",\"remark\":null,\"id\":723,\"assetId\":173,\"columnName\":\"CREATE_TIME\",\"columnComment\":\"创建时间\",\"columnType\":\"TIMESTAMP\",\"columnLength\":8,\"columnScale\":0,\"nullableFlag\":\"0\",\"pkFlag\":\"0\",\"defaultValue\":\"CURRENT_TIMESTAMP()\",\"dataElemCodeFlag\":\"0\",\"dataElemCodeId\":null,\"sensitiveLevelId\":null,\"relDataElmeFlag\":\"0\",\"relCleanFlag\":\"0\",\"relAuditFlag\":\"0\",\"description\":null,\"validFlag\":true,\"delFlag\":false,\"elementId\":[],\"relDataElmeName\":null,\"sensitiveLevelName\":null,\"dataElemCodeName\":null,\"cleanRuleList\":null,\"updatorId\":1},{\"fieldName\":\"UPDATE_BY\",\"fieldComment\":\"更新人\",\"creatorId\":1,\"createBy\":\"千桐科技\",\"createTime\":\"2025-05-23 17:42:19\",\"updateBy\":\"千桐科技\",\"updateTime\":\"2025-05-23 17:42:19\",\"remark\":null,\"id\":724,\"assetId\":173,\"columnName\":\"UPDATE_BY\",\"columnComment\":\"更新人\",\"columnType\":\"VARCHAR\",\"columnLength\":32,\"columnScale\":0,\"nullableFlag\":\"1\",\"pkFlag\":\"0\",\"defaultValue\":null,\"dataElemCodeFlag\":\"0\",\"dataElemCodeId\":null,\"sensitiveLevelId\":null,\"relDataElmeFlag\":\"0\",\"relCleanFlag\":\"0\",\"relAuditFlag\":\"0\",\"description\":null,\"validFlag\":true,\"delFlag\":false,\"elementId\":[],\"relDataElmeName\":null,\"sensitiveLevelName\":null,\"dataElemCodeName\":null,\"cleanRuleList\":null,\"updatorId\":1},{\"fieldName\":\"UPDATE_TIME\",\"fieldComment\":\"更新时间\",\"creatorId\":1,\"createBy\":\"千桐科技\",\"createTime\":\"2025-05-23 17:42:19\",\"updateBy\":\"千桐科技\",\"updateTime\":\"2025-05-23 17:42:19\",\"remark\":null,\"id\":725,\"assetId\":173,\"columnName\":\"UPDATE_TIME\",\"columnComment\":\"更新时间\",\"columnType\":\"TIMESTAMP\",\"columnLength\":8,\"columnScale\":0,\"nullableFlag\":\"0\",\"pkFlag\":\"0\",\"defaultValue\":\"CURRENT_TIMESTAMP()\",\"dataElemCodeFlag\":\"0\",\"dataElemCodeId\":null,\"sensitiveLevelId\":null,\"relDataElmeFlag\":\"0\",\"relCleanFlag\":\"0\",\"relAuditFlag\":\"0\",\"description\":null,\"validFlag\":true,\"delFlag\":false,\"elementId\":[],\"relDataElmeName\":null,\"sensitiveLevelName\":null,\"dataElemCodeName\":null,\"cleanRuleList\":null,\"updatorId\":1},{\"fieldName\":\"UPDATER_ID\",\"fieldComment\":\"更新人id\",\"creatorId\":1,\"createBy\":\"千桐科技\",\"createTime\":\"2025-05-23 17:42:19\",\"updateBy\":\"千桐科技\",\"updateTime\":\"2025-05-23 17:42:19\",\"remark\":null,\"id\":726,\"assetId\":173,\"columnName\":\"UPDATER_ID\",\"columnComment\":\"更新人id\",\"columnType\":\"BIGINT\",\"columnLength\":8,\"columnScale\":0,\"nullableFlag\":\"1\",\"pkFlag\":\"0\",\"defaultValue\":null,\"dataElemCodeFlag\":\"0\",\"dataElemCodeId\":null,\"sensitiveLevelId\":null,\"relDataElmeFlag\":\"0\",\"relCleanFlag\":\"0\",\"relAuditFlag\":\"0\",\"description\":null,\"validFlag\":true,\"delFlag\":false,\"elementId\":[],\"relDataElmeName\":null,\"sensitiveLevelName\":null,\"dataElemCodeName\":null,\"cleanRuleList\":null,\"updatorId\":1},{\"fieldName\":\"REMARK\",\"fieldComment\":\"备注\",\"creatorId\":1,\"createBy\":\"千桐科技\",\"createTime\":\"2025-05-23 17:42:19\",\"updateBy\":\"千桐科技\",\"updateTime\":\"2025-05-23 17:42:19\",\"remark\":null,\"id\":727,\"assetId\":173,\"columnName\":\"REMARK\",\"columnComment\":\"备注\",\"columnType\":\"VARCHAR\",\"columnLength\":512,\"columnScale\":0,\"nullableFlag\":\"1\",\"pkFlag\":\"0\",\"defaultValue\":null,\"dataElemCodeFlag\":\"0\",\"dataElemCodeId\":null,\"sensitiveLevelId\":null,\"relDataElmeFlag\":\"0\",\"relCleanFlag\":\"0\",\"relAuditFlag\":\"0\",\"description\":null,\"validFlag\":true,\"delFlag\":false,\"elementId\":[],\"relDataElmeName\":null,\"sensitiveLevelName\":null,\"dataElemCodeName\":null,\"cleanRuleList\":null,\"updatorId\":1}]', NULL, '0', '1', '0', 'qData', 1, '2025-05-24 10:47:25', 'qData', 1, '2025-05-24 10:51:37', NULL, NULL, NULL, 'null');
INSERT INTO `ds_api` VALUES (2, 64, 'A01', '示例-SQL形式', 'v0.0.1', '/sql/user/list', '1', '2', '3', NULL, '{\"apiServiceType\":\"2\",\"reqParams\":[{\"paramId\":null,\"paramName\":\"id\",\"nullable\":\"0\",\"paramComment\":null,\"whereType\":null,\"paramType\":null,\"exampleValue\":null,\"defaultValue\":null,\"reqParamlist\":null},{\"paramId\":null,\"paramName\":\"name\",\"nullable\":\"0\",\"paramComment\":null,\"whereType\":null,\"paramType\":null,\"exampleValue\":null,\"defaultValue\":null,\"reqParamlist\":null}],\"resParams\":[{\"fieldId\":null,\"fieldName\":\"ID\",\"fieldComment\":\"ID\",\"dataType\":\"BIGINT\",\"exampleValue\":null,\"fieldAliasName\":\"\",\"resParamList\":null},{\"fieldId\":null,\"fieldName\":\"CAT_ID\",\"fieldComment\":\"类目id\",\"dataType\":\"BIGINT\",\"exampleValue\":null,\"fieldAliasName\":\"\",\"resParamList\":null},{\"fieldId\":null,\"fieldName\":\"CAT_CODE\",\"fieldComment\":\"类目编码\",\"dataType\":\"VARCHAR2\",\"exampleValue\":null,\"fieldAliasName\":\"\",\"resParamList\":null},{\"fieldId\":null,\"fieldName\":\"NAME\",\"fieldComment\":\"API服务名称\",\"dataType\":\"VARCHAR2\",\"exampleValue\":null,\"fieldAliasName\":\"\",\"resParamList\":null},{\"fieldId\":null,\"fieldName\":\"API_VERSION\",\"fieldComment\":\"API版本\",\"dataType\":\"VARCHAR2\",\"exampleValue\":null,\"fieldAliasName\":\"\",\"resParamList\":null},{\"fieldId\":null,\"fieldName\":\"API_URL\",\"fieldComment\":\"API路径\",\"dataType\":\"VARCHAR2\",\"exampleValue\":null,\"fieldAliasName\":\"\",\"resParamList\":null},{\"fieldId\":null,\"fieldName\":\"REQ_METHOD\",\"fieldComment\":\"请求方式;1：get，2：post\",\"dataType\":\"CHAR\",\"exampleValue\":null,\"fieldAliasName\":\"\",\"resParamList\":null},{\"fieldId\":null,\"fieldName\":\"API_SERVICE_TYPE\",\"fieldComment\":\"服务提供类型;1：单表向导式，2：SQL脚本式，3：三方转发\",\"dataType\":\"CHAR\",\"exampleValue\":null,\"fieldAliasName\":\"\",\"resParamList\":null},{\"fieldId\":null,\"fieldName\":\"RES_DATA_TYPE\",\"fieldComment\":\"返回结果类型;1：详情，2：列表，3：分页\",\"dataType\":\"CHAR\",\"exampleValue\":null,\"fieldAliasName\":\"\",\"resParamList\":null},{\"fieldId\":null,\"fieldName\":\"DENY_IP\",\"fieldComment\":\"IP黑名单多个，隔开\",\"dataType\":\"TEXT\",\"exampleValue\":null,\"fieldAliasName\":\"\",\"resParamList\":null},{\"fieldId\":null,\"fieldName\":\"CONFIG_JSON\",\"fieldComment\":\"执行配置JSON\",\"dataType\":\"TEXT\",\"exampleValue\":null,\"fieldAliasName\":\"\",\"resParamList\":null},{\"fieldId\":null,\"fieldName\":\"LIMIT_JSON\",\"fieldComment\":\"限流配置JSON\",\"dataType\":\"VARCHAR2\",\"exampleValue\":null,\"fieldAliasName\":\"\",\"resParamList\":null},{\"fieldId\":null,\"fieldName\":\"REQ_PARAMS\",\"fieldComment\":\"请求参数\",\"dataType\":\"TEXT\",\"exampleValue\":null,\"fieldAliasName\":\"\",\"resParamList\":null},{\"fieldId\":null,\"fieldName\":\"RES_PARAMS\",\"fieldComment\":\"返回参数\",\"dataType\":\"TEXT\",\"exampleValue\":null,\"fieldAliasName\":\"\",\"resParamList\":null},{\"fieldId\":null,\"fieldName\":\"DESCRIPTION\",\"fieldComment\":\"描述\",\"dataType\":\"VARCHAR2\",\"exampleValue\":null,\"fieldAliasName\":\"\",\"resParamList\":null},{\"fieldId\":null,\"fieldName\":\"STATUS\",\"fieldComment\":\"状态;0：不启用，1启用\",\"dataType\":\"CHAR\",\"exampleValue\":null,\"fieldAliasName\":\"\",\"resParamList\":null},{\"fieldId\":null,\"fieldName\":\"VALID_FLAG\",\"fieldComment\":\"是否有效;0：无效，1：有效\",\"dataType\":\"VARCHAR2\",\"exampleValue\":null,\"fieldAliasName\":\"\",\"resParamList\":null},{\"fieldId\":null,\"fieldName\":\"DEL_FLAG\",\"fieldComment\":\"删除标志;1：已删除，0：未删除\",\"dataType\":\"VARCHAR2\",\"exampleValue\":null,\"fieldAliasName\":\"\",\"resParamList\":null},{\"fieldId\":null,\"fieldName\":\"CREATE_BY\",\"fieldComment\":\"创建人\",\"dataType\":\"VARCHAR2\",\"exampleValue\":null,\"fieldAliasName\":\"\",\"resParamList\":null},{\"fieldId\":null,\"fieldName\":\"CREATOR_ID\",\"fieldComment\":\"创建人id\",\"dataType\":\"BIGINT\",\"exampleValue\":null,\"fieldAliasName\":\"\",\"resParamList\":null},{\"fieldId\":null,\"fieldName\":\"CREATE_TIME\",\"fieldComment\":\"创建时间\",\"dataType\":\"DATETIME\",\"exampleValue\":null,\"fieldAliasName\":\"\",\"resParamList\":null},{\"fieldId\":null,\"fieldName\":\"UPDATE_BY\",\"fieldComment\":\"更新人\",\"dataType\":\"VARCHAR2\",\"exampleValue\":null,\"fieldAliasName\":\"\",\"resParamList\":null},{\"fieldId\":null,\"fieldName\":\"UPDATER_ID\",\"fieldComment\":\"更新人id\",\"dataType\":\"BIGINT\",\"exampleValue\":null,\"fieldAliasName\":\"\",\"resParamList\":null},{\"fieldId\":null,\"fieldName\":\"UPDATE_TIME\",\"fieldComment\":\"更新时间\",\"dataType\":\"DATETIME\",\"exampleValue\":null,\"fieldAliasName\":\"\",\"resParamList\":null},{\"fieldId\":null,\"fieldName\":\"REMARK\",\"fieldComment\":\"备注\",\"dataType\":\"VARCHAR2\",\"exampleValue\":null,\"fieldAliasName\":\"\",\"resParamList\":null},{\"fieldId\":null,\"fieldName\":\"TRANSMIT_TYPE\",\"fieldComment\":\"转发类型;1:API 2:地理空间数据\",\"dataType\":\"CHAR\",\"exampleValue\":null,\"fieldAliasName\":\"\",\"resParamList\":null},{\"fieldId\":null,\"fieldName\":\"API_ID\",\"fieldComment\":\"API id\",\"dataType\":\"BIGINT\",\"exampleValue\":null,\"fieldAliasName\":\"\",\"resParamList\":null},{\"fieldId\":null,\"fieldName\":\"HEADER_JSON\",\"fieldComment\":\"Header配置json\",\"dataType\":\"TEXT\",\"exampleValue\":null,\"fieldAliasName\":\"\",\"resParamList\":null}],\"headerJson\":[],\"table\":{},\"transmitType\":\"\",\"apiId\":\"\",\"categoryAssetList\":\"\",\"sqlText\":\"select * from QDATA_OPEN.DS_API  where del_flag = 1   ${and id = :id and name = :name}\",\"sourceId\":48,\"filteredTableOptions\":[{\"creatorId\":1,\"createBy\":\"千桐科技\",\"createTime\":\"2025-05-23 17:42:18\",\"updateBy\":\"千桐科技\",\"updateTime\":\"2025-05-23 17:42:18\",\"remark\":null,\"id\":173,\"name\":\"示例-消息\",\"type\":\"1\",\"catCode\":\"A01\",\"catName\":null,\"sourceType\":null,\"daAssetThemeRelList\":null,\"datasourceId\":48,\"datasourceName\":null,\"datasourceIp\":null,\"datasourceType\":null,\"tableName\":\"MESSAGE\",\"tableComment\":\"消息\",\"dataCount\":0,\"fieldCount\":22,\"source\":\"3\",\"status\":\"1\",\"description\":\"\",\"validFlag\":true,\"delFlag\":false,\"projectId\":null,\"projectCode\":null,\"updatorId\":1}],\"lastSqlText\":\"select * from QDATA_OPEN.DS_API  where del_flag = 1   ${and id = :id and name = :name}\"}', NULL, '[{\"paramId\":null,\"paramName\":\"id\",\"nullable\":\"0\",\"paramComment\":null,\"whereType\":null,\"paramType\":null,\"exampleValue\":null,\"defaultValue\":null,\"reqParamlist\":null},{\"paramId\":null,\"paramName\":\"name\",\"nullable\":\"0\",\"paramComment\":null,\"whereType\":null,\"paramType\":null,\"exampleValue\":null,\"defaultValue\":null,\"reqParamlist\":null}]', '[{\"fieldId\":null,\"fieldName\":\"ID\",\"fieldComment\":\"ID\",\"dataType\":\"BIGINT\",\"exampleValue\":null,\"fieldAliasName\":\"\",\"resParamList\":null},{\"fieldId\":null,\"fieldName\":\"CAT_ID\",\"fieldComment\":\"类目id\",\"dataType\":\"BIGINT\",\"exampleValue\":null,\"fieldAliasName\":\"\",\"resParamList\":null},{\"fieldId\":null,\"fieldName\":\"CAT_CODE\",\"fieldComment\":\"类目编码\",\"dataType\":\"VARCHAR2\",\"exampleValue\":null,\"fieldAliasName\":\"\",\"resParamList\":null},{\"fieldId\":null,\"fieldName\":\"NAME\",\"fieldComment\":\"API服务名称\",\"dataType\":\"VARCHAR2\",\"exampleValue\":null,\"fieldAliasName\":\"\",\"resParamList\":null},{\"fieldId\":null,\"fieldName\":\"API_VERSION\",\"fieldComment\":\"API版本\",\"dataType\":\"VARCHAR2\",\"exampleValue\":null,\"fieldAliasName\":\"\",\"resParamList\":null},{\"fieldId\":null,\"fieldName\":\"API_URL\",\"fieldComment\":\"API路径\",\"dataType\":\"VARCHAR2\",\"exampleValue\":null,\"fieldAliasName\":\"\",\"resParamList\":null},{\"fieldId\":null,\"fieldName\":\"REQ_METHOD\",\"fieldComment\":\"请求方式;1：get，2：post\",\"dataType\":\"CHAR\",\"exampleValue\":null,\"fieldAliasName\":\"\",\"resParamList\":null},{\"fieldId\":null,\"fieldName\":\"API_SERVICE_TYPE\",\"fieldComment\":\"服务提供类型;1：单表向导式，2：SQL脚本式，3：三方转发\",\"dataType\":\"CHAR\",\"exampleValue\":null,\"fieldAliasName\":\"\",\"resParamList\":null},{\"fieldId\":null,\"fieldName\":\"RES_DATA_TYPE\",\"fieldComment\":\"返回结果类型;1：详情，2：列表，3：分页\",\"dataType\":\"CHAR\",\"exampleValue\":null,\"fieldAliasName\":\"\",\"resParamList\":null},{\"fieldId\":null,\"fieldName\":\"DENY_IP\",\"fieldComment\":\"IP黑名单多个，隔开\",\"dataType\":\"TEXT\",\"exampleValue\":null,\"fieldAliasName\":\"\",\"resParamList\":null},{\"fieldId\":null,\"fieldName\":\"CONFIG_JSON\",\"fieldComment\":\"执行配置JSON\",\"dataType\":\"TEXT\",\"exampleValue\":null,\"fieldAliasName\":\"\",\"resParamList\":null},{\"fieldId\":null,\"fieldName\":\"LIMIT_JSON\",\"fieldComment\":\"限流配置JSON\",\"dataType\":\"VARCHAR2\",\"exampleValue\":null,\"fieldAliasName\":\"\",\"resParamList\":null},{\"fieldId\":null,\"fieldName\":\"REQ_PARAMS\",\"fieldComment\":\"请求参数\",\"dataType\":\"TEXT\",\"exampleValue\":null,\"fieldAliasName\":\"\",\"resParamList\":null},{\"fieldId\":null,\"fieldName\":\"RES_PARAMS\",\"fieldComment\":\"返回参数\",\"dataType\":\"TEXT\",\"exampleValue\":null,\"fieldAliasName\":\"\",\"resParamList\":null},{\"fieldId\":null,\"fieldName\":\"DESCRIPTION\",\"fieldComment\":\"描述\",\"dataType\":\"VARCHAR2\",\"exampleValue\":null,\"fieldAliasName\":\"\",\"resParamList\":null},{\"fieldId\":null,\"fieldName\":\"STATUS\",\"fieldComment\":\"状态;0：不启用，1启用\",\"dataType\":\"CHAR\",\"exampleValue\":null,\"fieldAliasName\":\"\",\"resParamList\":null},{\"fieldId\":null,\"fieldName\":\"VALID_FLAG\",\"fieldComment\":\"是否有效;0：无效，1：有效\",\"dataType\":\"VARCHAR2\",\"exampleValue\":null,\"fieldAliasName\":\"\",\"resParamList\":null},{\"fieldId\":null,\"fieldName\":\"DEL_FLAG\",\"fieldComment\":\"删除标志;1：已删除，0：未删除\",\"dataType\":\"VARCHAR2\",\"exampleValue\":null,\"fieldAliasName\":\"\",\"resParamList\":null},{\"fieldId\":null,\"fieldName\":\"CREATE_BY\",\"fieldComment\":\"创建人\",\"dataType\":\"VARCHAR2\",\"exampleValue\":null,\"fieldAliasName\":\"\",\"resParamList\":null},{\"fieldId\":null,\"fieldName\":\"CREATOR_ID\",\"fieldComment\":\"创建人id\",\"dataType\":\"BIGINT\",\"exampleValue\":null,\"fieldAliasName\":\"\",\"resParamList\":null},{\"fieldId\":null,\"fieldName\":\"CREATE_TIME\",\"fieldComment\":\"创建时间\",\"dataType\":\"DATETIME\",\"exampleValue\":null,\"fieldAliasName\":\"\",\"resParamList\":null},{\"fieldId\":null,\"fieldName\":\"UPDATE_BY\",\"fieldComment\":\"更新人\",\"dataType\":\"VARCHAR2\",\"exampleValue\":null,\"fieldAliasName\":\"\",\"resParamList\":null},{\"fieldId\":null,\"fieldName\":\"UPDATER_ID\",\"fieldComment\":\"更新人id\",\"dataType\":\"BIGINT\",\"exampleValue\":null,\"fieldAliasName\":\"\",\"resParamList\":null},{\"fieldId\":null,\"fieldName\":\"UPDATE_TIME\",\"fieldComment\":\"更新时间\",\"dataType\":\"DATETIME\",\"exampleValue\":null,\"fieldAliasName\":\"\",\"resParamList\":null},{\"fieldId\":null,\"fieldName\":\"REMARK\",\"fieldComment\":\"备注\",\"dataType\":\"VARCHAR2\",\"exampleValue\":null,\"fieldAliasName\":\"\",\"resParamList\":null},{\"fieldId\":null,\"fieldName\":\"TRANSMIT_TYPE\",\"fieldComment\":\"转发类型;1:API 2:地理空间数据\",\"dataType\":\"CHAR\",\"exampleValue\":null,\"fieldAliasName\":\"\",\"resParamList\":null},{\"fieldId\":null,\"fieldName\":\"API_ID\",\"fieldComment\":\"API id\",\"dataType\":\"BIGINT\",\"exampleValue\":null,\"fieldAliasName\":\"\",\"resParamList\":null},{\"fieldId\":null,\"fieldName\":\"HEADER_JSON\",\"fieldComment\":\"Header配置json\",\"dataType\":\"TEXT\",\"exampleValue\":null,\"fieldAliasName\":\"\",\"resParamList\":null}]', NULL, '0', '1', '0', 'qData', 1, '2025-05-24 10:49:41', 'qData', 1, '2025-05-24 15:34:34', NULL, '', NULL, '[]');

-- ----------------------------
-- Table structure for ds_api_log
-- ----------------------------
DROP TABLE IF EXISTS `ds_api_log`;
CREATE TABLE `ds_api_log`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `api_id` bigint(20) NOT NULL COMMENT '调用API服务Id',
  `caller_id` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '调用者id',
  `caller_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '调用者',
  `caller_ip` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '调用者ip',
  `caller_url` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '调用url',
  `caller_params` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '调用参数',
  `caller_start_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '调用开始时间',
  `caller_end_date` datetime NULL DEFAULT NULL COMMENT '调用结束时间',
  `caller_size` int(11) NULL DEFAULT 0 COMMENT '调用数据量',
  `caller_time` int(11) NULL DEFAULT 0 COMMENT '调用耗时(毫秒)',
  `msg` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '信息记录',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '1' COMMENT '状态;0：失败，1：成功',
  `valid_flag` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '1' COMMENT '是否有效;0：无效，1：有效',
  `del_flag` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '删除标志;1：已删除，0：未删除',
  `create_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `creator_id` bigint(20) NULL DEFAULT NULL COMMENT '创建人id',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `updater_id` bigint(20) NULL DEFAULT NULL COMMENT '更新人id',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `field_parameters` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '返回参数',
  `cat_id` bigint(20) NOT NULL COMMENT '类目id',
  `cat_code` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '类目编码',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `ID_90116884495900`(`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'API服务调用日志' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ds_api_log
-- ----------------------------

-- ----------------------------
-- Table structure for gen_table
-- ----------------------------
DROP TABLE IF EXISTS `gen_table`;
CREATE TABLE `gen_table`  (
  `table_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `table_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '表名称',
  `table_comment` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '表描述',
  `sub_table_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '关联子表的表名',
  `sub_table_fk_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '子表关联的外键名',
  `class_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '实体类名称',
  `tpl_category` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT 'crud' COMMENT '使用的模板（crud单表操作 tree树表操作）',
  `tpl_web_type` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '前端模板类型（element-ui模版 element-plus模版）',
  `package_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '生成包路径',
  `module_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '生成模块名',
  `business_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '生成业务名',
  `function_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '生成功能名',
  `function_author` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '生成功能作者',
  `gen_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '生成代码方式（0zip压缩包 1自定义路径）',
  `gen_path` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '/' COMMENT '生成路径（不填默认项目路径）',
  `options` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '其它生成选项',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`table_id`) USING BTREE,
  UNIQUE INDEX `TABLE_ID_90117249456300`(`table_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '代码生成业务表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of gen_table
-- ----------------------------

-- ----------------------------
-- Table structure for gen_table_column
-- ----------------------------
DROP TABLE IF EXISTS `gen_table_column`;
CREATE TABLE `gen_table_column`  (
  `column_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `table_id` bigint(20) NULL DEFAULT NULL COMMENT '归属表编号',
  `column_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '列名称',
  `column_comment` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '列描述',
  `column_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '列类型',
  `java_type` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'JAVA类型',
  `java_field` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'JAVA字段名',
  `is_pk` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '是否主键（1是）',
  `is_increment` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '是否自增（1是）',
  `is_required` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '是否必填（1是）',
  `is_insert` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '是否为插入字段（1是）',
  `is_edit` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '是否编辑字段（1是）',
  `is_list` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '是否列表字段（1是）',
  `is_query` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '是否查询字段（1是）',
  `query_type` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT 'EQ' COMMENT '查询方式（等于、不等于、大于、小于、范围）',
  `html_type` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '显示类型（文本框、文本域、下拉框、复选框、单选框、日期控件）',
  `dict_type` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '字典类型',
  `sort` int(11) NULL DEFAULT NULL COMMENT '排序',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`column_id`) USING BTREE,
  UNIQUE INDEX `COLUMN_ID_90117630446000`(`column_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '代码生成业务表字段' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of gen_table_column
-- ----------------------------

-- ----------------------------
-- Table structure for message
-- ----------------------------
DROP TABLE IF EXISTS `message`;
CREATE TABLE `message`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `sender_id` bigint(20) NULL DEFAULT NULL COMMENT '发送人',
  `receiver_id` bigint(20) NULL DEFAULT NULL COMMENT '接收人',
  `title` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '消息标题',
  `content` varchar(3072) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '消息模板内容',
  `category` int(11) NOT NULL COMMENT '消息类别',
  `msg_level` int(11) NOT NULL DEFAULT 0 COMMENT '消息等级',
  `module` int(11) NOT NULL DEFAULT 0 COMMENT '消息模块',
  `entity_type` int(11) NULL DEFAULT NULL COMMENT '实体类型',
  `entity_id` bigint(20) NULL DEFAULT NULL COMMENT '实体id',
  `entity_url` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '消息链接',
  `has_read` tinyint(4) NULL DEFAULT 0 COMMENT '是否已读',
  `has_retraction` tinyint(4) NULL DEFAULT 0 COMMENT '是否撤回',
  `valid_flag` tinyint(4) NOT NULL DEFAULT 1 COMMENT '是否有效;0：无效，1：有效',
  `del_flag` tinyint(4) NOT NULL DEFAULT 0 COMMENT '删除标志;1：已删除，0：未删除',
  `create_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `creator_id` bigint(20) NULL DEFAULT NULL COMMENT '创建人id',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `updater_id` bigint(20) NULL DEFAULT NULL COMMENT '更新人id',
  `remark` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `ID_90118031620800`(`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '消息' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of message
-- ----------------------------

-- ----------------------------
-- Table structure for message_template
-- ----------------------------
DROP TABLE IF EXISTS `message_template`;
CREATE TABLE `message_template`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `title` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '消息标题',
  `content` varchar(3072) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '消息模板内容',
  `category` int(11) NOT NULL COMMENT '消息类别',
  `msg_level` int(11) NOT NULL DEFAULT 0 COMMENT '消息等级',
  `valid_flag` tinyint(4) NOT NULL DEFAULT 1 COMMENT '是否有效;0：无效，1：有效',
  `del_flag` tinyint(4) NOT NULL DEFAULT 0 COMMENT '删除标志;1：已删除，0：未删除',
  `create_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `creator_id` bigint(20) NULL DEFAULT NULL COMMENT '创建人id',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `updater_id` bigint(20) NULL DEFAULT NULL COMMENT '更新人id',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `ID_90118424296500`(`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '消息模板' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of message_template
-- ----------------------------

-- ----------------------------
-- Table structure for qrtz_blob_triggers
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_blob_triggers`;
CREATE TABLE `qrtz_blob_triggers`  (
  `sched_name` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '调度名称',
  `trigger_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'qrtz_triggers表trigger_name的外键',
  `trigger_group` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'qrtz_triggers表trigger_group的外键',
  `blob_data` longblob NULL COMMENT '存放持久化Trigger对象',
  PRIMARY KEY (`sched_name`, `trigger_name`, `trigger_group`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'Blob类型的触发器表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of qrtz_blob_triggers
-- ----------------------------

-- ----------------------------
-- Table structure for qrtz_calendars
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_calendars`;
CREATE TABLE `qrtz_calendars`  (
  `sched_name` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '调度名称',
  `calendar_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '日历名称',
  `calendar` longblob NOT NULL COMMENT '存放持久化calendar对象',
  PRIMARY KEY (`sched_name`, `calendar_name`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '日历信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of qrtz_calendars
-- ----------------------------

-- ----------------------------
-- Table structure for qrtz_cron_triggers
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_cron_triggers`;
CREATE TABLE `qrtz_cron_triggers`  (
  `sched_name` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '调度名称',
  `trigger_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'qrtz_triggers表trigger_name的外键',
  `trigger_group` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'qrtz_triggers表trigger_group的外键',
  `cron_expression` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'cron表达式',
  `time_zone_id` varchar(80) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '时区',
  PRIMARY KEY (`sched_name`, `trigger_name`, `trigger_group`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'Cron类型的触发器表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of qrtz_cron_triggers
-- ----------------------------

-- ----------------------------
-- Table structure for qrtz_fired_triggers
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_fired_triggers`;
CREATE TABLE `qrtz_fired_triggers`  (
  `sched_name` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '调度名称',
  `entry_id` varchar(95) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '调度器实例id',
  `trigger_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'qrtz_triggers表trigger_name的外键',
  `trigger_group` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'qrtz_triggers表trigger_group的外键',
  `instance_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '调度器实例名',
  `fired_time` bigint(20) NOT NULL COMMENT '触发的时间',
  `sched_time` bigint(20) NOT NULL COMMENT '定时器制定的时间',
  `priority` int(11) NOT NULL COMMENT '优先级',
  `state` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '状态',
  `job_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '任务名称',
  `job_group` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '任务组名',
  `is_nonconcurrent` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '是否并发',
  `requests_recovery` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '是否接受恢复执行',
  PRIMARY KEY (`sched_name`, `entry_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '已触发的触发器表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of qrtz_fired_triggers
-- ----------------------------

-- ----------------------------
-- Table structure for qrtz_job_details
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_job_details`;
CREATE TABLE `qrtz_job_details`  (
  `sched_name` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '调度名称',
  `job_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '任务名称',
  `job_group` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '任务组名',
  `description` varchar(250) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '相关介绍',
  `job_class_name` varchar(250) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '执行任务类名称',
  `is_durable` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '是否持久化',
  `is_nonconcurrent` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '是否并发',
  `is_update_data` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '是否更新数据',
  `requests_recovery` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '是否接受恢复执行',
  `job_data` longblob NULL COMMENT '存放持久化job对象',
  PRIMARY KEY (`sched_name`, `job_name`, `job_group`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '任务详细信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of qrtz_job_details
-- ----------------------------

-- ----------------------------
-- Table structure for qrtz_locks
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_locks`;
CREATE TABLE `qrtz_locks`  (
  `sched_name` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '调度名称',
  `lock_name` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '悲观锁名称',
  PRIMARY KEY (`sched_name`, `lock_name`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '存储的悲观锁信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of qrtz_locks
-- ----------------------------

-- ----------------------------
-- Table structure for qrtz_paused_trigger_grps
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_paused_trigger_grps`;
CREATE TABLE `qrtz_paused_trigger_grps`  (
  `sched_name` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '调度名称',
  `trigger_group` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'qrtz_triggers表trigger_group的外键',
  PRIMARY KEY (`sched_name`, `trigger_group`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '暂停的触发器表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of qrtz_paused_trigger_grps
-- ----------------------------

-- ----------------------------
-- Table structure for qrtz_scheduler_state
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_scheduler_state`;
CREATE TABLE `qrtz_scheduler_state`  (
  `sched_name` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '调度名称',
  `instance_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '实例名称',
  `last_checkin_time` bigint(20) NOT NULL COMMENT '上次检查时间',
  `checkin_interval` bigint(20) NOT NULL COMMENT '检查间隔时间',
  PRIMARY KEY (`sched_name`, `instance_name`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '调度器状态表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of qrtz_scheduler_state
-- ----------------------------

-- ----------------------------
-- Table structure for qrtz_simple_triggers
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_simple_triggers`;
CREATE TABLE `qrtz_simple_triggers`  (
  `sched_name` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '调度名称',
  `trigger_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'qrtz_triggers表trigger_name的外键',
  `trigger_group` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'qrtz_triggers表trigger_group的外键',
  `repeat_count` bigint(20) NOT NULL COMMENT '重复的次数统计',
  `repeat_interval` bigint(20) NOT NULL COMMENT '重复的间隔时间',
  `times_triggered` bigint(20) NOT NULL COMMENT '已经触发的次数',
  PRIMARY KEY (`sched_name`, `trigger_name`, `trigger_group`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '简单触发器的信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of qrtz_simple_triggers
-- ----------------------------

-- ----------------------------
-- Table structure for qrtz_simprop_triggers
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_simprop_triggers`;
CREATE TABLE `qrtz_simprop_triggers`  (
  `sched_name` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '调度名称',
  `trigger_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'qrtz_triggers表trigger_name的外键',
  `trigger_group` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'qrtz_triggers表trigger_group的外键',
  `STR_PROP_1` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'String类型的trigger的第一个参数',
  `STR_PROP_2` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'String类型的trigger的第二个参数',
  `STR_PROP_3` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'String类型的trigger的第三个参数',
  `INT_PROP_1` int(11) NULL DEFAULT NULL COMMENT 'int类型的trigger的第一个参数',
  `INT_PROP_2` int(11) NULL DEFAULT NULL COMMENT 'int类型的trigger的第二个参数',
  `LONG_PROP_1` bigint(20) NULL DEFAULT NULL COMMENT 'long类型的trigger的第一个参数',
  `LONG_PROP_2` bigint(20) NULL DEFAULT NULL COMMENT 'long类型的trigger的第二个参数',
  `DEC_PROP_1` decimal(13, 4) NULL DEFAULT NULL COMMENT 'decimal类型的trigger的第一个参数',
  `DEC_PROP_2` decimal(13, 4) NULL DEFAULT NULL COMMENT 'decimal类型的trigger的第二个参数',
  `BOOL_PROP_1` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'Boolean类型的trigger的第一个参数',
  `BOOL_PROP_2` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'Boolean类型的trigger的第二个参数',
  PRIMARY KEY (`sched_name`, `trigger_name`, `trigger_group`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '同步机制的行锁表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of qrtz_simprop_triggers
-- ----------------------------

-- ----------------------------
-- Table structure for qrtz_triggers
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_triggers`;
CREATE TABLE `qrtz_triggers`  (
  `sched_name` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '调度名称',
  `trigger_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '触发器的名字',
  `trigger_group` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '触发器所属组的名字',
  `job_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'qrtz_job_details表job_name的外键',
  `job_group` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'qrtz_job_details表job_group的外键',
  `description` varchar(250) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '相关介绍',
  `next_fire_time` bigint(20) NULL DEFAULT NULL COMMENT '上一次触发时间（毫秒）',
  `prev_fire_time` bigint(20) NULL DEFAULT NULL COMMENT '下一次触发时间（默认为-1表示不触发）',
  `priority` int(11) NULL DEFAULT NULL COMMENT '优先级',
  `trigger_state` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '触发器状态',
  `trigger_type` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '触发器的类型',
  `start_time` bigint(20) NOT NULL COMMENT '开始时间',
  `end_time` bigint(20) NULL DEFAULT NULL COMMENT '结束时间',
  `calendar_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '日程表名称',
  `misfire_instr` smallint(6) NULL DEFAULT NULL COMMENT '补偿执行的策略',
  `job_data` longblob NULL COMMENT '存放持久化job对象',
  PRIMARY KEY (`sched_name`, `trigger_name`, `trigger_group`) USING BTREE,
  INDEX `sched_name`(`sched_name`, `job_name`, `job_group`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '触发器详细信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of qrtz_triggers
-- ----------------------------

-- ----------------------------
-- Table structure for system_config
-- ----------------------------
DROP TABLE IF EXISTS `system_config`;
CREATE TABLE `system_config`  (
  `config_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '参数主键',
  `config_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '参数名称',
  `config_key` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '参数键名',
  `config_value` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '参数键值',
  `config_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT 'N' COMMENT '系统内置（Y是 N否）',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`config_id`) USING BTREE,
  UNIQUE INDEX `CONFIG_ID_90124158453200`(`config_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '参数配置表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of system_config
-- ----------------------------
INSERT INTO `system_config` VALUES (1, '主框架页-默认皮肤样式名称', 'sys.index.skinName', 'skin-blue', 'Y', 'qData', '2024-05-06 06:12:18', 'qData', '2025-03-07 14:07:03', '蓝色 skin-blue、绿色 skin-green、紫色 skin-purple、红色 skin-red、黄色 skin-yellow');
INSERT INTO `system_config` VALUES (2, '用户管理-账号初始密码', 'sys.user.initPassword', '123456', 'Y', 'qData', '2024-05-06 06:12:18', '', NULL, '初始化密码 123456');
INSERT INTO `system_config` VALUES (3, '主框架页-侧边栏主题', 'sys.index.sideTheme', 'theme-dark', 'Y', 'qData', '2024-05-06 06:12:18', '', NULL, '深色主题theme-dark，浅色主题theme-light');
INSERT INTO `system_config` VALUES (4, '账号自助-验证码开关', 'sys.account.captchaEnabled', 'true', 'Y', 'qData', '2024-05-06 06:12:18', '', NULL, '是否开启验证码功能（true开启，false关闭）');
INSERT INTO `system_config` VALUES (5, '账号自助-是否开启用户注册功能', 'sys.account.registerUser', 'false', 'Y', 'qData', '2024-05-06 06:12:18', '', NULL, '是否开启注册用户功能（true开启，false关闭）');
INSERT INTO `system_config` VALUES (6, '用户登录-黑名单列表', 'sys.login.blackIPList', '-', 'Y', 'qData', '2024-05-06 06:12:18', 'qData', '2024-11-26 15:51:27', '设置登录IP黑名单限制，多个匹配项以;分隔，支持匹配（*通配、网段）');

-- ----------------------------
-- Table structure for system_content
-- ----------------------------
DROP TABLE IF EXISTS `system_content`;
CREATE TABLE `system_content`  (
  `id` int(11) NOT NULL COMMENT 'id',
  `sys_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '系统名称',
  `logo` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '系统logo',
  `login_logo` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '登录页面logo',
  `carousel_image` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '轮播图',
  `contact_number` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '联系电话',
  `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '电子邮箱',
  `copyright` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '版权方',
  `record_number` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备案号',
  `del_flag` int(11) NULL DEFAULT NULL COMMENT '删除标记',
  `status` int(11) NULL DEFAULT NULL COMMENT '状态',
  `create_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `creator_id` int(11) NULL DEFAULT NULL COMMENT '创建人id',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '修改人',
  `updater_id` int(11) NULL DEFAULT NULL COMMENT '修改人id',
  `update_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统配置' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of system_content
-- ----------------------------
INSERT INTO `system_content` VALUES (1, NULL, '', '', '', '400-660-8208', 'support@qiantong.tech', 'Copyright© 2025 江苏千桐科技有限公司 版权所有', '苏ICP备2022008519号-3', 0, NULL, NULL, NULL, NULL, 'qData', 1, '2025-01-03 09:40:50', NULL);

-- ----------------------------
-- Table structure for system_dept
-- ----------------------------
DROP TABLE IF EXISTS `system_dept`;
CREATE TABLE `system_dept`  (
  `dept_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '部门id',
  `parent_id` bigint(20) NULL DEFAULT 0 COMMENT '父部门id',
  `ancestors` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '祖级列表',
  `dept_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '部门名称',
  `order_num` int(11) NULL DEFAULT 0 COMMENT '显示顺序',
  `leader` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '负责人',
  `phone` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '联系电话',
  `email` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '邮箱',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '部门状态（0正常 1停用）',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`dept_id`) USING BTREE,
  UNIQUE INDEX `DEPT_ID_90021236426200`(`dept_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 855 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '部门表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of system_dept
-- ----------------------------
INSERT INTO `system_dept` VALUES (100, 0, '0', '千桐科技有限公司', 0, '千桐', '15888888888', 'qdata@qq.com', '0', '0', 'qData', '2024-05-06 06:12:17', 'qData', '2025-04-15 13:47:07');
INSERT INTO `system_dept` VALUES (101, 100, '0,100', '南京总部', 1, '千数', '15888888888', 'qdata@qq.com', '0', '0', 'qData', '2024-05-06 06:12:17', 'qData', '2025-04-15 13:47:21');
INSERT INTO `system_dept` VALUES (102, 100, '0,100', '郑州总部', 2, '千数', '15888888888', 'qdata@qq.com', '0', '0', 'qData', '2024-05-06 06:12:17', 'qData', '2025-04-15 13:47:31');
INSERT INTO `system_dept` VALUES (103, 101, '0,100,101', '研发部门', 1, '千数', '15888888888', 'qdata@qq.com', '0', '0', 'qData', '2024-05-06 06:12:17', '', NULL);
INSERT INTO `system_dept` VALUES (104, 101, '0,100,101', '市场部门', 2, '千数', '15888888888', 'qdata@qq.com', '0', '0', 'qData', '2024-05-06 06:12:17', '', NULL);
INSERT INTO `system_dept` VALUES (105, 101, '0,100,101', '测试部门', 3, '千数', '15888888888', 'qdata@qq.com', '0', '0', 'qData', '2024-05-06 06:12:17', '', NULL);
INSERT INTO `system_dept` VALUES (106, 101, '0,100,101', '财务部门', 4, '千数', '15888888888', 'qdata@qq.com', '0', '0', 'qData', '2024-05-06 06:12:17', '', NULL);
INSERT INTO `system_dept` VALUES (107, 101, '0,100,101', '运维部门', 5, '千数', '15888888888', 'qdata@qq.com', '0', '0', 'qData', '2024-05-06 06:12:17', 'qData', '2024-11-26 15:50:19');
INSERT INTO `system_dept` VALUES (108, 102, '0,100,102', '市场部门', 2, '千数', '15888888888', 'qdata@qq.com', '0', '0', 'qData', '2024-05-06 06:12:17', 'qData', '2025-02-05 17:03:26');
INSERT INTO `system_dept` VALUES (109, 102, '0,100,102', '财务部门', 3, '千数', '15888888888', 'qdata@qq.com', '0', '0', 'qData', '2024-05-06 06:12:17', 'qData', '2025-02-05 17:03:31');
INSERT INTO `system_dept` VALUES (852, 102, '0,100,102', '研发部门', 1, NULL, NULL, NULL, '0', '0', 'qData', '2025-02-05 17:03:07', '', NULL);
INSERT INTO `system_dept` VALUES (853, 101, '0,100,101', '产品部门', 6, NULL, NULL, NULL, '0', '0', 'qData', '2025-02-05 17:05:30', '', NULL);
INSERT INTO `system_dept` VALUES (854, 102, '0,100,102', '产品部门', 4, NULL, NULL, NULL, '0', '0', 'qData', '2025-02-05 17:05:46', '', NULL);

-- ----------------------------
-- Table structure for system_dict_data
-- ----------------------------
DROP TABLE IF EXISTS `system_dict_data`;
CREATE TABLE `system_dict_data`  (
  `dict_code` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '字典编码',
  `dict_sort` int(11) NULL DEFAULT 0 COMMENT '字典排序',
  `dict_label` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '字典标签',
  `dict_value` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '字典键值',
  `dict_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '字典类型',
  `css_class` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '样式属性（其他样式扩展）',
  `list_class` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '表格回显样式',
  `is_default` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT 'N' COMMENT '是否默认（Y是 N否）',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '状态（0正常 1停用）',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`dict_code`) USING BTREE,
  UNIQUE INDEX `DICT_CODE_90021707714100`(`dict_code`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 344 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '字典数据表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of system_dict_data
-- ----------------------------
INSERT INTO `system_dict_data` VALUES (1, 1, '男', '0', 'sys_user_sex', '', '', 'Y', '0', 'qData', '2024-05-06 06:12:18', '', NULL, '性别男');
INSERT INTO `system_dict_data` VALUES (2, 2, '女', '1', 'sys_user_sex', '', '', 'N', '0', 'qData', '2024-05-06 06:12:18', '', NULL, '性别女');
INSERT INTO `system_dict_data` VALUES (3, 3, '未知', '2', 'sys_user_sex', '', '', 'N', '0', 'qData', '2024-05-06 06:12:18', '', NULL, '性别未知');
INSERT INTO `system_dict_data` VALUES (4, 1, '显示', '0', 'sys_show_hide', '', 'primary', 'Y', '0', 'qData', '2024-05-06 06:12:18', '', NULL, '显示菜单');
INSERT INTO `system_dict_data` VALUES (5, 2, '隐藏', '1', 'sys_show_hide', '', 'danger', 'N', '0', 'qData', '2024-05-06 06:12:18', '', NULL, '隐藏菜单');
INSERT INTO `system_dict_data` VALUES (6, 1, '正常', '0', 'sys_normal_disable', '', 'primary', 'Y', '0', 'qData', '2024-05-06 06:12:18', '', NULL, '正常状态');
INSERT INTO `system_dict_data` VALUES (7, 2, '停用', '1', 'sys_normal_disable', '', 'danger', 'N', '0', 'qData', '2024-05-06 06:12:18', '', NULL, '停用状态');
INSERT INTO `system_dict_data` VALUES (8, 1, '正常', '0', 'sys_job_status', '', 'primary', 'Y', '0', 'qData', '2024-05-06 06:12:18', '', NULL, '正常状态');
INSERT INTO `system_dict_data` VALUES (9, 2, '暂停', '1', 'sys_job_status', '', 'danger', 'N', '0', 'qData', '2024-05-06 06:12:18', '', NULL, '停用状态');
INSERT INTO `system_dict_data` VALUES (10, 1, '默认', 'DEFAULT', 'sys_job_group', '', '', 'Y', '0', 'qData', '2024-05-06 06:12:18', '', NULL, '默认分组');
INSERT INTO `system_dict_data` VALUES (11, 2, '系统', 'SYSTEM', 'sys_job_group', '', '', 'N', '0', 'qData', '2024-05-06 06:12:18', '', NULL, '系统分组');
INSERT INTO `system_dict_data` VALUES (12, 1, '是', 'Y', 'sys_yes_no', '', 'primary', 'Y', '0', 'qData', '2024-05-06 06:12:18', '', NULL, '系统默认是');
INSERT INTO `system_dict_data` VALUES (13, 2, '否', 'N', 'sys_yes_no', '', 'danger', 'N', '0', 'qData', '2024-05-06 06:12:18', '', NULL, '系统默认否');
INSERT INTO `system_dict_data` VALUES (14, 1, '通知', '1', 'sys_notice_type', '', 'warning', 'Y', '0', 'qData', '2024-05-06 06:12:18', '', NULL, '通知');
INSERT INTO `system_dict_data` VALUES (15, 2, '公告', '2', 'sys_notice_type', '', 'success', 'N', '0', 'qData', '2024-05-06 06:12:18', '', NULL, '公告');
INSERT INTO `system_dict_data` VALUES (16, 1, '正常', '0', 'sys_notice_status', '', 'primary', 'Y', '0', 'qData', '2024-05-06 06:12:18', '', NULL, '正常状态');
INSERT INTO `system_dict_data` VALUES (17, 2, '关闭', '1', 'sys_notice_status', '', 'danger', 'N', '0', 'qData', '2024-05-06 06:12:18', '', NULL, '关闭状态');
INSERT INTO `system_dict_data` VALUES (18, 99, '其他', '0', 'sys_oper_type', '', 'info', 'N', '0', 'qData', '2024-05-06 06:12:18', '', NULL, '其他操作');
INSERT INTO `system_dict_data` VALUES (19, 1, '新增', '1', 'sys_oper_type', '', 'info', 'N', '0', 'qData', '2024-05-06 06:12:18', '', NULL, '新增操作');
INSERT INTO `system_dict_data` VALUES (20, 2, '修改', '2', 'sys_oper_type', '', 'info', 'N', '0', 'qData', '2024-05-06 06:12:18', '', NULL, '修改操作');
INSERT INTO `system_dict_data` VALUES (21, 3, '删除', '3', 'sys_oper_type', '', 'danger', 'N', '0', 'qData', '2024-05-06 06:12:18', '', NULL, '删除操作');
INSERT INTO `system_dict_data` VALUES (22, 4, '授权', '4', 'sys_oper_type', '', 'primary', 'N', '0', 'qData', '2024-05-06 06:12:18', '', NULL, '授权操作');
INSERT INTO `system_dict_data` VALUES (23, 5, '导出', '5', 'sys_oper_type', '', 'warning', 'N', '0', 'qData', '2024-05-06 06:12:18', '', NULL, '导出操作');
INSERT INTO `system_dict_data` VALUES (24, 6, '导入', '6', 'sys_oper_type', '', 'warning', 'N', '0', 'qData', '2024-05-06 06:12:18', '', NULL, '导入操作');
INSERT INTO `system_dict_data` VALUES (25, 7, '强退', '7', 'sys_oper_type', '', 'danger', 'N', '0', 'qData', '2024-05-06 06:12:18', '', NULL, '强退操作');
INSERT INTO `system_dict_data` VALUES (26, 8, '生成代码', '8', 'sys_oper_type', '', 'warning', 'N', '0', 'qData', '2024-05-06 06:12:18', '', NULL, '生成操作');
INSERT INTO `system_dict_data` VALUES (27, 9, '清空数据', '9', 'sys_oper_type', '', 'danger', 'N', '0', 'qData', '2024-05-06 06:12:18', '', NULL, '清空操作');
INSERT INTO `system_dict_data` VALUES (28, 1, '成功', '0', 'sys_common_status', '', 'primary', 'N', '0', 'qData', '2024-05-06 06:12:18', '', NULL, '正常状态');
INSERT INTO `system_dict_data` VALUES (29, 2, '失败', '1', 'sys_common_status', '', 'danger', 'N', '0', 'qData', '2024-05-06 06:12:18', '', NULL, '停用状态');
INSERT INTO `system_dict_data` VALUES (30, 0, 'Web', '0', 'auth_app_type', NULL, 'primary', 'N', '0', 'qData', '2024-08-31 14:27:22', 'qData', '2024-08-31 14:27:31', NULL);
INSERT INTO `system_dict_data` VALUES (31, 1, 'App', '1', 'auth_app_type', NULL, 'info', 'N', '0', 'qData', '2024-08-31 14:27:47', '', NULL, NULL);
INSERT INTO `system_dict_data` VALUES (32, 2, '小程序', '2', 'auth_app_type', NULL, 'warning', 'N', '0', 'qData', '2024-08-31 14:28:06', '', NULL, NULL);
INSERT INTO `system_dict_data` VALUES (33, 1, '有效', '1', 'sys_valid', NULL, 'success', 'N', '0', 'qData', '2024-08-31 15:33:49', 'qData', '2024-08-31 15:34:08', NULL);
INSERT INTO `system_dict_data` VALUES (34, 0, '无效', '0', 'sys_valid', NULL, 'danger', 'N', '0', 'qData', '2024-08-31 15:34:21', 'qData', '2025-01-21 16:31:24', NULL);
INSERT INTO `system_dict_data` VALUES (35, 0, '非公开', '0', 'auth_public', NULL, 'warning', 'N', '0', 'qData', '2024-08-31 15:36:48', '', NULL, NULL);
INSERT INTO `system_dict_data` VALUES (36, 1, '公开', '1', 'auth_public', NULL, 'success', 'N', '0', 'qData', '2024-08-31 15:36:59', '', NULL, NULL);
INSERT INTO `system_dict_data` VALUES (37, 0, '通知', '0', 'message_category', NULL, 'success', 'N', '0', 'qData', '2024-11-07 14:28:29', 'qData', '2024-12-19 14:19:58', NULL);
INSERT INTO `system_dict_data` VALUES (38, 0, '重要', '0', 'message_level', NULL, 'default', 'N', '0', 'qData', '2024-11-07 14:28:42', '', NULL, NULL);
INSERT INTO `system_dict_data` VALUES (39, 2, '审批', '2', 'message_category', NULL, 'warning', 'N', '0', 'qData', '2024-11-19 14:20:42', 'qData', '2024-11-19 16:55:16', NULL);
INSERT INTO `system_dict_data` VALUES (41, 3, '其他', '3', 'message_category', NULL, 'info', 'N', '0', 'qData', '2024-11-19 14:21:12', 'qData', '2024-12-19 14:19:31', NULL);
INSERT INTO `system_dict_data` VALUES (42, 1, '公告', '1', 'message_category', NULL, 'primary', 'N', '0', 'qData', '2024-11-19 16:54:18', 'qData', '2024-11-19 16:55:05', NULL);
INSERT INTO `system_dict_data` VALUES (43, 0, '是', 'true', 'sys_boolean', NULL, 'success', 'N', '0', 'qData', '2024-12-03 15:46:40', '', NULL, NULL);
INSERT INTO `system_dict_data` VALUES (44, 0, '否', 'false', 'sys_boolean', NULL, 'danger', 'N', '0', 'qData', '2024-12-03 15:46:57', '', NULL, NULL);
INSERT INTO `system_dict_data` VALUES (45, 1, '一般', '1', 'message_level', NULL, 'info', 'N', '0', 'qData', '2024-12-25 16:21:09', '', NULL, NULL);
INSERT INTO `system_dict_data` VALUES (46, 2, '警告', '2', 'message_level', NULL, 'warning', 'N', '0', 'qData', '2024-12-25 16:21:30', '', NULL, NULL);
INSERT INTO `system_dict_data` VALUES (47, 0, '未开始', '0', 'project_status', NULL, 'warning', 'N', '0', 'qData', '2024-12-30 16:33:45', 'qData', '2025-01-08 18:28:05', NULL);
INSERT INTO `system_dict_data` VALUES (48, 1, '活动', '1', 'project_status', NULL, 'primary', 'N', '0', 'qData', '2024-12-30 16:33:55', 'qData', '2025-01-08 18:28:10', NULL);
INSERT INTO `system_dict_data` VALUES (49, 2, '已结束', '2', 'project_status', NULL, 'success', 'N', '0', 'qData', '2024-12-30 16:34:04', 'qData', '2025-01-08 18:28:15', NULL);
INSERT INTO `system_dict_data` VALUES (50, 0, '未开始', '0', 'story_status', NULL, 'info', 'N', '0', 'qData', '2024-12-31 11:15:58', 'qData', '2024-12-31 11:16:15', NULL);
INSERT INTO `system_dict_data` VALUES (51, 1, '进行中', '1', 'story_status', NULL, 'warning', 'N', '0', 'qData', '2024-12-31 11:16:08', 'qData', '2024-12-31 11:16:23', NULL);
INSERT INTO `system_dict_data` VALUES (52, 2, '待演示', '2', 'story_status', NULL, 'primary', 'N', '0', 'qData', '2024-12-31 11:16:37', 'qData', '2024-12-31 11:16:42', NULL);
INSERT INTO `system_dict_data` VALUES (53, 3, '已完成', '3', 'story_status', NULL, 'success', 'N', '0', 'qData', '2024-12-31 11:16:59', '', NULL, NULL);
INSERT INTO `system_dict_data` VALUES (54, 1, '无关紧要(0-60)', '1', 'project_priority', NULL, 'default', 'N', '0', 'qData', '2025-01-03 13:20:35', 'qData', '2025-01-09 15:44:23', NULL);
INSERT INTO `system_dict_data` VALUES (55, 2, '低优先级(60-80)', '2', 'project_priority', NULL, 'default', 'N', '0', 'qData', '2025-01-03 13:20:56', 'qData', '2025-01-09 15:44:33', NULL);
INSERT INTO `system_dict_data` VALUES (56, 3, '中优先级(80-90)', '3', 'project_priority', NULL, 'default', 'N', '0', 'qData', '2025-01-03 13:21:11', 'qData', '2025-01-09 15:44:42', NULL);
INSERT INTO `system_dict_data` VALUES (57, 4, '高优先级(90-100)', '4', 'project_priority', NULL, 'default', 'N', '0', 'qData', '2025-01-03 13:21:25', 'qData', '2025-01-09 15:44:49', NULL);
INSERT INTO `system_dict_data` VALUES (58, 1, '建议', '1', 'project_severity', NULL, 'default', 'N', '0', 'qData', '2025-01-03 13:33:58', '', NULL, NULL);
INSERT INTO `system_dict_data` VALUES (59, 2, '提示', '2', 'project_severity', NULL, 'default', 'N', '0', 'qData', '2025-01-03 13:34:19', '', NULL, NULL);
INSERT INTO `system_dict_data` VALUES (60, 3, '一般', '3', 'project_severity', NULL, 'default', 'N', '0', 'qData', '2025-01-03 13:34:26', '', NULL, NULL);
INSERT INTO `system_dict_data` VALUES (61, 4, '严重', '4', 'project_severity', NULL, 'default', 'N', '0', 'qData', '2025-01-03 13:34:33', '', NULL, NULL);
INSERT INTO `system_dict_data` VALUES (62, 5, '致命', '5', 'project_severity', NULL, 'default', 'N', '0', 'qData', '2025-01-03 13:34:50', '', NULL, NULL);
INSERT INTO `system_dict_data` VALUES (63, 1, '待处理', '1', 'defect_status', NULL, 'default', 'N', '0', 'qData', '2025-01-03 14:23:09', 'qData', '2025-01-06 09:38:56', NULL);
INSERT INTO `system_dict_data` VALUES (64, 2, '已处理', '2', 'defect_status', NULL, 'default', 'N', '0', 'qData', '2025-01-03 14:23:28', 'qData', '2025-01-06 09:39:06', NULL);
INSERT INTO `system_dict_data` VALUES (70, 0, '待完成', '0', 'task_status', NULL, 'info', 'N', '0', 'qData', '2025-01-03 14:54:49', 'qData', '2025-01-08 10:16:47', NULL);
INSERT INTO `system_dict_data` VALUES (71, 1, '进行中', '1', 'task_status', NULL, 'warning', 'N', '0', 'qData', '2025-01-03 14:55:01', 'qData', '2025-01-08 10:17:02', NULL);
INSERT INTO `system_dict_data` VALUES (72, 2, '已完成', '2', 'task_status', NULL, 'success', 'N', '0', 'qData', '2025-01-03 14:55:13', 'qData', '2025-01-03 14:55:16', NULL);
INSERT INTO `system_dict_data` VALUES (73, 0, '上班', '0', 'day_report', NULL, 'default', 'N', '0', 'qData', '2025-01-06 11:41:42', 'qData', '2025-01-06 15:23:55', NULL);
INSERT INTO `system_dict_data` VALUES (74, 1, '请假', '1', 'day_report', NULL, 'default', 'N', '0', 'qData', '2025-01-06 11:41:49', 'qData', '2025-01-06 15:24:12', NULL);
INSERT INTO `system_dict_data` VALUES (75, 0, '待审核', '0', 'report_status', NULL, 'primary', 'N', '0', 'qData', '2025-01-06 15:52:26', 'qData', '2025-01-07 09:42:10', NULL);
INSERT INTO `system_dict_data` VALUES (76, 1, '审核通过', '1', 'report_status', NULL, 'success', 'N', '0', 'qData', '2025-01-06 15:52:37', 'qData', '2025-01-07 09:42:16', NULL);
INSERT INTO `system_dict_data` VALUES (77, 2, '驳回', '2', 'report_status', NULL, 'danger', 'N', '0', 'qData', '2025-01-07 14:47:39', 'qData', '2025-01-07 14:47:52', NULL);
INSERT INTO `system_dict_data` VALUES (78, 1, '禁用', '0', 'dp_model_status', NULL, 'warning', 'N', '0', 'qData', '2025-01-21 09:46:04', 'shuyexin', '2025-02-06 16:32:22', NULL);
INSERT INTO `system_dict_data` VALUES (79, 2, '启用', '1', 'dp_model_status', NULL, 'success', 'N', '0', 'qData', '2025-01-21 09:46:58', 'shuyexin', '2025-02-06 15:12:04', NULL);
INSERT INTO `system_dict_data` VALUES (80, 1, '手工录入', '1', 'dp_model_create_type', NULL, 'default', 'N', '0', 'qData', '2025-01-21 09:49:12', 'qData', '2025-01-21 09:50:05', NULL);
INSERT INTO `system_dict_data` VALUES (81, 2, '物化表生成', '2', 'dp_model_create_type', NULL, 'default', 'N', '0', 'qData', '2025-01-21 09:49:40', 'qData', '2025-01-21 09:50:02', NULL);
INSERT INTO `system_dict_data` VALUES (82, 1, '数据元', '1', 'dp_data_elem_code_type', NULL, 'success', 'N', '0', 'qData', '2025-01-21 10:07:52', 'qData', '2025-01-21 10:08:18', NULL);
INSERT INTO `system_dict_data` VALUES (83, 2, '代码表', '2', 'dp_data_elem_code_type', NULL, 'primary', 'N', '0', 'qData', '2025-01-21 10:08:13', '', NULL, NULL);
INSERT INTO `system_dict_data` VALUES (84, 1, '稽查规则', '1', 'dp_data_elem_rules_type', NULL, 'primary', 'N', '0', 'qData', '2025-01-21 10:08:49', '', NULL, NULL);
INSERT INTO `system_dict_data` VALUES (85, 2, '清洗规则', '2', 'dp_data_elem_rules_type', NULL, 'success', 'N', '0', 'qData', '2025-01-21 10:09:02', '', NULL, NULL);
INSERT INTO `system_dict_data` VALUES (86, 0, '未上线', '0', 'dp_template_online_status', NULL, 'primary', 'N', '0', 'qData', '2025-01-21 10:09:32', '', NULL, NULL);
INSERT INTO `system_dict_data` VALUES (87, 1, '已上线', '1', 'dp_template_online_status', NULL, 'success', 'N', '0', 'qData', '2025-01-21 10:09:42', '', NULL, NULL);
INSERT INTO `system_dict_data` VALUES (88, 1, '手动录入', '1', 'dp_template_data_source_type', NULL, 'primary', 'N', '0', 'qData', '2025-01-21 10:10:15', '', NULL, NULL);
INSERT INTO `system_dict_data` VALUES (89, 2, '物化表生成', '2', 'dp_template_data_source_type', NULL, 'success', 'N', '0', 'qData', '2025-01-21 10:10:46', 'qData', '2025-01-21 10:10:51', NULL);
INSERT INTO `system_dict_data` VALUES (90, 1, '未创建', '1', 'dp_build_log_build_status', NULL, 'primary', 'N', '0', 'qData', '2025-01-21 10:15:21', '', NULL, NULL);
INSERT INTO `system_dict_data` VALUES (91, 2, '创建中', '2', 'dp_build_log_build_status', NULL, 'info', 'N', '0', 'qData', '2025-01-21 10:15:32', 'qData', '2025-01-21 10:17:35', NULL);
INSERT INTO `system_dict_data` VALUES (92, 3, '已结束', '3', 'dp_build_log_build_status', NULL, 'danger', 'N', '0', 'qData', '2025-01-21 10:15:44', 'qData', '2025-01-21 10:15:52', NULL);
INSERT INTO `system_dict_data` VALUES (93, 1, '未创建', '1', 'dp_template_build_log_build_status', NULL, 'primary', 'N', '0', 'qData', '2025-01-21 10:16:15', '', NULL, NULL);
INSERT INTO `system_dict_data` VALUES (94, 2, '创建中', '2', 'dp_template_build_log_build_status', NULL, 'info', 'N', '0', 'qData', '2025-01-21 10:16:25', 'qData', '2025-01-21 10:16:46', NULL);
INSERT INTO `system_dict_data` VALUES (95, 3, '成功', '3', 'dp_template_build_log_build_status', NULL, 'success', 'N', '0', 'qData', '2025-01-21 10:16:37', 'qData', '2025-01-21 10:16:51', NULL);
INSERT INTO `system_dict_data` VALUES (96, 4, '失败', '4', 'dp_template_build_log_build_status', NULL, 'danger', 'N', '0', 'qData', '2025-01-21 10:17:13', '', NULL, NULL);
INSERT INTO `system_dict_data` VALUES (97, 5, '已存在', '5', 'dp_template_build_log_build_status', NULL, 'warning', 'N', '0', 'qData', '2025-01-21 10:17:23', '', NULL, NULL);
INSERT INTO `system_dict_data` VALUES (98, 1, '否', '0', 'dp_model_column_pk_flag', NULL, 'default', 'N', '0', 'qData', '2025-01-21 11:21:50', 'qData', '2025-01-21 11:22:11', NULL);
INSERT INTO `system_dict_data` VALUES (99, 1, '是', '1', 'dp_model_column_pk_flag', NULL, 'default', 'N', '0', 'qData', '2025-01-21 11:21:58', '', NULL, NULL);
INSERT INTO `system_dict_data` VALUES (100, 1, '否', '0', 'dp_model_column_nullable_flag', NULL, 'default', 'N', '0', 'qData', '2025-01-21 11:22:44', '', NULL, NULL);
INSERT INTO `system_dict_data` VALUES (101, 1, '是', '1', 'dp_model_column_nullable_flag', NULL, 'default', 'N', '0', 'qData', '2025-01-21 11:22:53', '', NULL, NULL);
INSERT INTO `system_dict_data` VALUES (102, 0, '未发布', '0', 'da_assets_status', NULL, 'info', 'N', '0', '', NULL, 'qData', '2025-01-22 14:05:30', NULL);
INSERT INTO `system_dict_data` VALUES (103, 1, '已发布', '1', 'da_assets_status', NULL, NULL, 'N', '0', '', NULL, 'qData', '2025-01-22 14:05:34', NULL);
INSERT INTO `system_dict_data` VALUES (105, 0, '完整性', '1', 'att_rule_audit_q_dimension', NULL, NULL, 'N', '0', '', NULL, '', NULL, NULL);
INSERT INTO `system_dict_data` VALUES (106, 0, '准确性', '2', 'att_rule_audit_q_dimension', NULL, NULL, 'N', '0', '', NULL, '', NULL, NULL);
INSERT INTO `system_dict_data` VALUES (107, 0, '一致性', '3', 'att_rule_audit_q_dimension', NULL, NULL, 'N', '0', '', NULL, '', NULL, NULL);
INSERT INTO `system_dict_data` VALUES (108, 0, '规范性', '4', 'att_rule_audit_q_dimension', NULL, NULL, 'N', '0', '', NULL, '', NULL, NULL);
INSERT INTO `system_dict_data` VALUES (109, 0, '时效性', '5', 'att_rule_audit_q_dimension', NULL, NULL, 'N', '0', '', NULL, '', NULL, NULL);
INSERT INTO `system_dict_data` VALUES (110, 0, '字符校验', '1', 'att_rule_audit_type', NULL, NULL, 'N', '0', '', NULL, '', NULL, NULL);
INSERT INTO `system_dict_data` VALUES (111, 0, '数值校验', '2', 'att_rule_audit_type', NULL, NULL, 'N', '0', '', NULL, '', NULL, NULL);
INSERT INTO `system_dict_data` VALUES (112, 0, '空值校验', '3', 'att_rule_audit_type', NULL, NULL, 'N', '0', '', NULL, '', NULL, NULL);
INSERT INTO `system_dict_data` VALUES (113, 0, '长度校验', '4', 'att_rule_audit_type', NULL, NULL, 'N', '0', '', NULL, '', NULL, NULL);
INSERT INTO `system_dict_data` VALUES (114, 0, '重复检查', '5', 'att_rule_audit_type', NULL, NULL, 'N', '0', '', NULL, '', NULL, NULL);
INSERT INTO `system_dict_data` VALUES (115, 0, '格式检查', '6', 'att_rule_audit_type', NULL, NULL, 'N', '0', '', NULL, '', NULL, NULL);
INSERT INTO `system_dict_data` VALUES (116, 0, '字段级', '1', 'att_rule_level', NULL, NULL, 'N', '0', '', NULL, '', NULL, NULL);
INSERT INTO `system_dict_data` VALUES (117, 0, '表级', '2', 'att_rule_level', NULL, NULL, 'N', '0', '', NULL, '', NULL, NULL);
INSERT INTO `system_dict_data` VALUES (118, 0, '字符串转化', '1', 'att_rule_clean_type', NULL, NULL, 'N', '0', '', NULL, '', NULL, NULL);
INSERT INTO `system_dict_data` VALUES (119, 0, '数值处理', '2', 'att_rule_clean_type', NULL, NULL, 'N', '0', '', NULL, '', NULL, NULL);
INSERT INTO `system_dict_data` VALUES (120, 0, '时间处理', '3', 'att_rule_clean_type', NULL, NULL, 'N', '0', '', NULL, '', NULL, NULL);
INSERT INTO `system_dict_data` VALUES (121, 0, '重复值处理', '4', 'att_rule_clean_type', NULL, NULL, 'N', '0', '', NULL, '', NULL, NULL);
INSERT INTO `system_dict_data` VALUES (122, 0, '空值处理', '5', 'att_rule_clean_type', NULL, NULL, 'N', '0', '', NULL, '', NULL, NULL);
INSERT INTO `system_dict_data` VALUES (123, 0, '全部', '1', 'da_sensitive_level_rule', NULL, NULL, 'N', '0', '', NULL, '', NULL, NULL);
INSERT INTO `system_dict_data` VALUES (124, 0, '起始位', '2', 'da_sensitive_level_rule', NULL, NULL, 'N', '0', '', NULL, '', NULL, NULL);
INSERT INTO `system_dict_data` VALUES (125, 0, '自定义', '3', 'da_sensitive_level_rule', NULL, NULL, 'N', '0', '', NULL, '', NULL, NULL);
INSERT INTO `system_dict_data` VALUES (126, 0, '待审核', '1', 'da_assets_audit_status1', NULL, NULL, 'N', '0', '', NULL, '', NULL, NULL);
INSERT INTO `system_dict_data` VALUES (127, 0, '通过', '2', 'da_assets_audit_status1', NULL, NULL, 'N', '0', '', NULL, '', NULL, NULL);
INSERT INTO `system_dict_data` VALUES (128, 0, '不通过', '3', 'da_assets_audit_status1', NULL, NULL, 'N', '0', '', NULL, '', NULL, NULL);
INSERT INTO `system_dict_data` VALUES (129, 1, 'TINYINT', 'TINYINT', 'column_type', NULL, 'default', 'N', '0', 'qData', '2025-01-21 13:49:12', '', NULL, NULL);
INSERT INTO `system_dict_data` VALUES (130, 2, 'INTEGER', 'INTEGER', 'column_type', NULL, 'default', 'N', '0', 'qData', '2025-01-21 13:49:27', '', NULL, NULL);
INSERT INTO `system_dict_data` VALUES (131, 3, 'BIGINT', 'BIGINT', 'column_type', NULL, 'default', 'N', '0', 'qData', '2025-01-21 13:49:44', '', NULL, NULL);
INSERT INTO `system_dict_data` VALUES (132, 4, 'DECIMAL', 'DECIMAL', 'column_type', NULL, 'default', 'N', '0', 'qData', '2025-01-21 13:49:52', '', NULL, NULL);
INSERT INTO `system_dict_data` VALUES (133, 5, 'NUMERIC', 'NUMERIC', 'column_type', NULL, 'default', 'N', '0', 'qData', '2025-01-21 13:50:04', '', NULL, NULL);
INSERT INTO `system_dict_data` VALUES (134, 6, 'FLOAT', 'FLOAT', 'column_type', NULL, 'default', 'N', '0', 'qData', '2025-01-21 13:50:13', '', NULL, NULL);
INSERT INTO `system_dict_data` VALUES (135, 7, 'DOUBLE', 'DOUBLE', 'column_type', NULL, 'default', 'N', '0', 'qData', '2025-01-21 13:50:21', '', NULL, NULL);
INSERT INTO `system_dict_data` VALUES (136, 8, 'NUMBER', 'NUMBER', 'column_type', NULL, 'default', 'N', '0', 'qData', '2025-01-21 13:50:31', '', NULL, NULL);
INSERT INTO `system_dict_data` VALUES (137, 9, 'CHAR', 'CHAR', 'column_type', NULL, 'default', 'N', '0', 'qData', '2025-01-21 13:50:41', '', NULL, NULL);
INSERT INTO `system_dict_data` VALUES (138, 10, 'VARCHAR', 'VARCHAR', 'column_type', NULL, 'default', 'N', '0', 'qData', '2025-01-21 13:50:58', '', NULL, NULL);
INSERT INTO `system_dict_data` VALUES (139, 11, 'VARCHAR2', 'VARCHAR2', 'column_type', NULL, 'default', 'N', '0', 'qData', '2025-01-21 13:51:07', 'qData', '2025-01-21 13:52:11', NULL);
INSERT INTO `system_dict_data` VALUES (140, 12, 'TEXT', 'TEXT', 'column_type', NULL, 'default', 'N', '0', 'qData', '2025-01-21 13:52:21', '', NULL, NULL);
INSERT INTO `system_dict_data` VALUES (141, 13, 'DATE', 'DATE', 'column_type', NULL, 'default', 'N', '0', 'qData', '2025-01-21 13:52:35', '', NULL, NULL);
INSERT INTO `system_dict_data` VALUES (142, 14, 'TIMESTAMP', 'TIMESTAMP', 'column_type', NULL, 'default', 'N', '0', 'qData', '2025-01-21 13:52:50', '', NULL, NULL);
INSERT INTO `system_dict_data` VALUES (143, 15, ' DATETIME', ' DATETIME', 'column_type', NULL, 'default', 'N', '0', 'qData', '2025-01-21 13:52:58', '', NULL, NULL);
INSERT INTO `system_dict_data` VALUES (144, 1, '启用', '1', 'sys_disable', NULL, 'primary', 'N', '0', 'qData', '2025-01-21 15:34:40', 'qData', '2025-01-22 10:42:12', NULL);
INSERT INTO `system_dict_data` VALUES (145, 2, '禁用', '0', 'sys_disable', NULL, 'danger', 'N', '0', 'qData', '2025-01-21 15:34:55', 'qData', '2025-01-22 10:42:17', NULL);
INSERT INTO `system_dict_data` VALUES (146, 5, 'Kingbase8', 'Kingbase8', 'datasource_type', NULL, 'primary', 'N', '0', 'qData', '2025-01-22 10:22:44', 'mengfanming', '2025-03-10 15:30:41', NULL);
INSERT INTO `system_dict_data` VALUES (147, 2, 'DM8', 'DM8', 'datasource_type', NULL, 'primary', 'N', '0', 'qData', '2025-01-22 10:25:01', 'qData', '2025-01-22 14:59:56', NULL);
INSERT INTO `system_dict_data` VALUES (150, 3, 'Oracle', 'Oracle', 'datasource_type', NULL, 'primary', 'N', '0', 'qData', '2025-01-22 10:26:01', 'mengfanming', '2025-03-10 09:55:44', NULL);
INSERT INTO `system_dict_data` VALUES (153, 0, '上线', '1', 'da_sensitive_status', NULL, 'default', 'N', '0', 'liuhaosheng', '2025-02-06 09:55:30', '', NULL, NULL);
INSERT INTO `system_dict_data` VALUES (154, 1, '下线', '0', 'da_sensitive_status', NULL, 'default', 'N', '0', 'liuhaosheng', '2025-02-06 09:55:39', 'qData', '2025-02-06 16:58:31', NULL);
INSERT INTO `system_dict_data` VALUES (155, 0, '停用', '0', 'ds_api_log_status', NULL, NULL, 'N', '0', '', NULL, 'qData', '2025-02-26 11:29:44', NULL);
INSERT INTO `system_dict_data` VALUES (156, 0, '启用', '1', 'ds_api_log_status', NULL, NULL, 'N', '0', '', NULL, '', NULL, NULL);
INSERT INTO `system_dict_data` VALUES (157, 0, 'GET', '1', 'ds_api_bas_info_api_method_type', NULL, NULL, 'N', '0', '', NULL, 'qData', '2025-02-26 11:26:48', NULL);
INSERT INTO `system_dict_data` VALUES (158, 0, 'POST', '2', 'ds_api_bas_info_api_method_type', NULL, 'success', 'N', '0', '', NULL, 'liuhaosheng', '2025-02-27 17:15:49', NULL);
INSERT INTO `system_dict_data` VALUES (159, 1, '单表向导式', '1', 'ds_api_bas_info_api_service_type', NULL, NULL, 'N', '0', '', NULL, 'qData', '2025-04-22 11:50:33', NULL);
INSERT INTO `system_dict_data` VALUES (160, 2, 'SQL脚本式', '2', 'ds_api_bas_info_api_service_type', NULL, NULL, 'N', '0', '', NULL, 'qData', '2025-04-22 11:50:48', NULL);
INSERT INTO `system_dict_data` VALUES (161, 3, '第三方转发', '3', 'ds_api_bas_info_api_service_type', NULL, NULL, 'N', '0', '', NULL, 'qData', '2025-04-22 11:50:42', NULL);
INSERT INTO `system_dict_data` VALUES (162, 0, '详情', '1', 'ds_api_bas_info_res_data_type', NULL, NULL, 'N', '0', '', NULL, '', NULL, NULL);
INSERT INTO `system_dict_data` VALUES (163, 0, '列表', '2', 'ds_api_bas_info_res_data_type', NULL, 'success', 'N', '0', '', NULL, 'liuhaosheng', '2025-02-27 17:16:28', NULL);
INSERT INTO `system_dict_data` VALUES (164, 0, '分页', '3', 'ds_api_bas_info_res_data_type', NULL, 'info', 'N', '0', '', NULL, 'liuhaosheng', '2025-02-27 17:16:41', NULL);
INSERT INTO `system_dict_data` VALUES (167, 0, '失败', '0', 'ds_api_log_res_status', NULL, 'danger', 'N', '0', '', NULL, 'qData', '2025-02-26 13:52:17', NULL);
INSERT INTO `system_dict_data` VALUES (168, 0, '成功', '1', 'ds_api_log_res_status', NULL, 'success', 'N', '0', '', NULL, 'shuyexin', '2025-03-25 17:44:31', NULL);
INSERT INTO `system_dict_data` VALUES (169, 0, '上线', '0', 'da_discovery_task_status', NULL, 'success', 'N', '0', 'mengfanming', '2025-02-13 14:48:00', 'mengfanming', '2025-02-13 14:53:48', NULL);
INSERT INTO `system_dict_data` VALUES (170, 0, '下线', '1', 'da_discovery_task_status', NULL, 'danger', 'N', '0', 'mengfanming', '2025-02-13 14:49:53', 'mengfanming', '2025-02-13 14:53:57', NULL);
INSERT INTO `system_dict_data` VALUES (171, 1, '新增', '1', 'da_discovery_table_change_flag', NULL, 'primary', 'N', '0', 'mengfanming', '2025-02-13 14:58:10', '', NULL, NULL);
INSERT INTO `system_dict_data` VALUES (172, 1, '修改', '2', 'da_discovery_table_change_flag', NULL, 'warning', 'N', '0', 'mengfanming', '2025-02-13 14:59:50', '', NULL, NULL);
INSERT INTO `system_dict_data` VALUES (173, 3, '删除', '3', 'da_discovery_table_change_flag', NULL, 'danger', 'N', '0', 'mengfanming', '2025-02-13 15:00:07', '', NULL, NULL);
INSERT INTO `system_dict_data` VALUES (174, 4, '无变化', '4', 'da_discovery_table_change_flag', NULL, 'success', 'N', '0', 'mengfanming', '2025-02-13 15:00:21', 'mengfanming', '2025-02-13 15:00:31', NULL);
INSERT INTO `system_dict_data` VALUES (175, 1, '待提交', '1', 'da_discovery_table_change_status', NULL, 'warning', 'N', '0', 'mengfanming', '2025-02-13 15:01:46', 'mengfanming', '2025-02-14 10:53:45', NULL);
INSERT INTO `system_dict_data` VALUES (176, 2, '已提交', '2', 'da_discovery_table_change_status', NULL, 'success', 'N', '0', 'mengfanming', '2025-02-13 15:02:02', 'mengfanming', '2025-02-13 15:02:07', NULL);
INSERT INTO `system_dict_data` VALUES (177, 0, '否', '0', 'da_discovery_table_change_ignore_flag', NULL, 'default', 'N', '0', 'mengfanming', '2025-02-13 15:03:41', '', NULL, NULL);
INSERT INTO `system_dict_data` VALUES (178, 1, '已忽略', '1', 'da_discovery_table_change_ignore_flag', NULL, 'primary', 'N', '0', 'mengfanming', '2025-02-13 15:03:53', 'shuyexin', '2025-02-28 17:51:01', NULL);
INSERT INTO `system_dict_data` VALUES (179, 1, '运行中', '1', 'da_discovery_task_log_status', NULL, 'default', 'N', '0', 'mengfanming', '2025-02-18 13:50:30', 'mengfanming', '2025-02-18 13:51:42', NULL);
INSERT INTO `system_dict_data` VALUES (180, 2, '成功', '2', 'da_discovery_task_log_status', NULL, 'success', 'N', '0', 'mengfanming', '2025-02-18 13:50:42', '', NULL, NULL);
INSERT INTO `system_dict_data` VALUES (181, 3, '失败', '3', 'da_discovery_task_log_status', NULL, 'danger', 'N', '0', 'mengfanming', '2025-02-18 13:50:57', 'mengfanming', '2025-02-18 13:51:03', NULL);
INSERT INTO `system_dict_data` VALUES (182, 0, '下线', '0', 'ds_api_status', NULL, 'default', 'N', '0', 'qData', '2025-02-18 18:33:34', '', NULL, NULL);
INSERT INTO `system_dict_data` VALUES (183, 1, '上线', '1', 'ds_api_status', NULL, 'default', 'N', '0', 'qData', '2025-02-18 18:33:45', '', NULL, NULL);
INSERT INTO `system_dict_data` VALUES (184, 0, '否', '0', 'ds_api_limit_status', NULL, 'default', 'N', '0', 'qData', '2025-02-18 18:38:39', '', NULL, NULL);
INSERT INTO `system_dict_data` VALUES (185, 1, '是', '1', 'ds_api_limit_status', NULL, 'default', 'N', '0', 'qData', '2025-02-18 18:38:47', '', NULL, NULL);
INSERT INTO `system_dict_data` VALUES (187, 1, '未上线', '0', 'dpp_etl_task_status', NULL, 'primary', 'N', '0', 'mengfanming', '2025-02-19 09:59:46', 'mengfanming', '2025-02-19 10:00:19', NULL);
INSERT INTO `system_dict_data` VALUES (188, 2, '已上线', '1', 'dpp_etl_task_status', NULL, 'success', 'N', '0', 'mengfanming', '2025-02-19 10:00:11', '', NULL, NULL);
INSERT INTO `system_dict_data` VALUES (189, 0, '字符串', '1', 'ds_api_param_type', NULL, 'default', 'N', '0', 'qData', '2025-02-19 10:22:28', '', NULL, NULL);
INSERT INTO `system_dict_data` VALUES (190, 1, '整数', '2', 'ds_api_param_type', NULL, 'default', 'N', '0', 'qData', '2025-02-19 10:22:36', 'qData', '2025-02-19 10:22:42', NULL);
INSERT INTO `system_dict_data` VALUES (191, 2, '浮点型', '3', 'ds_api_param_type', NULL, 'default', 'N', '0', 'qData', '2025-02-19 10:22:51', '', NULL, NULL);
INSERT INTO `system_dict_data` VALUES (192, 3, '时间', '4', 'ds_api_param_type', NULL, 'default', 'N', '0', 'qData', '2025-02-19 10:23:03', '', NULL, NULL);
INSERT INTO `system_dict_data` VALUES (193, 4, '集合', '5', 'ds_api_param_type', NULL, 'default', 'N', '0', 'qData', '2025-02-19 10:23:13', '', NULL, NULL);
INSERT INTO `system_dict_data` VALUES (194, 1, '等于', '1', 'da_api_param_operator', '', '', 'N', '0', 'qData', '2025-02-19 11:03:07', 'qData', '2025-02-19 11:03:07', '等于');
INSERT INTO `system_dict_data` VALUES (195, 2, '不等于', '2', 'da_api_param_operator', '', '', 'N', '0', 'qData', '2025-02-19 11:03:07', 'qData', '2025-02-19 11:03:07', '不等于');
INSERT INTO `system_dict_data` VALUES (196, 3, '全模糊查询', '3', 'da_api_param_operator', '', '', 'N', '0', 'qData', '2025-02-19 11:03:07', 'qData', '2025-02-19 11:03:07', '全模糊查询');
INSERT INTO `system_dict_data` VALUES (197, 4, '左模糊查询', '4', 'da_api_param_operator', '', '', 'N', '0', 'qData', '2025-02-19 11:03:07', 'qData', '2025-02-19 11:03:07', '左模糊查询');
INSERT INTO `system_dict_data` VALUES (198, 5, '右模糊查询', '5', 'da_api_param_operator', '', '', 'N', '0', 'qData', '2025-02-19 11:03:07', 'qData', '2025-02-19 11:03:07', '右模糊查询');
INSERT INTO `system_dict_data` VALUES (199, 6, '大于', '6', 'da_api_param_operator', '', '', 'N', '0', 'qData', '2025-02-19 11:03:07', 'qData', '2025-02-19 11:03:07', '大于');
INSERT INTO `system_dict_data` VALUES (200, 7, '大于等于', '7', 'da_api_param_operator', '', '', 'N', '0', 'qData', '2025-02-19 11:03:07', 'qData', '2025-02-19 11:03:07', '大于等于');
INSERT INTO `system_dict_data` VALUES (201, 8, '小于', '8', 'da_api_param_operator', '', '', 'N', '0', 'qData', '2025-02-19 11:03:07', 'qData', '2025-02-19 11:03:07', '小于');
INSERT INTO `system_dict_data` VALUES (202, 9, '小于等于', '9', 'da_api_param_operator', '', '', 'N', '0', 'qData', '2025-02-19 11:03:07', 'qData', '2025-02-19 11:03:07', '小于等于');
INSERT INTO `system_dict_data` VALUES (203, 10, '是否为空', '10', 'da_api_param_operator', '', '', 'N', '0', 'qData', '2025-02-19 11:03:07', 'qData', '2025-02-19 11:03:07', '是否为空');
INSERT INTO `system_dict_data` VALUES (204, 11, '是否不为空', '11', 'da_api_param_operator', '', '', 'N', '0', 'qData', '2025-02-19 11:03:07', 'qData', '2025-02-19 11:03:07', '是否不为空');
INSERT INTO `system_dict_data` VALUES (205, 12, 'IN', '12', 'da_api_param_operator', '', '', 'N', '0', 'qData', '2025-02-19 11:03:07', 'qData', '2025-02-19 11:03:07', 'IN');
INSERT INTO `system_dict_data` VALUES (206, 1, '并行', 'PARALLEL', 'dpp_etl_task_execution_type', NULL, 'default', 'N', '0', 'mengfanming', '2025-02-19 11:11:15', '', NULL, NULL);
INSERT INTO `system_dict_data` VALUES (207, 2, '串行等待', 'SERIAL_WAIT', 'dpp_etl_task_execution_type', NULL, 'default', 'N', '0', 'mengfanming', '2025-02-19 11:11:28', '', NULL, NULL);
INSERT INTO `system_dict_data` VALUES (208, 3, '串行抛弃', 'SERIAL_DISCARD', 'dpp_etl_task_execution_type', NULL, 'default', 'N', '0', 'mengfanming', '2025-02-19 11:11:43', '', NULL, NULL);
INSERT INTO `system_dict_data` VALUES (209, 4, '串行优先', 'SERIAL_PRIORITY', 'dpp_etl_task_execution_type', NULL, 'default', 'N', '0', 'mengfanming', '2025-02-19 11:12:07', '', NULL, NULL);
INSERT INTO `system_dict_data` VALUES (210, 0, '提交成功', '0', 'dpp_etl_node_instance', NULL, 'default', 'N', '1', 'shuyexin', '2025-02-26 10:38:46', 'shuyexin', '2025-02-27 18:07:03', NULL);
INSERT INTO `system_dict_data` VALUES (211, 1, '正在运行', '1', 'dpp_etl_node_instance', NULL, 'primary', 'N', '0', 'shuyexin', '2025-02-26 10:38:59', 'qData', '2025-03-10 10:21:24', NULL);
INSERT INTO `system_dict_data` VALUES (212, 3, '暂停', '3', 'dpp_etl_node_instance', NULL, 'default', 'N', '1', 'shuyexin', '2025-02-26 10:39:21', 'shuyexin', '2025-02-27 18:07:13', NULL);
INSERT INTO `system_dict_data` VALUES (213, 5, '停止', '5', 'dpp_etl_node_instance', NULL, 'default', 'N', '1', 'shuyexin', '2025-02-26 10:39:31', 'shuyexin', '2025-02-27 18:18:52', NULL);
INSERT INTO `system_dict_data` VALUES (214, 6, '失败', '6', 'dpp_etl_node_instance', NULL, 'danger', 'N', '0', 'shuyexin', '2025-02-26 10:40:15', 'qData', '2025-03-10 10:21:06', NULL);
INSERT INTO `system_dict_data` VALUES (215, 7, '成功', '7', 'dpp_etl_node_instance', NULL, 'success', 'N', '0', 'shuyexin', '2025-02-26 10:40:23', 'qData', '2025-03-10 10:21:00', NULL);
INSERT INTO `system_dict_data` VALUES (216, 8, '需要容错', '8', 'dpp_etl_node_instance', NULL, 'default', 'N', '1', 'shuyexin', '2025-02-26 10:40:31', 'shuyexin', '2025-02-27 18:07:31', NULL);
INSERT INTO `system_dict_data` VALUES (217, 9, 'Kill', '9', 'dpp_etl_node_instance', NULL, 'default', 'N', '1', 'shuyexin', '2025-02-26 10:40:40', 'shuyexin', '2025-02-27 18:07:42', NULL);
INSERT INTO `system_dict_data` VALUES (218, 12, '延时执行', '12', 'dpp_etl_node_instance', NULL, 'default', 'N', '1', 'shuyexin', '2025-02-26 10:40:47', 'shuyexin', '2025-02-27 18:07:35', NULL);
INSERT INTO `system_dict_data` VALUES (219, 13, '强制成功', '13', 'dpp_etl_node_instance', NULL, 'default', 'N', '1', 'shuyexin', '2025-02-26 10:40:55', 'shuyexin', '2025-02-27 18:07:38', NULL);
INSERT INTO `system_dict_data` VALUES (220, 17, '派发', '17', 'dpp_etl_node_instance', NULL, 'default', 'N', '1', 'shuyexin', '2025-02-26 10:41:04', 'shuyexin', '2025-02-27 18:08:03', NULL);
INSERT INTO `system_dict_data` VALUES (221, 0, '集成', 'DATAX', 'dpp_etl_node_type', NULL, 'primary', 'N', '0', 'shuyexin', '2025-02-27 18:31:08', 'qData', '2025-03-10 10:22:27', NULL);
INSERT INTO `system_dict_data` VALUES (222, 1, '转换', 'SPARK', 'dpp_etl_node_type', NULL, 'success', 'N', '0', 'shuyexin', '2025-02-27 18:31:28', 'qData', '2025-03-10 10:22:34', NULL);
INSERT INTO `system_dict_data` VALUES (223, 1, 'MySql', 'MySql', 'datasource_type', NULL, 'primary', 'N', '0', 'mengfanming', '2025-03-10 09:55:58', 'mengfanming', '2025-03-10 09:56:06', NULL);
INSERT INTO `system_dict_data` VALUES (224, 0, '数据开发任务', '3', 'dpp_etl_type', NULL, NULL, 'N', '0', '', NULL, '', NULL, NULL);
INSERT INTO `system_dict_data` VALUES (225, 0, '作业任务', '4', 'dpp_etl_type', NULL, NULL, 'N', '0', '', NULL, '', NULL, NULL);
INSERT INTO `system_dict_data` VALUES (226, 0, 'DATAX', 'DATAX', 'ddp_etl_node_type', NULL, NULL, 'N', '0', '', NULL, '', NULL, NULL);
INSERT INTO `system_dict_data` VALUES (227, 0, 'SPARK', 'SPARK', 'ddp_etl_node_type', NULL, NULL, 'N', '0', '', NULL, '', NULL, NULL);
INSERT INTO `system_dict_data` VALUES (228, 0, 'SUB_PROCESS', 'SUB_PROCESS', 'ddp_etl_node_type', NULL, NULL, 'N', '0', '', NULL, '', NULL, NULL);
INSERT INTO `system_dict_data` VALUES (229, 0, 'SQL', 'SQL', 'ddp_etl_node_type', NULL, NULL, 'N', '0', '', NULL, '', NULL, NULL);
INSERT INTO `system_dict_data` VALUES (230, 0, 'PROCEDURE', 'PROCEDURE', 'ddp_etl_node_type', NULL, NULL, 'N', '0', '', NULL, '', NULL, NULL);
INSERT INTO `system_dict_data` VALUES (231, 0, '已提交', '1', 'da_discovery_table_log_status', NULL, NULL, 'N', '0', '', NULL, '', NULL, NULL);
INSERT INTO `system_dict_data` VALUES (232, 0, '已撤销', '2', 'da_discovery_table_log_status', NULL, NULL, 'N', '0', '', NULL, '', NULL, NULL);
INSERT INTO `system_dict_data` VALUES (233, 0, '待审核', '1', 'da_asset_apply_status', NULL, NULL, 'N', '0', '', NULL, 'qData', '2025-03-20 15:03:17', NULL);
INSERT INTO `system_dict_data` VALUES (234, 1, '驳回', '2', 'da_asset_apply_status', NULL, 'danger', 'N', '0', '', NULL, 'qData', '2025-03-31 11:41:29', NULL);
INSERT INTO `system_dict_data` VALUES (235, 2, '通过', '3', 'da_asset_apply_status', NULL, 'success', 'N', '0', '', NULL, 'qData', '2025-03-31 11:41:35', NULL);
INSERT INTO `system_dict_data` VALUES (236, 4, 'Oracle11', 'Oracle11', 'datasource_type', NULL, 'primary', 'N', '0', 'mengfanming', '2025-03-10 15:30:34', '', NULL, NULL);
INSERT INTO `system_dict_data` VALUES (237, 0, '数据库输入', '1', 'dpp_etl_node_component_type', NULL, NULL, 'N', '0', '', NULL, '', NULL, NULL);
INSERT INTO `system_dict_data` VALUES (238, 0, 'EXCEL输入', '2', 'dpp_etl_node_component_type', NULL, NULL, 'N', '0', '', NULL, '', NULL, NULL);
INSERT INTO `system_dict_data` VALUES (239, 0, 'Kafka输入', '3', 'dpp_etl_node_component_type', NULL, NULL, 'N', '0', '', NULL, '', NULL, NULL);
INSERT INTO `system_dict_data` VALUES (240, 0, 'Spark清洗', '31', 'dpp_etl_node_component_type', NULL, NULL, 'N', '0', '', NULL, '', NULL, NULL);
INSERT INTO `system_dict_data` VALUES (241, 0, 'SQL开发', '51', 'dpp_etl_node_component_type', NULL, NULL, 'N', '0', '', NULL, '', NULL, NULL);
INSERT INTO `system_dict_data` VALUES (242, 0, '存储过程开发', '52', 'dpp_etl_node_component_type', NULL, NULL, 'N', '0', '', NULL, '', NULL, NULL);
INSERT INTO `system_dict_data` VALUES (243, 0, 'SparkSql开发', '53', 'dpp_etl_node_component_type', NULL, NULL, 'N', '0', '', NULL, '', NULL, NULL);
INSERT INTO `system_dict_data` VALUES (244, 0, '子任务', '71', 'dpp_etl_node_component_type', NULL, NULL, 'N', '0', '', NULL, '', NULL, NULL);
INSERT INTO `system_dict_data` VALUES (245, 0, '数据库输出', '91', 'dpp_etl_node_component_type', NULL, NULL, 'N', '0', '', NULL, '', NULL, NULL);
INSERT INTO `system_dict_data` VALUES (246, 21, 'Kafka', 'Kafka', 'datasource_type', NULL, 'primary', 'N', '0', 'qData', '2025-03-20 14:46:21', 'qData', '2025-03-20 14:46:51', NULL);
INSERT INTO `system_dict_data` VALUES (247, 0, '数据连接', '0', 'dpp_connection', NULL, 'primary', 'N', '0', 'shuyexin', '2025-03-24 13:43:11', '', NULL, NULL);
INSERT INTO `system_dict_data` VALUES (248, 0, '资产表', '1', 'dpp_connection', NULL, 'success', 'N', '0', 'shuyexin', '2025-03-24 13:43:23', '', NULL, NULL);
INSERT INTO `system_dict_data` VALUES (249, 0, '最高的', 'HIGHEST', 'dpp_etl_node_priority', NULL, NULL, 'N', '0', '', NULL, '', NULL, NULL);
INSERT INTO `system_dict_data` VALUES (250, 0, '高的', 'HIGH', 'dpp_etl_node_priority', NULL, NULL, 'N', '0', '', NULL, '', NULL, NULL);
INSERT INTO `system_dict_data` VALUES (251, 0, '中等的', 'MEDIUM', 'dpp_etl_node_priority', NULL, NULL, 'N', '0', '', NULL, '', NULL, NULL);
INSERT INTO `system_dict_data` VALUES (252, 0, '低的', 'LOW', 'dpp_etl_node_priority', NULL, NULL, 'N', '0', '', NULL, '', NULL, NULL);
INSERT INTO `system_dict_data` VALUES (253, 0, '最低的', 'LOWEST', 'dpp_etl_node_priority', NULL, NULL, 'N', '0', '', NULL, '', NULL, NULL);
INSERT INTO `system_dict_data` VALUES (254, 0, '启动任务流', '0', 'dpp_etl_task_instance_command_type', NULL, NULL, 'N', '0', '', NULL, 'qData', '2025-03-27 11:09:49', NULL);
INSERT INTO `system_dict_data` VALUES (255, 0, '从当前节点开始执行', '1', 'dpp_etl_task_instance_command_type', NULL, NULL, 'N', '0', '', NULL, '', NULL, NULL);
INSERT INTO `system_dict_data` VALUES (256, 0, '恢复被容错的工作流', '2', 'dpp_etl_task_instance_command_type', NULL, NULL, 'N', '0', '', NULL, '', NULL, NULL);
INSERT INTO `system_dict_data` VALUES (257, 0, '恢复暂停流程', '3', 'dpp_etl_task_instance_command_type', NULL, NULL, 'N', '0', '', NULL, '', NULL, NULL);
INSERT INTO `system_dict_data` VALUES (258, 0, '从失败节点开始执行', '4', 'dpp_etl_task_instance_command_type', NULL, NULL, 'N', '0', '', NULL, '', NULL, NULL);
INSERT INTO `system_dict_data` VALUES (259, 0, '补数', '5', 'dpp_etl_task_instance_command_type', NULL, NULL, 'N', '0', '', NULL, '', NULL, NULL);
INSERT INTO `system_dict_data` VALUES (260, 0, '调度', '6', 'dpp_etl_task_instance_command_type', NULL, NULL, 'N', '0', '', NULL, '', NULL, NULL);
INSERT INTO `system_dict_data` VALUES (261, 0, '重跑', '7', 'dpp_etl_task_instance_command_type', NULL, NULL, 'N', '0', '', NULL, '', NULL, NULL);
INSERT INTO `system_dict_data` VALUES (262, 0, '暂停', '8', 'dpp_etl_task_instance_command_type', NULL, NULL, 'N', '0', '', NULL, '', NULL, NULL);
INSERT INTO `system_dict_data` VALUES (263, 0, '停止', '9', 'dpp_etl_task_instance_command_type', NULL, NULL, 'N', '0', '', NULL, '', NULL, NULL);
INSERT INTO `system_dict_data` VALUES (264, 0, '恢复等待线程', '10', 'dpp_etl_task_instance_command_type', NULL, NULL, 'N', '0', '', NULL, '', NULL, NULL);
INSERT INTO `system_dict_data` VALUES (265, 0, '提交成功', '0', 'dpp_etl_task_instance', NULL, NULL, 'N', '0', '', NULL, '', NULL, NULL);
INSERT INTO `system_dict_data` VALUES (266, 0, '正在执行', '1', 'dpp_etl_task_instance', NULL, NULL, 'N', '0', '', NULL, '', NULL, NULL);
INSERT INTO `system_dict_data` VALUES (267, 0, '延时执行', '12', 'dpp_etl_task_instance', NULL, NULL, 'N', '1', '', NULL, 'qData', '2025-03-27 14:07:35', NULL);
INSERT INTO `system_dict_data` VALUES (268, 0, '串行等待', '14', 'dpp_etl_task_instance', NULL, NULL, 'N', '1', '', NULL, 'qData', '2025-03-27 14:07:40', NULL);
INSERT INTO `system_dict_data` VALUES (269, 0, '准备锁定', '15', 'dpp_etl_task_instance', NULL, NULL, 'N', '1', '', NULL, 'qData', '2025-03-27 14:07:44', NULL);
INSERT INTO `system_dict_data` VALUES (270, 0, '锁定', '16', 'dpp_etl_task_instance', NULL, NULL, 'N', '1', '', NULL, 'qData', '2025-03-27 14:07:48', NULL);
INSERT INTO `system_dict_data` VALUES (271, 0, '准备暂停', '2', 'dpp_etl_task_instance', NULL, NULL, 'N', '1', '', NULL, 'qData', '2025-03-27 14:07:52', NULL);
INSERT INTO `system_dict_data` VALUES (272, 0, '暂停', '3', 'dpp_etl_task_instance', NULL, NULL, 'N', '1', '', NULL, 'qData', '2025-03-27 14:07:55', NULL);
INSERT INTO `system_dict_data` VALUES (273, 0, '准备停止', '4', 'dpp_etl_task_instance', NULL, NULL, 'N', '1', '', NULL, 'qData', '2025-03-27 14:07:59', NULL);
INSERT INTO `system_dict_data` VALUES (274, 0, '停止', '5', 'dpp_etl_task_instance', NULL, NULL, 'N', '1', '', NULL, 'qData', '2025-03-27 14:08:02', NULL);
INSERT INTO `system_dict_data` VALUES (275, 0, '失败', '6', 'dpp_etl_task_instance', NULL, 'danger', 'N', '0', '', NULL, 'qData', '2025-03-27 14:05:54', NULL);
INSERT INTO `system_dict_data` VALUES (276, 0, '成功', '7', 'dpp_etl_task_instance', NULL, 'success', 'N', '0', '', NULL, 'qData', '2025-03-27 14:05:40', NULL);
INSERT INTO `system_dict_data` VALUES (277, 0, '数据发现', '1', 'da_asset_source', NULL, 'primary', 'N', '0', 'shuyexin', '2025-03-27 17:43:45', '', NULL, NULL);
INSERT INTO `system_dict_data` VALUES (278, 0, '数据模型', '2', 'da_asset_source', NULL, 'success', 'N', '0', 'shuyexin', '2025-03-27 17:43:57', '', NULL, NULL);
INSERT INTO `system_dict_data` VALUES (288, 1, '数据库表', '1', 'da_asset_type', NULL, 'primary', 'N', '0', 'qData', '2025-04-14 11:24:26', '', NULL, NULL);
INSERT INTO `system_dict_data` VALUES (289, 2, '外部API', '2', 'da_asset_type', NULL, 'primary', 'N', '0', 'qData', '2025-04-14 11:24:54', '', NULL, NULL);
INSERT INTO `system_dict_data` VALUES (290, 3, '地理空间服务', '3', 'da_asset_type', NULL, 'primary', 'N', '0', 'qData', '2025-04-14 11:25:42', '', NULL, NULL);
INSERT INTO `system_dict_data` VALUES (291, 4, '矢量数据', '4', 'da_asset_type', NULL, 'primary', 'N', '0', 'qData', '2025-04-14 11:25:56', '', NULL, NULL);
INSERT INTO `system_dict_data` VALUES (292, 5, '视频数据', '5', 'da_asset_type', NULL, 'primary', 'N', '0', 'qData', '2025-04-14 11:26:22', '', NULL, NULL);
INSERT INTO `system_dict_data` VALUES (293, 0, '行政区划', '1', 'att_perm_tier_type', NULL, 'primary', 'N', '0', 'qData', '2025-04-14 14:07:23', '', NULL, NULL);
INSERT INTO `system_dict_data` VALUES (294, 0, '流域机构', '2', 'att_perm_tier_type', NULL, 'success', 'N', '0', 'qData', '2025-04-14 14:07:41', '', NULL, NULL);
INSERT INTO `system_dict_data` VALUES (295, 1, 'String', 'String', 'da_asset_api_column_type', NULL, 'primary', 'N', '0', 'qData', '2025-04-18 14:53:15', '', NULL, NULL);
INSERT INTO `system_dict_data` VALUES (296, 2, 'Integer', 'Integer', 'da_asset_api_column_type', NULL, 'primary', 'N', '0', 'qData', '2025-04-18 14:53:26', '', NULL, NULL);
INSERT INTO `system_dict_data` VALUES (297, 3, 'Long', 'Long', 'da_asset_api_column_type', NULL, 'primary', 'N', '0', 'qData', '2025-04-18 14:53:36', '', NULL, NULL);
INSERT INTO `system_dict_data` VALUES (298, 4, 'Double', 'Double', 'da_asset_api_column_type', NULL, 'primary', 'N', '0', 'qData', '2025-04-18 14:53:47', '', NULL, NULL);
INSERT INTO `system_dict_data` VALUES (299, 5, 'Boolean', 'Boolean', 'da_asset_api_column_type', NULL, 'primary', 'N', '0', 'qData', '2025-04-18 14:53:57', '', NULL, NULL);
INSERT INTO `system_dict_data` VALUES (300, 6, 'Date', 'Date', 'da_asset_api_column_type', NULL, 'primary', 'N', '0', 'qData', '2025-04-18 14:54:09', '', NULL, NULL);
INSERT INTO `system_dict_data` VALUES (301, 7, 'Object', 'Object', 'da_asset_api_column_type', NULL, 'primary', 'N', '0', 'qData', '2025-04-18 14:54:22', '', NULL, NULL);
INSERT INTO `system_dict_data` VALUES (302, 8, 'Array', 'Array', 'da_asset_api_column_type', NULL, 'primary', 'N', '0', 'qData', '2025-04-18 14:54:33', '', NULL, NULL);
INSERT INTO `system_dict_data` VALUES (303, 1, 'GET', 'GET', 'da_asset_api_method', NULL, 'primary', 'N', '0', 'qData', '2025-04-18 16:55:49', '', NULL, NULL);
INSERT INTO `system_dict_data` VALUES (304, 2, 'POST', 'POST', 'da_asset_api_method', NULL, 'primary', 'N', '0', 'qData', '2025-04-18 16:56:02', '', NULL, NULL);
INSERT INTO `system_dict_data` VALUES (305, 1, 'geojson', 'geojson', 'da_asset_geo_file_type', NULL, 'primary', 'N', '0', 'qData', '2025-04-18 17:00:27', '', NULL, NULL);
INSERT INTO `system_dict_data` VALUES (306, 1, 'WMTS', '1', 'da_asset_gis_type', NULL, 'primary', 'N', '0', 'qData', '2025-04-18 17:08:01', '', NULL, NULL);
INSERT INTO `system_dict_data` VALUES (307, 1, '海康', '1', 'da_asset_video_platform', NULL, 'primary', 'N', '0', 'qData', '2025-04-18 17:22:29', '', NULL, NULL);
INSERT INTO `system_dict_data` VALUES (308, 2, '大华', '2', 'da_asset_video_platform', NULL, 'primary', 'N', '0', 'qData', '2025-04-18 17:22:46', '', NULL, NULL);
INSERT INTO `system_dict_data` VALUES (309, 0, '正常', '0', 'rp_status', NULL, 'default', 'N', '0', 'shuyexin', '2025-04-21 10:15:43', '', NULL, NULL);
INSERT INTO `system_dict_data` VALUES (310, 0, '停用', '1', 'rp_status', NULL, 'default', 'N', '0', 'shuyexin', '2025-04-21 10:15:52', '', NULL, NULL);
INSERT INTO `system_dict_data` VALUES (311, 0, 'sql', '2', 'dpp_connection', NULL, 'info', 'N', '0', 'qData', '2025-04-21 11:09:33', '', NULL, NULL);
INSERT INTO `system_dict_data` VALUES (312, 1, 'API资产服务', '1', 'ds_api_transmit_type', NULL, 'default', 'N', '0', 'qData', '2025-04-22 11:49:55', '', NULL, NULL);
INSERT INTO `system_dict_data` VALUES (313, 2, '地理空间数据', '2', 'ds_api_transmit_type', NULL, 'default', 'N', '0', 'qData', '2025-04-22 11:50:15', '', NULL, NULL);
INSERT INTO `system_dict_data` VALUES (314, 1, '基建资料', '1', 'rp_doc_type', NULL, 'default', 'N', '0', 'qData', '2025-04-28 10:42:43', 'qData', '2025-04-28 10:46:40', NULL);
INSERT INTO `system_dict_data` VALUES (315, 2, '监理资料', '2', 'rp_doc_type', NULL, 'default', 'N', '0', 'qData', '2025-04-28 10:42:59', 'qData', '2025-04-28 10:46:36', NULL);
INSERT INTO `system_dict_data` VALUES (316, 3, '施工资料', '3', 'rp_doc_type', NULL, 'default', 'N', '0', 'qData', '2025-04-28 10:43:47', 'qData', '2025-04-28 10:46:31', NULL);
INSERT INTO `system_dict_data` VALUES (317, 4, '竣工验收资料', '4', 'rp_doc_type', NULL, 'default', 'N', '0', 'qData', '2025-04-28 10:43:57', 'qData', '2025-04-28 10:46:26', NULL);
INSERT INTO `system_dict_data` VALUES (318, 5, '基础地理信息数据', '5', 'rp_doc_type', NULL, 'default', 'N', '0', 'qData', '2025-04-28 10:44:06', 'qData', '2025-04-28 10:46:21', NULL);
INSERT INTO `system_dict_data` VALUES (319, 6, '水文气象数据', '6', 'rp_doc_type', NULL, 'default', 'N', '0', 'qData', '2025-04-28 10:44:15', 'qData', '2025-04-28 10:46:17', NULL);
INSERT INTO `system_dict_data` VALUES (320, 7, '水利工程数据', '7', 'rp_doc_type', NULL, 'default', 'N', '0', 'qData', '2025-04-28 10:44:26', 'qData', '2025-04-28 10:46:12', NULL);
INSERT INTO `system_dict_data` VALUES (321, 8, '水质监测数据', '8', 'rp_doc_type', NULL, 'default', 'N', '0', 'qData', '2025-04-28 10:44:34', 'qData', '2025-04-28 10:46:07', NULL);
INSERT INTO `system_dict_data` VALUES (322, 9, '水资源管理数据', '9', 'rp_doc_type', '', 'default', 'N', '0', 'qData', '2025-04-28 10:44:45', 'qData', '2025-04-28 10:45:05', NULL);
INSERT INTO `system_dict_data` VALUES (323, 10, '社会经济数据', '10', 'rp_doc_type', NULL, 'default', 'N', '0', 'qData', '2025-04-28 10:45:14', '', NULL, NULL);
INSERT INTO `system_dict_data` VALUES (324, 11, '协议类资料', '11', 'rp_doc_type', NULL, 'default', 'N', '0', 'qData', '2025-04-28 10:45:38', '', NULL, NULL);
INSERT INTO `system_dict_data` VALUES (325, 12, '信函类资料', '12', 'rp_doc_type', NULL, 'default', 'N', '0', 'qData', '2025-04-28 10:45:45', '', NULL, NULL);
INSERT INTO `system_dict_data` VALUES (326, 13, '质量与安全资料', '13', 'rp_doc_type', NULL, 'default', 'N', '0', 'qData', '2025-04-28 10:45:52', '', NULL, NULL);
INSERT INTO `system_dict_data` VALUES (327, 14, '技术与管理类资料', '14', 'rp_doc_type', NULL, 'default', 'N', '0', 'qData', '2025-04-28 10:45:59', '', NULL, NULL);
INSERT INTO `system_dict_data` VALUES (328, 0, '行政区划', '0', 'rp_area_type', NULL, 'default', 'N', '0', 'qData', '2025-04-30 10:04:32', '', NULL, NULL);
INSERT INTO `system_dict_data` VALUES (329, 0, '流域机构', '1', 'rp_area_type', NULL, 'default', 'N', '0', 'qData', '2025-04-30 10:04:41', '', NULL, NULL);
INSERT INTO `system_dict_data` VALUES (330, 0, '职能型部门', '0', 'rp_dept_type', NULL, 'default', 'N', '0', 'liuhaosheng', '2025-04-30 17:49:29', '', NULL, NULL);
INSERT INTO `system_dict_data` VALUES (331, 1, '生产运营型部门', '1', 'rp_dept_type', NULL, 'default', 'N', '0', 'liuhaosheng', '2025-04-30 17:49:43', '', NULL, NULL);
INSERT INTO `system_dict_data` VALUES (332, 2, '技术研发型部门', '2', 'rp_dept_type', NULL, 'default', 'N', '0', 'liuhaosheng', '2025-04-30 17:49:57', '', NULL, NULL);
INSERT INTO `system_dict_data` VALUES (333, 3, '区域地理型部门', '3', 'rp_dept_type', NULL, 'default', 'N', '0', 'liuhaosheng', '2025-04-30 17:50:09', '', NULL, NULL);
INSERT INTO `system_dict_data` VALUES (334, 5, '专业服务型部门', '5', 'rp_dept_type', NULL, 'default', 'N', '0', 'liuhaosheng', '2025-04-30 17:50:21', '', NULL, NULL);
INSERT INTO `system_dict_data` VALUES (335, 6, 'Hive', 'Hive', 'datasource_type', NULL, 'primary', 'N', '0', 'qData', '2025-05-08 14:57:51', '', NULL, NULL);
INSERT INTO `system_dict_data` VALUES (336, 7, 'HDFS', 'HDFS', 'datasource_type', NULL, 'primary', 'N', '0', 'qData', '2025-05-08 14:58:45', '', NULL, NULL);
INSERT INTO `system_dict_data` VALUES (337, 1, '新增', '1', 'da_asset_operate_type', NULL, 'primary', 'N', '0', 'qData', '2025-05-09 15:10:32', '', NULL, NULL);
INSERT INTO `system_dict_data` VALUES (338, 2, '修改', '2', 'da_asset_operate_type', NULL, 'success', 'N', '0', 'qData', '2025-05-09 15:10:41', '', NULL, NULL);
INSERT INTO `system_dict_data` VALUES (339, 1, '执行中', '1', 'da_asset_operate_status', NULL, 'warning', 'N', '0', 'qData', '2025-05-20 11:02:02', '', NULL, NULL);
INSERT INTO `system_dict_data` VALUES (340, 2, '失败', '2', 'da_asset_operate_status', NULL, 'danger', 'N', '0', 'qData', '2025-05-20 11:02:13', '', NULL, NULL);
INSERT INTO `system_dict_data` VALUES (341, 3, '成功', '3', 'da_asset_operate_status', NULL, 'success', 'N', '0', 'qData', '2025-05-20 11:02:26', '', NULL, NULL);
INSERT INTO `system_dict_data` VALUES (342, 4, '回滚失败', '4', 'da_asset_operate_status', NULL, 'danger', 'N', '0', 'qData', '2025-05-20 11:02:42', 'qData', '2025-05-20 11:02:48', NULL);
INSERT INTO `system_dict_data` VALUES (343, 5, '回滚成功', '5', 'da_asset_operate_status', NULL, 'success', 'N', '0', 'qData', '2025-05-20 11:03:00', '', NULL, NULL);

-- ----------------------------
-- Table structure for system_dict_type
-- ----------------------------
DROP TABLE IF EXISTS `system_dict_type`;
CREATE TABLE `system_dict_type`  (
  `dict_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '字典主键',
  `dict_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '字典名称',
  `dict_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '字典类型',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '状态（0正常 1停用）',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`dict_id`) USING BTREE,
  UNIQUE INDEX `DICT_ID_90022127184400`(`dict_id`) USING BTREE,
  UNIQUE INDEX `dict_type`(`dict_type`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 95 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '字典类型表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of system_dict_type
-- ----------------------------
INSERT INTO `system_dict_type` VALUES (1, '用户性别', 'sys_user_sex', '0', 'qData', '2024-05-06 06:12:18', 'qData', '2025-03-07 14:06:58', '用户性别列表');
INSERT INTO `system_dict_type` VALUES (2, '菜单状态', 'sys_show_hide', '0', 'qData', '2024-05-06 06:12:18', '', NULL, '菜单状态列表');
INSERT INTO `system_dict_type` VALUES (3, '系统开关', 'sys_normal_disable', '0', 'qData', '2024-05-06 06:12:18', '', NULL, '系统开关列表');
INSERT INTO `system_dict_type` VALUES (4, '任务状态', 'sys_job_status', '0', 'qData', '2024-05-06 06:12:18', '', NULL, '任务状态列表');
INSERT INTO `system_dict_type` VALUES (5, '任务分组', 'sys_job_group', '0', 'qData', '2024-05-06 06:12:18', '', NULL, '任务分组列表');
INSERT INTO `system_dict_type` VALUES (6, '系统是否', 'sys_yes_no', '0', 'qData', '2024-05-06 06:12:18', '', NULL, '系统是否列表');
INSERT INTO `system_dict_type` VALUES (7, '通知类型', 'sys_notice_type', '0', 'qData', '2024-05-06 06:12:18', '', NULL, '通知类型列表');
INSERT INTO `system_dict_type` VALUES (8, '通知状态', 'sys_notice_status', '0', 'qData', '2024-05-06 06:12:18', '', NULL, '通知状态列表');
INSERT INTO `system_dict_type` VALUES (9, '操作类型', 'sys_oper_type', '0', 'qData', '2024-05-06 06:12:18', '', NULL, '操作类型列表');
INSERT INTO `system_dict_type` VALUES (10, '系统状态', 'sys_common_status', '0', 'qData', '2024-05-06 06:12:18', '', NULL, '登录状态列表');
INSERT INTO `system_dict_type` VALUES (11, '应用类型', 'auth_app_type', '0', 'qData', '2024-08-31 14:27:03', '', NULL, '应用类型列表');
INSERT INTO `system_dict_type` VALUES (12, '是否有效', 'sys_valid', '0', 'qData', '2024-08-31 15:33:08', 'qData', '2024-08-31 15:36:13', '系统有效列表');
INSERT INTO `system_dict_type` VALUES (13, '是否公开', 'auth_public', '0', 'qData', '2024-08-31 15:35:49', 'qData', '2024-08-31 15:36:00', '应用是否公开');
INSERT INTO `system_dict_type` VALUES (14, '消息类型', 'message_category', '0', 'qData', '2024-11-07 14:27:58', '', NULL, NULL);
INSERT INTO `system_dict_type` VALUES (15, '消息等级', 'message_level', '0', 'qData', '2024-11-07 14:28:09', '', NULL, NULL);
INSERT INTO `system_dict_type` VALUES (16, '布尔值字典', 'sys_boolean', '0', 'qData', '2024-12-03 15:46:14', '', NULL, NULL);
INSERT INTO `system_dict_type` VALUES (17, '项目状态', 'project_status', '0', 'qData', '2024-12-30 16:33:15', '', NULL, NULL);
INSERT INTO `system_dict_type` VALUES (18, '故事状态', 'story_status', '0', 'qData', '2024-12-31 11:15:41', '', NULL, NULL);
INSERT INTO `system_dict_type` VALUES (19, '优先级', 'project_priority', '0', 'qData', '2025-01-03 13:20:13', '', NULL, NULL);
INSERT INTO `system_dict_type` VALUES (20, '严重程度', 'project_severity', '0', 'qData', '2025-01-03 13:33:42', '', NULL, NULL);
INSERT INTO `system_dict_type` VALUES (21, '缺陷状态', 'defect_status', '0', 'qData', '2025-01-03 14:22:35', '', NULL, NULL);
INSERT INTO `system_dict_type` VALUES (22, '任务状态', 'task_status', '0', 'qData', '2025-01-03 14:54:24', '', NULL, NULL);
INSERT INTO `system_dict_type` VALUES (23, '报工状态', 'day_report', '0', 'qData', '2025-01-06 11:41:28', 'qData', '2025-01-07 09:37:27', NULL);
INSERT INTO `system_dict_type` VALUES (24, '报工审核状态', 'report_status', '0', 'qData', '2025-01-06 15:52:09', 'qData', '2025-01-07 09:37:34', NULL);
INSERT INTO `system_dict_type` VALUES (25, '是否发布', 'dp_model_status', '0', 'qData', '2025-01-21 09:44:05', '', NULL, NULL);
INSERT INTO `system_dict_type` VALUES (26, '模型创建方式', 'dp_model_create_type', '0', 'qData', '2025-01-21 09:48:54', '', NULL, NULL);
INSERT INTO `system_dict_type` VALUES (27, '标准数据元-类型', 'dp_data_elem_code_type', '0', 'qData', '2025-01-21 10:04:38', '', NULL, NULL);
INSERT INTO `system_dict_type` VALUES (28, '标准数据元数据规则关联信息表-规则类型', 'dp_data_elem_rules_type', '0', 'qData', '2025-01-21 10:04:59', '', NULL, NULL);
INSERT INTO `system_dict_type` VALUES (29, '逻辑模型-上线状态', 'dp_template_online_status', '0', 'qData', '2025-01-21 10:05:23', '', NULL, NULL);
INSERT INTO `system_dict_type` VALUES (30, '逻辑模型-数据来源类型', 'dp_template_data_source_type', '0', 'qData', '2025-01-21 10:05:44', '', NULL, NULL);
INSERT INTO `system_dict_type` VALUES (31, '物化模型任务-状态标志', 'dp_build_log_build_status', '0', 'qData', '2025-01-21 10:06:11', '', NULL, NULL);
INSERT INTO `system_dict_type` VALUES (32, '物化模型基础表-状态标志', 'dp_template_build_log_build_status', '0', 'qData', '2025-01-21 10:06:31', '', NULL, NULL);
INSERT INTO `system_dict_type` VALUES (33, '模型属性是否主键', 'dp_model_column_pk_flag', '0', 'qData', '2025-01-21 10:42:10', '', NULL, NULL);
INSERT INTO `system_dict_type` VALUES (34, '模型属性是否必填', 'dp_model_column_nullable_flag', '0', 'qData', '2025-01-21 10:42:49', '', NULL, NULL);
INSERT INTO `system_dict_type` VALUES (37, '数据资产表-状态', 'da_assets_status', '0', '', NULL, 'qData', '2025-01-21 11:57:53', NULL);
INSERT INTO `system_dict_type` VALUES (38, '稽查规则维度', 'att_rule_audit_q_dimension', '0', '', NULL, '', NULL, NULL);
INSERT INTO `system_dict_type` VALUES (39, '稽查规则类型字典', 'att_rule_audit_type', '0', '', NULL, '', NULL, NULL);
INSERT INTO `system_dict_type` VALUES (40, '规则级别', 'att_rule_level', '0', '', NULL, '', NULL, NULL);
INSERT INTO `system_dict_type` VALUES (41, '清洗规则类型', 'att_rule_clean_type', '0', '', NULL, '', NULL, NULL);
INSERT INTO `system_dict_type` VALUES (44, '敏感规则', 'da_sensitive_level_rule', '0', '', NULL, '', NULL, NULL);
INSERT INTO `system_dict_type` VALUES (45, '数据资产发布审核记录表-审核状态', 'da_assets_audit_status1', '0', '', NULL, '', NULL, NULL);
INSERT INTO `system_dict_type` VALUES (48, '数据库-字段类型', 'column_type', '0', 'qData', '2025-01-21 13:46:57', '', NULL, NULL);
INSERT INTO `system_dict_type` VALUES (49, '启用禁用', 'sys_disable', '0', 'qData', '2025-01-21 15:33:47', '', NULL, NULL);
INSERT INTO `system_dict_type` VALUES (50, '数据源类型', 'datasource_type', '0', 'qData', '2025-01-22 10:22:08', '', NULL, NULL);
INSERT INTO `system_dict_type` VALUES (51, '敏感等级上下线标识', 'da_sensitive_status', '0', 'liuhaosheng', '2025-02-06 09:54:31', '', NULL, NULL);
INSERT INTO `system_dict_type` VALUES (52, 'API服务-API服务状态', 'ds_api_log_status', '0', '', NULL, '', NULL, NULL);
INSERT INTO `system_dict_type` VALUES (53, 'API服务-API请求方式类型', 'ds_api_bas_info_api_method_type', '0', '', NULL, '', NULL, NULL);
INSERT INTO `system_dict_type` VALUES (54, 'API服务-API服务类型', 'ds_api_bas_info_api_service_type', '0', '', NULL, '', NULL, NULL);
INSERT INTO `system_dict_type` VALUES (55, 'API服务-API返回数据展示类型', 'ds_api_bas_info_res_data_type', '0', '', NULL, '', NULL, NULL);
INSERT INTO `system_dict_type` VALUES (57, 'API服务调用日志-API请求状态', 'ds_api_log_res_status', '0', '', NULL, '', NULL, NULL);
INSERT INTO `system_dict_type` VALUES (58, '数据发现任务状态', 'da_discovery_task_status', '0', 'mengfanming', '2025-02-13 14:47:28', 'mengfanming', '2025-02-13 14:54:33', NULL);
INSERT INTO `system_dict_type` VALUES (59, '数据发现库表信息-表结构标识', 'da_discovery_table_change_flag', '0', 'mengfanming', '2025-02-13 14:56:15', '', NULL, NULL);
INSERT INTO `system_dict_type` VALUES (60, '数据发现库表信息-状态', 'da_discovery_table_change_status', '0', 'mengfanming', '2025-02-13 15:01:19', '', NULL, NULL);
INSERT INTO `system_dict_type` VALUES (61, '数据发现库表信息-是否忽略', 'da_discovery_table_change_ignore_flag', '0', 'mengfanming', '2025-02-13 15:02:59', '', NULL, NULL);
INSERT INTO `system_dict_type` VALUES (62, '数据发现任务日志表状态', 'da_discovery_task_log_status', '0', 'mengfanming', '2025-02-18 13:50:03', '', NULL, NULL);
INSERT INTO `system_dict_type` VALUES (63, 'API服务状态', 'ds_api_status', '0', 'qData', '2025-02-18 18:32:56', '', NULL, NULL);
INSERT INTO `system_dict_type` VALUES (64, 'API服务限流状态', 'ds_api_limit_status', '0', 'qData', '2025-02-18 18:37:26', '', NULL, NULL);
INSERT INTO `system_dict_type` VALUES (65, '数据集成任务-状态', 'dpp_etl_task_status', '0', 'mengfanming', '2025-02-19 09:58:50', '', NULL, '任务状态');
INSERT INTO `system_dict_type` VALUES (66, '参数类型', 'ds_api_param_type', '0', 'qData', '2025-02-19 10:21:34', '', NULL, 'api服务参数类型');
INSERT INTO `system_dict_type` VALUES (67, 'API参数操作符', 'da_api_param_operator', '0', 'qData', '2025-02-19 10:27:57', '', NULL, 'API参数操作符');
INSERT INTO `system_dict_type` VALUES (68, '数据集成任务-任务的执行策略', 'dpp_etl_task_execution_type', '0', 'mengfanming', '2025-02-19 11:10:09', '', NULL, NULL);
INSERT INTO `system_dict_type` VALUES (69, '数据集成节点执行状态', 'dpp_etl_node_instance', '0', 'shuyexin', '2025-02-26 10:37:46', '', NULL, NULL);
INSERT INTO `system_dict_type` VALUES (70, '数据集成节点执行类型', 'dpp_etl_node_type', '0', 'shuyexin', '2025-02-27 18:30:55', '', NULL, NULL);
INSERT INTO `system_dict_type` VALUES (71, '数据集成任务-节点类型', 'ddp_etl_node_type', '0', '', NULL, '', NULL, NULL);
INSERT INTO `system_dict_type` VALUES (72, '数据发现库表提交记录-状态', 'da_discovery_table_log_status', '0', '', NULL, '', NULL, NULL);
INSERT INTO `system_dict_type` VALUES (73, '资产申请表-状态', 'da_asset_apply_status', '0', '', NULL, '', NULL, NULL);
INSERT INTO `system_dict_type` VALUES (74, '数据集成-组件类型', 'dpp_etl_node_component_type', '0', '', NULL, '', NULL, NULL);
INSERT INTO `system_dict_type` VALUES (75, '数据研发的连接方式', 'dpp_connection', '0', 'shuyexin', '2025-03-24 13:42:43', '', NULL, NULL);
INSERT INTO `system_dict_type` VALUES (76, '数据集成节点-节点优先级', 'dpp_etl_node_priority', '0', '', NULL, '', NULL, NULL);
INSERT INTO `system_dict_type` VALUES (77, '数据集成任务实例-运行类型', 'dpp_etl_task_instance_command_type', '0', '', NULL, '', NULL, NULL);
INSERT INTO `system_dict_type` VALUES (78, '数据集成任务实例状态', 'dpp_etl_task_instance', '0', '', NULL, '', NULL, NULL);
INSERT INTO `system_dict_type` VALUES (79, '资产地图数据来源', 'da_asset_source', '0', 'shuyexin', '2025-03-27 17:43:12', '', NULL, NULL);
INSERT INTO `system_dict_type` VALUES (81, '资产类型', 'da_asset_type', '0', 'qData', '2025-04-14 11:20:16', 'qData', '2025-04-14 13:53:42', NULL);
INSERT INTO `system_dict_type` VALUES (82, '权限层级-类型', 'att_perm_tier_type', '0', 'qData', '2025-04-14 14:06:54', '', NULL, NULL);
INSERT INTO `system_dict_type` VALUES (83, '数据资产api字段类型', 'da_asset_api_column_type', '0', 'qData', '2025-04-18 14:52:50', '', NULL, NULL);
INSERT INTO `system_dict_type` VALUES (84, '资产地图地理空间服务类型', 'da_asset_gis_type', '0', 'qData', '2025-04-18 15:47:04', 'qData', '2025-04-18 15:47:23', NULL);
INSERT INTO `system_dict_type` VALUES (85, '数据资产-api-请求方式', 'da_asset_api_method', '0', 'qData', '2025-04-18 16:55:30', '', NULL, NULL);
INSERT INTO `system_dict_type` VALUES (86, '资产地图-矢量文件类型', 'da_asset_geo_file_type', '0', 'qData', '2025-04-18 17:00:06', '', NULL, NULL);
INSERT INTO `system_dict_type` VALUES (87, '资产地图-视频平台', 'da_asset_video_platform', '0', 'qData', '2025-04-18 17:22:01', '', NULL, NULL);
INSERT INTO `system_dict_type` VALUES (88, '服务资源门户状态', 'rp_status', '0', 'shuyexin', '2025-04-21 10:15:17', '', NULL, NULL);
INSERT INTO `system_dict_type` VALUES (89, '数据服务-api-服务转发类型', 'ds_api_transmit_type', '0', 'qData', '2025-04-22 11:48:58', '', NULL, NULL);
INSERT INTO `system_dict_type` VALUES (90, '文档类型/资料类型', 'rp_doc_type', '0', 'qData', '2025-04-28 10:41:24', '', NULL, NULL);
INSERT INTO `system_dict_type` VALUES (91, '区域类型', 'rp_area_type', '0', 'qData', '2025-04-30 09:58:12', '', NULL, NULL);
INSERT INTO `system_dict_type` VALUES (92, '部门类型', 'rp_dept_type', '0', 'liuhaosheng', '2025-04-30 17:48:56', '', NULL, NULL);
INSERT INTO `system_dict_type` VALUES (93, '数据资产-数据操作类型', 'da_asset_operate_type', '0', 'qData', '2025-05-09 15:09:56', '', NULL, NULL);
INSERT INTO `system_dict_type` VALUES (94, '数据资产操作记录状态', 'da_asset_operate_status', '0', 'qData', '2025-05-20 11:01:25', '', NULL, NULL);

-- ----------------------------
-- Table structure for system_job
-- ----------------------------
DROP TABLE IF EXISTS `system_job`;
CREATE TABLE `system_job`  (
  `job_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '任务ID',
  `job_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '任务名称',
  `job_group` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'DEFAULT' COMMENT '任务组名',
  `invoke_target` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '调用目标字符串',
  `cron_expression` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT 'cron执行表达式',
  `misfire_policy` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '3' COMMENT '计划执行错误策略（1立即执行 2执行一次 3放弃执行）',
  `concurrent` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '1' COMMENT '是否并发执行（0允许 1禁止）',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '状态（0正常 1暂停）',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '备注信息',
  PRIMARY KEY (`job_id`, `job_name`, `job_group`) USING BTREE,
  UNIQUE INDEX `JOB_ID_90022634483400`(`job_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '定时任务调度表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of system_job
-- ----------------------------

-- ----------------------------
-- Table structure for system_job_log
-- ----------------------------
DROP TABLE IF EXISTS `system_job_log`;
CREATE TABLE `system_job_log`  (
  `job_log_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '任务日志ID',
  `job_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '任务名称',
  `job_group` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '任务组名',
  `invoke_target` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '调用目标字符串',
  `job_message` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '日志信息',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '执行状态（0正常 1失败）',
  `exception_info` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '异常信息',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`job_log_id`) USING BTREE,
  UNIQUE INDEX `JOB_LOG_ID_90023008430000`(`job_log_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '定时任务调度日志表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of system_job_log
-- ----------------------------

-- ----------------------------
-- Table structure for system_logininfor
-- ----------------------------
DROP TABLE IF EXISTS `system_logininfor`;
CREATE TABLE `system_logininfor`  (
  `info_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '访问ID',
  `user_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '用户账号',
  `ipaddr` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '登录IP地址',
  `login_location` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '登录地点',
  `browser` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '浏览器类型',
  `os` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '操作系统',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '登录状态（0成功 1失败）',
  `msg` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '提示消息',
  `login_time` datetime NULL DEFAULT NULL COMMENT '访问时间',
  PRIMARY KEY (`info_id`) USING BTREE,
  UNIQUE INDEX `INFO_ID_90023379342600`(`info_id`) USING BTREE,
  INDEX `idx_sys_logininfor_lt`(`login_time`) USING BTREE,
  INDEX `idx_sys_logininfor_s`(`status`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统访问记录' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of system_logininfor
-- ----------------------------

-- ----------------------------
-- Table structure for system_menu
-- ----------------------------
DROP TABLE IF EXISTS `system_menu`;
CREATE TABLE `system_menu`  (
  `menu_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '菜单ID',
  `menu_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '菜单名称',
  `parent_id` bigint(20) NULL DEFAULT 0 COMMENT '父菜单ID',
  `order_num` int(11) NULL DEFAULT 0 COMMENT '显示顺序',
  `path` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '路由地址',
  `component` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '组件路径',
  `query` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '路由参数',
  `is_frame` int(11) NULL DEFAULT 1 COMMENT '是否为外链（0是 1否）',
  `is_cache` int(11) NULL DEFAULT 0 COMMENT '是否缓存（0缓存 1不缓存）',
  `route_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '路由名称',
  `menu_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '菜单类型（M目录 C菜单 F按钮）',
  `visible` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '菜单状态（0显示 1隐藏）',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '菜单状态（0正常 1停用）',
  `perms` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '权限标识',
  `icon` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '#' COMMENT '菜单图标',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '备注',
  PRIMARY KEY (`menu_id`) USING BTREE,
  UNIQUE INDEX `MENU_ID_90023726248500`(`menu_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2555 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '菜单权限表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of system_menu
-- ----------------------------
INSERT INTO `system_menu` VALUES (1, '系统管理', 0, 998, 'system', NULL, '', 1, 0, NULL, 'M', '0', '0', '', 'settings-line', 'qData', '2024-05-06 06:12:17', 'admin', '2025-05-30 16:15:42', '系统管理目录');
INSERT INTO `system_menu` VALUES (2, '系统监控', 0, 999, 'monitor', NULL, '', 1, 0, NULL, 'M', '0', '0', '', 'slideshow-3-line', 'qData', '2024-05-06 06:12:17', 'admin', '2025-05-30 16:16:33', '系统监控目录');
INSERT INTO `system_menu` VALUES (3, '系统工具', 0, 1000, 'tool', NULL, '', 1, 0, NULL, 'M', '0', '0', '', 'briefcase-2-line', 'qData', '2024-05-06 06:12:17', 'admin', '2025-05-30 16:17:04', '系统工具目录');
INSERT INTO `system_menu` VALUES (100, '用户管理', 1, 1, 'user', 'system/system/user/index', '', 1, 0, NULL, 'C', '0', '0', 'system:user:list', 'user-3-line', 'qData', '2024-05-06 06:12:17', 'admin', '2025-05-30 16:19:36', '用户管理菜单');
INSERT INTO `system_menu` VALUES (101, '角色管理', 1, 2, 'role', 'system/system/role/index', '', 1, 0, NULL, 'C', '0', '0', 'system:role:list', 'group-3-line', 'qData', '2024-05-06 06:12:17', 'admin', '2025-05-30 16:20:14', '角色管理菜单');
INSERT INTO `system_menu` VALUES (102, '菜单管理', 1, 3, 'menu', 'system/system/menu/index', '', 1, 0, NULL, 'C', '0', '0', 'system:menu:list', 'list-view', 'qData', '2024-05-06 06:12:17', 'admin', '2025-05-30 16:20:45', '菜单管理菜单');
INSERT INTO `system_menu` VALUES (103, '部门管理', 1, 4, 'dept', 'system/system/dept/index', '', 1, 0, NULL, 'C', '0', '0', 'system:dept:list', 'contacts-book-line', 'qData', '2024-05-06 06:12:17', 'admin', '2025-05-30 16:21:21', '部门管理菜单');
INSERT INTO `system_menu` VALUES (104, '岗位管理', 1, 5, 'post', 'system/system/post/index', '', 1, 0, NULL, 'C', '0', '0', 'system:post:list', 'node-tree', 'qData', '2024-05-06 06:12:17', 'admin', '2025-05-30 16:22:21', '岗位管理菜单');
INSERT INTO `system_menu` VALUES (105, '字典管理', 1, 6, 'dict', 'system/system/dict/index', '', 1, 0, NULL, 'C', '0', '0', 'system:dict:list', 'book-2-line', 'qData', '2024-05-06 06:12:17', 'admin', '2025-05-30 16:23:00', '字典管理菜单');
INSERT INTO `system_menu` VALUES (106, '参数设置', 1, 7, 'config', 'system/system/config/index', '', 1, 0, NULL, 'C', '0', '0', 'system:config:list', 'exchange-funds-line', 'qData', '2024-05-06 06:12:17', 'admin', '2025-05-30 16:23:21', '参数设置菜单');
INSERT INTO `system_menu` VALUES (107, '通知公告', 1, 8, 'notice', 'system/system/notice/index', '', 1, 0, NULL, 'C', '0', '0', 'system:notice:list', 'megaphone-line', 'qData', '2024-05-06 06:12:17', 'admin', '2025-05-30 16:23:44', '通知公告菜单');
INSERT INTO `system_menu` VALUES (108, '日志管理', 1, 9, 'log', '', '', 1, 0, NULL, 'M', '0', '0', '', 'book-marked-line', 'qData', '2024-05-06 06:12:17', 'admin', '2025-05-30 16:24:05', '日志管理菜单');
INSERT INTO `system_menu` VALUES (109, '在线用户', 2, 1, 'online', 'system/monitor/online/index', '', 1, 0, NULL, 'C', '0', '0', 'monitor:online:list', 'user-3-line', 'qData', '2024-05-06 06:12:17', 'admin', '2025-05-30 16:24:40', '在线用户菜单');
INSERT INTO `system_menu` VALUES (110, '定时任务', 2, 2, 'job', 'system/monitor/job/index', '', 1, 0, NULL, 'C', '0', '0', 'monitor:job:list', 'calendar-schedule-line', 'qData', '2024-05-06 06:12:17', 'admin', '2025-05-30 16:25:01', '定时任务菜单');
INSERT INTO `system_menu` VALUES (112, '服务监控', 2, 4, 'server', 'system/monitor/server/index', '', 1, 0, NULL, 'C', '0', '0', 'monitor:server:list', 'slideshow-4-line', 'qData', '2024-05-06 06:12:17', 'admin', '2025-05-30 16:25:22', '服务监控菜单');
INSERT INTO `system_menu` VALUES (113, '缓存监控', 2, 5, 'cache', 'system/monitor/cache/index', '', 1, 0, NULL, 'C', '0', '0', 'monitor:cache:list', 'slideshow-2-line', 'qData', '2024-05-06 06:12:17', 'admin', '2025-05-30 16:26:03', '缓存监控菜单');
INSERT INTO `system_menu` VALUES (114, '缓存列表', 2, 6, 'cacheList', 'system/monitor/cache/list', '', 1, 0, NULL, 'C', '0', '0', 'monitor:cache:list', 'file-list-2-line', 'qData', '2024-05-06 06:12:17', 'admin', '2025-05-30 16:26:33', '缓存列表菜单');
INSERT INTO `system_menu` VALUES (116, '代码生成', 3, 2, 'gen', 'system/tool/gen/index', '', 1, 0, NULL, 'C', '0', '0', 'tool:gen:list', 'code-box-line', 'qData', '2024-05-06 06:12:17', 'admin', '2025-05-30 16:28:00', '代码生成菜单');
INSERT INTO `system_menu` VALUES (117, '系统接口', 3, 3, 'swagger', 'system/tool/swagger/index', '', 1, 0, NULL, 'C', '0', '0', 'tool:swagger:list', 'equalizer-line', 'qData', '2024-05-06 06:12:17', 'admin', '2025-05-30 16:28:18', '系统接口菜单');
INSERT INTO `system_menu` VALUES (500, '操作日志', 108, 1, 'operlog', 'system/monitor/operlog/index', '', 1, 0, NULL, 'C', '0', '0', 'monitor:operlog:list', '#', 'qData', '2024-05-06 06:12:17', '', NULL, '操作日志菜单');
INSERT INTO `system_menu` VALUES (501, '登录日志', 108, 2, 'logininfor', 'system/monitor/logininfor/index', '', 1, 0, NULL, 'C', '0', '0', 'monitor:logininfor:list', '#', 'qData', '2024-05-06 06:12:17', '', NULL, '登录日志菜单');
INSERT INTO `system_menu` VALUES (1000, '用户查询', 100, 1, '', '', '', 1, 0, NULL, 'F', '0', '0', 'system:user:query', '#', 'qData', '2024-05-06 06:12:17', '', NULL, '');
INSERT INTO `system_menu` VALUES (1001, '用户新增', 100, 2, '', '', '', 1, 0, NULL, 'F', '0', '0', 'system:user:add', '#', 'qData', '2024-05-06 06:12:17', '', NULL, '');
INSERT INTO `system_menu` VALUES (1002, '用户修改', 100, 3, '', '', '', 1, 0, NULL, 'F', '0', '0', 'system:user:edit', '#', 'qData', '2024-05-06 06:12:17', '', NULL, '');
INSERT INTO `system_menu` VALUES (1003, '用户删除', 100, 4, '', '', '', 1, 0, NULL, 'F', '0', '0', 'system:user:remove', '#', 'qData', '2024-05-06 06:12:17', '', NULL, '');
INSERT INTO `system_menu` VALUES (1004, '用户导出', 100, 5, '', '', '', 1, 0, NULL, 'F', '0', '0', 'system:user:export', '#', 'qData', '2024-05-06 06:12:17', '', NULL, '');
INSERT INTO `system_menu` VALUES (1005, '用户导入', 100, 6, '', '', '', 1, 0, NULL, 'F', '0', '0', 'system:user:import', '#', 'qData', '2024-05-06 06:12:17', '', NULL, '');
INSERT INTO `system_menu` VALUES (1006, '重置密码', 100, 7, '', '', '', 1, 0, NULL, 'F', '0', '0', 'system:user:resetPwd', '#', 'qData', '2024-05-06 06:12:17', '', NULL, '');
INSERT INTO `system_menu` VALUES (1007, '角色查询', 101, 1, '', '', '', 1, 0, NULL, 'F', '0', '0', 'system:role:query', '#', 'qData', '2024-05-06 06:12:17', '', NULL, '');
INSERT INTO `system_menu` VALUES (1008, '角色新增', 101, 2, '', '', '', 1, 0, NULL, 'F', '0', '0', 'system:role:add', '#', 'qData', '2024-05-06 06:12:17', '', NULL, '');
INSERT INTO `system_menu` VALUES (1009, '角色修改', 101, 3, '', '', '', 1, 0, NULL, 'F', '0', '0', 'system:role:edit', '#', 'qData', '2024-05-06 06:12:17', '', NULL, '');
INSERT INTO `system_menu` VALUES (1010, '角色删除', 101, 4, '', '', '', 1, 0, NULL, 'F', '0', '0', 'system:role:remove', '#', 'qData', '2024-05-06 06:12:17', '', NULL, '');
INSERT INTO `system_menu` VALUES (1011, '角色导出', 101, 5, '', '', '', 1, 0, NULL, 'F', '0', '0', 'system:role:export', '#', 'qData', '2024-05-06 06:12:17', '', NULL, '');
INSERT INTO `system_menu` VALUES (1012, '菜单查询', 102, 1, '', '', '', 1, 0, NULL, 'F', '0', '0', 'system:menu:query', '#', 'qData', '2024-05-06 06:12:17', '', NULL, '');
INSERT INTO `system_menu` VALUES (1013, '菜单新增', 102, 2, '', '', '', 1, 0, NULL, 'F', '0', '0', 'system:menu:add', '#', 'qData', '2024-05-06 06:12:17', '', NULL, '');
INSERT INTO `system_menu` VALUES (1014, '菜单修改', 102, 3, '', '', '', 1, 0, NULL, 'F', '0', '0', 'system:menu:edit', '#', 'qData', '2024-05-06 06:12:17', '', NULL, '');
INSERT INTO `system_menu` VALUES (1015, '菜单删除', 102, 4, '', '', '', 1, 0, NULL, 'F', '0', '0', 'system:menu:remove', '#', 'qData', '2024-05-06 06:12:17', '', NULL, '');
INSERT INTO `system_menu` VALUES (1016, '部门查询', 103, 1, '', '', '', 1, 0, NULL, 'F', '0', '0', 'system:dept:query', '#', 'qData', '2024-05-06 06:12:17', '', NULL, '');
INSERT INTO `system_menu` VALUES (1017, '部门新增', 103, 2, '', '', '', 1, 0, NULL, 'F', '0', '0', 'system:dept:add', '#', 'qData', '2024-05-06 06:12:17', '', NULL, '');
INSERT INTO `system_menu` VALUES (1018, '部门修改', 103, 3, '', '', '', 1, 0, NULL, 'F', '0', '0', 'system:dept:edit', '#', 'qData', '2024-05-06 06:12:17', '', NULL, '');
INSERT INTO `system_menu` VALUES (1019, '部门删除', 103, 4, '', '', '', 1, 0, NULL, 'F', '0', '0', 'system:dept:remove', '#', 'qData', '2024-05-06 06:12:17', '', NULL, '');
INSERT INTO `system_menu` VALUES (1020, '岗位查询', 104, 1, '', '', '', 1, 0, NULL, 'F', '0', '0', 'system:post:query', '#', 'qData', '2024-05-06 06:12:17', '', NULL, '');
INSERT INTO `system_menu` VALUES (1021, '岗位新增', 104, 2, '', '', '', 1, 0, NULL, 'F', '0', '0', 'system:post:add', '#', 'qData', '2024-05-06 06:12:17', '', NULL, '');
INSERT INTO `system_menu` VALUES (1022, '岗位修改', 104, 3, '', '', '', 1, 0, NULL, 'F', '0', '0', 'system:post:edit', '#', 'qData', '2024-05-06 06:12:17', '', NULL, '');
INSERT INTO `system_menu` VALUES (1023, '岗位删除', 104, 4, '', '', '', 1, 0, NULL, 'F', '0', '0', 'system:post:remove', '#', 'qData', '2024-05-06 06:12:17', '', NULL, '');
INSERT INTO `system_menu` VALUES (1024, '岗位导出', 104, 5, '', '', '', 1, 0, NULL, 'F', '0', '0', 'system:post:export', '#', 'qData', '2024-05-06 06:12:17', '', NULL, '');
INSERT INTO `system_menu` VALUES (1025, '字典查询', 105, 1, '#', '', '', 1, 0, NULL, 'F', '0', '0', 'system:dict:query', '#', 'qData', '2024-05-06 06:12:17', '', NULL, '');
INSERT INTO `system_menu` VALUES (1026, '字典新增', 105, 2, '#', '', '', 1, 0, NULL, 'F', '0', '0', 'system:dict:add', '#', 'qData', '2024-05-06 06:12:17', '', NULL, '');
INSERT INTO `system_menu` VALUES (1027, '字典修改', 105, 3, '#', '', '', 1, 0, NULL, 'F', '0', '0', 'system:dict:edit', '#', 'qData', '2024-05-06 06:12:17', '', NULL, '');
INSERT INTO `system_menu` VALUES (1028, '字典删除', 105, 4, '#', '', '', 1, 0, NULL, 'F', '0', '0', 'system:dict:remove', '#', 'qData', '2024-05-06 06:12:17', '', NULL, '');
INSERT INTO `system_menu` VALUES (1029, '字典导出', 105, 5, '#', '', '', 1, 0, NULL, 'F', '0', '0', 'system:dict:export', '#', 'qData', '2024-05-06 06:12:17', '', NULL, '');
INSERT INTO `system_menu` VALUES (1030, '参数查询', 106, 1, '#', '', '', 1, 0, NULL, 'F', '0', '0', 'system:config:query', '#', 'qData', '2024-05-06 06:12:17', '', NULL, '');
INSERT INTO `system_menu` VALUES (1031, '参数新增', 106, 2, '#', '', '', 1, 0, NULL, 'F', '0', '0', 'system:config:add', '#', 'qData', '2024-05-06 06:12:17', '', NULL, '');
INSERT INTO `system_menu` VALUES (1032, '参数修改', 106, 3, '#', '', '', 1, 0, NULL, 'F', '0', '0', 'system:config:edit', '#', 'qData', '2024-05-06 06:12:17', '', NULL, '');
INSERT INTO `system_menu` VALUES (1033, '参数删除', 106, 4, '#', '', '', 1, 0, NULL, 'F', '0', '0', 'system:config:remove', '#', 'qData', '2024-05-06 06:12:17', '', NULL, '');
INSERT INTO `system_menu` VALUES (1034, '参数导出', 106, 5, '#', '', '', 1, 0, NULL, 'F', '0', '0', 'system:config:export', '#', 'qData', '2024-05-06 06:12:17', '', NULL, '');
INSERT INTO `system_menu` VALUES (1035, '公告查询', 107, 1, '#', '', '', 1, 0, NULL, 'F', '0', '0', 'system:notice:query', '#', 'qData', '2024-05-06 06:12:17', '', NULL, '');
INSERT INTO `system_menu` VALUES (1036, '公告新增', 107, 2, '#', '', '', 1, 0, NULL, 'F', '0', '0', 'system:notice:add', '#', 'qData', '2024-05-06 06:12:17', '', NULL, '');
INSERT INTO `system_menu` VALUES (1037, '公告修改', 107, 3, '#', '', '', 1, 0, NULL, 'F', '0', '0', 'system:notice:edit', '#', 'qData', '2024-05-06 06:12:17', '', NULL, '');
INSERT INTO `system_menu` VALUES (1038, '公告删除', 107, 4, '#', '', '', 1, 0, NULL, 'F', '0', '0', 'system:notice:remove', '#', 'qData', '2024-05-06 06:12:17', '', NULL, '');
INSERT INTO `system_menu` VALUES (1039, '操作查询', 500, 1, '#', '', '', 1, 0, NULL, 'F', '0', '0', 'monitor:operlog:query', '#', 'qData', '2024-05-06 06:12:17', '', NULL, '');
INSERT INTO `system_menu` VALUES (1040, '操作删除', 500, 2, '#', '', '', 1, 0, NULL, 'F', '0', '0', 'monitor:operlog:remove', '#', 'qData', '2024-05-06 06:12:17', '', NULL, '');
INSERT INTO `system_menu` VALUES (1041, '日志导出', 500, 3, '#', '', '', 1, 0, NULL, 'F', '0', '0', 'monitor:operlog:export', '#', 'qData', '2024-05-06 06:12:17', '', NULL, '');
INSERT INTO `system_menu` VALUES (1042, '登录查询', 501, 1, '#', '', '', 1, 0, NULL, 'F', '0', '0', 'monitor:logininfor:query', '#', 'qData', '2024-05-06 06:12:17', '', NULL, '');
INSERT INTO `system_menu` VALUES (1043, '登录删除', 501, 2, '#', '', '', 1, 0, NULL, 'F', '0', '0', 'monitor:logininfor:remove', '#', 'qData', '2024-05-06 06:12:17', '', NULL, '');
INSERT INTO `system_menu` VALUES (1044, '日志导出', 501, 3, '#', '', '', 1, 0, NULL, 'F', '0', '0', 'monitor:logininfor:export', '#', 'qData', '2024-05-06 06:12:17', '', NULL, '');
INSERT INTO `system_menu` VALUES (1045, '账户解锁', 501, 4, '#', '', '', 1, 0, NULL, 'F', '0', '0', 'monitor:logininfor:unlock', '#', 'qData', '2024-05-06 06:12:17', '', NULL, '');
INSERT INTO `system_menu` VALUES (1046, '在线查询', 109, 1, '#', '', '', 1, 0, NULL, 'F', '0', '0', 'monitor:online:query', '#', 'qData', '2024-05-06 06:12:17', '', NULL, '');
INSERT INTO `system_menu` VALUES (1047, '批量强退', 109, 2, '#', '', '', 1, 0, NULL, 'F', '0', '0', 'monitor:online:batchLogout', '#', 'qData', '2024-05-06 06:12:17', '', NULL, '');
INSERT INTO `system_menu` VALUES (1048, '单条强退', 109, 3, '#', '', '', 1, 0, NULL, 'F', '0', '0', 'monitor:online:forceLogout', '#', 'qData', '2024-05-06 06:12:17', '', NULL, '');
INSERT INTO `system_menu` VALUES (1049, '任务查询', 110, 1, '#', '', '', 1, 0, NULL, 'F', '0', '0', 'monitor:job:query', '#', 'qData', '2024-05-06 06:12:17', '', NULL, '');
INSERT INTO `system_menu` VALUES (1050, '任务新增', 110, 2, '#', '', '', 1, 0, NULL, 'F', '0', '0', 'monitor:job:add', '#', 'qData', '2024-05-06 06:12:17', '', NULL, '');
INSERT INTO `system_menu` VALUES (1051, '任务修改', 110, 3, '#', '', '', 1, 0, NULL, 'F', '0', '0', 'monitor:job:edit', '#', 'qData', '2024-05-06 06:12:17', '', NULL, '');
INSERT INTO `system_menu` VALUES (1052, '任务删除', 110, 4, '#', '', '', 1, 0, NULL, 'F', '0', '0', 'monitor:job:remove', '#', 'qData', '2024-05-06 06:12:17', '', NULL, '');
INSERT INTO `system_menu` VALUES (1053, '状态修改', 110, 5, '#', '', '', 1, 0, NULL, 'F', '0', '0', 'monitor:job:changeStatus', '#', 'qData', '2024-05-06 06:12:17', '', NULL, '');
INSERT INTO `system_menu` VALUES (1054, '任务导出', 110, 6, '#', '', '', 1, 0, NULL, 'F', '0', '0', 'monitor:job:export', '#', 'qData', '2024-05-06 06:12:17', '', NULL, '');
INSERT INTO `system_menu` VALUES (1055, '生成查询', 116, 1, '#', '', '', 1, 0, NULL, 'F', '0', '0', 'tool:gen:query', '#', 'qData', '2024-05-06 06:12:17', '', NULL, '');
INSERT INTO `system_menu` VALUES (1056, '生成修改', 116, 2, '#', '', '', 1, 0, NULL, 'F', '0', '0', 'tool:gen:edit', '#', 'qData', '2024-05-06 06:12:17', '', NULL, '');
INSERT INTO `system_menu` VALUES (1057, '生成删除', 116, 3, '#', '', '', 1, 0, NULL, 'F', '0', '0', 'tool:gen:remove', '#', 'qData', '2024-05-06 06:12:17', '', NULL, '');
INSERT INTO `system_menu` VALUES (1058, '导入代码', 116, 4, '#', '', '', 1, 0, NULL, 'F', '0', '0', 'tool:gen:import', '#', 'qData', '2024-05-06 06:12:17', '', NULL, '');
INSERT INTO `system_menu` VALUES (1059, '预览代码', 116, 5, '#', '', '', 1, 0, NULL, 'F', '0', '0', 'tool:gen:preview', '#', 'qData', '2024-05-06 06:12:17', '', NULL, '');
INSERT INTO `system_menu` VALUES (1060, '生成代码', 116, 6, '#', '', '', 1, 0, NULL, 'F', '0', '0', 'tool:gen:code', '#', 'qData', '2024-05-06 06:12:17', '', NULL, '');
INSERT INTO `system_menu` VALUES (2026, '应用管理', 1, 1, 'client', 'system/auth/client/index', NULL, 1, 0, NULL, 'C', '1', '1', 'auth:client:list', 'user-3-line', 'qData', '2024-08-31 14:33:05', 'admin', '2025-05-30 16:19:04', '应用管理菜单');
INSERT INTO `system_menu` VALUES (2027, '应用管理查询', 2026, 1, '#', '', NULL, 1, 0, NULL, 'F', '0', '0', 'auth:client:query', '#', 'qData', '2024-08-31 14:33:05', '', NULL, '');
INSERT INTO `system_menu` VALUES (2028, '应用管理新增', 2026, 2, '#', '', NULL, 1, 0, NULL, 'F', '0', '0', 'auth:client:add', '#', 'qData', '2024-08-31 14:33:05', '', NULL, '');
INSERT INTO `system_menu` VALUES (2029, '应用管理修改', 2026, 3, '#', '', NULL, 1, 0, NULL, 'F', '0', '0', 'auth:client:edit', '#', 'qData', '2024-08-31 14:33:05', '', NULL, '');
INSERT INTO `system_menu` VALUES (2030, '应用管理删除', 2026, 4, '#', '', NULL, 1, 0, NULL, 'F', '0', '0', 'auth:client:remove', '#', 'qData', '2024-08-31 14:33:05', '', NULL, '');
INSERT INTO `system_menu` VALUES (2031, '应用管理导出', 2026, 5, '#', '', NULL, 1, 0, NULL, 'F', '0', '0', 'auth:client:export', '#', 'qData', '2024-08-31 14:33:05', '', NULL, '');
INSERT INTO `system_menu` VALUES (2062, '消息', 1, 1, 'message', 'system/message/message/index', NULL, 1, 0, NULL, 'C', '0', '1', 'system:message:message:list', '#', 'qData', '2024-11-01 09:51:29', 'qData', '2025-02-10 15:00:58', '消息菜单');
INSERT INTO `system_menu` VALUES (2063, '消息查询', 2062, 1, '#', '', NULL, 1, 0, NULL, 'F', '0', '0', 'system:message:message:query', '#', 'qData', '2024-11-01 09:51:29', '', NULL, '');
INSERT INTO `system_menu` VALUES (2064, '消息新增', 2062, 2, '#', '', NULL, 1, 0, NULL, 'F', '0', '0', 'system:message:message:add', '#', 'qData', '2024-11-01 09:51:29', '', NULL, '');
INSERT INTO `system_menu` VALUES (2065, '消息修改', 2062, 3, '#', '', NULL, 1, 0, NULL, 'F', '0', '0', 'system:message:message:edit', '#', 'qData', '2024-11-01 09:51:29', '', NULL, '');
INSERT INTO `system_menu` VALUES (2066, '消息删除', 2062, 4, '#', '', NULL, 1, 0, NULL, 'F', '0', '0', 'system:message:message:remove', '#', 'qData', '2024-11-01 09:51:29', '', NULL, '');
INSERT INTO `system_menu` VALUES (2067, '消息导出', 2062, 5, '#', '', NULL, 1, 0, NULL, 'F', '0', '0', 'system:message:message:export', '#', 'qData', '2024-11-01 09:51:29', '', NULL, '');
INSERT INTO `system_menu` VALUES (2243, '项目管理', 2375, 1, 'attProject', 'att/project/index', NULL, 1, 0, NULL, 'C', '0', '0', 'att:project:project:list', 'article-line', 'qData', '2025-01-20 19:13:20', 'admin', '2025-05-30 15:39:51', '项目菜单');
INSERT INTO `system_menu` VALUES (2244, '项目查询', 2243, 1, '#', '', NULL, 1, 0, NULL, 'F', '0', '0', 'att:project:project:query', '#', 'qData', '2025-01-20 19:14:21', '', NULL, '');
INSERT INTO `system_menu` VALUES (2245, '项目新增', 2243, 2, '#', '', NULL, 1, 0, NULL, 'F', '0', '0', 'att:project:project:add', '#', 'qData', '2025-01-20 19:14:21', '', NULL, '');
INSERT INTO `system_menu` VALUES (2246, '项目修改', 2243, 3, '#', '', NULL, 1, 0, NULL, 'F', '0', '0', 'att:project:project:edit', '#', 'qData', '2025-01-20 19:14:21', '', NULL, '');
INSERT INTO `system_menu` VALUES (2247, '项目删除', 2243, 4, '#', '', NULL, 1, 0, NULL, 'F', '0', '0', 'att:project:project:remove', '#', 'qData', '2025-01-20 19:14:21', '', NULL, '');
INSERT INTO `system_menu` VALUES (2248, '项目导出', 2243, 5, '#', '', NULL, 1, 0, NULL, 'F', '0', '0', 'att:project:project:export', '#', 'qData', '2025-01-20 19:14:21', '', NULL, '');
INSERT INTO `system_menu` VALUES (2249, '项目导入', 2243, 6, '#', '', NULL, 1, 0, NULL, 'F', '0', '0', 'att:project:project:import', '#', 'qData', '2025-01-20 19:14:21', '', NULL, '');
INSERT INTO `system_menu` VALUES (2257, '清洗规则', 2351, 2, 'AttCleanRule', 'att/rule/AttCleanRule/index', NULL, 1, 0, NULL, 'C', '0', '0', 'att:rule:attcleanrule:list', '#', 'qData', '2025-01-20 19:16:53', 'qData', '2025-02-27 17:29:56', '清洗规则菜单');
INSERT INTO `system_menu` VALUES (2258, '清洗规则查询', 2257, 1, '#', '', NULL, 1, 0, NULL, 'F', '0', '0', 'att:rule:attcleanrule:query', '#', 'qData', '2025-01-20 19:17:25', '', NULL, '');
INSERT INTO `system_menu` VALUES (2259, '清洗规则新增', 2257, 2, '#', '', NULL, 1, 0, NULL, 'F', '0', '0', 'att:rule:attcleanrule:add', '#', 'qData', '2025-01-20 19:17:25', '', NULL, '');
INSERT INTO `system_menu` VALUES (2260, '清洗规则修改', 2257, 3, '#', '', NULL, 1, 0, NULL, 'F', '0', '0', 'att:rule:attcleanrule:edit', '#', 'qData', '2025-01-20 19:17:25', '', NULL, '');
INSERT INTO `system_menu` VALUES (2261, '清洗规则删除', 2257, 4, '#', '', NULL, 1, 0, NULL, 'F', '0', '0', 'att:rule:attcleanrule:remove', '#', 'qData', '2025-01-20 19:17:25', '', NULL, '');
INSERT INTO `system_menu` VALUES (2262, '清洗规则导出', 2257, 5, '#', '', NULL, 1, 0, NULL, 'F', '0', '0', 'att:rule:attcleanrule:export', '#', 'qData', '2025-01-20 19:17:25', '', NULL, '');
INSERT INTO `system_menu` VALUES (2263, '清洗规则导入', 2257, 6, '#', '', NULL, 1, 0, NULL, 'F', '0', '0', 'att:rule:attcleanrule:import', '#', 'qData', '2025-01-20 19:17:25', '', NULL, '');
INSERT INTO `system_menu` VALUES (2264, '主题管理', 2375, 3, 'attTheme', 'att/theme/index', NULL, 1, 0, NULL, 'C', '0', '0', 'att:theme:theme:list', 'archive-2-line', 'qData', '2025-01-20 19:20:23', 'admin', '2025-05-30 15:41:02', '主题菜单');
INSERT INTO `system_menu` VALUES (2265, '主题查询', 2264, 1, '#', '', NULL, 1, 0, NULL, 'F', '0', '0', 'att:theme:theme:query', '#', 'qData', '2025-01-20 19:20:35', '', NULL, '');
INSERT INTO `system_menu` VALUES (2266, '主题新增', 2264, 2, '#', '', NULL, 1, 0, NULL, 'F', '0', '0', 'att:theme:theme:add', '#', 'qData', '2025-01-20 19:20:35', '', NULL, '');
INSERT INTO `system_menu` VALUES (2267, '主题修改', 2264, 3, '#', '', NULL, 1, 0, NULL, 'F', '0', '0', 'att:theme:theme:edit', '#', 'qData', '2025-01-20 19:20:36', '', NULL, '');
INSERT INTO `system_menu` VALUES (2268, '主题删除', 2264, 4, '#', '', NULL, 1, 0, NULL, 'F', '0', '0', 'att:theme:theme:remove', '#', 'qData', '2025-01-20 19:20:36', '', NULL, '');
INSERT INTO `system_menu` VALUES (2269, '主题导出', 2264, 5, '#', '', NULL, 1, 0, NULL, 'F', '0', '0', 'att:theme:theme:export', '#', 'qData', '2025-01-20 19:20:36', '', NULL, '');
INSERT INTO `system_menu` VALUES (2270, '主题导入', 2264, 6, '#', '', NULL, 1, 0, NULL, 'F', '0', '0', 'att:theme:theme:import', '#', 'qData', '2025-01-20 19:20:36', '', NULL, '');
INSERT INTO `system_menu` VALUES (2271, '逻辑模型类目', 2352, 1, 'attModelCat', 'att/cat/attModelCat/index', NULL, 1, 0, NULL, 'C', '0', '0', 'att:cat:modelcat:list', '#', 'qData', '2025-01-20 19:24:52', 'qData', '2025-03-31 09:41:03', '逻辑模型类目管理菜单');
INSERT INTO `system_menu` VALUES (2272, '逻辑模型类目管理查询', 2271, 1, '#', '', NULL, 1, 0, NULL, 'F', '0', '0', 'att:cat:modelcat:query', '#', 'qData', '2025-01-20 19:25:12', '', NULL, '');
INSERT INTO `system_menu` VALUES (2273, '逻辑模型类目管理新增', 2271, 2, '#', '', NULL, 1, 0, NULL, 'F', '0', '0', 'att:cat:modelcat:add', '#', 'qData', '2025-01-20 19:25:12', '', NULL, '');
INSERT INTO `system_menu` VALUES (2274, '逻辑模型类目管理修改', 2271, 3, '#', '', NULL, 1, 0, NULL, 'F', '0', '0', 'att:cat:modelcat:edit', '#', 'qData', '2025-01-20 19:25:12', '', NULL, '');
INSERT INTO `system_menu` VALUES (2275, '逻辑模型类目管理删除', 2271, 4, '#', '', NULL, 1, 0, NULL, 'F', '0', '0', 'att:cat:modelcat:remove', '#', 'qData', '2025-01-20 19:25:12', '', NULL, '');
INSERT INTO `system_menu` VALUES (2276, '逻辑模型类目管理导出', 2271, 5, '#', '', NULL, 1, 0, NULL, 'F', '0', '0', 'att:cat:modelcat:export', '#', 'qData', '2025-01-20 19:25:12', '', NULL, '');
INSERT INTO `system_menu` VALUES (2277, '逻辑模型类目管理导入', 2271, 6, '#', '', NULL, 1, 0, NULL, 'F', '0', '0', 'att:cat:modelcat:import', '#', 'qData', '2025-01-20 19:25:12', '', NULL, '');
INSERT INTO `system_menu` VALUES (2278, '数据元类目', 2352, 0, 'attDataElemCat', 'att/cat/attDataElemCat/index', NULL, 1, 0, NULL, 'C', '0', '0', 'att:cat:dataelemcat:list', '#', 'qData', '2025-01-20 19:31:14', 'qData', '2025-03-31 09:41:50', '数据元类目管理菜单');
INSERT INTO `system_menu` VALUES (2279, '数据元类目管理查询', 2278, 1, '#', '', NULL, 1, 0, NULL, 'F', '0', '0', 'att:cat:dataelemcat:query', '#', 'qData', '2025-01-20 19:31:25', '', NULL, '');
INSERT INTO `system_menu` VALUES (2280, '数据元类目管理新增', 2278, 2, '#', '', NULL, 1, 0, NULL, 'F', '0', '0', 'att:cat:dataelemcat:add', '#', 'qData', '2025-01-20 19:31:25', '', NULL, '');
INSERT INTO `system_menu` VALUES (2281, '数据元类目管理修改', 2278, 3, '#', '', NULL, 1, 0, NULL, 'F', '0', '0', 'att:cat:dataelemcat:edit', '#', 'qData', '2025-01-20 19:31:26', '', NULL, '');
INSERT INTO `system_menu` VALUES (2282, '数据元类目管理删除', 2278, 4, '#', '', NULL, 1, 0, NULL, 'F', '0', '0', 'att:cat:dataelemcat:remove', '#', 'qData', '2025-01-20 19:31:26', '', NULL, '');
INSERT INTO `system_menu` VALUES (2283, '数据元类目管理导出', 2278, 5, '#', '', NULL, 1, 0, NULL, 'F', '0', '0', 'att:cat:dataelemcat:export', '#', 'qData', '2025-01-20 19:31:26', '', NULL, '');
INSERT INTO `system_menu` VALUES (2284, '数据元类目管理导入', 2278, 6, '#', '', NULL, 1, 0, NULL, 'F', '0', '0', 'att:cat:dataelemcat:import', '#', 'qData', '2025-01-20 19:31:26', '', NULL, '');
INSERT INTO `system_menu` VALUES (2292, '稽查规则', 2351, 1, 'attAuditRule', 'att/rule/attAuditRule/index', NULL, 1, 0, NULL, 'C', '0', '0', 'att:rule:auditrule:list', '#', 'qData', '2025-01-20 19:39:59', 'qData', '2025-02-27 17:28:28', '稽查规则菜单');
INSERT INTO `system_menu` VALUES (2293, '稽查规则查询', 2292, 1, '#', '', NULL, 1, 0, NULL, 'F', '0', '0', 'att:rule:auditrule:query', '#', 'qData', '2025-01-20 19:40:21', '', NULL, '');
INSERT INTO `system_menu` VALUES (2294, '稽查规则新增', 2292, 2, '#', '', NULL, 1, 0, NULL, 'F', '0', '0', 'att:rule:auditrule:add', '#', 'qData', '2025-01-20 19:40:21', '', NULL, '');
INSERT INTO `system_menu` VALUES (2295, '稽查规则修改', 2292, 3, '#', '', NULL, 1, 0, NULL, 'F', '0', '0', 'att:rule:auditrule:edit', '#', 'qData', '2025-01-20 19:40:21', '', NULL, '');
INSERT INTO `system_menu` VALUES (2296, '稽查规则删除', 2292, 4, '#', '', NULL, 1, 0, NULL, 'F', '0', '0', 'att:rule:auditrule:remove', '#', 'qData', '2025-01-20 19:40:21', '', NULL, '');
INSERT INTO `system_menu` VALUES (2297, '稽查规则导出', 2292, 5, '#', '', NULL, 1, 0, NULL, 'F', '0', '0', 'att:rule:auditrule:export', '#', 'qData', '2025-01-20 19:40:21', '', NULL, '');
INSERT INTO `system_menu` VALUES (2298, '稽查规则导入', 2292, 6, '#', '', NULL, 1, 0, NULL, 'F', '0', '0', 'att:rule:auditrule:import', '#', 'qData', '2025-01-20 19:40:21', '', NULL, '');
INSERT INTO `system_menu` VALUES (2299, '数据资产类目', 2352, 4, 'attAssetCat', 'att/cat/attAssetCat/index', NULL, 1, 0, NULL, 'C', '0', '0', 'att:cat:assetcat:list', '#', 'qData', '2025-01-20 19:40:58', 'qData', '2025-03-31 09:41:16', '数据资产类目管理菜单');
INSERT INTO `system_menu` VALUES (2300, '数据资产类目管理查询', 2299, 1, '#', '', NULL, 1, 0, NULL, 'F', '0', '0', 'att:cat:assetcat:query', '#', 'qData', '2025-01-20 19:41:09', '', NULL, '');
INSERT INTO `system_menu` VALUES (2301, '数据资产类目管理新增', 2299, 2, '#', '', NULL, 1, 0, NULL, 'F', '0', '0', 'att:cat:assetcat:add', '#', 'qData', '2025-01-20 19:41:10', '', NULL, '');
INSERT INTO `system_menu` VALUES (2302, '数据资产类目管理修改', 2299, 3, '#', '', NULL, 1, 0, NULL, 'F', '0', '0', 'att:cat:assetcat:edit', '#', 'qData', '2025-01-20 19:41:10', '', NULL, '');
INSERT INTO `system_menu` VALUES (2303, '数据资产类目管理删除', 2299, 4, '#', '', NULL, 1, 0, NULL, 'F', '0', '0', 'att:cat:assetcat:remove', '#', 'qData', '2025-01-20 19:41:10', '', NULL, '');
INSERT INTO `system_menu` VALUES (2304, '数据资产类目管理导出', 2299, 5, '#', '', NULL, 1, 0, NULL, 'F', '0', '0', 'att:cat:assetcat:export', '#', 'qData', '2025-01-20 19:41:10', '', NULL, '');
INSERT INTO `system_menu` VALUES (2305, '数据资产类目管理导入', 2299, 6, '#', '', NULL, 1, 0, NULL, 'F', '0', '0', 'att:cat:assetcat:import', '#', 'qData', '2025-01-20 19:41:10', '', NULL, '');
INSERT INTO `system_menu` VALUES (2306, '数据规划', 0, 2, 'dp', NULL, NULL, 1, 0, NULL, 'M', '0', '0', '', 'stack-line', 'qData', '2025-01-21 10:02:05', 'admin', '2025-05-30 15:35:51', '');
INSERT INTO `system_menu` VALUES (2307, '逻辑模型', 2306, 1, 'dpModel', 'dp/model/index', NULL, 1, 0, NULL, 'C', '0', '0', 'dp:model:model:list', 'instance-line', 'qData', '2025-01-21 11:03:26', 'admin', '2025-05-30 15:44:34', '逻辑模型菜单');
INSERT INTO `system_menu` VALUES (2308, '逻辑模型查询', 2307, 1, '#', '', NULL, 1, 0, NULL, 'F', '0', '0', 'dp:model:model:query', '#', 'qData', '2025-01-21 11:09:26', '', NULL, '');
INSERT INTO `system_menu` VALUES (2309, '逻辑模型新增', 2307, 2, '#', '', NULL, 1, 0, NULL, 'F', '0', '0', 'dp:model:model:add', '#', 'qData', '2025-01-21 11:09:26', '', NULL, '');
INSERT INTO `system_menu` VALUES (2310, '逻辑模型修改', 2307, 3, '#', '', NULL, 1, 0, NULL, 'F', '0', '0', 'dp:model:model:edit', '#', 'qData', '2025-01-21 11:09:26', '', NULL, '');
INSERT INTO `system_menu` VALUES (2311, '逻辑模型删除', 2307, 4, '#', '', NULL, 1, 0, NULL, 'F', '0', '0', 'dp:model:model:remove', '#', 'qData', '2025-01-21 11:09:26', '', NULL, '');
INSERT INTO `system_menu` VALUES (2312, '逻辑模型导出', 2307, 5, '#', '', NULL, 1, 0, NULL, 'F', '0', '0', 'dp:model:model:export', '#', 'qData', '2025-01-21 11:09:26', '', NULL, '');
INSERT INTO `system_menu` VALUES (2313, '逻辑模型导入', 2307, 6, '#', '', NULL, 1, 0, NULL, 'F', '0', '0', 'dp:model:model:import', '#', 'qData', '2025-01-21 11:09:26', '', NULL, '');
INSERT INTO `system_menu` VALUES (2314, '数据资产', 0, 3, 'da', NULL, NULL, 1, 0, NULL, 'M', '0', '0', '', 'newspaper-line', 'qData', '2025-01-21 14:01:11', 'admin', '2025-05-30 15:36:20', '');
INSERT INTO `system_menu` VALUES (2315, '标准数据元', 2306, 2, 'dpDataElem', 'dp/dataElem/index', NULL, 1, 0, NULL, 'C', '0', '0', 'dp:dataElem:dataelem:list', 'hard-drive-3-line', 'qData', '2025-01-21 14:53:06', 'admin', '2025-05-30 15:45:24', '数据元菜单');
INSERT INTO `system_menu` VALUES (2316, '数据元查询', 2315, 1, '#', '', NULL, 1, 0, NULL, 'F', '0', '0', 'dp:dataElem:dataelem:query', '#', 'qData', '2025-01-21 14:54:20', 'shuyexin', '2025-02-06 10:07:56', '');
INSERT INTO `system_menu` VALUES (2317, '数据元新增', 2315, 2, '#', '', NULL, 1, 0, NULL, 'F', '0', '0', 'dp:dataElem:dataelem:add', '#', 'qData', '2025-01-21 14:54:20', '', NULL, '');
INSERT INTO `system_menu` VALUES (2318, '数据元修改', 2315, 3, '#', '', NULL, 1, 0, NULL, 'F', '0', '0', 'dp:dataElem:dataelem:edit', '#', 'qData', '2025-01-21 14:54:21', '', NULL, '');
INSERT INTO `system_menu` VALUES (2319, '数据元删除', 2315, 4, '#', '', NULL, 1, 0, NULL, 'F', '0', '0', 'dp:dataElem:dataelem:remove', '#', 'qData', '2025-01-21 14:54:21', '', NULL, '');
INSERT INTO `system_menu` VALUES (2320, '数据元导出', 2315, 5, '#', '', NULL, 1, 0, NULL, 'F', '0', '0', 'dp:dataElem:dataelem:export', '#', 'qData', '2025-01-21 14:54:21', '', NULL, '');
INSERT INTO `system_menu` VALUES (2321, '数据元导入', 2315, 6, '#', '', NULL, 1, 0, NULL, 'F', '0', '0', 'dp:dataElem:dataelem:import', '#', 'qData', '2025-01-21 14:54:21', '', NULL, '');
INSERT INTO `system_menu` VALUES (2322, '数据资产字段', 2329, 100, 'daAssetColumn', 'da/assetColumn/index', NULL, 1, 0, NULL, 'C', '1', '0', 'da:assetColumn:assetcolumn:list', '#', 'qData', '2025-01-21 15:02:49', 'shuyexin', '2025-04-02 11:25:49', '数据资产字段菜单');
INSERT INTO `system_menu` VALUES (2323, '数据资产字段查询', 2322, 1, '#', '', NULL, 1, 0, NULL, 'F', '0', '0', 'da:assetColumn:assetcolumn:query', '#', 'qData', '2025-01-21 15:08:48', '', NULL, '');
INSERT INTO `system_menu` VALUES (2324, '数据资产字段新增', 2322, 2, '#', '', NULL, 1, 0, NULL, 'F', '0', '0', 'da:assetColumn:assetcolumn:add', '#', 'qData', '2025-01-21 15:08:48', '', NULL, '');
INSERT INTO `system_menu` VALUES (2325, '数据资产字段修改', 2322, 3, '#', '', NULL, 1, 0, NULL, 'F', '0', '0', 'da:assetColumn:assetcolumn:edit', '#', 'qData', '2025-01-21 15:08:48', '', NULL, '');
INSERT INTO `system_menu` VALUES (2326, '数据资产字段删除', 2322, 4, '#', '', NULL, 1, 0, NULL, 'F', '0', '0', 'da:assetColumn:assetcolumn:remove', '#', 'qData', '2025-01-21 15:08:48', '', NULL, '');
INSERT INTO `system_menu` VALUES (2327, '数据资产字段导出', 2322, 5, '#', '', NULL, 1, 0, NULL, 'F', '0', '0', 'da:assetColumn:assetcolumn:export', '#', 'qData', '2025-01-21 15:08:48', '', NULL, '');
INSERT INTO `system_menu` VALUES (2328, '数据资产字段导入', 2322, 6, '#', '', NULL, 1, 0, NULL, 'F', '0', '0', 'da:assetColumn:assetcolumn:import', '#', 'qData', '2025-01-21 15:08:48', '', NULL, '');
INSERT INTO `system_menu` VALUES (2329, '资产地图', 2314, 3, 'daAsset', 'da/asset/index', NULL, 1, 0, NULL, 'C', '0', '0', 'da:asset:asset:list', 'money-dollar-box-line', 'qData', '2025-01-21 15:10:11', 'admin', '2025-05-30 15:48:16', '数据资产菜单');
INSERT INTO `system_menu` VALUES (2330, '数据资产查询', 2329, 1, '#', '', NULL, 1, 0, NULL, 'F', '0', '0', 'da:asset:asset:query', '#', 'qData', '2025-01-21 15:10:54', '', NULL, '');
INSERT INTO `system_menu` VALUES (2331, '数据资产新增', 2329, 2, '#', '', NULL, 1, 0, NULL, 'F', '0', '0', 'da:asset:asset:add', '#', 'qData', '2025-01-21 15:10:54', '', NULL, '');
INSERT INTO `system_menu` VALUES (2332, '数据资产修改', 2329, 3, '#', '', NULL, 1, 0, NULL, 'F', '0', '0', 'da:asset:asset:edit', '#', 'qData', '2025-01-21 15:10:54', '', NULL, '');
INSERT INTO `system_menu` VALUES (2333, '数据资产删除', 2329, 4, '#', '', NULL, 1, 0, NULL, 'F', '0', '0', 'da:asset:asset:remove', '#', 'qData', '2025-01-21 15:10:54', '', NULL, '');
INSERT INTO `system_menu` VALUES (2334, '数据资产导出', 2329, 5, '#', '', NULL, 1, 0, NULL, 'F', '0', '0', 'da:asset:asset:export', '#', 'qData', '2025-01-21 15:10:54', '', NULL, '');
INSERT INTO `system_menu` VALUES (2335, '数据资产导入', 2329, 6, '#', '', NULL, 1, 0, NULL, 'F', '0', '0', 'da:asset:asset:import', '#', 'qData', '2025-01-21 15:10:54', '', NULL, '');
INSERT INTO `system_menu` VALUES (2336, '数据连接', 2314, 1, 'daDatasource', 'da/datasource/index', '{\"type\":0}', 1, 0, NULL, 'C', '0', '0', 'da:datasource:datasource:list', 'copper-coin-line', 'qData', '2025-01-21 15:11:31', 'admin', '2025-05-30 15:46:04', '数据源菜单');
INSERT INTO `system_menu` VALUES (2337, '数据源查询', 2336, 1, '#', '', NULL, 1, 0, NULL, 'F', '0', '0', 'da:datasource:datasource:query', '#', 'qData', '2025-01-21 15:12:00', '', NULL, '');
INSERT INTO `system_menu` VALUES (2338, '数据源新增', 2336, 2, '#', '', NULL, 1, 0, NULL, 'F', '0', '0', 'da:datasource:datasource:add', '#', 'qData', '2025-01-21 15:12:00', '', NULL, '');
INSERT INTO `system_menu` VALUES (2339, '数据源修改', 2336, 3, '#', '', NULL, 1, 0, NULL, 'F', '0', '0', 'da:datasource:datasource:edit', '#', 'qData', '2025-01-21 15:12:00', '', NULL, '');
INSERT INTO `system_menu` VALUES (2340, '数据源删除', 2336, 4, '#', '', NULL, 1, 0, NULL, 'F', '0', '0', 'da:datasource:datasource:remove', '#', 'qData', '2025-01-21 15:12:00', '', NULL, '');
INSERT INTO `system_menu` VALUES (2341, '数据源导出', 2336, 5, '#', '', NULL, 1, 0, NULL, 'F', '0', '0', 'da:datasource:datasource:export', '#', 'qData', '2025-01-21 15:12:00', '', NULL, '');
INSERT INTO `system_menu` VALUES (2342, '数据源导入', 2336, 6, '#', '', NULL, 1, 0, NULL, 'F', '0', '0', 'da:datasource:datasource:import', '#', 'qData', '2025-01-21 15:12:00', '', NULL, '');
INSERT INTO `system_menu` VALUES (2343, '敏感等级', 2350, 1, 'daSensitiveLevel', 'da/sensitiveLevel/index', NULL, 1, 0, NULL, 'C', '0', '0', 'da:sensitiveLevel:sensitivelevel:list', '#', 'qData', '2025-01-21 15:12:20', 'qData', '2025-02-27 17:31:04', '敏感等级菜单');
INSERT INTO `system_menu` VALUES (2344, '敏感等级查询', 2343, 1, '#', '', NULL, 1, 0, NULL, 'F', '0', '0', 'da:sensitiveLevel:sensitivelevel:query', '#', 'qData', '2025-01-21 15:13:21', '', NULL, '');
INSERT INTO `system_menu` VALUES (2345, '敏感等级新增', 2343, 2, '#', '', NULL, 1, 0, NULL, 'F', '0', '0', 'da:sensitiveLevel:sensitivelevel:add', '#', 'qData', '2025-01-21 15:13:21', '', NULL, '');
INSERT INTO `system_menu` VALUES (2346, '敏感等级修改', 2343, 3, '#', '', NULL, 1, 0, NULL, 'F', '0', '0', 'da:sensitiveLevel:sensitivelevel:edit', '#', 'qData', '2025-01-21 15:13:21', '', NULL, '');
INSERT INTO `system_menu` VALUES (2347, '敏感等级删除', 2343, 4, '#', '', NULL, 1, 0, NULL, 'F', '0', '0', 'da:sensitiveLevel:sensitivelevel:remove', '#', 'qData', '2025-01-21 15:13:21', '', NULL, '');
INSERT INTO `system_menu` VALUES (2348, '敏感等级导出', 2343, 5, '#', '', NULL, 1, 0, NULL, 'F', '0', '0', 'da:sensitiveLevel:sensitivelevel:export', '#', 'qData', '2025-01-21 15:13:21', '', NULL, '');
INSERT INTO `system_menu` VALUES (2349, '敏感等级导入', 2343, 6, '#', '', NULL, 1, 0, NULL, 'F', '0', '0', 'da:sensitiveLevel:sensitivelevel:import', '#', 'qData', '2025-01-21 15:13:22', '', NULL, '');
INSERT INTO `system_menu` VALUES (2350, '数据安全', 2314, 30, 'daSensitiveLevel', NULL, NULL, 1, 0, NULL, 'M', '0', '0', '', 'shield-check-line', 'qData', '2025-01-21 16:25:22', 'admin', '2025-05-30 15:49:46', '');
INSERT INTO `system_menu` VALUES (2351, '规则管理', 2375, 0, 'rule', NULL, NULL, 1, 0, NULL, 'M', '0', '0', '', 'microsoft-line', 'qData', '2025-01-23 15:40:00', 'admin', '2025-05-30 15:39:22', '');
INSERT INTO `system_menu` VALUES (2352, '类目管理', 2375, 4, 'cat', NULL, NULL, 1, 0, NULL, 'M', '0', '0', '', 'briefcase-2-line', 'qData', '2025-01-23 15:40:38', 'admin', '2025-05-30 15:41:31', '');
INSERT INTO `system_menu` VALUES (2354, '逻辑模型属性信息', 2307, 1, 'dpModelColumn', 'dp/model/index', NULL, 1, 0, NULL, 'C', '0', '0', 'dp:model:modelcolumn:list', '#', 'qData', '2025-02-06 09:56:17', 'shuyexin', '2025-02-06 10:00:06', '逻辑模型属性信息菜单');
INSERT INTO `system_menu` VALUES (2355, '逻辑模型属性信息查询', 2354, 1, '#', '', NULL, 1, 0, NULL, 'F', '0', '0', 'dp:model:modelcolumn:query', '#', 'qData', '2025-02-06 09:56:43', '', NULL, '');
INSERT INTO `system_menu` VALUES (2356, '逻辑模型属性信息新增', 2354, 2, '#', '', NULL, 1, 0, NULL, 'F', '0', '0', 'dp:model:modelcolumn:add', '#', 'qData', '2025-02-06 09:56:43', '', NULL, '');
INSERT INTO `system_menu` VALUES (2357, '逻辑模型属性信息修改', 2354, 3, '#', '', NULL, 1, 0, NULL, 'F', '0', '0', 'dp:model:modelcolumn:edit', '#', 'qData', '2025-02-06 09:56:43', '', NULL, '');
INSERT INTO `system_menu` VALUES (2358, '逻辑模型属性信息删除', 2354, 4, '#', '', NULL, 1, 0, NULL, 'F', '0', '0', 'dp:model:modelcolumn:remove', '#', 'qData', '2025-02-06 09:56:43', '', NULL, '');
INSERT INTO `system_menu` VALUES (2359, '逻辑模型属性信息导出', 2354, 5, '#', '', NULL, 1, 0, NULL, 'F', '0', '0', 'dp:model:modelcolumn:export', '#', 'qData', '2025-02-06 09:56:43', '', NULL, '');
INSERT INTO `system_menu` VALUES (2360, '逻辑模型属性信息导入', 2354, 6, '#', '', NULL, 1, 0, NULL, 'F', '0', '0', 'dp:model:modelcolumn:import', '#', 'qData', '2025-02-06 09:56:43', '', NULL, '');
INSERT INTO `system_menu` VALUES (2368, '数据元数据资产关联信息', 2315, 1, 'dpDataElemAssetRel', 'dp/dataElemAssetRel/index', NULL, 1, 0, NULL, 'C', '0', '0', 'dp:dataElemAssetRel:dataelemassetrel:list', '#', 'qData', '2025-02-06 10:21:35', 'shuyexin', '2025-02-06 10:38:46', '数据元数据资产关联信息菜单');
INSERT INTO `system_menu` VALUES (2369, '数据元数据资产关联信息查询', 2368, 1, '#', '', NULL, 1, 0, NULL, 'F', '0', '0', 'dp:dataElemAssetRel:dataelemassetrel:query', '#', 'qData', '2025-02-06 10:21:59', 'shuyexin', '2025-02-06 10:38:53', '');
INSERT INTO `system_menu` VALUES (2370, '数据元数据资产关联信息新增', 2368, 2, '#', '', NULL, 1, 0, NULL, 'F', '0', '0', 'dp:dataElemAssetRel:dataelemassetrel:add', '#', 'qData', '2025-02-06 10:21:59', 'shuyexin', '2025-02-06 10:38:57', '');
INSERT INTO `system_menu` VALUES (2371, '数据元数据资产关联信息修改', 2368, 3, '#', '', NULL, 1, 0, NULL, 'F', '0', '0', 'dp:dataElemAssetRel:dataelemassetrel:edit', '#', 'qData', '2025-02-06 10:21:59', 'shuyexin', '2025-02-06 10:39:01', '');
INSERT INTO `system_menu` VALUES (2372, '数据元数据资产关联信息删除', 2368, 4, '#', '', NULL, 1, 0, NULL, 'F', '0', '0', 'dp:dataElemAssetRel:dataelemassetrel:remove', '#', 'qData', '2025-02-06 10:21:59', 'shuyexin', '2025-02-06 10:39:05', '');
INSERT INTO `system_menu` VALUES (2373, '数据元数据资产关联信息导出', 2368, 5, '#', '', NULL, 1, 0, NULL, 'F', '0', '0', 'dp:dataElemAssetRel:dataelemassetrel:export', '#', 'qData', '2025-02-06 10:21:59', 'shuyexin', '2025-02-06 10:39:11', '');
INSERT INTO `system_menu` VALUES (2374, '数据元数据资产关联信息导入', 2368, 6, '#', '', NULL, 1, 0, NULL, 'F', '0', '0', 'dp:dataElemAssetRel:dataelemassetrel:import', '#', 'qData', '2025-02-06 10:21:59', 'shuyexin', '2025-02-06 10:39:16', '');
INSERT INTO `system_menu` VALUES (2375, '基础管理', 0, 1, 'att', NULL, NULL, 1, 0, NULL, 'M', '0', '0', '', 'archive-drawer-line', 'mengfanming', '2025-02-06 10:24:51', 'admin', '2025-05-30 15:29:19', '');
INSERT INTO `system_menu` VALUES (2376, '数据元数据规则关联信息', 2306, 1, 'dpDataElemRuleRel', 'dp/dataElem/index', NULL, 1, 0, NULL, 'C', '1', '0', 'dp:dataElem:dataelemrulerel:list', '#', 'qData', '2025-02-06 10:40:15', 'shuyexin', '2025-02-06 10:42:15', '数据元数据规则关联信息菜单');
INSERT INTO `system_menu` VALUES (2377, '数据元数据规则关联信息查询', 2376, 1, '#', '', NULL, 1, 0, NULL, 'F', '0', '0', 'dp:dataElem:dataelemrulerel:query', '#', 'qData', '2025-02-06 10:40:55', '', NULL, '');
INSERT INTO `system_menu` VALUES (2378, '数据元数据规则关联信息新增', 2376, 2, '#', '', NULL, 1, 0, NULL, 'F', '0', '0', 'dp:dataElem:dataelemrulerel:add', '#', 'qData', '2025-02-06 10:40:55', '', NULL, '');
INSERT INTO `system_menu` VALUES (2379, '数据元数据规则关联信息修改', 2376, 3, '#', '', NULL, 1, 0, NULL, 'F', '0', '0', 'dp:dataElem:dataelemrulerel:edit', '#', 'qData', '2025-02-06 10:40:55', '', NULL, '');
INSERT INTO `system_menu` VALUES (2380, '数据元数据规则关联信息删除', 2376, 4, '#', '', NULL, 1, 0, NULL, 'F', '0', '0', 'dp:dataElem:dataelemrulerel:remove', '#', 'qData', '2025-02-06 10:40:55', '', NULL, '');
INSERT INTO `system_menu` VALUES (2381, '数据元数据规则关联信息导出', 2376, 5, '#', '', NULL, 1, 0, NULL, 'F', '0', '0', 'dp:dataElem:dataelemrulerel:export', '#', 'qData', '2025-02-06 10:40:55', '', NULL, '');
INSERT INTO `system_menu` VALUES (2382, '数据元数据规则关联信息导入', 2376, 6, '#', '', NULL, 1, 0, NULL, 'F', '0', '0', 'dp:dataElem:dataelemrulerel:import', '#', 'qData', '2025-02-06 10:40:55', '', NULL, '');
INSERT INTO `system_menu` VALUES (2383, '数据元代码映射', 2306, 1, 'dpCodeMap', 'dp/codeMap/index', NULL, 1, 0, NULL, 'C', '1', '0', 'dp:codeMap:codemap:list', '#', 'qData', '2025-02-06 10:44:19', 'qData', '2025-02-06 10:47:23', '数据元代码映射菜单');
INSERT INTO `system_menu` VALUES (2384, '数据元代码映射查询', 2383, 1, '#', '', NULL, 1, 0, NULL, 'F', '0', '0', 'dp:codeMap:codemap:query', '#', 'qData', '2025-02-06 10:44:36', '', NULL, '');
INSERT INTO `system_menu` VALUES (2385, '数据元代码映射新增', 2383, 2, '#', '', NULL, 1, 0, NULL, 'F', '0', '0', 'dp:codeMap:codemap:add', '#', 'qData', '2025-02-06 10:44:36', '', NULL, '');
INSERT INTO `system_menu` VALUES (2386, '数据元代码映射修改', 2383, 3, '#', '', NULL, 1, 0, NULL, 'F', '0', '0', 'dp:codeMap:codemap:edit', '#', 'qData', '2025-02-06 10:44:36', '', NULL, '');
INSERT INTO `system_menu` VALUES (2387, '数据元代码映射删除', 2383, 4, '#', '', NULL, 1, 0, NULL, 'F', '0', '0', 'dp:codeMap:codemap:remove', '#', 'qData', '2025-02-06 10:44:36', '', NULL, '');
INSERT INTO `system_menu` VALUES (2388, '数据元代码映射导出', 2383, 5, '#', '', NULL, 1, 0, NULL, 'F', '0', '0', 'dp:codeMap:codemap:export', '#', 'qData', '2025-02-06 10:44:36', '', NULL, '');
INSERT INTO `system_menu` VALUES (2389, '数据元代码映射导入', 2383, 6, '#', '', NULL, 1, 0, NULL, 'F', '0', '0', 'dp:codeMap:codemap:import', '#', 'qData', '2025-02-06 10:44:36', '', NULL, '');
INSERT INTO `system_menu` VALUES (2390, '数据元代码', 2306, 1, 'dpDataElemCode', 'dp/dataElemCode/index', NULL, 1, 0, NULL, 'C', '1', '0', 'dp:dataElemCode:dataelemcode:list', '#', 'qData', '2025-02-06 10:45:00', 'qData', '2025-02-06 10:47:36', '数据元代码菜单');
INSERT INTO `system_menu` VALUES (2391, '数据元代码查询', 2390, 1, '#', '', NULL, 1, 0, NULL, 'F', '0', '0', 'dp:dataElemCode:dataelemcode:query', '#', 'qData', '2025-02-06 10:45:16', '', NULL, '');
INSERT INTO `system_menu` VALUES (2392, '数据元代码新增', 2390, 2, '#', '', NULL, 1, 0, NULL, 'F', '0', '0', 'dp:dataElemCode:dataelemcode:add', '#', 'qData', '2025-02-06 10:45:16', '', NULL, '');
INSERT INTO `system_menu` VALUES (2393, '数据元代码修改', 2390, 3, '#', '', NULL, 1, 0, NULL, 'F', '0', '0', 'dp:dataElemCode:dataelemcode:edit', '#', 'qData', '2025-02-06 10:45:16', '', NULL, '');
INSERT INTO `system_menu` VALUES (2394, '数据元代码删除', 2390, 4, '#', '', NULL, 1, 0, NULL, 'F', '0', '0', 'dp:dataElemCode:dataelemcode:remove', '#', 'qData', '2025-02-06 10:45:16', '', NULL, '');
INSERT INTO `system_menu` VALUES (2395, '数据元代码导出', 2390, 5, '#', '', NULL, 1, 0, NULL, 'F', '0', '0', 'dp:dataElemCode:dataelemcode:export', '#', 'qData', '2025-02-06 10:45:16', '', NULL, '');
INSERT INTO `system_menu` VALUES (2396, '数据元代码导入', 2390, 6, '#', '', NULL, 1, 0, NULL, 'F', '0', '0', 'dp:dataElemCode:dataelemcode:import', '#', 'qData', '2025-02-06 10:45:16', '', NULL, '');
INSERT INTO `system_menu` VALUES (2397, '数据研发', 0, 4, 'dpp', NULL, NULL, 1, 0, NULL, 'M', '0', '0', '', 'book-3-line', 'changmingyao', '2025-02-11 09:51:19', 'admin', '2025-05-30 15:37:17', '');
INSERT INTO `system_menu` VALUES (2398, '成员角色管理', 2552, 4, 'AttProjectUserRel', 'att/projectUserRel/index', NULL, 1, 0, NULL, 'C', '0', '0', 'att:project:attprojectuserrel:list', '#', 'qData', '2025-02-11 09:57:17', 'qData', '2025-04-27 14:03:58', '项目与用户关联关系菜单');
INSERT INTO `system_menu` VALUES (2399, '项目与用户关联关系查询', 2398, 1, '#', '', NULL, 1, 0, NULL, 'F', '0', '0', 'att:project:attprojectuserrel:query', '#', 'qData', '2025-02-11 09:58:01', '', NULL, '');
INSERT INTO `system_menu` VALUES (2400, '项目与用户关联关系新增', 2398, 2, '#', '', NULL, 1, 0, NULL, 'F', '0', '0', 'att:project:attprojectuserrel:add', '#', 'qData', '2025-02-11 09:58:01', '', NULL, '');
INSERT INTO `system_menu` VALUES (2401, '项目与用户关联关系修改', 2398, 3, '#', '', NULL, 1, 0, NULL, 'F', '0', '0', 'att:project:attprojectuserrel:edit', '#', 'qData', '2025-02-11 09:58:01', '', NULL, '');
INSERT INTO `system_menu` VALUES (2402, '项目与用户关联关系删除', 2398, 4, '#', '', NULL, 1, 0, NULL, 'F', '0', '0', 'att:project:attprojectuserrel:remove', '#', 'qData', '2025-02-11 09:58:01', '', NULL, '');
INSERT INTO `system_menu` VALUES (2403, '项目与用户关联关系导出', 2398, 5, '#', '', NULL, 1, 0, NULL, 'F', '0', '0', 'att:project:attprojectuserrel:export', '#', 'qData', '2025-02-11 09:58:01', '', NULL, '');
INSERT INTO `system_menu` VALUES (2404, '项目与用户关联关系导入', 2398, 6, '#', '', NULL, 1, 0, NULL, 'F', '0', '0', 'att:project:attprojectuserrel:import', '#', 'qData', '2025-02-11 09:58:01', '', NULL, '');
INSERT INTO `system_menu` VALUES (2426, '数据集成', 2510, 1, 'dppEtlTask', 'dpp/etl/index', NULL, 1, 0, '', 'C', '0', '0', 'dpp:etl:etltask:list', '#', 'qData', '2025-02-12 11:56:06', 'qData', '2025-03-12 18:14:49', '');
INSERT INTO `system_menu` VALUES (2427, '数据服务', 0, 5, 'ds', NULL, NULL, 1, 0, NULL, 'M', '0', '0', '', 'hourglass-line', 'qData', '2025-02-12 16:08:29', 'admin', '2025-05-30 15:38:07', '');
INSERT INTO `system_menu` VALUES (2428, '调用日志', 2427, 1, 'apiLog', 'ds/apiLog/index', NULL, 1, 1, NULL, 'C', '0', '0', 'ds:apiLog:apilog:list', 'telegram-line', 'qData', '2025-02-12 16:13:42', 'admin', '2025-05-30 16:15:03', 'API服务调用日志菜单');
INSERT INTO `system_menu` VALUES (2429, 'API服务调用日志查询', 2428, 1, '#', '', NULL, 1, 0, NULL, 'F', '0', '0', 'ds:apiLog:apilog:query', '#', 'qData', '2025-02-12 16:14:14', '', NULL, '');
INSERT INTO `system_menu` VALUES (2430, 'API服务调用日志新增', 2428, 2, '#', '', NULL, 1, 0, NULL, 'F', '0', '0', 'ds:apiLog:apilog:add', '#', 'qData', '2025-02-12 16:14:14', '', NULL, '');
INSERT INTO `system_menu` VALUES (2431, 'API服务调用日志修改', 2428, 3, '#', '', NULL, 1, 0, NULL, 'F', '0', '0', 'ds:apiLog:apilog:edit', '#', 'qData', '2025-02-12 16:14:14', '', NULL, '');
INSERT INTO `system_menu` VALUES (2432, 'API服务调用日志删除', 2428, 4, '#', '', NULL, 1, 0, NULL, 'F', '0', '0', 'ds:apiLog:apilog:remove', '#', 'qData', '2025-02-12 16:14:14', '', NULL, '');
INSERT INTO `system_menu` VALUES (2433, 'API服务调用日志导出', 2428, 5, '#', '', NULL, 1, 0, NULL, 'F', '0', '0', 'ds:apiLog:apilog:export', '#', 'qData', '2025-02-12 16:14:14', '', NULL, '');
INSERT INTO `system_menu` VALUES (2434, 'API服务调用日志导入', 2428, 6, '#', '', NULL, 1, 0, NULL, 'F', '0', '0', 'ds:apiLog:apilog:import', '#', 'qData', '2025-02-12 16:14:14', '', NULL, '');
INSERT INTO `system_menu` VALUES (2436, 'API  管理', 2427, 0, 'dsApi', 'ds/api/index', NULL, 1, 1, NULL, 'C', '0', '0', 'ds:api:api:list', 'rainbow-line', 'qData', '2025-02-12 16:17:04', 'admin', '2025-05-30 16:14:46', 'API服务菜单');
INSERT INTO `system_menu` VALUES (2437, 'API服务查询', 2436, 1, '#', '', NULL, 1, 0, NULL, 'F', '0', '0', 'ds:api:api:query', '#', 'qData', '2025-02-12 16:17:32', '', NULL, '');
INSERT INTO `system_menu` VALUES (2438, 'API服务新增', 2436, 2, '#', '', NULL, 1, 0, NULL, 'F', '0', '0', 'ds:api:api:add', '#', 'qData', '2025-02-12 16:17:32', '', NULL, '');
INSERT INTO `system_menu` VALUES (2439, 'API服务修改', 2436, 3, '#', '', NULL, 1, 0, NULL, 'F', '0', '0', 'ds:api:api:edit', '#', 'qData', '2025-02-12 16:17:32', '', NULL, '');
INSERT INTO `system_menu` VALUES (2440, 'API服务删除', 2436, 4, '#', '', NULL, 1, 0, NULL, 'F', '0', '0', 'ds:api:api:remove', '#', 'qData', '2025-02-12 16:17:32', '', NULL, '');
INSERT INTO `system_menu` VALUES (2441, 'API服务导出', 2436, 5, '#', '', NULL, 1, 0, NULL, 'F', '0', '0', 'ds:api:api:export', '#', 'qData', '2025-02-12 16:17:32', '', NULL, '');
INSERT INTO `system_menu` VALUES (2442, 'API服务导入', 2436, 6, '#', '', NULL, 1, 0, NULL, 'F', '0', '0', 'ds:api:api:import', '#', 'qData', '2025-02-12 16:17:32', '', NULL, '');
INSERT INTO `system_menu` VALUES (2444, '角色列表', 2398, 7, '', NULL, NULL, 1, 0, NULL, 'F', '0', '0', 'att:project:role:list', '#', 'shuyexin', '2025-02-13 14:54:22', 'qData', '2025-02-13 14:56:48', '');
INSERT INTO `system_menu` VALUES (2445, '角色导出', 2398, 8, '', NULL, NULL, 1, 0, NULL, 'F', '0', '0', 'att:project:role:export', '#', 'shuyexin', '2025-02-13 14:54:46', '', NULL, '');
INSERT INTO `system_menu` VALUES (2446, '角色修改', 2398, 7, '', NULL, NULL, 1, 0, NULL, 'F', '0', '0', 'att:project:role:edit', '#', 'shuyexin', '2025-02-13 14:55:14', '', NULL, '');
INSERT INTO `system_menu` VALUES (2447, '角色添加', 2398, 10, '', NULL, NULL, 1, 0, NULL, 'F', '0', '0', 'att:project:role:add', '#', 'qData', '2025-02-13 14:56:39', '', NULL, '');
INSERT INTO `system_menu` VALUES (2448, '角色查询', 2398, 11, '', NULL, NULL, 1, 0, NULL, 'F', '0', '0', 'att:project:role:query', '#', 'qData', '2025-02-13 14:57:06', 'qData', '2025-02-13 14:57:13', '');
INSERT INTO `system_menu` VALUES (2449, '角色删除', 2398, 12, '', NULL, NULL, 1, 0, NULL, 'F', '0', '0', 'att:project:role:remove', '#', 'qData', '2025-02-13 14:57:38', '', NULL, '');
INSERT INTO `system_menu` VALUES (2457, '应用管理', 2375, 5, 'client', 'att/client/index', NULL, 1, 0, NULL, 'C', '0', '0', 'att:client:client:list', 'color-filter-line', 'qData', '2025-02-18 10:57:53', 'admin', '2025-05-30 15:42:39', '应用管理菜单');
INSERT INTO `system_menu` VALUES (2458, '应用管理查询', 2457, 1, '#', '', NULL, 1, 0, NULL, 'F', '0', '0', 'att:client:client:query', '#', 'qData', '2025-02-18 10:58:56', '', NULL, '');
INSERT INTO `system_menu` VALUES (2459, '应用管理新增', 2457, 2, '#', '', NULL, 1, 0, NULL, 'F', '0', '0', 'att:client:client:add', '#', 'qData', '2025-02-18 10:58:56', '', NULL, '');
INSERT INTO `system_menu` VALUES (2460, '应用管理修改', 2457, 3, '#', '', NULL, 1, 0, NULL, 'F', '0', '0', 'att:client:client:edit', '#', 'qData', '2025-02-18 10:58:56', '', NULL, '');
INSERT INTO `system_menu` VALUES (2461, '应用管理删除', 2457, 4, '#', '', NULL, 1, 0, NULL, 'F', '0', '0', 'att:client:client:remove', '#', 'qData', '2025-02-18 10:58:56', '', NULL, '');
INSERT INTO `system_menu` VALUES (2462, '应用管理导出', 2457, 5, '#', '', NULL, 1, 0, NULL, 'F', '0', '0', 'att:client:client:export', '#', 'qData', '2025-02-18 10:58:56', '', NULL, '');
INSERT INTO `system_menu` VALUES (2463, '应用管理导入', 2457, 6, '#', '', NULL, 1, 0, NULL, 'F', '0', '0', 'att:client:client:import', '#', 'qData', '2025-02-18 10:58:57', '', NULL, '');
INSERT INTO `system_menu` VALUES (2464, '数据集成任务查询', 2426, 1, '#', '', NULL, 1, 0, NULL, 'F', '0', '0', 'dpp:etl:etltask:query', '#', 'qData', '2025-02-19 11:32:10', '', NULL, '');
INSERT INTO `system_menu` VALUES (2465, '数据集成任务新增', 2426, 2, '#', '', NULL, 1, 0, NULL, 'F', '0', '0', 'dpp:etl:etltask:add', '#', 'qData', '2025-02-19 11:32:10', '', NULL, '');
INSERT INTO `system_menu` VALUES (2466, '数据集成任务修改', 2426, 3, '#', '', NULL, 1, 0, NULL, 'F', '0', '0', 'dpp:etl:etltask:edit', '#', 'qData', '2025-02-19 11:32:10', '', NULL, '');
INSERT INTO `system_menu` VALUES (2467, '数据集成任务删除', 2426, 4, '#', '', NULL, 1, 0, NULL, 'F', '0', '0', 'dpp:etl:etltask:remove', '#', 'qData', '2025-02-19 11:32:10', '', NULL, '');
INSERT INTO `system_menu` VALUES (2468, '数据集成任务导出', 2426, 5, '#', '', NULL, 1, 0, NULL, 'F', '0', '0', 'dpp:etl:etltask:export', '#', 'qData', '2025-02-19 11:32:10', '', NULL, '');
INSERT INTO `system_menu` VALUES (2469, '数据集成任务导入', 2426, 6, '#', '', NULL, 1, 0, NULL, 'F', '0', '0', 'dpp:etl:etltask:import', '#', 'qData', '2025-02-19 11:32:11', '', NULL, '');
INSERT INTO `system_menu` VALUES (2470, '任务实例', 2477, 1, 'dppEtlTaskInstance', 'dpp/etltaskinstance/index', NULL, 1, 0, NULL, 'C', '0', '0', 'dpp:etl:etlnodeinstance:list', '#', 'qData', '2025-02-21 21:08:31', 'qData', '2025-03-31 09:39:42', '数据集成任务实例菜单');
INSERT INTO `system_menu` VALUES (2471, '数据集成任务实例查询', 2470, 1, '#', '', NULL, 1, 0, NULL, 'F', '0', '0', 'dpp:etl:etlnodeinstance:query', '#', 'qData', '2025-02-21 21:09:17', 'qData', '2025-02-27 10:20:40', '');
INSERT INTO `system_menu` VALUES (2472, '数据集成任务实例新增', 2470, 2, '#', '', NULL, 1, 0, NULL, 'F', '0', '0', 'dpp:etl:etlnodeinstance:add', '#', 'qData', '2025-02-21 21:09:17', 'qData', '2025-02-27 10:20:48', '');
INSERT INTO `system_menu` VALUES (2473, '数据集成任务实例修改', 2470, 3, '#', '', NULL, 1, 0, NULL, 'F', '0', '0', 'dpp:etl:etlnodeinstance:edit', '#', 'qData', '2025-02-21 21:09:17', 'qData', '2025-02-27 10:20:53', '');
INSERT INTO `system_menu` VALUES (2474, '数据集成任务实例删除', 2470, 4, '#', '', NULL, 1, 0, NULL, 'F', '0', '0', 'dpp:etl:etlnodeinstance:remove', '#', 'qData', '2025-02-21 21:09:17', 'qData', '2025-02-27 10:20:58', '');
INSERT INTO `system_menu` VALUES (2475, '数据集成任务实例导出', 2470, 5, '#', '', NULL, 1, 0, NULL, 'F', '0', '0', 'dpp:etl:etlnodeinstance:export', '#', 'qData', '2025-02-21 21:09:17', 'qData', '2025-02-27 10:21:03', '');
INSERT INTO `system_menu` VALUES (2476, '数据集成任务实例导入', 2470, 6, '#', '', NULL, 1, 0, NULL, 'F', '0', '0', 'dpp:etl:etlnodeinstance:import', '#', 'qData', '2025-02-21 21:09:17', 'qData', '2025-02-27 10:21:11', '');
INSERT INTO `system_menu` VALUES (2477, '运维管理', 2397, 7, 'etltaskinstance', NULL, NULL, 1, 0, NULL, 'M', '0', '0', '', 'box-3-line', 'qData', '2025-02-27 14:00:09', 'admin', '2025-05-30 16:12:53', '');
INSERT INTO `system_menu` VALUES (2487, '数据开发类目', 2552, 3, 'AttDataDevCat', 'att/cat/attDataDevCat/index', NULL, 1, 0, NULL, 'C', '0', '0', 'att:cat:attdatadevcat:list', '#', 'qData', '2025-03-11 11:18:56', 'qData', '2025-04-27 14:04:51', '数据开发类目管理菜单');
INSERT INTO `system_menu` VALUES (2488, '数据开发类目管理查询', 2487, 1, '#', '', NULL, 1, 0, NULL, 'F', '0', '0', 'att:cat:attdatadevcat:query', '#', 'qData', '2025-03-11 11:19:16', '', NULL, '');
INSERT INTO `system_menu` VALUES (2489, '数据开发类目管理新增', 2487, 2, '#', '', NULL, 1, 0, NULL, 'F', '0', '0', 'att:cat:attdatadevcat:add', '#', 'qData', '2025-03-11 11:19:16', '', NULL, '');
INSERT INTO `system_menu` VALUES (2490, '数据开发类目管理修改', 2487, 3, '#', '', NULL, 1, 0, NULL, 'F', '0', '0', 'att:cat:attdatadevcat:edit', '#', 'qData', '2025-03-11 11:19:16', '', NULL, '');
INSERT INTO `system_menu` VALUES (2491, '数据开发类目管理删除', 2487, 4, '#', '', NULL, 1, 0, NULL, 'F', '0', '0', 'att:cat:attdatadevcat:remove', '#', 'qData', '2025-03-11 11:19:16', '', NULL, '');
INSERT INTO `system_menu` VALUES (2492, '数据开发类目管理导出', 2487, 5, '#', '', NULL, 1, 0, NULL, 'F', '0', '0', 'att:cat:attdatadevcat:export', '#', 'qData', '2025-03-11 11:19:16', '', NULL, '');
INSERT INTO `system_menu` VALUES (2493, '数据开发类目管理导入', 2487, 6, '#', '', NULL, 1, 0, NULL, 'F', '0', '0', 'att:cat:attdatadevcat:import', '#', 'qData', '2025-03-11 11:19:16', '', NULL, '');
INSERT INTO `system_menu` VALUES (2494, '数据API服务目录', 2352, 16, 'AttApiCat', 'att/cat/attApiCat/index', NULL, 1, 0, NULL, 'C', '0', '0', 'att:cat:attapicat:list', '#', 'qData', '2025-03-11 14:09:22', 'qData', '2025-05-23 16:23:49', '数据服务类目管理菜单');
INSERT INTO `system_menu` VALUES (2495, '数据服务类目管理查询', 2494, 1, '#', '', NULL, 1, 0, NULL, 'F', '0', '0', 'att:cat:attapicat:query', '#', 'qData', '2025-03-11 14:09:50', '', NULL, '');
INSERT INTO `system_menu` VALUES (2496, '数据服务类目管理新增', 2494, 2, '#', '', NULL, 1, 0, NULL, 'F', '0', '0', 'att:cat:attapicat:add', '#', 'qData', '2025-03-11 14:09:50', '', NULL, '');
INSERT INTO `system_menu` VALUES (2497, '数据服务类目管理修改', 2494, 3, '#', '', NULL, 1, 0, NULL, 'F', '0', '0', 'att:cat:attapicat:edit', '#', 'qData', '2025-03-11 14:09:50', '', NULL, '');
INSERT INTO `system_menu` VALUES (2498, '数据服务类目管理删除', 2494, 4, '#', '', NULL, 1, 0, NULL, 'F', '0', '0', 'att:cat:attapicat:remove', '#', 'qData', '2025-03-11 14:09:50', '', NULL, '');
INSERT INTO `system_menu` VALUES (2499, '数据服务类目管理导出', 2494, 5, '#', '', NULL, 1, 0, NULL, 'F', '0', '0', 'att:cat:attapicat:export', '#', 'qData', '2025-03-11 14:09:50', '', NULL, '');
INSERT INTO `system_menu` VALUES (2500, '数据服务类目管理导入', 2494, 6, '#', '', NULL, 1, 0, NULL, 'F', '0', '0', 'att:cat:attapicat:import', '#', 'qData', '2025-03-11 14:09:50', '', NULL, '');
INSERT INTO `system_menu` VALUES (2501, '任务类目', 2552, 1, 'AttTaskCat', 'att/cat/attTaskCat/index', NULL, 1, 0, NULL, 'C', '0', '0', 'att:cat:atttaskcat:list', '#', 'qData', '2025-03-11 14:32:20', 'qData', '2025-04-27 14:04:23', '数据集成任务类目管理菜单');
INSERT INTO `system_menu` VALUES (2502, '数据集成任务类目管理查询', 2501, 1, '#', '', NULL, 1, 0, NULL, 'F', '0', '0', 'att:cat:atttaskcat:query', '#', 'qData', '2025-03-11 14:32:35', '', NULL, '');
INSERT INTO `system_menu` VALUES (2503, '数据集成任务类目管理新增', 2501, 2, '#', '', NULL, 1, 0, NULL, 'F', '0', '0', 'att:cat:atttaskcat:add', '#', 'qData', '2025-03-11 14:32:35', '', NULL, '');
INSERT INTO `system_menu` VALUES (2504, '数据集成任务类目管理修改', 2501, 3, '#', '', NULL, 1, 0, NULL, 'F', '0', '0', 'att:cat:atttaskcat:edit', '#', 'qData', '2025-03-11 14:32:35', '', NULL, '');
INSERT INTO `system_menu` VALUES (2505, '数据集成任务类目管理删除', 2501, 4, '#', '', NULL, 1, 0, NULL, 'F', '0', '0', 'att:cat:atttaskcat:remove', '#', 'qData', '2025-03-11 14:32:35', '', NULL, '');
INSERT INTO `system_menu` VALUES (2506, '数据集成任务类目管理导出', 2501, 5, '#', '', NULL, 1, 0, NULL, 'F', '0', '0', 'att:cat:atttaskcat:export', '#', 'qData', '2025-03-11 14:32:35', '', NULL, '');
INSERT INTO `system_menu` VALUES (2507, '数据集成任务类目管理导入', 2501, 6, '#', '', NULL, 1, 0, NULL, 'F', '0', '0', 'att:cat:atttaskcat:import', '#', 'qData', '2025-03-11 14:32:35', '', NULL, '');
INSERT INTO `system_menu` VALUES (2510, '任务管理', 2397, 3, 'tasker', NULL, NULL, 1, 0, NULL, 'M', '0', '0', '', 'file-list-line', 'qData', '2025-03-12 17:47:13', 'admin', '2025-05-30 15:51:17', '');
INSERT INTO `system_menu` VALUES (2511, '数据开发', 2510, 2, 'dpptaskerddv', 'dpp/tasker/ddv/index', NULL, 1, 0, NULL, 'C', '0', '0', 'dpp:tasker:ddv:list', '#', 'qData', '2025-03-12 17:55:40', 'qData', '2025-03-26 18:30:02', '');
INSERT INTO `system_menu` VALUES (2513, '数据连接', 2397, 1, 'dppDatasource', 'da/datasource/index', '{\"type\":1}', 1, 0, NULL, 'C', '0', '0', 'da:datasource:datasource:list', 'shuffle-line', 'qData', '2025-03-12 18:19:41', 'admin', '2025-05-30 15:50:17', '');
INSERT INTO `system_menu` VALUES (2519, '数据开发新增', 2511, 0, '', NULL, NULL, 1, 0, NULL, 'F', '0', '0', 'dpp:etl:ddv:add', '#', 'qData', '2025-03-13 09:38:04', '', NULL, '');
INSERT INTO `system_menu` VALUES (2520, '数据开发修改', 2511, 0, '', NULL, NULL, 1, 0, NULL, 'F', '0', '0', 'dpp:etl:ddv:edit', '#', 'qData', '2025-03-13 09:38:28', '', NULL, '');
INSERT INTO `system_menu` VALUES (2521, '数据开发删除', 2511, 0, '', NULL, NULL, 1, 0, NULL, 'F', '0', '0', 'dpp:etl:ddv:remove', '#', 'qData', '2025-03-13 09:38:59', '', NULL, '');
INSERT INTO `system_menu` VALUES (2522, '数据开发调度周期', 2511, 0, '', NULL, NULL, 1, 0, NULL, 'F', '0', '0', 'dpp:etl:ddv:query:query', '#', 'qData', '2025-03-13 09:39:24', '', NULL, '');
INSERT INTO `system_menu` VALUES (2523, '数据开发运行实例', 2511, 0, '', NULL, NULL, 1, 0, NULL, 'F', '0', '0', 'dpp:etl:ddv:shili', '#', 'qData', '2025-03-13 09:39:53', '', NULL, '');
INSERT INTO `system_menu` VALUES (2524, '数据源查询', 2513, 0, '', NULL, NULL, 1, 0, NULL, 'F', '0', '0', 'da:datasource:datasource:query', '#', 'qData', '2025-03-13 11:55:35', '', NULL, '');
INSERT INTO `system_menu` VALUES (2525, '数据源新增', 2513, 1, '', NULL, NULL, 1, 0, NULL, 'F', '0', '0', 'da:datasource:datasource:add', '#', 'qData', '2025-03-13 11:55:54', '', NULL, '');
INSERT INTO `system_menu` VALUES (2526, '数据源修改', 2513, 2, '', NULL, NULL, 1, 0, NULL, 'F', '0', '0', 'da:datasource:datasource:edit', '#', 'qData', '2025-03-13 11:56:16', '', NULL, '');
INSERT INTO `system_menu` VALUES (2527, '数据源删除', 2513, 3, '', NULL, NULL, 1, 0, NULL, 'F', '0', '0', 'da:datasource:datasource:remove', '#', 'qData', '2025-03-13 11:56:40', '', NULL, '');
INSERT INTO `system_menu` VALUES (2528, '数据源导出', 2513, 4, '', NULL, NULL, 1, 0, NULL, 'F', '0', '0', 'da:datasource:datasource:export', '#', 'qData', '2025-03-13 11:57:04', '', NULL, '');
INSERT INTO `system_menu` VALUES (2529, '数据源导入', 2513, 0, '', NULL, NULL, 1, 0, NULL, 'F', '0', '0', 'da:datasource:datasource:import', '#', 'qData', '2025-03-13 11:57:22', '', NULL, '');
INSERT INTO `system_menu` VALUES (2530, '资产审核', 2314, 4, 'daAssetApply', 'da/daAssetApply/index', NULL, 1, 0, NULL, 'C', '0', '0', 'da:daAssetApply:daassetapply:list', 'currency-line', 'qData', '2025-03-19 11:00:10', 'admin', '2025-05-30 15:48:46', '数据资产申请菜单');
INSERT INTO `system_menu` VALUES (2531, '数据资产申请查询', 2530, 1, '#', '', NULL, 1, 0, NULL, 'F', '0', '0', 'da:daAssetApply:daassetapply:query', '#', 'qData', '2025-03-19 11:00:56', '', NULL, '');
INSERT INTO `system_menu` VALUES (2532, '数据资产申请新增', 2530, 2, '#', '', NULL, 1, 0, NULL, 'F', '0', '0', 'da:daAssetApply:daassetapply:add', '#', 'qData', '2025-03-19 11:00:56', '', NULL, '');
INSERT INTO `system_menu` VALUES (2533, '数据资产申请修改', 2530, 3, '#', '', NULL, 1, 0, NULL, 'F', '0', '0', 'da:daAssetApply:daassetapply:edit', '#', 'qData', '2025-03-19 11:00:56', '', NULL, '');
INSERT INTO `system_menu` VALUES (2534, '数据资产申请删除', 2530, 4, '#', '', NULL, 1, 0, NULL, 'F', '0', '0', 'da:daAssetApply:daassetapply:remove', '#', 'qData', '2025-03-19 11:00:56', '', NULL, '');
INSERT INTO `system_menu` VALUES (2535, '数据资产申请导出', 2530, 5, '#', '', NULL, 1, 0, NULL, 'F', '0', '0', 'da:daAssetApply:daassetapply:export', '#', 'qData', '2025-03-19 11:00:56', '', NULL, '');
INSERT INTO `system_menu` VALUES (2536, '数据资产申请导入', 2530, 6, '#', '', NULL, 1, 0, NULL, 'F', '0', '0', 'da:daAssetApply:daassetapply:import', '#', 'qData', '2025-03-19 11:00:56', '', NULL, '');
INSERT INTO `system_menu` VALUES (2537, '项目资产', 2397, 2, 'dppAsset', 'dpp/asset/index', '{\"type\":1}', 1, 0, '', 'C', '0', '0', 'dpp:asset:asset:list', 'exchange-dollar-line', 'qData', '2025-03-19 19:21:11', 'admin', '2025-05-30 15:50:57', '');
INSERT INTO `system_menu` VALUES (2538, '资产地图查询', 2537, 0, '', NULL, NULL, 1, 0, NULL, 'F', '0', '0', 'dpp:asset:asset:query', '#', 'qData', '2025-03-21 09:56:13', 'renmengting', '2025-04-16 11:33:48', '');
INSERT INTO `system_menu` VALUES (2539, '添加', 2537, 0, '', NULL, NULL, 1, 0, NULL, 'F', '0', '0', 'dpp:asset:asset:add', '#', 'qData', '2025-03-21 09:57:39', 'renmengting', '2025-04-16 11:34:45', '');
INSERT INTO `system_menu` VALUES (2540, '修改', 2537, 0, '', NULL, NULL, 1, 0, NULL, 'F', '0', '0', 'dpp:asset:asset:edit', '#', 'qData', '2025-03-21 09:58:01', 'renmengting', '2025-04-16 11:35:14', '');
INSERT INTO `system_menu` VALUES (2541, '删除', 2537, 0, '', NULL, NULL, 1, 0, NULL, 'F', '0', '0', 'dpp:asset:asset:remove', '#', 'qData', '2025-03-21 09:58:53', 'qData', '2025-04-21 11:48:51', '');
INSERT INTO `system_menu` VALUES (2542, '导入', 2537, 0, '', NULL, NULL, 1, 0, NULL, 'F', '0', '0', 'da:asset:asset:export', '#', 'qData', '2025-03-21 09:59:14', '', NULL, '');
INSERT INTO `system_menu` VALUES (2543, '导出', 2537, 0, '', NULL, NULL, 1, 0, NULL, 'F', '0', '0', 'da:asset:asset:import', '#', 'qData', '2025-03-21 09:59:30', '', NULL, '');
INSERT INTO `system_menu` VALUES (2544, '任务分类管理', 2552, 6, 'cat', NULL, NULL, 1, 0, NULL, 'M', '1', '1', '', '#', 'qData', '2025-03-27 15:30:38', 'qData', '2025-04-27 14:05:00', '');
INSERT INTO `system_menu` VALUES (2545, '数据集成任务详情', 2426, 0, '', NULL, NULL, 1, 0, NULL, 'F', '0', '0', 'dpp:etl:etltask:info', '#', 'qData', '2025-03-28 14:43:45', 'qData', '2025-03-28 14:44:03', '');
INSERT INTO `system_menu` VALUES (2546, '数据集成任务调度周期', 2426, 0, '', NULL, NULL, 1, 0, NULL, 'F', '0', '0', 'dpp:etl:etltask:query', '#', 'qData', '2025-03-28 14:45:11', '', NULL, '');
INSERT INTO `system_menu` VALUES (2547, '数据集成任务执行一次', 2426, 0, '', NULL, NULL, 1, 0, NULL, 'F', '0', '0', 'da:discovery:discoverytask:once', '#', 'qData', '2025-03-28 14:45:55', '', NULL, '');
INSERT INTO `system_menu` VALUES (2548, '数据开发详情', 2511, 0, '', NULL, NULL, 1, 0, NULL, 'F', '0', '0', 'dpp:etl:ddv:info', '#', 'qData', '2025-03-28 14:48:28', '', NULL, '');
INSERT INTO `system_menu` VALUES (2549, '数据开发执行一次', 2511, 0, '', NULL, NULL, 1, 0, NULL, 'F', '0', '0', 'dpp:etl:ddv::once', '#', 'qData', '2025-03-28 14:49:11', '', NULL, '');
INSERT INTO `system_menu` VALUES (2552, '项目基础管理', 2397, 9, 'dpp', NULL, NULL, 1, 0, NULL, 'M', '0', '0', '', 'lifebuoy-line', 'qData', '2025-03-31 09:29:41', 'admin', '2025-05-30 16:13:58', '');
INSERT INTO `system_menu` VALUES (2554, '数据查询', 2314, 5, 'executeSqlQuery', 'da/executesqlquery/index', NULL, 1, 0, NULL, 'C', '0', '0', 'da:datasource:executesqlquery:list', 'safe-line', 'qData', '2025-04-09 15:56:05', 'admin', '2025-05-30 15:49:11', '');

-- ----------------------------
-- Table structure for system_notice
-- ----------------------------
DROP TABLE IF EXISTS `system_notice`;
CREATE TABLE `system_notice`  (
  `notice_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '公告ID',
  `notice_title` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '公告标题',
  `notice_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '公告类型（1通知 2公告）',
  `notice_content` longblob NULL COMMENT '公告内容',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '公告状态（0正常 1关闭）',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`notice_id`) USING BTREE,
  UNIQUE INDEX `NOTICE_ID_90024528315100`(`notice_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 13 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '通知公告表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of system_notice
-- ----------------------------
INSERT INTO `system_notice` VALUES (11, 'qData数据中台正式开源！', '2', NULL, '0', 'qData', '2025-05-12 10:00:00', '', NULL, NULL);
INSERT INTO `system_notice` VALUES (12, 'qData期待与您携手共建数据生态！', '2', NULL, '0', 'qData', '2025-05-26 14:00:00', '', NULL, NULL);

-- ----------------------------
-- Table structure for system_oper_log
-- ----------------------------
DROP TABLE IF EXISTS `system_oper_log`;
CREATE TABLE `system_oper_log`  (
  `oper_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '日志主键',
  `title` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '模块标题',
  `business_type` int(11) NULL DEFAULT 0 COMMENT '业务类型（0其它 1新增 2修改 3删除）',
  `method` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '方法名称',
  `request_method` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '请求方式',
  `operator_type` int(11) NULL DEFAULT 0 COMMENT '操作类别（0其它 1后台用户 2手机端用户）',
  `oper_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '操作人员',
  `dept_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '部门名称',
  `oper_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '请求URL',
  `oper_ip` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '主机地址',
  `oper_location` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '操作地点',
  `oper_param` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '请求参数',
  `json_result` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '返回参数',
  `status` int(11) NULL DEFAULT 0 COMMENT '操作状态（0正常 1异常）',
  `error_msg` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '错误消息',
  `oper_time` datetime NULL DEFAULT NULL COMMENT '操作时间',
  `cost_time` bigint(20) NULL DEFAULT 0 COMMENT '消耗时间',
  UNIQUE INDEX `OPER_ID_26035488445100`(`oper_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 922 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '操作日志记录' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of system_oper_log
-- ----------------------------

-- ----------------------------
-- Table structure for system_post
-- ----------------------------
DROP TABLE IF EXISTS `system_post`;
CREATE TABLE `system_post`  (
  `post_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '岗位ID',
  `post_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '岗位编码',
  `post_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '岗位名称',
  `post_sort` int(11) NOT NULL COMMENT '显示顺序',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '状态（0正常 1停用）',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`post_id`) USING BTREE,
  UNIQUE INDEX `POST_ID_90024906430300`(`post_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '岗位信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of system_post
-- ----------------------------
INSERT INTO `system_post` VALUES (1, 'ceo', '董事长', 1, '0', 'qData', '2024-05-06 06:12:17', 'qData', '2025-03-07 14:06:54', '');
INSERT INTO `system_post` VALUES (2, 'se', '项目经理', 2, '0', 'qData', '2024-05-06 06:12:17', '', NULL, '');
INSERT INTO `system_post` VALUES (3, 'hr', '人力资源', 3, '0', 'qData', '2024-05-06 06:12:17', 'qData', '2024-11-26 15:50:39', '');
INSERT INTO `system_post` VALUES (4, 'user', '普通员工', 4, '0', 'qData', '2024-05-06 06:12:17', '', NULL, '');

-- ----------------------------
-- Table structure for system_role
-- ----------------------------
DROP TABLE IF EXISTS `system_role`;
CREATE TABLE `system_role`  (
  `role_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '角色ID',
  `role_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '角色名称',
  `role_key` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '角色权限字符串',
  `role_sort` int(11) NOT NULL COMMENT '显示顺序',
  `data_scope` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '1' COMMENT '数据范围（1：全部数据权限 2：自定数据权限 3：本部门数据权限 4：本部门及以下数据权限）',
  `menu_check_strictly` tinyint(4) NULL DEFAULT 1 COMMENT '菜单树选择项是否关联显示',
  `dept_check_strictly` tinyint(4) NULL DEFAULT 1 COMMENT '部门树选择项是否关联显示',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '角色状态（0正常 1停用）',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `project_id` bigint(20) NULL DEFAULT NULL COMMENT '项目空间ID',
  PRIMARY KEY (`role_id`) USING BTREE,
  UNIQUE INDEX `ROLE_ID_90025418482400`(`role_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 190 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '角色信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of system_role
-- ----------------------------
INSERT INTO `system_role` VALUES (1, '超级管理员', 'qData', 1, '1', 1, 1, '0', '0', 'qData', '2024-05-06 06:12:17', '', NULL, '超级管理员', 0);
INSERT INTO `system_role` VALUES (2, '研发人员', 'common', 2, '2', 1, 0, '0', '0', 'qData', '2024-05-06 06:12:17', 'qData', '2025-05-23 16:12:25', '普通角色', 0);
INSERT INTO `system_role` VALUES (3, '默认角色', 'default', 3, '1', 0, 0, '0', '0', 'qData', '2024-12-20 14:54:59', 'qData', '2025-05-23 16:11:59', NULL, 0);
INSERT INTO `system_role` VALUES (4, '产品组', 'product', 4, '1', 1, 1, '0', '0', 'qData', '2025-02-05 17:11:59', 'qData', '2025-05-23 16:12:10', NULL, 0);
INSERT INTO `system_role` VALUES (12, '研发模块开发系统级', 'sysDpp', 3, '1', 1, 1, '0', '0', 'liuhaosheng', '2025-02-13 14:36:35', 'qData', '2025-05-23 16:12:04', NULL, 0);
INSERT INTO `system_role` VALUES (15, '空间管理员', 'gly', 1, '1', 1, 1, '0', '0', 'qData', '2025-02-14 10:58:46', 'qData', '2025-04-01 09:54:33', NULL, -1);
INSERT INTO `system_role` VALUES (43, '数据开发人员', 'sjkf', 2, '1', 1, 1, '0', '0', '', NULL, 'qData', '2025-04-01 09:54:48', NULL, -1);
INSERT INTO `system_role` VALUES (44, '质量评估人员', 'zlpg', 3, '1', 1, 1, '0', '0', '', NULL, 'qData', '2025-04-01 09:57:02', NULL, -1);
INSERT INTO `system_role` VALUES (45, '数据分析人员', 'sjfx', 4, '1', 1, 1, '0', '0', '', NULL, 'qData', '2025-04-01 09:56:56', NULL, -1);
INSERT INTO `system_role` VALUES (46, '运维人员', 'yw', 5, '1', 1, 1, '0', '0', '', NULL, 'shuyexin', '2025-03-25 11:31:19', NULL, -1);
INSERT INTO `system_role` VALUES (185, '空间管理员', 'gly', 1, '1', 1, 1, '0', '0', '', NULL, '', NULL, NULL, 84);
INSERT INTO `system_role` VALUES (186, '数据开发人员', 'sjkf', 2, '1', 1, 1, '0', '0', '', NULL, '', NULL, NULL, 84);
INSERT INTO `system_role` VALUES (187, '质量评估人员', 'zlpg', 3, '1', 1, 1, '0', '0', '', NULL, '', NULL, NULL, 84);
INSERT INTO `system_role` VALUES (188, '数据分析人员', 'sjfx', 4, '1', 1, 1, '0', '0', '', NULL, '', NULL, NULL, 84);
INSERT INTO `system_role` VALUES (189, '运维人员', 'yw', 5, '1', 1, 1, '0', '0', '', NULL, '', NULL, NULL, 84);

-- ----------------------------
-- Table structure for system_role_dept
-- ----------------------------
DROP TABLE IF EXISTS `system_role_dept`;
CREATE TABLE `system_role_dept`  (
  `role_id` bigint(20) NOT NULL COMMENT '角色ID',
  `dept_id` bigint(20) NOT NULL COMMENT '部门ID',
  PRIMARY KEY (`role_id`, `dept_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '角色和部门关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of system_role_dept
-- ----------------------------
INSERT INTO `system_role_dept` VALUES (2, 100);
INSERT INTO `system_role_dept` VALUES (2, 101);
INSERT INTO `system_role_dept` VALUES (2, 103);
INSERT INTO `system_role_dept` VALUES (2, 105);
INSERT INTO `system_role_dept` VALUES (2, 107);

-- ----------------------------
-- Table structure for system_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `system_role_menu`;
CREATE TABLE `system_role_menu`  (
  `role_id` bigint(20) NOT NULL COMMENT '角色ID',
  `menu_id` bigint(20) NOT NULL COMMENT '菜单ID',
  `project_id` bigint(20) NULL DEFAULT 0 COMMENT '项目id',
  PRIMARY KEY (`role_id`, `menu_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '角色和菜单关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of system_role_menu
-- ----------------------------
INSERT INTO `system_role_menu` VALUES (3, 2243, 0);
INSERT INTO `system_role_menu` VALUES (3, 2244, 0);
INSERT INTO `system_role_menu` VALUES (3, 2245, 0);
INSERT INTO `system_role_menu` VALUES (3, 2246, 0);
INSERT INTO `system_role_menu` VALUES (3, 2247, 0);
INSERT INTO `system_role_menu` VALUES (3, 2248, 0);
INSERT INTO `system_role_menu` VALUES (3, 2249, 0);
INSERT INTO `system_role_menu` VALUES (3, 2257, 0);
INSERT INTO `system_role_menu` VALUES (3, 2258, 0);
INSERT INTO `system_role_menu` VALUES (3, 2259, 0);
INSERT INTO `system_role_menu` VALUES (3, 2260, 0);
INSERT INTO `system_role_menu` VALUES (3, 2261, 0);
INSERT INTO `system_role_menu` VALUES (3, 2262, 0);
INSERT INTO `system_role_menu` VALUES (3, 2263, 0);
INSERT INTO `system_role_menu` VALUES (3, 2264, 0);
INSERT INTO `system_role_menu` VALUES (3, 2265, 0);
INSERT INTO `system_role_menu` VALUES (3, 2266, 0);
INSERT INTO `system_role_menu` VALUES (3, 2267, 0);
INSERT INTO `system_role_menu` VALUES (3, 2268, 0);
INSERT INTO `system_role_menu` VALUES (3, 2269, 0);
INSERT INTO `system_role_menu` VALUES (3, 2270, 0);
INSERT INTO `system_role_menu` VALUES (3, 2271, 0);
INSERT INTO `system_role_menu` VALUES (3, 2272, 0);
INSERT INTO `system_role_menu` VALUES (3, 2273, 0);
INSERT INTO `system_role_menu` VALUES (3, 2274, 0);
INSERT INTO `system_role_menu` VALUES (3, 2275, 0);
INSERT INTO `system_role_menu` VALUES (3, 2276, 0);
INSERT INTO `system_role_menu` VALUES (3, 2277, 0);
INSERT INTO `system_role_menu` VALUES (3, 2278, 0);
INSERT INTO `system_role_menu` VALUES (3, 2279, 0);
INSERT INTO `system_role_menu` VALUES (3, 2280, 0);
INSERT INTO `system_role_menu` VALUES (3, 2281, 0);
INSERT INTO `system_role_menu` VALUES (3, 2282, 0);
INSERT INTO `system_role_menu` VALUES (3, 2283, 0);
INSERT INTO `system_role_menu` VALUES (3, 2284, 0);
INSERT INTO `system_role_menu` VALUES (3, 2292, 0);
INSERT INTO `system_role_menu` VALUES (3, 2293, 0);
INSERT INTO `system_role_menu` VALUES (3, 2294, 0);
INSERT INTO `system_role_menu` VALUES (3, 2295, 0);
INSERT INTO `system_role_menu` VALUES (3, 2296, 0);
INSERT INTO `system_role_menu` VALUES (3, 2297, 0);
INSERT INTO `system_role_menu` VALUES (3, 2298, 0);
INSERT INTO `system_role_menu` VALUES (3, 2299, 0);
INSERT INTO `system_role_menu` VALUES (3, 2300, 0);
INSERT INTO `system_role_menu` VALUES (3, 2301, 0);
INSERT INTO `system_role_menu` VALUES (3, 2302, 0);
INSERT INTO `system_role_menu` VALUES (3, 2303, 0);
INSERT INTO `system_role_menu` VALUES (3, 2304, 0);
INSERT INTO `system_role_menu` VALUES (3, 2305, 0);
INSERT INTO `system_role_menu` VALUES (3, 2306, 0);
INSERT INTO `system_role_menu` VALUES (3, 2307, 0);
INSERT INTO `system_role_menu` VALUES (3, 2308, 0);
INSERT INTO `system_role_menu` VALUES (3, 2309, 0);
INSERT INTO `system_role_menu` VALUES (3, 2310, 0);
INSERT INTO `system_role_menu` VALUES (3, 2311, 0);
INSERT INTO `system_role_menu` VALUES (3, 2312, 0);
INSERT INTO `system_role_menu` VALUES (3, 2313, 0);
INSERT INTO `system_role_menu` VALUES (3, 2314, 0);
INSERT INTO `system_role_menu` VALUES (3, 2315, 0);
INSERT INTO `system_role_menu` VALUES (3, 2316, 0);
INSERT INTO `system_role_menu` VALUES (3, 2317, 0);
INSERT INTO `system_role_menu` VALUES (3, 2318, 0);
INSERT INTO `system_role_menu` VALUES (3, 2319, 0);
INSERT INTO `system_role_menu` VALUES (3, 2320, 0);
INSERT INTO `system_role_menu` VALUES (3, 2321, 0);
INSERT INTO `system_role_menu` VALUES (3, 2322, 0);
INSERT INTO `system_role_menu` VALUES (3, 2323, 0);
INSERT INTO `system_role_menu` VALUES (3, 2324, 0);
INSERT INTO `system_role_menu` VALUES (3, 2325, 0);
INSERT INTO `system_role_menu` VALUES (3, 2326, 0);
INSERT INTO `system_role_menu` VALUES (3, 2327, 0);
INSERT INTO `system_role_menu` VALUES (3, 2328, 0);
INSERT INTO `system_role_menu` VALUES (3, 2329, 0);
INSERT INTO `system_role_menu` VALUES (3, 2330, 0);
INSERT INTO `system_role_menu` VALUES (3, 2331, 0);
INSERT INTO `system_role_menu` VALUES (3, 2332, 0);
INSERT INTO `system_role_menu` VALUES (3, 2333, 0);
INSERT INTO `system_role_menu` VALUES (3, 2334, 0);
INSERT INTO `system_role_menu` VALUES (3, 2335, 0);
INSERT INTO `system_role_menu` VALUES (3, 2336, 0);
INSERT INTO `system_role_menu` VALUES (3, 2337, 0);
INSERT INTO `system_role_menu` VALUES (3, 2338, 0);
INSERT INTO `system_role_menu` VALUES (3, 2339, 0);
INSERT INTO `system_role_menu` VALUES (3, 2340, 0);
INSERT INTO `system_role_menu` VALUES (3, 2341, 0);
INSERT INTO `system_role_menu` VALUES (3, 2342, 0);
INSERT INTO `system_role_menu` VALUES (3, 2343, 0);
INSERT INTO `system_role_menu` VALUES (3, 2344, 0);
INSERT INTO `system_role_menu` VALUES (3, 2345, 0);
INSERT INTO `system_role_menu` VALUES (3, 2346, 0);
INSERT INTO `system_role_menu` VALUES (3, 2347, 0);
INSERT INTO `system_role_menu` VALUES (3, 2348, 0);
INSERT INTO `system_role_menu` VALUES (3, 2349, 0);
INSERT INTO `system_role_menu` VALUES (3, 2350, 0);
INSERT INTO `system_role_menu` VALUES (3, 2351, 0);
INSERT INTO `system_role_menu` VALUES (3, 2352, 0);
INSERT INTO `system_role_menu` VALUES (3, 2354, 0);
INSERT INTO `system_role_menu` VALUES (3, 2355, 0);
INSERT INTO `system_role_menu` VALUES (3, 2356, 0);
INSERT INTO `system_role_menu` VALUES (3, 2357, 0);
INSERT INTO `system_role_menu` VALUES (3, 2358, 0);
INSERT INTO `system_role_menu` VALUES (3, 2359, 0);
INSERT INTO `system_role_menu` VALUES (3, 2360, 0);
INSERT INTO `system_role_menu` VALUES (3, 2368, 0);
INSERT INTO `system_role_menu` VALUES (3, 2369, 0);
INSERT INTO `system_role_menu` VALUES (3, 2370, 0);
INSERT INTO `system_role_menu` VALUES (3, 2371, 0);
INSERT INTO `system_role_menu` VALUES (3, 2372, 0);
INSERT INTO `system_role_menu` VALUES (3, 2373, 0);
INSERT INTO `system_role_menu` VALUES (3, 2374, 0);
INSERT INTO `system_role_menu` VALUES (3, 2375, 0);
INSERT INTO `system_role_menu` VALUES (3, 2376, 0);
INSERT INTO `system_role_menu` VALUES (3, 2377, 0);
INSERT INTO `system_role_menu` VALUES (3, 2378, 0);
INSERT INTO `system_role_menu` VALUES (3, 2379, 0);
INSERT INTO `system_role_menu` VALUES (3, 2380, 0);
INSERT INTO `system_role_menu` VALUES (3, 2381, 0);
INSERT INTO `system_role_menu` VALUES (3, 2382, 0);
INSERT INTO `system_role_menu` VALUES (3, 2383, 0);
INSERT INTO `system_role_menu` VALUES (3, 2384, 0);
INSERT INTO `system_role_menu` VALUES (3, 2385, 0);
INSERT INTO `system_role_menu` VALUES (3, 2386, 0);
INSERT INTO `system_role_menu` VALUES (3, 2387, 0);
INSERT INTO `system_role_menu` VALUES (3, 2388, 0);
INSERT INTO `system_role_menu` VALUES (3, 2389, 0);
INSERT INTO `system_role_menu` VALUES (3, 2390, 0);
INSERT INTO `system_role_menu` VALUES (3, 2391, 0);
INSERT INTO `system_role_menu` VALUES (3, 2392, 0);
INSERT INTO `system_role_menu` VALUES (3, 2393, 0);
INSERT INTO `system_role_menu` VALUES (3, 2394, 0);
INSERT INTO `system_role_menu` VALUES (3, 2395, 0);
INSERT INTO `system_role_menu` VALUES (3, 2396, 0);
INSERT INTO `system_role_menu` VALUES (3, 2427, 0);
INSERT INTO `system_role_menu` VALUES (3, 2428, 0);
INSERT INTO `system_role_menu` VALUES (3, 2429, 0);
INSERT INTO `system_role_menu` VALUES (3, 2430, 0);
INSERT INTO `system_role_menu` VALUES (3, 2431, 0);
INSERT INTO `system_role_menu` VALUES (3, 2432, 0);
INSERT INTO `system_role_menu` VALUES (3, 2433, 0);
INSERT INTO `system_role_menu` VALUES (3, 2434, 0);
INSERT INTO `system_role_menu` VALUES (3, 2436, 0);
INSERT INTO `system_role_menu` VALUES (3, 2437, 0);
INSERT INTO `system_role_menu` VALUES (3, 2438, 0);
INSERT INTO `system_role_menu` VALUES (3, 2439, 0);
INSERT INTO `system_role_menu` VALUES (3, 2440, 0);
INSERT INTO `system_role_menu` VALUES (3, 2441, 0);
INSERT INTO `system_role_menu` VALUES (3, 2442, 0);
INSERT INTO `system_role_menu` VALUES (3, 2457, 0);
INSERT INTO `system_role_menu` VALUES (3, 2458, 0);
INSERT INTO `system_role_menu` VALUES (3, 2459, 0);
INSERT INTO `system_role_menu` VALUES (3, 2460, 0);
INSERT INTO `system_role_menu` VALUES (3, 2461, 0);
INSERT INTO `system_role_menu` VALUES (3, 2462, 0);
INSERT INTO `system_role_menu` VALUES (3, 2463, 0);
INSERT INTO `system_role_menu` VALUES (3, 2494, 0);
INSERT INTO `system_role_menu` VALUES (3, 2530, 0);
INSERT INTO `system_role_menu` VALUES (3, 2531, 0);
INSERT INTO `system_role_menu` VALUES (3, 2532, 0);
INSERT INTO `system_role_menu` VALUES (3, 2533, 0);
INSERT INTO `system_role_menu` VALUES (3, 2534, 0);
INSERT INTO `system_role_menu` VALUES (3, 2535, 0);
INSERT INTO `system_role_menu` VALUES (3, 2536, 0);
INSERT INTO `system_role_menu` VALUES (3, 2554, 0);
INSERT INTO `system_role_menu` VALUES (7, 2397, 0);
INSERT INTO `system_role_menu` VALUES (7, 2398, 0);
INSERT INTO `system_role_menu` VALUES (7, 2399, 0);
INSERT INTO `system_role_menu` VALUES (8, 2397, 0);
INSERT INTO `system_role_menu` VALUES (8, 2426, 0);
INSERT INTO `system_role_menu` VALUES (9, 2397, 0);
INSERT INTO `system_role_menu` VALUES (9, 2398, 0);
INSERT INTO `system_role_menu` VALUES (9, 2399, 0);
INSERT INTO `system_role_menu` VALUES (9, 2400, 0);
INSERT INTO `system_role_menu` VALUES (9, 2401, 0);
INSERT INTO `system_role_menu` VALUES (9, 2402, 0);
INSERT INTO `system_role_menu` VALUES (9, 2403, 0);
INSERT INTO `system_role_menu` VALUES (9, 2404, 0);
INSERT INTO `system_role_menu` VALUES (9, 2426, 0);
INSERT INTO `system_role_menu` VALUES (15, 2397, -1);
INSERT INTO `system_role_menu` VALUES (15, 2398, -1);
INSERT INTO `system_role_menu` VALUES (15, 2399, -1);
INSERT INTO `system_role_menu` VALUES (15, 2400, -1);
INSERT INTO `system_role_menu` VALUES (15, 2401, -1);
INSERT INTO `system_role_menu` VALUES (15, 2402, -1);
INSERT INTO `system_role_menu` VALUES (15, 2403, -1);
INSERT INTO `system_role_menu` VALUES (15, 2404, -1);
INSERT INTO `system_role_menu` VALUES (15, 2426, -1);
INSERT INTO `system_role_menu` VALUES (15, 2444, -1);
INSERT INTO `system_role_menu` VALUES (15, 2445, -1);
INSERT INTO `system_role_menu` VALUES (15, 2446, -1);
INSERT INTO `system_role_menu` VALUES (15, 2447, -1);
INSERT INTO `system_role_menu` VALUES (15, 2448, -1);
INSERT INTO `system_role_menu` VALUES (15, 2449, -1);
INSERT INTO `system_role_menu` VALUES (15, 2464, -1);
INSERT INTO `system_role_menu` VALUES (15, 2465, -1);
INSERT INTO `system_role_menu` VALUES (15, 2466, -1);
INSERT INTO `system_role_menu` VALUES (15, 2467, -1);
INSERT INTO `system_role_menu` VALUES (15, 2468, -1);
INSERT INTO `system_role_menu` VALUES (15, 2469, -1);
INSERT INTO `system_role_menu` VALUES (15, 2470, -1);
INSERT INTO `system_role_menu` VALUES (15, 2471, -1);
INSERT INTO `system_role_menu` VALUES (15, 2472, -1);
INSERT INTO `system_role_menu` VALUES (15, 2473, -1);
INSERT INTO `system_role_menu` VALUES (15, 2474, -1);
INSERT INTO `system_role_menu` VALUES (15, 2475, -1);
INSERT INTO `system_role_menu` VALUES (15, 2476, -1);
INSERT INTO `system_role_menu` VALUES (15, 2477, -1);
INSERT INTO `system_role_menu` VALUES (15, 2480, -1);
INSERT INTO `system_role_menu` VALUES (15, 2481, -1);
INSERT INTO `system_role_menu` VALUES (15, 2482, -1);
INSERT INTO `system_role_menu` VALUES (15, 2483, -1);
INSERT INTO `system_role_menu` VALUES (15, 2484, -1);
INSERT INTO `system_role_menu` VALUES (15, 2485, -1);
INSERT INTO `system_role_menu` VALUES (15, 2486, -1);
INSERT INTO `system_role_menu` VALUES (15, 2487, -1);
INSERT INTO `system_role_menu` VALUES (15, 2488, -1);
INSERT INTO `system_role_menu` VALUES (15, 2489, -1);
INSERT INTO `system_role_menu` VALUES (15, 2490, -1);
INSERT INTO `system_role_menu` VALUES (15, 2491, -1);
INSERT INTO `system_role_menu` VALUES (15, 2492, -1);
INSERT INTO `system_role_menu` VALUES (15, 2493, -1);
INSERT INTO `system_role_menu` VALUES (15, 2501, -1);
INSERT INTO `system_role_menu` VALUES (15, 2502, -1);
INSERT INTO `system_role_menu` VALUES (15, 2503, -1);
INSERT INTO `system_role_menu` VALUES (15, 2504, -1);
INSERT INTO `system_role_menu` VALUES (15, 2505, -1);
INSERT INTO `system_role_menu` VALUES (15, 2506, -1);
INSERT INTO `system_role_menu` VALUES (15, 2507, -1);
INSERT INTO `system_role_menu` VALUES (15, 2508, -1);
INSERT INTO `system_role_menu` VALUES (15, 2510, -1);
INSERT INTO `system_role_menu` VALUES (15, 2511, -1);
INSERT INTO `system_role_menu` VALUES (15, 2512, -1);
INSERT INTO `system_role_menu` VALUES (15, 2513, -1);
INSERT INTO `system_role_menu` VALUES (15, 2514, -1);
INSERT INTO `system_role_menu` VALUES (15, 2515, -1);
INSERT INTO `system_role_menu` VALUES (15, 2516, -1);
INSERT INTO `system_role_menu` VALUES (15, 2517, -1);
INSERT INTO `system_role_menu` VALUES (15, 2518, -1);
INSERT INTO `system_role_menu` VALUES (15, 2519, -1);
INSERT INTO `system_role_menu` VALUES (15, 2520, -1);
INSERT INTO `system_role_menu` VALUES (15, 2521, -1);
INSERT INTO `system_role_menu` VALUES (15, 2522, -1);
INSERT INTO `system_role_menu` VALUES (15, 2523, -1);
INSERT INTO `system_role_menu` VALUES (15, 2524, -1);
INSERT INTO `system_role_menu` VALUES (15, 2525, -1);
INSERT INTO `system_role_menu` VALUES (15, 2526, -1);
INSERT INTO `system_role_menu` VALUES (15, 2527, -1);
INSERT INTO `system_role_menu` VALUES (15, 2528, -1);
INSERT INTO `system_role_menu` VALUES (15, 2529, -1);
INSERT INTO `system_role_menu` VALUES (15, 2537, -1);
INSERT INTO `system_role_menu` VALUES (15, 2538, -1);
INSERT INTO `system_role_menu` VALUES (15, 2539, -1);
INSERT INTO `system_role_menu` VALUES (15, 2540, -1);
INSERT INTO `system_role_menu` VALUES (15, 2541, -1);
INSERT INTO `system_role_menu` VALUES (15, 2542, -1);
INSERT INTO `system_role_menu` VALUES (15, 2543, -1);
INSERT INTO `system_role_menu` VALUES (15, 2544, -1);
INSERT INTO `system_role_menu` VALUES (15, 2545, -1);
INSERT INTO `system_role_menu` VALUES (15, 2546, -1);
INSERT INTO `system_role_menu` VALUES (15, 2547, -1);
INSERT INTO `system_role_menu` VALUES (15, 2548, -1);
INSERT INTO `system_role_menu` VALUES (15, 2549, -1);
INSERT INTO `system_role_menu` VALUES (15, 2550, -1);
INSERT INTO `system_role_menu` VALUES (15, 2551, -1);
INSERT INTO `system_role_menu` VALUES (15, 2552, -1);
INSERT INTO `system_role_menu` VALUES (43, 2397, -1);
INSERT INTO `system_role_menu` VALUES (43, 2426, -1);
INSERT INTO `system_role_menu` VALUES (43, 2464, -1);
INSERT INTO `system_role_menu` VALUES (43, 2465, -1);
INSERT INTO `system_role_menu` VALUES (43, 2466, -1);
INSERT INTO `system_role_menu` VALUES (43, 2467, -1);
INSERT INTO `system_role_menu` VALUES (43, 2468, -1);
INSERT INTO `system_role_menu` VALUES (43, 2469, -1);
INSERT INTO `system_role_menu` VALUES (43, 2470, -1);
INSERT INTO `system_role_menu` VALUES (43, 2471, -1);
INSERT INTO `system_role_menu` VALUES (43, 2472, -1);
INSERT INTO `system_role_menu` VALUES (43, 2473, -1);
INSERT INTO `system_role_menu` VALUES (43, 2474, -1);
INSERT INTO `system_role_menu` VALUES (43, 2475, -1);
INSERT INTO `system_role_menu` VALUES (43, 2476, -1);
INSERT INTO `system_role_menu` VALUES (43, 2477, -1);
INSERT INTO `system_role_menu` VALUES (43, 2508, -1);
INSERT INTO `system_role_menu` VALUES (43, 2510, -1);
INSERT INTO `system_role_menu` VALUES (43, 2511, -1);
INSERT INTO `system_role_menu` VALUES (43, 2512, -1);
INSERT INTO `system_role_menu` VALUES (43, 2513, -1);
INSERT INTO `system_role_menu` VALUES (43, 2514, -1);
INSERT INTO `system_role_menu` VALUES (43, 2515, -1);
INSERT INTO `system_role_menu` VALUES (43, 2516, -1);
INSERT INTO `system_role_menu` VALUES (43, 2517, -1);
INSERT INTO `system_role_menu` VALUES (43, 2518, -1);
INSERT INTO `system_role_menu` VALUES (43, 2519, -1);
INSERT INTO `system_role_menu` VALUES (43, 2520, -1);
INSERT INTO `system_role_menu` VALUES (43, 2521, -1);
INSERT INTO `system_role_menu` VALUES (43, 2522, -1);
INSERT INTO `system_role_menu` VALUES (43, 2523, -1);
INSERT INTO `system_role_menu` VALUES (43, 2524, -1);
INSERT INTO `system_role_menu` VALUES (43, 2525, -1);
INSERT INTO `system_role_menu` VALUES (43, 2526, -1);
INSERT INTO `system_role_menu` VALUES (43, 2527, -1);
INSERT INTO `system_role_menu` VALUES (43, 2528, -1);
INSERT INTO `system_role_menu` VALUES (43, 2529, -1);
INSERT INTO `system_role_menu` VALUES (43, 2537, -1);
INSERT INTO `system_role_menu` VALUES (43, 2538, -1);
INSERT INTO `system_role_menu` VALUES (43, 2539, -1);
INSERT INTO `system_role_menu` VALUES (43, 2540, -1);
INSERT INTO `system_role_menu` VALUES (43, 2541, -1);
INSERT INTO `system_role_menu` VALUES (43, 2542, -1);
INSERT INTO `system_role_menu` VALUES (43, 2543, -1);
INSERT INTO `system_role_menu` VALUES (43, 2545, -1);
INSERT INTO `system_role_menu` VALUES (43, 2546, -1);
INSERT INTO `system_role_menu` VALUES (43, 2547, -1);
INSERT INTO `system_role_menu` VALUES (43, 2548, -1);
INSERT INTO `system_role_menu` VALUES (43, 2549, -1);
INSERT INTO `system_role_menu` VALUES (43, 2550, -1);
INSERT INTO `system_role_menu` VALUES (43, 2551, -1);
INSERT INTO `system_role_menu` VALUES (44, 2397, -1);
INSERT INTO `system_role_menu` VALUES (44, 2426, -1);
INSERT INTO `system_role_menu` VALUES (44, 2464, -1);
INSERT INTO `system_role_menu` VALUES (44, 2465, -1);
INSERT INTO `system_role_menu` VALUES (44, 2466, -1);
INSERT INTO `system_role_menu` VALUES (44, 2467, -1);
INSERT INTO `system_role_menu` VALUES (44, 2468, -1);
INSERT INTO `system_role_menu` VALUES (44, 2469, -1);
INSERT INTO `system_role_menu` VALUES (44, 2470, -1);
INSERT INTO `system_role_menu` VALUES (44, 2471, -1);
INSERT INTO `system_role_menu` VALUES (44, 2472, -1);
INSERT INTO `system_role_menu` VALUES (44, 2473, -1);
INSERT INTO `system_role_menu` VALUES (44, 2474, -1);
INSERT INTO `system_role_menu` VALUES (44, 2475, -1);
INSERT INTO `system_role_menu` VALUES (44, 2476, -1);
INSERT INTO `system_role_menu` VALUES (44, 2477, -1);
INSERT INTO `system_role_menu` VALUES (44, 2508, -1);
INSERT INTO `system_role_menu` VALUES (44, 2510, -1);
INSERT INTO `system_role_menu` VALUES (44, 2511, -1);
INSERT INTO `system_role_menu` VALUES (44, 2512, -1);
INSERT INTO `system_role_menu` VALUES (44, 2514, -1);
INSERT INTO `system_role_menu` VALUES (44, 2515, -1);
INSERT INTO `system_role_menu` VALUES (44, 2516, -1);
INSERT INTO `system_role_menu` VALUES (44, 2517, -1);
INSERT INTO `system_role_menu` VALUES (44, 2518, -1);
INSERT INTO `system_role_menu` VALUES (44, 2519, -1);
INSERT INTO `system_role_menu` VALUES (44, 2520, -1);
INSERT INTO `system_role_menu` VALUES (44, 2521, -1);
INSERT INTO `system_role_menu` VALUES (44, 2522, -1);
INSERT INTO `system_role_menu` VALUES (44, 2523, -1);
INSERT INTO `system_role_menu` VALUES (44, 2545, -1);
INSERT INTO `system_role_menu` VALUES (44, 2546, -1);
INSERT INTO `system_role_menu` VALUES (44, 2547, -1);
INSERT INTO `system_role_menu` VALUES (44, 2548, -1);
INSERT INTO `system_role_menu` VALUES (44, 2549, -1);
INSERT INTO `system_role_menu` VALUES (44, 2550, -1);
INSERT INTO `system_role_menu` VALUES (44, 2551, -1);
INSERT INTO `system_role_menu` VALUES (45, 2397, -1);
INSERT INTO `system_role_menu` VALUES (45, 2426, -1);
INSERT INTO `system_role_menu` VALUES (45, 2464, -1);
INSERT INTO `system_role_menu` VALUES (45, 2465, -1);
INSERT INTO `system_role_menu` VALUES (45, 2466, -1);
INSERT INTO `system_role_menu` VALUES (45, 2467, -1);
INSERT INTO `system_role_menu` VALUES (45, 2468, -1);
INSERT INTO `system_role_menu` VALUES (45, 2469, -1);
INSERT INTO `system_role_menu` VALUES (45, 2470, -1);
INSERT INTO `system_role_menu` VALUES (45, 2471, -1);
INSERT INTO `system_role_menu` VALUES (45, 2472, -1);
INSERT INTO `system_role_menu` VALUES (45, 2473, -1);
INSERT INTO `system_role_menu` VALUES (45, 2474, -1);
INSERT INTO `system_role_menu` VALUES (45, 2475, -1);
INSERT INTO `system_role_menu` VALUES (45, 2476, -1);
INSERT INTO `system_role_menu` VALUES (45, 2477, -1);
INSERT INTO `system_role_menu` VALUES (45, 2508, -1);
INSERT INTO `system_role_menu` VALUES (45, 2510, -1);
INSERT INTO `system_role_menu` VALUES (45, 2511, -1);
INSERT INTO `system_role_menu` VALUES (45, 2512, -1);
INSERT INTO `system_role_menu` VALUES (45, 2514, -1);
INSERT INTO `system_role_menu` VALUES (45, 2515, -1);
INSERT INTO `system_role_menu` VALUES (45, 2516, -1);
INSERT INTO `system_role_menu` VALUES (45, 2517, -1);
INSERT INTO `system_role_menu` VALUES (45, 2518, -1);
INSERT INTO `system_role_menu` VALUES (45, 2519, -1);
INSERT INTO `system_role_menu` VALUES (45, 2520, -1);
INSERT INTO `system_role_menu` VALUES (45, 2521, -1);
INSERT INTO `system_role_menu` VALUES (45, 2522, -1);
INSERT INTO `system_role_menu` VALUES (45, 2523, -1);
INSERT INTO `system_role_menu` VALUES (45, 2545, -1);
INSERT INTO `system_role_menu` VALUES (45, 2546, -1);
INSERT INTO `system_role_menu` VALUES (45, 2547, -1);
INSERT INTO `system_role_menu` VALUES (45, 2548, -1);
INSERT INTO `system_role_menu` VALUES (45, 2549, -1);
INSERT INTO `system_role_menu` VALUES (45, 2550, -1);
INSERT INTO `system_role_menu` VALUES (45, 2551, -1);
INSERT INTO `system_role_menu` VALUES (185, 2397, 84);
INSERT INTO `system_role_menu` VALUES (185, 2398, 84);
INSERT INTO `system_role_menu` VALUES (185, 2399, 84);
INSERT INTO `system_role_menu` VALUES (185, 2400, 84);
INSERT INTO `system_role_menu` VALUES (185, 2401, 84);
INSERT INTO `system_role_menu` VALUES (185, 2402, 84);
INSERT INTO `system_role_menu` VALUES (185, 2403, 84);
INSERT INTO `system_role_menu` VALUES (185, 2404, 84);
INSERT INTO `system_role_menu` VALUES (185, 2426, 84);
INSERT INTO `system_role_menu` VALUES (185, 2444, 84);
INSERT INTO `system_role_menu` VALUES (185, 2445, 84);
INSERT INTO `system_role_menu` VALUES (185, 2446, 84);
INSERT INTO `system_role_menu` VALUES (185, 2447, 84);
INSERT INTO `system_role_menu` VALUES (185, 2448, 84);
INSERT INTO `system_role_menu` VALUES (185, 2449, 84);
INSERT INTO `system_role_menu` VALUES (185, 2464, 84);
INSERT INTO `system_role_menu` VALUES (185, 2465, 84);
INSERT INTO `system_role_menu` VALUES (185, 2466, 84);
INSERT INTO `system_role_menu` VALUES (185, 2467, 84);
INSERT INTO `system_role_menu` VALUES (185, 2468, 84);
INSERT INTO `system_role_menu` VALUES (185, 2469, 84);
INSERT INTO `system_role_menu` VALUES (185, 2470, 84);
INSERT INTO `system_role_menu` VALUES (185, 2471, 84);
INSERT INTO `system_role_menu` VALUES (185, 2472, 84);
INSERT INTO `system_role_menu` VALUES (185, 2473, 84);
INSERT INTO `system_role_menu` VALUES (185, 2474, 84);
INSERT INTO `system_role_menu` VALUES (185, 2475, 84);
INSERT INTO `system_role_menu` VALUES (185, 2476, 84);
INSERT INTO `system_role_menu` VALUES (185, 2477, 84);
INSERT INTO `system_role_menu` VALUES (185, 2480, 84);
INSERT INTO `system_role_menu` VALUES (185, 2481, 84);
INSERT INTO `system_role_menu` VALUES (185, 2482, 84);
INSERT INTO `system_role_menu` VALUES (185, 2483, 84);
INSERT INTO `system_role_menu` VALUES (185, 2484, 84);
INSERT INTO `system_role_menu` VALUES (185, 2485, 84);
INSERT INTO `system_role_menu` VALUES (185, 2486, 84);
INSERT INTO `system_role_menu` VALUES (185, 2487, 84);
INSERT INTO `system_role_menu` VALUES (185, 2488, 84);
INSERT INTO `system_role_menu` VALUES (185, 2489, 84);
INSERT INTO `system_role_menu` VALUES (185, 2490, 84);
INSERT INTO `system_role_menu` VALUES (185, 2491, 84);
INSERT INTO `system_role_menu` VALUES (185, 2492, 84);
INSERT INTO `system_role_menu` VALUES (185, 2493, 84);
INSERT INTO `system_role_menu` VALUES (185, 2501, 84);
INSERT INTO `system_role_menu` VALUES (185, 2502, 84);
INSERT INTO `system_role_menu` VALUES (185, 2503, 84);
INSERT INTO `system_role_menu` VALUES (185, 2504, 84);
INSERT INTO `system_role_menu` VALUES (185, 2505, 84);
INSERT INTO `system_role_menu` VALUES (185, 2506, 84);
INSERT INTO `system_role_menu` VALUES (185, 2507, 84);
INSERT INTO `system_role_menu` VALUES (185, 2508, 84);
INSERT INTO `system_role_menu` VALUES (185, 2510, 84);
INSERT INTO `system_role_menu` VALUES (185, 2511, 84);
INSERT INTO `system_role_menu` VALUES (185, 2512, 84);
INSERT INTO `system_role_menu` VALUES (185, 2513, 84);
INSERT INTO `system_role_menu` VALUES (185, 2514, 84);
INSERT INTO `system_role_menu` VALUES (185, 2515, 84);
INSERT INTO `system_role_menu` VALUES (185, 2516, 84);
INSERT INTO `system_role_menu` VALUES (185, 2517, 84);
INSERT INTO `system_role_menu` VALUES (185, 2518, 84);
INSERT INTO `system_role_menu` VALUES (185, 2519, 84);
INSERT INTO `system_role_menu` VALUES (185, 2520, 84);
INSERT INTO `system_role_menu` VALUES (185, 2521, 84);
INSERT INTO `system_role_menu` VALUES (185, 2522, 84);
INSERT INTO `system_role_menu` VALUES (185, 2523, 84);
INSERT INTO `system_role_menu` VALUES (185, 2524, 84);
INSERT INTO `system_role_menu` VALUES (185, 2525, 84);
INSERT INTO `system_role_menu` VALUES (185, 2526, 84);
INSERT INTO `system_role_menu` VALUES (185, 2527, 84);
INSERT INTO `system_role_menu` VALUES (185, 2528, 84);
INSERT INTO `system_role_menu` VALUES (185, 2529, 84);
INSERT INTO `system_role_menu` VALUES (185, 2537, 84);
INSERT INTO `system_role_menu` VALUES (185, 2538, 84);
INSERT INTO `system_role_menu` VALUES (185, 2539, 84);
INSERT INTO `system_role_menu` VALUES (185, 2540, 84);
INSERT INTO `system_role_menu` VALUES (185, 2541, 84);
INSERT INTO `system_role_menu` VALUES (185, 2542, 84);
INSERT INTO `system_role_menu` VALUES (185, 2543, 84);
INSERT INTO `system_role_menu` VALUES (185, 2544, 84);
INSERT INTO `system_role_menu` VALUES (185, 2545, 84);
INSERT INTO `system_role_menu` VALUES (185, 2546, 84);
INSERT INTO `system_role_menu` VALUES (185, 2547, 84);
INSERT INTO `system_role_menu` VALUES (185, 2548, 84);
INSERT INTO `system_role_menu` VALUES (185, 2549, 84);
INSERT INTO `system_role_menu` VALUES (185, 2550, 84);
INSERT INTO `system_role_menu` VALUES (185, 2551, 84);
INSERT INTO `system_role_menu` VALUES (185, 2552, 84);
INSERT INTO `system_role_menu` VALUES (186, 2397, 84);
INSERT INTO `system_role_menu` VALUES (186, 2426, 84);
INSERT INTO `system_role_menu` VALUES (186, 2464, 84);
INSERT INTO `system_role_menu` VALUES (186, 2465, 84);
INSERT INTO `system_role_menu` VALUES (186, 2466, 84);
INSERT INTO `system_role_menu` VALUES (186, 2467, 84);
INSERT INTO `system_role_menu` VALUES (186, 2468, 84);
INSERT INTO `system_role_menu` VALUES (186, 2469, 84);
INSERT INTO `system_role_menu` VALUES (186, 2470, 84);
INSERT INTO `system_role_menu` VALUES (186, 2471, 84);
INSERT INTO `system_role_menu` VALUES (186, 2472, 84);
INSERT INTO `system_role_menu` VALUES (186, 2473, 84);
INSERT INTO `system_role_menu` VALUES (186, 2474, 84);
INSERT INTO `system_role_menu` VALUES (186, 2475, 84);
INSERT INTO `system_role_menu` VALUES (186, 2476, 84);
INSERT INTO `system_role_menu` VALUES (186, 2477, 84);
INSERT INTO `system_role_menu` VALUES (186, 2510, 84);
INSERT INTO `system_role_menu` VALUES (186, 2511, 84);
INSERT INTO `system_role_menu` VALUES (186, 2513, 84);
INSERT INTO `system_role_menu` VALUES (186, 2519, 84);
INSERT INTO `system_role_menu` VALUES (186, 2520, 84);
INSERT INTO `system_role_menu` VALUES (186, 2521, 84);
INSERT INTO `system_role_menu` VALUES (186, 2522, 84);
INSERT INTO `system_role_menu` VALUES (186, 2523, 84);
INSERT INTO `system_role_menu` VALUES (186, 2524, 84);
INSERT INTO `system_role_menu` VALUES (186, 2525, 84);
INSERT INTO `system_role_menu` VALUES (186, 2526, 84);
INSERT INTO `system_role_menu` VALUES (186, 2527, 84);
INSERT INTO `system_role_menu` VALUES (186, 2528, 84);
INSERT INTO `system_role_menu` VALUES (186, 2529, 84);
INSERT INTO `system_role_menu` VALUES (186, 2537, 84);
INSERT INTO `system_role_menu` VALUES (186, 2538, 84);
INSERT INTO `system_role_menu` VALUES (186, 2539, 84);
INSERT INTO `system_role_menu` VALUES (186, 2540, 84);
INSERT INTO `system_role_menu` VALUES (186, 2541, 84);
INSERT INTO `system_role_menu` VALUES (186, 2542, 84);
INSERT INTO `system_role_menu` VALUES (186, 2543, 84);
INSERT INTO `system_role_menu` VALUES (186, 2545, 84);
INSERT INTO `system_role_menu` VALUES (186, 2546, 84);
INSERT INTO `system_role_menu` VALUES (186, 2547, 84);
INSERT INTO `system_role_menu` VALUES (186, 2548, 84);
INSERT INTO `system_role_menu` VALUES (186, 2549, 84);
INSERT INTO `system_role_menu` VALUES (187, 2397, 84);
INSERT INTO `system_role_menu` VALUES (187, 2426, 84);
INSERT INTO `system_role_menu` VALUES (187, 2464, 84);
INSERT INTO `system_role_menu` VALUES (187, 2465, 84);
INSERT INTO `system_role_menu` VALUES (187, 2466, 84);
INSERT INTO `system_role_menu` VALUES (187, 2467, 84);
INSERT INTO `system_role_menu` VALUES (187, 2468, 84);
INSERT INTO `system_role_menu` VALUES (187, 2469, 84);
INSERT INTO `system_role_menu` VALUES (187, 2470, 84);
INSERT INTO `system_role_menu` VALUES (187, 2471, 84);
INSERT INTO `system_role_menu` VALUES (187, 2472, 84);
INSERT INTO `system_role_menu` VALUES (187, 2473, 84);
INSERT INTO `system_role_menu` VALUES (187, 2474, 84);
INSERT INTO `system_role_menu` VALUES (187, 2475, 84);
INSERT INTO `system_role_menu` VALUES (187, 2476, 84);
INSERT INTO `system_role_menu` VALUES (187, 2477, 84);
INSERT INTO `system_role_menu` VALUES (187, 2510, 84);
INSERT INTO `system_role_menu` VALUES (187, 2511, 84);
INSERT INTO `system_role_menu` VALUES (187, 2519, 84);
INSERT INTO `system_role_menu` VALUES (187, 2520, 84);
INSERT INTO `system_role_menu` VALUES (187, 2521, 84);
INSERT INTO `system_role_menu` VALUES (187, 2522, 84);
INSERT INTO `system_role_menu` VALUES (187, 2523, 84);
INSERT INTO `system_role_menu` VALUES (187, 2545, 84);
INSERT INTO `system_role_menu` VALUES (187, 2546, 84);
INSERT INTO `system_role_menu` VALUES (187, 2547, 84);
INSERT INTO `system_role_menu` VALUES (187, 2548, 84);
INSERT INTO `system_role_menu` VALUES (187, 2549, 84);
INSERT INTO `system_role_menu` VALUES (188, 2397, 84);
INSERT INTO `system_role_menu` VALUES (188, 2426, 84);
INSERT INTO `system_role_menu` VALUES (188, 2464, 84);
INSERT INTO `system_role_menu` VALUES (188, 2465, 84);
INSERT INTO `system_role_menu` VALUES (188, 2466, 84);
INSERT INTO `system_role_menu` VALUES (188, 2467, 84);
INSERT INTO `system_role_menu` VALUES (188, 2468, 84);
INSERT INTO `system_role_menu` VALUES (188, 2469, 84);
INSERT INTO `system_role_menu` VALUES (188, 2470, 84);
INSERT INTO `system_role_menu` VALUES (188, 2471, 84);
INSERT INTO `system_role_menu` VALUES (188, 2472, 84);
INSERT INTO `system_role_menu` VALUES (188, 2473, 84);
INSERT INTO `system_role_menu` VALUES (188, 2474, 84);
INSERT INTO `system_role_menu` VALUES (188, 2475, 84);
INSERT INTO `system_role_menu` VALUES (188, 2476, 84);
INSERT INTO `system_role_menu` VALUES (188, 2477, 84);
INSERT INTO `system_role_menu` VALUES (188, 2510, 84);
INSERT INTO `system_role_menu` VALUES (188, 2511, 84);
INSERT INTO `system_role_menu` VALUES (188, 2519, 84);
INSERT INTO `system_role_menu` VALUES (188, 2520, 84);
INSERT INTO `system_role_menu` VALUES (188, 2521, 84);
INSERT INTO `system_role_menu` VALUES (188, 2522, 84);
INSERT INTO `system_role_menu` VALUES (188, 2523, 84);
INSERT INTO `system_role_menu` VALUES (188, 2545, 84);
INSERT INTO `system_role_menu` VALUES (188, 2546, 84);
INSERT INTO `system_role_menu` VALUES (188, 2547, 84);
INSERT INTO `system_role_menu` VALUES (188, 2548, 84);
INSERT INTO `system_role_menu` VALUES (188, 2549, 84);
INSERT INTO `system_role_menu` VALUES (189, 2397, 84);
INSERT INTO `system_role_menu` VALUES (189, 2470, 84);
INSERT INTO `system_role_menu` VALUES (189, 2471, 84);
INSERT INTO `system_role_menu` VALUES (189, 2472, 84);
INSERT INTO `system_role_menu` VALUES (189, 2473, 84);
INSERT INTO `system_role_menu` VALUES (189, 2474, 84);
INSERT INTO `system_role_menu` VALUES (189, 2475, 84);
INSERT INTO `system_role_menu` VALUES (189, 2476, 84);
INSERT INTO `system_role_menu` VALUES (189, 2477, 84);

-- ----------------------------
-- Table structure for system_user
-- ----------------------------
DROP TABLE IF EXISTS `system_user`;
CREATE TABLE `system_user`  (
  `user_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `dept_id` bigint(20) NULL DEFAULT NULL COMMENT '部门ID',
  `user_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户账号',
  `nick_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户昵称',
  `user_type` varchar(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '00' COMMENT '用户类型（00系统用户）',
  `email` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '用户邮箱',
  `phonenumber` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '手机号码',
  `sex` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '用户性别（0男 1女 2未知）',
  `avatar` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '头像地址',
  `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '密码',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '帐号状态（0正常 1停用）',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
  `login_ip` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '最后登录IP',
  `login_date` datetime NULL DEFAULT NULL COMMENT '最后登录时间',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `auth_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '认证平台id',
  PRIMARY KEY (`user_id`) USING BTREE,
  UNIQUE INDEX `USER_ID_90026803063700`(`user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 741 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of system_user
-- ----------------------------
INSERT INTO `system_user` VALUES (1, 103, 'admin', '管理员', '00', '', '19951942681', '1', '', '$2a$10$LOgcmTijb/krLwReokOD5.24moLGwspqbfBzXMKykmJse/ETUHQwS', '0', '0', '125.42.97.139', '2025-05-29 07:15:08', 'qData', '2024-05-06 06:12:17', '', '2025-05-29 07:15:08', '管理员', NULL);
INSERT INTO `system_user` VALUES (2, 103, 'qData', '小桐', '00', 'support@qiantong.tech', '19951942682', '1', '', '$2a$10$3dzj0nGSmlNjPw4iuZNHse5.lxrNzumTCQ8aEaV7nNEtn2pXmmimW', '0', '0', '140.207.49.186', '2025-05-29 08:05:46', 'qData', '2025-02-13 14:37:54', 'admin', '2025-05-29 08:05:45', NULL, NULL);
INSERT INTO `system_user` VALUES (732, 854, 'yanshi', '演示账号', '00', '', '18878909876', '2', '', '$2a$10$8kofmE.RuwIB0ddqO7j9luspbU/GYG84CWMoshTmgd5Re/drPHD9S', '0', '0', '172.23.0.1', '2025-05-09 17:18:41', 'qData', '2025-02-05 17:12:47', 'qData', '2025-05-09 17:18:40', NULL, NULL);
INSERT INTO `system_user` VALUES (738, 101, 'yanfaceshi', '研发模块测试人员', '00', '', '18225806566', '0', '', '$2a$10$AXwgtxDwPO0f1/Gi.G3Gr.uO7LUtSVLcz9rAYFJv3lsyvkCCkxyca', '1', '0', '127.0.0.1', '2025-02-20 18:06:23', 'liuhaosheng', '2025-02-13 14:37:54', 'qData', '2025-02-27 10:19:52', NULL, NULL);
INSERT INTO `system_user` VALUES (740, 100, 'rmt1', 'rmt1', '00', '', '', '0', '', '$2a$10$TtKdji.U5LRrnt1bxg2vTuv6t0du4gU4GBkgfK1ZJKtFtZOgbqXAe', '0', '2', '127.0.0.1', '2025-05-24 18:50:19', 'qData', '2025-05-24 18:50:09', '', '2025-05-24 18:50:18', NULL, NULL);

-- ----------------------------
-- Table structure for system_user_post
-- ----------------------------
DROP TABLE IF EXISTS `system_user_post`;
CREATE TABLE `system_user_post`  (
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `post_id` bigint(20) NOT NULL COMMENT '岗位ID',
  PRIMARY KEY (`user_id`, `post_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户与岗位关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of system_user_post
-- ----------------------------
INSERT INTO `system_user_post` VALUES (1, 1);
INSERT INTO `system_user_post` VALUES (732, 2);
INSERT INTO `system_user_post` VALUES (738, 4);

-- ----------------------------
-- Table structure for system_user_role
-- ----------------------------
DROP TABLE IF EXISTS `system_user_role`;
CREATE TABLE `system_user_role`  (
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `role_id` bigint(20) NOT NULL COMMENT '角色ID',
  PRIMARY KEY (`user_id`, `role_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户和角色关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of system_user_role
-- ----------------------------
INSERT INTO `system_user_role` VALUES (1, 1);
INSERT INTO `system_user_role` VALUES (1, 15);
INSERT INTO `system_user_role` VALUES (1, 185);
INSERT INTO `system_user_role` VALUES (1, 186);
INSERT INTO `system_user_role` VALUES (1, 187);
INSERT INTO `system_user_role` VALUES (1, 188);
INSERT INTO `system_user_role` VALUES (1, 189);
INSERT INTO `system_user_role` VALUES (2, 3);
INSERT INTO `system_user_role` VALUES (2, 186);
INSERT INTO `system_user_role` VALUES (2, 187);
INSERT INTO `system_user_role` VALUES (2, 188);
INSERT INTO `system_user_role` VALUES (2, 189);
INSERT INTO `system_user_role` VALUES (732, 4);
INSERT INTO `system_user_role` VALUES (732, 130);
INSERT INTO `system_user_role` VALUES (732, 159);
INSERT INTO `system_user_role` VALUES (732, 168);
INSERT INTO `system_user_role` VALUES (732, 175);
INSERT INTO `system_user_role` VALUES (738, 12);

SET FOREIGN_KEY_CHECKS = 1;
