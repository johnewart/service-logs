package net.johnewart.servicelogs;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class RequestMetrics {
    private final Map<String, Counter> counters;
    private final Map<String, String> tags;
    private final Map<String, Timer> timers;

    private String methodName;
    private long startTime, endTime;
    private String hostname;

    public RequestMetrics() {
        this.counters = new HashMap<>();
        this.tags = new HashMap<>();
        this.timers = new HashMap<>();
        try {
            this.hostname = InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            this.hostname = "unknown";
        }
    }

    public void reset() {
        this.counters.clear();
    }

    public void start() {
        this.startTime = new Date().getTime();
    }

    public void stop() {
        this.endTime = new Date().getTime();
    }

    public Counter counter(String name) {
        counters.putIfAbsent(name, new Counter(name));
        return counters.get(name);
    }

    public Timer timer(String name) {
        timers.putIfAbsent(name, new Timer());
        return timers.get(name);
    }

    public void tag(String key, String value) {
        this.tags.put(key, value);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Host:").append(hostname).append("\n");
        sb.append("Method:").append(methodName).append("\n");
        sb.append("StartTime:").append(startTime).append("\n");
        sb.append("EndTime:").append(endTime).append("\n");
        sb.append("Time:").append(endTime-startTime).append("\n");

        sb.append("Tags:");
        for(String t : tags.keySet()) {
            sb.append(t).append("=").append(tags.get(t)).append(";");
        }
        sb.append("\n");

        sb.append("Counters:");
        for (Counter c : counters.values()) {
            sb.append(c.name).append("/").append(c.get()).append(";");
        }
        sb.append("\n");

        sb.append("Timers:");
        for(String name : timers.keySet()) {
           sb.append(name).append("/").append(timers.get(name).elapsedTime()).append(";");
        }
        sb.append("\n");

        return sb.toString();
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public long getEndTime() {
        return endTime;
    }

    public long getStartTime() {
        return startTime;
    }

    public String getMethodName() {
        return methodName;
    }

    public Map<String, Timer> getTimers() {
        return timers;
    }

    public Map<String, String> getTags() {
        return tags;
    }

    public Map<String, Counter> getCounters() {
        return counters;
    }

    public String getHostname() {
        return hostname;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }
}
