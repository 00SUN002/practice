# 创建表admin
CREATE TABLE `admin`  (
                          `id` integer NOT NULL AUTO_INCREMENT COMMENT 'ID',
                          `username` varchar(255) NULL,
                          `password` varchar(255) NULL,
                          `name` varchar(255) NULL,
                          `phone` varchar(255) NULL,
                          `email` varchar(255) NULL,
                          PRIMARY KEY (`id` DESC)
);