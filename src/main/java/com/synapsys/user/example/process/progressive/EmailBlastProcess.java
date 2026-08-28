package com.synapsys.user.example.process.progressive;

import com.synapsys.springboot.starter.core.AbstractProgressiveProcess;
import com.synapsys.springboot.starter.core.SynapsysProcess;
import com.synapsys.user.example.service.WorkSimulator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Many small items. Checks for cancellation on every one, so a stop lands within
 * a single recipient rather than at the end of the batch.
 */
@Slf4j
@RequiredArgsConstructor
@SynapsysProcess(name = "email-blast")
public class EmailBlastProcess extends AbstractProgressiveProcess {

    private static final int RECIPIENTS = 60;

    private final WorkSimulator work;

    @Override
    protected void run() throws Exception {
        log.info("sending to {} recipients", RECIPIENTS);
        int sent = 0;
        for (int i = 1; i <= RECIPIENTS; i++) {
            checkCancelled();
            work.simulateIo(150);
            sent++;
            if (i % 10 == 0) {
                log.info("  {}/{} sent", sent, RECIPIENTS);
            }
        }
        log.info("blast complete, {} delivered", sent);
    }
}
