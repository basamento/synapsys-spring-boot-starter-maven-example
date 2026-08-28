package com.synapsys.user.example.process.endless;

import com.synapsys.springboot.starter.core.AbstractEndlessProcess;
import com.synapsys.springboot.starter.core.SynapsysProcess;
import lombok.extern.slf4j.Slf4j;

/** Infrequent housekeeping. Sleeps far longer than the heartbeat interval. */
@Slf4j
@SynapsysProcess(name = "session-reaper")
public class SessionReaperProcess extends AbstractEndlessProcess {

    @Override
    protected void runForever(StopSignal stopSignal) throws Exception {
        long sweep = 0;
        while (!stopSignal.isStopRequested()) {
            sweep++;
            int expired = (int) (sweep * 7 % 23);
            log.info("sweep {}: expired {} idle session(s)", sweep, expired);
            // Longer than one heartbeat on purpose: the process stays 'running'
            // between sweeps, and the stop still lands immediately because the
            // starter interrupts this sleep.
            Thread.sleep(30_000);
        }
    }
}
