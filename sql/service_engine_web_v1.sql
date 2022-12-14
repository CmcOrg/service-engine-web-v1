SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for sys_dict
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict`;
CREATE TABLE `sys_dict`
(
    `id`          bigint                                                        NOT NULL,
    `create_id`   bigint                                                        NOT NULL,
    `create_time` datetime                                                      NOT NULL,
    `update_id`   bigint                                                        NOT NULL,
    `update_time` datetime                                                      NOT NULL,
    `enable_flag` tinyint(1)                                                    NOT NULL COMMENT '启用/禁用',
    `version`     int                                                           NOT NULL COMMENT '乐观锁',
    `del_flag`    tinyint(1)                                                    NOT NULL COMMENT '是否逻辑删除',
    `remark`      varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '描述/备注',
    `dict_key`    varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NOT NULL COMMENT '字典 key（不能重复），字典项要冗余这个 key，目的：方便操作',
    `name`        varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NOT NULL COMMENT '字典/字典项 名',
    `type`        tinyint                                                       NOT NULL COMMENT '字典类型：1 字典 2 字典项',
    `value`       tinyint                                                       NOT NULL COMMENT '字典项 value（数字 123...）备注：字典为 -1',
    `order_no`    int                                                           NOT NULL COMMENT '排序号（值越大越前面，默认为 0）',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '主表：字典'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_dict
-- ----------------------------
INSERT INTO `sys_dict`
VALUES (1, 1, '2022-03-07 16:13:14', 0, '2022-10-06 04:08:09', 1, 3, 0, '', 'request_category', '请求类别', 1, -1, 10000);
INSERT INTO `sys_dict`
VALUES (2, 1, '2022-03-07 16:16:34', 1, '2022-05-06 15:04:59', 1, 3, 0, '', 'request_category', 'H5（网页端）', 2, 1, 10000);
INSERT INTO `sys_dict`
VALUES (3, 1, '2022-03-07 16:16:50', 1, '2022-05-06 15:05:07', 1, 3, 0, '', 'request_category', 'APP（移动端）', 2, 2, 9990);
INSERT INTO `sys_dict`
VALUES (4, 1, '2022-03-07 16:17:01', 1, '2022-05-06 15:05:15', 1, 3, 0, '', 'request_category', 'PC（桌面程序）', 2, 3, 9980);
INSERT INTO `sys_dict`
VALUES (5, 1, '2022-04-13 21:47:07', 1, '2022-05-06 15:05:22', 1, 1, 0, '', 'request_category', '微信小程序', 2, 4, 9970);

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu`
(
    `id`          bigint                                                        NOT NULL,
    `create_id`   bigint                                                        NOT NULL,
    `create_time` datetime                                                      NOT NULL,
    `update_id`   bigint                                                        NOT NULL,
    `update_time` datetime                                                      NOT NULL,
    `enable_flag` tinyint(1)                                                    NOT NULL COMMENT '启用/禁用',
    `version`     int                                                           NOT NULL COMMENT '乐观锁',
    `del_flag`    tinyint(1)                                                    NOT NULL COMMENT '是否逻辑删除',
    `remark`      varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '描述/备注',
    `name`        varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '菜单名',
    `path`        varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '页面的 path，备注：相同父菜单下，子菜单 path不能重复',
    `icon`        varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '图标',
    `parent_id`   bigint                                                        NOT NULL COMMENT '父节点id（顶级则为0）',
    `auths`       varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '权限，多个可用逗号拼接，例如：menu:insertOrUpdate,menu:page,menu:deleteByIdSet,menu:infoById',
    `show_flag`   tinyint(1)                                                    NOT NULL COMMENT '是否显示在 左侧的菜单栏里面，如果为 false，也可以通过 $router.push()访问到',
    `link_flag`   tinyint(1)                                                    NOT NULL COMMENT '是否外链，即，打开页面会在一个新的窗口打开，可以配合 router',
    `router`      varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '路由',
    `redirect`    varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '重定向，优先级最高',
    `order_no`    int                                                           NOT NULL COMMENT '排序号（值越大越前面，默认为 0）',
    `first_flag`  tinyint(1)                                                    NOT NULL COMMENT '是否是起始页面，备注：只能存在一个 firstFlag === true 的菜单',
    `auth_flag`   tinyint(1)                                                    NOT NULL COMMENT '是否是权限菜单，权限菜单：不显示，只代表菜单权限',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '主表：菜单'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO `sys_menu`
VALUES (3, 1, '2021-12-22 10:53:32', 1, '2022-06-23 16:36:39', 1, 2, 0, '', '系统管理', '', 'SettingOutlined',
        221004205817000054, '', 1, 0, '', '', 9990, 0, 0);
INSERT INTO `sys_menu`
VALUES (4, 1, '2021-12-20 11:10:15', 1, '2022-05-03 16:21:50', 1, 2, 0, '', '菜单管理', '/admin/sys/menu', '', 3, '', 1, 0,
        'sysMenuMenu', '', 10000, 0, 0);
INSERT INTO `sys_menu`
VALUES (5, 1, '2022-05-21 01:00:18', 1, '2022-05-21 01:00:18', 1, 2, 0, '', '新增修改', '', '', 4, 'sysMenu:insertOrUpdate',
        0, 0, '', '', 10000, 0, 1);
INSERT INTO `sys_menu`
VALUES (6, 1, '2022-05-21 01:00:18', 1, '2022-05-21 01:00:18', 1, 2, 0, '', '列表查询', '', '', 4, 'sysMenu:page', 0, 0, '',
        '', 9990, 0, 1);
INSERT INTO `sys_menu`
VALUES (7, 1, '2022-05-21 01:00:19', 1, '2022-05-21 01:00:19', 1, 2, 0, '', '删除', '', '', 4, 'sysMenu:deleteByIdSet', 0,
        0, '', '', 9980, 0, 1);
INSERT INTO `sys_menu`
VALUES (8, 1, '2022-05-21 01:00:19', 1, '2022-05-21 01:00:19', 1, 2, 0, '', '查看详情', '', '', 4, 'sysMenu:infoById', 0, 0,
        '', '', 9970, 0, 1);
INSERT INTO `sys_menu`
VALUES (9, 1, '2021-12-22 11:09:35', 0, '2022-07-18 02:56:28', 1, 2, 0, '', '角色管理', '/admin/sys/role', '', 3, '', 1, 0,
        'sysRoleRole', '', 9990, 0, 0);
INSERT INTO `sys_menu`
VALUES (10, 1, '2022-05-21 01:03:25', 1, '2022-05-21 01:03:25', 1, 2, 0, '', '新增修改', '', '', 9,
        'sysRole:insertOrUpdate', 0, 0, '', '', 10000, 0, 1);
INSERT INTO `sys_menu`
VALUES (11, 1, '2022-05-21 01:03:26', 1, '2022-05-21 01:03:26', 1, 2, 0, '', '列表查询', '', '', 9, 'sysRole:page', 0, 0,
        '', '', 9990, 0, 1);
INSERT INTO `sys_menu`
VALUES (12, 1, '2022-05-21 01:03:26', 1, '2022-05-21 01:03:26', 1, 2, 0, '', '删除', '', '', 9, 'sysRole:deleteByIdSet',
        0, 0, '', '', 9980, 0, 1);
INSERT INTO `sys_menu`
VALUES (13, 1, '2022-05-21 01:03:26', 1, '2022-05-21 01:03:26', 1, 2, 0, '', '查看详情', '', '', 9, 'sysRole:infoById', 0,
        0, '', '', 9970, 0, 1);
INSERT INTO `sys_menu`
VALUES (14, 1, '2021-12-22 11:09:16', 1, '2022-05-03 17:00:27', 1, 2, 0, '', '用户管理', '/admin/sys/user', '', 3, '', 1, 0,
        'sysUserUser', '', 9980, 0, 0);
INSERT INTO `sys_menu`
VALUES (15, 1, '2022-05-21 01:03:16', 1, '2022-05-21 01:03:16', 1, 2, 0, '', '新增修改', '', '', 14,
        'sysUser:insertOrUpdate', 0, 0, '', '', 10000, 0, 1);
INSERT INTO `sys_menu`
VALUES (16, 1, '2022-05-21 01:03:17', 1, '2022-05-21 01:03:17', 1, 2, 0, '', '列表查询', '', '', 14, 'sysUser:page', 0, 0,
        '', '', 9990, 0, 1);
INSERT INTO `sys_menu`
VALUES (17, 1, '2022-05-21 01:03:17', 1, '2022-05-21 01:03:17', 1, 2, 0, '', '删除', '', '', 14, 'sysUser:deleteByIdSet',
        0, 0, '', '', 9980, 0, 1);
INSERT INTO `sys_menu`
VALUES (18, 1, '2022-05-21 01:03:17', 1, '2022-05-21 01:03:17', 1, 2, 0, '', '查看详情', '', '', 14, 'sysUser:infoById', 0,
        0, '', '', 9970, 0, 1);
INSERT INTO `sys_menu`
VALUES (19, 1, '2021-12-22 11:09:53', 1, '2022-07-03 14:12:10', 1, 2, 0, '', '字典管理', '/admin/sys/dict', '', 3, '', 1, 0,
        'sysDictDict', '', 9970, 0, 0);
INSERT INTO `sys_menu`
VALUES (20, 1, '2022-05-21 01:03:32', 1, '2022-05-21 01:03:32', 1, 2, 0, '', '新增修改', '', '', 19,
        'sysDict:insertOrUpdate', 0, 0, '', '', 10000, 0, 1);
INSERT INTO `sys_menu`
VALUES (21, 1, '2022-05-21 01:03:32', 1, '2022-05-21 01:03:32', 1, 2, 0, '', '列表查询', '', '', 19, 'sysDict:page', 0, 0,
        '', '', 9990, 0, 1);
INSERT INTO `sys_menu`
VALUES (22, 1, '2022-05-21 01:03:32', 1, '2022-05-21 01:03:32', 1, 2, 0, '', '删除', '', '', 19, 'sysDict:deleteByIdSet',
        0, 0, '', '', 9980, 0, 1);
INSERT INTO `sys_menu`
VALUES (23, 1, '2022-05-21 01:03:32', 1, '2022-05-21 01:03:32', 1, 2, 0, '', '查看详情', '', '', 19, 'sysDict:infoById', 0,
        0, '', '', 9970, 0, 1);
INSERT INTO `sys_menu`
VALUES (24, 1, '2021-12-22 11:10:31', 1, '2022-07-03 14:12:01', 1, 2, 0, '', '系统参数', '/admin/sys/param', '', 3, '', 1,
        0, 'sysParamParam', '', 9960, 0, 0);
INSERT INTO `sys_menu`
VALUES (25, 1, '2022-05-21 01:03:57', 1, '2022-05-21 01:03:57', 1, 2, 0, '', '新增修改', '', '', 24,
        'sysParam:insertOrUpdate', 0, 0, '', '', 10000, 0, 1);
INSERT INTO `sys_menu`
VALUES (26, 1, '2022-05-21 01:03:57', 1, '2022-05-21 01:03:57', 1, 2, 0, '', '列表查询', '', '', 24, 'sysParam:page', 0, 0,
        '', '', 9990, 0, 1);
INSERT INTO `sys_menu`
VALUES (27, 1, '2022-05-21 01:03:57', 1, '2022-05-21 01:03:57', 1, 2, 0, '', '删除', '', '', 24, 'sysParam:deleteByIdSet',
        0, 0, '', '', 9980, 0, 1);
INSERT INTO `sys_menu`
VALUES (28, 1, '2022-05-21 01:03:57', 1, '2022-05-21 01:03:57', 1, 2, 0, '', '查看详情', '', '', 24, 'sysParam:infoById', 0,
        0, '', '', 9970, 0, 1);
INSERT INTO `sys_menu`
VALUES (55, 1, '2021-12-24 11:37:27', 0, '2022-10-05 13:23:04', 1, 2, 0, '', '个人管理', '', '', 0, '', 0, 0, '', '', 0, 0,
        0);
INSERT INTO `sys_menu`
VALUES (56, 1, '2021-12-24 11:37:42', 0, '2022-10-05 13:24:13', 1, 2, 0, '', '个人中心', '/admin/user/self', '', 55, '', 0,
        0, 'userSelfSelf', '', 10000, 0, 0);
INSERT INTO `sys_menu`
VALUES (63, 1, '2022-07-12 11:01:41', 1, '2022-07-12 11:02:01', 1, 2, 0, '', 'Helper App', 'https://cmc0.github.io',
        'CodeOutlined', 221004205817000054, '', 1, 1, '', '', 0, 0, 0);
INSERT INTO `sys_menu`
VALUES (221004205727000053, 0, '2022-10-04 12:57:27', 0, '2022-10-04 16:30:07', 1, 2, 0, '', '欢迎', '/admin/sys/welcome',
        'SmileOutlined', 221004205817000054, '', 1, 0, 'sysWelcomeWelcome', '', 10000, 1, 0);
INSERT INTO `sys_menu`
VALUES (221004205817000054, 0, '2022-10-04 12:58:17', 0, '2022-10-04 13:12:47', 1, 2, 0, '', '系统平台', '',
        'SettingOutlined', 0, '', 1, 0, '', '/admin/sys/welcome', 10000, 0, 0);
INSERT INTO `sys_menu`
VALUES (221004211526000055, 0, '2022-10-04 13:15:27', 0, '2022-10-05 13:10:02', 1, 4, 0, '', '订单平台', '', 'TagOutlined',
        0, '', 1, 0, '', '/admin/order/welcome', 9700, 0, 0);
INSERT INTO `sys_menu`
VALUES (221004211614000056, 0, '2022-10-04 13:16:15', 0, '2022-10-04 13:33:26', 1, 2, 0, '', '欢迎',
        '/admin/order/welcome', 'SmileOutlined', 221004211526000055, '', 1, 0, 'orderWelcomeWelcome', '', 0, 0, 0);
INSERT INTO `sys_menu`
VALUES (221005155605000086, 0, '2022-10-05 07:56:05', 0, '2022-10-05 07:56:16', 1, 2, 0, '', '欢迎', '/admin/welcome',
        'SmileOutlined', 0, '', 1, 0, 'WelcomeWelcome', '', 11000, 0, 0);
INSERT INTO `sys_menu`
VALUES (221005205314000014, 0, '2022-10-05 12:53:15', 0, '2022-10-05 13:10:40', 1, 1, 0, '', '游戏平台', '',
        'BuildOutlined', 0, '', 1, 0, '', '/admin/game/welcome', 9900, 0, 0);
INSERT INTO `sys_menu`
VALUES (221005205331000015, 0, '2022-10-05 12:53:32', 0, '2022-10-05 13:11:26', 1, 1, 0, '', '商品平台', '', 'ShopOutlined',
        0, '', 1, 0, '', '/admin/goods/welcome', 9800, 0, 0);
INSERT INTO `sys_menu`
VALUES (221005205841000016, 0, '2022-10-05 12:58:42', 0, '2022-10-05 12:59:06', 1, 0, 0, '', '欢迎',
        '/admin/game/welcome', 'SmileOutlined', 221005205314000014, '', 1, 0, 'gameWelcomeWelcome', '', 10000, 0, 0);
INSERT INTO `sys_menu`
VALUES (221005205950000017, 0, '2022-10-05 12:59:51', 0, '2022-10-05 12:59:51', 1, 0, 0, '', '欢迎',
        '/admin/goods/welcome', 'SmileOutlined', 221005205331000015, '', 1, 0, 'goodsWelcomeWelcome', '', 10000, 0, 0);

-- ----------------------------
-- Table structure for sys_param
-- ----------------------------
DROP TABLE IF EXISTS `sys_param`;
CREATE TABLE `sys_param`
(
    `id`          bigint                                                        NOT NULL,
    `create_id`   bigint                                                        NOT NULL,
    `create_time` datetime                                                      NOT NULL,
    `update_id`   bigint                                                        NOT NULL,
    `update_time` datetime                                                      NOT NULL,
    `enable_flag` tinyint(1)                                                    NOT NULL COMMENT '启用/禁用',
    `version`     int                                                           NOT NULL COMMENT '乐观锁',
    `del_flag`    tinyint(1)                                                    NOT NULL COMMENT '是否逻辑删除',
    `remark`      varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '描述/备注',
    `name`        varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '配置名，以 id为不变值进行使用，不要用此属性',
    `value`       text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci         NOT NULL COMMENT '值',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '主表：系统参数'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_param
-- ----------------------------
INSERT INTO `sys_param`
VALUES (1, 1, '2021-12-26 11:32:38', 1, '2022-01-11 21:03:30', 1, 0, 0,
        '获取私钥方法：new RSA().getPrivateKeyBase64()，备注：获取公钥同理，但是必须使用同一个对象，因为必须成对', '非对称加密，私钥',
        'MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBANp2ZoJp983rckLlCwwr/6V7m+gIHgPM6xX3E4FmAYnKqeLOH39RwWQihFp7mt5t1ZS2c87gJOB0lu5K+79O0gg+P0N7n/CImKUUBZ3VV7uoZrHFPGpbAU2nun8UuvRikx4GM7KV2z0bIDVyeMiqu411i/6pFmex4TRKrZYRm6z1AgMBAAECgYAMjU//f0IvMS97+3gCh4alRfBjyQ+cbUo2lV8oCKne9meDcg9qO9LOQ5NyNXbk/8+NP1xxDvzfbqN7ZpCHYep8VoxJYMqr15czK9Sk34A5AdpOb5kQhUAgfyaQlIu+2s3NSjyJUXcNqLRRb0xiGhoJmH1V9zGSVFaJnGsUJuZAkwJBAPl/4VNtvcWTyoGBHFlJjto4V1lYkD63qKh66evXiI7PEQhGB4b8ubBFnEJephWQO/tWo6AYFipMrtjJ1z+KqWsCQQDgJ4IsRZleS5vr5bYhL5+YE8BN8TyzyJ/7MvSjV6ZB7Qoq+w7CSsWm4wTnO9zdSuJaXJ7QmMfDR9Y/tAx2MLsfAkBkdJOxtqbI7VeEywox/QbyX+rzg1AYoHPc2hhjJ9XIwiB2d1PCivDswypGIru2ROuRp/GbnPcXsuZXTPVIlTjfAkEAkhtukCj1pS8nfQYIR21hW6FUMfnSlWVqUjSOnYHeTw6RGB75Kc/PMc68PXUZq+zJyhihNFrBqxpCHtffX5K4BQJAIIs70dCXBmZ1AjWUTgRY1piEGtwoxPO229guHvF6P8IOyxCuFWrCgY/1UnZt3Yc/XubImBb/xQx5CcdFWYcgjw==');
INSERT INTO `sys_param`
VALUES (2, 1, '2021-12-26 11:32:38', 0, '2022-10-05 08:53:06', 1, 0, 0,
        '多少秒钟，一个 ip可以请求多少次，用冒号隔开的，任意值小于等于 0，则不会进行检查，超过了，则一天无法访问任何接口', 'ip请求速率', '10:75');

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`
(
    `id`           bigint                                                        NOT NULL,
    `create_id`    bigint                                                        NOT NULL,
    `create_time`  datetime                                                      NOT NULL,
    `update_id`    bigint                                                        NOT NULL,
    `update_time`  datetime                                                      NOT NULL,
    `enable_flag`  tinyint(1)                                                    NOT NULL COMMENT '启用/禁用',
    `version`      int                                                           NOT NULL COMMENT '乐观锁',
    `del_flag`     tinyint(1)                                                    NOT NULL COMMENT '是否逻辑删除',
    `remark`       varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '描述/备注',
    `name`         varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '角色名（不能重复）',
    `default_flag` tinyint(1)                                                    NOT NULL COMMENT '是否是默认角色，备注：只会有一个默认角色',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '主表：角色'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role`
VALUES (221005154618000085, 0, '2022-10-05 07:46:19', 0, '2022-10-06 06:23:20', 1, 0, 0, '只有查看的权限，没有修改和删除的权限', '游客角色',
        1);

-- ----------------------------
-- Table structure for sys_role_ref_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_ref_menu`;
CREATE TABLE `sys_role_ref_menu`
(
    `role_id` bigint NOT NULL COMMENT '角色id',
    `menu_id` bigint NOT NULL COMMENT '菜单id',
    PRIMARY KEY (`role_id`, `menu_id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '关联表：角色，菜单'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role_ref_menu
-- ----------------------------
INSERT INTO `sys_role_ref_menu`
VALUES (221005154618000085, 6);
INSERT INTO `sys_role_ref_menu`
VALUES (221005154618000085, 8);
INSERT INTO `sys_role_ref_menu`
VALUES (221005154618000085, 11);
INSERT INTO `sys_role_ref_menu`
VALUES (221005154618000085, 13);
INSERT INTO `sys_role_ref_menu`
VALUES (221005154618000085, 16);
INSERT INTO `sys_role_ref_menu`
VALUES (221005154618000085, 21);
INSERT INTO `sys_role_ref_menu`
VALUES (221005154618000085, 23);
INSERT INTO `sys_role_ref_menu`
VALUES (221005154618000085, 26);
INSERT INTO `sys_role_ref_menu`
VALUES (221005154618000085, 28);
INSERT INTO `sys_role_ref_menu`
VALUES (221005154618000085, 55);
INSERT INTO `sys_role_ref_menu`
VALUES (221005154618000085, 63);
INSERT INTO `sys_role_ref_menu`
VALUES (221005154618000085, 221004205727000053);
INSERT INTO `sys_role_ref_menu`
VALUES (221005154618000085, 221004211526000055);
INSERT INTO `sys_role_ref_menu`
VALUES (221005154618000085, 221005155605000086);
INSERT INTO `sys_role_ref_menu`
VALUES (221005154618000085, 221005205314000014);
INSERT INTO `sys_role_ref_menu`
VALUES (221005154618000085, 221005205331000015);

-- ----------------------------
-- Table structure for sys_role_ref_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_ref_user`;
CREATE TABLE `sys_role_ref_user`
(
    `role_id` bigint NOT NULL COMMENT '角色id',
    `user_id` bigint NOT NULL COMMENT '用户id',
    PRIMARY KEY (`role_id`, `user_id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '关联表：角色，用户'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role_ref_user
-- ----------------------------

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`
(
    `id`             bigint                                                        NOT NULL,
    `create_id`      bigint                                                        NOT NULL,
    `create_time`    datetime                                                      NOT NULL,
    `update_id`      bigint                                                        NOT NULL,
    `update_time`    datetime                                                      NOT NULL,
    `enable_flag`    tinyint(1)                                                    NOT NULL COMMENT '正常/冻结',
    `version`        int                                                           NOT NULL COMMENT '乐观锁',
    `del_flag`       tinyint(1)                                                    NOT NULL COMMENT '是否注销',
    `remark`         varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '描述/备注（暂时未使用）',
    `jwt_secret_suf` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NOT NULL COMMENT '用户 jwt私钥后缀（uuid）',
    `password`       varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '密码，可为空，如果为空，则登录时需要提示【进行忘记密码操作】',
    `email`          varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '邮箱，可以为空',
    `sign_in_name`   varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NOT NULL COMMENT '登录名，可以为空',
    `phone`          varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '手机号，可以为空',
    `tenant_id`      bigint                                                        NOT NULL COMMENT '租户主键 id',
    `wx_open_id`     varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '微信 openId，可以为空',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT = '主表：用户'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user
-- ----------------------------

-- ----------------------------
-- Table structure for sys_user_0
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_0`;
CREATE TABLE `sys_user_0`
(
    `id`             bigint                                                        NOT NULL,
    `create_id`      bigint                                                        NOT NULL,
    `create_time`    datetime                                                      NOT NULL,
    `update_id`      bigint                                                        NOT NULL,
    `update_time`    datetime                                                      NOT NULL,
    `enable_flag`    tinyint(1)                                                    NOT NULL COMMENT '正常/冻结',
    `version`        int                                                           NOT NULL COMMENT '乐观锁',
    `del_flag`       tinyint(1)                                                    NOT NULL COMMENT '是否注销',
    `remark`         varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '描述/备注（暂时未使用）',
    `jwt_secret_suf` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NOT NULL COMMENT '用户 jwt私钥后缀（uuid）',
    `password`       varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '密码，可为空，如果为空，则登录时需要提示【进行忘记密码操作】',
    `email`          varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '邮箱，可以为空',
    `sign_in_name`   varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NOT NULL COMMENT '登录名，可以为空',
    `phone`          varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '手机号，可以为空',
    `tenant_id`      bigint                                                        NOT NULL COMMENT '租户主键 id',
    `wx_open_id`     varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '微信 openId，可以为空',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT = '主表：用户'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user_0
-- ----------------------------

-- ----------------------------
-- Table structure for sys_user_1
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_1`;
CREATE TABLE `sys_user_1`
(
    `id`             bigint                                                        NOT NULL,
    `create_id`      bigint                                                        NOT NULL,
    `create_time`    datetime                                                      NOT NULL,
    `update_id`      bigint                                                        NOT NULL,
    `update_time`    datetime                                                      NOT NULL,
    `enable_flag`    tinyint(1)                                                    NOT NULL COMMENT '正常/冻结',
    `version`        int                                                           NOT NULL COMMENT '乐观锁',
    `del_flag`       tinyint(1)                                                    NOT NULL COMMENT '是否注销',
    `remark`         varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '描述/备注（暂时未使用）',
    `jwt_secret_suf` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NOT NULL COMMENT '用户 jwt私钥后缀（uuid）',
    `password`       varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '密码，可为空，如果为空，则登录时需要提示【进行忘记密码操作】',
    `email`          varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '邮箱，可以为空',
    `sign_in_name`   varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NOT NULL COMMENT '登录名，可以为空',
    `phone`          varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '手机号，可以为空',
    `tenant_id`      bigint                                                        NOT NULL COMMENT '租户主键 id',
    `wx_open_id`     varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '微信 openId，可以为空',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT = '主表：用户'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user_1
-- ----------------------------

-- ----------------------------
-- Table structure for sys_user_info
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_info`;
CREATE TABLE `sys_user_info`
(
    `id`         bigint                                                        NOT NULL COMMENT '用户主键 id',
    `uuid`       varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NOT NULL COMMENT '该用户的 uuid，本系统使用 id，不使用 uuid',
    `nickname`   varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NOT NULL COMMENT '昵称',
    `bio`        varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '个人简介',
    `avatar_uri` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '头像uri',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT = '子表：用户基本信息，主表：用户'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user_info
-- ----------------------------

-- ----------------------------
-- Table structure for sys_user_info_0
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_info_0`;
CREATE TABLE `sys_user_info_0`
(
    `id`         bigint                                                        NOT NULL COMMENT '用户主键 id',
    `uuid`       varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NOT NULL COMMENT '该用户的 uuid，本系统使用 id，不使用 uuid',
    `nickname`   varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NOT NULL COMMENT '昵称',
    `bio`        varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '个人简介',
    `avatar_uri` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '头像uri',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT = '子表：用户基本信息，主表：用户'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user_info_0
-- ----------------------------

-- ----------------------------
-- Table structure for sys_user_info_1
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_info_1`;
CREATE TABLE `sys_user_info_1`
(
    `id`         bigint                                                        NOT NULL COMMENT '用户主键 id',
    `uuid`       varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NOT NULL COMMENT '该用户的 uuid，本系统使用 id，不使用 uuid',
    `nickname`   varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  NOT NULL COMMENT '昵称',
    `bio`        varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '个人简介',
    `avatar_uri` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '头像uri',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT = '子表：用户基本信息，主表：用户'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user_info_1
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;
