package com.synapsys.user.example.service.processes.endless;

import com.synapsys.springboot.starter.core.AbstractEndlessProcess;
import com.synapsys.springboot.starter.core.SynapsysProcess;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SynapsysProcess(name = "endless process 75")
public class TestEndlessProcess75 extends AbstractEndlessProcess {

    @Override
    protected void runForever(StopSignal stopSignal) throws Exception {
        log.info("{}: endless loop started", this.getName());

        long tick = 0;

        while (!stopSignal.isStopRequested()) {
            tick++;

            log.info("{}: tick {}", this.getName(), tick);

            try {
                Thread.sleep(1000);
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
                break;
            }
        }

        log.info("{}: endless loop exiting", this.getName());
    }
}
