package com.synapsys.user.example.process.progressive;

import com.synapsys.springboot.starter.core.AbstractProgressiveProcess;
import com.synapsys.springboot.starter.core.SynapsysProcess;
import com.synapsys.user.example.service.InvoiceService;
import com.synapsys.user.example.service.WorkSimulator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Two injected services in one process. Reuses InvoiceService, which the
 * invoice-poller endless process also uses - the same business logic backing both
 * a continuous and a one-shot process.
 */
@Slf4j
@RequiredArgsConstructor
@SynapsysProcess(name = "tax-calculator")
public class TaxCalculatorProcess extends AbstractProgressiveProcess {

    private final InvoiceService invoices;
    private final WorkSimulator work;

    @Override
    protected void run() throws Exception {
        log.info("calculating tax over the current invoice set");
        for (int quarter = 1; quarter <= 4; quarter++) {
            checkCancelled();
            invoices.pollInvoices();
            work.burnCpu(5_000_000L);
            log.info("  Q{} settled ({} polls seen in total)", quarter, invoices.totalPolls());
        }
        log.info("tax calculation complete");
    }
}
