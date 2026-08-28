package com.synapsys.user.example.process.progressive;

import com.synapsys.springboot.starter.core.AbstractProgressiveProcess;
import com.synapsys.springboot.starter.core.SynapsysProcess;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * Fails with an unchecked exception, rather than the deliberate IllegalStateException
 * that flaky-importer throws.
 *
 * The distinction is worth seeing: a NullPointerException from ordinary code is
 * caught at exactly the same boundary, ends the run as 'failed', and reaches the
 * console in Core with its stack. Nothing about the failure path depends on the
 * exception having been anticipated.
 */
@Slf4j
@SynapsysProcess(name = "payroll-exporter")
public class PayrollExporterProcess extends AbstractProgressiveProcess {

    private static final Map<String, String> CONFIG = Map.of("format", "csv");

    @Override
    protected void run() throws Exception {
        log.info("exporting payroll (this process fails deliberately, with an NPE)");

        String format = CONFIG.get("format");
        log.info("  format resolved: {}", format);

        // The key is absent, so this returns null and the call below throws.
        String destination = CONFIG.get("destination");
        log.info("  destination resolved: {} - about to dereference it", destination);

        log.info("  writing to {}", destination.toUpperCase());
    }
}
