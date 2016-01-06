package net.johnewart.servicelogs;

import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Point;

import java.util.concurrent.TimeUnit;

public class InfluxDbReporter implements Reporter {

    private final InfluxDB influxDB;
    private final String serviceName;

    public InfluxDbReporter(String serviceName, String url, String username, String password) {
        influxDB = InfluxDBFactory.connect(url, username, password);
        this.serviceName = serviceName;
    }

    @Override
    public void report(RequestMetrics requestMetrics) {
        Point.Builder p = Point.measurement("ServiceRequest")
                        .time(System.currentTimeMillis(), TimeUnit.MILLISECONDS)
                        .tag("Service", serviceName)
                        .tag("Method", requestMetrics.getMethodName())
                        .tag("Host", requestMetrics.getHostname());

        for(String tag : requestMetrics.getTags().values()) {
            p.field(tag, requestMetrics.getTags().get(tag));
        }

        for(String counterName : requestMetrics.getCounters().keySet()) {
            p.field(counterName, requestMetrics.getCounters().get(counterName).get());
        }

        for(String timerName : requestMetrics.getTimers().keySet()) {
            p.field(timerName, requestMetrics.getTimers().get(timerName).elapsedTime());
        }

       p.field("RequestTime", requestMetrics.getEndTime() - requestMetrics.getStartTime());

        influxDB.write("requestmetrics", "default", p.build());
    }
}
