CREATE TABLE `INVOICE_NOTIFICATION_LIST` (
  `notification_id` int(11) NOT NULL AUTO_INCREMENT,
  `invoice_id` int(11) NOT NULL,
  `financier_id` int(11) NOT NULL,
  `insert_dt` datetime NOT NULL,
  `expiration_dt` datetime DEFAULT NULL,
  PRIMARY KEY (`notification_id`),
  KEY `FK_INVOICE_ID_idx` (`invoice_id`),
  CONSTRAINT `FK_INVOICE_ID` FOREIGN KEY (`invoice_id`) REFERENCES `INVOICE_INFO` (`invoice_id`) ON DELETE CASCADE ON UPDATE CASCADE
)