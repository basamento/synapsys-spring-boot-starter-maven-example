package com.synapsys.user.example.process.endless;

import com.synapsys.springboot.starter.core.AbstractEndlessProcess;
import com.synapsys.springboot.starter.core.SynapsysProcess;
import lombok.extern.slf4j.Slf4j;

/**
 * An endless process that dies partway through, on purpose.
 *
 * Shows the failure path for the endless type: the run ends as 'failed' rather
 * than 'idle', the stack trace reaches the console in Core, and the exception
 * never surfaces on the host application's own console. Start it again to clear
 * the state; every third run survives.
 *
 * This is the deliberate counterpart to DeadLetterRetrierProcess, which handles
 * the same kind of error itself and therefore keeps running.
 */
@Slf4j
@SynapsysProcess(name = "crashing-watchdog")
public class CrashingWatchdogProcess extends AbstractEndlessProcess {

    private int runs = 0;

    @Override
    protected void runForever(StopSignal stopSignal) throws Exception {
        runs++;
        log.info("watchdog run {} started (this process fails deliberately)", runs);

        long tick = 0;
        while (!stopSignal.isStopRequested()) {
            tick++;
            log.info("tick {}", tick);
            Thread.sleep(2_000);

            if (tick == 3 && runs % 3 != 0) {
                log.info("throwing on purpose now - expect state=failed in the console");
                throw new IllegalStateException(
                        "DELIBERATE FAILURE (the example working): watchdog lost its lease on run " + runs);
            }
        }
    }
}
