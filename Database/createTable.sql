# 创建表admin
CREATE TABLE `admin`  (
    `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `username` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '账号',
    `password` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '密码',
    `name` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '名称',
    `phone` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '手机',
    `email` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '邮箱',
    `role` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT 'ADMIN' COMMENT '角色',
    `avatar` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '头像',
    PRIMARY KEY (`id`),
    UNIQUE KEY `username_index` (`username`) COMMENT '账号'
)ENGINE = InnoDB AUTO_INCREMENT = 1 DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '管理员信息';

# 创建user
CREATE TABLE `user`  (
    `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `username` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '账号',
    `password` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '密码',
    `name` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '名称',
    `phone` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '手机',
    `email` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '邮箱',
    `role` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT 'USER' COMMENT '角色',
    `avatar` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '头像',
    PRIMARY KEY (`id`),
    UNIQUE KEY `username_index` (`username`) COMMENT '账号'
)ENGINE = InnoDB AUTO_INCREMENT = 1 DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '用户信息';