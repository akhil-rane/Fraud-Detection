CREATE TABLE `INVOICE_NOTIFICATION_LIST` (
  `notification_id` INT(11) NOT NULL,
  `invoice_id` INT(11) NOT NULL,
  `financier_id` INT(11) NOT NULL,
  `insert_dt` DATETIME NOT NULL,
  `expiration_dt` DATETIME NULL,
  PRIMARY KEY (`notification_id`),
  INDEX `FK_INVOICE_ID_idx` (`invoice_id` ASC),
  CONSTRAINT `FK_INVOICE_ID`
    FOREIGN KEY (`invoice_id`)
    REFERENCES `INVOICE_INFO` (`invoice_id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE);
