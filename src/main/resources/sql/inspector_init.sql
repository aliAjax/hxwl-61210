-- =============================================
-- 巡检人员管理模块 - 数据库脚本
-- 执行日期: 2026-06-13
-- =============================================

-- ---------------------------------------------
-- 1. 创建巡检人员表
-- ---------------------------------------------
DROP TABLE IF EXISTS `inspector`;
CREATE TABLE `inspector` (
    `id`                BIGINT          NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `name`              VARCHAR(50)     NOT NULL COMMENT '巡检人员姓名',
    `phone`             VARCHAR(20)     DEFAULT NULL COMMENT '联系电话',
    `responsible_zone`  VARCHAR(255)    DEFAULT NULL COMMENT '负责展区',
    `enabled`           TINYINT(1)      NOT NULL DEFAULT 1 COMMENT '启用状态: 1-启用, 0-禁用',
    `create_time`       DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`       DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_name` (`name`),
    KEY `idx_phone` (`phone`),
    KEY `idx_enabled` (`enabled`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='巡检人员表';

-- ---------------------------------------------
-- 2. 为巡检记录表添加 inspector_id 字段
--    注意: 请将下方的 `inspection_record` 替换为实际的巡检记录表名
-- ---------------------------------------------
-- ALTER TABLE `inspection_record`
--     ADD COLUMN `inspector_id` BIGINT DEFAULT NULL COMMENT '巡检人员ID' AFTER `id`;

-- 如果需要外键约束（可选，根据项目规范决定是否启用）:
-- ALTER TABLE `inspection_record`
--     ADD CONSTRAINT `fk_inspection_record_inspector`
--     FOREIGN KEY (`inspector_id`) REFERENCES `inspector` (`id`)
--     ON DELETE SET NULL ON UPDATE CASCADE;

-- 添加索引:
-- ALTER TABLE `inspection_record` ADD KEY `idx_inspector_id` (`inspector_id`);

-- ---------------------------------------------
-- 示例数据（可选）
-- ---------------------------------------------
INSERT INTO `inspector` (`name`, `phone`, `responsible_zone`, `enabled`) VALUES
('张三', '13800138001', 'A区-古代文物展区', 1),
('李四', '13800138002', 'B区-近现代史展区', 1),
('王五', '13800138003', 'C区-民俗文化展区', 1),
('赵六', '13800138004', 'A区-古代文物展区', 0),
('钱七', '13800138005', 'D区-科技互动展区', 1);
