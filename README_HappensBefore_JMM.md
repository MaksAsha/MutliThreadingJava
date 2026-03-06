# Happens-Before in Java Memory Model (JMM)

## What is Happens-Before?

**Happens-Before** is a formal relationship between actions in a multithreaded program that guarantees the **visibility of results** from one action to another.

If action **A** *happens-before* action **B**, then:
- All changes made in **A** are **guaranteed to be visible** to the thread executing **B**
- The ordering of **A** before **B** is **guaranteed**

> ⚠️ Happens-Before is **not** about real-time execution order — it's about **visibility guarantees**.

---

## Core Happens-Before Rules

### 1. Program Order Rule

Within a single thread, every action happens-before any subsequent action in that thread.

```java
int x = 5;   // action A
int y = x;   // action B — guaranteed to see x = 5
```

---

### 2. Monitor Lock Rule

An `unlock` on a monitor happens-before every subsequent `lock` on that same monitor.

```java
synchronized (obj) {
    x = 42;
} // unlock happens here

// ... some time later ...

synchronized (obj) {
    System.out.println(x); // lock happens here — guaranteed to see x = 42
}
```

---

### 3. Volatile Variable Rule

A write to a `volatile` field happens-before every subsequent read of that same field.

```java
volatile boolean ready = false;
int data = 0;

// Thread A:
data = 42;
ready = true;   // volatile write

// Thread B:
if (ready) {        // volatile read
    System.out.println(data); // guaranteed to see 42
}
```

> The volatile write "flushes" all preceding writes. The volatile read "refreshes" all subsequent reads.  
> This is why `data = 42` is also visible — due to **transitivity**.

---

### 4. Thread Start Rule

A call to `Thread.start()` happens-before any action in the started thread.

```java
x = 100;
thread.start(); // everything written before start() is visible inside the new thread

// Inside the thread:
System.out.println(x); // guaranteed to see 100
```

---

### 5. Thread Join Rule

All actions in a thread happen-before the return from `thread.join()`.

```java
thread.join(); // after this returns, all results of the thread are visible
System.out.println(result); // safe to read
```

---

### 6. Thread Interruption Rule

A call to `thread.interrupt()` happens-before the interrupted thread detects the interruption (via `InterruptedException`, `isInterrupted()`, or `interrupted()`).

---

### 7. Finalizer Rule

The completion of a constructor for an object happens-before the start of the finalizer for that object.

---

### 8. Transitivity

If **A** happens-before **B**, and **B** happens-before **C**, then **A** happens-before **C**.

```
write data=42  →HB→  volatile write ready=true  →HB→  volatile read ready  →HB→  read data
```

This chain guarantees that `data = 42` is visible after the volatile read of `ready`.

---

## The Problem Without Happens-Before

```java
// ❌ NO synchronization — data race!
boolean flag = false;
int value = 0;

// Thread A:
value = 42;
flag = true;

// Thread B:
if (flag) {
    System.out.println(value); // may print 0!
}
```

The compiler and CPU are allowed to **reorder instructions** for optimization. Thread B might see `flag = true` but `value = 0` because there is no HB relationship between the write and read of `value`.

### Fixed with `volatile`:

```java
// ✅ volatile establishes HB
volatile boolean flag = false;
int value = 0;

// Thread A:
value = 42;
flag = true;   // volatile write

// Thread B:
if (flag) {     // volatile read — HB with volatile write
    System.out.println(value); // guaranteed to see 42
}
```

---

## Summary Table

| Rule | HB Relationship |
|---|---|
| **Program Order** | Each action → next action in the same thread |
| **Monitor Unlock** | `unlock(m)` → subsequent `lock(m)` |
| **Volatile Write** | `write(v)` → subsequent `read(v)` |
| **Thread Start** | `thread.start()` → first action in thread |
| **Thread Join** | last action in thread → `thread.join()` return |
| **Interruption** | `thread.interrupt()` → detection of interruption |
| **Finalizer** | end of constructor → start of finalizer |
| **Transitivity** | A →HB→ B, B →HB→ C ⟹ A →HB→ C |

---

## Key Takeaways

- **No HB** between a write and a read = **data race** = undefined behavior
- `volatile`, `synchronized`, and `java.util.concurrent` classes are all built on HB guarantees
- HB is **transitive** — you can build chains of visibility guarantees
- The JMM does **not** guarantee sequentially consistent behavior unless HB is properly established
- HB is about **visibility**, not just about ordering

---

## Further Reading

- [JSR-133: Java Memory Model Specification](https://jcp.org/en/jsr/detail?id=133)
- [Java Language Specification, Chapter 17](https://docs.oracle.com/javase/specs/jls/se17/html/jls-17.html)
- *Java Concurrency in Practice* — Brian Goetz et al.
