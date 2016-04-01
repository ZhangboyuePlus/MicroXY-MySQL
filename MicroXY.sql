-- --------------------------------------------------------
-- 主机:                           127.0.0.1
-- 服务器版本:                        5.6.23-log - MySQL Community Server (GPL)
-- 服务器操作系统:                      Win64
-- HeidiSQL 版本:                  9.2.0.4981
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;

-- 导出 micro_xiyou 的数据库结构
DROP DATABASE IF EXISTS `micro_xiyou`;
CREATE DATABASE IF NOT EXISTS `micro_xiyou` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `micro_xiyou`;


-- 导出  表 micro_xiyou.account 结构
DROP TABLE IF EXISTS `account`;
CREATE TABLE IF NOT EXISTS `account` (
  `Email` varchar(30) NOT NULL,
  `userName` varchar(24) DEFAULT NULL,
  `passWord` varchar(18) DEFAULT NULL,
  PRIMARY KEY (`Email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 正在导出表  micro_xiyou.account 的数据：~0 rows (大约)
/*!40000 ALTER TABLE `account` DISABLE KEYS */;
REPLACE INTO `account` (`Email`, `userName`, `passWord`) VALUES
	('123456@qq.com', '啦啦啦', '123456');
/*!40000 ALTER TABLE `account` ENABLE KEYS */;


-- 导出  表 micro_xiyou.articlesfound 结构
DROP TABLE IF EXISTS `articlesfound`;
CREATE TABLE IF NOT EXISTS `articlesfound` (
  `id` int(11) NOT NULL,
  `title` varchar(60) NOT NULL,
  `publisher` varchar(24) NOT NULL,
  `time` date DEFAULT NULL,
  `state` varchar(1) DEFAULT NULL,
  `kind` varchar(1) DEFAULT NULL,
  `article` varchar(600) DEFAULT NULL,
  `telephoneNumber` varchar(11) DEFAULT NULL,
  `publisherEmail` varchar(30) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 正在导出表  micro_xiyou.articlesfound 的数据：~1 rows (大约)
/*!40000 ALTER TABLE `articlesfound` DISABLE KEYS */;
REPLACE INTO `articlesfound` (`id`, `title`, `publisher`, `time`, `state`, `kind`, `article`, `telephoneNumber`, `publisherEmail`) VALUES
	(1, '提地XP', '了先生', '2015-05-08', '2', '1', '8碰你一定你Dior', '1234567', '123456@qq.com'),
	(2, 'hjfjjjjjfggu', '想先生', '2016-03-01', '1', '1', 'fgujjgyujjbvv拉我女', '13645869995', '123456@qq.com');
/*!40000 ALTER TABLE `articlesfound` ENABLE KEYS */;


-- 导出  表 micro_xiyou.coursetable 结构
DROP TABLE IF EXISTS `coursetable`;
CREATE TABLE IF NOT EXISTS `coursetable` (
  `id` varchar(2) NOT NULL,
  `admissionYear` varchar(4) DEFAULT NULL,
  `professional` varchar(2) DEFAULT NULL,
  `class` varchar(2) DEFAULT NULL,
  `isDoubleWeek` varchar(1) DEFAULT NULL,
  `course` varchar(30) DEFAULT NULL,
  `dayOfWeek` varchar(1) DEFAULT NULL,
  `classOfDay` varchar(1) DEFAULT NULL,
  `building` varchar(1) DEFAULT NULL,
  `classRoom` varchar(3) DEFAULT NULL,
  `teacher` varchar(12) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 正在导出表  micro_xiyou.coursetable 的数据：~8 rows (大约)
/*!40000 ALTER TABLE `coursetable` DISABLE KEYS */;
REPLACE INTO `coursetable` (`id`, `admissionYear`, `professional`, `class`, `isDoubleWeek`, `course`, `dayOfWeek`, `classOfDay`, `building`, `classRoom`, `teacher`) VALUES
	('01', '2012', '01', '02', '1', '通信原理', '1', '1', 'A', '111', '张博玥'),
	('02', '2012', '01', '02', '1', '数电', '1', '2', 'A', '112', '波乐章'),
	('03', '2012', '01', '02', '1', '电路', '1', '3', 'B', '113', 'boyuezhang'),
	('04', '2012', '01', '02', '1', '数据库', '1', '4', 'A', '114', 'ZBY'),
	('05', '2012', '01', '02', '1', '英语', '2', '1', 'C', '211', 'bzy'),
	('06', '2012', '01', '02', '1', '语文', '2', '2', 'B', '212', 'Yzb'),
	('07', '2012', '01', '02', '1', '数学', '2', '3', 'C', '213', 'ybz'),
	('08', '2012', '01', '02', '1', '嵌入式', '3', '2', 'a', '311', 'sb');
/*!40000 ALTER TABLE `coursetable` ENABLE KEYS */;


-- 导出  表 micro_xiyou.meals 结构
DROP TABLE IF EXISTS `meals`;
CREATE TABLE IF NOT EXISTS `meals` (
  `id` int(11) NOT NULL,
  `name` varchar(30) DEFAULT NULL,
  `price` varchar(4) DEFAULT NULL,
  `restaurant` varchar(3) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 正在导出表  micro_xiyou.meals 的数据：~11 rows (大约)
/*!40000 ALTER TABLE `meals` DISABLE KEYS */;
REPLACE INTO `meals` (`id`, `name`, `price`, `restaurant`) VALUES
	(1, '西红柿炒鸡蛋', '7.5', '1'),
	(2, '西红柿', '6', '1'),
	(3, '鸡蛋', '6.5', '2'),
	(4, '鸡蛋炒西红柿', '8', '2'),
	(5, '鸡爪', '7.5', '3'),
	(6, '鸭脖', '12.5', '3'),
	(7, '牛肉', '44', '10'),
	(8, '拉面', '5', '10'),
	(9, '大肉面', '6.5', '11'),
	(10, '小肉面', '5.5', '11'),
	(11, '中肉面', '6', '11');
/*!40000 ALTER TABLE `meals` ENABLE KEYS */;


-- 导出  表 micro_xiyou.recuitment 结构
DROP TABLE IF EXISTS `recuitment`;
CREATE TABLE IF NOT EXISTS `recuitment` (
  `id` int(11) NOT NULL,
  `title` varchar(30) NOT NULL,
  `time` date NOT NULL,
  `readCount` int(11) NOT NULL,
  `article` longtext NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 正在导出表  micro_xiyou.recuitment 的数据：~16 rows (大约)
/*!40000 ALTER TABLE `recuitment` DISABLE KEYS */;
REPLACE INTO `recuitment` (`id`, `title`, `time`, `readCount`, `article`) VALUES
	(1, 'recuitmentTitle 1', '0000-00-00', 2, 'recuitmentContent 1'),
	(2, 'recuitmentTitle 2', '0000-00-00', 2, 'recuitmentContent 2'),
	(3, 'recuitmentTitle 3', '0000-00-00', 2, 'recuitmentContent 3'),
	(4, 'recuitmentTitle 4', '0000-00-00', 2, 'recuitmentContent 4'),
	(5, 'recuitmentTitle 5', '0000-00-00', 2, 'recuitmentContent 5'),
	(6, 'recuitmentTitle 6', '0000-00-00', 2, 'recuitmentContent 6'),
	(7, 'recuitmentTitle 7', '0000-00-00', 2, 'recuitmentContent 7'),
	(8, 'recuitmentTitle 8', '0000-00-00', 2, 'recuitmentContent 8'),
	(9, 'recuitmentTitle 9', '0000-00-00', 2, 'recuitmentContent 9'),
	(10, 'recuitmentTitle 10', '0000-00-00', 2, 'recuitmentContent 10'),
	(11, 'recuitmentTitle 11', '0000-00-00', 2, 'recuitmentContent 11'),
	(12, 'recuitmentTitle 12', '0000-00-00', 2, 'recuitmentContent 12'),
	(13, 'recuitmentTitle 13', '0000-00-00', 2, 'recuitmentContent 13'),
	(14, 'recuitmentTitle 14', '0000-00-00', 2, 'recuitmentContent 14'),
	(15, 'recuitmentTitle 15', '0000-00-00', 2, 'recuitmentContent 15'),
	(16, 'recuitmentTitle 16', '0000-00-00', 2, 'recuitmentContent 16');
/*!40000 ALTER TABLE `recuitment` ENABLE KEYS */;


-- 导出  表 micro_xiyou.restaurant 结构
DROP TABLE IF EXISTS `restaurant`;
CREATE TABLE IF NOT EXISTS `restaurant` (
  `id` int(11) NOT NULL,
  `name` varchar(30) DEFAULT NULL,
  `telephoneNumber` varchar(11) DEFAULT NULL,
  `isInXRY` varchar(1) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 正在导出表  micro_xiyou.restaurant 的数据：~18 rows (大约)
/*!40000 ALTER TABLE `restaurant` DISABLE KEYS */;
REPLACE INTO `restaurant` (`id`, `name`, `telephoneNumber`, `isInXRY`) VALUES
	(1, '1号饭店', '11111111111', '0'),
	(2, '2号饭店', '22222222222', '0'),
	(3, '3号饭店', '33333333333', '0'),
	(4, '4号饭店', '44444444444', '0'),
	(5, '5号饭店', '55555555555', '0'),
	(6, '6号饭店', '66666666666', '0'),
	(7, '7号饭店', '77777777777', '0'),
	(8, '8号饭店', '88888888888', '0'),
	(9, '9号饭店', '99999999999', '0'),
	(10, '10号饭店', '11111111111', '1'),
	(11, '11号饭店', '22222222222', '1'),
	(12, '12号饭店', '33333333333', '1'),
	(13, '13号饭店', '44444444444', '1'),
	(14, '14号饭店', '55555555555', '1'),
	(15, '15号饭店', '66666666666', '1'),
	(16, '16号饭店', '77777777777', '1'),
	(17, '17号饭店', '88888888888', '1'),
	(18, '18号饭店', '99999999999', '1');
/*!40000 ALTER TABLE `restaurant` ENABLE KEYS */;


-- 导出  表 micro_xiyou.schoolannouncement 结构
DROP TABLE IF EXISTS `schoolannouncement`;
CREATE TABLE IF NOT EXISTS `schoolannouncement` (
  `id` int(11) NOT NULL,
  `title` varchar(255) NOT NULL,
  `article` longtext NOT NULL,
  `readCount` int(11) NOT NULL DEFAULT '0',
  `time` date DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 正在导出表  micro_xiyou.schoolannouncement 的数据：~11 rows (大约)
/*!40000 ALTER TABLE `schoolannouncement` DISABLE KEYS */;
REPLACE INTO `schoolannouncement` (`id`, `title`, `article`, `readCount`, `time`) VALUES
	(1, '公告1', 'content公告1', 0, NULL),
	(2, '公告2', 'content公告2', 0, NULL),
	(3, '公告3', 'content公告3', 0, NULL),
	(4, '公告4', 'content公告4', 1, NULL),
	(5, '公告5', 'content公告5', 0, NULL),
	(6, '公告6', 'content公告6', 0, NULL),
	(7, '公告7', 'content公告7', 0, NULL),
	(8, '公告8', 'content公告8', 0, NULL),
	(9, '公告9', 'content公告9', 0, NULL),
	(10, 'gon gao 10', 'content 10 ', 1, NULL),
	(11, 'gongao 11', 'content 11', 1, NULL);
/*!40000 ALTER TABLE `schoolannouncement` ENABLE KEYS */;


-- 导出  表 micro_xiyou.schoolinfo 结构
DROP TABLE IF EXISTS `schoolinfo`;
CREATE TABLE IF NOT EXISTS `schoolinfo` (
  `id` int(11) NOT NULL,
  `title` varchar(255) NOT NULL,
  `article` longtext NOT NULL,
  `readCount` int(11) NOT NULL DEFAULT '0',
  `time` date DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 正在导出表  micro_xiyou.schoolinfo 的数据：~10 rows (大约)
/*!40000 ALTER TABLE `schoolinfo` DISABLE KEYS */;
REPLACE INTO `schoolinfo` (`id`, `title`, `article`, `readCount`, `time`) VALUES
	(1, 'info title 1', '', 0, NULL),
	(2, 'info title 2', '', 0, NULL),
	(3, 'info title 3', '', 0, NULL),
	(4, 'info title 4', '', 0, NULL),
	(5, 'info title 5', '', 0, NULL),
	(6, 'info title 6', '', 0, NULL),
	(7, 'info title 7', '', 0, NULL),
	(8, 'info title 8', '', 1, NULL),
	(9, 'info title 9', '', 0, NULL),
	(10, 'info title 10', '', 1, NULL);
/*!40000 ALTER TABLE `schoolinfo` ENABLE KEYS */;


-- 导出  表 micro_xiyou.schoolnews 结构
DROP TABLE IF EXISTS `schoolnews`;
CREATE TABLE IF NOT EXISTS `schoolnews` (
  `id` int(11) NOT NULL,
  `title` varchar(30) NOT NULL,
  `article` longtext NOT NULL,
  `readCount` int(11) NOT NULL DEFAULT '0',
  `time` date,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 正在导出表  micro_xiyou.schoolnews 的数据：~50 rows (大约)
/*!40000 ALTER TABLE `schoolnews` DISABLE KEYS */;
REPLACE INTO `schoolnews` (`id`, `title`, `article`, `readCount`, `time`) VALUES
	(1, '新闻1标题                         ', '这是第一条新闻的内容', 0, NULL),
	(2, '新闻2标题                         ', '这是第2条新闻的内容', 0, NULL),
	(3, '新闻3标题                         ', '这是第3条新闻的内容', 0, NULL),
	(4, '新闻4标题                         ', '这是第4条新闻的内容', 0, NULL),
	(5, '新闻5标题                         ', '这是第5条新闻的内容', 0, NULL),
	(6, '新闻6标题                         ', '这是第6条新闻的内容', 0, NULL),
	(7, '新闻6标题                         ', '这是第7条新闻的内容', 0, NULL),
	(8, '新闻8标题                         ', '第八条新闻的内容', 0, NULL),
	(9, '新闻9标题', '第八条新闻的内容', 0, NULL),
	(10, '新闻10标题', '第八条新闻的内容', 0, NULL),
	(11, '新闻11标题', '第八条新闻的内容', 0, NULL),
	(12, '新闻12标题', '第八条新闻的内容', 0, NULL),
	(13, '新闻13标题', '第八条新闻的内容', 0, NULL),
	(14, '新闻14标题', '第八条新闻的内容', 0, NULL),
	(15, '新闻15标题', '第八条新闻的内容', 0, NULL),
	(16, '新闻16标题', '第八条新闻的内容', 0, NULL),
	(17, '新闻17标题', '第八条新闻的内容', 0, NULL),
	(18, '新闻18标题', '第八条新闻的内容', 0, NULL),
	(19, '新闻19标题', '第八条新闻的内容', 0, NULL),
	(20, '新闻20标题', '第八条新闻的内容第八条新闻的内容', 0, NULL),
	(21, '新闻21标题', '第八条新闻的内容', 0, NULL),
	(22, '新闻22标题', '第八条新闻的内容', 0, NULL),
	(23, '新闻23标题', '第八条新闻的内容', 0, NULL),
	(24, '新闻24标题', '第八条新闻的内容', 0, NULL),
	(25, '新闻25标题', '第八条新闻的内容', 0, NULL),
	(26, '新闻26标题', '第八条新闻的内容', 0, NULL),
	(27, '新闻27标题', '第八条新闻的内容', 0, NULL),
	(28, '新闻28标题', '第八条新闻的内容', 0, NULL),
	(29, '新闻29标题', '第八条新闻的内容', 0, NULL),
	(30, '新闻30标题', '第八条新闻的内容', 0, NULL),
	(31, '新闻31标题', '第八条新闻的内容', 0, NULL),
	(32, '新闻32标题', '第八条新闻的内容', 0, NULL),
	(33, '新闻33标题', '第八条新闻的内容', 0, NULL),
	(34, '新闻34标题', '第八条新闻的内容', 0, NULL),
	(35, '新闻35标题', '第八条新闻的内容', 0, NULL),
	(36, '新闻36标题', '第八条新闻的内容', 0, NULL),
	(37, '新闻37标题', '第八条新闻的内容', 0, NULL),
	(38, '新闻38标题', '第八条新闻的内容', 0, NULL),
	(39, '新闻39标题', '第八条新闻的内容第八条新闻的内容', 0, NULL),
	(40, '新闻40标题', '第八条新闻的内容', 0, NULL),
	(41, '新闻41标题', '第八条新闻的内容', 0, NULL),
	(42, '新闻42标题', '第八条新闻的内容', 0, NULL),
	(43, '新闻43标题', '第八条新闻的内容', 0, NULL),
	(44, '新闻44标题', '第八条新闻的内容', 0, NULL),
	(45, '新闻45标题', '第八条新闻的内容', 0, NULL),
	(46, '新闻46标题', '第八条新闻的内容', 0, NULL),
	(47, '新闻47标题', '第八条新闻的内容', 0, NULL),
	(48, '新闻48标题', '第八条新闻的内容', 0, NULL),
	(49, '新闻49标题', '第八条新闻的内容', 0, NULL),
	(50, '新闻50标题', '第八条新闻的内容', 2, NULL);
/*!40000 ALTER TABLE `schoolnews` ENABLE KEYS */;


-- 导出  表 micro_xiyou.secondarymarket 结构
DROP TABLE IF EXISTS `secondarymarket`;
CREATE TABLE IF NOT EXISTS `secondarymarket` (
  `id` int(11) NOT NULL,
  `title` varchar(60) NOT NULL,
  `publisher` varchar(24) NOT NULL,
  `time` date DEFAULT NULL,
  `state` varchar(1) DEFAULT NULL,
  `kind` varchar(1) DEFAULT NULL,
  `article` varchar(600) DEFAULT NULL,
  `telephoneNumber` varchar(11) DEFAULT NULL,
  `publisherEmail` varchar(30) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 正在导出表  micro_xiyou.secondarymarket 的数据：~1 rows (大约)
/*!40000 ALTER TABLE `secondarymarket` DISABLE KEYS */;
REPLACE INTO `secondarymarket` (`id`, `title`, `publisher`, `time`, `state`, `kind`, `article`, `telephoneNumber`, `publisherEmail`) VALUES
	(1, '垃圾le', '就女士', '2016-03-01', '3', '1', '来了附近老K', '13568568575', '123456@qq.com');
/*!40000 ALTER TABLE `secondarymarket` ENABLE KEYS */;


-- 导出  表 micro_xiyou.studyroom 结构
DROP TABLE IF EXISTS `studyroom`;
CREATE TABLE IF NOT EXISTS `studyroom` (
  `id` int(11) NOT NULL,
  `isDoubleWeek` varchar(1) DEFAULT NULL,
  `building` varchar(1) DEFAULT NULL,
  `classRoom` varchar(3) DEFAULT NULL,
  `dayOfWeek` varchar(1) DEFAULT NULL,
  `classOfDay` varchar(1) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 正在导出表  micro_xiyou.studyroom 的数据：~20 rows (大约)
/*!40000 ALTER TABLE `studyroom` DISABLE KEYS */;
REPLACE INTO `studyroom` (`id`, `isDoubleWeek`, `building`, `classRoom`, `dayOfWeek`, `classOfDay`) VALUES
	(1, '1', 'A', '101', '1', '1'),
	(2, '1', 'A', '201', '1', '2'),
	(3, '1', 'A', '301', '2', '3'),
	(4, '1', 'A', '401', '2', '4'),
	(5, '1', 'B', '102', '3', '1'),
	(6, '1', 'B', '202', '3', '2'),
	(7, '1', 'B', '302', '4', '3'),
	(8, '1', 'B', '402', '4', '4'),
	(9, '1', 'C', '103', '5', '1'),
	(10, '1', 'C', '203', '5', '2'),
	(11, '1', 'C', '303', '4', '3'),
	(12, '1', 'C', '403', '4', '4'),
	(13, '0', 'A', '111', '1', '1'),
	(14, '0', 'A', '222', '2', '2'),
	(15, '0', 'B', '333', '3', '3'),
	(16, '0', 'B', '444', '4', '4'),
	(17, '0', 'C', '555', '5', '1'),
	(18, '0', 'C', '666', '4', '2'),
	(19, '0', 'D', '777', '3', '3'),
	(20, '0', 'D', '888', '2', '4');
/*!40000 ALTER TABLE `studyroom` ENABLE KEYS */;


-- 导出  表 micro_xiyou.version 结构
DROP TABLE IF EXISTS `version`;
CREATE TABLE IF NOT EXISTS `version` (
  `id` int(11) NOT NULL,
  `ver` varchar(5) DEFAULT NULL,
  `ownerOfVersion` varchar(30) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 正在导出表  micro_xiyou.version 的数据：~1 rows (大约)
/*!40000 ALTER TABLE `version` DISABLE KEYS */;
REPLACE INTO `version` (`id`, `ver`, `ownerOfVersion`) VALUES
	(1, '1', 'studyRoom');
/*!40000 ALTER TABLE `version` ENABLE KEYS */;


-- 导出  表 micro_xiyou.xyprofessional 结构
DROP TABLE IF EXISTS `xyprofessional`;
CREATE TABLE IF NOT EXISTS `xyprofessional` (
  `id` varchar(2) NOT NULL,
  `name` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 正在导出表  micro_xiyou.xyprofessional 的数据：~7 rows (大约)
/*!40000 ALTER TABLE `xyprofessional` DISABLE KEYS */;
REPLACE INTO `xyprofessional` (`id`, `name`) VALUES
	('01', '通信工程'),
	('02', '哲学'),
	('03', '教育学'),
	('04', '经济学'),
	('05', '法学'),
	('06', '文学'),
	('07', '历史学');
/*!40000 ALTER TABLE `xyprofessional` ENABLE KEYS */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
