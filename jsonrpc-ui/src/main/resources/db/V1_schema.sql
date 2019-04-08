CREATE DATABASE `jsonrpc-ui` /*!40100 DEFAULT CHARACTER SET utf8 */;

CREATE TABLE `timer` (
  `id` bigint(11) unsigned NOT NULL AUTO_INCREMENT,
  `host` varchar(64) NOT NULL COMMENT '主机',
  `port` int(11) NOT NULL COMMENT '端口',
  `method` varchar(128) NOT NULL COMMENT '方法',
  `count` bigint(11) unsigned NOT NULL COMMENT '计数',
  `one_minute_rate` bigint(11) unsigned NOT NULL COMMENT '1分钟QPS',
  `five_minute_rate` bigint(11) unsigned NOT NULL COMMENT '5分钟QPS',
  `fifteen_minute_rate` bigint(11) unsigned NOT NULL COMMENT '15分钟QPS',
  `ct` varchar(64) NOT NULL COMMENT '分钟',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;