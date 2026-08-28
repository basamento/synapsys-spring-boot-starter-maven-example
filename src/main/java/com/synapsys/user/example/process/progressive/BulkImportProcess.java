package com.synapsys.user.example.process.progressive;

import com.synapsys.springboot.starter.core.AbstractProgressiveProcess;
import com.synapsys.springboot.starter.core.SynapsysProcess;
import com.synapsys.user.example.service.WorkSimulator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * A long job over many items, cancellable between them.
 *
 * checkCancelled() is the cancellation point. Stop it mid-run and it returns to
 * idle, not failed: a stop is not a failure.
 */
@Slf4j
@RequiredArgsConstructor
@SynapsysProcess(name = "bulk-import")
public class BulkImportProcess extends AbstractProgressiveProcess {

    private static final int ROWS = 40;

    private final WorkSimulator work;

    @Override
    protected void run() throws Exception {
        log.info("importing {} rows", ROWS);
        for (int row = 1; row <= ROWS; row++) {
            checkCancelled();
            work.simulateIo(250);
            if (row % 5 == 0 || row == ROWS) {
                log.info("  imported {}/{} rows", row, ROWS);
            }
        }
        log.info("import finished - all {} rows", ROWS);
    }
}
