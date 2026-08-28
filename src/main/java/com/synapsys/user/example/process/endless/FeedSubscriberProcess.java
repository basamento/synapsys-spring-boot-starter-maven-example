package com.synapsys.user.example.process.endless;

import com.synapsys.springboot.starter.core.AbstractEndlessProcess;
import com.synapsys.springboot.starter.core.SynapsysProcess;
import com.synapsys.user.example.service.WorkSimulator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Blocked in I/O when the stop arrives.
 *
 * The starter interrupts the worker thread, so the blocking call throws
 * InterruptedException rather than running to completion. Letting it propagate is
 * correct: the starter treats it as a stop, not a failure, and the process reaches
 * idle immediately instead of after the full ten seconds.
 */
@Slf4j
@RequiredArgsConstructor
@SynapsysProcess(name = "feed-subscriber")
public class FeedSubscriberProcess extends AbstractEndlessProcess {

    private final WorkSimulator work;

    @Override
    protected void runForever(StopSignal stopSignal) throws Exception {
        while (!stopSignal.isStopRequested()) {
            log.info("waiting for the upstream feed (blocks for ~10s)");
            long startedAt = System.currentTimeMillis();
            work.simulateIo(10_000);
            log.info("feed received after {}ms", System.currentTimeMillis() - startedAt);
            Thread.sleep(2_000);
        }
    }
}
