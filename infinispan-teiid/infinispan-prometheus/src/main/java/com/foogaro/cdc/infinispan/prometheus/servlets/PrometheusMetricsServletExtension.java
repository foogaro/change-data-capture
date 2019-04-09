package com.foogaro.cdc.infinispan.prometheus.servlets;

import io.undertow.servlet.ServletExtension;
import io.undertow.servlet.api.DeploymentInfo;
import io.undertow.servlet.api.FilterInfo;

import javax.servlet.DispatcherType;
import javax.servlet.ServletContext;
import java.util.Arrays;
import java.util.List;

public class PrometheusMetricsServletExtension  implements ServletExtension {

    private static List<String> contextBlacklist = Arrays.asList(System.getProperty("prometheus.wildfly.filter.blacklist", "/metrics").split(","));


    @Override
    public void handleDeployment(DeploymentInfo deploymentInfo, ServletContext servletContext) {

        if (!contextBlacklist.contains(deploymentInfo.getContextPath())) {
            System.out.println("Adding metrics filter to  deployment for context " + deploymentInfo.getContextPath());
            FilterInfo metricsFilterInfo = new FilterInfo("metricsfilter", PrometheusMetricsFilter.class);
            metricsFilterInfo.setAsyncSupported(true);
            metricsFilterInfo.addInitParam(PrometheusMetricsFilter.BUCKET_CONFIG_PARAM,System.getProperty("prometheus.wildfly.filter.buckets",""));
            deploymentInfo.addFilter(metricsFilterInfo);
            deploymentInfo.addFilterUrlMapping("metricsfilter", "/*", DispatcherType.REQUEST);
        } else {
            System.out.println("Metrics filter not added to black listed context " + deploymentInfo.getContextPath());
            System.out.println(contextBlacklist.toString());
        }
    }
}