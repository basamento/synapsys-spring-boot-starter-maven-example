package com.synapsys.user.example.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/** Shared by the reporting processes, so the same logic backs more than one of them. */
@Slf4j
@Service
@RequiredArgsConstructor
public class ReportService {

    public static final List<String> SECTIONS = List.of(
            "revenue", "refunds", "taxes", "payroll", "depreciation", "summary"
    );

    private final WorkSimulator work;

    public void buildSection(String section) throws InterruptedException {
        work.simulateIo(700);
        log.info("  section built: {}", section);
    }
}
