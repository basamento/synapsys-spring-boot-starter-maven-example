package com.synapsys.user.example.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Stand-ins for the work a real application would do, shared by several processes.
 *
 * Keeping them here rather than in each process is the point of the example: a
 * process is a thin declaration around business logic that already exists, and the
 * logic itself knows nothing about Synapsys.
 */
@Slf4j
@Service
public class WorkSimulator {

    /**
     * Blocking I/O, the kind a JDBC driver or an HTTP client would do.
     *
     * The InterruptedException is not swallowed: the starter interrupts the worker
     * thread on a stop, and a process that eats the interrupt silently is a process
     * that cannot be stopped promptly.
     */
    public void simulateIo(long millis) throws InterruptedException {
        Thread.sleep(millis);
    }

    /**
     * Genuine CPU work, on the calling thread.
     *
     * Each progressive process gets its own single-thread executor, so burning CPU
     * here delays nothing but this process. That is a real difference from the Node
     * SDK, where the same loop would block the event loop and stall the heartbeat.
     */
    public double burnCpu(long iterations) {
        double sink = 0;
        for (long i = 0; i < iterations; i++) {
            sink += Math.sqrt(i) * Math.sin(i);
        }
        return sink;
    }

    /** Deterministic pseudo-random count, so runs read consistently in the console. */
    public int countFor(long tick, int bound) {
        return (int) Math.floorMod(tick * 2654435761L, bound);
    }
}
