/*
Navicat MySQL Data Transfer

Source Server         : local
Source Server Version : 50713
Source Host           : localhost:3306
Source Database       : nutz

Target Server Type    : MYSQL
Target Server Version : 50713
File Encoding         : 65001

Date: 2017-02-04 11:47:41
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for TB_AT_USER
-- ----------------------------
DROP TABLE IF EXISTS `TB_AT_USER`;
CREATE TABLE `TB_AT_USER` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(60) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `BIRTH` datetime DEFAULT NULL,
  `AGE` int(11) DEFAULT NULL,
  `EMAIL` varchar(30) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
