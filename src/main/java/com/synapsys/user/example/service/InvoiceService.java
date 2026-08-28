package com.synapsys.user.example.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicLong;

/** An ordinary Spring service. Injected into processes; unaware any of them exist. */
@Slf4j
@Service
@RequiredArgsConstructor
public class InvoiceService {

    private final WorkSimulator work;
    private final AtomicLong polls = new AtomicLong();

    public int pollInvoices() throws InterruptedException {
        long poll = polls.incrementAndGet();
        work.simulateIo(120);
        int found = work.countFor(poll, 4);
        log.info("poll #{} found {} new invoice(s)", poll, found);
        for (int i = 0; i < found; i++) {
            log.info("  processing invoice INV-{}", 1000 + poll * 10 + i);
        }
        return found;
    }

    public long totalPolls() {
        return polls.get();
    }
}
