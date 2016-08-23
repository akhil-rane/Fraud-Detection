package com.floatinvoice.business;

import com.floatinvoice.messages.FraudInvoiceDtls;
import com.floatinvoice.messages.ListMsg;

public interface FraudInvoiceService {

	ListMsg<FraudInvoiceDtls> findFraudInvoicesBySupplier( String acronym );
	ListMsg<FraudInvoiceDtls> findAllFraudInvoices();
}
