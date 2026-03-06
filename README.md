# Multithreading in Java

---

## Table of Contents

1. [The Thread Class](#1-the-thread-class)
2. [Creating and Running Threads](#2-creating-and-running-threads)
3. [Thread Management](#3-thread-management)
4. [Stopping and Interrupting a Thread](#4-stopping-and-interrupting-a-thread)
5. [Virtual Threads](#5-virtual-threads)
6. [Synchronization. The synchronized Keyword](#6-synchronization-the-synchronized-keyword)
7. [Thread Interaction. wait and notify](#7-thread-interaction-wait-and-notify)
8. [Semaphores](#8-semaphores)
9. [Thread Data Exchange. Exchanger](#9-thread-data-exchange-exchanger)
10. [The Phaser Class](#10-the-phaser-class)
11. [Locks. ReentrantLock](#11-locks-reentrantlock)
12. [Conditions in Locks](#12-conditions-in-locks)
13. [volatile Variables](#13-volatile-variables)
14. [Atomicity and Atomics](#14-atomicity-and-atomics)

---

## 1. The Thread Class

Multithreading allows multiple tasks to run in parallel without blocking the main thread (e.g., the UI). In Java, the `Thread` class is responsible for all thread operations.

Since **Java 21**, there are two types of threads:
- **Platform thread** — an OS-level thread, scheduled by the operating system.
- **Virtual thread** — a lightweight thread, scheduled by the JVM on top of platform threads.

When a program starts, the **main thread** (`main`) is launched, and all other threads are spawned from it.

**Thread states:**

| State | Description |
|---|---|
| `NEW` | Created but not yet started |
| `RUNNABLE` | Currently executing |
| `BLOCKED` | Waiting to acquire a monitor lock |
| `WAITING` | Waiting indefinitely for another thread |
| `TIMED_WAITING` | Waiting for a specified amount of time |
| `TERMINATED` | Finished execution |

**Priorities:** range from `MIN_PRIORITY` (1) to `MAX_PRIORITY` (10), default is `NORM_PRIORITY` (5). In practice, modern JVMs often ignore thread priorities.

```java
Thread t = Thread.currentThread();
System.out.println(t.getName());     // main
System.out.println(t.getPriority()); // 5
System.out.println(t.isAlive());     // true
```

---

## 2. Creating and Running Threads

There are two main ways to create a thread:

**Option 1 — extend `Thread`:**
```java
class MyThread extends Thread {
    @Override
    public void run() {
        System.out.println("Thread: " + getName());
    }
}
new MyThread().start();
```

**Option 2 — implement `Runnable`** (preferred):
```java
Thread t = new Thread(() -> System.out.println("Thread started"));
t.start();
```

> `run()` defines the thread's task; `start()` launches the thread. Calling `run()` directly does **not** create a new thread.

---

## 3. Thread Management

**Key methods:**

- `Thread.sleep(millis)` — pause the thread for a given number of milliseconds.
- `join()` — make the current thread wait until another thread finishes.
- `setDaemon(true)` — mark the thread as a daemon; the JVM exits when only daemon threads remain.

```java
Thread t = new Thread(() -> {
    try { Thread.sleep(2000); } catch (InterruptedException e) {}
    System.out.println("Done");
});
t.start();
t.join(); // main thread waits for t to finish
```

**Daemon threads** run in the background (e.g., the garbage collector). They should not be used for tasks that require guaranteed completion.

---

## 4. Stopping and Interrupting a Thread

A thread ends naturally when its `run()` method returns. There is no way to forcibly stop a thread — instead, Java uses an **interruption** mechanism.

```java
thread.interrupt(); // set the interruption flag
```

Inside the thread, interruption must be handled explicitly:
```java
while (!Thread.currentThread().isInterrupted()) {
    // do work
}
// or via exception:
try {
    Thread.sleep(1000);
} catch (InterruptedException e) {
    Thread.currentThread().interrupt(); // restore the flag
}
```

> `isInterrupted()` — checks the flag without clearing it. `interrupted()` — checks and clears the flag.

---

## 5. Virtual Threads

Introduced in **Java 21** (Project Loom). Virtual threads are extremely lightweight — you can create millions of them without impacting performance.

```java
// Create a virtual thread
Thread vt = Thread.ofVirtual().start(() -> System.out.println("Virtual!"));

// Using a factory method
Thread.startVirtualThread(() -> System.out.println("Virtual 2"));
```

**Differences from platform threads:**
- Managed by the JVM, not the OS.
- During a blocking operation (I/O, `sleep`), the platform thread is released — other virtual threads continue running.
- Priority is always `NORM_PRIORITY` and cannot be changed.
- Best suited for tasks with heavy I/O workloads.

---

## 6. Synchronization. The `synchronized` Keyword

When multiple threads access a shared resource, a **race condition** can occur. The `synchronized` keyword protects against this.

```java
class Counter {
    private int count = 0;

    public synchronized void increment() {
        count++;
    }
}
```

`synchronized` can be applied to a method or a block of code:
```java
synchronized (this) {
    count++;
}
```

Every Java object has a **monitor**. A thread entering a synchronized block acquires the monitor — other threads wait until it is released.

> Synchronization solves race conditions but can lead to a **deadlock** if two threads are each waiting for a lock held by the other.

---

## 7. Thread Interaction. `wait` and `notify`

The methods `wait()`, `notify()`, and `notifyAll()` are defined in `Object` and allow threads to coordinate with each other.

```java
synchronized (obj) {
    while (!condition) {
        obj.wait();    // releases the monitor and waits
    }
    // do work
    obj.notify();  // wakes up one waiting thread
}
```

- `wait()` — the thread releases the monitor and enters `WAITING`.
- `notify()` — wakes up one thread waiting on this object.
- `notifyAll()` — wakes up all threads waiting on this object.

> All three methods must be called from within a synchronized block, otherwise an `IllegalMonitorStateException` is thrown.

---

## 8. Semaphores

`Semaphore` from `java.util.concurrent` limits the number of threads that can access a resource simultaneously.

```java
Semaphore semaphore = new Semaphore(3); // at most 3 threads at a time

semaphore.acquire(); // acquire a permit (or wait)
try {
    // access the resource
} finally {
    semaphore.release(); // release the permit
}
```

`Semaphore(1)` is equivalent to a mutex. A semaphore with `fair = true` guarantees FIFO ordering when granting permits.

---

## 9. Thread Data Exchange. `Exchanger`

`Exchanger<V>` allows exactly two threads to swap data at a synchronization point.

```java
Exchanger<String> exchanger = new Exchanger<>();

// Thread 1
String result = exchanger.exchange("data from thread 1");

// Thread 2
String result = exchanger.exchange("data from thread 2");
```

Both threads block at `exchange()` until both have arrived — then they swap values and continue. Useful for pipeline algorithms such as producer-consumer patterns.

---

## 10. The `Phaser` Class

`Phaser` is a flexible barrier for synchronizing a group of threads across multiple phases. It is similar to `CountDownLatch` and `CyclicBarrier`, but supports dynamic registration and deregistration of participants.

```java
Phaser phaser = new Phaser(3); // 3 participants

// In each thread:
phaser.arriveAndAwaitAdvance(); // wait for all participants to complete the phase
```

Key methods:
- `arrive()` — signal phase completion without waiting.
- `arriveAndAwaitAdvance()` — complete the phase and wait for others.
- `arriveAndDeregister()` — complete the phase and leave the phaser.
- `getPhase()` — returns the current phase number.

---

## 11. Locks. `ReentrantLock`

`ReentrantLock` from `java.util.concurrent.locks` is an explicit alternative to `synchronized` with extended capabilities.

```java
ReentrantLock lock = new ReentrantLock();

lock.lock();
try {
    // critical section
} finally {
    lock.unlock(); // always in finally
}
```

**Advantages over `synchronized`:**
- `tryLock()` — attempt to acquire the lock without blocking.
- `tryLock(time, unit)` — attempt with a timeout.
- `lockInterruptibly()` — allows a waiting thread to be interrupted.
- `isLocked()`, `getQueueLength()` — inspect lock state.
- Supports `Condition` objects (see next section).

The **Reentrant** prefix means a thread can re-acquire a lock it already holds.

---

## 12. Conditions in Locks

`Condition` enables structured waiting within a `ReentrantLock`, replacing `wait`/`notify`.

```java
ReentrantLock lock = new ReentrantLock();
Condition condition = lock.newCondition();

// Waiting
lock.lock();
try {
    condition.await();       // like wait()
} finally { lock.unlock(); }

// Signaling
lock.lock();
try {
    condition.signal();      // like notify()
    condition.signalAll();   // like notifyAll()
} finally { lock.unlock(); }
```

A single lock can have **multiple conditions**, enabling fine-grained control over different groups of waiting threads — for example, separate conditions for waiting readers and waiting writers.

---

## 13. `volatile` Variables

The `volatile` modifier guarantees that changes to a variable are immediately visible to all threads — preventing caching in CPU registers or thread-local memory.

```java
private volatile boolean running = true;

// Thread 1
running = false;

// Thread 2
while (running) { /* do work */ }
```

**`volatile` guarantees:**
- Visibility — a write by one thread is immediately seen by others.
- Prevention of instruction reordering around the variable.

**`volatile` does NOT guarantee:**
- Atomicity of compound operations (e.g., `count++` is three separate operations).

> Use `volatile` for flags and single-write references. For counters, use `Atomic` classes.

---

## 14. Atomicity and Atomics

The `java.util.concurrent.atomic` package provides classes for lock-free atomic operations based on CAS (Compare-And-Swap).

**Main classes:**

| Class | Purpose |
|---|---|
| `AtomicInteger` | Atomic `int` |
| `AtomicLong` | Atomic `long` |
| `AtomicBoolean` | Atomic `boolean` |
| `AtomicReference<V>` | Atomic object reference |

```java
AtomicInteger counter = new AtomicInteger(0);

counter.incrementAndGet();        // atomic ++counter
counter.getAndAdd(5);             // atomic counter += 5
counter.compareAndSet(5, 10);     // if == 5, then set to 10
```

**Advantages over `synchronized`:** better performance under low contention, no risk of deadlock. Under high contention, `LongAdder` / `LongAccumulator` may outperform `AtomicLong`.

---

## Quick Reference Cheat Sheet

| Goal | Tool |
|---|---|
| Create a thread | `Thread`, `Runnable`, lambda |
| Lightweight I/O threads | Virtual threads (Java 21+) |
| Protect a shared resource | `synchronized` or `ReentrantLock` |
| Coordinate threads | `wait`/`notify` or `Condition` |
| Limit concurrent access | `Semaphore` |
| Exchange data between two threads | `Exchanger` |
| Phase-based barrier | `Phaser` |
| Visibility flag | `volatile` |
| Lock-free counter | `AtomicInteger` / `AtomicLong` |

---
