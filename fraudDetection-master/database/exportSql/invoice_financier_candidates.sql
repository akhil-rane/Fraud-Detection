CREATE TABLE `INVOICE_FINANCIER_CANDIDATES` (
  `candidate_id` int(11) NOT NULL AUTO_INCREMENT,
  `invoice_id` int(11) NOT NULL,
  `financier_id` int(11) NOT NULL,
  `interest_rate` double NOT NULL,
  `loan_period` int(11) NOT NULL,
  `ref_id` varchar(45) NOT NULL,
  `request_id` varchar(45) NOT NULL,
  `update_by` varchar(45) NOT NULL,
  `source_app` int(11) NOT NULL,
  `insert_dt` datetime,
  PRIMARY KEY (`candidate_id`),
  KEY `FK_INVOICE_ID_idx` (`invoice_id`),
  CONSTRAINT `FK_INVOICE_F_CANDIDATES_ID` FOREIGN KEY (`invoice_id`) REFERENCES `INVOICE_INFO` (`invoice_id`) ON DELETE CASCADE ON UPDATE CASCADE
)