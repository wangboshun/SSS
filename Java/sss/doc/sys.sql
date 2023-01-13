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

 Date: 12/01/2023 17:07:12
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for sys_api
-- ----------------------------
DROP TABLE IF EXISTS `sys_api`;
CREATE TABLE `sys_api`  (
  `id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `api_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'api名称',
  `api_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'api路径',
  `api_status` int NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT NULL,
  `api_type` varchar(6) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'api类型',
  `api_group` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'api组名',
  `api_path` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'api包路径',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_api
-- ----------------------------
INSERT INTO `sys_api` VALUES ('0148c3b2-cda1-434f-af36-5dd13a87a6ab', '解绑源节点到角色', '/pipe/source/unbind_by_role', 1, '2022-10-19 10:10:51', 'PATCH', 'source模块', 'com.zny.pipe.controller.SourceController.unBindSourceByRole');
INSERT INTO `sys_api` VALUES ('030ea560-bc91-4e7a-b11f-ed1c3d9e52fe', '绑定菜单到角色', '/user/menu/bind_by_role', 1, '2022-09-29 17:24:03', 'PATCH', '菜单模块', 'com.zny.user.controller.MenuController.bindMenuByRole');
INSERT INTO `sys_api` VALUES ('04c6c71b-6e6a-4e98-ae75-2aacfc47b94b', '绑定目的节点到用户', '/pipe/sink/bind_by_user', 1, '2022-10-19 08:54:49', 'PATCH', 'sink模块', 'com.zny.pipe.controller.SinkController.bindSinkByUser');
INSERT INTO `sys_api` VALUES ('05f81e5d-4c97-42ff-a630-2c98a78c7c67', '获取角色列表', '/user/role/list', 1, '2022-09-29 17:24:03', 'GET', '角色模块', 'com.zny.user.controller.RoleController.list');
INSERT INTO `sys_api` VALUES ('0a0d4c12-a8e4-449e-a8fb-2227e77a2384', '根据连接获取下面所有的模式', '/pipe/db/get_schemas', 1, '2022-12-07 09:46:19', 'GET', 'db模块', 'com.zny.pipe.controller.DataBaseController.getSchemas');
INSERT INTO `sys_api` VALUES ('0c002aed-8210-4276-9cee-cfd2c4b7b23c', '添加表配置', '/pipe/table/add', 1, '2022-12-07 09:46:19', 'POST', 'db模块', 'com.zny.pipe.controller.DataBaseController.add');
INSERT INTO `sys_api` VALUES ('0cd05df4-0a9f-43a2-8f6f-3b7208c0e333', '获取权限信息', '/user/permission/{id}', 1, '2022-09-29 17:24:03', 'GET', '权限模块', 'com.zny.user.controller.PermissionController.get');
INSERT INTO `sys_api` VALUES ('0cf58d60-907a-42c5-9d8a-2d64f3bd49da', '根据角色获取权限', '/user/permission/{roleId}/role', 1, '2022-12-06 10:55:19', 'GET', '权限模块', 'com.zny.user.controller.PermissionController.getPermissionByRole');
INSERT INTO `sys_api` VALUES ('0e1df2d2-1983-49e9-9721-858e1b808d7c', '删除字段配置', '/pipe/column/{taskId}/task', 1, '2022-12-06 10:55:19', 'DELETE', 'column模块', 'com.zny.pipe.controller.ColumnController.deleteByTask');
INSERT INTO `sys_api` VALUES ('1274ea4d-6998-4f0d-a9cf-c4f13c375ccd', '根据连接获取下面所有的表名', '/pipe/db/get_tables', 1, '2022-12-07 09:46:19', 'GET', 'db模块', 'com.zny.pipe.controller.DataBaseController.getTables');
INSERT INTO `sys_api` VALUES ('1306137c-3cae-4a88-92e3-b3c8db60ad99', '根据用户获取目的节点', '/pipe/sink/{userId}/user', 1, '2022-12-06 10:55:19', 'GET', 'sink模块', 'com.zny.pipe.controller.SinkController.getSinkByUser');
INSERT INTO `sys_api` VALUES ('13517291-7682-4ac0-b303-8c4c3fd219ef', '获取链接信息', '/pipe/connect/{id}', 1, '2022-10-19 11:36:30', 'GET', 'connect模块', 'com.zny.pipe.controller.ConnectController.get');
INSERT INTO `sys_api` VALUES ('15bac2a6-eb7a-4551-ae1b-ef5a2c502ba2', '获取字段配置列表', '/pipe/column/{taskId}/task', 1, '2022-12-06 10:55:19', 'GET', 'column模块', 'com.zny.pipe.controller.ColumnController.list');
INSERT INTO `sys_api` VALUES ('16d5260f-6b8f-4f61-a4c5-fc55c4fc32ce', '解绑用户到角色', '/user/unbind_by_role', 1, '2022-09-29 17:24:03', 'PATCH', '用户模块', 'com.zny.user.controller.UserController.unBindUserByRole');
INSERT INTO `sys_api` VALUES ('17dc0730-0d2b-47f1-ae1f-9556c169f6ed', '获取任务列表', '/pipe/task/list', 1, '2022-10-19 11:36:30', 'GET', 'task模块', 'com.zny.pipe.controller.TaskController.list');
INSERT INTO `sys_api` VALUES ('1982bd89-1060-4196-af90-9e707599994a', '删除源节点', '/pipe/source/{id}', 1, '2022-10-19 10:10:51', 'DELETE', 'source模块', 'com.zny.pipe.controller.SourceController.delete');
INSERT INTO `sys_api` VALUES ('1d8eab9c-9cb9-4087-ae45-d88be6c87322', '添加资源', '/system/resource/add', 1, '2022-09-29 17:24:03', 'POST', '资源模块', 'com.zny.system.controller.ResourceController.add');
INSERT INTO `sys_api` VALUES ('1ec3a2cf-3dd8-47b5-9eb0-c970d557c0f2', '获取权限信息', '/user/permission/{id}', 1, '2022-09-29 17:24:03', 'GET', '权限模块', 'com.zny.user.controller.PermissionController.get');
INSERT INTO `sys_api` VALUES ('1f9373c8-0e8b-4969-87fd-cab0faaafadd', '获取接口列表', '/user/api/list', 1, '2022-09-29 17:24:03', 'GET', '接口模块', 'com.zny.system.controller.ApiController.list');
INSERT INTO `sys_api` VALUES ('2207a8ca-3922-4452-89ae-452f0371ae0b', '获取用户信息', '/user/{id}', 1, '2022-09-29 17:24:03', 'GET', '用户模块', 'com.zny.user.controller.UserController.get');
INSERT INTO `sys_api` VALUES ('2305835f-2373-4513-9c29-cb613e0e58d1', '删除目的节点', '/pipe/sink/{id}', 1, '2022-10-19 08:54:49', 'DELETE', 'sink模块', 'com.zny.pipe.controller.SinkController.delete');
INSERT INTO `sys_api` VALUES ('231570ad-9bc0-432a-8a52-ae463a51e039', '获取节点信息', '/pipe/sink/{id}', 1, '2022-10-19 08:54:49', 'GET', 'sink模块', 'com.zny.controller.SinkController.get');
INSERT INTO `sys_api` VALUES ('241c21e9-3090-4da4-8f57-2750fc0ab89f', '解绑菜单到角色', '/user/menu/unbind_by_role', 1, '2022-09-29 17:24:03', 'PATCH', '菜单模块', 'com.zny.user.controller.MenuController.unBindMenuByRole');
INSERT INTO `sys_api` VALUES ('249139ae-aa7a-4bb4-a29c-4b409ca54e1f', '添加链接', '/pipe/connect/add', 1, '2022-10-19 11:36:30', 'POST', 'connect模块', 'com.zny.pipe.controller.ConnectController.add');
INSERT INTO `sys_api` VALUES ('25c0c2c4-356b-479c-bb87-7bdcd220e114', '获取权限树', '/user/permission/tree', 1, '2022-09-29 17:24:03', 'GET', '权限模块', 'com.zny.user.controller.PermissionController.tree');
INSERT INTO `sys_api` VALUES ('2605f4e6-cec9-42aa-9d9e-e678b120856b', '根据用户获取菜单', '/user/menu/{userId}/user', 1, '2022-12-06 10:55:19', 'GET', '菜单模块', 'com.zny.user.controller.MenuController.getMenuByUser');
INSERT INTO `sys_api` VALUES ('27d6b5f4-4e6a-4c5d-80ca-63aa7f45342f', '添加源节点', '/pipe/source/add', 1, '2022-10-19 10:10:51', 'POST', 'source模块', 'com.zny.pipe.controller.SourceController.add');
INSERT INTO `sys_api` VALUES ('292dbaa9-b244-4697-9444-26c9aa75497c', '解绑目的节点到用户', '/pipe/sink/unbind_by_user', 1, '2022-10-19 08:54:49', 'PATCH', 'sink模块', 'com.zny.pipe.controller.SinkController.unBindSinkByUser');
INSERT INTO `sys_api` VALUES ('2a9773d7-8085-45bc-ac0f-d923dc7ca96b', '绑定api到用户', '/user/api/bind_by_user', 1, '2022-09-29 17:24:03', 'PATCH', '接口模块', 'com.zny.system.controller.ApiController.bindApiByUser');
INSERT INTO `sys_api` VALUES ('2fc969a4-36f1-4e73-84fb-0bb8dd0ce557', '删除表配置', '/pipe/table/delete', 1, '2022-12-07 09:46:19', 'DELETE', 'db模块', 'com.zny.pipe.controller.DataBaseController.delete');
INSERT INTO `sys_api` VALUES ('37452c89-4ce2-4da0-a2fe-d2395acc9302', '根据角色获取任务', '/pipe/task/{roleId}/role', 1, '2022-12-06 10:55:19', 'GET', 'task模块', 'com.zny.pipe.controller.TaskController.getTaskByRole');
INSERT INTO `sys_api` VALUES ('3a534701-173b-44fb-a102-e049cfbbf58c', '添加字段配置', '/pipe/column/add', 1, '2022-11-15 17:19:18', 'POST', 'column模块', 'com.zny.pipe.controller.ColumnController.add');
INSERT INTO `sys_api` VALUES ('3af4d286-49c4-4f87-b82e-8c387b846d66', '解绑任务到角色', '/pipe/task/unbind_by_role', 1, '2022-10-19 11:36:30', 'POST', 'task模块', 'com.zny.pipe.controller.TaskController.unBindTaskByRole');
INSERT INTO `sys_api` VALUES ('3c1a73c2-5c41-4341-8376-36b82f59583e', '更新权限信息', '/user/permission/{id}', 1, '2022-09-29 17:24:03', 'PATCH', '权限模块', 'com.zny.user.controller.PermissionController.update');
INSERT INTO `sys_api` VALUES ('3dd433c0-c702-43e1-a673-224aef380095', '绑定菜单到用户', '/user/menu/bind_by_user', 1, '2022-09-29 17:24:03', 'PATCH', '菜单模块', 'com.zny.user.controller.MenuController.bindMenuByUser');
INSERT INTO `sys_api` VALUES ('3dd7775a-f780-4e1d-a06b-35b4540ffb12', '根据用户获取源节点', '/pipe/source/{userId}/user', 1, '2022-12-06 10:55:19', 'GET', 'source模块', 'com.zny.pipe.controller.SourceController.getSourceByUser');
INSERT INTO `sys_api` VALUES ('4025e618-35a7-4be6-a6ad-6cf73096ca95', '批量删除日志', '/system/apilog/{ids}', 1, '2022-09-29 17:24:03', '', '系统模块', 'com.zny.system.controller.ApiLogController.delete');
INSERT INTO `sys_api` VALUES ('44f76b11-b910-4258-a996-ec2833094c89', '解绑目的节点到角色', '/pipe/sink/unbind_by_role', 1, '2022-10-19 08:54:49', 'PATCH', 'sink模块', 'com.zny.pipe.controller.SinkController.unBindSinkByRole');
INSERT INTO `sys_api` VALUES ('489c8058-613f-426d-a680-8082cee50d04', '更新链接信息', '/pipe/connect/{id}', 1, '2022-10-19 11:36:30', 'PATCH', 'connect模块', 'com.zny.pipe.controller.ConnectController.update');
INSERT INTO `sys_api` VALUES ('4aa36d1e-304e-4182-88c6-1384af38d531', '更新源节点信息', '/pipe/source/{id}', 1, '2022-10-19 10:10:51', 'PATCH', 'source模块', 'com.zny.pipe.controller.SourceController.update');
INSERT INTO `sys_api` VALUES ('4e9514ef-3b6b-4c7f-be68-9a68e5deced5', '获取权限列表', '/user/permission/list', 1, '2022-09-29 17:24:03', 'GET', '权限模块', 'com.zny.user.controller.PermissionController.list');
INSERT INTO `sys_api` VALUES ('512e64ad-638a-48c3-b4de-dc47ba307f23', '解绑权限到角色', '/user/permission/unbind_by_role', 1, '2022-09-29 17:24:03', 'PATCH', '权限模块', 'com.zny.user.controller.PermissionController.unBindPermissionByRole');
INSERT INTO `sys_api` VALUES ('51baa26e-f3e6-4be6-a56c-75197d92a32a', '绑定源节点到角色', '/pipe/source/bind_by_role', 1, '2022-10-19 10:10:51', 'PATCH', 'source模块', 'com.zny.pipe.controller.SourceController.bindSourceByRole');
INSERT INTO `sys_api` VALUES ('5bc91b78-05b3-4a60-a197-e705e618f904', '运行任务', '/pipe/task/run', 1, '2022-11-03 09:06:36', 'GET', 'task模块', 'com.zny.pipe.controller.TaskController.run');
INSERT INTO `sys_api` VALUES ('5c01eb4c-8fc3-4ab4-89d6-87d689bdd7e9', '绑定权限到角色', '/user/permission/bind_by_role', 1, '2022-09-29 17:24:03', 'PATCH', '权限模块', 'com.zny.user.controller.PermissionController.bindPermissionByRole');
INSERT INTO `sys_api` VALUES ('5d4aeb35-a0f8-418f-8cab-4dd8e97f56f3', '解绑源节点到用户', '/pipe/source/unbind_by_user', 1, '2022-10-19 10:10:51', 'PATCH', 'source模块', 'com.zny.pipe.controller.SourceController.unBindSourceByUser');
INSERT INTO `sys_api` VALUES ('5db5e7ff-68da-469d-89b8-f3867fceebd4', '根据角色获取链接', '/pipe/connect/{roleId}/role', 1, '2022-12-06 10:55:19', 'GET', 'connect模块', 'com.zny.pipe.controller.ConnectController.getConnectByRole');
INSERT INTO `sys_api` VALUES ('5e119191-9cc4-4ccc-a8a1-95918bf98b95', '解绑api到角色', '/user/api/unbind_by_role', 1, '2022-09-29 17:24:03', 'PATCH', '接口模块', 'com.zny.system.controller.ApiController.unBindApiByRole');
INSERT INTO `sys_api` VALUES ('690e8900-0251-483b-94dd-15ec2738b7b8', '绑定任务到用户', '/pipe/task/bind_by_user', 1, '2022-10-19 11:36:30', 'POST', 'task模块', 'com.zny.pipe.controller.TaskController.bindTaskByUser');
INSERT INTO `sys_api` VALUES ('6ac64c12-b950-48ee-9288-a7fb63f6859d', '根据角色获取源节点', '/pipe/source/{roleId}/role', 1, '2022-12-06 10:55:19', 'GET', 'source模块', 'com.zny.pipe.controller.SourceController.getSourceByRole');
INSERT INTO `sys_api` VALUES ('6b352536-3079-40f5-8c5f-6fe30355a43e', '添加目的节点', '/pipe/sink/add', 1, '2022-10-19 08:54:49', 'POST', 'sink模块', 'com.zny.pipe.controller.SinkController.add');
INSERT INTO `sys_api` VALUES ('6b83de86-0731-44e6-a38f-8bfea55cf0ff', '绑定api到角色', '/user/api/bind_by_role', 1, '2022-09-29 17:24:03', 'PATCH', '接口模块', 'com.zny.system.controller.ApiController.bindApiByRole');
INSERT INTO `sys_api` VALUES ('6d2c773c-1015-43b5-8e0a-dab896e918d4', '删除菜单', '/user/menu/{id}', 1, '2022-09-29 17:24:03', 'DELETE', '菜单模块', 'com.zny.user.controller.MenuController.delete');
INSERT INTO `sys_api` VALUES ('6d80212e-f4c8-47c8-a605-c642ff3c8ab4', '根据用户获取任务', '/pipe/task/{userId}/user', 1, '2022-12-06 10:55:19', 'GET', 'task模块', 'com.zny.pipe.controller.TaskController.getTaskByUser');
INSERT INTO `sys_api` VALUES ('6e8652e3-be4a-4c71-9ebc-90296a925e31', '查询日志', '/system/apilog/{id}', 1, '2022-09-29 17:24:03', 'GET', '系统模块', 'com.zny.system.controller.ApiLogController.get');
INSERT INTO `sys_api` VALUES ('6fa00e18-5741-4185-9606-1d19ee8cef70', '根据用户获取Api', '/user/api/{userId}/user', 1, '2022-12-06 10:55:19', 'GET', '接口模块', 'com.zny.system.controller.ApiController.getApiByUser');
INSERT INTO `sys_api` VALUES ('704a8dc8-ca89-4ed5-99d8-c444a6dce9f0', '注销', '/user/logout', 1, '2022-09-29 17:24:03', 'POST', '用户模块', 'com.zny.user.controller.UserController.logout');
INSERT INTO `sys_api` VALUES ('75bfa036-9168-46ba-beb4-17a5999ad034', '更新任务信息', '/pipe/task/{id}', 1, '2022-10-19 11:36:30', 'PATCH', 'task模块', 'com.zny.pipe.controller.TaskController.update');
INSERT INTO `sys_api` VALUES ('76898d27-e95b-494c-bb14-8df382c0128a', '获取源节点列表', '/pipe/source/list', 1, '2022-10-19 10:10:51', 'GET', 'source模块', 'com.zny.pipe.controller.SourceController.list');
INSERT INTO `sys_api` VALUES ('7718c41e-3a8d-48c0-91b0-fe9fc8d4fff9', '根据连接获取下面所有的数据库', '/pipe/db/get_dbs', 1, '2022-12-07 09:46:19', 'GET', 'db模块', 'com.zny.pipe.controller.DataBaseController.getDataBases');
INSERT INTO `sys_api` VALUES ('7cdf703c-957a-487c-bd11-6935992a5b00', '根据角色获取用户', '/user/{roleId}/role', 1, '2022-12-06 10:55:19', 'GET', '用户模块', 'com.zny.user.controller.UserController.getUserByRole');
INSERT INTO `sys_api` VALUES ('7ec6cbe3-570a-4bc8-8dc6-56a3ec090fe1', '获取用户信息', '/user/{id}', 1, '2022-09-29 17:24:03', 'GET', '用户模块', 'com.zny.user.controller.UserController.get');
INSERT INTO `sys_api` VALUES ('7ed1a713-0d98-4d4d-a629-2d1089569cf3', '更新源节点信息', '/pipe/source/update', 1, '2022-11-24 14:52:37', 'PATCH', 'source模块', 'com.zny.pipe.controller.SourceController.update');
INSERT INTO `sys_api` VALUES ('8419da70-7e1b-4177-87d5-89c36702985a', '获取资源信息', '/system/resource/{id}', 1, '2022-09-29 17:24:03', 'GET', '资源模块', 'com.zny.system.controller.ResourceController.get');
INSERT INTO `sys_api` VALUES ('885af1b2-0f96-48bf-bde2-480c556c3c5a', '删除节点', '/pipe/sink/{id}', 1, '2022-10-19 08:54:49', 'DELETE', 'sink模块', 'com.zny.controller.SinkController.delete');
INSERT INTO `sys_api` VALUES ('8b27ac7e-b1d3-4b65-84df-5d1b5bbcf8ac', '获取菜单信息', '/user/menu/{id}', 1, '2022-09-29 17:24:03', 'GET', '菜单模块', 'com.zny.user.controller.MenuController.get');
INSERT INTO `sys_api` VALUES ('922778db-1b3c-4763-9b39-2bdfebb4b5e7', '绑定权限到用户', '/user/permission/bind_by_user', 1, '2022-09-29 17:24:03', 'PATCH', '权限模块', 'com.zny.user.controller.PermissionController.bindPermissionByUser');
INSERT INTO `sys_api` VALUES ('9306a67b-97a6-4445-a87b-6fdeef0ad91a', '根据用户获取权限', '/user/permission/{userId}/user', 1, '2022-12-06 10:55:19', 'GET', '权限模块', 'com.zny.user.controller.PermissionController.getPermissionByUser');
INSERT INTO `sys_api` VALUES ('93659873-7cdb-4961-8981-acc315f508c4', '根据角色获取目的节点', '/pipe/sink/{roleId}/role', 1, '2022-12-06 10:55:19', 'GET', 'sink模块', 'com.zny.pipe.controller.SinkController.getSinkByRole');
INSERT INTO `sys_api` VALUES ('959ddf8e-4661-412f-8863-ba6129768d8d', '获取资源列表', '/system/resource/list', 1, '2022-09-29 17:24:03', 'GET', '资源模块', 'com.zny.system.controller.ResourceController.list');
INSERT INTO `sys_api` VALUES ('970b2d8e-fa84-404d-b3ce-a198c88ad108', '绑定任务到角色', '/pipe/task/bind_by_role', 1, '2022-10-19 11:36:30', 'POST', 'task模块', 'com.zny.pipe.controller.TaskController.bindTaskByRole');
INSERT INTO `sys_api` VALUES ('97e57c2f-4515-4a6e-a937-e07e6bbdea87', '获取角色树', '/user/role/tree', 1, '2022-09-29 17:24:03', 'GET', '角色模块', 'com.zny.user.controller.RoleController.tree');
INSERT INTO `sys_api` VALUES ('984f8787-cda5-4fc4-87d7-9b1d517cfcf8', '获取接口信息', '/user/api/{id}', 1, '2022-09-29 17:24:03', 'GET', '接口模块', 'com.zny.system.controller.ApiController.get');
INSERT INTO `sys_api` VALUES ('990baf56-b9ba-4ffe-b7c9-2ba4a6068318', '解绑链接到用户', '/pipe/connect/unbind_by_user', 1, '2022-10-19 11:36:30', 'PATCH', 'connect模块', 'com.zny.pipe.controller.ConnectController.unBindConnectByUser');
INSERT INTO `sys_api` VALUES ('9ff24f6f-c474-4e26-baf9-b663d7b93585', '添加角色', '/user/role/add', 1, '2022-09-29 17:24:03', 'POST', '角色模块', 'com.zny.user.controller.RoleController.add');
INSERT INTO `sys_api` VALUES ('a0d59b56-2fc8-4c33-ae5d-d7248bc3f3ec', '更新角色信息', '/user/role/{id}', 1, '2022-09-29 17:24:03', 'PATCH', '角色模块', 'com.zny.user.controller.RoleController.update');
INSERT INTO `sys_api` VALUES ('a244d98e-4401-4c1c-ba06-c35378f2afad', '查询日志列表', '/system/apilog/list', 1, '2022-09-29 17:24:03', 'GET', '系统模块', 'com.zny.system.controller.ApiLogController.list');
INSERT INTO `sys_api` VALUES ('a313a5e4-cea6-42bb-aa49-cf9f4a95e62b', '根据角色获取菜单', '/user/menu/{roleId}/role', 1, '2022-12-06 10:55:19', 'GET', '菜单模块', 'com.zny.user.controller.MenuController.getMenuByRole');
INSERT INTO `sys_api` VALUES ('a3367827-2e86-4f88-9dba-c70414883980', '获取表信息配置', '/pipe/table/{id}', 1, '2022-12-07 09:46:19', 'GET', 'db模块', 'com.zny.pipe.controller.DataBaseController.get');
INSERT INTO `sys_api` VALUES ('a50950b5-5fd3-46f7-a51e-10db1d4ebd45', '添加任务', '/pipe/task/add', 1, '2022-10-19 11:36:30', 'POST', 'task模块', 'com.zny.pipe.controller.TaskController.add');
INSERT INTO `sys_api` VALUES ('a515e2da-da82-49e3-8a00-1c28f30ccfc8', '更新表配置', '/pipe/table/update', 1, '2022-12-07 09:46:19', 'POST', 'db模块', 'com.zny.pipe.controller.DataBaseController.update');
INSERT INTO `sys_api` VALUES ('a8e03934-bcd7-4751-b266-cf64c1eb72b2', '任务记录', '/pipe/task/log', 1, '2022-11-16 15:58:09', 'GET', 'task模块', 'com.zny.pipe.controller.TaskController.log');
INSERT INTO `sys_api` VALUES ('a90b7675-dc95-4edc-929d-a30c759675d2', '更新用户信息', '/user/{id}', 1, '2022-09-29 17:24:03', 'PATCH', '用户模块', 'com.zny.user.controller.UserController.update');
INSERT INTO `sys_api` VALUES ('ab1cdbb3-f2ad-486f-bff1-cbd21f18073d', '根据角色获取Api', '/user/api/{roleId}/role', 1, '2022-12-06 10:55:19', 'GET', '接口模块', 'com.zny.system.controller.ApiController.getApiByRole');
INSERT INTO `sys_api` VALUES ('af2decad-dcd8-4081-b930-43ba3d1db64e', '绑定用户到角色', '/user/bind_by_role', 1, '2022-09-29 17:24:03', 'PATCH', '用户模块', 'com.zny.user.controller.UserController.bindUserByRole');
INSERT INTO `sys_api` VALUES ('afed181a-1ff7-4079-a483-59236e038a2f', '更新任务信息', '/pipe/task/{id}', 1, '2022-10-19 11:36:30', 'PATCH', 'task模块', 'com.zny.pipe.controller.TaskController.update');
INSERT INTO `sys_api` VALUES ('b10f0ceb-ac81-48f0-abcf-9a1ea60a42a7', '绑定链接到用户', '/pipe/connect/bind_by_user', 1, '2022-10-19 11:36:30', 'PATCH', 'connect模块', 'com.zny.pipe.controller.ConnectController.bindConnectByUser');
INSERT INTO `sys_api` VALUES ('b77f7b3f-47ff-4e55-a9fe-059897eb8ca8', '任务状态', '/pipe/task/status', 1, '2022-11-16 16:49:19', 'GET', 'task模块', 'com.zny.pipe.controller.TaskController.status');
INSERT INTO `sys_api` VALUES ('b7905772-3143-4ae5-9ec1-120a88e3695d', '解绑菜单到用户', '/user/menu/unbind_by_user', 1, '2022-09-29 17:24:03', 'PATCH', '菜单模块', 'com.zny.user.controller.MenuController.unBindMenuByUser');
INSERT INTO `sys_api` VALUES ('b841c4bc-1195-49b0-98ee-05bb64d28038', '绑定链接到角色', '/pipe/connect/bind_by_role', 1, '2022-10-19 11:36:30', 'PATCH', 'connect模块', 'com.zny.pipe.controller.ConnectController.bindConnectByRole');
INSERT INTO `sys_api` VALUES ('bd06e52a-27b5-49bc-a3b1-771573e1c5b6', '删除菜单', '/user/menu/{id}', 1, '2022-09-29 17:24:03', 'DELETE', '菜单模块', 'com.zny.user.controller.MenuController.delete');
INSERT INTO `sys_api` VALUES ('be4d0967-9f4a-4180-aa2c-a20e72faf7ab', '获取接口信息', '/user/api/{id}', 1, '2022-09-29 17:24:03', 'GET', '接口模块', 'com.zny.system.controller.ApiController.get');
INSERT INTO `sys_api` VALUES ('c378fbb3-2fae-43f6-96ea-5f74315ab148', '登录', '/user/login', 1, '2022-09-29 17:24:03', 'GET', '用户模块', 'com.zny.user.controller.UserController.login');
INSERT INTO `sys_api` VALUES ('c594bd13-a699-4264-827b-fc2e7e430943', '删除角色', '/user/role/{id}', 1, '2022-09-29 17:24:03', 'DELETE', '角色模块', 'com.zny.user.controller.RoleController.delete');
INSERT INTO `sys_api` VALUES ('cd3180b5-db55-453a-ad3c-1f25fb74994a', '获取任务信息', '/pipe/task/{id}', 1, '2022-10-19 11:36:30', 'GET', 'task模块', 'com.zny.pipe.controller.TaskController.get');
INSERT INTO `sys_api` VALUES ('cd3fd148-38c5-43f5-a743-19b94710db9a', '删除字段配置', '/pipe/column/{id}', 1, '2022-11-15 17:19:18', 'DELETE', 'column模块', 'com.zny.pipe.controller.ColumnController.delete');
INSERT INTO `sys_api` VALUES ('d002bc3a-9b08-4052-b20a-c61a639097d1', '根据用户获取链接', '/pipe/connect/{userId}/user', 1, '2022-12-06 10:55:19', 'GET', 'connect模块', 'com.zny.pipe.controller.ConnectController.getConnectByUser');
INSERT INTO `sys_api` VALUES ('d0d83469-fe10-4054-8066-ad9edfa5ff05', '添加用户', '/user/add', 1, '2022-09-29 17:24:03', 'POST', '用户模块', 'com.zny.user.controller.UserController.add');
INSERT INTO `sys_api` VALUES ('d302f0af-cd6d-4008-b1ad-beb574b558e3', '解绑权限到用户', '/user/permission/unbind_by_user', 1, '2022-09-29 17:24:03', 'PATCH', '权限模块', 'com.zny.user.controller.PermissionController.unBindPermissionByUser');
INSERT INTO `sys_api` VALUES ('d37e9d52-333d-4fa4-a8d5-5b97c7a5870f', '获取目的节点列表', '/pipe/sink/list', 1, '2022-10-19 08:54:49', 'GET', 'sink模块', 'com.zny.pipe.controller.SinkController.list');
INSERT INTO `sys_api` VALUES ('d8226a89-772f-44c1-85fc-14c2f1fadcde', '获取用户列表', '/user/list', 1, '2022-09-29 17:24:03', 'GET', '用户模块', 'com.zny.user.controller.UserController.list');
INSERT INTO `sys_api` VALUES ('d8cb0d5d-a3f9-4ce5-b311-53e054d9d270', '添加权限', '/user/permission/add', 1, '2022-09-29 17:24:03', 'POST', '权限模块', 'com.zny.user.controller.PermissionController.add');
INSERT INTO `sys_api` VALUES ('d8e39bb4-d764-4ee0-8815-0a19f5bd846d', '获取链接信息', '/pipe/connect/{id}', 1, '2022-10-19 11:36:30', 'GET', 'connect模块', 'com.zny.pipe.controller.ConnectController.get');
INSERT INTO `sys_api` VALUES ('dc0936c4-ffbf-4946-a60d-5098a7fc97a7', '更新字段配置信息', '/pipe/column/update', 1, '2022-11-15 17:19:18', 'PATCH', 'column模块', 'com.zny.pipe.controller.ColumnController.update');
INSERT INTO `sys_api` VALUES ('de453440-540f-45be-9c58-60b93b2b2ca7', '获取用户树', '/user/tree', 1, '2022-09-29 17:24:03', 'GET', '用户模块', 'com.zny.user.controller.UserController.tree');
INSERT INTO `sys_api` VALUES ('dec53f55-855a-4a50-87be-9a0a1497efbf', '绑定源节点到用户', '/pipe/source/bind_by_user', 1, '2022-10-19 10:10:51', 'PATCH', 'source模块', 'com.zny.pipe.controller.SourceController.bindSourceByUser');
INSERT INTO `sys_api` VALUES ('e04bf4e6-d0e4-47a3-b5e3-ffb5ae1e3c97', '获取字段配置信息', '/pipe/column/{id}', 1, '2022-11-15 17:19:18', 'GET', 'column模块', 'com.zny.pipe.controller.ColumnController.get');
INSERT INTO `sys_api` VALUES ('e2499111-8c37-418b-af1a-7a5f3839834e', '获取链接列表', '/pipe/connect/list', 1, '2022-10-19 11:36:30', 'GET', 'connect模块', 'com.zny.pipe.controller.ConnectController.list');
INSERT INTO `sys_api` VALUES ('ec64f806-94a5-44b2-8e9f-535bfcf394a1', '禁用接口', '/user/api/{id}', 1, '2022-09-29 17:24:03', 'DELETE', '接口模块', 'com.zny.system.controller.ApiController.off');
INSERT INTO `sys_api` VALUES ('f0305278-a313-43bd-a0e7-0259551614e9', '解绑api到用户', '/user/api/unbind_by_user', 1, '2022-09-29 17:24:03', 'PATCH', '接口模块', 'com.zny.system.controller.ApiController.unBindApiByUser');
INSERT INTO `sys_api` VALUES ('f05a7b92-27e9-4aab-860f-4a1bab442a9e', '获取角色信息', '/user/role/{id}', 1, '2022-09-29 17:24:03', 'GET', '角色模块', 'com.zny.user.controller.RoleController.get');
INSERT INTO `sys_api` VALUES ('f17ebb54-df2c-4285-b859-faf7ea0b24b0', '根据连接和表名获取表信息', '/pipe/table/get_by_connect', 1, '2022-12-07 09:53:24', 'GET', 'db模块', 'com.zny.pipe.controller.DataBaseController.getByConnect');
INSERT INTO `sys_api` VALUES ('f53d0198-349d-42b5-8eaf-349a30399b75', '删除资源', '/system/resource/{ids}', 1, '2022-09-29 17:24:03', 'DELETE', '资源模块', 'com.zny.system.controller.ResourceController.delete');
INSERT INTO `sys_api` VALUES ('f723f26b-9505-4352-be46-b3767f107a76', '获取菜单树', '/user/menu/tree', 1, '2022-09-29 17:24:03', 'GET', '菜单模块', 'com.zny.user.controller.MenuController.tree');
INSERT INTO `sys_api` VALUES ('f7b3f19d-71c6-4b83-a938-39bbd5301f2d', '添加菜单', '/user/menu/add', 1, '2022-09-29 17:24:03', 'POST', '菜单模块', 'com.zny.user.controller.MenuController.add');
INSERT INTO `sys_api` VALUES ('f7fad383-05b6-4407-930e-062178a3446c', '解绑链接到角色', '/pipe/connect/unbind_by_role', 1, '2022-10-19 11:36:30', 'PATCH', 'connect模块', 'com.zny.pipe.controller.ConnectController.unBindConnectByRole');
INSERT INTO `sys_api` VALUES ('f881e948-babd-4d92-9d9c-c312f2fa4cb1', '获取菜单列表', '/user/menu/list', 1, '2022-09-29 17:24:03', 'GET', '菜单模块', 'com.zny.user.controller.MenuController.list');
INSERT INTO `sys_api` VALUES ('fa1a1be7-f5a3-4b52-b218-4e5b2d8d18d9', '绑定目的节点到角色', '/pipe/sink/bind_by_role', 1, '2022-10-19 08:54:49', 'PATCH', 'sink模块', 'com.zny.pipe.controller.SinkController.bindSinkByRole');
INSERT INTO `sys_api` VALUES ('fa4c7eb1-5b7f-4d25-8c52-6f2d36ec7577', '解绑任务到用户', '/pipe/task/unbind_by_user', 1, '2022-10-19 11:36:30', 'POST', 'task模块', 'com.zny.pipe.controller.TaskController.unBindTaskByUser');
INSERT INTO `sys_api` VALUES ('fbe89b72-b9ee-40e0-ba10-e7c1c49bc15c', '删除源节点', '/pipe/source/{id}', 1, '2022-10-19 10:10:51', 'DELETE', 'source模块', 'com.zny.pipe.controller.SourceController.delete');
INSERT INTO `sys_api` VALUES ('fdf8d076-5171-455f-a68b-65c9ab176088', '更新资源信息', '/system/resource/{id}', 1, '2022-09-29 17:24:03', 'PATCH', '资源模块', 'com.zny.system.controller.ResourceController.update');

-- ----------------------------
-- Table structure for sys_api_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_api_log`;
CREATE TABLE `sys_api_log`  (
  `id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `user_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户id',
  `spend` decimal(10, 2) NULL DEFAULT NULL COMMENT '花费时间',
  `url` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'api路径',
  `method` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '方法类型',
  `params` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '请求参数',
  `ip` varchar(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '请求pi',
  `code` varchar(5) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '响应码',
  `data` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '返回值',
  `start_time` datetime NULL DEFAULT NULL COMMENT '开始时间',
  `end_time` datetime NULL DEFAULT NULL COMMENT '结束时间',
  `api_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '接口名',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_api_log
-- ----------------------------
INSERT INTO `sys_api_log` VALUES ('1571002003214553089', 'fb0e075a-5943-43f8-a8aa-4e5f4df211b8', 0.07, '/user/resource/add', 'POST', NULL, '127.0.0.1', '200', '{\"code\": 200, \"msg\": \"添加资源成功！\", \"data\": null}', '2022-09-17 13:03:28', '2022-09-17 13:03:28', '添加资源');
INSERT INTO `sys_api_log` VALUES ('1573596348031574018', 'fb0e075a-5943-43f8-a8aa-4e5f4df211b8', 0.02, '/system/resource/add', 'POST', NULL, '127.0.0.1', '200', '{\"code\": 200, \"msg\": \"添加资源成功！\", \"data\": null}', '2022-09-24 16:52:32', '2022-09-24 16:52:32', '添加资源');
INSERT INTO `sys_api_log` VALUES ('1573596851272556546', 'fb0e075a-5943-43f8-a8aa-4e5f4df211b8', 0.05, '/system/resource/add', 'POST', NULL, '127.0.0.1', '200', '{\"code\": 200, \"msg\": \"添加资源成功！\", \"data\": null}', '2022-09-24 16:54:27', '2022-09-24 16:54:27', '添加资源');
INSERT INTO `sys_api_log` VALUES ('1573596851272556547', 'fb0e075a-5943-43f8-a8aa-4e5f4df211b8', 0.05, '/system/resource/add', 'POST', NULL, '127.0.0.1', '200', '{\"code\": 200, \"msg\": \"添加资源成功！\", \"data\": null}', '2022-09-24 16:54:53', '2022-09-24 16:54:53', '添加资源');
INSERT INTO `sys_api_log` VALUES ('1573597102884659201', 'fb0e075a-5943-43f8-a8aa-4e5f4df211b8', 0.05, '/system/resource/add', 'POST', NULL, '127.0.0.1', '200', '{\"code\": 200, \"msg\": \"添加资源成功！\", \"data\": null}', '2022-09-24 16:55:47', '2022-09-24 16:55:47', '添加资源');
INSERT INTO `sys_api_log` VALUES ('1573598612918099970', 'fb0e075a-5943-43f8-a8aa-4e5f4df211b8', 0.06, '/system/resource/add', 'POST', NULL, '127.0.0.1', '200', '{\"code\": 200, \"msg\": \"添加资源成功！\", \"data\": null}', '2022-09-24 17:01:11', '2022-09-24 17:01:11', '添加资源');
INSERT INTO `sys_api_log` VALUES ('1573598612918099971', 'fb0e075a-5943-43f8-a8aa-4e5f4df211b8', 0.01, '/system/resource/add', 'POST', NULL, '127.0.0.1', '200', '{\"code\": 200, \"msg\": \"添加资源成功！\", \"data\": null}', '2022-09-24 17:01:38', '2022-09-24 17:01:38', '添加资源');
INSERT INTO `sys_api_log` VALUES ('1573606917639897089', 'fb0e075a-5943-43f8-a8aa-4e5f4df211b8', 0.06, '/user/bind_by_role', 'POST', NULL, '127.0.0.1', '200', '{\"code\": 200, \"msg\": \"添加资源成功！\", \"data\": null}', '2022-09-24 17:34:23', '2022-09-24 17:34:23', '绑定用户到角色');
INSERT INTO `sys_api_log` VALUES ('1573607169218445314', 'fb0e075a-5943-43f8-a8aa-4e5f4df211b8', 0.05, '/user/api/bind_by_role', 'POST', NULL, '127.0.0.1', '200', '{\"code\": 200, \"msg\": \"添加资源成功！\", \"data\": null}', '2022-09-24 17:35:06', '2022-09-24 17:35:06', '绑定api到角色');
INSERT INTO `sys_api_log` VALUES ('1573630321860423681', 'fb0e075a-5943-43f8-a8aa-4e5f4df211b8', 7.06, '/user/api/unbind_by_role', 'POST', 'roleId=243574bd-38f7-44ca-a892-87da833a490c&apiId=036bf4fc-554a-4d4a-a423-59b4a4d1ba70', '127.0.0.1', '200', '{\"code\": 200, \"msg\": \"删除资源信息成功！\", \"data\": null}', '2022-09-24 19:07:54', '2022-09-24 19:07:54', '解绑api到角色');
INSERT INTO `sys_api_log` VALUES ('1573632335155785730', 'fb0e075a-5943-43f8-a8aa-4e5f4df211b8', 8.40, '/user/menu/bind_by_role', 'POST', NULL, '127.0.0.1', '200', '{\"code\": 200, \"msg\": \"添加资源成功！\", \"data\": null}', '2022-09-24 19:15:26', '2022-09-24 19:15:26', '绑定菜单到角色');
INSERT INTO `sys_api_log` VALUES ('1573632335155785731', 'fb0e075a-5943-43f8-a8aa-4e5f4df211b8', 9.50, '/user/menu/bind_by_role', 'POST', NULL, '127.0.0.1', '200', '{\"code\": 200, \"msg\": \"添加资源成功！\", \"data\": null}', '2022-09-24 19:15:50', '2022-09-24 19:15:50', '绑定菜单到角色');
INSERT INTO `sys_api_log` VALUES ('1573632586688196610', 'fb0e075a-5943-43f8-a8aa-4e5f4df211b8', 6.05, '/user/menu/bind_by_role', 'POST', NULL, '127.0.0.1', '200', '{\"code\": 200, \"msg\": \"添加资源成功！\", \"data\": null}', '2022-09-24 19:16:06', '2022-09-24 19:16:06', '绑定菜单到角色');
INSERT INTO `sys_api_log` VALUES ('1573632586688196611', 'fb0e075a-5943-43f8-a8aa-4e5f4df211b8', 1.48, '/user/menu/bind_by_role', 'POST', NULL, '127.0.0.1', '200', '{\"code\": 200, \"msg\": \"添加资源成功！\", \"data\": null}', '2022-09-24 19:16:22', '2022-09-24 19:16:22', '绑定菜单到角色');
INSERT INTO `sys_api_log` VALUES ('1573632586688196612', 'fb0e075a-5943-43f8-a8aa-4e5f4df211b8', 1.73, '/user/menu/unbind_by_role', 'POST', NULL, '127.0.0.1', '200', '{\"code\": 200, \"msg\": \"删除资源信息成功！\", \"data\": null}', '2022-09-24 19:16:47', '2022-09-24 19:16:47', '解绑菜单到角色');
INSERT INTO `sys_api_log` VALUES ('1573632586688196613', 'fb0e075a-5943-43f8-a8aa-4e5f4df211b8', 1.41, '/user/menu/bind_by_role', 'POST', NULL, '127.0.0.1', '200', '{\"code\": 200, \"msg\": \"添加资源成功！\", \"data\": null}', '2022-09-24 19:16:54', '2022-09-24 19:16:54', '绑定菜单到角色');
INSERT INTO `sys_api_log` VALUES ('1573633090373783554', 'fb0e075a-5943-43f8-a8aa-4e5f4df211b8', 2.20, '/user/menu/bind_by_role', 'POST', NULL, '127.0.0.1', '200', '{\"code\": 200, \"msg\": \"添加资源成功！\", \"data\": null}', '2022-09-24 19:18:10', '2022-09-24 19:18:10', '绑定菜单到角色');
INSERT INTO `sys_api_log` VALUES ('1573633090373783555', 'fb0e075a-5943-43f8-a8aa-4e5f4df211b8', 2.03, '/user/menu/bind_by_role', 'POST', NULL, '127.0.0.1', '200', '{\"code\": 200, \"msg\": \"添加资源成功！\", \"data\": null}', '2022-09-24 19:18:25', '2022-09-24 19:18:25', '绑定菜单到角色');
INSERT INTO `sys_api_log` VALUES ('1573649951253585921', 'fb0e075a-5943-43f8-a8aa-4e5f4df211b8', 0.03, '/user/add', 'POST', NULL, '127.0.0.1', '200', '{\"code\": 200, \"msg\": \"添加用户成功！\", \"data\": null}', '2022-09-24 20:25:10', '2022-09-24 20:25:10', '添加用户');
INSERT INTO `sys_api_log` VALUES ('1573649951253585922', 'fb0e075a-5943-43f8-a8aa-4e5f4df211b8', 0.01, '/user/add', 'POST', NULL, '127.0.0.1', '200', '{\"code\": 200, \"msg\": \"添加用户成功！\", \"data\": null}', '2022-09-24 20:25:24', '2022-09-24 20:25:24', '添加用户');
INSERT INTO `sys_api_log` VALUES ('1574593166332796930', 'fb0e075a-5943-43f8-a8aa-4e5f4df211b8', 7.26, '/iot/station/bind_by_user', 'POST', NULL, '127.0.0.1', '200', '{\"code\": 200, \"msg\": \"添加资源成功！\", \"data\": null}', '2022-09-27 10:53:00', '2022-09-27 10:53:00', '绑定测站到用户');
INSERT INTO `sys_api_log` VALUES ('1575032076213030913', 'fb0e075a-5943-43f8-a8aa-4e5f4df211b8', 11.77, '/iot/station/add', 'POST', NULL, '127.0.0.1', '200', '{\"code\": 200, \"msg\": \"站点新增成功！\", \"data\": null}', '2022-09-28 15:57:13', '2022-09-28 15:57:13', '添加测站');
INSERT INTO `sys_api_log` VALUES ('1575032309848346626', 'fb0e075a-5943-43f8-a8aa-4e5f4df211b8', 11.69, '/iot/station/add', 'POST', NULL, '127.0.0.1', '200', '{\"code\": 200, \"msg\": \"站点新增成功！\", \"data\": null}', '2022-09-28 15:58:04', '2022-09-28 15:58:04', '添加测站');
INSERT INTO `sys_api_log` VALUES ('1575035078172876801', 'fb0e075a-5943-43f8-a8aa-4e5f4df211b8', 1.53, '/iot/station/add', 'POST', NULL, '127.0.0.1', '200', '{\"code\": 200, \"msg\": \"站点新增成功！\", \"data\": null}', '2022-09-28 16:09:54', '2022-09-28 16:09:54', '添加测站');
INSERT INTO `sys_api_log` VALUES ('1575406525915590658', 'fb0e075a-5943-43f8-a8aa-4e5f4df211b8', 3.49, '/iot/station/bind_by_role', 'POST', 'roleIds=15f26c05-4615-4e43-bfe0-b337bed2142d&roleIds=243574bd-38f7-44ca-a892-87da833a490c&stationBaseSetIds=5&stationBaseSetIds=6', '127.0.0.1', '200', '{\"code\": 200, \"msg\": \"添加资源成功！\", \"data\": null}', '2022-09-29 16:45:50', '2022-09-29 16:45:50', '绑定测站到角色');
INSERT INTO `sys_api_log` VALUES ('1575409294143627265', 'fb0e075a-5943-43f8-a8aa-4e5f4df211b8', 1.16, '/user/bind_by_role', 'POST', 'roleIds=15f26c05-4615-4e43-bfe0-b337bed2142d&roleIds=243574bd-38f7-44ca-a892-87da833a490c&userIds=fb0e075a-5943-43f8-a8aa-4e5f4df211b8&userIds=6410dd27-2143-40ea-b83b-d61f886686eb', '127.0.0.1', '200', '{\"code\": 200, \"msg\": \"添加资源成功！\", \"data\": null}', '2022-09-29 16:56:21', '2022-09-29 16:56:21', '绑定用户到角色');
INSERT INTO `sys_api_log` VALUES ('1582545064398188546', 'fb0e075a-5943-43f8-a8aa-4e5f4df211b8', 6.28, '/pipe/sink/add', 'POST', 'sinkName=aa&connectId=11&tableName=pptn', '127.0.0.1', '200', '{\"code\": 200, \"msg\": \"添加节点成功！\", \"data\": null}', '2022-10-19 09:31:45', '2022-10-19 09:31:45', '添加节点');
INSERT INTO `sys_api_log` VALUES ('1582548334927704065', 'fb0e075a-5943-43f8-a8aa-4e5f4df211b8', 0.06, '/pipe/sink/0d810ea8-6103-441f-9b4e-94d03dbffb26', 'PATCH', 'sinkName=bb', '127.0.0.1', '200', '{\"code\": 200, \"msg\": \"更新节点信息成功！\", \"data\": null}', '2022-10-19 09:44:37', '2022-10-19 09:44:37', '更新节点信息');
INSERT INTO `sys_api_log` VALUES ('1582577527447191553', 'fb0e075a-5943-43f8-a8aa-4e5f4df211b8', 8.69, '/pipe/connect/add', 'POST', 'connectName=localhost&host=127.0.0.1&port=3306&userName=root&passWord=123456&dbName=wbs&dbType=0', '127.0.0.1', '200', '{\"code\": 200, \"msg\": \"添加链接成功！\", \"data\": null}', '2022-10-19 11:40:37', '2022-10-19 11:40:37', '添加链接');
INSERT INTO `sys_api_log` VALUES ('1582621315863412737', 'fb0e075a-5943-43f8-a8aa-4e5f4df211b8', 0.03, '/pipe/connect/add', 'POST', 'connectName=test1&host=127.0.0.1&port=3306&userName=root&passWord=123456&dbName=test1&dbType=0', '127.0.0.1', '200', '{\"code\": 200, \"msg\": \"添加链接成功！\", \"data\": null}', '2022-10-19 14:34:50', '2022-10-19 14:34:50', '添加链接');
INSERT INTO `sys_api_log` VALUES ('1582621567349686274', 'fb0e075a-5943-43f8-a8aa-4e5f4df211b8', 0.01, '/pipe/connect/add', 'POST', 'connectName=test2&host=127.0.0.1&port=3306&userName=root&passWord=123456&dbName=test2&dbType=0', '127.0.0.1', '200', '{\"code\": 200, \"msg\": \"添加链接成功！\", \"data\": null}', '2022-10-19 14:35:12', '2022-10-19 14:35:12', '添加链接');
INSERT INTO `sys_api_log` VALUES ('1582622070682943490', 'fb0e075a-5943-43f8-a8aa-4e5f4df211b8', 5.69, '/pipe/sink/add', 'POST', 'sinkName=test2&connectId=494d2568-8e01-4ed1-8c13-3d2322993f5f&tableName=test2', '127.0.0.1', '200', '{\"code\": 200, \"msg\": \"添加目的节点成功！\", \"data\": null}', '2022-10-19 14:37:08', '2022-10-19 14:37:08', '添加目的节点');
INSERT INTO `sys_api_log` VALUES ('1582622322345377794', 'fb0e075a-5943-43f8-a8aa-4e5f4df211b8', 0.03, '/pipe/source/add', 'POST', 'sourceName=test1&connectId=34ad2ff7-d861-4b68-83dd-f1719bb1e4af&tableName=test1&primaryField=Id,TM&timeField=TM&wrtmField=&orderField=TM&orderType=1&getType=1', '127.0.0.1', '200', '{\"code\": 200, \"msg\": \"添加源节点成功！\", \"data\": null}', '2022-10-19 14:38:52', '2022-10-19 14:38:52', '添加源节点');
INSERT INTO `sys_api_log` VALUES ('1582622825682829313', 'fb0e075a-5943-43f8-a8aa-4e5f4df211b8', 0.03, '/pipe/task/add', 'POST', 'taskName=ceshi&sinkId=4daf7a87-05da-4ca6-bb70-44066fd19c50&sourceId=79552eea-815b-4d67-8617-15feda29713f&startTime=2022-01-01%2000:00:00&endTime=2023-01-01%2000:00:00&timeStep=5&insertType=0&whereParam=&executeType=0&executeParam=1', '127.0.0.1', '200', '{\"code\": 200, \"msg\": \"添加任务成功！\", \"data\": null}', '2022-10-19 14:40:31', '2022-10-19 14:40:31', '添加任务');
INSERT INTO `sys_api_log` VALUES ('1592080142627659777', 'fb0e075a-5943-43f8-a8aa-4e5f4df211b8', 7.18, '/pipe/table/add', 'POST', 'connectId=34ad2ff7-d861-4b68-83dd-f1719bb1e4af&tableName=test1', '127.0.0.1', '200', '{\"code\": 200, \"msg\": \"添加表信息成功！\", \"data\": null}', '2022-11-14 17:00:08', '2022-11-14 17:00:15', '');
INSERT INTO `sys_api_log` VALUES ('1592081400885284865', 'fb0e075a-5943-43f8-a8aa-4e5f4df211b8', 7.50, '/pipe/table/add', 'POST', 'connectId=34ad2ff7-d861-4b68-83dd-f1719bb1e4af&tableName=test1', '127.0.0.1', '200', '{\"code\": 200, \"msg\": \"添加表信息成功！\", \"data\": null}', '2022-11-14 17:05:23', '2022-11-14 17:05:30', '');
INSERT INTO `sys_api_log` VALUES ('1592081903975272450', 'fb0e075a-5943-43f8-a8aa-4e5f4df211b8', 63.11, '/pipe/table/add', 'POST', 'connectId=34ad2ff7-d861-4b68-83dd-f1719bb1e4af&tableName=test1', '127.0.0.1', '200', '{\"code\": 200, \"msg\": \"添加表信息成功！\", \"data\": null}', '2022-11-14 17:06:06', '2022-11-14 17:07:09', '');
INSERT INTO `sys_api_log` VALUES ('1592081903975272451', 'fb0e075a-5943-43f8-a8aa-4e5f4df211b8', 3.14, '/pipe/table/add', 'POST', 'connectId=34ad2ff7-d861-4b68-83dd-f1719bb1e4af&tableName=test1', '127.0.0.1', '200', '{\"code\": 200, \"msg\": \"添加表信息成功！\", \"data\": null}', '2022-11-14 17:07:14', '2022-11-14 17:07:17', '');
INSERT INTO `sys_api_log` VALUES ('1592081903975272452', 'fb0e075a-5943-43f8-a8aa-4e5f4df211b8', 2.74, '/pipe/table/add', 'POST', 'connectId=34ad2ff7-d861-4b68-83dd-f1719bb1e4af&tableName=test1', '127.0.0.1', '200', '{\"code\": 200, \"msg\": \"添加表信息成功！\", \"data\": null}', '2022-11-14 17:07:39', '2022-11-14 17:07:42', '');
INSERT INTO `sys_api_log` VALUES ('1592090963814621186', 'fb0e075a-5943-43f8-a8aa-4e5f4df211b8', 5.86, '/pipe/table/add', 'POST', 'connectId=34ad2ff7-d861-4b68-83dd-f1719bb1e4af&tableName=test1', '127.0.0.1', '200', '{\"code\": 200, \"msg\": \"添加表信息成功！\", \"data\": null}', '2022-11-14 17:42:57', '2022-11-14 17:43:03', '');
INSERT INTO `sys_api_log` VALUES ('1592091552359358466', 'fb0e075a-5943-43f8-a8aa-4e5f4df211b8', 1.54, '/pipe/table/add', 'POST', 'connectId=34ad2ff7-d861-4b68-83dd-f1719bb1e4af&tableName=test1', '127.0.0.1', '200', '{\"code\": 200, \"msg\": \"添加表信息成功！\", \"data\": null}', '2022-11-14 17:44:19', '2022-11-14 17:44:21', '');
INSERT INTO `sys_api_log` VALUES ('1592118394595852289', 'fb0e075a-5943-43f8-a8aa-4e5f4df211b8', 1.38, '/pipe/table/add', 'POST', 'connectId=34ad2ff7-d861-4b68-83dd-f1719bb1e4af&tableName=test1', '127.0.0.1', '200', '{\"code\": 200, \"msg\": \"添加表信息成功！\", \"data\": null}', '2022-11-14 19:32:46', '2022-11-14 19:32:47', '');
INSERT INTO `sys_api_log` VALUES ('1592119652647976962', 'fb0e075a-5943-43f8-a8aa-4e5f4df211b8', 27.45, '/pipe/table/add', 'POST', 'connectId=34ad2ff7-d861-4b68-83dd-f1719bb1e4af&tableName=test1', '127.0.0.1', '200', '{\"code\": 200, \"msg\": \"添加表信息成功！\", \"data\": null}', '2022-11-14 19:37:27', '2022-11-14 19:37:54', '');
INSERT INTO `sys_api_log` VALUES ('1592120156111355906', 'fb0e075a-5943-43f8-a8aa-4e5f4df211b8', 9.68, '/pipe/table/add', 'POST', 'connectId=34ad2ff7-d861-4b68-83dd-f1719bb1e4af&tableName=test1', '127.0.0.1', '200', '{\"code\": 200, \"msg\": \"添加表信息成功！\", \"data\": null}', '2022-11-14 19:39:01', '2022-11-14 19:39:10', '');
INSERT INTO `sys_api_log` VALUES ('1592133745698172930', 'fb0e075a-5943-43f8-a8aa-4e5f4df211b8', 33.78, '/pipe/table/update', 'POST', 'connectId=34ad2ff7-d861-4b68-83dd-f1719bb1e4af&tableName=test1', '127.0.0.1', '200', '{\"code\": 200, \"msg\": \"添加表信息成功！\", \"data\": null}', '2022-11-14 20:32:42', '2022-11-14 20:33:15', '');
INSERT INTO `sys_api_log` VALUES ('1592133745698172931', 'fb0e075a-5943-43f8-a8aa-4e5f4df211b8', 1.65, '/pipe/table/update', 'POST', 'connectId=34ad2ff7-d861-4b68-83dd-f1719bb1e4af&tableName=test1', '127.0.0.1', '200', '{\"code\": 200, \"msg\": \"添加表信息成功！\", \"data\": null}', '2022-11-14 20:33:24', '2022-11-14 20:33:26', '');
INSERT INTO `sys_api_log` VALUES ('1592135759098294273', 'fb0e075a-5943-43f8-a8aa-4e5f4df211b8', 0.06, '/pipe/table/delete', 'DELETE', 'connectId=34ad2ff7-d861-4b68-83dd-f1719bb1e4af&tableName=test1', '127.0.0.1', '200', '{\"code\": 200, \"msg\": \"删除表信息成功！\", \"data\": null}', '2022-11-14 20:41:53', '2022-11-14 20:41:53', '');
INSERT INTO `sys_api_log` VALUES ('1592136010433572865', 'fb0e075a-5943-43f8-a8aa-4e5f4df211b8', 4.62, '/pipe/table/add', 'POST', 'connectId=34ad2ff7-d861-4b68-83dd-f1719bb1e4af&tableName=test1', '127.0.0.1', '200', '{\"code\": 200, \"msg\": \"添加表信息成功！\", \"data\": null}', '2022-11-14 20:42:10', '2022-11-14 20:42:15', '');
INSERT INTO `sys_api_log` VALUES ('1592136010433572866', 'fb0e075a-5943-43f8-a8aa-4e5f4df211b8', 6.50, '/pipe/table/update', 'POST', 'connectId=34ad2ff7-d861-4b68-83dd-f1719bb1e4af&tableName=test1', '127.0.0.1', '200', '{\"code\": 200, \"msg\": \"添加表信息成功！\", \"data\": null}', '2022-11-14 20:42:43', '2022-11-14 20:42:50', '');
INSERT INTO `sys_api_log` VALUES ('1592449073452011521', 'fb0e075a-5943-43f8-a8aa-4e5f4df211b8', 12.98, '/pipe/column/add', 'POST', 'taskId=', '127.0.0.1', '200', '{\"code\": 200, \"msg\": \"添加字段配置成功！\", \"data\": null}', '2022-11-15 17:26:23', '2022-11-15 17:26:36', '添加字段配置');
INSERT INTO `sys_api_log` VALUES ('1592449324955062273', 'fb0e075a-5943-43f8-a8aa-4e5f4df211b8', 15.93, '/pipe/column/add', 'POST', 'taskId=', '127.0.0.1', '200', '{\"code\": 200, \"msg\": \"添加字段配置成功！\", \"data\": null}', '2022-11-15 17:27:07', '2022-11-15 17:27:23', '添加字段配置');
INSERT INTO `sys_api_log` VALUES ('1592449324955062274', 'fb0e075a-5943-43f8-a8aa-4e5f4df211b8', 0.96, '/pipe/column/add', 'POST', 'taskId=', '127.0.0.1', '200', '{\"code\": 200, \"msg\": \"添加字段配置成功！\", \"data\": null}', '2022-11-15 17:27:29', '2022-11-15 17:27:30', '添加字段配置');

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu`  (
  `id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `menu_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '菜单名',
  `menu_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '菜单代码',
  `menu_status` int NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT NULL,
  `parent_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '父id',
  `menu_index` int NULL DEFAULT NULL COMMENT '菜单序号',
  `menu_url` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '链接地址',
  `menu_icon` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '图标',
  `menu_type` int NULL DEFAULT NULL COMMENT '类型：链接、按钮',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO `sys_menu` VALUES ('1', '菜单1', 'menu_1', NULL, '2022-09-07 12:23:09', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES ('11', '菜单1-1', 'menu_1_1', NULL, '2022-09-07 12:23:09', '1', NULL, NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES ('111', '菜单2修改', 'menu_2_update', NULL, '2022-09-06 16:34:06', '11', NULL, NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES ('ac14b3bb-1f29-4950-b1f3-3e136394c29d', '菜单3', 'menu_3', NULL, '2022-09-07 12:23:09', NULL, NULL, NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES ('eae17cc1-86c6-4fd4-9e94-9cbd66245201', '菜单4_1', 'menu_4_1', NULL, '2022-09-16 17:55:03', '', 1, 'www', 'icon', 11);

-- ----------------------------
-- Table structure for sys_permission
-- ----------------------------
DROP TABLE IF EXISTS `sys_permission`;
CREATE TABLE `sys_permission`  (
  `id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `permission_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '权限名称',
  `permission_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '权限代码',
  `permission_status` int NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT NULL,
  `parent_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '父级id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_permission
-- ----------------------------
INSERT INTO `sys_permission` VALUES ('15f26c05-4615-4e43-bfe0-b337bed2142d', '查看', 'select', NULL, '2022-09-06 16:33:20', NULL);
INSERT INTO `sys_permission` VALUES ('e87c9193-8b92-4740-a306-de98180c2628', '修改', 'update', NULL, '2022-09-06 16:48:40', NULL);

-- ----------------------------
-- Table structure for sys_resource
-- ----------------------------
DROP TABLE IF EXISTS `sys_resource`;
CREATE TABLE `sys_resource`  (
  `id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `main_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '主id',
  `main_type` int NULL DEFAULT NULL COMMENT '主类型',
  `slave_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '属id',
  `slave_type` int NULL DEFAULT NULL COMMENT '属类型',
  `create_time` datetime NULL DEFAULT NULL,
  `resource_status` int NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_resource
-- ----------------------------
INSERT INTO `sys_resource` VALUES ('0cab03e0-ce68-4437-ac33-fec8458d36c0', '15f26c05-4615-4e43-bfe0-b337bed2142d', 0, '5', 5, '2022-09-29 16:45:49', NULL);
INSERT INTO `sys_resource` VALUES ('6323e53d-dc5c-4b7f-8374-6ac055070a2d', '243574bd-38f7-44ca-a892-87da833a490c', 0, 'fb0e075a-5943-43f8-a8aa-4e5f4df211b8', 1, '2022-09-29 16:56:21', NULL);
INSERT INTO `sys_resource` VALUES ('92e2a6db-bc11-4e3a-8e83-22d05010c763', '243574bd-38f7-44ca-a892-87da833a490c', 0, '6', 5, '2022-09-29 16:45:49', NULL);
INSERT INTO `sys_resource` VALUES ('98025b7a-e770-4d28-8987-dab839d4276f', '15f26c05-4615-4e43-bfe0-b337bed2142d', 0, 'fb0e075a-5943-43f8-a8aa-4e5f4df211b8', 1, '2022-09-29 16:56:20', NULL);
INSERT INTO `sys_resource` VALUES ('a13207b0-9c03-43a6-90f8-2357f6dcecce', '243574bd-38f7-44ca-a892-87da833a490c', 0, '5', 5, '2022-09-29 16:45:49', NULL);
INSERT INTO `sys_resource` VALUES ('a44980b0-8cac-4e2e-9cb0-53dcf9335155', '243574bd-38f7-44ca-a892-87da833a490c', 0, '6410dd27-2143-40ea-b83b-d61f886686eb', 1, '2022-09-29 16:56:21', NULL);
INSERT INTO `sys_resource` VALUES ('cfa22bfa-81a8-4cf2-a1c3-7b73f0ed6dd5', '15f26c05-4615-4e43-bfe0-b337bed2142d', 0, '6410dd27-2143-40ea-b83b-d61f886686eb', 1, '2022-09-29 16:56:20', NULL);
INSERT INTO `sys_resource` VALUES ('f2658b0e-3ea6-4c65-b78e-1ecc42b908f1', '15f26c05-4615-4e43-bfe0-b337bed2142d', 0, '6', 5, '2022-09-29 16:45:49', NULL);

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`  (
  `id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `role_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '角色名称',
  `role_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '角色代码',
  `role_status` int NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT NULL,
  `parent_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '父id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES ('15f26c05-4615-4e43-bfe0-b337bed2142d', '管理员', 'admin', NULL, '2022-09-06 16:33:20', NULL);
INSERT INTO `sys_role` VALUES ('243574bd-38f7-44ca-a892-87da833a490c', '普通用户', 'guest', NULL, '2022-09-06 16:34:06', NULL);

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
  `id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `user_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户名',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '密码',
  `create_time` datetime NULL DEFAULT NULL,
  `user_status` int NULL DEFAULT NULL,
  `parent_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '父id',
  `user_type` int NULL DEFAULT NULL COMMENT '用户类型：0普通用户 1为超管 2为只读',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES ('1128634f-b0fb-4b87-12', 'wbs3-1', 'e10adc3949ba59abbe56e057f20f883e', '2022-09-13 14:53:18', NULL, '1128634f-b0fb-4b87-bb25-12', 0);
INSERT INTO `sys_user` VALUES ('1128634f-b0fb-4b87-bb25-12', 'wbs2-1', 'e10adc3949ba59abbe56e057f20f883e', '2022-09-13 14:53:18', NULL, '1128634f-b0fb-4b87-bb25-c9d761e69e78', 0);
INSERT INTO `sys_user` VALUES ('1128634f-b0fb-4b87-bb25-c9d761e69e78', 'wbs1', 'e10adc3949ba59abbe56e057f20f883e', '2022-09-13 14:53:18', NULL, '6410dd27-2143-40ea-b83b-d61f886686eb', 0);
INSERT INTO `sys_user` VALUES ('6410dd27-2143-40ea-b83b-d61f886686eb', 'wbs', '202cb962ac59075b964b07152d234b70', '2022-09-13 14:52:38', NULL, '', 0);
INSERT INTO `sys_user` VALUES ('73b86c62-a6d5-4e97-a8bd-1e7e9c7732a0', 'wbs123', 'e10adc3949ba59abbe56e057f20f883e', '2022-09-24 20:25:10', NULL, '6410dd27-2143-40ea-b83b-d61f886686eb', 0);
INSERT INTO `sys_user` VALUES ('c272a01f-3ee3-439e-8edf-d915d74bd520', '123ww', '202cb962ac59075b964b07152d234b70', '2022-09-02 23:32:23', NULL, NULL, 0);
INSERT INTO `sys_user` VALUES ('c7892785-0249-4236-b88f-15a7df4d48e5', 'wbs123456', 'e10adc3949ba59abbe56e057f20f883e', '2022-09-24 20:25:24', NULL, '123', 0);
INSERT INTO `sys_user` VALUES ('e40170bf-1200-4d10-a8c4-01e07454e7a9', 'aaaaaabc', '202cb962ac59075b964b07152d234b70', '2022-09-02 23:30:28', NULL, NULL, 0);
INSERT INTO `sys_user` VALUES ('fb0e075a-5943-43f8-a8aa-4e5f4df211b8', '123', '202cb962ac59075b964b07152d234b70', '2022-09-02 23:31:44', NULL, NULL, 1);

SET FOREIGN_KEY_CHECKS = 1;
