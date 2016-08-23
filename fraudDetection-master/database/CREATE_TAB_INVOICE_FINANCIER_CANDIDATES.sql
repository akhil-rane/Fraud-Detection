CREATE TABLE `INVOICE_FINANCIER_CANDIDATES` (
  `candidate_id` INT(11) NOT NULL,
  `invoice_id` INT(11) NOT NULL,
  `financier_id` INT(11) NOT NULL,
  `interest_rate` DECIMAL(2,2) NOT NULL,
  `loan_period` INT NOT NULL,
  `ref_id` VARCHAR(45) NOT NULL,
  `request_id` VARCHAR(45) NOT NULL,
  `user_id` int(11) NOT NULL,
  `source_app` INT(11) NOT NULL,
  PRIMARY KEY (`candidate_id`),
  INDEX `FK_INVOICE_ID_idx` (`invoice_id` ASC),
  CONSTRAINT `FK_INVOICE_F_CANDIDATES_ID`
    FOREIGN KEY (`invoice_id`)
    REFERENCES `INVOICE_INFO` (`invoice_id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE);
