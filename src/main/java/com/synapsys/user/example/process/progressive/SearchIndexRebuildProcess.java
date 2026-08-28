package com.synapsys.user.example.process.progressive;

import com.synapsys.springboot.starter.core.AbstractProgressiveProcess;
import com.synapsys.springboot.starter.core.SynapsysProcess;
import com.synapsys.user.example.service.WorkSimulator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/** CPU-bound, sharing the same burn helper as the checksum process. */
@Slf4j
@RequiredArgsConstructor
@SynapsysProcess(name = "search-index-rebuild")
public class SearchIndexRebuildProcess extends AbstractProgressiveProcess {

    private static final int SHARDS = 8;

    private final WorkSimulator work;

    @Override
    protected void run() throws Exception {
        log.info("rebuilding {} shards", SHARDS);
        for (int shard = 1; shard <= SHARDS; shard++) {
            checkCancelled();
            work.burnCpu(30_000_000L);
            log.info("  shard {}/{} rebuilt", shard, SHARDS);
        }
        log.info("index rebuilt");
    }
}
