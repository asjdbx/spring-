/*
Navicat MySQL Data Transfer

Source Server         : localhost_3306
Source Server Version : 50628
Source Host           : localhost:3306
Source Database       : blog_db

Target Server Type    : MYSQL
Target Server Version : 50628
File Encoding         : 65001

Date: 2026-05-29 21:39:53
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for article
-- ----------------------------
DROP TABLE IF EXISTS `article`;
CREATE TABLE `article` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `title` varchar(30) NOT NULL COMMENT '文章标题',
  `content` varchar(10000) NOT NULL COMMENT '文章内容',
  `cover_img` varchar(128) NOT NULL COMMENT '文章封面',
  `state` varchar(3) DEFAULT '草稿' COMMENT '文章状态: 只能是[已发布] 或者 [草稿]',
  `category_id` int(10) unsigned DEFAULT NULL COMMENT '文章分类ID',
  `create_user` int(10) unsigned NOT NULL COMMENT '创建人ID',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '修改时间',
  `artiState` varchar(3) DEFAULT NULL,
  `reading` int(30) DEFAULT NULL,
  `today_reads` int(30) DEFAULT NULL,
  `last_da` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_article_category` (`category_id`),
  KEY `fk_article_user` (`create_user`),
  CONSTRAINT `fk_article_category` FOREIGN KEY (`category_id`) REFERENCES `category` (`id`),
  CONSTRAINT `fk_article_user` FOREIGN KEY (`create_user`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=38 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of article
-- ----------------------------
INSERT INTO `article` VALUES ('2', 'a12', '<p>111</p>', 'https://blong1.sypo3c5e8.hn-bkt.clouddn.com/1eab164d-2c6f-48fe-b95e-65e3d3925f9c.txt', '已发布', '6', '4', '2025-10-29 00:06:56', '2025-11-21 15:02:20', '1', '33', null, null);
INSERT INTO `article` VALUES ('9', '123', '<p>123</p>', 'https://blong1.sypo3c5e8.hn-bkt.clouddn.com/d2c5513d-3e1e-4c03-8924-1c60ba3b6317.txt', '已发布', '6', '6', '2025-11-04 15:28:11', '2025-11-21 15:28:11', '1', '36', '1', '2025-07-20 00:00:00');
INSERT INTO `article` VALUES ('14', '面', '<p>ms</p>', 'http://sypo3c5e8.hn-bkt.clouddn.com/d1f772f2-fd00-4c85-83c0-361c4130f205.jpg', '草稿', '12', '9', '2025-11-07 16:37:09', '2025-11-21 16:37:09', null, '12', null, null);
INSERT INTO `article` VALUES ('15', '啊啊', '<p>啊啊</p>', 'http://sypo3c5e8.hn-bkt.clouddn.com/fbc124f3-f0bb-459e-a35e-35474266e9f1.jpg', '已发布', '12', '9', '2025-11-07 16:39:48', '2025-11-21 16:39:48', '2', '27', null, null);
INSERT INTO `article` VALUES ('17', '123', '<p>132</p>', 'http://tds79gjsg.hn-bkt.clouddn.com/01885213-9584-4b6f-9a6e-e0017ca7ef45.jpg', '已发布', '1', '11', '2025-11-07 22:15:00', '2026-04-20 16:40:28', '2', '38', null, null);
INSERT INTO `article` VALUES ('18', '嗷嗷嗷', '<p>啊啊</p>', 'http://tds79gjsg.hn-bkt.clouddn.com/27aee2bd-75f8-41bd-b459-91fce58fc916.jpg', '已发布', '3', '11', '2025-11-10 16:20:50', '2026-04-20 16:44:38', '2', '48', '1', '2025-07-17 00:00:00');
INSERT INTO `article` VALUES ('19', '1234', '<p>11</p>', 'http://tds79gjsg.hn-bkt.clouddn.com/32e9edba-355c-4f66-a04e-a989e3e98069.jpg', '已发布', '1', '11', '2025-11-11 16:36:44', '2026-04-20 16:44:58', '2', '41', '1', '2025-07-17 00:00:00');
INSERT INTO `article` VALUES ('24', '123456', '<p>123456</p>', 'http://tds79gjsg.hn-bkt.clouddn.com/389c8018-2587-4630-af00-e41094641541.jpg', '已发布', '5', '11', '2025-11-11 22:58:42', '2026-04-20 16:45:26', '1', '37', '1', '2025-07-19 00:00:00');
INSERT INTO `article` VALUES ('25', '123', '<p>456</p>', 'http://sypo3c5e8.hn-bkt.clouddn.com/02fc0eb9-7181-4122-84b7-e3cda930a6f0.jpg', '已发布', '1', '16', '2025-11-12 17:54:44', '2026-03-11 17:54:44', '2', '14', null, null);
INSERT INTO `article` VALUES ('26', '1234', '<p>111</p>', 'http://sypo3c5e8.hn-bkt.clouddn.com/d7f3ff23-3202-4cab-b56f-e0393c4e481d.txt', '已发布', '1', '9', '2025-11-24 15:37:25', '2025-12-04 15:37:25', '2', '0', '0', null);
INSERT INTO `article` VALUES ('29', '1234567', '<p><em>111</em></p>', 'http://tds79gjsg.hn-bkt.clouddn.com/cb1e75de-3670-46a0-b468-4077881ca1ae.jpg', '草稿', '1', '11', '2025-11-24 18:48:00', '2026-04-20 16:45:32', '0', '0', '0', null);
INSERT INTO `article` VALUES ('30', '1235', '<p>123</p>', 'http://tds79gjsg.hn-bkt.clouddn.com/a95d8e7f-15ee-46f4-8c60-940720017f44.jpg', '已发布', '1', '23', '2026-05-08 16:17:20', '2026-05-08 16:17:20', '1', '0', '0', null);
INSERT INTO `article` VALUES ('31', '123', '<p>1111</p>', 'http://tds79gjsg.hn-bkt.clouddn.com/446812cb-d398-44be-a182-2304d2b9e017.jpg', '已发布', '1', '24', '2026-05-08 18:36:42', '2026-05-08 18:36:42', '0', '0', '0', null);
INSERT INTO `article` VALUES ('32', '123', '<p>123456</p>', 'http://tds79gjsg.hn-bkt.clouddn.com/8281d826-eba3-409b-98d0-929c0d24ac90.jpg', '已发布', '1', '25', '2026-05-08 18:41:00', '2026-05-08 18:41:00', '0', '0', '0', null);
INSERT INTO `article` VALUES ('33', '123', '<p>1111</p>', 'http://tds79gjsg.hn-bkt.clouddn.com/827eed73-6621-4cb2-b4a6-36de34d88641.jpg', '已发布', '1', '27', '2026-05-08 18:51:00', '2026-05-08 18:51:09', '0', '0', '0', null);
INSERT INTO `article` VALUES ('34', '66', '<p>11</p>', 'http://tds79gjsg.hn-bkt.clouddn.com/129fc715-8c44-442c-b3b7-e7865c34eb69.jpg', '草稿', '1', '28', '2026-05-08 18:52:44', '2026-05-08 18:52:49', '0', '0', '0', null);
INSERT INTO `article` VALUES ('35', '1236', '<p>12333</p>', 'http://tds79gjsg.hn-bkt.clouddn.com/58b5acfe-6e91-4593-ba88-4f2537fd2854.jpg', '草稿', '1', '30', '2026-05-08 19:00:18', '2026-05-08 19:00:25', '0', '0', '0', null);
INSERT INTO `article` VALUES ('36', '123', '<p><strong><em>222</em></strong></p>', 'http://tds79gjsg.hn-bkt.clouddn.com/f8cdc00e-6837-404f-a24d-d72afaa92e6e.jpg', '已发布', '1', '31', '2026-05-08 19:10:07', '2026-05-08 19:10:07', '0', '0', '0', null);
INSERT INTO `article` VALUES ('37', '美食', '<p>好好的美食在xxxx....</p>', 'http://tds79gjsg.hn-bkt.clouddn.com/7c0b42dc-16cd-4204-8137-3e869a4089ea.jpg', '已发布', '1', '29', '2026-05-10 10:12:06', '2026-05-10 10:19:13', '0', '0', '0', null);

-- ----------------------------
-- Table structure for category
-- ----------------------------
DROP TABLE IF EXISTS `category`;
CREATE TABLE `category` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `category_name` varchar(32) NOT NULL COMMENT '分类名称',
  `category_alias` varchar(32) NOT NULL COMMENT '分类别名',
  `create_user` int(10) unsigned NOT NULL COMMENT '创建人ID',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`),
  KEY `fk_category_user` (`create_user`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of category
-- ----------------------------
INSERT INTO `category` VALUES ('1', '美食', '111', '4', '2025-06-30 19:31:05', '2026-05-08 18:43:38');
INSERT INTO `category` VALUES ('3', '人文', 'renwen', '4', '2025-06-30 19:32:11', '2025-06-30 23:49:18');
INSERT INTO `category` VALUES ('5', '旅游12', 'lyou', '4', '2025-06-30 23:48:44', '2025-07-11 17:03:36');
INSERT INTO `category` VALUES ('6', 'AI', 'AI', '6', '2025-12-19 21:57:06', '2025-07-02 21:57:13');
INSERT INTO `category` VALUES ('12', '旅游', 'ly', '9', '2025-11-28 16:29:29', '2025-07-07 16:29:34');
INSERT INTO `category` VALUES ('13', '风景', 'Fj', '11', '2025-12-14 17:03:24', '2026-04-20 15:25:46');
INSERT INTO `category` VALUES ('14', '123', '111', '26', '2026-05-08 18:45:04', '2026-05-08 18:45:04');

-- ----------------------------
-- Table structure for reading_history
-- ----------------------------
DROP TABLE IF EXISTS `reading_history`;
CREATE TABLE `reading_history` (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `user_id` int(20) unsigned NOT NULL,
  `article_id` int(20) unsigned NOT NULL,
  `read_time` datetime DEFAULT NULL,
  `read_duration` int(255) DEFAULT NULL,
  `read_type` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  KEY `article_id` (`article_id`),
  CONSTRAINT `reading_history_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
  CONSTRAINT `reading_history_ibfk_2` FOREIGN KEY (`article_id`) REFERENCES `article` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=53 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of reading_history
-- ----------------------------
INSERT INTO `reading_history` VALUES ('17', '9', '9', '2025-07-24 15:24:56', '0', '1');
INSERT INTO `reading_history` VALUES ('18', '9', '17', '2025-07-24 15:25:22', '0', '1');
INSERT INTO `reading_history` VALUES ('19', '9', '19', '2025-07-24 16:19:34', '0', '1');
INSERT INTO `reading_history` VALUES ('23', '6', '18', '2026-05-06 20:02:20', '2', null);
INSERT INTO `reading_history` VALUES ('24', '6', '19', '2026-05-06 20:04:48', '2', null);
INSERT INTO `reading_history` VALUES ('25', '6', '17', '2026-05-04 18:52:16', '1', '1');
INSERT INTO `reading_history` VALUES ('26', '6', '2', '2026-05-04 19:44:31', '1', '1');
INSERT INTO `reading_history` VALUES ('27', '6', '15', '2026-05-04 19:28:22', '1', '1');
INSERT INTO `reading_history` VALUES ('29', '11', '17', '2026-05-02 20:10:30', '1', '1');
INSERT INTO `reading_history` VALUES ('30', '11', '15', '2026-05-02 19:57:47', '1', '1');
INSERT INTO `reading_history` VALUES ('31', '11', '2', '2026-05-02 20:07:04', '1', '1');
INSERT INTO `reading_history` VALUES ('32', '11', '19', '2026-05-02 19:45:05', '2', null);
INSERT INTO `reading_history` VALUES ('33', '11', '18', '2026-05-02 18:58:11', '5', null);
INSERT INTO `reading_history` VALUES ('34', '6', '25', '2026-05-04 11:33:32', '2', '1');
INSERT INTO `reading_history` VALUES ('35', '19', '17', '2026-05-08 07:50:08', '5', '1');
INSERT INTO `reading_history` VALUES ('36', '19', '18', '2026-05-08 07:50:45', '3', '1');
INSERT INTO `reading_history` VALUES ('37', '19', '19', '2026-05-08 07:50:51', '1', '1');
INSERT INTO `reading_history` VALUES ('38', '20', '25', '2026-05-08 07:52:20', '3', '1');
INSERT INTO `reading_history` VALUES ('39', '22', '2', '2026-05-08 16:13:55', '4', '1');
INSERT INTO `reading_history` VALUES ('40', '22', '15', '2026-05-08 07:59:27', '1', '1');
INSERT INTO `reading_history` VALUES ('41', '22', '17', '2026-05-08 08:13:52', '1', '1');
INSERT INTO `reading_history` VALUES ('46', '27', '25', '2026-05-08 10:50:40', '1', '1');
INSERT INTO `reading_history` VALUES ('48', '30', '19', '2026-05-08 11:00:00', '1', '1');
INSERT INTO `reading_history` VALUES ('51', '29', '15', '2026-05-10 05:28:34', '5', '1');
INSERT INTO `reading_history` VALUES ('52', '29', '18', '2026-05-10 05:28:59', '4', null);

-- ----------------------------
-- Table structure for statice
-- ----------------------------
DROP TABLE IF EXISTS `statice`;
CREATE TABLE `statice` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `article_id` int(10) unsigned NOT NULL,
  `read_date` datetime DEFAULT NULL,
  `daily_reads` int(11) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `week` int(11) DEFAULT NULL,
  `readata` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`,`article_id`),
  KEY `fk_article_read` (`article_id`),
  CONSTRAINT `fk_article_read` FOREIGN KEY (`article_id`) REFERENCES `article` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of statice
-- ----------------------------
INSERT INTO `statice` VALUES ('1', '18', '2025-07-25 14:47:19', '0', '2025-07-20 18:17:14', '2026-05-10 13:28:29', '0', '5');
INSERT INTO `statice` VALUES ('2', '9', '2025-07-24 15:24:55', '0', '2025-07-20 18:17:37', '2026-05-10 13:28:29', '0', '4');
INSERT INTO `statice` VALUES ('3', '15', '2026-05-02 21:35:40', '1', '2025-07-23 17:33:44', '2026-05-10 13:28:29', '0', '4');
INSERT INTO `statice` VALUES ('4', '19', '2026-05-02 20:46:40', '0', '2025-07-23 17:52:48', '2026-05-10 13:28:29', '0', '4');
INSERT INTO `statice` VALUES ('5', '2', '2026-05-04 18:28:20', '0', '2025-07-23 18:29:45', '2026-05-10 13:28:29', '0', '4');
INSERT INTO `statice` VALUES ('6', '17', '2026-05-02 21:30:56', '1', '2025-07-23 18:57:18', '2026-05-10 13:28:29', '0', '5');
INSERT INTO `statice` VALUES ('8', '15', '2026-05-04 00:00:00', '1', '2026-05-04 19:28:19', '2026-05-04 19:28:19', '1', '1');
INSERT INTO `statice` VALUES ('9', '25', '2026-05-04 00:00:00', '1', '2026-05-04 19:33:29', '2026-05-04 19:33:29', '1', '1');
INSERT INTO `statice` VALUES ('10', '2', '2026-05-06 00:00:00', '1', '2026-05-06 20:24:03', '2026-05-06 20:24:03', '1', '1');
INSERT INTO `statice` VALUES ('11', '17', '2026-05-06 00:00:00', '1', '2026-05-06 20:24:08', '2026-05-06 20:24:08', '1', '1');
INSERT INTO `statice` VALUES ('12', '18', '2026-05-08 00:00:00', '1', '2026-05-08 15:50:41', '2026-05-08 15:50:41', '1', '1');
INSERT INTO `statice` VALUES ('13', '19', '2026-05-08 00:00:00', '1', '2026-05-08 15:50:50', '2026-05-08 15:50:50', '1', '1');
INSERT INTO `statice` VALUES ('14', '25', '2026-05-08 00:00:00', '1', '2026-05-08 15:52:17', '2026-05-08 15:52:17', '1', '1');
INSERT INTO `statice` VALUES ('15', '17', '2026-05-08 00:00:00', '1', '2026-05-08 16:13:50', '2026-05-08 16:13:50', '1', '1');
INSERT INTO `statice` VALUES ('16', '2', '2026-05-08 00:00:00', '1', '2026-05-08 16:13:53', '2026-05-08 16:13:53', '1', '1');
INSERT INTO `statice` VALUES ('17', '17', '2026-05-08 00:00:00', '1', '2026-05-08 16:16:55', '2026-05-08 16:16:55', '1', '1');
INSERT INTO `statice` VALUES ('18', '17', '2026-05-08 00:00:00', '1', '2026-05-08 18:36:25', '2026-05-08 18:36:25', '1', '1');
INSERT INTO `statice` VALUES ('19', '17', '2026-05-08 00:00:00', '1', '2026-05-08 18:40:43', '2026-05-08 18:40:43', '1', '1');
INSERT INTO `statice` VALUES ('20', '25', '2026-05-08 00:00:00', '1', '2026-05-08 18:50:38', '2026-05-08 18:50:38', '1', '1');
INSERT INTO `statice` VALUES ('21', '19', '2026-05-08 00:00:00', '1', '2026-05-08 18:52:29', '2026-05-08 18:52:29', '1', '1');
INSERT INTO `statice` VALUES ('22', '19', '2026-05-08 00:00:00', '1', '2026-05-08 18:59:58', '2026-05-08 18:59:58', '1', '1');
INSERT INTO `statice` VALUES ('23', '15', '2026-05-08 00:00:00', '1', '2026-05-08 19:01:09', '2026-05-08 19:01:09', '1', '1');
INSERT INTO `statice` VALUES ('24', '17', '2026-05-08 00:00:00', '1', '2026-05-08 19:09:45', '2026-05-08 19:09:45', '1', '1');
INSERT INTO `statice` VALUES ('25', '15', '2026-05-10 00:00:00', '1', '2026-05-10 13:28:29', '2026-05-10 13:28:29', '1', '1');

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `username` varchar(20) NOT NULL COMMENT '用户名',
  `password` varchar(32) DEFAULT NULL COMMENT '密码',
  `nickname` varchar(10) DEFAULT '' COMMENT '昵称',
  `email` varchar(128) DEFAULT '' COMMENT '邮箱',
  `user_pic` varchar(128) DEFAULT '' COMMENT '头像',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '修改时间',
  `role` varchar(255) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=33 DEFAULT CHARSET=utf8 COMMENT='用户表';

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES ('4', 'wangxing', 'fcea920f7412b5da7be0cf42b8c93759', 'wba', 'wac12356@qq.com', 'http://sypo3c5e8.hn-bkt.clouddn.com/FpO3EilKCP-d2ilNxT-KyaAoZTfn', '2025-10-29 17:18:54', '2025-11-04 23:57:03', '2', '0');
INSERT INTO `user` VALUES ('6', '123456', 'fcea920f7412b5da7be0cf42b8c93759', '123456789', '123456789@qq.com', 'http://tds79gjsg.hn-bkt.clouddn.com/5b9e5df2-26bf-4877-9741-9bee6e525623.jpg', '2025-11-02 17:51:13', '2026-05-08 18:34:29', '2', '1');
INSERT INTO `user` VALUES ('9', '111111', '7fa8282ad93047a4d6fe6111c93b308a', '', '', '', '2025-11-07 16:28:42', '2025-11-07 17:28:42', '2', '1');
INSERT INTO `user` VALUES ('11', 'wang12', '25d55ad283aa400af464c76d713c07ad', '', '', 'http://tds79gjsg.hn-bkt.clouddn.com/c59d0922-99ce-4376-aca2-89da76a6d786.jpg', '2025-11-07 18:15:40', '2026-04-20 16:40:12', '2', '1');
INSERT INTO `user` VALUES ('14', 'wang78', '29ec8e27ef36bfeb33f93cd4434b5080', '', '', '', '2025-11-09 17:59:06', '2025-11-09 17:59:06', '1', '0');
INSERT INTO `user` VALUES ('15', '123456789w', '787222bc43251c9d734eb4365b595ba6', '123', '287639090@qq.com', 'http://sypo3c5e8.hn-bkt.clouddn.com/278c5002-585d-454f-b428-070029071248.jpg', '2025-11-11 16:34:44', '2025-11-11 16:58:38', '1', '1');
INSERT INTO `user` VALUES ('16', '123456ww', 'b437d42941ac34450db956f9ae35ef8a', '1245', '111111141@qq.com', 'http://sypo3c5e8.hn-bkt.clouddn.com/941c0321-4005-4a6b-9fdd-a68c3537617d.jpg', '2025-11-11 16:59:39', '2026-05-06 17:50:48', '1', '1');
INSERT INTO `user` VALUES ('17', '1234567w', '5b51bd55d72d66985bf9b843c2315f56', '', '', '', '2025-11-11 17:05:56', '2026-05-06 17:05:56', '1', '1');
INSERT INTO `user` VALUES ('18', '11111', 'e10adc3949ba59abbe56e057f20f883e', '', '', '', '2026-05-08 15:48:15', '2026-05-08 15:48:15', '1', '1');
INSERT INTO `user` VALUES ('19', '12347', 'fcea920f7412b5da7be0cf42b8c93759', '', '', '', '2026-05-08 15:49:38', '2026-05-08 15:49:38', '1', '1');
INSERT INTO `user` VALUES ('20', '123654', '25d55ad283aa400af464c76d713c07ad', '', '', '', '2026-05-08 15:51:46', '2026-05-08 15:51:46', '1', '1');
INSERT INTO `user` VALUES ('21', '1236549', 'e10adc3949ba59abbe56e057f20f883e', '', '', '', '2026-05-08 15:55:13', '2026-05-08 15:55:13', '1', '1');
INSERT INTO `user` VALUES ('22', '12w12', 'e10adc3949ba59abbe56e057f20f883e', '', '', '', '2026-05-08 15:56:35', '2026-05-08 15:56:35', '1', '1');
INSERT INTO `user` VALUES ('23', '13w13', 'e10adc3949ba59abbe56e057f20f883e', '111', '1222@qq.com', 'http://tds79gjsg.hn-bkt.clouddn.com/cc54dae7-5ce2-4dfa-b936-fddbf638e0be.jpg', '2026-05-08 16:16:35', '2026-05-08 16:26:47', '1', '1');
INSERT INTO `user` VALUES ('24', '121V121', 'fcea920f7412b5da7be0cf42b8c93759', '12345', '2212365@qq.com', 'http://tds79gjsg.hn-bkt.clouddn.com/6de9d067-d765-4a5e-a060-0c4282383fc2.jpg', '2026-05-08 18:36:05', '2026-05-08 18:39:07', '1', '1');
INSERT INTO `user` VALUES ('25', '13V14', 'fcea920f7412b5da7be0cf42b8c93759', '13256', '12456789@qq.com', 'http://tds79gjsg.hn-bkt.clouddn.com/5192c26e-33a5-4ecd-bf51-8927f8df593b.jpg', '2026-05-08 18:40:23', '2026-05-08 18:42:26', '1', '1');
INSERT INTO `user` VALUES ('26', '55555', '96e79218965eb72c92a549dd5a330112', '', '', '', '2026-05-08 18:43:23', '2026-05-08 18:43:23', '2', '1');
INSERT INTO `user` VALUES ('27', '13V11', 'e10adc3949ba59abbe56e057f20f883e', '', '', '', '2026-05-08 18:50:24', '2026-05-08 18:50:24', '1', '1');
INSERT INTO `user` VALUES ('28', '12v11', '25d55ad283aa400af464c76d713c07ad', '1222', '1111235@QQ.COM', 'http://tds79gjsg.hn-bkt.clouddn.com/3b17c4a6-f63d-468d-be8f-cb20a3c6193e.jpg', '2026-05-08 18:52:08', '2026-05-08 18:54:12', '1', '1');
INSERT INTO `user` VALUES ('29', '22222', 'fcea920f7412b5da7be0cf42b8c93759', '', '', 'http://tds79gjsg.hn-bkt.clouddn.com/5b56ade4-b3d4-4095-9f3c-56c1711f39c6.jpg', '2026-05-08 18:55:35', '2026-05-10 12:59:55', '2', '1');
INSERT INTO `user` VALUES ('30', '12345', 'fcea920f7412b5da7be0cf42b8c93759', '', '', '', '2026-05-08 18:59:37', '2026-05-08 18:59:37', '1', '1');
INSERT INTO `user` VALUES ('31', '1234567', 'e10adc3949ba59abbe56e057f20f883e', '1236W', '123456@QQ.COM', 'http://tds79gjsg.hn-bkt.clouddn.com/81e1ab29-8d42-4452-8387-2cfebb6dbed5.jpg', '2026-05-08 19:09:15', '2026-05-08 19:11:57', '1', '0');
INSERT INTO `user` VALUES ('32', '456123', 'e10adc3949ba59abbe56e057f20f883e', '', '', '', '2026-05-08 19:12:29', '2026-05-08 19:12:29', '2', '1');
