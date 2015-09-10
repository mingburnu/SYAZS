--customer
insert into customer(serNo, cUid, uUid, cDTime, uDTime, name, engName, contactUserName) values(1, 'admin', 'admin', sysdate(), sysdate(), '長庚紀念醫院', 'CGMH', '林麗雯');
insert into customer(serNo, cUid, uUid, cDTime, uDTime, name, engName, contactUserName) values(2, 'admin', 'admin', sysdate(), sysdate(), '國防醫學中心', 'NDMC', '陳小美');
insert into customer(serNo, cUid, uUid, cDTime, uDTime, name, engName, contactUserName) values(3, 'admin', 'admin', sysdate(), sysdate(), '成功大學附設醫院', 'NCKUH', '陳桔根');
insert into customer(serNo, cUid, uUid, cDTime, uDTime, name, engName, contactUserName) values(4, 'admin', 'admin', sysdate(), sysdate(), '台北醫學院附屬醫院', 'TMUH', '曾慧君');
insert into customer(serNo, cUid, uUid, cDTime, uDTime, name, engName, contactUserName) values(5, 'admin', 'admin', sysdate(), sysdate(), '恩主公醫院', 'ECKH', '歐陽慧');
insert into customer(serNo, cUid, uUid, cDTime, uDTime, name, engName, contactUserName) values(6, 'admin', 'admin', sysdate(), sysdate(), '中國醫藥大學附設醫院', 'CMUH', '董世勳');
insert into customer(serNo, cUid, uUid, cDTime, uDTime, name, engName, contactUserName) values(7, 'admin', 'admin', sysdate(), sysdate(), '宏恩醫院', 'HEH', '郭啟文');
insert into customer(serNo, cUid, uUid, cDTime, uDTime, name, engName, contactUserName) values(8, 'admin', 'admin', sysdate(), sysdate(), '馬偕紀念醫院', 'MMH', '鄞玉娟');
insert into customer(serNo, cUid, uUid, cDTime, uDTime, name, engName, contactUserName) values(9, 'admin', 'admin', sysdate(), sysdate(), '碩睿資訊公司', 'SRISC', 'Roderick');
insert into customer(serNo, cUid, uUid, cDTime, uDTime, name, engName, contactUserName) values(10, 'admin', 'admin', sysdate(), sysdate(), '佛教慈濟綜合醫院', 'BTCGH', '廖振智');
insert into customer(serNo, cUid, uUid, cDTime, uDTime, name, engName, contactUserName) values(11, 'admin', 'admin', sysdate(), sysdate(), '高雄醫學院附設醫院', 'KMUH', '張大功');
insert into customer(serNo, cUid, uUid, cDTime, uDTime, name, engName, contactUserName) values(12, 'admin', 'admin', sysdate(), sysdate(), '天主教耕莘醫療財團法人耕莘醫院', 'CTH', '許蕎麟');
insert into customer(serNo, cUid, uUid, cDTime, uDTime, name, engName, contactUserName) values(13, 'admin', 'admin', sysdate(), sysdate(), '為恭紀念醫院', 'WGMH', '韓菱紗');
insert into customer(serNo, cUid, uUid, cDTime, uDTime, name, engName, contactUserName) values(14, 'admin', 'admin', sysdate(), sysdate(), '台灣大學附設醫院', 'NTUH', '王大明');
insert into customer(serNo, cUid, uUid, cDTime, uDTime, name, engName, contactUserName) values(15, 'admin', 'admin', sysdate(), sysdate(), '疾病管制署', 'CDC', '王小明');

--groupMapping
insert into group_mapping(serNo, parentGID, Title, Level) values(1, null, '長庚紀念醫院', 0);
insert into group_mapping(serNo, parentGID, Title, Level) values(2, null, '國防醫學中心', 0);
insert into group_mapping(serNo, parentGID, Title, Level) values(3, null, '成功大學附設醫院', 0);
insert into group_mapping(serNo, parentGID, Title, Level) values(4, null, '台北醫學院附屬醫院', 0);
insert into group_mapping(serNo, parentGID, Title, Level) values(5, null, '恩主公醫院', 0);
insert into group_mapping(serNo, parentGID, Title, Level) values(6, null, '中國醫藥大學附設醫院', 0);
insert into group_mapping(serNo, parentGID, Title, Level) values(7, null, '宏恩醫院', 0);
insert into group_mapping(serNo, parentGID, Title, Level) values(8, null, '馬偕紀念醫院', 0);
insert into group_mapping(serNo, parentGID, Title, Level) values(9, null, '碩睿資訊公司', 0);
insert into group_mapping(serNo, parentGID, Title, Level) values(10, null, '佛教慈濟綜合醫院', 0);
insert into group_mapping(serNo, parentGID, Title, Level) values(11, null, '高雄醫學院附設醫院', 0);
insert into group_mapping(serNo, parentGID, Title, Level) values(12, null, '天主教耕莘醫療財團法人耕莘醫院', 0);
insert into group_mapping(serNo, parentGID, Title, Level) values(13, null, '為恭紀念醫院', 0);
insert into group_mapping(serNo, parentGID, Title, Level) values(14, null, '台灣大學附設醫院', 0);
insert into group_mapping(serNo, parentGID, Title, Level) values(15, null, '疾病管制署', 0);
insert into group_mapping(serNo, parentGID, Title, Level) values(16, 15, '疾病管制署', 1);
insert into group_mapping(serNo, parentGID, Title, Level) values(17, 16, '疾病管制署', 2);
insert into group_mapping(serNo, parentGID, Title, Level) values(18, 17, '疾病管制署', 3);
insert into group_mapping(serNo, parentGID, Title, Level) values(19, 16, '疾病管制署', 2);
insert into group_mapping(serNo, parentGID, Title, Level) values(20, 2, '國防醫學中心', 1);
insert into group_mapping(serNo, parentGID, Title, Level) values(21, 15, '疾病管制署', 1);
insert into group_mapping(serNo, parentGID, Title, Level) values(22, 21, '疾病管制署', 2);
insert into group_mapping(serNo, parentGID, Title, Level) values(23, 21, '疾病管制署', 2);

--group
insert into groups(serNo, uUid, uDTime, gro_m_serNo, cus_serNo, groupName) values(1, 'admin', sysdate(), 1, 1, '長庚紀念醫院');
insert into groups(serNo, uUid, uDTime, gro_m_serNo, cus_serNo, groupName) values(2, 'admin', sysdate(), 2, 2, '國防醫學中心');
insert into groups(serNo, uUid, uDTime, gro_m_serNo, cus_serNo, groupName) values(3, 'admin', sysdate(), 3, 3, '成功大學附設醫院');
insert into groups(serNo, uUid, uDTime, gro_m_serNo, cus_serNo, groupName) values(4, 'admin', sysdate(), 4, 4, '台北醫學院附屬醫院');
insert into groups(serNo, uUid, uDTime, gro_m_serNo, cus_serNo, groupName) values(5, 'admin', sysdate(), 5, 5, '恩主公醫院');
insert into groups(serNo, uUid, uDTime, gro_m_serNo, cus_serNo, groupName) values(6, 'admin', sysdate(), 6, 6, '中國醫藥大學附設醫院');
insert into groups(serNo, uUid, uDTime, gro_m_serNo, cus_serNo, groupName) values(7, 'admin', sysdate(), 7, 7, '宏恩醫院');
insert into groups(serNo, uUid, uDTime, gro_m_serNo, cus_serNo, groupName) values(8, 'admin', sysdate(), 8, 8, '馬偕紀念醫院');
insert into groups(serNo, uUid, uDTime, gro_m_serNo, cus_serNo, groupName) values(9, 'admin', sysdate(), 9, 9, '碩睿資訊公司');
insert into groups(serNo, uUid, uDTime, gro_m_serNo, cus_serNo, groupName) values(10, 'admin', sysdate(), 10, 10, '佛教慈濟綜合醫院');
insert into groups(serNo, uUid, uDTime, gro_m_serNo, cus_serNo, groupName) values(11, 'admin', sysdate(), 11, 11, '高雄醫學院附設醫院');
insert into groups(serNo, uUid, uDTime, gro_m_serNo, cus_serNo, groupName) values(12, 'admin', sysdate(), 12, 12, '天主教耕莘醫療財團法人耕莘醫院');
insert into groups(serNo, uUid, uDTime, gro_m_serNo, cus_serNo, groupName) values(13, 'admin', sysdate(), 13, 13, '為恭紀念醫院');
insert into groups(serNo, uUid, uDTime, gro_m_serNo, cus_serNo, groupName) values(14, 'admin', sysdate(), 14, 14, '台灣大學附設醫院');
insert into groups(serNo, uUid, uDTime, gro_m_serNo, cus_serNo, groupName) values(15, 'admin', sysdate(), 15, 15, '疾病管制署');
insert into groups(serNo, uUid, uDTime, gro_m_serNo, cus_serNo, groupName) values(16, 'admin', sysdate(), 16, 15, '微生物組');
insert into groups(serNo, uUid, uDTime, gro_m_serNo, cus_serNo, groupName) values(17, 'admin', sysdate(), 17, 15, '細菌組');
insert into groups(serNo, uUid, uDTime, gro_m_serNo, cus_serNo, groupName) values(18, 'admin', sysdate(), 18, 15, '球菌組');
insert into groups(serNo, uUid, uDTime, gro_m_serNo, cus_serNo, groupName) values(19, 'admin', sysdate(), 19, 15, '病毒組');
insert into groups(serNo, uUid, uDTime, gro_m_serNo, cus_serNo, groupName) values(20, 'admin', sysdate(), 20, 2, '生理所');
insert into groups(serNo, uUid, uDTime, gro_m_serNo, cus_serNo, groupName) values(21, 'admin', sysdate(), 21, 15, '寄生蟲組');
insert into groups(serNo, uUid, uDTime, gro_m_serNo, cus_serNo, groupName) values(22, 'admin', sysdate(), 22, 15, '外寄生蟲組');
insert into groups(serNo, uUid, uDTime, gro_m_serNo, cus_serNo, groupName) values(23, 'admin', sysdate(), 23, 15, '內寄生蟲組');

--accountNumber
insert into accountNumber(serNo, cUid, uUid, cDTime, uDTime, cus_serNo, userID, userName, userPW, role, status) values(1, 'admin', 'admin', sysdate(), sysdate(), 5, 'user3', 'Default User', '8w5y4CYvLHP69kq5Wm2vHDVfPX1IOcrpskUugS/4KZN6budffcIYbfhpEL6HmNZ0', '使用者','生效');
insert into accountNumber(serNo, cUid, uUid, cDTime, uDTime, cus_serNo, userID, userName, userPW, role, status) values(2, 'admin', 'admin', sysdate(), sysdate(), 2,'admin2', 'administer', '8w5y4CYvLHP69kq5Wm2vHDVfPX1IOcrpskUugS/4KZN6budffcIYbfhpEL6HmNZ0', '維護人員','生效');
insert into accountNumber(serNo, cUid, uUid, cDTime, uDTime, cus_serNo, userID, userName, userPW, role, status) values(3, 'admin', 'admin', sysdate(), sysdate(), 5, 'user', 'Default User', '8w5y4CYvLHP69kq5Wm2vHDVfPX1IOcrpskUugS/4KZN6budffcIYbfhpEL6HmNZ0', '使用者','生效');
insert into accountNumber(serNo, cUid, uUid, cDTime, uDTime, cus_serNo, userID, userName, userPW, role, status) values(4, 'admin', 'admin', sysdate(), sysdate(), 1,'admin3', 'administer', '8w5y4CYvLHP69kq5Wm2vHDVfPX1IOcrpskUugS/4KZN6budffcIYbfhpEL6HmNZ0', '管理員','生效');
insert into accountNumber(serNo, cUid, uUid, cDTime, uDTime, cus_serNo, userID, userName, userPW, role, status) values(5, 'admin', 'admin', sysdate(), sysdate(), 2,'admin4', 'administer', '8w5y4CYvLHP69kq5Wm2vHDVfPX1IOcrpskUugS/4KZN6budffcIYbfhpEL6HmNZ0', '系統管理員','生效');
insert into accountNumber(serNo, cUid, uUid, cDTime, uDTime, cus_serNo, userID, userName, userPW, role, status) values(6, 'admin', 'admin', sysdate(), sysdate(), 5, 'user2', 'Default User', '8w5y4CYvLHP69kq5Wm2vHDVfPX1IOcrpskUugS/4KZN6budffcIYbfhpEL6HmNZ0', '使用者','生效');
insert into accountNumber(serNo, cUid, uUid, cDTime, uDTime, cus_serNo, userID, userName, userPW, role, status) values(7, 'admin', 'admin', sysdate(), sysdate(), 1,'admin5', 'administer', '8w5y4CYvLHP69kq5Wm2vHDVfPX1IOcrpskUugS/4KZN6budffcIYbfhpEL6HmNZ0', '系統管理員','生效');
insert into accountNumber(serNo, cUid, uUid, cDTime, uDTime, cus_serNo, userID, userName, userPW, role, status) values(8, 'admin', 'admin', sysdate(), sysdate(), 2,'admin6', 'administer', '8w5y4CYvLHP69kq5Wm2vHDVfPX1IOcrpskUugS/4KZN6budffcIYbfhpEL6HmNZ0', '系統管理員','生效');
insert into accountNumber(serNo, cUid, uUid, cDTime, uDTime, cus_serNo, userID, userName, userPW, role, status) values(9, 'admin', 'admin', sysdate(), sysdate(), 9,'admin', 'administer', '8w5y4CYvLHP69kq5Wm2vHDVfPX1IOcrpskUugS/4KZN6budffcIYbfhpEL6HmNZ0', '系統管理員','生效');
insert into accountNumber(serNo, cUid, uUid, cDTime, uDTime, cus_serNo, userID, userName, userPW, role, status) values(10, 'admin', 'admin', sysdate(), sysdate(), 1,'admin7', 'administer', '8w5y4CYvLHP69kq5Wm2vHDVfPX1IOcrpskUugS/4KZN6budffcIYbfhpEL6HmNZ0', '系統管理員','生效');
insert into accountNumber(serNo, cUid, uUid, cDTime, uDTime, cus_serNo, userID, userName, userPW, role, status) values(11, 'admin', 'admin', sysdate(), sysdate(), 2,'admin8', 'administer', '8w5y4CYvLHP69kq5Wm2vHDVfPX1IOcrpskUugS/4KZN6budffcIYbfhpEL6HmNZ0', '系統管理員','生效');
insert into accountNumber(serNo, cUid, uUid, cDTime, uDTime, cus_serNo, userID, userName, userPW, role, status) values(12, 'admin', 'admin', sysdate(), sysdate(), 5, 'user4', 'Default User', '8w5y4CYvLHP69kq5Wm2vHDVfPX1IOcrpskUugS/4KZN6budffcIYbfhpEL6HmNZ0', '使用者','生效');

--referenceOwner
insert into referenceOwner(serNo, cUid, uUid, cDTime, uDTime, name, engName, contactUserName) values(1, 'admin', 'admin', sysdate(), sysdate(), '李靖', 'Li', '林麗雯');
insert into referenceOwner(serNo, cUid, uUid, cDTime, uDTime, name, engName, contactUserName) values(2, 'admin', 'admin', sysdate(), sysdate(), '陳靖仇', 'Chen', '陳小美');
insert into referenceOwner(serNo, cUid, uUid, cDTime, uDTime, name, engName, contactUserName) values(3, 'admin', 'admin', sysdate(), sysdate(), '于小雪', 'Yu', '陳桔根');
insert into referenceOwner(serNo, cUid, uUid, cDTime, uDTime, name, engName, contactUserName) values(4, 'admin', 'admin', sysdate(), sysdate(), '拓跋玉兒', 'Tuoba', '曾慧君');
insert into referenceOwner(serNo, cUid, uUid, cDTime, uDTime, name, engName, contactUserName) values(5, 'admin', 'admin', sysdate(), sysdate(), '張烈', 'Cheng', '歐陽慧');
insert into referenceOwner(serNo, cUid, uUid, cDTime, uDTime, name, engName, contactUserName) values(6, 'admin', 'admin', sysdate(), sysdate(), '陳輔', 'Chen', '董世勳');
insert into referenceOwner(serNo, cUid, uUid, cDTime, uDTime, name, engName, contactUserName) values(7, 'admin', 'admin', sysdate(), sysdate(), '宇文拓', 'Yuwen', '郭啟文');
insert into referenceOwner(serNo, cUid, uUid, cDTime, uDTime, name, engName, contactUserName) values(8, 'admin', 'admin', sysdate(), sysdate(), '獨孤寧珂', 'Dugu', '鄞玉娟');
insert into referenceOwner(serNo, cUid, uUid, cDTime, uDTime, name, engName, contactUserName) values(9, 'admin', 'admin', sysdate(), sysdate(), '姬良', 'Ji', '謝滄行');
insert into referenceOwner(serNo, cUid, uUid, cDTime, uDTime, name, engName, contactUserName) values(10, 'admin', 'admin', sysdate(), sysdate(), '屈嫺', 'Chui', '廖振智');
insert into referenceOwner(serNo, cUid, uUid, cDTime, uDTime, name, engName, contactUserName) values(11, 'admin', 'admin', sysdate(), sysdate(), '劉季', 'Liu', '張大功');
insert into referenceOwner(serNo, cUid, uUid, cDTime, uDTime, name, engName, contactUserName) values(12, 'admin', 'admin', sysdate(), sysdate(), '陸承軒', 'Lu', '許蕎麟');
insert into referenceOwner(serNo, cUid, uUid, cDTime, uDTime, name, engName, contactUserName) values(13, 'admin', 'admin', sysdate(), sysdate(), '夏柔', 'Shia', '韓菱紗');
insert into referenceOwner(serNo, cUid, uUid, cDTime, uDTime, name, engName, contactUserName) values(14, 'admin', 'admin', sysdate(), sysdate(), '毛民福', 'Mau', '王大明');
insert into referenceOwner(serNo, cUid, uUid, cDTime, uDTime, name, engName, contactUserName) values(15, 'admin', 'admin', sysdate(), sysdate(), '皇甫卓', 'Huangfu', '王小明');

--resourcesBuyers
insert into resourcesBuyers(serNo, startdate, maturitydate, Rcategory) values(1, '1980', '2020', '買斷');
insert into resourcesBuyers(serNo, startdate, maturitydate, Rcategory) values(2, '1980', '2020', '買斷');
insert into resourcesBuyers(serNo, startdate, maturitydate, Rcategory) values(3, '1980', '2020', '買斷');
insert into resourcesBuyers(serNo, startdate, maturitydate, Rcategory) values(4, '1980', '2020', '買斷');
insert into resourcesBuyers(serNo, startdate, maturitydate, Rcategory) values(5, '1980', '2020', '買斷');
insert into resourcesBuyers(serNo, startdate, maturitydate, Rcategory) values(6, '1980', '2020', '買斷');
insert into resourcesBuyers(serNo, startdate, maturitydate, Rcategory) values(7, '1980', '2020', '買斷');
insert into resourcesBuyers(serNo, startdate, maturitydate, Rcategory) values(8, '1980', '2020', '買斷');
insert into resourcesBuyers(serNo, startdate, maturitydate, Rcategory) values(9, '1980', '2020', '買斷');
insert into resourcesBuyers(serNo, startdate, maturitydate, Rcategory) values(10, '1980', '2020', '買斷');
insert into resourcesBuyers(serNo, startdate, maturitydate, Rcategory) values(11, '1980', '2020', '買斷');
insert into resourcesBuyers(serNo, startdate, maturitydate, Rcategory) values(12, '1980', '2020', '買斷');
insert into resourcesBuyers(serNo, startdate, maturitydate, Rcategory) values(13, '1980', '2020', '買斷');
insert into resourcesBuyers(serNo, startdate, maturitydate, Rcategory) values(14, '1980', '2020', '買斷');
insert into resourcesBuyers(serNo, startdate, maturitydate, Rcategory) values(15, '1980', '2020', '買斷');
insert into resourcesBuyers(serNo, startdate, maturitydate, Rcategory) values(16, '1980', '2020', '買斷');
insert into resourcesBuyers(serNo, startdate, maturitydate, Rcategory) values(17, '1980', '2020', '買斷');
insert into resourcesBuyers(serNo, startdate, maturitydate, Rcategory) values(18, '1980', '2020', '買斷');
insert into resourcesBuyers(serNo, startdate, maturitydate, Rcategory) values(19, '1980', '2020', '買斷');
insert into resourcesBuyers(serNo, startdate, maturitydate, Rcategory) values(20, '1980', '2020', '買斷');
insert into resourcesBuyers(serNo, startdate, maturitydate, Rcategory) values(21, '1980', '2020', '買斷');
insert into resourcesBuyers(serNo, startdate, maturitydate, Rcategory) values(22, '1980', '2020', '買斷');
insert into resourcesBuyers(serNo, startdate, maturitydate, Rcategory) values(23, '1980', '2020', '買斷');
insert into resourcesBuyers(serNo, startdate, maturitydate, Rcategory) values(24, '1980', '2020', '買斷');
insert into resourcesBuyers(serNo, startdate, maturitydate, Rcategory) values(25, '1980', '2020', '買斷');

--database
insert into db(serNo, cUid, uUid, cDTime, uDTime, DBtitle, Rtype, res_serNo, uuIdentifier, openAccess) values(1, 'admin', 'admin', sysdate(), sysdate() , 'History', '資料庫', 8, random_uuid(), true);
insert into db(serNo, cUid, uUid, cDTime, uDTime, DBtitle, Rtype, res_serNo, uuIdentifier, openAccess) values(2, 'admin', 'admin', sysdate(), sysdate() , 'Medicine', '資料庫', 9, random_uuid(), true);
insert into db(serNo, cUid, uUid, cDTime, uDTime, DBtitle, Rtype, res_serNo, uuIdentifier, openAccess) values(3, 'admin', 'admin', sysdate(), sysdate() , 'Physics', '資料庫', 10, random_uuid(), true);
insert into db(serNo, cUid, uUid, cDTime, uDTime, DBtitle, Rtype, res_serNo, uuIdentifier, openAccess) values(4, 'admin', 'admin', sysdate(), sysdate() , 'Math', '資料庫', 11, random_uuid(), true);
insert into db(serNo, cUid, uUid, cDTime, uDTime, DBtitle, Rtype, res_serNo, uuIdentifier, openAccess) values(5, 'admin', 'admin', sysdate(), sysdate() , 'Chemistry', '資料庫', 12, random_uuid(), true);
insert into db(serNo, cUid, uUid, cDTime, uDTime, DBtitle, Rtype, res_serNo, uuIdentifier, openAccess) values(6, 'admin', 'admin', sysdate(), sysdate() , 'Geography', '資料庫', 13, random_uuid(), true);
insert into db(serNo, cUid, uUid, cDTime, uDTime, DBtitle, Rtype, res_serNo, uuIdentifier, openAccess) values(7, 'admin', 'admin', sysdate(), sysdate() , 'Law', '資料庫', 14, random_uuid(), true);
insert into db(serNo, cUid, uUid, cDTime, uDTime, DBtitle, Rtype, res_serNo, uuIdentifier, openAccess) values(8, 'admin', 'admin', sysdate(), sysdate() , 'Biology', '資料庫', 15, random_uuid(), true);
insert into db(serNo, cUid, uUid, cDTime, uDTime, DBtitle, Rtype, res_serNo, uuIdentifier, openAccess) values(9, 'admin', 'admin', sysdate(), sysdate() , 'Femem', '資料庫', 16, random_uuid(), true);

--ref_dat
insert into ref_dat(ref_serNo, dat_serNo) values(2, 1);
insert into ref_dat(ref_serNo, dat_serNo) values(2, 2);
insert into ref_dat(ref_serNo, dat_serNo) values(2, 3);
insert into ref_dat(ref_serNo, dat_serNo) values(2, 4);
insert into ref_dat(ref_serNo, dat_serNo) values(2, 5);
insert into ref_dat(ref_serNo, dat_serNo) values(2, 6);
insert into ref_dat(ref_serNo, dat_serNo) values(2, 7);
insert into ref_dat(ref_serNo, dat_serNo) values(3, 1);
insert into ref_dat(ref_serNo, dat_serNo) values(3, 2);
insert into ref_dat(ref_serNo, dat_serNo) values(3, 3);
insert into ref_dat(ref_serNo, dat_serNo) values(3, 4);
insert into ref_dat(ref_serNo, dat_serNo) values(3, 5);
insert into ref_dat(ref_serNo, dat_serNo) values(3, 6);
insert into ref_dat(ref_serNo, dat_serNo) values(3, 7);
insert into ref_dat(ref_serNo, dat_serNo) values(3, 8);
insert into ref_dat(ref_serNo, dat_serNo) values(3, 9);

--journal
insert into journal(serNo, cUid, uUid, cDTime, uDTime, title, ISSN, version, res_serNo, openAccess) values(1, 'admin', 'admin', sysdate(), sysdate(), 'New England', '15334406', 1, 1, true);
insert into journal(serNo, cUid, uUid, cDTime, uDTime, title, ISSN, version, res_serNo, openAccess) values(2, 'admin', 'admin', sysdate(), sysdate(), 'Nature', '00280836', 3, 2, false);
insert into journal(serNo, cUid, uUid, cDTime, uDTime, title, ISSN, version, res_serNo, openAccess) values(3, 'admin', 'admin', sysdate(), sysdate(), 'Science', '00368075', 2, 3, true);
insert into journal(serNo, cUid, uUid, cDTime, uDTime, title, ISSN, version, res_serNo, openAccess) values(4, 'admin', 'admin', sysdate(), sysdate(), 'Lancet', '01406736', 4, 4, false);
insert into journal(serNo, cUid, uUid, cDTime, uDTime, title, ISSN, version, res_serNo, openAccess) values(5, 'admin', 'admin', sysdate(), sysdate(), 'Cell',  '00928674', 2, 5, true);
insert into journal(serNo, cUid, uUid, cDTime, uDTime, title, ISSN, version, res_serNo, openAccess) values(6, 'admin', 'admin', sysdate(), sysdate(), 'Proceedings of the National Academy of Sciences, USA', '10916490', 2, 6, false);
insert into journal(serNo, cUid, uUid, cDTime, uDTime, title, ISSN, version, res_serNo, openAccess) values(7, 'admin', 'admin', sysdate(), sysdate(), 'Journal of Biological Chemistry', '00219258', 2, 7, true);

--ref_jou
insert into ref_jou(ref_serNo, jou_serNo) values(3, 1);
insert into ref_jou(ref_serNo, jou_serNo) values(3, 2);
insert into ref_jou(ref_serNo, jou_serNo) values(3, 3);
insert into ref_jou(ref_serNo, jou_serNo) values(3, 4);
insert into ref_jou(ref_serNo, jou_serNo) values(3, 5);
insert into ref_jou(ref_serNo, jou_serNo) values(3, 6);
insert into ref_jou(ref_serNo, jou_serNo) values(3, 7);

--ebook
insert into ebook(serNo, cUid, uUid, cDTime, uDTime, bookname, ISBN ,version, authername, authers, res_serNo, uuIdentifier, openAccess) values(1, 'admin', 'admin', sysdate(), sysdate(), 'The Anglo-Saxons', 9780140143959, 3, 'James Campbell' , 'Eric John, Patrick Wormald', 17, random_uuid(), true);
insert into ebook(serNo, cUid, uUid, cDTime, uDTime, bookname, ISBN ,version, authername, authers, res_serNo, uuIdentifier, openAccess) values(2, 'admin', 'admin', sysdate(), sysdate(), 'Pocket Surgery', 9781451112962, 2, 'Melanie Goldfarb MD' , ' Mark A. Gromski BA, James M. Hurst MD FACS, Daniel B. Jones MD MS', 18, random_uuid(), true);
insert into ebook(serNo, cUid, uUid, cDTime, uDTime, bookname, ISBN ,version, authername, authers, res_serNo, uuIdentifier, openAccess) values(3, 'admin', 'admin', sysdate(), sysdate(), 'Metal Cats', 9781576876770, 1, 'Alexandra Crockett' , '', 19, random_uuid(), true);
insert into ebook(serNo, cUid, uUid, cDTime, uDTime, bookname, ISBN ,version, authername, authers, res_serNo, uuIdentifier, openAccess) values(4, 'admin', 'admin', sysdate(), sysdate(), 'Hitler: A Biography', 9780393337617, 3, 'Ian Kershaw' , '', 20, random_uuid(), true);
insert into ebook(serNo, cUid, uUid, cDTime, uDTime, bookname, ISBN ,version, authername, authers, res_serNo, uuIdentifier, openAccess) values(5, 'admin', 'admin', sysdate(), sysdate(), 'Edison - A Biography', 9780965569934, 2, 'Matthew Josephson' , '', 21, random_uuid(), true);
insert into ebook(serNo, cUid, uUid, cDTime, uDTime, bookname, ISBN ,version, authername, authers, res_serNo, uuIdentifier, openAccess) values(6, 'admin', 'admin', sysdate(), sysdate(), 'The Normans', 9781405149655, 1, 'Marjorie Chibnall' , '', 22, random_uuid(), true);
insert into ebook(serNo, cUid, uUid, cDTime, uDTime, bookname, ISBN ,version, authername, authers, res_serNo, uuIdentifier, openAccess) values(7, 'admin', 'admin', sysdate(), sysdate(), 'The Huns', 9780631214434, 3, 'E. A. Thompson' , '', 23, random_uuid(), true);
insert into ebook(serNo, cUid, uUid, cDTime, uDTime, bookname, ISBN ,version, authername, authers, res_serNo, uuIdentifier, openAccess) values(8, 'admin', 'admin', sysdate(), sysdate(), 'Racist', 9780753821503, 2, 'Kunal Basu' , '', 24, random_uuid(), true);
insert into ebook(serNo, cUid, uUid, cDTime, uDTime, bookname, ISBN ,version, authername, authers, res_serNo, uuIdentifier, openAccess) values(9, 'admin', 'admin', sysdate(), sysdate(), 'Ten Little Indians', 9780802141170, 1, 'Sherman Alexie' , '', 25, random_uuid(), true);

--ref_ebk
insert into ref_ebk(ref_serNo, ebk_serNo) values(4, 1);
insert into ref_ebk(ref_serNo, ebk_serNo) values(4, 2);
insert into ref_ebk(ref_serNo, ebk_serNo) values(4, 3);
insert into ref_ebk(ref_serNo, ebk_serNo) values(4, 4);
insert into ref_ebk(ref_serNo, ebk_serNo) values(4, 5);
insert into ref_ebk(ref_serNo, ebk_serNo) values(4, 6);
insert into ref_ebk(ref_serNo, ebk_serNo) values(4, 7);
insert into ref_ebk(ref_serNo, ebk_serNo) values(4, 8);
insert into ref_ebk(ref_serNo, ebk_serNo) values(4, 9);

--ip_range
insert into ip_range (serNo, cDTime, cUid, uDTime, uUid, cus_serNo, ipRangeEnd, ipRangeStart) values('1',sysdate(),'admin',sysdate(),'admin','9','59.120.245.198','59.120.245.193');
insert into ip_range (serNo, cDTime, cUid, uDTime, uUid, cus_serNo, ipRangeEnd, ipRangeStart) values('2',sysdate(),'admin',sysdate(),'admin','9','61.219.77.42','61.219.77.37');
insert into ip_range (serNo, cDTime, cUid, uDTime, uUid, cus_serNo, ipRangeEnd, ipRangeStart) values('3',sysdate(),'admin',sysdate(),'admin','9','59.125.8.189','59.125.8.187');
insert into ip_range (serNo, cDTime, cUid, uDTime, uUid, cus_serNo, ipRangeEnd, ipRangeStart) values('4',sysdate(),'admin',sysdate(),'admin','9','59.125.8.198','59.125.8.196');
insert into ip_range (serNo, cDTime, cUid, uDTime, uUid, cus_serNo, ipRangeEnd, ipRangeStart) values('5',sysdate(),'admin',sysdate(),'admin','9','60.250.74.159','60.250.74.157');
insert into ip_range (serNo, cDTime, cUid, uDTime, uUid, cus_serNo, ipRangeEnd, ipRangeStart) values('6',sysdate(),'admin',sysdate(),'admin','9','60.250.74.198','60.250.74.196');
insert into ip_range (serNo, cDTime, cUid, uDTime, uUid, cus_serNo, ipRangeEnd, ipRangeStart) values('7',sysdate(),'admin',sysdate(),'admin','9','60.250.74.210','60.250.74.208');
insert into ip_range (serNo, cDTime, cUid, uDTime, uUid, cus_serNo, ipRangeEnd, ipRangeStart) values('8',sysdate(),'admin',sysdate(),'admin','9','60.250.74.240','60.250.74.238');
insert into ip_range (serNo, cDTime, cUid, uDTime, uUid, cus_serNo, ipRangeEnd, ipRangeStart) values('9',sysdate(),'admin',sysdate(),'admin','9','122.147.148.255','122.147.148.128');
insert into ip_range (serNo, cDTime, cUid, uDTime, uUid, cus_serNo, ipRangeEnd, ipRangeStart) values('10',sysdate(),'admin',sysdate(),'admin','10','163.29.19.210','163.29.19.200');
insert into ip_range (serNo, cDTime, cUid, uDTime, uUid, cus_serNo, ipRangeEnd, ipRangeStart) values('11',sysdate(),'admin',sysdate(),'admin','1','117.56.2.1','117.56.2.1');
insert into ip_range (serNo, cDTime, cUid, uDTime, uUid, cus_serNo, ipRangeEnd, ipRangeStart) values('12',sysdate(),'admin',sysdate(),'admin','2','163.29.9.210','163.29.9.200');
insert into ip_range (serNo, cDTime, cUid, uDTime, uUid, cus_serNo, ipRangeEnd, ipRangeStart) values('13',sysdate(),'admin',sysdate(),'admin','3','163.29.8.210','163.29.8.200');
insert into ip_range (serNo, cDTime, cUid, uDTime, uUid, cus_serNo, ipRangeEnd, ipRangeStart) values('14',sysdate(),'admin',sysdate(),'admin','4','163.29.80.210','163.29.80.200');
insert into ip_range (serNo, cDTime, cUid, uDTime, uUid, cus_serNo, ipRangeEnd, ipRangeStart) values('15',sysdate(),'admin',sysdate(),'admin','9','163.29.10.210','163.29.10.200');
insert into ip_range (serNo, cDTime, cUid, uDTime, uUid, cus_serNo, ipRangeEnd, ipRangeStart) values('16',sysdate(),'admin',sysdate(),'admin','9','163.29.10.247','163.29.10.247');
insert into ip_range (serNo, cDTime, cUid, uDTime, uUid, cus_serNo, ipRangeEnd, ipRangeStart) values('17',sysdate(),'admin',sysdate(),'admin','3','117.56.1.254','117.56.1.1');
insert into ip_range (serNo, cDTime, cUid, uDTime, uUid, cus_serNo, ipRangeEnd, ipRangeStart) values('18',sysdate(),'admin',sysdate(),'admin','3','220.133.250.208','220.133.250.208');
insert into ip_range (serNo, cDTime, cUid, uDTime, uUid, cus_serNo, ipRangeEnd, ipRangeStart) values('19',sysdate(),'admin',sysdate(),'admin','10','163.29.14.210','163.29.14.200');
insert into ip_range (serNo, cDTime, cUid, uDTime, uUid, cus_serNo, ipRangeEnd, ipRangeStart) values('20',sysdate(),'admin',sysdate(),'admin','7','163.29.106.210','163.29.106.200');
insert into ip_range (serNo, cDTime, cUid, uDTime, uUid, cus_serNo, ipRangeEnd, ipRangeStart) values('21',sysdate(),'admin',sysdate(),'admin','10','163.29.81.254','163.29.81.1');
insert into ip_range (serNo, cDTime, cUid, uDTime, uUid, cus_serNo, ipRangeEnd, ipRangeStart) values('22',sysdate(),'admin',sysdate(),'admin','10','203.65.149.254','203.65.149.193');
insert into ip_range (serNo, cDTime, cUid, uDTime, uUid, cus_serNo, ipRangeEnd, ipRangeStart) values('23',sysdate(),'admin',sysdate(),'admin','6','163.29.75.254','163.29.75.1');
insert into ip_range (serNo, cDTime, cUid, uDTime, uUid, cus_serNo, ipRangeEnd, ipRangeStart) values('24',sysdate(),'admin',sysdate(),'admin','12','163.29.73.160','163.29.73.150');
insert into ip_range (serNo, cDTime, cUid, uDTime, uUid, cus_serNo, ipRangeEnd, ipRangeStart) values('25',sysdate(),'admin',sysdate(),'admin','8','163.29.85.254','163.29.85.1');
insert into ip_range (serNo, cDTime, cUid, uDTime, uUid, cus_serNo, ipRangeEnd, ipRangeStart) values('26',sysdate(),'admin',sysdate(),'admin','1','163.29.112.210 ','163.29.112.200');
insert into ip_range (serNo, cDTime, cUid, uDTime, uUid, cus_serNo, ipRangeEnd, ipRangeStart) values('27',sysdate(),'admin',sysdate(),'admin','2','163.29.108.151','163.29.108.110');
insert into ip_range (serNo, cDTime, cUid, uDTime, uUid, cus_serNo, ipRangeEnd, ipRangeStart) values('28',sysdate(),'admin',sysdate(),'admin','4','163.29.114.254','163.29.114.1');
insert into ip_range (serNo, cDTime, cUid, uDTime, uUid, cus_serNo, ipRangeEnd, ipRangeStart) values('29',sysdate(),'admin',sysdate(),'admin','6','163.29.107.254','163.29.107.1');
insert into ip_range (serNo, cDTime, cUid, uDTime, uUid, cus_serNo, ipRangeEnd, ipRangeStart) values('30',sysdate(),'admin',sysdate(),'admin','8','163.29.111.200','163.29.111.200');
insert into ip_range (serNo, cDTime, cUid, uDTime, uUid, cus_serNo, ipRangeEnd, ipRangeStart) values('31',sysdate(),'admin',sysdate(),'admin','10','163.29.99.99','163.29.99.89');
insert into ip_range (serNo, cDTime, cUid, uDTime, uUid, cus_serNo, ipRangeEnd, ipRangeStart) values('32',sysdate(),'admin',sysdate(),'admin','11','163.29.11.254','163.29.11.1');
insert into ip_range (serNo, cDTime, cUid, uDTime, uUid, cus_serNo, ipRangeEnd, ipRangeStart) values('33',sysdate(),'admin',sysdate(),'admin','11','163.29.7.210','163.29.7.200');
insert into ip_range (serNo, cDTime, cUid, uDTime, uUid, cus_serNo, ipRangeEnd, ipRangeStart) values('34',sysdate(),'admin',sysdate(),'admin','13','210.69.125.20','210.69.125.1');
insert into ip_range (serNo, cDTime, cUid, uDTime, uUid, cus_serNo, ipRangeEnd, ipRangeStart) values('35',sysdate(),'admin',sysdate(),'admin','1','163.29.77.210','163.29.77.200');
insert into ip_range (serNo, cDTime, cUid, uDTime, uUid, cus_serNo, ipRangeEnd, ipRangeStart) values('36',sysdate(),'admin',sysdate(),'admin','2','163.29.113.200','163.29.113.200');
insert into ip_range (serNo, cDTime, cUid, uDTime, uUid, cus_serNo, ipRangeEnd, ipRangeStart) values('37',sysdate(),'admin',sysdate(),'admin','6','163.29.116.254','163.29.116.1');
insert into ip_range (serNo, cDTime, cUid, uDTime, uUid, cus_serNo, ipRangeEnd, ipRangeStart) values('38',sysdate(),'admin',sysdate(),'admin','9','163.29.109.254','163.29.109.1');
insert into ip_range (serNo, cDTime, cUid, uDTime, uUid, cus_serNo, ipRangeEnd, ipRangeStart) values('39',sysdate(),'admin',sysdate(),'admin','9','59.125.248.253','59.125.248.250');
insert into ip_range (serNo, cDTime, cUid, uDTime, uUid, cus_serNo, ipRangeEnd, ipRangeStart) values('40',sysdate(),'admin',sysdate(),'admin','5','210.241.120.69','210.241.120.60');
insert into ip_range (serNo, cDTime, cUid, uDTime, uUid, cus_serNo, ipRangeEnd, ipRangeStart) values('41',sysdate(),'admin',sysdate(),'admin','9','210.241.100.254','210.241.100.254');
insert into ip_range (serNo, cDTime, cUid, uDTime, uUid, cus_serNo, ipRangeEnd, ipRangeStart) values('42',sysdate(),'admin',sysdate(),'admin','8','211.79.180.127','211.79.180.1');
insert into ip_range (serNo, cDTime, cUid, uDTime, uUid, cus_serNo, ipRangeEnd, ipRangeStart) values('43',sysdate(),'admin',sysdate(),'admin','8','210.241.14.158','210.241.14.128');
insert into ip_range (serNo, cDTime, cUid, uDTime, uUid, cus_serNo, ipRangeEnd, ipRangeStart) values('44',sysdate(),'admin',sysdate(),'admin','1','203.65.72.254','203.65.71.1');
insert into ip_range (serNo, cDTime, cUid, uDTime, uUid, cus_serNo, ipRangeEnd, ipRangeStart) values('45',sysdate(),'admin',sysdate(),'admin','1','59.120.22.254','59.120.22.1');
insert into ip_range (serNo, cDTime, cUid, uDTime, uUid, cus_serNo, ipRangeEnd, ipRangeStart) values('46',sysdate(),'admin',sysdate(),'admin','2','201.69.111.249','201.69.111.249');
insert into ip_range (serNo, cDTime, cUid, uDTime, uUid, cus_serNo, ipRangeEnd, ipRangeStart) values('47',sysdate(),'admin',sysdate(),'admin','7','210.69.214.41','210.69.214.41');
insert into ip_range (serNo, cDTime, cUid, uDTime, uUid, cus_serNo, ipRangeEnd, ipRangeStart) values('48',sysdate(),'admin',sysdate(),'admin','5','203.65.114.33','203.65.114.33');
insert into ip_range (serNo, cDTime, cUid, uDTime, uUid, cus_serNo, ipRangeEnd, ipRangeStart) values('49',sysdate(),'admin',sysdate(),'admin','5','117.56.56.130','117.56.56.130');
insert into ip_range (serNo, cDTime, cUid, uDTime, uUid, cus_serNo, ipRangeEnd, ipRangeStart) values('50',sysdate(),'admin',sysdate(),'admin','8','203.65.102.254','203.65.102.1');
insert into ip_range (serNo, cDTime, cUid, uDTime, uUid, cus_serNo, ipRangeEnd, ipRangeStart) values('51',sysdate(),'admin',sysdate(),'admin','8','203.65.103.254','203.65.103.1');
insert into ip_range (serNo, cDTime, cUid, uDTime, uUid, cus_serNo, ipRangeEnd, ipRangeStart) values('52',sysdate(),'admin',sysdate(),'admin','8','203.65.104.1','203.65.104.1');
insert into ip_range (serNo, cDTime, cUid, uDTime, uUid, cus_serNo, ipRangeEnd, ipRangeStart) values('53',sysdate(),'admin',sysdate(),'admin','8','203.65.106.254','203.65.106.1');
insert into ip_range (serNo, cDTime, cUid, uDTime, uUid, cus_serNo, ipRangeEnd, ipRangeStart) values('54',sysdate(),'admin',sysdate(),'admin','8','203.65.109.254','203.65.109.1');
insert into ip_range (serNo, cDTime, cUid, uDTime, uUid, cus_serNo, ipRangeEnd, ipRangeStart) values('55',sysdate(),'admin',sysdate(),'admin','8','203.65.100.13','203.65.100.13');
insert into ip_range (serNo, cDTime, cUid, uDTime, uUid, cus_serNo, ipRangeEnd, ipRangeStart) values('56',sysdate(),'admin',sysdate(),'admin','8','210.241.104.254','210.241.104.128');


--BE_Logs
insert into BE_Logs(serNo, cDTime, actionType, acc_serNo, cus_serNo) values(1, sysdate(), '登入', 1, 1);
insert into BE_Logs(serNo, cDTime, actionType, acc_serNo, cus_serNo) values(2, sysdate(), '登入', 2, 2);
insert into BE_Logs(serNo, cDTime, actionType, acc_serNo, cus_serNo) values(3, sysdate(), '登入', 3, 5);
insert into BE_Logs(serNo, cDTime, actionType, acc_serNo, cus_serNo) values(4, sysdate(), '登入', 4, 1);
insert into BE_Logs(serNo, cDTime, actionType, acc_serNo, cus_serNo) values(5, sysdate(), '登入', 5, 2);
insert into BE_Logs(serNo, cDTime, actionType, acc_serNo, cus_serNo) values(6, sysdate(), '登入', 6, 5);
insert into BE_Logs(serNo, cDTime, actionType, acc_serNo, cus_serNo) values(7, sysdate(), '登入', 7, 1);
insert into BE_Logs(serNo, cDTime, actionType, acc_serNo, cus_serNo) values(8, sysdate(), '登入', 8, 2);
insert into BE_Logs(serNo, cDTime, actionType, acc_serNo, cus_serNo) values(9, sysdate(), '登入', 9, 9);
insert into BE_Logs(serNo, cDTime, actionType, acc_serNo, cus_serNo) values(10, sysdate(), '登入', 10, 1);
insert into BE_Logs(serNo, cDTime, actionType, acc_serNo, cus_serNo) values(11, sysdate(), '登入', 11, 2);
insert into BE_Logs(serNo, cDTime, actionType, acc_serNo, cus_serNo) values(12, sysdate(), '登入', 12, 5);
insert into BE_Logs(serNo, cDTime, actionType, acc_serNo, cus_serNo) values(13, sysdate(), '登入', 1, 1);

--FE_Logs
insert into FE_Logs(serNo, cDTime, actionType, keyword, cus_SerNo, acc_SerNo, dat_SerNo, ebk_SerNo, jou_SerNo) values(1, sysdate(), '綜合查詢', 'Hello', 1, 1, null, null, null);
insert into FE_Logs(serNo, cDTime, actionType, keyword, cus_SerNo, acc_SerNo, dat_SerNo, ebk_SerNo, jou_SerNo) values(2, sysdate(), '綜合查詢', 'world', 1, 1, null, null, null);
insert into FE_Logs(serNo, cDTime, actionType, keyword, cus_SerNo, acc_SerNo, dat_SerNo, ebk_SerNo, jou_SerNo) values(3, sysdate(), '綜合查詢', 'c++', 1, 1, null, null, null);
insert into FE_Logs(serNo, cDTime, actionType, keyword, cus_SerNo, acc_SerNo, dat_SerNo, ebk_SerNo, jou_SerNo) values(4, sysdate(), '綜合查詢', 'python', 1, 1, null, null, null);
insert into FE_Logs(serNo, cDTime, actionType, keyword, cus_SerNo, acc_SerNo, dat_SerNo, ebk_SerNo, jou_SerNo) values(5, sysdate(), '綜合查詢', 'php', 1, 1, null, null, null);
insert into FE_Logs(serNo, cDTime, actionType, keyword, cus_SerNo, acc_SerNo, dat_SerNo, ebk_SerNo, jou_SerNo) values(6, sysdate(), '綜合查詢', 'ruby', 1, 1, null, null, null);
insert into FE_Logs(serNo, cDTime, actionType, keyword, cus_SerNo, acc_SerNo, dat_SerNo, ebk_SerNo, jou_SerNo) values(7, sysdate(), '綜合查詢', 'java', 1, 1, null, null, null);
insert into FE_Logs(serNo, cDTime, actionType, keyword, cus_SerNo, acc_SerNo, dat_SerNo, ebk_SerNo, jou_SerNo) values(8, sysdate(), '綜合查詢', 'javascript', 1, 1, null, null, null);
insert into FE_Logs(serNo, cDTime, actionType, keyword, cus_SerNo, acc_SerNo, dat_SerNo, ebk_SerNo, jou_SerNo) values(9, sysdate(), '綜合查詢', 'jQuery', 1, 1, null, null, null);
insert into FE_Logs(serNo, cDTime, actionType, keyword, cus_SerNo, acc_SerNo, dat_SerNo, ebk_SerNo, jou_SerNo) values(10, sysdate(), '綜合查詢', 'c#', 1, 1, null, null, null);
insert into FE_Logs(serNo, cDTime, actionType, keyword, cus_SerNo, acc_SerNo, dat_SerNo, ebk_SerNo, jou_SerNo) values(11, sysdate(), '綜合查詢', 'json', 1, 1, null, null, null);
insert into FE_Logs(serNo, cDTime, actionType, keyword, cus_SerNo, acc_SerNo, dat_SerNo, ebk_SerNo, jou_SerNo) values(12, sysdate(), '綜合查詢', 'xml', 1, 1, null, null, null);
insert into FE_Logs(serNo, cDTime, actionType, keyword, cus_SerNo, acc_SerNo, dat_SerNo, ebk_SerNo, jou_SerNo) values(13, sysdate(), '綜合查詢', 'html', 1, 1, null, null, null);
insert into FE_Logs(serNo, cDTime, actionType, keyword, cus_SerNo, acc_SerNo, dat_SerNo, ebk_SerNo, jou_SerNo) values(14, sysdate(), '綜合查詢', 'jsp', 1, 1, null, null, null);
insert into FE_Logs(serNo, cDTime, actionType, keyword, cus_SerNo, acc_SerNo, dat_SerNo, ebk_SerNo, jou_SerNo) values(15, sysdate(), '綜合查詢', 'objective-c', 1, 1, null, null, null);
insert into FE_Logs(serNo, cDTime, actionType, keyword, cus_SerNo, acc_SerNo, dat_SerNo, ebk_SerNo, jou_SerNo) values(16, sysdate(), '綜合查詢', 'java', 1, 1, null, null, null);
insert into FE_Logs(serNo, cDTime, actionType, keyword, cus_SerNo, acc_SerNo, dat_SerNo, ebk_SerNo, jou_SerNo) values(17, sysdate(), '綜合查詢', 'java', 2, 2, null, null, null);