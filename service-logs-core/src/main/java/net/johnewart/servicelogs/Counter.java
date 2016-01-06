package net.johnewart.servicelogs;

import java.util.concurrent.atomic.AtomicLong;

public class Counter {
    private final AtomicLong value;
    public final String name;

    public Counter(String name) {
        this.name = name;
        this.value = new AtomicLong(0);
    }

    public void incr() {
        this.value.incrementAndGet();
    }

    public long get() {
        return this.value.get();
    }

    public void set(long count) {
        this.value.addAndGet(count);
    }
}
