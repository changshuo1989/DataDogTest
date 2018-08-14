package com.test;

import com.timgroup.statsd.ServiceCheck;
import com.timgroup.statsd.StatsDClient;
import com.timgroup.statsd.NonBlockingStatsDClient;

public class Foo {
	  private static final StatsDClient statsd = new NonBlockingStatsDClient(
			    "my.prefix",                          /* prefix to any stats; may be null or empty string */
			    "statsd-host",                        /* common case: localhost */
			    8125,                                 /* port */
			    new String[] {"tag:value"}            /* Datadog extension: Constant tags, always applied */
			  );

			  public static final void main(String[] args) {
			    statsd.incrementCounter("foo");
			    statsd.recordGaugeValue("bar", 100);
			    statsd.recordGaugeValue("baz", 0.01); /* DataDog extension: support for floating-point gauges */
			    statsd.recordHistogramValue("qux", 15);     /* DataDog extension: histograms */
			    statsd.recordHistogramValue("qux", 15.5);   /* ...also floating-point */
			    statsd.recordDistributionValue("qux", 15);     /* DataDog extension: global distributions */
			    statsd.recordDistributionValue("qux", 15.5);   /* ...also floating-point */

			    ServiceCheck sc = ServiceCheck
			          .builder()
			          .withName("my.check.name")
			          .withStatus(ServiceCheck.Status.OK)
			          .build();
			    statsd.serviceCheck(sc); /* Datadog extension: send service check status */

			    /* Compatibility note: Unlike upstream statsd, DataDog expects execution times to be a
			     * floating-point value in seconds, not a millisecond value. This library
			     * does the conversion from ms to fractional seconds.
			     */
			    statsd.recordExecutionTime("bag", 25, "cluster:foo"); /* DataDog extension: cluster tag */
			  }
}
