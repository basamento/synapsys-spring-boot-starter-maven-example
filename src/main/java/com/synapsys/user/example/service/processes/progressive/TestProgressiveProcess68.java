package com.synapsys.user.example.service.processes.progressive;

import com.synapsys.springboot.starter.core.AbstractProgressiveProcess;
import com.synapsys.springboot.starter.core.SynapsysProcess;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SynapsysProcess(name = "progressive process 68")
public class TestProgressiveProcess68 extends AbstractProgressiveProcess {

    @Override
    protected void run() {
        int steps = 100;
        long workPerStep = 10_000_000L;

        log.info("{}: start entered", this.getName());

        for (int step = 1; step <= steps; step++) {
            checkCancelled();

            double sink = 0;
            for (long i = 0; i < workPerStep; i++) {
                if ((i & 0x3FFF) == 0) checkCancelled();
                sink += Math.sqrt(i) * Math.sin(i);
            }
        }

        log.info("{}: computation finished", this.getName());
    }
}
