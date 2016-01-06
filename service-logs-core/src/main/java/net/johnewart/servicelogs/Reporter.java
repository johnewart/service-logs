package net.johnewart.servicelogs;

public interface Reporter {
    void report(RequestMetrics requestMetrics);
}
