package com.synapsys.user.example.process.endless;

import com.synapsys.springboot.starter.core.AbstractEndlessProcess;
import com.synapsys.springboot.starter.core.SynapsysProcess;
import lombok.extern.slf4j.Slf4j;

/**
 * Written in 2009 and never modernised: no streams, no var, no lambdas.
 *
 * It compiles and runs unchanged. Adopting Synapsys meant extending a base class
 * and adding an annotation, not rewriting the body.
 */
@Slf4j
@SynapsysProcess(name = "legacy-nightly-sync")
public class LegacyNightlySyncProcess extends AbstractEndlessProcess {

    private int recordsSynced = 0;

    @Override
    protected void runForever(StopSignal stopSignal) throws Exception {
        while (!stopSignal.isStopRequested()) {
            int batch = 0;
            for (int i = 0; i < 10; i++) {
                batch = batch + (i * 3) % 7;
            }
            recordsSynced = recordsSynced + batch;
            log.info("synced " + batch + " records (" + recordsSynced + " total)");
            Thread.sleep(4000);
        }
    }
}
