import net.johnewart.servicelogs.InfluxDbReporter;
import net.johnewart.servicelogs.Reporter;

public class FilterDemoInfluxReporter {
    public static void main(final String[] args) throws Exception {
        Reporter r = new InfluxDbReporter("FilterDemoInfluxReporter", "http://127.0.0.1:8086", "root", "root");
        new FilterDemo(r);
    }
}
