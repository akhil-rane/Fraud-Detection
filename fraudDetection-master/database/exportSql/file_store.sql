CREATE TABLE `FILE_STORE` (
  `FILE_ID` int(11) NOT NULL AUTO_INCREMENT,
  `FILE_NAME` varchar(45) NOT NULL,
  `FILE_BYTES` blob NOT NULL,
  `INSERT_DT` datetime NOT NULL,
  `COMPANY_ID` int(11) NOT NULL,
  `USER_ID` int(11) NOT NULL,
  `ref_id` varchar(45) NOT NULL,
  `request_id` varchar(45) NOT NULL,
  `source_app` int(11) NOT NULL,
  PRIMARY KEY (`FILE_ID`)
)