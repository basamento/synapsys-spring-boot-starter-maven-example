package com.synapsys.user.example.process.endless;

import com.synapsys.springboot.starter.core.AbstractEndlessProcess;
import com.synapsys.springboot.starter.core.SynapsysProcess;
import lombok.extern.slf4j.Slf4j;

/**
 * Chatty on purpose: one line per second, to exercise the console capture path and
 * the per-beat log budget.
 */
@Slf4j
@SynapsysProcess(name = "metrics-publisher")
public class MetricsPublisherProcess extends AbstractEndlessProcess {

    @Override
    protected void runForever(StopSignal stopSignal) throws Exception {
        long sample = 0;
        while (!stopSignal.isStopRequested()) {
            sample++;
            log.info("sample {} | heap={}MB threads={}",
                    sample,
                    (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1048576,
                    Thread.activeCount());
            Thread.sleep(1_000);
        }
    }
}
