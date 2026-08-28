package com.synapsys.user.example.process.progressive;

import com.synapsys.springboot.starter.core.AbstractProgressiveProcess;
import com.synapsys.springboot.starter.core.SynapsysProcess;
import com.synapsys.user.example.service.WorkSimulator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Fails on purpose, to show what a failure looks like from Core.
 *
 * The process lands in 'failed', the stack trace is captured as stderr and shown
 * against the run, and the exception never reaches the host application's own
 * error handling. Starting it again clears the state - 'failed' is a legal
 * starting point, and every third attempt succeeds.
 */
@Slf4j
@RequiredArgsConstructor
@SynapsysProcess(name = "flaky-importer")
public class FlakyImporterProcess extends AbstractProgressiveProcess {

    private final WorkSimulator work;
    private int attempts = 0;

    @Override
    protected void run() throws Exception {
        attempts++;
        log.info("attempt {}: connecting to upstream", attempts);
        log.info("  (this process fails deliberately - every third attempt succeeds)");
        work.simulateIo(600);

        if (attempts % 3 != 0) {
            log.info("  throwing on purpose now - expect state=failed in the console");
            throw new IllegalStateException(
                    "DELIBERATE FAILURE (the example working): upstream returned 502 for batch " + attempts);
        }

        log.info("attempt {}: batch accepted - the process returns to idle", attempts);
    }
}
