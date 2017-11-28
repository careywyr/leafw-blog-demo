CREATE TABLE `user_info` (
  `user_id` varchar(50) NOT NULL COMMENT 'id',
  `login_name` varchar(100) DEFAULT NULL COMMENT '登录用户名',
  `password` varchar(50) DEFAULT NULL COMMENT '密码',
  `email` varchar(200) DEFAULT NULL COMMENT '邮箱',
  `create_by` varchar(30) DEFAULT NULL COMMENT '创建人',
  `create_time` timestamp NULL DEFAULT NULL,
  `update_by` varchar(30) DEFAULT NULL COMMENT '修改人',
  `update_time` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户表';