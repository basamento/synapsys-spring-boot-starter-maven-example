package com.synapsys.user.example.process.progressive;

import com.synapsys.springboot.starter.core.AbstractProgressiveProcess;
import com.synapsys.springboot.starter.core.SynapsysProcess;
import com.synapsys.user.example.service.WorkSimulator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/** A mix of blocking I/O and CPU, which is what most real batch work actually is. */
@Slf4j
@RequiredArgsConstructor
@SynapsysProcess(name = "pdf-renderer")
public class PdfRendererProcess extends AbstractProgressiveProcess {

    private static final int PAGES = 12;

    private final WorkSimulator work;

    @Override
    protected void run() throws Exception {
        log.info("rendering {} pages", PAGES);
        for (int page = 1; page <= PAGES; page++) {
            checkCancelled();
            work.simulateIo(300);        // fetch the page source
            work.burnCpu(8_000_000L);    // lay it out
            log.info("  page {}/{} rendered", page, PAGES);
        }
        log.info("document complete, {} pages", PAGES);
    }
}
