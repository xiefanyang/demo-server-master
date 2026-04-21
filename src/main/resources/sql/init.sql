/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 80028
 Source Host           : localhost:3306
 Source Schema         : demo

 Target Server Type    : MySQL
 Target Server Version : 80028
 File Encoding         : 65001

 Date: 13/10/2023 16:07:35
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_bis_xy_branch
-- ----------------------------
DROP TABLE IF EXISTS `t_bis_xy_branch`;
CREATE TABLE `t_bis_xy_branch` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `create_time` bigint DEFAULT NULL,
  `is_deleted` bit(1) NOT NULL,
  `update_time` bigint DEFAULT NULL,
  `version` int NOT NULL,
  `record_id` varchar(64) COLLATE utf8mb4_general_ci NOT NULL,
  `enable` bit(1) NOT NULL DEFAULT b'1' COMMENT '状态 1正常 0冻结',
  `name` varchar(100) COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '名称',
  `parent_id` bigint NOT NULL DEFAULT '1' COMMENT '父级id，总会为0',
  `sort` int NOT NULL DEFAULT '1' COMMENT '排序',
  `logo` varchar(64) COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '会标',
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_9ss7487hjnyogfgithu6m6qj7` (`record_id`),
  UNIQUE KEY `UK_hqi3nsva11efwne0ho6s0guh3` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ----------------------------
-- Records of t_bis_xy_branch
-- ----------------------------
BEGIN;
INSERT INTO `t_bis_xy_branch` (`id`, `create_time`, `is_deleted`, `update_time`, `version`, `record_id`, `enable`, `name`, `parent_id`, `sort`, `logo`) VALUES (1, 1, b'0', 1697161615864, 8, '9999', b'1', '校友总会', 0, 1, '6452178278bb45c7986da1c34483d7a8');
INSERT INTO `t_bis_xy_branch` (`id`, `create_time`, `is_deleted`, `update_time`, `version`, `record_id`, `enable`, `name`, `parent_id`, `sort`, `logo`) VALUES (2, 1, b'0', 1697082399446, 4, '1111', b'1', '测试分会1', 1, 1, '');
INSERT INTO `t_bis_xy_branch` (`id`, `create_time`, `is_deleted`, `update_time`, `version`, `record_id`, `enable`, `name`, `parent_id`, `sort`, `logo`) VALUES (3, 1, b'0', NULL, 0, '2222', b'1', '测试分会2', 1, 1, '');
COMMIT;

-- ----------------------------
-- Table structure for t_bis_xy_branch_member
-- ----------------------------
DROP TABLE IF EXISTS `t_bis_xy_branch_member`;
CREATE TABLE `t_bis_xy_branch_member` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `create_time` bigint DEFAULT NULL,
  `is_deleted` bit(1) NOT NULL,
  `update_time` bigint DEFAULT NULL,
  `version` int NOT NULL,
  `record_id` varchar(64) COLLATE utf8mb4_general_ci NOT NULL,
  `branch_id` varchar(64) COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '分会id',
  `enable` bit(1) NOT NULL DEFAULT b'1' COMMENT '状态 1正常 0冻结',
  `leader` smallint NOT NULL DEFAULT '0' COMMENT '成员身份 0成员 1分会管理员',
  `user_id` bigint NOT NULL COMMENT '成员id',
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_n3w7ycjr23ghvwid7hqbr8jvh` (`record_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ----------------------------
-- Records of t_bis_xy_branch_member
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for t_sys_data_purview_define
-- ----------------------------
DROP TABLE IF EXISTS `t_sys_data_purview_define`;
CREATE TABLE `t_sys_data_purview_define` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `create_time` bigint NOT NULL,
  `is_deleted` bit(1) NOT NULL,
  `update_time` bigint NOT NULL,
  `version` int NOT NULL,
  `content` text COLLATE utf8mb4_general_ci NOT NULL COMMENT '权限范围SQL',
  `enable` bit(1) NOT NULL DEFAULT b'1' COMMENT '是否有效 1有效 0无效',
  `name` varchar(50) COLLATE utf8mb4_general_ci NOT NULL COMMENT '权限范围名称',
  `type` tinyint NOT NULL DEFAULT '0' COMMENT '范围类型 0 默认  1自定义',
  `valid` text COLLATE utf8mb4_general_ci NOT NULL COMMENT '权限范围限定SQL',
  `value_type` varchar(30) COLLATE utf8mb4_general_ci NOT NULL COMMENT 'value字段类型：Long String Integer',
  `filter` text COLLATE utf8mb4_general_ci NOT NULL COMMENT '权限过滤限定SQL',
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_60dd0gxsd3yqo000u7cuo2x5r` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ----------------------------
-- Records of t_sys_data_purview_define
-- ----------------------------
BEGIN;
INSERT INTO `t_sys_data_purview_define` (`id`, `create_time`, `is_deleted`, `update_time`, `version`, `content`, `enable`, `name`, `type`, `valid`, `value_type`, `filter`) VALUES (1, 1696858495877, b'1', 1696859032667, 2, '1', b'1', '--', 0, '', '', '');
INSERT INTO `t_sys_data_purview_define` (`id`, `create_time`, `is_deleted`, `update_time`, `version`, `content`, `enable`, `name`, `type`, `valid`, `value_type`, `filter`) VALUES (2, 1696858601594, b'0', -1, 0, '1', b'1', '随功能默认不区分', 0, '', '', '');
INSERT INTO `t_sys_data_purview_define` (`id`, `create_time`, `is_deleted`, `update_time`, `version`, `content`, `enable`, `name`, `type`, `valid`, `value_type`, `filter`) VALUES (3, 1696859046215, b'0', 1696991962263, 4, 'select name as label, CAST(id as CHAR) as value from t_sys_role where enable = 1 and is_deleted  = 0', b'1', '角色', 1, '', 'Long', '');
INSERT INTO `t_sys_data_purview_define` (`id`, `create_time`, `is_deleted`, `update_time`, `version`, `content`, `enable`, `name`, `type`, `valid`, `value_type`, `filter`) VALUES (6, 1696917230871, b'0', 1697007798322, 4, 'select record_id as value,name as label from t_bis_xy_branch where enable = 1 and is_deleted = 0', b'1', '校友分会', 1, '', 'String', '');
COMMIT;

-- ----------------------------
-- Table structure for t_sys_file
-- ----------------------------
DROP TABLE IF EXISTS `t_sys_file`;
CREATE TABLE `t_sys_file` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `create_time` bigint DEFAULT NULL,
  `is_deleted` bit(1) NOT NULL,
  `update_time` bigint DEFAULT NULL,
  `version` int NOT NULL,
  `record_id` varchar(64) COLLATE utf8mb4_general_ci NOT NULL,
  `category` varchar(128) COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '文件分类',
  `clear_time` bigint DEFAULT NULL COMMENT '彻底删除时间戳',
  `clear_user` bigint DEFAULT NULL COMMENT '彻底删除人',
  `create_user` bigint NOT NULL COMMENT '上传人',
  `delete_time` bigint DEFAULT NULL COMMENT '删除人',
  `delete_user` bigint DEFAULT NULL COMMENT '删除人',
  `enable` bit(1) NOT NULL DEFAULT b'1' COMMENT '使用状态 1正常 2移除(回收站，没有清理文件)',
  `file_time` bigint DEFAULT NULL COMMENT '音频视频文件时长',
  `original_name` varchar(128) COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '原始文件名',
  `public_read` bit(1) DEFAULT b'0' COMMENT '是否为公共读 1是0否',
  `size` bigint NOT NULL COMMENT '文件大小，存储默认单位为byte',
  `suffix` varchar(10) COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '文件结尾',
  `type` varchar(128) COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT 'MIME文件类型',
  `type_name` varchar(64) COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '文件类型名称',
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_jkp03yjibiyraglpb2lodmj68` (`record_id`)
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ----------------------------
-- Table structure for t_sys_file_data_process_record
-- ----------------------------
DROP TABLE IF EXISTS `t_sys_file_data_process_record`;
CREATE TABLE `t_sys_file_data_process_record` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `create_time` bigint DEFAULT NULL,
  `is_deleted` bit(1) NOT NULL,
  `update_time` bigint DEFAULT NULL,
  `version` int NOT NULL,
  `record_id` varchar(64) COLLATE utf8mb4_general_ci NOT NULL,
  `bis` varchar(50) COLLATE utf8mb4_general_ci NOT NULL COMMENT '上传业务名称，如：导入课程信息',
  `count_num` int DEFAULT NULL COMMENT '总数据',
  `creator` bigint NOT NULL COMMENT '上传人',
  `fail_num` int DEFAULT NULL COMMENT '失败数据',
  `file_id` varchar(64) COLLATE utf8mb4_general_ci NOT NULL COMMENT '文件id',
  `finished_time` bigint DEFAULT NULL COMMENT '处理结束时间',
  `result_content` longtext COLLATE utf8mb4_general_ci COMMENT '处理结果',
  `skip_num` int DEFAULT NULL COMMENT '跳过数据',
  `state` tinyint NOT NULL DEFAULT '0' COMMENT '处理结果 0 待处理 1 处理中 2处理结束 3 处理失败',
  `success_num` int DEFAULT NULL COMMENT '成功数据',
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_lwjif42r49lfpe01c76mef5se` (`record_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ----------------------------
-- Records of t_sys_file_data_process_record
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for t_sys_file_export_record
-- ----------------------------
DROP TABLE IF EXISTS `t_sys_file_export_record`;
CREATE TABLE `t_sys_file_export_record` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `create_time` bigint DEFAULT NULL,
  `is_deleted` bit(1) NOT NULL,
  `update_time` bigint DEFAULT NULL,
  `version` int NOT NULL,
  `record_id` varchar(64) COLLATE utf8mb4_general_ci NOT NULL,
  `bis` varchar(50) COLLATE utf8mb4_general_ci NOT NULL COMMENT '导出业务，如：导出教师基本信息',
  `creator` bigint NOT NULL COMMENT '导出人',
  `error_message` text COLLATE utf8mb4_general_ci COMMENT '失败结果',
  `file_id` varchar(64) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '文件id',
  `file_name` varchar(100) COLLATE utf8mb4_general_ci NOT NULL COMMENT '要导出的文件名称',
  `finished_time` bigint DEFAULT NULL COMMENT '处理结束时间',
  `state` tinyint NOT NULL DEFAULT '0' COMMENT '处理结果 0 待处理 1 处理中 2处理结束',
  `success` tinyint DEFAULT NULL COMMENT '是否处理成功',
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_haw9w7jm5vghao913dymkcca1` (`record_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ----------------------------
-- Records of t_sys_file_export_record
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for t_sys_resource
-- ----------------------------
DROP TABLE IF EXISTS `t_sys_resource`;
CREATE TABLE `t_sys_resource` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `create_time` bigint NOT NULL,
  `is_deleted` bit(1) NOT NULL,
  `update_time` bigint NOT NULL,
  `version` int NOT NULL,
  `code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '资源code（全局唯一）',
  `icon` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '图标',
  `name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '资源名称',
  `open_use` bit(1) NOT NULL DEFAULT b'1' COMMENT '是否默认可用',
  `out_link` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否为外链菜单',
  `parent_id` bigint NOT NULL DEFAULT '0' COMMENT '父资源Id',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '描述',
  `sort` smallint NOT NULL DEFAULT '999' COMMENT '排序',
  `type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '资源类型',
  `visible` bit(1) NOT NULL DEFAULT b'1' COMMENT '是否可见',
  `purview` smallint NOT NULL DEFAULT '0' COMMENT '是否指定数据权限 0 不指定 1 指定',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `UK_avini1trg6x8wbh7g1u22f27v` (`code`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=144 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ----------------------------
-- Records of t_sys_resource
-- ----------------------------
BEGIN;
INSERT INTO `t_sys_resource` (`id`, `create_time`, `is_deleted`, `update_time`, `version`, `code`, `icon`, `name`, `open_use`, `out_link`, `parent_id`, `remark`, `sort`, `type`, `visible`, `purview`) VALUES (1, 1661526419812, b'0', 1695812373876, 4, 'sys', '', '系统管理中心', b'1', b'0', 0, '', 1, '目录', b'1', 0);
INSERT INTO `t_sys_resource` (`id`, `create_time`, `is_deleted`, `update_time`, `version`, `code`, `icon`, `name`, `open_use`, `out_link`, `parent_id`, `remark`, `sort`, `type`, `visible`, `purview`) VALUES (2, 1661526994016, b'0', -1, 0, 'sys.rbac', '', '用户权限管理', b'1', b'0', 1, '', 0, '目录', b'1', 0);
INSERT INTO `t_sys_resource` (`id`, `create_time`, `is_deleted`, `update_time`, `version`, `code`, `icon`, `name`, `open_use`, `out_link`, `parent_id`, `remark`, `sort`, `type`, `visible`, `purview`) VALUES (3, 1661527037727, b'0', 1661737310055, 1, 'sys.system', '', '系统管理', b'1', b'0', 1, '', 2, '目录', b'1', 0);
INSERT INTO `t_sys_resource` (`id`, `create_time`, `is_deleted`, `update_time`, `version`, `code`, `icon`, `name`, `open_use`, `out_link`, `parent_id`, `remark`, `sort`, `type`, `visible`, `purview`) VALUES (4, 1661527056993, b'0', 1661737321747, 2, 'sys.monitor', '', '运行监控', b'1', b'0', 1, '', 4, '目录', b'1', 0);
INSERT INTO `t_sys_resource` (`id`, `create_time`, `is_deleted`, `update_time`, `version`, `code`, `icon`, `name`, `open_use`, `out_link`, `parent_id`, `remark`, `sort`, `type`, `visible`, `purview`) VALUES (5, 1661529223456, b'0', 1661529998716, 2, 'sys.security', '', '安全设置', b'1', b'0', 1, '', 3, '目录', b'1', 0);
INSERT INTO `t_sys_resource` (`id`, `create_time`, `is_deleted`, `update_time`, `version`, `code`, `icon`, `name`, `open_use`, `out_link`, `parent_id`, `remark`, `sort`, `type`, `visible`, `purview`) VALUES (6, 1661529259705, b'0', 1661737098660, 1, 'sys.rbac.user', '', '用户管理', b'1', b'0', 2, '', 0, '菜单', b'1', 0);
INSERT INTO `t_sys_resource` (`id`, `create_time`, `is_deleted`, `update_time`, `version`, `code`, `icon`, `name`, `open_use`, `out_link`, `parent_id`, `remark`, `sort`, `type`, `visible`, `purview`) VALUES (7, 1661529690065, b'0', 1696863934678, 4, 'sys.rbac.grant', '', '用户角色授权', b'1', b'0', 2, '需指定角色管理权限', 1, '菜单', b'1', 1);
INSERT INTO `t_sys_resource` (`id`, `create_time`, `is_deleted`, `update_time`, `version`, `code`, `icon`, `name`, `open_use`, `out_link`, `parent_id`, `remark`, `sort`, `type`, `visible`, `purview`) VALUES (8, 1661529701453, b'0', 1661737115492, 1, 'sys.rbac.role', '', '角色管理', b'1', b'0', 2, '', 2, '菜单', b'1', 0);
INSERT INTO `t_sys_resource` (`id`, `create_time`, `is_deleted`, `update_time`, `version`, `code`, `icon`, `name`, `open_use`, `out_link`, `parent_id`, `remark`, `sort`, `type`, `visible`, `purview`) VALUES (9, 1661529714895, b'0', 1661737103511, 1, 'sys.rbac.resource', '', '资源管理', b'1', b'0', 2, '', 0, '菜单', b'1', 0);
INSERT INTO `t_sys_resource` (`id`, `create_time`, `is_deleted`, `update_time`, `version`, `code`, `icon`, `name`, `open_use`, `out_link`, `parent_id`, `remark`, `sort`, `type`, `visible`, `purview`) VALUES (10, 1661530023187, b'0', 1661737326691, 1, 'sys.audit', '', '安全审计', b'1', b'0', 1, '', 5, '目录', b'1', 0);
INSERT INTO `t_sys_resource` (`id`, `create_time`, `is_deleted`, `update_time`, `version`, `code`, `icon`, `name`, `open_use`, `out_link`, `parent_id`, `remark`, `sort`, `type`, `visible`, `purview`) VALUES (11, 1661530061603, b'0', 1661737138943, 1, 'sys.system.info', '', '系统信息', b'1', b'0', 3, '', 0, '菜单', b'1', 0);
INSERT INTO `t_sys_resource` (`id`, `create_time`, `is_deleted`, `update_time`, `version`, `code`, `icon`, `name`, `open_use`, `out_link`, `parent_id`, `remark`, `sort`, `type`, `visible`, `purview`) VALUES (12, 1661530078734, b'0', 1661737142374, 1, 'sys.system.application', '', '应用管理', b'1', b'0', 3, '', 1, '菜单', b'1', 0);
INSERT INTO `t_sys_resource` (`id`, `create_time`, `is_deleted`, `update_time`, `version`, `code`, `icon`, `name`, `open_use`, `out_link`, `parent_id`, `remark`, `sort`, `type`, `visible`, `purview`) VALUES (13, 1661530093386, b'0', 1661737145492, 1, 'sys.system.backup', '', '备份管理', b'1', b'0', 3, '', 2, '菜单', b'1', 0);
INSERT INTO `t_sys_resource` (`id`, `create_time`, `is_deleted`, `update_time`, `version`, `code`, `icon`, `name`, `open_use`, `out_link`, `parent_id`, `remark`, `sort`, `type`, `visible`, `purview`) VALUES (14, 1661530107864, b'0', 1661737153578, 1, 'sys.system.file', '', '文件管理', b'1', b'0', 3, '', 3, '菜单', b'1', 0);
INSERT INTO `t_sys_resource` (`id`, `create_time`, `is_deleted`, `update_time`, `version`, `code`, `icon`, `name`, `open_use`, `out_link`, `parent_id`, `remark`, `sort`, `type`, `visible`, `purview`) VALUES (15, 1661530139013, b'0', 1661737159609, 1, 'sys.security.login', '', '登录设置', b'1', b'0', 5, '', 0, '菜单', b'1', 0);
INSERT INTO `t_sys_resource` (`id`, `create_time`, `is_deleted`, `update_time`, `version`, `code`, `icon`, `name`, `open_use`, `out_link`, `parent_id`, `remark`, `sort`, `type`, `visible`, `purview`) VALUES (16, 1661530356710, b'0', 1661737163789, 1, 'sys.audit.search', '', '日志查询统计', b'1', b'0', 10, '', 0, '菜单', b'1', 0);
INSERT INTO `t_sys_resource` (`id`, `create_time`, `is_deleted`, `update_time`, `version`, `code`, `icon`, `name`, `open_use`, `out_link`, `parent_id`, `remark`, `sort`, `type`, `visible`, `purview`) VALUES (17, 1661530378024, b'0', 1661737167564, 1, 'sys.audit.setting', '', '日志管理配置', b'1', b'0', 10, '', 1, '菜单', b'1', 0);
INSERT INTO `t_sys_resource` (`id`, `create_time`, `is_deleted`, `update_time`, `version`, `code`, `icon`, `name`, `open_use`, `out_link`, `parent_id`, `remark`, `sort`, `type`, `visible`, `purview`) VALUES (33, 1661737519430, b'0', 1661737546013, 1, 'sys.monitor.server', '', '服务环境监控', b'1', b'0', 4, '', 0, '菜单', b'1', 0);
INSERT INTO `t_sys_resource` (`id`, `create_time`, `is_deleted`, `update_time`, `version`, `code`, `icon`, `name`, `open_use`, `out_link`, `parent_id`, `remark`, `sort`, `type`, `visible`, `purview`) VALUES (34, 1661737538897, b'0', 1661737574900, 1, 'sys.monitor.data', '', '访问运行监控', b'1', b'0', 4, '', 0, '目录', b'1', 0);
INSERT INTO `t_sys_resource` (`id`, `create_time`, `is_deleted`, `update_time`, `version`, `code`, `icon`, `name`, `open_use`, `out_link`, `parent_id`, `remark`, `sort`, `type`, `visible`, `purview`) VALUES (64, 1661928869980, b'0', 1661928947878, 1, 'workbench', '', '工作台', b'1', b'0', 0, '', 0, '目录', b'1', 0);
INSERT INTO `t_sys_resource` (`id`, `create_time`, `is_deleted`, `update_time`, `version`, `code`, `icon`, `name`, `open_use`, `out_link`, `parent_id`, `remark`, `sort`, `type`, `visible`, `purview`) VALUES (90, 1667462299436, b'0', 1667462304384, 1, 'sys.system.forgot', '', '忘记密码邮箱配置', b'1', b'0', 3, '', 4, '菜单', b'1', 0);
INSERT INTO `t_sys_resource` (`id`, `create_time`, `is_deleted`, `update_time`, `version`, `code`, `icon`, `name`, `open_use`, `out_link`, `parent_id`, `remark`, `sort`, `type`, `visible`, `purview`) VALUES (91, 1667555867627, b'0', 1667555877178, 1, 'sys.rbac.user.add', '', '添加', b'1', b'0', 6, '', 0, '按钮', b'1', 0);
INSERT INTO `t_sys_resource` (`id`, `create_time`, `is_deleted`, `update_time`, `version`, `code`, `icon`, `name`, `open_use`, `out_link`, `parent_id`, `remark`, `sort`, `type`, `visible`, `purview`) VALUES (92, 1667555900069, b'0', -1, 0, 'sys.rbac.user.bind_role', '', '授权角色', b'1', b'0', 6, '', 4, '按钮', b'1', 0);
INSERT INTO `t_sys_resource` (`id`, `create_time`, `is_deleted`, `update_time`, `version`, `code`, `icon`, `name`, `open_use`, `out_link`, `parent_id`, `remark`, `sort`, `type`, `visible`, `purview`) VALUES (93, 1667555913037, b'0', 1667555934352, 1, 'sys.rbac.user.delete', '', '删除', b'1', b'0', 6, '', 1, '按钮', b'1', 0);
INSERT INTO `t_sys_resource` (`id`, `create_time`, `is_deleted`, `update_time`, `version`, `code`, `icon`, `name`, `open_use`, `out_link`, `parent_id`, `remark`, `sort`, `type`, `visible`, `purview`) VALUES (94, 1667555927591, b'0', 1667555937696, 1, 'sys.rbac.user.reset_pass', '', '重置密码', b'1', b'0', 6, '', 2, '按钮', b'1', 0);
INSERT INTO `t_sys_resource` (`id`, `create_time`, `is_deleted`, `update_time`, `version`, `code`, `icon`, `name`, `open_use`, `out_link`, `parent_id`, `remark`, `sort`, `type`, `visible`, `purview`) VALUES (95, 1667555951974, b'0', 1667555966005, 1, 'sys.rbac.resource.add', '', '添加', b'1', b'0', 9, '', 1, '按钮', b'1', 0);
INSERT INTO `t_sys_resource` (`id`, `create_time`, `is_deleted`, `update_time`, `version`, `code`, `icon`, `name`, `open_use`, `out_link`, `parent_id`, `remark`, `sort`, `type`, `visible`, `purview`) VALUES (96, 1667555961534, b'0', -1, 0, 'sys.rbac.resource.delete', '', '删除', b'1', b'0', 9, '', 2, '按钮', b'1', 0);
INSERT INTO `t_sys_resource` (`id`, `create_time`, `is_deleted`, `update_time`, `version`, `code`, `icon`, `name`, `open_use`, `out_link`, `parent_id`, `remark`, `sort`, `type`, `visible`, `purview`) VALUES (97, 1667555992654, b'0', 1697014490876, 2, 'sys.rbac.role.add', '', '添加角色', b'1', b'0', 8, '', 0, '按钮', b'1', 0);
INSERT INTO `t_sys_resource` (`id`, `create_time`, `is_deleted`, `update_time`, `version`, `code`, `icon`, `name`, `open_use`, `out_link`, `parent_id`, `remark`, `sort`, `type`, `visible`, `purview`) VALUES (98, 1667556009766, b'0', 1697014497637, 1, 'sys.rbac.role.delete', '', '删除角色', b'1', b'0', 8, '', 2, '按钮', b'1', 0);
INSERT INTO `t_sys_resource` (`id`, `create_time`, `is_deleted`, `update_time`, `version`, `code`, `icon`, `name`, `open_use`, `out_link`, `parent_id`, `remark`, `sort`, `type`, `visible`, `purview`) VALUES (99, 1667573269763, b'0', -1, 0, 'sys.audit.import.search', '', '审计日志查询', b'1', b'0', 10, '', 2, '目录', b'1', 0);
INSERT INTO `t_sys_resource` (`id`, `create_time`, `is_deleted`, `update_time`, `version`, `code`, `icon`, `name`, `open_use`, `out_link`, `parent_id`, `remark`, `sort`, `type`, `visible`, `purview`) VALUES (111, 1695808404531, b'0', 1697014494039, 1, 'sys.rbac.role.edit', '', '编辑角色', b'1', b'0', 8, '', 1, '按钮', b'1', 0);
INSERT INTO `t_sys_resource` (`id`, `create_time`, `is_deleted`, `update_time`, `version`, `code`, `icon`, `name`, `open_use`, `out_link`, `parent_id`, `remark`, `sort`, `type`, `visible`, `purview`) VALUES (112, 1695808434306, b'0', 1695808460377, 1, 'sys.rbac.role.add-child', '', '添加子角色', b'1', b'0', 8, '', 3, '按钮', b'1', 0);
INSERT INTO `t_sys_resource` (`id`, `create_time`, `is_deleted`, `update_time`, `version`, `code`, `icon`, `name`, `open_use`, `out_link`, `parent_id`, `remark`, `sort`, `type`, `visible`, `purview`) VALUES (113, 1695808455980, b'0', -1, 0, 'sys.rbac.role.user-grant', '', '用户授权', b'1', b'0', 8, '', 4, '按钮', b'1', 0);
INSERT INTO `t_sys_resource` (`id`, `create_time`, `is_deleted`, `update_time`, `version`, `code`, `icon`, `name`, `open_use`, `out_link`, `parent_id`, `remark`, `sort`, `type`, `visible`, `purview`) VALUES (114, 1695810728146, b'0', -1, 0, 'sys.rbac.role.resource-bind', '', '资源绑定', b'1', b'0', 8, '', 5, '按钮', b'1', 0);
INSERT INTO `t_sys_resource` (`id`, `create_time`, `is_deleted`, `update_time`, `version`, `code`, `icon`, `name`, `open_use`, `out_link`, `parent_id`, `remark`, `sort`, `type`, `visible`, `purview`) VALUES (115, 1695812427726, b'0', 1695812436248, 1, 'bis', '', '业务管理中心', b'1', b'0', 0, '', 2, '目录', b'1', 0);
INSERT INTO `t_sys_resource` (`id`, `create_time`, `is_deleted`, `update_time`, `version`, `code`, `icon`, `name`, `open_use`, `out_link`, `parent_id`, `remark`, `sort`, `type`, `visible`, `purview`) VALUES (116, 1695812483843, b'0', 1696756735660, 2, 'bis.xy', '', '校友系统', b'1', b'0', 115, '', 0, '目录', b'1', 0);
INSERT INTO `t_sys_resource` (`id`, `create_time`, `is_deleted`, `update_time`, `version`, `code`, `icon`, `name`, `open_use`, `out_link`, `parent_id`, `remark`, `sort`, `type`, `visible`, `purview`) VALUES (117, 1695812552638, b'0', 1697011476366, 4, 'bis.xy.news', '', '新闻', b'1', b'0', 116, '', 0, '菜单', b'1', 0);
INSERT INTO `t_sys_resource` (`id`, `create_time`, `is_deleted`, `update_time`, `version`, `code`, `icon`, `name`, `open_use`, `out_link`, `parent_id`, `remark`, `sort`, `type`, `visible`, `purview`) VALUES (118, 1695812582165, b'0', 1697011750097, 4, 'bis.xy.branch', '', '校友组织', b'1', b'0', 116, '', 2, '菜单', b'1', 0);
INSERT INTO `t_sys_resource` (`id`, `create_time`, `is_deleted`, `update_time`, `version`, `code`, `icon`, `name`, `open_use`, `out_link`, `parent_id`, `remark`, `sort`, `type`, `visible`, `purview`) VALUES (119, 1695812616294, b'0', 1697011707878, 1, 'bis.xy.news.add', '', '添加', b'1', b'0', 135, '', 0, '按钮', b'1', 0);
INSERT INTO `t_sys_resource` (`id`, `create_time`, `is_deleted`, `update_time`, `version`, `code`, `icon`, `name`, `open_use`, `out_link`, `parent_id`, `remark`, `sort`, `type`, `visible`, `purview`) VALUES (120, 1695812630718, b'0', 1697011712583, 1, 'bis.xy.news.list.edit', '', '编辑', b'1', b'0', 135, '', 1, '按钮', b'1', 0);
INSERT INTO `t_sys_resource` (`id`, `create_time`, `is_deleted`, `update_time`, `version`, `code`, `icon`, `name`, `open_use`, `out_link`, `parent_id`, `remark`, `sort`, `type`, `visible`, `purview`) VALUES (121, 1695812650098, b'0', 1697011772762, 5, 'bis.xy.audit', '', '审核认证', b'1', b'0', 116, '', 3, '按钮', b'1', 0);
INSERT INTO `t_sys_resource` (`id`, `create_time`, `is_deleted`, `update_time`, `version`, `code`, `icon`, `name`, `open_use`, `out_link`, `parent_id`, `remark`, `sort`, `type`, `visible`, `purview`) VALUES (122, 1695812698256, b'0', 1697011717458, 1, 'bis.xy.news.list.delete', '', '删除', b'1', b'0', 135, '', 3, '按钮', b'1', 0);
INSERT INTO `t_sys_resource` (`id`, `create_time`, `is_deleted`, `update_time`, `version`, `code`, `icon`, `name`, `open_use`, `out_link`, `parent_id`, `remark`, `sort`, `type`, `visible`, `purview`) VALUES (123, 1695812716902, b'0', 1697011722074, 1, 'bis.xy.news.list.upload', '', '上传', b'1', b'0', 135, '', 4, '按钮', b'1', 0);
INSERT INTO `t_sys_resource` (`id`, `create_time`, `is_deleted`, `update_time`, `version`, `code`, `icon`, `name`, `open_use`, `out_link`, `parent_id`, `remark`, `sort`, `type`, `visible`, `purview`) VALUES (124, 1695812737610, b'0', 1697011726932, 1, 'bis.xy.news.list.export', '', '导出', b'1', b'0', 135, '', 5, '按钮', b'1', 0);
INSERT INTO `t_sys_resource` (`id`, `create_time`, `is_deleted`, `update_time`, `version`, `code`, `icon`, `name`, `open_use`, `out_link`, `parent_id`, `remark`, `sort`, `type`, `visible`, `purview`) VALUES (125, 1695812834763, b'0', 1697011731033, 1, 'bis.xy.news.list.search', '', '检索', b'1', b'0', 135, '', 5, '按钮', b'1', 0);
INSERT INTO `t_sys_resource` (`id`, `create_time`, `is_deleted`, `update_time`, `version`, `code`, `icon`, `name`, `open_use`, `out_link`, `parent_id`, `remark`, `sort`, `type`, `visible`, `purview`) VALUES (128, 1696865954182, b'0', -1, 0, 'sys.rbac.purview', '', '数据权限定义', b'1', b'0', 2, '', 6, '菜单', b'1', 0);
INSERT INTO `t_sys_resource` (`id`, `create_time`, `is_deleted`, `update_time`, `version`, `code`, `icon`, `name`, `open_use`, `out_link`, `parent_id`, `remark`, `sort`, `type`, `visible`, `purview`) VALUES (129, 1696866097538, b'0', 1696994642920, 2, 'sys.rbac.grant.users', '', '授权用户', b'1', b'0', 7, '', 0, '按钮', b'1', 1);
INSERT INTO `t_sys_resource` (`id`, `create_time`, `is_deleted`, `update_time`, `version`, `code`, `icon`, `name`, `open_use`, `out_link`, `parent_id`, `remark`, `sort`, `type`, `visible`, `purview`) VALUES (131, 1696923093506, b'0', 1697011780899, 2, 'bis.xy.contact', '', '联系我们', b'1', b'0', 116, '', 4, '菜单', b'1', 0);
INSERT INTO `t_sys_resource` (`id`, `create_time`, `is_deleted`, `update_time`, `version`, `code`, `icon`, `name`, `open_use`, `out_link`, `parent_id`, `remark`, `sort`, `type`, `visible`, `purview`) VALUES (132, 1697010687050, b'0', 1697016697237, 3, 'bis.xy.branch.index', '', '校友会', b'1', b'0', 118, '', 1, '菜单', b'1', 1);
INSERT INTO `t_sys_resource` (`id`, `create_time`, `is_deleted`, `update_time`, `version`, `code`, `icon`, `name`, `open_use`, `out_link`, `parent_id`, `remark`, `sort`, `type`, `visible`, `purview`) VALUES (134, 1697011546067, b'0', -1, 0, 'bis.xy.news.category', '', '新闻模块', b'1', b'0', 117, '', 0, '菜单', b'1', 0);
INSERT INTO `t_sys_resource` (`id`, `create_time`, `is_deleted`, `update_time`, `version`, `code`, `icon`, `name`, `open_use`, `out_link`, `parent_id`, `remark`, `sort`, `type`, `visible`, `purview`) VALUES (135, 1697011565528, b'0', -1, 0, 'bis.xy.news.list', '', '新闻发布', b'1', b'0', 117, '', 1, '目录', b'1', 0);
INSERT INTO `t_sys_resource` (`id`, `create_time`, `is_deleted`, `update_time`, `version`, `code`, `icon`, `name`, `open_use`, `out_link`, `parent_id`, `remark`, `sort`, `type`, `visible`, `purview`) VALUES (137, 1697079505925, b'0', -1, 0, 'bis.xy.branch.manager', '', '总会管理', b'1', b'0', 118, '', 2, '按钮', b'1', 0);
INSERT INTO `t_sys_resource` (`id`, `create_time`, `is_deleted`, `update_time`, `version`, `code`, `icon`, `name`, `open_use`, `out_link`, `parent_id`, `remark`, `sort`, `type`, `visible`, `purview`) VALUES (138, 1697080182601, b'0', -1, 0, 'bis.xy.branch.index.join-audit', '', '入会审核', b'1', b'0', 132, '', 1, '按钮', b'1', 0);
INSERT INTO `t_sys_resource` (`id`, `create_time`, `is_deleted`, `update_time`, `version`, `code`, `icon`, `name`, `open_use`, `out_link`, `parent_id`, `remark`, `sort`, `type`, `visible`, `purview`) VALUES (139, 1697080198583, b'0', -1, 0, 'bis.xy.branch.index.member', '', '成员管理', b'1', b'0', 132, '', 2, '按钮', b'1', 0);
INSERT INTO `t_sys_resource` (`id`, `create_time`, `is_deleted`, `update_time`, `version`, `code`, `icon`, `name`, `open_use`, `out_link`, `parent_id`, `remark`, `sort`, `type`, `visible`, `purview`) VALUES (140, 1697080269756, b'0', -1, 0, 'bis.xy.branch.index.info', '', '组织资讯管理', b'1', b'0', 132, '', 3, '按钮', b'1', 0);
INSERT INTO `t_sys_resource` (`id`, `create_time`, `is_deleted`, `update_time`, `version`, `code`, `icon`, `name`, `open_use`, `out_link`, `parent_id`, `remark`, `sort`, `type`, `visible`, `purview`) VALUES (141, 1697080328503, b'0', -1, 0, 'bis.xy.branch.index.info.add', '', '组织资讯添加', b'1', b'0', 140, '', 1, '按钮', b'1', 0);
INSERT INTO `t_sys_resource` (`id`, `create_time`, `is_deleted`, `update_time`, `version`, `code`, `icon`, `name`, `open_use`, `out_link`, `parent_id`, `remark`, `sort`, `type`, `visible`, `purview`) VALUES (142, 1697080347306, b'0', -1, 0, 'bis.xy.branch.index.info.edit', '', '组织资讯编辑', b'1', b'0', 140, '', 2, '按钮', b'1', 0);
INSERT INTO `t_sys_resource` (`id`, `create_time`, `is_deleted`, `update_time`, `version`, `code`, `icon`, `name`, `open_use`, `out_link`, `parent_id`, `remark`, `sort`, `type`, `visible`, `purview`) VALUES (143, 1697080362813, b'0', -1, 0, 'bis.xy.branch.index.info.delete', '', '组织资讯删除', b'1', b'0', 140, '', 3, '目录', b'1', 0);
COMMIT;

-- ----------------------------
-- Table structure for t_sys_role
-- ----------------------------
DROP TABLE IF EXISTS `t_sys_role`;
CREATE TABLE `t_sys_role` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `create_time` bigint NOT NULL,
  `is_deleted` bit(1) NOT NULL,
  `update_time` bigint NOT NULL DEFAULT '-1',
  `version` int NOT NULL,
  `code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '角色CODE',
  `enable` bit(1) NOT NULL DEFAULT b'1' COMMENT '状态：1正常 0冻结',
  `name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '角色名称',
  `parent_id` bigint NOT NULL DEFAULT '0' COMMENT '父角色Id',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '描述',
  `scope_type` tinyint NOT NULL DEFAULT '0' COMMENT '默认权限范围类型：0自己1租户2所在部门3自定义9全平台',
  `sort` smallint NOT NULL DEFAULT '999' COMMENT '排序',
  `sys` bit(1) NOT NULL DEFAULT b'1' COMMENT '系统内置角色1，不可删除冻结',
  `bis_show` bit(1) NOT NULL DEFAULT b'1' COMMENT '是否在业务统一授权中显示 1 显示 0 不显示',
  `level` tinyint NOT NULL DEFAULT '0' COMMENT '角色等级（用于区分权限大小，管理角色使用）：30学校 20院系 10教研中心',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `UK_t7hsyr4f9bgbf4vku1hecn9rw` (`code`) USING BTREE,
  KEY `IDXt7hsyr4f9bgbf4vku1hecn9rw` (`code`)
) ENGINE=InnoDB AUTO_INCREMENT=42 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ----------------------------
-- Records of t_sys_role
-- ----------------------------
BEGIN;
INSERT INTO `t_sys_role` (`id`, `create_time`, `is_deleted`, `update_time`, `version`, `code`, `enable`, `name`, `parent_id`, `remark`, `scope_type`, `sort`, `sys`, `bis_show`, `level`) VALUES (1, 1661764673964, b'0', 1697014585523, 2, 'sys_admin', b'1', '系统管理员', 0, '', 0, 0, b'1', b'0', 30);
INSERT INTO `t_sys_role` (`id`, `create_time`, `is_deleted`, `update_time`, `version`, `code`, `enable`, `name`, `parent_id`, `remark`, `scope_type`, `sort`, `sys`, `bis_show`, `level`) VALUES (2, 1661764695654, b'0', 1697014604957, 4, 'sys_security', b'1', '安全保密员', 0, '', 0, 1, b'1', b'0', 30);
INSERT INTO `t_sys_role` (`id`, `create_time`, `is_deleted`, `update_time`, `version`, `code`, `enable`, `name`, `parent_id`, `remark`, `scope_type`, `sort`, `sys`, `bis_show`, `level`) VALUES (3, 1661764718729, b'0', 1697014618393, 3, 'sys_audit', b'1', '安全审计员', 0, '', 0, 2, b'1', b'0', 30);
INSERT INTO `t_sys_role` (`id`, `create_time`, `is_deleted`, `update_time`, `version`, `code`, `enable`, `name`, `parent_id`, `remark`, `scope_type`, `sort`, `sys`, `bis_show`, `level`) VALUES (33, 1696867974703, b'0', 1696867991609, 1, 'xy_admin', b'1', '校友管理员', 0, '', 0, 7, b'0', b'1', 0);
INSERT INTO `t_sys_role` (`id`, `create_time`, `is_deleted`, `update_time`, `version`, `code`, `enable`, `name`, `parent_id`, `remark`, `scope_type`, `sort`, `sys`, `bis_show`, `level`) VALUES (34, 1696868010417, b'0', -1, 0, 'xy_admin.branch_leader', b'1', '分会管理员', 33, '', 0, 0, b'0', b'0', 0);
INSERT INTO `t_sys_role` (`id`, `create_time`, `is_deleted`, `update_time`, `version`, `code`, `enable`, `name`, `parent_id`, `remark`, `scope_type`, `sort`, `sys`, `bis_show`, `level`) VALUES (40, 1697014370043, b'0', -1, 0, 'xy_admin.news', b'1', '新闻管理员', 33, '', 0, 0, b'0', b'0', 0);
COMMIT;

-- ----------------------------
-- Table structure for t_sys_role_resource
-- ----------------------------
DROP TABLE IF EXISTS `t_sys_role_resource`;
CREATE TABLE `t_sys_role_resource` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `create_time` bigint DEFAULT NULL,
  `is_deleted` bit(1) NOT NULL,
  `update_time` bigint DEFAULT NULL,
  `version` int NOT NULL,
  `enable` bit(1) NOT NULL DEFAULT b'1' COMMENT '是否有效',
  `resource_id` bigint NOT NULL COMMENT '资源菜单Id',
  `role_id` bigint NOT NULL COMMENT '角色Id',
  `scope_type` tinyint NOT NULL DEFAULT '0' COMMENT '权限范围0自己1所在组织2所在部门3自定义',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=308 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ----------------------------
-- Records of t_sys_role_resource
-- ----------------------------
BEGIN;
INSERT INTO `t_sys_role_resource` (`id`, `create_time`, `is_deleted`, `update_time`, `version`, `enable`, `resource_id`, `role_id`, `scope_type`) VALUES (1, 1661927884193, b'0', NULL, 0, b'1', 2, 1, 0);
INSERT INTO `t_sys_role_resource` (`id`, `create_time`, `is_deleted`, `update_time`, `version`, `enable`, `resource_id`, `role_id`, `scope_type`) VALUES (2, 1661927884980, b'0', 1667556060728, 2, b'1', 6, 1, 0);
INSERT INTO `t_sys_role_resource` (`id`, `create_time`, `is_deleted`, `update_time`, `version`, `enable`, `resource_id`, `role_id`, `scope_type`) VALUES (3, 1661927889308, b'0', 1667556103520, 1, b'0', 9, 1, 0);
INSERT INTO `t_sys_role_resource` (`id`, `create_time`, `is_deleted`, `update_time`, `version`, `enable`, `resource_id`, `role_id`, `scope_type`) VALUES (4, 1661927889900, b'0', 1661927932496, 1, b'0', 7, 1, 0);
INSERT INTO `t_sys_role_resource` (`id`, `create_time`, `is_deleted`, `update_time`, `version`, `enable`, `resource_id`, `role_id`, `scope_type`) VALUES (5, 1661927890525, b'0', 1661927939541, 1, b'0', 8, 1, 0);
INSERT INTO `t_sys_role_resource` (`id`, `create_time`, `is_deleted`, `update_time`, `version`, `enable`, `resource_id`, `role_id`, `scope_type`) VALUES (6, 1661927894146, b'0', NULL, 0, b'1', 32, 1, 0);
INSERT INTO `t_sys_role_resource` (`id`, `create_time`, `is_deleted`, `update_time`, `version`, `enable`, `resource_id`, `role_id`, `scope_type`) VALUES (7, 1661927894657, b'0', NULL, 0, b'1', 35, 1, 0);
INSERT INTO `t_sys_role_resource` (`id`, `create_time`, `is_deleted`, `update_time`, `version`, `enable`, `resource_id`, `role_id`, `scope_type`) VALUES (8, 1661927895220, b'0', NULL, 0, b'1', 36, 1, 0);
INSERT INTO `t_sys_role_resource` (`id`, `create_time`, `is_deleted`, `update_time`, `version`, `enable`, `resource_id`, `role_id`, `scope_type`) VALUES (9, 1661927895655, b'0', NULL, 0, b'1', 37, 1, 0);
INSERT INTO `t_sys_role_resource` (`id`, `create_time`, `is_deleted`, `update_time`, `version`, `enable`, `resource_id`, `role_id`, `scope_type`) VALUES (10, 1661927897489, b'0', NULL, 0, b'1', 3, 1, 0);
INSERT INTO `t_sys_role_resource` (`id`, `create_time`, `is_deleted`, `update_time`, `version`, `enable`, `resource_id`, `role_id`, `scope_type`) VALUES (11, 1661927898141, b'0', 1668084011724, 2, b'1', 11, 1, 0);
INSERT INTO `t_sys_role_resource` (`id`, `create_time`, `is_deleted`, `update_time`, `version`, `enable`, `resource_id`, `role_id`, `scope_type`) VALUES (12, 1661927899184, b'0', 1668084011208, 2, b'1', 12, 1, 0);
INSERT INTO `t_sys_role_resource` (`id`, `create_time`, `is_deleted`, `update_time`, `version`, `enable`, `resource_id`, `role_id`, `scope_type`) VALUES (13, 1661927900401, b'0', 1668084012310, 2, b'1', 13, 1, 0);
INSERT INTO `t_sys_role_resource` (`id`, `create_time`, `is_deleted`, `update_time`, `version`, `enable`, `resource_id`, `role_id`, `scope_type`) VALUES (14, 1661927901202, b'0', NULL, 0, b'1', 14, 1, 0);
INSERT INTO `t_sys_role_resource` (`id`, `create_time`, `is_deleted`, `update_time`, `version`, `enable`, `resource_id`, `role_id`, `scope_type`) VALUES (15, 1661927913381, b'0', 1668084016348, 2, b'1', 33, 1, 0);
INSERT INTO `t_sys_role_resource` (`id`, `create_time`, `is_deleted`, `update_time`, `version`, `enable`, `resource_id`, `role_id`, `scope_type`) VALUES (16, 1661927914195, b'0', 1668084016948, 2, b'1', 34, 1, 0);
INSERT INTO `t_sys_role_resource` (`id`, `create_time`, `is_deleted`, `update_time`, `version`, `enable`, `resource_id`, `role_id`, `scope_type`) VALUES (17, 1661927918626, b'0', NULL, 0, b'1', 16, 1, 0);
INSERT INTO `t_sys_role_resource` (`id`, `create_time`, `is_deleted`, `update_time`, `version`, `enable`, `resource_id`, `role_id`, `scope_type`) VALUES (18, 1661927919764, b'0', NULL, 0, b'1', 10, 1, 0);
INSERT INTO `t_sys_role_resource` (`id`, `create_time`, `is_deleted`, `update_time`, `version`, `enable`, `resource_id`, `role_id`, `scope_type`) VALUES (19, 1661927920389, b'0', 1668084015657, 2, b'1', 4, 1, 0);
INSERT INTO `t_sys_role_resource` (`id`, `create_time`, `is_deleted`, `update_time`, `version`, `enable`, `resource_id`, `role_id`, `scope_type`) VALUES (20, 1661927921085, b'0', 1661927926999, 1, b'0', 5, 1, 0);
INSERT INTO `t_sys_role_resource` (`id`, `create_time`, `is_deleted`, `update_time`, `version`, `enable`, `resource_id`, `role_id`, `scope_type`) VALUES (21, 1661927947964, b'0', 1668084024905, 2, b'1', 41, 1, 0);
INSERT INTO `t_sys_role_resource` (`id`, `create_time`, `is_deleted`, `update_time`, `version`, `enable`, `resource_id`, `role_id`, `scope_type`) VALUES (22, 1661927948697, b'0', 1668084025451, 2, b'1', 42, 1, 0);
INSERT INTO `t_sys_role_resource` (`id`, `create_time`, `is_deleted`, `update_time`, `version`, `enable`, `resource_id`, `role_id`, `scope_type`) VALUES (23, 1661927949337, b'0', 1668084025850, 2, b'1', 43, 1, 0);
INSERT INTO `t_sys_role_resource` (`id`, `create_time`, `is_deleted`, `update_time`, `version`, `enable`, `resource_id`, `role_id`, `scope_type`) VALUES (24, 1661928715425, b'0', NULL, 0, b'1', 6, 2, 0);
INSERT INTO `t_sys_role_resource` (`id`, `create_time`, `is_deleted`, `update_time`, `version`, `enable`, `resource_id`, `role_id`, `scope_type`) VALUES (25, 1661928717005, b'0', 1696660528735, 1, b'0', 7, 2, 0);
INSERT INTO `t_sys_role_resource` (`id`, `create_time`, `is_deleted`, `update_time`, `version`, `enable`, `resource_id`, `role_id`, `scope_type`) VALUES (26, 1661928718099, b'0', 1696660523456, 3, b'0', 8, 2, 0);
INSERT INTO `t_sys_role_resource` (`id`, `create_time`, `is_deleted`, `update_time`, `version`, `enable`, `resource_id`, `role_id`, `scope_type`) VALUES (27, 1661928722824, b'0', NULL, 0, b'1', 5, 2, 0);
INSERT INTO `t_sys_role_resource` (`id`, `create_time`, `is_deleted`, `update_time`, `version`, `enable`, `resource_id`, `role_id`, `scope_type`) VALUES (28, 1661928723337, b'0', NULL, 0, b'1', 15, 2, 0);
INSERT INTO `t_sys_role_resource` (`id`, `create_time`, `is_deleted`, `update_time`, `version`, `enable`, `resource_id`, `role_id`, `scope_type`) VALUES (29, 1661928732780, b'0', NULL, 0, b'1', 10, 2, 0);
INSERT INTO `t_sys_role_resource` (`id`, `create_time`, `is_deleted`, `update_time`, `version`, `enable`, `resource_id`, `role_id`, `scope_type`) VALUES (30, 1661928733455, b'0', NULL, 0, b'1', 16, 2, 0);
INSERT INTO `t_sys_role_resource` (`id`, `create_time`, `is_deleted`, `update_time`, `version`, `enable`, `resource_id`, `role_id`, `scope_type`) VALUES (31, 1661928744655, b'0', NULL, 0, b'1', 10, 3, 0);
INSERT INTO `t_sys_role_resource` (`id`, `create_time`, `is_deleted`, `update_time`, `version`, `enable`, `resource_id`, `role_id`, `scope_type`) VALUES (32, 1661928745314, b'0', 1667573279723, 1, b'0', 16, 3, 0);
INSERT INTO `t_sys_role_resource` (`id`, `create_time`, `is_deleted`, `update_time`, `version`, `enable`, `resource_id`, `role_id`, `scope_type`) VALUES (33, 1661928745805, b'0', 1667573279321, 1, b'0', 17, 3, 0);
INSERT INTO `t_sys_role_resource` (`id`, `create_time`, `is_deleted`, `update_time`, `version`, `enable`, `resource_id`, `role_id`, `scope_type`) VALUES (118, 1663894664390, b'0', 1667556078161, 1, b'0', 44, 1, 0);
INSERT INTO `t_sys_role_resource` (`id`, `create_time`, `is_deleted`, `update_time`, `version`, `enable`, `resource_id`, `role_id`, `scope_type`) VALUES (119, 1663894665537, b'0', 1667556079015, 1, b'0', 54, 1, 0);
INSERT INTO `t_sys_role_resource` (`id`, `create_time`, `is_deleted`, `update_time`, `version`, `enable`, `resource_id`, `role_id`, `scope_type`) VALUES (120, 1663894666500, b'0', 1667556079576, 1, b'0', 74, 1, 0);
INSERT INTO `t_sys_role_resource` (`id`, `create_time`, `is_deleted`, `update_time`, `version`, `enable`, `resource_id`, `role_id`, `scope_type`) VALUES (121, 1663894667084, b'0', 1667556080031, 1, b'0', 86, 1, 0);
INSERT INTO `t_sys_role_resource` (`id`, `create_time`, `is_deleted`, `update_time`, `version`, `enable`, `resource_id`, `role_id`, `scope_type`) VALUES (122, 1663894667631, b'0', 1667556080587, 1, b'0', 87, 1, 0);
INSERT INTO `t_sys_role_resource` (`id`, `create_time`, `is_deleted`, `update_time`, `version`, `enable`, `resource_id`, `role_id`, `scope_type`) VALUES (123, 1663894668206, b'0', 1667556082232, 1, b'0', 88, 1, 0);
INSERT INTO `t_sys_role_resource` (`id`, `create_time`, `is_deleted`, `update_time`, `version`, `enable`, `resource_id`, `role_id`, `scope_type`) VALUES (124, 1663894668700, b'0', 1667556082673, 1, b'0', 89, 1, 0);
INSERT INTO `t_sys_role_resource` (`id`, `create_time`, `is_deleted`, `update_time`, `version`, `enable`, `resource_id`, `role_id`, `scope_type`) VALUES (125, 1663894669304, b'0', 1667556083222, 1, b'0', 55, 1, 0);
INSERT INTO `t_sys_role_resource` (`id`, `create_time`, `is_deleted`, `update_time`, `version`, `enable`, `resource_id`, `role_id`, `scope_type`) VALUES (137, 1667462340134, b'0', NULL, 0, b'1', 90, 1, 0);
INSERT INTO `t_sys_role_resource` (`id`, `create_time`, `is_deleted`, `update_time`, `version`, `enable`, `resource_id`, `role_id`, `scope_type`) VALUES (138, 1667556057400, b'0', NULL, 0, b'1', 1, 1, 0);
INSERT INTO `t_sys_role_resource` (`id`, `create_time`, `is_deleted`, `update_time`, `version`, `enable`, `resource_id`, `role_id`, `scope_type`) VALUES (139, 1667556061356, b'0', NULL, 0, b'1', 91, 1, 0);
INSERT INTO `t_sys_role_resource` (`id`, `create_time`, `is_deleted`, `update_time`, `version`, `enable`, `resource_id`, `role_id`, `scope_type`) VALUES (140, 1667556062428, b'0', NULL, 0, b'1', 93, 1, 0);
INSERT INTO `t_sys_role_resource` (`id`, `create_time`, `is_deleted`, `update_time`, `version`, `enable`, `resource_id`, `role_id`, `scope_type`) VALUES (141, 1667556065505, b'0', NULL, 0, b'1', 94, 1, 0);
INSERT INTO `t_sys_role_resource` (`id`, `create_time`, `is_deleted`, `update_time`, `version`, `enable`, `resource_id`, `role_id`, `scope_type`) VALUES (142, 1667556096991, b'0', NULL, 0, b'1', 17, 1, 0);
INSERT INTO `t_sys_role_resource` (`id`, `create_time`, `is_deleted`, `update_time`, `version`, `enable`, `resource_id`, `role_id`, `scope_type`) VALUES (143, 1667556118039, b'0', NULL, 0, b'1', 92, 2, 0);
INSERT INTO `t_sys_role_resource` (`id`, `create_time`, `is_deleted`, `update_time`, `version`, `enable`, `resource_id`, `role_id`, `scope_type`) VALUES (144, 1667556119985, b'0', 1668076009249, 1, b'0', 9, 2, 0);
INSERT INTO `t_sys_role_resource` (`id`, `create_time`, `is_deleted`, `update_time`, `version`, `enable`, `resource_id`, `role_id`, `scope_type`) VALUES (145, 1667556120484, b'0', 1668076010068, 1, b'0', 95, 2, 0);
INSERT INTO `t_sys_role_resource` (`id`, `create_time`, `is_deleted`, `update_time`, `version`, `enable`, `resource_id`, `role_id`, `scope_type`) VALUES (146, 1667556120857, b'0', 1668076010673, 1, b'0', 96, 2, 0);
INSERT INTO `t_sys_role_resource` (`id`, `create_time`, `is_deleted`, `update_time`, `version`, `enable`, `resource_id`, `role_id`, `scope_type`) VALUES (147, 1667556125815, b'0', 1668076012175, 1, b'0', 97, 2, 0);
INSERT INTO `t_sys_role_resource` (`id`, `create_time`, `is_deleted`, `update_time`, `version`, `enable`, `resource_id`, `role_id`, `scope_type`) VALUES (148, 1667556126429, b'0', 1668076012677, 1, b'0', 98, 2, 0);
INSERT INTO `t_sys_role_resource` (`id`, `create_time`, `is_deleted`, `update_time`, `version`, `enable`, `resource_id`, `role_id`, `scope_type`) VALUES (149, 1667556900869, b'0', NULL, 0, b'1', 2, 2, 0);
INSERT INTO `t_sys_role_resource` (`id`, `create_time`, `is_deleted`, `update_time`, `version`, `enable`, `resource_id`, `role_id`, `scope_type`) VALUES (150, 1667573281082, b'0', NULL, 0, b'1', 99, 3, 0);
INSERT INTO `t_sys_role_resource` (`id`, `create_time`, `is_deleted`, `update_time`, `version`, `enable`, `resource_id`, `role_id`, `scope_type`) VALUES (205, 1695807128714, b'0', NULL, 0, b'1', 92, 1, 0);
INSERT INTO `t_sys_role_resource` (`id`, `create_time`, `is_deleted`, `update_time`, `version`, `enable`, `resource_id`, `role_id`, `scope_type`) VALUES (246, 1696990305604, b'0', -1, 0, b'1', 7, 33, 0);
INSERT INTO `t_sys_role_resource` (`id`, `create_time`, `is_deleted`, `update_time`, `version`, `enable`, `resource_id`, `role_id`, `scope_type`) VALUES (247, 1696990336602, b'0', 1697104811380, 1, b'0', 7, 34, 0);
INSERT INTO `t_sys_role_resource` (`id`, `create_time`, `is_deleted`, `update_time`, `version`, `enable`, `resource_id`, `role_id`, `scope_type`) VALUES (248, 1696993391749, b'0', -1, 0, b'1', 2, 33, 0);
INSERT INTO `t_sys_role_resource` (`id`, `create_time`, `is_deleted`, `update_time`, `version`, `enable`, `resource_id`, `role_id`, `scope_type`) VALUES (249, 1696993402861, b'0', 1697104821044, 1, b'0', 2, 34, 0);
INSERT INTO `t_sys_role_resource` (`id`, `create_time`, `is_deleted`, `update_time`, `version`, `enable`, `resource_id`, `role_id`, `scope_type`) VALUES (250, 1696994716344, b'0', -1, 0, b'1', 129, 33, 0);
INSERT INTO `t_sys_role_resource` (`id`, `create_time`, `is_deleted`, `update_time`, `version`, `enable`, `resource_id`, `role_id`, `scope_type`) VALUES (251, 1696994730663, b'0', -1, 0, b'1', 115, 33, 0);
INSERT INTO `t_sys_role_resource` (`id`, `create_time`, `is_deleted`, `update_time`, `version`, `enable`, `resource_id`, `role_id`, `scope_type`) VALUES (252, 1696994731620, b'0', -1, 0, b'1', 116, 33, 0);
INSERT INTO `t_sys_role_resource` (`id`, `create_time`, `is_deleted`, `update_time`, `version`, `enable`, `resource_id`, `role_id`, `scope_type`) VALUES (253, 1696994735446, b'0', -1, 0, b'1', 117, 33, 0);
INSERT INTO `t_sys_role_resource` (`id`, `create_time`, `is_deleted`, `update_time`, `version`, `enable`, `resource_id`, `role_id`, `scope_type`) VALUES (254, 1696994735801, b'0', -1, 0, b'1', 119, 33, 0);
INSERT INTO `t_sys_role_resource` (`id`, `create_time`, `is_deleted`, `update_time`, `version`, `enable`, `resource_id`, `role_id`, `scope_type`) VALUES (255, 1696994736218, b'0', -1, 0, b'1', 120, 33, 0);
INSERT INTO `t_sys_role_resource` (`id`, `create_time`, `is_deleted`, `update_time`, `version`, `enable`, `resource_id`, `role_id`, `scope_type`) VALUES (256, 1696994736570, b'0', -1, 0, b'1', 122, 33, 0);
INSERT INTO `t_sys_role_resource` (`id`, `create_time`, `is_deleted`, `update_time`, `version`, `enable`, `resource_id`, `role_id`, `scope_type`) VALUES (257, 1696994736966, b'0', -1, 0, b'1', 123, 33, 0);
INSERT INTO `t_sys_role_resource` (`id`, `create_time`, `is_deleted`, `update_time`, `version`, `enable`, `resource_id`, `role_id`, `scope_type`) VALUES (258, 1696994737472, b'0', -1, 0, b'1', 124, 33, 0);
INSERT INTO `t_sys_role_resource` (`id`, `create_time`, `is_deleted`, `update_time`, `version`, `enable`, `resource_id`, `role_id`, `scope_type`) VALUES (259, 1696994737889, b'0', -1, 0, b'1', 125, 33, 0);
INSERT INTO `t_sys_role_resource` (`id`, `create_time`, `is_deleted`, `update_time`, `version`, `enable`, `resource_id`, `role_id`, `scope_type`) VALUES (260, 1696994738707, b'0', -1, 0, b'1', 118, 33, 0);
INSERT INTO `t_sys_role_resource` (`id`, `create_time`, `is_deleted`, `update_time`, `version`, `enable`, `resource_id`, `role_id`, `scope_type`) VALUES (261, 1696994739272, b'0', -1, 0, b'1', 121, 33, 0);
INSERT INTO `t_sys_role_resource` (`id`, `create_time`, `is_deleted`, `update_time`, `version`, `enable`, `resource_id`, `role_id`, `scope_type`) VALUES (262, 1696994739612, b'0', -1, 0, b'1', 131, 33, 0);
INSERT INTO `t_sys_role_resource` (`id`, `create_time`, `is_deleted`, `update_time`, `version`, `enable`, `resource_id`, `role_id`, `scope_type`) VALUES (263, 1696994747789, b'0', 1697104812027, 1, b'0', 129, 34, 0);
INSERT INTO `t_sys_role_resource` (`id`, `create_time`, `is_deleted`, `update_time`, `version`, `enable`, `resource_id`, `role_id`, `scope_type`) VALUES (264, 1696994761681, b'0', -1, 0, b'1', 118, 34, 0);
INSERT INTO `t_sys_role_resource` (`id`, `create_time`, `is_deleted`, `update_time`, `version`, `enable`, `resource_id`, `role_id`, `scope_type`) VALUES (265, 1697010807949, b'0', -1, 0, b'1', 133, 33, 0);
INSERT INTO `t_sys_role_resource` (`id`, `create_time`, `is_deleted`, `update_time`, `version`, `enable`, `resource_id`, `role_id`, `scope_type`) VALUES (266, 1697010816988, b'0', -1, 0, b'1', 132, 34, 0);
INSERT INTO `t_sys_role_resource` (`id`, `create_time`, `is_deleted`, `update_time`, `version`, `enable`, `resource_id`, `role_id`, `scope_type`) VALUES (267, 1697012334150, b'0', -1, 0, b'1', 134, 33, 0);
INSERT INTO `t_sys_role_resource` (`id`, `create_time`, `is_deleted`, `update_time`, `version`, `enable`, `resource_id`, `role_id`, `scope_type`) VALUES (268, 1697012334876, b'0', -1, 0, b'1', 135, 33, 0);
INSERT INTO `t_sys_role_resource` (`id`, `create_time`, `is_deleted`, `update_time`, `version`, `enable`, `resource_id`, `role_id`, `scope_type`) VALUES (269, 1697012584625, b'0', -1, 0, b'1', 6, 33, 0);
INSERT INTO `t_sys_role_resource` (`id`, `create_time`, `is_deleted`, `update_time`, `version`, `enable`, `resource_id`, `role_id`, `scope_type`) VALUES (270, 1697012586759, b'0', -1, 0, b'1', 94, 33, 0);
INSERT INTO `t_sys_role_resource` (`id`, `create_time`, `is_deleted`, `update_time`, `version`, `enable`, `resource_id`, `role_id`, `scope_type`) VALUES (271, 1697012591430, b'0', -1, 0, b'1', 93, 33, 0);
INSERT INTO `t_sys_role_resource` (`id`, `create_time`, `is_deleted`, `update_time`, `version`, `enable`, `resource_id`, `role_id`, `scope_type`) VALUES (272, 1697012604380, b'0', -1, 0, b'1', 8, 33, 0);
INSERT INTO `t_sys_role_resource` (`id`, `create_time`, `is_deleted`, `update_time`, `version`, `enable`, `resource_id`, `role_id`, `scope_type`) VALUES (273, 1697012607478, b'0', -1, 0, b'1', 113, 33, 0);
INSERT INTO `t_sys_role_resource` (`id`, `create_time`, `is_deleted`, `update_time`, `version`, `enable`, `resource_id`, `role_id`, `scope_type`) VALUES (274, 1697012615745, b'0', -1, 0, b'1', 114, 33, 0);
INSERT INTO `t_sys_role_resource` (`id`, `create_time`, `is_deleted`, `update_time`, `version`, `enable`, `resource_id`, `role_id`, `scope_type`) VALUES (275, 1697012624880, b'0', -1, 0, b'1', 112, 33, 0);
INSERT INTO `t_sys_role_resource` (`id`, `create_time`, `is_deleted`, `update_time`, `version`, `enable`, `resource_id`, `role_id`, `scope_type`) VALUES (276, 1697012625244, b'0', -1, 0, b'1', 98, 33, 0);
INSERT INTO `t_sys_role_resource` (`id`, `create_time`, `is_deleted`, `update_time`, `version`, `enable`, `resource_id`, `role_id`, `scope_type`) VALUES (277, 1697012625743, b'0', -1, 0, b'1', 111, 33, 0);
INSERT INTO `t_sys_role_resource` (`id`, `create_time`, `is_deleted`, `update_time`, `version`, `enable`, `resource_id`, `role_id`, `scope_type`) VALUES (278, 1697012626327, b'0', 1697014472042, 1, b'0', 97, 33, 0);
INSERT INTO `t_sys_role_resource` (`id`, `create_time`, `is_deleted`, `update_time`, `version`, `enable`, `resource_id`, `role_id`, `scope_type`) VALUES (279, 1697013684324, b'0', -1, 0, b'1', 1, 33, 0);
INSERT INTO `t_sys_role_resource` (`id`, `create_time`, `is_deleted`, `update_time`, `version`, `enable`, `resource_id`, `role_id`, `scope_type`) VALUES (280, 1697013692885, b'0', 1697104822870, 1, b'0', 1, 34, 0);
INSERT INTO `t_sys_role_resource` (`id`, `create_time`, `is_deleted`, `update_time`, `version`, `enable`, `resource_id`, `role_id`, `scope_type`) VALUES (281, 1697013700274, b'0', -1, 0, b'1', 115, 34, 0);
INSERT INTO `t_sys_role_resource` (`id`, `create_time`, `is_deleted`, `update_time`, `version`, `enable`, `resource_id`, `role_id`, `scope_type`) VALUES (282, 1697014390984, b'0', -1, 0, b'1', 115, 40, 0);
INSERT INTO `t_sys_role_resource` (`id`, `create_time`, `is_deleted`, `update_time`, `version`, `enable`, `resource_id`, `role_id`, `scope_type`) VALUES (283, 1697014392038, b'0', -1, 0, b'1', 117, 40, 0);
INSERT INTO `t_sys_role_resource` (`id`, `create_time`, `is_deleted`, `update_time`, `version`, `enable`, `resource_id`, `role_id`, `scope_type`) VALUES (284, 1697014400010, b'0', -1, 0, b'1', 135, 40, 0);
INSERT INTO `t_sys_role_resource` (`id`, `create_time`, `is_deleted`, `update_time`, `version`, `enable`, `resource_id`, `role_id`, `scope_type`) VALUES (285, 1697014401598, b'0', -1, 0, b'1', 119, 40, 0);
INSERT INTO `t_sys_role_resource` (`id`, `create_time`, `is_deleted`, `update_time`, `version`, `enable`, `resource_id`, `role_id`, `scope_type`) VALUES (286, 1697014402194, b'0', -1, 0, b'1', 120, 40, 0);
INSERT INTO `t_sys_role_resource` (`id`, `create_time`, `is_deleted`, `update_time`, `version`, `enable`, `resource_id`, `role_id`, `scope_type`) VALUES (287, 1697014403150, b'0', -1, 0, b'1', 122, 40, 0);
INSERT INTO `t_sys_role_resource` (`id`, `create_time`, `is_deleted`, `update_time`, `version`, `enable`, `resource_id`, `role_id`, `scope_type`) VALUES (288, 1697014403536, b'0', -1, 0, b'1', 123, 40, 0);
INSERT INTO `t_sys_role_resource` (`id`, `create_time`, `is_deleted`, `update_time`, `version`, `enable`, `resource_id`, `role_id`, `scope_type`) VALUES (289, 1697014404405, b'0', -1, 0, b'1', 124, 40, 0);
INSERT INTO `t_sys_role_resource` (`id`, `create_time`, `is_deleted`, `update_time`, `version`, `enable`, `resource_id`, `role_id`, `scope_type`) VALUES (290, 1697014405028, b'0', 1697117566385, 2, b'1', 125, 40, 0);
INSERT INTO `t_sys_role_resource` (`id`, `create_time`, `is_deleted`, `update_time`, `version`, `enable`, `resource_id`, `role_id`, `scope_type`) VALUES (291, 1697014409615, b'0', -1, 0, b'1', 116, 40, 0);
INSERT INTO `t_sys_role_resource` (`id`, `create_time`, `is_deleted`, `update_time`, `version`, `enable`, `resource_id`, `role_id`, `scope_type`) VALUES (292, 1697014414458, b'0', -1, 0, b'1', 116, 34, 0);
INSERT INTO `t_sys_role_resource` (`id`, `create_time`, `is_deleted`, `update_time`, `version`, `enable`, `resource_id`, `role_id`, `scope_type`) VALUES (293, 1697015034732, b'0', -1, 0, b'1', 132, 33, 0);
INSERT INTO `t_sys_role_resource` (`id`, `create_time`, `is_deleted`, `update_time`, `version`, `enable`, `resource_id`, `role_id`, `scope_type`) VALUES (294, 1697080396166, b'0', -1, 0, b'1', 138, 33, 0);
INSERT INTO `t_sys_role_resource` (`id`, `create_time`, `is_deleted`, `update_time`, `version`, `enable`, `resource_id`, `role_id`, `scope_type`) VALUES (295, 1697080396822, b'0', -1, 0, b'1', 139, 33, 0);
INSERT INTO `t_sys_role_resource` (`id`, `create_time`, `is_deleted`, `update_time`, `version`, `enable`, `resource_id`, `role_id`, `scope_type`) VALUES (296, 1697080397343, b'0', -1, 0, b'1', 140, 33, 0);
INSERT INTO `t_sys_role_resource` (`id`, `create_time`, `is_deleted`, `update_time`, `version`, `enable`, `resource_id`, `role_id`, `scope_type`) VALUES (297, 1697080398047, b'0', -1, 0, b'1', 141, 33, 0);
INSERT INTO `t_sys_role_resource` (`id`, `create_time`, `is_deleted`, `update_time`, `version`, `enable`, `resource_id`, `role_id`, `scope_type`) VALUES (298, 1697080398609, b'0', -1, 0, b'1', 142, 33, 0);
INSERT INTO `t_sys_role_resource` (`id`, `create_time`, `is_deleted`, `update_time`, `version`, `enable`, `resource_id`, `role_id`, `scope_type`) VALUES (299, 1697080398985, b'0', -1, 0, b'1', 143, 33, 0);
INSERT INTO `t_sys_role_resource` (`id`, `create_time`, `is_deleted`, `update_time`, `version`, `enable`, `resource_id`, `role_id`, `scope_type`) VALUES (300, 1697080399804, b'0', -1, 0, b'1', 137, 33, 0);
INSERT INTO `t_sys_role_resource` (`id`, `create_time`, `is_deleted`, `update_time`, `version`, `enable`, `resource_id`, `role_id`, `scope_type`) VALUES (301, 1697080424605, b'0', -1, 0, b'1', 138, 34, 0);
INSERT INTO `t_sys_role_resource` (`id`, `create_time`, `is_deleted`, `update_time`, `version`, `enable`, `resource_id`, `role_id`, `scope_type`) VALUES (302, 1697080426151, b'0', -1, 0, b'1', 140, 34, 0);
INSERT INTO `t_sys_role_resource` (`id`, `create_time`, `is_deleted`, `update_time`, `version`, `enable`, `resource_id`, `role_id`, `scope_type`) VALUES (303, 1697080427082, b'0', -1, 0, b'1', 141, 34, 0);
INSERT INTO `t_sys_role_resource` (`id`, `create_time`, `is_deleted`, `update_time`, `version`, `enable`, `resource_id`, `role_id`, `scope_type`) VALUES (304, 1697080427606, b'0', -1, 0, b'1', 142, 34, 0);
INSERT INTO `t_sys_role_resource` (`id`, `create_time`, `is_deleted`, `update_time`, `version`, `enable`, `resource_id`, `role_id`, `scope_type`) VALUES (305, 1697080428270, b'0', -1, 0, b'1', 143, 34, 0);
INSERT INTO `t_sys_role_resource` (`id`, `create_time`, `is_deleted`, `update_time`, `version`, `enable`, `resource_id`, `role_id`, `scope_type`) VALUES (306, 1697080429081, b'0', -1, 0, b'1', 139, 34, 0);
INSERT INTO `t_sys_role_resource` (`id`, `create_time`, `is_deleted`, `update_time`, `version`, `enable`, `resource_id`, `role_id`, `scope_type`) VALUES (307, 1697117570607, b'0', -1, 0, b'1', 134, 40, 0);
COMMIT;

-- ----------------------------
-- Table structure for t_sys_role_user
-- ----------------------------
DROP TABLE IF EXISTS `t_sys_role_user`;
CREATE TABLE `t_sys_role_user` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `create_time` bigint DEFAULT NULL,
  `is_deleted` bit(1) NOT NULL,
  `update_time` bigint DEFAULT NULL,
  `version` int NOT NULL,
  `enable` bit(1) NOT NULL DEFAULT b'1' COMMENT '是否有效 1有效 0无效',
  `role_id` bigint NOT NULL COMMENT '角色Id',
  `user_id` bigint NOT NULL COMMENT '用户Id',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `UKfqp4aub5yw4ngdc19ua0s1o6e` (`user_id`,`role_id`),
  UNIQUE KEY `UK3fbre22wvnj4j2kmv2ylfag96` (`user_id`,`role_id`),
  KEY `IDXewcat6erakmxo7sxlg6ryxd8h` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5554 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ----------------------------
-- Records of t_sys_role_user
-- ----------------------------
BEGIN;
INSERT INTO `t_sys_role_user` (`id`, `create_time`, `is_deleted`, `update_time`, `version`, `enable`, `role_id`, `user_id`) VALUES (1, 1662114693200, b'0', 1695806549675, 2, b'1', 1, 2);
INSERT INTO `t_sys_role_user` (`id`, `create_time`, `is_deleted`, `update_time`, `version`, `enable`, `role_id`, `user_id`) VALUES (2, 1662119366894, b'0', NULL, 0, b'1', 2, 3);
INSERT INTO `t_sys_role_user` (`id`, `create_time`, `is_deleted`, `update_time`, `version`, `enable`, `role_id`, `user_id`) VALUES (3, 1662119369762, b'0', 1697012662497, 3, b'0', 3, 5);
INSERT INTO `t_sys_role_user` (`id`, `create_time`, `is_deleted`, `update_time`, `version`, `enable`, `role_id`, `user_id`) VALUES (184, 1667552247662, b'0', NULL, 0, b'1', 1, 1);
INSERT INTO `t_sys_role_user` (`id`, `create_time`, `is_deleted`, `update_time`, `version`, `enable`, `role_id`, `user_id`) VALUES (185, 1667805237601, b'0', 1667805238993, 1, b'0', 2, 8);
INSERT INTO `t_sys_role_user` (`id`, `create_time`, `is_deleted`, `update_time`, `version`, `enable`, `role_id`, `user_id`) VALUES (5535, 1695806541371, b'0', 1695806550446, 1, b'0', 2, 2);
INSERT INTO `t_sys_role_user` (`id`, `create_time`, `is_deleted`, `update_time`, `version`, `enable`, `role_id`, `user_id`) VALUES (5536, 1695806607888, b'0', NULL, 0, b'1', 3, 4);
INSERT INTO `t_sys_role_user` (`id`, `create_time`, `is_deleted`, `update_time`, `version`, `enable`, `role_id`, `user_id`) VALUES (5540, 1696667377720, b'0', 1696667510414, 1, b'0', 3, 1);
INSERT INTO `t_sys_role_user` (`id`, `create_time`, `is_deleted`, `update_time`, `version`, `enable`, `role_id`, `user_id`) VALUES (5541, 1696667377732, b'0', 1696667510430, 1, b'0', 3, 6);
INSERT INTO `t_sys_role_user` (`id`, `create_time`, `is_deleted`, `update_time`, `version`, `enable`, `role_id`, `user_id`) VALUES (5548, 1696868444218, b'0', NULL, 0, b'1', 33, 5);
INSERT INTO `t_sys_role_user` (`id`, `create_time`, `is_deleted`, `update_time`, `version`, `enable`, `role_id`, `user_id`) VALUES (5549, 1696868453092, b'0', 1697008931347, 4, b'1', 34, 6);
INSERT INTO `t_sys_role_user` (`id`, `create_time`, `is_deleted`, `update_time`, `version`, `enable`, `role_id`, `user_id`) VALUES (5550, 1696868457654, b'0', NULL, 0, b'1', 34, 7);
INSERT INTO `t_sys_role_user` (`id`, `create_time`, `is_deleted`, `update_time`, `version`, `enable`, `role_id`, `user_id`) VALUES (5552, 1697004873926, b'0', 1697104761037, 1, b'0', 33, 6);
INSERT INTO `t_sys_role_user` (`id`, `create_time`, `is_deleted`, `update_time`, `version`, `enable`, `role_id`, `user_id`) VALUES (5553, 1697014380311, b'0', -1, 0, b'1', 40, 7);
COMMIT;

-- ----------------------------
-- Table structure for t_sys_user
-- ----------------------------
DROP TABLE IF EXISTS `t_sys_user`;
CREATE TABLE `t_sys_user` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `create_time` bigint DEFAULT NULL,
  `is_deleted` bit(1) NOT NULL,
  `update_time` bigint DEFAULT NULL,
  `version` int NOT NULL,
  `avatar` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '头像',
  `creator` bigint DEFAULT NULL COMMENT '创建人',
  `modifier` bigint DEFAULT NULL COMMENT '最后更新人',
  `name` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '姓名',
  `password` varchar(128) COLLATE utf8mb4_general_ci NOT NULL COMMENT '密码',
  `state` tinyint DEFAULT '0' COMMENT '用户状态 0正常1冻结',
  `type` tinyint NOT NULL DEFAULT '1' COMMENT '用户类型 1业务用户 9管理账号',
  `update_password_time` bigint DEFAULT '0' COMMENT '密码更新时间',
  `username` varchar(64) COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户名',
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_l9ny6wux3mnbiqy1bnb0rvsj7` (`username`),
  UNIQUE KEY `UKl9ny6wux3mnbiqy1bnb0rvsj7` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ----------------------------
-- Records of t_sys_user
-- ----------------------------
BEGIN;
INSERT INTO `t_sys_user` (`id`, `create_time`, `is_deleted`, `update_time`, `version`, `avatar`, `creator`, `modifier`, `name`, `password`, `state`, `type`, `update_password_time`, `username`) VALUES (1, 1627114331592, b'0', 1695457080665, 30, NULL, 1, 1, '管理员', '0eca69f3bc93d40be3f5615c2e759a7ae782f6b9fcb50793f7fb627ffd05c772', 0, 9, 1695457080632, 'sadmin');
INSERT INTO `t_sys_user` (`id`, `create_time`, `is_deleted`, `update_time`, `version`, `avatar`, `creator`, `modifier`, `name`, `password`, `state`, `type`, `update_password_time`, `username`) VALUES (2, 1695806417472, b'0', NULL, 0, NULL, NULL, NULL, '系统管理员', 'efecffb8f5fbbd80e3adb3d6fbe4d9ee35921def8d6c081033527d486f8c828a', 0, 9, NULL, '_sys_admin');
INSERT INTO `t_sys_user` (`id`, `create_time`, `is_deleted`, `update_time`, `version`, `avatar`, `creator`, `modifier`, `name`, `password`, `state`, `type`, `update_password_time`, `username`) VALUES (3, 1695806538425, b'0', NULL, 0, NULL, NULL, NULL, '安全保密管理员', 'efecffb8f5fbbd80e3adb3d6fbe4d9ee35921def8d6c081033527d486f8c828a', 0, 9, NULL, '_sec_admin');
INSERT INTO `t_sys_user` (`id`, `create_time`, `is_deleted`, `update_time`, `version`, `avatar`, `creator`, `modifier`, `name`, `password`, `state`, `type`, `update_password_time`, `username`) VALUES (4, 1695806603714, b'0', NULL, 0, NULL, NULL, NULL, '安全审计员', 'efecffb8f5fbbd80e3adb3d6fbe4d9ee35921def8d6c081033527d486f8c828a', 0, 9, NULL, '_audit_admin');
INSERT INTO `t_sys_user` (`id`, `create_time`, `is_deleted`, `update_time`, `version`, `avatar`, `creator`, `modifier`, `name`, `password`, `state`, `type`, `update_password_time`, `username`) VALUES (5, 1695808520212, b'0', 1695809990460, 1, NULL, NULL, 5, '测试老师1', '0eca69f3bc93d40be3f5615c2e759a7ae782f6b9fcb50793f7fb627ffd05c772', 0, 1, 1695809990432, '202300001');
INSERT INTO `t_sys_user` (`id`, `create_time`, `is_deleted`, `update_time`, `version`, `avatar`, `creator`, `modifier`, `name`, `password`, `state`, `type`, `update_password_time`, `username`) VALUES (6, 1695808543756, b'0', 1695808569046, 1, NULL, NULL, NULL, '测试老师 2', '0eca69f3bc93d40be3f5615c2e759a7ae782f6b9fcb50793f7fb627ffd05c772', 0, 1, 1695809990432, '202300002');
INSERT INTO `t_sys_user` (`id`, `create_time`, `is_deleted`, `update_time`, `version`, `avatar`, `creator`, `modifier`, `name`, `password`, `state`, `type`, `update_password_time`, `username`) VALUES (7, 1695808562288, b'0', 1697182877754, 2, '', NULL, 5, ' 测试老师 3', '0ed662f1468afc8e9e369f4c214d1941f223b28f4da7cfb8b2c62e94b1bf8ddb', 0, 1, 0, '202300003');
INSERT INTO `t_sys_user` (`id`, `create_time`, `is_deleted`, `update_time`, `version`, `avatar`, `creator`, `modifier`, `name`, `password`, `state`, `type`, `update_password_time`, `username`) VALUES (8, 1696923789662, b'0', 1696924068428, 1, '', 1, 1, '测试4', 'c811196bffff0b97d872a2c1e8ee958a3e0f5dba03e88ce036024e6f4bc4d85b', 0, 1, -1, '202300004');
INSERT INTO `t_sys_user` (`id`, `create_time`, `is_deleted`, `update_time`, `version`, `avatar`, `creator`, `modifier`, `name`, `password`, `state`, `type`, `update_password_time`, `username`) VALUES (9, 1697183126632, b'0', 1697183162991, 1, '', 1, 9, '测试5', '0eca69f3bc93d40be3f5615c2e759a7ae782f6b9fcb50793f7fb627ffd05c772', 0, 1, 1697183162990, '202300005');
COMMIT;

-- ----------------------------
-- Table structure for t_sys_user_data_purview
-- ----------------------------
DROP TABLE IF EXISTS `t_sys_user_data_purview`;
CREATE TABLE `t_sys_user_data_purview` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `create_time` bigint NOT NULL,
  `is_deleted` bit(1) NOT NULL,
  `update_time` bigint NOT NULL,
  `version` int NOT NULL,
  `enable` tinyint NOT NULL DEFAULT '1' COMMENT '状态（1正常0冻结）',
  `ids` text COLLATE utf8mb4_general_ci NOT NULL COMMENT '授权数据对象范围id，以,分割',
  `purview_id` bigint NOT NULL COMMENT '授权级别ID',
  `resource_id` bigint NOT NULL COMMENT '资源id',
  `role_id` bigint NOT NULL COMMENT '角色id',
  `user_id` bigint NOT NULL COMMENT '账号',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ----------------------------
-- Records of t_sys_user_data_purview
-- ----------------------------
BEGIN;
INSERT INTO `t_sys_user_data_purview` (`id`, `create_time`, `is_deleted`, `update_time`, `version`, `enable`, `ids`, `purview_id`, `resource_id`, `role_id`, `user_id`) VALUES (4, 1696991254310, b'0', 1697014238920, 3, 1, '33,34,33,34,39', 3, 7, 33, 5);
INSERT INTO `t_sys_user_data_purview` (`id`, `create_time`, `is_deleted`, `update_time`, `version`, `enable`, `ids`, `purview_id`, `resource_id`, `role_id`, `user_id`) VALUES (5, 1696993261567, b'0', 1697010599941, 2, 1, '37', 3, 7, 34, 7);
INSERT INTO `t_sys_user_data_purview` (`id`, `create_time`, `is_deleted`, `update_time`, `version`, `enable`, `ids`, `purview_id`, `resource_id`, `role_id`, `user_id`) VALUES (7, 1697004373945, b'0', -1, 0, 1, '1111', 6, 129, 34, 7);
INSERT INTO `t_sys_user_data_purview` (`id`, `create_time`, `is_deleted`, `update_time`, `version`, `enable`, `ids`, `purview_id`, `resource_id`, `role_id`, `user_id`) VALUES (8, 1697008940045, b'0', -1, 0, 1, '2222', 6, 129, 34, 6);
INSERT INTO `t_sys_user_data_purview` (`id`, `create_time`, `is_deleted`, `update_time`, `version`, `enable`, `ids`, `purview_id`, `resource_id`, `role_id`, `user_id`) VALUES (9, 1697009114563, b'0', 1697010591285, 1, 1, '', 3, 7, 34, 6);
INSERT INTO `t_sys_user_data_purview` (`id`, `create_time`, `is_deleted`, `update_time`, `version`, `enable`, `ids`, `purview_id`, `resource_id`, `role_id`, `user_id`) VALUES (10, 1697010830222, b'0', -1, 0, 1, '1111', 6, 132, 34, 6);
INSERT INTO `t_sys_user_data_purview` (`id`, `create_time`, `is_deleted`, `update_time`, `version`, `enable`, `ids`, `purview_id`, `resource_id`, `role_id`, `user_id`) VALUES (11, 1697010840941, b'0', -1, 0, 1, '1111,2222', 6, 132, 34, 7);
INSERT INTO `t_sys_user_data_purview` (`id`, `create_time`, `is_deleted`, `update_time`, `version`, `enable`, `ids`, `purview_id`, `resource_id`, `role_id`, `user_id`) VALUES (12, 1697016726727, b'0', -1, 0, 1, '', 2, 132, 33, 5);
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
