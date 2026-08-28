package com.synapsys.user.example.process.progressive;

import com.synapsys.springboot.starter.core.AbstractProgressiveProcess;
import com.synapsys.springboot.starter.core.SynapsysProcess;
import com.synapsys.user.example.service.WorkSimulator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Real CPU work, and the place the JVM differs most from the Node SDK.
 *
 * Every progressive process gets its own dedicated single-thread executor, so this
 * loop delays nothing but itself: heartbeats keep flowing and every other process
 * keeps running. The same loop written against the Node SDK would block the event
 * loop and stall the whole worker, which is why that library pushes CPU-bound work
 * onto a worker_thread.
 *
 * checkCancelled() still has to be called often enough. Thread.interrupt() sets a
 * flag; it does not stop a thread spinning in arithmetic.
 */
@Slf4j
@RequiredArgsConstructor
@SynapsysProcess(name = "checksum-cruncher")
public class ChecksumCruncherProcess extends AbstractProgressiveProcess {

    private static final int ROUNDS = 30;

    private final WorkSimulator work;

    @Override
    protected void run() throws Exception {
        double sink = 0;
        for (int round = 1; round <= ROUNDS; round++) {
            checkCancelled();
            sink += work.burnCpu(20_000_000L);
            log.info("  round {}/{} done on this process's own thread", round, ROUNDS);
        }
        log.info("checksum complete: {}", Math.round(sink));
    }
}
