package com.floatinvoice.business.dao;

import com.floatinvoice.messages.FraudInvoiceDtls;
import com.floatinvoice.messages.ListMsg;

public interface FraudInvoiceInfoDao {

	ListMsg<FraudInvoiceDtls> findFraudInvoicesBySupplier( int orgId );
	ListMsg<FraudInvoiceDtls> findAllFraudInvoices();
}
