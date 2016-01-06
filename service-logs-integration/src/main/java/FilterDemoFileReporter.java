import net.johnewart.servicelogs.*;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletHandler;

import javax.servlet.DispatcherType;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.ws.http.HTTPException;
import java.io.IOException;
import java.util.EnumSet;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Filter;

public class FilterDemoFileReporter {
    public static void main( final String[] args ) throws Exception {
        Reporter r = new LogFileReporter();
        FilterDemo demo = new FilterDemo(r);
    }
}
