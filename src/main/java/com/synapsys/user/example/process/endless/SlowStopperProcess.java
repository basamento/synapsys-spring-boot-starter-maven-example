package com.synapsys.user.example.process.endless;

import com.synapsys.springboot.starter.core.AbstractEndlessProcess;
import com.synapsys.springboot.starter.core.SynapsysProcess;
import lombok.extern.slf4j.Slf4j;

/**
 * Takes its time noticing a stop, so 'stopping' is visible in the console.
 *
 * It swallows the interrupt deliberately and only checks the stop signal between
 * chunks, which is what a process doing non-interruptible work looks like. Expect
 * it to sit in 'stopping' for up to fifteen seconds before reaching idle.
 *
 * The stop is still honoured. The lesson is that the state is honest about the
 * delay rather than hiding it, and that checking the signal more often is what
 * shortens it.
 */
@Slf4j
@SynapsysProcess(name = "slow-stopper")
public class SlowStopperProcess extends AbstractEndlessProcess {

    @Override
    protected void runForever(StopSignal stopSignal) throws Exception {
        long chunk = 0;
        while (!stopSignal.isStopRequested()) {
            chunk++;
            log.info("chunk {} started, this one cannot be interrupted for ~15s", chunk);
            long deadline = System.currentTimeMillis() + 15_000;
            while (System.currentTimeMillis() < deadline) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    // Deliberately not rethrown: this stands for work that has to
                    // finish, such as a transaction that must commit. The stop is
                    // taken on the next pass of the outer loop.
                    Thread.interrupted();
                    log.info("chunk {} was interrupted, but must finish first", chunk);
                }
            }
            log.info("chunk {} finished", chunk);
        }
        log.info("stop taken at a safe point");
    }
}
