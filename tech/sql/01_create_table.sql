
CREATE SCHEMA `velrs` DEFAULT CHARACTER SET utf8 ;

CREATE TABLE `velrs`.`t_rule_byte_code` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `rule_id` varchar(32) NOT NULL DEFAULT '' COMMENT '规则id，要求为英文字母，无空格，且全局唯一',
  `project_id` varchar(128) NOT NULL DEFAULT '' COMMENT '项目id',
  `version` int(11) NOT NULL DEFAULT '0' COMMENT '版本号',
  `rule_class_code` mediumtext NOT NULL COMMENT '规则字节码',
  `rule_size` int(11) NOT NULL DEFAULT '0' COMMENT '字节码大小',
  `call_param` varchar(1024) NOT NULL DEFAULT '' COMMENT '运行时请求参数',
  `compile_time` bigint(20) NOT NULL DEFAULT '0' COMMENT '编译时间',
  `last_publish_user_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '最后一次发布人用户id',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uniq_rule_id` (`rule_id`),
  KEY `idx_utime` (`update_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='规则字节码表';

CREATE TABLE `velrs`.`t_rule` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自动递增id',
  `rule_id` varchar(32) NOT NULL DEFAULT '' COMMENT '规则id，要求为英文字母，无空格，且全局唯一',
  `rule_name` varchar(128) NOT NULL DEFAULT '' COMMENT '规则中文名称',
  `project_id` varchar(128) NOT NULL DEFAULT '' COMMENT '规则所属项目ID',
  `rule_status` int(5) NOT NULL DEFAULT '0' COMMENT '规则状态：0:初始（未编辑）;2:编辑中;3:已编译;1:已发布',
  `test_status` int(5) NOT NULL DEFAULT '0' COMMENT '测试状态：0：待测试；1：已测试',
  `compile_time` bigint(20) NOT NULL DEFAULT '0' COMMENT '编译时间，测试类名会取该时间',
  `publish_status` int(5) NOT NULL DEFAULT '0' COMMENT '规则发布状态：0=待发布;1=已发布;',
  `last_edit_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '最后编辑时间',
  `last_edit_user_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '最后编辑用户',
  `last_compile_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '最后编译时间',
  `last_compile_user_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '最后编译用户',
  `last_publish_version` int(11) NOT NULL DEFAULT '0' COMMENT '最后一次发布的版本号（发布的时候才累加）',
  `create_user_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '规则创建人id',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `description` varchar(256) NOT NULL DEFAULT '' COMMENT '规则描述',
  `rule` json NOT NULL COMMENT '当前正在编辑的规则',
  `rule_class_code` mediumtext NOT NULL COMMENT '编译的字节码',
  `call_param` varchar(1024) NOT NULL DEFAULT '' COMMENT '测试请求参数',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uniq_rule_id` (`rule_id`),
  KEY `idx_ctime` (`create_time`),
  KEY `idx_utime` (`update_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='规则表';

CREATE TABLE `velrs`.`t_rule_his` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'PK，自动递增',
  `rule_id` varchar(32) NOT NULL DEFAULT '' COMMENT '规则id',
  `version` int(11) NOT NULL DEFAULT '0' COMMENT '历史版本号',
  `rule` json NOT NULL COMMENT '规则内容',
  `rule_class_code` mediumtext NOT NULL COMMENT '发布后编译成功的java字节码',
  `call_param` varchar(1024) NOT NULL DEFAULT '' COMMENT '运行时请求参数',
  `compile_time` bigint(20) NOT NULL DEFAULT '0' COMMENT '编译时间',
  `create_user_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '规则创建人id',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uniq_ruleid_version` (`rule_id`,`version`),
  KEY `idx_ctime` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='规则历史表';

