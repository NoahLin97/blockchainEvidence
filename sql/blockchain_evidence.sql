/*
 Navicat Premium Data Transfer

 Source Server         : 127.0.0.1
 Source Server Type    : MySQL
 Source Server Version : 80026
 Source Host           : localhost:3306
 Source Schema         : blockchain_evidence

 Target Server Type    : MySQL
 Target Server Version : 80026
 File Encoding         : 65001

 Date: 01/08/2021 15:18:51
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for evidence
-- ----------------------------
DROP TABLE IF EXISTS `evidence`;
CREATE TABLE `evidence`  (
  `evitable_id` int(0) UNSIGNED NOT NULL AUTO_INCREMENT,
  `user_id` int(0) NOT NULL,
  `evidence_type` enum('1','2','3') CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `evidence_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `evidence_time` datetime(0) NULL DEFAULT NULL,
  `organization_id` int(0) NULL DEFAULT NULL,
  `notary_id` int(0) NULL DEFAULT NULL,
  `notarization_status` enum('1','2','3') CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `notarization_start_time` datetime(0) NULL DEFAULT NULL,
  `notarization_money` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `notarization_type` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `notarization_information` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `notarization _end_time` datetime(0) NULL DEFAULT NULL,
  `notarization_ matters` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`evitable_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of evidence
-- ----------------------------
INSERT INTO `evidence` VALUES (1, 1, '1', '不在场证明', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `evidence` VALUES (2, 1, '2', '杀人凶器', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);

-- ----------------------------
-- Table structure for notary
-- ----------------------------
DROP TABLE IF EXISTS `notary`;
CREATE TABLE `notary`  (
  `notaryid` int(0) UNSIGNED NOT NULL AUTO_INCREMENT,
  `notaryname` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `organizationid` int(0) NOT NULL,
  `notarizationtype` enum('1','2','3') CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  PRIMARY KEY (`notaryid`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of notary
-- ----------------------------
INSERT INTO `notary` VALUES (1, '公证员1', 1, '1');

-- ----------------------------
-- Table structure for organization
-- ----------------------------
DROP TABLE IF EXISTS `organization`;
CREATE TABLE `organization`  (
  `organization_id` int(0) UNSIGNED NOT NULL AUTO_INCREMENT,
  `organization_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  PRIMARY KEY (`organization_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of organization
-- ----------------------------
INSERT INTO `organization` VALUES (1, '福州大学');
INSERT INTO `organization` VALUES (2, '福州公证处');

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `userid` int(0) UNSIGNED NOT NULL AUTO_INCREMENT,
  `username` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `remains` int(0) NULL DEFAULT NULL,
  `storagespace` int(0) NULL DEFAULT NULL,
  PRIMARY KEY (`userid`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (1, '用户1', 2333, 6777);

SET FOREIGN_KEY_CHECKS = 1;
