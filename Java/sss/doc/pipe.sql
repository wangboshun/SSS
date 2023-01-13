/*
 Navicat Premium Data Transfer

 Source Server         : mysql-123.60.141.63
 Source Server Type    : MySQL
 Source Server Version : 80031 (8.0.31)
 Source Host           : 123.60.141.63:3306
 Source Schema         : wbs

 Target Server Type    : MySQL
 Target Server Version : 80031 (8.0.31)
 File Encoding         : 65001

 Date: 12/01/2023 17:06:58
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for pipe_column_config
-- ----------------------------
DROP TABLE IF EXISTS `pipe_column_config`;
CREATE TABLE `pipe_column_config`  (
  `id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `task_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '任务id',
  `sink_column` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '目标字段',
  `sink_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `source_column` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '源字段',
  `source_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `default_value` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '默认值',
  `column_status` int NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of pipe_column_config
-- ----------------------------
INSERT INTO `pipe_column_config` VALUES ('1', '3', 'STCD', NULL, 'STCD', NULL, NULL, NULL, '2022-11-15 17:26:29');
INSERT INTO `pipe_column_config` VALUES ('2', '3', 'DRP', NULL, 'DRP', NULL, NULL, NULL, '2022-11-15 17:27:30');
INSERT INTO `pipe_column_config` VALUES ('3', '3', 'TM', NULL, 'TM', NULL, NULL, NULL, '2022-11-15 17:27:30');
INSERT INTO `pipe_column_config` VALUES ('4', '4', 'STCD', NULL, 'STCD', NULL, NULL, NULL, '2022-11-15 17:26:29');
INSERT INTO `pipe_column_config` VALUES ('5', '4', 'DRP', NULL, 'DRP', NULL, NULL, NULL, '2022-11-15 17:27:30');
INSERT INTO `pipe_column_config` VALUES ('6', '4', 'TM', NULL, 'TM', NULL, NULL, NULL, '2022-11-15 17:27:30');

-- ----------------------------
-- Table structure for pipe_connect_config
-- ----------------------------
DROP TABLE IF EXISTS `pipe_connect_config`;
CREATE TABLE `pipe_connect_config`  (
  `id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `connect_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '连接名',
  `host` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '主机',
  `port` int NULL DEFAULT NULL COMMENT '端口',
  `username` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户',
  `password` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '密码',
  `db_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '数据库名',
  `db_schema` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '数据库模式，pgsql会用到',
  `db_type` int NULL DEFAULT NULL COMMENT '数据类型：MySQL(0)、MsSQL(1)',
  `create_time` datetime NULL DEFAULT NULL,
  `connect_status` int NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of pipe_connect_config
-- ----------------------------
INSERT INTO `pipe_connect_config` VALUES ('1', 'test1', '123.60.141.63', 3306, 'root', 'mima123456', 'test1', NULL, 0, '2022-10-19 14:34:50', NULL);
INSERT INTO `pipe_connect_config` VALUES ('2', 'wbs', '127.0.0.1', 1433, 'sa', 'mima123456', 'wbs', NULL, 1, '2022-10-19 14:35:12', NULL);
INSERT INTO `pipe_connect_config` VALUES ('3', 'test2', '123.60.141.63', 3306, 'root', 'mima123456', 'test2', NULL, 0, '2022-10-19 14:35:12', NULL);
INSERT INTO `pipe_connect_config` VALUES ('4', 'postgres1', '123.60.141.63', 5432, 'postgres', 'mima123456', 'test1', 'public', 2, '2022-10-19 14:35:12', NULL);
INSERT INTO `pipe_connect_config` VALUES ('5', 'clickhouse1', '123.60.141.63', 8123, 'default', 'mima123456', 'test1', NULL, 3, '2022-10-19 14:35:12', NULL);

-- ----------------------------
-- Table structure for pipe_convert_config
-- ----------------------------
DROP TABLE IF EXISTS `pipe_convert_config`;
CREATE TABLE `pipe_convert_config`  (
  `id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `task_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '任务id',
  `convert_column` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '转换列',
  `convert_value` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '待转换值',
  `convert_symbol` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '转换符合',
  `convert_number` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '转换值',
  `convert_index` int NULL DEFAULT NULL COMMENT '转换次序',
  `create_time` datetime NULL DEFAULT NULL,
  `convert_status` int NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of pipe_convert_config
-- ----------------------------

-- ----------------------------
-- Table structure for pipe_filter_config
-- ----------------------------
DROP TABLE IF EXISTS `pipe_filter_config`;
CREATE TABLE `pipe_filter_config`  (
  `id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `task_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '任务id',
  `filter_column` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '过滤列',
  `filter_symbol` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '过滤符合',
  `filter_type` varchar(5) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '过滤类型',
  `use_type` int NULL DEFAULT NULL COMMENT '使用场景0为数据筛选、1为转换筛选',
  `filter_value` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '筛选值',
  `create_time` datetime NULL DEFAULT NULL,
  `filter_status` int NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of pipe_filter_config
-- ----------------------------

-- ----------------------------
-- Table structure for pipe_sink_config
-- ----------------------------
DROP TABLE IF EXISTS `pipe_sink_config`;
CREATE TABLE `pipe_sink_config`  (
  `id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `sink_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '目的名称',
  `connect_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '连接id',
  `create_time` datetime NULL DEFAULT NULL,
  `sink_status` int NULL DEFAULT NULL,
  `table_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '表名',
  `primary_column` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '主键字段',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of pipe_sink_config
-- ----------------------------
INSERT INTO `pipe_sink_config` VALUES ('1', 'test2', '3', '2022-10-19 14:37:08', NULL, 'ST_PPTN_R', 'STCD,TM');
INSERT INTO `pipe_sink_config` VALUES ('2', 'clickhouse1', '5', '2022-10-19 14:37:08', NULL, 'ST_PPTN_R', 'STCD,TM');
INSERT INTO `pipe_sink_config` VALUES ('3', 'postgresql', '4', '2022-10-19 14:37:08', NULL, 'ST_PPTN_R', 'STCD,TM');

-- ----------------------------
-- Table structure for pipe_source_config
-- ----------------------------
DROP TABLE IF EXISTS `pipe_source_config`;
CREATE TABLE `pipe_source_config`  (
  `id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `source_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '源名称',
  `connect_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '连接id',
  `create_time` datetime NULL DEFAULT NULL,
  `source_status` int NULL DEFAULT NULL,
  `table_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '表名',
  `primary_column` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '主键字段',
  `time_column` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '时间字段',
  `wrtm_column` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '写入字段',
  `order_type` int NULL DEFAULT NULL COMMENT '排序类型：0为asc、1为desc',
  `order_column` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '排序字段',
  `get_type` int NULL DEFAULT NULL COMMENT '获取数据的方式： 0为按wrtm获取、1为按数据时间获取',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of pipe_source_config
-- ----------------------------
INSERT INTO `pipe_source_config` VALUES ('1', 'test1', '1', '2022-10-19 14:38:52', NULL, 'ST_PPTN_R', 'STCD,TM', 'TM', '', 1, 'TM', 1);
INSERT INTO `pipe_source_config` VALUES ('2', 'clickhouse1', '5', '2022-10-19 14:38:52', NULL, 'ST_PPTN_R', 'STCD,TM', 'TM', '', 1, 'TM', 1);
INSERT INTO `pipe_source_config` VALUES ('3', 'postgresql', '4', '2022-10-19 14:37:08', NULL, 'ST_PPTN_R', 'STCD,TM', 'TM', NULL, 1, 'TM', 1);
INSERT INTO `pipe_source_config` VALUES ('4', 'test2', '3', '2022-10-19 14:38:52', NULL, 'ST_PPTN_R', 'STCD,TM', 'TM', '', 1, 'TM', 1);

-- ----------------------------
-- Table structure for pipe_table_config
-- ----------------------------
DROP TABLE IF EXISTS `pipe_table_config`;
CREATE TABLE `pipe_table_config`  (
  `id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `connect_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '连接id',
  `table_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '表名',
  `column_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '字段名',
  `java_type` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'java数据类型',
  `db_type` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '数据库类型',
  `is_primary` int NULL DEFAULT NULL COMMENT '是否主键',
  `is_null` int NULL DEFAULT NULL COMMENT '是否为空',
  `create_time` datetime NULL DEFAULT NULL,
  `table_status` int NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of pipe_table_config
-- ----------------------------

-- ----------------------------
-- Table structure for pipe_task_config
-- ----------------------------
DROP TABLE IF EXISTS `pipe_task_config`;
CREATE TABLE `pipe_task_config`  (
  `id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `task_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '任务名',
  `sink_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '目的id',
  `sink_type` int NULL DEFAULT NULL COMMENT '目的类型',
  `source_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '源id',
  `source_type` int NULL DEFAULT NULL COMMENT '源类型',
  `start_time` datetime NULL DEFAULT NULL COMMENT '开始时间',
  `end_time` datetime NULL DEFAULT NULL COMMENT '结束时间',
  `time_step` int NULL DEFAULT NULL COMMENT '时间步长',
  `insert_type` int NULL DEFAULT NULL COMMENT '插入方式：0存在跳过  1存在更新',
  `create_time` datetime NULL DEFAULT NULL,
  `task_status` int NULL DEFAULT NULL,
  `where_param` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'where条件',
  `execute_type` int NULL DEFAULT NULL COMMENT '任务执行类型：0为间隔时间、1为cron',
  `execute_param` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '任务执行参数：0为间隔时间，单位秒、1为cron表达式',
  `add_type` int NULL DEFAULT NULL COMMENT '新增方式：0为增量、1为全量',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of pipe_task_config
-- ----------------------------
INSERT INTO `pipe_task_config` VALUES ('1', 'ceshi', '1', 0, '1', 0, '2022-01-01 00:00:00', '2023-01-01 00:00:00', 5, 1, '2022-10-19 14:40:31', NULL, '', 0, '1', 1);
INSERT INTO `pipe_task_config` VALUES ('2', 'clickhouse', '2', 0, '1', 0, '2022-01-01 00:00:00', '2023-01-01 00:00:00', 5, 1, '2022-10-19 14:40:31', NULL, '', 0, '1', 1);
INSERT INTO `pipe_task_config` VALUES ('3', 'postgresql', '1', 0, '3', 2, '2000-01-01 00:00:00', '2023-01-01 00:00:00', 5, 1, '2022-10-19 14:40:31', NULL, '', 0, '1', 0);
INSERT INTO `pipe_task_config` VALUES ('4', 'mysql2', '3', 2, '4', 0, '2000-01-01 00:00:00', '2023-01-01 00:00:00', 5, 1, '2022-10-19 14:40:31', NULL, '', 0, '1', 0);

SET FOREIGN_KEY_CHECKS = 1;
