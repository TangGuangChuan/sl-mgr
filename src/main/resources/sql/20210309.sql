CREATE TABLE `sl_device_no` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `device_id` int(11) DEFAULT NULL COMMENT '设备ID',
  `device_no` varchar(255) DEFAULT NULL COMMENT '设备编号',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

ALTER TABLE `sl_device_no`
ADD INDEX `sl_device_no_index` (`device_id`, `device_no`) ;

