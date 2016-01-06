## Service Logs

If you want to track some metrics on a per-request basis, this is the tool for
you! 

### Why would I want this?

Let's consider that you want to do some ad-hoc analysis of how your service is
performing over time and you want to be able to record arbitrary timers and
counters in your service endpoints. This library lets you have a thread-local
(therefore thread-safe) request-metrics object that you can track timers and
counters inside of and then write that data out to a sink. That sink is
configurable -- you can report to a log file, InfluxDB or other compatible
storage mechanism. 

### Example usage

```java
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
```

This would result in some data like the following:

```
Host:belr9zym16.sea.corp.expecn.com
Method:GoodbyeCruelWorld
StartTime:1452107029939
EndTime:1452107030743
Time:804
Tags:
Counters:RequestsMade/1;
Timers:dbQuery/804;
```

### JAX Filter implementation

There is a built-in JAX filter implementation in the packages, it looks
something like the code below but you can always implement your own. The goal is
to initialize a new `RequestMetrics` object and inject into the thread local
context so every request will have isolated metrics. Then the reporter exports 
the metrics at the end. 

```
MetricsThreadLocal.set(new RequestMetrics());
MetricsThreadLocal.get().start();

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
  MetricsThreadLocal.get().stop();
  System.err.println(MetricsThreadLocal.get().toString());
  reporter.report(MetricsThreadLocal.get());
}
```


