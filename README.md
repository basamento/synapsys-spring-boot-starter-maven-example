# Synapsys Spring Boot starter — Maven example

A Spring Boot application registering 24 Synapsys processes, twelve of each type.

Every process has a name and a job, and each one demonstrates something specific:
the canonical shape, cleanup, blocking I/O interrupted by a stop, CPU-bound work,
and several ways of failing. Nothing runs until you start it from the Core console.

The Gradle example is the same application with a different build tool; the `src`
trees are identical.

## Requirements

- JDK 21 (Lombok's annotation processing is version-sensitive — pin it)
- A reachable Synapsys Core instance and a worker token

## Run

```bash
export SYNAPSYS_CORE_URL=https://core.example.com
```

```bash
export SYNAPSYS_CORE_TOKEN=syn_your_token_here
```

```bash
./mvnw spring-boot:run
```

Spring Boot's relaxed binding maps those environment variables onto
`synapsys.core-url` and `synapsys.core-token`, so no configuration file is needed.

## The processes

Each is one class, and several share the same services under
`com.synapsys.user.example.service` — a process is a thin declaration around
business logic that already exists.

### Endless — run until stopped

| Process | Demonstrates |
| --- | --- |
| `invoice-poller` | The canonical shape: poll, sleep, repeat, exit on the stop signal. |
| `payment-reconciler` | Constructor injection of an ordinary `@Service`. |
| `feed-subscriber` | Blocked in I/O when the stop arrives. The starter interrupts the thread, so it returns immediately rather than after ten seconds. |
| `cache-warmer` | Holds a resource and releases it in `onStop`. |
| `session-reaper` | Sleeps far longer than the heartbeat interval, and still stops instantly. |
| `metrics-publisher` | One line per second — exercises console capture and the per-beat log budget. |
| `dead-letter-retrier` | Handles its own exceptions and keeps running. Contrast with `crashing-watchdog`. |
| `legacy-nightly-sync` | Java written in 2009, adopted unchanged. |
| `queue-consumer` | Blocks on a `BlockingQueue` rather than sleeping. |
| `temperature-monitor` | Trivial and always well behaved — a control. |
| `slow-stopper` | Takes ~15s to reach a safe stopping point, so `stopping` is visible in the console. |
| `crashing-watchdog` | **Fails deliberately.** Ends `failed`, not `idle`. Every third run survives. |

### Progressive — run to completion

| Process | Demonstrates |
| --- | --- |
| `monthly-report` | Runs to completion and returns itself to `idle`. No stop involved. |
| `bulk-import` | `checkCancelled()` between items. Stop it mid-run: `idle`, not `failed`. |
| `flaky-importer` | **Fails deliberately**, with a thrown `IllegalStateException`. Every third attempt succeeds. |
| `checksum-cruncher` | 30 rounds of real CPU work on this process's own thread. |
| `data-migration` | 20 steps of ~3s — long enough to always catch mid-run. |
| `pdf-renderer` | A mix of blocking I/O and CPU, which most batch work actually is. |
| `search-index-rebuild` | CPU-bound, sharing the same helper as `checksum-cruncher`. |
| `email-blast` | 60 items, cancellable on every one. |
| `archive-compactor` | `onStop` on a progressive process — runs only when actually stopped, not when it finishes. |
| `tax-calculator` | Two injected services, one of them shared with `invoice-poller`. |
| `payroll-exporter` | **Fails deliberately**, with an unanticipated `NullPointerException`. |
| `uncancellable-batch` | Never calls `checkCancelled()`. Sits in `stopping` until it finishes on its own. |

Three of them fail on purpose. That is the example working, not a fault — each says
so in its own log output before throwing.

## Things worth trying

**Watch a command take effect.** Start `invoice-poller`. Within one heartbeat the
console shows `running`, and the lines it prints appear against that run.

**Stop something blocked in I/O.** Start `feed-subscriber`, wait for the line saying
it is waiting, then stop it. It returns immediately instead of after the full ten
seconds, because the starter interrupts the thread.

**Watch `stopping` actually mean something.** Start `slow-stopper` and stop it. It
reports `stopping` for up to fifteen seconds before reaching `idle` — an honest
report of work that has to finish, not a hung worker.

**Compare the two failure shapes.** `flaky-importer` throws a checked, deliberate
exception; `payroll-exporter` dereferences a null. Both land in `failed` at the same
boundary and both reach the console with a stack. Nothing about the failure path
depends on the exception having been anticipated.

**Compare handling with failing.** `dead-letter-retrier` and `crashing-watchdog` hit
the same kind of error. The first catches it and stays `running`; the second lets it
escape and ends `failed`.

**Run several at once.** Start `checksum-cruncher`, `search-index-rebuild` and
`uncancellable-batch` together. Each progressive process gets its own single-thread
executor, so none of them delays another and the heartbeat is unaffected throughout.

## A note on CPU-bound work

`checksum-cruncher` burns real CPU on the calling thread, and that is fine here:
every progressive process owns a dedicated executor, so the cost is confined to
itself.

This is the sharpest difference from the Node SDK. The same loop written there would
block the event loop, stall the heartbeat, and freeze every other process on the
worker — which is why that library pushes CPU-bound work onto a `worker_thread`. The
protocol is identical; what a runtime can get away with is not.

`uncancellable-batch` shows the limit that does carry across. `Thread.interrupt()`
sets a flag and unblocks waiting; it cannot force a return from a loop that never
looks at it. Cancellation is cooperative in every Synapsys client.

## Configuration

| Property | Default | Description |
| --- | --- | --- |
| `synapsys.core-url` | — | **Required.** Base URL of Core. |
| `synapsys.core-token` | — | Bearer token. Core rejects heartbeats without it. |
| `synapsys.worker-name` | `spring.application.name` | Identity across restarts. |
| `synapsys.enabled` | `true` | `false` disables heartbeats and console capture entirely. |
| `synapsys.heartbeat-interval` | `5s` | Whole seconds. |
| `synapsys.fail-fast` | `false` | Fail startup when Core is unreachable. |

Unknown `synapsys.*` keys fail startup rather than being ignored, so a typo surfaces
immediately.
