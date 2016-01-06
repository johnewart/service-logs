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

public class FilterDemo {
    private static final String[] METHODS = { "HelloWorld", "GoodbyeCruelWorld", "ComputeBandwidth", "SortUsers", "DeleteUser" };

    public FilterDemo(Reporter reporter) {
        Server server = new Server(9090);
        FilterHolder filterHolder = new FilterHolder();
        filterHolder.setFilter(new ServiceLogFilter(reporter));
        ServletHandler handler = new ServletHandler();
        server.setHandler(handler);
        handler.addServletWithMapping(HelloServlet.class, "/*");
        handler.addFilterWithMapping(filterHolder, "/*", EnumSet.of(DispatcherType.REQUEST));
        try {
            server.start();
            server.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public static class HelloServlet extends HttpServlet {
        private static final long serialVersionUID = 1L;
        private static AtomicInteger offset = new AtomicInteger(0);

        @Override
        protected void doGet(HttpServletRequest request, HttpServletResponse response)
                throws ServletException, IOException {
            int randomCounter = new Random().nextInt(100);
            String methodName = METHODS[randomCounter % 4];
            RequestMetrics metrics = MetricsThreadLocal.get();
            metrics.setMethodName(methodName);
            Counter c = metrics.counter("RequestsMade");
            c.incr();
            Timer t = metrics.timer("dbQuery");
            t.start();
            try {
                Thread.sleep(new Random().nextInt(1000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            t.stop();

            if(randomCounter > 50) {
                if(randomCounter > 75) {
                    throw new HTTPException(400);
                } else {
                    throw new HTTPException(502);
                }
            }

            response.setContentType("text/html");
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().println("<h1>Hello SimpleServlet</h1>");
            metrics.tag("userid", "50");
            metrics.tag("companyid", "10");


        }
    }
}
