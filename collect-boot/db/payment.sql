CREATE TABLE `sb_payment` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  create_by varchar(50) COMMENT '创建者',
	create_date datetime NOT NULL COMMENT '创建时间',
	update_by varchar(50) COMMENT '更新者',
	update_date datetime NOT NULL COMMENT '更新时间',
	remarks varchar(255) COMMENT '备注信息',
	del_flag char(1) DEFAULT '0' NOT NULL COMMENT '删除标记',

  ali_user_id varchar(50) NOT NULL COMMENT '阿里userid',
  `order_sn` varchar(30) NOT NULL COMMENT '订单号',
  `price` decimal(21,6) NOT NULL COMMENT '金额',
  `trade_no` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `notify_url` varchar(2000) COLLATE utf8_bin DEFAULT NULL,
  `notify_time` varchar(30) COLLATE utf8_bin DEFAULT NULL,
  `buyer_id` varchar(120) COLLATE utf8_bin DEFAULT NULL,
  `buyer_logon_id` varchar(120) COLLATE utf8_bin DEFAULT NULL,
  `seller_id` varchar(120) COLLATE utf8_bin DEFAULT NULL,
  `recyclers_id` int(11) ,
  `status` int,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin ROW_FORMAT=COMPACT