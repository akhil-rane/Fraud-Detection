INSERT INTO `ficore`.`client_login_info`
(`USER_ID`,
`EMAIL`,
`PASSWORD`,
`INSERT_DT`,
`REGISTRATION_STATUS`)
VALUES
(1,
'abc.xyz@gmail.com',
'Welcome1',
sysdate(),
1);


INSERT INTO `ficore`.`file_store`
(`FILE_ID`,
`FILE_NAME`,
`FILE_BYTES`,
`INSERT_DT`,
`COMPANY_ID`,
`USER_ID`,
`ref_id`,
`request_id`,
`source_app`)
VALUES
(1,
'InvoiceUpload.xls',
'Invoice',
sysdate(),
1,
1,
'ABCD',
'REQ1',
0);

INSERT INTO `ficore`.`invoice_info`
(`invoice_start_dt`,
`invoice_end_dt`,
`amount`,
`invoice_id`,
`invoice_no`,
`company_id`,
`file_id`,
`buyer_name`,
`buyer_id`,
`buyer_approval`,
`insert_dt`,
`description`,
`ref_id`,
`request_id`,
`user_id`,
`source_app`)
VALUES
(sysdate(), sysdate(),
10000,
1,
100,
1,
1,
'Walmart',
3,
'N',
sysdate(), 'MyInvoice2', 'XYZ', 'REQ2',1, 0);


INSERT INTO `ficore`.`client_company_info`
(`COMPANY_ID`,
`USER_ID`,
`BANK_ACCOUNT_NO`,
`BANK_IFSC_CODE`,
`BANK_NAME`,
`BRANCH_NAME`,
`DIRECTOR_FNAME`,
`DIRECTOR_LNAME`,
`PAN_CARD_NO`,
`AADHAR_CARD_ID`,
`COMPANY_NAME`,
`STREET`,
`CITY`,
`STATE`,
`ZIP_CODE`,
`COUNTRY`,
`PHONE_NO`,
`INSERT_DT`,
`ACRONYM`)
VALUES
(1,
1,
000000000000,
98765,
'Bank of Punjab',
'Chennai',
'Mr. Foo',
'Mr. Bar',
'AAAAAAAAAA',
'1234567890',
'Cottage Industries',
'Royce Rd',
'Chennai',
'Tamil Nadu',
'421501',
'India',
617880000,
sysdate(),
'COTIND'
);

INSERT INTO `ficore`.`organization_info`
(`COMPANY_ID`,
`ACRONYM`,
`COMPANY_NAME`,
`STREET`,
`CITY`,
`STATE`,
`ZIP_CODE`,
`COUNTRY`,
`PHONE_NO`,
`INSERT_DT`,
`UPDATE_DT`,
`UPDATE_BY`,
`CREATED_BY`,
`ORG_TYPE`)
VALUES
(1,
'COTIND',
'XYZ Industries',
'Royce Rd',
'Chennai',
'Tamil Nadu',
'421501',
'INDIA',
617880000,
sysdate(),
sysdate(),
'ABC',
'ABC',
'SELLER');


