package com.synapsys.user.example.process.progressive;

import com.synapsys.springboot.starter.core.AbstractProgressiveProcess;
import com.synapsys.springboot.starter.core.SynapsysProcess;
import com.synapsys.user.example.service.WorkSimulator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Never calls checkCancelled(), on purpose.
 *
 * Start it, then press Stop. The process reports 'stopping' and stays there for the
 * full remaining run, because nothing in the body ever looks at the cancellation
 * flag. It does reach idle eventually, and the heartbeat is unaffected throughout -
 * the delay is confined to this process, because it owns its executor.
 *
 * The starter interrupts the thread, which is why the sleeps below still end early;
 * an interrupt sets a flag and unblocks waiting, but it cannot force a return.
 * Cancellation is cooperative in every Synapsys client, and this is what declining
 * to cooperate looks like.
 */
@Slf4j
@RequiredArgsConstructor
@SynapsysProcess(name = "uncancellable-batch")
public class UncancellableBatchProcess extends AbstractProgressiveProcess {

    private static final int BATCHES = 12;

    private final WorkSimulator work;

    @Override
    protected void run() throws Exception {
        log.info("{} batches, and no cancellation point anywhere in this loop", BATCHES);
        for (int batch = 1; batch <= BATCHES; batch++) {
            // Deliberately no checkCancelled() here.
            work.burnCpu(15_000_000L);
            log.info("  batch {}/{} - a stop cannot land until this loop ends", batch, BATCHES);
        }
        log.info("batches finished; only now can the process return to idle");
    }
}
