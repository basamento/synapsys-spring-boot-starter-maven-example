package com.synapsys.user.example.process.endless;

import com.synapsys.springboot.starter.core.AbstractEndlessProcess;
import com.synapsys.springboot.starter.core.SynapsysProcess;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Holds a resource for as long as it runs, and releases it in onStop.
 *
 * onStop runs on the heartbeat thread as part of the stop, whether the body was
 * working or sleeping, and a failure inside it is logged without aborting the stop.
 */
@Slf4j
@SynapsysProcess(name = "cache-warmer")
public class CacheWarmerProcess extends AbstractEndlessProcess {

    private final Map<String, Long> cache = new ConcurrentHashMap<>();

    @Override
    protected void runForever(StopSignal stopSignal) throws Exception {
        log.info("cache opened");
        long warmed = 0;
        while (!stopSignal.isStopRequested()) {
            warmed++;
            cache.put("product:" + (warmed % 50), System.currentTimeMillis());
            log.info("warmed product:{} ({} entries held)", warmed % 50, cache.size());
            Thread.sleep(2_000);
        }
    }

    @Override
    protected void onStop() {
        int held = cache.size();
        cache.clear();
        log.info("cache closed, released {} entries", held);
    }
}
