CREATE DATABASE IF NOT EXISTS express default charset utf8mb4 COLLATE utf8mb4_general_ci;

USE express;

-- ----------------------------
-- Table structure for order_evaluate
-- ----------------------------
DROP TABLE IF EXISTS `order_evaluate`;
CREATE TABLE `order_evaluate`  (
                                   `id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '订单ID',
                                   `has_open` tinyint(1) NULL DEFAULT 0 COMMENT '评论是否开启（1：开启；0：关闭）',
                                   `user_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户ID',
                                   `user_score` decimal(6, 3) NULL DEFAULT NULL COMMENT '用户评分',
                                   `user_evaluate` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户评价',
                                   `user_date` datetime(0) NULL DEFAULT NULL COMMENT '用户评价时间',
                                   `courier_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '配送员ID',
                                   `courier_score` decimal(6, 3) NULL DEFAULT NULL COMMENT '配送员评分',
                                   `courier_evaluate` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '配送员评价',
                                   `version` int(11) DEFAULT '0',
                                   `courier_date` datetime(0) NULL DEFAULT NULL COMMENT '配送员评价时间',
                                   `update_date` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
                                   PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '订单评价表';

-- ----------------------------
-- Records of order_evaluate
-- ----------------------------
BEGIN;
INSERT INTO `order_evaluate` VALUES ('1120376407025811458', 0, '1', 9.500, '满意', '2019-05-04 12:57:26', 'f10960e7392847a2c691ad066e2a87c4', 7.000, '可以的', 0, '2019-05-04 12:57:12', '2019-05-04 12:57:26'), ('1120377206619549698', 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, NULL, '2019-05-04 12:57:05' ), ( '1120377582852812802', 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, NULL, '2019-05-04 12:57:05' ), ( '1120378178322362370', 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, NULL, '2019-05-04 12:57:05'), ('1120378688504975362', 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, NULL, '2019-05-04 12:57:05'), ('1120379705921404930', 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, NULL, '2019-05-04 12:57:20'), ('1121054404204625921', 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, NULL, '2019-05-04 12:57:05'), ('1124573211875373058', 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, NULL, '2019-05-04 15:16:08');
COMMIT;