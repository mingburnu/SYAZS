--create db
CREATE DATABASE syazsdb DEFAULT CHARACTER SET UTF8 COLLATE utf8_unicode_ci;
USE syazsdb;

--insert
INSERT INTO `customer` (`serNo`, `cDTime`, `cUid`, `uDTime`, `uUid`, `address`, `contactUserName`, `engName`, `memo`, `name`, `tel`) VALUES (9, '2015-09-24 15:04:38', 'admin', '2015-09-24 15:04:38', 'admin', NULL, 'Roderick', 'SRISC', NULL, '碩睿資訊公司', NULL);
INSERT INTO `accountNumber` (`serNo`, `cDTime`, `cUid`, `uDTime`, `uUid`, `email`, `role`, `status`, `userID`, `userName`, `userPW`, `cus_serNo`) VALUES (9, '2015-09-24 15:04:40', 'admin', '2015-09-24 15:04:40', 'admin', NULL, '系統管理員', '生效', 'admin', 'administer', '8w5y4CYvLHP69kq5Wm2vHDVfPX1IOcrpskUugS/4KZN6budffcIYbfhpEL6HmNZ0', 9);
