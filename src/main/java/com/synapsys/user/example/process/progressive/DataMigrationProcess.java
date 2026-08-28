package com.synapsys.user.example.process.progressive;

import com.synapsys.springboot.starter.core.AbstractProgressiveProcess;
import com.synapsys.springboot.starter.core.SynapsysProcess;
import com.synapsys.user.example.service.WorkSimulator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/** Long enough to always catch mid-run, with a cancellation point every step. */
@Slf4j
@RequiredArgsConstructor
@SynapsysProcess(name = "data-migration")
public class DataMigrationProcess extends AbstractProgressiveProcess {

    private static final int STEPS = 20;

    private final WorkSimulator work;

    @Override
    protected void run() throws Exception {
        log.info("migration starting: {} steps, ~3s each", STEPS);
        for (int step = 1; step <= STEPS; step++) {
            checkCancelled();
            log.info("  step {}/{} - migrating table batch {}", step, STEPS, step);
            work.simulateIo(3_000);
        }
        log.info("migration complete");
    }
}
