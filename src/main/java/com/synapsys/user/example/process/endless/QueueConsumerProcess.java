package com.synapsys.user.example.process.endless;

import com.synapsys.springboot.starter.core.AbstractEndlessProcess;
import com.synapsys.springboot.starter.core.SynapsysProcess;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * Waits on a queue rather than sleeping.
 *
 * poll() with a timeout is interruptible, so a stop lands immediately even though
 * the process spends nearly all its time blocked with nothing to do.
 */
@Slf4j
@SynapsysProcess(name = "queue-consumer")
public class QueueConsumerProcess extends AbstractEndlessProcess {

    private final BlockingQueue<String> queue = new ArrayBlockingQueue<>(64);

    @Override
    protected void runForever(StopSignal stopSignal) throws Exception {
        long produced = 0;
        while (!stopSignal.isStopRequested()) {
            if (queue.isEmpty()) {
                queue.offer("job-" + (++produced));
            }
            String job = queue.poll(2, TimeUnit.SECONDS);
            if (job == null) {
                log.info("queue empty, still waiting");
                continue;
            }
            log.info("consumed {}", job);
            Thread.sleep(1_500);
        }
    }
}
