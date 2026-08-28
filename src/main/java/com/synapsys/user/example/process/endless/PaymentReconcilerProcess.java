package com.synapsys.user.example.process.endless;

import com.synapsys.springboot.starter.core.AbstractEndlessProcess;
import com.synapsys.springboot.starter.core.SynapsysProcess;
import com.synapsys.user.example.service.WorkSimulator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Uses an injected service, which is the whole reason to declare a process as a
 * bean: constructor injection works exactly as it does anywhere else in the
 * application.
 */
@Slf4j
@RequiredArgsConstructor
@SynapsysProcess(name = "payment-reconciler")
public class PaymentReconcilerProcess extends AbstractEndlessProcess {

    private final WorkSimulator work;

    @Override
    protected void runForever(StopSignal stopSignal) throws Exception {
        long batch = 0;
        while (!stopSignal.isStopRequested()) {
            batch++;
            int matched = work.countFor(batch, 40);
            work.simulateIo(400);
            log.info("batch {}: reconciled {} payment(s) against the ledger", batch, matched);
            Thread.sleep(4_000);
        }
    }
}
