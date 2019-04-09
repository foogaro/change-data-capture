package com.foogaro.cdc.infinispan.prometheus.servlets;

import com.foogaro.cdc.infinispan.prometheus.collectors.InfinispanCollector;
import io.prometheus.client.exporter.MetricsServlet;
import io.prometheus.client.hotspot.DefaultExports;

import javax.servlet.ServletConfig;
import javax.servlet.annotation.WebServlet;

@WebServlet("/")
public class PrometheusMetricsServlet extends MetricsServlet {

    @Override
    public void init(ServletConfig config) {
        DefaultExports.initialize();
        new InfinispanCollector().register();
    }

}
