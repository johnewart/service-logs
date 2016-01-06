package net.johnewart.servicelogs;

public class MetricsThreadLocal {

    public static final ThreadLocal<RequestMetrics> METRICS_THREAD_LOCAL = new ThreadLocal<>();

    public static void set(RequestMetrics user) {
        METRICS_THREAD_LOCAL.set(user);
    }

    public static void unset() {
        METRICS_THREAD_LOCAL.remove();
    }

    public static RequestMetrics get() {
        return METRICS_THREAD_LOCAL.get();
    }
}
