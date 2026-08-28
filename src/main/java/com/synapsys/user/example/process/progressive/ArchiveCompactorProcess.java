package com.synapsys.user.example.process.progressive;

import com.synapsys.springboot.starter.core.AbstractProgressiveProcess;
import com.synapsys.springboot.starter.core.SynapsysProcess;
import com.synapsys.user.example.service.WorkSimulator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Progressive processes get onStop too, and it runs only when the run was actually
 * stopped or cancelled - not when it finished on its own. Let it complete and no
 * cleanup line appears; stop it mid-run and one does.
 */
@Slf4j
@RequiredArgsConstructor
@SynapsysProcess(name = "archive-compactor")
public class ArchiveCompactorProcess extends AbstractProgressiveProcess {

    private static final int SEGMENTS = 15;

    private final WorkSimulator work;
    private volatile boolean scratchOpen = false;

    @Override
    protected void run() throws Exception {
        scratchOpen = true;
        log.info("scratch space opened, compacting {} segments", SEGMENTS);
        for (int segment = 1; segment <= SEGMENTS; segment++) {
            checkCancelled();
            work.simulateIo(1_200);
            log.info("  segment {}/{} compacted", segment, SEGMENTS);
        }
        scratchOpen = false;
        log.info("compaction complete, scratch released normally");
    }

    @Override
    protected void onStop() {
        if (scratchOpen) {
            scratchOpen = false;
            log.info("stopped mid-run - scratch space released by onStop");
        }
    }
}
