/*
SQLyog Ultimate v11.27 (32 bit)
MySQL - 5.7.21-log : Database - devices
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`devices` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `devices`;

/*Table structure for table `hp_devices` */

DROP TABLE IF EXISTS `hp_devices`;

CREATE TABLE `hp_devices` (
  `dev_id` varchar(32) NOT NULL COMMENT '设备ID',
  `serial` varchar(64) NOT NULL COMMENT '物联网终端唯一识别码',
  `alias` varchar(32) NOT NULL COMMENT '编号或者别名',
  `device_type` varchar(32) NOT NULL COMMENT '设备类型,来自于设备类型列表下拉',
  `tenant_id` varchar(32) NOT NULL COMMENT '租户ID',
  `branch_id` varchar(32) NOT NULL COMMENT '分支ID',
  `location` varchar(255) DEFAULT NULL COMMENT '位置',
  PRIMARY KEY (`dev_id`),
  KEY `serial` (`serial`),
  KEY `alias` (`alias`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `hp_devices` */

/*Table structure for table `hp_devices_alarm` */

DROP TABLE IF EXISTS `hp_devices_alarm`;

CREATE TABLE `hp_devices_alarm` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '处理ID',
  `device_type` varchar(32) DEFAULT NULL COMMENT '传感器类型',
  `serial` varchar(32) NOT NULL COMMENT '物联网终端唯一识别码',
  `tenant_id` varchar(32) NOT NULL COMMENT '租户ID',
  `attime` int(11) NOT NULL COMMENT '告警时间',
  `processtime` int(11) NOT NULL COMMENT '处理时间',
  `status` int(11) NOT NULL COMMENT '处理状态：0-告警已上报 1-告警正在处理 2-告警处理完成',
  `alarm_val` varchar(32) NOT NULL COMMENT '发生告警的当时值',
  `except_type` int(11) NOT NULL DEFAULT '0' COMMENT '异常类型 0：数据异常 1：设备异常',
  `except_level` int(11) DEFAULT NULL COMMENT '异常级别 1：1级 2：2级 3：3级   当except_type为0时，设置',
  PRIMARY KEY (`id`),
  KEY `tenant_id` (`tenant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='设备告警';

/*Data for the table `hp_devices_alarm` */

/*Table structure for table `hp_devices_alarm_logs` */

DROP TABLE IF EXISTS `hp_devices_alarm_logs`;

CREATE TABLE `hp_devices_alarm_logs` (
  `log_id` varchar(32) NOT NULL COMMENT '日志ID',
  `tenant_id` varchar(32) NOT NULL DEFAULT '*' COMMENT '租户ID',
  `device_type` varchar(32) NOT NULL COMMENT '设备类型',
  `serial` varchar(64) NOT NULL COMMENT '物联网终端唯一识别码',
  `attime` date NOT NULL COMMENT '告警时间',
  `content` varchar(1024) NOT NULL COMMENT '告警内容',
  `status` int(11) NOT NULL COMMENT '处理状态：0-告警已上报，1-告警正在处理，2-告警处理完成',
  PRIMARY KEY (`log_id`),
  KEY `tenant_id` (`tenant_id`),
  KEY `device_type` (`device_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `hp_devices_alarm_logs` */

/*Table structure for table `hp_devices_alarm_rules` */

DROP TABLE IF EXISTS `hp_devices_alarm_rules`;

CREATE TABLE `hp_devices_alarm_rules` (
  `rule_id` varchar(32) NOT NULL COMMENT '规则ID',
  `tenant_id` varchar(32) NOT NULL DEFAULT '*' COMMENT '租户ID',
  `branch_id` varchar(32) NOT NULL DEFAULT '*' COMMENT '分支ID',
  `serial` varchar(64) NOT NULL DEFAULT '*' COMMENT '物联网终端唯一识别码',
  `device_type` varchar(32) NOT NULL COMMENT '设备类型',
  `action` int(11) NOT NULL DEFAULT '0' COMMENT '告警动作，0x00-推送通知，0x01-发送短信，0x02-发送邮件',
  `always` int(11) NOT NULL DEFAULT '0' COMMENT '是否一直发送，0-否，1-是',
  `escalate` int(11) NOT NULL DEFAULT '0' COMMENT '是否反馈给上一级，0-否，1-是',
  `sms_id` varchar(32) DEFAULT NULL COMMENT '短信ID 当action为0x01时，指定',
  `resend_duration` int(11) DEFAULT '30' COMMENT '重发周期，单位秒，默认30',
  `resendCount` int(11) NOT NULL DEFAULT '3' COMMENT '重发次数，缺省为3 如果resendCount重发次数达到边界，需要视escalate是否为1，启动向上一级的反馈',
  PRIMARY KEY (`rule_id`),
  KEY `tenant_id` (`tenant_id`),
  KEY `branch_id` (`branch_id`),
  KEY `serial` (`serial`),
  KEY `device_type` (`device_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `hp_devices_alarm_rules` */

/*Table structure for table `hp_devices_collection` */

DROP TABLE IF EXISTS `hp_devices_collection`;

CREATE TABLE `hp_devices_collection` (
  `collect_id` varchar(32) NOT NULL COMMENT '列表ID',
  `device_type` varchar(32) NOT NULL COMMENT '设备类型',
  `tenant_id` varchar(32) NOT NULL COMMENT '租户ID',
  `name` varchar(255) NOT NULL COMMENT '设备名称',
  `meta_device_id` varchar(32) DEFAULT NULL COMMENT '设备元数据ID',
  PRIMARY KEY (`collect_id`),
  UNIQUE KEY `device_type` (`device_type`),
  KEY `device_type_2` (`device_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `hp_devices_collection` */

/*Table structure for table `hp_devices_maintains_employee` */

DROP TABLE IF EXISTS `hp_devices_maintains_employee`;

CREATE TABLE `hp_devices_maintains_employee` (
  `device_me_id` varchar(32) NOT NULL COMMENT '设备维保人员ID',
  `tenant_id` varchar(32) NOT NULL COMMENT '租户ID',
  `device_type` varchar(32) NOT NULL COMMENT '设备类型',
  `employee_id` varchar(32) NOT NULL COMMENT '维保人员ID',
  `action` int(11) NOT NULL DEFAULT '0' COMMENT '维保动作，0x00-发送短信，0x01-发送邮件',
  PRIMARY KEY (`device_me_id`),
  KEY `tenant_id` (`tenant_id`),
  KEY `device_type` (`device_type`),
  KEY `employee_id` (`employee_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `hp_devices_maintains_employee` */

/*Table structure for table `hp_devices_mfs` */

DROP TABLE IF EXISTS `hp_devices_mfs`;

CREATE TABLE `hp_devices_mfs` (
  `mf_id` varchar(32) NOT NULL COMMENT '制造商ID',
  `name` varchar(255) NOT NULL COMMENT '制造商名称',
  `location` varchar(1024) DEFAULT NULL COMMENT '制造商地址',
  PRIMARY KEY (`mf_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `hp_devices_mfs` */

/*Table structure for table `hp_devices_models` */

DROP TABLE IF EXISTS `hp_devices_models`;

CREATE TABLE `hp_devices_models` (
  `model_id` varchar(32) NOT NULL COMMENT '型号ID',
  `name` varchar(32) NOT NULL COMMENT '型号名称',
  `description` varchar(1024) DEFAULT NULL COMMENT '型号描述',
  PRIMARY KEY (`model_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `hp_devices_models` */

/*Table structure for table `hp_maintains_company` */

DROP TABLE IF EXISTS `hp_maintains_company`;

CREATE TABLE `hp_maintains_company` (
  `mtc_id` varchar(32) NOT NULL COMMENT '维保单位ID',
  `tenant_id` varchar(32) NOT NULL COMMENT '租户ID',
  `name` varchar(255) NOT NULL COMMENT '维保单位名称',
  `location` varchar(1024) DEFAULT NULL COMMENT '维保单位地址',
  PRIMARY KEY (`mtc_id`),
  KEY `tenant_id` (`tenant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `hp_maintains_company` */

/*Table structure for table `hp_maintains_employee` */

DROP TABLE IF EXISTS `hp_maintains_employee`;

CREATE TABLE `hp_maintains_employee` (
  `employee_id` varchar(32) NOT NULL COMMENT '维保人员ID',
  `tenant_id` varchar(32) NOT NULL COMMENT '租户ID',
  `mtc_id` varchar(32) NOT NULL COMMENT '维保单位ID',
  `name` varchar(32) NOT NULL COMMENT '维保人员名称',
  `phone` varchar(32) DEFAULT NULL COMMENT '维保人员手机号码',
  `mail` varchar(255) DEFAULT NULL COMMENT '维保人员邮箱',
  PRIMARY KEY (`employee_id`),
  KEY `tenant_id` (`tenant_id`),
  KEY `mtc_id` (`mtc_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `hp_maintains_employee` */

/*Table structure for table `hp_tenants_devices_collections` */

DROP TABLE IF EXISTS `hp_tenants_devices_collections`;

CREATE TABLE `hp_tenants_devices_collections` (
  `collect_id` varchar(32) NOT NULL COMMENT '列表ID',
  `device_type` varchar(32) NOT NULL COMMENT '设备类型',
  `name` varchar(255) NOT NULL COMMENT '设备名称',
  `tenant_id` varchar(32) NOT NULL COMMENT '租户ID',
  PRIMARY KEY (`collect_id`),
  KEY `device_type` (`device_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `hp_tenants_devices_collections` */

/*Table structure for table `hp_users` */

DROP TABLE IF EXISTS `hp_users`;

CREATE TABLE `hp_users` (
  `user_id` varchar(32) NOT NULL COMMENT '用户ID',
  `tenant_id` varchar(32) DEFAULT NULL COMMENT '租户id，索引',
  `branch_id` varchar(32) DEFAULT NULL COMMENT '分支id，索引',
  `dep_id` varchar(32) DEFAULT NULL COMMENT '部门id，索引',
  `job_id` varchar(32) DEFAULT NULL COMMENT '岗位id，索引',
  `name` varchar(32) NOT NULL COMMENT '成员姓名',
  `portrait` varchar(1024) DEFAULT NULL COMMENT '用户头像url',
  `role` int(4) NOT NULL COMMENT '成员角色0:超级管理员,1:管理员,2:运营人员,3:用户',
  `is_notice` tinyint(1) DEFAULT '0' COMMENT '是否接受通知 0:否,1:是',
  `is_alert` tinyint(1) DEFAULT '0' COMMENT '是否接受预警 0:否,1:是',
  `status` int(4) NOT NULL COMMENT '成员状态:0:待激活,1:可用,2:停用',
  `create_date` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '成员创建时间',
  `last_auth_date` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '最后一次登陆时间',
  `password` varchar(128) DEFAULT NULL COMMENT '成员密码,sha1(salt + sha1(password))',
  `slat` varchar(128) DEFAULT NULL COMMENT '成员密码盐值,6位随机数',
  `phone` varchar(16) DEFAULT NULL COMMENT '成员手机号码',
  `email` varchar(64) DEFAULT NULL COMMENT '成员邮箱',
  `extra` text COMMENT '附加信息',
  `id_card` varchar(32) DEFAULT NULL COMMENT '身份证号码',
  `address` varchar(1024) DEFAULT NULL COMMENT '家庭地址',
  `signature` varchar(255) DEFAULT NULL COMMENT '个性签名',
  `wx_nick` varchar(255) DEFAULT NULL COMMENT '微信号',
  `wx_id` varchar(255) DEFAULT NULL COMMENT '微信ID',
  `height` int(11) DEFAULT '0' COMMENT '身高',
  `weight` int(11) DEFAULT '0' COMMENT '体重',
  `bmi` float DEFAULT '0' COMMENT 'BMI值',
  `birthday` varchar(255) DEFAULT NULL COMMENT '出生日期',
  `sex` int(11) DEFAULT '0' COMMENT '性别0：女,1：男',
  PRIMARY KEY (`user_id`),
  KEY `tenant_id` (`tenant_id`,`branch_id`,`dep_id`,`wx_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `hp_users` */

insert  into `hp_users`(`user_id`,`tenant_id`,`branch_id`,`dep_id`,`job_id`,`name`,`portrait`,`role`,`is_notice`,`is_alert`,`status`,`create_date`,`last_auth_date`,`password`,`slat`,`phone`,`email`,`extra`,`id_card`,`address`,`signature`,`wx_nick`,`wx_id`,`height`,`weight`,`bmi`,`birthday`,`sex`) values ('c4592b8027444333b50a46169528c991','kpbase',NULL,NULL,NULL,'zhao_weijun','qweqwe',1,0,0,1,'2018-05-28 10:37:35','2018-05-28 10:37:59','274e01c0af877f6028ee8932ae8556edadf80886','59c1e8bca07641bdb3cf225624be6587','18115170737','zhao_weijun@hoperun.com',NULL,NULL,NULL,NULL,NULL,NULL,0,0,0,NULL,1),('c4592b8027444333b50a46169528c992','kpbase',NULL,NULL,NULL,'徐*','https://wx.qlogo.cn/mmopen/vi_32/Q0j4TwGTfTK01va323ic2PThoicd7LQONklicBhNoICcAW3gxtcz7hk3YFQXtIt43yIq9bYOw5gvDQtWicRYrV63Qg/132',3,0,0,1,'2018-05-28 10:37:35','2018-05-28 10:37:59','274e01c0af877f6028ee8932ae8556edadf80886','59c1e8bca07641bdb3cf225624be6587','13952081881','30316737@qq.com','',NULL,NULL,NULL,'徐*','oh89n5G3Cu_cblu_T42QhyIzdxl8',0,0,0,NULL,1),('c4592b8027444333b50a46169528c993','kpbase',NULL,NULL,NULL,'zhu_yongliang','qweqwe',3,0,0,1,'2018-05-28 10:37:35','2018-06-04 14:35:27','274e01c0af877f6028ee8932ae8556edadf80886','59c1e8bca07641bdb3cf225624be6587','15861133151','zhu_yongliang@hoperun.com',NULL,NULL,NULL,NULL,NULL,NULL,185,45,0,'1222-09-09',0);

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
