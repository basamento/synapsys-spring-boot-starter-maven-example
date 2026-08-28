package com.synapsys.user.example.process.progressive;

import com.synapsys.springboot.starter.core.AbstractProgressiveProcess;
import com.synapsys.springboot.starter.core.SynapsysProcess;
import com.synapsys.user.example.service.ReportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * The canonical progressive process: runs to completion and returns itself to idle
 * by finishing. No stop command is involved.
 */
@Slf4j
@RequiredArgsConstructor
@SynapsysProcess(name = "monthly-report")
public class MonthlyReportProcess extends AbstractProgressiveProcess {

    private final ReportService reports;

    @Override
    protected void run() throws Exception {
        log.info("building the monthly report");
        for (String section : ReportService.SECTIONS) {
            checkCancelled();
            reports.buildSection(section);
        }
        log.info("report complete - returning to idle on its own");
    }
}
