-- ==============================
-- 图书管理系统 数据库初始化脚本
-- 数据库: library_db
-- 作者: 吴定宇
-- 日期: 2025-12-17
-- ==============================

-- 如果没有数据库则创建
CREATE DATABASE IF NOT EXISTS library_db
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_general_ci;

-- 使用该数据库
USE library_db;

-- ==============================
-- 用户表
-- ==============================
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
                        `id` INT NOT NULL AUTO_INCREMENT,
                        `username` VARCHAR(50) NOT NULL,
                        `password` VARCHAR(100) NOT NULL,
                        `role` VARCHAR(20) NOT NULL COMMENT '角色：admin=管理员, user=读者',
                        PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 初始用户数据（一个管理员，一个普通用户）
INSERT INTO `user` (`username`, `password`, `role`) VALUES
                                                        ('admin', 'admin123', 'admin'),
                                                        ('reader', 'reader123', 'user');

-- ==============================
-- 图书表
-- ==============================
DROP TABLE IF EXISTS `book`;
CREATE TABLE `book` (
                        `id` INT NOT NULL AUTO_INCREMENT,
                        `title` VARCHAR(100) NOT NULL,
                        `author` VARCHAR(50) NOT NULL,
                        PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 初始图书数据
INSERT INTO `book` (`title`, `author`) VALUES
                                           ('Java编程思想', 'Bruce Eckel'),
                                           ('Spring实战', 'Craig Walls'),
                                           ('MySQL从入门到精通', 'Ben Forta');

-- ==============================
-- 借阅记录表
-- ==============================
DROP TABLE IF EXISTS `borrow_record`;
CREATE TABLE `borrow_record` (
                                 `id` INT NOT NULL AUTO_INCREMENT,
                                 `user_id` INT NOT NULL,
                                 `book_id` INT NOT NULL,
                                 `borrow_date` DATE NOT NULL,
                                 `return_date` DATE DEFAULT NULL,
                                 PRIMARY KEY (`id`),
                                 FOREIGN KEY (`user_id`) REFERENCES `user`(`id`),
                                 FOREIGN KEY (`book_id`) REFERENCES `book`(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 示例借阅记录（reader 借了 Java编程思想）
INSERT INTO `borrow_record` (`user_id`, `book_id`, `borrow_date`, `return_date`) VALUES
    (2, 1, '2025-12-17', NULL);
