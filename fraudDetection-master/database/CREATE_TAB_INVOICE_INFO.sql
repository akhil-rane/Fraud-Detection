CREATE TABLE `INVOICE_INFO` (
  `invoice_start_dt` DATE NOT NULL,
  `invoice_end_dt` DATE NOT NULL,
  `amount` DECIMAL(8,2) NOT NULL,
  `invoice_id` INT(11) NOT NULL AUTO_INCREMENT,
  `invoice_no` VARCHAR(45) NOT NULL,
  `company_id` INT(11) NOT NULL,
  `file_id` INT(11) NOT NULL,
  `buyer_name` VARCHAR(45) NOT NULL,
  `buyer_id` INT(11) NULL,
  `buyer_approval` char(1) NOT NULL DEFAULT 'N',
  `insert_dt` DATETIME NULL,
  `description` VARCHAR(45) NOT NULL,
  `ref_id` VARCHAR(45) NOT NULL,
  `request_id` VARCHAR(45) NOT NULL,
  `user_id` int(11) NOT NULL,
  `source_app` INT(11) NOT NULL,
  
  PRIMARY KEY (`invoice_id`),
  INDEX `fk_file_id_idx` (`file_id` ASC),
  CONSTRAINT `fk_file_id`
    FOREIGN KEY (`file_id`)
    REFERENCES `FILE_STORE` (`FILE_ID`)
    ON DELETE CASCADE
    ON UPDATE CASCADE);