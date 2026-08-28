package com.synapsys.user.example.process.endless;

import com.synapsys.springboot.starter.core.AbstractEndlessProcess;
import com.synapsys.springboot.starter.core.SynapsysProcess;
import com.synapsys.user.example.service.InvoiceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * The canonical endless process: poll, sleep, repeat, until told to stop.
 *
 * Everything else in this package is a variation on this shape.
 */
@Slf4j
@RequiredArgsConstructor
@SynapsysProcess(name = "invoice-poller")
public class InvoicePollerProcess extends AbstractEndlessProcess {

    private final InvoiceService invoices;

    @Override
    protected void runForever(StopSignal stopSignal) throws Exception {
        log.info("polling for invoices every 5s");
        while (!stopSignal.isStopRequested()) {
            invoices.pollInvoices();
            Thread.sleep(5_000);
        }
        log.info("stop observed, leaving the loop");
    }
}
