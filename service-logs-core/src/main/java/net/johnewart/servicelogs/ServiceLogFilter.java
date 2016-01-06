package net.johnewart.servicelogs;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.xml.ws.http.HTTPException;
import java.io.IOException;

public class ServiceLogFilter implements Filter {
    private final Reporter reporter;

    public ServiceLogFilter(Reporter reporter) {
        this.reporter = reporter;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        MetricsThreadLocal.set(new RequestMetrics());
        MetricsThreadLocal.get().start();
        System.err.println("Start Filter");
        try {
            chain.doFilter(new HttpServletRequestWrapper((HttpServletRequest) request), response);
            MetricsThreadLocal.get().counter("Success").incr();
        } catch (HTTPException e ){
            if(e.getStatusCode() >= 400 && e.getStatusCode() < 500) {
                MetricsThreadLocal.get().counter("Error").incr();
            } else {
                MetricsThreadLocal.get().counter("Fault").incr();
            }
            throw e;
        } finally {
            System.err.println("End Filter");
            MetricsThreadLocal.get().stop();
            System.err.println("Metrics:\n" + MetricsThreadLocal.get().toString());
            reporter.report(MetricsThreadLocal.get());
        }
    }

    @Override
    public void destroy() {

    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

}
