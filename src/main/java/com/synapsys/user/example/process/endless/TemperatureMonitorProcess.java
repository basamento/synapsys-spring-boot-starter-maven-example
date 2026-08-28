package com.synapsys.user.example.process.endless;

import com.synapsys.springboot.starter.core.AbstractEndlessProcess;
import com.synapsys.springboot.starter.core.SynapsysProcess;
import lombok.extern.slf4j.Slf4j;

/** Trivial and always well behaved. Useful as a control when something else misbehaves. */
@Slf4j
@SynapsysProcess(name = "temperature-monitor")
public class TemperatureMonitorProcess extends AbstractEndlessProcess {

    @Override
    protected void runForever(StopSignal stopSignal) throws Exception {
        long reading = 0;
        while (!stopSignal.isStopRequested()) {
            reading++;
            double celsius = 19.5 + Math.sin(reading / 6.0) * 4.5;
            log.info("reading {}: {}C", reading, String.format("%.1f", celsius));
            Thread.sleep(3_000);
        }
    }
}
