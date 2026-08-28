package com.synapsys.user.example.process.endless;

import com.synapsys.springboot.starter.core.AbstractEndlessProcess;
import com.synapsys.springboot.starter.core.SynapsysProcess;
import lombok.extern.slf4j.Slf4j;

/**
 * Fails at its work, but handles it and keeps going.
 *
 * The contrast worth noticing: this process stays 'running' because it catches its
 * own exception. Letting one escape would end the run as 'failed' instead — see
 * CrashingWatchdogProcess.
 */
@Slf4j
@SynapsysProcess(name = "dead-letter-retrier")
public class DeadLetterRetrierProcess extends AbstractEndlessProcess {

    @Override
    protected void runForever(StopSignal stopSignal) throws Exception {
        long attempt = 0;
        while (!stopSignal.isStopRequested()) {
            attempt++;
            try {
                if (attempt % 3 != 0) {
                    throw new IllegalStateException("dead letter " + attempt + " still undeliverable");
                }
                log.info("attempt {}: dead letter redelivered", attempt);
            } catch (IllegalStateException e) {
                // Handled here, so the process keeps running. This is a retry loop,
                // not a failure.
                log.warn("attempt {}: {} | will retry", attempt, e.getMessage());
            }
            Thread.sleep(3_000);
        }
    }
}
