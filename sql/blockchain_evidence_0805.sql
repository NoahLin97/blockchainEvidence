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

 Date: 05/08/2021 11:39:49
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for aut_manager
-- ----------------------------
DROP TABLE IF EXISTS `aut_manager`;
CREATE TABLE `aut_manager`  (
  `autManId` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `autName` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `phoneNumber` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `idCard` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '密文',
  `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `sex` enum('0','1') CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `organizationId` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '外键',
  PRIMARY KEY (`autManId`) USING BTREE,
  INDEX `organization`(`organizationId`) USING BTREE,
  CONSTRAINT `organization` FOREIGN KEY (`organizationId`) REFERENCES `organization` (`organizationId`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of aut_manager
-- ----------------------------
INSERT INTO `aut_manager` VALUES ('1', 'Sigrdrifa', NULL, NULL, NULL, NULL, '1', '1');
INSERT INTO `aut_manager` VALUES ('2', 'Brynhildr', NULL, NULL, NULL, NULL, '1', '2');

-- ----------------------------
-- Table structure for evidence
-- ----------------------------
DROP TABLE IF EXISTS `evidence`;
CREATE TABLE `evidence`  (
  `evidenceId` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `userId` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '外键',
  `evidenceType` enum('0','1','2','3','4','5','6') CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `evidenceName` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '密文',
  `filePath` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `fileSize` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '密文',
  `fileHash` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `evidenceBlockchainId` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `blockchainTime` datetime(0) NULL DEFAULT NULL,
  `evidenceTime` datetime(0) NULL DEFAULT NULL,
  `organizationId` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '外键',
  `notaryId` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '外键',
  `notarizationStatus` enum('0','1','2','3','4') CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `transactionID` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `notarizationStartTime` datetime(0) NULL DEFAULT NULL,
  `notarizationMoney` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '密文',
  `notarizationType` enum('0','1','2','3','4','5','6') CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `notarizationInformation` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `notarizationEndTime` datetime(0) NULL DEFAULT NULL,
  `notarizationMatters` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `notarizationBlockchainIdStart` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `notarizationBlockchainIdEnd` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `transactionStatus` enum('0','1') CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`evidenceId`) USING BTREE,
  INDEX `userId`(`userId`) USING BTREE,
  INDEX `organizationId`(`organizationId`) USING BTREE,
  INDEX `notaryId`(`notaryId`) USING BTREE,
  INDEX `transactionID`(`transactionID`) USING BTREE,
  CONSTRAINT `evidence_ibfk_1` FOREIGN KEY (`userId`) REFERENCES `user` (`userId`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `evidence_ibfk_2` FOREIGN KEY (`organizationId`) REFERENCES `organization` (`organizationId`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `evidence_ibfk_3` FOREIGN KEY (`notaryId`) REFERENCES `notary` (`notaryId`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `evidence_ibfk_4` FOREIGN KEY (`transactionID`) REFERENCES `transaction` (`transactionId`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of evidence
-- ----------------------------
INSERT INTO `evidence` VALUES ('1', '1', '1', 'Agares', NULL, NULL, NULL, NULL, NULL, NULL, '1', '1', '0', '1', '2021-08-03 19:38:19', '125', '2', NULL, '2021-08-19 19:38:35', NULL, NULL, NULL, NULL);
INSERT INTO `evidence` VALUES ('2', '1', '2', 'Barbatos', NULL, NULL, NULL, NULL, NULL, NULL, '1', NULL, '1', '1', '2021-07-31 11:24:57', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `evidence` VALUES ('3', '2', '1', 'Bael', NULL, NULL, NULL, NULL, NULL, NULL, '2', '5', '0', NULL, '2021-08-04 11:24:51', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `evidence` VALUES ('4', '1', '3', 'Cimeries', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);

-- ----------------------------
-- Table structure for manager
-- ----------------------------
DROP TABLE IF EXISTS `manager`;
CREATE TABLE `manager`  (
  `manId` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `phoneNumber` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `idCard` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '密文',
  `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `sex` enum('0','1') CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`manId`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of manager
-- ----------------------------

-- ----------------------------
-- Table structure for notary
-- ----------------------------
DROP TABLE IF EXISTS `notary`;
CREATE TABLE `notary`  (
  `notaryId` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `notaryName` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `jobNumber` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `phoneNumber` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `idCard` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '密文',
  `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `sex` enum('0','1') CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `organizationId` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '外键',
  `notarizationType` enum('0','1','2','3','4','5','6') CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`notaryId`) USING BTREE,
  INDEX `organizationId`(`organizationId`) USING BTREE,
  CONSTRAINT `organizationId` FOREIGN KEY (`organizationId`) REFERENCES `organization` (`organizationId`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of notary
-- ----------------------------
INSERT INTO `notary` VALUES ('1', 'Nephthys', NULL, NULL, NULL, NULL, NULL, '1', '1', '0');
INSERT INTO `notary` VALUES ('2', 'Osiris', NULL, NULL, NULL, NULL, NULL, '0', '3', '1');
INSERT INTO `notary` VALUES ('3', 'Nut', NULL, NULL, NULL, NULL, NULL, '0', '2', '1');
INSERT INTO `notary` VALUES ('4', 'Horus', NULL, NULL, NULL, NULL, NULL, '0', '1', '0');
INSERT INTO `notary` VALUES ('5', 'Amon', NULL, NULL, NULL, NULL, NULL, '0', '2', '2');
INSERT INTO `notary` VALUES ('6', 'Medjed', NULL, NULL, NULL, NULL, NULL, '1', '1', '1');

-- ----------------------------
-- Table structure for notary_statistics
-- ----------------------------
DROP TABLE IF EXISTS `notary_statistics`;
CREATE TABLE `notary_statistics`  (
  `notaryStatisticsId` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `notstyId` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '外键',
  `notaryName` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `organizationName` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `notarizationCount` int(0) NULL DEFAULT NULL,
  `notarizationTotalMoney` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '密文',
  `notarizationSuccessCount` int(0) NULL DEFAULT NULL,
  `notarizationType` enum('0','1','2','3','4','5','6') CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `timeFlag` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`notaryStatisticsId`) USING BTREE,
  INDEX `notstyId`(`notstyId`) USING BTREE,
  CONSTRAINT `notary_statistics_ibfk_1` FOREIGN KEY (`notstyId`) REFERENCES `notary` (`notaryId`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of notary_statistics
-- ----------------------------

-- ----------------------------
-- Table structure for organization
-- ----------------------------
DROP TABLE IF EXISTS `organization`;
CREATE TABLE `organization`  (
  `organizationId` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `organizationName` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `phoneNumber` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `legalPeople` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`organizationId`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of organization
-- ----------------------------
INSERT INTO `organization` VALUES ('1', 'R\'lyeh', NULL, NULL, NULL, NULL);
INSERT INTO `organization` VALUES ('2', 'Arkham', NULL, NULL, NULL, NULL);
INSERT INTO `organization` VALUES ('3', 'Dunwich', NULL, NULL, NULL, NULL);
INSERT INTO `organization` VALUES ('4', 'Innsmouth', NULL, NULL, NULL, NULL);

-- ----------------------------
-- Table structure for organization_statistics
-- ----------------------------
DROP TABLE IF EXISTS `organization_statistics`;
CREATE TABLE `organization_statistics`  (
  `organizationStatisticsId` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `organizationId` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '外键',
  `organizationName` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `notarizationCount` int(0) NULL DEFAULT NULL,
  `notarizationTotalMoney` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '密文',
  `notarizationSuccessCount` int(0) NULL DEFAULT NULL,
  `notstyCount` int(0) NULL DEFAULT NULL,
  `timeFlag` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`organizationStatisticsId`) USING BTREE,
  INDEX `organizationId`(`organizationId`) USING BTREE,
  CONSTRAINT `organization_statistics_ibfk_1` FOREIGN KEY (`organizationId`) REFERENCES `organization` (`organizationId`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of organization_statistics
-- ----------------------------

-- ----------------------------
-- Table structure for rank
-- ----------------------------
DROP TABLE IF EXISTS `rank`;
CREATE TABLE `rank`  (
  `rankId` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `notstyId` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '外键',
  `notaryName` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `notarizationCount` int(0) NULL DEFAULT NULL,
  `notaryRank` int(0) NULL DEFAULT NULL,
  `timeFlag` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`rankId`) USING BTREE,
  INDEX `notstyId`(`notstyId`) USING BTREE,
  CONSTRAINT `rank_ibfk_1` FOREIGN KEY (`notstyId`) REFERENCES `notary` (`notaryId`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of rank
-- ----------------------------

-- ----------------------------
-- Table structure for transaction
-- ----------------------------
DROP TABLE IF EXISTS `transaction`;
CREATE TABLE `transaction`  (
  `transactionId` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `userId` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '外键',
  `userRemains` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '密文',
  `transactionMoney` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '密文',
  `transactionPeople` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '密文',
  `transactionType` enum('0','1','2','3','4') CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `storageSize` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '密文',
  `transactionTime` datetime(0) NULL DEFAULT NULL,
  `transactionBlockchainId` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `blockchainTime` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`transactionId`) USING BTREE,
  INDEX `userId`(`userId`) USING BTREE,
  CONSTRAINT `transaction_ibfk_1` FOREIGN KEY (`userId`) REFERENCES `user` (`userId`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of transaction
-- ----------------------------
INSERT INTO `transaction` VALUES ('1', '1', '200', '34', NULL, '1', NULL, NULL, NULL, NULL);

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `userId` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `phoneNumber` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `idCard` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '密文',
  `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `sex` enum('0','1') CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `remains` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '密文',
  `storageSpace` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '密文',
  `publicKey` varchar(2000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `hasUsedStorage` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '密文',
  PRIMARY KEY (`userId`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES ('1', 'Gawaine', NULL, NULL, NULL, NULL, '0', '200', '300', NULL, '157');
INSERT INTO `user` VALUES ('2', 'Lancelot·Du·Lake', NULL, NULL, NULL, NULL, '0', '250', '250', NULL, '123');
INSERT INTO `user` VALUES ('3', 'Guinevere', NULL, NULL, NULL, NULL, '1', '150', '300', NULL, '233');
INSERT INTO `user` VALUES ('4', 'Artoria ', NULL, NULL, NULL, NULL, '1', '340', '444', NULL, '297');
INSERT INTO `user` VALUES ('5', 'Mordred', NULL, NULL, NULL, NULL, '0', '300', '500', NULL, '349');
INSERT INTO `user` VALUES ('6', 'Merlin', NULL, NULL, NULL, NULL, '0', '100', '600', NULL, '147');
INSERT INTO `user` VALUES ('7', 'Morgan', NULL, NULL, NULL, NULL, '1', '200', '400', NULL, '379');

SET FOREIGN_KEY_CHECKS = 1;
