CREATE TABLE `INVOICE_FINANCIER_MAP` (
  `invoice_id` int(11) NOT NULL,
  `financier_id` int(11) NOT NULL,
  `status` varchar(10) NOT NULL,
  `insert_dt` date NOT NULL,
  `payback_dt` date NOT NULL,
  `ref_id` varchar(45) NOT NULL,
  `request_id` varchar(45) NOT NULL,
  `user_id` int(11) NOT NULL,
  `source_app` int(11) NOT NULL,
  KEY `FK_INVOICE_ID_IFM_idx` (`invoice_id`),
  CONSTRAINT `FK_INVOICE_ID_IFM` FOREIGN KEY (`invoice_id`) REFERENCES `INVOICE_INFO` (`invoice_id`) ON DELETE CASCADE ON UPDATE CASCADE
) 