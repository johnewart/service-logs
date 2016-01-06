package net.johnewart.servicelogs;

import java.util.Date;

public class Timer {
    private long startTime, endTime;

    public void start() {
        this.startTime = new Date().getTime();
    }

    public void stop() {
        this.endTime = new Date().getTime();
    }

    public long elapsedTime() {
        return endTime - startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }
}
